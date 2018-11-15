package com.liar.testcall.utils.service;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by LiarShi on 2018/9/26.
 */
public class ServiceUtils {


    //私有化ServiceUtils，保证isServiceRunning方法的唯一性（因为isServiceRunning为静态）
    private ServiceUtils() {
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param context 上下文
     *
     * @param serviceName  是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     *
     * @return true代表正在运行，false代表服务没有正在运行
     *
     * */

    public static boolean isServiceRunning(Context context, String serviceName) {


        if (("").equals(serviceName) || serviceName == null){
            return false;
        }
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(myManager==null){
            return false;
        }

        ArrayList<ActivityManager.RunningServiceInfo> runningService=(ArrayList<ActivityManager.RunningServiceInfo>)
                myManager.getRunningServices(9999);//设置最大数量9999

        if (runningService.size() <= 0) {
            //当前没有服务
            return false;
        }

        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName()
                    .equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
