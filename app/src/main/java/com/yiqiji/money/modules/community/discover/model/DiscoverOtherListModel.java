package com.yiqiji.money.modules.community.discover.model;

import com.yiqiji.money.modules.community.model.BookCellModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/8/2.
 */

public class DiscoverOtherListModel implements Serializable{

    public BookGroupModel accountbook;

    public class BookGroupModel{
        public String title;
        public List<BookCellModel> list;
    }
}
