package com.yiqiji.money.modules.homeModule.home.perecenter;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/6/1.
 * address huang.weishui@71dai.com
 */
public class BookListPerecenter {

//    /**
//     * 重新拉去账本列表
//     */
//    public static void initBooks(final ViewCallBack viewCallBack) {
//
//        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack<BooksListInfo>() {
//            @Override
//            public void onSuccess(BooksListInfo o) throws Exception {
//                super.onSuccess(o);
//                boolean isHasBook = false;
//                BooksListInfo booksListInfo = o;
//                List<BooksDbInfo> booksDbInfos = booksListInfo.getAccountbook();
//                if (booksDbInfos != null || booksDbInfos.size() > 0) {
//                    isHasBook = true;
//
//                }
//                viewCallBack.onSuccess(isHasBook);
//
//            }
//
//            @Override
//            public void onFailed(SimpleMsg simleMsg) {
//                super.onFailed(simleMsg);
//                try {
//                    viewCallBack.onSuccess(false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////                ((HomeActivity) mContext).showToast(simleMsg);
//
//            }
//
//
//        });
//    }

    /**
     * 重新拉去账本列表
     */
    public static void initBooks(final ViewCallBack<BooksDbInfo> viewCallBack) {

        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack<BooksListInfo>() {
            @Override
            public void onSuccess(BooksListInfo o) throws Exception {
                super.onSuccess(o);
                BooksListInfo booksListInfo = o;
                List<BooksDbInfo> booksDbInfos = booksListInfo.getAccountbook();
                BooksDbInfo booksDbInfo = null;
                if (booksDbInfos != null && booksDbInfos.size() > 0) {
                    booksDbInfo = booksDbInfos.get(0);
                    DownUrlUtil.myBooks(booksDbInfos);
//                            String bookid = booksDbInfos.get(0).getAccountbookid();
                    LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());


                }
                viewCallBack.onSuccess(booksDbInfo);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
//                ((HomeActivity) mContext).showToast(simleMsg);

            }


        });
    }

    public static void getBookDetailinfo(final String mAccountid, final ViewCallBack viewCallBack) {
        String string_start_time = "";
        String string_end_time = "";
//        if (booksDbInfo.getSorttype().equals("0")) {
//            string_start_time = "";
//            string_end_time = "";
//        }
        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountid,
                "stime", string_start_time, "etime", string_end_time);

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
//                if (simleMsg == null) {
//                    return;
//                }
//                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
//                    ((HomeActivity) mContext).showToast(simleMsg);
//                } else {
//                    if (simleMsg.getErrCode() == 50099) {
//                        ((HomeActivity) mContext).showSimpleAlertDialog("提示", simleMsg.getErrMsg(), "确定", false, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ((HomeActivity) mContext).dismissDialog();
//                                //账本id不存在重新请求账本
//                                initBooks();
//                            }
//                        });
//                    } else {
//                        //账本id不存在重新请求账本
//                        initBooks();
//                    }
//
//
//                }
//
//
            }

        });

    }

}
