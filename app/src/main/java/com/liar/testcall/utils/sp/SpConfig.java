package com.liar.testcall.utils.sp;

/**
 * SharedPreferences配置信息
 * Created by zhouL on 2016/12/26.
 */
public class SpConfig {

    private SpConfig() {}

    /** sp文件名称 */
    public static final String SP_NAME = "sp_setting";

//---------------------------- 存储的Key -----------------------------------

    /** 是否第一次安装APP */ //
    public static final String FIRST_INSTALL = "first_install";

    /**手机号码 */ //
    public static final String PHONE_NUMBER = "phone_number";
    /**
     * 拨打方式 0循环 1定时
     */ //
    public static final String CALL_MODE = "call_mode";

    /**
     * 循环播放时间 单位分钟
     */
    public static final String CALL_LOOP_TIME = "call_loop_time";

    /**
     * 定时播放时间 单位分钟
     */
    public static final String CALL_TIMER_TIME = "call_timer_time";

    /**
     * 是否拨打 0开启 1停止
     */
    public static final String IS_CALL = "is_call";

    /**
     * 是否开启广告 0开启 1停止
     */
    public static final String IS_OPEN_AD = "IS_OPEN_AD";

}
