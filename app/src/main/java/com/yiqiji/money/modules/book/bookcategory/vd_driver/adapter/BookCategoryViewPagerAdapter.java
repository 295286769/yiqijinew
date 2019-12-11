package com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.model.IconModel;
import com.yiqiji.money.modules.common.adapter.ViewPageAdapter;

import java.util.List;

/**
 * Created by leichi on 2017/5/26.
 */

public class BookCategoryViewPagerAdapter extends ViewPageAdapter{

    List<View> mViewList;
    List<List<IconModel>> mDataList;
    Context mContext;
    public BookCategoryViewPagerAdapter(Context context, List<List<IconModel>> dataList, List<View> views) {
        super(views);
        this.mViewList=views;
        this.mDataList=dataList;
        this.mContext=context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewList.get(position);
        GridView gridView=(GridView)view.findViewById(R.id.gridView_viewpager_item);
        GridViewAdapter gridViewAdapter=new GridViewAdapter(mContext,mDataList.get(position));
        gridView.setAdapter(gridViewAdapter);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }
}
