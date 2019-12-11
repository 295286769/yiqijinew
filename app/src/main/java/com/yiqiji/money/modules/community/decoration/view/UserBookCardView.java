package com.yiqiji.money.modules.community.decoration.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.view.BookCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by dansakai on 2017/8/3.
 */

public class UserBookCardView extends LinearLayout {


    @BindView(R.id.book_first)
    BookCardView bookFirst;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.tv_first)
    TextView tvFirst;

    public UserBookCardView(Context context) {
        this(context, null);
    }

    public UserBookCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.adapter_item_user_book, this);
        ButterKnife.bind(this);
    }

    public void setData(BookCellModel cellModel) {
        bookFirst.setViewData(cellModel);
        ImageLoaderManager.loadImage(cellModel.getUserinfo().getUsericon(), ivFirst);
        tvFirst.setText(cellModel.getUserinfo().getUsername());
    }
}
