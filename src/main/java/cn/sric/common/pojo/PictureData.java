
package cn.sric.common.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author sric
 */
@Data
@Accessors(chain = true)
public class PictureData implements Serializable {
    /**
     * 主键
     */
    private long id;
    /**
     * 作品 PID
     */
    private long pid;
    /**
     * 作品所在 P
     */
    private int p;
    /**
     * 作者 UID
     */
    private long uid;
    /**
     * 作品标题
     */
    private String title;
    /**
     * 作品作者
     */
    private String author;
    /**
     * 作品路径
     */
    private String url;
    /**
     * 是否r18
     */
    private int r18;
    /**
     * 原图宽度 px
     */
    private int width;
    /**
     * 原图高度 px
     */
    private int height;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 作品标签，包含标签的中文翻译（有的话）
     */
    private String tags;

    /**
     * 文件在本地的路径
     */
    private String localUrl;

}