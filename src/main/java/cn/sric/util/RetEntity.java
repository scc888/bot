package cn.sric.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.Date;

@Data
@Accessors(chain = true)
public final class RetEntity<T extends Object> {

    @JSONField(ordinal = 1)
    private int code;
    @JSONField(ordinal = 2)
    private boolean status;
    @JSONField(ordinal = 3)
    private String message;

    @JSONField(ordinal = 4)
    private T body;
    @JSONField(ordinal = 5)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date timestamp;

    public RetEntity() {
    }


    //region # ERROR
    public static RetEntity error() {
        return error(null);
    }

    public static RetEntity error(String message) {
        return error(null, message);
    }

    public static RetEntity error(Integer code, String message) {
        if (code == null) {
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        if (message == null) {
            message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return build(code, false, message);
    }
    //endregion

    //region # OK
    public static RetEntity ok() {
        return ok(null);
    }

    public static RetEntity ok(String message) {
        return ok(null, message);
    }

    public static RetEntity ok(Integer code, String message) {
        if (code == null) {
            code = HttpStatus.OK.value();
        }
        if (message == null) {
            message = HttpStatus.OK.getReasonPhrase();
        }
        return build(code, true, message);
    }


    public static RetEntity build(int code, boolean status, String message) {
        return new RetEntity()
                .setCode(code)
                .setStatus(status)
                .setMessage(message)
                .setTimestamp(new Date());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, true);
    }

    public static RetEntity convert(String jsonString) throws IllegalArgumentException {
        Assert.hasLength(jsonString, "jsonString is null or empty");
        try {
            return JSON.parseObject(jsonString, RetEntity.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("jsonString did not comply with the format");
        }
    }

}
