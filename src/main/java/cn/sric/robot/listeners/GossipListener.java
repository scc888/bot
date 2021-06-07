package cn.sric.robot.listeners;

import cn.sric.robot.filter.LogFilter;
import cn.sric.service.common.IListApiService;
import cn.sric.util.ConstUtil;
import cn.sric.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/3/10
 * @package cn.sric.robot.listeners
 * @description
 **/
@Component
@Slf4j
public class GossipListener {

    @Resource
    IListApiService iListApiService;

    @OnPrivate
    @SpareListen
    @Filters(customFilter = {FilterUtil.PRIVATE_PRINT_LOG})
    public void privateMessage(PrivateMsg privateMsg, MsgSender sender) {
        String msg = privateMsg.getMsg();
        msg = msg.replaceAll(" ", "");
        String gossip = iListApiService.gossip(msg);
        sender.SENDER.sendPrivateMsg(privateMsg, gossip);
    }


}
