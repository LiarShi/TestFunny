package com.liar.testcall.wigdet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.ReplacementTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liar.testcall.R;
import com.liar.testcall.config.Constants;




/**
 * 采集项输入控件
 *
 Created by liar on 2020/4/29.
 */
public class CltEditTextView extends FrameLayout {

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
     * 单位控件
     */
    private TextView mUnitTv;
    /**
     * 跳转按钮
     */
    private TextView mJumpBtn;

    /**
     * 当前输入类型
     */
    private int mInputType = Constants.CltInputType.TYPE_TEXT;

    /**
     * 文字监听器
     */
    private TextWatcher mTextWatcher;

    public CltEditTextView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CltEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CltEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CltEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_clt_edit_text, this);
        mMustImg = findViewById(R.id.must_img);
        mTitleTv = findViewById(R.id.title_tv);
        mContentEdit = findViewById(R.id.content_edit);
        mUnitTv = findViewById(R.id.unit_tv);
        mJumpBtn = findViewById(R.id.jump_btn);

    }

    private void initAttributeSet(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CltEditTextView);
        // 设置标题
        setTitleText(typedArray.getString(R.styleable.CltEditTextView_titleText));
        // 设置内容
        setContentText(typedArray.getString(R.styleable.CltEditTextView_contentText));
        setContentHint(typedArray.getString(R.styleable.CltEditTextView_contentHint));
        setEditInputType(typedArray.getInt(R.styleable.CltEditTextView_inputType, Constants.CltInputType.TYPE_TEXT));
        // 设置必填提示显隐
        setRequiredTips(typedArray.getBoolean(R.styleable.CltEditTextView_isRequired, true));
        // 设置是否需要跳转按钮
        setNeedJumpBtn(typedArray.getBoolean(R.styleable.CltEditTextView_isNeedJump, false));
        setJumpBtnText(typedArray.getString(R.styleable.CltEditTextView_jumpText));
        // 设置是否需要单位
        setNeedUnit(typedArray.getBoolean(R.styleable.CltEditTextView_isNeedUnit, false));
        setUnitText(typedArray.getString(R.styleable.CltEditTextView_unitText));
        setUnitDrawableEnd(typedArray.getResourceId(R.styleable.CltEditTextView_unitDrawableEnd, 0));
        int maxLength = typedArray.getInt(R.styleable.CltEditTextView_maxLength, 0);
        // 如果不指定默认只能输入50个字符
        setMaxLength(maxLength > 0 ? maxLength : 50);
        typedArray.recycle();
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mTextWatcher != null) {
                    mTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mTextWatcher != null) {
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
                if (mInputType == Constants.CltInputType.TYPE_NUMBER_DECIMAL) {
                    int index = getContentText().indexOf(".");
                    int lastIndex = getContentText().lastIndexOf(".");
                    if (index == 0) {
                        // 第一位输入点则默认头部加0
                        setContentText("0.");
                        mContentEdit.setSelection(getContentText().length());
                        return;
                    }
                    if (index != lastIndex) {
                        // 输入了多个小数点只保留第一个
                        String str = getContentText().substring(0, lastIndex);
                        setContentText(str);
                        mContentEdit.setSelection(str.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
                }
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
     * 设置内容文字
     *
     * @param content 文字
     */
    public void setContentText(String content) {
        mContentEdit.setText(content);
    }

    /**
     * 设置内容文字资源id
     *
     * @param resId 资源id
     */
    public void setContentText(@StringRes int resId) {
        mContentEdit.setText(resId);
    }

    /**
     * 获取内容文字
     */
    public String getContentText() {
        return mContentEdit.getText().toString();
    }

    /**
     * 添加内容编辑文字监听
     *
     * @param watcher 文字监听
     */
    public void addContentTextChangedListener(TextWatcher watcher) {
        mTextWatcher = watcher;
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
     * 设置文本输入类型
     *
     * @param type 输入类型
     */
    public void setEditInputType(@Constants.CltInputType int type) {
        mInputType = type;
        switch (type) {
            case Constants.CltInputType.TYPE_TEXT:
                // 输入普通文本
                break;
            case Constants.CltInputType.TYPE_ID_CARD:
                // 输入身份证号
                mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                mContentEdit.setKeyListener(DigitsKeyListener.getInstance("1234567890xX"));
                mContentEdit.setTransformationMethod(new UpperCaseTransformation());
                break;
            case Constants.CltInputType.TYPE_PHONE:
                // 输入手机号
                mContentEdit.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case Constants.CltInputType.TYPE_NUMBER:
                // 输入数字
                mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case Constants.CltInputType.TYPE_NUMBER_DECIMAL:
                // 输入小数
                mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                mContentEdit.setKeyListener(DigitsKeyListener.getInstance("1234567890."));
                break;
            case Constants.CltInputType.TYPE_FOREIGN_CERT:
                // 输入国外证件号
                mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                mContentEdit.setKeyListener(DigitsKeyListener.getInstance("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
                mContentEdit.setTransformationMethod(new UpperCaseTransformation());
                break;
            default:
                break;
        }

    }

    /**
     * 设置是否需要单位
     *
     * @param isNeed 是否需要
     */
    public void setNeedUnit(boolean isNeed) {
        mUnitTv.setVisibility(isNeed ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否显示单位
     */
    public boolean isShowUnit() {
        return mUnitTv.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置单位文字
     *
     * @param unit 文字
     */
    public void setUnitText(String unit) {
        mUnitTv.setText(unit);
    }

    /**
     * 设置单位文字资源id
     *
     * @param resId 资源id
     */
    public void setUnitText(@StringRes int resId) {
        mUnitTv.setText(resId);
    }

    /**
     * 获取单位文字
     */
    public String getUnitText() {
        return mUnitTv.getText().toString();
    }

    /**
     * 设置单位右侧图标
     *
     * @param drawableId 图标资源id
     */
    public void setUnitDrawableEnd(@DrawableRes int drawableId) {
        mUnitTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
    }

    /**
     * 设置单位点击监听器
     *
     * @param listener 监听器
     */
    public void setUnitListener(OnClickListener listener) {
        mUnitTv.setOnClickListener(listener);
    }

    /**
     * 设置是否需要跳转按钮
     *
     * @param isNeed 是否需要
     */
    public void setNeedJumpBtn(boolean isNeed) {
        mJumpBtn.setVisibility(isNeed ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否显示跳转按钮
     */
    public boolean isShowJumpBtn() {
        return mJumpBtn.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置跳转按钮文字
     *
     * @param text 文字
     */
    public void setJumpBtnText(String text) {
        mJumpBtn.setText(text);
    }

    /**
     * 设置跳转按钮文字资源id
     *
     * @param resId 资源id
     */
    public void setJumpBtnText(@StringRes int resId) {
        mJumpBtn.setText(resId);
    }

    /**
     * 获取跳转按钮文字
     */
    public String getJumpBtnText() {
        return mJumpBtn.getText().toString();
    }

    /**
     * 设置跳转按钮监听器
     *
     * @param listener 监听器
     */
    public void setJumpBtnListener(OnClickListener listener) {
        mJumpBtn.setOnClickListener(listener);
    }

    /**
     * 设置焦点变化监听器
     */
    public void setTextChangedListener(TextWatcher watcher) {
        mContentEdit.addTextChangedListener(watcher);
    }

    /**
     * 获取EditText
     */
    public EditText getEdit() {
        return mContentEdit;
    }

    /**
     * 设置文本最大长度
     *
     * @param max 最大值
     */
    public void setMaxLength(int max) {
        mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    /**
     * 小写字母转大写
     */
    public class UpperCaseTransformation extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }

        @Override
        protected char[] getReplacement() {
            return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        }
    }

    /**
     * 控件进入只读状态（除选择按钮）
     */
    public void setViewRead() {
        mJumpBtn.setVisibility(GONE);
        mMustImg.setVisibility(GONE);
        mContentEdit.setEnabled(false);
        mContentEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mContentEdit.setHint("");
    }

    /**
     * 设置是否可输入
     */
    public void setEtEnable(boolean etEnable) {
        mContentEdit.setEnabled(etEnable);
    }

    /**
     * 设置内容文字颜色
     *
     * @param color 颜色
     */
    public void setContentTextColor(int color) {
        mContentEdit.setTextColor(color);
    }

    /**
     * 设置内容文字加粗
     */
    public void setContentTextBold() {
        TextPaint tp = mContentEdit.getPaint();
        tp.setFakeBoldText(true);
    }
}
