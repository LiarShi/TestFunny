package com.liar.testcall.keeplive.onepx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.liar.testcall.App;


/**
 * Created by LiarShi on 2018/9/26.
 * 监听锁屏广播
 * 锁屏开启一像素界面，屏幕亮起销毁界面
 *
 */


public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private CheckTopTask mCheckTopTask = new CheckTopTask(App.get());
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "onReceive(): context = [" + context + "], intent = [" + intent + "]");
        String action = intent.getAction();
        // 这里可以启动一些服务
        try {
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                Log.e(TAG, "锁屏开启一像素");
                CheckTopTask.startForeground(context);
                mHandler.postDelayed(mCheckTopTask, 3000);
            } else if ("android.intent.action.USER_PRESENT".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
                Log.e(TAG, "开屏关闭一像素");
                OnePxActivity onePxActivity = OnePxActivity.getInstance() != null ? OnePxActivity.getInstance().get() : null;
                if (onePxActivity != null) {
                    onePxActivity.finishSelf();
                }
                mHandler.removeCallbacks(mCheckTopTask);
            }
        } catch (Exception e) {
            Log.e(TAG, "e:", e);
        }
    }
}