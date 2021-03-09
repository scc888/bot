package cn.sric.robot.filter;

import cn.sric.util.ConstUtil;
import cn.sric.util.GroupUtil;
import cn.sric.util.param.SystemParam;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/18
 * @package cn.sric.robot.filter
 * @description
 **/
@Component
public class AdminFilter {
    @Component("isItMe")
    public static class isItMe implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            return data.getMsgGet().getAccountInfo().getAccountCode().equals(ConstUtil.QQ_CODE);
        }
    }

    @Component("botIsAdmin")
    public static class BotIsAdmin implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            String groupCode = "";

            if (data.getMsgGet() instanceof GroupMsg) {
                groupCode = ((GroupMsg) data.getMsgGet()).getGroupInfo().getGroupCode();
            }
            boolean admin = GroupUtil.isAdmin(groupCode, SystemParam.strCurrentQQ);
            if (admin) {
                return true;
            } else {
                SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "我暂时还没有权限");
                return false;
            }
        }
    }

}
