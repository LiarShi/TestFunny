package com.liar.testcall.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.liar.testcall.config.Constants;
import com.liar.testcall.utils.sp.SpManager;
import com.lodz.android.core.log.PrintLog;

/**
 * Created by LiarShi on 2018/11/6.
 */
public class CallService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        callPhone(SpManager.get().getPHONE_NUMBER());
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 拨打电话
     **/
    public void callPhone(String phoneNum) {
        //判断是否拨打电话
        if(SpManager.get().getIS_CALL().equals(Constants.STOP_CALL)){
            return;
        }
        //判断是否处于定时拨打电话状态
        if(SpManager.get().getCALL_MODE().equals(Constants.CALL_LOOP)){
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                PrintLog.d("callPhone", "拨打电话失败！：没有权限");
                onDestroy();
                return;
            }
            startActivity(intent);

            PrintLog.d("callPhone", "拨打电话成功！");
        }catch (Exception e) {
            PrintLog.d("callPhone", "拨打电话失败！：" + e.toString());
        }
        SpManager.get().setIS_CALL(Constants.STOP_CALL);
        onDestroy();
    }

}
