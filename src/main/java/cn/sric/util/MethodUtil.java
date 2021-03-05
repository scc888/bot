package cn.sric.util;

import java.time.LocalDateTime;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/19
 * @package cn.sric.util
 * @description
 **/
public class MethodUtil {

    public static String getLocalDateTime() {
        return LocalDateTime.now().format(ConstUtil.DATE_TIME_FORMATTER);
    }


    public static String getTimestamp(boolean isAnd) {
        return isAnd ? "&timestamp=" + System.currentTimeMillis() : "?timestamp=" + System.currentTimeMillis();
    }
}
