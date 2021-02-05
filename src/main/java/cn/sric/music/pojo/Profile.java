
package cn.sric.music.pojo;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Profile {

    private String description;
    private int userType;
    private boolean followed;
    private String backgroundUrl;
    private String detailDescription;
    private long userId;
    private int vipType;
    private int gender;
    private int accountStatus;
    private long avatarImgId;
    private String nickname;
    private int birthday;
    private long city;
    private long backgroundImgId;
    private String avatarUrl;
    private long province;
    private boolean defaultAvatar;
    private int djStatus;
    private Experts experts;
    private boolean mutual;
    private String remarkName;
    private String expertTags;
    private int authStatus;
    private String avatarImgIdStr;
    private String backgroundImgIdStr;
    private String signature;
    private int authority;
    private String avatarImgId_str;
    private int followeds;
    private int follows;
    private int eventCount;
    private String avatarDetail;
    private int playlistCount;
    private int playlistBeSubscribedCount;
}