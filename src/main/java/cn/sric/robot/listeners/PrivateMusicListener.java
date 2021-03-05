package cn.sric.robot.listeners;

import cn.sric.common.pojo.ResultMessage;
import cn.sric.music.service.ILoginService;
import cn.sric.music.service.IMusicService;
import cn.sric.util.Cat;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.Listener;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/4
 * @package cn.sric.robot.listeners
 * @description
 **/
@Component
@Slf4j
public class PrivateMusicListener {


    @Resource
    ILoginService iLoginService;

    @Resource
    IMusicService iMusicService;


    @OnPrivate
    @Filters(value = {@Filter(value = "登录网易云", matchType = MatchType.STARTS_WITH)}, customFilter = {"privatePrintLog"})
    public void refreshPicture(PrivateMsg privateMsg, Sender sender) {
        String code = privateMsg.getAccountInfo().getAccountCode();
        ResultMessage<String> login = iLoginService.login(privateMsg.getMsg(), code);
        if (login.getCode() == 5000) {
            sender.sendPrivateMsg(privateMsg, login.getMessage());
            return;
        }
        if (login.getCode() == 200) {
            sender.sendPrivateMsg(privateMsg, login.getMessage());
        }
    }

    @OnPrivate
    @Filters(value = {@Filter(value = "退出网易云", matchType = MatchType.EQUALS)}, customFilter = {"privatePrintLog"})
    public void logout(PrivateMsg privateMsg, Sender sender) {
        String code = privateMsg.getAccountInfo().getAccountCode();
        ResultMessage<String> logout = null;
        try {
            logout = iLoginService.logout(code);
        } catch (Exception e) {
            sender.sendPrivateMsg(privateMsg, "成功退出");
        }
        if (logout.getCode().equals(200)) {
            sender.sendPrivateMsg(privateMsg, "成功退出");
        }
    }

    @OnPrivate
    @Filters(value = {@Filter(value = "今日推荐音乐", matchType = MatchType.EQUALS)}, customFilter = {"privatePrintLog"})
    public void recommend(PrivateMsg privateMsg, Sender sender) throws InterruptedException {
        String code = privateMsg.getAccountInfo().getAccountCode();
        ResultMessage<?> recommend = iMusicService.getRecommend(code);
        List<Map<String, Object>> mapList = JSON.parseObject(JSON.toJSONString(recommend.getData()), List.class);
        if (recommend.getCode() == 200) {
            Integer i = 0;
            for (Map<String, Object> map : mapList) {
                Object name = map.get("name");
                sender.sendPrivateMsg(privateMsg, (++i) + ":歌名:『" + name + "』\n" + "歌手:" + map.get("singer") + "\n" + map.get("reason") + "\n" + "音乐id" + map.get("id"));
            }
        } else if (recommend.getCode() == 301) {
            sender.sendPrivateMsg(privateMsg, "需要登录");
            sender.sendPrivateMsg(privateMsg, "登录格式---『" + "登录网易云手机号:13888888888密码:123456" + "』");
        }
        Thread.sleep(1000);
    }

//    @OnPrivate
//    @Filters(value = {@Filter(value = "网易云ID", matchType = MatchType.STARTS_WITH)}, customFilter = {"privatePrintLog"})
//    public void getMusicById(PrivateMsg privateMsg, Sender sender) {
//    }
}
