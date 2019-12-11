package com.yiqiji.money.modules.book.detailinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.book.detailinfo.vd_driver.BookCommentListAdapter;
import com.yiqiji.money.modules.book.detailinfo.view.CommentEdiTextView;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookCommentListActivity extends BaseCommentListActivity {


    private String accountBookId;
    private BookCommentListAdapter adapter;
    private List<BookCommentModel> bookCommentModelList;


    public void initEvent() {
        super.initEvent();
        BookDataController.getInstance().addReplyCommentObserver(new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                getCommentListData();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }
        });
    }

    /**
     * 获取评论列表数据
     */
    public void getCommentListData() {
        showLoadingDialog();
        BookDataController.getInstance().getBookCommentList(accountBookId, new ViewCallBack<List<BookCommentModel>>() {
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
        BookDataController.getInstance().sendBookComment(accountBookId, commentText, new ViewCallBack() {
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
        BookDataManager.saveReadCommentCount(accountBookId,bookCommentModelList.size());
        tvTitle.setText(bookCommentModelList.size()+"条评论");
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if(adapter==null){
            adapter = new BookCommentListAdapter(this);
        }
        return adapter;
    }

    @Override
    public void getBundleData() {
        accountBookId = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_ID);
        showInput=getIntent().getBooleanExtra(DataConstant.BUNDLE_KEY_SHOW_INPUT_BOARD,false);
    }

    /**
     * 外部打开Activity方法
     *
     * @param context
     * @param accountBookId
     */
    public static void open(Context context, String accountBookId,boolean showInput) {
        Intent intent = new Intent();
        intent.setClass(context, BookCommentListActivity.class);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        intent.putExtra(DataConstant.BUNDLE_KEY_SHOW_INPUT_BOARD,showInput);
        context.startActivity(intent);
    }
}
