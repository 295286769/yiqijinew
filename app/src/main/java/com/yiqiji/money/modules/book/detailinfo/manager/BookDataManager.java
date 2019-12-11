package com.yiqiji.money.modules.book.detailinfo.manager;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.model.AttentionResultModel;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;

import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookDataManager {

    /**
     * 根据账本的id查询账单列表
     *
     * @param accountBookId 账本id
     * @param viewCallBack
     */
    public static void getBookBillInfo(final String accountBookId, String mMemberid, int sortType, int pagetype, final ViewCallBack viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountBookId);             // 账本ID
        hashMap.put("pagetype", pagetype + "");           // 成员ID
        hashMap.put("sort", String.valueOf(sortType));// 排序类型：0.降序（默认，又近至远），1.升序
        CommonFacade.getInstance().exec(Constants.BOOKS_BILL, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jsonObject = (JSONObject) o;
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

    /**
     * 根据账本的id查询评论列表
     *
     * @param accountBookId 账本id
     * @param viewCallBack
     */
    public static void getBookCommentList(final String accountBookId, final ViewCallBack viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountBookId);// 账本ID
        CommonFacade.getInstance().exec(Constants.GETCOMMENT, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jsonObject = (JSONObject) o;
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

    /**
     * 对账本进行评论
     *
     * @param accountBookId
     * @param content
     * @param callBack
     */
    public static void sendBookCommnet(final String accountBookId, String content, final ViewCallBack callBack) {
        sendBookComment(accountBookId, "", "", content, callBack);
    }


    /**
     * 对账本的评论进行回复
     *
     * @param accountBookId
     * @param content
     * @param callBack
     */
    public static void sendBookCommentReplay(final String accountBookId, String topNodeId, String childNodeId, String content, final ViewCallBack callBack) {
        sendBookComment(accountBookId, topNodeId, childNodeId, content, callBack);
    }

    /**
     * 对账本的评论进行回复
     *
     * @param accountBookId
     * @param content
     * @param callBack
     */
    public static void sendBookComment(final String accountBookId, String topNodeId, String childNodeId, String content, final ViewCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountBookId);// 账本ID
        hashMap.put("content", content);
        hashMap.put("topnodeid", topNodeId);
        hashMap.put("childnodeid", childNodeId);
        CommonFacade.getInstance().exec(Constants.COMMENT_BOOK, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("code").equals("0")) {
                    callBack.onSuccess(jsonObject);
                } else {
                    SimpleMsg simleMsg = new SimpleMsg(0, jsonObject.getString("msg"));
                    callBack.onFailed(simleMsg);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callBack.onFinish();
            }
        });
    }

    /**
     * 删除账本的评论
     *
     * @param commentId
     * @param callBack
     */
    public static void deleteBookComment(final String commentId, final ViewCallBack callBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", commentId);// 账本ID
        CommonFacade.getInstance().exec(Constants.DELETE_COMMENT_BOOK, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("code").equals("0")) {
                    callBack.onSuccess(jsonObject);
                } else {
                    SimpleMsg simleMsg = new SimpleMsg(0, jsonObject.getString("msg"));
                    callBack.onFailed(simleMsg);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callBack.onFinish();
            }
        });
    }


    /**
     * 关注账本接口
     * @param id
     * @param inviteId
     * @param viewCallBack
     */
    public static void toAttentionBook(String id, String inviteId, final ViewCallBack<AttentionResultModel> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);               // 账本ID
        hashMap.put("inviteid", inviteId);   // 邀请人Uid
        CommonFacade.getInstance().exec(Constants.SAVESUBSCRIBE, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("code").equals("0")) {
                    AttentionResultModel model= GsonUtil.GsonToBean(jsonObject.getString("data"),AttentionResultModel.class);
                    viewCallBack.onSuccess(model);
                    EventBus.getDefault().post(model);
                }else {
                    SimpleMsg simpleMsg=new SimpleMsg(-1,"请求失败");
                    viewCallBack.onFailed(simpleMsg);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }
        });
    }

    /**
     * 取消关注账本接口
     * @param id
     * @param viewCallBack
     */
    public static void toCancelAttentionBook(String id, final ViewCallBack<AttentionResultModel> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);               // 账本ID
        CommonFacade.getInstance().exec(Constants.CANCLE_ACCOUNTER_SUBSCRIBE, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.getString("code").equals("0")) {
                    AttentionResultModel model= GsonUtil.GsonToBean(jsonObject.getString("data"),AttentionResultModel.class);
                    viewCallBack.onSuccess(model);
                    EventBus.getDefault().post(model);
                }else {
                    SimpleMsg simpleMsg=new SimpleMsg(-1,"请求失败");
                    viewCallBack.onFailed(simpleMsg);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }
        });
    }


    public static int getUnReadCommentCount(String key, int commentTotalCount) {
        int readCommentCount = SPUtils.getParam(DataConstant.SP_KEY_BOOK_READ_COMMENT_COUNT + key, 0);
        return commentTotalCount - readCommentCount;
    }

    public static void saveReadCommentCount(String key, int readCommentCount) {
        SPUtils.setParam(DataConstant.SP_KEY_BOOK_READ_COMMENT_COUNT + key, readCommentCount);
    }
}
