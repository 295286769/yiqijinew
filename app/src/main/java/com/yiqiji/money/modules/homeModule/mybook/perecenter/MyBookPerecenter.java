package com.yiqiji.money.modules.homeModule.mybook.perecenter;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.AddBookInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseDataInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseInfo;

import java.util.HashMap;

/**
 * Created by ${huangweishui} on 2017/8/7.
 * address huang.weishui@71dai.com
 */
public class MyBookPerecenter {
    public static void updateBookDetail(String isEdit, String id, String accountbookid, int isopen, String editBookName,
                                        String image_url, final ViewCallBack<BooksDbInfo> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        String baseUrl = Constants.ADD_BOOK;
        String comitid = id;
        if (!isEdit.equals("false")) {
            baseUrl = Constants.BOOKS_EDIT;
            comitid = accountbookid;
            hashMap.put("isopen", isopen + "");
        }
        hashMap.put("id", comitid);
        hashMap.put("name", editBookName);
        hashMap.put("bgimg", image_url);
        CommonFacade.getInstance().exec(baseUrl, hashMap, new ViewCallBack<AddBookInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(AddBookInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo booksDbInfo = o.getData();
                viewCallBack.onSuccess(booksDbInfo);


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
     * 获取房屋信息
     *
     * @param accountbookid//账本id
     * @param viewCallBack
     */
    public static void initHouseInfo(String accountbookid, final ViewCallBack<HouseDataInfo> viewCallBack) {
        HashMap<String, String> hashMap = StringUtils.getParamas("id", accountbookid);
        CommonFacade.getInstance().exec(Constants.GETHOUSEINFO, hashMap, new ViewCallBack<HouseInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(HouseInfo o) throws Exception {
                super.onSuccess(o);
                viewCallBack.onSuccess(o.getData());
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
