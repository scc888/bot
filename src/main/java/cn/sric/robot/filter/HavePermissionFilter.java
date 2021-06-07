package cn.sric.robot.filter;

import cn.sric.service.group.impl.AdminServiceImpl;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2021/5/31
 * @package cn.sric.robot.filter
 * @description  功能权限
 **/
public class HavePermissionFilter {
   static Map<String, Object> map = new HashMap<>();


    static {

    }

    public static void main(String[] args) {
        Arrays.stream(AdminServiceImpl.class.getDeclaredMethods()).forEach(System.out::println);
    }


}
