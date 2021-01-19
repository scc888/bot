package cn.sric.service.music;

import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/6
 * @package cn.sric.service.music
 * @description
 **/
public interface MusicService {

    /**
     * 查询歌单
     *
     * @return
     */
    Map<String, Object> playlist();

}
