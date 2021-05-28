package cn.sric;

import cn.sric.common.pojo.PictureData;
import cn.sric.dao.picture.PictureDataMapper;
import cn.sric.service.common.IListApiService;
import cn.sric.service.file.IPictureFileService;
import cn.sric.util.Cat;
import cn.sric.util.ConstUtil;
import cn.sric.util.GroupUtil;
import cn.sric.util.param.SystemParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import catcode.Neko;
import love.forte.simbot.api.message.results.GroupAdmin;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Component
public class ApplicationTests {

    @Resource
    IPictureFileService pictureFileService;

    @Test
    public void contextLoads() {
        List<String> list = pictureFileService.downloadPicture(5);
        list.forEach(System.out::println);
    }


    @Test
    public void image() {
        String image = Cat.getImage(ConstUtil.FILE_URL + "123123.png", true);
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, image);
    }

    @Test
    public void getAdmin() {
//        GroupList groupList = SystemParam.msgSender.GETTER.getGroupList();
//        groupList.forEach(group -> {
//
//            });
        List<GroupAdmin> admins = SystemParam.msgSender.GETTER.getGroupInfo("555933776").getAdmins();
        admins.forEach(admin -> {
            System.out.println(admin.getAccountCode());
        });
    }

//    @Test
//    public void at() {
//        String at = Cat.at("2472560050");
//        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, at);
//    }


    @Test
    public void token() {
    }

    @Resource
    PictureDataMapper pictureDataMapper;

    @Test
    public void randomFind() {
        List<PictureData> pictureDataList = pictureDataMapper.randomFind(1, 1, null);
        pictureDataList.forEach(pictureData -> {
            System.out.println(pictureData.toString());
        });
    }

    @Resource
    IListApiService iListApiService;

    @Test
    public void Voice() {
        String voice = iListApiService.voice("张俊告诉大家我是张俊");
        Neko record = Cat.getRecord(voice);
        String url = Cat.getVoice(voice);
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, url);
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, record.toString());
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, "123123");
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, "666");
    }


    @Test
    public void music() {
        String music1 = Cat.getMusic1("422594");
        SystemParam.msgSender.SENDER.sendPrivateMsg(ConstUtil.QQ_CODE, music1);
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, music1);
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, Cat.getMusic("哈哈"));
    }


    @Resource
    RestTemplate restTemplate;

    @Test
    public void login() {
        String phone = "13783882591";
        String passWord = "sc123123";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(ConstUtil.MUSIC_URL + "/login/cellphone?phone=" + phone + "&password=" + passWord, String.class);
        JSONObject jsonObject = JSON.parseObject(forEntity.getBody());
        String cookie = jsonObject.getString("cookie");
        System.out.println(cookie);
    }


    @Test
    public void at() {
        SystemParam.msgSender.SENDER.sendGroupMsg(ConstUtil.GROUP_CODE, Cat.at("2472560050"));
    }


    @Test
    public void getAdmin2() {
        List<String> groupAdmin = GroupUtil.getGroupAdmin("555933776");
        groupAdmin.forEach(System.out::println);
    }

    @Test
    public void send() {
    }
}
