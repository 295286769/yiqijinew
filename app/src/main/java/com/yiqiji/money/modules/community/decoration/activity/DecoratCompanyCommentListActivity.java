package com.yiqiji.money.modules.community.decoration.activity;

import android.content.Context;
import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.detailinfo.activity.BaseCommentListActivity;
import com.yiqiji.money.modules.book.detailinfo.activity.BookCommentListActivity;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.book.detailinfo.vd_driver.BookCommentListAdapter;
import com.yiqiji.money.modules.common.utils.ToastUtils;

import java.util.List;

/**
 * Created by leichi on 2017/8/2.
 */

public class DecoratCompanyCommentListActivity extends BaseCommentListActivity{

    String decorationCompanyId;
    private BookCommentListAdapter adapter;
    private List<BookCommentModel> bookCommentModelList;

    @Override
    public void getBundleData() {
        decorationCompanyId = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_ID);
        showInput=getIntent().getBooleanExtra(DataConstant.BUNDLE_KEY_SHOW_INPUT_BOARD,false);
    }

    /**
     * 获取评论列表数据
     */
    public void getCommentListData() {
        showLoadingDialog();
        BookDataController.getInstance().getBookCommentList(decorationCompanyId, new ViewCallBack<List<BookCommentModel>>() {
            @Override
            public void onSuccess(List<BookCommentModel> bookCommentModels) throws Exception {
                dismissDialog();
                bookCommentModelList = bookCommentModels;
                refreshViewData();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                retryDialogHandler.showRetryDialog("请求失败" + simleMsg.getErrMsg());
            }
        });
    }

    /**
     * 发送账本评论
     * @param commentText
     */
    public void sendBookComment(String commentText) {
        showLoadingDialog();
        BookDataController.getInstance().sendBookComment(decorationCompanyId, commentText, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                dismissDialog();
                getCommentListData();
                commentEdit.onSendSuccessCallBack();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                ToastUtils.DiyToast(getBaseContext(), simleMsg.getErrMsg());
            }
        });
    }

    @Override
    public boolean getCommentIsEmpty() {
        return bookCommentModelList==null||bookCommentModelList.size()==0;
    }

    @Override
    public void setRecycleViewData() {
        adapter.setDataList(bookCommentModelList);
        BookDataManager.saveReadCommentCount(decorationCompanyId,bookCommentModelList.size());
        tvTitle.setText(bookCommentModelList.size()+"条评论");
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if(adapter==null){
            adapter = new BookCommentListAdapter(this);
        }
        return adapter;
    }
}
