package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.common.callback.BaseCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
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
import com.yiqiji.money.modules.common.utils.MaxLengthWatcher;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.common.view.UserHeadImageView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillDetailInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillSyncInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CommentList;
import com.yiqiji.money.modules.homeModule.home.entity.CommentListItem;
import com.yiqiji.money.modules.homeModule.home.entity.SettmentDetailInfo;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.write.activity.DetailsActivity;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Response;
import retrofit.Retrofit;

public class PaymentDetailsActivity extends BaseActivity implements OnClickListener {
    private View layout_first_gon;
    //    private View view_visible;
    private ImageView bill_image;
    private TextView title_text;
    private TextView remark;
    private TextView not_involved;
    private TextView liquidated;
    private TextView blance;
    private TextView text_view;
    private TextView location_text;
    private TextView name;
    private RelativeLayout relayout;
    private ImageView location_image;
    private SelectableRoundedImageView roundedImageView;
    private RelativeLayout relayout_item;

    private View bottom_button;//底部按钮
    private boolean isSubscribe = false;//是否是订阅跳转
    private boolean isStatistics = false;//是否是统计跳转
    private DailycostEntity dailyEntity;

    private View layout_item;
    private TextView custodian;// 付款人
    private LinearLayout layout;// 付款人
    private TextView payment;// 参与人
    private LinearLayout layout_visible;// 不需要结算不显示

    private TextView operator;// 操作人
    private TextView time;// 结算时间
    private TextView write;// 写评论
    private TextView te;//
    private TextView total_balace;// 累计消费
    private LinearLayout layout_clickreview;// 没有评论的layout
    private RelativeLayout rela;//
    private LinearLayout comm_list;//
    private TextView clickreview;// 没有评论的写评论
    private MyRecyclerView list_clickreview;// 评论列表
    private View not_comment;
    //    private TextView change_button, delet_button;
    //    private LinearLayout myid_button;
    private MyRecyclerView list;
    private CommonRecyclerViewAdapter<BillMemberInfo> adapter;
    private String myuid, mAccountbookid, billid, memberid, mAccountbookcatename, mAccountbooktitle, mAccountbookcount;
    private DailycostEntity beans;
    private List<BillMemberInfo> pay = new ArrayList<BillMemberInfo>();
    private List<BillMemberInfo> custodians = new ArrayList<BillMemberInfo>();

    private CommonRecyclerViewAdapter<CommentListItem> recyclerViewAdapter;
    private List<CommentListItem> commentListItems;
    private ApiService apiService;

    private float screeWith;

    public static void openActivity(Context mContext, String mAccountbookid, DailycostEntity dailycostEntity) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        String mBilltype = dailycostEntity.getBilltype();
        if (mBilltype.equals("5")) {
            intent = new Intent(mContext, WriteJournalDetailActivity.class);
        } else {
            intent = new Intent(mContext, PaymentDetailsActivity.class);
        }
        bundle.putParcelable("list", dailycostEntity);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("billid", dailycostEntity.getBillid());

        intent.putExtras(bundle);
        if (mContext != null) {
            mContext.startActivity(intent);
        }
    }

    public static void openActivity(Context mContext, String mAccountbookid, DailycostEntity dailycostEntity, boolean isSubscribe, boolean isStatistics) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        String mBilltype = dailycostEntity.getBilltype();
        intent = new Intent(mContext, PaymentDetailsActivity.class);
        bundle.putParcelable("list", dailycostEntity);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("billid", dailycostEntity.getBillid());
        intent.putExtra("isSubscribe", isSubscribe);
        intent.putExtra("isStatistics", isStatistics);

        intent.putExtras(bundle);
        if (mContext != null) {
            mContext.startActivity(intent);
        }
    }

    public static void openActivity(Context mContext, String mAccountbookid, String billid, boolean isSubscribe, boolean isStatistics) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        intent = new Intent(mContext, PaymentDetailsActivity.class);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("billid", billid);
        intent.putExtra("isSubscribe", isSubscribe);
        intent.putExtra("isStatistics", isStatistics);

        intent.putExtras(bundle);
        if (mContext != null) {
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        apiService = RetrofitInstance.get().create(ApiService.class);
        screeWith = XzbUtils.getPhoneScreen(this).widthPixels / 3.5f;
        billid = getIntent().getStringExtra("billid");
        mAccountbookid = getIntent().getStringExtra("mAccountbookid");
        isSubscribe = getIntent().getBooleanExtra("isSubscribe", false);
        isStatistics = getIntent().getBooleanExtra("isStatistics", false);
        initView();
        initListener();
        initTile();
        setMemberList();
        if (isSubscribe) {//订阅账单详情不走数据库
            dailyEntity = getIntent().getParcelableExtra("list");
            getBillDetail();
        } else {
            DownUrlUtil.initBooksDataAndMember(mAccountbookid, handler, RequsterTag.SEARCHBOOKDETAIL);
        }
        initListClickreview();
        initData();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStatistics) {
            DownUrlUtil.initBooksDataAndMember(mAccountbookid, handler, RequsterTag.SEARCHBOOKDETAIL);
        }
    }

    private void initView() {
        bottom_button = findViewById(R.id.bottom_button);


        layout_item = (View) findViewById(R.id.layout_item);
        bill_image = (ImageView) findViewById(R.id.bill_image);
        title_text = (TextView) findViewById(R.id.title_text);
        remark = (TextView) findViewById(R.id.remark);
        not_involved = (TextView) findViewById(R.id.not_involved);
        liquidated = (TextView) findViewById(R.id.liquidated);
        blance = (TextView) findViewById(R.id.blance);
        text_view = (TextView) findViewById(R.id.text);
        relayout = (RelativeLayout) findViewById(R.id.relayout);
        location_text = (TextView) findViewById(R.id.location_text);
        name = (TextView) findViewById(R.id.name);
        location_image = (ImageView) findViewById(R.id.location_image);
        roundedImageView = (SelectableRoundedImageView) findViewById(R.id.roundedImageView);
        relayout_item = (RelativeLayout) findViewById(R.id.relayout_item);

        custodian = (TextView) findViewById(R.id.custodian);
        payment = (TextView) findViewById(R.id.payment);
        operator = (TextView) findViewById(R.id.operator);
        write = (TextView) findViewById(R.id.write);
        te = (TextView) findViewById(R.id.te);
        total_balace = (TextView) findViewById(R.id.total_balace);
        time = (TextView) findViewById(R.id.time);
//        change_button = (TextView) findViewById(R.id.share_button);
//        delet_button = (TextView) findViewById(R.id.settlement_button);
        clickreview = (TextView) findViewById(R.id.clickreview);
        layout = (LinearLayout) findViewById(R.id.layout);
//        myid_button = (LinearLayout) findViewById(R.id.myid_button);
        layout_clickreview = (LinearLayout) findViewById(R.id.layout_clickreview);
        rela = (RelativeLayout) findViewById(R.id.rela);
        comm_list = (LinearLayout) findViewById(R.id.comm_list);
        list = (MyRecyclerView) findViewById(R.id.list);
        list_clickreview = (MyRecyclerView) findViewById(R.id.list_clickreview);
        layout_visible = (LinearLayout) findViewById(R.id.layout_visible);
        layout_first_gon = (View) findViewById(R.id.layout_first_gon);
        not_comment = (View) findViewById(R.id.not_comment);
//        view_visible = (View) findViewById(R.id.view_visible);
    }

    private void initTile() {

        initTitle("消费详情", -1, null);
        bottomTwoButtonText("删除", "修改", this);

    }

    private void initText() {
        if (beans == null) {
            return;
        }
        if (BooksDetailPerecenter.isAccountbookCount(mAccountbookcount)) {// 多人账本
            setMyidButtonVisible(myuid);
            setDaiInfo();
            setdataOperater();
        } else {
            setMyidButtonVisible(myuid);
            setDaiInfo();
            setdataOperater();
        }

    }

    private void setMyidButtonVisible(String myuid) {
        boolean participate = BaserClassMode.isOperation(beans, myuid);
//        if (!participate) {
//            bottomTwoButtonGoneOrVisible(View.GONE);
//        } else {
//            bottomTwoButtonGoneOrVisible(View.VISIBLE);
//        }
        if (!isSubscribe) {
            if (!participate) {
                bottomTwoButtonGoneOrVisible(View.GONE);
            } else {
                bottomTwoButtonGoneOrVisible(View.VISIBLE);
            }
        } else {
            bottomTwoButtonGoneOrVisible(View.GONE);
        }
        if (beans.getBillclear().equals("1")) {
            bottomTwoButtonGoneOrVisible(View.GONE);
        }


    }

    private void setdataOperater() {
        initList();
        Date data = new Date(Long.parseLong(beans.getTradetime()) * 1000);
        String title = "消费详情";
        if (beans.getBilltype().equals("3")) {
            title = "结算详情";
//            changeTitle("结算详情");
            te.setVisibility(View.GONE);
            rela.setVisibility(View.GONE);
            comm_list.setVisibility(View.GONE);
            bottomTwoButtonGoneOrVisible(View.GONE);
//            myid_button.setVisibility(View.GONE);
            layout_item.setVisibility(View.GONE);
            custodian.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            payment.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                ToastUtils.DiyToast(PaymentDetailsActivity.this, "连接失败,请检查网络连接");
            }

            getClearInfo();
        } else {
            name.setVisibility(View.GONE);
            not_involved.setVisibility(View.GONE);
            bill_image.setVisibility(View.VISIBLE);
            location_text.setVisibility(View.GONE);
            location_image.setVisibility(View.GONE);
            liquidated.setVisibility(View.GONE);
            if (beans.getIsclear().equals("0")) {
                layout_visible.setVisibility(View.GONE);
            }

            String timeString = DateUtil.transferLongToDate(1, data.getTime() / 1000);
            String mBillamount = "";
            if (!TextUtils.isEmpty(beans.getBillamount())) {
                mBillamount = XzbUtils.formatDouble("%.2f", Double.parseDouble(beans.getBillamount()));
            }

            String image_url = beans.getBillcateicon();
            String cateid = beans.getBillcateid();
            String billCateName = beans.getBillcatename();
            String remark_text = beans.getBillmark();
            final String locationImage = beans.getBillimg();
            String locationText = beans.getAddress();
            String isclear = beans.getIsclear();
            String billbrand = beans.getBillbrand();
            final String billtype = beans.getBilltype();
            String text = "";
            int drawid = 0;

//
            BillDetailPerecenter.setCateNameImage(mContext, isclear, beans, new BillDetailPerecenter.CateNameImageInterface() {
                @Override
                public void getCateNameImageInterface(String cateid, String bill_type, String url, String cate_name, int title_color,
                                                      String bill_mark, String bill_balance, int cilor_balance, String bill_text, String bill_brand) {
                    title_text.setText(cate_name);
                    title_text.setTextColor(title_color);
                    remark.setText(bill_mark);
                    text_view.setText(bill_text);

                    blance.setText(StringUtils.moneySplitComma(bill_balance));
                    blance.setTextColor(cilor_balance);
                    XzbUtils.displayImage(bill_image, url, 0);
                    if (!TextUtils.isEmpty(bill_balance)) {
                        blance.setVisibility(View.VISIBLE);
                    } else {
                        blance.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(bill_brand)) {
                        name.setVisibility(View.VISIBLE);
                        name.setText(bill_brand);
                    }
                }
            });

            if ((TextUtils.isEmpty(billbrand) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(locationText) && TextUtils.isEmpty(locationImage))) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bill_image.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                bill_image.setLayoutParams(layoutParams);
                layoutParams = (RelativeLayout.LayoutParams) title_text.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                title_text.setLayoutParams(layoutParams);
                layoutParams = (RelativeLayout.LayoutParams) relayout.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                relayout.setLayoutParams(layoutParams);
            } else {
//            viewHolder.blance.setTextColor(mContext.getResources().getColor(R.color.context_color));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relayout.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                relayout.setLayoutParams(layoutParams);
                layoutParams = (RelativeLayout.LayoutParams) title_text.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                title_text.setLayoutParams(layoutParams);
                layoutParams = (RelativeLayout.LayoutParams) bill_image.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                bill_image.setLayoutParams(layoutParams);
            }
            BillDetailPerecenter.setBlancelayoutlocation(blance, mAccountbookcount);
            BillDetailPerecenter.setBillImageWithHeight(mContext, beans, roundedImageView);
            if (!TextUtils.isEmpty(locationImage)) {
                roundedImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {//账单详情查看大图
                        XzbUtils.hidePointInUmg(mContext, Constants.HIDE_PHOTO_SEE);
                        Intent intent = new Intent(mContext, PhotosShowActivity.class);
                        intent.putExtra("path", locationImage);
                        intent.putExtra("isNeedDelet", true);
                        startActivity(intent);
                    }
                });
            }
            BillDetailPerecenter.setlocationHeight(mContext, beans, location_text, location_image);
//            if (!TextUtils.isEmpty(locationText)) {
//                location_text.setText(locationText);
//                location_image.setVisibility(View.VISIBLE);
//                location_text.setVisibility(View.VISIBLE);
//
//            } else {
//                location_text.setVisibility(View.GONE);
//                location_image.setVisibility(View.GONE);
//            }
//            XzbUtils.displayImage(bill_image, image_url, 0);
//            blance.setText(mBillamount);
//
//
//            remark.setText(remark_text);
//            text_view.setText(text);
            if (!BooksDetailPerecenter.isAccountbookCount(mAccountbookcount)) {
                text_view.setVisibility(View.GONE);
            } else {
                text_view.setVisibility(View.VISIBLE);
            }
//            title_text.setText(billCateName);

        }
        changeTitle(title);
        operator.setText("操作人:" + beans.getUsername());
        Date dataDate = new Date(Long.parseLong(beans.getBillctime()) * 1000);
        time.setText("操作时间:" + DateUtil.transferLongToDate(5, dataDate.getTime() / 1000));
//        view_visible.setVisibility(View.GONE);
        if (beans != null) {
            layout_first_gon.setVisibility(View.GONE);
        } else {
            layout_first_gon.setVisibility(View.VISIBLE);
        }

    }

    private void getBillDetail() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("billid", billid);
        CommonFacade.getInstance().exec(Constants.BILLDETAIL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog(false);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                BillDetailInfo billDetailInfo = GsonUtil.GsonToBean(o.toString(), BillDetailInfo.class);
                if (billDetailInfo.getData() != null) {
                    beans = billDetailInfo.getData();
                    List<BillMemberInfo> billMemberInfos = beans.getMemberlist();
                    if (!StringUtils.isEmptyList(billMemberInfos)) {
                        for (int i = 0; i < billMemberInfos.size(); i++) {
                            BillMemberInfo billMemberInfo = billMemberInfos.get(i);
                            billMemberInfo.setBillid(beans.getBillid());
                            billMemberInfos.set(i, billMemberInfo);
                        }
                    }
                    beans.setMemberlist(billMemberInfos);

                    if (booksDbInfo != null) {
                        beans.setIsclear(booksDbInfo.getIsclear());
                        beans.setWhichbook(booksDbInfo.getAccountbooktitle());
                        beans.setAccountbooktype(booksDbInfo.getAccountbooktype());
                        beans.setIssynchronization("true");
                    } else {
                        mAccountbookcount = beans.getBillcount();
                        if (dailyEntity != null) {
//                            beans.setIsclear(dailyEntity.getIsclear());
                            beans.setWhichbook(dailyEntity.getWhichbook());
                            beans.setAccountbooktype(dailyEntity.getAccountbooktype());
                            beans.setIssynchronization("false");
                        }

                    }

                    setMyidButtonVisible(myuid);
                    setDaiInfo();
                    setdataOperater();
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                dismissDialog();
            }
        });

    }

    private void getClearInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", mAccountbookid);
        hashMap.put("billid", billid);

        CommonFacade.getInstance().exec(Constants.GETCLEARINFO, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
//                showLoadingDialog(false);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                SettmentDetailInfo settmentDetailInfo = GsonUtil.GsonToBean(o.toString(), SettmentDetailInfo.class);
                DailycostEntity dailycostEntity = settmentDetailInfo.getData();
                if (dailycostEntity != null) {
                    beans = dailycostEntity;
                }
                operator.setText("操作人:" + dailycostEntity.getUsername());
                Date data = new Date(Long.parseLong(dailycostEntity.getBillctime()) * 1000);

                time.setText("操作時間:" + DateUtil.transferLongToDate(0, data.getTime() / 1000));
                List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                pay.clear();
                for (BillMemberInfo billMemberInfo : billMemberInfos) {
                    billMemberInfo.setType(dailycostEntity.getBilltype());
                    pay.add(billMemberInfo);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                dismissDialog();
            }
        });

    }

    private void initListener() {
        clickreview.setOnClickListener(this);
        write.setOnClickListener(this);
//        delet_button.setOnClickListener(this);
//        change_button.setOnClickListener(this);

    }

    private void initList() {

        if (beans == null || beans.getMemberlist() == null) {
            return;
        }
        pay.clear();
        custodians.clear();
        for (int i = 0; i < beans.getMemberlist().size(); i++) {
            BillMemberInfo billMemberInfo = beans.getMemberlist().get(i);
            if (beans.getBilltype().equals("4")) {
                if (billMemberInfo.getType().equals("1")) {// 收款人
                    custodians.add(billMemberInfo);
                } else {
                    pay.add(billMemberInfo);

                }
            } else {
                if (billMemberInfo.getType().equals("0")) {// 收款人
                    custodians.add(billMemberInfo);
                } else {
                    pay.add(billMemberInfo);

                }
            }

        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        custodian.setText("付款人" + " " + "(" + custodians.size() + ")");
        payment.setText("参与人" + " " + "(" + pay.size() + ")");
        if (beans.getBilltype().equals("4")) {
            custodian.setText("收款人" + " " + "(" + custodians.size() + ")");
            payment.setText("付款人" + " " + "(" + pay.size() + ")");
        }

        if (layout != null && layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        for (int i = 0; i < custodians.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_payment_details_item, null);
            TextView user_name = (TextView) view.findViewById(R.id.user_name);
            TextView balane = (TextView) view.findViewById(R.id.balane);
            TextView text = (TextView) view.findViewById(R.id.text);
            TextView total_balace = (TextView) view.findViewById(R.id.total_balace);
            RelativeLayout layout_para = (RelativeLayout) view.findViewById(R.id.layout_para);
            UserHeadImageView head_image = (UserHeadImageView) view.findViewById(R.id.head_image);
            text.setText("支付");
            if (beans.getBilltype().equals("4")) {
                text.setText("收款");
            }
            BillMemberInfo billMemberInfo = custodians.get(i);

            total_balace.setVisibility(View.GONE);
            head_image.displayImage(custodians.get(i));

            user_name.setText(billMemberInfo.getUsername());
            balane.setText(billMemberInfo.getAmount());
            LayoutParams layoutParams = (LayoutParams) layout_para.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.list_item_height_60);
            layout_para.setLayoutParams(layoutParams);
            layout.addView(view);
            String type = beans.getBilltype();// 类型（0表示收入，1表示支出,2转账，3结算，4交款）
            if (type.equals("0")) {
                balane.setTextColor(getResources().getColor(R.color.imcom_text_color));
            } else if (type.equals("1")) {
                balane.setTextColor(getResources().getColor(R.color.expenditure));
            } else {
                balane.setTextColor(getResources().getColor(R.color.context_color));
            }

        }

    }

    private void setMemberList() {
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        list.setLayoutManager(fullyLinearLayoutManager);
        adapter = new CommonRecyclerViewAdapter<BillMemberInfo>(this, pay) {

            @Override
            public int getLayoutViewId(int viewType) {

                return R.layout.activity_payment_details_item;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, BillMemberInfo entity, int position) {
                RelativeLayout layout_para = h.getView(R.id.layout_para);
                LayoutParams layoutParams = (LayoutParams) layout_para.getLayoutParams();
                layoutParams.height = (int) getResources().getDimension(R.dimen.list_item_height_60);
                layout_para.setLayoutParams(layoutParams);
                h.setText(R.id.user_name, entity.getUsername());

                int index = Integer.parseInt(entity.getMemberid()) % 10;
                String image_url = entity.getUsericon();
                String mAmout = entity.getAmount();
                if (TextUtils.isEmpty(image_url)) {
                    image_url = "drawable://" + RequsterTag.head_image[index];
                }
                h.setText(R.id.text, "花费");
                if (beans.getBilltype().equals("4")) {
                    h.setText(R.id.text, "付款");
                }
                h.setImage(R.id.head_image, image_url, RequsterTag.head_image[index]);
                String type = beans.getBilltype();// 类型（0表示收入，1表示支出,2转账，3结算，4交款）
                if (type.equals("1")) {// 账单类型
                    // :入账类型:0.收款,1.交款,2.转账，3.结算，
                    ((TextView) h.getView(R.id.total_balace)).setVisibility(View.GONE);
                    h.setText(R.id.balane, entity.getAmount());
                    h.setTextColor(R.id.balane, getResources().getColor(R.color.expenditure));
                } else if (type.equals("0")) {
                    ((TextView) h.getView(R.id.total_balace)).setVisibility(View.GONE);
                    h.setText(R.id.balane, entity.getAmount());
                    h.setTextColor(R.id.balane, getResources().getColor(R.color.imcom_text_color));
                } else if (type.equals("2")) {
                    ((TextView) h.getView(R.id.total_balace)).setVisibility(View.GONE);
                    h.setText(R.id.text, "转账");
                    h.setTextColor(R.id.balane, getResources().getColor(R.color.context_color));
                } else if (type.equals("3")) {
                    ((TextView) h.getView(R.id.total_balace)).setVisibility(View.VISIBLE);
                    h.setText(R.id.total_balace, "累计消费:" + entity.getSpent());
                    if (entity.getRichman().equals("1")) {
                        h.setText(R.id.text, "已收款");
                        h.setTextColor(R.id.text, getResources().getColor(R.color.income));
                        h.setText(R.id.balane, entity.getRecamount());
                    } else {
                        ((TextView) h.getView(R.id.total_balace)).setVisibility(View.VISIBLE);
                        h.setText(R.id.text, "已还款");
                        h.setTextColor(R.id.text, getResources().getColor(R.color.expenditure));
                        h.setText(R.id.balane, entity.getPayamount());
                    }

                } else if (type.equals("4")) {//成员交款
                    ((TextView) h.getView(R.id.total_balace)).setVisibility(View.GONE);
                    h.setText(R.id.balane, entity.getAmount());
                    h.setTextColor(R.id.balane, getResources().getColor(R.color.context_color));
                }
                if (beans.getIsclear().equals("1")) {

                }
                if (entity.getDeviceid().equals(beans.getDeviceid())) {

                }

            }
        };
        list.setAdapter(adapter);
    }

    private void initListClickreview() {
        commentListItems = new ArrayList<CommentListItem>();
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        list_clickreview.setLayoutManager(fullyLinearLayoutManager);
        recyclerViewAdapter = new CommonRecyclerViewAdapter<CommentListItem>(this, commentListItems) {

            @Override
            public int getLayoutViewId(int viewType) {
                // TODO Auto-generated method stub
                return R.layout.activity_comment_tems;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, CommentListItem entity, int position) {
                Date date = new Date(Long.parseLong(entity.getCtime()) * 1000);
                h.setText(R.id.total_balace, entity.getContent());
                h.setTextVisible(R.id.total_balace);
                String time = DateUtil.transferLongToDate(5, Long.parseLong(entity.getCtime()));
                h.setText(R.id.user_name, entity.getUsername());
                h.setText(R.id.user_time, time);
                XzbUtils.displayImage((CircleImageView) h.getView(R.id.head_image), entity.getUsericon(), R.drawable.me_icon);

            }
        };

        list_clickreview.setAdapter(recyclerViewAdapter);
    }

    private void initData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", mAccountbookid);
        hashMap.put("billid", billid);

        CommonFacade.getInstance().exec(Constants.GETCOMMENT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                CommentList commentList = GsonUtil.GsonToBean(o.toString(), CommentList.class);
                commentListItems.clear();
                for (int i = 0; i < commentList.getData().size(); i++) {
                    commentListItems.add(commentList.getData().get(i));
                }
                if (recyclerViewAdapter != null) {
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                if (commentList.getData().size() == 0) {
                    layout_clickreview.setVisibility(View.VISIBLE);
                    not_comment.setVisibility(View.VISIBLE);
                    write.setVisibility(View.GONE);
                } else {
                    layout_clickreview.setVisibility(View.GONE);
                    not_comment.setVisibility(View.GONE);
                    write.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.settlement_button:// 修改
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    if (booksDbInfo != null && booksDbInfo.getIsclear().equals("1")) {
                        showToast("请连接网络");
                        return;
                    }
                }
                if (beans != null) {
                    onStartActivity(mAccountbookcount, beans.getIsclear());
                }


                break;

            case R.id.share_button:// 删除
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    if (booksDbInfo != null && booksDbInfo.getIsclear().equals("1")) {
                        showToast("请连接网络");
                        return;
                    }
                }
                DialogUtils.showConfirm(this, "提示", "您确定要删除吗？", "确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletBill();
                        DialogUtils.dismissConfirmDialog();
                    }
                }, "取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.dismissConfirmDialog();
                    }
                });

                break;
            case R.id.delet:// 取消操作
                basePopuWindow.dismiss();
                break;
            case R.id.hasign:// 已经记过
                if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                    deletBill();
                } else {
                    DownUrlUtil.deleteDailycostInfo(beans.getBillid(), handler, 0);
                }

                basePopuWindow.dismiss();
                break;
            case R.id.sign_rong:// 记错
                deletBill();
                basePopuWindow.dismiss();
                break;
            case R.id.other_reson:// 其他原因
                deletBill();
                basePopuWindow.dismiss();
                break;
            case R.id.clickreview:
                String tokenid = LoginConfig.getInstance().getTokenId();
                if (TextUtils.isEmpty(tokenid)) {
                    DialogUtils.showConfirm(this, "提示", "您还未登录,是否登录？ ", "确定", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            LoginBaseActivity.openActivity(PaymentDetailsActivity.this);
//                            IntentUtils.startActivityOnLogin(PaymentDetailsActivity.this, IntentUtils.LoginIntentType.PINGLUN);
//                            Intent intent = new Intent(PaymentDetailsActivity.this, LoginActivity.class);
//                            startActivity(intent);
                            DialogUtils.dismissConfirmDialog();

                        }
                    }, "取消", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DialogUtils.dismissConfirmDialog();

                        }
                    });
                    return;
                }

                onScroll();
                break;
            case R.id.write:
                onScroll();
                break;

            default:
                break;
        }
    }

    /**
     * 初始化成员
     */
    private void initMemberData() {
        DownUrlUtil.initMemberData(booksDbInfo.getAccountbookid(), handler, RequsterTag.SEARCHBOOKMENBER);

    }

    BooksDbInfo booksDbInfo;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isStatistics) {
                        EventBus.getDefault().post("updatebill");
                        finish();
                        return;
                    }
                    Intent intent = new Intent(PaymentDetailsActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case RequsterTag.SEARCHBOOKDETAIL:
                    booksDbInfo = (BooksDbInfo) msg.obj;
                    if (booksDbInfo == null) {
                        getBillDetail();
                        return;
                    }
                    changeMemberIncomExpexe(booksDbInfo);
                    DownUrlUtil.searchDailycostEntitysAndMember(mAccountbookid, billid, handler,
                            RequsterTag.SEARCHNDAIANMENBER);

                    // initMemberData();
                    break;
                case RequsterTag.SEARCHBOOKMENBER:
                    // List<BooksDbMemberInfo> booksDbMemberInfos =
                    // (List<BooksDbMemberInfo>) msg.obj;
                    // booksDbInfo.setMember(booksDbMemberInfos);

                    break;
                case RequsterTag.SEARCHNDAIANMENBER:

                    DailycostEntity dailycostEntity = (DailycostEntity) msg.obj;
                    if (dailycostEntity != null && dailycostEntity.getIssynchronization().equals("true")) {
                        getBillDetail();
                    }

                    if (dailycostEntity == null) {
                        getBillDetail();
                        return;
                    }
                    beans = dailycostEntity;
                    initText();

                    break;
                case 2:

                    break;

                default:
                    break;
            }
        }
    };

    public void seachBillMember(final List<DailycostEntity> dailycostEntities) {
        DownUrlUtil.seachBillMemberAssignmentDai(dailycostEntities, 2, handler);
    }

    private void changeMemberIncomExpexe(BooksDbInfo booksDbInfo) {
        String deviceid = booksDbInfo.getDeviceid();
        myuid = booksDbInfo.getMyuid();
        mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        mAccountbookcatename = booksDbInfo.getAccountbookcatename();
        mAccountbookcount = booksDbInfo.getAccountbookcount();
//        List<BooksDbMemberInfo> booksDbMemberInfos = booksDbInfo.getMember();
        memberid = getMySelfNyMemeberId(myuid, booksDbInfo.getMember());

    }

    /**
     * 生成一个提交表单对象
     *
     * @param action 当前是add updata delete
     * @return
     */
    private PostBill getPostBillData(String action) {

//        String mPkid = LoginConfig.getInstance(MyApplicaction.getContext()).getDeviceid();// 作为主键的Pkid
        String mPkid = beans.getPkid();// 作为主键的Pkid
        List<MemberlistBean> memberlist = new ArrayList<>();
        MemberlistBean mMemberlistBean = null;

        // 判断参与人中是否有付款人

        // 这个适合多人的时候
        List<BillMemberInfo> memberInfoList = beans.getMemberlist();
        if (!StringUtils.isEmptyList(memberInfoList)) {
            for (int i = 0; i < memberInfoList.size(); i++) {
                BillMemberInfo billMemberInfo = memberInfoList.get(i);
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


        PostBill mPostBill = new PostBill();

        mPostBill.setTradetime(Integer.parseInt(Long.parseLong(beans.getTradetime()) + ""));// 需要修改
        mPostBill.setPkid(mPkid + "");//
        mPostBill.setAccountbookid(beans.getAccountbookid());
        mPostBill.setCateid(beans.getBillcateid());// 一级分类ID
        mPostBill.setAction(action);// 是添加删除修改
        mPostBill.setBillctime(Integer.parseInt(Long.parseLong(beans.getBillctime()) + ""));// 时间选择器时间
        mPostBill.setBillclear(Integer.parseInt(beans.getBillclear()));// 0创建时是未结算
        mPostBill.setBillamount(beans.getBillamount());
        mPostBill.setBilltype(Integer.parseInt(beans.getBilltype()));// 账单类型，收入，支出，转账
        // 需要修改
        mPostBill.setRemark(beans.getBillmark());
        mPostBill.setAccountnumber(beans.getAccountnumber());
        mPostBill.setBillid(beans.getBillid());
        mPostBill.setMemberlist(memberlist);
        mPostBill.setAccountbooktype(Integer.parseInt(beans.getBilltype()));
        mPostBill.setBillimg(dailycostEntity.getBillimg());
        mPostBill.setAddress(dailycostEntity.getAddress());
        return mPostBill;
    }

    private void deletBill() {
        if (beans == null) {
            return;
        }
        List<PostBill> dataList = new ArrayList<>();
        if (beans.getIssynchronization().equals("false")) {
            DownUrlUtil.deleteDailycostInfo(beans.getBillid(), handler, 0);
//            DbInterface.deleteDailycostEntityDataBase(beans.getBillid());
//            Intent intent = new Intent(PaymentDetailsActivity.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();

            return;
        } else {
            if (beans.getIsclear().endsWith("0")) {
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    DownUrlUtil.deleteDailycostInfo(beans.getBillid(), handler, 0);
                }

//                DbInterface.deleteDailycostEntityDataBase(beans.getBillid());
//                Intent intent = new Intent(PaymentDetailsActivity.this, HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            } else {
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    showToast("没有网络");
                    return;
                }
            }
        }


        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
        String sign = StringUtils.setSign(valus, params);
        hashMap.put("sign", sign);


        final PostBill mBillData = getPostBillData("del");
        dataList.add(mBillData);

        String json = GsonUtil.GsonString(dataList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.sync_book(hashMap, body).enqueue(new BaseCallBack<BillSyncInfo>(this, true) {
            @Override
            public void onResponse(Response<BillSyncInfo> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                try {
                    BillSyncInfo billSyncInfo = arg0.body();
                    if (billSyncInfo.getCode() == 0) {
                        DownUrlUtil.deleteDailycostInfo(beans.getBillid(), handler, 0);

                        // Intent intent = new
                        // Intent(PaymentDetailsActivity.this,
                        // HomeActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // startActivity(intent);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            public void onFailure(Throwable arg0) {
                showToast("网络连接出错");
            }
        });
    }

    private MyPopuwindows basePopuWindow;
    // 评论是入矿

    EditText content_praise;
    // 发表
    TextView publish_button;

    public void onScroll() {

        View view2 = LayoutInflater.from(this).inflate(R.layout.beauty_finance_vedios, null);
        basePopuWindow = new MyPopuwindows(this, view2);
        basePopuWindow.showAtLocation(list_clickreview, Gravity.BOTTOM, 0, 0);

        content_praise = (EditText) view2.findViewById(R.id.content_praise);
        new MaxLengthWatcher(120, content_praise);


        publish_button = (TextView) view2.findViewById(R.id.publish_button);
        content_praise.setFocusable(true);
        content_praise.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) publish_button.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        publish_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = StringUtils.StringFilter(content_praise.getText().toString());

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.DiyToast(PaymentDetailsActivity.this, "您还没有添加评论内容");
                    return;
                }
                comment(content);

            }
        });

    }

    private void comment(String content) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("id", mAccountbookid);
        hashMap.put("billid", billid);
        hashMap.put("memberid", memberid);
        hashMap.put("content", content);
        CommonFacade.getInstance().exec(Constants.COMMENT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                basePopuWindow.dismiss();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                initData();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);

                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -110 || simleMsg.getErrCode() == -100) {
                    showToast(simleMsg);
                } else {
                    DialogUtils.showConfirm(PaymentDetailsActivity.this, "提示", "您还未登录,是否登录？ ", "确定", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            LoginBaseActivity.openActivity(PaymentDetailsActivity.this);
//                            IntentUtils.startActivityOnLogin(PaymentDetailsActivity.this, IntentUtils.LoginIntentType.PINGLUN);
                            DialogUtils.dismissConfirmDialog();

                        }
                    }, "取消", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DialogUtils.dismissConfirmDialog();

                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }

    /**
     * 根据账本类型分别跳转 在记一笔中区别就是有没有成员 1、单人记账 2、多人记账
     */
    private void onStartActivity(String mAccountbookcount, String isclear) {
        Date date = new Date(Long.parseLong(dailycostEntity.getTradetime()) * 1000);
        DetailsActivity.startActivity(this, booksDbInfo, dailycostEntity, memberid, date, isStatistics);
    }

    DailycostEntity dailycostEntity;

    public void setDaiInfo() {
        dailycostEntity = beans;
        dailycostEntity.setPkid(StringUtils.getUUID());

        long time = Long.parseLong(beans.getTradetime());
        Date date_time = new Date(time * 1000);

        dailycostEntity.setYear(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 5)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 3)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 2)));
    }

    public void onEventMainThread(CheckMessageInfo checkMessageInfo) {
        if (isStatistics) {//订阅账单详情不走数据库
            getBillDetail();
        } else {
            DownUrlUtil.initBooksDataAndMember(mAccountbookid, handler, RequsterTag.SEARCHBOOKDETAIL);
        }
    }
}
