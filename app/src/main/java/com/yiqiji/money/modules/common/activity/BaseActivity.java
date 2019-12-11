package com.yiqiji.money.modules.common.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.CustomAlertDialog;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.mybrocase.NetBroadcastReceiver;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.StatusBarUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyDialog;
import com.yiqiji.money.modules.common.view.MyTitleLayout;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;

import java.util.List;

public class BaseActivity extends FragmentActivity implements NetBroadcastReceiver.NetEvevt {

    private MyDialog dialog;
    private ViewGroup contentView;
    private TextView dialog_tv_title, layout_title_view_right_btn;
    private TextView dialog_tv_content;
    private Button dialog_btn_cancel;
    private Button dialog_btn_got;
    private Activity currentActivity;
    protected boolean isDestroy;
    public NetBroadcastReceiver.NetEvevt evevt;
    public Context mContext;
    protected Toast toast;//通用的toast
    protected Dialog managedDialog;//通用的dialog
    public NetBroadcastReceiver receiver;
    private MyTitleLayout titleLayout;
    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplicaction.getInstence().addActivity(this);
        mContext = this;
        LogUtil.log_msg(this.getClass().getSimpleName());

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.half_transparent);
        evevt = this;
        inspectNet();
        isDestroy = false;
        initDialog();
    }

    /**
     * 修改标题
     * titleb标题
     */
    public void changeTitle(String title) {
        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setTitle(title);

    }


    /**
     * 不限制title长度
     */
    public void unrestrictedTitleLenth(String title) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.unrestrictedTitleLenth(this, title, UIHelper.Dp2Px(this, 20), UIHelper.Dp2Px(this, 20));
        }

    }

    /**
     * 修改标题
     * titleb标题
     */
    public void changeRightBtn(String title, int color) {
        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.showRightBtn(title, color);

    }

    /**
     * 修改标题颜色
     */
    public void changeTitlteColor(int color) {
        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setTitleColor(color);

    }

    /**
     * 修改右边标题
     * titleb标题
     */
    public void changeRightBtn(String title) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.showRightBtn(title);
        }
    }


    /**
     * 修改标题
     * titleb标题
     */
    public void setReturnBtnVisiable(boolean isShow) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.showReturnBtn(isShow);

    }


    /**
     * 标题和背景
     * titleb标题
     */
    public void initTitle(String title) {

        initTitle(title, null);

    }

    public void initTitle(String title, OnClickListener onClickListener) {

        initTitle(title, -1, onClickListener);

    }


    /**
     * 有标题和左边图片如果id为-1为返回键
     * 如果onClickListener为空直接返回
     * title标题
     * drableid左边图片
     * onClickListener 按钮监听
     */
    public void initTitle(String title, int drableid, OnClickListener clickListener) {

        initTitle(title, drableid, null, clickListener);


    }


    /**
     * 有标题和右边文字没有左边图片
     * title标题
     * rightText右边文字
     * rightonClickListener右边图片监听
     */
    public void initTitle(String title, String rightText, OnClickListener clickListener) {

        initTitle(title, -1, rightText, clickListener);

    }

    /**
     * 左边文字右边文字没有左边图片没有标题
     * title标题
     * rightText右边文字
     * rightonClickListener右边图片监听
     */
    public void initTitle(OnClickListener clickListener, String leftText, String rightText) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.showReturnBtnText(leftText);
            initTitle("", -1, rightText, clickListener);
            titleLayout.showReturnBtn(false);
        }


    }

    /**
     * 左边文字右边文字没有左边图片有标题
     * title标题
     * rightText右边文字
     * rightonClickListener右边图片监听
     */
    public void initTitle(OnClickListener clickListener, String leftText, String rightText, String title) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.showReturnBtnText(leftText);
            initTitle(title, -1, rightText, clickListener);
            titleLayout.showReturnBtn(false);
        }


    }

    /**
     * 有标题和左边图片右边文字
     * 如果id为-1为返回键
     * title标题
     * drableid左边图片id
     * rightText右边文字
     * leftonClickListener左边图片监听
     * rightonClickListener右边图片监听
     */
    public void initTitle(String title, int drableid, String rightText, OnClickListener clickListener) {

        initTitle(title, drableid, -1, -1, rightText, clickListener);

    }


    /**
     * 有标题和左边图片和右边图片
     * 如果id为-1为返回键
     * title标题
     * drableid左边图片id
     * rightDrableid右边图片id
     * leftOnClickListener左边图片监听
     * rightOnClickListener右边图片监听
     */
    public void initTitle(String title, int drableid, int rightDrableid, OnClickListener clickListener) {

        initTitle(title, drableid, rightDrableid, -1, null, clickListener);

    }


    /**
     * @param title
     * @param drableid           左边返回
     * @param rightDrableid      右边第一个图片按钮
     * @param seconRightDrableid 右边第二个图片按钮  常用
     * @param rightText          右边文字按钮
     * @param clickListener      上面所有按键监听
     */
    public void initTitle(String title, int drableid, int rightDrableid, int seconRightDrableid, String rightText, OnClickListener clickListener) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            if (!TextUtils.isEmpty(title)) {
                title = StringUtils.onHideText(title, 8);
            }
            titleLayout.setTitle(title);
            if (drableid != -1) {
                titleLayout.setLeftBtnImg(drableid);
            } else {
                titleLayout.setLeftBtnImg(R.drawable.left_white_arrow_ui);
            }
            if (rightDrableid != -1) {
                titleLayout.showRightImgBtn(rightDrableid);
            }
            if (seconRightDrableid != -1) {
                titleLayout.showRightSecondImgBtn(seconRightDrableid);
            }

            if (!TextUtils.isEmpty(rightText)) {
                titleLayout.showRightBtn(rightText, R.color.main_back);
            }

            if (clickListener == null) {
                titleLayout.leftImageFinish(this);

            } else {
                titleLayout.setListener(clickListener);
            }
        }

    }

    /**
     * @param title                * @param titledrableid        titile图片
     * @param drableid             左边返回
     * @param rightDrableid        右边第一个图片按钮
     * @param seconRightDrableid   右边第二个图片按钮  常用
     * @param noteIsShow           是否显示消息提示
     * @param leftOnClickListener  返回建监听
     * @param rightOnClickListener 右边所有按钮监听
     */
    public void initTitle(String title, int titledrableid, int bagroudColor, int drableid, int rightDrableid, int seconRightDrableid, boolean noteIsShow, OnClickListener leftOnClickListener, OnClickListener rightOnClickListener) {
        initTitle(title, -1, titledrableid, bagroudColor, drableid, rightDrableid, seconRightDrableid, noteIsShow, leftOnClickListener, rightOnClickListener);
    }

    /**
     * @param title                * @param titledrableid        titile图片
     * @param colorid              标题颜色
     * @param drableid             左边返回
     * @param rightDrableid        右边第一个图片按钮
     * @param seconRightDrableid   右边第二个图片按钮  常用
     * @param noteIsShow           是否显示消息提示
     * @param leftOnClickListener  返回建监听
     * @param rightOnClickListener 右边所有按钮监听
     */
    public void initTitle(String title, int colorid, int titledrableid, int bagroudColor, int drableid, int rightDrableid, int seconRightDrableid, boolean noteIsShow, OnClickListener leftOnClickListener, OnClickListener rightOnClickListener) {

        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.setBackgroundColor(getResources().getColor(bagroudColor));
            if (!TextUtils.isEmpty(title)) {
                title = StringUtils.onHideText(title, 8);
            }
            titleLayout.setTitle(title);
            if (colorid > 0) {
                titleLayout.setTitleColor(colorid);
            }

            titleLayout.setNotesHodeOrShow(noteIsShow);
            if (drableid != -1) {
                titleLayout.setLeftBtnImg(drableid);
            } else {
                titleLayout.setLeftBtnImg(R.drawable.left_white_arrow_ui);
            }
            if (rightDrableid != -1) {
                titleLayout.showRightImgBtn(rightDrableid);
            }
            if (seconRightDrableid != -1) {
                titleLayout.showRightSecondImgBtn(seconRightDrableid);
            }

            titleLayout.setTitleImage(titledrableid);

            if (leftOnClickListener == null) {
                ImageView leftImageView = (ImageView) this.findViewById(R.id.layout_title_view_return);
                leftImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            } else {
                titleLayout.setListener(leftOnClickListener);
            }
            if (rightOnClickListener != null) {
                titleLayout.setListener(rightOnClickListener);
            }
        }
    }

    /**
     * 底部两个按钮
     *
     * @param leftText
     * @param rightText
     * @param onClickListener
     */
    public void bottomTwoButtonText(String leftText, String rightText, OnClickListener onClickListener) {
        if (!this.isDestroy) {
            TextView leftButton = (TextView) findViewById(R.id.share_button);
            TextView rightButton = (TextView) findViewById(R.id.settlement_button);
            leftButton.setText(leftText);
            rightButton.setText(rightText);
            if (onClickListener != null) {
                leftButton.setOnClickListener(onClickListener);
                rightButton.setOnClickListener(onClickListener);
            }

        }
    }


    /**
     * @param drawbleid
     * @param visible
     */
    public void setViewVisible(int drawbleid, int visible) {
        if (!this.isDestroy) {
            View view = findViewById(drawbleid);
            if (view != null) {
                view.setVisibility(visible);
            }
        }
    }

    /**
     * 底部两个按钮是否显示
     *
     * @param visible
     */

    public void bottomTwoButtonGoneOrVisible(int visible) {
        if (!this.isDestroy) {
            LinearLayout twobuttonlayout = (LinearLayout) findViewById(R.id.myid_button);
            twobuttonlayout.setVisibility(visible);
        }
    }

    public void changeRightImage(int drableidRight) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.showRightImgBtn(drableidRight);
    }

    /**
     * 修改title文案
     */
    public void changeTitleText(String titleText) {
        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.changeInitBookTitle(titleText);
    }

    /**
     * 修改title只用在首页
     *
     * @param title
     * @param titledrableid
     */
    public void changeTitle(String title, int titledrableid, int colorid) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.synchronizationTitle(titledrableid, title, colorid);
    }


    /**
     * s是否隐藏title
     *
     * @param visibleOrgone
     */
    public void setTiltgone(int visibleOrgone) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setAnimation(null);
        if (visibleOrgone == View.GONE) {
            titleLayout.cleaSysAnimation();
        }
        titleLayout.setVisibility(visibleOrgone);

    }

    /**
     * 跟换title背景色
     *
     * @param bagroudColor
     */

    public void setBagroudColor(int bagroudColor) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setBackgroudColor(bagroudColor);
    }

    /**
     * 支队首页有效
     */
    public void onelyTitleShow(String title) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.onelyTitleShow(title);
        }

    }

    /**
     * 显示账本消息
     *
     * @param
     */
    public void setBookNoteShow(int number) {
        if (!this.isDestroy) {
            if (titleLayout == null) {
                titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
            }
            titleLayout.setBookNoteShow(number);
        }
    }

    /**
     * 显示消息
     * 0 为不显示消息数量
     *
     * @param
     */
    public void setMessageNoteShow(int number) {


        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setMessageNoteShow(number);
    }

    public void setRightImgBtnPading(int left, int top, int right, int bottom) {

        if (titleLayout == null) {
            titleLayout = (MyTitleLayout) this.findViewById(R.id.my_title);
        }
        titleLayout.setRightImgBtnPading(this, left, top, right, bottom);
    }


    private void initDialog() {
        dialog = new MyDialog(this);
        contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_like_ios, null);
        dialog_tv_title = (TextView) contentView.findViewById(R.id.tv_title_dialog_like_ios);
        dialog_tv_content = ((TextView) contentView.findViewById(R.id.tv_content_dialog_like_ios));
        dialog_btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel_dialog_like_ios);
        dialog_btn_got = (Button) contentView.findViewById(R.id.btn_got_dialog_like_ios);
        dialog_tv_title.setText("提示");
        dialog_tv_content.setText("...");
        dialog_btn_cancel.setText("取消");
        dialog_btn_got.setText("好");
    }


    public void showMyDialog(String content, String leftButtonText) {
        dialog_tv_title.setText("提示");
        dialog_tv_content.setText(content);
        if (TextUtils.isEmpty(leftButtonText)) {
            dialog_btn_cancel.setVisibility(View.GONE);
        } else {
            dialog_btn_cancel.setText(leftButtonText);
        }
        ((View) dialog_btn_got.getParent()).setVisibility(View.GONE);

        dialog_btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.showDialog(contentView);
    }

    public void showMyDialog(String title, String cotent, String leftButtonText, String rightButtonText,
                             DialogClickListener leftButtonClickListener, DialogClickListener rightButtonClickListener) {
        if (TextUtils.isEmpty(title)) {
            dialog_tv_title.setText("提示");
        } else {
            dialog_tv_title.setText(title);
        }
        dialog_tv_content.setText(cotent);
        if (TextUtils.isEmpty(leftButtonText)) {
            dialog_btn_cancel.setVisibility(View.GONE);
        } else {
            dialog_btn_cancel.setText(leftButtonText);
        }
        if (TextUtils.isEmpty(rightButtonText)) {
            ((View) dialog_btn_got.getParent()).setVisibility(View.GONE);
        } else {
            ((View) dialog_btn_got.getParent()).setVisibility(View.VISIBLE);
            dialog_btn_got.setText(rightButtonText);
        }

        if (leftButtonClickListener == null) {
            dialog_btn_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
        } else {
            dialog_btn_cancel.setOnClickListener(leftButtonClickListener);
        }

        if (rightButtonClickListener == null) {
            dialog_btn_got.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
        } else {
            dialog_btn_got.setOnClickListener(rightButtonClickListener);
        }

        dialog.setCancelable(true);
        dialog.showDialog(contentView);
    }

    public void showMyDialogDropped(final Context context, String title, String cotent, String leftButtonText) {
        if (TextUtils.isEmpty(title)) {
            dialog_tv_title.setText("提示");
        } else {
            dialog_tv_title.setText(title);
        }
        dialog_tv_content.setText(cotent);
        if (TextUtils.isEmpty(leftButtonText)) {
            dialog_btn_cancel.setVisibility(View.GONE);
        } else {
            dialog_btn_cancel.setText(leftButtonText);
        }

        dialog_btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (context instanceof Activity && !(((Activity) context) instanceof HomeActivity)) {
                    IntentUtils.startActivityOnLogin((Activity) context, IntentUtils.LoginIntentType.MINE);
                    MyApplicaction.getInstence().exit();
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.showDialog(contentView);
    }


    public class DialogClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            dialog.dismiss();
        }
    }

    /***
     * 数据请求返回失败的情况下使用
     *
     * @param simpleMsg
     */
    public void showToast(SimpleMsg simpleMsg) {
        if (!this.isDestroy) {
            if (simpleMsg != null) {
                if (simpleMsg.getErrCode() == RequsterTag.DROPPED) {
                    if (isLogin()) {
                        showToast(simpleMsg.getErrMsg());
                    }
                    IntentUtils.drappedClear(this);
//                EventBus.getDefault().post(RequsterTag.RefreshMine);
                } else {
                    showToast(simpleMsg.getErrMsg());
                }
            } else {
                //暂不做处理
            }
        }

    }

    /***
     * 数据请求返回失败的情况下使用，这里会对特殊errcode做个性化处理(需要打开网络连接)
     *
     * @param simpleMsg
     */
    public void showToast(SimpleMsg simpleMsg, boolean isOpenNetwork) {
        if (!this.isDestroy) {
            if (simpleMsg != null) {
                if (simpleMsg.getErrCode() == 20012) {
                    LoginConfig.getInstance().setTokenId("");
                    IntentUtils.drappedClear(this);
//                EventBus.getDefault().post(RequsterTag.RefreshMine);
                }
                if (simpleMsg.getErrCode() == -100) {
                    if (isOpenNetwork) {
                        InternetUtils.setNetworkMethod(this);
                    } else {
                        showToast(simpleMsg.getErrMsg());
                    }

                } else {
                    showToast(simpleMsg.getErrMsg());
                }
            } else {
                //暂不做处理
            }
        }
    }


    /***
     * toast 封装
     *
     * @param text
     */
    public void showToast(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    /***
     * toast 封装
     *
     * @param text
     * @param showType ：传0为LENGTH_SHORT
     *                 ：传1为LENGTH_LONG
     */
    public void showToast(String text, int showType) {
        if (this.toast == null) {
            this.toast = Toast.makeText(this, text, showType);
        } else {
            this.toast.setText(text);
            this.toast.setDuration(showType);
        }
        this.toast.show();
    }


    /***
     * 加载数据dialog
     */
    public void showLoadingDialog() {
        if (!this.isDestroy) {
            this.dismissDialog();
            LoadingDialog loadingDialog = new LoadingDialog(this, R.layout.dialog_layout, R.style.DialogTheme);
            this.managedDialog = loadingDialog;
            managedDialog.setCancelable(false);
            loadingDialog.show();
        }
    }

    /***
     * 加载数据dialog是否可以点击消失
     */
    public void showLoadingDialog(boolean isDialogCanDismiss) {
        if (!this.isDestroy) {
            this.dismissDialog();
            LoadingDialog loadingDialog = new LoadingDialog(this, R.layout.dialog_layout, R.style.DialogTheme);
            this.managedDialog = loadingDialog;
            managedDialog.setCancelable(isDialogCanDismiss);
            loadingDialog.show();
        }
    }

    /***
     * dismiss所有正在显示的对话框
     */
    public void dismissDialog() {
        if (!this.isDestroy) {
            if (this.managedDialog != null && this.managedDialog.isShowing()) {
                this.managedDialog.dismiss();
            }
            this.managedDialog = null;
        }
    }

    /**
     * dialog 有标题内容左右按钮是否点击取消message是否支持html
     *
     * @param title
     * @param message
     * @param yesText
     * @param cannelText
     * @param isHtml
     * @param cacenl
     * @param yesTextonClickListener
     * @param annelTextonClickListener
     */
    public void showSimpleAlertDialog(String title, String message, String yesText, String cannelText, boolean isHtml,
                                      boolean cacenl, OnClickListener yesTextonClickListener, OnClickListener annelTextonClickListener) {

        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            if (isHtml) {
                customAlertDialog.setMessageHtml(message);
            } else {
                customAlertDialog.setMessage(message);
            }
            customAlertDialog.setRightButton(yesText, yesTextonClickListener);
            customAlertDialog.setLeftButton(cannelText, annelTextonClickListener);
            customAlertDialog.setCancelable(cacenl);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }

    }

    /**
     * dialog 有标题内容左右按钮是否点击取消自定义view
     *
     * @param title
     * @param view
     * @param yesText
     * @param cannelText
     * @param cacenl
     * @param yesTextonClickListener
     * @param annelTextonClickListener
     */
    public void showSimpleAlertDialog(String title, View view, String yesText, String cannelText,
                                      boolean cacenl, OnClickListener yesTextonClickListener, OnClickListener annelTextonClickListener) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            customAlertDialog.setMessage("");
            customAlertDialog.setView(view);
            customAlertDialog.setRightButton(yesText, yesTextonClickListener);
            customAlertDialog.setLeftButton(cannelText, annelTextonClickListener);
            customAlertDialog.setCancelable(cacenl);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }

    }

    /**
     * 只有一个按钮dialog（有标题和消息体）
     *
     * @param title
     * @param message
     * @param oneText
     * @param cacenl
     * @param oneTextOnClickListener
     */
    public void showSimpleAlertDialog(String title, String message, String oneText, boolean cacenl, OnClickListener oneTextOnClickListener) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            customAlertDialog.setMessage(message);
            customAlertDialog.showOneButton(oneText, oneTextOnClickListener);
            customAlertDialog.setCancelable(cacenl);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }
    }

    /**
     * 只显示消息
     *
     * @param message
     */
    public void showSimpleAlertDialog(String message) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);

            customAlertDialog.setMessageOnly(message);
            customAlertDialog.setCancelable(true);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }
    }

    /**
     * 新增吐槽和好评弹窗
     *
     * @param title
     * @param message
     * @param upText
     * @param Uplistener
     * @param debunklistener
     * @param isHtml
     */
    public void showSimpleAlertDialog(String title, String message, String upText, String version, OnClickListener Uplistener, OnClickListener debunklistener,
                                      boolean isHtml, boolean isCancle) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            if (isHtml) {
                customAlertDialog.setMessageHtml(message);
            } else {
                customAlertDialog.setMessage(message);
            }
            customAlertDialog.showUpButton(upText, version, Uplistener, debunklistener, isCancle);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();
        }
    }


    /**
     * 账单添加成功显示
     */
    public void billAddShow() {
        if (!this.isDestroy) {
            TextView textView = (TextView) this.findViewById(R.id.add_bill_text);
            if (textView != null) {
                AnimationUtil.setAddBillShow(this, textView);
            }
        }
    }

    /**
     * 获取自己成员名称
     */
    public String getMySelfMemeberName(String mMyuid, List<BooksDbMemberInfo> booksDbMemberInfos) {
        String member_name = "";
        if (!this.isDestroy) {

            String deviceid = LoginConfig.getInstance().getDeviceid();
            if (booksDbMemberInfos != null) {
                for (int i = 0; i < booksDbMemberInfos.size(); i++) {
                    if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                        if (deviceid.equals(booksDbMemberInfos.get(i).getDeviceid())) {
                            member_name = booksDbMemberInfos.get(i).getUsername();
                        }
                    } else {
                        if (mMyuid.equals(booksDbMemberInfos.get(i).getUserid())) {
                            member_name = booksDbMemberInfos.get(i).getUsername();
                        }
                    }
                }
            }
        }

        return member_name;
    }

    /**
     * 获取自己成员memberid
     */
    public String getMySelfNyMemeberId(String mMyuid, List<BooksDbMemberInfo> booksDbMemberInfos) {
        String member_id = "";
        if (!this.isDestroy) {
            String deviceid = LoginConfig.getInstance().getDeviceid();
            if (booksDbMemberInfos != null) {
                for (int i = 0; i < booksDbMemberInfos.size(); i++) {
                    if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                        if (deviceid.equals(booksDbMemberInfos.get(i).getDeviceid())) {
                            member_id = booksDbMemberInfos.get(i).getMemberid();
                            break;
                        }
                    } else {
                        if (mMyuid.equals(booksDbMemberInfos.get(i).getUserid())) {
                            member_id = booksDbMemberInfos.get(i).getMemberid();
                            break;
                        }
                    }

                }
            }

        }

        return member_id;
    }

    /**
     * 判断是否是群主true可以操作false不能操作
     */
    public boolean isGroup(String userid, String deviceid, BooksDbMemberInfo booksDbMemberInfo) {
        boolean isGroups = true;
        if (!this.isDestroy) {
            String mydeviceid = LoginConfig.getInstance().getDeviceid();
            String myuserid = LoginConfig.getInstance().getUserid();
            if (booksDbMemberInfo != null) {
                if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                    if (deviceid.equals(booksDbMemberInfo.getDeviceid())) {
                        if (!mydeviceid.equals(deviceid)) {
                            isGroups = false;
                        }
                    }
                } else {
                    if (userid.equals(booksDbMemberInfo.getUserid())) {
                        if (!myuserid.equals(userid)) {
                            isGroups = false;
                        }
                    }
                }

            }
        }

        return isGroups;
    }

    /**
     * 是否是账本创建者
     */
    public boolean isBookFount(String bookuserid, String bookdeviceid, BooksDbMemberInfo booksDbMemberInfo) {
        boolean isBookFount = false;
//        String mydeviceid = LoginConfig.getInstance().getDeviceid();
//        String myuserid = LoginConfig.getInstance().getUserid();
        String tokenid = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenid)) {
            if (bookdeviceid.equals(booksDbMemberInfo.getDeviceid())) {
                isBookFount = true;
            }
        } else {
            if (bookuserid.equals(booksDbMemberInfo.getUserid())) {
                isBookFount = true;
            }
        }
        return isBookFount;
    }

    /**
     * 是否是账单创建者
     */
    public boolean isBookFount(String billuserid, String billdeviceid, String memberuserid, String memberdeveceid) {
        boolean isBookFount = false;
//        String mydeviceid = LoginConfig.getInstance().getDeviceid();
//        String myuserid = LoginConfig.getInstance().getUserid();
        String tokenid = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenid)) {
            if (billdeviceid.equals(memberdeveceid)) {
                isBookFount = true;
            }
        } else {
            if (billuserid.equals(memberuserid)) {
                isBookFount = true;
            }
        }
        return isBookFount;
    }

    /**
     * 本人是否是账本创建者
     */
    public boolean isBookFount(String bookuserid, String bookdeviceid) {
        boolean isBookFount = false;
        String mydeviceid = LoginConfig.getInstance().getDeviceid();
        String myuserid = LoginConfig.getInstance().getUserid();
        String tokenid = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenid)) {
            if (bookdeviceid.equals(mydeviceid)) {
                isBookFount = true;
            }
        } else {
            if (bookuserid.equals(myuserid)) {
                isBookFount = true;
            }
        }
        return isBookFount;
    }

    /**
     * 成员头像是否需要显示我的
     */
    public boolean getIsNeedShowMine() {
        boolean isNeedShowMine = true;
        if (!isDestroy) {

            int issetavatar = LoginConfig.getInstance().getIssetavatar();    //是否修改过头像
            int issetnick = LoginConfig.getInstance().getIssetnick();        //是否修改过昵称
            String wxid = "";
            String wbid = "";
            String qqid = "";
            String userinfojson = LoginConfig.getInstance().getUserinfojson();
            if (!TextUtils.isEmpty(userinfojson)) {
                UserInfo userInfo = GsonUtil.GsonToBean(userinfojson, UserInfo.class);
                if (userInfo != null) {
                    wxid = userInfo.getWxid();
                    wbid = userInfo.getWbid();
                    qqid = userInfo.getQqid();
                    if (issetavatar == 1 || issetnick == 1 || !TextUtils.isEmpty(wxid) || !TextUtils.isEmpty(wbid) || !TextUtils.isEmpty(qqid)) {
                        isNeedShowMine = false;
                    }
                }
            } else {
                if (issetavatar == 1 || issetnick == 1) {
                    isNeedShowMine = false;
                }
            }
        }

        return isNeedShowMine;
    }

    public void DroppedDialog(String msg) {
        showMyDialogDropped(this, "", msg, "知道了");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (!XzbUtils.isAppOnForeground()) {
            XzbUtils.isForn = false;// app进入后台
        }
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = InternetUtils.getNetWorkState(BaseActivity.this);
        return InternetUtils.isNetConnect(netMobile);
    }


    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        InternetUtils.isNetConnect(netMobile);
    }

    /**
     * 设置网络监听 注册广播
     */
    public void setRegistBreoadcast() {
        if (!this.isDestroy) {
            receiver = new NetBroadcastReceiver();
            if (evevt != null) {
                receiver.setEvevt(evevt);
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            this.registerReceiver(receiver, filter);
        }
    }

    /**
     * 取消广播注册
     */
    public void setUnRegistBreoadcast() {
        if (!this.isDestroy) {
            this.unregisterReceiver(receiver);
            receiver = null;
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public boolean isLogin() {
        String tokenId = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenId)) {
            return false;
        }
        return true;
    }

    public void showSimpleAlertDialog(String title, String message, boolean cacenldimiss, String yesText, View.OnClickListener yesclickListener, String cannelText, View.OnClickListener cannelclickListener) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            customAlertDialog.setMessage(message);
            customAlertDialog.setRightButton(yesText, yesclickListener);
            customAlertDialog.setLeftButton(cannelText, cannelclickListener);
            customAlertDialog.setCancelable(cacenldimiss);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }
    }

    public void showSimpleAlertDialog(String title, String message, String yesText, View.OnClickListener yesclickListener, String cannelText, View.OnClickListener cannelclickListener) {
        if (!this.isDestroy) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(mContext);
            customAlertDialog.setTitle(title);
            customAlertDialog.setMessage(message);
            customAlertDialog.setRightButton(yesText, yesclickListener);
            customAlertDialog.setLeftButton(cannelText, cannelclickListener);
            this.managedDialog = customAlertDialog;
            customAlertDialog.show();

        }
    }

}
