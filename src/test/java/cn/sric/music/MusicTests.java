package cn.sric.music;

import cn.sric.Application;
import cn.sric.util.Cat;
import love.forte.simbot.api.sender.MsgSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MusicTests {
    @Resource
    MsgSender msgSender;

    @Test
    public void music() {
        msgSender.SENDER.sendGroupMsg("981599975", Cat.at("2472560050"));
    }


}
