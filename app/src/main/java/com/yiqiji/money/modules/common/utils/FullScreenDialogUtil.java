package com.yiqiji.money.modules.common.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import java.util.List;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by Administrator on 2017/3/24.
 */
public class FullScreenDialogUtil {

    private static FullScreenDialog mDialog;


    private static int selectTag = 0;

    public void showDialog(Activity activity, String number, List<PropertyTransEntity> cunterList, final Oklistener oklistener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (cunterList == null) {
            return;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.item_add_capital, null);
        final LinearLayout ll_popu_son_icon = (LinearLayout) view.findViewById(R.id.ll_popu_son_icon);
        ScrollView sl_popu_son_icon = (ScrollView) view.findViewById(R.id.sl_popu_son_icon);
        if (!StringUtils.isEmptyList(cunterList) && cunterList.size() > 5) {
            LinearLayout.LayoutParams list_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            list_params.height = UIHelper.Dp2Px(activity, 50) * 6 + UIHelper.Dp2Px(activity, 5);
            sl_popu_son_icon.setLayoutParams(list_params);
        } else if (cunterList != null) {
//            PropertyTransEntity transEntity = new PropertyTransEntity();
//            transEntity.setAssetid("-1");
//            transEntity.setItemname("新建账户");
//            cunterList.add(0, transEntity);
        }
//        if (StringUtils.isEmptyList(cunterList)) {
//
//        }

        View child;
        ImageView iv_son_icon;
        TextView tv_son_iconname;
        TextView tv_son_remark;
        ImageView iv_item_select;

        for (int i = 0; i < cunterList.size(); i++) {
            final PropertyTransEntity propertyTransEntity = cunterList.get(i);
            child = LayoutInflater.from(activity).inflate(R.layout.popu_item_son_icon, null);
            iv_son_icon = (ImageView) child.findViewById(R.id.iv_son_icon);
            tv_son_iconname = (TextView) child.findViewById(R.id.tv_son_iconname);
            tv_son_remark = (TextView) child.findViewById(R.id.tv_son_remark);
            iv_item_select = (ImageView) child.findViewById(R.id.iv_item_select);
            iv_item_select.setTag(i);
            tv_son_iconname.setText(propertyTransEntity.getItemname());
            tv_son_remark.setText(propertyTransEntity.getMarktext());

            String url_service = propertyTransEntity.getItemicon();


            child.setTag(i);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oklistener.onOklistener(propertyTransEntity);

                }
            });
            ImageLoaderManager.loadImage(activity, url_service, iv_son_icon);
            iv_item_select.setVisibility(View.GONE);
            if (cunterList.get(i).getAssetid().equals(number)) {
                iv_item_select.setVisibility(View.VISIBLE);
            }

            if (cunterList.get(i).getAssetid().equals("-1")) {
//                iv_item_select.setVisibility(View.VISIBLE);
                ImageLoaderManager.loadImage(activity, R.drawable.add_account, iv_son_icon);
                tv_son_iconname.setTextColor(activity.getResources().getColor(R.color.main_back));
//                XzbUtils.displayImage(iv_son_icon, url_service, 0);
            } else {
//                iv_item_select.setVisibility(View.GONE);

//                XzbUtils.displayImage(iv_son_icon, R.drawable.add_account, 0);
            }

            ll_popu_son_icon.addView(child);
        }
        mDialog = new FullScreenDialog(activity);
        mDialog.showDialog(view);
    }


    public interface Oklistener {
        void onOklistener(PropertyTransEntity propertyTransEntity);
    }

    public static void dismissConfirmDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }


}
