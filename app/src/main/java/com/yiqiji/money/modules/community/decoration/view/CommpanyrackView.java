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
 * Created by dansakai on 2017/8/2.
 */

public class CommpanyrackView extends LinearLayout {

    @BindView(R.id.book_first)
    BookCardView bookFirst;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.tv_first)
    TextView tvFirst;
    @BindView(R.id.book_second)
    BookCardView bookSecond;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.book_third)
    BookCardView bookThird;
    @BindView(R.id.iv_third)
    ImageView ivThird;
    @BindView(R.id.tv_third)
    TextView tvThird;

    public CommpanyrackView(Context context) {
        this(context, null);
    }

    public CommpanyrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_commpany_rack_view, this);
        ButterKnife.bind(this);
    }

    public void setData(List<BookCellModel> listBookCell) {
        bookFirst.setViewData(listBookCell.get(0));
        ImageLoaderManager.loadImage(listBookCell.get(0).getUserinfo().getUsericon(), ivFirst);
        tvFirst.setText(listBookCell.get(0).getUserinfo().getUsername());

        bookSecond.setViewData(listBookCell.get(1));
        ImageLoaderManager.loadImage(listBookCell.get(1).getUserinfo().getUsericon(), ivSecond);
        tvSecond.setText(listBookCell.get(1).getUserinfo().getUsername());

        bookThird.setViewData(listBookCell.get(2));
        ImageLoaderManager.loadImage(listBookCell.get(2).getUserinfo().getUsericon(), ivThird);
        tvThird.setText(listBookCell.get(2).getUserinfo().getUsername());

    }
}
