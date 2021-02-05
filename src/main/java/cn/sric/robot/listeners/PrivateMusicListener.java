package cn.sric.robot.listeners;

import cn.sric.common.pojo.ResultMessage;
import cn.sric.music.pojo.recommend.Music;
import cn.sric.music.service.ILoginService;
import cn.sric.music.service.IMusicService;
import cn.sric.util.ConstUtil;
import cn.sric.util.OkHttp;
import cn.sric.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listener;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
@Listener(priority = 3)
public class PrivateMusicListener {


    @Resource
    ILoginService iLoginService;

    @Resource
    IMusicService iMusicService;


    @OnPrivate
    @Filter(value = "登录网易云", matchType = MatchType.STARTS_WITH)
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
    @Filter(value = "退出网易云", matchType = MatchType.EQUALS)
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
    @Filter(value = "今日推荐音乐", matchType = MatchType.EQUALS)
    public void recommend(PrivateMsg privateMsg, Sender sender) throws InterruptedException {
        String code = privateMsg.getAccountInfo().getAccountCode();
        List<Map<String, Object>> recommend = iMusicService.getRecommend(code);
        Integer i = 0;
        for (Map<String, Object> map : recommend) {
            Object name = map.get("name");
            sender.sendPrivateMsg(privateMsg, (++i) + ":歌名:『" + name + "』\n" + "歌手:" + map.get("singer") + "\n" + map.get("reason") + "\n" + "音乐id" + map.get("id"));
        }
        Thread.sleep(1000);
    }


}
