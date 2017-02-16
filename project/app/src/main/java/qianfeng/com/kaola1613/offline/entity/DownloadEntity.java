package qianfeng.com.kaola1613.offline.entity;

/**
 * 下载实体类
 *
 * Created by liujianping on 2016/10/20.
 */
public class DownloadEntity {

    private String image;

    private String albumName;

    private int count;

    private String size;

    public DownloadEntity() {
    }

    public DownloadEntity(String image, String albumName, int count, String size) {
        this.image = image;
        this.albumName = albumName;
        this.count = count;
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
