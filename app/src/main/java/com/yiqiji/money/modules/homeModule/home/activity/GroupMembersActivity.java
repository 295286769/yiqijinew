package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.common.widget.SwipeLayout;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementItemInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementListInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Response;
import retrofit.Retrofit;

public class GroupMembersActivity extends BaseActivity implements OnClickListener, CommonRecyclerViewAdapter.OnRecyclerViewItemLongClickListener, CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener {
    private MyRecyclerView list_view;
    private TextView share_button, settlement_button;
    private View not_data;
    private View bottom_button;
    private CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter;
    private List<BooksSettlementItemInfo> booksSettlementItemInfos;
    private String mAccountbooktitle;
    private String userid = "";
    private List<SwipeLayout> swipeLayouts;
    private ApiService apiService;
    private String isClear, myuid, bookNameType;
    private String memberid = "";
    private BooksDbInfo bookDetailInfo;
    private String mAccountbookid = "";
    private String mAccountbooktype = "";
    private String mSortType = "";
    private int longPosition;
    private boolean isEdit = false;
    private Date date_time;

    public static void startActivity(Context mContext, String mAccountbookid) {
        Intent intent = new Intent(mContext, GroupMembersActivity.class);
        intent.putExtra("mAccountbookid", mAccountbookid);
        mContext.startActivity(intent);
    }

//    public static void startActivity(Context mContext, String mAccountbookid, String mSortType,String isClear,Date date_time) {
//        Intent intent = new Intent(mContext, GroupMembersActivity.class);
//        intent.putExtra("mAccountbookid", mAccountbookid);
//        intent.putExtra("mAccountbookid", date_time);
//        if(mSortType.equals("1")||isClear.equals("0")){}
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("date", (Parcelable) date_time);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        EventBus.getDefault().register(this);
        apiService = RetrofitInstance.get().create(ApiService.class);

        mAccountbookid = getIntent().getStringExtra("mAccountbookid");

        initView();
        initTitle();
        setListener();
        initAdapter();
        getBookDetailinfo();
    }

    private void initView() {
        share_button = (TextView) findViewById(R.id.share_button);
        settlement_button = (TextView) findViewById(R.id.settlement_button);
        list_view = (MyRecyclerView) findViewById(R.id.list_view);
        not_data = (View) findViewById(R.id.not_data);
        bottom_button = (View) findViewById(R.id.bottom_button);


    }

    private void initAdapter() {
        if (booksSettlementItemInfos == null) {
            booksSettlementItemInfos = new ArrayList<BooksSettlementItemInfo>();
        }

        swipeLayouts = new ArrayList<SwipeLayout>();
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        list_view.setLayoutManager(fullyLinearLayoutManager);
        adapter = new CommonRecyclerViewAdapter<BooksSettlementItemInfo>(this, booksSettlementItemInfos) {

            @Override
            public int getLayoutViewId(int viewType) {
                // TODO Auto-generated method stub
                return R.layout.activity_group_members_item;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, BooksSettlementItemInfo entity, int position) {
                boolean isMyseft = memberid.endsWith(entity.getMemberid()) ? true : false;
                if (isEdit) {
                    h.setViewVisibleOrGone(R.id.memer_editlayout, View.VISIBLE);
                    h.setOnClick(R.id.quite, GroupMembersActivity.this, position);
                    h.setOnClick(R.id.edit, GroupMembersActivity.this, position);
                } else {
                    h.setViewVisibleOrGone(R.id.memer_editlayout, View.GONE);
                }
                if (isMyseft) {
                    h.setViewText(R.id.quite, "退出");
                } else {
                    h.setViewText(R.id.quite, "删除");
                }
                if (entity.getUserid().equals("0")) {
                    h.setTextVisible(R.id.islogin_text);
                } else {
                    h.setTextInvisible(R.id.islogin_text);
                }
                if (entity.getRichman().equals("0")) {// 是否是财主，0.否，1.是
                    h.setText(R.id.pay_context, "需支付");
                    h.setText(R.id.pay_money, StringUtils.moneySplitComma(entity.getPayamount() + ""));
                    h.setTextColor(R.id.pay_context, getResources().getColor(R.color.orangered));
                    if (Double.parseDouble(entity.getPayamount()) == 0) {
                        h.setText(R.id.pay_money, "0.00");
                    }
                } else {
                    h.setText(R.id.pay_context, "应收取");
                    h.setText(R.id.pay_money, StringUtils.moneySplitComma(entity.getReceivable() + ""));
                    h.setTextColor(R.id.pay_context, getResources().getColor(R.color.green));
                }
                if (isClear.equals("0")) {// 无需结算
                    h.setText(R.id.pay_context, "");
                    h.setText(R.id.pay_money, "");
                }
                h.setText(R.id.user_name, entity.getUsername());
                h.setText(R.id.money, StringUtils.moneySplitComma(entity.getSpentamount() + ""));
                int index = Integer.parseInt(entity.getMemberid()) % 10;
                String image_url = entity.getUsericon();
                if (TextUtils.isEmpty(image_url)) {
                    image_url = "drawable://" + RequsterTag.head_image[index];
                }

                h.setImage(R.id.head_image, image_url, RequsterTag.head_image[index]);
            }
        };
        adapter.setOnRecyclerViewItemLongClickListener(this);
        adapter.setOnRecyclerViewItemClickListener(this);
        list_view.setAdapter(adapter);
        // adapter.notifyDataSetChanged();
    }

    private void initTitle() {
        initTitle("记账成员", "编辑", this);
        bottomTwoButtonText("添加成员", "我要结算", this);
    }

    private void setListener() {
        share_button.setOnClickListener(this);
        settlement_button.setOnClickListener(this);

    }

    private int position = -1;
    private EditText editText;
    String title = "一起记";
    String text = LoginConfig.group_context;
    int imageid = R.drawable.icon;
    private SHARE_MEDIA[] share_medias = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
            SHARE_MEDIA.QZONE};

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_title_view_right_btn:
                if (!isEdit) {
                    isEdit = true;
                    changeRightBtn("完成");
                    bottom_button.setVisibility(View.GONE);
                } else {
                    changeRightBtn("编辑");
                    isEdit = false;
                    bottom_button.setVisibility(View.VISIBLE);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
//                intent = new Intent(this, ManuallyAddActivity.class);
//                intent.putExtra("inviteid", memberid);
//                intent.putExtra("id", mAccountbookid);
//                startActivity(intent);
                break;
            case R.id.layout_title_view_return:
                finish();
                break;
            case R.id.layout_item:
                int poition = Integer.parseInt(v.getTag().toString());
                String user_name = booksSettlementItemInfos.get(position).getUsername();
                String memberid_other = booksSettlementItemInfos.get(position).getMemberid();
                String userid_other = booksSettlementItemInfos.get(position).getUserid();
                NumberDtailActivity.startActivity(this, mAccountbookid, memberid_other, userid_other, myuid, user_name, "");
//                intent.putExtra("titleName", booksSettlementItemInfos.get(poition).getUsername());
//                intent.putExtra("mAccountbookid", mAccountbookid);
//                intent.putExtra("memberid", booksSettlementItemInfos.get(poition).getMemberid());
//                intent.putExtra("inviteid", memberid);
//                intent.putExtra("myuid", myuid);
//                intent.putExtra("mAccountbooktitle", mAccountbooktitle);
//                intent.putExtra("isClear", isClear);
//                intent.putExtra("bookNameType", bookNameType);
//                intent.putExtra("mAccountbooktype", mAccountbooktype);
//                intent.putExtra("userid", userid);
//                startActivity(intent);
                break;
            case R.id.share_button:// 添加成员
                intent = new Intent(this, ManuallyAddActivity.class);
                intent.putExtra("inviteid", memberid);
                intent.putExtra("id", mAccountbookid);
                startActivity(intent);
//                HashMap<String, String> hashMapv = XzbUtils.mapParmas();
//                String[] valusa = new String[]{hashMapv.get("tokenid"), hashMapv.get("plat"), hashMapv.get("deviceid"),
//                        hashMapv.get("appver"), hashMapv.get("osver"), hashMapv.get("machine"), mAccountbookid};
//                String[] paramsa = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id"};
//
//                String signb = StringUtils.setSign(valusa, paramsa);
//                String url = Constants.BASE_URL + Constants.COST + "?tokenid=" + hashMapv.get("tokenid") + "&deviceid="
//                        + hashMapv.get("deviceid") + "&id=" + mAccountbookid + "&sign=" + signb;
//                String member_name = getMySelfMemeberName(bookDetailInfo.getMyuid(), bookDetailInfo.getMember());
//                text = member_name + "分享了" + "<<" + mAccountbooktitle + ">>" + text;
//
//                String title = "“" + member_name + "”分享了" + "<<" + mAccountbooktitle + ">> 成员的消费详情，点此马上查看。";
//                String text = "参与成员" + bookDetailInfo.getMember().size() + "人,全员共消费¥" + bookDetailInfo.getPayamount();
//                //todo 分享 （群组成员消费）
//                ShareUtil.setUrlList(GroupMembersActivity.this, url, title, text, share_medias, imageid);

                break;
            case R.id.settlement_button:// 结算
                intent = new Intent(this, SettlementMethodActivity.class);
                intent.putExtra("id", mAccountbookid);
                intent.putExtra("memberid", memberid);
                startActivity(intent);

                break;
            case R.id.edit:// 修改名片

                position = Integer.parseInt(v.getTag().toString());
                if (bookDetailInfo != null) {
                    String userid = bookDetailInfo.getUserid();
                    String deviceid = bookDetailInfo.getDeviceid();
                    List<BooksDbMemberInfo> booksDbMemberInfos = bookDetailInfo.getMember();
                    if (booksDbMemberInfos != null) {
                        BooksDbMemberInfo booksDbMemberInfo = booksDbMemberInfos.get(position);
                        boolean isGroup = isGroup(userid, deviceid, booksDbMemberInfo);//能否对账本成员进行操作
                        if (!isGroup) {
                            showSimpleAlertDialog("您暂无权限对账本创建者执行该操作");
                            return;
                        }
                    }
                    View view = LayoutInflater.from(this).inflate(R.layout.change_member_name, null);
                    editText = (EditText) view.findViewById(R.id.editName);
                    Button change_cancels = (Button) view.findViewById(R.id.change_cancels);
                    Button comit_change_name = (Button) view.findViewById(R.id.comit_change_name);
                    change_cancels.setText("取消");
                    comit_change_name.setText("确定");

                    change_cancels.setOnClickListener(this);
                    comit_change_name.setOnClickListener(this);
                    DialogUtils.showDialog(this, view);
                }


                break;

            case R.id.change_cancels:// 取消修改名片
                DialogUtils.dismissConfirmDialog();
                break;
            case R.id.quite:// 退出或删除
                position = Integer.parseInt(v.getTag().toString());
                deletOrQuite(position);
                break;
            case R.id.comit_change_name:// 修改名片确定
                final String memberName = editText.getText().toString();
                if (TextUtils.isEmpty(memberName)) {
                    ToastUtils.DiyToast(this, "请填写成员名片");
                    return;
                }
                if (booksSettlementItemInfos.get(position).getUsername() != null
                        && booksSettlementItemInfos.get(position).getUsername().equals("memberName")) {
                    DialogUtils.dismissConfirmDialog();
                    return;
                }

                HashMap<String, String> hashMap = XzbUtils.mapParmas();
                String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                        hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), mAccountbookid,
                        booksSettlementItemInfos.get(position).getMemberid(), memberName};
                String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine", "id",
                        "memberid", "visitcard"};
                hashMap.put("id", mAccountbookid);
                hashMap.put("memberid", booksSettlementItemInfos.get(position).getMemberid());
                hashMap.put("visitcard", memberName);
                String sign = StringUtils.setSign(valus, params);
                hashMap.put("sign", sign);
                apiService.chang_memeber_name(hashMap).enqueue(
                        new BaseCallBack<AddMemberResponse>(GroupMembersActivity.this, true) {
                            @Override
                            public void onResponse(Response<AddMemberResponse> arg0, Retrofit arg1) {
                                // TODO Auto-generated method stub
                                super.onResponse(arg0, arg1);

//                                changeTitle("");
                                AddMemberResponse response = arg0.body();
                                if (response == null) {
                                    return;
                                }
                                if (response.getCode() == 0) {
                                    DialogUtils.dismissConfirmDialog();
                                    EventBus.getDefault().post(response);
                                    BooksSettlementItemInfo booksSettlementItemInfo = booksSettlementItemInfos
                                            .get(position);
                                    booksSettlementItemInfo.setUsername(memberName);
                                    booksSettlementItemInfos.remove(position);
                                    booksSettlementItemInfos.add(position, booksSettlementItemInfo);
                                    adapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onFailure(Throwable arg0) {
                                // TODO Auto-generated method stub
                                super.onFailure(arg0);

                                DialogUtils.dismissConfirmDialog();
                            }
                        });

                break;
            default:
                break;
        }
    }

    public void deletOrQuite(final int position) {
        String title = "";
        if (StringUtils.isEmpty(memberid)) {
            return;
        }
        String userid = bookDetailInfo.getUserid();
        String deviceid = bookDetailInfo.getDeviceid();
        List<BooksDbMemberInfo> booksDbMemberInfos = bookDetailInfo.getMember();
        if (booksDbMemberInfos != null) {
            BooksDbMemberInfo booksDbMemberInfo = booksDbMemberInfos.get(position);
            boolean isGroup = isGroup(userid, deviceid, booksDbMemberInfo);//能否对账本成员进行操作
            if (!isGroup) {
                showSimpleAlertDialog("您暂无权限对账本创建者执行该操作");
                return;
            }
        }

        if (memberid.equals(booksSettlementItemInfos.get(position).getMemberid())) {
            title = "你确定要退出群组吗？";
        } else {
            title = "你确定要删除吗？";
        }

        DialogUtils.showConfirm(this, "提示", title, "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {

                HashMap<String, String> hashMapa = new HashMap<String, String>();

                hashMapa.put("id", mAccountbookid);
                hashMapa.put("memberid", booksSettlementItemInfos.get(position).getMemberid());
                if (memberid.equals(booksSettlementItemInfos.get(position).getMemberid())) {
                    quite(hashMapa);
                } else {
                    delet(hashMapa);
                }

            }

        }, "取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtils.dismissConfirmDialog();

            }
        });
    }

    private void goSettement(String msg) {
        DialogUtils.dismissConfirmDialog();
        DialogUtils.showConfirmOnlyDetermine(GroupMembersActivity.this, "提示", msg, "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtils.dismissConfirmDialog();

            }
        });
    }

    private void delet(HashMap<String, String> hashMap) {
        CommonFacade.getInstance().exec(Constants.DELET_MEMBER_NAME, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                if (booksSettlementItemInfos.size() > 0) {
                    booksSettlementItemInfos.remove(longPosition);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }

                EventBus.getDefault().post(addMemberResponse);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    showToast(simleMsg);
                } else {
                    DialogUtils.showConfirmOnlyDetermine(GroupMembersActivity.this, "提示",
                            simleMsg.getErrMsg(), "确定", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    DialogUtils.dismissConfirmDialog();

                                }
                            });
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                DialogUtils.dismissConfirmDialog();
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }

    private void quite(HashMap<String, String> hashMap) {
        CommonFacade.getInstance().exec(Constants.QUIT_MEMBER_NAME, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                DialogUtils.dismissConfirmDialog();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                String where = DailycostContract.DtBooksColumns.ACCOUNTBOOKID + "=?";
                String[] selectionArgs = {mAccountbookid};
                DownUrlUtil.deleBook(where, selectionArgs, handler, RequsterTag.DELETBOOK);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    showToast(simleMsg);
                } else {
                    DialogUtils.showConfirmOnlyDetermine(GroupMembersActivity.this, "提示",
                            simleMsg.getErrMsg(), "确定", new OnClickListener() {

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

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case RequsterTag.BOOKSDBINFO:
                    List<BooksDbInfo> booksDbInfo = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfo.size() > 0) {
                        BooksDbInfo dbInfo = booksDbInfo.get(0);
                        Intent intent = new Intent(GroupMembersActivity.this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("BooksDbInfo", dbInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                    break;

                case RequsterTag.DELETBOOK:
                    String orderBy = DailycostContract.DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
                    String limit = "1";
                    DownUrlUtil.serchBookList(null, null, null, null, orderBy, limit, handler, RequsterTag.BOOKSDBINFO);

                    break;

                default:
                    break;
            }
        }

    };

    public void initSettmentList() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", mAccountbookid);
        CommonFacade.getInstance().exec(Constants.BOOK_SETTLEMENT_LIST, hashMap, new ViewCallBack() {

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                BooksSettlementListInfo booksSettlementListInfo = GsonUtil.GsonToBean(o.toString(), BooksSettlementListInfo.class);
                booksSettlementItemInfos.clear();
                List<BooksSettlementItemInfo> data = booksSettlementListInfo.getData();
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        booksSettlementItemInfos.add(booksSettlementListInfo.getData().get(i));
                    }
                }

                if (booksSettlementItemInfos.size() > 0) {
                    not_data.setVisibility(View.GONE);
                } else {
                    not_data.setVisibility(View.VISIBLE);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                settlement_button.setVisibility(View.GONE);
                showToast(simleMsg);
            }

        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void getBookDetailinfo() {

        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountbookid,
                "stime", "", "etime", "");

        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                bookDetailInfo = o;
                bookNameType = bookDetailInfo.getAccountbookcatename();
                mAccountbooktitle = bookDetailInfo.getAccountbooktitle();
                userid = bookDetailInfo.getUserid();
                isClear = bookDetailInfo.getIsclear();
                myuid = bookDetailInfo.getMyuid();
                mAccountbooktype = bookDetailInfo.getAccountbooktype();
                mSortType = bookDetailInfo.getSorttype();
                if (isClear.equals("0")) {
                    settlement_button.setVisibility(View.GONE);
                }
                List<BooksDbMemberInfo> booksDbMemberInfos = bookDetailInfo.getMember();
                memberid = getMySelfNyMemeberId(myuid, booksDbMemberInfos);
//                changeMemberIncomExpexe(booksDbMemberInfos, bookDetailInfo.getDeviceid(), myuid);
                initSettmentList();

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

//    private void changeMemberIncomExpexe(List<BooksDbMemberInfo> booksDbMemberInfos, String deviceid, String myuid) {
//        if (booksDbMemberInfos != null) {
//            for (int i = 0; i < booksDbMemberInfos.size(); i++) {
//                if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
//                    if (deviceid.equals(booksDbMemberInfos.get(i).getDeviceid())) {
//                        memberid = booksDbMemberInfos.get(i).getMemberid();
//                    }
//                } else {
//                    if (myuid.equals(booksDbMemberInfos.get(i).getUserid())) {
//                        memberid = booksDbMemberInfos.get(i).getMemberid();
//                    }
//                }
//
//            }
//
//        }
//
//    }

    public void onEventMainThread(AddMemberResponse addMemberResponse) {// 添加成员刷新界面

        initSettmentList();

    }

    @Override
    public void onItemLongClick(View v, final int position) {
//        longPosition = position;
//        String title = "";
//        if (StringUtils.isEmpty(memberid)) {
//            return;
//        }
//        if (memberid.equals(booksSettlementItemInfos.get(position).getMemberid())) {
//            title = "你确定要退出群组吗？";
//        } else {
//            title = "你确定要删除吗？";
//        }
//
//        DialogUtils.showConfirm(this, "提示", title, "确定", new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                HashMap<String, String> hashMapa = new HashMap<String, String>();
//
//                hashMapa.put("id", mAccountbookid);
//                hashMapa.put("memberid", booksSettlementItemInfos.get(position).getMemberid());
//                if (memberid.equals(booksSettlementItemInfos.get(position).getMemberid())) {
//                    quite(hashMapa);
//                } else {
//                    delet(hashMapa);
//                }
//
//            }
//
//        }, "取消", new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                DialogUtils.dismissConfirmDialog();
//
//            }
//        });
    }


    @Override
    public void onItemClick(View v, int position) {

        String user_name = booksSettlementItemInfos.get(position).getUsername();
        String memberid_other = booksSettlementItemInfos.get(position).getMemberid();
        String userid_other = booksSettlementItemInfos.get(position).getUserid();
        NumberDtailActivity.startActivity(this, mAccountbookid, memberid_other, userid_other, myuid, user_name, "");
//        Intent intent = new Intent(this, NumberDtailActivity.class);
//        intent.putExtra("titleName", booksSettlementItemInfos.get(position).getUsername());
//        intent.putExtra("mAccountbookid", mAccountbookid);
//        intent.putExtra("memberid", booksSettlementItemInfos.get(position).getMemberid());
//        intent.putExtra("inviteid", memberid);
//        intent.putExtra("myuid", myuid);
//        intent.putExtra("mAccountbooktitle", mAccountbooktitle);
//        intent.putExtra("isClear", isClear);
//        intent.putExtra("bookNameType", bookNameType);
//        intent.putExtra("mAccountbooktype", mAccountbooktype);
//        intent.putExtra("userid", booksSettlementItemInfos.get(position).getUserid());
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
