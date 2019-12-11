package com.yiqiji.money.modules.community.discover.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/8/7.
 */

public class ItemTitleViewHolder extends BaseViewHolder{

    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.ll_more)
    LinearLayout llMore;

    View contentView;
    Context context;

    public ItemTitleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        contentView = view;
        context = contentView.getContext();
    }

    public void bindViewData(DiscoverMultipleItem item) {
        final TitleModel titleModel = (TitleModel) item.getData();
        titleName.setText(titleModel.titleName);
        llMore.setVisibility(titleModel.type == 0 ? View.GONE : View.VISIBLE);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (titleModel.type) {
                    case 0:

                        break;
                    case 1:            //旅游
                        StartActivityUtil.startTravelMain(context);
                        break;
                    case 2:            //装修
                        ActivityUtil.startDecorateHome(context);
                        break;
                    case 3:            //其他
                        StartActivityUtil.startDiscoverOther(context);
                        break;

                }
            }
        });

    }
}
