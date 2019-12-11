package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.plication.MyApplicaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountItemView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Paint paint_text;
    private Paint money_paint_text;

    private Paint paint_line;
    private Context context;
    private float with;
    private float height;
    private float init_left;
    private float init_top;
    private float bitmap_left;
    private float bitmap_top;
    private String text_title = "";//
    private String text_context = "";
    private String date_time = "";// 时间
    private String addOrSettment = "";// 是否参与或者结算
    private String IsclearText = "";// 是否结算描述
    private String pay_money = "";
    private String pay_stype = "";
    private int text_title_size;
    private int text_title_color;
    private int text_context_color;
    private int second_gray_ary_text;
    private int text_splite_color;
    private int text_while_color;
    private int text_context_size;
    private int money_color_expense;
    private int money_color_incom;
    private int money_size;
    private float bitmap_with;
    private float bitmap_height;
    private float texttotalHeight;
    private float title_lenth;
    private FontMetrics fontMetrics;
    private float title_height;
    private float name_with;
    private String url;
    private boolean isDraLine = false;

    public AccountItemView(Context context) {
        this(context, null);
    }

    public AccountItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = MyApplicaction.getContext();
        text_title_color = getResources().getColor(R.color.context_color);
        text_title_size = (int) getResources().getDimension(R.dimen.context_text);
        text_context_color = getResources().getColor(R.color.secondary_text);
        second_gray_ary_text = getResources().getColor(R.color.second_gray_ary_text);
        text_while_color = getResources().getColor(R.color.white);
        text_splite_color = getResources().getColor(R.color.split_line);
        text_context_size = (int) getResources().getDimension(R.dimen.text_13);
        money_color_expense = getResources().getColor(R.color.expenditure);
        money_color_incom = getResources().getColor(R.color.income);
        money_size = (int) getResources().getDimension(R.dimen.contexts_titel_text);

        initPait();
        // bitmap.recycle();
    }

    private void initPait() {
        paint = new Paint();
        paint.setAntiAlias(true);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setTextSize(text_title_size);
        paint_text.setColor(text_title_color);
        paint_line = new Paint();
        paint_line.setAntiAlias(true);
        paint_line.setColor(getResources().getColor(R.color.split_line));

        money_paint_text = new Paint();
        money_paint_text.setAntiAlias(true);
        money_paint_text.setTextSize(text_title_size);
        money_paint_text.setColor(text_title_color);

    }

    public void setContent(int drawblwId, String title, String content, String date_time, String addOrSettment,
                           String IsclearText, String money, String payType, String billType, Boolean isDraLine) {
        if (billType.equals("1")) {
            money_paint_text.setColor(money_color_expense);
        } else if (billType.equals("0")) {
            money_paint_text.setColor(money_color_incom);
        } else if (billType.equals("4")) {
            money_paint_text.setColor(text_title_color);
        }
        this.bitmap = returnBitmap(drawblwId);
        this.text_title = title;
        this.text_context = content;
        this.date_time = date_time;
        this.addOrSettment = addOrSettment;
        this.pay_money = money;
        this.pay_stype = payType;
        this.IsclearText = IsclearText;
        this.isDraLine = isDraLine;
        if (TextUtils.isEmpty(text_title)) {
            text_title = "";
        }
        if (TextUtils.isEmpty(text_context)) {
            text_context = "";
        }
        if (TextUtils.isEmpty(date_time)) {
            date_time = "";
        }
        if (TextUtils.isEmpty(addOrSettment)) {
            addOrSettment = "";
        }
        if (TextUtils.isEmpty(pay_money)) {
            pay_money = "";
        }
        if (TextUtils.isEmpty(pay_stype)) {
            pay_stype = "";
        }
        postInvalidate();
    }

    public void setContent(String url, String title, String content, String date_time, String addOrSettment,
                           String IsclearText, String money, String payType, String billType, boolean isDraLine) {
        if (billType.equals("1")) {
            money_paint_text.setColor(money_color_expense);
        } else if (billType.equals("0")) {
            money_paint_text.setColor(money_color_incom);
        } else if (billType.equals("4")) {
            money_paint_text.setColor(text_title_color);
        }
        try {
            this.url = url;
            if (url != null) {
                if (url.contains("http")) {
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    new Thread(networkTask).start();
                    // this.bitmap = getImage(url);
                } else {
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    new Thread(file_thread).start();
                    // this.bitmap = getImageFile(url);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.text_title = title;
        this.text_context = content;
        this.date_time = date_time;
        this.addOrSettment = addOrSettment;
        this.pay_money = money;
        this.pay_stype = payType;
        this.IsclearText = IsclearText;
        this.isDraLine = isDraLine;
        if (TextUtils.isEmpty(text_title)) {
            text_title = "";
        }
        if (TextUtils.isEmpty(text_context)) {
            text_context = "";
        }
        if (TextUtils.isEmpty(date_time)) {
            date_time = "";
        }
        if (TextUtils.isEmpty(addOrSettment)) {
            addOrSettment = "";
        }
        if (TextUtils.isEmpty(pay_money)) {
            pay_money = "";
        }
        if (TextUtils.isEmpty(pay_stype)) {
            pay_stype = "";
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        init_left = UIHelper.Dp2PxFloat(context, 15);
        height = getMeasuredHeight();
        init_top = UIHelper.Dp2PxFloat(context, 15);

        if (bitmap != null) {
            bitmap_left = init_left;

            bitmap_with = UIHelper.Dp2Px(context, 35);
            bitmap_height = UIHelper.Dp2Px(context, 35);
            bitmap_top = height / 2 - bitmap_height / 2;
        }

    }

    private Bitmap returnBitmap(int drawbleId) {
        if (drawbleId == 0) {
            return null;
        }
        bitmap = BitmapFactory.decodeResource(getResources(), drawbleId);
        BitmapFactory.Options config = new BitmapFactory.Options();
        config.inJustDecodeBounds = true;
        config.inSampleSize = 1;
        config.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), drawbleId, config);
        // bitmap = Bitmap.createBitmap(UIHelper.Dp2Px(context, 35),
        // UIHelper.Dp2Px(context, 35), Bitmap.Config.RGB_565);
        // Bitmap.createBitmap(bitmap, 0, 0, UIHelper.Dp2Px(context, 35),
        // UIHelper.Dp2Px(context, 35));
        //

        return bitmap;

    }

    public Bitmap getImage() throws Exception {

        if (url != null) {
            URL url_a = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url_a.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            InputStream is = null;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
            } else {
                is = null;
            }
            if (is == null) {
                throw new RuntimeException("stream is null");
            } else {
                try {
                    byte[] data = readStream(is);
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        return bitmap;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is.close();
                return null;
            }
        }
        return null;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // UI界面的更新等相关操作
            switch (msg.what) {
                case 0:// 网络图片
                    postInvalidate();
                    break;
                case 1:// 本地图片
                    postInvalidate();
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            try {
                bitmap = getImage();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 0;

            handler.sendMessage(msg);
        }
    };
    /**
     * 网络操作相关的子线程
     */
    Runnable file_thread = new Runnable() {

        @Override
        public void run() {
            try {
                bitmap = getImageFile();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };

    /*
     * 得到图片字节流 数组大小
     */
    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public Bitmap getImageFile() throws Exception {
        Bitmap bitmap_a = null;
        if (url != null) {
            try {
                File mFile = new File(url);
                // 若该文件存在
                if (mFile.exists()) {
                    // bitmap_a = BitmapFactory.decodeFile(url, opt);
                    bitmap_a = decodeSampledBitmapFromFile(url, UIHelper.Dp2Px(context, 20),
                            UIHelper.Dp2Px(context, 20));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bitmap_a;

    }

    /**
     * @param options   参数
     * @param reqWidth  目标的宽度
     * @param reqHeight 目标的高度
     * @return
     * @description 计算图片的压缩比率
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     * @description 从SD卡上加载图片
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
    }

    /**
     * @param src
     * @param dstWidth
     * @param dstHeight
     * @return
     * @description 通过传入的bitmap，进行压缩，得到符合标准的bitmap
     */
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        draLine(canvas);
        draBitmap(canvas);
        draText(canvas);
    }

    private void draLine(Canvas canvas) {
        if (isDraLine) {
            canvas.drawLine(0, height - UIHelper.Dp2PxFloat(context, 1), with, height, paint_line);
        }

    }

    private void draText(Canvas canvas) {

        paint_text.setTextSize(text_context_size);

        float name_with = paint_text.measureText(date_time);
        fontMetrics = paint_text.getFontMetrics();
        float name_Height = fontMetrics.descent - fontMetrics.ascent;

        paint_text.setTextSize(text_title_size);
        paint_text.setColor(text_title_color);
        title_lenth = paint_text.measureText(text_title);
        fontMetrics = paint_text.getFontMetrics();
        title_height = fontMetrics.descent - fontMetrics.ascent;

        texttotalHeight = title_height + name_Height + UIHelper.Dp2Px(context, 10);
        float start_x = bitmap_left + bitmap_with + UIHelper.Dp2Px(context, 10);
        float start_y = (height - texttotalHeight) / 2f + title_height;
        float x = start_x + title_lenth;
        float y = start_y + name_Height / 4 + UIHelper.Dp2Px(context, 5);
        canvas.drawText(text_title, (int) start_x, (int) start_y, paint_text);

        paint_text.setColor(text_context_color);
        paint_text.setTextSize(text_context_size);
        if (!TextUtils.isEmpty(date_time)) {
            canvas.drawText(date_time, start_x, y + title_height, paint_text);
        }

        float time_with = paint_text.measureText(addOrSettment);
        float start_time_x = start_x + UIHelper.Dp2Px(context, 5) + name_with;
        float start_time_x_end = start_time_x + time_with + UIHelper.Dp2Px(context, 3);
        float start_time_y = y + title_height;
        float start_time_y_end = start_time_y + title_height / 4;
        RectF rect = new RectF(start_time_x - UIHelper.Dp2Px(context, 2), start_time_y - title_height / 2
                - UIHelper.Dp2Px(context, 5), start_time_x_end, start_time_y_end + UIHelper.Dp2Px(context, 2));
        if (!TextUtils.isEmpty(addOrSettment)) {
            paint_text.setColor(second_gray_ary_text);
            canvas.drawRoundRect(rect, UIHelper.Dp2Px(context, 5), UIHelper.Dp2Px(context, 5), paint_text);
            paint_text.setColor(text_while_color);
            canvas.drawText(addOrSettment, start_time_x, start_time_y, paint_text);
        }

        float IsclearText_with = paint_text.measureText(IsclearText);

        if (!TextUtils.isEmpty(IsclearText)) {
            float IsclearText_start = start_time_x_end + UIHelper.Dp2Px(context, 10);
            float IsclearText_end = IsclearText_start + IsclearText_with + UIHelper.Dp2Px(context, 3);

            RectF rectF = new RectF(start_time_x_end + UIHelper.Dp2Px(context, 5), y + title_height - title_height / 2
                    - UIHelper.Dp2Px(context, 5), IsclearText_end, y + title_height + title_height / 4
                    + UIHelper.Dp2Px(context, 2));
            paint_text.setColor(second_gray_ary_text);
            canvas.drawRoundRect(rectF, UIHelper.Dp2Px(context, 5), UIHelper.Dp2Px(context, 5), paint_text);
            paint_text.setColor(text_while_color);
            canvas.drawText(IsclearText, IsclearText_start, y + title_height, paint_text);
        }

        // paint_text.setColor(money_color_expense);
        paint_text.setColor(text_title_color);
        paint_text.setTextSize(money_size);
        float money_lenth = money_paint_text.measureText(pay_money);
        float money_left = with - UIHelper.Dp2Px(context, 15) - money_lenth;
        float money_top = start_y;
        fontMetrics = money_paint_text.getFontMetrics();
        float money_height = fontMetrics.descent - fontMetrics.ascent;

        canvas.drawText(pay_money, money_left, money_top + UIHelper.Dp2Px(context, 5), money_paint_text);

        paint_text.setColor(text_context_color);
        paint_text.setTextSize(text_context_size);
        float pay_stype_lenth = paint_text.measureText(pay_stype);
        canvas.drawText(pay_stype, with - UIHelper.Dp2Px(context, 15) - pay_stype_lenth, money_top + money_height
                + UIHelper.Dp2Px(context, 10), paint_text);
        if (!TextUtils.isEmpty(text_context)) {
            float text_context_with = paint_text.measureText(text_context);
            if (x + UIHelper.Dp2Px(context, 5) + text_context_with > money_left - UIHelper.Dp2Px(context, 10)) {
                float lenth = money_left - UIHelper.Dp2Px(context, 10) - x - UIHelper.Dp2Px(context, 5) - 3;
                int lenth_one = (int) (text_context_with / text_context.length());
                int position = (int) (lenth / lenth_one);
                text_context = text_context.substring(0, position) + "...";
            }
            canvas.drawText(text_context, x + UIHelper.Dp2Px(context, 5), start_y + UIHelper.Dp2Px(context, 1),
                    paint_text);
        }

    }

    private void draBitmap(Canvas canvas) {
        if (bitmap != null) {
            bitmap_left = init_left;
            bitmap_with = UIHelper.Dp2Px(context, 35);
            bitmap_height = UIHelper.Dp2Px(context, 35);
            bitmap_top = height / 2 - bitmap_height / 2;

            canvas.drawBitmap(bitmap, bitmap_left, bitmap_top, paint);
        }

    }

}
