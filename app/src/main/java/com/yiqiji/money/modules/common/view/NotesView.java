package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;

public class NotesView extends View {
	private Paint paint;
	private int notes_color;
	private float with;
	private float height;
	private float radios;

	public NotesView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public NotesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public NotesView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		notes_color = getResources().getColor(R.color.red);
		initPaith();
	}

	private void initPaith() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(notes_color);
		paint.setStyle(Style.FILL);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		with = getMeasuredWidth();
		height = getMeasuredHeight();
		radios = Math.min(with / 2, height / 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawCircle(with / 2, height / 2, radios, paint);
	}

}
