package com.yiqiji.money.modules.Found.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;
import com.yiqiji.money.modules.community.view.DiscoverHSItemView;

import java.util.List;

/**
 * Created by leichi on 2017/6/6.
 */

public class TabHScorllerAdapter implements BaseScorllerAdapter {

    Context mContext;
    List<TabListModel.TabModel> mTabModelList;
    ViewGroup contentView;

    public TabHScorllerAdapter(Context context, TabListModel tabListModel) {
        mContext = context;
        mTabModelList = tabListModel.list;
    }


    @Override
    public void initView(ViewGroup viewGroup) {
        contentView = viewGroup;
        contentView.removeAllViews();
//        calculateScaleValue();
        for (int i = 0; i < mTabModelList.size(); i++) {
            TabListModel.TabModel tabModel = mTabModelList.get(i);
            DiscoverHSItemView discoverHSItemView = new DiscoverHSItemView(mContext);
            discoverHSItemView.setTabModel(tabModel);
            viewGroup.addView(discoverHSItemView);
            discoverHSItemView.setTag(i);
            OnEnvent(discoverHSItemView);
//                setEvent(discoverHSItemView);
//            ImageLoaderManager.loadRoundCornerImage(mContext, tabModel.img, imageView, 14);
        }

        contentView.requestLayout();
    }


    @Override
    public void OnItemClickListener(BaseOnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public void OnEnvent(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag().toString());
                if (onItemClick != null) {
                    onItemClick.onItemClick(mTabModelList.get(index));
                }
            }
        });
    }

//    private void setEvent(View itemView) {
//        itemView.setOnClickListener(onClickListener);
//    }

    int imageMargin;
    int imageWidth;
    int imageHeight;

    private void calculateScaleValue() {
        int sreenWidth = UIHelper.getDisplayWidth((Activity) mContext);
        imageMargin = sreenWidth / 151 * 3;
        imageWidth = sreenWidth / 151 * 56;
        imageHeight = imageWidth / 2;

    }


//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int index = Integer.parseInt(v.getTag().toString());
//            if (onItemClick != null) {
//                onItemClick.onItemClick(mTabModelList.get(index));
//            }
//        }
//    };

    BaseOnItemClickListener onItemClick;

//    public interface OnItemClickListener {
//        void onItemClick(TabListModel.TabModel tabModel);
//    }

//    public void setOnItemClick(OnItemClickListener onItemClick) {
//        this.onItemClick = onItemClick;
//    }
}
