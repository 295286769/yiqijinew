package com.yiqiji.money.modules.community.travel.manager;

import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.community.discover.manager.DiscoverOtherListDataAssembler;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem;
import com.yiqiji.money.modules.community.discover.model.DiscoverMultipleItem.ViewType;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.model.BookGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/7/31.
 */

public class TravelListDataAssembler {

    public static List<DiscoverMultipleItem> getAssemblerMultipItem(BookGroupModel hotbook) {
        List<DiscoverMultipleItem> discoverMultipleItemList = new ArrayList<>();
        discoverMultipleItemList.add(getTitleMutipleItem(hotbook.title,0));
        discoverMultipleItemList.addAll(DiscoverOtherListDataAssembler.getAssemblerMultipItem(hotbook.list));
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
}

