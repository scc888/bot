package cn.sric.service.music.impl;

import cn.sric.service.music.MusicService;
import cn.sric.util.ConstUtil;
import cn.sric.util.OkHttp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/6
 * @package cn.sric.service.music.impl
 * @description
 **/
public class MusicServiceImpl implements MusicService {

    @Override
    public Map<String, Object> playlist() {
        Map<String, Object> map = new HashMap<>();
        String resp = OkHttp.get(ConstUtil.PLAY_LIST(null, null));
        JSONObject jsonObject = JSON.parseObject(resp);
        if (jsonObject.getInteger("code") != 200 || StringUtils.isEmpty(jsonObject.getInteger("playlists"))) {
            map.put("message", "返回错误");
            return map;
        }

        return null;
    }
}
