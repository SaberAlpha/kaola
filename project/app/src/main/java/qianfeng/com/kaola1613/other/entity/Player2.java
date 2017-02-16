package qianfeng.com.kaola1613.other.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujianping on 2016/10/24.
 */
public class Player2 {


    private long audioId;
    private String audioName;
    private Object audioPic;
    private String audioDes;
    private long albumId;
    private String albumPic;
    private String albumName;
    private int orderNum;
    private String playUrl;
    private String mp3PlayUrl;
    private String aacPlayUrl;
    private String m3u8PlayUrl;
    private String shareUrl;
    private int categoryId;
    private int fileSize;
    private int aacFileSize;
    private int mp3FileSize;
    private int isStored;
    private String updateTime;
    private long createTime;
    private String clockId;
    private int duration;
    private int originalDuration;
    private int mp3Duration;
    private int listenNum;
    private int likedNum;
    private int islistened;
    private int hasCopyright;
    private int type;
    private String tips;
    private int commentNum;
    private int uploaderId;
    private String uploaderName;
    private String uploaderImg;
    private int isReward;
    private int trailerStart;
    private int trailerEnd;
    private List<?> host;

    public static Player2 objectFromData(String str) {

        return new Gson().fromJson(str, Player2.class);
    }

    public static Player2 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), Player2.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Player2> arrayPlayer2FromData(String str) {

        Type listType = new TypeToken<ArrayList<Player2>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Player2> arrayPlayer2FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Player2>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public long getAudioId() {
        return audioId;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public Object getAudioPic() {
        return audioPic;
    }

    public void setAudioPic(Object audioPic) {
        this.audioPic = audioPic;
    }

    public String getAudioDes() {
        return audioDes;
    }

    public void setAudioDes(String audioDes) {
        this.audioDes = audioDes;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getMp3PlayUrl() {
        return mp3PlayUrl;
    }

    public void setMp3PlayUrl(String mp3PlayUrl) {
        this.mp3PlayUrl = mp3PlayUrl;
    }

    public String getAacPlayUrl() {
        return aacPlayUrl;
    }

    public void setAacPlayUrl(String aacPlayUrl) {
        this.aacPlayUrl = aacPlayUrl;
    }

    public String getM3u8PlayUrl() {
        return m3u8PlayUrl;
    }

    public void setM3u8PlayUrl(String m3u8PlayUrl) {
        this.m3u8PlayUrl = m3u8PlayUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getAacFileSize() {
        return aacFileSize;
    }

    public void setAacFileSize(int aacFileSize) {
        this.aacFileSize = aacFileSize;
    }

    public int getMp3FileSize() {
        return mp3FileSize;
    }

    public void setMp3FileSize(int mp3FileSize) {
        this.mp3FileSize = mp3FileSize;
    }

    public int getIsStored() {
        return isStored;
    }

    public void setIsStored(int isStored) {
        this.isStored = isStored;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getClockId() {
        return clockId;
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getOriginalDuration() {
        return originalDuration;
    }

    public void setOriginalDuration(int originalDuration) {
        this.originalDuration = originalDuration;
    }

    public int getMp3Duration() {
        return mp3Duration;
    }

    public void setMp3Duration(int mp3Duration) {
        this.mp3Duration = mp3Duration;
    }

    public int getListenNum() {
        return listenNum;
    }

    public void setListenNum(int listenNum) {
        this.listenNum = listenNum;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public int getIslistened() {
        return islistened;
    }

    public void setIslistened(int islistened) {
        this.islistened = islistened;
    }

    public int getHasCopyright() {
        return hasCopyright;
    }

    public void setHasCopyright(int hasCopyright) {
        this.hasCopyright = hasCopyright;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(int uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploaderImg() {
        return uploaderImg;
    }

    public void setUploaderImg(String uploaderImg) {
        this.uploaderImg = uploaderImg;
    }

    public int getIsReward() {
        return isReward;
    }

    public void setIsReward(int isReward) {
        this.isReward = isReward;
    }

    public int getTrailerStart() {
        return trailerStart;
    }

    public void setTrailerStart(int trailerStart) {
        this.trailerStart = trailerStart;
    }

    public int getTrailerEnd() {
        return trailerEnd;
    }

    public void setTrailerEnd(int trailerEnd) {
        this.trailerEnd = trailerEnd;
    }

    public List<?> getHost() {
        return host;
    }

    public void setHost(List<?> host) {
        this.host = host;
    }
}
