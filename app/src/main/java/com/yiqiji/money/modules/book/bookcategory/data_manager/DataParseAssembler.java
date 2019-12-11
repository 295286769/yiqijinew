package com.yiqiji.money.modules.book.bookcategory.data_manager;

import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.common.entity.BookExpenditure;
import com.yiqiji.money.modules.common.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leichi on 2017/5/16.
 */

public class DataParseAssembler {

    static DataParseAssembler dataParseAssembler;

    public static DataParseAssembler getInstance() {
        if (dataParseAssembler == null) {
            dataParseAssembler = new DataParseAssembler();
        }
        return dataParseAssembler;
    }

    /**
     * json映射成模型
     *
     * @param jsonObject
     * @return
     */
    public BookCategoryModel jsonObjectMapModel(JSONObject jsonObject) {
        BookCategoryModel bookCategoryModel = new BookCategoryModel();
        try {
            if (jsonObject.has("0")) {
                BookCategory bookCategory = GsonUtil.GsonToBean(jsonObject.getJSONObject("0").toString(), BookCategory.class);
                allotBillCategoryBybilltype(bookCategory, bookCategoryModel);
            }

            if (jsonObject.has("1")) {
                BookCategory bookCategory = GsonUtil.GsonToBean(jsonObject.getJSONObject("1").toString(), BookCategory.class);
                allotBillCategoryBybilltype(bookCategory, bookCategoryModel);
            }

            if (jsonObject.has("expendCategory")) {
                BookCategory bookCategory = GsonUtil.GsonToBean(jsonObject.getJSONObject("expendCategory").toString(), BookCategory.class);
                allotBillCategoryBybilltype(bookCategory, bookCategoryModel);
            }

            if (jsonObject.has("incomeCategory")) {
                BookCategory bookCategory = GsonUtil.GsonToBean(jsonObject.getJSONObject("incomeCategory").toString(), BookCategory.class);
                allotBillCategoryBybilltype(bookCategory, bookCategoryModel);
            }

            if (jsonObject.has("isSyced")) {
                bookCategoryModel.isSyced = jsonObject.getBoolean("isSyced");
            }

            if (jsonObject.has("accountbookid")) {
                bookCategoryModel.accountbookid = jsonObject.getString("accountbookid");
            }

        } catch (JSONException e) {
            String error = e.getMessage();
            error.length();
        }
        return bookCategoryModel;
    }

    private void allotBillCategoryBybilltype(BookCategory bookCategory, BookCategoryModel bookCategoryModel) {
        if (bookCategory.billtype.equals(String.valueOf(DataConstant.INCOME_BILL_TYPE))) {                           //账单收入类型结构
            bookCategoryModel.incomeCategory = bookCategory;
        } else if (bookCategory.billtype.equals(String.valueOf(DataConstant.EXPEND_BILL_TYPE))) {                    //账单支出类型结构
            bookCategoryModel.expendCategory = bookCategory;
        }else if(bookCategory.billtype.equals(String.valueOf(DataConstant.PAYMENT_BILL_TYPE))){
            bookCategoryModel.incomeCategory = bookCategory;
        }
        bookCategoryModel.accountbookid = bookCategory.accountbookid;
    }


    /**
     * 根据账本id取本地数据，构建成Adapter需要的数据
     *
     * @param accountbookid 账本id
     * @param billType      账单类型：1：支出；0：收入。
     * @return
     */
    public List<BookCategoryListMultipleItem> getLocalBookCategoryAdapterData(String accountbookid, int billType) {
        List<BookCategoryListMultipleItem> itemList = new ArrayList<>();
        BookCategoryModel bookCategoryModel = DataLocalPersistencer.getBookCategoryModel(accountbookid);

        if (bookCategoryModel != null && billType == DataConstant.EXPEND_BILL_TYPE && bookCategoryModel.expendCategory != null) {
            itemList.addAll(assemblerBookCategoryForAdapter(bookCategoryModel.expendCategory));
        }

        if (bookCategoryModel != null && billType == DataConstant.INCOME_BILL_TYPE && bookCategoryModel.incomeCategory != null) {
            itemList.addAll(assemblerBookCategoryForAdapter(bookCategoryModel.incomeCategory));
        }
        return itemList;
    }

    public List<BookCategoryListMultipleItem> assemblerBookCategoryForAdapter(BookCategory bookCategory) {

        List<BookCategoryListMultipleItem> allItemList = new ArrayList<>();
        List<BookCategoryListMultipleItem> enableItemList = new ArrayList<>();
        List<BookCategoryListMultipleItem> disAbleItemList = new ArrayList<>();

        if (bookCategory.child != null && bookCategory.child.size() > 0)
            for (int i = 0; i < bookCategory.child.size(); i++) {
                BookCategory bookGroupCategoryModel = bookCategory.child.get(i);
                if (bookGroupCategoryModel.status.equals("0")) {
                    disAbleItemList.add(assemblerBookCategoryForAdapterForChild(bookGroupCategoryModel));
                } else {
                    enableItemList.add(assemblerBookCategoryForAdapterForChild(bookGroupCategoryModel));
                }
            }

        setLastItemTag(disAbleItemList);
        setLastItemTag(enableItemList);

        //添加启用的item
        allItemList.addAll(enableItemList);
        //添加启用/停用分割线
        BookCategoryListMultipleItem cutLine = new BookCategoryListMultipleItem();
        cutLine.setItemType(BookCategoryListMultipleItem.ViewType.CUTLINE);
        allItemList.add(cutLine);
        //添加停用的item
        allItemList.addAll(disAbleItemList);


        return allItemList;
    }

    private static void setLastItemTag(List<BookCategoryListMultipleItem> itemList) {
        if (itemList.size() > 0) {
            ((BookCategory) itemList.get(itemList.size() - 1).getData()).isLastItem = true;
        }
    }


    private BookCategoryListMultipleItem assemblerBookCategoryForAdapterForChild(BookCategory bookGroupCategoryModel) {
        //构造第一级的分类
        BookCategoryListMultipleItem groupItem = new BookCategoryListMultipleItem();
        groupItem.setItemType(BookCategoryListMultipleItem.ViewType.GROUP);
        groupItem.setData(bookGroupCategoryModel);

        //构造第二级分类
        if (bookGroupCategoryModel.child != null && bookGroupCategoryModel.child.size() > 0)
            for (int n = 0; n < bookGroupCategoryModel.child.size(); n++) {
                BookCategory bookChildCategoryModel = bookGroupCategoryModel.child.get(n);
                bookChildCategoryModel.parentName = bookGroupCategoryModel.categorytitle;
                BookCategoryListMultipleItem childItem = new BookCategoryListMultipleItem();
                childItem.setItemType(BookCategoryListMultipleItem.ViewType.CHILD);
                childItem.setData(bookChildCategoryModel);
                childItem.setParent(groupItem);
                groupItem.addSubItem(childItem);
            }

        //添加Add按钮进去
        BookCategoryListMultipleItem addButton = new BookCategoryListMultipleItem();
        addButton.setItemType(BookCategoryListMultipleItem.ViewType.ADD_BUTTON);
        addButton.setData(createChildCategoryModel(bookGroupCategoryModel));
        groupItem.addSubItem(addButton);

        return groupItem;
    }

    private BookCategory createChildCategoryModel(IBaseBookCategory groupCategoryModel) {
        BookCategory childCategoryModel = new BookCategory();
        childCategoryModel.billtype = groupCategoryModel.getBilltype();
        childCategoryModel.parentName = groupCategoryModel.getCategorytitle();
        childCategoryModel.categorytype = groupCategoryModel.getCategorytype();
        childCategoryModel.accountbookid = groupCategoryModel.getAccountbookid();
        childCategoryModel.parentid = groupCategoryModel.getCategoryid();
        childCategoryModel.status = groupCategoryModel.getStatus();
        return childCategoryModel;
    }


    public BookCategoryListMultipleItem createGroupMultipleItem(IBaseBookCategory groupBookCategory) {
        BookCategoryListMultipleItem groupItem = new BookCategoryListMultipleItem();
        groupItem.setItemType(BookCategoryListMultipleItem.ViewType.GROUP);
        groupItem.setData(groupBookCategory);

        //添加Add按钮进去
        BookCategoryListMultipleItem addButton = new BookCategoryListMultipleItem();
        addButton.setItemType(BookCategoryListMultipleItem.ViewType.ADD_BUTTON);
        addButton.setData(createChildCategoryModel(groupBookCategory));

        groupItem.addSubItem(addButton);

        return groupItem;
    }

    /**
     * 将adapter的数据映射成map : key为分类的id ;value为分类的序号值。用于本地做排序用
     *
     * @param itemList
     * @return
     */
    public HashMap<String, Integer> bookCategoryItemListMapBookCategorySortMap(List<BookCategoryListMultipleItem> itemList) {
        HashMap<String, Integer> bookCategorySortMap = new HashMap<>();
        int groupSerialNum = 0;
        for (int i = 0; i < itemList.size(); i++) {

            BookCategoryListMultipleItem groupMultipleItem = itemList.get(i);
            if (groupMultipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {

                BookCategory bookGroupCategoryModel = ((BookCategory) groupMultipleItem.getData());
                String categoryid = bookGroupCategoryModel.categoryid;

                bookCategorySortMap.put(categoryid, groupSerialNum);
                groupSerialNum++;


                List<BookCategoryListMultipleItem> childItemList = groupMultipleItem.getSubItems();
                if (childItemList == null || childItemList.size() == 0) {
                    continue;
                }
                int childSerialNum = 0;
                for (int n = 0; n < childItemList.size(); n++) {
                    BookCategoryListMultipleItem childMultipleItem = childItemList.get(n);
                    if (childMultipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.CHILD.ordinal()) {
                        String childCategoryId = ((BookCategory) childMultipleItem.getData()).categoryid;
                        bookCategorySortMap.put(categoryid + "_" + childCategoryId, childSerialNum);
                        childSerialNum++;
                    }
                }
            }
        }
        return bookCategorySortMap;
    }

    /**
     * 只获取可用的分类列表
     *
     * @param jsonObject
     * @return
     */
    public BookExpenditure getExpenditureForEnable(JSONObject jsonObject) {
        BookExpenditure bookExpenditure = GsonUtil.GsonToBean(jsonObject.toString(), BookExpenditure.class);

        if(bookExpenditure.getChild()==null){
            return bookExpenditure;
        }
        Iterator<BookExpenditure.ChildBean> mIterator = bookExpenditure.getChild().iterator();
        while (mIterator.hasNext()) {
            BookExpenditure.ChildBean mChildBean = mIterator.next();
            //移除禁用了的分类
            if (mChildBean.getStatus().equals("0")) {
                mIterator.remove();
                continue;
            }
            if (mChildBean.getChild() != null) {
                Iterator<BookExpenditure.ChildBean> iterator = mChildBean.getChild().iterator();
                while (iterator.hasNext()) {
                    BookExpenditure.ChildBean childBean = iterator.next();
                    //移除禁用了的分类
                    if (childBean.getStatus().equals("0")) {
                        iterator.remove();
                    }
                }
            }
        }
        return bookExpenditure;
    }
}
