package qianfeng.com.kaola1613.other.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by liujianping on 2016/10/11.
 */
public abstract class CommImagePagerAdapter extends PagerAdapter{

    private List<ImageView> viewList;

    private List<?> anchor2List;

    private Context context;
    private int width, height;

    public CommImagePagerAdapter(Context context, List<ImageView> viewList, List<?> anchor2List) {
        this.viewList = viewList;
        this.context = context;
        this.anchor2List = anchor2List;
        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        int index = position % viewList.size();
        ImageView imageView = viewList.get(index);

        container.addView(imageView);

//        Anchor2 anchor2 = anchor2List.get(index);
//
//        Picasso.with(context)
//                .load(anchor2.getPic())//加载图片地址
//                .resize(width, height)//显示指定大小的图片
//                .into(imageView);//加载到指定的ImageView

        showImage(position, imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int index = position % viewList.size();
        ImageView imageView = viewList.get(index);

        container.removeView(imageView);
    }

    public abstract void showImage(int position, ImageView imageView);



}
