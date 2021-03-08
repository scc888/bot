package cn.sric.robot.filter;

import cn.sric.util.ConstUtil;
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

}
