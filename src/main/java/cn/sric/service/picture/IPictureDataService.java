package cn.sric.service.picture;

import cn.sric.common.pojo.PictureData;

import java.util.List;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/30
 * @package cn.sric.service.picture
 * @description
 **/
public interface IPictureDataService {

    /**
     * 查询总数
     *
     * @param isR
     * @return
     */
    int findPictureCount(boolean isR);

    /**
     * 保存 保存前有判断库里面是否存在这个数据
     *
     * @param pictureData
     * @return boolean
     */
    boolean savePictureData(PictureData pictureData);


    /**
     * 从库里面随机读取几条信息
     *
     * @param num
     * @param is8
     * @return
     */
    List<PictureData> randomFind(int num, boolean is8,String like);


}
