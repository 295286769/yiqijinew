package com.yiqiji.money.modules.book.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.model.AttentionResultModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookDetailModel;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

/**
 * Created by xiaolong on 2017/7/19.
 */

public class AttentionView extends LinearLayout {

    private Context context;
    private ImageView imageIcon;
    private BookDetailModel mBookDetailModel;
    private String bookid, inviteid, isfollow, attentionId;
    private LoadingDialog loadingDialog;
    private WarnAlertDialog warnAlertDialog;
    public AttentionView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AttentionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_attention_layout, this);
        imageIcon = (ImageView) findViewById(R.id.image_icon);
        loadingDialog = new LoadingDialog(context);
        warnAlertDialog=new WarnAlertDialog(context);
        initEvent();
    }

    private void initEvent(){
        warnAlertDialog.setButtonOnClickListener(new WarnAlertDialog.OnButtonOnClickListener() {
            @Override
            public void onClick(WarnAlertDialog.ClickType type) {
                switch (type){
                    case OK:
                        toCancelAttentionBook();
                        break;
                    case CANCEL:
                        break;
                }
            }
        });
    }
    private void toAttentionBook() {
        loadingDialog.show();
        BookDataManager.toAttentionBook(bookid, inviteid, new ViewCallBack<AttentionResultModel>() {
            @Override
            public void onSuccess(AttentionResultModel model) throws Exception {
                loadingDialog.dismiss();
                isfollow = "1";
                attentionId = model.subscribeid;
                initViewData();
                ToastUtils.DiyToast(context, "关注成功!");
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                loadingDialog.dismiss();
            }
        });
    }


    private void toCancelAttentionBook() {
        loadingDialog.show();
        BookDataManager.toCancelAttentionBook(attentionId, new ViewCallBack<AttentionResultModel>() {
            @Override
            public void onSuccess(AttentionResultModel model) throws Exception {
                loadingDialog.dismiss();
                isfollow = "0";
                initViewData();
                ToastUtils.DiyToast(context, "取消成功!");
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                loadingDialog.dismiss();
            }
        });
    }


    public void setViewData(BookDetailModel bookDetailModel) {
        mBookDetailModel = bookDetailModel;
        bookid = mBookDetailModel.accountbookid;
        inviteid = mBookDetailModel.memberid;
        isfollow = mBookDetailModel.isfollow;
        attentionId = mBookDetailModel.subscribeid;
        initViewData();
    }

    OnClickListener toAttentionClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkLogin())
                toAttentionBook();
        }
    };

    OnClickListener toCancelAttentionClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkLogin()){
                warnAlertDialog.showWarnAlertDialog("确定要取消关注吗?");
            }
        }
    };


    private boolean checkLogin() {
        //分享需要先登录
        if (!LoginConfig.getInstance().isLogin()) {
            Intent intent = new Intent();
            intent.setClass(context, LoginBaseActivity.class);
            context.startActivity(intent);
            return false;
        }

        return true;
    }

    private void initViewData() {
        if (isfollow.equals("0")) {
            //未关注
            setOnClickListener(toAttentionClick);
            imageIcon.setBackgroundResource(R.drawable.icon_not_attention);
        } else {
            //已关注
            setOnClickListener(toCancelAttentionClick);
            imageIcon.setBackgroundResource(R.drawable.icon_attentioned);
        }
    }

}
