package com.yiqiji.money.modules.community.discover.manager;

import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem.ViewType;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverListDataAssembler {

    public static List<DiscoverMultipleItem> getAssemblerMultipItem(DiscoverListModel discoverListModel) {
        List<DiscoverMultipleItem> discoverMultipleItemList = new ArrayList<>();

        //添加热门账本title
        discoverMultipleItemList.add(getTitleMutipleItem(discoverListModel.hotbook.title,0));
        //热门账本第一个账本
        discoverMultipleItemList.add(getBookMutipleItem(discoverListModel.hotbook.list.get(0),ViewType.BOOK_TEXT_IMAGE));
        //添加书架view
        List<BookCellModel> list=discoverListModel.hotbook.list.subList(1,4);
        discoverMultipleItemList.add(getBookrackMutipleItem(list));

        //添加TabListView
        if(discoverListModel.tab!=null&&discoverListModel.tab.list!=null&&discoverListModel.tab.list.size()>0)
        discoverMultipleItemList.add(getTabListMutipleItem(discoverListModel.tab));

        //添加时光账本title
        discoverMultipleItemList.add(getTitleMutipleItem(discoverListModel.travelbook.title,1));
        //添加时光第一个账本
        discoverMultipleItemList.add(getBookMutipleItem(discoverListModel.travelbook.list.get(0),ViewType.BOOK_SINGLE_IMAGE));
        //添加时光账本书架
        List<BookCellModel> travelList=discoverListModel.travelbook.list.subList(1,4);
        discoverMultipleItemList.add(getBookrackMutipleItem(travelList));


        //添加房屋装修账本title
        discoverMultipleItemList.add(getTitleMutipleItem(discoverListModel.housebook.title,2));
        //添加房屋装修第一个账本
        discoverMultipleItemList.add(getBookMutipleItem(discoverListModel.housebook.list.get(0),ViewType.BOOK_SINGLE_IMAGE));
        //添加房屋装修账本书架
        List<BookCellModel> housebookList=discoverListModel.housebook.list.subList(1,4);
        discoverMultipleItemList.add(getBookrackMutipleItem(housebookList));


        //添加其他账本title
        discoverMultipleItemList.add(getTitleMutipleItem(discoverListModel.otherbook.title,3));
        //循环添加其他账本
        for(BookCellModel bookCellModel:discoverListModel.otherbook.list){
            discoverMultipleItemList.add(DiscoverOtherListDataAssembler.getBookMutipleItem(bookCellModel));
        }
        return discoverMultipleItemList;
    }


    private static DiscoverMultipleItem getTitleMutipleItem(String name,int type){
        DiscoverMultipleItem discoverMultipleItem=new DiscoverMultipleItem();
        discoverMultipleItem.setViewType(ViewType.TITLE);
        TitleModel titleModel=new TitleModel();
        titleModel.titleName=name;
        titleModel.type=type;
        discoverMultipleItem.setData(titleModel);
        return discoverMultipleItem;
    }

    private static DiscoverMultipleItem getBookMutipleItem(BookCellModel bookCellModel,ViewType viewType){
        DiscoverMultipleItem discoverMultipleItem=new DiscoverMultipleItem();
        discoverMultipleItem.setViewType(viewType);
        discoverMultipleItem.setData(bookCellModel);
        return discoverMultipleItem;
    }

    private static DiscoverMultipleItem getBookrackMutipleItem(List<BookCellModel> bookCellModelList){
        DiscoverMultipleItem discoverMultipleItem=new DiscoverMultipleItem();
        discoverMultipleItem.setViewType(ViewType.BOOK_RACK);
        discoverMultipleItem.setData(bookCellModelList);
        return discoverMultipleItem;
    }

    private static DiscoverMultipleItem getTabListMutipleItem(TabListModel tabListModel){
        DiscoverMultipleItem discoverMultipleItem=new DiscoverMultipleItem();
        discoverMultipleItem.setViewType(ViewType.TAB_LIST);
        discoverMultipleItem.setData(tabListModel);
        return discoverMultipleItem;
    }


}

