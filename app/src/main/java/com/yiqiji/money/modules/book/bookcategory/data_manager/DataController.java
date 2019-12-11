package com.yiqiji.money.modules.book.bookcategory.data_manager;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.SyncCategoryResult;
import com.yiqiji.money.modules.common.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by leichi on 2017/5/16.
 * 分类数据网络请求层的包装层。用于对数据进行分解组装等，配合View使用
 */

public class DataController {

    /**
     * 根据账本的id查询账单分类
     *
     * @param accountBookId 账本id
     * @param viewCallBack
     */
    public static void getAccountBookCategoryByAccountBookId(final String accountBookId, final ViewCallBack<BookCategoryModel> viewCallBack) {
        DataRequstManager.getAccountBookCategoryByAccountBookId(accountBookId, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject=(JSONObject)o;
                viewCallBack.onSuccess(DataParseAssembler.getInstance().jsonObjectMapModel(jsonObject));
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                viewCallBack.onFinish();
            }
        });
    }


    /**
     * 添加分类没有同步的分类
     *
     * @param accountBookId
     * @param viewCallBack
     */
    public static void sycAllCategoryList(final String accountBookId, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        final BookCategoryModel bookCategoryModel=DataLocalPersistencer.getBookCategoryModel(accountBookId);
        if(bookCategoryModel.isSyced){
            try {
                viewCallBack.onSuccess(new SyncCategoryResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        JSONArray jsonArray = new JSONArray();
        try {
            if(bookCategoryModel.expendCategory!=null){
                List<IBaseBookCategory> expendCategoryList=getNotSycCategoryList(bookCategoryModel.expendCategory);
                for (IBaseBookCategory baseBookCategory:expendCategoryList){
                    JSONObject jsonObject = getJSONObject(baseBookCategory);
                    jsonObject.put("action", "add");
                    jsonArray.put(jsonObject);
                }
            }

            if(bookCategoryModel.incomeCategory!=null){
                List<IBaseBookCategory> incomeCategoryList=getNotSycCategoryList(bookCategoryModel.incomeCategory);
                for (IBaseBookCategory baseBookCategory:incomeCategoryList){
                    JSONObject jsonObject = getJSONObject(baseBookCategory);
                    jsonObject.put("action", "add");
                    jsonArray.put(jsonObject);
                }
            }
            syncCategory(jsonArray, new ViewCallBack<SyncCategoryResult>() {
                @Override
                public void onSuccess(SyncCategoryResult syncCategoryResult) throws Exception {
                    super.onSuccess(syncCategoryResult);
                    viewCallBack.onSuccess(syncCategoryResult);
                    bookCategoryModel.isSyced=true;
                    DataLocalPersistencer.saveBoolCategoryModel(bookCategoryModel);
                }

                @Override
                public void onFailed(SimpleMsg simleMsg) {
                    super.onFailed(simleMsg);
                    viewCallBack.onFailed(simleMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<IBaseBookCategory> getNotSycCategoryList(BookCategory bookCategory){
        List<IBaseBookCategory> baseBookCategoryList=new ArrayList<>();

        if(bookCategory.getCustomid()==null||bookCategory.getCustomid().length()==0)
            baseBookCategoryList.add(bookCategory);

        if(bookCategory.child!=null){
            baseBookCategoryList.addAll(bookCategory.child);
            for (int i=0;i<bookCategory.child.size();i++){
                BookCategory bookGroupCategoryModel=bookCategory.child.get(i);
                if(bookGroupCategoryModel.child!=null)
                    baseBookCategoryList.addAll(bookGroupCategoryModel.child);
            }
        }

        Iterator<IBaseBookCategory> iterator=baseBookCategoryList.iterator();
        while (iterator.hasNext()){
            IBaseBookCategory baseBookCategory=iterator.next();
            if(baseBookCategory.getCustomid()!=null&&baseBookCategory.getCustomid().length()>0){
                iterator.remove();
            }
            baseBookCategory.setBilltype(String.valueOf(bookCategory.billtype));
        }
        return baseBookCategoryList;
    }


    /**
     * 添加分类
     *
     * @param billCategory
     * @param viewCallBack
     */
    public static void addCategory(IBaseBookCategory billCategory, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        try {
            JSONObject jsonObject = getJSONObject(billCategory);
            jsonObject.put("action", "add");
            syncCategory(jsonObject, viewCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除分类
     *
     * @param billCategory
     * @param viewCallBack
     */
    public static void deleteCategory(IBaseBookCategory billCategory, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        JSONObject jsonObject = getJSONObject(billCategory);
        try {
            jsonObject.put("action", "del");
            syncCategory(jsonObject, viewCallBack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编辑分类
     *
     * @param billCategory
     * @param viewCallBack
     */
    public static void editCategory(IBaseBookCategory billCategory, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        JSONObject jsonObject = getJSONObject(billCategory);
        try {
            jsonObject.put("action", "edit");
            syncCategory(jsonObject, viewCallBack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停用分类
     *
     * @param billCategory
     * @param viewCallBack
     */
    public static void disableCategory(IBaseBookCategory billCategory, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        JSONObject jsonObject = getJSONObject(billCategory);
        try {
            jsonObject.put("status","0");
            jsonObject.put("action", "edit");
            syncCategory(jsonObject, viewCallBack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启用分类
     *
     * @param billCategory
     * @param viewCallBack
     */
    public static void enableCategory(IBaseBookCategory billCategory, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        JSONObject jsonObject = getJSONObject(billCategory);
        try {
            jsonObject.put("status","1");
            jsonObject.put("action", "edit");
            syncCategory(jsonObject, viewCallBack);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void syncCategory(JSONObject jsonObject, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray.put(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        syncCategory(jsonArray,viewCallBack);
    }

    public static void syncCategory(JSONArray jsonArray, final ViewCallBack<SyncCategoryResult> viewCallBack) {
        if(jsonArray.length()==0){
            try {
                viewCallBack.onSuccess(new SyncCategoryResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        DataRequstManager.syncCategory(jsonArray, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject=(JSONObject)o;
                JSONArray resultJsonArray = jsonObject.getJSONArray("data");
                if (resultJsonArray != null && resultJsonArray.length() > 0) {
                    SyncCategoryResult syncCategoryResult = GsonUtil.GsonToBean(resultJsonArray.getString(0), SyncCategoryResult.class);
                    viewCallBack.onSuccess(syncCategoryResult);
                } else {
                    SimpleMsg simleMsg = new SimpleMsg(0, "数据解析异常");
                    viewCallBack.onFailed(simleMsg);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                viewCallBack.onFinish();
            }
        });
    }


    public static JSONObject getJSONObject(IBaseBookCategory billCategory) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("categorytitle", billCategory.getCategorytitle());
            jsonObject.put("parentid", billCategory.getParentid());
            jsonObject.put("pkid", billCategory.getPikId());
            jsonObject.put("categoryid", billCategory.getCategoryid());
            jsonObject.put("billtype", billCategory.getBilltype());
            jsonObject.put("categoryicon", billCategory.getCategoryicon());
            jsonObject.put("status", billCategory.getStatus());
            jsonObject.put("accountbookid", billCategory.getAccountbookid());
            jsonObject.put("categorytype", billCategory.getCategorytype());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
