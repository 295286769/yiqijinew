package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/18.
 * address huang.weishui@71dai.com
 */
public class DiaryImageView extends RelativeLayout {
    private Context mContext;
    @BindView(R.id.diary_image)
    ImageView diary_image;
    @BindView(R.id.diary_number)
    TextView diary_number;
    private float diary_number_size = 0;
    private int diary_number_color;
    private float diary_height_ratio;

    public DiaryImageView(Context context) {
        this(context, null);
    }

    public DiaryImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiaryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initStyle(attrs);
        initView();
    }

    private void initStyle(AttributeSet attributeSet) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attributeSet, R.styleable.DiaryImageViewStyle);
        if (typedArray != null) {
            diary_number_size = typedArray.getDimension(R.styleable.DiaryImageViewStyle_diary_number_size, 0);
            diary_height_ratio = typedArray.getFloat(R.styleable.DiaryImageViewStyle_diary_height_width_ratio, 0);
            diary_number_color = typedArray.getColor(R.styleable.DiaryImageViewStyle_diary_number_color, mContext.getResources().getColor(R.color.white));
            typedArray.recycle();
        }
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_renovation_diary_image, this, true);
        ButterKnife.bind(this, view);
//        diary_number.setTextColor(diary_number_color);
//        diary_number.setTextSize(diary_number_size);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = getMeasuredWidth();
        int height = (int) (with * diary_height_ratio);
        setMeasuredDimension(with, height);
    }

    public void setDiaryImage(String url) {
        if (diary_image != null) {
            ImageLoaderManager.loadImage(mContext, url, diary_image);
        }
    }

    public void setDiaryNumber(String diary) {
        if (diary_number != null) {
            diary_number.setText(diary);
        }
    }

}
