package cn.sric.music.service;

import cn.sric.music.pojo.recommend.Music;

import java.util.List;
import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/4
 * @package cn.sric.music.service
 * @description
 **/
public interface IMusicService {


    List<Map<String, Object>> getRecommend(String code);


}
