package com.yiqiji.money.modules.common.widget;

import android.animation.TypeEvaluator;

import com.yiqiji.money.modules.common.entity.MyTypePoiont;

public class MyTypeEvaluator implements TypeEvaluator<MyTypePoiont> {

	@Override
	public MyTypePoiont evaluate(float fraction, MyTypePoiont startValue,
			MyTypePoiont endValue) {
		float x = startValue.getX() + fraction
				* (endValue.getX() - startValue.getX());
		float y = startValue.getY() + fraction
				* (endValue.getY() - startValue.getY());
		return new MyTypePoiont(x, y);
	}

}
