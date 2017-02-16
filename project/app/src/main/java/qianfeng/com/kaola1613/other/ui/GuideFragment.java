package qianfeng.com.kaola1613.other.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import qianfeng.com.kaola1613.R;

/**
 * Created by liujianping on 2016/10/9.
 */
public class GuideFragment extends Fragment {

    private VideoView videoView;

    private ImageView ivLeft, ivRight, ivCover;

    private int videoId, ivLeftId, ivRightId, ivCoverId;

    private int position;

    public GuideFragment() {
    }

    @SuppressLint("ValidFragment")
    public GuideFragment(int position, int videoId, int ivLeftId, int ivRightId, int ivCoverId) {
        this.ivCoverId = ivCoverId;
        this.position = position;
        this.videoId = videoId;
        this.ivLeftId = ivLeftId;
        this.ivRightId = ivRightId;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.e("GuideFragment", position + "setUserVisibleHint isVisibleToUser = " + isVisibleToUser);

        if (videoView == null)
        {
            return;
        }
        if (isVisibleToUser)
        {
            showImages();
        }
        else
        {
            hideImages();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.w("GuideFragment", position + " onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("GuideFragment", position + " onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_guide, null);

        Log.w("GuideFragment", position + " onCreateView");
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.w("GuideFragment", position + " onViewCreated");
        videoView = (VideoView) view.findViewById(R.id.guide_vv);
        ivLeft = (ImageView) view.findViewById(R.id.guide_left_iv);
        ivRight = (ImageView) view.findViewById(R.id.guide_right_iv);
        ivCover = (ImageView) view.findViewById(R.id.guide_cover);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w("GuideFragment", position + " onActivityCreated");
        //显示对应的内容
        ivLeft.setImageResource(ivLeftId);
        ivRight.setImageResource(ivRightId);
        ivCover.setImageResource(ivCoverId);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        //设置视频播放路径
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoId);
//        Uri uri = Uri.parse("rtsp://58.248.254.8/tvlive/gdty-l.sdp");
//        Uri uri = Uri.parse("rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp");
        videoView.setVideoURI(uri);
        //开始播放
        videoView.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w("GuideFragment", position + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.w("GuideFragment", position + " onResume");
        if (getUserVisibleHint())
        {
            if (position == 0)
            {
                showImages();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("GuideFragment", position + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("GuideFragment", position + " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("GuideFragment", position + " onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w("GuideFragment", position + " onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w("GuideFragment", position + " onDetach");
    }

    public void hideImages()
    {
        ivCover.setVisibility(View.VISIBLE);
        ivLeft.clearAnimation();
        ivRight.clearAnimation();
        videoView.pause();
    }

    public void showImages()
    {
        //加载指定的动画
        Animation rightAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_guide_right);
        final Animation leftAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_guide_left);

        rightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在右边的动画执行完后，执行左边的动画
                ivLeft.startAnimation(leftAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //先执行右边的动画
        ivRight.startAnimation(rightAnim);

        ivCover.setVisibility(View.GONE);
        videoView.start();

    }
}
