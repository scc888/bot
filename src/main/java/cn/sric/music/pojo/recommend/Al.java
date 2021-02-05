/**
 * Copyright 2021 json.cn
 */
package cn.sric.music.pojo.recommend;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Al {
    private long id;
    private String name;
    private String picUrl;
    private List<String> tns;
    private String pic_str;
    private long pic;
}