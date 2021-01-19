package cn.sric.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @author sric
 */
@Data
@Accessors(chain = true)
public class PictureDto {
    /**
     * 返回编码
     */
    private int code;
    /**
     * 错误信息之类的
     */
    private String msg;
    /**
     * 剩余调用额度
     */
    private int quota;
    /**
     * 距离下一次调用额度恢复(+1)的秒数
     */
    private int quota_min_ttl;
    /**
     * 结果数
     */
    private int count;
    /**
     * 数组
     */
    private List<PictureDataDto> data;
}