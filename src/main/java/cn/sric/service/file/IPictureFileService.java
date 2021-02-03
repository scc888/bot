package cn.sric.service.file;

import cn.sric.common.dto.PictureDto;
import cn.sric.common.pojo.PictureData;

import java.util.List;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/30
 * @package cn.sric.service.file
 * @description
 **/
public interface IPictureFileService {


    /**
     * 查询图片信息
     *
     * @param num 一次查询的数量 最多10条
     * @return
     */
    PictureDto astringentGraph(int num);

    /**
     * 查询图片 默认为1
     *
     * @return
     */
    PictureDto astringentGraph();

    /**
     * 去网络查询图片信息
     *
     * @param num 一次查询的数量 最多10条
     * @return
     */
    List<PictureData> saveAstringent(int num);


    /**
     * 下载图片
     *
     * @param num 一次下载几张 ，也就是请求查询图片信息的数量
     * @return
     */
    List<String> downloadPicture(int num);

    /**
     * 下载图片 默认1张
     *
     * @return
     */
    String downloadPicture();


    /**
     * 随机查询下载
     *
     * @param num
     * @param is8
     * @return
     */
    List<String> randomFind(int num, boolean is8,String like);


    /**
     * 随机1张查询下载
     *
     * @param is8
     * @return
     */
    String randomFind(boolean is8,String like);


    void refresh();
}
