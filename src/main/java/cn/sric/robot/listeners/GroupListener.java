package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.GroupMessage;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.group.AdminService;
import cn.sric.util.Cat;
import cn.sric.util.ConstUtil;
import cn.sric.util.RedisUtil;
import cn.sric.util.TApiUtil;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnGroupMsgRecall;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.filter.MostMatchType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/4
 * @package cn.sric.robot.listeners
 * @description
 **/
@Component
@Slf4j
public class GroupListener {

    ThreadPoolExecutorUtil threadPoolExecutorUtil = new ThreadPoolExecutorUtil(2, 2, 0
            , TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    @Resource
    IListApiService iListApiService;

    @Resource
    IPictureFileService pictureFileService;

    @Resource
    GroupMessageMapper groupMessageMapper;

    @Resource
    AdminService adminService;

    @Resource
    RedisUtil redisUtil;

    @OnGroup()
    @Filters(customFilter = {"picture", "groupPrintLog"}, customMostMatchType = MostMatchType.ALL)
    public void groupPicture(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String msg = groupMsg.getMsg();
        threadPoolExecutorUtil.execute(() -> {
            String s = redisUtil.get("picture" + qq);
            if (!StringUtils.isEmpty(s)) {
                sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qq) + "老色批爪巴");
                return;
            }
            String trgs = null;
            if (msg.contains(ConstUtil.LIKE)) {
                trgs = msg.substring(msg.indexOf(ConstUtil.LIKE) + 4);
            }
            boolean fal = false;
            if (msg.equals(ConstUtil.SE_TU)) {
                fal = true;
            }
            int i = RandomUtil.randomInt(1, 100);
            if (i == 50) {
                sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qq) + "老色批爪巴");
                redisUtil.set("picture" + qq, "停止刷图5分钟", 300, TimeUnit.SECONDS);
            } else {
                String url = pictureFileService.randomFind(fal, trgs);
                sender.SENDER.sendGroupMsg(groupMsg, Cat.getImage(url));
            }
        });
    }


    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.REVIEW, matchType = MatchType.STARTS_WITH))
    public void review(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        Map<String, String> map = iListApiService.review();
        sender.SENDER.sendGroupMsg(groupMsg, map.get("review"));
        sender.SENDER.sendPrivateMsg(qq, map.get("music"));
    }

    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.BANNED, matchType = MatchType.STARTS_WITH))
    public void banNed(GroupMsg groupMsg, MsgSender sender) {
        adminService.getThePackage(groupMsg, sender);
    }

    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.FAN_YI, matchType = MatchType.STARTS_WITH))
    public void fanYi(GroupMsg groupMsg, MsgSender sender) {
        String translate = iListApiService.translate(groupMsg.getMsg());
        sender.SENDER.sendGroupMsg(groupMsg, translate);
    }

    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.QR, matchType = MatchType.STARTS_WITH))
    public void qr(GroupMsg groupMsg, MsgSender sender) {
        String msg = groupMsg.getMsg();
        String file = iListApiService.findQr(msg);
        String image = Cat.getImage(file);
        sender.SENDER.sendGroupMsg(groupMsg, image + "\n");
    }

    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.MUSIC_TEST, matchType = MatchType.STARTS_WITH))
    public void musicTest(GroupMsg groupMsg, MsgSender sender) {
        String msg = groupMsg.getMsg();
        String music = Cat.getMusic(msg.substring(msg.indexOf("歌") + 1));
        sender.SENDER.sendGroupMsg(groupMsg, music);
    }

    @OnGroup()
    @Filters(customFilter = {"groupPrintLog"}, value = @Filter(value = ConstUtil.BAI_DU, matchType = MatchType.STARTS_WITH))
    public void baiDu(GroupMsg groupMsg, MsgSender sender) {
        String msg = groupMsg.getMsg();
        String s = iListApiService.letMeBaiDu(msg);
        sender.SENDER.sendGroupMsg(groupMsg, s);
    }


    @OnGroup()
    @Filters(value = {@Filter(atBot = true)}, customFilter = {"groupPrintLog"})
    public void groupVoice(GroupMsg groupMsg, MsgSender sender) {
        String msg = groupMsg.getText().trim();
        String qq = groupMsg.getAccountInfo().getAccountCode();
        if (msg.startsWith("说")) {
            String voice = null;
            try {
                voice = TApiUtil.getVoice(msg);
            } catch (Exception e) {
                e.printStackTrace();
                sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qq) + "看不懂你想BB什么。");
            }
            sender.SENDER.sendGroupMsg(groupMsg, Cat.getVoice(voice));
            File file = new File(voice);
            file.delete();
        }
    }

    @OnGroupMsgRecall
    public void privateMsgRecall(GroupMsgRecall msgRecall, Sender sender) {
        String id = msgRecall.getId();
        groupMessageMapper.selectOne(new QueryWrapper<GroupMessage>().eq("msg_id", id).select("message"));
        String msg = msgRecall.getMsg();
        String code = msgRecall.getAccountInfo().getAccountCode();
        String groupCode = msgRecall.getGroupInfo().getGroupCode();
        String groupName = msgRecall.getGroupInfo().getGroupName();
        GroupMsgRecall.Type groupRecallType = msgRecall.getGroupRecallType();
        String type = groupRecallType.toString().equals("PROACTIVE") ? "主动" : "被动";
        String name = msgRecall.getAccountInfo().getAccountNickname();
        String message = "在这个群中" + groupCode + "\n群名称" + groupName + "\n账号:" + code + "\n昵称" + name + "\n想撤回" + msg + "\n是" + type + "动撤回的";
        sender.sendPrivateMsg(ConstUtil.QQ_CODE, message);
    }


}
