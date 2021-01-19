package cn.sric.util;

import java.util.regex.Pattern;

/**
 * @author sric
 * 判断数字
 */
public class IsNumberUtil {


    private static Pattern DOUBLE_PATTERN = Pattern.compile("^\\d+\\.\\d+$");
    private static Pattern INTEGER_PATTERN = Pattern.compile("[0-9]*");

    /**
     * 判断是否为正整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return DOUBLE_PATTERN.matcher(str).matches();
    }

    /**
     * 判断是否为小数
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        return INTEGER_PATTERN.matcher(str).matches();
    }


}
