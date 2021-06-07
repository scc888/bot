package cn.sric.robot.filter;

import cn.sric.util.FilterUtil;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PictureFilter {

    @Component(FilterUtil.PICTURE)
    @Async
    public static class Picture implements ListenerFilter {
        static List<String> list = new ArrayList<>();

        static {
            list.add("图来");
            list.add("来点图片");
            list.add("来点色图");
            list.add("来点涩图");
            list.add("来一份色图");
            list.add("来一份涩图");
            list.add("来份色图");
            list.add("来份涩图");
            list.add("来点好看的");
            list.add("来点好康的");

        }

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                if (list.contains(message)) {
                    return true;
                }
                return message.contains("色") && message.contains("图") && message.length() < 10;
            }
            return false;
        }
    }
}
