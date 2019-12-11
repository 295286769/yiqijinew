package com.yiqiji.money.modules.community.decoration.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;
import com.yiqiji.money.modules.community.decoration.view.DecorateCommpanyView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/1.
 */

public class ItemDecorationCpViewHolder extends BaseViewHolder {

    @BindView(R.id.commpany_view)
    DecorateCommpanyView commpanyView;

    public ItemDecorationCpViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindViewData(DecorateListMultiItem item) {
        commpanyView.setData((DecorationcompanyBean) item.getData());
    }

    public void bindViewData(DecorationcompanyBean item) {
        commpanyView.setData(item);
    }
}
