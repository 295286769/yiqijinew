package com.yiqiji.money.modules.book.detailinfo.manager;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.common.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leichi on 2017/6/13.
 */

public class BookDataController {

    static String LOCK = "LOCK";
    static BookDataController bookDataController;
    ViewCallBack replyCommentObserver;

    private BookDataController() {

    }

    public static BookDataController getInstance() {
        if (bookDataController == null) {
            synchronized (LOCK) {
                bookDataController = new BookDataController();
            }
        }
        return bookDataController;
    }

    /**
     * 根据账本的id查询账单列表
     *
     * @param accountBookId 账本id
     * @param viewCallBack
     */
    public void getBookBillInfo(final String accountBookId, String mMemberid, int sortType, int pagetype, final ViewCallBack<BookBillModel> viewCallBack) {
        BookDataManager.getBookBillInfo(accountBookId, mMemberid, sortType,pagetype, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject = (JSONObject) o;
                String json = jsonObject.getString("data");
                BookBillModel bookBillModel = GsonUtil.GsonToBean(json, BookBillModel.class);
                BookInfoListDataAssembler.insertValue(bookBillModel);
                viewCallBack.onSuccess(bookBillModel);
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
    public void getBookCommentList(final String accountBookId, final ViewCallBack<List<BookCommentModel>> viewCallBack) {
        BookDataManager.getBookCommentList(accountBookId, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject = (JSONObject) o;
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                ArrayList<BookCommentModel> arrayList = new ArrayList<>();
                if (jsonArray == null || jsonArray.length() == 0) {
                    viewCallBack.onSuccess(arrayList);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(GsonUtil.GsonToBean(jsonArray.getString(i), BookCommentModel.class));
                }
                viewCallBack.onSuccess(arrayList);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
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
    public void sendBookComment(final String accountBookId, String content, final ViewCallBack callBack) {
        BookDataManager.sendBookCommnet(accountBookId, content, callBack);
    }

    /**
     * 对账本的评论进行回复
     *
     * @param accountBookId
     * @param content
     * @param callBack
     */
    public void sendBookCommentReplay(final String accountBookId, String topNodeId, String childNodeId, String content, final ViewCallBack callBack) {
        BookDataManager.sendBookCommentReplay(accountBookId, topNodeId, childNodeId, content, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                callBack.onSuccess(o);
                if (replyCommentObserver != null) {
                    replyCommentObserver.onSuccess(o);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }
        });
    }


    /**
     * 对账本的评论进行回复
     * @param commentId
     * @param callBack
     */
    public void deleteBookCommentReplay(final String commentId,final ViewCallBack callBack){
        BookDataManager.deleteBookComment(commentId, new ViewCallBack(){
            @Override
            public void onSuccess(Object o) throws Exception {
                callBack.onSuccess(o);
                if(replyCommentObserver!=null){
                    replyCommentObserver.onSuccess(o);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }
        });
    }


    public void addReplyCommentObserver(ViewCallBack replyCommentObserver) {
        this.replyCommentObserver = replyCommentObserver;
    }

}
