package com.yiqiji.money.modules.community.travel.model;

import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.community.model.BookGroupModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/8/2.
 */

public class TravelMainListModel implements Serializable{

    public String booktotal;
    public PlaceListModel place;
    public TabListModel tab;
    public BookGroupModel hotbook;

    public class PlaceListModel{
        public String title;
        public List<PlaceModel> list;
    }

    public class PlaceModel{
        public String title;
        public String img;
    }
}
