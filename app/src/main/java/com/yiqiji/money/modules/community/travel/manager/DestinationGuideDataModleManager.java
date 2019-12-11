package com.yiqiji.money.modules.community.travel.manager;

import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.travel.model.GuideContentInfo;
import com.yiqiji.money.modules.community.travel.model.HotDestinationsMultiitem;
import com.yiqiji.money.modules.community.travel.model.HotPlace;
import com.yiqiji.money.modules.community.travel.model.HotbookBean;
import com.yiqiji.money.modules.community.travel.model.RaidersDataItemBean;
import com.yiqiji.money.modules.community.travel.model.RaidersMultiItem;
import com.yiqiji.money.modules.community.travel.model.RaidersSectionsBean;
import com.yiqiji.money.modules.community.travel.model.RaiderspPhotosInfo;
import com.yiqiji.money.modules.community.travel.uitl.ConstantTravel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class DestinationGuideDataModleManager {
    public static List<GuideContentInfo> getGuideContentInfo(List<HotPlace> hotPlaces) {
        List<GuideContentInfo> guideContentInfos = new ArrayList<>();
        for (int i = 0; i < hotPlaces.size(); i++) {
            GuideContentInfo guideContentInfo = new GuideContentInfo();
            guideContentInfo.setContent(ConstantTravel.guide_content[i]);
            guideContentInfo.setDrawbleid(ConstantTravel.guide_image[i]);
            guideContentInfos.add(guideContentInfo);
        }
        return guideContentInfos;
    }

    public static List<HotDestinationsMultiitem> getHotDestinationsMultiitem(HotbookBean hotbookBean) {
        List<BookCellModel> bookCellModels = hotbookBean.getList();
        List<HotDestinationsMultiitem> hotDestinationsMultiitems = new ArrayList<>();
        String title = hotbookBean.getTitle();
        BookCellModel bookCellModel = new BookCellModel();
        bookCellModel.setTitle(title);
        HotDestinationsMultiitem hotDestinationsMultiitem = new HotDestinationsMultiitem();
        hotDestinationsMultiitem.setItemType(ConstantTravel.HOTDESTINATIONS_TITLT);
        hotDestinationsMultiitem.setData(bookCellModel);
        hotDestinationsMultiitems.add(hotDestinationsMultiitem);

        for (int i = 0; i < bookCellModels.size(); i++) {
            BookCellModel bookCellModel1 = bookCellModels.get(i);
            HotDestinationsMultiitem hotDestinationsMultiitem1 = new HotDestinationsMultiitem();
            hotDestinationsMultiitem1.setItemType(ConstantTravel.HOTDESTINATIONS_CONTENT);
            hotDestinationsMultiitem1.setData(bookCellModel1);
            hotDestinationsMultiitems.add(hotDestinationsMultiitem1);
        }
        return hotDestinationsMultiitems;
    }

    public static List<RaidersMultiItem> getRaidersMultiItem(List<RaidersDataItemBean> raidersDataItemBeanList) {
        List<RaidersMultiItem> raidersMultiItems = new ArrayList<>();
        for (int i = 0; i < raidersDataItemBeanList.size(); i++) {
            RaidersDataItemBean raidersDataItemBean = raidersDataItemBeanList.get(i);
            RaidersMultiItem raidersMultiItem = new RaidersMultiItem();
            raidersMultiItem.setItemType(ConstantTravel.HOTDESTINATIONS_TITLT);
            raidersMultiItem.setData(raidersDataItemBean);

            List<RaidersSectionsBean> sections = raidersDataItemBean.getSections();
//            raidersMultiItem = new RaidersMultiItem();
//            raidersMultiItem.setItemType(ConstantTravel.HOTDESTINATIONS_CONTENT);
//            raidersMultiItem.setData(sections);
            for (int j = 0; j < sections.size(); j++) {
                RaidersSectionsBean raidersSectionsBean = sections.get(j);
                String desContent = raidersSectionsBean.getDescription();
                RaidersMultiItem raidersMultiItem1 = new RaidersMultiItem();
                raidersMultiItem1.setItemType(ConstantTravel.HOTDESTINATIONS_CONTENT);
                raidersMultiItem1.setData(desContent);
                raidersMultiItem1.setParent(raidersMultiItem);
                raidersMultiItem.addSubItem(raidersMultiItem1);
//                raidersMultiItems.add(raidersMultiItem1);
                List<RaiderspPhotosInfo> imageList = raidersSectionsBean.getPhotos();
                RaidersMultiItem raidersMultiItem2 = new RaidersMultiItem();
                raidersMultiItem2.setItemType(ConstantTravel.HOTDESTINATIONS_CONTENT_IMAGE);
                raidersMultiItem2.setData(imageList);
                raidersMultiItem2.setParent(raidersMultiItem);
                raidersMultiItem.addSubItem(raidersMultiItem2);
//                raidersMultiItems.add(raidersMultiItem2);

            }
            raidersMultiItems.add(raidersMultiItem);

        }
        return raidersMultiItems;
    }
}
