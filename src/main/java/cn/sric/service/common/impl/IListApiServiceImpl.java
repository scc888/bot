package cn.sric.service.common.impl;

import cn.sric.service.common.IListApiService;
import cn.sric.util.*;
import cn.sric.util.down.file.PictureFileUtil;
import cn.xsshome.taip.nlp.TAipNlp;
import cn.xsshome.taip.speech.TAipSpeech;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/11/20
 * @package cn.sric.service.single.impl
 * @description
 **/
@Service
@Slf4j
public class IListApiServiceImpl implements IListApiService {
    static TAipNlp aipNlp = new TAipNlp(ConstUtil.APP_ID, ConstUtil.APP_KEY);
    static TAipSpeech client = new TAipSpeech(ConstUtil.APP_ID, ConstUtil.APP_KEY);

    @Override
    public String gossip(String city) {
        try {
            city = city.replaceAll(" ", "");
            JSONObject json;
            //基础闲聊
            String result = aipNlp.nlpTextchat("session", city);
            json = JSON.parseObject(result);
            System.out.println("第1次请求");
            System.out.println("返回值: " + result);
            for (int i = 2; i < 11; i++) {
                if (json.getInteger("ret") == 0) {
                    break;
                }
                //基础闲聊
                result = aipNlp.nlpTextchat("session", city);
                json = JSON.parseObject(result);
                System.out.println("第" + i + "次请求");
                System.out.println("返回值: " + result);
            }
            JSONObject data = json.getJSONObject("data");
            String answer = data.getString("answer");
            if (json.getInteger("ret") == 0) {
                return answer;
            } else if (json.getInteger("ret") == 16394) {
                return "请求出现异常 false 返回码:{16394}";
            } else {
                return "请求时出现异常";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public Map<String, String> review() {
        Map<String, String> map = new HashMap<>();
        String run;
        run = OkHttp.get(ConstUtil.REVIEW);
        JSONObject jsonObject = JSONObject.parseObject(run);
        String name = jsonObject.getString("name");
        String artistsName = jsonObject.getString("artists_name");
        String musicUrl = jsonObject.getString("music_url");
        String nickname = jsonObject.getString("nickname");
        String comments = jsonObject.getString("comments");
        String review = comments + "\n\n" + "来自" + nickname + "    在「" + name + "」歌曲下方的评论\n\n" + "音乐:" + musicUrl;
        map.put("review", review);
        map.put("music", Cat.getMusic(name));
        return map;
    }

    @Override
    public String letMeBaiDu(String val) {
        val = val.replaceAll(" ", "");
        int index = val.indexOf("百度");
        val = val.substring(index + 2);
        Map<String, Object> map = new HashMap<>(1);
        map.put("keyword", val);
        String response = OkHttp.post(ConstUtil.LET_ME, map);
        JSONObject jsonObject = JSONObject.parseObject(response);
        String uniqId = jsonObject.getString("uniqId");
        String keyword = jsonObject.getString("keyword");
        return "你要搜索的【" + keyword + "】结果点击下列链接即可\n\n\n" + ConstUtil.LET_ME_RESULTS + uniqId;
    }

    @Override
    public String findQr(String msg) {
        msg = msg.substring(msg.indexOf("码") + 1);
        msg = msg.replaceAll(" ", "");
        StringBuilder stringBuilder = new StringBuilder(ConstUtil.QR_URL);
        StringBuilder append = stringBuilder.append(msg);
        String newUrl = StringEscapeUtils.unescapeHtml(append.toString());
        return PictureFileUtil.download(newUrl, ConstUtil.FILE_URL + "erWeiMa.jpg");
    }

    @Override
    public String voice(String msg) {
        try {
            msg = msg.substring(msg.indexOf("说") + 1).replaceAll(" ", "");
            //参数内容
            String text = msg;//合成的文本内容
            int speaker = 7;//语音发音人编码 普通话男声 1 静琪女声 5 欢馨女声 6 碧萱女声 7
            int format = 3;//合成语音格式编码 PCM 1 WAV 2 MP3 3
            int volume = 2;//合成语音音量 取值范围[-10, 10]，如-10表示音量相对默认值小10dB，0表示默认音量，10表示音量相对默认值大10dB
            int speed = 100;// 合成语音语速，默认100
            int aht = 5;//合成语音降低/升高半音个数，即改变音高，默认0
            int apc = 26;//控制频谱翘曲的程度，改变说话人的音色，默认58
//        String result = client.TtsSynthesis(text, speaker,format );//语音合成（AI Lab） 默认参数
            String result = client.TtsSynthesis(text, speaker, format, volume, speed, aht, apc);//语音合成（AI Lab） 全部参数
            JSONObject jsonObject = JSON.parseObject(result);
            String speech = jsonObject.getJSONObject("data").getString("speech");
            String retUrl = Base64Util.decodeBase64(speech, ConstUtil.VOICE_URL, "WAV", false);
            return retUrl;
        } catch (Exception e) {
            return "出现异常";
        }
    }

    @Override
    public String translate(String msg) {
        msg = msg.substring(msg.indexOf("译") + 1);
        String language = TApiUtil.language(msg);
        String rest;
        if (language.equals("zh")) {
            rest = TApiUtil.translate(msg, language, "en");
        } else if (language.equals("en")) {
            rest = TApiUtil.translate(msg, language, "zh");
        } else {
            String en = TApiUtil.translate(msg, language, "en");
            String zh = TApiUtil.translate(msg, language, "zh");
            rest = "中文:" + zh + "\n英文:" + en;
        }
        return rest;
    }


    public static void main(String[] args) throws Exception {
        IListApiServiceImpl iListApiService = new IListApiServiceImpl();
        System.out.println(iListApiService.voice("刘叔牛逼"));

    }
}

