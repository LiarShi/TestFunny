package com.liar.testcall.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.liar.testcall.App;
import com.liar.testcall.R;
import com.liar.testcall.utils.crash.CrashHandler;
import com.liar.testcall.utils.file.FileManager;
import com.liar.testcall.utils.set.OpenAutoStartUtil;
import com.liar.testcall.utils.sp.SpManager;
import com.lodz.android.component.base.activity.BaseActivity;
import com.lodz.android.core.cache.ACacheUtils;
import com.lodz.android.core.utils.AppUtils;
import com.lodz.android.core.utils.ToastUtils;
import com.lodz.android.core.utils.UiHandler;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import zlc.season.rxdownload3.core.DownloadConfig;

@RuntimePermissions
public class SplashActivity extends BaseActivity {


    @Override
    protected void findViews(Bundle savedInstanceState) {
        showStatusCompleted();
        goneTitleBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
        UiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){// 6.0以上的手机对权限进行动态申请
                    SplashActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(SplashActivity.this);//申请权限
                }else {
                    init();
                }
            }
        }, 1000);
    }


    /** 权限申请成功 */
    @NeedsPermission({
            Manifest.permission.CALL_PHONE,// 打电话
            Manifest.permission.READ_PHONE_STATE,// 读取手机信息
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡写
            Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读
    })

    protected void requestPermission() {
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.CALL_PHONE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_PHONE_STATE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            return;
        }
        init();
    }

    /** 被拒绝 */
    @OnPermissionDenied({
            Manifest.permission.CALL_PHONE,// 打电话
            Manifest.permission.READ_PHONE_STATE,// 读取手机信息
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡写
            Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读
    })
    protected void onDenied() {
        SplashActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(this);//申请权限
    }
    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale({
            Manifest.permission.CALL_PHONE,// 打电话
            Manifest.permission.READ_PHONE_STATE,// 读取手机信息
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡写
            Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读
    })

    protected void showRationaleBeforeRequest(PermissionRequest request) {
        request.proceed();//请求权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain({
            Manifest.permission.CALL_PHONE,// 打电话
            Manifest.permission.READ_PHONE_STATE,// 读取手机信息
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡写
            Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读

    })
    protected void onNeverAskAgain() {
        ToastUtils.showShort(getContext(), R.string.splash_check_permission_tips);
        showPermissionCheckDialog();
        AppUtils.jumpAppDetailSetting(this);
    }

    /** 显示权限核对弹框 */
    private void showPermissionCheckDialog() {
        CheckDialog dialog = new CheckDialog(getContext());
        dialog.setContentMsg(R.string.splash_check_permission_title);
        dialog.setPositiveText(R.string.splash_check_permission_confirm, new CheckDialog.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                SplashActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(SplashActivity.this);//申请权限
                dialog.dismiss();
            }
        });
        dialog.setNegativeText(R.string.splash_check_permission_unconfirmed, new CheckDialog.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                AppUtils.jumpAppDetailSetting(getContext());
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ToastUtils.showShort(getContext(), R.string.splash_check_permission_cancel);
                App.get().exit();
            }
        });
        dialog.show();
    }

    private void init() {
        FileManager.init();// 初始化文件管理
        initCrashHandler();//初始化异常处理
        initACache();// 初始化缓存类
        initRxDownload(getContext());/** 初始化下载器 */
    }



    /** 初始化下载器 */
    private void initRxDownload(Context context) {
        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(context.getApplicationContext())
                .setFps(20) //设置更新频率
                .setMaxMission(2)//设置同时下载数量
                .enableAutoStart(false) //自动开始下载
                .setDefaultPath(FileManager.getDownloadFolderPath()) //设置默认的下载地址
                .enableDb(true) //启用数据库
                .enableService(true) //启用Service
                .enableNotification(true); //启用Notification
        DownloadConfig.INSTANCE.init(builder);
    }

    /** 初始化异常处理 */
    private void initCrashHandler() {
        CrashHandler.get()
                .setLauncherClass(SplashActivity.class)// 如果不设置重启的Activity，闪退后就直接退出应用
                .setInterceptor(true)
//                .setToastTips("嗝屁啦")
                .setSaveFolderPath(FileManager.getCrashFolderPath())
//                .setLogFileName("heheda.log")
                .init();
    }

    /** 初始化缓存类 */
    private void initACache() {
        ACacheUtils.get().newBuilder()
                .setCacheDir(FileManager.getCacheFolderPath())
//                .setMaxSize(1024 * 1024 * 50)
//                .setMaxCount(Integer.MAX_VALUE)
                .build(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_PHONE_STATE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            return;
        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.CALL_PHONE)){
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){// 8.0以上的手机申请进入白名单
            ignoreBatteryOptimization(this);
        }
//        if (SpManager.get().getFIRST_INSTALL().length()==0){
//            //如果长度为0，是第一次启动APP
//            showIsOpenDialog();
//        }else {
//            MainActivity.start(getContext());
//            finish();
//
//        }
        ChoseActivty.start(getContext());
        finish();
//        SystemProperties.set("sys.boot_completed", "1");0默认 1可用 2禁止 3user disable
//        ComponentName mComponentName = new ComponentName("com.snxun.fjyrydzgk","com.snxun.fjyrydzgk.receiver.BootBroadcastReceiver");
//        int a = getPackageManager().getComponentEnabledSetting(mComponentName);
        //反射方式获取SystemProperties
//        Class<?> cls = ReflectUtils.getClassForName("android.os.SystemProperties");
//        Object obj = ReflectUtils.getObject(cls);
//        ReflectUtils.executeFunction(cls, obj, "set", new Class[]{String.class, String.class}, new String[]{"sys.boot_completed", "1"})==null
//        if(){
//            //如果为null，获取自启动权限失败
//            showIsOpenDialog();
//        }else {
//            getValidate();
//        }

    }


    /** 提示是否开机启动 */
    private void showIsOpenDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("该手机设置没有开启启动权限，请设置");
        //点击空白区域Dialog不会消失
        dialog.setCancelable(false);
        dialog.setNeutralButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SpManager.get().setFIRST_INSTALL("是第一次");
                        OpenAutoStartUtil.jumpStartInterface(getContext());

                    }
                });
        dialog.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                finish();
            }
        } );
        dialog.show();
    }


    /** 加入电池优化的白名单 */
    public void ignoreBatteryOptimization(Activity activity) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean hasIgnored = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
        }

        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if(!hasIgnored) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+activity.getPackageName()));
            startActivity(intent);
        }
    }
}
