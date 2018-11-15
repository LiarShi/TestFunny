package com.liar.testcall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lodz.android.core.utils.ToastUtils;

/**
 * Created by LiarShi on 2018/9/20.
 *
 * 开机启动的广播
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //  启动特定服务或者启动某个应用
        Log.e("Lionet", "APP保活接收到开机启动的广播onReceive");
        Intent startApp = context.getPackageManager().getLaunchIntentForPackage("com.liar.testcall");
        context.startActivity(startApp);
        ToastUtils.showLong(context,"接收到重启的广播");
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Intent i = new Intent(context, SplashActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(i);
//            Log.e("------------", "onReceive: 开机自启动");
//            context.startActivity(i);
//        }
    }
}
