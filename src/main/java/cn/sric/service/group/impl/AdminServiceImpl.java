package cn.sric.service.group.impl;

import cn.hutool.core.util.RandomUtil;
import cn.sric.service.group.AdminService;
import cn.sric.util.Cat;
import cn.sric.util.GroupUtil;
import cn.sric.util.IsNumberUtil;
import cn.sric.util.param.SystemParam;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/12/2
 * @package cn.sric.service.single.impl
 * @description
 **/
@Service
public class AdminServiceImpl implements AdminService {


    @Override
    public void getThePackage(GroupMsg groupMsg, MsgSender sender) {
        //当前群账号
        String groupId = groupMsg.getGroupInfo().getGroupCode();
        //发送人的QQ
        String qqCode = groupMsg.getAccountInfo().getAccountCode();
        String patter = "^领取套餐.*?";
        String chinese = "[(\\d+|\\-|*|/|%|<<|>>\\d)]*";
        //发送的信息
        String msg = groupMsg.getMsg();
        //禁闭时长
        long duration = 0;
        //随机数1——10
        long ran = RandomUtil.randomInt(1, 10);
        //返回的消息
        String message = "";

        boolean isMatch = Pattern.matches(patter, groupMsg.getMsg());
        if (isMatch) {
            Boolean isAdmin = this.isAdmin(groupId, qqCode);
            if (isAdmin) {
                if (RandomUtil.randomInt(0, 100) == 6) {
                    sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qqCode) + "管理员领你" + Cat.emoji(128052) + "套餐呢¿");
                } else {
                    sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qqCode) + "你能不能领取套餐你心里没点13数嘛？哼~");
                }
                return;
            }
            boolean meIsAdmin = meIsAdmin(groupId, SystemParam.strCurrentQQ);
            if (!meIsAdmin) {
                sender.SENDER.sendGroupMsg(groupId, Cat.at(qqCode) + "我的当前的权限还不能禁言 (T.T)");
                return;
            }
            String result = msg.substring(msg.indexOf("餐") + 1).trim();
            if (result.length() == 0) {
                duration = ran;
                message = "想冷静又不知道冷静多久？那就大发慈悲的赏你" + duration + "分钟吧 不要谢谢我哟~";
                sender.SETTER.setGroupBan(groupId, qqCode, duration * 60);
                sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qqCode) + message);
                return;
            }
            //判断领取套餐后的内容是否为数字或者带小数点的数字
            if (IsNumberUtil.isInteger(result) || IsNumberUtil.isDouble(result)) {
                double num = Double.parseDouble(result);
                if (num < 1) {
                    duration = 1;
                    message = "别这么小气，都不大于1呢，我就大发慈悲给你凑个整好啦!";
                } else {
                    duration = (int) num;
                    message = bannedTime(duration);
                }
            } else {
                //匹配包含数字和运算符的内容，否则机器人无法看懂
                if (Pattern.matches(chinese, result)) {
                    //防止只有运算符而没有数字的运算，或者多个运算符 例如：1+++++1 等
                    try {
                        // 计算方法
                        duration = Long.parseLong(eval(result).toString());
                        if (duration < 0) {
                            duration = Math.abs(duration);
                            message = "-" + duration + "？" + "负数？那怎么行呢？已经为您贴心的转为正数了呢~";
                        } else if (duration == 0) {
                            duration = ran;
                            message = "0？这可不行呢！就随便赏你" + duration + "分钟吧";
                        } else {
                            message = bannedTime(duration);
                        }
                    } catch (Exception e) {
                        duration = ran;
                        message = "完全看不懂你在说什么呢？就让你去小黑屋待会儿吧~ 嘻嘻";
                    }
                } else {
                    duration = ran;
                    message = "完全看不懂你在说什么呢？就让你去小黑屋待会儿吧~ 嘻嘻";
                }
            }
        }
        sender.SETTER.setGroupBan(groupId, qqCode, duration * 60);
        sender.SENDER.sendGroupMsg(groupMsg, Cat.at(qqCode) + message);
    }

    @Override
    public int updateRecall(String msgId) {
        return 0;
    }


    @Override
    public String bannedTime(long t) {
        String result = "";
        long day = t / (24 * 60);
        long hour = (t % (24 * 60)) / 60;
        long minute = (t % (24 * 60)) % 60;
        if (day >= 30) {
            result = "这？这都" + day + "天了！上限30天呢，有一句话叫做不作死就不会死，等下没人给你解除看你找谁哭鼻子去！嘻嘻";
        } else if (day >= 1) {
            result = "哦哦，" + day + "天" + hour + "小时" + minute + "分钟" + "是吧？" + "兄跌有对自己有点恨呢！下次不要这样了好不好？萌萌新看着都怪开心的呢~ (偷笑)";
        } else if (hour >= 1) {
            result = "噢噢，" + hour + "小时" + minute + "分钟" + "是吧？" + "那就满足你这个大大的要求吧~ 要好好的面壁思过啦！";

        } else {
            result = "嗯嗯，" + minute + "分钟" + "是吧？" + "那就满足你这个小小的要求吧~ 要好好的反省自己哦！";
        }
        return result;
    }

    /**
     * 判断是否为管理员
     *
     * @param groupQQ 群号
     * @param qq
     * @return 返回是否为管理员
     */
    private boolean isAdmin(String groupQQ, String qq) {
        boolean flag;
        //获取管理员列表但不包括自己
        List<String> groupAdmin = GroupUtil.getGroupAdmin(groupQQ);
        flag = groupAdmin.contains(qq);
        return flag;
    }

    private boolean meIsAdmin(String groupQQ, String qq) {
        return GroupUtil.isAdmin(groupQQ, qq);
    }


    public static Object eval(String str) throws ScriptException {
        //创建一个js脚本执行器
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
        //脚本执行并返回结果
        Object eval = se.eval(str);
        return eval;
    }

    public static void main(String[] args) throws ScriptException {
        System.out.println(Long.parseLong(eval("undefined").toString()));
    }
}
