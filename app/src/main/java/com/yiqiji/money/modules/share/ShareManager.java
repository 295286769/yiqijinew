package com.yiqiji.money.modules.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.FacadeUtil;
import com.yiqiji.money.modules.book.detailinfo.model.BookDetailModel;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.mybook.activity.AddBookActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.BooksActivity;
import com.yiqiji.money.modules.homeModule.mybook.entity.AddBookInfo;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/5/9.
 */

public class ShareManager {

    Context context;
    LoadingDialog loadingDialog;
    public ShareManager(Context context){
        this.context=context;
        loadingDialog=new LoadingDialog(context);
    }

    private static SHARE_MEDIA[] share_medias = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
            SHARE_MEDIA.QZONE};

    public void shareBook(final BookDetailModel bookDetailModel,boolean haveOpen) {

        //分享需要先登录
        if (!LoginConfig.getInstance().isLogin()) {
            Intent intent = new Intent();
            intent.setClass(context, LoginBaseActivity.class);
            context.startActivity(intent);
            return;
        }

        final String title = LoginConfig.getInstance().getUserName() + "分享了他的<<" + bookDetailModel.accountbooktitle + ">>,好奇他们都记了些什么？点击马上查看。";
        final String content = "订阅你喜欢的账本，可以参考作者的消费开支，来优化自己的消费，包括装修预算和旅行预算哦。";
        String imageUrl = bookDetailModel.accountbookbgimg;

        HashMap<String, String> map = new HashMap<>();
        map.put("id", bookDetailModel.accountbookid);
        map.put("memberid", bookDetailModel.memberid);
        map.put("inviteid", LoginConfig.getInstance().getUserid());

        final String shareUrl = Constants.BASE_URL + "/accounting/bill/h5index?" + FacadeUtil.getSignParamasString(map);

        ShareAction shareAction= new ShareAction((Activity) context).setDisplayList(share_medias);
        if(haveOpen){
            shareAction.addButton("share_book_in_community","share_book_in_community","icon","icon");
        }
        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if(snsPlatform.mShowWord.equals("share_book_in_community")){
                    openBook(bookDetailModel.accountbookid);
                }else {
                    ShareUtil.toShareForPlatform(context, share_media, R.drawable.icon, shareUrl, title, content);
                }
            }
        }).open();
    }

    private void openBook(String accountbookid){
        loadingDialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        String baseUrl = Constants.BOOKS_EDIT;
        hashMap.put("id", accountbookid);
        hashMap.put("isopen", "1");
        CommonFacade.getInstance().exec(baseUrl, hashMap, new ViewCallBack<AddBookInfo>() {
            @Override
            public void onSuccess(AddBookInfo o) throws Exception {
                super.onSuccess(o);
                loadingDialog.dismiss();
                ToastUtils.DiyToast(context,"分享成功");
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ToastUtils.DiyToast(context,simleMsg.getErrMsg());
                loadingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
