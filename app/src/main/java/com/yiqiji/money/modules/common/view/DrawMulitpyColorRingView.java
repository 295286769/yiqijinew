package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.Constants;

public class DrawMulitpyColorRingView extends View {

	private int width;
	private int height;
	private Canvas canvas;
	private Paint paintForOutside;
	private int cicleRadius;
	private int maginTop;
	private Paint paintForInside;
	private final Context context;
	private Bitmap income;
	private Bitmap expenses;
	private Bitmap tolal;
	private OnDrawCircleClicListener circleClicListener;
	private int innerCircleCenterX;
	private int innerCircleCentY;
	private String[] categories;
	private double[] amount;
	private int type;

	public DrawMulitpyColorRingView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.context = context;
		initCanvas();
	}

	public DrawMulitpyColorRingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initCanvas();
	}

	public DrawMulitpyColorRingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		initCanvas();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth();
		height = getHeight();
	}

	public void setOnDrawCircleClicListener(
			OnDrawCircleClicListener circleClicListener) {
		this.circleClicListener = circleClicListener;
	}

	public interface OnDrawCircleClicListener {
		public boolean onClickListener();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas = canvas;
		width = getWidth();
		height = getHeight();
		// String[] categoris = new String[]{
		// "1","2","3","1","2","3","1","2","3","1",
		// "1","2","3","1","2","3","1","2","3","1"};
		// double[] amout = new double[]{
		// 10,20,30,40,50,60,70,80,90,100,
		// 110,120,130,140,150,160,170,180,190,200};

		// String[] categoris = new String[]{
		// "1","2","3","1","2","3","1","2","3","1"
		// };
		// double[] amout = new double[]{
		// 10,20,30,40,50,60,70,80,90,100
		// };

		// String[] categoris = new String[]{
		// "1","2","3","1","2"
		// };
		// double[] amout = new double[]{
		// 10,20,30,40,50
		// };
		drawMultipleColorRingDependentOnScreenCenterPoint();
		// width = getWidth();
		// height = getHeight();
		// drawCircleDependentOnMaginTop();
	}

	/**
	 * 点击事件只限于内层圆环
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		if (Math.pow((innerCircleCenterX - x), 2)
				+ Math.pow((y - innerCircleCentY), 2) < Math.pow(
				(cicleRadius - 20), 2)) // 圆标准方程
		{
			return circleClicListener.onClickListener();
		}
		return super.onTouchEvent(event);
	}

	private void initCanvas() {
		paintForOutside = new Paint();
		paintForOutside.setAntiAlias(true);
		paintForOutside.setStyle(Style.STROKE);
		paintForOutside.setStrokeWidth(90);
		// paintForOutside.setColor(Color.parseColor("#FF7D49"));

		paintForInside = new Paint();
		paintForInside.setAntiAlias(true);
		paintForInside.setStyle(Style.STROKE);
		paintForInside.setStrokeWidth(30);
		paintForInside.setColor(Color.parseColor("#3f000000"));

		income = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.income_no_text);
		expenses = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.expenses);
		tolal = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.tolal);
	}

	/***
	 * @param type
	 *            0:income,1:expenses,2:all or no records
	 * @param categories
	 * @param amount
	 */
	public void reDrawMultipleColorRing(int type, String[] categories,
			double[] amount) {
		this.type = type;
		this.categories = categories;
		this.amount = amount;
		postInvalidate();
	}

	private void drawMultipleColorRingDependentOnScreenCenterPoint() {
		if (width > height) {
			cicleRadius = (int) (height / 3);
		} else {
			cicleRadius = (int) (width / 3);
		}

		int left = width / 2 - cicleRadius;
		int top = height / 2 - cicleRadius;
		int right = left + cicleRadius * 2;
		int bottom = top + cicleRadius * 2;

		RectF rectForOut = new RectF(left, top, right, bottom);

		if (categories != null && categories.length >= 1) {
			float startAngle = 0;
			float sweepAngle = 0;
			double sum = 0;
			for (int i = 0; i < amount.length; i++) {
				sum += amount[i];
			}

			for (int i = 0; i < amount.length; i++) {
				sweepAngle = -Float
						.valueOf((amount[i] / sum * (360 + amount.length * 1))
								+ "");
				// sweepAngle = -Float.valueOf((amount[i]/sum*360)+"");
				paintForOutside.setColor(Color.parseColor(Constants.COLORS[i]));
				canvas.drawArc(rectForOut, startAngle, sweepAngle, false,
						paintForOutside);

				startAngle += sweepAngle + 1;
				// startAngle += sweepAngle;
			}
		} else {
			paintForOutside.setColor(Color.parseColor(Constants.NO_RECORDS));
			canvas.drawArc(rectForOut, 0, 360, false, paintForOutside);
		}

		innerCircleCenterX = width / 2;
		innerCircleCentY = height / 2;

		// canvas.drawCircle(innerCircleCenterX, innerCircleCentY, cicleRadius -
		// 20, paintForInside);

		Bitmap bitmap = null;
		if (type == 0) {
			bitmap = income;
		} else if (type == 1) {
			bitmap = expenses;
		} else {
			bitmap = tolal;
		}
		canvas.drawBitmap(bitmap, width / 2 - income.getWidth() / 2, height / 2
				- income.getHeight() / 2, new Paint());
	}

	private void drawCircleDependentOnMaginTop() {
		maginTop = 40;

		if (width > height) {
			cicleRadius = (height - maginTop * 2) / 2;
		} else {
			cicleRadius = width / 2;
		}

		int left = width / 2 - cicleRadius;
		int top = maginTop;
		int right = left + cicleRadius * 2;
		int bottom = top + cicleRadius * 2;

		RectF rectForOut = new RectF(left, top, right, bottom);

		// canvas.drawRect(rectForOut, paintForOut);
		// canvas.drawArc(rectForOut, 0, -80, false, paintForOutside);

		paintForOutside.setColor(Color.parseColor(Constants.COLORS[0]));
		canvas.drawArc(rectForOut, 0f, -120f, false, paintForOutside);

		paintForOutside.setColor(Color.parseColor(Constants.COLORS[1]));
		canvas.drawArc(rectForOut, -120f, -120f, false, paintForOutside);

		paintForOutside.setColor(Color.parseColor(Constants.COLORS[2]));
		canvas.drawArc(rectForOut, -240f, -120f, false, paintForOutside);
		canvas.drawCircle(width / 2, height / 2, cicleRadius - 20,
				paintForInside);
	}
}
