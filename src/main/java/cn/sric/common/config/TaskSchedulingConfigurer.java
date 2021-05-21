/*
package cn.sric.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

*/
/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/3/12
 * @package cn.sric.common.config
 * @description
 **//*

@Configuration
public class TaskSchedulingConfigurer implements SchedulingConfigurer {
    private String cron = "0 0/1 * * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 构建一个线程来执行job
        Runnable job = () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String nowStr = sdf.format(new Date());
            System.out.println(nowStr);
        };

        // 构建一个trigger ,并且设置重写trigger的下一次执行时间
        Trigger trigger = triggerContext -> {
            CronTrigger ct = new CronTrigger(cron);
            return ct.nextExecutionTime(triggerContext);
        };
        taskRegistrar.addTriggerTask(job, trigger);

    }
}
*/
