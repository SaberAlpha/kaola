package qianfeng.com.kaola1613.other.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujianping on 2016/10/23.
 */
public class Comment {


    private int commentId;
    private long resourceId;
    private int resourceType;
    private int reviewerUid;
    private String userImg;
    private String userName;
    private String content;
    private int status;
    private String createTime;
    private String updateTime;
    private String reviewerName;
    private Object replyCommentId;
    private Object replyedUid;
    private Object replyedName;
    private int praiseNum;
    private int contentType;
    private String resourceName;
    private String utime;
    private int isPraise;
    private Object replyedStatus;
    private Object replyedContent;
    private int replyedType;
    private String backgroundImg;
    private String comeFrom;
    private Object comeFromId;
    private int resourceStatus;
    private int replyedReplyedUid;
    private String replyedReplyedName;
    private Object sendTime;
    private int uploaderId;

    public static Comment objectFromData(String str) {

        return new Gson().fromJson(str, Comment.class);
    }

    public static Comment objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), Comment.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Comment> arrayCommentFromData(String str) {

        Type listType = new TypeToken<ArrayList<Comment>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Comment> arrayCommentFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Comment>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public int getReviewerUid() {
        return reviewerUid;
    }

    public void setReviewerUid(int reviewerUid) {
        this.reviewerUid = reviewerUid;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Object getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Object replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public Object getReplyedUid() {
        return replyedUid;
    }

    public void setReplyedUid(Object replyedUid) {
        this.replyedUid = replyedUid;
    }

    public Object getReplyedName() {
        return replyedName;
    }

    public void setReplyedName(Object replyedName) {
        this.replyedName = replyedName;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public Object getReplyedStatus() {
        return replyedStatus;
    }

    public void setReplyedStatus(Object replyedStatus) {
        this.replyedStatus = replyedStatus;
    }

    public Object getReplyedContent() {
        return replyedContent;
    }

    public void setReplyedContent(Object replyedContent) {
        this.replyedContent = replyedContent;
    }

    public int getReplyedType() {
        return replyedType;
    }

    public void setReplyedType(int replyedType) {
        this.replyedType = replyedType;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Object getComeFromId() {
        return comeFromId;
    }

    public void setComeFromId(Object comeFromId) {
        this.comeFromId = comeFromId;
    }

    public int getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(int resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public int getReplyedReplyedUid() {
        return replyedReplyedUid;
    }

    public void setReplyedReplyedUid(int replyedReplyedUid) {
        this.replyedReplyedUid = replyedReplyedUid;
    }

    public String getReplyedReplyedName() {
        return replyedReplyedName;
    }

    public void setReplyedReplyedName(String replyedReplyedName) {
        this.replyedReplyedName = replyedReplyedName;
    }

    public Object getSendTime() {
        return sendTime;
    }

    public void setSendTime(Object sendTime) {
        this.sendTime = sendTime;
    }

    public int getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(int uploaderId) {
        this.uploaderId = uploaderId;
    }
}
