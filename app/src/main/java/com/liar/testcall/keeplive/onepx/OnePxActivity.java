package com.liar.testcall.keeplive.onepx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.liar.testcall.service.MainService;
import com.lodz.android.core.log.PrintLog;

import java.lang.ref.WeakReference;

/**
 * Created by LiarShi on 2018/9/26.
 * 一像素界面
 *
 */

public class OnePxActivity extends Activity {
    private static final String TAG = "OnePxActivity";
    private static  WeakReference<OnePxActivity> instance ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate(): savedInstanceState = [" + savedInstanceState + "]");

        Log.e(TAG, "一像素界面启动:[" + savedInstanceState + "]");
        instance = new WeakReference<>(this);
        Window window = getWindow();
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        attributes.height = 1;
        attributes.width = 1;
        window.setAttributes(attributes);

    }

    public static WeakReference<OnePxActivity> getInstance(){
        return instance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
        starService();
        if (isScreenOn()) {
            finishSelf();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        Log.e(TAG, "一像素界面启动销毁");
        starService();
        if (instance != null && instance.get() == this) {
            instance = null;
        }
    }

    public void finishSelf() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.onTouchEvent(motionEvent);
    }

    private boolean isScreenOn() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                return powerManager.isInteractive();
            } else {
                return powerManager.isScreenOn();
            }
        } catch (Exception e) {
            Log.e(TAG, "e:", e);
        }
        return false;
    }

    /**
     * 开启服务
     **/
    public void starService() {

        PrintLog.e(TAG, "在一像素界面开启MainService");
        Intent service = new Intent(getApplication(), MainService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        }else {
            startService(service);
        }
    }

}