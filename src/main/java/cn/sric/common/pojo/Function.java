package cn.sric.common.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/6/2
 * @package cn.sric.common.pojo
 * @description
 **/
@Data
@Accessors(chain = true)
public class Function {
    int id;
    String name;
    int type;
    String format;


}
