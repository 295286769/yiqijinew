package com.yiqiji.money.modules.homeModule.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MothsMoneyView;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.common.widget.SwipeLayout;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderListView.PinnedHeaderAdapter, SwipeLayout.OnClickItem {

    private static final String TAG = CustomAdapter.class.getSimpleName();

    private Context mContext;
    private List<DailycostEntity> mData;
    private LayoutInflater mLayoutInflater;
    private OnClickListener clickListener;
    private List<SwipeLayout> swipeLayouts;
    private List<TotalBalance> list;
    private int bumber = 0;
    private int head_number = 0;
    private List<DailycostEntity> dailycostEntities;
    private int position = 0;
    private float screaWith = 0;
    private String sorttype = "";
    private String accountbookcount = "";// 是否是多人账本 0:单人
    private boolean isSubscribe = false;
    private ViewHolder viewHolder = null;

    public void setAccountbookcount(String accountbookcount) {
        this.accountbookcount = accountbookcount;
    }


    public void setSorttype(String sorttype) {
        this.sorttype = sorttype;
    }


    DecimalFormat df;

    public CustomAdapter(Context context, List<DailycostEntity> pData, OnClickListener clickListener) {
        screaWith = XzbUtils.getPhoneScreen((Activity) context).widthPixels / 3.5f;
        mData = pData;
        this.clickListener = clickListener;

        swipeLayouts = new ArrayList<SwipeLayout>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        df = new DecimalFormat("0.00");
        position = 0;
    }

    public CustomAdapter(Context context, List<DailycostEntity> pData, OnClickListener clickListener, boolean isSubscribe) {
        screaWith = XzbUtils.getPhoneScreen((Activity) context).widthPixels / 3.5f;
        mData = pData;
        this.clickListener = clickListener;

        swipeLayouts = new ArrayList<SwipeLayout>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        df = new DecimalFormat("0.00");
        position = 0;
        this.isSubscribe = isSubscribe;
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
        position = 0;
        bumber = 0;
        head_number = 0;
        if (swipeLayouts != null && swipeLayouts.size() > 0) {
            swipeLayouts.clear();
        }
    }

    public void setTitleBlance(List<TotalBalance> list) {
        this.list = list;
        bumber = 0;
        head_number = 0;
        notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            viewHolder.journal_text = (TextView) convertView.findViewById(R.id.journal_text);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.location_text = (TextView) convertView.findViewById(R.id.location_text);
            viewHolder.look_all = (TextView) convertView.findViewById(R.id.look_all);
            viewHolder.location_image = (ImageView) convertView.findViewById(R.id.location_image);
            viewHolder.roundedImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.roundedImageView);
            viewHolder.relayout_item = (RelativeLayout) convertView.findViewById(R.id.relayout_item);
            viewHolder.progressBar2 = (ProgressBar) convertView.findViewById(R.id.progressBar2);
            viewHolder.relayout = (RelativeLayout) convertView.findViewById(R.id.relayout);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
            viewHolder.text.setVisibility(View.GONE);
        } else {
            viewHolder.text.setVisibility(View.VISIBLE);
        }

//        viewHolder.name.setText("");
        viewHolder.name.setVisibility(View.GONE);
        viewHolder.not_involved.setVisibility(View.GONE);
        viewHolder.journal_text.setVisibility(View.GONE);
        viewHolder.bill_image.setVisibility(View.VISIBLE);
        viewHolder.location_text.setText("");
        viewHolder.location_text.setVisibility(View.GONE);
        viewHolder.location_image.setVisibility(View.GONE);
        viewHolder.liquidated.setVisibility(View.GONE);
        // 获取数据
        final DailycostEntity itemEntity = (DailycostEntity) getItem(position);
        String data_string = DateUtil.getDateToStringhhmm(Long.parseLong(itemEntity.getTradetime()));
        int drawbleId = 0;

        String text = "";
        String iamge_url = itemEntity.getBillcateicon();
        String billbrand = itemEntity.getBillbrand();

        String balance = itemEntity.getBillamount();
        balance = df.format(Double.valueOf(balance));
        String cateid = itemEntity.getBillcateid();
        String cateName = itemEntity.getBillcatename();
        String remark = itemEntity.getBillmark();
        String locationImage = itemEntity.getBillimg();
        String locationText = itemEntity.getAddress();
        String isclearText = itemEntity.getBillclear().equals("1") ? "已结算" : "";

        boolean participate = BaserClassMode.isParticipate(itemEntity);
        String isParticipate = participate == false ? "未参与" : "";
        if (itemEntity.getBilltype().equals("3")) {
            isParticipate = "";
            isclearText = "";
        }
        if (itemEntity.getMemberlist() == null || !BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
            isParticipate = "";
        }
        BillDetailPerecenter.setCateNameImage(mContext, itemEntity.getIsclear(), itemEntity, new BillDetailPerecenter.CateNameImageInterface() {
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
                if (itemEntity.getBilltype().equals("5")) {
                    viewHolder.blance.setVisibility(View.GONE);
                    viewHolder.bill_image.setVisibility(View.INVISIBLE);
                    viewHolder.journal_text.setVisibility(View.VISIBLE);
                    viewHolder.name.setVisibility(View.VISIBLE);
                    viewHolder.name.setText(itemEntity.getUsername());
                    if (!TextUtils.isEmpty(itemEntity.getBillcatename())) {
                        viewHolder.liquidated.setVisibility(View.VISIBLE);
                        viewHolder.liquidated.setText(itemEntity.getBillcatename());
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


        BillDetailPerecenter.setBillImageWithHeight(mContext, itemEntity, viewHolder.roundedImageView);
        BillDetailPerecenter.setlocationHeight(mContext, itemEntity, viewHolder.location_text, viewHolder.location_image);
        if (!TextUtils.isEmpty(isclearText)) {
            viewHolder.liquidated.setText(isclearText);
            if (isSubscribe) {
                viewHolder.liquidated.setVisibility(View.GONE);
            } else {
                viewHolder.liquidated.setVisibility(View.VISIBLE);
            }
        }
        if (!TextUtils.isEmpty(isParticipate) && !isSubscribe) {
            viewHolder.not_involved.setText(isParticipate);
            viewHolder.not_involved.setVisibility(View.VISIBLE);

        }
//        if (itemEntity.getBilltype().equals("5")) {
//            viewHolder.liquidated.setVisibility(View.GONE);
//            viewHolder.liquidated.setVisibility(View.GONE);
//
//        }

        if (itemEntity.getBilltype().equals("3") ||
                (TextUtils.isEmpty(billbrand) && TextUtils.isEmpty(locationText) && isSubscribe && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(locationImage) && !itemEntity.getBilltype().equals("5")) ||
                (TextUtils.isEmpty(billbrand) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(isParticipate) && TextUtils.isEmpty(isclearText) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(locationImage) && !itemEntity.getBilltype().equals("5"))) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.bill_image.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            viewHolder.bill_image.setLayoutParams(layoutParams);
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.journal_text.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            viewHolder.journal_text.setLayoutParams(layoutParams);
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
            layoutParams = (RelativeLayout.LayoutParams) viewHolder.journal_text.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            viewHolder.journal_text.setLayoutParams(layoutParams);
        }
        BillDetailPerecenter.setBlancelayoutlocation(viewHolder.blance,accountbookcount);
//        if (!BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.blance.getLayoutParams();
//            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
////            viewHolder.blance.setGravity(Gravity.CENTER);
//            viewHolder.blance.setLayoutParams(layoutParams);
//
//        } else {
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.blance.getLayoutParams();
//            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
//            viewHolder.blance.setGravity(Gravity.TOP | Gravity.RIGHT);
//            viewHolder.blance.setLayoutParams(layoutParams);
//
//        }


//        viewHolder.item.setContent(null, cateName, remark, data_string, isParticipate, isclearText,
//                balance, text, itemEntity.getBilltype(), false);

        if (needTitle(position)) {
            // 显示标题并设置内容
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (itemEntity.getDay() == list.get(i).getDay()) {
                        viewHolder.title.setMothsMoney(itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", ""
                                + list.get(i).getTotal_balance());
                    }
                }
            }

            long currenttradeTime = Long.parseLong(itemEntity.getTradetime());
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
            if (currenttradeTime > start_time) {
                viewHolder.title.setMothsMoney(itemEntity.getYear() + "年" + itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
            } else {
                viewHolder.title.setMothsMoney(itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
            }
            if (!TextUtils.isEmpty(sorttype)) {
                if (sorttype.equals("0")) {
                    viewHolder.title.setMothsMoney(itemEntity.getYear() + "年" + itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
                }
            }

            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);

        }
        viewHolder.relayout_item.setTag(position);
        viewHolder.relayout_item.setOnClickListener(clickListener);
        viewHolder.roundedImageView.setTag(R.id.roundedImageView, position);
        viewHolder.roundedImageView.setOnClickListener(clickListener);
        viewHolder.look_all.setTag(position);
        viewHolder.look_all.setOnClickListener(clickListener);

        return convertView;
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

    @Override
    public int getCount() {
        if (null != mData) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mData && position < getCount()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public int getPinnedHeaderState(int position) {
        if (getCount() == 0 || position < 0) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_GONE;
        }

        if (isMove(position) == true) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
        }

        return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
    }

    @Override
    public void configurePinnedHeader(View headerView, int position, int alpaha) {
        // 设置标题的内容
        DailycostEntity itemEntity = (DailycostEntity) getItem(position);
        String headerValue = String.valueOf(itemEntity.getDay());

        if (!TextUtils.isEmpty(headerValue)) {

            MothsMoneyView headerTextView = (MothsMoneyView) headerView.findViewById(R.id.header);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (itemEntity.getDay() == list.get(i).getDay()) {
                        headerTextView.setMothsMoney(itemEntity.getMoth() + "月" + headerValue + "日", ""
                                + list.get(i).getTotal_balance());
                    }
                }

            } else {
//                headerTextView.setMothsMoney(itemEntity.getMoth() + "月" + headerValue + "日", "0.00");
            }
            long currenttradeTime = Long.parseLong(itemEntity.getTradetime());
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
            if (currenttradeTime > start_time) {
                headerTextView.setMothsMoney(itemEntity.getYear() + "年" + itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
            } else {
                headerTextView.setMothsMoney(itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
            }
            if (!TextUtils.isEmpty(sorttype)) {
                if (sorttype.equals("0")) {
                    headerTextView.setMothsMoney(itemEntity.getYear() + "年" + itemEntity.getMoth() + "月" + itemEntity.getDay() + "日", "");
                }
            }
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================

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
        long currenttradeTime = Long.parseLong(currentEntity.getTradetime());
        long previoustradeTime = Long.parseLong(previousEntity.getTradetime());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间

        String currentTitle = String.valueOf(currentEntity.getMoth());
        String previousTitle = String.valueOf(previousEntity.getMoth());
        String currentDay = String.valueOf(currentEntity.getDay());
        String previousDay = String.valueOf(previousEntity.getDay());

        if (null == previousTitle || null == currentTitle) {
            return false;
        }
        String currentEntityYear = String.valueOf(currentEntity.getYear());
        String previousYear = String.valueOf(previousEntity.getYear());
        if (!TextUtils.isEmpty(sorttype)) {
            if (sorttype.equals("0")) {
                if (!currentTitle.equals(previousTitle) || !currentEntityYear.equals(previousYear) || !currentDay.equals(previousDay)) {
                    return true;
                }
            }
        }
        if (currenttradeTime > start_time) {

            if (currentTitle.equals(previousTitle) && currentEntityYear.equals(previousYear) && currentDay.equals(previousDay)) {
                return false;
            }
        } else {
            // 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
            if (currentTitle.equals(previousTitle) && currentDay.equals(previousDay)) {
                return false;
            }


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
        String currentDay = String.valueOf(currentEntity.getDay());
        String nextEntityDay = String.valueOf(nextEntity.getDay());
        if (null == currentTitle || null == nextTitle) {
            return false;
        }

        String currentEntityYear = String.valueOf(currentEntity.getYear());
        String nextYear = String.valueOf(nextEntity.getYear());
        if (!TextUtils.isEmpty(sorttype)) {
            if (sorttype.equals("0")) {
                if (!currentTitle.equals(nextTitle) || !currentEntityYear.equals(nextYear) || !currentDay.equals(nextEntityDay)) {
                    return true;
                }
            }
        }
        long currenttradeTime = Long.parseLong(currentEntity.getTradetime());
        long nexttradeTime = Long.parseLong(nextEntity.getTradetime());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
        if (currenttradeTime > start_time) {

            if (!currentTitle.equals(nextTitle) || !currentEntityYear.equals(nextYear) || !currentDay.equals(nextEntityDay)) {
                return true;
            }
        } else {
            // 当前不等于下一项header，当前项需要移动了
            if (!currentTitle.equals(nextTitle) || !currentDay.equals(nextEntityDay)) {
                return true;
            }

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
        TextView look_all;//
        TextView remark;//备注
        TextView not_involved;//没有参与
        TextView liquidated;//已结算
        TextView blance;//金额
        TextView journal_text;//日志
        TextView name;//昵称
        TextView text;//多少人消费
        SelectableRoundedImageView roundedImageView;//大图
        RelativeLayout relayout_item;//大图
        RelativeLayout relayout;//
        ProgressBar progressBar2;

    }

    private int position_item = -1;

    @Override
    public void onClick(View view, int position, float draggedX, float xvel, int yvel, int positionIndex) {
        position_item = position;
        if (draggedX == 0 && xvel == 0 && yvel == 0) {// 点击事件

        }
        if (swipeLayouts != null) {
            for (int i = 0; i < swipeLayouts.size(); i++) {
                SwipeLayout swipeLayout = swipeLayouts.get(i);
                int item = swipeLayout.getItem_positions();
                if (item != -1 && item != position) {
                    swipeLayout.setSmooth(false);
                    swipeLayout.setItem_positions(-1);
                }

            }
        }

    }

}
