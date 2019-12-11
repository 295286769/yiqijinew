package com.yiqiji.money.modules.common.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;

public class ConsumptionChart extends View {
	private Context mContext;
	private int screenWith;
	private int screenHeight;
	private Bitmap bitmap, bitMaptop, bitMapBottom;
	private int one_with;
	private int mostHeight;
	private int left;
	private int top;
	private int bottom;
	private int right;
	private int miniHeight;
	private Paint paint;
	private int addHeight = 0;
	private int[] drawale = new int[] { R.drawable.sunday_bottom_one, R.drawable.sunday_center_one,
			R.drawable.sunday_top_one };
	private int[] drawale1 = new int[] { R.drawable.monday_bottom, R.drawable.monday_center, R.drawable.monday_top };
	private int[] drawale2 = new int[] { R.drawable.tuesday_bottom, R.drawable.tuesday_center, R.drawable.tuesday_top };
	private int[] drawale3 = new int[] { R.drawable.wenday_bottom, R.drawable.wenday_center, R.drawable.wenday_top };
	private int[] drawale4 = new int[] { R.drawable.thursday_bottom, R.drawable.thursday_center,
			R.drawable.thursday_top };
	private int[] drawale5 = new int[] { R.drawable.friday_bottom, R.drawable.friday_center, R.drawable.friday_top };
	private int[] drawale6 = new int[] { R.drawable.saturday_bottom, R.drawable.saturday_center,
			R.drawable.saturday_top };
	BitmapFactory.Options options = null;
	private List<int[]> integers = new ArrayList<int[]>();
	private List<Integer> integers2;
	private double maxHeight = 100f;// 每周收入支出差值最高值
	private int bitmapStartHeight = 0;

	public void setAddHeight(String[] array) {
		integers2 = new ArrayList<Integer>();
		maxHeight = XzbUtils.getMax(XzbUtils.returnInt(array));
		int[] h = XzbUtils.returnInt(array);
		for (int i = 0; i < h.length; i++) {
			integers2.add((int) (h[i] / maxHeight * (mostHeight - bitmapStartHeight)));
		}
		// integers2.add((int)(0/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(30f/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(90f/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(50f/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(70f/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(40f/maxHeight*(mostHeight-bitmapStartHeight)));
		// integers2.add((int)(30f/maxHeight*(mostHeight-bitmapStartHeight)));
		postInvalidate();
		// new Thread(){
		// public void run() {
		// while (addHeight<ddHeight) {
		//
		// try {
		// sleep(100);
		// addHeight +=5;
		// postInvalidate();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// };
		// }.start();

	}

	public ConsumptionChart(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ConsumptionChart(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ConsumptionChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		screenHeight = XzbUtils.getPhoneScreen((Activity) context).heightPixels;
		screenWith = XzbUtils.getPhoneScreen((Activity) context).widthPixels;
		one_with = (screenWith - UIHelper.Dp2Px(context, 10)) / 7 - UIHelper.Dp2Px(context, 10);
		mostHeight = screenHeight / 8;
		left = UIHelper.Dp2Px(context, 10);
		paint = new Paint();

		integers.add(drawale);
		integers.add(drawale1);
		integers.add(drawale2);
		integers.add(drawale3);
		integers.add(drawale4);
		integers.add(drawale5);
		integers.add(drawale6);

		initBitMap(drawale[0]);
		bitMapBottom = BitmapFactory.decodeResource(getResources(), drawale[0], options);

		initBitMap(drawale[2]);
		bitMaptop = BitmapFactory.decodeResource(getResources(), drawale[2], options);
		initBitMap(drawale[1]);
		bitmap = BitmapFactory.decodeResource(getResources(), drawale[1], options);
		bitmapStartHeight = bitMapBottom.getHeight() + bitmap.getHeight() + bitMaptop.getHeight();

	}

	private void initBitMap(int drable) {
		options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		bitmap = BitmapFactory.decodeResource(getResources(), drable, options);
		int bitMapHeight = options.outHeight * one_with / options.outWidth;
		options.outWidth = one_with;
		options.outHeight = bitMapHeight;
		options.inSampleSize = options.outWidth / one_with; /* 图片长宽方向缩小倍数 */
		options.inDither = false; /* 不进行图片抖动处理 */
		options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
		options.inJustDecodeBounds = false;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(screenWith, mostHeight);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		left = UIHelper.Dp2Px(mContext, 10);
		for (int i = 0; i < 7; i++) {
			addHeight = integers2.get(i);
			initBitMap(integers.get(i)[0]);
			bitMapBottom = BitmapFactory.decodeResource(getResources(), integers.get(i)[0], options);

			initBitMap(integers.get(i)[2]);
			bitMaptop = BitmapFactory.decodeResource(getResources(), integers.get(i)[2], options);
			initBitMap(integers.get(i)[1]);
			bitmap = BitmapFactory.decodeResource(getResources(), integers.get(i)[1], options);

			int bitMapBottom_height = (int) (((double) bitMapBottom.getHeight() / bitMapBottom.getWidth()) * one_with);
			int buttom_top_height = mostHeight - bitMapBottom_height;
			Rect rect = new Rect(0, 0, bitMapBottom.getWidth() + left, mostHeight);
			RectF dst1 = new RectF(left, buttom_top_height, one_with + left, mostHeight);
			canvas.drawBitmap(bitMapBottom, rect, dst1, paint);

			int bitmapHeight = (int) ((double) bitmap.getHeight() / bitmap.getWidth() * one_with);
			top = mostHeight - bitmapHeight - bitMapBottom_height;
			Rect src = new Rect(0, 0, bitmap.getWidth() + left, mostHeight - bitMapBottom.getHeight());
			RectF dst = new RectF(left, top - addHeight, one_with + left, mostHeight - bitMapBottom_height);
			canvas.drawBitmap(bitmap, src, dst, paint);

			int bitMaptop_height = (int) ((double) bitMaptop.getHeight() / bitMaptop.getWidth() * one_with);
			int top_three = mostHeight - bitMaptop_height - bitmapHeight - bitMapBottom_height;
			Rect src2 = new Rect(0, 0, bitmap.getWidth() + left, mostHeight - bitmap.getHeight()
					- bitMapBottom.getHeight() - addHeight);
			RectF dst2 = new RectF(left, top_three - addHeight, one_with + left, mostHeight - bitmapHeight
					- bitMapBottom_height - addHeight);
			canvas.drawBitmap(bitMaptop, src2, dst2, paint);
			left += one_with + UIHelper.Dp2Px(mContext, 10);

		}

	}

}
