package qianfeng.com.kaola1613.discover.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujianping on 2016/10/14.
 */
public class Recommend2 implements Parcelable{

    private String weburl;
    private String albumName;
    private long rid;
    private int num;
    private String tip;
    private String rvalue;
    private String rname;
    private String mp3PlayUrl;
    private Object area;
    private int listenNum;
    private int rtype;
    private String adId;
    private String pic;
    private int adType;
    private String adUserId;
    private String des;
    private int cornerMark;
    private int followedNum;
    private List<?> host;
    private List<?> expoUrl;
    private List<?> reportUrl;

    public Recommend2()
    {

    }

    protected Recommend2(Parcel in) {
        weburl = in.readString();
        albumName = in.readString();
        rid = in.readLong();
        num = in.readInt();
        tip = in.readString();
        rvalue = in.readString();
        rname = in.readString();
        mp3PlayUrl = in.readString();
        listenNum = in.readInt();
        rtype = in.readInt();
        adId = in.readString();
        pic = in.readString();
        adType = in.readInt();
        adUserId = in.readString();
        des = in.readString();
        cornerMark = in.readInt();
        followedNum = in.readInt();
    }

    public static final Creator<Recommend2> CREATOR = new Creator<Recommend2>() {
        @Override
        public Recommend2 createFromParcel(Parcel in) {
            return new Recommend2(in);
        }

        @Override
        public Recommend2[] newArray(int size) {
            return new Recommend2[size];
        }
    };

    public static Recommend2 objectFromData(String str) {

        return new Gson().fromJson(str, Recommend2.class);
    }

    public static Recommend2 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), Recommend2.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Recommend2> arrayRecommend2FromData(String str) {

        Type listType = new TypeToken<ArrayList<Recommend2>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Recommend2> arrayRecommend2FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Recommend2>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getRvalue() {
        return rvalue;
    }

    public void setRvalue(String rvalue) {
        this.rvalue = rvalue;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getMp3PlayUrl() {
        return mp3PlayUrl;
    }

    public void setMp3PlayUrl(String mp3PlayUrl) {
        this.mp3PlayUrl = mp3PlayUrl;
    }

    public Object getArea() {
        return area;
    }

    public void setArea(Object area) {
        this.area = area;
    }

    public int getListenNum() {
        return listenNum;
    }

    public void setListenNum(int listenNum) {
        this.listenNum = listenNum;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(String adUserId) {
        this.adUserId = adUserId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCornerMark() {
        return cornerMark;
    }

    public void setCornerMark(int cornerMark) {
        this.cornerMark = cornerMark;
    }

    public int getFollowedNum() {
        return followedNum;
    }

    public void setFollowedNum(int followedNum) {
        this.followedNum = followedNum;
    }

    public List<?> getHost() {
        return host;
    }

    public void setHost(List<?> host) {
        this.host = host;
    }

    public List<?> getExpoUrl() {
        return expoUrl;
    }

    public void setExpoUrl(List<?> expoUrl) {
        this.expoUrl = expoUrl;
    }

    public List<?> getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(List<?> reportUrl) {
        this.reportUrl = reportUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(weburl);
        parcel.writeString(albumName);
        parcel.writeLong(rid);
        parcel.writeInt(num);
        parcel.writeString(tip);
        parcel.writeString(rvalue);
        parcel.writeString(rname);
        parcel.writeString(mp3PlayUrl);
        parcel.writeInt(listenNum);
        parcel.writeInt(rtype);
        parcel.writeString(adId);
        parcel.writeString(pic);
        parcel.writeInt(adType);
        parcel.writeString(adUserId);
        parcel.writeString(des);
        parcel.writeInt(cornerMark);
        parcel.writeInt(followedNum);
    }

    public static class RType
    {
        public static final int TYPE_PLAYER0 = 0;
        public static final int TYPE_PLAYER1 = 1;
        public static final int TYPE_PLAYER2_1 = 3;
        public static final int TYPE_PLAYER2_2 = 11;
        public static final int TYPE_WEB = 6;

    }
}
