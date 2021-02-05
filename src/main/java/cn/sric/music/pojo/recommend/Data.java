/**
 * Copyright 2021 json.cn
 */
package cn.sric.music.pojo.recommend;


import lombok.experimental.Accessors;

import java.util.List;

@lombok.Data
@Accessors(chain = true)
public class Data {
    private List<DailySongs> dailySongs;
    private List<String> orderSongs;
    private List<RecommendReasons> recommendReasons;
}