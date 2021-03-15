package cn.sric.service.file.impl;

import cn.sric.common.dto.PictureDataDto;
import cn.sric.common.dto.PictureDto;
import cn.sric.common.pojo.PictureData;
import cn.sric.service.file.IPictureFileService;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.ConstUtil;
import cn.sric.util.OkHttp;
import cn.sric.util.down.file.PictureFileUtil;
import cn.sric.util.param.SystemParam;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.Sender;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/30
 * @package cn.sric.service.file.impl
 * @description
 **/
@Service
@Slf4j
public class PictureFileServiceImpl implements IPictureFileService {
    @Resource
    IPictureDataService iPictureDataService;

    @Override
    public PictureDto astringentGraph(int num) {
        String run = OkHttp.get(ConstUtil.SET_URL + "&num=" + num);
        return JSON.parseObject(run, PictureDto.class);
    }


    @Override
    public PictureDto astringentGraph() {
        return astringentGraph(1);
    }


    private List<PictureData> data(PictureDto pictureDto) {
        List<PictureData> pictureDataList = null;
        if (pictureDto.getCode() == 0) {
            pictureDataList = new ArrayList<>();
            for (PictureDataDto datum : pictureDto.getData()) {
                PictureData pictureData = JSON.parseObject(JSON.toJSONString(datum), PictureData.class);
                pictureData.setR18(datum.isR18() ? 1 : 0)
                        .setCreateTime(LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
                pictureDataList.add(pictureData);
            }
        }
        return pictureDataList;
    }

    @Override
    public List<String> downloadPicture(int num) {
        PictureDto pictureDto = null;
        try {
            pictureDto = astringentGraph(num);
        } catch (Exception e) {
            SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "获取图片时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
            log.error("获取图片时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
        }
        List<String> retString = new ArrayList<>();
        if (pictureDto.getCode() == 0) {
            List<PictureData> pictureDataList = data(pictureDto);
            pictureDataList.forEach(pictureData -> {
                String download = null;
                try {
                    download = PictureFileUtil.download(pictureData.getUrl());
                } catch (Exception e) {
                    SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "下载图片时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
                    log.error("下载图片时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
                }
                if (StringUtils.isEmpty(download)) {
                    pictureData.setLocalUrl("xxx").setFileSize(0L);
                } else {
                    pictureData.setLocalUrl(download);
                    File file = new File(download);
                    long l = file.length() / 1024;
                    pictureData.setLocalUrl(download).setFileSize(l);
                }
                retString.add(download);
                try {
                    iPictureDataService.savePictureData(pictureData);
                } catch (Exception e) {
                    SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "储存图片信息时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER));
                    log.info("储存图片信息时出现错误 \n时间:" + LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER) + "\n信息为:{}", pictureData.toString());
                }
            });
        }
        return retString;
    }

    @Override
    public String downloadPicture() {
        List<String> list = downloadPicture(1);
        return list.get(0);
    }

    @Override
    public List<String> randomFind(int num, boolean is8, String like) {
        List<PictureData> pictureDataList = iPictureDataService.randomFind(num, is8, like);
        List<String> list = new ArrayList<>();
        pictureDataList.forEach(pictureData -> {
            String download;
            try {
                download = PictureFileUtil.download(pictureData.getUrl());
                if (StringUtils.isEmpty(download)) {
                    download = ConstUtil.XXX;
                }
            } catch (Exception e) {
                download = ConstUtil.XXX;
            }
            list.add(download);
        });
        return list;
    }

    @Override
    public String randomFind(boolean is8, String like) {
        String url = randomFind(1, is8, like).get(0);
        while (true) {
            if (url.equals(ConstUtil.XXX)) {
                randomFind(is8, null);
            } else {
                break;
            }
        }
        return url;
    }

    @Override
    public void refresh() {
        long start = System.currentTimeMillis();
        ThreadPoolExecutorUtil executorService = new ThreadPoolExecutorUtil(3, 3,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        int size = 10;
        int count = iPictureDataService.findPictureCount(false);
        int num = count % size > 0 ? count / size + 1 : count / size;
        for (int i = 1; i <= num; i++) {
            List<PictureData> records = iPictureDataService.pageFind(i, size, null);
            for (PictureData record : records) {
                executorService.execute(() -> {
                    String download = PictureFileUtil.download(record.getUrl());
                    File file = new File(download);
                    long l = file.length() / 1024;
                    record.setLocalUrl(download).setFileSize(l);
                    iPictureDataService.savePictureData(record);
                });
            }
        }

        Sender sender = SystemParam.msgSender.SENDER;
        sender.sendPrivateMsg(ConstUtil.QQ_CODE, "总共要更新" + num + "条数据");

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(300);
            } catch (InterruptedException e) {
                log.error("刷新本地图片暂停出现异常");
            }
            //获取队列中的信息
            BlockingQueue<Runnable> queue = executorService.getQueue();
            //活动的线程的数量，也就是线程池在运行的最大数量
            int activeCount = executorService.getActiveCount();

            sender.sendPrivateMsg(ConstUtil.QQ_CODE, "大概还剩余   [" + queue.size() + "]   条数据要更新");

            if (queue.isEmpty() && activeCount == 0) {
                long end = System.currentTimeMillis();
                sender.sendPrivateMsg(ConstUtil.QQ_CODE, "更新完啦大概用了---->>>" + ((end - start) / 1000) + "秒");
                executorService.shutdown();
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        String url = "C:\\Users\\sric\\Desktop\\image\\123123.png";
        File file = new File(url);
        System.out.println(Math.round(file.length()));
        System.out.println((file.length() / 1024));

    }
}
