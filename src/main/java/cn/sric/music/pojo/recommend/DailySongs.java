/**
 * Copyright 2021 json.cn
 */
package cn.sric.music.pojo.recommend;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DailySongs {

    /**
     * 音乐名
     */
    private String name;
    /**
     * 音乐id
     */
    private long id;

    private Integer pst;
    private Integer t;
    private List<Ar> ar;
    private List<String> alia;
    private Integer pop;
    private Integer st;
    private String rt;
    private Integer fee;
    private Integer v;
    private String crbt;
    private String cf;
    private Al al;
    private long dt;
    private H h;
    private M m;
    private L l;
    private String a;
    private String cd;
    private Integer no;
    private String rtUrl;
    private Integer ftype;
    private List<String> rtUrls;
    private Integer djId;
    private Integer copyright;
    private Integer s_id;
    private long mark;
    private Integer originCoverType;
    private String originSongSimpleData;
    private Integer single;
    private String noCopyrightRcmd;
    private Integer cp;
    private Integer mv;
    private Integer rtype;
    private String rurl;
    private Integer mst;
    private Long publishTime;
    //根据喜欢的推荐的
    private String reason;
    private Privilege privilege;
    private String alg;

}