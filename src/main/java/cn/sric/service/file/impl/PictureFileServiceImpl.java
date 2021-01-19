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
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<PictureData> saveAstringent(int num) {
        String run = OkHttp.get(ConstUtil.SET_URL + "&num=" + num);
        PictureDto pictureDto = JSON.parseObject(run, PictureDto.class);
        if (pictureDto != null) {
            List<PictureData> data = data(pictureDto);
            data.forEach(pictureData -> {
                pictureData.setLocalUrl(ConstUtil.XXX);
                iPictureDataService.savePictureData(pictureData);
            });
            return data;
        }
        return null;
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
                pictureData.setLocalUrl(StringUtils.isEmpty(download) ? "xxx" : download);
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
    public List<String> randomFind(int num, boolean is8) {
        List<PictureData> pictureDataList = iPictureDataService.randomFind(num, is8);
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
    public String randomFind(boolean is8) {
        String url = randomFind(1, is8).get(0);
        while (true) {
            if (url.equals(ConstUtil.XXX)) {
                randomFind(is8);
            } else {
                break;
            }
        }
        return url;
    }

    @Override
    public void refresh() {


    }

}
