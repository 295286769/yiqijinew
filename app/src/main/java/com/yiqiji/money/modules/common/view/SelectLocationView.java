package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/31.
 * address huang.weishui@71dai.com
 */
public class SelectLocationView extends LinearLayout {
    @BindView(R.id.leftText)
    TextView leftText;//左边文字
    @BindView(R.id.rightText)
    TextView rightText;//右边文字
    @BindView(R.id.arrowImage)
    ImageView arrowImage;//右边图片
    @BindView(R.id.rlExportDt)
    RelativeLayout rlExportDt;
    private Context mContext;
    private String text;//左边文字
    private String address_text;//右边文字

    public SelectLocationView(Context context) {
        this(context, null);
    }

    public SelectLocationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectLocationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initTypeArray(attrs);
        initView();
        setLeftData(text);
        setSelectText(address_text);
    }

    private void initTypeArray(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SelectLocationView);
        text = typedArray.getString(R.styleable.SelectLocationView_select_text);
        address_text = typedArray.getString(R.styleable.SelectLocationView_address_init_text);

        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_select_location, this, true);
        ButterKnife.bind(this, view);
    }

    private void setLeftData(String text) {
        if (text != null) {
            leftText.setText(text);
        }
    }

    public void setSelectText(String selectText) {
        if (rightText != null) {
            rightText.setText(selectText);
        }
    }

    public String getSelectText() {
        if (rightText != null) {
            return !TextUtils.isEmpty(rightText.getText().toString()) ? rightText.getText().toString() : "";
        }
        return "";
    }
}
