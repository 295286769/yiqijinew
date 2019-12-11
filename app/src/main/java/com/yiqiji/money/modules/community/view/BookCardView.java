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
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem;
import com.yiqiji.money.modules.book.creater.model.BookCoverModel;
import com.yiqiji.money.modules.book.view.AdvancedCardView;
import com.yiqiji.money.modules.community.discover.manager.EventUtil;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/7/28.
 */

public class BookCardView extends LinearLayout {

    Context context;
    @BindView(R.id.image_bookCover)
    ImageView imageBookCover;
    @BindView(R.id.book_name)
    TextView bookName;
    @BindView(R.id.tv_book_title)
    TextView tvBookTitle;
    @BindView(R.id.book_card_view)
    AdvancedCardView bookCardView;

    public BookCardView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BookCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.item_book_card_view, this);
        ButterKnife.bind(this);
    }

    BookCellModel bookCellModel;
    public void setViewData(BookCellModel bookCellModel) {
        this.bookCellModel=bookCellModel;
        ImageLoaderManager.loadImage(bookCellModel.getImg(), imageBookCover);
        tvBookTitle.setText(bookCellModel.getTitle());
        bookName.setText(bookCellModel.getText());
        initEvent();
    }

    public void bindViewData(BookCoverListMultipleItem item) {
        BookCoverModel bookCoverModel = (BookCoverModel) item.getData();
        ImageLoaderManager.loadImage(bookCoverModel.categoryicon, imageBookCover);
        tvBookTitle.setText(bookCoverModel.categorytitle);
        bookName.setText(bookCoverModel.categorytitle);
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
