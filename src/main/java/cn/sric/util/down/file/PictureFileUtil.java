package cn.sric.util.down.file;

import cn.sric.util.ConstUtil;
import cn.sric.util.down.NetWorkFile;
import cn.sric.util.threadpoolutil.ThreadPoolExecutorUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sunchuanchuan
 * @version V1.0
 * @date 2020/11/18
 * @package cn.sric.util
 * @description
 **/
public class PictureFileUtil {


    public static String download(String url) {
        //下载的时候先判断本地是否存在这个文件，如果存在直接return 本地路径，否则再下载，然后返回路径
        String substringUrl = url.substring(url.lastIndexOf("/") + 1);
        // 服务器地址
        String imgUrl = (ConstUtil.FILE_URL + substringUrl);
        File file = new File(imgUrl);
        if (file.isFile()) {
            return file.getPath();
        }

        InputStream inputStream = null;
        FileOutputStream out = null;
        try {
            //将网络URL转换为流
            inputStream = NetWorkFile.sendGetForFile(url);
            if (inputStream != null) {
                out = new FileOutputStream(imgUrl);
                int j;
                while ((j = inputStream.read()) != -1) {
                    out.write(j);
                }
                out.flush();
                return imgUrl;
            }
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static String download(String url, String localUrl) {
        InputStream inputStream = null;
        FileOutputStream out = null;
        try {
            //将网络URL转换为流
            inputStream = NetWorkFile.sendGetForFile(url);
            if (inputStream != null) {
                out = new FileOutputStream(localUrl);
                int j;
                while ((j = inputStream.read()) != -1) {
                    out.write(j);
                }
                out.flush();
                return localUrl;
            }
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }





}
