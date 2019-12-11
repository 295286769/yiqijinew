package com.yiqiji.money.modules.book.creater.manager;

import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem;
import com.yiqiji.money.modules.book.creater.model.BookCoverModel;
import com.yiqiji.money.modules.book.creater.model.BookCoverTemplateListModel;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/6/27.
 */

public class BookCreatorLocalDataManager {

    public static final String KEY_TEMPLATE_BOOK_LIST = "key_template_book_list";

    public static void saveTemplateBookCoverList(BookCoverTemplateListModel bookCoverTemplateListModel) {
        String json = GsonUtil.GsonString(bookCoverTemplateListModel);
        SPUtils.setParam(KEY_TEMPLATE_BOOK_LIST, json);
    }

    public static BookCoverTemplateListModel getLocalTemplateBookCoverList() {
        String json =SPUtils.getParam(KEY_TEMPLATE_BOOK_LIST,"");
        return GsonUtil.GsonToBean(json,BookCoverTemplateListModel.class);
    }

    public static List<BookCoverListMultipleItem> getBookCoverListMultipleItemList(){
        List<BookCoverListMultipleItem> bookCoverListMultipleItemList=new ArrayList<>();
        BookCoverTemplateListModel bookCoverTemplateListModel=getLocalTemplateBookCoverList();
        List<BookCoverModel> single=bookCoverTemplateListModel.single;          //非AA账本
        List<BookCoverModel> multiple=bookCoverTemplateListModel.multiple;      //AA账本

        //添加非AA账本title
        BookCoverListMultipleItem bookCoverListMultipleItem=new BookCoverListMultipleItem();
        bookCoverListMultipleItem.setViewType(BookCoverListMultipleItem.ViewType.BOOK_NO_AA_TYPE_TITLE);
        bookCoverListMultipleItemList.add(bookCoverListMultipleItem);

        //添加非AA账本数据
        for (int i=0;i<single.size();i++){
            BookCoverModel bookCoverModel=single.get(i);
            bookCoverListMultipleItem=new BookCoverListMultipleItem();
            bookCoverListMultipleItem.setViewType(BookCoverListMultipleItem.ViewType.BOOK_COVER_MODEL);
            bookCoverListMultipleItem.setData(bookCoverModel);
            bookCoverListMultipleItemList.add(bookCoverListMultipleItem);
        }

        //添加AA账本title
        bookCoverListMultipleItem=new BookCoverListMultipleItem();
        bookCoverListMultipleItem.setViewType(BookCoverListMultipleItem.ViewType.BOOK_AA_TYPE_TITLE);
        bookCoverListMultipleItemList.add(bookCoverListMultipleItem);

        //添加AA账本数据
        for (int i=0;i<multiple.size();i++){
            BookCoverModel bookCoverModel=multiple.get(i);
            bookCoverListMultipleItem=new BookCoverListMultipleItem();
            bookCoverListMultipleItem.setViewType(BookCoverListMultipleItem.ViewType.BOOK_COVER_MODEL);
            bookCoverListMultipleItem.setData(bookCoverModel);
            bookCoverListMultipleItemList.add(bookCoverListMultipleItem);
        }

        return bookCoverListMultipleItemList;
    }
}
