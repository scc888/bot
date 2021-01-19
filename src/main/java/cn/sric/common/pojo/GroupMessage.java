package cn.sric.common.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/6
 * @package simbot.example.demo.pojo
 * @description
 **/
@Data
@Accessors(chain = true)
public class GroupMessage {
    private Long id;
    private String qqCode;
    private String msgId;
    private String groupCode;
    private String message;
    private Date time;
    private Integer isRecall;
    private String recipient;
}
