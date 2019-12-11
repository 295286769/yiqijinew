package com.yiqiji.money.modules.homeModule.write.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by whl on 16/9/19.
 */
public class RecyclePageAdapter extends PagerAdapter {

	private final List<ViewGroup> mViews;

	private final Context mContext;

	private int childCount;

	public RecyclePageAdapter(Context mContext, List<ViewGroup> view) {
		this.mContext = mContext;
		this.mViews = view;
	}

	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViews.get(position));

		return mViews.get(position);
	}

	/**
	 * 销毁对象
	 * 
	 * @param position
	 *            将要被销毁对象的索引位置
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}

}
