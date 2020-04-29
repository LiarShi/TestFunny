package com.liar.testcall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.liar.testcall.R;
import com.liar.testcall.wigdet.CltEditTextView;
import com.liar.testcall.wigdet.CltMultiEditTextView;
import com.lodz.android.component.base.activity.BaseActivity;
import com.lodz.android.core.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择页
 * Created by liar on 2020/4/29.
 */
public class YxhscActivty extends BaseActivity {

    public String TAG = "YxhscActivty";

    @BindView(R.id.ed_zt)
    CltEditTextView ed_zt;//主体
    @BindView(R.id.ed_sj)
    CltEditTextView ed_sj;//事件
    @BindView(R.id.ed_lyzsf)
    CltEditTextView ed_lyzsf;//另一种说法
    @BindView(R.id.ed_scnr)
    CltMultiEditTextView ed_scnr;//主体

    @BindView(R.id.btn_sc)
    Button btn_sc;//生成


    public static void start(Context context) {
        Intent starter = new Intent(context, YxhscActivty.class);
        context.startActivity(starter);
    }


    private void getIntentData() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.ac_yxhcq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getIntentData();
        initTitleBar();
        setView();
        showStatusCompleted();
//        ed_scnr.setViewRead();
    }

    private void initTitleBar() {
        getTitleBarLayout().setTitleName("营销号生成器");
        getTitleBarLayout().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getTitleBarLayout().setElevation(0);
        }
    }

    private void setView() {

    }


    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void setListeners() {
        super.setListeners();

        btn_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_zt.getContentText())) {
                    ToastUtils.showShort(getContext(), "主体内容不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(ed_sj.getContentText())) {
                    ToastUtils.showShort(getContext(), "事件内容不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(ed_lyzsf.getContentText())) {
                    ToastUtils.showShort(getContext(), "另一种说法内容不能为空！");
                    return;
                }

                String a=ed_zt.getContentText();//主体
                String b=ed_sj.getContentText();//事件
                String c=ed_lyzsf.getContentText();//另一种说法
                ed_scnr.setContentText(
                                "　　"+a+b+"是怎么回事呢？"+a+"相信大家都很熟悉，但是"+a+b+"是怎么回事呢，下面就让小编带大家一起了解吧。\n" +
                                "　　"+a+b+"，其实就是"+c+"，大家可能会很惊讶"+a+"怎么会"+b+"呢？但事实就是这样，小编也感到非常惊讶。\n" +
                                "　　这就是关于"+a+b+"的事情了，大家有什么想法呢，欢迎在评论区告诉小编一起讨论哦！");
            }
        });
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

}
