/**
  * Copyright 2021 json.cn 
  */
package cn.sric.music.pojo.recommend;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class ChargeInfoList {

    private long rate;
    private String chargeUrl;
    private String chargeMessage;
    private int chargeType;

}