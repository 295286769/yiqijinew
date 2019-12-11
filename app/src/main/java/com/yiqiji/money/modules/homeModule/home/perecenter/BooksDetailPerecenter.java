package com.yiqiji.money.modules.homeModule.home.perecenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.community.travel.model.DecorateHomeEntity;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;

import java.util.HashMap;

/**
 * Created by ${huangweishui} on 2017/6/19.
 * address huang.weishui@71dai.com
 */
public class BooksDetailPerecenter {
    /**
     * @param accountbookcount
     * @return 返回//false 单人账本 true多人账本
     */
    public static boolean isAccountbookCount(String accountbookcount) {
        boolean isAccountbookCount = false;//false 单人账本 true多人账本
        if (!TextUtils.isEmpty(accountbookcount) && Integer.parseInt(accountbookcount) > 0) {
            isAccountbookCount = true;
            return isAccountbookCount;
        }
        return isAccountbookCount;
    }

    public static void getBookDetail(final Context mContext, String mAccountid, final ViewCallBack<BooksDbInfo> viewCallBack) {
//        long start_time = DateUtil.getStartTime(date_time);//开始时间
//        long end_time = DateUtil.getEndTime(date_time);// 结束时间
//        String string_start_time = start_time + "";
//        String string_end_time = end_time + "";
//        if (booksDbInfo.getSorttype().equals("0")) {
//            string_start_time = "";
//            string_end_time = "";
//        }
        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountid/*,
                "stime", string_start_time, "etime", string_end_time*/);

        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {


            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo booksDbI = o;

                viewCallBack.onSuccess(booksDbI);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    ((BaseActivity) mContext).showToast(simleMsg);
                } else {
                    if (simleMsg.getErrCode() == 50099) {
                        ((BaseActivity) mContext).showSimpleAlertDialog("提示", simleMsg.getErrMsg(), "确定", false, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((BaseActivity) mContext).dismissDialog();
//                                //账本id不存在重新请求账本
//                                initBooks();
                            }
                        });
                    } else {
//                        //账本id不存在重新请求账本
//                        initBooks();
                    }


                }


            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    public static void initHouseInfo(final ViewCallBack<DecorateHomeEntity.DataBean> viewCallBack) {
        CommonFacade.getInstance().exec(Constants.FIND_HOUSE, new ViewCallBack<DecorateHomeEntity>() {
            @Override
            public void onStart() {
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(DecorateHomeEntity decorateHomeEntity) throws Exception {

                DecorateHomeEntity.DataBean dataBean = decorateHomeEntity.getData();
                viewCallBack.onSuccess(dataBean);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                viewCallBack.onFinish();
            }
        });
    }

    public static void checkMessage(final ViewCallBack<CheckMessageInfo.MessgeinfoItem> viewCallBack) {

        CommonFacade.getInstance().exec(Constants.CHECK_MESSAGE, new ViewCallBack<CheckMessageInfo>() {
            @Override
            public void onSuccess(CheckMessageInfo o) throws Exception {
                super.onSuccess(o);
                CheckMessageInfo.MessgeinfoItem messgeinfoItem = o.getData();
                viewCallBack.onSuccess(messgeinfoItem);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }
        });
    }
}
