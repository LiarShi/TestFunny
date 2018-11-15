package com.liar.testcall.utils.set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.lodz.android.core.log.PrintLog;

/**
 * Created by Administrator on 2018/9/20.
 * 开机启动权限的工具类
 */
public class OpenAutoStartUtil {


    public static String HUAWEI_PKG ="com.huawei.systemmanager";

    /**
     * Get Mobile Type
     *
     * @return
     */
    private static String getMobileType() {

        return Build.MANUFACTURER==null?"":Build.MANUFACTURER;
    }

    /**
     * GoTo Open Self Setting Layout
     * Compatible Mainstream Models 兼容市面主流机型
     *
     * @param context
     */
    public static void jumpStartInterface(Context context) {

        PrintLog.e("OpenAutoStartUtil", "******************当前手机型号为：" + getMobileType());
        Intent intent =new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        String mobileType=getMobileType()
                .toLowerCase();//转小写

        ComponentName comp =null;

        if (!TextUtils.isEmpty(mobileType)) {

            if ("honor".equals(mobileType) ||"huawei".equals(mobileType)) {
                if (Build.VERSION.SDK_INT >=26){
                    comp =new ComponentName(HUAWEI_PKG,"com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity");
                }else if (Build.VERSION.SDK_INT >=23){
                    comp =new ComponentName(HUAWEI_PKG,"com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                }else {
                    comp =new ComponentName(HUAWEI_PKG,"com.huawei.systemmanager.com.huawei.permissionmanager.ui.MainActivity");
                }
            }else if ("xiaomi".equals(mobileType)) {
                comp =new ComponentName("com.miui.securitycenter","com.miui.permcenter.autostart.AutoStartManagementActivity");
            }else if ("vivo".equals(mobileType)) {
                if (Build.VERSION.SDK_INT >=26) {
                    comp =new ComponentName("com.vivo.permissionmanager","com.vivo.permissionmanager.activity.PurviewTabActivity");
                    }else {
                    comp =new ComponentName("com.iqoo.secure","com.iqoo.secure.ui.phoneoptimize.SoftwareManagerActivity");
                }
            }else if ("oppo".equals(mobileType)) {
                if (Build.VERSION.SDK_INT >=26){
                    comp =new ComponentName("com.coloros.safecenter","com.coloros.safecenter.startupapp.StartupAppListActivity");
                }else {
                    comp =new ComponentName("com.color.safecenter","com.color.safecenter.permission.startup.StartupAppListActivity");
                }
            }else if ("samsung".equals(mobileType)) {
                comp =new ComponentName("com.samsung.android.sm_cn","com.samsung.android.sm_cn.com.samsung.android.sm.ui.ram.AutoRunActivity");
            }
        }
        //其他机型直接跳设置界面，适配的几个厂商中有不同系列的手机，可能会跳不到指定界面，因此需要在异常时调到普通设置界面做保护

        try{
            if (comp ==null){
                intent=new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }else {
                intent.setComponent(comp);
                context.startActivity(intent);
            }
        }catch (Exception e){//抛出异常就直接打开设置页面
            PrintLog.e("OpenAutoStartUtil" ,"******************获取开机自启界面异常："+e);
            intent=new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }

    }
}
