package cn.sric.task;

import cn.sric.common.vo.PictureRanking;
import cn.sric.dao.message.GroupMessageMapper;
import cn.sric.util.Cat;
import cn.sric.util.param.SystemParam;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.results.GroupList;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author sric
 */
@Slf4j
@Component
@EnableScheduling
public class GroupTask {

    @Resource
    GroupMessageMapper groupMessageMapper;


    @Scheduled(cron = "0 30 23 * * ? ")
    public void addGroupMsg() throws InterruptedException {
        GroupList groupList = SystemParam.msgSender.GETTER.getGroupList();
        List<PictureRanking> pictureRanKing = groupMessageMapper.findPictureRanKing("981599975");
        pictureRanKing.sort(Comparator.comparing(PictureRanking::getNum).reversed());
        PictureRanking first = pictureRanKing.get(0);
        SystemParam.msgSender.SENDER.sendGroupMsg("981599975", Cat.at(first.getQqCode()) + "-->恭喜这个b获得今日的色p 冠军,总共要了" + first.getNum() + "张");
    }


    public static void main(String[] args) {
        List<PictureRanking> list = new ArrayList<>();
        PictureRanking pictureRanking = new PictureRanking();
        pictureRanking.setQqCode("1").setNum(5);
        list.add(pictureRanking);
        PictureRanking pictureRanking1 = new PictureRanking();
        pictureRanking1.setQqCode("2").setNum(2);
        list.add(pictureRanking1);
        PictureRanking pictureRanking3 = new PictureRanking();
        pictureRanking3.setQqCode("4").setNum(8);
        list.add(pictureRanking3);
        PictureRanking pictureRanking2 = new PictureRanking();
        pictureRanking2.setQqCode("3").setNum(8);
        list.add(pictureRanking2);

        list.forEach(System.out::println);
        list.sort(Comparator.comparing(PictureRanking::getNum).reversed());
        System.out.println("-------------------------------------------------------------");
        list.forEach(System.out::println);
    }
}
