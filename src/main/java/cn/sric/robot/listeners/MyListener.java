//package cn.sric.robot.listeners;
//
//import cn.sric.common.pojo.GroupMessage;
//import cn.sric.common.pojo.PrivateMessage;
//import cn.sric.util.param.SystemParam;
//import love.forte.simbot.annotation.OnGroup;
//import love.forte.simbot.annotation.OnPrivate;
//import love.forte.simbot.api.message.events.GroupMsg;
//import love.forte.simbot.api.message.events.PrivateMsg;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * 测试监听器。
// * 在未配置simbot依赖扫描的情况下，应当使用Springboot的依赖管理注解，例如@Component。
// *
// * @author ForteScarlet
// */
//@Component
//public class MyListener {
//
//
//    public static boolean isOK = false;
//    public static LinkedBlockingQueue<PrivateMessage> blockingQueue = new LinkedBlockingQueue<>();
//    public static LinkedBlockingQueue<GroupMessage> groupMessagesQueue = new LinkedBlockingQueue<>();
//
//
//
//    @OnPrivate
//    public void privateMessage(PrivateMsg privateMsg) throws InterruptedException {
//
//    }
//
//
//    @OnGroup
//    public void groupMessage(GroupMsg groupMsg) throws InterruptedException {
//        GroupMessage groupMessage = new GroupMessage();
//        Date date = new Date(groupMsg.getTime());
//        groupMessage.setTime(date)
//                .setIsRecall(0)
//                .setMessage(groupMsg.getMsg())
//                .setQqCode(groupMsg.getAccountInfo().getAccountCode())
//                .setGroupCode(groupMsg.getGroupInfo().getGroupCode())
//                .setRecipient(SystemParam.strCurrentQQ);
//        groupMessagesQueue.put(groupMessage);
//    }
//
//
//}
