package com.liar.testcall.wigdet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liar.testcall.R;
import com.lodz.android.core.utils.ToastUtils;




/**
 * 带记数的EditText
 *
 *Created by liar on 2020/4/29.
 */
public class CltMultiEditTextView extends FrameLayout {

    /**
     * 最大文字数
     */
    private static final int MAX_COUNT = 300;
    /**
     * 最小行数
     */
    private static final int MIN_LINES = 3;

    /**
     * 必填按钮
     */
    private ImageView mMustImg;
    /**
     * 标题控件
     */
    private TextView mTitleTv;
    /**
     * 内容输入框
     */
    private EditText mContentEdit;
    /**
     * 计数控件
     */
    private TextView mCountTv;

    /**
     * 最大文字数
     */
    private int mMaxCount = MAX_COUNT;

    public CltMultiEditTextView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CltMultiEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CltMultiEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CltMultiEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        findViews();
        if (attrs != null) {
            initAttributeSet(attrs);
        }
        setListeners();
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_clt_multi_edittext, this);
        mMustImg = findViewById(R.id.must_img);
        mTitleTv = findViewById(R.id.title_tv);
        mContentEdit = findViewById(R.id.content_edit);
        mCountTv = findViewById(R.id.count_tv);

    }

    private void initAttributeSet(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CltMultiEditTextView);
        // 设置标题
        setTitleText(typedArray.getString(R.styleable.CltMultiEditTextView_titleText));
        // 设置内容
        setContentHint(typedArray.getString(R.styleable.CltMultiEditTextView_contentHint));
        // 设置必填提示显隐
        setRequiredTips(typedArray.getBoolean(R.styleable.CltMultiEditTextView_isRequired, true));
        // 设置是最小行数
        setMinLines(typedArray.getInt(R.styleable.CltMultiEditTextView_minLines, MIN_LINES));
        setMaxCount(typedArray.getInt(R.styleable.CltMultiEditTextView_count, MAX_COUNT));
        typedArray.recycle();
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mContentEdit.getText().length() == mMaxCount && start != 0) {
                    ToastUtils.showShort(getContext(), "最多可输入" + mMaxCount + "个字");
                }
                String str = mContentEdit.getText().length() + "/" + mMaxCount;
                mCountTv.setText(str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置显示必填提示
     *
     * @param isShow 是否显示
     */
    public void setRequiredTips(boolean isShow) {
        mMustImg.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 是否必填
     */
    public boolean isRequired() {
        return mMustImg.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置标题文字
     *
     * @param title 文字
     */
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }

    /**
     * 设置标题文字资源id
     *
     * @param resId 资源id
     */
    public void setTitleText(@StringRes int resId) {
        mTitleTv.setText(resId);
    }

    /**
     * 获取标题文字
     */
    public String getTitleText() {
        return mTitleTv.getText().toString();
    }

    /**
     * 获取内容文字
     */
    public String getContentText() {
        return mContentEdit.getText().toString();
    }

    /**
     * 设置内容文字
     */
    public void setContentText(String msg) {
        mContentEdit.setText(msg);
    }

    /**
     * 设置内容提示
     *
     * @param hint 提示文字
     */
    public void setContentHint(String hint) {
        mContentEdit.setHint(hint);
    }

    /**
     * 设置内容提示资源id
     *
     * @param resId 资源id
     */
    public void getContentHint(@StringRes int resId) {
        mContentEdit.setHint(resId);
    }

    /**
     * 最小行数
     *
     * @param lines 行数
     */
    public void setMinLines(int lines) {
        mContentEdit.setMinLines(lines);
    }

    /**
     * 最大的字数
     *
     * @param count 字数
     */
    public void setMaxCount(int count) {
        mMaxCount = count;
        mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxCount)});
        String str = mContentEdit.getText().length() + "/" + mMaxCount;
        mCountTv.setText(str);
    }

    /**
     * 控件进入只读状态
     */
    public void setViewRead() {
        mMustImg.setVisibility(GONE);
        mContentEdit.setEnabled(false);
        mContentEdit.setHint("");
        mCountTv.setVisibility(GONE);
        setMinLines(1);
    }
}
