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
 * Created by leichi on 2017/8/1.
 */

public class MultipleImageItemBookView extends LinearLayout {

    Context context;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.tv_readNm)
    TextView tvReadNm;
    @BindView(R.id.tv_subscribNm)
    TextView tvSubscribNm;
    @BindView(R.id.tv_commentNm)
    TextView tvCommentNm;

    public MultipleImageItemBookView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MultipleImageItemBookView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
        LayoutInflater.from(context).inflate(R.layout.item_multiple_image_book_view, this);
        ButterKnife.bind(this);
        initEvent();
        setBackgroundResource(R.color.white);
    }

    BookCellModel bookCellModel;

    public void setData(BookCellModel bookCellModel) {
        this.bookCellModel = bookCellModel;
        tvTitle.setText(bookCellModel.getTitle());
        ImageLoaderManager.loadImage(bookCellModel.getImglist().get(0), image1);
        ImageLoaderManager.loadImage(bookCellModel.getImglist().get(1), image2);
        ImageLoaderManager.loadImage(bookCellModel.getImglist().get(2), image3);
        tvReadNm.setText(bookCellModel.getViewcount() + "");
        tvSubscribNm.setText(bookCellModel.getFollownum() + "");
        tvCommentNm.setText(bookCellModel.getCommentcount() + "");
    }

    private void initEvent() {
        setOnClickListener(new OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   EventUtil.OnBookItemClick(context, bookCellModel);
                               }
                           }
        );
    }
}
