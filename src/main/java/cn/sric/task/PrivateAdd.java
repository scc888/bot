package cn.sric.task;

import cn.sric.common.pojo.PrivateMessage;
import cn.sric.dao.message.PrivateMessageMapper;
import cn.sric.robot.listeners.PrivateListener;
import cn.sric.util.ConstUtil;
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
public class PrivateAdd {


    @Resource
    PrivateMessageMapper privateMessageMapper;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void addPrivateMsg() throws InterruptedException {
        log.info("时间:----->>>>{},正在准备消费私聊队列中的数据--->>>大概有这么多({})", LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER), PrivateListener.blockingQueue.size());
        int num = 0;
        do {
            PrivateMessage poll = PrivateListener.blockingQueue.poll(60, TimeUnit.SECONDS);
            if (!StringUtils.isEmpty(poll)) {
                privateMessageMapper.insert(poll);
                num++;
            }
        } while (PrivateListener.blockingQueue.size() != 0);
        log.info("时间:----->>>>{}, 太好啦 (~.~) 消费完私聊队列中的数据--->>>大概消费了有这么多({})"
                , LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER), num);
    }
}