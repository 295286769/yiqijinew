package com.yiqiji.money.modules.homeModule.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MothsMoneyView;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView.PinnedHeaderAdapter;
import com.yiqiji.money.modules.common.widget.SwipeLayout;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettledDetailAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderAdapter {
    private List<DailycostEntity> beans;
    private Context mContext;
    private List<TotalBalance> list;
    private int bumber = 0;
    private int head_number = 0;
    private LayoutInflater mLayoutInflater;
    private List<SwipeLayout> swipeLayouts;
    private String bookName = "";
    //    private String mAccountbooktype = "";
    private long time;
    private OnClickListener clickListener;
    private OnLongClickListener onLongClickListener;
    private float screaWith;
    private ViewHolder viewHolder;
    private DailycostEntity dailycostEntity;

    public SettledDetailAdapter(Context mContext, List<DailycostEntity> beans, String bookName,
                                OnClickListener clickListener, OnLongClickListener onLongClickListener) {
        this.mContext = mContext;
        this.beans = beans;
        this.bookName = bookName;
//        this.mAccountbooktype = mAccountbooktype;
        this.clickListener = clickListener;
        this.onLongClickListener = onLongClickListener;
        mLayoutInflater = LayoutInflater.from(mContext);
        swipeLayouts = new ArrayList<SwipeLayout>();
        screaWith = XzbUtils.getPhoneScreen((Activity) mContext).widthPixels / 3.5f;
    }

    public void setTitleBlance(List<TotalBalance> list) {
        this.list = list;
        bumber = 0;
        head_number = 0;
        notifyDataSetChanged();
    }

    public void set(long time) {
        this.time = time;

        // notifyDataSetChanged();
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
        // 常见的优化ViewHolder
        dailycostEntity = beans.get(position);
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.journal_text = (TextView) convertView.findViewById(R.id.journal_text);
            viewHolder.relayout = (RelativeLayout) convertView.findViewById(R.id.relayout);
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
        viewHolder.journal_text.setVisibility(View.GONE);
        int drawbleId = 0;
        String text = "";
        String balance = "0.00";
        String iamge_url = dailycostEntity.getBillcateicon();
        String cateid = dailycostEntity.getBillcateid();
        String cateName = dailycostEntity.getBillcatename();
        String remark = dailycostEntity.getBillmark();
        String locationImage = dailycostEntity.getBillimg();
        String locationText = dailycostEntity.getAddress();
        String billtype = dailycostEntity.getBilltype();
        String data_string = DateUtil.getDateToStringhhmm(Long.parseLong(dailycostEntity.getTradetime()));
        boolean participate = BaserClassMode.isParticipate(dailycostEntity);
        String isclearText = dailycostEntity.getBillclear().equals("1") ? "已结算" : "";
        String isParticipate = participate == false ? "未参与" : "";
        BillDetailPerecenter.setCateNameImage(mContext, dailycostEntity.getIsclear(), dailycostEntity, new BillDetailPerecenter.CateNameImageInterface() {
            @Override
            public void getCateNameImageInterface(String cateid, String bill_type, String url, String cate_name, int title_color,
                                                  String bill_mark, String bill_balance, int cilor_balance, String bill_text, String bill_brand) {
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
                if (dailycostEntity.getBilltype().equals("5")) {
                    viewHolder.blance.setVisibility(View.GONE);
                    viewHolder.bill_image.setVisibility(View.INVISIBLE);
                    viewHolder.journal_text.setVisibility(View.VISIBLE);
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(dailycostEntity.getUsername());
                    if (!TextUtils.isEmpty(dailycostEntity.getBillcatename())) {
                        viewHolder.liquidated.setVisibility(View.VISIBLE);
                        viewHolder.liquidated.setText(dailycostEntity.getBillcatename());
                    }
                    setlayout(viewHolder.relayout, viewHolder.title_text);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.relayout.getLayoutParams();
                    layoutParams.height = UIHelper.Dp2Px(mContext, 45);
                    viewHolder.relayout.setLayoutParams(layoutParams);


                }

                if (!TextUtils.isEmpty(bill_brand)) {
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(bill_brand);
                }
            }
        });
        BillDetailPerecenter.setBillImageWithHeight(mContext, dailycostEntity, viewHolder.roundedImageView);
        BillDetailPerecenter.setlocationHeight(mContext, dailycostEntity, viewHolder.location_text, viewHolder.location_image);

        if (!TextUtils.isEmpty(isclearText) && !billtype.equals(RequsterTag.DIARYACCOUNTBOOKCATE)) {
            viewHolder.liquidated.setText(isclearText);
            viewHolder.liquidated.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(isParticipate) && !billtype.equals(RequsterTag.DIARYACCOUNTBOOKCATE)) {
            viewHolder.not_involved.setText(isParticipate);
            viewHolder.not_involved.setVisibility(View.VISIBLE);
        }
        if (needTitle(position)) {
            // 显示标题并设置内容
            Date date = new Date(Long.parseLong(dailycostEntity.getTradetime()) * 1000);
            String day = DateUtil.formatTheDateToMM_dd(date, 2);
            String moth = DateUtil.formatTheDateToMM_dd(date, 3);
            String year = DateUtil.formatTheDateToMM_dd(date, 5);

            viewHolder.title.setMothsMoney(year + "年" + moth + "月" + day + "日", "");

            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);

        }
        // if (itemEntity.getIsNumberBook().equals("0")) {// 单人账本
        viewHolder.relayout_item.setTag(position);
        viewHolder.relayout_item.setOnClickListener(clickListener);
        viewHolder.relayout_item.setOnLongClickListener(onLongClickListener);

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

    @SuppressWarnings("deprecation")
    @Override
    public void configurePinnedHeader(View headerView, int position, int alpaha) {
        // 设置标题的内容
        DailycostEntity itemEntity = (DailycostEntity) getItem(position);
        Date date = new Date(Long.parseLong(itemEntity.getTradetime()));
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
        String currentTitle = String.valueOf(currentEntity.getMoth());
        String nextTitle = String.valueOf(nextEntity.getMoth());
        if (null == currentTitle || null == nextTitle) {
            return false;
        }
        String currentDay = String.valueOf(currentEntity.getDay());
        String nextDay = String.valueOf(nextEntity.getDay());
        String currentEntityYear = String.valueOf(currentEntity.getYear());
        String nextYear = String.valueOf(nextEntity.getYear());


        // 当前不等于下一项header，当前项需要移动了
        if (!currentTitle.equals(nextTitle) || !currentDay.equals(nextDay) || !currentEntityYear.equals(nextYear)) {
            return true;
        }
        return false;
    }

    public void setlayout(final RelativeLayout relativeLayout, final TextView textview) {
        textview.post(new Runnable() {
            @Override
            public void run() {
                Layout layout = textview.getLayout();
                if (layout != null) {
                    int lineCount = layout.getLineCount();
                    if (lineCount > 1) {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                        layoutParams.height = UIHelper.Dp2Px(mContext, 55);
                        relativeLayout.setLayoutParams(layoutParams);
                    } else {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                        layoutParams.height = UIHelper.Dp2Px(mContext, 45);
                        relativeLayout.setLayoutParams(layoutParams);
                    }
                }

            }
        });

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
        TextView name;//装修品牌
        TextView journal_text;//日志
        RelativeLayout relayout;
        SelectableRoundedImageView roundedImageView;//大图
        RelativeLayout relayout_item;//大图
    }

}
