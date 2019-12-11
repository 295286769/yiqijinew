package com.yiqiji.money.modules.community.discover.manager;

import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem.ViewType;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverOtherListDataAssembler {

    public static List<DiscoverMultipleItem> getAssemblerMultipItem(List<BookCellModel> list) {
        List<DiscoverMultipleItem> discoverMultipleItemList = new ArrayList<>();
        //循环添加其他账本
        for(BookCellModel bookCellModel:list){
            discoverMultipleItemList.add(getBookMutipleItem(bookCellModel));
        }
        return discoverMultipleItemList;
    }

    public static DiscoverMultipleItem getBookMutipleItem(BookCellModel bookCellModel){
        ViewType viewType;
        if(bookCellModel.getImg()==null||bookCellModel.getImg().length()==0){
            viewType=ViewType.BOOK_SINGLE_TEXT;
        }else if(bookCellModel.getImglist()!=null&&bookCellModel.getImglist().size()>=3){
            viewType=ViewType.BOOK_MULTIPLE_IMAGE;
        }else if(bookCellModel.getText()==null||bookCellModel.getText().length()==0){
            viewType=ViewType.BOOK_SINGLE_IMAGE;
        }else {
            viewType=ViewType.BOOK_TEXT_IMAGE;
        }

        DiscoverMultipleItem discoverMultipleItem=new DiscoverMultipleItem();
        discoverMultipleItem.setViewType(viewType);
        discoverMultipleItem.setData(bookCellModel);
        return discoverMultipleItem;
    }

}

