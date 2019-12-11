package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

/**
 * Created by ${huangweishui} on 2017/3/22.
 * address huang.weishui@71dai.com
 */
public class TickView extends View {
    private Path path;
    private Paint paint;
    private int color;
    private float size;
    private int height;
    private int with;
    private int start_top;
    private int with_two;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        color = getResources().getColor(R.color.main_back);
        size = UIHelper.Dp2Px(context, 2);
        initPaith();
    }

    private void initPaith() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        with = getMeasuredWidth();
        start_top = height / 3;
        with_two = with / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0, start_top);
        path.lineTo(with_two, height);
        path.moveTo(with, 0);
        path.close();
        canvas.drawPath(path, paint);

    }
}
