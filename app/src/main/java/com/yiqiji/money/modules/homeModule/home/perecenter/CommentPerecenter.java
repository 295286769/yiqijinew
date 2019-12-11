package com.yiqiji.money.modules.homeModule.home.perecenter;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BillDetailInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CommentList;
import com.yiqiji.money.modules.homeModule.home.entity.CommentListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/20.
 * address huang.weishui@71dai.com
 */
public class CommentPerecenter {
    public static void cmitComment(String id, String billid, String memberid, String content, final ViewCallBack viewCallBack) {
        HashMap<String, String> stringHashMap = StringUtils.getParamas("id", id, "billid", billid, "memberid", memberid, "content", content);

        CommonFacade.getInstance().exec(Constants.COMMENT, stringHashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                viewCallBack.onSuccess(addMemberResponse);
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

    public static void getComments(String mAccountbookid, String billid, final ViewCallBack<List<CommentListItem>> viewCallBack) {
        HashMap<String, String> hashMap = StringUtils.getParamas("id", mAccountbookid, "billid", billid);

        CommonFacade.getInstance().exec(Constants.GETCOMMENT, hashMap, new ViewCallBack<CommentList>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(CommentList o) throws Exception {
                super.onSuccess(o);
                CommentList commentList = o;
                List<CommentListItem> commentLists = commentList.getData();
                if (commentLists == null) {
                    commentLists = new ArrayList<CommentListItem>();
                }
                viewCallBack.onSuccess(commentLists);
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

    public static void getBookDetail(boolean isSubscribe, String mAccountid, final ViewCallBack<BooksDbInfo> viewCallBack) {
        String string_start_time = "";
        String string_end_time = "";
        HashMap<String, String> hashMap = null;
        if (isSubscribe) {
            hashMap = DateUtil.getmapParama("id", mAccountid,
                    "stime", string_start_time, "etime", string_end_time, "ref", "subscribe");
        } else {
            hashMap = DateUtil.getmapParama("id", mAccountid,
                    "stime", string_start_time, "etime", string_end_time);
        }
        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {


            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
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
            }
        });

    }

    public static void getBillInfo(String billid, final ViewCallBack<DailycostEntity> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("billid", billid);
        CommonFacade.getInstance().exec(Constants.BILLDETAIL, hashMap, new ViewCallBack<BillDetailInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(BillDetailInfo o) throws Exception {
                super.onSuccess(o);
                BillDetailInfo billDetailInfo = o;
                if (billDetailInfo.getData() != null) {
                    DailycostEntity dailycostEntity = billDetailInfo.getData();
                    dailycostEntity.setIssynchronization("true");
                    viewCallBack.onSuccess(dailycostEntity);
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

}
