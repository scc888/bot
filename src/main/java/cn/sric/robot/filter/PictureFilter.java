package cn.sric.robot.filter;

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

    @Component("picture")
    @Async
    public static class Picture implements ListenerFilter {
        @Override
        public boolean test(@NotNull FilterData data) {
            List<String> list = new ArrayList<>();
            list.add("图来");
            list.add("来点图片");
            list.add("r18色图");
            list.add("r18涩图");
            list.add("来点r18色图");
            list.add("来点r18涩图");
            list.add("来一份r18色图");
            list.add("来一份r18涩图");
            list.add("来份r18色图");
            list.add("来份r18涩图");
            list.add("色图");
            list.add("涩图");
            list.add("来点色图");
            list.add("来点涩图");
            list.add("来一份色图");
            list.add("来一份涩图");
            list.add("来份色图");
            list.add("来份涩图");
            list.add("来点好看的");
            list.add("来点好康的");
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
