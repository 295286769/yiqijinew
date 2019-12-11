package com.yiqiji.money.modules.common.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DrawSimpleLineChartView extends View {
    private Context mContext;
    private Paint paint;
    private Path path;
    private int day_week_current;
    private int day_week;
    private long time;
    private Canvas canvas;
    private float lineStartX;
    float screnHeight;
    float screntWidth;
    float strokeWidth = 3;
    private float circleRadius = 5;
    private float lineLength;
    private float circleStartX;
    private int marginBottom = 50;
    private int marginBottomForText = 45;
    private float leftSpaceing = 30;
    private float textSize = 20;
    private float standardStopY;
    private Paint paintForText;
    private int cireleCount;
    private float lineStopX;
    private Circle[] circles;
    private Integer spicalPoint;
    private Float max;
    private String[] amount;
    private String[] moths;
    private int[] day_colors = new int[]{R.color.sunday_color, R.color.monday_color, R.color.tuesday_color,
            R.color.Wednesday, R.color.thursday_color, R.color.friday_color, R.color.saturday_color};
    private PathEffect effect;
    private long currentTime = 0;
    private String[] weeks = new String[7];

    private int moths_color;
    private int amount_color;
    private int line_future_color;
    private int statis_cicle_color;
    private int secend_amount_color;

    private int lineColor;
    private String type = "";

    public DrawSimpleLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        strokeWidth = UIHelper.Dp2Px(mContext, 2);
        circleRadius = UIHelper.Dp2Px(mContext, 3);
        leftSpaceing = UIHelper.Dp2Px(mContext, 15);
        marginBottom = UIHelper.Dp2Px(mContext, 35);
        moths_color = getResources().getColor(R.color.context_color);
        amount_color = getResources().getColor(R.color.secondary_text);
        lineColor = getResources().getColor(R.color.main_back);
        secend_amount_color = getResources().getColor(R.color.split_line);
        line_future_color = getResources().getColor(R.color.secondary_text);
        statis_cicle_color = getResources().getColor(R.color.statis_cicle_color);
        textSize = getResources().getDimension(R.dimen.text_12);
        marginBottomForText = UIHelper.Dp2Px(mContext, 25);
        effect = new DashPathEffect(new float[]{15, 10, 15, 10}, 0);
        Calendar calendar = Calendar.getInstance();
        day_week = calendar.get(Calendar.DAY_OF_WEEK);

        initCanvas();
    }

    public DrawSimpleLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
        // initCanvas();
    }

    public DrawSimpleLineChartView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
        // initCanvas();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screnHeight = getMeasuredHeight();
        screntWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;
        // screnHeight = getHeight();
        // screntWidth = getWidth();
        // cireleCount = 31;
        // String[] amount ={"10","30","50","70","10","5","200","10","30","50",
        // "10","30","50","70","10","5","200","10","30","50",
        // "10","30","50","70","10","5","200","10","30","50" ,"1000"};

        // cireleCount = 20;
        // String[] amount ={"10","30","50","70","10","5","200","10","30","50",
        // "10","30","50","70","10","5","200","10","30","50",
        // };
        // cireleCount = 7;
        // amount = new String[]{"0","0","0","0","0","10","10"};
        beforDrawing();
        drawFoldLinePoionts();
        // drawWatch();
        // drawCommonPoionts();
        reDrawText();

    }

    private void initCanvas() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Style.STROKE);

        paintForText = new Paint();
        paint.setAntiAlias(true);
        paintForText.setTextSize(textSize);
        // paintForText.setFakeBoldText(true);
        // paintForText.setStyle(Style.FILL_AND_STROKE);
        paintForText.setTextAlign(Align.CENTER);
        paintForText.setColor(moths_color);
        paintForText.setTypeface(Typeface.DEFAULT_BOLD);
        replaceTextToWeek();
        path = new Path();
    }

    private void beforDrawing() {

        standardStopY = screnHeight - marginBottom;

        lineLength = (screntWidth - leftSpaceing * 4 - cireleCount * circleRadius * 2) / (cireleCount - 1)
                - leftSpaceing / 4;
        circleStartX = leftSpaceing * 2f + circleRadius * 2;

        if (amount == null) {
            return;
        }
        circles = new Circle[cireleCount];
        Circle c = null;

        max = Float.valueOf(amount[0]);

        for (int i = 0; i < amount.length; i++) {
            float temp = Float.valueOf(amount[i]);
            c = new Circle();
            c.radius = circleRadius;
            c.amount = temp;
            circles[i] = c;

            if (temp > max) {
                max = temp;
            }
        }
        if (max == 0) {
            max = 1f;
        }
        for (int i = 0; i < circles.length; i++) {
            circles[i].startY = reMeasureCircleStopY(Float.valueOf(amount[i]));
        }
    }

    // private void clearCanvas(){
    // canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
    // Paint paint = new Paint();
    // paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    // canvas.drawPaint(paint);
    // paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
    // }
    private float reMeasureCircleStopY(float amount) {
        if (moths != null) {
            return standardStopY + circleRadius * 2 - (standardStopY - leftSpaceing) / max * amount;
        }

        return standardStopY + circleRadius * 2;
    }

    public void resetTheSpicalPointStorkeWidth(int index, Date adata) {
        spicalPoint = index;
        postInvalidate();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        postInvalidate();
    }

    private long[] times;

    @SuppressLint("SimpleDateFormat")
    public void reDrawFoldLinePoionts(String[] points, long[] time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        try {
            Date dateFomat = dateFormat.parse(dateString);
            calendar.setTime(dateFomat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        currentTime = calendar.getTimeInMillis();
        cireleCount = points.length;
        amount = points;
        times = time;

        postInvalidate();
    }

    @SuppressLint("SimpleDateFormat")
    public void reDrawFoldLinePoionts(String[] points, long[] time, String[] moths, String type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        long times_date = DateUtil.stringToTime(calendar.getTimeInMillis()) - 60 * 1000;
        Date date = new Date(times_date);

        calendar.setTime(date);
        // calendar.add(Calendar.MONTH, 1);

        currentTime = calendar.getTimeInMillis();
        currentTime = DateUtil.stringToTime(calendar.getTimeInMillis());// 结束时间
        // 23:59
        cireleCount = points.length;
        amount = points;
        times = time;
        this.moths = moths;
        this.type = type;

        postInvalidate();
    }

    private void drawFoldLinePoionts() {
        if (circles == null) {
            return;
        }
        paint.setPathEffect(null);
        paint.setStyle(Style.FILL);
        strokeWidth = UIHelper.Dp2Px(mContext, 3);
        for (int i = 0; i < circles.length; i++) {
            if (spicalPoint != null) {
                if (i == circles.length - 1) {
                    if (times[4] < currentTime) {
                        paint.setColor(lineColor);
                    } else {
                        paint.setColor(amount_color);
                    }

                } else {
                    if (times[i] > currentTime) {
                        paint.setColor(amount_color);
                    } else {
                        paint.setColor(lineColor);
                    }

                }
                if (spicalPoint == i) {
                    if (times[i] > currentTime) {
                        paint.setColor(amount_color);
                    } else {
                        paint.setColor(lineColor);
                    }

                    canvas.drawCircle(circleStartX, circles[i].getStartY(), circleRadius + UIHelper.Dp2Px(mContext, 2),
                            paint);
                    paint.setStyle(Style.STROKE);
                    strokeWidth = UIHelper.Dp2Px(mContext, 2);
                    paint.setStrokeWidth(strokeWidth);
                    if (times[i] > currentTime) {
                        paint.setColor(secend_amount_color);
                    } else {
                        // paint.setColor(statis_cicle_color);
                        paint.setColor(lineColor);

                    }

                    canvas.drawCircle(circleStartX, circles[i].getStartY(),
                            circles[i].getRadius() + UIHelper.Dp2Px(mContext, 3), paint);

                } else {

                    paint.setStyle(Style.FILL);
                    canvas.drawCircle(circleStartX, circles[i].getStartY(), circleRadius + UIHelper.Dp2Px(mContext, 2),
                            paint);
                }
            } else {
                canvas.drawCircle(circleStartX, circles[i].getStartY(), circleRadius, paint);
            }

            if (amount != null) {
                String amountString = XzbUtils.getBalance(Double.parseDouble(amount[i]));
                paintForText.setColor(amount_color);
                if (Float.parseFloat(amount[i]) > 0) {
                    if (type.equals("0")) {
                        amountString = "+" + amountString;
                    } else if (type.equals("1")) {
                        amountString = "-" + amountString;
                    }
                }

                float amount_lenth = paint.measureText(amountString);
                float amount_left = circleStartX - amount_lenth / 2;
                FontMetrics fontMetrics = paint.getFontMetrics();
                float amount_height = fontMetrics.descent - fontMetrics.ascent;
                float amount_top = circles[i].getStartY() - circles[i].getRadius() + UIHelper.Dp2Px(mContext, 3)
                        - amount_height - amount_height / 1.3f;
                if (spicalPoint != null && spicalPoint == i) {
                    if (type.equals("1")) {
                        paintForText.setColor(getResources().getColor(R.color.expenditure));
                    } else {
                        paintForText.setColor(getResources().getColor(R.color.income));
                    }

                } else {
                    paintForText.setColor(moths_color);
                }

                canvas.drawText(amountString, amount_left, amount_top, paintForText);
            }

            circles[i].startX = circleStartX;

            lineStartX = circleStartX + circleRadius * 2;
            lineStopX = lineStartX + lineLength;

            circleStartX = lineStopX + circleRadius;

        }
        strokeWidth = UIHelper.Dp2Px(mContext, 1);
        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(getResources().getColor(R.color.day_line_color));
        if (times != null && times.length > 0) {
            for (int i = 0; i < circles.length; i++) {

                if (times[i] >= currentTime) {// 未来
                    path.reset();
                    paint.setPathEffect(effect);
                    paint.setStyle(Style.STROKE);
                    paint.setColor(amount_color);
                    if (spicalPoint != null && i < circles.length - 1 && i == spicalPoint) {
                        path.moveTo(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 3),
                                circles[i].getStartY());
                        path.lineTo(circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                circles[i + 1].getStartY());
                        canvas.drawPath(path, paint);

                    } else if (i == 0) {
                        // if (i == 0) {
                        path.moveTo(leftSpaceing, standardStopY + circleRadius * 2);
                        path.lineTo(circles[i].getStartX() - circleRadius + UIHelper.Dp2Px(mContext, 2),
                                circles[i].getStartY());

                        // canvas.drawPath(path, paint);
                        // }

                        if (spicalPoint != null && spicalPoint == 1) {

                            path.moveTo(circles[0].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[0].getStartY());
                            path.lineTo(circles[1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 3),
                                    circles[1].getStartY());
                            canvas.drawPath(path, paint);
                        } else {
                            path.moveTo(circles[0].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[0].getStartY());
                            path.lineTo(circles[1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                    circles[1].getStartY());
                            canvas.drawPath(path, paint);
                        }

                    } else if (i > 0 && i < circles.length - 1) {
                        if (spicalPoint != null && i + 1 == spicalPoint) {
                            path.moveTo(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[i].getStartY());
                            path.lineTo(circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 3),
                                    circles[i + 1].getStartY());
                            canvas.drawPath(path, paint);
                        } else {

                            path.moveTo(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[i].getStartY());
                            path.lineTo(circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                    circles[i + 1].getStartY());
                            canvas.drawPath(path, paint);

                        }

                    } else if (i == circles.length - 1) {
                        path.moveTo(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                circles[i].getStartY());
                        path.lineTo(screntWidth - leftSpaceing, standardStopY + circleRadius * 2);
                        canvas.drawPath(path, paint);
                    }

                } else {// 以前
                    paint.setPathEffect(null);
                    paint.setStyle(Style.FILL);
                    paint.setStrokeWidth(UIHelper.Dp2Px(mContext, 2));
                    paint.setColor(lineColor);
                    if (spicalPoint != null && i < circles.length - 1 && i == spicalPoint) {
                        canvas.drawLine(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 3),
                                circles[i].getStartY(),
                                circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                circles[i + 1].getStartY(), paint);

                    } else if (i == 0) {
                        if (spicalPoint != null && spicalPoint == 1) {
                            canvas.drawLine(circles[0].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[0].getStartY(),
                                    circles[1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 3),
                                    circles[1].getStartY(), paint);
                        } else {
                            canvas.drawLine(circles[0].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[0].getStartY(),
                                    circles[1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                    circles[1].getStartY(), paint);
                        }

                    } else if (i > 0 && i < circles.length - 1) {
                        if (spicalPoint != null && i + 1 == spicalPoint) {
                            paint.setPathEffect(null);
                            canvas.drawLine(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[i].getStartY(),
                                    circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 3),
                                    circles[i + 1].getStartY(), paint);
                        } else {

                            canvas.drawLine(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                    circles[i].getStartY(),
                                    circles[i + 1].getStartX() - circleRadius - UIHelper.Dp2Px(mContext, 2),
                                    circles[i + 1].getStartY(), paint);

                        }

                    }
                    if (i == 0) {
                        canvas.drawLine(leftSpaceing, standardStopY + circleRadius * 2, circles[0].getStartX() - circleRadius,
                                circles[0].getStartY(), paint);
                    } else if (i == circles.length - 1) {
                        canvas.drawLine(circles[i].getStartX() + circleRadius + UIHelper.Dp2Px(mContext, 2),
                                circles[i].getStartY(), screntWidth - leftSpaceing, standardStopY + circleRadius * 2, paint);
                    }
                }

            }
        }

    }

    ValueAnimator animator = ValueAnimator.ofObject(new CircleEvaluator(), cireleCount);

    public class CircleEvaluator implements TypeEvaluator<Circle> {

        @Override
        public Circle evaluate(float fraction, Circle arg1, Circle arg2) {
            // TODO Auto-generated method stub
            lineStartX = circleStartX + circleRadius;
            lineStopX = lineStartX + lineLength;

            circleStartX = lineStopX + circleRadius;
            // float amount = arg1.getAmount();
            // float startX = arg1.getStartX();
            // float radius = arg1.getRadius();
            // float stopY = arg1.getStartY();
            // int color = arg1.getColor();
            // int index = arg1.getIndex();
            // new Circle(circleStartX)
            return new Circle();
        }
    }

    // private float resetLineStopX(float oldX){
    // return oldX-circleRadius*(float)Math.cos(45*Math.PI/180);
    // }
    // private float resetLineStartX(float oldX){
    // return oldX+circleRadius*(float)Math.cos(45*Math.PI/180);
    // }
    // private void drawFoldLinePoionts(){
    //
    // for(int i = 0;i<circles.length;i++){
    //
    // canvas.drawCircle(circleStartX, circles[i].getStartY(), circleRadius,
    // paint);
    //
    // circles[i].startX = circleStartX;
    //
    // lineStartX = circleStartX+circleRadius;
    // lineStopX = lineStartX+lineLength;
    //
    // circleStartX = lineStopX+circleRadius;
    // }
    //
    // for(int i = 0;i<circles.length;i++){
    // if(i == 0){
    // canvas.drawLine(resetLineStartX(circles[0].getStartX()),
    // circles[0].getStartY(),
    // resetLineStopX(circles[1].getStartX()), circles[1].getStartY(), paint);
    // }else if(i>0 && i<circles.length-1){
    // // if(i == circles.length-2){
    // // canvas.drawLine(circles[i].getStartX(), circles[i].getStartY(),
    // circles[i+1].getStartX(),
    // circles[i+1].getStartY(), paint);
    // // }else{
    // // canvas.drawLine(circles[i].getStartX(), circles[i].getStartY(),
    // circles[i+1].getStartX(),
    // circles[i+1].getStartY(), paint);
    // // }
    // canvas.drawLine(resetLineStartX(circles[i].getStartX()),
    // circles[i].getStartY(),
    // resetLineStopX(circles[i+1].getStartX()), circles[i+1].getStartY(),
    // paint);
    // }
    // }
    // }
    private void drawCommonPoionts() {
        circles = new Circle[cireleCount];

        for (int i = 0; i < cireleCount; i++) {
            // if(i<cireleCount/2){
            // paint.setColor(Color.WHITE);
            // }else{
            // paint.setColor(Color.BLUE);
            // circleRadius = 6;
            // }
            canvas.drawCircle(circleStartX, standardStopY, circleRadius, paint);

            circles[i] = new Circle(circleStartX, standardStopY, i, 0);

            lineStartX = circleStartX + circleRadius;
            lineStopX = lineStartX + lineLength;

            if (i == cireleCount - 1) {
                break;
            }
            canvas.drawLine(lineStartX, standardStopY, lineStopX, standardStopY, paint);
            circleStartX = lineStopX + circleRadius;

        }
    }

    private void reDrawText() {
        paintForText.setColor(moths_color);
        if (circles == null) {
            return;
        }
        if (moths != null) {
            draMothDays(moths);
        } else {
            draMothDays(weeks);
        }

    }

    private void draMothDays(String[] text) {
        if (text != null && text.length > 0) {
            LogUtil.log_msg("__++++" + text[0]);
            paintForText.measureText(text[0]);
            FontMetrics fontMetrics = paintForText.getFontMetrics();

            float text_height = fontMetrics.descent - fontMetrics.ascent;
            if (cireleCount == 7 || cireleCount == 5) {
                for (int i = 0; i < circles.length; i++) {

                    canvas.drawText(text[i], circles[i].getStartX(), standardStopY + marginBottomForText + text_height / 4,
                            paintForText);
                }
            } else {
                for (int i = 1; i <= circles.length; i++) {
                    if (i % 5 == 0) {
                        canvas.drawText(i + "", circles[i - 1].getStartX(), standardStopY + marginBottomForText,
                                paintForText);
                    }
                }
            }
        }

    }

    private void replaceTextToWeek() {
        weeks[0] = "周日";
        weeks[1] = "周一";
        weeks[2] = "周二";
        weeks[3] = "周三";
        weeks[4] = "周四";
        weeks[5] = "周五";
        weeks[6] = "周六";
    }

    public class Circle {
        private float radius;
        private float startX;
        private float startY;
        private float amount;
        private int index;
        private int color;

        public Circle() {
            super();
            // TODO Auto-generated constructor stub
        }

        public Circle(float startX, float startY, int index, int color) {
            super();
            this.startX = startX;
            this.startY = startY;
            this.index = index;
            this.color = color;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getStartX() {
            return startX;
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        public float getStartY() {
            return startY;
        }

        public void setStartY(float startY) {
            this.startY = startY;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

    }

    public class Line {
        private String startX;
        private String stopX;
        private String stopY;
        private int color;

        public String getStartX() {
            return startX;
        }

        public void setStartX(String startX) {
            this.startX = startX;
        }

        public String getStopX() {
            return stopX;
        }

        public void setStopX(String stopX) {
            this.stopX = stopX;
        }

        public String getStopY() {
            return stopY;
        }

        public void setStopY(String stopY) {
            this.stopY = stopY;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

    }

    public class Text {
        private String startX;
        private String startY;
        private int index;
        private int color;

        public Text(String startX, String startY, int index, int color) {
            super();
            this.startX = startX;
            this.startY = startY;
            this.index = index;
            this.color = color;
        }

        public String getStartX() {
            return startX;
        }

        public void setStartX(String startX) {
            this.startX = startX;
        }

        public String getStartY() {
            return startY;
        }

        public void setStartY(String startY) {
            this.startY = startY;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

}
