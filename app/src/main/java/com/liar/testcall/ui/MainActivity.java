package com.liar.testcall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.liar.testcall.R;
import com.liar.testcall.service.MainService;
import com.liar.testcall.utils.clock.AlarmManagerUtil;
import com.liar.testcall.utils.sp.SpManager;
import com.lodz.android.component.base.activity.BaseActivity;
import com.lodz.android.core.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity {


    public TextView tv_msg;//信息
    public Button btn_set;//设置
    public Button btn_start;//开始
    public Button btn_stop;//停止
    public Button btn_save;//保存
    public Button btn_cancel;//取消

    public EditText ed_phone;//手机号码
    public EditText edit_loop;//循环时间 分钟
    public TextView edit_timer;//定时时间 分钟
    public RadioButton radio_loop;//循环拨打按钮
    public RadioButton radio_timer;//定时拨打按钮

    public LinearLayout line_set;//属性设置布局

    public TextView tv_timer;

    public static final String CALL_LOOP="0";
    public static final String CALL_TIMER="1";

    public static final String START_CALL = "0";//开始拨打
    public static final String STOP_CALL = "1";//停止拨打

    private TimePickerView pvTime;


    private String callName="";

    /**
     * 主服务
     **/
    private Intent mainService;



    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_msg = findViewById(R.id.tv_msg);
        btn_set = findViewById(R.id.btn_set);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        ed_phone = findViewById(R.id.ed_phone);
        edit_loop = findViewById(R.id.edit_loop);
        edit_timer = findViewById(R.id.edit_timer);
        radio_loop = findViewById(R.id.radio_loop);
        radio_timer = findViewById(R.id.radio_timer);
        line_set = findViewById(R.id.line_set);

        tv_timer = findViewById(R.id.tv_timer);

        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                edit_timer.setText(getTime(date));
            }
        });

        showMsg();
        setViewListeners();


    }



    public void setViewListeners() {
        super.setListeners();

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //展示设置布局
                showMsg();
                line_set.setVisibility(View.VISIBLE);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消设置
                line_set.setVisibility(View.GONE);
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(SpManager.get().getPHONE_NUMBER().length()==0){
                    ToastUtils.showLong(getContext(),"拨打手机号码为空");
                    return;
                }
                if(TextUtils.isEmpty(SpManager.get().getCALL_MODE())){
                    ToastUtils.showLong(getContext(),"没有设置拨打方式");
                    return;

                }
                if(SpManager.get().getCALL_MODE().equalsIgnoreCase(CALL_TIMER)){
                    //定时拨打
                    if(TextUtils.isEmpty(SpManager.get().getCALL_TIMER_TIME())){
                        ToastUtils.showLong(getContext(),"没有设置定时拨打时间");
                        return;
                    }
                    starTimerCall();
                    SpManager.get().setIS_CALL(START_CALL);
                    ToastUtils.showLong(getContext(),"开始");
                    showMsg();
                    return;
                }

                if(SpManager.get().getCALL_MODE().equalsIgnoreCase(CALL_LOOP)){
                    //循环拨打
                    if(TextUtils.isEmpty(SpManager.get().getCALL_LOOP_TIME())){
                        ToastUtils.showLong(getContext(),"没有设置循环拨打时间");
                        return;
                    }
                    starLoopCall();
                    SpManager.get().setIS_CALL(START_CALL);
                    ToastUtils.showLong(getContext(),"开始");
                    showMsg();
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLong(getContext(),"停止");
                SpManager.get().setIS_CALL(STOP_CALL);
                showMsg();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpManager.get().setPHONE_NUMBER(ed_phone.getText().toString());
                SpManager.get().setCALL_LOOP_TIME(edit_loop.getText().toString());
                SpManager.get().setCALL_TIMER_TIME(edit_timer.getText().toString());
                line_set.setVisibility(View.GONE);
                showMsg();

            }
        });

        radio_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置为循环拨打
                SpManager.get().setCALL_MODE(CALL_LOOP);
                showMsg();
            }
        });
        radio_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置为定时拨打
                SpManager.get().setCALL_MODE(CALL_TIMER);
                showMsg();
            }
        });

        tv_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     *展示手机设置数据
     **/
    public void showMsg(){

        if(SpManager.get().getCALL_MODE().equalsIgnoreCase(CALL_LOOP)){
            callName="循环拨打";
            radio_loop.setChecked(true);
            radio_timer.setChecked(false);
        }else if(SpManager.get().getCALL_MODE().equalsIgnoreCase(CALL_TIMER)){
            callName="定时拨打";
            radio_timer.setChecked(true);
            radio_loop.setChecked(false);
        }else {
            callName="没有设置拨打方式";
            radio_timer.setChecked(false);
            radio_loop.setChecked(false);
        }
        String isCall="";
        if(SpManager.get().getIS_CALL().equals("0")){
            isCall="正在执行";
        }else{
            isCall="已经停止";
        }

        tv_msg.setText("当前拨打方式设置："+callName+"； \n"
                +"当前拨打状态："+isCall+"； \n"
                +"拨打号码："+SpManager.get().getPHONE_NUMBER()+"； \n"
                +"循环拨打间隔时间："+SpManager.get().getCALL_LOOP_TIME()+"分钟； \n"
                +"定时拨打时间："+SpManager.get().getCALL_TIMER_TIME()+"分钟\n");
        ed_phone.setText(SpManager.get().getPHONE_NUMBER());
        edit_loop.setText(SpManager.get().getCALL_LOOP_TIME());
        edit_timer.setText(SpManager.get().getCALL_TIMER_TIME());

    }
    /**
     * 开启循环拨打电话服务
     **/
    public void starLoopCall() {
        mainService = new Intent(getApplication(), MainService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mainService);
        }else {
            startService(mainService);
        }
    }

    /**
     * 关闭循环拨打电话服务
     **/
    public void stopLoopCall() {
        if(mainService!=null){
            stopService(mainService);
        }
    }



    /**
     * 开启定时拨打电话
     **/
    public void starTimerCall() {
        String[] times = SpManager.get().getCALL_TIMER_TIME().split(":");
        //只响一次的闹钟,拨打一次电话
        AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                    (times[1]), 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMsg();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
