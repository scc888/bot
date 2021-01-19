package cn.sric.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/3
 * @package cn.sric.common.pojo
 * @description
 **/
@Data
@Accessors(chain = true)
@TableName(value = "`group`")
public class QqGroup implements Serializable {

    @TableField("`id`")
    private long id;
    /**
     * 群号
     */
    private String qqGroupCode;
    /**
     * 状态是否可以进行操作
     * 0 可以进行
     * -1 不可以
     */
    private Integer status;
    /**
     * 加入时间
     */
    private String createTime;
    /**
     * 当前的权限
     * 1 群主
     * 0 管理员
     * -1 平民
     */
    private int powerType;

}
