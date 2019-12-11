/**
 * DialogUtils.java[V 2.0.0] Classs : .utils.DialogUtils Dingmao.SUN create at 2016年10月28日
 * 下午5:53:31
 */
package com.yiqiji.money.modules.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementItemInfo;
import com.yiqiji.money.modules.common.view.MyDialog;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;

import java.util.List;

/**
 * .utils.DialogUtils
 *
 * @author Dingmao.SUN <br/>
 *         Create at 2016年10月28日 下午5:53:31
 */
public class DialogUtils {

    private static MyDialog mDialog;
    private static EditText etInput;
    private static ImageView captchCode;

    /**
     * @return the etInput
     */
    public static EditText getEtInput() {
        return etInput;
    }

    public static void showDialog(Activity context, int viewLayout, OnClickListener oklistener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new MyDialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(viewLayout, null);
        TextView confirm = (TextView) layout.findViewById(R.id.tv_recharge_confirm);
        confirm.setOnClickListener(oklistener);
        mDialog.showDialog(layout);

    }

    public static void showDialog(Activity context, View viewLayout) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = new MyDialog(context);

        mDialog.showDialog(viewLayout);

    }

    /**
     * 弹出框 有确认和返回 全员结算提示
     * <p/>
     * context
     * hasNoStocks 中间显示的文字
     * listener    确定按钮的事件
     */
//    @SuppressLint("InflateParams")
//    public static void showSettlementchDialog(Activity context, List<BooksSettlementItemInfo> booksDbMemberInfos,
//                                              OnClickListener clickListener) {
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//            mDialog = null;
//        }
//        etInput = null;
//        mDialog = new MyDialog(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_settlement, null);
//        MyRecyclerView recyclerView = (MyRecyclerView) layout.findViewById(R.id.listView);
//        LinearLayout layou_settlemnt = (LinearLayout) layout.findViewById(R.id.layou_settlemnt);
//        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(context);
//        recyclerView.setLayoutManager(fullyLinearLayoutManager);
//        TextView share_button = (TextView) layout.findViewById(R.id.cannel_button);
//        TextView settlement_button = (TextView) layout.findViewById(R.id.comit_button);
//        share_button.setOnClickListener(clickListener);
//        settlement_button.setOnClickListener(clickListener);
//
//        CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter = new CommonRecyclerViewAdapter<BooksSettlementItemInfo>(
//                context, booksDbMemberInfos) {
//
//            @Override
//            public int getLayoutViewId(int viewType) {
//                // TODO Auto-generated method stub
//                return R.layout.dailog_settlement_item;
//            }
//
//            @Override
//            public void convert(CommonRecyclerViewHolder h, BooksSettlementItemInfo entity, int position) {
//
//                if (entity.getRichman().equals("1")) {// 财主
//                    h.setText(R.id.pay_tex, "收取:");
//                    h.setTextColor(R.id.pay_tex, context.getResources().getColor(R.color.income));
//                    double balance = (double) (Double.parseDouble(entity.getReceivable()) == 0 ? 0.00 : Double
//                            .parseDouble(entity.getReceivable()));
//                    h.setText(R.id.bablance, XzbUtils.formatDouble("%.2f", balance));
//                } else {
//                    h.setText(R.id.pay_tex, "支付:");
//                    h.setTextColor(R.id.pay_tex, context.getResources().getColor(R.color.expenditure));
//                    double balance = (double) (Double.parseDouble(entity.getPayamount()) == 0 ? 0.00f : Double
//                            .parseDouble(entity.getPayamount()));
//                    h.setText(R.id.bablance, XzbUtils.formatDouble("%.2f", balance));
//
//                }
//                h.setText(R.id.userName, entity.getUsername());
//
//            }
//        };
//        recyclerView.setAdapter(adapter);
//
//        float screwith = XzbUtils.getPhoneScreen(context).widthPixels;
//        float screeHeight = XzbUtils.getPhoneScreen(context).heightPixels;
//        // RelativeLayout.LayoutParams layoutParams1 =
//        // (android.widget.RelativeLayout.LayoutParams) recyclerView
//        // .getLayoutParams();
//        // layoutParams1.width = (int) screwith;
//        // recyclerView.setLayoutParams(layoutParams1);
//        float height = 0;
//        height = booksDbMemberInfos.size() * UIHelper.Dp2Px(context, 45) + UIHelper.Dp2Px(context, 120);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layou_settlemnt.getLayoutParams();
//
//        if (height > (screeHeight / 2)) {
//            height = screeHeight / 2;
//        }
//        layoutParams.height = (int) height;
//        // layoutParams.width = (int) (screwith / 1.5);
//        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.bottomMargin = UIHelper.Dp2Px(context, 10);
//        layou_settlemnt.setLayoutParams(layoutParams);
//        mDialog.showDialog(layout);
//    }

    /**
     * 弹出框 有确认和返回
     *
     * @param context hasNoStocks 中间显示的文字
     *                listener    确定按钮的事件
     */
    @SuppressLint("InflateParams")
    public static void showCaptchDialog(Activity context, String titleStr, String okText, OnClickListener oklistener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        etInput = null;
        mDialog = new MyDialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_input_captch, null);
        TextView title = (TextView) layout.findViewById(R.id.dialog_text_title);
        title.setText(titleStr);
        etInput = (EditText) layout.findViewById(R.id.input_info);
        captchCode = (ImageView) layout.findViewById(R.id.captchcode);
        captchCode.setOnClickListener(oklistener);
        Button okBtn = (Button) layout.findViewById(R.id.exit_oks);
        okBtn.setText(okText);
        okBtn.setOnClickListener(oklistener);
        mDialog.showDialog(layout);
    }

    /**
     * 弹出框 有确认和返回
     *
     * @param context 中间显示的文字
     *                listener 确定按钮的事件
     */
    @SuppressLint("InflateParams")
    public static void showLoginPwdDialog(Activity context, String titleStr, String okText, OnClickListener oklistener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        etInput = null;
        mDialog = new MyDialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_input_captch, null);
        TextView title = (TextView) layout.findViewById(R.id.dialog_text_title);
        title.setText(titleStr);
        etInput = (EditText) layout.findViewById(R.id.input_info);
        layout.findViewById(R.id.captchcode).setVisibility(View.GONE);
        Button okBtn = (Button) layout.findViewById(R.id.exit_oks);
        okBtn.setText(okText);
        okBtn.setOnClickListener(oklistener);
        mDialog.showDialog(layout);
    }

    /**
     * 弹出框 有确认和返回
     *
     * @param context listener    确定按钮的事件
     */
    public static void showConfirm(Activity context, String titleStr, String msg, String okText,
                                   OnClickListener oklistener, String cancelText, OnClickListener cancellistener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

        if (context.getParent() != null)
            context = context.getParent();

        mDialog = new MyDialog(context, R.style.noDialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_two_lines_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.dialog_text_title);
        title.setText(titleStr);

        TextView hint_info = (TextView) layout.findViewById(R.id.hint_info);
        hint_info.setText(msg);

        Button okBtn = (Button) layout.findViewById(R.id.exit_oks);
        okBtn.setText(okText);
        okBtn.setOnClickListener(oklistener);

        Button cancal = (Button) layout.findViewById(R.id.exit_cancels);
        cancal.setText(cancelText);
        cancal.setOnClickListener(cancellistener);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mDialog.addContentView(layout, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * 弹出框 只有有确认
     *
     * @param context hasNoStocks
     *                中间显示的文字
     *                listener
     *                确定按钮的事件
     */
    public static void showConfirmOnlyDetermine(Activity context, String titleStr, String msg, String okText,
                                                OnClickListener oklistener) {
        if (mDialog != null && mDialog.isShowing())

            mDialog.dismiss();
        if (context.getParent() != null)
            context = context.getParent();

        mDialog = new MyDialog(context, R.style.noDialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_two_lines_layout, null);
        TextView title = (TextView) layout.findViewById(R.id.dialog_text_title);
        View line = (View) layout.findViewById(R.id.line);
        line.setVisibility(View.GONE);
        title.setText(titleStr);

        TextView hint_info = (TextView) layout.findViewById(R.id.hint_info);
        if (msg.contains("</br>")) {
            hint_info.setText(Html.fromHtml(msg));
        } else {
            hint_info.setText(msg);
        }


        Button okBtn = (Button) layout.findViewById(R.id.exit_oks);
        okBtn.setText(okText);
        okBtn.setOnClickListener(oklistener);

        Button cancal = (Button) layout.findViewById(R.id.exit_cancels);
        cancal.setVisibility(View.GONE);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mDialog.addContentView(layout, params);
        mDialog.setCancelable(false);
        setKeyBack(context);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private static void setKeyBack(final Activity context) {
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    context.moveTaskToBack(false);
                }
                return false;
            }
        });
    }

    public static boolean getDialogIsShow() {
        return mDialog == null ? false : mDialog.isShowing();
    }

    public static void dismissConfirmDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

    public static void dismissReviseInviterDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }
}
