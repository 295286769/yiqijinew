package com.yiqiji.money.modules.Found.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/6/6.
 */

public class TabListModel implements Serializable {

    public String title;
    public List<TabModel> list;
    public static class TabModel {
        public String title;
        public String text;
        public String img;
        public String type;
        public String url;
        public String icon;
    }
}
