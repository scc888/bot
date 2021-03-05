package cn.sric.util;

import cn.sric.util.param.SystemParam;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * @author admin
 * @date 2020年08月01日 14:29
 */
@Component
public class SenderUtil {
    public static void sendGroupMsg(String fromGroup, String msg) {
        if (Cat.CONTEXT.get(fromGroup) == null) {
            Cat.CONTEXT.put(fromGroup, new String[]{"", "", "", "", msg});
            SystemParam.msgSender.SENDER.sendGroupMsg(fromGroup, msg);
            return;
        }
        String[] ret = getList(fromGroup);
        Cat.CONTEXT.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], msg});
        SystemParam.msgSender.SENDER.sendGroupMsg(fromGroup, msg);
        System.out.println();
    }

    public static void sendPrivateMsg(String qq, String msg) {
        sendPrivateMsg(qq, null, msg);
    }

    public static void sendPrivateMsg(String qq, String group, String msg) {
        try {
            SystemParam.msgSender.SENDER.sendPrivateMsg(qq, group, msg);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            SenderUtil.sendPrivateMsg(ConstUtil.QQ_CODE, "尝试给[" + qq + "]发送消息: " + msg + " 失败");
        }
    }

    public static String[] getList(String fromGroup) {
        return Cat.CONTEXT.get(fromGroup);
    }


}
