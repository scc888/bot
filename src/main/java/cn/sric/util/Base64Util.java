package cn.sric.util;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;


/**
 * base64编码解析
 *
 * @author sric
 */
public class Base64Util {
    private static final String separator = "/";

    /**
     * 获取base64字符串
     *
     * @param fileName
     * @param isSafe
     * @return
     */
    public static String encodeBase64(String fileName, boolean isSafe) {
        if (StringUtils.isEmpty(fileName)) {
            throw new NullPointerException();
        }
        InputStream in = null;
        byte[] data = null;
        String encodedText = null;
        //读取图片字节数组
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        if (isSafe) {
            java.util.Base64.Encoder encoder = java.util.Base64.getUrlEncoder();
            encodedText = encoder.encodeToString(data);
        } else {
            BASE64Encoder encoder = new BASE64Encoder();
            encodedText = encoder.encode(data);
            encodedText = encodedText.replaceAll("[\\s*\t\n\r]", "");
        }
        return encodedText;
    }

    /**
     * 解析base64
     *
     * @param base64   base64编码
     * @param filePath 文件路径
     * @param suffix   文件后缀
     * @param isSafe   是否是安全的文件
     * @return 返回文件最后生成的路径
     */
    public static String decodeBase64(String base64, String filePath, String suffix, boolean isSafe) {
        if (StringUtils.isEmpty(base64) || StringUtils.isEmpty(filePath) || StringUtils.isEmpty(suffix)) {
            throw new NullPointerException();
        }
        OutputStream out = null;
        String fileName = null;
        try {
            byte[] b;
            if (isSafe) {
                java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
                b = decoder.decode(base64);
            } else {
                BASE64Decoder decoder = new BASE64Decoder();
                b = decoder.decodeBuffer(base64.substring(base64.indexOf(",") + 1));
            }
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName = filePath + System.currentTimeMillis() + "." + suffix;
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            out.write(b);
            out.flush();
        } catch (Exception e) {

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return fileName;
    }


    public static void main(String[] args) {
        String base64 = "data:image/png;base64,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";
        String filePath = ConstUtil.WINDOWS_FILE_URL;
        String suffix = "JPG";
        String s = decodeBase64(base64, filePath, suffix, false);
        System.out.println(s);
    }
}