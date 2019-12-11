package com.yiqiji.money.modules.book.bookcategory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataController;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataParseAssembler;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.FragmentSubscibeDataHandler;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategoryListAdapter;
import com.yiqiji.money.modules.book.view.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leichi on 2017/5/17.
 * 支出的分类列表
 */

public abstract class BaseCategoryListFragment extends BaseFragment implements SubscibeDataFragment{

    @BindView(R.id.ryv_book_expand_category)
    RecyclerView ryvBookExpandCategory;

    Unbinder unbinder;
    String accountBoolId;
    BookCategoryListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    BookCategory bookCategory;

    boolean hidden;

    FragmentSubscibeDataHandler subscibeDataHandler;
    RetryDialogHandler retryDialogHandler;



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden=hidden;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBoolId=getArguments().getString(DataConstant.BUNDLE_KEY_BOOK_ID);
        subscibeDataHandler=new FragmentSubscibeDataHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_book_category_layout, null);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookCategory= DataLocalPersistencer.getLocalBookCategory(accountBoolId,getBillType());
        retryDialogHandler=new RetryDialogHandler(getActivity());
        initAdapter();
        initEvent();
    }

    private void initAdapter(){
        adapter=new BookCategoryListAdapter(getActivity(), DataParseAssembler.getInstance().assemblerBookCategoryForAdapter(bookCategory));
        ryvBookExpandCategory.setAdapter(adapter);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 5);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType=adapter.getItemViewType(position);
                int groupViewType= BookCategoryListMultipleItem.ViewType.GROUP.ordinal();
                int cutlineViewType=BookCategoryListMultipleItem.ViewType.CUTLINE.ordinal();
                return viewType==groupViewType||viewType==cutlineViewType?manager.getSpanCount():1;
            }
        });
        ryvBookExpandCategory.setLayoutManager(manager);
        mItemDragAndSwipeCallback=new ItemDragAndSwipeCallback(adapter);
        mItemTouchHelper=new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(ryvBookExpandCategory);
    }

    private void initEvent(){
        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        subscibeDataHandler.unSubscibe();
    }

    public void refreshData(){
        bookCategory= DataLocalPersistencer.getLocalBookCategory(accountBoolId,getBillType());
        adapter.setNewData(DataParseAssembler.getInstance().assemblerBookCategoryForAdapter(bookCategory));
    }

    public void reloadData(){
        DataController.getAccountBookCategoryByAccountBookId(accountBoolId, new ViewCallBack<BookCategoryModel>() {
            @Override
            public void onSuccess(BookCategoryModel bookCategoryModel) throws Exception {
                super.onSuccess(bookCategoryModel);
                refreshData();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                retryDialogHandler.showRetryDialog(simleMsg.getErrMsg());
            }
        });
    }

    public abstract int getBillType();


    /**
     * 保存排序结果到文件
     */
    public void saveSortData(){
        DataLocalPersistencer.saveSortBookCategoryMap(accountBoolId,adapter.getData(),getBillType());
    }

    @Override
    public boolean hidden() {
        return hidden;
    }

    @Override
    public BookCategoryListAdapter getAdapter() {
        return adapter;
    }
}
