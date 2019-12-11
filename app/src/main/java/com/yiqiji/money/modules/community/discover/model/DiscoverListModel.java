package com.yiqiji.money.modules.community.discover.model;

import com.yiqiji.money.modules.Found.entity.BannerAdEntity;
import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.community.model.BookGroupModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverListModel implements Serializable{

    public List<BannerAdEntity> ads;
    public BookGroupModel hotbook;
    public BookGroupModel travelbook;
    public BookGroupModel housebook;
    public BookGroupModel otherbook;
    public TabListModel tab;
    public List<TabListModel.TabModel> nav;

}
