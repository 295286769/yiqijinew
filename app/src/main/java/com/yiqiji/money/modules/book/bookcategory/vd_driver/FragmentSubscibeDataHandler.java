package com.yiqiji.money.modules.book.bookcategory.vd_driver;

import com.yiqiji.money.modules.book.bookcategory.fragment.SubscibeDataFragment;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryOperationType;
import com.yiqiji.money.modules.book.bookcategory.model.OperaBookCategoryResult;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/5/28.
 */

public class FragmentSubscibeDataHandler {

    SubscibeDataFragment fragment;
    public FragmentSubscibeDataHandler(SubscibeDataFragment fragment){
          this.fragment=fragment;
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(OperaBookCategoryResult bookCategoryResult) {
        if(!fragment.hidden()){
            switch (bookCategoryResult.operaHierarchy){
                case 1:
                    if(bookCategoryResult.mInstruct== BookCategoryOperationType.TO_EDIT){
                        fragment.reloadData();
                    }else {
                        fragment.getAdapter().insertGroup(bookCategoryResult.baseBookCategory);
                    }
                    break;
                case 2:
                    if(bookCategoryResult.mInstruct==BookCategoryOperationType.TO_EDIT){
                        fragment.getAdapter().modifyChild(bookCategoryResult.baseBookCategory);
                    }else {
                        fragment.getAdapter().insertChild(bookCategoryResult.baseBookCategory);
                    }
                    break;
            }
        }
    }

    public void unSubscibe(){
        EventBus.getDefault().unregister(this);
    }

}
