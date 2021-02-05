/**
  * Copyright 2021 json.cn 
  */
package cn.sric.music.pojo.recommend;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class RecommendReasons {

    private long songId;
    private String reason;

}