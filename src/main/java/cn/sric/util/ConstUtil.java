package cn.sric.util;


import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/11/11
 * @package cn.sric.util
 * @description
 **/
public class ConstUtil {
    /**
     * 腾讯AI智能聊天秘钥
     */
    public static final String APP_ID = "2139011227";
    public static final String APP_KEY = "SzzM2xeqSU9l7N94";


    public static final String QQ_CODE = "2472560050";
    public static final String GROUP_CODE = "555933776";
    /**
     * 时间格式
     */

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static final String PRIVATE = "private";
    public static final String GROUP = "group";


    /**
     * ApiKet
     */
    private final static String SE_API_KEY = "555966255faa07e87f8578";
    String a = "https://api.lolicon.app/setu?apikey=555966255faa07e87f8578&num=10";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(SET_URL, String.class);
        System.out.println(forEntity.getBody());
    }


    /**
     * 嘻嘻
     */
    public static String SET_URL = "https://api.lolicon.app/setu?apikey=" + SE_API_KEY + "&r18=0";
    /**
     * 随机头像URL
     */
    public final static String HEAD_URL = "https://api.66mz8.com/api/rand.portrait.php";

    /**
     * 网抑云热评
     */
    public final static String REVIEW = "https://api.66mz8.com/api/music.163.php?format=json";

    public final static String PUBLIC_URL = "https://autumnfish.cn/";

    /**
     * 标签api
     */
    public final static String TAGS = PUBLIC_URL + "playlist/highquality/tags";


    /**
     * 获取音乐歌单的URL
     *
     * @param before
     * @param limit
     * @return
     */
    public static String PLAY_LIST(String before, Integer limit) {
        if (StringUtils.isEmpty(before)) {
            return PUBLIC_URL + "top/playlist/highquality?limit=" + limit;
        } else if (StringUtils.isEmpty(limit) && limit != 0) {
            return PUBLIC_URL + "top/playlist/highquality?before=" + before;
        }
        return PUBLIC_URL + "top/playlist/highquality?before=" + before + "&limit=" + limit;
    }

    public static String GET_URL(String url, Object... param) {
        if (param.length % 2 != 0) {
            return "传入的param参数数量必须为复数";
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        if (StringUtils.isEmpty(param)) {
            return stringBuffer.toString();
        }
        stringBuffer.append("?");
        for (int i = 0; i < param.length; i++) {
            if (i == 0) {
                stringBuffer.append(param[i]);
                stringBuffer.append("=");
                stringBuffer.append(param[i + 1]);
            } else {
                stringBuffer.append("&");
                stringBuffer.append(param[i]);
                stringBuffer.append("=");
                stringBuffer.append(param[i + 1]);
            }
            i++;
        }
        return stringBuffer.toString();
    }

    /**
     * 百度
     */
    public final static String LET_ME = "https://lmbtfy.cn/s/create";

    /**
     * 百度 结果
     */
    public final static String LET_ME_RESULTS = "https://lmbtfy.cn/s/";


    /**
     * Linux图片路径
     */
    public final static String LINUX_FILE_URL = "/usr/sric/scc/images/";
    /**
     * Windows 图片路径
     */
    public final static String WINDOWS_FILE_URL = "C:/Users/sric/Desktop/image/";

    public final static String LINUX_VOICE_URL = "/usr/sric/scc/voice/";


    public final static String WINDOWS_VOICE_URL = "C:/Users/sric/Desktop/voice/";

    /**
     * 最终的路径
     */
    public final static String FILE_URL = LINUX_FILE_URL;

    /**
     * 最终的路径
     */
    public final static String VOICE_URL = LINUX_VOICE_URL;


    /**
     * 获取二维码的参数
     */
    public static final String QR_SU_GN = "c969d36d75849a6be992b2519ab0f672";
    public static final String QR_APP_KEY = "51394";
    public static String QR_URL = "http://api.k780.com/?app=qr.get&level=L&size=6&appkey=" + QR_APP_KEY + "&sugn=" + QR_SU_GN + "&data=";


    /**
     * 公共的字符串参数
     */
    public static final String XXX = "xxx";
}
