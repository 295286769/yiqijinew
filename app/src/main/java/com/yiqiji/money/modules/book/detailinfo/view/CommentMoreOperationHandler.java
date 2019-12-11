package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;

import com.yiqiji.frame.core.system.AssistUtil;
import com.yiqiji.frame.ui.dialog.ListItemDialog;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.ToastUtils;

/**
 * Created by xiaolong on 2017/7/17.
 * 评论的更多操作
 */

public class CommentMoreOperationHandler {

    final String repay = "回复", copy = "复制", delete = "删除";

    private ListItemDialog listItemDialog;
    private BookCommentModel bookCommentModel;
    private LoadingDialog loadingDialog;
    private ReplyCommentViewHandler mReplyCommentViewHandler;
    private Context mContext;


    String[] hostItemNameList = {repay, copy, delete};
    String[] visitorItemNameList = {repay, copy};

    public CommentMoreOperationHandler(Context context, ReplyCommentViewHandler replyCommentViewHandler) {
        mContext = context;
        mReplyCommentViewHandler = replyCommentViewHandler;
        loadingDialog = new LoadingDialog(context);
    }

    public void initListItemDialog(BookCommentModel bookCommentModel) {
        this.bookCommentModel = bookCommentModel;
        boolean host = bookCommentModel.userid.equals(LoginConfig.getInstance().getUserid());
        listItemDialog = ListItemDialog.newInstance(host ? hostItemNameList : visitorItemNameList);
        initEvent();
    }

    private void initEvent() {
        listItemDialog.setOnItemClickListener(new ListItemDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(String itemName) {
                switch (itemName) {
                    case repay:
                        mReplyCommentViewHandler.showView(bookCommentModel);
                        break;
                    case copy:
                        AssistUtil.copy(bookCommentModel.content, mContext);
                        ToastUtils.DiyToast(mContext, "复制内容成功!");
                        break;
                    case delete:
                        deleteComment(bookCommentModel);
                        break;
                }
            }
        });
    }

    private void deleteComment(BookCommentModel bookCommentModel) {
        loadingDialog.show();
        BookDataController.getInstance().deleteBookCommentReplay(bookCommentModel.commentid, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                loadingDialog.dismiss();
                ToastUtils.DiyToast(mContext, "删除成功!");
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                loadingDialog.dismiss();
                ToastUtils.DiyToast(mContext, "删除失败!");
            }
        });
    }

    public void showMoreOperationDialog() {
        listItemDialog.show(mContext);
    }
}
