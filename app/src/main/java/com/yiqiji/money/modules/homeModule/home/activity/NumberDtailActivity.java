package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.homeModule.home.adapter.NotesAdapter;
import com.yiqiji.money.modules.homeModule.home.adapter.NumberDtailAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BillInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillListInfo;
import com.yiqiji.money.modules.homeModule.home.homeinterface.NotesItemClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的消费(全员消费)
 */
public class NumberDtailActivity extends BaseActivity implements OnClickListener, NotesItemClick {
    private String title_name;
    private ImageView layout_title_view_right_img_btn;
    private PinnedHeaderListView pinnedListview;
    private TextView total_expese, total_incom;
    private LinearLayout invitation_layout;// 邀请成员
    private LinearLayout layout_expe;//
    private TextView invitation_member, need_text, text_left;
    private View not_data;// 没有数据
    private EditText editText;
    private NumberDtailAdapter adapter;
    private NotesAdapter notesAdapter;
    private List<DailycostEntity> beans;
    private String mAccountbooktitle = "";
    private String memberid = "";// 被邀请人成员id

    private String inviteid = "";// 邀请人成员id
    private String mAccountbookid = "";
    private String userid = "";
    private String myuid = "";
    private String isClear = "0";
    //    private String mAccountbooktype = "0";
    private String bookNameType = "";
    private MyPopuwindows myPopuwindows;
    private Date date;
    private boolean isMy = false;
    private String deviceid = "";
    private String deviceid_member = "";
    private String member;// 全员消费
    private boolean isCanDele = true;// 是否可以删除账本true可以false不能
    private boolean isMySef = false;// 是否是我自己false不是
    private String setbugt_money = "";

    private int screeWith;
    private String[] shareText = new String[]{"微信", "新浪微博", "QQ"};
    private int share_icon[] = new int[]{R.drawable.chart_icon, R.drawable.weibo_icon, R.drawable.qq};
    private String type[] = new String[]{"wx", "wb", "qq"};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ};
    private String typeString = "";
    private String mMemberName = "";
    private boolean isBookFount;//本人是否是账本创建者
    private BooksDbInfo booksDbInfo;

    public static void startActivity(Context mContext, String mAccountbookid, String memberid, String userid,
                                     String myuid, String user_name, String member) {

        Intent intent = new Intent(mContext, NumberDtailActivity.class);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("memberid", memberid);
        intent.putExtra("userid", userid);
        intent.putExtra("myuid", myuid);
        intent.putExtra("user_name", user_name);
        if (!TextUtils.isEmpty(member)) {
            intent.putExtra("member", member);
        }
        mContext.startActivity(intent);
    }

    public static void startActivity(Context mContext, String mAccountbookid, String memberid, String userid,
                                     String myuid, String mSorttype, String isClear, String user_name, String member, Date date) {

        Intent intent = new Intent(mContext, NumberDtailActivity.class);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("memberid", memberid);
        intent.putExtra("userid", userid);
        intent.putExtra("myuid", myuid);
        intent.putExtra("mSorttype", mSorttype);
        intent.putExtra("isClear", isClear);
        intent.putExtra("user_name", user_name);
        if (!TextUtils.isEmpty(member)) {
            intent.putExtra("member", member);
        }

        if (mSorttype.equals("1") && isClear.equals("0")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("date", (Serializable) date);
            intent.putExtras(intent);
        }

        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_detail);
        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        try {

            deviceid = LoginConfig.getInstance().getDeviceid();
            date = (Date) getIntent().getExtras().getSerializable("date");
//            if (date == null) {
//                date = new Date();
//            }
            mAccountbookid = getIntent().getStringExtra("mAccountbookid");
            memberid = getIntent().getStringExtra("memberid");
            userid = getIntent().getStringExtra("userid");
            myuid = getIntent().getStringExtra("myuid");
            title_name = getIntent().getStringExtra("user_name") + "的账单";
            beans = new ArrayList<DailycostEntity>();
            member = getIntent().getStringExtra("member");
            setbugt_money = LoginConfig.getInstance().getBudget(mAccountbookid);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        initView();
        initInternetDate();
        seachBookInfo();
        DownUrlUtil.initBooksDataAndMember(mAccountbookid, handler, 0);
    }

    private void seachBookInfo() {

        DownUrlUtil.serchBookList(null, null, null, null, null, null, handler, RequsterTag.BOOKSDBINFOS);

    }

    BooksDbInfo dbInfo;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case RequsterTag.BOOKSDBINFO:
                    List<BooksDbInfo> booksDbInfo = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfo.size() > 0) {
                        dbInfo = booksDbInfo.get(0);
                        Intent intent = new Intent(NumberDtailActivity.this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("BooksDbInfo", dbInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }

                    break;
                case RequsterTag.BOOKSDBINFOS:
                    List<BooksDbInfo> booksDbInfos = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfos.size() < 2) {
                        isCanDele = false;
                    }
                    break;
                case RequsterTag.DELETBOOK:
                    String orderBy = DailycostContract.DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
                    String limit = "1";
                    DownUrlUtil.serchBookList(null, null, null, null, orderBy, limit, handler, RequsterTag.BOOKSDBINFO);

                    break;
                case 0:
                    BooksDbInfo booksDb = (BooksDbInfo) msg.obj;
                    if (booksDb != null) {
                        NumberDtailActivity.this.booksDbInfo = booksDb;
                        mMemberName = getMySelfMemeberName(booksDb.getMyuid(), booksDb.getMember());
                        isBookFount = isBookFount(booksDb.getUserid(), booksDb.getDeviceid());
                        isClear = booksDb.getIsclear();
                    }

                    break;

                default:
                    break;
            }
        }

    };

    private void initView() {
        layout_title_view_right_img_btn = (ImageView) findViewById(R.id.layout_title_view_right_img_btn);
        pinnedListview = (PinnedHeaderListView) findViewById(R.id.pinnedListview);
        total_expese = (TextView) findViewById(R.id.total_expese);
        total_incom = (TextView) findViewById(R.id.total_incom);
        need_text = (TextView) findViewById(R.id.need_text);
        text_left = (TextView) findViewById(R.id.text_left);
        invitation_member = (TextView) findViewById(R.id.invitation_member);
        invitation_layout = (LinearLayout) findViewById(R.id.invitation_layout);
        layout_expe = (LinearLayout) findViewById(R.id.layout_expe);
        not_data = (View) findViewById(R.id.not_data);

        setRightImgBtnPading(UIHelper.Dp2Px(this, 20), UIHelper.Dp2Px(this, 10), UIHelper.Dp2Px(this, 10), UIHelper.Dp2Px(this, 10));
        invitation_layout.setOnClickListener(this);
        // if (userid.equals("0")) {
        invitation_layout.setVisibility(View.VISIBLE);
    }

    private void initAdapter() {
        adapter = new NumberDtailAdapter(this, beans, myuid, userid, mAccountbooktitle, memberid, bookNameType,
                mAccountbookid, isClear);
        adapter.setMember(member);
        pinnedListview.setAdapter(adapter);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }


    }

    private void initInternetDate() {
        long start_time = 0;
        long end_time = 0;
        String start_time_String = "";
        String end_time_String = "";
        if (date != null) {
            start_time = DateUtil.getStartTime(date);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.MONTH, 1);
            end_time = DateUtil.getEndTime(date);// 结束时间
            start_time_String = start_time + "";
            end_time_String = end_time + "";
        } else {
            start_time_String = "";
            end_time_String = "";
        }


        HashMap<String, String> hashMap = StringUtils.getParamas("id", mAccountbookid, "memberid", memberid, "pagetype", "1", "stime", start_time_String, "etime", end_time_String, "page", "1");

        CommonFacade.getInstance().exec(Constants.BOOKS_BILL, hashMap, new ViewCallBack<BillListInfo>() {
            @Override
            public void onSuccess(BillListInfo o) throws Exception {
                super.onSuccess(o);
                BillListInfo billListInfo = o;
                BillInfo billInfo = billListInfo.getData();
                String accountcount = "0";
                if (billInfo == null) {
                    return;
                }
                BooksDbInfo booksDbInfo = billInfo.getBookdetail();
                if (booksDbInfo != null) {
                    mAccountbooktitle = booksDbInfo.getAccountbooktitle();
                    bookNameType = booksDbInfo.getAccountbookcatename();
                    isClear = booksDbInfo.getIsclear();
                    accountcount = booksDbInfo.getAccountbookcount();

                }
                List<DailycostEntity> dailycostEntities = billInfo.getList();
                if (dailycostEntities == null) {
                    return;
                }

                for (int i = 0; i < dailycostEntities.size(); i++) {
                    DailycostEntity dailycostEntity = dailycostEntities.get(i);
                    beans.add(dailycostEntities.get(i));
                    List<BillMemberInfo> billMemberInfos = beans.get(i).getMemberlist();
                    if (billMemberInfos != null) {
                        for (int j = 0; j < billMemberInfos.size(); j++) {
                            BillMemberInfo billMemberInfo = billMemberInfos.get(j);
                            String memberid_other = billMemberInfo.getMemberid();
                            String deviceid_other = billMemberInfo.getDeviceid();
                            if (memberid.equals(memberid_other)) {
                                title_name = billMemberInfo.getUsername() + "的账单";
                                deviceid_member = billMemberInfo.getDeviceid();
//                                userid = billMemberInfo.getUserid();
                            }
                            if (deviceid.equals(deviceid_other)) {
                                inviteid = memberid_other;
                            }
                            if (!TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                                String userid = LoginConfig.getInstance().getUserid();
                                if (userid.equals(billMemberInfo.getUserid())) {
                                    inviteid = memberid_other;
                                }
                            }

                        }
                    }


                }
                initTitle(StringUtils.onHideText(title_name, 8), -1, R.drawable.more_icon, NumberDtailActivity.this);
                unrestrictedTitleLenth(title_name);
                if (!StringUtils.isEmpty(inviteid)) {
                    if (inviteid.equals(memberid)) {
                        if ("0".equals(userid)) {
                            changeTitle("我的账单");
                            invitation_member.setText("您还尚未注册,请立即注册以便账单同步");
                        }
                    }
                }

                if (!"0".equals(userid)) {
                    invitation_member.setVisibility(View.GONE);
                }
                if (bookNameType != null && bookNameType.equals("装修账本")) {
                    need_text.setText("预算金额");
                    if (!TextUtils.isEmpty(setbugt_money)) {
                        total_incom.setText(StringUtils.moneySplitComma(setbugt_money));
                    }
                }
                if (isClear.equals("0")) {
                    text_left.setText("累计支出");
                } else {
                    text_left.setText("累计消费");
                }
                if (!TextUtils.isEmpty(member)) {
                    layout_expe.setVisibility(View.GONE);
                    // memberid = "";
                    changeTitle("全员消费");
                    layout_title_view_right_img_btn.setVisibility(View.GONE);
                }
                if (beans.size() > 0) {
                    not_data.setVisibility(View.GONE);
                } else {
                    not_data.setVisibility(View.VISIBLE);
                }
                double mMyspentdiff = Double.parseDouble(billInfo.getMyspentdiff());
                double mMyincome = Double.parseDouble(billInfo.getMyincome());
                if (bookNameType != null && bookNameType.equals("装修账本")) {
                    need_text.setText("预算金额");
                    need_text.setBackgroundResource(0);
                } else if (isClear.equals("0")) {
                    need_text.setText("累计收入");
                    total_incom.setText(StringUtils.moneySplitComma(Math.abs(mMyincome) + ""));
                } else if (mMyspentdiff < 0) {// 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
                    need_text.setText("需付");
                    need_text.setBackgroundResource(R.drawable.need_pay_bagroun_red);
                    total_incom.setText(StringUtils.moneySplitComma(Math.abs(mMyspentdiff) + ""));
                } else if (mMyspentdiff == 0) {
                    need_text.setText("已结清");
                    total_incom.setText(StringUtils.moneySplitComma(mMyspentdiff + ""));
                    need_text.setBackgroundResource(R.drawable.need_pay_bagroud_green);
                } else if (mMyspentdiff > 0) {
                    need_text.setText("应收");
                    total_incom.setText(StringUtils.moneySplitComma(mMyspentdiff + ""));
                    need_text.setBackgroundResource(R.drawable.need_pay_bagroud_green);
                }

                total_expese.setText(StringUtils.moneySplitComma(billInfo.getMyspent()));
                initAdapter();


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
                not_data.setVisibility(View.VISIBLE);
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
        Intent intent = null;
        switch (v.getId()) {

            case R.id.layout_title_view_return:
                finish();
                break;
            case R.id.layout_title_view_right_img_btn:
                ImageView right_img_btn = (ImageView) findViewById(R.id.layout_title_view_right_img_btn);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_books_note_list, null);
                notesAdapter = new NotesAdapter(this, view, R.layout.activity_books_note_item, right_img_btn);
                notesAdapter.setNotesItemClick(this);
                boolean isMSelf = inviteid.equals(memberid) == true ? true : false;
                if (isMSelf) {
                    isMySef = true;
                } else {
                    isMySef = false;
                }
                notesAdapter.setNotesList(isMSelf, isBookFount, isClear);


//                TextView wanto_settlement = (TextView) view.findViewById(R.id.wanto_settlement);// 修改名片
//                TextView settlement_detail = (TextView) view.findViewById(R.id.settlement_detail);// 我要结算
//                TextView delet_settlement_detail = (TextView) view.findViewById(R.id.delet_settlement_detail);// 退出群组
//                TextView mumbers = (TextView) view.findViewById(R.id.mumbers);//
//                TextView notes = (TextView) view.findViewById(R.id.notes);//
//                View one_line = (View) view.findViewById(R.id.one_line);//
//                View two_line = (View) view.findViewById(R.id.two_line);//
//                View view_three = (View) view.findViewById(R.id.view_three);//
//                mumbers.setVisibility(View.GONE);
//                notes.setVisibility(View.GONE);
//                one_line.setVisibility(View.GONE);
//                two_line.setVisibility(View.GONE);
//                view_three.setVisibility(View.GONE);
////                view_three.setPadding(UIHelper.Dp2Px(NumberDtailActivity.this, 10), 0, UIHelper.Dp2Px(NumberDtailActivity.this, 10), 0);
//                wanto_settlement.setOnClickListener(this);
//                settlement_detail.setOnClickListener(this);
//                delet_settlement_detail.setOnClickListener(this);
//                wanto_settlement.setText("修改名片");//
//                Drawable drawable = getResources().getDrawable(R.drawable.group);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                wanto_settlement.setCompoundDrawables(drawable, null, null, null);
//                if (inviteid.equals(memberid)) {// 自己
//                    isMySef = true;
//                    if (isClear.equals("1")) {// 需要结算
//                        settlement_detail.setText("我要结算");//
//                        delet_settlement_detail.setText("退出群组");//
//                        drawable = getResources().getDrawable(R.drawable.group);
//                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                        delet_settlement_detail.setCompoundDrawables(drawable, null, null, null);
//                        settlement_detail.setVisibility(View.VISIBLE);
//                        if (member != null) {// 全员消费
//                            wanto_settlement.setText("已结算明细");//
//                            drawable = getResources().getDrawable(R.drawable.settlement_detail_picture);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            wanto_settlement.setCompoundDrawables(drawable, null, null, null);
//                            settlement_detail.setText("已删除明细");//
//                            drawable = getResources().getDrawable(R.drawable.delet_picture_detail);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            settlement_detail.setCompoundDrawables(drawable, null, null, null);
//                            delet_settlement_detail.setText("我要结算");//
//                            drawable = getResources().getDrawable(R.drawable.settlement_picture);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            delet_settlement_detail.setCompoundDrawables(drawable, null, null, null);
//                        }
//                        view_three.setVisibility(View.VISIBLE);
//                    } else {
//                        settlement_detail.setVisibility(View.GONE);
//                        delet_settlement_detail.setText("退出群组");//
//                        drawable = getResources().getDrawable(R.drawable.group);
//                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                        delet_settlement_detail.setCompoundDrawables(drawable, null, null, null);
//                    }
//
//                } else {
//                    isMySef = false;
//                    settlement_detail.setVisibility(View.GONE);
//                    delet_settlement_detail.setText("删除成员");//
//                    drawable = getResources().getDrawable(R.drawable.delet_picture_detail);
//                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                    delet_settlement_detail.setCompoundDrawables(drawable, null, null, null);
//
//                }
//                wanto_settlement.setCompoundDrawables(null, null, null, null);
//                settlement_detail.setCompoundDrawables(null, null, null, null);
//                delet_settlement_detail.setCompoundDrawables(null, null, null, null);
//                int[] location = new int[2];
//                right_img_btn.getLocationOnScreen(location);
//                myPopuwindows = new MyPopuwindows(this, view);
//                myPopuwindows.setWithAndHeightList();
//                myPopuwindows.backgroundAlpha(1.0f);
//                myPopuwindows.showAtLocation(right_img_btn, Gravity.NO_GRAVITY,
//                        (int) (location[0] - right_img_btn.getWidth() / 1.6), location[1] + right_img_btn.getHeight() / 2);

                break;
//            case R.id.wanto_settlement:
//                if (member != null) {// 全员消费 已结算明细
//                    intent = new Intent(this, SettledDetailActivity.class);
//                    intent.putExtra("titleName", "已结算明细");
//                    intent.putExtra("id", mAccountbookid);
//                    startActivity(intent);
//                } else {
//                    View view2 = LayoutInflater.from(this).inflate(R.layout.change_member_name, null);
//                    editText = (EditText) view2.findViewById(R.id.editName);
//                    Button change_cancels = (Button) view2.findViewById(R.id.change_cancels);
//                    Button comit_change_name = (Button) view2.findViewById(R.id.comit_change_name);
//                    change_cancels.setText("取消");
//                    comit_change_name.setText("确定");
//
//                    change_cancels.setOnClickListener(this);
//                    comit_change_name.setOnClickListener(this);
//                    DialogUtils.showDialog(this, view2);
//                    XzbUtils.show(editText, this);
//
//                }
//                myPopuwindows.dismiss();
//
//                break;
//            case R.id.settlement_detail:
//                if (member != null) {//
//                    intent = new Intent(this, SettledDetailActivity.class);
//                    intent.putExtra("id", mAccountbookid);
//                    intent.putExtra("titleName", "已删除明细");
//                    startActivity(intent);
//
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(this, SettlementMethodActivity.class);
//                    intent.putExtra("id", mAccountbookid);
//                    intent.putExtra("memberid", inviteid);
//                    startActivity(intent);
//                }
//                myPopuwindows.dismiss();
//
//                break;

            case R.id.invitation_layout:// 邀请成员
                if (userid.equals("0")) {
                    invitation_layout.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                        if (deviceid.equals(deviceid_member)) {
                            IntentUtils.startActivityOnLogin(NumberDtailActivity.this, IntentUtils.LoginIntentType.OTHER);
                            return;

                        }
                    }
                    String title = "“" + mMemberName + "”邀请你加入" + "<<" + mAccountbooktitle + ">>一起记账，快来加入吧！";
                    String text = LoginConfig.invest_context;
                    boolean isNeedTypeString = false;
                    ShareUtil.shareMethInviteid(this, invitation_layout, shareText, share_icon, share_medias, type, title, text, screeWith, isNeedTypeString, mAccountbookid, inviteid, memberid, null);

                }

                break;
//            case R.id.delet_settlement_detail:// 退出
//                myPopuwindows.dismiss();
//                if (member != null) {// 我要结算
//                    intent = new Intent(this, SettlementMethodActivity.class);
//                    intent.putExtra("id", mAccountbookid);
//                    intent.putExtra("memberid", memberid);
//
//                    startActivity(intent);
//                } else {
//                    if (quitOrDelet()) return;
//                }
//
//                myPopuwindows.dismiss();
//                break;
            case R.id.change_cancels:// 取消修改名片
                UIHelper.hideSoftInputMethod(editText);
                DialogUtils.dismissConfirmDialog();
                break;
            case R.id.comit_change_name:// 修改名片确定
                final String memberName = editText.getText().toString();
                if (TextUtils.isEmpty(memberName)) {
                    ToastUtils.DiyToast(this, "请填写成员名片");
                    return;
                }
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    showToast("无网络");
                    return;
                }

                UIHelper.hideSoftInputMethod(editText);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("id", mAccountbookid);
                hashMap.put("memberid", memberid);
                hashMap.put("visitcard", memberName);
                CommonFacade.getInstance().exec(Constants.CHANG_MEMBER_NAME, hashMap, new ViewCallBack() {
                    @Override
                    public void onSuccess(Object o) throws Exception {
                        super.onSuccess(o);
                        AddMemberResponse response = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                        if (isMySef) {//自己
                            LoginConfig.getInstance().setIssetavatar(1);
                        }
                        EventBus.getDefault().post(response);
                        changeTitle(memberName + "的账单");

                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        super.onFailed(simleMsg);
                        showToast(simleMsg);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        showLoadingDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        DialogUtils.dismissConfirmDialog();
                        dismissDialog();
                    }
                });

                break;

            default:
                break;
        }
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
                    DialogUtils.showConfirmOnlyDetermine(NumberDtailActivity.this, "提示",
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

    private void delet(HashMap<String, String> hashMap) {
        CommonFacade.getInstance().exec(Constants.DELET_MEMBER_NAME, hashMap, new ViewCallBack() {
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
                Intent intent = new Intent(NumberDtailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
                    DialogUtils.showConfirmOnlyDetermine(NumberDtailActivity.this, "提示",
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

    private String initUrl(boolean b) {
        String url = null;
        String sign = null;
        long time = new Date().getTime() / 1000;
        HashMap<String, String> hashMap = XzbUtils.mapParmas();

        if (b) {
            String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), mAccountbookid, inviteid,
                    memberid, time + ""};
            String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime"};

            sign = StringUtils.setSign(valus, params);
            // hashMap.put("sign", sign);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + mAccountbookid
                    + "&inviteid=" + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&sign=" + sign;
            url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        } else {
            String[] valusStrings = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), mAccountbookid, inviteid,
                    memberid, time + "", typeString};
            String[] paramsStrings = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime", "type"};
            sign = StringUtils.setSign(valusStrings, paramsStrings);
            // hashMap.put("sign", sign);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + mAccountbookid
                    + "&inviteid=" + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&type=" + typeString + "&sign=" + sign;
        }

        return url;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onClickNotesItem(View view) {
        Intent intent = null;
        int textid = Integer.parseInt(view.getTag().toString());
        switch (textid) {
            case R.string.change_name://修改名片

                View view2 = LayoutInflater.from(this).inflate(R.layout.change_member_name, null);
                editText = (EditText) view2.findViewById(R.id.editName);
                Button change_cancels = (Button) view2.findViewById(R.id.change_cancels);
                Button comit_change_name = (Button) view2.findViewById(R.id.comit_change_name);
                change_cancels.setText("取消");
                comit_change_name.setText("确定");

                change_cancels.setOnClickListener(this);
                comit_change_name.setOnClickListener(this);
                DialogUtils.showDialog(this, view2);
                XzbUtils.show(editText, this);
                break;
            case R.string.goto_seetlement://我要结算
                intent = new Intent(this, SettlementMethodActivity.class);
                intent.putExtra("id", mAccountbookid);
                intent.putExtra("memberid", memberid);

                startActivity(intent);
                break;
            case R.string.quit_group://退出群组
                quitOrDelet(true);

                break;
            case R.string.delet_member://删除成员
                quitOrDelet(false);
                break;
        }
    }

    /**
     * quitOrDelet是否是退出还是删除true：退出 false：删除
     *
     * @return
     */
    private void quitOrDelet(final boolean quitOrDelet) {
        String title = "";
        if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
            showToast("无网络");
            return;
        }

//        if (!isCanDele && quitOrDelet) {
//            showToast("无法退出,至少保留一个账本");
//            return true;
//        }
        if (quitOrDelet) {
            title = "你确定要退出吗？";
        } else {
            title = "你确定要删除吗？";
        }

        DialogUtils.showConfirm(this, "提示", title, "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("id", mAccountbookid);
                hashMap.put("memberid", memberid);

                if (quitOrDelet) {
                    quite(hashMap);
                } else {
                    delet(hashMap);
                }

            }

        }, "取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtils.dismissConfirmDialog();

            }
        });
    }

}
