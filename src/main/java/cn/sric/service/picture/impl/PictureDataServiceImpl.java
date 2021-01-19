package cn.sric.service.picture.impl;

import cn.sric.common.pojo.PictureData;
import cn.sric.dao.picture.PictureDataMapper;
import cn.sric.service.picture.IPictureDataService;
import cn.sric.util.ConstUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/30
 * @package cn.sric.service.picture.impl
 * @description
 **/
@Service
public class PictureDataServiceImpl implements IPictureDataService {

    @Resource
    PictureDataMapper pictureDataMapper;


    @Override
    public int findPictureCount(boolean isR) {
        QueryWrapper<PictureData> queryWrapper = new QueryWrapper<>();
        if (isR) {
            queryWrapper.eq("r18", 1);
        }
        return pictureDataMapper.selectCount(queryWrapper);
    }



    @Override
    public boolean savePictureData(PictureData pictureData) {
        //根据pid 查询库里面是否存在数据
        QueryWrapper<PictureData> pictureDataQueryWrapper = new QueryWrapper<>();
        pictureDataQueryWrapper.eq("pid", pictureData.getPid());
        PictureData pictureDataOne = pictureDataMapper.selectOne(pictureDataQueryWrapper);
        //判断查出来的数据是否为空，如果为空直接添加
        if (StringUtils.isEmpty(pictureDataOne)) {
            return pictureDataMapper.insert(pictureData) > 0;
        }
        //数据不为空，判断一下传过来的本地路径是否和库里的本地路径一致，如果不一致则修改库的本地路径，如果一致返回true;
        if (!pictureDataOne.getLocalUrl().equals(pictureData.getLocalUrl()) && !pictureData.getLocalUrl().equals(ConstUtil.XXX)) {
            return pictureDataMapper.updateLocalUrlById(pictureDataOne.getId(), pictureData.getLocalUrl()) > 0;
        }
        return true;
    }

    @Override
    public List<PictureData> randomFind(int num, boolean is8) {
        List<PictureData> pictureDataList = pictureDataMapper.randomFind(num, is8 ? 1 : 0);
        return pictureDataList;
    }
}
