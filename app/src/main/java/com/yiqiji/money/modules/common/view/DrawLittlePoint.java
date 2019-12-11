package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawLittlePoint extends View {
	private int width;
	private int height;
	private Paint paint;
	public DrawLittlePoint(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();
		
		canvas.drawCircle(width/2, height/2, 8, paint);
	}
	public void resetPointColor(int color){
		paint.setColor(color);
		invalidate();
	}
}
