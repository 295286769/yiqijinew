package com.yiqiji.money.modules.homeModule.mybook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 风格
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class RenovationStypeItemView extends LinearLayout {
    @BindView(R.id.tv_stype_item)
    TextView tvStypeItem;
    private Context mContext;

    public RenovationStypeItemView(Context context) {
        this(context, null);
    }

    public RenovationStypeItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RenovationStypeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_renovation_stype_view_layout, this, true);
        ButterKnife.bind(this, view);

    }

    public void setStypeContent(String contnent) {
        if (tvStypeItem != null) {
            tvStypeItem.setText(contnent);
        }
    }
}
