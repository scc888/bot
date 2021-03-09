package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.PrivateMessage;
import cn.sric.dao.message.PrivateMessageMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.Cat;
import cn.sric.util.ConstUtil;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.annotation.OnPrivateMsgRecall;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
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
 * @date 2020/12/31
 * @package cn.sric.robot.listeners
 * @description 监听私聊消息
 **/
@Component
@Slf4j
public class PrivateListener {

    ThreadPoolExecutorUtil threadPoolExecutorUtil = new ThreadPoolExecutorUtil(2, 2, 0
            , TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());


    @Resource
    IPictureFileService pictureFileService;

    @Resource
    IListApiService iListApiService;


    @Resource
    IPictureDataService iPictureDataService;
    @Resource
    PrivateMessageMapper privateMessageMapper;


    @OnPrivateMsgRecall
    public void privateMsgRecall(PrivateMsgRecall msgRecall, Sender sender) {
        String id = msgRecall.getId();
        id = id.replaceFirst("REC-", "");
        QueryWrapper<PrivateMessage> query = new QueryWrapper<>();
        query.eq("msg_id", id).select("message");
        PrivateMessage privateMessage = privateMessageMapper.selectOne(query);
        String msg = privateMessage.getMessage();
        String code = msgRecall.getAccountInfo().getAccountCode();
        String name = msgRecall.getAccountInfo().getAccountNickname();
        sender.sendPrivateMsg(ConstUtil.QQ_CODE, "这个人\n账号:" + code + "\n昵称:" + name + "\n想撤回" + msg);
        privateMessageMapper.updateRecall(id);
    }

    @OnPrivate
    @Filters(customFilter = {"privatePrintLog"})
    public void privateMessage(PrivateMsg privateMsg, MsgSender sender) throws InterruptedException {
        String msg = privateMsg.getMsg();
        msg = msg.replaceAll(" ", "");
        String gossip = iListApiService.gossip(msg);
        sender.SENDER.sendPrivateMsg(privateMsg, gossip);
    }


    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.FAN_YI, matchType = MatchType.STARTS_WITH, trim = true)})
    public void fanYi(PrivateMsg privateMsg, Sender senderMsg) {
        String ret = iListApiService.translate(privateMsg.getMsg());
        senderMsg.sendPrivateMsg(privateMsg, ret);
    }

    @OnPrivate
    @Filters(value = {@Filter(value = "查看总数", matchType = MatchType.STARTS_WITH, trim = true)}, customFilter = {"isItMe"})
    public void getCountPicture(PrivateMsg privateMsg, Sender senderMsg) {
        senderMsg.sendPrivateMsg(privateMsg, "总数数量为:" + iPictureDataService.findPictureCount(false));
        senderMsg.sendPrivateMsg(privateMsg, "true数量为:" + iPictureDataService.findPictureCount(true));
    }

    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.QR, matchType = MatchType.STARTS_WITH, trim = true)})
    public void qr(PrivateMsg privateMsg, Sender senderMsg) {
        String qrUrl = iListApiService.findQr(privateMsg.getMsg());
        String image = Cat.getImage(ConstUtil.QR);
        senderMsg.sendPrivateMsg(privateMsg, image + "\n");
        File file = new File(qrUrl);
        file.delete();
    }


    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.MUSIC_TEST, matchType = MatchType.STARTS_WITH, trim = true)})
    public void musicTest(PrivateMsg privateMsg, Sender senderMsg) {
        String msg = privateMsg.getMsg();
        int index = msg.indexOf("点歌");
        msg = msg.substring(index + 2);
        String music = Cat.getMusic(msg);
        log.info("点歌成功本次点歌的内容为:---->>>" + music);
        senderMsg.sendPrivateMsg(privateMsg, music);
    }

    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.REVIEW, matchType = MatchType.STARTS_WITH, trim = true)})
    public void review(PrivateMsg privateMsg, Sender senderMsg) {
        Map<String, String> map = iListApiService.review();
        senderMsg.sendPrivateMsg(privateMsg, map.get("review"));
        senderMsg.sendPrivateMsg(privateMsg, map.get("music"));
    }


    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.BAI_DU, matchType = MatchType.STARTS_WITH, trim = true)})
    public void baiDu(PrivateMsg privateMsg, Sender senderMsg) {
        String msg = privateMsg.getMsg();
        String ret = iListApiService.letMeBaiDu(msg);
        senderMsg.sendPrivateMsg(privateMsg, ret);
    }

    @OnPrivate
    @Filters(value = {@Filter(value = ConstUtil.PICTURE, matchType = MatchType.STARTS_WITH, trim = true), @Filter(value = ConstUtil.TU_LAI, matchType = MatchType.STARTS_WITH, trim = true)})
    public void privatePicture(PrivateMsg privateMsg, Sender senderMsg) {
        String finalMsg = privateMsg.getMsg();
        threadPoolExecutorUtil.execute(() -> {
            String like = null;
            if (finalMsg.contains(ConstUtil.LIKE)) {
                like = finalMsg.substring(finalMsg.indexOf(ConstUtil.LIKE) + 4);
            }
            senderMsg.sendPrivateMsg(privateMsg, "请稍后");
            int i = RandomUtil.randomInt(1, 6);
            String url = pictureFileService.randomFind(i == 1, like);
            if (!StringUtils.isEmpty(url)) {
                senderMsg.sendPrivateMsg(privateMsg, i == 1 ? Cat.getImage(url, true) : Cat.getImage(url));
            } else {
                senderMsg.sendPrivateMsg(privateMsg, "找不到你想要的这种图");
            }
        });
    }


    @OnPrivate
    @Filters(customFilter = {"isItMe", "botIsAdmin"}, customMostMatchType = MostMatchType.ALL, value = {@Filter(value = "解禁", matchType = MatchType.STARTS_WITH, trim = true), @Filter(value = "禁言", matchType = MatchType.STARTS_WITH, trim = true)})
    public void groupSetGroupBan(PrivateMsg privateMsg, MsgSender sender) {
        try {
            String msg = privateMsg.getMsg();
            String groupCode = msg.substring(msg.indexOf("群号:") + 3, msg.indexOf(','));
            String qqCode = msg.substring(msg.indexOf("账号:") + 3);
            if (msg.contains("解禁")) {
                sender.SETTER.setGroupBan(groupCode, qqCode, 0);
                sender.SENDER.sendPrivateMsg(privateMsg, "解禁成功");
            } else {
                String time = msg.substring(msg.indexOf("时长:" + 3), msg.indexOf("。"));
                Integer integer = Integer.valueOf(time);
                sender.SETTER.setGroupBan(groupCode, qqCode, integer);
                sender.SENDER.sendPrivateMsg(privateMsg, "禁言成功。时长为:" + integer + "分钟");
            }
        } catch (Exception e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "解禁的正确格式为:\n 解禁群号:{groupCode},账号:{qqCode}");
            sender.SENDER.sendPrivateMsg(privateMsg, "禁言的正确格式为:\n 禁言群号:{groupCode},时长:{time}。账号:{qqCode}");

        }
    }
}
