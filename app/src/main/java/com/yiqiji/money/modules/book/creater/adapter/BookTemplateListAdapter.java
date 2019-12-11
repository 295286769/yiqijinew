package com.yiqiji.money.modules.book.creater.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem;
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem.ViewType;
import com.yiqiji.money.modules.book.creater.model.BookCoverModel;
import com.yiqiji.money.modules.community.view.BookCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/27.
 */

public class BookTemplateListAdapter extends BaseMultiItemQuickAdapter<BookCoverListMultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BookTemplateListAdapter(List<BookCoverListMultipleItem> data) {
        super(data);
        addItemType(ViewType.BOOK_AA_TYPE_TITLE.ordinal(), R.layout.item_book_type_title_view, BookTypeTitleViewHolder.class);
        addItemType(ViewType.BOOK_NO_AA_TYPE_TITLE.ordinal(), R.layout.item_book_type_title_view, BookTypeTitleViewHolder.class);
        addItemType(ViewType.BOOK_COVER_MODEL.ordinal(), R.layout.adapter_item_book_card_view, BookCoverViewHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookCoverListMultipleItem item) {
        ViewType viewType = item.getViewType();
        switch (viewType) {
            case BOOK_AA_TYPE_TITLE:
                ((BookTypeTitleViewHolder) helper).bindViewData("AA账本", "好友之间结账，不再有糊涂账");
                break;
            case BOOK_NO_AA_TYPE_TITLE:
                ((BookTypeTitleViewHolder) helper).bindViewData("账本", "支持单人或多人共同记账");
                break;
            case BOOK_COVER_MODEL:
                ((BookCoverViewHolder) helper).bindViewData(item);
                break;
        }
    }


    public class BookCoverViewHolder extends BaseViewHolder {

        @BindView(R.id.book_cardView)
        BookCardView bookCardView;

        public BookCoverViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(BookCoverListMultipleItem item) {
            BookCoverModel bookCoverModel = (BookCoverModel) item.getData();
            bookCardView.bindViewData(item);
        }
    }

    public class BookTypeTitleViewHolder extends BaseViewHolder {

        @BindView(R.id.book_type)
        TextView tvBookType;
        @BindView(R.id.book_describe)
        TextView tvBookDescribe;

        public BookTypeTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(String bookType, String bookDescribe) {
            tvBookType.setText(bookType);
            tvBookDescribe.setText(bookDescribe);
        }
    }
}
