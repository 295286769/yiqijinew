package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BookItemIcon extends View {

	private Paint mPaint;

	public BookItemIcon(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BookItemIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BookItemIcon(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(Color.RED); // 设置画笔颜色
		mPaint.setStyle(Paint.Style.STROKE);// 设置填充样式
		mPaint.setStrokeWidth(3);// 设置画笔宽度
		mPaint.setAntiAlias(true);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

	}

}
