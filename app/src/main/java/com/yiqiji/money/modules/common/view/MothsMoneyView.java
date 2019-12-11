package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

public class MothsMoneyView extends View {
    private Context context;
    private Paint paint;
    private int color;
    private float size;
    private float with;
    private float height;
    private float left;
    private String moths = "";
    private String money = "";
    private float top;

    public void setMothsMoney(String moths, String money) {
        this.money = money;
        this.moths = moths;
        postInvalidate();
    }

    public MothsMoneyView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public MothsMoneyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MothsMoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        color = getResources().getColor(R.color.secondary_text);
        size = getResources().getDimension(R.dimen.text_13);
        left = UIHelper.Dp2Px(context, 15);
        iniPait();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private void iniPait() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setTextSize(size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (moths != null && !TextUtils.isEmpty(moths)) {
            float moth_lenth = paint.measureText(moths);
            FontMetrics fontMetrics = paint.getFontMetrics();
            float moth_height = fontMetrics.descent - fontMetrics.ascent;
            top = height / 2 + moth_height / 4 + UIHelper.dip2px(context, 2);
            canvas.drawText(moths, left, top, paint);

            float money_lenth = paint.measureText(money);
            float money_left = with - left - money_lenth;

            // canvas.drawText(money, money_left, top, paint);
        }

    }
}
