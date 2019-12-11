package com.yiqiji.money.modules.book.bookcategory.data_manager;

import com.google.gson.Gson;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leichi on 2017/5/16.
 * 账本分类本地序列化保存和反序列化管理类
 */
public class DataLocalPersistencer {

    /**
     * 保存BookCategory json数据
     *
     * @param accountBookId
     * @param jsonObject
     */
    public static void saveBookCategoryJSONObject(String accountBookId, JSONObject jsonObject) {
        BookCategoryModel bookCategoryModel=DataParseAssembler.getInstance().jsonObjectMapModel(jsonObject);

        BookCategory expendCategory=bookCategoryModel.expendCategory;
        if(expendCategory!=null&&expendCategory.child!=null&&expendCategory.child.size()>0){
            sortBookGroupCategoryModelList(accountBookId,DataConstant.EXPEND_BILL_TYPE,expendCategory.child);
            insertValue(expendCategory.billtype,expendCategory.child);
        }

        BookCategory incomeCategory=bookCategoryModel.incomeCategory;
        if(incomeCategory!=null&&incomeCategory.child!=null&&incomeCategory.child.size()>0){
            sortBookGroupCategoryModelList(accountBookId,DataConstant.INCOME_BILL_TYPE,incomeCategory.child);
            insertValue(incomeCategory.billtype,incomeCategory.child);
        }
        SPUtils.setParam(accountBookId,GsonUtil.GsonString(bookCategoryModel));
    }


    /**
     * 给子类赋需要的值
     * @param billtype
     * @param group
     */
    private static void insertValue(String billtype,List<BookCategory> group){
        for (int i=0;i<group.size();i++){
            BookCategory bookGroupCategoryModel=group.get(i);
            bookGroupCategoryModel.billtype=billtype;
            String categoryTitle=bookGroupCategoryModel.categorytitle;
            if(bookGroupCategoryModel.child!=null){
                for (int n=0;n<bookGroupCategoryModel.child.size();n++){
                    BookCategory childCategoryModel=bookGroupCategoryModel.child.get(n);
                    childCategoryModel.billtype=billtype;
                    childCategoryModel.parentName=categoryTitle;
                }
            }
        }
    }

    /**
     * 更新本地的分类json数据
     * @param accountBookId
     */
    public static void updateBookCategoryJSONObject(String accountBookId){
        saveBookCategoryJSONObject(accountBookId,getBookCategoryJSONObject(accountBookId));
    }


    public static void sortBookGroupCategoryModelList(String accountBookId,int bilType,List<BookCategory> group){
        HashMap<String,Double> sortMap=getSortBookCategoryMap(accountBookId,String.valueOf(bilType));
        if(sortMap==null||sortMap.size()==0){
            return;
        }
        Collections.sort(group,new GoupSerialNumComparator(sortMap));
        for (int i=0;i<group.size();i++){
            List<BookCategory> child=group.get(i).child;
            if(child==null||child.size()==0){
                continue;
            }
            Collections.sort(child,new ChildSerialNumComparator(sortMap));
        }
    }

    // 自定义比较器
    static class GoupSerialNumComparator implements Comparator<IBaseBookCategory> {
        HashMap<String,Double> sortMap;
        public GoupSerialNumComparator(HashMap<String,Double> sortMap){
            this.sortMap=sortMap;
        }
        public int compare(IBaseBookCategory object1, IBaseBookCategory object2) {
            Double category1SerialNum=sortMap.get(object1.getCategoryid());
            Double category2SerialNum=sortMap.get(object2.getCategoryid());

            if(category1SerialNum==category2SerialNum){
                return 0;
            }else if(category1SerialNum==null){
                category1SerialNum=0.0;
            }else if(category2SerialNum==null){
                category2SerialNum=0.0;
            }
            return category1SerialNum.compareTo(category2SerialNum);
        }
    }

    // 自定义比较器
    static class ChildSerialNumComparator implements Comparator<IBaseBookCategory> {
        HashMap<String,Double> sortMap;
        public ChildSerialNumComparator(HashMap<String,Double> sortMap){
            this.sortMap=sortMap;
        }
        public int compare(IBaseBookCategory object1, IBaseBookCategory object2) {
            Double category1SerialNum=sortMap.get(object1.getParentid()+"_"+object1.getCategoryid());
            Double category2SerialNum=sortMap.get(object2.getParentid()+"_"+object2.getCategoryid());

            if(category1SerialNum==category2SerialNum){
                return 0;
            }else if(category1SerialNum==null){
                category1SerialNum=0.0;
            }else if(category2SerialNum==null){
                category2SerialNum=0.0;
            }
            return category1SerialNum.compareTo(category2SerialNum);
        }
    }


    /**
     * 保存BookCategoryModel 到本地
     *
     * @param bookCategoryModel
     */
    public static void saveBoolCategoryModel(BookCategoryModel bookCategoryModel) {
        Gson gson=new Gson();
        SPUtils.setParam(bookCategoryModel.accountbookid, gson.toJson(bookCategoryModel));
    }

    /**
     * 获取BookCategory JSONObject对象
     *
     * @param accountBookId
     * @return
     */
    public static JSONObject  getBookCategoryJSONObject(String accountBookId) {
        JSONObject jsonObject = null;
        try {
            String json=SPUtils.getParam(accountBookId, "");
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject == null ? new JSONObject() : jsonObject;
    }

    /**
     * 直接获取BookCategoryModel对象
     *
     * @param accountBookId
     * @return
     */
    public static BookCategoryModel getBookCategoryModel(String accountBookId) {
        BookCategoryModel bookCategoryModel = new BookCategoryModel();
        JSONObject jsonObject = getBookCategoryJSONObject(accountBookId);
        if (jsonObject != null) {
            bookCategoryModel = DataParseAssembler.getInstance().jsonObjectMapModel(jsonObject);
        }
        return bookCategoryModel;
    }


    /**
     * 根据账本id取本地数据，BookCategory对象
     *
     * @param accountbookid 账本id
     * @param billType      账单类型：1：支出；0：收入。
     * @return
     */
    public static BookCategory getLocalBookCategory(String accountbookid, int billType) {
        BookCategoryModel bookCategoryModel = DataLocalPersistencer.getBookCategoryModel(accountbookid);

        if (bookCategoryModel != null && billType == DataConstant.EXPEND_BILL_TYPE && bookCategoryModel.expendCategory != null) {
            return bookCategoryModel.expendCategory;
        }

        if (bookCategoryModel != null && billType == DataConstant.INCOME_BILL_TYPE&& bookCategoryModel.incomeCategory != null) {
            return bookCategoryModel.incomeCategory;
        }
        return null;
    }

    /**
     * 保存所有的排序Map
     * @param accountBookId
     * @param itemList
     * @param billType
     */
    public static void saveSortBookCategoryMap(String accountBookId, List<BookCategoryListMultipleItem> itemList, int billType){
        HashMap<String,Integer> sortMap=DataParseAssembler.getInstance().bookCategoryItemListMapBookCategorySortMap(itemList);
        SPUtils.setParam(accountBookId+"_"+billType,GsonUtil.GsonString(sortMap));
    }

    /**
     * 保存一级Group的排序
     * @param accountBookId
     * @param groupItemList
     * @param billType
     */
    public static void saveSortBookForGroupCategoryMap(String accountBookId,List<BookCategoryListMultipleItem> groupItemList,int billType){
        HashMap<String,Double> map=getSortBookCategoryMap(accountBookId,String.valueOf(billType));
        HashMap<String,Double> sortMap=DataOrderCategoryAssembler.bookCategoryGroupItemListMapBookCategorySortMap(map,groupItemList);
        SPUtils.setParam(accountBookId+"_"+billType,GsonUtil.GsonString(sortMap));
    }

    /**
     * 添加一个子类，保存其排序下标
     * @param baseBookCategory
     */
    public static void addSortBookForChildCategory(IBaseBookCategory baseBookCategory){
        String accountBookId=baseBookCategory.getAccountbookid();
        String billType=baseBookCategory.getBilltype();
        String parentId=baseBookCategory.getParentid();
        String categoryId=baseBookCategory.getCategoryid();

        double lastSerialNum=0;

        BookCategory bookCategory=getLocalBookCategory(accountBookId,Integer.parseInt(billType));
        List<BookCategory> groupChildList=bookCategory.child;
        for (int i=0;i<groupChildList.size();i++){
            BookCategory groupBookCategory=groupChildList.get(i);
            if(groupBookCategory.categoryid.equals(parentId)){
                lastSerialNum=groupBookCategory.child==null?0:groupBookCategory.child.size()+1;
            }
        }

        HashMap<String,Double> sortMap=getSortBookCategoryMap(accountBookId,billType);
        sortMap.put(parentId+"_"+categoryId,lastSerialNum);
        SPUtils.setParam(accountBookId+"_"+billType,GsonUtil.GsonString(sortMap));
        updateBookCategoryJSONObject(accountBookId);
    }

    /**
     * 添加一个大类，保存其排序位置信息
     * @param baseBookCategory
     */
    public static void addSortBookForGroupCategory(IBaseBookCategory baseBookCategory){
        String accountBookId=baseBookCategory.getAccountbookid();
        String billType=baseBookCategory.getBilltype();
        String categoryId=baseBookCategory.getCategoryid();

        BookCategory bookCategory=getLocalBookCategory(accountBookId,Integer.parseInt(billType));

        double lastSerialNum=bookCategory.child.size();

        HashMap<String,Double> sortMap=getSortBookCategoryMap(accountBookId,billType);
        sortMap.put(categoryId,lastSerialNum);
        SPUtils.setParam(accountBookId+"_"+billType,GsonUtil.GsonString(sortMap));
        updateBookCategoryJSONObject(accountBookId);
    }

    public static HashMap<String,Double> getSortBookCategoryMap(String accountBookId,String billType){
        String jsonString=SPUtils.getParam(accountBookId+"_"+billType,"");
        Map<String,Double> sortMap=GsonUtil.GsonToMaps(jsonString);
        HashMap<String,Double> hashMap=new HashMap<>();
        if(sortMap!=null){
            hashMap.putAll(sortMap);
        }
        return hashMap;
    }
}
