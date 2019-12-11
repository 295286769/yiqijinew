package com.yiqiji.money.modules.homeModule.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MothsMoneyView;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView.PinnedHeaderAdapter;
import com.yiqiji.money.modules.common.widget.SwipeLayout;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NumberDtailAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderAdapter, OnClickListener {
    private List<DailycostEntity> beans;
    private Context mContext;
    private List<TotalBalance> list;
    private int bumber = 0;
    private int head_number = 0;
    private LayoutInflater mLayoutInflater;
    private List<SwipeLayout> swipeLayouts;
    private String myuid = "";
    private String userid = "";
    private String memberid = "";
    private String bookName;
    private String bookNameType;
    private String mAccountbookid;
    private String isClear;
    private String mMember;
    private float screaWith = 0;
    private ViewHolder viewHolder;
    private DailycostEntity itemEntity;
    private String balance;

    public NumberDtailAdapter(Context mContext, List<DailycostEntity> beans, String myuid, String userid,
                              String bookName, String memberid, String bookNameType, String mAccountbookid, String isClear) {

        this.mContext = mContext;
        screaWith = XzbUtils.getPhoneScreen((Activity) mContext).widthPixels / 3.5f;
        this.beans = beans;
        this.myuid = myuid;
        this.userid = userid;
        this.memberid = memberid;
        this.bookName = bookName;
        this.bookNameType = bookNameType;
        this.mAccountbookid = mAccountbookid;
        this.isClear = isClear;
        mLayoutInflater = LayoutInflater.from(mContext);
        swipeLayouts = new ArrayList<SwipeLayout>();
    }

    public void setMember(String mMember) {
        this.mMember = mMember;
    }

    public void setTitleBlance(List<TotalBalance> list) {
        this.list = list;
        bumber = 0;
        head_number = 0;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (beans != null) {
            return beans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != beans && position < getCount()) {
            return beans.get(position);
        }
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itemEntity = beans.get(position);
        // 常见的优化ViewHolder
        viewHolder = null;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.activity_list_accounting_item, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (MothsMoneyView) convertView.findViewById(R.id.title);
            viewHolder.bill_image = (ImageView) convertView.findViewById(R.id.bill_image);
            viewHolder.title_text = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.remark = (TextView) convertView.findViewById(R.id.remark);
            viewHolder.not_involved = (TextView) convertView.findViewById(R.id.not_involved);
            viewHolder.liquidated = (TextView) convertView.findViewById(R.id.liquidated);
            viewHolder.blance = (TextView) convertView.findViewById(R.id.blance);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.location_text = (TextView) convertView.findViewById(R.id.location_text);
            viewHolder.location_image = (ImageView) convertView.findViewById(R.id.location_image);
            viewHolder.roundedImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.roundedImageView);
            viewHolder.relayout_item = (RelativeLayout) convertView.findViewById(R.id.relayout_item);
            viewHolder.relayout = (RelativeLayout) convertView.findViewById(R.id.relayout);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setVisibility(View.GONE);
        viewHolder.not_involved.setVisibility(View.GONE);
        viewHolder.bill_image.setVisibility(View.VISIBLE);
        viewHolder.location_text.setText("");
        viewHolder.location_text.setVisibility(View.GONE);
        viewHolder.location_image.setVisibility(View.GONE);
        viewHolder.liquidated.setVisibility(View.GONE);

        Date date = new Date(Long.parseLong(itemEntity.getTradetime()) * 1000);

        String moth = DateUtil.formatTheDateToMM_dd(date, 3);
        String day = DateUtil.formatTheDateToMM_dd(date, 2);
        String year = DateUtil.formatTheDateToMM_dd(date, 5);
        String text = "";
        int drawbleId = 0;
        String iamge_url = itemEntity.getBillcateicon();
        balance = itemEntity.getBillamount();
        String cateName = itemEntity.getBillcatename();
        String cateid = itemEntity.getBillcateid();
        String locationImage = itemEntity.getBillimg();
        String locationText = itemEntity.getAddress();
        String remark = itemEntity.getBillmark();
        String username = itemEntity.getUsername();
        String billbrand = itemEntity.getBillbrand();
        String isclearText = itemEntity.getBillclear().equals("1") ? "已结算" : "";
        String data_string = DateUtil.getDateToStringhhmm(Long.parseLong(itemEntity.getTradetime()));
        String billtype = itemEntity.getBilltype();
        if (itemEntity.getMemberlist() != null && memberid != null) {
            for (int i = 0; i < itemEntity.getMemberlist().size(); i++) {
                BillMemberInfo billMemberInfo = itemEntity.getMemberlist().get(i);
                String type = billMemberInfo.getType();
                if (billMemberInfo.getMemberid().equals(memberid)) {
                    if (billtype.equals("4")) {
                        if (type.equals("0")) {
                            balance = billMemberInfo.getAmount();
                            break;
                        }
                    } else {
                        if (type.equals("1")) {
                            balance = billMemberInfo.getAmount();
                            break;
                        }
                    }


                }


            }
        }
        BillDetailPerecenter.setCateNameImage(mContext, isClear, itemEntity, new BillDetailPerecenter.CateNameImageInterface() {
            @Override
            public void getCateNameImageInterface(String cateid, String bill_type, String url, String cate_name, int title_color,
                                                  String bill_mark, String bill_balance, int cilor_balance, String bill_text, String bill_brand) {
                if (TextUtils.isEmpty(mMember)) {
                    bill_balance = balance;
                }

                if (TextUtils.isEmpty(mMember) && !TextUtils.isEmpty(bill_text)) {
                    bill_text = bill_text + StringUtils.moneySplitComma(itemEntity.getBillamount());
                }
                if (isClear.equals("0")) {
                    bill_text = itemEntity.getUsername();
                }
                viewHolder.title_text.setText(cate_name);
                viewHolder.title_text.setTextColor(title_color);
                viewHolder.remark.setText(bill_mark);
                viewHolder.text.setText(bill_text);

                viewHolder.blance.setText(StringUtils.moneySplitComma(bill_balance));
                viewHolder.blance.setTextColor(cilor_balance);
                XzbUtils.displayImage(viewHolder.bill_image, url, 0);
                if (!TextUtils.isEmpty(bill_balance)) {
                    viewHolder.blance.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.blance.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(bill_brand)) {
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(bill_brand);
                }
            }
        });

        if (itemEntity.getBilltype().equals("3") || (TextUtils.isEmpty(billbrand) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(isclearText) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(locationImage))) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.bill_image.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            viewHolder.bill_image.setLayoutParams(layoutParams);
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.title_text.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            viewHolder.title_text.setLayoutParams(layoutParams);
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.relayout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            viewHolder.relayout.setLayoutParams(layoutParams);
        } else {
//            viewHolder.blance.setTextColor(mContext.getResources().getColor(R.color.context_color));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.relayout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            viewHolder.relayout.setLayoutParams(layoutParams);
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.title_text.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            viewHolder.title_text.setLayoutParams(layoutParams);
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.bill_image.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            viewHolder.bill_image.setLayoutParams(layoutParams);
        }
        BillDetailPerecenter.setBillImageWithHeight(mContext, itemEntity, viewHolder.roundedImageView);
        BillDetailPerecenter.setlocationHeight(mContext, itemEntity, viewHolder.location_text, viewHolder.location_image);
        if (!TextUtils.isEmpty(isclearText) && !billtype.equals("3")) {
            viewHolder.liquidated.setText(isclearText);
            viewHolder.liquidated.setVisibility(View.VISIBLE);
        }
        viewHolder.relayout_item.setTag(position);
        viewHolder.relayout_item.setOnClickListener(this);

        if (needTitle(position)) {
            // 显示标题并设置内容
            // if (list != null) {

            viewHolder.title.setMothsMoney(year + "年" + moth + "月" + day + "日", "");

            // } else {
            // viewHolder.title.setMothsMoney(moth + "月" + day + "日", "");
            // }

            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);

        }


        return convertView;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        if (getCount() == 0 || position < 0) {
            return PinnedHeaderAdapter.PINNED_HEADER_GONE;
        }

        if (isMove(position) == true) {
            return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
        }

        return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
    }

    @Override
    public void configurePinnedHeader(View headerView, int position, int alpaha) {
        // 设置标题的内容
        DailycostEntity itemEntity = (DailycostEntity) getItem(position);
        @SuppressWarnings("deprecation")
        Date date = new Date(Long.parseLong(itemEntity.getTradetime()) * 1000);

        String moth = DateUtil.formatTheDateToMM_dd(date, 3);
        String day = DateUtil.formatTheDateToMM_dd(date, 2);
        String year = DateUtil.formatTheDateToMM_dd(date, 5);
        String headerValue = day;

        if (!TextUtils.isEmpty(headerValue)) {

            MothsMoneyView headerTextView = (MothsMoneyView) headerView.findViewById(R.id.header);
            headerTextView.setMothsMoney(year + "年" + moth + "月" + headerValue + "日", "");

        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
        }

    }

    /**
     * 判断是否需要显示标题
     *
     * @param position
     * @return
     */
    private boolean needTitle(int position) {
        // 第一个肯定是分类
        if (position == 0) {
            return true;
        }

        // 异常处理
        if (position < 0) {
            return false;
        }

        // 当前 // 上一个
        DailycostEntity currentEntity = (DailycostEntity) getItem(position);
        DailycostEntity previousEntity = (DailycostEntity) getItem(position - 1);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }
        long currenttradeTime = Long.parseLong(currentEntity.getTradetime()) * 1000;
        long previoustradeTime = Long.parseLong(previousEntity.getTradetime()) * 1000;
        Date currenttradedate = new Date(currenttradeTime);
        Date previoustradedate = new Date(previoustradeTime);
        String currentTitle = DateUtil.formatTheDateToMM_dd(currenttradedate, 3);
        String previousTitle = DateUtil.formatTheDateToMM_dd(previoustradedate, 3);
        String currentDay = DateUtil.formatTheDateToMM_dd(currenttradedate, 2);
        String previousDay = DateUtil.formatTheDateToMM_dd(previoustradedate, 2);

        if (null == previousTitle || null == currentTitle) {
            return false;
        }
        String currentEntityYear = DateUtil.formatTheDateToMM_dd(currenttradedate, 5);
        String previousYear = DateUtil.formatTheDateToMM_dd(previoustradedate, 5);


        if (currentTitle.equals(previousTitle) && currentDay.equals(previousDay) && currentEntityYear.equals(previousYear)) {
            return false;
        }

        return true;
    }

    private boolean isMove(int position) {
        // 获取当前与下一项
        DailycostEntity currentEntity = (DailycostEntity) getItem(position);
        DailycostEntity nextEntity = (DailycostEntity) getItem(position + 1);
        if (null == currentEntity || null == nextEntity) {
            return false;
        }

        // 获取两项header内容
        long currenttradeTime = Long.parseLong(currentEntity.getTradetime()) * 1000;
        long nexttradeTime = Long.parseLong(nextEntity.getTradetime()) * 1000;
        Date currenttradedate = new Date(currenttradeTime);
        Date nextdate = new Date(nexttradeTime);
        String currentTitle = DateUtil.formatTheDateToMM_dd(currenttradedate, 3);
        String nextTitle = DateUtil.formatTheDateToMM_dd(nextdate, 3);
        String currentDay = DateUtil.formatTheDateToMM_dd(currenttradedate, 2);
        String nextDay = DateUtil.formatTheDateToMM_dd(nextdate, 2);
        String currentEntityYear = DateUtil.formatTheDateToMM_dd(currenttradedate, 5);
        String nextYear = DateUtil.formatTheDateToMM_dd(nextdate, 5);
        if (null == currentTitle || null == nextTitle) {
            return false;
        }

        // 当前不等于下一项header，当前项需要移动了
        if (!currentTitle.equals(nextTitle) || !currentDay.equals(nextDay) || !currentEntityYear.equals(nextYear)) {
            return true;
        }
        return false;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class ViewHolder {
        MothsMoneyView title;
        ImageView bill_image;//分类图标
        ImageView location_image;//定位图标
        TextView title_text;//分类
        TextView location_text;//定位描述
        TextView remark;//备注
        TextView not_involved;//没有参与
        TextView liquidated;//已结算
        TextView blance;//金额
        TextView text;//多少人消费
        TextView name;//装修分类
        SelectableRoundedImageView roundedImageView;//大图
        RelativeLayout relayout_item;//大图
        RelativeLayout relayout;//
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Intent intent = new Intent(mContext, PaymentDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("list", beans.get(position));
        intent.setExtrasClassLoader(DailycostEntity.class.getClassLoader());
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("billid", beans.get(position).getBillid());
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

}
