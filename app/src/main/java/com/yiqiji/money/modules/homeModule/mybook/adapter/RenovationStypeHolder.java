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
public class RenovationStypeHolder extends BaseViewHolder {
    @BindView(R.id.tv_rv_item_title)
    TextView tv_rv_item_title;
    @BindView(R.id.tv_item_content)
    TextView tv_item_content;
    @BindView(R.id.ig_rv_select)
    ImageView ig_rv_select;

    public RenovationStypeHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(Context context, HouseStypeInfo houseStypeInfo) {
        if (houseStypeInfo.isSelect()) {
            ImageLoaderManager.loadImage(context, R.drawable.select_true, ig_rv_select);
        } else {
            ImageLoaderManager.loadImage(context, 0, ig_rv_select);
        }
        tv_rv_item_title.setText(houseStypeInfo.getTitle());
        tv_item_content.setText(houseStypeInfo.getContent());
    }
}
