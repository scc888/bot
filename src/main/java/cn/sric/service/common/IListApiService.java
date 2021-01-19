package cn.sric.service.common;

import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/11/20
 * @package cn.sric.service
 * @description
 **/
public interface IListApiService {

    /**
     * tx聊天
     *
     * @param city
     * @return
     */
    String gossip(String city);

    /**
     * 网抑云热评
     *
     * @return
     */
    Map<String, String> review();

    /**
     * 百度
     *
     * @param val
     * @return
     */
    String letMeBaiDu(String val);


    /**
     * 获取二维码
     *
     * @param keyWord
     * @return
     */
    String findQr(String keyWord);


    /**
     * 将消息转换为语音
     *
     * @param msg
     * @return 语音文件路径
     */
    String voice(String msg);



    String translate(String msg);

}
