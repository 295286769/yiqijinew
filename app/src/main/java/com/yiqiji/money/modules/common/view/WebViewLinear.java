package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class WebViewLinear extends ScrollView implements Pullable {

	public WebViewLinear(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WebViewLinear(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WebViewLinear(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}
}
