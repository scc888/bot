package cn.sric.dao.picture;

import cn.sric.common.pojo.PictureData;
import cn.sric.common.vo.PictureRanking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/30
 * @package cn.sric.dao
 * @description
 **/
@Mapper
public interface PictureDataMapper extends BaseMapper<PictureData> {
   /* *//**
     * 随机查询几条信息
     *
     * @param num
     * @param isR
     * @return
     *//*
    List<PictureData> randomFind(@Param("num") Integer num, @Param("isR") Integer isR);

*/
    /**
     * 随机查询几条信息
     *
     * @param num  数量
     * @param isR  boolean
     * @param like tags
     * @return
     */
    List<PictureData> randomFind(@Param("num") Integer num, @Param("isR") Integer isR, @Param("like") String like);


    /**
     * 根据id修改本地路径
     *
     * @param id
     * @param localUrl
     * @return
     */
    @Update("UPDATE picture_data SET local_url=#{localUrl} WHERE id=#{id}")
    int updateLocalUrlById(@Param("id") long id, @Param("localUrl") String localUrl);




}
