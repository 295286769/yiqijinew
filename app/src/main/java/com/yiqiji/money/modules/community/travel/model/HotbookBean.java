package com.yiqiji.money.modules.community.travel.model;

import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class HotbookBean {
    /**
     * title : 热门共享账本
     * list : [{"title":"游走最纯粹的净土，稻城五日行","text":"''","img":"http://cloud.test.yiqijiba.com/advert/1501234782310196.jpg","imglist":["http://cloud.test.yiqijiba.com/advert/1501234782310196.jpg"],"url":"ziniuapp://money/accountbook/67039/share/","type":"1","viewcount":1,"follownum":0,"commentcount":0},{"title":"烟台长岛春日短歌行，人生最爱的一次旅行","text":"''","img":"http://cloud.test.yiqijiba.com/advert/1501234614232382.jpg","imglist":["http://cloud.test.yiqijiba.com/advert/1501234614232382.jpg"],"url":"ziniuapp://money/accountbook/67059/share/","type":"1","viewcount":1,"follownum":0,"commentcount":0},{"title":"苏州行,一个人的烟雨江南","text":"''","img":"http://cloud.test.yiqijiba.com/advert/1501234524553919.jpg","imglist":["http://cloud.test.yiqijiba.com/advert/1501234524553919.jpg","http://cloud.test.yiqijiba.com/advert/1501234526698062.jpg"],"url":"ziniuapp://money/accountbook/66911/share/","type":"1","viewcount":1,"follownum":0,"commentcount":0},{"title":"一城山色半城湖\u2014\u2014济南","text":"''","img":"","imglist":[],"url":"ziniuapp://money/accountbook/67045/share/","type":"1","viewcount":1,"follownum":0,"commentcount":0}]
     */

    private String title;
    private List<BookCellModel> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookCellModel> getList() {
        return list;
    }

    public void setList(List<BookCellModel> list) {
        this.list = list;
    }
}
