package cn.sric.music.service.impl;

import cn.sric.music.pojo.recommend.Ar;
import cn.sric.music.pojo.recommend.DailySongs;
import cn.sric.music.pojo.recommend.Music;
import cn.sric.music.service.IMusicService;
import cn.sric.music.util.RedisKeyUtil;
import cn.sric.util.ConstUtil;
import cn.sric.util.OkHttp;
import cn.sric.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/4
 * @package cn.sric.music.service.impl
 * @description
 **/
@Slf4j
@Service
public class MusicServiceImpl implements IMusicService {

    @Resource
    RedisUtil redisUtil;


    @Override
    public List<Map<String, Object>> getRecommend(String code) {
        String phone = redisUtil.get(RedisKeyUtil.getPhone(code));
        String cookie = redisUtil.get(RedisKeyUtil.getCookie(code, phone));
        String s;
        try {
            s = OkHttp.get(ConstUtil.MUSIC_URL + "/recommend/songs" + ConstUtil.getTimestamp(false), cookie);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        List<Map<String, Object>> restList = new ArrayList<>();


        Music music = JSON.parseObject(s, Music.class);
        assert music != null;
        if (music.getCode() == 200) {
            List<DailySongs> dailySongs = music.getData().getDailySongs();
            dailySongs.forEach(dailySong -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", dailySong.getName());
                map.put("id", dailySong.getId());
                StringBuffer singer = new StringBuffer();
                List<Ar> ar = dailySong.getAr();
                for (int i = 0; i < ar.size(); i++) {
                    if (i > 0) {
                        singer.append("  |  " + ar.get(i).getName());
                    } else {
                        singer.append(ar.get(i).getName());
                    }
                }
                map.put("singer", singer.toString());
                map.put("reason", dailySong.getReason());
                restList.add(map);
            });
        }
        return restList;
    }
}
