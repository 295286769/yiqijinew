package com.yiqiji.money.modules.book.detailinfo.vd_driver;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillItemModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookInfoListMultipItem;
import com.yiqiji.money.modules.book.detailinfo.view.DiaryItemView;
import com.yiqiji.money.modules.common.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookInfoListAdapter extends BaseMultiItemQuickAdapter<BookInfoListMultipItem, BaseViewHolder> {


    public BookInfoListAdapter(Context context) {
        super(null);
        addItemType(BookInfoListMultipItem.ViewType.BILL.ordinal(), R.layout.item_book_bill_view, ItemBookBillViewHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_DIARY.ordinal(), R.layout.item_book_bill_diary_view, ItemBookDiaryViewHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_PAYMENT.ordinal(), R.layout.item_book_payment_view, BookBillPaymentHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_SETTLEMENT.ordinal(), R.layout.item_book_settlement_layout, BookBillSettlementHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_DATE.ordinal(), R.layout.item_book_bill_date_view, BookBillDateHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_RENOVATION.ordinal(), R.layout.item_book_renovation, ItemBookRenovationViewHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_RENOVATION_DIARY.ordinal(), R.layout.item_book_renovation_diary_base, ItemBookRenovationDiaryViewHolder.class);
        addItemType(BookInfoListMultipItem.ViewType.BILL_EMPTY.ordinal(), R.layout.item_empty_bill_list_data_layout, BaseViewHolder.class);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BookInfoListMultipItem item) {
        BookInfoListMultipItem.ViewType viewType = item.getItemTypeEnum();
        switch (viewType) {
            case BILL:
                ((ItemBookBillViewHolder) helper).bindViewData(item);
                break;
            case BILL_DATE:
                ((BookBillDateHolder) helper).bindViewData(item);
                break;
            case BILL_PAYMENT:
                ((BookBillPaymentHolder) helper).bindViewData(item);
                break;
            case BILL_SETTLEMENT:
                ((BookBillSettlementHolder) helper).bindViewData(item);
                break;
            case BILL_DIARY:
                ((ItemBookDiaryViewHolder) helper).bindViewData(item);
                break;
            case BILL_RENOVATION:
                ((ItemBookRenovationViewHolder) helper).bindViewData(item);
                break;
            case BILL_RENOVATION_DIARY:
                ((ItemBookRenovationDiaryViewHolder) helper).bindViewData(item);
                break;
        }
    }

    public class BookBillPaymentHolder extends BaseViewHolder {

        @BindView(R.id.blance)
        TextView blance;
        @BindView(R.id.tv_paymentCount)
        TextView tvPaymentCount;

        public BookBillPaymentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(final BookInfoListMultipItem item) {
            BookBillItemModel bookBillItemModel = (BookBillItemModel) item.getData();
            blance.setText(bookBillItemModel.billamount + "");
            tvPaymentCount.setText(bookBillItemModel.billcount + "人交款");
        }
    }

    public class BookBillSettlementHolder extends BaseViewHolder {

        @BindView(R.id.bill_settlementUserIcon)
        ImageView billSettlementUserIcon;
        @BindView(R.id.tv_settlementInfo)
        TextView tvSettlementInfo;

        public BookBillSettlementHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(final BookInfoListMultipItem item) {
            BookBillItemModel bookBillItemModel = (BookBillItemModel) item.getData();
            ImageLoaderManager.loadImage(mContext, bookBillItemModel.usericon, billSettlementUserIcon);
            tvSettlementInfo.setText(bookBillItemModel.username + "执行了全员结算");
        }
    }

    public class BookBillDateHolder extends BaseViewHolder {

        TextView tv_bill_date;

        public BookBillDateHolder(View view) {
            super(view);
            tv_bill_date = (TextView) view.findViewById(R.id.tv_bill_date);
        }

        public void bindViewData(final BookInfoListMultipItem item) {
            String time = item.getData().toString();
            tv_bill_date.setText(DateUtil.formatTime(time, "MM月dd日"));
        }
    }

    public class ItemBookRenovationDiaryViewHolder extends BaseViewHolder {
        @BindView(R.id.diaryItemView)
        DiaryItemView diaryItemView;

        public ItemBookRenovationDiaryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(final BookInfoListMultipItem item) {
            BookBillItemModel bookBillItemModel = (BookBillItemModel) item.getData();
            String JournalText = bookBillItemModel.billcatename;
            String billMark = bookBillItemModel.billmark;
            String diaryImage = "";
            List<String> imageList = bookBillItemModel.imglist;
            int size = 0;
            if (imageList != null) {
                size = imageList.size();
                if (size > 0) {
                    diaryImage = imageList.get(0);
                }
            }

            diaryItemView.setDiaryNumber(size + "张图片");
            diaryItemView.setDiaryImage(diaryImage);
            diaryItemView.setBillDiaryText(billMark);
            diaryItemView.setJournalText(JournalText);
        }
    }

}
