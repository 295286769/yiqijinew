package com.yiqiji.money.modules.homeModule.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.entity.PostBill.MemberlistBean;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.homeModule.home.activity.BillActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.BillIAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillListInfoItem;
import com.yiqiji.money.modules.homeModule.home.entity.BillSyncInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@SuppressLint("ValidFragment")
public class BillIFragment extends Fragment implements OnLongClickListener, OnClickListener {
    private Context context;
    private Date date_time;
    private View view;
    private int mType;
    private PinnedHeaderListView listview;
    private TextView textView;
    private TextView moth_text;
    private TextView note_data_text;
    private ImageView curent_image;
    private View nodata;
    private BillIAdapter customAdapter;
    private String mAccountbookid = "";
    private String sorttype = "";
    private String accountbooktype = "";
    private String accountbookcount = "";
    private ApiService apiService;

    public BillIFragment(Context context, Date date_time, int mType, String mAccountbookid, String sorttype, String accountbooktype, String accountbookcount) {
        this.context = context;
        this.date_time = date_time;
        this.mType = mType;
        this.mAccountbookid = mAccountbookid;
        this.sorttype = sorttype;
        this.accountbooktype = accountbooktype;
        this.accountbookcount = accountbookcount;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bill, null);
            apiService = RetrofitInstance.get().create(ApiService.class);
            initView();
            initData(date_time, sorttype);
        }
        return view;
    }

    private void initView() {
        listview = (PinnedHeaderListView) view.findViewById(R.id.listview);
        textView = (TextView) view.findViewById(R.id.text);
        curent_image = (ImageView) view.findViewById(R.id.curent_image);
        moth_text = (TextView) view.findViewById(R.id.moth_text);
        note_data_text = (TextView) view.findViewById(R.id.note_data_text);
        nodata = (View) view.findViewById(R.id.nodata);
        View HeaderView = getActivity().getLayoutInflater().inflate(R.layout.listview_item_header, listview, false);
        listview.setPinnedHeader(HeaderView);
        customAdapter = new BillIAdapter(getActivity(), sorttype, accountbookcount, list, this, this);

        listview.setAdapter(customAdapter);
        listview.setOnScrollListener(customAdapter);


    }

    public void setType(int type) {
        this.mType = type;
        initData(date_time, sorttype);
    }

    public void initData(final Date record_date, String sorttype) {
        this.date_time = record_date;
        this.sorttype = sorttype;
        boolean isExpectMoth = DateUtil.isExpectMoth(date_time);
        int what = 0;
        if (isExpectMoth) {// 预期月份
            what = RequsterTag.EXPECTMOTH;
        }
        DownUrlUtil.initDataReturnDailycostEntitys(date_time, mAccountbookid, sorttype, mType + "", isExpectMoth, 1,
                1000, handler, what);

    }

    private List<DailycostEntity> list = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (nodata == null) {
                        return;
                    }
                    list.clear();
                    List<DailycostEntity> lists = (List<DailycostEntity>) msg.obj;
                    updateData(lists);

                    break;
                case 1:
                    break;
                case RequsterTag.EXPECTMOTH:
                    List<DailycostEntity> list3 = (List<DailycostEntity>) msg.obj;
                    Message message2 = new Message();
                    message2.what = 0;
                    message2.obj = list3;
                    handler.sendMessage(message2);
                    break;

                default:
                    break;
            }

        }

    };

    private void updateData(List<DailycostEntity> lists) {
        for (int i = 0; i < lists.size(); i++) {
            list.add(lists.get(i));
        }
        if (list.size() == 0) {
            nodata.setVisibility(View.VISIBLE);
            Date date = new Date();
            String current_moth = DateUtil.formatTheDateToMM_dd(date, 3);
            String current_year = DateUtil.formatTheDateToMM_dd(date, 5);
            String moth = DateUtil.formatTheDateToMM_dd(date_time, 3);
            String year = DateUtil.formatTheDateToMM_dd(date_time, 5);

            if (sorttype.equals("0")) {
                moth_text.setVisibility(View.VISIBLE);
                note_data_text.setVisibility(View.VISIBLE);
                curent_image.setImageResource(R.drawable.no_counter_data);
                moth_text.setText("");
            } else if (current_moth.equals(moth) && current_year.equals(year)) {
                moth_text.setVisibility(View.VISIBLE);
                note_data_text.setVisibility(View.VISIBLE);
                moth_text.setText(StringUtils.getCurMonth(moth));

                curent_image.setImageResource(R.drawable.no_counter_data);
            } else {

                moth_text.setVisibility(View.GONE);
                note_data_text.setVisibility(View.GONE);
                curent_image.setImageResource(R.drawable.not_current_moth);
            }
        } else {
            nodata.setVisibility(View.GONE);

        }
        if (customAdapter != null) {
            customAdapter.notifyDataSetChanged();
        }
    }

    private BillListInfoItem toBillListInfoItem(DailycostEntity dailycostEntity) {
        BillListInfoItem billListInfoItem = new BillListInfoItem();
        billListInfoItem.setAccountbookid(dailycostEntity.getAccountbookid());
        billListInfoItem.setAccountnumber(dailycostEntity.getAccountnumber());
        billListInfoItem.setBillamount(dailycostEntity.getBillamount() + "");
        billListInfoItem.setBillcateicon(dailycostEntity.getBillcateicon());
        billListInfoItem.setBillcateid(dailycostEntity.getBillcateid());
        billListInfoItem.setBillcatename(dailycostEntity.getBillcatename());
        billListInfoItem.setBillclear(dailycostEntity.getBillclear());
        billListInfoItem.setBillcount(dailycostEntity.getBillcount());
        billListInfoItem.setBillctime(dailycostEntity.getTradetime());
        billListInfoItem.setBillid(dailycostEntity.getBillid());
        billListInfoItem.setBillmark(dailycostEntity.getBillmark());
        billListInfoItem.setBillsubcateicon(dailycostEntity.getBillsubcateicon());
        billListInfoItem.setBillsubcateid(dailycostEntity.getBillsubcateid());
        billListInfoItem.setBillsubcatename(dailycostEntity.getBillsubcatename());
        billListInfoItem.setBilltype(dailycostEntity.getBilltype());
        billListInfoItem.setTradetime(dailycostEntity.getBillctime());
        billListInfoItem.setUsername(dailycostEntity.getUsername());
        return billListInfoItem;

    }

    private void deleteDailycostInfo(final String billid, List<DailycostEntity> listData, int position) {
        listData.remove(position);
        updateData(listData);
//        if (customAdapter != null) {
//            customAdapter.notifyDataSetChanged();
//        }
        DownUrlUtil.deleteDailycostInfo(billid, handler, 890);
        EventBus.getDefault().post(new AddMemberResponse());
    }

    private void deletBill(final int position, DailycostEntity beans) {
        List<PostBill> dataList = new ArrayList<>();
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
        String sign = StringUtils.setSign(valus, params);
        hashMap.put("sign", sign);
        final PostBill mBillData = getPostBillData("del", beans);
        dataList.add(mBillData);

        String json = GsonUtil.GsonString(dataList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.sync_book(hashMap, body).enqueue(new Callback<BillSyncInfo>() {
            @Override
            public void onResponse(Response<BillSyncInfo> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                try {
                    BillSyncInfo billSyncInfo = arg0.body();
                    if (billSyncInfo.getCode() == 0) {
                        deleteDailycostInfo(list.get(position).getBillid(), list, position);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) {
                ToastUtils.DiyToast(getActivity(), "网络连接出错");
            }
        });
    }

    /**
     * 生成一个提交表单对象
     *
     * @param action 当前是add updata delete
     * @return
     */
    private PostBill getPostBillData(String action, DailycostEntity beans) {
//        String mPkid = LoginConfig.getInstance(MyApplicaction.getContext()).getDeviceid();// 作为主键的Pkid
        String mPkid = beans.getPkid();// 作为主键的Pkid
        List<MemberlistBean> memberlist = new ArrayList<>();
        MemberlistBean mMemberlistBean = null;
        PostBill mPostBill = new PostBill();
        if (beans == null) {
            return mPostBill;
        }

        // 判断参与人中是否有付款人
        if (beans.getMemberlist() != null) {
            for (int i = 0; i < beans.getMemberlist().size(); i++) {
                BillMemberInfo billMemberInfo = beans.getMemberlist().get(i);
                String memberid = billMemberInfo.getMemberid();
                mMemberlistBean = new MemberlistBean();
                mMemberlistBean.setAmount(billMemberInfo.getAmount());
                mMemberlistBean.setCtime(Long.parseLong(beans.getBillctime()));
                mMemberlistBean.setMemberid(memberid);
                mMemberlistBean.setStatus(Integer.parseInt(billMemberInfo.getStatus()));// 需要修改
                mMemberlistBean.setType(Integer.parseInt(billMemberInfo.getType()));// 0.收入;1.支出;2.转账
                memberlist.add(mMemberlistBean);
            }
        }


        mPostBill.setTradetime(Integer.parseInt(beans.getTradetime()));// 需要修改
        mPostBill.setPkid(mPkid);//
        mPostBill.setAccountbookid(beans.getAccountbookid());
        mPostBill.setCateid(beans.getBillcateid());// 一级分类ID
        mPostBill.setAction(action);// 是添加删除修改
        mPostBill.setBillctime(Integer.parseInt(beans.getBillctime()));// 时间选择器时间
        mPostBill.setBillclear(Integer.parseInt(beans.getBillclear()));// 0创建时是未结算
        mPostBill.setBillamount(beans.getBillamount());
        mPostBill.setBilltype(Integer.parseInt(beans.getBilltype()));// 账单类型，收入，支出，转账
        // 需要修改
        mPostBill.setRemark(beans.getBillmark());
        mPostBill.setAccountnumber(beans.getAccountnumber());
        mPostBill.setBillid(beans.getBillid());
        mPostBill.setMemberlist(memberlist);
        mPostBill.setAccountbooktype(Integer.parseInt(beans.getBilltype()));
        mPostBill.setBillimg(beans.getBillimg());
        mPostBill.setAddress(beans.getAddress());
        return mPostBill;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            view = null;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.relayout_item:
                final int positio = Integer.parseInt(v.getTag().toString());

                DialogUtils.showConfirm(getActivity(), "提示", "您确定要删除这条账单?", "确定", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                            deletBill(positio, list.get(positio));
                        } else {
                            ((BillActivity) context).showToast("请检查网络连接");
//                            deleteDailycostInfo(list.get(positio).getBillid(), list, positio);
                        }
                        DialogUtils.dismissConfirmDialog();

                    }
                }, "取消", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DialogUtils.dismissConfirmDialog();

                    }
                });

                break;

            default:
                break;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.relayout_item://账单详情
                final int positio = Integer.parseInt(v.getTag().toString());
                BaserClassMode.billDetail(getActivity(), positio, list, mAccountbookid);

                break;

            default:
                break;
        }

    }
}
