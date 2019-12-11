package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.discover.manager.EventUtil;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by dansakai on 2017/7/28.
 * 一张大图
 */

public class BigImageItemView extends LinearLayout {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_readNm)
    TextView tvReadNm;
    @BindView(R.id.tv_subscribNm)
    TextView tvSubscribNm;
    @BindView(R.id.tv_commentNm)
    TextView tvCommentNm;

    private Context context;

    public BigImageItemView(Context context) {
        this(context, null);
    }

    public BigImageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_decorat_bigimg, this);
        ButterKnife.bind(this);
        initEvent();
    }
    BookCellModel bookCellModel;
    public void setData(BookCellModel bookCellModel) {
         this.bookCellModel=bookCellModel;
        tvTitle.setText(bookCellModel.getTitle());
        ImageLoaderManager.loadImage(bookCellModel.getImg(), ivPhoto);
        tvReadNm.setText(bookCellModel.getViewcount()+"");
        tvSubscribNm.setText(bookCellModel.getFollownum()+"");
        tvCommentNm.setText(bookCellModel.getCommentcount()+"");
    }

    private void initEvent() {
        setOnClickListener(new OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   EventUtil.OnBookItemClick(context,bookCellModel);
                               }
                           }
        );
    }
}
