package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.MaxLengthWatcher;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;


/**
 * Created by leichi on 2017/6/16.
 */

public class ReplyCommentViewHandler {

    private Context mContext;

    private MyPopuwindows basePopuWindow;
    private View popWindowParent;

    private EditText content_praise;
    private TextView publish_button;
    private View contentView;
    private LoadingDialog loadingDialog;

    public ReplyCommentViewHandler(Context context){
        mContext=context;
        initView();
    }

    private void initView(){
        contentView=LayoutInflater.from(mContext).inflate(R.layout.beauty_finance_vedios, null);
        content_praise = (EditText) contentView.findViewById(R.id.content_praise);
        new MaxLengthWatcher(120, content_praise);

        publish_button = (TextView) contentView.findViewById(R.id.publish_button);
        publish_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = StringUtils.StringFilter(content_praise.getText().toString());

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.DiyToast(mContext, "您还没有添加评论内容");
                    return;
                }

                sendReplyCommentText(content);
            }
        });

        loadingDialog=new LoadingDialog(mContext);
    }

    private void sendReplyCommentText(String text){
        String topNodeId="";
        String childNodeId="";
        if(mBookCommentModel.isChild){
            topNodeId=mBookCommentModel.topnodeid;
            childNodeId=mBookCommentModel.commentid;
        }else {
            topNodeId=mBookCommentModel.commentid;
            childNodeId=mBookCommentModel.commentid;
        }
        loadingDialog.show();
        BookDataController.getInstance().sendBookCommentReplay(mBookCommentModel.accountbookid,topNodeId,childNodeId, text, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                loadingDialog.dismiss();
                reset();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                loadingDialog.dismiss();
                ToastUtils.DiyToast(mContext,simleMsg.getErrMsg());
            }
        });
    }

    public void setPopWindowParentView(View parent){
        this.popWindowParent=parent;
    }

    BookCommentModel mBookCommentModel;
    public void showView(BookCommentModel bookCommentModel){
        mBookCommentModel=bookCommentModel;
        basePopuWindow = new MyPopuwindows(mContext, contentView);
        basePopuWindow.showAtLocation(popWindowParent, Gravity.BOTTOM, 0, 0);
        content_praise.setFocusable(true);
        content_praise.requestFocus();
        showInputBoard();
        content_praise.setHint("回复@"+bookCommentModel.username);
        basePopuWindow.setInnerOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideInputBoard();
            }
        });
    }

    public void showInputBoard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(content_praise, 0);
            }
        },400);
    }

    public void hideInputBoard() {
        InputMethodManager inputManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(content_praise.getWindowToken(),0);
    }

    private void reset(){
        content_praise.getText().clear();
        basePopuWindow.dismiss();
        hideInputBoard();

    }
}
