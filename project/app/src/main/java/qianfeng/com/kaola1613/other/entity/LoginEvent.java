package qianfeng.com.kaola1613.other.entity;

/**
 * Created by liujianping on 2016/11/1.
 */
public class LoginEvent {

    private String nickName;
    private String headUrl;

    public LoginEvent(String nickName, String headUrl) {
        this.nickName = nickName;
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
