package cn.sric.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import catcode.CatCodeUtil;
import catcode.CodeTemplate;
import catcode.MutableNeko;
import catcode.Neko;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/31
 * @package simbot.example.demo.util
 * @description
 **/
@Slf4j
public class Cat {
    public static final Map<String, String[]> CONTEXT = new HashMap<>();
    public static final CatCodeUtil UTILS = CatCodeUtil.getInstance();
    final static CodeTemplate<String> STRING_TEMPLATE = UTILS.getStringTemplate();
    final static CodeTemplate<Neko> NEKO_TEMPLATE = UTILS.getNekoTemplate();

    public static String at(String qq) {
        String at = STRING_TEMPLATE.at(qq) + " ";
        return at;
    }


    public static String atAll() {
        String at = STRING_TEMPLATE.atAll() + " ";
        return at;
    }


    public static String emoji(long id) {
        String emoji = STRING_TEMPLATE.bface(id);
        return emoji;
    }

    public static String shake() {
        String shake = STRING_TEMPLATE.shake();
        return shake;
    }

    public static String getImage(String file) {
        return STRING_TEMPLATE.image(file);
    }

    public static String getImage(String file, boolean flash) {
        return STRING_TEMPLATE.image(file, flash);
    }

    public static Neko getRecord(String path) {
        MutableNeko neko = NEKO_TEMPLATE.record(path, true).mutable();
        neko.setType("voice");
        return neko.immutable();
    }


    public static String getVoice(String file) {
        Map<String, String> map = new HashMap<>();
        map.put("file", file);
        return Cat.UTILS.toCat("voice", map);
    }

    public static String getMusic1(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "qq");
        map.put("id", id);
        return Cat.UTILS.toCat("app", map);
    }

    public static String getMusic(String music) {
        String resp = OkHttp.get("https://autumnfish.cn/search?keywords=" + music + "&limit=1");
        Map<String, Object> map = new HashMap<>();
        JSONObject json = JSON.parseObject(resp);
        JSONArray jsonArray = json.getJSONObject("result").getJSONArray("songs");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String id = jsonObject.getString("id");
        //获取歌曲的详情内容
        JSONArray artistsJson = jsonObject.getJSONArray("artists");
        List<String> artistList = artistsJson.stream().map(item -> {
            JSONObject object = (JSONObject) item;
            return object.getString("name");
        }).collect(Collectors.toList());
        String artists = String.join("&", artistList);
        String song = "https://autumnfish.cn/song/url?id=" + id;
        String url = JSON.parseObject(OkHttp.get(song)).getJSONArray("data").getJSONObject(0).getString("url");
        String detail = "https://autumnfish.cn/song/detail?ids=" + id;
        String preview = JSON.parseObject(OkHttp.get(detail)).getJSONArray("songs").getJSONObject(0).getJSONObject("al").getString("picUrl");
        String jumpUrl = "https://music.163.com/#/song?id=" + id;
        String title = jsonObject.getString("name");
        String code = "{" +
                "  \"app\": \"com.tencent.structmsg\"," +
                "  \"desc\": \"音乐\"," +
                "  \"view\": \"music\"," +
                "  \"ver\": \"0.0.0.1\"," +
                "  \"prompt\": \"[分享]" + title + "\"," +
                "  \"appID\": \"\"," +
                "  \"sourceName\": \"\"," +
                "  \"actionData\": \"\"," +
                "  \"actionData_A\": \"\"," +
                "  \"sourceUrl\": \"\"," +
                "  \"meta\": {" +
                "    \"music\": {" +
                "      \"preview\": \"" + preview + "\"," +
                "      \"app_type\": 1," +
                "      \"musicUrl\": \"" + url + "\"," +
                "      \"title\": \"" + title + "\"," +
                "      \"jumpUrl\": \"" + jumpUrl + "\"," +
                "      \"source_url\": \"\"," +
                "      \"android_pkg_name\": \"\"," +
                "      \"source_icon\": \"\"," +
                "      \"appid\": 100495085," +
                "      \"sourceMsgId\": \"0\"," +
                "      \"action\": \"\"," +
                "      \"tag\": \"网易云音乐\"," +
                "      \"desc\": \"" + artists + "\"" +
                "    }" +
                "  }," +
                "  \"config\": {" +
                "    \"forward\": true," +
                "    \"ctime\": " + (System.currentTimeMillis() / 1000) + "," +
                "    \"type\": \"normal\"," +
                "    \"token\": \"\"" +
                "  }," +
                "  \"text\": \"\"," +
                "  \"sourceAd\": \"\"," +
                "  \"extra\": \"\"" +
                "}";
        map.put("content", code);
        return Cat.UTILS.toCat("app", map);
    }
}
