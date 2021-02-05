package cn.sric.music.util;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/5
 * @package cn.sric.music.util
 * @description
 **/
public class RedisKeyUtil {

    public static String getCookie(String code, String phone) {
        return "cookie:" + code + ":" + phone;
    }

    public static String getPhone(String code) {
        return "phone:" + code;
    }

    /**
     * 获取个人信息
     *
     * @param code QQ号
     * @return
     */
    public static String getPersonalDetails(String code) {
        return "personalDetails:" + code;
    }
}
