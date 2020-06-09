package com.liar.testcall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.liar.testcall.R;
import com.lodz.android.component.base.activity.BaseActivity;
import com.lodz.android.core.utils.UiHandler;

import java.util.Timer;
import java.util.TimerTask;

/** 广告 页面*/
public class AdvertisementActivity extends BaseActivity {

    //广告总时长
    private long adTime=5;
    //更新UI循环时长
    private long duration = 1000L;

    private Timer timer;

    private TextView tv_ad;

    public static void start(Context context) {
        Intent starter = new Intent(context, AdvertisementActivity.class);

        //清空要启动的activity所在的task栈
//        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        //在Application启动Activity，要设置FLAG_ACTIVITY_NEW_TASK
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }


    @Override
    protected void findViews(Bundle savedInstanceState) {
        showStatusCompleted();
        goneTitleBar();
        tv_ad= findViewById(R.id.tv_ad);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();

        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                UiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adTime=adTime-1;
                        tv_ad.setText("广告-"+adTime+"秒");
                    }
                }, 0);
            }
        }, 1000, duration);

        UiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                ChoseActivty.start(getContext());
                finish();
            }
        }, adTime*1000);
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
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
