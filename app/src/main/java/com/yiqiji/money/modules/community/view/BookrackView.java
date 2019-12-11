package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示三个类似书架的账本view
 * Created by leichi on 2017/7/27.
 */

public class BookrackView extends LinearLayout {

    Context context;
    @BindView(R.id.book_first)
    BookCardView bookFirst;
    @BindView(R.id.book_second)
    BookCardView bookSecond;
    @BindView(R.id.book_three)
    BookCardView bookThree;


    List<BookCellModel> bookCellModelList;
    public BookrackView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BookrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.item_book_rack_view, this);
        ButterKnife.bind(this);
    }

    public void setViewData(List<BookCellModel> bookCellModelList) {
        bookFirst.setViewData(bookCellModelList.get(0));
        bookSecond.setViewData(bookCellModelList.get(1));
        bookThree.setViewData(bookCellModelList.get(2));
    }
}
