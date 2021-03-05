package cn.sric.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/19
 * @package cn.sric.common.vo
 * @description
 **/
@Data
@Accessors(chain = true)
public class PictureRanking {

    private String qqCode;
    private Integer num;


}
