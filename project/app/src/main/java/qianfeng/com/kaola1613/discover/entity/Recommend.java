package qianfeng.com.kaola1613.discover.entity;

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
public class Recommend {


    private int contentType;
    private String relatedValue;
    private String icon;
    private int id;
    private int componentType;
    private String desc;
    private String pic;
    private int moreType;
    private int count;
    private int contentSourceId;
    private String name;
    private int hasmore;
    private List<Recommend2> dataList;

    public static Recommend objectFromData(String str) {

        return new Gson().fromJson(str, Recommend.class);
    }

    public static Recommend objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), Recommend.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Recommend> arrayRecommendFromData(String str) {

        Type listType = new TypeToken<ArrayList<Recommend>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Recommend> arrayRecommendFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Recommend>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getRelatedValue() {
        return relatedValue;
    }

    public void setRelatedValue(String relatedValue) {
        this.relatedValue = relatedValue;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getMoreType() {
        return moreType;
    }

    public void setMoreType(int moreType) {
        this.moreType = moreType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(int contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHasmore() {
        return hasmore;
    }

    public void setHasmore(int hasmore) {
        this.hasmore = hasmore;
    }

    public List<Recommend2> getDataList() {
        return dataList;
    }

    public void setDataList(List<Recommend2> dataList) {
        this.dataList = dataList;
    }

    public static class ComponentType
    {
        /**
         * 广告
         */
        public static final int TYPE_BANNER = 1;

        /**
         * 猜你喜欢的
         */
        public static final int TYPE_GUESS = 29;

        /**
         * 特别策划
         */
        public static final int TYPE_PLAN = 8;

        /**
         * 显示专辑
         */
        public static final int TYPE_PANEL = 3;

        /**
         * 快捷入口
         */
        public static final int TYPE_ENTER = 28;


    }
}
