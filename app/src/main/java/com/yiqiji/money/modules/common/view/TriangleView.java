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
 * Created by ${huangweishui} on 2017/3/9.
 * address huang.weishui@71dai.com
 */
public class TriangleView extends View{
    private int height;
    private int with;
    private Path path;
    private Paint paint;
    private int color;
    private Context mContext;

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TriangleView(Context context) {
        this(context,null);
    }
    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        color=getResources().getColor(R.color.white);
        initPait();
    }

    private void initPait() {
        paint=new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        path=new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height=getMeasuredHeight();
        with=getMeasuredHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        path.moveTo(0,height);
        path.lineTo(with/2, UIHelper.Dp2Px(mContext,5));
        path.lineTo(height,with);
        path.close();
        canvas.drawPath(path,paint);
    }
}
