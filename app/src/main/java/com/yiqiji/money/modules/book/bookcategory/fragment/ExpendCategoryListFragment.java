package com.yiqiji.money.modules.book.bookcategory.fragment;

import android.os.Bundle;

import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;

/**
 * Created by leichi on 2017/5/17.
 * 账单支出的分类列表
 */

public class ExpendCategoryListFragment extends BaseCategoryListFragment{


    public static ExpendCategoryListFragment newInstance(String accountBookId){
        ExpendCategoryListFragment fragment = new ExpendCategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBillType() {
        return DataConstant.EXPEND_BILL_TYPE;
    }
}
