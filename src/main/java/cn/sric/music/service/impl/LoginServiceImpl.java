package cn.sric.music.service.impl;

import cn.sric.common.pojo.ResultMessage;
import cn.sric.music.pojo.Profile;
import cn.sric.music.service.ILoginService;
import cn.sric.music.util.RedisKeyUtil;
import cn.sric.util.ConstUtil;
import cn.sric.util.OkHttp;
import cn.sric.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/2/4
 * @package cn.sric.music.service.impl
 * @description
 **/
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    RedisUtil redisUtil;

    @Override
    public ResultMessage<String> cellphone(String phone, String passWord) {
        ResultMessage<String> resultMessage = new ResultMessage<>();
        String rest = OkHttp.get(ConstUtil.MUSIC_URL + "/login/cellphone?phone=" + phone + "&password=" + passWord + ConstUtil.getTimestamp(true));
        JSONObject jsonObject = JSON.parseObject(rest);
        Integer code = jsonObject.getInteger("code");
        if (code != 200) {
            resultMessage.setCode(code).setData(rest);
        }
        return resultMessage.setCode(code).setData(rest);
    }

    @Override
    public boolean getCaptcha(String phone) {
        JSONObject rest = JSON.parseObject(OkHttp.get(ConstUtil.MUSIC_URL + "/captcha/sent?phone=" + phone + ConstUtil.getTimestamp(true)));
        Integer code = rest.getInteger("code");
        if (code == 200) {
            return true;
        }
        return false;
    }

    @Override
    public ResultMessage<String> login(String msg, String code) {
        msg = msg.replaceAll(" ", "");
        int i = msg.indexOf("密码:");
        String phone = msg.substring(msg.indexOf("手机号:") + 4, i);
        String pwd = msg.substring(i + 3);
        ResultMessage<String> message = null;
        String redisPhone = redisUtil.get("phone:" + code);
        if (!StringUtils.isEmpty(redisPhone)) {
            if (redisPhone.equals(phone)) {
                message = new ResultMessage<>();
                message.setCode(5000);
                message.setMessage("当前账号已登录");
                return message;
            }
            message = new ResultMessage<>();
            message.setCode(5000);
            message.setMessage("你已经登录过一个账号啦-->" + redisPhone + "\n" + "如果想更换登录请先发『 退出网易云 』");
            return message;
        }

        message = this.cellphone(phone, pwd);
        if (message.getCode() == 200) {
            JSONObject data = JSON.parseObject(message.getData());
            JSONObject profile = data.getJSONObject("profile");
            redisUtil.set(RedisKeyUtil.getCookie(code, phone), data.getString("cookie"));
            redisUtil.set(RedisKeyUtil.getPhone(code), phone);
            redisUtil.set(RedisKeyUtil.getPersonalDetails(code), profile.toJSONString());
            message.setMessage("昵称:『" + profile.getString("nickname") + "』 登陆成功");
        }
        return message;
    }

    @Override
    public ResultMessage<String> logout(String code) {
        String rest = OkHttp.get(ConstUtil.MUSIC_URL + "/logout" + ConstUtil.getTimestamp(false));
        if (JSON.parseObject(rest).getInteger("code").equals(200)) {
            String phone = redisUtil.get(RedisKeyUtil.getPhone(code));
            redisUtil.del(RedisKeyUtil.getCookie(code, phone));
            redisUtil.del(RedisKeyUtil.getPhone(code));
            redisUtil.del(RedisKeyUtil.getPersonalDetails(code));
            return new ResultMessage<String>().setCode(200);
        }
        return null;
    }
}
