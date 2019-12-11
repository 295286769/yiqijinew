package com.yiqiji.money.modules.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;

public class AddBookView extends View {
    private float with;
    private float height;
    private Context mContext;
    private Bitmap addBitmap;
    private Bitmap addBitmapBackground;
    private Paint paint_text;
    private Paint paint_bitmap;

    public AddBookView(Context context) {
        super(context);
        init();
    }

    public AddBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint_bitmap = new Paint();
        paint_bitmap.setAntiAlias(true);

        paint_text = new Paint();
        paint_text.setAntiAlias(true);

        paint_text.setTextSize(getResources().getDimension(R.dimen.context_text));
        paint_text.setColor(getResources().getColor(R.color.secondary_text));

        addBitmap = returnBitmap(R.drawable.book_add);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        height = getMeasuredHeight();

    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        if (addBitmap != null) {
            Bitmap new_bitmap = zoomImg(addBitmap, (int) with, (int) height);
            canvas.drawBitmap(new_bitmap, 0, 0, paint_bitmap);
        }
        // 要放到下面
        float lenth = paint_text.measureText("添加账本");
        float x = with / 2 - lenth / 2;
        canvas.drawText("添加账本", x, (height / 3) * 2, paint_text);
    }

    private Bitmap returnBitmap(int drawbleId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawbleId);
        BitmapFactory.Options config = new BitmapFactory.Options();
        config.inJustDecodeBounds = true;
        config.inSampleSize = 1;
        config.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), drawbleId, config);
        return bitmap;

    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片 www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
