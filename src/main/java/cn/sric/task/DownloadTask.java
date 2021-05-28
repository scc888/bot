package cn.sric.task;

import cn.sric.common.pojo.Code;
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
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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


    ThreadPoolExecutorUtil taskExecutorService = new ThreadPoolExecutorUtil(0, 3,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    public static Integer a = 0;

    @Scheduled(cron = "0 0 4 ? 1-12 2,5,7 ")
    public void task() throws InterruptedException {
        if (!ConstUtil.IS_TASK) {
            log.info("定时任务被关闭");
            return;
        }
        Integer startCount = iPictureDataService.findPictureCount(false);
        String start = LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER);
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "开始时间---:" +
                start + "\n开始总数为:" + startCount);
        log.info("开始时间---:" + start
                + "\n开始总数为:" + startCount);
        int num = 200;
        for (int i = 0; i < num; i++) {
            taskExecutorService.execute(() -> {
                System.out.println("线程" + Thread.currentThread().getName() + "正在运行第:" + ++a + "次");
                pictureFileService.downloadPicture(10);
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

    public static void main(String[] args) {
        List<Code> list = new ArrayList<>();
        Code code = new Code();
        code.setId(1);
        code.setQqCode("1");
        list.add(code);
        Code code1 = new Code();
        code1.setId(1);
        code1.setQqCode("1");
        list.add(code1);
        Code code2 = new Code();
        code2.setId(2);
        code2.setQqCode("2");
        list.add(code2);
        list.forEach(System.out::println);
        System.out.println("--------------------------");
        List<String> collect = list.stream().map(Code::getQqCode).distinct().collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

}