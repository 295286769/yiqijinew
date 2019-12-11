package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.content.res.TypedArray;
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
public class LeftTextRightImageView extends LinearLayout {
    @BindView(R.id.leftText)
    TextView leftText;
    @BindView(R.id.arrowImage)
    ImageView arrowImage;
    @BindView(R.id.rlExportDt)
    RelativeLayout rlExportDt;
    private Context mContext;
    private String left_text;

    public LeftTextRightImageView(Context context) {
        this(context, null);
    }

    public LeftTextRightImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftTextRightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initTyeArray(attrs);
        initView();
        setLeftText(left_text);
    }


    private void initTyeArray(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LeftTextRightImageView);
        left_text = typedArray.getString(R.styleable.LeftTextRightImageView_left_text);
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_lefttext_rightimage_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setLeftText(String left_text) {
        if (leftText != null) {
            leftText.setText(left_text);
        }
    }

    public void setArrowImage(int drawbleid) {
        if (arrowImage != null) {
            arrowImage.setImageResource(drawbleid);
        }
    }
}
