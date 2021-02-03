package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.GroupMessage;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.group.AdminService;
import cn.sric.util.Cat;
import cn.sric.util.ConstUtil;
import cn.sric.util.TApiUtil;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnGroupMsgRecall;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
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


    @OnGroup()
    public void groupMessage(GroupMsg groupMsg, MsgSender sender){
        String qq = groupMsg.getAccountInfo().getAccountCode();

        String msg = groupMsg.getMsg();
        if (msg.contains(ConstUtil.BANNED)) {
            adminService.getThePackage(groupMsg, sender);
        } else if (msg.startsWith(ConstUtil.FAN_YI)) {
            String translate = iListApiService.translate(msg);
            sender.SENDER.sendGroupMsg(groupMsg, translate);
        } else if (msg.equals(ConstUtil.REVIEW)) {
            Map<String, String> map = iListApiService.review();
            sender.SENDER.sendGroupMsg(groupMsg, map.get("review"));
            sender.SENDER.sendPrivateMsg(qq, map.get("music"));
        } else if (msg.startsWith(ConstUtil.PICTURE) || msg.startsWith(ConstUtil.TU_LAI)) {
            threadPoolExecutorUtil.execute(() -> {
                String trgs = null;
                if (msg.contains(ConstUtil.LIKE)) {
                    trgs = msg.substring(msg.indexOf(ConstUtil.LIKE) + 4);
                }
                int i = RandomUtil.randomInt(1, 6);
                String url = pictureFileService.randomFind(i == 1, trgs);
                sender.SENDER.sendGroupMsg(groupMsg, i == 1 ? Cat.getImage(url, true) : Cat.getImage(url));
            });
        } else if (msg.startsWith(ConstUtil.QR)) {
            String file = iListApiService.findQr(msg);
            String image = Cat.getImage(file);
            sender.SENDER.sendGroupMsg(groupMsg, image);
        } else if (msg.startsWith(ConstUtil.MUSIC_TEST)) {
            String music = Cat.getMusic(msg.substring(msg.indexOf("歌") + 1));
            sender.SENDER.sendGroupMsg(groupMsg, music);
        } else if (msg.startsWith(ConstUtil.BAI_DU)) {
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

}
