package cn.sric.task;

import cn.sric.service.file.IPictureFileService;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.ConstUtil;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sric
 */
@Slf4j
@Component
@EnableScheduling
public class DownloadTask {

    @Resource
    IPictureFileService pictureFileService;

    @Resource
    IPictureDataService iPictureDataService;


    ThreadPoolExecutorUtil taskExecutorService = new ThreadPoolExecutorUtil(3, 3,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());


    public static Integer a = 0;

    @Scheduled(cron = "0 40 16 * * * ")
    public void task() throws InterruptedException {
        Integer startCount = iPictureDataService.findPictureCount(false);
        String start = LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER);
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "开始时间---:" +
                start + "\n开始总数为:" + startCount);
        log.info("开始时间---:" + start
                + "\n开始总数为:" + startCount);
        int num = 300;
        for (int i = 0; i < num; i++) {
            taskExecutorService.execute(() -> {
                System.out.println("线程" + Thread.currentThread().getName() + "正在运行第:" + ++a + "次");
                pictureFileService.downloadPicture(10);
                // pictureFileService.saveAstringent(10);
            });
        }
        while (true) {
            TimeUnit.SECONDS.sleep(60);
            //获取队列中的信息
            BlockingQueue<Runnable> queue = taskExecutorService.getQueue();
            //活动的线程的数量，也就是线程池在运行的最大数量
            int activeCount = taskExecutorService.getActiveCount();
            if (queue.isEmpty() && activeCount == 0) {
                break;
            }
        }
        Integer endCount = iPictureDataService.findPictureCount(false);
        String endTime = LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER);
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "结束时间---:"
                + endTime + "\n结束总数"
                + endCount + "\n总共添加了" + (endCount - startCount) + "条数据");
        log.info("结束时间---:" + endTime + "\n结束总数"
                + endCount + "\n总共添加了" + (endCount - startCount) + "条数据");
        a = 0;
        //业务执行完成 关闭连接池
//        taskExecutorService.shutdownNow();
    }


}