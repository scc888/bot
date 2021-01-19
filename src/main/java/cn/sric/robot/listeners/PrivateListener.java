package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.PrivateMessage;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.Cat;
import cn.sric.util.RedisUtil;
import cn.sric.util.TApiUtil;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Listener;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/31
 * @package cn.sric.robot.listeners
 * @description
 **/
@Component
@Slf4j
@Listener(priority = 2)
public class PrivateListener {

    ThreadPoolExecutorUtil threadPoolExecutorUtil = new ThreadPoolExecutorUtil(2, 2, 0
            , TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    public static LinkedBlockingQueue<PrivateMessage> blockingQueue = new LinkedBlockingQueue<>();


    @Resource
    IPictureFileService pictureFileService;

    @Resource
    IListApiService iListApiService;


    @Resource
    IPictureDataService iPictureDataService;


//    @OnPrivate
//    @Filter(value = "点歌", matchType = MatchType.STARTS_WITH)
//    public void privateMusic(PrivateMsg privateMsg, MsgSender sender) {
//        String msg = privateMsg.getMsg();
//        redisUtil.set(ConstUtil.PRIVATE + ":" + privateMsg.getAccountInfo().getAccountCode() + ":" + privateMsg.getId(), msg, 7200);
//        msg = msg.replaceAll(" ", "");
//
//    }

    @OnPrivate
    public void refreshPicture() {
        pictureFileService.refresh();
    }


    @OnPrivate
    public void privateMessage(PrivateMsg privateMsg, MsgSender sender) throws InterruptedException {
        Sender senderMsg = sender.SENDER;
        String msg = privateMsg.getMsg();
//        redisUtil.set(ConstUtil.PRIVATE + ":" + privateMsg.getAccountInfo().getAccountCode() + ":" + privateMsg.getId(), msg, 7200);
        msg = msg.replaceAll(" ", "");
        String picture = "来点图片";
        String baiDu = "百度";
        String review = "网易云热评";
        String qr = "获取二维码";
        String musicTest = "点歌";
        String getCountPicture = "查看总数";
        if (msg.equals(picture)) {
            threadPoolExecutorUtil.execute(() -> {
                senderMsg.sendPrivateMsg(privateMsg, "请稍后");
                int i = RandomUtil.randomInt(1, 6);
                String url = pictureFileService.randomFind(i == 1);
                senderMsg.sendPrivateMsg(privateMsg, i == 1 ? Cat.getImage(url, true) : Cat.getImage(url));
            });
        } else if (msg.contains(baiDu)) {
            String ret = iListApiService.letMeBaiDu(msg);
            senderMsg.sendPrivateMsg(privateMsg, ret);
        } else if (msg.equals(review)) {
            Map<String, String> map = iListApiService.review();
            senderMsg.sendPrivateMsg(privateMsg, map.get("review"));
            senderMsg.sendPrivateMsg(privateMsg, map.get("music"));
        } else if (msg.startsWith(qr)) {
            String qrUrl = iListApiService.findQr(msg);
            String image = Cat.getImage(qr);
            senderMsg.sendPrivateMsg(privateMsg, image + "\n" + qr);
            File file = new File(qrUrl);
            file.delete();
        } else if (msg.startsWith(musicTest)) {
            int index = msg.indexOf("点歌");
            msg = msg.substring(index + 2);
            String music = Cat.getMusic(msg);
            log.info("点歌成功本次点歌的内容为:---->>>" + music);
            sender.SENDER.sendPrivateMsg(privateMsg, music);
        } else if (msg.equals(getCountPicture)) {
            sender.SENDER.sendPrivateMsg(privateMsg, "总数数量为:" + iPictureDataService.findPictureCount(false));
            sender.SENDER.sendPrivateMsg(privateMsg, "true数量为:" + iPictureDataService.findPictureCount(true));
        } else if (msg.equals("刷新图库")) {

        } else if (msg.startsWith("翻译")) {
            String translate = iListApiService.translate(msg);
            sender.SENDER.sendPrivateMsg(privateMsg, translate);
        } else {
            String gossip = iListApiService.gossip(msg);
            sender.SENDER.sendPrivateMsg(privateMsg, gossip);
        }

        PrivateMessage privateMessage = new PrivateMessage();
        Date date = new Date(privateMsg.getTime());
        privateMessage.setIsRecall(0)
                .setTime(date)
                .setMessage(privateMsg.getMsg())
                .setQqCode(privateMsg.getAccountInfo().getAccountCode()).setMsgId(privateMsg.getId())
                .setRecipient(SystemParam.strCurrentQQ);
        blockingQueue.put(privateMessage);
    }


    public static void main(String[] args) {
    }
}
