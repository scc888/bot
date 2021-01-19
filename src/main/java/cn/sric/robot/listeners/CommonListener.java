//package cn.sric.robot.listeners;
//
//import cn.sric.service.common.IListApiService;
//import cn.sric.util.Cat;
//import cn.sric.util.ConstUtil;
//import cn.sric.util.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import love.forte.simbot.annotation.Filter;
//import love.forte.simbot.annotation.Listener;
//import love.forte.simbot.annotation.OnPrivate;
//import love.forte.simbot.api.message.events.PrivateMsg;
//import love.forte.simbot.api.sender.MsgSender;
//import love.forte.simbot.filter.MatchType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//
///**
// * @author sunchuanchuan
// * @version V1.0
// * @date 2020/12/31
// * @package cn.sric.robot.listeners
// * @description
// **/
//@Listener(priority = 1)
//@Component
//@Slf4j
//public class CommonListener {
//
//
//    @Resource
//    RedisUtil redisUtil;
//
//    @Resource
//    IListApiService iListApiService;
//
//
//    @OnPrivate
//    public void privateMusic(PrivateMsg privateMsg, MsgSender sender) {
//        if (!StringUtils.isEmpty(redisUtil.get(ConstUtil.PRIVATE + ":" + privateMsg.getAccountInfo().getAccountCode() + ":" + privateMsg.getId()))) {
//            return;
//        }
//        String msg = privateMsg.getMsg();
//        redisUtil.set(ConstUtil.PRIVATE + ":" + privateMsg.getAccountInfo().getAccountCode() + ":" + privateMsg.getId(), msg, 7200);
//        String gossip = iListApiService.gossip(msg);
//        sender.SENDER.sendPrivateMsg(privateMsg, gossip);
//    }
//
//
//}
