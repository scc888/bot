package cn.sric.common.enums;

import lombok.Data;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/18
 * @package cn.sric.common.enums
 * @description
 **/
public enum LangEnums {
    ZH("中文", "zh"),
    EN("英文", "en"),
    JP("日文", "jp"),
    KR("韩文", "kr"),
    FR("法文", "fr");


    private String lang;
    private String abbreviation;

    public String getLang() {
        return lang;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    LangEnums(String lang, String abbreviation) {
        this.lang = lang;
        this.abbreviation = abbreviation;
    }


    public static String getLang(String abbreviation) {
        for (LangEnums value : LangEnums.values()) {
            value.getAbbreviation().equals(abbreviation);
            return value.getLang();
        }
        return null;
    }
}
