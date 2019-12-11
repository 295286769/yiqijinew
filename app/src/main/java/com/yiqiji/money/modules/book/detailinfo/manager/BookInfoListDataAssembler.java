package com.yiqiji.money.modules.book.detailinfo.manager;

import com.yiqiji.money.modules.book.detailinfo.model.BookBillItemModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookDetailModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookInfoListMultipItem;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookInfoListDataAssembler {


    public static List<BookInfoListMultipItem> getAssemblerMultipItem(BookBillModel bookBillModel, String mAccountbookcateid, int renovationordairy) {
        List<BookInfoListMultipItem> bookInfoListMultipItemList = new ArrayList<>();
        List<BookBillItemModel> billItemModelList = null;
        if (mAccountbookcateid.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
            if (renovationordairy == 1) {
                billItemModelList = bookBillModel.list;
            } else {
                billItemModelList = bookBillModel.daily;
            }

        } else {
            billItemModelList = bookBillModel.list;
        }

        if (billItemModelList == null || billItemModelList.size() == 0) {

            BookInfoListMultipItem bookInfoListMultipItem = new BookInfoListMultipItem();
            bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_EMPTY);
            bookInfoListMultipItemList.add(bookInfoListMultipItem);

            return bookInfoListMultipItemList;
        }

        String timestamp = "";
        for (int i = 0; i < billItemModelList.size(); i++) {

            BookBillItemModel bookBillItemModel = billItemModelList.get(i);

            if (!bookBillItemModel.billstatus.equals("1")) {
                //如果账本不是正常的状态则过滤掉
                continue;
            }
            String thisDate = DateUtil.timeStampDate(bookBillItemModel.tradetime);
            boolean isNewDate = !timestamp.equals(thisDate);
            if (isNewDate) {
                timestamp = thisDate;
                bookInfoListMultipItemList.add(getDateBookInfoListMultipItem(bookBillItemModel.tradetime));
            }

            BookInfoListMultipItem bookInfoListMultipItem = new BookInfoListMultipItem();


            switch (bookBillItemModel.billtype) {
                case "0":
                case "1":
                case "2":
                    if (bookBillModel.bookdetail.accountbookcate.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {//是否是装修账本
                        bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_RENOVATION);
                    } else {
                        bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL);
                    }
                    break;
                case "3":
                    bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_SETTLEMENT);
                    break;
                case "4":
                    bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL);
                    break;
                case "5":

                    if (bookBillModel.bookdetail.accountbookcate.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {//是否是装修账本
                        bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_RENOVATION_DIARY);
                    } else {
                        bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_DIARY);
                    }

                    break;

            }
            bookInfoListMultipItem.setData(bookBillItemModel);
            bookInfoListMultipItemList.add(bookInfoListMultipItem);
        }
        return bookInfoListMultipItemList;
    }


    public static BookInfoListMultipItem getDateBookInfoListMultipItem(String time) {
        BookInfoListMultipItem bookInfoListMultipItem = new BookInfoListMultipItem();
        bookInfoListMultipItem.setData(time);
        bookInfoListMultipItem.setItemType(BookInfoListMultipItem.ViewType.BILL_DATE);
        return bookInfoListMultipItem;
    }


    public static void insertValue(BookBillModel bookBillModel) {
        if (bookBillModel.list == null || bookBillModel.list.size() == 0) {
            return;
        }
        BookDetailModel bookdetail = bookBillModel.bookdetail;
        for (BookBillItemModel bookBillItemModel : bookBillModel.list) {
            bookBillItemModel.bookIsAA = bookdetail.isclear.equals("1");
        }

    }
}
