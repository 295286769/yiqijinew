package com.yiqiji.money.modules.common.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.UIHelper;

public class DrawProgressMulitpyColorRingView extends View {
    private int width;
    private int height;
    private Canvas canvas;
    private Paint paintForOutside;
    private Paint paintForLine;
    private Paint paintForTheMiaddle;
    private int strokeWidth;
    private float currentAngle;
    private int currentScore;
    private String[] array = new String[]{"0", "较差", "200", "一般", "400", "良好", "600", "优秀", "800", "较好", "1000"};
    private Paint paintForText;
    private Paint paintForInside;
    private Rect rect;
    private Bitmap point;
    private float startAngle;
    private float sweepAngle;

    public DrawProgressMulitpyColorRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DrawProgressMulitpyColorRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public DrawProgressMulitpyColorRingView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;
        width = getWidth();
        height = getHeight();
        draw();
    }

    private void draw() {
        int marginTop = UIHelper.Dp2Px(context, 30f);
        int radius = (Math.min(width, height) - strokeWidth) / 2;
        int centerX = width / 2;
        int centerY = height / 2 + marginTop;

        SweepGradient shader = new SweepGradient(centerX, centerY, new int[]{Color.parseColor("#6ED888"),
                Color.parseColor("#E4483E"), Color.parseColor("#E88122"), Color.parseColor("#CECF24"),
                Color.parseColor("#87DD4B"), Color.parseColor("#6ED888"),}, new float[]{0f, 0.5f, 0.625f, 0.75f,
                0.875f, 1f});

        paintForOutside.setShader(shader);

        canvas.save();
        RectF rectf = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectf, startAngle, sweepAngle, false, paintForOutside);

        paintForText.setColor(Color.parseColor("#D3D3D3"));
        paintForText.setTextSize(20);

        int index = 0;
        int total = 31;
        float startY = centerY - radius - strokeWidth / 2;
        canvas.rotate(-(90 + startAngle), centerX, centerY);
        for (int i = 0; i < total; i++) {
            if (i % 3 == 0) {
                index++;
                float x = centerX - paintForText.measureText(array[index - 1]) / 2;
                float y = startY + strokeWidth + strokeWidth / 2 + 10;
                canvas.drawText(array[index - 1], x, y, paintForText);

                paintForLine.setStrokeWidth(1);
            } else {
                paintForLine.setStrokeWidth(0.5f);
            }
            canvas.drawLine(centerX, startY, centerX, startY + strokeWidth, paintForLine);
            canvas.rotate(-(sweepAngle) / (total - 1), centerX, centerY);
        }
        canvas.restore();

        canvas.save();

        radius = radius - strokeWidth - 20;
        rectf = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        paintForTheMiaddle.setColor(Color.parseColor("#EEF5F2"));
        canvas.drawArc(rectf, startAngle, sweepAngle, false, paintForTheMiaddle);

        paintForTheMiaddle.setColor(Color.parseColor("#63C395"));
        canvas.drawArc(rectf, sweepAngle + startAngle, currentAngle, false, paintForTheMiaddle);

        paintForText.getTextBounds(array[1], 0, array[1].length(), rect);

        startY = centerY - radius - strokeWidth + rect.height() + 5;

        canvas.rotate(-(90 + startAngle) + currentAngle, centerX, centerY);
        canvas.drawBitmap(point, centerX - point.getWidth() / 2, startY, new Paint());

        canvas.restore();

        radius -= 40;
        rectf = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectf, startAngle, sweepAngle, false, paintForInside);

        paintForText.setColor(Color.parseColor("#63C395"));
        paintForText.setTextSize(50);
        // paintForText.setTypeface(Typeface.create("宋体", Typeface.BOLD));
        paintForText.getTextBounds(currentScore + "", 0, (currentScore + "").length(), rect);
        canvas.drawText(currentScore + "", centerX - rect.width() / 2, centerY, paintForText);

        paintForText.setTextSize(20);
        String str = "信用一般";
        paintForText.getTextBounds(str, 0, 4, rect);
        canvas.drawText(str, centerX - rect.width() / 2, centerY + rect.height(), paintForText);

    }

    /**
     * @param 0<=score<=1000
     */
    public void setScore(int score) {
        this.currentScore = score;
        float progressSweep = (float) (score / 1000.0) * (-sweepAngle);
        animateValue(progressSweep);
    }

    public void animateValue(float sweepAngle) {
        ValueAnimator animator = ValueAnimator.ofFloat(sweepAngle);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                // TODO Auto-generated method stub
                currentAngle = (Float) arg0.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animatorForText = ValueAnimator.ofInt(currentScore);
        animatorForText.setDuration(2000);
        animatorForText.setInterpolator(new LinearInterpolator());
        animatorForText.start();
        animatorForText.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                // TODO Auto-generated method stub
                currentScore = (Integer) arg0.getAnimatedValue();
                invalidate();
            }
        });
    }

    private Context context;

    private void init(Context context) {
        this.context = context;
        strokeWidth = UIHelper.Dp2Px(context, 18f);
        startAngle = 25;
        sweepAngle = -(180 + startAngle * 2);
        rect = new Rect();
        // 外层圆环
        paintForOutside = new Paint();
        paintForOutside.setAntiAlias(true);
        paintForOutside.setStyle(Style.STROKE);
        paintForOutside.setColor(Color.parseColor(Constants.DARK_BLUE));
        paintForOutside.setStrokeCap(Cap.SQUARE);
        paintForOutside.setStrokeWidth(strokeWidth);
        // 中间圆环
        paintForTheMiaddle = new Paint();
        paintForTheMiaddle.setAntiAlias(true);
        paintForTheMiaddle.setStyle(Style.STROKE);
        paintForTheMiaddle.setColor(Color.parseColor("#63C395"));
        paintForTheMiaddle.setStrokeWidth(3);

        // 圆环刻度
        paintForLine = new Paint();
        paintForLine.setAntiAlias(true);
        paintForLine.setStyle(Style.STROKE);
        paintForLine.setColor(Color.WHITE);

        paintForText = new Paint();
        paintForText.setAntiAlias(true);
        paintForText.setStyle(Style.STROKE);
        paintForText.setTextSize(20);
        // 内层圆环
        paintForInside = new Paint();
        paintForInside.setAntiAlias(true);
        paintForInside.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        paintForInside.setStrokeWidth(2);
        paintForInside.setStyle(Style.STROKE);
        paintForInside.setColor(Color.parseColor("#ECEDEE"));

        point = BitmapFactory.decodeResource(context.getResources(), R.drawable.pointer);
    }
}
