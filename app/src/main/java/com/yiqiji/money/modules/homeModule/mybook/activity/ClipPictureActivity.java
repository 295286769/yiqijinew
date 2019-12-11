package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.homeModule.mybook.view.ClipImageLayout;
import com.yiqiji.money.modules.homeModule.mybook.view.ClipView;

import java.io.ByteArrayOutputStream;

/**
 * Created by ${huangweishui} on 2017/6/15.
 * address huang.weishui@71dai.com
 */
public class ClipPictureActivity extends BaseActivity implements
        View.OnClickListener {
    private ImageView srcPic;
    private ImageView trant_image;
    private TextView sure, cannel;
    private ClipView clipview;
    private ClipImageLayout clipImageLayout;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    /**
     * 动作标志：无
     */
    private static final int NONE = 0;
    /**
     * 动作标志：拖动
     */
    private static final int DRAG = 1;
    /**
     * 动作标志：缩放
     */
    private static final int ZOOM = 2;
    /**
     * 初始化动作标志
     */
    private int mode = NONE;

    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;

    private Bitmap bitmap;
    private String url_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_picture);
        url_path = getIntent().getStringExtra("path");
        srcPic = (ImageView) this.findViewById(R.id.src_pic);
        clipImageLayout = (ClipImageLayout) this.findViewById(R.id.clipImageLayout);
//        srcPic.setOnTouchListener(this);

//        ViewTreeObserver observer = clipImageLayout.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @SuppressWarnings("deprecation")
//            public void onGlobalLayout() {
//                clipImageLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                initClipView(srcPic.getTop());
//            }
//        });

        sure = (TextView) this.findViewById(R.id.sure);
        cannel = (TextView) this.findViewById(R.id.cannel);
        trant_image = (ImageView) this.findViewById(R.id.trant_image);
        sure.setOnClickListener(this);
        cannel.setOnClickListener(this);
        bitmap = BitmapFactory.decodeFile(url_path);
        clipImageLayout.setImageBitmap(bitmap);
    }

    /**
     * 初始化截图区域，并将源图按裁剪框比例缩放
     *
     * @param top
     */
    private void initClipView(int top) {


        clipview = new ClipView(ClipPictureActivity.this);
        clipview.setCustomTopBarHeight(top);
        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {

            public void onDrawCompelete() {
                clipview.removeOnDrawCompleteListener();
//                int clipHeight = clipview.getClipHeight();
//                int clipWidth = clipview.getClipWidth();
//                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
//                int midY = clipview.getClipTopMargin() + (clipHeight / 2);
//
//                int imageWidth = bitmap.getWidth();
//                int imageHeight = bitmap.getHeight();
//                // 按裁剪框求缩放比例
//                float scale = (clipWidth * 1.0f) / imageWidth;
//                if (imageWidth > imageHeight) {
//                    scale = (clipHeight * 1.0f) / imageHeight;
//                }
//
//                // 起始中心点
//                float imageMidX = imageWidth * scale / 2;
//                float imageMidY = clipview.getCustomTopBarHeight()
//                        + imageHeight * scale / 2;
//                srcPic.setScaleType(ImageView.ScaleType.MATRIX);
//
//                // 缩放
//                matrix.postScale(scale, scale);
//                // 平移
//                matrix.postTranslate(midX - imageMidX, midY - imageMidY);

//                srcPic.setImageMatrix(matrix);
//                srcPic.setImageBitmap(bitmap);
                clipImageLayout.setImageBitmap(bitmap);
            }
        });

//        this.addContentView(clipview, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

//    public boolean onTouch(View v, MotionEvent event) {
//        ImageView view = (ImageView) v;
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                savedMatrix.set(matrix);
//                // 设置开始点位置
//                start.set(event.getX(), event.getY());
//                mode = DRAG;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                oldDist = spacing(event);
//                if (oldDist > 10f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                mode = NONE;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mode == DRAG) {
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - start.x, event.getY()
//                            - start.y);
//                } else if (mode == ZOOM) {
//                    float newDist = spacing(event);
//                    if (newDist > 10f) {
//                        matrix.set(savedMatrix);
//                        float scale = newDist / oldDist;
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                    }
//                }
//                break;
//        }
//        view.setImageMatrix(matrix);
//        return true;
//    }

    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                Bitmap clipBitmap = clipImageLayout.clip();
                clipBitmap = BitmapUtil.compressImage(clipBitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();

                Intent intent = new Intent();
                intent.setClass(this, AddBookActivity.class);
                intent.putExtra("bitmap", bitmapByte);

                startActivity(intent);
                break;
            case R.id.cannel:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 获取裁剪框内截图
     *
     * @return
     */
    private Bitmap getBitmap() {
        // 获取截屏
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bitmap = ((BitmapDrawable) srcPic.getDrawable()).getBitmap();
//        float with = bitmap.getWidth();
//        Log.i("tag", "-----+" + with);
        Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
                clipview.getClipLeftMargin(), clipview.getClipTopMargin()
                        + statusBarHeight, clipview.getClipWidth(),
                clipview.getClipHeight());

        // 释放资源
        view.destroyDrawingCache();
        return finalBitmap;
    }
}
