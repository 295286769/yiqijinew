package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;

public class IncomeExpenditureCircleView extends View {
    private Context mContext;
    private Paint paint_text;
    private Paint paint_circle;
    private Paint paint_line;
    private Path path;
    private Path path_line;
    private int text_expenditure_cilor;
    private int text_imcom_cilor;
    private float text_expenditure_size;
    private float text_pesent_size;
    private int max_one_color;
    private int max_two_color;
    private int max_three_color;
    private int max_four_color;
    private int circle_color;
    private int[] statis_color;

    private float screenWith;
    private float screenHeigt;

    private float center_x;
    private float center_y;
    private float radiou;
    private float radiou_arc;

    private String expenditure_content = "";

    private float start_arc_x;
    private float start_arc_y;
    private float max_arc = 360f;
    private double max_value;
    private float startAngle;
    private float sweepAngle;
    private float path_sweepAngle;// 外圆的角度
    private double[] values;
    private float circle_bagroun_stroke;
    private float circle_point;
    private String[] contents;

    private float path_line_lenth;
    private float margrn;
    private String type = "";

    public IncomeExpenditureCircleView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public IncomeExpenditureCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public IncomeExpenditureCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        text_expenditure_cilor = getResources().getColor(R.color.context_color);
        text_imcom_cilor = getResources().getColor(R.color.secondary_text);
        max_one_color = getResources().getColor(R.color.secondary_text);
        circle_color = getResources().getColor(R.color.white);
        text_expenditure_size = getResources().getDimension(R.dimen.context_text);
        text_pesent_size = getResources().getDimension(R.dimen.text_10);
        circle_point = getResources().getDimension(R.dimen.circle_point);
        margrn = getResources().getDimension(R.dimen.margin_left_15);
        circle_bagroun_stroke = getResources().getDimension(R.dimen.circle_bagroun_stroke);
        startAngle = -90;
        initPaitn();
    }

    private void initPaitn() {
        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(text_expenditure_cilor);
        paint_text.setTextSize(text_expenditure_size);

        paint_circle = new Paint();
        paint_circle.setColor(circle_color);
        paint_circle.setStyle(Style.FILL);
        paint_circle.setAntiAlias(true);
        paint_line = new Paint();
        paint_line.setColor(getResources().getColor(R.color.main_back));
        paint_line.setAntiAlias(true);
        paint_line.setStyle(Style.STROKE);
        paint_line.setStrokeWidth(circle_point);

        path = new Path();

        path_line = new Path();

    }

    public void setPesent(double[] values, int[] statis_color, String[] contents, String type) {
        this.values = values;
        this.statis_color = statis_color;
        this.contents = contents;
        this.type = type;
        max_value = 0;
        startAngle = -90;
        // this.one_total_pesent = one_total_pesent;
        // this.two_total_pesent = two_total_pesent;
        // this.thire_total_pesent = thire_total_pesent;
        // this.four_total_pesent = four_total_pesent;
        for (int i = 0; i < values.length; i++) {
            max_value += values[i];
        }
        postInvalidate();
    }

    public void setExpenditure_content(String expenditure_content) {
        this.expenditure_content = expenditure_content;
        startAngle = -90;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWith = getMeasuredWidth();
        screenHeigt = getMeasuredHeight();
        center_x = screenWith / 2;
        center_y = screenHeigt / 2;
        radiou = screenWith / 15;
        radiou_arc = screenWith / 5.5f;
        start_arc_x = center_x - radiou_arc;
        start_arc_y = center_y - radiou_arc;
        startAngle = -90;
        path_line_lenth = (screenWith - (radiou_arc + circle_bagroun_stroke) * 2 - UIHelper.Dp2Px(mContext, 40)) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        draCircleBagroun(canvas);
        drawArc(canvas);
        drawCircle(canvas);
        drawTextExpenditure(canvas);

    }

    private void draCircleBagroun(Canvas canvas) {
        paint_circle.setColor(getResources().getColor(R.color.split_line));
        canvas.drawCircle(center_x, center_y, radiou_arc + circle_bagroun_stroke, paint_circle);

    }

    private void drawArc(Canvas canvas) {
        if (values == null || statis_color == null || contents == null) {
            return;
        }
        startAngle = -90;
        PathMeasure pathMeasure = null;
        paint_text.setTextSize(text_pesent_size);
        RectF oval = new RectF(start_arc_x, start_arc_y, center_x + radiou_arc, center_y + radiou_arc);
        RectF rectF = new RectF(start_arc_x - circle_bagroun_stroke * 2, start_arc_y - circle_bagroun_stroke * 2,
                center_x + radiou_arc + circle_bagroun_stroke * 2, center_y + radiou_arc + circle_bagroun_stroke * 2);
        float[] points = new float[2];
        float pesent_value;

        FontMetrics fontMetrics = null;

        for (int i = 0; i < values.length; i++) {
            path.reset();// 重置path重新计算弧的长度
            path_line = new Path();// 重新创建path对象是为了实现path画线的不同颜色
            // path_line.reset();
            // path.reset();
            paint_circle.setColor(statis_color[i]);
            paint_line.setColor(statis_color[i]);
            // path.moveTo(start_arc_x, start_arc_y);
            pesent_value = (float) (values[i] / max_value);
            float se = 1;
            if (i == 0) {
                for (int j = 0; j < values.length; j++) {
                    float pesent = (float) (values[j] / max_value);
                    if (pesent > 0 && pesent < 0.1f) {
                        pesent = 0.1f;

                    }
                    if (j > 0) {
                        se -= pesent;
                    }

                }
                pesent_value = se;
            }
            if (values[i] > 0 && pesent_value < 0.1f) {
                pesent_value = 0.1f;
            }

            sweepAngle = pesent_value * max_arc;
            path_sweepAngle = sweepAngle;
            if (sweepAngle >= max_arc) {// 为了测量外圆弧长度 角度必须小于360 不然获取不到准确的弧上的坐标
                path_sweepAngle = 359f;
            }

            // path.addArc(oval, startAngle, sweepAngle);
            // pathMeasure = new PathMeasure(path, false);
            // float lenth_path = pathMeasure.getLength();
            // pathMeasure.getPosTan(lenth_path, points, null);
            // float point_x = points[0];
            // float point_y = points[1];

            // path.close();
            // canvas.drawPath(path, paint_circle);
            canvas.drawArc(oval, startAngle, sweepAngle, true, paint_circle);
            path.addArc(rectF, startAngle, path_sweepAngle);

            pathMeasure = new PathMeasure(path, false);
            float lenth_path = pathMeasure.getLength();
            pathMeasure.getPosTan(lenth_path / 2f, points, null);
            canvas.drawCircle(points[0], points[1], circle_point, paint_circle);
            path_line.moveTo(points[0], points[1]);

            float point_x = 0;
            float point_y = 0;
            float point_x_last = 0;

            String content = contents[i];
            if(StringUtils.isEmpty(content)){
                continue;
            }
            float content_lenth = 0;
            float pesent_content_lenth = 0;

            float start_x = 0;
            float start_pesent_content_x = 0;
            // String pesent_content = XzbUtils.setTwoDecimalFormat("#.0",
            // pesent_value * 100) + "%";
            startAngle += sweepAngle;
            String balance = XzbUtils.getBalance(values[i]);
            if (values[i] > 0) {
                if (type.equals("0")) {
                    balance = "+" + balance;
                } else if (type.equals("1")) {
                    balance = "-" + balance;
                }
            }
            if (startAngle - sweepAngle / 2 > -90 && startAngle - sweepAngle / 2 <= 0) {
                point_x = points[0] + circle_bagroun_stroke;
                point_y = points[1] - circle_bagroun_stroke;
                path_line.lineTo(point_x, point_y);

                path_line.moveTo(point_x, point_y);
                // point_x_last = point_x + path_line_lenth;
                point_x_last = point_x + screenWith - point_x - margrn;
                content_lenth = paint_text.measureText(content);
                pesent_content_lenth = paint_text.measureText(balance);
                start_x = point_x_last - content_lenth;
                start_pesent_content_x = point_x_last - pesent_content_lenth;
                path_line.lineTo(point_x_last, point_y);
            } else if (startAngle - sweepAngle / 2 > 0 && startAngle - sweepAngle / 2 <= 90) {
                point_x = points[0] + circle_bagroun_stroke;
                point_y = points[1] + circle_bagroun_stroke;
                path_line.lineTo(point_x, point_y);

                path_line.moveTo(point_x, point_y);
                // point_x_last = point_x + path_line_lenth;
                point_x_last = point_x + screenWith - point_x - margrn;
                content_lenth = paint_text.measureText(content);
                start_x = point_x_last - content_lenth;
                pesent_content_lenth = paint_text.measureText(balance);
                start_pesent_content_x = point_x_last - pesent_content_lenth;
                path_line.lineTo(point_x_last, point_y);
            } else if (startAngle - sweepAngle / 2 > 90 && startAngle - sweepAngle / 2 <= 180) {
                point_x = points[0] - circle_bagroun_stroke;
                point_y = points[1] + circle_bagroun_stroke;
                path_line.lineTo(point_x, point_y);

                path_line.moveTo(point_x, point_y);
                // point_x_last = point_x - path_line_lenth;
                point_x_last = margrn;
                start_x = point_x_last;

                start_pesent_content_x = start_x;
                path_line.lineTo(point_x_last, point_y);

            } else if (startAngle - sweepAngle / 2 > 180) {
                point_x = points[0] - circle_bagroun_stroke;
                point_y = points[1] - circle_bagroun_stroke;
                path_line.lineTo(point_x, point_y);

                path_line.moveTo(point_x, point_y);
                // point_x_last = point_x - path_line_lenth;
                point_x_last = margrn;
                start_x = point_x_last;
                start_pesent_content_x = start_x;
                path_line.lineTo(point_x_last, point_y);
            }

            canvas.drawPath(path_line, paint_line);

            fontMetrics = paint_text.getFontMetrics();
            float content_height = fontMetrics.descent - fontMetrics.ascent;

            float start_y = point_y - content_height / 4;
            canvas.drawText(content, start_x, start_y, paint_text);

            canvas.drawText(balance, start_pesent_content_x,
                    point_y + content_height - content_height / 4 + UIHelper.Dp2Px(mContext, 1), paint_text);

        }

        // canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint)

    }

    private void drawTextExpenditure(Canvas canvas) {
        paint_text.setTextSize(text_expenditure_size);
        paint_text.setColor(text_expenditure_cilor);
        float lenth = paint_text.measureText(expenditure_content);
        FontMetrics fontMetrics = paint_text.getFontMetrics();
        float height = fontMetrics.descent - fontMetrics.ascent;
        float content_x = center_x - lenth / 2;
        float content_y = center_y + height / 4;

        canvas.drawText(expenditure_content, content_x, content_y, paint_text);

    }

    private void drawCircle(Canvas canvas) {
        paint_circle.setColor(getResources().getColor(R.color.white));
        canvas.drawCircle(center_x, center_y, radiou, paint_circle);

    }
}
