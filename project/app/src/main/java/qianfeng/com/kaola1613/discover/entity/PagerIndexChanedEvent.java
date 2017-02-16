package qianfeng.com.kaola1613.discover.entity;

/**
 * Created by liujianping on 2016/10/29.
 */
public class PagerIndexChanedEvent {

    private int currIndex;

    public PagerIndexChanedEvent(int currIndex) {
        this.currIndex = currIndex;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }
}
