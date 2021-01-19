package cn.sric.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/4
 * @package cn.sric.common.pojo
 * @description签到表
 **/
@Data
@Accessors(chain = true)
@TableName("`code`")
public class Code implements Serializable {

    @TableField("id")
    private long id;

    private String qqCode;

    private long groupId;

    private Integer integral;

    private int signInNumber;

    private String operationTime;

}
