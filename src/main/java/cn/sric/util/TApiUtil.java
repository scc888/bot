package cn.sric.util;

import cn.xsshome.taip.nlp.TAipNlp;
import cn.xsshome.taip.speech.TAipSpeech;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/18
 * @package cn.sric.util
 * @description
 **/
@Slf4j
public class TApiUtil {
    static TAipNlp aipNlp = new TAipNlp(ConstUtil.APP_ID, ConstUtil.APP_KEY);
    static TAipSpeech client = new TAipSpeech(ConstUtil.APP_ID, ConstUtil.APP_KEY);


    /**
     * 文字转换为语音
     * 使用这个需要使用 base64Util 帮助类
     *
     * @param msg 要转换的文字
     * @return 文件路径
     */
    public static String getVoice(String msg) throws Exception {
        String retUrl = null;
        msg = msg.substring(msg.indexOf("说") + 1).replaceAll(" ", "");
        //参数内容
        String text = msg;//合成的文本内容
        int speaker = 7;//语音发音人编码 普通话男声 1 静琪女声 5 欢馨女声 6 碧萱女声 7
        int format = 3;//合成语音格式编码 PCM 1 WAV 2 MP3 3
        int volume = 5;//合成语音音量 取值范围[-10, 10]，如-10表示音量相对默认值小10dB，0表示默认音量，10表示音量相对默认值大10dB
        int speed = 100;// 合成语音语速，默认100
        int aht = 0;//合成语音降低/升高半音个数，即改变音高，默认0
        int apc = 58;//控制频谱翘曲的程度，改变说话人的音色，默认58
//        String result = client.TtsSynthesis(text, speaker,format );//语音合成（AI Lab） 默认参数
        String result = null;//语音合成（AI Lab） 全部参数
        result = client.TtsSynthesis(text, speaker, format, volume, speed, aht, apc);
        JSONObject jsonObject = JSON.parseObject(result);
        String speech = jsonObject.getJSONObject("data").getString("speech");
        retUrl = Base64Util.decodeBase64(speech, "C:\\Users\\sric\\Desktop\\image\\", "WAV", false);
        return retUrl;
    }

    /**
     * 翻译文字
     *
     * @param msg    需要翻译的文本
     * @param source 翻译前的语言
     * @param target 翻译后的语言
     * @return
     */
    public static String translate(String msg, String source, String target) {
        try {
            String result = aipNlp.nlpTextTranslate(msg, source, target);//文本翻译（AI Lab）
            String targetTest = JSON.parseObject(result).getJSONObject("data").getString("target_text");
            return targetTest;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "出现了错误";
    }


    String[] lang = {"zh", "en", "jp", "kr", "fr", "es", "it", "de", "tr", "ru", "pt", "vi", "id", "ms", "th"};

    /**
     * 判断文字的语言
     *
     * @param text
     * @return
     */
    public static String language(String text) {
        String result;
        try {
            int force = 0;//是否强制从候选语言中选择（只对二选一有效）
            result = aipNlp.nlpTextDetect(text, force);
            String lang = JSON.parseObject(result).getJSONObject("data").getString("lang");
            return lang;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 意图成分识别
     *
     * @param text
     * @return
     */
    @Deprecated
    public static String intention(String text) {
        //参数内容
        String result = null;//意图成分识别
        try {
            result = aipNlp.nlpWordcom(text);
            JSONObject resp = JSONObject.parseObject(result);
            if (resp.getInteger("ret")==0) {
                result = resp.getString("data");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    public static String emotion(String text) {
        String result = null;
        try {
            result = aipNlp.nlpTextpolar(text);//情感分析识别
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(intention("你家在哪里?"));
    }

}
