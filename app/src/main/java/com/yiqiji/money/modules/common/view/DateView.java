package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

public class DateView extends View {
    private Context context;
    private Paint paint;
    private Paint paint_bottom;
    private Path path;
    private Paint paint_line;
    private int text_color;
    private int text_second_color;
    private int bagroud_color;
    private int lin_color;
    private float text_size;
    private float with;
    private float height;
    private String name = "";
    private float text_lenth;
    private float text_lheight;
    private float text_x;
    private float text_y;
    private float corner;
    private float marginLeft;
    public int mIndex;
    private boolean isSelect = false;
    private float one_dp;

    public DateView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
    }

    private void initPaint() {
        corner = UIHelper.Dp2PxFloat(context, 5);
        one_dp = UIHelper.Dp2PxFloat(context, 1);
        marginLeft = UIHelper.Dp2PxFloat(context, 10);
        text_color = getResources().getColor(R.color.context_color);
        text_second_color = getResources().getColor(R.color.secondary_text);
        lin_color = getResources().getColor(R.color.split_line);
        text_size = getResources().getDimension(R.dimen.context_text_14);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(text_size);
        paint.setStyle(Style.FILL);
        paint_bottom = new Paint();
        paint_bottom.setAntiAlias(true);
        paint_bottom.setStyle(Style.FILL);
        paint_bottom.setStrokeWidth(5);
        paint_bottom.setColor(getResources().getColor(R.color.module_line));

        paint_line = new Paint();
        paint_line.setAntiAlias(true);
        paint_line.setColor(lin_color);
        path = new Path();
    }

    public void setText(String name) {
        this.name = name;
        postInvalidate();
    }

    public void setIsSelect(boolean isSelect, int colorIndex) {
        this.isSelect = isSelect;
        this.text_color = colorIndex;
        postInvalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        text_lenth = paint.measureText(name);
        FontMetrics metrics = paint.getFontMetrics();
        text_lheight = metrics.descent - metrics.ascent;
        text_x = with / 2 - text_lenth / 2;
        text_y = height / 2 + text_lheight / 4;
        if (isSelect) {
            draBagroud(canvas);
        }
        if (name != null && !name.equals("")) {
            draText(canvas);
        }
//        draLine(canvas);

    }

    private void draLine(Canvas canvas) {
        canvas.drawLine(UIHelper.dip2px(context, 15), height - UIHelper.Dp2Px(context, 0.3f), with - UIHelper.dip2px(context, 15), height, paint_line);

    }

    private void draText(Canvas canvas) {
        paint.setColor(text_color);

        canvas.drawText(name, text_x, text_y, paint);

    }

    private void draBagroud(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.module_line));
        float left = text_x - marginLeft;
        float top = text_lheight - text_lheight / 4f;
        float right = text_x + text_lenth + marginLeft;
        float bottom = text_lheight + text_lheight;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, corner, corner, paint);
        float half_with = with / 2;
        float half_height = height / 2;
        path.moveTo(half_with - corner, bottom - one_dp);
        path.lineTo(half_with, bottom + corner + one_dp);
        path.lineTo(half_with + corner, bottom - one_dp);
        path.close();
        canvas.drawPath(path, paint_bottom);

    }

    public int getIndex() {
        return mIndex;
    }
}
