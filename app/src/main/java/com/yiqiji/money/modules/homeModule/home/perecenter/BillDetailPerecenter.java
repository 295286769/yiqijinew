package com.yiqiji.money.modules.homeModule.home.perecenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillDetailInfo;

import java.text.DecimalFormat;
import java.util.HashMap;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/6/5.
 * address huang.weishui@71dai.com
 */
public class BillDetailPerecenter {
    public static void getBillDetail(final Context context, String billid, final ViewCallBack viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("billid", billid);
        CommonFacade.getInstance().exec(Constants.BILLDETAIL, hashMap, new ViewCallBack<BillDetailInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                ((BaseActivity) context).showLoadingDialog(false);
            }

            @Override
            public void onSuccess(BillDetailInfo o) throws Exception {
                super.onSuccess(o);
                BillDetailInfo billDetailInfo = o;
                if (billDetailInfo.getData() != null) {
                    DailycostEntity dailycostEntity = billDetailInfo.getData();
                    if (dailycostEntity == null) {
                        viewCallBack.onSuccess("");
                    } else {
                        viewCallBack.onSuccess(dailycostEntity.getBilltype());
                    }

                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((BaseActivity) context).showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) context).dismissDialog();
            }
        });

    }

    public static void setBillImageWithHeight(Context context, DailycostEntity dailycostEntity, SelectableRoundedImageView selectableRoundedImageView) {
        if (dailycostEntity != null) {
            String locationImage = dailycostEntity.getBillimg();
            selectableRoundedImageView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(locationImage)) {
                selectableRoundedImageView.setVisibility(View.VISIBLE);
                int screaWith = (int) (XzbUtils.getPhoneScreen((Activity) context).widthPixels / 3.5f);
                setViewWithHeight(context, selectableRoundedImageView, screaWith);
//                ImageLoaderManager.loadRoundCornerImage(context, locationImage, selectableRoundedImageView, 100);
                ImageLoaderManager.loadImage(context, locationImage, selectableRoundedImageView);
//                setImage(context, selectableRoundedImageView, locationImage + "?imageMogr2/auto-orient");
            }
        }
    }

    public static void setlocationHeight(Context context, DailycostEntity dailycostEntity, TextView location, ImageView location_image) {
        if (dailycostEntity != null) {
            String locationText = dailycostEntity.getAddress();
            if (!TextUtils.isEmpty(locationText) && !locationText.equals("定位位置")) {
                boolean participate = BaserClassMode.isParticipate(dailycostEntity);
                String isParticipate = participate == false ? "未参与" : "";
                String isclearText = dailycostEntity.getBillclear().equals("1") ? "已结算" : "";
                location.setText(dailycostEntity.getAddress());
                location_image.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(isParticipate) || !TextUtils.isEmpty(isclearText)) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) location.getLayoutParams();
                    layoutParams.width = UIHelper.Dp2Px(context, 70);
                    location.setLayoutParams(layoutParams);
                }
            }

        }
    }

    public static void setViewWithHeight(Context context, View view, int with) {
//        int screenWith = XzbUtils.getPhoneScreenintWith((Activity) context);
        int screenWith = with;
        ViewGroup.LayoutParams layoutParams = null;
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams = layoutParams1;

        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams layoutParams_relative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams = layoutParams_relative;
        }
        layoutParams.width = (int) screenWith;
        layoutParams.height = (int) (screenWith * 7.1f / 10.8f);
        view.setLayoutParams(layoutParams);

    }

    public static void setImage(Context context, View view, String url) {
        if (view instanceof ImageView) {
            XzbUtils.displayImage((ImageView) view, url, 0);
        } else if (view instanceof SelectableRoundedImageView) {
            XzbUtils.displayImage((SelectableRoundedImageView) view, url, 0);
        }
    }

    public static void decorationBrand(Context context, TextView textView) {

    }

    public static void setCateNameImage(Context context, String is_Clear, DailycostEntity dailycostEntity, CateNameImageInterface cateNameImageInterface) {
        if (dailycostEntity != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            ;
            String text = "";
            String isParticipate = "";
            String isclearText = "";
            String cateid = dailycostEntity.getBillcateid();
            String userid = dailycostEntity.getUserid();
            String iamge_url = dailycostEntity.getBillcateicon();
            String cateName = dailycostEntity.getBillcatename();
            String billmark = dailycostEntity.getBillmark();
            String billtype = dailycostEntity.getBilltype();
            String balance = dailycostEntity.getBillamount();
            balance = df.format(Double.valueOf(balance));
            String isclear = dailycostEntity.getIsclear();
            if (!TextUtils.isEmpty(is_Clear)) {
                isclear = is_Clear;
            }
            String billcount = dailycostEntity.getBillcount();
            String billbrand = dailycostEntity.getBillbrand();
            int drawbleId = -1;
            int cilor_balance = context.getResources().getColor(R.color.context_color);
            int title_color = context.getResources().getColor(R.color.title_color);
            if (billtype.equals("0")) {// 入账类型:0.收入,1.支出,2.转账，3.结算，4.交款5.日志
                balance = "+" + balance;
                if (dailycostEntity.getIsclear().equals("0")) {
                    text = dailycostEntity.getUsername();
                } else {
                    text = dailycostEntity.getBillcount() + "人收入";
                }
                cilor_balance = context.getResources().getColor(R.color.income);
//                viewHolder.blance.setTextColor(mContext.getResources().getColor(R.color.income));

            } else if (dailycostEntity.getBilltype().equals("1")) {
                balance = "-" + balance;
                if (isclear.equals("0")) {
                    text = dailycostEntity.getUsername();
                } else {
                    text = dailycostEntity.getBillcount() + "人消费";
                }
                cilor_balance = context.getResources().getColor(R.color.expenditure);
            } else if (billtype.equals("2")) {
                text = billcount + "人转账";

            } else if (billtype.equals("3")) {
                cateName = billmark;
                isParticipate = "";
                balance = "";
                text = "";
                isclearText = "";
                billmark = "";
                iamge_url = dailycostEntity.getUsericon();

                int index = Integer.parseInt(userid) % 10;
                if (TextUtils.isEmpty(iamge_url)) {
                    drawbleId = RequsterTag.head_image[index];
                    iamge_url = "drawable://" + drawbleId;
                }
            } else if (billtype.equals("4")) {
                text = billcount + "人交款";
                cateName = "成员交款";
                drawbleId = R.drawable.payment;
                iamge_url = "drawable://" + drawbleId;
            }
            if (!TextUtils.isEmpty(dailycostEntity.getBillsubcateicon())) {
                cateid = dailycostEntity.getBillsubcateid() + "child";
                iamge_url = dailycostEntity.getBillsubcateicon();
                cateName = dailycostEntity.getBillsubcatename();
            }
            if (!dailycostEntity.getBilltype().equals("3") && !dailycostEntity.getBilltype().equals("4")) {
                iamge_url = XzbUtils.initImageUrlNeedFile(iamge_url, cateid);

            }
            if (billtype.equals("5")) {
                iamge_url = dailycostEntity.getUsericon();
                cateName = dailycostEntity.getBillmark();
                balance = "";
                billmark = "";
                text = "";
                title_color = context.getResources().getColor(R.color.secondary_text);
            }
            cateNameImageInterface.getCateNameImageInterface(cateid, billtype, iamge_url, cateName, title_color, billmark, balance, cilor_balance, text, billbrand);
        }

    }

    public static void setCateNameImage(DailycostEntity dailycostEntity, CateNameImageInterface cateNameImageInterface) {
        if (dailycostEntity != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            ;
            String text = "";
            String cateid = dailycostEntity.getBillcateid();
            String userid = dailycostEntity.getUserid();
            String iamge_url = dailycostEntity.getBillcateicon();
            String cateName = dailycostEntity.getBillcatename();
            String billmark = dailycostEntity.getBillmark();
            String billtype = dailycostEntity.getBilltype();
            String balance = dailycostEntity.getBillamount();
            balance = df.format(Double.valueOf(balance));
            String billcount = dailycostEntity.getBillcount();
            String billbrand = dailycostEntity.getBillbrand();
            int drawbleId = -1;
            int cilor_balance = R.color.context_color;
            int title_color = R.color.title_color;
            if (billtype.equals("0")) {// 入账类型:0.收入,1.支出,2.转账，3.结算，4.交款5.日志
                balance = "+" + balance;
                if (dailycostEntity.getIsclear().equals("0")) {
                    text = dailycostEntity.getUsername();
                } else {
                    text = dailycostEntity.getBillcount() + "人收入";
                }
                cilor_balance = R.color.income;

            } else if (dailycostEntity.getBilltype().equals("1")) {
                balance = "-" + balance;
                text = dailycostEntity.getBillcount() + "人消费";

                cilor_balance = R.color.expenditure;
            } else if (billtype.equals("2")) {
                text = billcount + "人转账";

            } else if (billtype.equals("3")) {
                cateName = billmark;
                balance = "";
                text = "";
                billmark = "";
                iamge_url = dailycostEntity.getUsericon();

                int index = Integer.parseInt(userid) % 10;
                if (TextUtils.isEmpty(iamge_url)) {
                    drawbleId = RequsterTag.head_image[index];
                    iamge_url = "drawable://" + drawbleId;
                }
            } else if (billtype.equals("4")) {
                text = billcount + "人交款";
                cateName = "成员交款";
                drawbleId = R.drawable.payment;
                iamge_url = "drawable://" + drawbleId;
            }
            if (!TextUtils.isEmpty(dailycostEntity.getBillsubcatename())) {
                cateid = dailycostEntity.getBillsubcateid() + "child";
                iamge_url = dailycostEntity.getBillsubcateicon();
                cateName = dailycostEntity.getBillsubcatename();
            }
            if (!dailycostEntity.getBilltype().equals("3") && !dailycostEntity.getBilltype().equals("4")) {
                iamge_url = XzbUtils.initImageUrlNeedFile(iamge_url, cateid);

            }
            if (billtype.equals("5")) {
                iamge_url = dailycostEntity.getUsericon();
                cateName = dailycostEntity.getBillmark();
                balance = "";
                billmark = "";
                text = "";
                title_color = R.color.secondary_text;
            }
            cateNameImageInterface.getCateNameImageInterface(cateid, billtype, iamge_url, cateName, title_color, billmark, balance, cilor_balance, text, billbrand);
        }

    }

    public static void setBlancelayoutlocation(TextView blancelayoutlocation, String accountbookcount) {
        if (!BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) blancelayoutlocation.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
//            viewHolder.blance.setGravity(Gravity.CENTER);
            blancelayoutlocation.setLayoutParams(layoutParams);

        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) blancelayoutlocation.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
            blancelayoutlocation.setGravity(Gravity.TOP | Gravity.RIGHT);
            blancelayoutlocation.setLayoutParams(layoutParams);

        }
    }

    public interface CateNameImageInterface {
        void getCateNameImageInterface(String cateid, String bill_type, String url, String cate_name, int title_color, String bill_mark, String bill_balance, int cilor_balance, String bill_text, String bill_brand);
    }
}
