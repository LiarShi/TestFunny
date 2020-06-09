package com.liar.testcall.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by liar on 2020/4/29.
 */
public class Constants {


    /**
     * 采集输入框输入类型
     */
//    @IntDef({CltInputType.TYPE_TEXT, CltInputType.TYPE_ID_CARD, CltInputType.TYPE_PHONE, CltInputType.TYPE_NUMBER,
//            CltInputType.TYPE_NUMBER_DECIMAL, CltInputType.TYPE_FOREIGN_CERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CltInputType {
        /**
         * 输入文本
         */
        int TYPE_TEXT = 0;
        /**
         * 输入身份证
         */
        int TYPE_ID_CARD = 1;
        /**
         * 输入手机号
         */
        int TYPE_PHONE = 2;
        /**
         * 输入整型数字
         */
        int TYPE_NUMBER = 3;
        /**
         * 输入小数数字
         */
        int TYPE_NUMBER_DECIMAL = 4;
        /**
         * 外国证件
         */
        int TYPE_FOREIGN_CERT = 5;
    }

    public static final String CALL_LOOP="0";
    public static final String CALL_TIMER="1";

    public static final String START_CALL = "0";//开始拨打
    public static final String STOP_CALL = "1";//停止拨打

    public static final String OPEN_AD = "0";//停止拨打
    public static final String CLOSE_AD = "1";//停止拨打
}
