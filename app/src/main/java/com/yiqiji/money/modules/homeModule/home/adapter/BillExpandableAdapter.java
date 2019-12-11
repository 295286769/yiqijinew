package com.yiqiji.money.modules.homeModule.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;

import java.util.Date;
import java.util.List;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/6/27.
 * address huang.weishui@71dai.com
 */
public class BillExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<TotalBalance> totalBalances;//大类分类
    //    private List<TotalBalance> dailycostEntities;//二级分类
    private double groupTotalbalance;

    public BillExpandableAdapter(Context context, List<TotalBalance> totalBalances) {
        this.mContext = context;
        this.totalBalances = totalBalances;
    }

    public void setGroupTotalbalance(double groupTotalbalance) {
        this.groupTotalbalance = groupTotalbalance;
    }

    @Override
    public int getGroupCount() {
        return totalBalances.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return totalBalances.get(groupPosition).getTotalBalances().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return totalBalances.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return totalBalances.get(groupPosition).getTotalBalances().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        BillGroupViewHolder billGroupViewHolder = null;
        if (convertView == null) {
            billGroupViewHolder = new BillGroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_balance_list, null);
            billGroupViewHolder.left_down = (ImageView) convertView.findViewById(R.id.left_down);
            billGroupViewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            billGroupViewHolder.content = (TextView) convertView.findViewById(R.id.content);
            billGroupViewHolder.balance = (TextView) convertView.findViewById(R.id.balance);
            billGroupViewHolder.balance_pesent = (TextView) convertView.findViewById(R.id.balance_pesent);
            convertView.setTag(billGroupViewHolder);
        } else {
            billGroupViewHolder = (BillGroupViewHolder) convertView.getTag();
        }
        TotalBalance totalBalance = totalBalances.get(groupPosition);
        String billtype = totalBalance.getBilltype();
//        TextView contentView = h.getView(R.id.content);
        double mBalance = totalBalance.getTotal_balance();
//                String mBalanceString = XzbUtils.getBalance(mBalance);
        String mBalanceString = XzbUtils.formatDouble("%.2f", mBalance);
        if (billtype.equals("1")) {// 类型（0表示收入，1表示支出,2转账，3结算，4交款）
            billGroupViewHolder.balance.setText("-" + StringUtils.moneySplitComma(mBalanceString));
            billGroupViewHolder.balance.setTextColor(mContext.getResources().getColor(R.color.expenditure));
        } else {
            billGroupViewHolder.balance.setText("+" + StringUtils.moneySplitComma(mBalanceString));
            billGroupViewHolder.balance.setTextColor(mContext.getResources().getColor(R.color.income));
        }
        String iamge_url = totalBalance.getBillcateicon();
        String cateid = totalBalance.getBillcateid();
        String cateName = totalBalance.getContent();
        String percentage = totalBalance.getPercentage();
//        if (!TextUtils.isEmpty(totalBalance.getBillsubcateicon())) {
//            cateid = totalBalance.getBillsubcateid();
//            cateName = totalBalance.getBillsubcatename();
//            iamge_url = totalBalance.getBillsubcateicon();
//        }

        iamge_url = XzbUtils.initImageUrlNeedFile(iamge_url, cateid);
        XzbUtils.displayImageHead(billGroupViewHolder.image, iamge_url, 0);
        billGroupViewHolder.content.setText(cateName);

        String pesentString = "";
        if (!TextUtils.isEmpty(percentage)) {
            pesentString = XzbUtils.formatDouble("%.1f", Double.parseDouble(percentage) * 100);
        } else {
            double pesent = 0.00;
            if (groupTotalbalance == 0) {
                pesent = 0.00;
            } else {
                pesent = XzbUtils.setTwoDecimalFormat("#.000", mBalance / groupTotalbalance) * 100;
            }

            pesentString = XzbUtils.formatDouble("%.1f", pesent);
        }
        billGroupViewHolder.balance_pesent.setText(pesentString + "%");
        ImageLoaderManager.loadImage(mContext, R.drawable.bill_riht_icon, 0, billGroupViewHolder.left_down);
        if (isExpanded) {
            ImageLoaderManager.loadImage(mContext, R.drawable.bill_down_icon, 0, billGroupViewHolder.left_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BillChildViewHolder billChildViewHolder = null;
        if (convertView == null) {
            billChildViewHolder = new BillChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.statistics_frament_bill_child_item, null);
            billChildViewHolder.child_icon = (ImageView) convertView.findViewById(R.id.child_icon);
            billChildViewHolder.child_content = (TextView) convertView.findViewById(R.id.child_content);
            billChildViewHolder.chile_date_time = (TextView) convertView.findViewById(R.id.child_time);
            billChildViewHolder.child_balance = (TextView) convertView.findViewById(R.id.child_balance);
            billChildViewHolder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(billChildViewHolder);
        } else {
            billChildViewHolder = (BillChildViewHolder) convertView.getTag();
        }
        TotalBalance totalBalance = totalBalances.get(groupPosition).getTotalBalances().get(childPosition);
        String iamge_url = totalBalance.getBillcateicon();

        String cateid = totalBalance.getBillcateid();
        String cateName = totalBalance.getContent();
        if (!TextUtils.isEmpty(totalBalance.getBillsubcatename())) {
            cateName = totalBalance.getBillsubcatename();
            cateid = totalBalance.getBillsubcateid();
            iamge_url = totalBalance.getBillsubcateicon();
        }
        int mCounts = totalBalance.getCounts();
        long timelong = totalBalance.getTridetime();
        String dateString = DateUtil.formatTheDateToMM_dd(new Date(timelong * 1000), 9);
        iamge_url = XzbUtils.initImageUrlNeedFile(iamge_url, cateid);
        String billtype = totalBalance.getBilltype();
        double mBalance = totalBalance.getTotal_balance();
        String mBalanceString = XzbUtils.formatDouble("%.2f", mBalance);
        if (billtype.equals("1")) {// 类型（0表示收入，1表示支出,2转账，3结算，4交款）
            billChildViewHolder.child_balance.setText("-" + StringUtils.moneySplitComma(mBalanceString));
            billChildViewHolder.child_balance.setTextColor(mContext.getResources().getColor(R.color.expenditure));
        } else {
            billChildViewHolder.child_balance.setText("+" + StringUtils.moneySplitComma(mBalanceString));
            billChildViewHolder.child_balance.setTextColor(mContext.getResources().getColor(R.color.income));
        }

        XzbUtils.displayImageHead(billChildViewHolder.child_icon, iamge_url, 0);
        billChildViewHolder.child_content.setText(cateName);
        billChildViewHolder.chile_date_time.setText(dateString);
        billChildViewHolder.count.setText(mCounts + "笔");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class BillGroupViewHolder {
        ImageView left_down;//箭头
        ImageView image;//分类图标
        TextView content;//分类
        TextView balance_pesent;//百分比
        TextView balance;//金额

    }

    class BillChildViewHolder {
        ImageView child_icon;//分类图标
        TextView child_content;//分类
        TextView chile_date_time;//日期
        TextView child_balance;//金额
        TextView count;//金额
    }
}
