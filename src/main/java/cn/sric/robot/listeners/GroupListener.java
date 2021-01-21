package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.GroupMessage;
import cn.sric.common.pojo.PrivateMessage;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.group.AdminService;
import cn.sric.util.*;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.catcode.CatCodeUtil;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.assists.ActionMotivations;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
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
    public static LinkedBlockingQueue<GroupMessage> groupMessagesQueue = new LinkedBlockingQueue<>();


    @OnGroup()
    public void groupMessage(GroupMsg groupMsg, MsgSender sender) throws InterruptedException {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String banned = "领取套餐";
        String picture = "来点图片";
        String baiDu = "百度";
        String review = "网易云热评";
        String qr = "获取二维码";
        String musicTest = "点歌";
        String msg = groupMsg.getMsg();
        if (msg.contains(banned)) {
            adminService.getThePackage(groupMsg, sender);
        } else if (msg.startsWith("翻译")) {
            String translate = iListApiService.translate(msg);
            sender.SENDER.sendGroupMsg(groupMsg, translate);
        } else if (msg.equals(review)) {
            Map<String, String> map = iListApiService.review();
            sender.SENDER.sendGroupMsg(groupMsg, map.get("review"));
            sender.SENDER.sendPrivateMsg(qq, map.get("music"));
        } else if (msg.equals(picture)) {
            threadPoolExecutorUtil.execute(() -> {
                int i = RandomUtil.randomInt(1, 6);
                String url = pictureFileService.randomFind(i == 1);
                sender.SENDER.sendGroupMsg(groupMsg, i == 1 ? Cat.getImage(url, true) : Cat.getImage(url));
            });
        } else if (msg.startsWith(qr)) {
            String file = iListApiService.findQr(msg);
            String image = Cat.getImage(file);
            sender.SENDER.sendGroupMsg(groupMsg, image);
        } else if (msg.startsWith(musicTest)) {
            String music = Cat.getMusic(msg.substring(msg.indexOf("歌") + 1));
            sender.SENDER.sendGroupMsg(groupMsg, music);
        } else if (msg.startsWith(baiDu)) {
            String s = iListApiService.letMeBaiDu(msg);
            sender.SENDER.sendGroupMsg(groupMsg, s);
        }


        //去库里面添加数据
        GroupMessage groupMessage = new GroupMessage();
        Date date = new Date(groupMsg.getTime());
        groupMessage.setTime(date)
                .setIsRecall(0)
                .setMessage(msg)
                .setMsgId(groupMsg.getId())
                .setQqCode(groupMsg.getAccountInfo().getAccountCode())
                .setGroupCode(groupMsg.getGroupInfo().getGroupCode())
                .setRecipient(SystemParam.strCurrentQQ);
        groupMessageMapper.insert(groupMessage);
    }


    @OnGroup()
    @Filter(anyAt = true)
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
        String name = msgRecall.getAccountInfo().getAccountNickname();
        String message = "在这个群中" + groupCode + "\n群名称" + groupName + "\n账号:" + code + "\n昵称" + name + "\n想撤回" + msg + "\n是" + groupRecallType.toString() + "动撤回的";
        sender.sendPrivateMsg(ConstUtil.QQ_CODE, message);
    }

    public static void main(String[] args) {

        String msg = "说123";
        System.out.println(msg.substring(msg.indexOf("说") + 1));

    }
}
