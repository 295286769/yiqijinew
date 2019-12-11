package com.yiqiji.money.modules.book.bookcategory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataController;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataOrderCategoryAssembler;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategorySortListAdapter;
import com.yiqiji.money.modules.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/5/17.
 */

public class BookCategoryOrderActivity extends BaseActivity {

    @BindView(R.id.ryv_book_category_order)
    RecyclerView ryvBookCategoryOrder;
    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;

    private int billType;
    private String accountBookId;
    private BookCategorySortListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    private RetryDialogHandler retryDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category_order_layout);
        ButterKnife.bind(this);
        retryDialogHandler = new RetryDialogHandler(this);
        initData();
        initEvent();
    }

    private void initIntentData() {
        accountBookId = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_ID);
        billType = getIntent().getIntExtra(DataConstant.BUNDLE_KEY_BOOK_TYPE, -1);
        if (billType == -1) {
            closeActivity();
        }
    }

    private void initData() {
        initIntentData();
        getAccountBookCategoryData();
    }

    private void initEvent() {
        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountBookCategoryData();
            }
        });

        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerViewData(BookCategory bookCategory) {
        adapter = new BookCategorySortListAdapter(this, DataOrderCategoryAssembler.getBookCategoryListMultipleItemList(bookCategory));
        ryvBookCategoryOrder.setAdapter(adapter);
        final GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                int cutlineViewType = BookCategoryListMultipleItem.ViewType.CUTLINE.ordinal();
                return viewType == cutlineViewType ? manager.getSpanCount() : 1;
            }
        });
        ryvBookCategoryOrder.setLayoutManager(manager);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(ryvBookCategoryOrder);
    }

    private void getAccountBookCategoryData() {
        showLoadingDialog();
        DataController.getAccountBookCategoryByAccountBookId(accountBookId, new ViewCallBack<BookCategoryModel>() {
            @Override
            public void onSuccess(BookCategoryModel bookCategoryModel) throws Exception {
                super.onSuccess(bookCategoryModel);
                dismissDialog();
                initRecyclerViewData(DataLocalPersistencer.getLocalBookCategory(accountBookId, billType));
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                retryDialogHandler.showRetryDialog(simleMsg.getErrMsg());
            }
        });
    }

    @Override
    public void finish() {
        DataLocalPersistencer.saveSortBookForGroupCategoryMap(accountBookId, adapter.getData(), billType);
        DataLocalPersistencer.updateBookCategoryJSONObject(accountBookId);
        setResult(Activity.RESULT_OK);
        super.finish();
    }

    private void closeActivity(){
        setResult(Activity.RESULT_CANCELED);
        super.finish();
    }


    public static void open(Activity context, String accountBookId, int billType) {
        Intent intent = new Intent();
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_TYPE, billType);
        intent.setClass(context, BookCategoryOrderActivity.class);
        context.startActivityForResult(intent, DataConstant.REQUEST_CODE_CATEGORY_ORDER);
    }

}
