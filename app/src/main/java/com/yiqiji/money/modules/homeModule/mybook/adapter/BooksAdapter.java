package com.yiqiji.money.modules.homeModule.mybook.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/6/21.
 * address huang.weishui@71dai.com
 */
public class BooksAdapter extends BaseQuickAdapter<BooksDbInfo, BooksAdapter.BooksViewHolder> {
    private Context mContext;
    private boolean isEdit = false;

    public BooksAdapter(Context context) {
        super(R.layout.books_list_item);
        this.mContext = context;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    protected void convert(BooksViewHolder helper, BooksDbInfo item) {
        helper.bindData(item);
    }

    public class BooksViewHolder extends BaseViewHolder {
        @BindView(R.id.books_item)
        RelativeLayout books_item;
        @BindView(R.id.books_bagroud)
        SelectableRoundedImageView books_bagroud;
        @BindView(R.id.select_image)
        ImageView select_image;
        @BindView(R.id.bookEdit)
        ImageView bookEdit;
        @BindView(R.id.new_books)
        ImageView new_books;
        @BindView(R.id.book_title)
        TextView book_title;
        @BindView(R.id.tv_book_title)
        TextView tv_book_title;
        @BindView(R.id.book_memeber_number)
        TextView book_memeber_number;
        @BindView(R.id.add_book_text)
        TextView add_book_text;

        public BooksViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            initOnClick();
        }


        public void bindData(BooksDbInfo booksDbInfo) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) books_bagroud.getLayoutParams();
            float width_image = (XzbUtils.getPhoneScreen((Activity) mContext).widthPixels - UIHelper.Dp2Px(mContext, 20) * 3) / 3;
            layoutParams.width = (int) width_image;
            layoutParams.height = (int) (width_image / 0.8f);
            books_bagroud.setLayoutParams(layoutParams);
            books_bagroud.setScaleType(ImageView.ScaleType.CENTER_CROP);
            add_book_text.setText("");
            book_memeber_number.setText("");
            book_title.setText("");
            tv_book_title.setText("");
            select_image.setVisibility(View.INVISIBLE);
            bookEdit.setVisibility(View.INVISIBLE);
            new_books.setVisibility(View.INVISIBLE);
            books_bagroud.setVisibility(View.VISIBLE);
            tv_book_title.setVisibility(View.VISIBLE);
            if (booksDbInfo != null) {
                int position = getAdapterPosition();
                String book_url = booksDbInfo.getAccountbookbgimg();
                String accountbookcateicon = booksDbInfo.getAccountbookcateicon();
                String mAccountbooktitle = booksDbInfo.getAccountbooktitle();
                String mAccountbookcount = booksDbInfo.getAccountbookcount();
                String mIsnew = booksDbInfo.getIsnew();
                if (TextUtils.isEmpty(book_url)) {
                    book_url = accountbookcateicon;
                }
                ImageLoaderManager.loadImage(mContext, book_url, R.drawable.sing_icon, books_bagroud);
                book_title.setText(mAccountbooktitle);
                tv_book_title.setText(mAccountbooktitle);
                if (position == 0) {
                    select_image.setVisibility(View.VISIBLE);
                }
                if (mIsnew.equals("1")) {
                    new_books.setVisibility(View.VISIBLE);
                }
                if (BooksDetailPerecenter.isAccountbookCount(mAccountbookcount)) {
                    book_memeber_number.setText("共" + mAccountbookcount + "人");
                }
                if (isEdit) {
//                    select_image.setVisibility(View.VISIBLE);
                    bookEdit.setVisibility(View.VISIBLE);

                }

            } else {
                ImageLoaderManager.loadImage(mContext, R.drawable.book_add, books_bagroud);
                if (isEdit) {
                    books_bagroud.setVisibility(View.INVISIBLE);
                    tv_book_title.setVisibility(View.INVISIBLE);
                } else {
                    add_book_text.setText("添加账本");
                    tv_book_title.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
