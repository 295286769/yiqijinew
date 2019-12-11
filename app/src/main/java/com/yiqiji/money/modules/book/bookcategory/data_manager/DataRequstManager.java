package com.yiqiji.money.modules.book.bookcategory.data_manager;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by leichi on 2017/5/16.
 * 账单的分类数据请求网络请求层
 */

public class DataRequstManager {

    /**
     * 根据账本的id查询账单分类
     *
     * @param accountBookId 账本id
     * @param viewCallBack
     */
    public static void getAccountBookCategoryByAccountBookId(final String accountBookId, final ViewCallBack viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountBookId);// 账本ID
        CommonFacade.getInstance().exec(Constants.BOOKS_CATE, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jsonObject = (JSONObject) o;
                DataLocalPersistencer.saveBookCategoryJSONObject(accountBookId,jsonObject);
                viewCallBack.onSuccess(jsonObject);
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

    public static void syncCategory(JSONArray jsonArray, final ViewCallBack viewCallBack) {
        CommonFacade.getInstance().exec(Constants.SYNC_CATEGORY, jsonArray, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                viewCallBack.onSuccess(o);
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
}
