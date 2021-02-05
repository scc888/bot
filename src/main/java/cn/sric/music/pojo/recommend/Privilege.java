/**
  * Copyright 2021 json.cn 
  */
package cn.sric.music.pojo.recommend;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Auto-generated: 2021-02-05 14:55:53
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
@Accessors(chain = true)
public class Privilege {

    private long id;
    private int fee;
    private int payed;
    private int st;
    private long pl;
    private long dl;
    private int sp;
    private int cp;
    private int subp;
    private boolean cs;
    private long maxbr;
    private long fl;
    private boolean toast;
    private int flag;
    private boolean preSell;
    private long playMaxbr;
    private long downloadMaxbr;
    private FreeTrialPrivilege freeTrialPrivilege;
    private List<ChargeInfoList> chargeInfoList;

}