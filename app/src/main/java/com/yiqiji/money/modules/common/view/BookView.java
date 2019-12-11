package com.yiqiji.money.modules.common.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.io.File;

public class BookView extends View {
    private float with;
    private float height;

    private Context mContext;
    // 背景 账本bitmap
    private Bitmap bgbitmap = null;

    // 选中图标
    private Bitmap selectBitmap;

    // 多人图标
    private Bitmap morePeopleBitmap;
    // 新图标
    private Bitmap newsBitmap;
    // 编辑背景图标
    private Bitmap BookAccount_editBitmap;

    private Bitmap BookAccount_modify;

    private Paint paint_bitmap;

    //账本名称
    private Paint paint_text;

    //人数
    private Paint people_text;

    private float BookAccount_content_x;
    private float BookAccount_content_y;

    public BookView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private BooksDbInfo mbookAccount;
    private boolean isEdit;

    private boolean isAdd;

    private boolean isSelected;
    private Context context;

    public BookView(Context context, BooksDbInfo mbookAccount, boolean isEdit, boolean isAdd, boolean isSelected) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.mbookAccount = mbookAccount;
        this.isEdit = isEdit;
        this.isAdd = isAdd;
        this.isSelected = isSelected;
    }

    private void init(boolean isEdit) {

        paint_bitmap = new Paint();
        paint_bitmap.setAntiAlias(true);
        paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setTextSize(getResources().getDimension(R.dimen.context_text));
        paint_text.setColor(getResources().getColor(R.color.white));


        people_text = new Paint();
        people_text.setAntiAlias(true);
        people_text.setTextSize(getResources().getDimension(R.dimen.text_12));
        people_text.setColor(getResources().getColor(R.color.white));


        String drawableUrl = "book_icon_" + mbookAccount.getAccountbookcate();
        int id = getResources().getIdentifier(drawableUrl, "drawable", XzbUtils.getPackageInfo(context));
        if (id > 0) {
            bgbitmap = BitmapFactory.decodeResource(getResources(), id);
        }

        if (bgbitmap == null) {
            String imageurl = DownUrlUtil.bookimage + mbookAccount.getAccountbookcate() + "_bg";
            try {
                bgbitmap = getImageFile(imageurl);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        selectBitmap = returnBitmap(R.drawable.book_selected);
        //    morePeopleBitmap = returnBitmap(R.drawable.book_more);
        newsBitmap = returnBitmap(R.drawable.news);
        // 为true则是编辑状态  isEdit
        if (isEdit) {
            //   BookAccount_editBitmap = returnBitmap(R.drawable.book_my_mb);
            BookAccount_modify = returnBitmap(R.drawable.book_edit);
        }
    }

    public Bitmap getImageFile(String url) throws Exception {
        Bitmap bitmap_a = null;
        File mFile;
        if (url != null) {
            mFile = new File(url);
            // 若该文件存在
            if (mFile.exists()) {
                return XzbUtils.decodeSampledBitmapFromResource(url, (int) with, (int) height);
            }
        }
        return bitmap_a;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        height = getMeasuredHeight();
        BookAccount_content_y = height / 4;
        new_bitmap_width = (int) with;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        init(isEdit);
        draBitmap(canvas);
        drawText(canvas);
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

    private void drawText(Canvas canvas) {

        float BookAccount_content_lenth = paint_text.measureText(mbookAccount.getAccountbooktitle());
        // name的起始Y坐标是2/5处开始
        String accountbooktitle = StringUtils.onHideText(mbookAccount.getAccountbooktitle(), 5);
        if (mbookAccount.getAccountbooktitle().length() > 5) {
            BookAccount_content_lenth = paint_text.measureText(accountbooktitle);
        }
        BookAccount_content_x = new_bitmap_width / 2 - BookAccount_content_lenth / 2;
        canvas.drawText(accountbooktitle, BookAccount_content_x, BookAccount_content_y, paint_text);

        // 计算这个paint所设置的文字的高度
        FontMetrics fm = paint_text.getFontMetrics();
        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
        String mAccountbookcount = mbookAccount.getAccountbookcount();
        if (!TextUtils.isEmpty(mAccountbookcount) && Integer.parseInt(mAccountbookcount) > 0) {
            float lenth = people_text.measureText("共" + mbookAccount.getAccountbookcount() + "人");
            int x = (int) (new_bitmap_width / 2 - lenth / 2);
            // name的起始Y坐标是2/5处开始
            canvas.drawText("共" + mbookAccount.getAccountbookcount() + "人", x, BookAccount_content_y + textHeight, people_text);

        }
    }

    private int new_bitmap_width;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    private void draBitmap(Canvas canvas) {
        if (bgbitmap != null) {

            Bitmap new_bitmap = zoomImg(bgbitmap, (int) with, (int) height);
            new_bitmap = BitmapUtil.compressImage(new_bitmap);
            canvas.drawBitmap(new_bitmap, 0, 0, paint_bitmap);
            bgbitmap = null;
        }
        // 选中图标
        if (isSelected && selectBitmap != null) {

            int bitmapWidth = selectBitmap.getWidth();
            canvas.drawBitmap(selectBitmap, with - bitmapWidth - 10, 10, paint_bitmap);
        }

        // 新图标
        if (mbookAccount.getIsnew().equals("1") && newsBitmap != null) {
            int bitmapWidth = newsBitmap.getWidth();
            canvas.drawBitmap(newsBitmap, 20, 10, paint_bitmap);
        }

        if (isEdit) {

            int bitmapWidth = BookAccount_modify.getWidth();
            int bitmapHeight = BookAccount_modify.getHeight();
            float modify_x = new_bitmap_width / 2 - bitmapWidth / 2;
            float modify_y = height / 2 - bitmapHeight / 2;
            canvas.drawBitmap(BookAccount_modify, modify_x, modify_y, paint_bitmap);
        }
    }


    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
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
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }
}
