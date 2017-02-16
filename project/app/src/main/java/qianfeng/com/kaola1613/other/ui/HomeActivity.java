package qianfeng.com.kaola1613.other.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioGroup;

import qianfeng.com.kaola1613.R;
import qianfeng.com.kaola1613.discover.ui.DiscoverFragment;
import qianfeng.com.kaola1613.mine.ui.MineFragment;
import qianfeng.com.kaola1613.offline.ui.OfflineFragment;
import qianfeng.com.kaola1613.other.utils.LogUtil;
import qianfeng.com.kaola1613.other.utils.PlayerService;
import qianfeng.com.kaola1613.other.widget.ExitDialog;

/**
 *
 * 主页面
 *
 * Created by Liu Jianping on 2016/10/09.
 */
public class HomeActivity extends AppCompatActivity {

    private Fragment[] fragments;

    private RadioGroup radioGroup;

//    private KaolaSlidePanelLayout slidingPaneLayout;

    private int lastFragment;

    private NavigationView navigationView;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        radioGroup = (RadioGroup) findViewById(R.id.home_rg);

        if (fragments == null)
        {
            fragments = new Fragment[]
                    {
                            new DiscoverFragment(),
                            new MineFragment(),
                            new OfflineFragment()
                    };
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启一次事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (int i = 0; i < fragments.length; i++) {
            //把创建好的Fragment放到指定的FrameLayout里
            transaction.add(R.id.home_content_fl, fragments[i]);
            //先隐藏起来
            transaction.hide(fragments[i]);
        }

        //默认显示第一个Fragment
        transaction.show(fragments[0]);
        //提交事务
        transaction.commit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                int currFragment = 0;
                if (id == R.id.home_discover_rb)
                {
                    //显示发现
                    currFragment = 0;
                }
                else if (id == R.id.home_mine_rb)
                {
                    //显示我的
                    currFragment = 1;
                }
                else if (id == R.id.home_offline_rb)
                {
                    //显示离线
                    currFragment = 2;
                }

                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                //隐藏上一次显示的页面
                beginTransaction.hide(fragments[lastFragment]);
                beginTransaction.show(fragments[currFragment]);
                beginTransaction.commit();

                //重新记录下标
                lastFragment = currFragment;

            }
        });

//        slidingPaneLayout = (KaolaSlidePanelLayout) findViewById(R.id.home_spl);
//
//        //侧滑事件监听
//        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
//            /**
//             * 正在滑动
//             * @param panel
//             * @param slideOffset
//             */
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//
//                LogUtil.d("onPanelSlide slideOffset = " + slideOffset);
//
//                //往右滑动慢慢变小,往左滑动慢慢变大
//
//                float offset = 1 - slideOffset;
//
//                if (offset < 0.5f)
//                {
//                    offset = 0.5f;
//                }
//
//
//                float scale = offset;
//
//
////                panel.setScaleX(scale);
////                panel.setScaleY(scale);
//
//            }
//
//            /**
//             * 打开的状态回调方法
//             *
//             * @param panel
//             */
//            @Override
//            public void onPanelOpened(View panel) {
//                Toast.makeText(HomeActivity.this, "onPanelOpened", Toast.LENGTH_SHORT).show();
//            }
//
//            /**
//             * 关闭状态回调方法
//             * @param panel
//             */
//            @Override
//            public void onPanelClosed(View panel) {
//                Toast.makeText(HomeActivity.this, "onPanelClosed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        slidingPaneLayout.setInterceptListener(new KaolaSlidePanelLayout.IInterceptListener() {
//            @Override
//            public boolean isIntercept() {
//
//                //在第一个页面需要拦截，往右滑动是可以显示侧滑菜单
//                return discoverPagerIndex == 0;
//            }
//        });

        navigationView = (NavigationView) findViewById(R.id.home_nv);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                switch (id)
                {
                    case R.id.menu_shake:

                        intent = new Intent(HomeActivity.this, ShakeActivity.class);

                        break;

                    case R.id.menu_camera:
                        intent = new Intent(HomeActivity.this, CameraActivity.class);
                        break;

                    case R.id.menu_qrCode:
                        intent = new Intent(HomeActivity.this, QrCodeActivity.class);
                        break;

                    case R.id.menu_map:
                        intent = new Intent(HomeActivity.this, MapActivity.class);
                        break;

                    case R.id.menu_secret:
                        intent = new Intent(HomeActivity.this, SecretActivity.class);
                        break;
                }

                startActivity(intent);

                return true;
            }
        });

    }

    private int discoverPagerIndex;

    public void setDiscoverPagerIndex(int index)
    {
        discoverPagerIndex = index;
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    /**
     * 显示退出对话框
     */
    private void showExitDialog() {

        ExitDialog dialog = new ExitDialog(this, R.style.kaola_dialog)
        {
            @Override
            public void dialogFinish() {

                Intent intent = new Intent(HomeActivity.this, PlayerService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);

                finish();
            }
        };
        dialog.show();
    }

    private PlayerService playerService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //如果服务已经调用了onBind方法，那么会回调此方法
            LogUtil.d("HomeActivity onServiceConnected ");

            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) iBinder;

            playerService = playerBinder.getService();

            //要中止播放服务，必须获得PlayerService对象，然后才能调用PlayerService.stop();
            if (playerService != null)
            {
                playerService.stop();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //解绑那么会回调此方法
            LogUtil.d("HomeActivity onServiceDisconnected ");
        }
    };
}
