package com.liar.testcall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.liar.testcall.R;
import com.lodz.android.component.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择页
 * Created by liar on 2020/4/29.
 */
public class ChoseActivty extends BaseActivity {

    public String TAG = "WzglChoseActivty";


    @BindView(R.id.lin_bddh)
    LinearLayout lin_bddh;//自动拨打电话
    @BindView(R.id.lin_yxhscq)
    LinearLayout lin_yxhscq;//营销号生成器
    @BindView(R.id.lin_wzgl_ckgl)
    LinearLayout lin_wzgl_ckgl;//出库管理
    @BindView(R.id.lin_wzgl_rkcx)
    LinearLayout lin_wzgl_rkcx;//入库查询
    @BindView(R.id.lin_wzgl_kcck)
    LinearLayout lin_wzgl_kcck;//库存查看
    @BindView(R.id.lin_wzgl_yjwzck)
    LinearLayout lin_wzgl_yjwzck;//应急物资查看
    @BindView(R.id.lin_wzgl_dcgwz)
    LinearLayout lin_wzgl_dcgwz;//待采购物资
    @BindView(R.id.lin_wzgl_wzpd)
    LinearLayout lin_wzgl_wzpd;//物资盘点


    public static void start(Context context) {
        Intent starter = new Intent(context, ChoseActivty.class);
        context.startActivity(starter);
    }


    private void getIntentData() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.ac_chose;
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
    }

    private void initTitleBar() {
        getTitleBarLayout().setTitleName("选择");
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
        lin_bddh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                MainActivity.start(getContext());
            }
        });
        lin_yxhscq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                YxhscActivty.start(getContext());
            }
        });



    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

}
