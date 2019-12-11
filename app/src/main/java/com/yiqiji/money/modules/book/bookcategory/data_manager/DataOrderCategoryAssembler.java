package com.yiqiji.money.modules.book.bookcategory.data_manager;

import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leichi on 2017/6/1.
 */

public class DataOrderCategoryAssembler {


    /**
     * 将adapter的数据映射成map : key为分类的id ;value为分类的序号值。用于本地做排序用
     * @param groupItemList
     * @return
     */
    public static HashMap<String, Double> bookCategoryGroupItemListMapBookCategorySortMap(HashMap<String,Double> map, List<BookCategoryListMultipleItem> groupItemList) {
        int groupSerialNum = 0;
        for (int i = 0; i < groupItemList.size(); i++) {
            BookCategoryListMultipleItem groupMultipleItem = groupItemList.get(i);
            if (groupMultipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                BookCategory bookGroupCategoryModel = ((BookCategory) groupMultipleItem.getData());
                String categoryid = bookGroupCategoryModel.categoryid;
                map.put(categoryid, Double.valueOf(groupSerialNum));
                groupSerialNum++;
            }
        }
        return map;
    }


    public static List<BookCategoryListMultipleItem> getBookCategoryListMultipleItemList(BookCategory bookCategory){
        List<BookCategoryListMultipleItem> itemList=new ArrayList<>();

        int addCount=0;
        for (int i=0;i<bookCategory.child.size();i++){
            BookCategory bookGroupCategoryModel=bookCategory.child.get(i);
            if(bookGroupCategoryModel.status.equals("0")){
                continue;
            }

            if(addCount%8==0){
                BookCategoryListMultipleItem cutLine = new BookCategoryListMultipleItem();
                cutLine.setItemType(BookCategoryListMultipleItem.ViewType.CUTLINE);
                cutLine.setData((i/8)+1);
                itemList.add(cutLine);
            }

            BookCategoryListMultipleItem groupItem = new BookCategoryListMultipleItem();
            groupItem.setItemType(BookCategoryListMultipleItem.ViewType.GROUP);
            groupItem.setData(bookGroupCategoryModel);
            itemList.add(groupItem);
            addCount++;
        }

        return itemList;
    }
}
