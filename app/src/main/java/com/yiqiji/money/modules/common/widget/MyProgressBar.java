/**
 * huangweishui
 */
package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author huangweishui
 * 
 */
public class MyProgressBar extends ProgressBar {
	private String text_progress;
	private Paint mPaint;// 画笔
	private int position;

	public MyProgressBar(Context context) {
		super(context);
		initPaint();
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		this.position = progress;
		// setTextProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// Rect rect = new Rect();
		// this.mPaint.getTextBounds(this.text_progress, 0,
		// this.text_progress.length(), rect);
		// // 进度=100时,文字位置保持不变
		// int x = position > 100 ? getWidth() - rect.width() : ((getWidth()
		// * position / 100) - rect.width());
		// int y = (getHeight() / 2) - rect.centerY();
		// canvas.drawText(this.text_progress, x, y, this.mPaint);

	}

	/**
	 * 
	 * description: 初始化画笔 Create by lll on 2013-8-13 下午1:41:49
	 */
	private void initPaint() {
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(Color.WHITE);
		this.mPaint.setTextSize(18);
	}

	private void setTextProgress(int progress) {
		int i = (int) ((progress * 1.0f / this.getMax()) * 100);
		this.text_progress = String.valueOf(i) + "%";

	}
}
