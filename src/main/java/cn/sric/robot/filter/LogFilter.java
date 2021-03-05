package cn.sric.robot.filter;

import cn.sric.common.pojo.GroupMessage;
import cn.sric.common.pojo.PrivateMessage;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.dao.message.PrivateMessageMapper;
import cn.sric.util.ConstUtil;
import cn.sric.util.MethodUtil;
import cn.sric.util.param.SystemParam;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupContainer;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/18
 * @package cn.sric.robot.filter
 * @description
 **/
@Component
public class LogFilter {


    @Component("privatePrintLog")
    @Slf4j
    public static class privatePrintLog implements ListenerFilter {
        @Resource
        PrivateMessageMapper privateMessageMapper;

        @Override
        public boolean test(@NotNull FilterData data) {

            AccountInfo accountInfo = data.getMsgGet().getAccountInfo();
            System.out.println(MethodUtil.getLocalDateTime() + " V/Bot " + SystemParam.strCurrentQQ + ":" + accountInfo.getAccountNickname() + "(" + accountInfo.getAccountCode() + ") -> " + data.getMsgGet().getOriginalData());
            if (data.getMsgGet() instanceof PrivateMsg) {
                PrivateMsg privateMsg = (PrivateMsg) data.getMsgGet();
                PrivateMessage privateMessage = new PrivateMessage();
                Date date = new Date(privateMsg.getTime());
                privateMessage.setIsRecall(0)
                        .setTime(date)
                        .setMessage((privateMsg.getMsg()))
                        .setQqCode(privateMsg.getAccountInfo().getAccountCode()).setMsgId(privateMsg.getId())
                        .setRecipient(SystemParam.strCurrentQQ);
                privateMessageMapper.insert(privateMessage);
            }
            return true;
        }
    }


    @Component("groupPrintLog")
    @Slf4j
    public static class groupPrintLog implements ListenerFilter {
        @Resource
        GroupMessageMapper groupMessageMapper;

        @Override
        public boolean test(@NotNull FilterData data) {
            GroupInfo groupInfo = null;
            if (data.getMsgGet() instanceof GroupContainer) {
                groupInfo = ((GroupContainer) data.getMsgGet()).getGroupInfo();
            }
            AccountInfo accountInfo = data.getMsgGet().getAccountInfo();
            System.out.println(MethodUtil.getLocalDateTime() + " V/Bot " + SystemParam.strCurrentQQ + ":" + groupInfo.getGroupName() + "(" + groupInfo.getGroupCode() + ")--->>>" + accountInfo.getAccountNickname() + "(" + accountInfo.getAccountCode() + ") -> " + data.getMsgGet().getOriginalData());
            if (data.getMsgGet() instanceof GroupMsg) {
                GroupMsg groupMsg = (GroupMsg) data.getMsgGet();
                //去库里面添加数据
                GroupMessage groupMessage = new GroupMessage();
                Date date = new Date(groupMsg.getTime());
                groupMessage.setTime(date)
                        .setIsRecall(0)
                        .setMessage(groupMsg.getMsg())
                        .setMsgId(groupMsg.getId())
                        .setQqCode(groupMsg.getAccountInfo().getAccountCode())
                        .setGroupCode(groupMsg.getGroupInfo().getGroupCode())
                        .setRecipient(SystemParam.strCurrentQQ);
                groupMessageMapper.insert(groupMessage);
            }
            return true;
        }
    }

}
