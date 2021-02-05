/**
 * Copyright 2021 json.cn
 */
package cn.sric.music.pojo.recommend;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Ar {

    private long id;
    //歌手名
    private String name;
    private List<String> tns;
    private List<String> alias;


}