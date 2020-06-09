package com.liar.testcall;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.multidex.MultiDex;

import com.liar.testcall.config.Constants;
import com.liar.testcall.config.NotifiConfig;
import com.liar.testcall.keeplive.onepx.ScreenReceiver;
import com.liar.testcall.ui.AdvertisementActivity;
import com.liar.testcall.utils.ForegroundCallbacks;
import com.liar.testcall.utils.sp.SpManager;
import com.lodz.android.component.base.application.BaseApplication;
import com.lodz.android.core.cache.ACacheUtils;
import com.lodz.android.core.log.PrintLog;
import com.lodz.android.core.network.NetworkManager;
import com.lodz.android.core.utils.DensityUtils;
import com.lodz.android.core.utils.NotificationUtils;
import com.lodz.android.core.utils.UiHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/8/20.
 */
public class App extends BaseApplication {


    private boolean isShowAD;
    private Timer timer;
    //广告总时长
    private long adTime=60;
    //广告总时长
    private long currentTime=0;
    //更新UI循环时长
    private long duration = 1000L;

    public static App getInstance() {
        return (App) get();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //多dex加载机制
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initAppStatusListener();
    }

    @Override
    protected void afterCreate() {
        PrintLog.setPrint(BuildConfig.LOG_DEBUG);// 配置日志开关
        NetworkManager.get().init(this);// 初始化网络管理
        initACache();
        configBaseLayout();
        configScreenReceiver();// 配置监听锁屏广播
        initNotificationChannel();// 配置通知通道

    }
    /** 初始化缓存类 */
    private void initACache() {
        ACacheUtils.get().newBuilder()
                .setCacheDir(this.getApplicationContext().getCacheDir().getAbsolutePath())
                .build(this);
    }

    /** 配置基类 */
    private void configBaseLayout() {
        configTitleBarLayout();
        configErrorLayout();
        configLoadingLayout();
        configNoDataLayout();
    }

    /** 配置无数据 */
    private void configNoDataLayout() {
        getBaseLayoutConfig().getNoDataLayoutConfig().setOrientation(LinearLayout.HORIZONTAL);
        getBaseLayoutConfig().getNoDataLayoutConfig().setNeedImg(true);
        getBaseLayoutConfig().getNoDataLayoutConfig().setNeedTips(false);

    }

    /** 配置加载页 */
    private void configLoadingLayout() {
        getBaseLayoutConfig().getLoadingLayoutConfig().setOrientation(LinearLayout.HORIZONTAL);
        getBaseLayoutConfig().getLoadingLayoutConfig().setNeedTips(true);

        getBaseLayoutConfig().getLoadingLayoutConfig().setIsIndeterminate(true);

        getBaseLayoutConfig().getLoadingLayoutConfig().setPbWidth(DensityUtils.dp2px(this, 50));
        getBaseLayoutConfig().getLoadingLayoutConfig().setPbHeight(DensityUtils.dp2px(this, 50));
    }

    /** 配置标题栏 */
    private void configTitleBarLayout() {
        getBaseLayoutConfig().getTitleBarLayoutConfig().setNeedBackButton(true);

        getBaseLayoutConfig().getTitleBarLayoutConfig().setBackgroundColor(R.color.color_00a0e9);

        getBaseLayoutConfig().getTitleBarLayoutConfig().setTitleTextColor(R.color.white);

    }

    /** 配置错误页 */
    private void configErrorLayout() {
        getBaseLayoutConfig().getErrorLayoutConfig().setOrientation(LinearLayout.VERTICAL);
        getBaseLayoutConfig().getErrorLayoutConfig().setNeedTips(false);
        getBaseLayoutConfig().getErrorLayoutConfig().setNeedImg(true);

    }


    /** 配置监听锁屏广播 */
    private void configScreenReceiver() {

        // 动态注册开关屏广播
        BroadcastReceiver receiver = new ScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(receiver, intentFilter);
    }


    /** 初始化通知通道  8.0系统需要创建通道*/
    private void initNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannelGroup group = new NotificationChannelGroup(NotifiConfig.NOTIFI_GROUP_ID, "通知组");
            NotificationUtils.create(this).createNotificationChannelGroup(group);// 设置通知组
            List<NotificationChannel> channels = new ArrayList<>();
            channels.add(getMainChannel());
            NotificationUtils.create(this).createNotificationChannels(channels);// 设置频道
        }
    }


    /** 获取主通道 */
    private NotificationChannel getMainChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifiConfig.NOTIFI_CHANNEL_MAIN_ID, "学习园地通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);// 开启指示灯，如果设备有的话。
            channel.setLightColor(Color.GREEN);// 设置指示灯颜色
            channel.setDescription("学习园地通知频道");// 通道描述
            channel.enableVibration(true);// 开启震动
            channel.setVibrationPattern(new long[]{100, 200, 400, 300, 100});// 设置震动频率
            channel.setGroup(NotifiConfig.NOTIFI_GROUP_ID);
            channel.canBypassDnd();// 检测是否绕过免打扰模式
            channel.setBypassDnd(true);// 设置绕过免打扰模式
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.canShowBadge();// 检测是否显示角标
            channel.setShowBadge(true);// 设置是否显示角标
            return channel;
        }
        return null;
    }


    @Override
    protected void beforeExit() {

        UiHandler.destroy();
        NetworkManager.get().release(this);// 释放网络管理资源
        NetworkManager.get().clearNetworkListener();// 清除所有网络监听器
        System.exit(0);// 退出整个应用
    }


    /** 前台后台切换监听 */
    private void initAppStatusListener() {
        ForegroundCallbacks.init(this).addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                Log.e("App",".initAppStatusListener.onBecameForeground():已切换到前台。");
                if(SpManager.get().getIS_OPEN_AD().equals(Constants.OPEN_AD)) {
                    //广告开启
                    if(isShowAD){
                        AdvertisementActivity.start(get());
                    }
                    stopTimer();
                    isShowAD=false;
                }
            }

            @Override
            public void onBecameBackground() {
                Log.e("App",".initAppStatusListener.onBecameForeground():已切换到后台。");

                if(SpManager.get().getIS_OPEN_AD().equals(Constants.OPEN_AD)) {
                    //广告开启
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            currentTime = currentTime + 1;
                            Log.e("App", "切换到后台第" + currentTime + "秒。");
                            if (currentTime >= adTime) {
                                Log.e("App", "切换后台时间大于等于弹广告的时间，计时停止");
                                isShowAD = true;
                                stopTimer();
                            }
                        }
                    }, 1000, duration);
                }

            }
        });
    }

    /**
     * 停止计时
     */
    private void stopTimer() {
        Log.e("App","stopTimer()触发");
        if (timer != null) {
            Log.e("App","timer不为空，停止计时");
            timer.cancel();
            timer.purge();
            timer = null;
        }else {
            Log.e("App","timer为空，无需停止计时");
        }
        currentTime=0;
    }


}
