package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import android.view.View.MeasureSpec;

public class MyLinearLayoutManager extends LinearLayoutManager {
public MyLinearLayoutManager(Context context) {
	super(context);
}
//	public MyLinearLayoutManager(Context context, int orientation) {
//		this(context, orientation, false);
//		// TODO Auto-generated constructor stub
//	}
//	public MyLinearLayoutManager(Context context, int orientation,
//			boolean reverseLayout) {
//		super(context, orientation, reverseLayout);
//		
//	}
	@Override
		public void onMeasure(Recycler recycler, State state, int widthSpec,
				int heightSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(recycler, state, widthSpec, heightSpec);
			 View view = recycler.getViewForPosition(0);
			 int position=0;
			 
		        if(view != null){  
//		        	position=view.getMeasuredHeight()*getChildCount();
		            measureChild(view, widthSpec, heightSpec);  
		            int measuredWidth = MeasureSpec.getSize(widthSpec);  
		            int measuredHeight = view.getMeasuredHeight()*getChildCount();  
		            setMeasuredDimension(measuredWidth, measuredHeight);  
		        }  
		}

}
