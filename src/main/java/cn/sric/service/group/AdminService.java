package cn.sric.service.group;

import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.apache.ibatis.annotations.Update;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/2
 * @package cn.sric.service
 * @description
 **/
public interface AdminService {
    /**
     * 计算分钟数为几天几时几分
     *
     * @param time 分钟数
     * @return 返回字符串结果
     */
    String bannedTime(long time);

    /**
     * 设置禁言套餐
     *
     * @param groupMsg
     * @param sender
     */
    void getThePackage(GroupMsg groupMsg, MsgSender sender);



    int updateRecall(String msgId);
}
