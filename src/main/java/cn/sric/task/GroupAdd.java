package cn.sric.task;

import cn.sric.common.pojo.GroupMessage;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.robot.listeners.GroupListener;
import cn.sric.util.ConstUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author sric
 */
@Slf4j
@Component
@EnableScheduling
public class GroupAdd {

    @Resource
    GroupMessageMapper groupMessageMapper;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void addGroupMsg() throws InterruptedException {
        log.info("时间:----->>>>{},正在准备消费群聊队列中的数据---->>>大概有这么多({})"
                , LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER), GroupListener.groupMessagesQueue.size());
        int num = 0;
        do {
            //poll(timeout,unit) 消费队列中的数据，当队列中没有数据的话就会等待60秒，如果60秒内仍然队列中还没有新的数据添加，则返回null
            GroupMessage poll = GroupListener.groupMessagesQueue.poll(60, TimeUnit.SECONDS);
            if (!StringUtils.isEmpty(poll)) {
                groupMessageMapper.insert(poll);
                num++;
            }
        } while (GroupListener.groupMessagesQueue.size() != 0);
        log.info("时间:----->>>>{},消费完群聊队列中的数据--->>>大概消费了有这么多({})"
                , LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER), num);
    }
}
