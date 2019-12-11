package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.entity.TabListModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/28.
 * address huang.weishui@71dai.com
 */
public class DiscoverHSItemView extends LinearLayout {
    @BindView(R.id.image_tab_icon)
    DiscoverHSItemImageView imageTabIcon;//图片
    @BindView(R.id.tv_item_tab)
    TextView tvItemTab;//描述
    private Context mContext;
    private TabListModel.TabModel tabModel;

    public DiscoverHSItemView(Context context) {
        this(context, null);
    }

    public DiscoverHSItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscoverHSItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
        setOnPress();
    }

    private void setOnPress() {

    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_discover_found_item_tab_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setTabModel(TabListModel.TabModel tabModel) {
        this.tabModel = tabModel;
        if (tabModel != null) {
            diplayImage(tabModel.img);
            setContent(tabModel.title);
        }
    }

    public void diplayImage(String image_url) {
        if (imageTabIcon != null) {
            ImageLoaderManager.loadRoundCornerImage(mContext, image_url, imageTabIcon, 14);
        }

    }

    /**
     * @param withScreenPesent//宽度和屏幕的比例
     * @param withHeightPesent//宽度和高度的比例
     */
    public void setImageWithHeight(float withScreenPesent, float withHeightPesent) {
        if (imageTabIcon != null) {
            imageTabIcon.setWithAndHeigtPesent(withScreenPesent, withHeightPesent);
        }
    }

    public void setContent(String content) {
        if (tvItemTab != null) {
            tvItemTab.setText(content);
        }

    }
}
