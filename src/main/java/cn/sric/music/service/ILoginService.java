package cn.sric.music.service;

import cn.sric.common.pojo.ResultMessage;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/4
 * @package cn.sric.music
 * @description
 **/
public interface ILoginService {

    ResultMessage<String> cellphone(String phone, String passWord);


    boolean getCaptcha(String phone);


    ResultMessage<String> login(String msg, String code);


    ResultMessage<String> logout(String code);
}
