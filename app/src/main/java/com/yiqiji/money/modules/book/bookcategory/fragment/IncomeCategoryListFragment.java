package com.yiqiji.money.modules.book.bookcategory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataParseAssembler;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.FragmentSubscibeDataHandler;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategoryListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leichi on 2017/5/17.
 * 账单收入的分类列表
 */

public class IncomeCategoryListFragment extends BaseCategoryListFragment{


    public static IncomeCategoryListFragment newInstance(String accountBookId){
        IncomeCategoryListFragment fragment = new IncomeCategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBillType() {
        return DataConstant.INCOME_BILL_TYPE;
    }
}
