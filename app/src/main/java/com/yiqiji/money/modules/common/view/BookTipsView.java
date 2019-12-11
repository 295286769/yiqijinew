package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

/**
 * Created by ${huangweishui} on 2017/3/21.
 * address huang.weishui@71dai.com
 */
public class BookTipsView extends View {
    //    private String bookText = "账本列表,点击这切换或创建哦";
    private String bookText = "";
    //    private String newBookText = "发现被邀请的新账本";
    private Paint paint_text;
    private Paint paint_bagroud;
    private int textSize;
    private int bagroudColor;
    private int textColor;
    private int with;
    private int height;
    private boolean isFirstBook = true;//true为第一次安装
    private Path path;
    private int left;
    private int textleft;
    private int top;
    private int top_with;
    private int radio;

    public BookTipsView(Context context) {
        this(context, null);
    }

    public BookTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setBookText(boolean isFirstBook) {
        this.isFirstBook = isFirstBook;
        if (isFirstBook) {
            bookText = "账本列表,点击这切换或创建哦";
        } else {
            bookText = "发现被邀请的新账本";
        }

    }

    public BookTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textSize = (int) getResources().getDimension(R.dimen.context_text);
        textColor = getResources().getColor(R.color.white);
        bagroudColor = getResources().getColor(R.color.half_transparent);
        left = UIHelper.Dp2Px(context, 15);
        radio = UIHelper.Dp2Px(context, 5);
        textleft = UIHelper.Dp2Px(context, 10);
        initPaith();
    }

    private void initPaith() {
        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setTextSize(textSize);
        paint_text.setColor(textColor);
        paint_bagroud = new Paint();
        paint_bagroud.setAntiAlias(true);
        paint_bagroud.setStyle(Paint.Style.FILL);
        paint_bagroud.setColor(bagroudColor);

        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
        int lenth = (int) paint_text.measureText(bookText);
        int height_text = (int) (fontMetrics.descent - fontMetrics.ascent);
        with = lenth + left * 2;
        height = height_text + height_text / 3 + left * 2;
        top_with = with / 6;
        top = height / 4;
        setMeasuredDimension(with, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(bookText)) {
            path.moveTo(left, top);
            path.lineTo(left + top_with / 2, 0);
            path.lineTo(left + top_with, top);
            path.close();
            canvas.drawPath(path, paint_bagroud);
            RectF rectF = new RectF(radio, top, with, height);
            canvas.drawRoundRect(rectF, radio, radio, paint_bagroud);
            paint_text.measureText(bookText);
            Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
            int textY = (int) (fontMetrics.descent - fontMetrics.ascent) / 4;
            canvas.drawText(bookText, textleft, (height - top) / 2 + top + textY, paint_text);
        }
    }
}
