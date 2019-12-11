package com.yiqiji.money.modules.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.activity.BookCategoryEditActivity;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.common.entity.BookExpenditure.ChildBean;
import com.yiqiji.money.modules.common.view.CommonPopupWindow;
import com.yiqiji.money.modules.homeModule.write.WriteUtil;

import java.util.List;

public class popupWindowUtils {

    private CommonPopupWindow mApplayPopupWindow;

    private EditTextListener editTextListener;

    public static interface EditTextListener {
        void getTextToString(EditText editText);
    }

    public void setTextToStringListener(EditTextListener editTextListener) {
        this.editTextListener = editTextListener;
    }

    @SuppressLint("NewApi")
    public void showMonetIsOkPopupWindow(final Activity activity, String payMoney, String paymentMoneyCount,
                                         String participateMoneyCount) {
        backgroundAlpha(0.6f, activity);
        if (mApplayPopupWindow != null && mApplayPopupWindow.isShowing()) {
            mApplayPopupWindow.dismiss();
            mApplayPopupWindow = null;
        }
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.popupwindow_ecpend_money_isok, null);
        TextView moneyText = (TextView) layout.findViewById(R.id.et_pop_money);
        moneyText.setText("记账总金额" + payMoney + ",付款人总金额" + paymentMoneyCount + ",参与人总金额" + participateMoneyCount
                + ",三者金额不相符,请调整后在保存");
        layout.findViewById(R.id.ll_money_determine).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                backgroundAlpha(1f, activity);
                mApplayPopupWindow.dismiss();
            }
        });

        mApplayPopupWindow = new CommonPopupWindow(activity);
        mApplayPopupWindow.setCommonPopupWindow(activity, layout, 0);
        mApplayPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }

    private int selectTag = 0;

    @SuppressLint("NewApi")
    public void showSonIconPopupWindow(boolean isExpenditure,String bookId,String parentName,String chilsSon_title, final Activity activity, List<ChildBean> list) {
        backgroundAlpha(0.6f, activity);
        if (mApplayPopupWindow != null && mApplayPopupWindow.isShowing()) {
            mApplayPopupWindow.dismiss();
            mApplayPopupWindow = null;
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.popu_son_icon, null);
        final LinearLayout ll_popu_son_icon = (LinearLayout) view.findViewById(R.id.ll_popu_son_icon);
        ScrollView sl_popu_son_icon = (ScrollView) view.findViewById(R.id.sl_popu_son_icon);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mApplayPopupWindow.dismiss();
                backgroundAlpha(1f, activity);
            }
        });

        if (list.size() > 5) {
            RelativeLayout.LayoutParams list_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            list_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            list_params.height = UIHelper.Dp2Px(activity, 50) * 6 + UIHelper.Dp2Px(activity, 5);
            sl_popu_son_icon.setLayoutParams(list_params);
        }
        View child;
        ImageView iv_son_icon;
        TextView tv_son_iconname;
        ImageView iv_item_select;


        //todo 在这里加入添加子类的选项
        for (int i = 0; i < list.size(); i++) {
            child = LayoutInflater.from(activity).inflate(R.layout.popu_item_son_icon, null);
            iv_son_icon = (ImageView) child.findViewById(R.id.iv_son_icon);
            tv_son_iconname = (TextView) child.findViewById(R.id.tv_son_iconname);
            iv_item_select = (ImageView) child.findViewById(R.id.iv_item_select);
            iv_item_select.setTag(i);
            tv_son_iconname.setText(list.get(i).getCategorytitle());
            String categoryid = list.get(i).getCategoryid() + "child";
            if (i == 0) {
                categoryid = list.get(i).getCategoryid();
            }
            WriteUtil.setImage(iv_son_icon, categoryid, list.get(i).getCategoryicon());

            child.setTag(i);

            child.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnSonIconClickListener.setonSonIcon(v);
                    ImageView imageview = (ImageView) v.findViewById(R.id.iv_item_select);
                    imageview.setVisibility(View.VISIBLE);
                    ViewGroup viewgroup = (ViewGroup) ll_popu_son_icon.getChildAt(selectTag);
                    viewgroup.findViewById(R.id.iv_item_select).setVisibility(View.GONE);
                    backgroundAlpha(1f, activity);
                    mApplayPopupWindow.dismiss();
                }
            });

            if (list.get(i).getCategorytitle().equals(chilsSon_title)) {
                selectTag = i;
                iv_item_select.setVisibility(View.VISIBLE);
            } else {
                iv_item_select.setVisibility(View.GONE);
            }

            ll_popu_son_icon.addView(child);
        }


        //添加[添加子类]按钮
        addAdditionChildCategory(isExpenditure,bookId,parentName,activity,ll_popu_son_icon,list.get(1));


        mApplayPopupWindow = new CommonPopupWindow(activity);
        mApplayPopupWindow.setCommonPopupWindow(activity, view, R.style.popu_son_icon);
        mApplayPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    //-----------------------------------添加[添加子类]按钮------------------------------------//
    private void addAdditionChildCategory(final boolean isExpenditure, final String bookId, final String parentName, final Activity activity, final LinearLayout ll_popu_son_icon, final ChildBean childBean){
        View child = LayoutInflater.from(activity).inflate(R.layout.popu_item_son_icon, null);
        ImageView iv_son_icon = (ImageView) child.findViewById(R.id.iv_son_icon);
        TextView tv_son_iconname = (TextView) child.findViewById(R.id.tv_son_iconname);

        iv_son_icon.setImageResource(R.drawable.icon_add_child_category);
        tv_son_iconname.setTextColor(activity.getResources().getColor(R.color.main_back));
        tv_son_iconname.setText("添加子类");

        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BookCategory bookCategory=getChildBeanMapBookCategory(isExpenditure,bookId,parentName,childBean);
                BookCategoryEditActivity.openAddChild(activity,bookCategory);
                mApplayPopupWindow.dismiss();
                backgroundAlpha(1f, activity);
            }
        });
        ll_popu_son_icon.addView(child);
    }

    private BookCategory getChildBeanMapBookCategory(boolean isExpenditure,String bookId,String parentName,ChildBean childBean){
        BookCategory bookCategory=new BookCategory();
        bookCategory.parentid=childBean.getParentid();
        bookCategory.categorytitle=childBean.getCategorytitle();
        bookCategory.accountbookid=bookId;
        bookCategory.parentName=parentName;
        bookCategory.billtype=isExpenditure?"1":"0";
        bookCategory.status="1";
        return bookCategory;
    }

    public interface onSonIconClickListener {
        void setonSonIcon(View child);
    }

    private onSonIconClickListener mOnSonIconClickListener;

    public void setIconClickListener(onSonIconClickListener onSonIconClickListener) {
        this.mOnSonIconClickListener = onSonIconClickListener;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha, Activity mActivity) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

}
