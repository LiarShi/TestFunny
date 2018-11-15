package com.liar.testcall.utils.sp;

/**
 * SharedPreferences管理类
 * Created by zhouL on 2016/12/26.
 */
public class SpManager {

    private static SpManager mInstance = new SpManager();

    public static SpManager get() {
        return mInstance;
    }

    private SpManager() {}


    /**
     * 设置是否第一次安装
     * @param isFirst
     */
    public void setFIRST_INSTALL(String isFirst){
        SharedPreferencesUtils.putString(SpConfig.FIRST_INSTALL, isFirst);
    }

    /** 获取是否第一次安装 */
    public String getFIRST_INSTALL(){
        return SharedPreferencesUtils.getString(SpConfig.FIRST_INSTALL, "");

    }


    /**
     * 设置手机号码
     * @param phone
     */
    public void setPHONE_NUMBER(String phone){
        SharedPreferencesUtils.putString(SpConfig.PHONE_NUMBER, phone);
    }

    /** 获取手机号码 */
    public String getPHONE_NUMBER(){
        return SharedPreferencesUtils.getString(SpConfig.PHONE_NUMBER, "");

    }

    /**
     * 设置拨打方式
     * @param mode 0循环 1定时
     */
    public void setCALL_MODE(String mode){
        SharedPreferencesUtils.putString(SpConfig.CALL_MODE, mode);
    }

    /** 获取拨打方式 */
    public String getCALL_MODE(){
        return SharedPreferencesUtils.getString(SpConfig.CALL_MODE, "");

    }

    /**
     * 设置循环拨打时间
     * @param loopTime
     */
    public void setCALL_LOOP_TIME(String loopTime){
        SharedPreferencesUtils.putString(SpConfig.CALL_LOOP_TIME, loopTime);
    }

    /** 获取循环拨打时间 */
    public String getCALL_LOOP_TIME(){
        return SharedPreferencesUtils.getString(SpConfig.CALL_LOOP_TIME, "");

    }

    /**
     * 设置定时拨打时间
     * @param time
     */
    public void setCALL_TIMER_TIME(String time){
        SharedPreferencesUtils.putString(SpConfig.CALL_TIMER_TIME, time);
    }

    /** 获取定时拨打时间 */
    public String getCALL_TIMER_TIME(){
        return SharedPreferencesUtils.getString(SpConfig.CALL_TIMER_TIME, "");

    }



    /**
     * 设置是否拨打 0拨打 1停止
     * @param isCall
     */
    public void setIS_CALL(String isCall){
        SharedPreferencesUtils.putString(SpConfig.IS_CALL, isCall);
    }

    /** 获取是否拨打状态 */
    public String getIS_CALL(){
        return SharedPreferencesUtils.getString(SpConfig.IS_CALL, "");

    }
}
