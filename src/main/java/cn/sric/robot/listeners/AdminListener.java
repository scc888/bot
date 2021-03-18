package cn.sric.robot.listeners;

import cn.sric.service.file.IPictureFileService;
import cn.sric.util.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/18
 * @package cn.sric.robot.listeners
 * @description
 **/
@Component
@Slf4j
@Listens(priority = 0, value = {@Listen(value = PrivateMsg.class)})
public class AdminListener {

    @Resource
    IPictureFileService pictureFileService;

    @OnPrivate
    @Filters(value = {@Filter(value = "定时任务", matchType = MatchType.ENDS_WITH)}, customFilter = {"isItMe"})
    public void turnOnOrOff(PrivateMsg privateMsg, MsgSender sender) {
        String msg = privateMsg.getMsg();
        if ("开启定时任务".equals(msg)) {
            ConstUtil.IS_TASK = true;
            sender.SENDER.sendPrivateMsg(privateMsg, "开启定时任务");
        } else if ("关闭定时任务".equals(msg)) {
            ConstUtil.IS_TASK = false;
            sender.SENDER.sendPrivateMsg(privateMsg, "关闭定时任务");
        }
    }

    @OnPrivate
    @Filters(value = {@Filter(value = "刷新本地图片", matchType = MatchType.EQUALS)}, customFilter = {"isItMe"})
    public void refresh(PrivateMsg privateMsg, MsgSender sender) {
        pictureFileService.refresh();
    }


}
