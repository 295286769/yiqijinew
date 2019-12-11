package com.yiqiji.money.modules.community.travel.holder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.travel.model.RaidersMultiItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/8/4.
 * address huang.weishui@71dai.com
 */
public class RaidersChildHolder extends BaseViewHolder {
    @BindView(R.id.title)
    TextView title;

    public RaidersChildHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindData(RaidersMultiItem item) {
        String raidersSectionsBean = (String) item.getData();
//        title.setText(Html.fromHtml(raidersSectionsBean));
        title.setText(raidersSectionsBean);
    }
}
