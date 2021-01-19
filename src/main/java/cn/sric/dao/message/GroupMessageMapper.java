package cn.sric.dao.message;

import cn.sric.common.pojo.GroupMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/6
 * @package simbot.example.demo.dao
 * @description
 **/
@Mapper
public interface GroupMessageMapper extends BaseMapper<GroupMessage> {

    /**
     * 修改消息状态为撤回
     *
     * @param msgId 消息id
     * @return int
     */
    @Update("UPDATE group_message SET is_recall = 1 where msg_id=#{msgId}")
    int updateRecall(String msgId);

}
