package com.yiqiji.money.modules.book.bookcategory.fragment;

import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategoryListAdapter;

/**
 * Created by leichi on 2017/5/28.
 */

public interface SubscibeDataFragment {

    boolean hidden();
    BookCategoryListAdapter getAdapter();
    void reloadData();
}
