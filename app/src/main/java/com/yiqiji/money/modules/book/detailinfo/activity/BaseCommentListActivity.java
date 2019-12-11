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
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.book.detailinfo.vd_driver.BookCommentListAdapter;
import com.yiqiji.money.modules.book.detailinfo.view.CommentEdiTextView;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/12.
 */

public abstract class BaseCommentListActivity extends BaseActivity {

    @BindView(R.id.iv_details_return)
    public ImageView ivDetailsReturn;
    @BindView(R.id.commentEdit)
    public CommentEdiTextView commentEdit;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.ryv_book_comment_list)
    public RecyclerView ryvBookCommentList;


    private BaseQuickAdapter adapter;
    public boolean showInput;
    public RetryDialogHandler retryDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comment_list_layout);
        ButterKnife.bind(this);
        showInput=getIntent().getBooleanExtra(DataConstant.BUNDLE_KEY_SHOW_INPUT_BOARD,false);
        getBundleData();
        initView();
        initEvent();
        getCommentListData();
    }

    private void initView() {
        retryDialogHandler = new RetryDialogHandler(this);
        adapter = getAdapter();
        ryvBookCommentList.setLayoutManager(new LinearLayoutManager(this));
        ryvBookCommentList.setAdapter(adapter);
    }

    public void initEvent() {
        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCommentListData();
            }
        });

        commentEdit.setOnSendCommentListener(new CommentEdiTextView.OnSendCommentListener() {
            @Override
            public void onSend(String text) {
                sendBookComment(text);
            }
        });

        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化页面数据
     */
    public void refreshViewData() {
        if(getCommentIsEmpty()){
            showEmptyView();
        }else {
            setRecycleViewData();
        }
        if(showInput){
            showInputBoard();
        }
    }


    /**
     * 显示空页面
     */
    private void showEmptyView(){
        adapter.getData().clear();
        ryvBookCommentList.setHasFixedSize(true);
        View notDataView = getLayoutInflater().inflate(R.layout.empty_view_book_comment_layout, (ViewGroup) ryvBookCommentList.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputBoard();
            }
        });
        adapter.setEmptyView(notDataView);
        adapter.notifyDataSetChanged();
        tvTitle.setText("暂无评论");

    }

    /**
     * 获取BundleData
     */
    public abstract void getBundleData();
    /**
     * 获取评论列表数据
     */
    public abstract void getCommentListData();
    /**
     * 发送账本评论
     * @param commentText
     */
    public abstract void sendBookComment(String commentText);
    /**
     * 获取评论列表是否为空
     * @return
     */
    public abstract boolean getCommentIsEmpty();
    /**
     * 显示评论列表数据
     */
    public abstract void setRecycleViewData();
    /**
     * 获取适配器
     */
    public abstract BaseQuickAdapter getAdapter();

    /**
     * 弹出软键盘
     */
    private void showInputBoard(){
        showInput=false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                commentEdit.showInputBoard();
            }
        },500);
    }
}
