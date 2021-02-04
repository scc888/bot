package cn.sric.robot.listeners;

import cn.hutool.core.util.RandomUtil;
import cn.sric.common.pojo.PrivateMessage;
import cn.sric.dao.message.PrivateMessageMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.Cat;
import cn.sric.util.ConstUtil;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listener;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.annotation.OnPrivateMsgRecall;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.FilterTargets;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
 * @description 监听私聊消息
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
    @Resource
    PrivateMessageMapper privateMessageMapper;


    @OnPrivate
    public void refreshPicture() {
        pictureFileService.refresh();
    }

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
    @Filter(target = FilterTargets.MSG)
    public void privateMessage(PrivateMsg privateMsg, MsgSender sender) throws InterruptedException {
        Sender senderMsg = sender.SENDER;
        String msg = privateMsg.getMsg();
        String getCountPicture = "查看总数";
        msg = msg.replaceAll(" ", "");
        if (msg.startsWith(ConstUtil.PICTURE) || msg.startsWith(ConstUtil.TU_LAI)) {
            String finalMsg = msg;
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
        } else if (msg.startsWith(ConstUtil.BAI_DU)) {
            String ret = iListApiService.letMeBaiDu(msg);
            senderMsg.sendPrivateMsg(privateMsg, ret);
        } else if (msg.equals(ConstUtil.REVIEW)) {
            Map<String, String> map = iListApiService.review();
            senderMsg.sendPrivateMsg(privateMsg, map.get("review"));
            senderMsg.sendPrivateMsg(privateMsg, map.get("music"));
        } else if (msg.startsWith(ConstUtil.QR)) {
            String qrUrl = iListApiService.findQr(msg);
            String image = Cat.getImage(ConstUtil.QR);
            senderMsg.sendPrivateMsg(privateMsg, image + "\n");
            File file = new File(qrUrl);
            file.delete();
        } else if (msg.startsWith(ConstUtil.MUSIC_TEST)) {
            int index = msg.indexOf("点歌");
            msg = msg.substring(index + 2);
            String music = Cat.getMusic(msg);
            log.info("点歌成功本次点歌的内容为:---->>>" + music);
            sender.SENDER.sendPrivateMsg(privateMsg, music);
        } else if (msg.equals(getCountPicture)) {
            sender.SENDER.sendPrivateMsg(privateMsg, "总数数量为:" + iPictureDataService.findPictureCount(false));
            sender.SENDER.sendPrivateMsg(privateMsg, "true数量为:" + iPictureDataService.findPictureCount(true));
        } else if (msg.startsWith(ConstUtil.FAN_YI)) {
            String ret = iListApiService.translate(msg);
            sender.SENDER.sendPrivateMsg(privateMsg, ret);
        } else {
            String gossip = iListApiService.gossip(msg);
            if (!gossip.contains("呵呵")) {
                sender.SENDER.sendPrivateMsg(privateMsg, gossip);
            }
        }

        PrivateMessage privateMessage = new PrivateMessage();
        Date date = new Date(privateMsg.getTime());
        privateMessage.setIsRecall(0)
                .setTime(date)
                .setMessage(privateMsg.getMsg())
                .setQqCode(privateMsg.getAccountInfo().getAccountCode()).setMsgId(privateMsg.getId())
                .setRecipient(SystemParam.strCurrentQQ);
        privateMessageMapper.insert(privateMessage);
    }


    public static void main(String[] args) {
    }
}
