package com.yiqiji.money.modules.homeModule.home.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.fragment.LazyFragment;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalDetailActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.CustomAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class SignOneBookFragment extends LazyFragment implements OnClickListener {
    private boolean isPress = false;
    private TextView textView;
    private Context context;
    private PinnedHeaderListView listview;
    private LinearLayout notadate;
    private ImageView notedata_image;
    private RelativeLayout rl_nodata_book;
    private TextView tv_month;
    private View view;
    private CustomAdapter customAdapter;
    private Date date_time;
    private String bookName = "日常账本";
    private int count = 0;
    private String mAccountbookcount = "0";// 0:单人账本
    private String mIsclear = "0";// 0:不需要结算 1：需要结算
    private ApiService apiService;
    private String mAccountbookid = "";
    private List<DailycostEntity> billListInfoItems = new ArrayList<DailycostEntity>();
    private String sorttype = "1";// 有月份
    private int screeWith = 0;

    //    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        screeWith = XzbUtils.getPhoneScreen((Activity) context).widthPixels;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        initData(date_time);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            apiService = RetrofitInstance.get().create(ApiService.class);
            view = LayoutInflater.from(context).inflate(R.layout.singe_book_fragment, null);
            initView();

        }
        isPress = true;
        lazyLoad();
        return view;
    }


    private List<DailycostEntity> list = new ArrayList<>();
    private List<TotalBalance> totalBalances = new ArrayList<TotalBalance>();


    private void updateList(List<DailycostEntity> dailycostEntities) {

        if (notadate == null) {
            return;
        }
        if (list != null) {
            list.clear();
        }

        for (int i = 0; i < dailycostEntities.size(); i++) {
            list.add(dailycostEntities.get(i));
        }
        if (list.size() == 0) {
            Date date = new Date();
            String current_day = DateUtil.formatTheDateToMM_dd(date, 3);
            String current_year = DateUtil.formatTheDateToMM_dd(date, 5);
            String day = DateUtil.formatTheDateToMM_dd(date_time, 3);
            String year = DateUtil.formatTheDateToMM_dd(date_time, 5);
            if (sorttype.equals("0")) {
                notadate.setVisibility(View.GONE);
                rl_nodata_book.setVisibility(View.VISIBLE);
                tv_month.setText("");
            } else if (current_day.equals(day) && current_year.equals(year)) {
                notadate.setVisibility(View.GONE);
                rl_nodata_book.setVisibility(View.VISIBLE);
                tv_month.setText(StringUtils.getCurMonth(current_day));
            } else {
                notadate.setVisibility(View.VISIBLE);
                rl_nodata_book.setVisibility(View.GONE);
            }

        } else {
            rl_nodata_book.setVisibility(View.GONE);
            notadate.setVisibility(View.GONE);
        }
        if (customAdapter != null) {
            customAdapter.notifyDataSetChanged();
        }
        listview.post(new Runnable() {
            @Override
            public void run() {
                measureListViewHeight(listview);
            }
        });

    }

    private void initView() {
        listview = (PinnedHeaderListView) view.findViewById(R.id.listview);
        notadate = (LinearLayout) view.findViewById(R.id.notadate);
        notedata_image = (ImageView) view.findViewById(R.id.notedata_image);
        rl_nodata_book = (RelativeLayout) view.findViewById(R.id.rl_nodata_book);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        textView = (TextView) view.findViewById(R.id.text);
        View HeaderView = getActivity().getLayoutInflater().inflate(R.layout.listview_item_header, listview, false);
        listview.setPinnedHeader(HeaderView);
        customAdapter = new CustomAdapter(context, list, this);
        listview.setAdapter(customAdapter);
        listview.setFocusable(false);
        listview.setOnScrollListener(customAdapter);
        // initData(date_time);
    }

    DailycostEntity mdDailycostEntity = null;

    @Override
    public void onClick(View v) {
        int positio = 0;

        switch (v.getId()) {

            case R.id.relayout_item://账单详情
                positio = Integer.parseInt(v.getTag().toString());
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_ACOUNTER_DETAIL);
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_COST_DETAIL);
                billDetail(positio);
                break;
            case R.id.roundedImageView:

                XzbUtils.hidePointInUmg(context, Constants.HIDE_PHOTO_SEE);
                positio = Integer.parseInt(v.getTag(R.id.roundedImageView).toString());
                String urlPath = billListInfoItems.get(positio).getBillimg();
                Intent intent = new Intent(context, PhotosShowActivity.class);
                intent.putExtra("path", urlPath);
                intent.putExtra("isNeedDelet", true);
                startActivity(intent);
                break;
            case R.id.look_all:
                positio = Integer.parseInt(v.getTag().toString());
                String billid = billListInfoItems.get(positio).getBillid();
                positio = Integer.parseInt(v.getTag().toString());
                IntentUtils.startActivity(context, WriteJournalDetailActivity.class, "mAccountbookid", mAccountbookid, "billid", billid);
                break;

            default:
                break;
        }

    }

    @Override
    protected void lazyLoad() {
        if (!isPress || view == null || !isVisible) {
            return;
        }


    }

    // 动态改变listView的高度
    public void measureListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int listViewWidth = screeWith;//listView在布局时的宽度
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(widthSpec, 0);

            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
        }
        // 减掉底部分割线的高度
        int historyHeight = totalHeight
                + (listView.getDividerHeight() * listAdapter.getCount() - 1);
        if (pagerHeight != null) {
            pagerHeight.setPagerHeight(historyHeight);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度
        // if (totalHeight + (listView.getDividerHeight() *
        // (listAdapter.getCount() - 1)) == 0) {
        // return;
        // }

        // listView.setLayoutParams(params);
        if (pagerHeight != null) {
            pagerHeight.setPagerHeight(totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        }

    }

    public interface PagerHeight {
        void setPagerHeight(int height);
    }

    private PagerHeight pagerHeight;

    public void setPagerHeight(PagerHeight pagerHeight) {
        this.pagerHeight = pagerHeight;
    }

    private String myuid;
    private String bookNameType;
    private String memberid = "";

    public void setBillList(String mAccountbookid, String memberid, String myuid, String mAccountbookcount,
                            String bookNameType, String bookName, Date record_date, String sorttype, String isClear) {
        this.mAccountbookid = mAccountbookid;
        this.date_time = record_date;
        this.bookName = bookName;
        this.myuid = myuid;
        this.mAccountbookcount = mAccountbookcount;
        this.bookNameType = bookNameType;
        this.memberid = memberid;
        this.sorttype = sorttype;
        this.mIsclear = isClear;
//        initData(date_time);
    }

    public void setbillListInfoItems(List<DailycostEntity> billListInfoItems) {
        this.billListInfoItems = billListInfoItems;

        if (customAdapter != null) {
            customAdapter.setSorttype(sorttype);
            customAdapter.setAccountbookcount(mAccountbookcount);
            updateList(billListInfoItems);
        }
    }

//    public void initBoosIntenet(Date date_time2, final int pagerIndex) {
//        // this.date_time = date_time2;
//        // initData(date_time);
//        this.date_time = date_time2;
//        this.pagerIndex = pagerIndex;
//        long start_time = DateUtil.stringToTime(date_time2.getTime()) / 1000;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date_time2);
//        calendar.add(Calendar.MONTH, 1);
//
//        long end_time = (DateUtil.stringToTime(calendar.getTimeInMillis()) - 60 * 1000) / 1000;// 结束时间
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), mAccountbookid, "",
//                start_time + "", end_time + "", pagerIndex + ""};
//        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine", "id", "memberid",
//                "stime", "etime", "page"};
//        hashMap.put("id", mAccountbookid);
//        hashMap.put("memberid", "");
//        if (sorttype.equals("1")) {
//            hashMap.put("stime", start_time + "");
//            hashMap.put("etime", end_time + "");
//            hashMap.put("page", pagerIndex + "");
//        } else {
//            hashMap.put("stime", "");
//            hashMap.put("etime", "");
//            hashMap.put("page", "");
//        }
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        if (apiService == null) {
//            return;
//        }
//        apiService.getBookBill(hashMap).enqueue(new BaseCallBack<BillListInfo>(context, false) {
//            @Override
//            public void onResponse(Response<BillListInfo> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                if (pullToRefreshLayout != null) {
//                    pullToRefreshLayout.finishLoadAndFresh();
//                }
//                BillListInfo billListInfo = arg0.body();
//                if (billListInfo == null) {
//                    return;
//                }
//                // List<DailycostEntity> dailycostEntities = new
//                // ArrayList<DailycostEntity>();
//                if (billListInfo.getCode() == 0) {
//                    if (isRefresh && billListInfoItems.size() > 0) {
//                        billListInfoItems.clear();
//                    }
//                    if (pullToRefreshLayout != null && billListInfo.getData() != null
//                            && billListInfo.getData().getList() != null) {
//                        if (billListInfo.getData().getList().size() < 10) {
//                            pullToRefreshLayout.setHasMore(false);
//                        } else {
//                            pullToRefreshLayout.setHasMore(true);
//                        }
//                    }
//                    List<DailycostEntity> list = new ArrayList<DailycostEntity>();
//
//                    for (int i = 0; i < billListInfo.getData().getList().size(); i++) {
//                        String where = null;
//                        String[] selectionArgs = null;
//                        DailycostEntity dailycostEntity = billListInfo.getData().getList().get(i);
//                        where = DailycostContract.DtInfoColumns.BILLID + "=?";
//                        selectionArgs = new String[]{dailycostEntity.getBillid()};
//
//                        dailycostEntity.setPkid(StringUtils.getUUID());
//                        dailycostEntity.setWhichbook(bookName);
//                        dailycostEntity.setAccountbooktype(mAccountbooktype);
//                        dailycostEntity.setIsclear(mIsclear);
//                        dailycostEntity.setIssynchronization("true");
//                        long time = Long.parseLong(dailycostEntity.getBillctime());
//                        date_time = new Date(time * 1000);
//                        dailycostEntity.setYear(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 5)));
//                        dailycostEntity.setMoth(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 3)));
//                        dailycostEntity.setDay(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 2)));
//                        List<BillMemberInfo> billMemberInfos = new ArrayList<BillMemberInfo>();
//                        if (dailycostEntity.getMemberlist() != null && dailycostEntity.getMemberlist().size() > 0) {
//                            for (int j = 0; j < dailycostEntity.getMemberlist().size(); j++) {
//                                BillMemberInfo billMemberInfo = dailycostEntity.getMemberlist().get(j);
//                                billMemberInfo.setBillid(dailycostEntity.getBillid());
//                                billMemberInfos.add(billMemberInfo);
//                            }
//                            dailycostEntity.setMemberlist(billMemberInfos);
//                        }
//                        billListInfoItems.add(dailycostEntity);
//                        list.add(dailycostEntity);
//
//                    }
//                    Message msg = new Message();
//                    msg.what = 6;
//                    msg.obj = list;
//                    handler.sendMessage(msg);
//                    synchronization(billListInfoItems);
//                    // updateList(billListInfoItems);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//                super.onFailure(arg0);
//                if (pullToRefreshLayout != null) {
//                    pullToRefreshLayout.finishLoadAndFresh();
//                }
//            }
//        });
//    }

//    private void synchronization(List<DailycostEntity> dailycostEntities) {
//        // DownUrlUtil.synchronization(mAccountbookid, dailycostEntities);
//    }

    public void billDetail(int position) {
        try {

//            Intent intent = null;
//            Bundle bundle = new Bundle();
            if (billListInfoItems == null || billListInfoItems.size() == 0) {
                return;
            }

            DailycostEntity dailycostEntity = billListInfoItems.get(position);
            PaymentDetailsActivity.openActivity(context, mAccountbookid, dailycostEntity);
//            if (dailycostEntity.getBilltype().equals("5")) {
//                intent = new Intent(context, WriteJournalDetailActivity.class);
//            } else {
//                intent = new Intent(context, PaymentDetailsActivity.class);
//            }
//            bundle.putParcelable("list", dailycostEntity);
//            intent.putExtra("mAccountbookid", mAccountbookid);
//            intent.putExtra("billid", dailycostEntity.getBillid());
//
//            intent.putExtras(bundle);
//            if (context != null) {
//                context.startActivity(intent);
//            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
