package cn.sric.util;

import cn.sric.util.param.SystemParam;
import love.forte.simbot.api.message.results.*;
import love.forte.simbot.api.sender.Sender;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/1/4
 * @package cn.sric.util
 * @description
 **/
public class GroupUtil {


    /**
     * 获取群里面的群主和管理员但是不包括自己
     *
     * @param qqGroup
     * @return
     */
    public static List<String> getGroupAdmin(String qqGroup) {
        GroupFullInfo groupInfo = SystemParam.msgSender.GETTER.getGroupInfo(qqGroup);
        List<GroupAdmin> admins = groupInfo.getAdmins();
        List<String> list = new ArrayList<>();
        admins.forEach(admin -> {
            list.add(admin.getAccountCode());
        });
        return list;
    }


    public static boolean isAdmin(String qqGroup, String qq) {
        GroupMemberInfo memberInfo = SystemParam.msgSender.GETTER.getMemberInfo(qqGroup, qq);
        return memberInfo.getPermission().isOwnerOrAdmin();
    }


}
