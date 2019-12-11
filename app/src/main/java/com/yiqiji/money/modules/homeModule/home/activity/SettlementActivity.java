package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.UnListenerHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementItemInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author huangweishui
 */
public class SettlementActivity extends BaseActivity implements OnClickListener {
    private MyRecyclerView list_view;
    private View not_data;
    private CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter;
    private TextView share_button, settlement_button;
    private List<BooksSettlementItemInfo> booksDbMemberInfos;
    private BooksDbMemberInfo booksDbMemberInfo;
    private String id;
    private String memberid;
    private String memberName = "";
    private String bookName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settlement_activity);
        id = getIntent().getStringExtra("id");
        memberid = getIntent().getStringExtra("memberid");
        initView();
        initAdapter();
        initInternetData();
        DownUrlUtil.initBooksDataAndMember(id, handler, 0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    BooksDbInfo booksDbInfo = (BooksDbInfo) msg.obj;
                    if (booksDbInfo != null) {
                        bookName = booksDbInfo.getAccountbooktitle();
                        memberName = getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbInfo.getMember());
                    }

                    break;

            }
        }
    };

    private void initInternetData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        CommonFacade.getInstance().exec(Constants.BOOK_CLEAR_DETAIL, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                BooksSettlementListInfo booksSettlementListInfo = GsonUtil.GsonToBean(o.toString(), BooksSettlementListInfo.class);
                if (booksDbMemberInfos != null && booksDbMemberInfos.size() > 0) {
                    booksDbMemberInfos.clear();
                }
                double total = 0.00;
                List<BooksSettlementItemInfo> booksDbMembers = booksSettlementListInfo.getData();
                if (booksDbMembers != null) {
                    for (int i = 0; i < booksDbMembers.size(); i++) {
                        BooksSettlementItemInfo booksSettlementItemInfo = booksDbMembers.get(i);
                        if (booksSettlementItemInfo.getRichman().equals("0")) {
                            total += Double.parseDouble(booksSettlementItemInfo.getPayamount());
                        }
//                        if (booksSettlementItemInfo.getMemberid().equals(memberid)) {
//                            memberName = booksSettlementItemInfo.getUsername();
//                        }
                        booksDbMemberInfos.add(booksSettlementItemInfo);
                    }
                }

                if (booksDbMemberInfos.size() > 0) {
                    not_data.setVisibility(View.GONE);
                } else {
                    not_data.setVisibility(View.VISIBLE);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
                settlement_button.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }

    private void initView() {

        list_view = (MyRecyclerView) findViewById(R.id.list_view);
        share_button = (TextView) findViewById(R.id.share_button);
        settlement_button = (TextView) findViewById(R.id.settlement_button);
        not_data = (View) findViewById(R.id.not_data);

        share_button.setOnClickListener(this);
        settlement_button.setOnClickListener(this);
        if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
            not_data.setVisibility(View.VISIBLE);
        }

    }

    private void initAdapter() {
        booksDbMemberInfos = new ArrayList<BooksSettlementItemInfo>();
        initTitle("全员结算");
        adapter = new CommonRecyclerViewAdapter<BooksSettlementItemInfo>(this, booksDbMemberInfos) {

            @Override
            public int getLayoutViewId(int viewType) {

                return R.layout.settlement_activity_item;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, BooksSettlementItemInfo entity, int position) {

                if (entity.getRichman().equals("1")) {// 财主
                    h.setText(R.id.pay_text, "收取");
                    h.setTextColor(R.id.pay_text, getResources().getColor(R.color.income));
                    double balance = (Double) (Double.parseDouble(entity.getReceivable()) == 0 ? 0.00 : Double
                            .parseDouble(entity.getReceivable()));
                    h.setText(R.id.collect_balance, StringUtils.moneySplitComma(XzbUtils.formatDouble("%.2f", balance)));

                } else {
                    h.setText(R.id.pay_text, "支付");
                    h.setTextColor(R.id.pay_text, getResources().getColor(R.color.expenditure));
                    double balance = (double) (Double.parseDouble(entity.getPayamount()) == 0 ? 0.00 : Double.parseDouble(entity.getPayamount()));

                    h.setText(R.id.collect_balance, StringUtils.moneySplitComma(XzbUtils.formatDouble("%.2f", balance)));

                }
                int index = Integer.parseInt(entity.getMemberid()) % 10;
                String image_url = entity.getUsericon();
                if (TextUtils.isEmpty(image_url)) {
                    image_url = "drawable://" + RequsterTag.head_image[index];
                }

                h.setImage(R.id.head_image, image_url, RequsterTag.head_image[index]);
                h.setText(R.id.collect_member, entity.getTouser());
                h.setText(R.id.pay_member, entity.getUsername());

            }
        };
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        list_view.setLayoutManager(layoutManager);
        list_view.setAdapter(adapter);

    }

    String title = "一起记";
    String text = LoginConfig.group_context;
    int imageid = R.drawable.icon;
    private SHARE_MEDIA[] share_medias = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.share_button:// 分享
                HashMap<String, String> hashMapv = XzbUtils.mapParmas();
                String[] valusa = new String[]{hashMapv.get("tokenid"), hashMapv.get("plat"), hashMapv.get("deviceid"),
                        hashMapv.get("appver"), hashMapv.get("osver"), hashMapv.get("machine"), id};
                String[] paramsa = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id"};

                String signb = StringUtils.setSign(valusa, paramsa);
                String url = Constants.BASE_URL + Constants.COST + "?tokenid=" + hashMapv.get("tokenid") + "&deviceid="
                        + hashMapv.get("deviceid") + "&id=" + id + "&sign=" + signb;

//                ShareUtil.setUrl(SettlementActivity.this, url, title, text, SHARE_MEDIA.WEIXIN, imageid);
                text = memberName + "分享了" + "<<" + bookName + ">>" + text;
                UMImage image = new UMImage(this, R.drawable.icon);// 资源文件
                UMWeb web = new UMWeb(url);
                web.setTitle(title);//标题s
                web.setThumb(image);  //缩略图
                web.setDescription(text);
                new ShareAction(this).withMedia(web)
                        .setPlatform(SHARE_MEDIA.WEIXIN).setCallback(UnListenerHelper.getUMShareListener(SettlementActivity.this)).share();
                break;
            case R.id.settlement_button:// 结算
                Intent intent = new Intent(this, GroupSettlementEndActivity.class);
                startActivityForResult(intent, 1000);

                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        UMShareAPI.get(this).onActivityResult(arg0, arg1, arg2);
        if (arg2 == null) {
            return;
        }
        if (arg0 == 1000) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", id);
            CommonFacade.getInstance().exec(Constants.BOOK_SETTLEMENT_LIST, hashMap, new ViewCallBack() {
                @Override
                public void onStart() {
                    super.onStart();
                    showLoadingDialog();
                }

                @Override
                public void onSuccess(Object o) throws Exception {
                    super.onSuccess(o);
                    View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_settlement, null);
                    LinearLayout layou_settlemnt = (LinearLayout) view.findViewById(R.id.layou_settlemnt);
                    MyRecyclerView recyclerView = (MyRecyclerView) view.findViewById(R.id.listView);
                    FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(mContext);
                    float screeHeight = XzbUtils.getPhoneScreen(SettlementActivity.this).heightPixels;
                    float height = 0;
                    height = booksDbMemberInfos.size() * UIHelper.Dp2Px(mContext, 60) + UIHelper.Dp2Px(mContext, 40);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layou_settlemnt.getLayoutParams();

                    if (height > (screeHeight / 2)) {
                        height = screeHeight / 2;
                    }
                    layoutParams.height = (int) height;
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.bottomMargin = UIHelper.Dp2Px(mContext, 10);
                    layoutParams.rightMargin = UIHelper.Dp2Px(mContext, 10);
                    layoutParams.leftMargin = UIHelper.Dp2Px(mContext, 10);
                    layou_settlemnt.setLayoutParams(layoutParams);
                    recyclerView.setLayoutManager(fullyLinearLayoutManager);
                    CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter = new CommonRecyclerViewAdapter<BooksSettlementItemInfo>(
                            mContext, booksDbMemberInfos) {

                        @Override
                        public int getLayoutViewId(int viewType) {
                            // TODO Auto-generated method stub
                            return R.layout.dailog_settlement_item;
                        }

                        @Override
                        public void convert(CommonRecyclerViewHolder h, BooksSettlementItemInfo entity, int position) {

                            if (entity.getRichman().equals("1")) {// 财主
                                h.setText(R.id.pay_tex, "收取:");
                                h.setTextColor(R.id.pay_tex, context.getResources().getColor(R.color.income));
                                double balance = (double) (Double.parseDouble(entity.getReceivable()) == 0 ? 0.00f : Double
                                        .parseDouble(entity.getReceivable()));
                                h.setText(R.id.bablance, StringUtils.moneySplitComma(XzbUtils.formatDouble("%.2f", balance)));
                            } else {
                                h.setText(R.id.pay_tex, "支付:");
                                h.setTextColor(R.id.pay_tex, context.getResources().getColor(R.color.expenditure));
                                double balance = (double) (Double.parseDouble(entity.getPayamount()) == 0 ? 0.00f : Double
                                        .parseDouble(entity.getPayamount()));
                                h.setText(R.id.bablance, StringUtils.moneySplitComma(XzbUtils.formatDouble("%.2f", balance)));

                            }
                            h.setText(R.id.userName, entity.getUsername());

                        }
                    };
                    recyclerView.setAdapter(adapter);
                    showSimpleAlertDialog(null, view, "确定", "取消",
                            true, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    compleSettle();
                                }
                            },
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            });
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
    }


    /**
     * 完成结算
     */
    private void compleSettle() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("id", id);
        hashMap.put("memberid", memberid);
        CommonFacade.getInstance().exec(Constants.BOOK_CLEAR, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                settlement_button.setVisibility(View.GONE);
                ToastUtils.DiyToast(SettlementActivity.this, "您已完成结算");
                EventBus.getDefault().post("updatebill");
                finish();
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

}
