package com.yiqiji.money.modules.homeModule.mybook.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypeHolder extends BaseViewHolder {
    @BindView(R.id.tv_item_title)
    TextView tv_item_title;
    @BindView(R.id.im_select)
    ImageView im_select;

    public HouseStypeHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(Context context, HouseStypeInfo houseStypeInfo) {
        boolean isSelect = houseStypeInfo.isSelect();
        if (isSelect) {
            ImageLoaderManager.loadImage(context, R.drawable.select_true, im_select);
        } else {
            ImageLoaderManager.loadImage(context, 0, im_select);
        }
        tv_item_title.setText(houseStypeInfo.getTitle());
    }
}
