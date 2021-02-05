package cn.sric;

import cn.sric.util.param.SystemParam;
import lombok.extern.slf4j.Slf4j;
import love.forte.common.configuration.Configuration;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotContext;
import love.forte.simbot.core.SimbotProcess;
import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author sric
 */
@SpringBootApplication
@SimbotApplication
@EnableSimbot
@MapperScan("cn.sric.dao")
@Slf4j
public class Application implements SimbotProcess {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void post(@NotNull SimbotContext context) {
        Bot defaultBot = context.getBotManager().getDefaultBot();
        SystemParam.strCurrentQQ = defaultBot.getBotInfo().getBotCode();
        SystemParam.msgSender = defaultBot.getSender();
        SystemParam.strCurrentQQName = defaultBot.getBotInfo().getBotName();
    }

    @Override
    public void pre(@NotNull Configuration config) {
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
