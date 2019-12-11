package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.CountMoney;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementItemInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.MemberlistParamas;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SingeSettlementActivity extends BaseActivity implements OnClickListener {
    private TextView money, change_settlement_member, need_pay_collect;
    private MyRecyclerView list_view;
    private View not_data;
    private TextView comit;
    private CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter;
    private List<BooksSettlementItemInfo> booksDbMemberInfos;
    private List<BooksSettlementItemInfo> booksDbMemberInfos_pay;
    private String id;
    private BooksDbMemberInfo booksDbMemberInfo;
    List<MemberlistParamas> memberlist;
    private String memberid;
    private ArrayList<String> booleans = new ArrayList<String>();
    private TextView moeny_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singe_settlement);
        id = getIntent().getStringExtra("id");
        memberid = getIntent().getStringExtra("memberid");
        initView();
        initKeyboard();
        initTitle();
        initAdapter();
        initListener();

        initInternetData();

    }

    private void initView() {
        list_view = (MyRecyclerView) findViewById(R.id.list_view);
        money = (TextView) findViewById(R.id.money);
        comit = (TextView) findViewById(R.id.comit);
        need_pay_collect = (TextView) findViewById(R.id.need_pay_collect);

        change_settlement_member = (TextView) findViewById(R.id.change_settlement_member);
        not_data = (View) findViewById(R.id.not_data);

    }

    private void initTitle() {
        initTitle("个人结算");
    }

    private void initListener() {
        comit.setOnClickListener(this);
        change_settlement_member.setOnClickListener(this);

    }

    private void initAdapter() {
        booksDbMemberInfos = new ArrayList<BooksSettlementItemInfo>();
        booksDbMemberInfos_pay = new ArrayList<BooksSettlementItemInfo>();
        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(this);
        list_view.setLayoutManager(fullyLinearLayoutManager);
        adapter = new CommonRecyclerViewAdapter<BooksSettlementItemInfo>(SingeSettlementActivity.this,
                booksDbMemberInfos) {

            @Override
            public int getLayoutViewId(int viewType) {
                // TODO Auto-generated method stub
                return R.layout.activity_singe_settlement_item;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, BooksSettlementItemInfo entity, int position) {

                if (entity.getRichman().equals("1")) {// 财主
                    h.setText(R.id.text, "收取");
                    h.setTextColor(R.id.text, getResources().getColor(R.color.income));
                    double balance = (double) (Double.parseDouble(entity.getReceivable()) == 0 ? 0.00 : Double
                            .parseDouble(entity.getReceivable()));
                    h.setText(R.id.money, XzbUtils.formatDouble("%.2f", balance));

                } else {
                    h.setText(R.id.text, "支付");
                    h.setTextColor(R.id.text, getResources().getColor(R.color.expenditure));
                    double balance = (double) (Double.parseDouble(entity.getPayamount()) == 0 ? 0.00 : Double
                            .parseDouble(entity.getPayamount()));
                    h.setText(R.id.money, XzbUtils.formatDouble("%.2f", balance));

                }
                int index = Integer.parseInt(entity.getMemberid()) % 10;
                String image_url = entity.getTouserid();
                if (TextUtils.isEmpty(image_url)) {
                    image_url = "drawable://" + RequsterTag.head_image[index];
                }

                h.setImage(R.id.head_image, image_url, RequsterTag.head_image[index]);
                h.setText(R.id.userName, entity.getTouser());
                h.setOnClick(R.id.rela, SingeSettlementActivity.this, position);

            }

        };
        list_view.setAdapter(adapter);

    }

    private void initInternetData() {
        memberlist = new ArrayList<MemberlistParamas>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("memberid", memberid);
        CommonFacade.getInstance().exec(Constants.BOOK_CLEAR_DETAIL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                BooksSettlementListInfo booksSettlementListInfo = GsonUtil.GsonToBean(o.toString(), BooksSettlementListInfo.class);
                if (booksDbMemberInfos != null && booksDbMemberInfos.size() > 0) {
                    booksDbMemberInfos.clear();
                }
                List<BooksSettlementItemInfo> booksSettlementItemInfos = booksSettlementListInfo.getData();
                if (booksSettlementItemInfos == null) {
                    return;
                }
                booleans.clear();
                booksDbMemberInfos_pay.clear();
                memberlist.clear();
                double total = 0.00;
                double total_receivable = 0.00;
                for (int i = 0; i < booksSettlementItemInfos.size(); i++) {
                    double payamount = XzbUtils.setTwoDecimalFormat("#.00",
                            Double.parseDouble(booksSettlementItemInfos.get(i).getPayamount()));
                    double receivable = XzbUtils.setTwoDecimalFormat("#.00",
                            Double.parseDouble(booksSettlementItemInfos.get(i).getReceivable()));

                    total += payamount;
                    total_receivable += receivable;

                    booksDbMemberInfos.add(booksSettlementItemInfos.get(i));
                    booleans.add("true");
                    MemberlistParamas memberlistParamas = new MemberlistParamas();
                    String type = "";
                    double comit_payamount = 0.00;
                    if (payamount > 0) {// 付款
                        type = "1";
                        comit_payamount = payamount;
                    } else if (receivable > 0) {
                        type = "0";
                        comit_payamount = receivable;
                    }
                    booksDbMemberInfos_pay.add(booksSettlementItemInfos.get(i));

                }
                if (booksDbMemberInfos.size() > 0) {
                    not_data.setVisibility(View.GONE);
                } else {
                    not_data.setVisibility(View.VISIBLE);
                }
                if (total > 0) {// 我是成员分摊人
                    need_pay_collect.setBackgroundResource(R.drawable.need_pay_bagroud_green);
                    money.setText(XzbUtils.formatDouble("%.2f", total));
                    need_pay_collect.setText("我应支付");

                } else {// 我是付款人
                    need_pay_collect.setBackgroundResource(R.drawable.need_pay_bagroun_red);
                    money.setText(XzbUtils.formatDouble("%.2f", total_receivable));
                    need_pay_collect.setText("我应收取");
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
                dismissDialog();
            }
        });

    }

    private int position = -1;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.comit:
                intent = new Intent(this, GroupSettlementEndActivity.class);
                startActivityForResult(intent, 1000);

                break;

            case R.id.change_settlement_member:
                intent = new Intent(this, ChangeSettlementMemberActivity.class);
                intent.putExtra("id", id);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) booksDbMemberInfos_pay);
                intent.putStringArrayListExtra("booleans", booleans);
                intent.putExtras(bundle);
                startActivityForResult(intent, 11);

                break;
            case R.id.rela:// 修改
                position = Integer.parseInt(v.getTag().toString());
                RelativeLayout layout = (RelativeLayout) v;
                moeny_text = (TextView) layout.findViewById(R.id.money);
                showKeyboard();
                break;
            case R.id.ll_gone_keyboard_many_people:
                // 关闭软件盘

                String mMoney = moeny_text.getText().toString();
                onPlusMoney(mMoney);
                hideKeyboard();
                BooksSettlementItemInfo booksSettlementItemInfo = booksDbMemberInfos.get(position);
                String money_s = moeny_text.getText().toString();
                booksSettlementItemInfo.setPayamount(money_s);
                booksDbMemberInfos.set(position, booksSettlementItemInfo);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.tv_keyboard_spot:
                onTextChange(KEY_SPOT);
                break;
            case R.id.tv_keyboard_zero:
                onTextChange("0");
                break;
            case R.id.tv_keyboard_one:
                onTextChange("1");
                break;
            case R.id.tv_keyboard_tow:
                onTextChange("2");
                break;
            case R.id.tv_keyboard_three:
                onTextChange("3");
                break;
            case R.id.tv_keyboard_four:
                onTextChange("4");
                break;
            case R.id.tv_keyboard_five:
                onTextChange("5");
                break;
            case R.id.tv_keyboard_sex:
                onTextChange("6");
                break;
            case R.id.tv_keyboard_seven:
                onTextChange("7");
                break;
            case R.id.tv_keyboard_eight:
                onTextChange("8");
                break;
            case R.id.tv_keyboard_nine:
                onTextChange("9");
                break;
            case R.id.iv_keyboard_plus: // 加

                onTextChange(KEY_PLUS);
                break;
            case R.id.iv_keyboard_delete:
                onTextChange(KEY_DELETE);
                break;

            case R.id.tv_keyboard_complete:
                onTextChange(KEY_COMPLETE);
                break;
            case R.id.quit_save:
                onTextChange(KEY_QUIT_SAVE);
                break;
            case R.id.tv_keyboard_equal:
                onTextChange(KEY_EQUAL);

                break;
            default:
                break;
        }
    }

    private void onTextChange(String changData) {
        onTextChange(changData, moeny_text);
    }

    @SuppressWarnings("unused")
    private void onTextChange(String changData, TextView editMoneyTextView) {

        String mMoney = editMoneyTextView.getText().toString();

        // 最后一个字符
        String cutMoney = mMoney.substring(mMoney.length() - 1);
        if (changData.equals(KEY_EQUAL)) {

            // 如果里面有加号或者减号则是计算值,否则关闭
            if (mMoney.contains(KEY_REDUCE) || mMoney.contains(KEY_PLUS)) {
                String money = onSumMoney(mMoney, editMoneyTextView);
                editMoneyTextView.setText(money);
            } else {
                // 避免删除一个数后就关闭
                DecimalFormat df = new DecimalFormat("0.00");
                String a = df.format(Double.valueOf(mMoney));
                editMoneyTextView.setText(a);
            }
            return;
        } else if (changData.equals(KEY_REDUCE)) {// 减
            // 如果最后一个是“.”则删除最后一个
            String deleteSpotMoney = XzbUtils.deleteSpot(editMoneyTextView, KEY_SPOT);
            String deleteSpotCutMoney = deleteSpotMoney.substring(deleteSpotMoney.length() - 1);
            // 最后一个是"-"号不做操作
            if (deleteSpotCutMoney.equals(KEY_REDUCE)) {
                return;
            }
            // 如果最后一个是+号,则加号改-号
            if (deleteSpotCutMoney.equals(KEY_PLUS)) {
                editMoneyTextView.setText(deleteSpotMoney.substring(0, deleteSpotMoney.length() - 1) + KEY_REDUCE);
            } else {
                editMoneyTextView.setText(deleteSpotMoney + KEY_REDUCE);
            }

        } else if (changData.equals(KEY_PLUS)) {// 加
            // 如果最后一个是点则删除最后一个
            String deleteSpotMoney = XzbUtils.deleteSpot(editMoneyTextView, KEY_SPOT);
            String deleteSpotCutMoney = deleteSpotMoney.substring(deleteSpotMoney.length() - 1);

            // 最后一个是"+"号不做操作
            if (deleteSpotCutMoney.equals(KEY_PLUS)) {
                return;
            }
            // 如果最后一个是-号,则加号改+号
            if (deleteSpotCutMoney.equals(KEY_REDUCE)) {

                editMoneyTextView.setText(deleteSpotMoney.substring(0, deleteSpotMoney.length() - 1) + KEY_PLUS);
            } else {
                editMoneyTextView.setText(deleteSpotMoney + KEY_PLUS);
            }

        } else if (changData.equals(KEY_DELETE)) {
            if (mMoney.length() == 1) {
                editMoneyTextView.setText(0 + "");

            } else if (XzbUtils.equalsIsZero(editMoneyTextView)) {
                XzbUtils.cleanTextToZero(editMoneyTextView);
            } else {
                editMoneyTextView.setText(mMoney.substring(0, mMoney.length() - 1));
            }

        } else if (changData.equals(KEY_COMPLETE)) {// 确定
            mMoney = onPlusMoney(mMoney);
            editMoneyTextView.setText(mMoney);
            hideKeyboard();

            BooksSettlementItemInfo booksSettlementItemInfo = booksDbMemberInfos.get(position);
            String money_s = editMoneyTextView.getText().toString();
            if (booksSettlementItemInfo.getRichman().equals("0")) {//付款人
                booksSettlementItemInfo.setPayamount(money_s);
            } else {
                booksSettlementItemInfo.setReceivable(money_s);
            }


            booksDbMemberInfos.set(position, booksSettlementItemInfo);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        } else if (changData.equals(KEY_SPOT)) {// 输入点
            String cleanData;
            // 用于刚开始是0.00的时候，然后走最下面的追加 “.”
            cleanData = XzbUtils.cleanTextToZero(editMoneyTextView);
            // 不让输入点的条件
            // 1、 最后是点 不让输入
            if (cutMoney.equals(KEY_SPOT)) {
                XzbUtils.shakeTextView(editMoneyTextView);
                return;
            }
            // 2、倒数第二位是点、后面判断加减 处理这样情况 633.22+603.2+
            if (cleanData.length() >= 3
                    && cleanData.substring(cleanData.length() - 2, cleanData.length() - 1).equals(KEY_SPOT)) {
                XzbUtils.shakeTextView(editMoneyTextView);
                return;
            }
            // 3、倒数第三位是点
            if (cleanData.length() >= 4
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && !cutMoney.equals(KEY_REDUCE) && !cutMoney.equals(KEY_PLUS)) {
                XzbUtils.shakeTextView(editMoneyTextView);
                return;
            }
            // 最后是加减号 输入点，则在前面加0
            if (cutMoney.equals(KEY_REDUCE) || cutMoney.equals(KEY_PLUS)) {
                cleanData = XzbUtils.addTextToZero(editMoneyTextView);
            }
            editMoneyTextView.setText(cleanData + changData);

        } else {
            String cleanData = XzbUtils.cleanTextView(editMoneyTextView);
            if (cleanData.length() >= 4
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && !cutMoney.equals(KEY_REDUCE) && !cutMoney.equals(KEY_PLUS)) {
                XzbUtils.shakeTextView(editMoneyTextView);
                return;
            }
            editMoneyTextView.setText(cleanData + changData);

        }

    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        int visibility = keyboard_view.getVisibility();
        if (visibility == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
            anim.setDuration(500);
            keyboard_view.setVisibility(View.GONE);
            keyboard_view.startAnimation(anim);
        }

    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        int visibility = keyboard_view.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            anim.setDuration(500);
            keyboard_view.setVisibility(View.VISIBLE);
            keyboard_view.startAnimation(anim);

        }

    }

    private View keyboard_view;
    /**
     * 初始化软件盘
     */
    /**
     * 初始化软件盘
     */
    private ImageView iv_keyboard_plus;// 加
    private ImageView iv_keyboard_delete;
    private TextView tv_keyboard_complete;

    private void initKeyboard() {
        keyboard_view = findViewById(R.id.keyboard_view);

        keyboard_view.findViewById(R.id.tv_keyboard_spot).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_zero).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_one).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_tow).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_three).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_four).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_five).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_sex).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_seven).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_eight).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_nine).setOnClickListener(this);
        keyboard_view.findViewById(R.id.tv_keyboard_equal).setOnClickListener(this);
        // keyboard_view.findViewById(R.id.quit_save).setOnClickListener(this);
        keyboard_view.findViewById(R.id.ll_gone_keyboard_many_people).setOnClickListener(this);

        iv_keyboard_plus = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_plus);
        iv_keyboard_delete = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_delete);
        tv_keyboard_complete = (TextView) keyboard_view.findViewById(R.id.tv_keyboard_complete);

        iv_keyboard_plus.setOnClickListener(this);
        iv_keyboard_delete.setOnClickListener(this);
        tv_keyboard_complete.setOnClickListener(this);
    }

    private static String KEY_SPOT = ".";
    private static String KEY_REDUCE = "-";
    private static String KEY_PLUS = "+";
    private static String KEY_DELETE = "D";
    private static String KEY_COMPLETE = "C";
    private static String KEY_QUIT_SAVE = "quit";
    private static String KEY_EQUAL = "equal";


    // 点击确定计算
    private String onSumMoney(String money, TextView editMoneyTextView) {
        // 如果最后一个是加减则删除,避免输入一个符号就点确定
        // 最后一个字符
        String geData;
        String cutdata = money.substring(money.length() - 1);
        if (cutdata.equals(KEY_REDUCE) || cutdata.equals(KEY_PLUS)) {
            geData = editMoneyTextView.getText().toString() + "0.00";
        } else {
            geData = editMoneyTextView.getText().toString();
        }
        ArrayList result = CountMoney.getStringList(geData); // String转换为List
        result = CountMoney.getPostOrder(result); // 中缀变后缀
        Double i = CountMoney.calculate(result);

        DecimalFormat df = new DecimalFormat("0.00");
        String datas = df.format(i) + "";
        // 如果值为负数则至为0;
        char a = datas.charAt(0);
        if (a == '-') {
            datas = "0.00";
        }
        return datas;
    }

    // 如果输入金额没有计算先计算金额
    private String onPlusMoney(String mMoney) {
        // 点击确定和点击闪存入口是一样的判断
        String money = mMoney;
        if (mMoney.contains(KEY_REDUCE) || mMoney.contains(KEY_PLUS)) {
            money = onSumMoney(mMoney, moeny_text);
            moeny_text.setText(money);
            if (money.equals("0.00")) {
                XzbUtils.shakeTextView(moeny_text);
                return money;
            }
        } else {
            // 避免删除一个数后就关闭
            DecimalFormat df = new DecimalFormat("0.00");
            money = df.format(Double.valueOf(mMoney));
            moeny_text.setText(money);
            if (money.equals("0.00")) {
                XzbUtils.shakeTextView(moeny_text);
                return money;
            }
        }
        return money;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 == null) {
            return;
        }
        switch (arg0) {
            case 1000://
                if (TextUtils.isEmpty(money.getText().toString())) {
                    return;
                }
                double money_float = Double.parseDouble(money.getText().toString());
                double money_float_total = 0.00;
                for (BooksSettlementItemInfo booksSettlementItemInfo : booksDbMemberInfos) {
                    money_float_total += Double.parseDouble(booksSettlementItemInfo.getPayamount());

                }
                if (money_float_total == 0) {
                    for (BooksSettlementItemInfo booksSettlementItemInfo : booksDbMemberInfos) {
                        money_float_total += Double.parseDouble(booksSettlementItemInfo.getReceivable());

                    }
                }
                if (money_float != money_float_total) {
                    ToastUtils.DiyToast(this, "需付金额与实际金额不符,无法完成结算");
                    return;
                }
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
                        BooksSettlementListInfo booksSettlementListInfo = GsonUtil.GsonToBean(o.toString(), BooksSettlementListInfo.class);
                        String type = "";
                        double payamount = 0.00;
                        double receivable = 0.00;
                        String comit_payamount = "0.00";
                        for (BooksSettlementItemInfo booksSettlementItemInfo : booksSettlementListInfo
                                .getData()) {
                            MemberlistParamas memberlistParamas = new MemberlistParamas();
                            payamount = Double.parseDouble(booksSettlementItemInfo.getPayamount());
                            receivable = Double.parseDouble(booksSettlementItemInfo.getReceivable());
                            if (payamount > 0) {// 付款
                                type = "1";
                                comit_payamount = booksSettlementItemInfo.getPayamount();
                            } else if (receivable > 0) {
                                type = "0";
                                comit_payamount = booksSettlementItemInfo.getReceivable();
                            }
                            memberlistParamas.setAmount(XzbUtils.formatDouble("%.2f",
                                    Double.parseDouble(comit_payamount)));
                            memberlistParamas.setMemberid(booksSettlementItemInfo.getMemberid());
                            memberlistParamas.setType(type);
                            memberlist.add(memberlistParamas);

                        }
                        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_settlement, null);
                        LinearLayout layou_settlemnt = (LinearLayout) view.findViewById(R.id.layou_settlemnt);
                        MyRecyclerView recyclerView = (MyRecyclerView) view.findViewById(R.id.listView);
                        FullyLinearLayoutManager fullyLinearLayoutManager = new FullyLinearLayoutManager(mContext);
                        float screeHeight = XzbUtils.getPhoneScreen(SingeSettlementActivity.this).heightPixels;
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
                                    double balance = (double) (Double.parseDouble(entity.getReceivable()) == 0 ? 0.00 : Double
                                            .parseDouble(entity.getReceivable()));
                                    h.setText(R.id.bablance, XzbUtils.formatDouble("%.2f", balance));
                                } else {
                                    h.setText(R.id.pay_tex, "支付:");
                                    h.setTextColor(R.id.pay_tex, context.getResources().getColor(R.color.expenditure));
                                    double balance = (double) (Double.parseDouble(entity.getPayamount()) == 0 ? 0.00f : Double
                                            .parseDouble(entity.getPayamount()));
                                    h.setText(R.id.bablance, XzbUtils.formatDouble("%.2f", balance));

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

                break;
            case 11:
                List<String> boo = arg2.getStringArrayListExtra("booleans");
                if (boo == null) {
                    return;
                }

                booleans.clear();
                booksDbMemberInfos.clear();
                for (int i = 0; i < boo.size(); i++) {
                    booleans.add(boo.get(i));
                    if (boo.get(i).equals("true")) {
                        booksDbMemberInfos.add(booksDbMemberInfos_pay.get(i));
                    }
                }

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }

        // initInternetData();
        // initDate();
    }

    /**
     * 完成结算
     */
    private void compleSettle() {
        Gson gson = new Gson();
        String stringJson = "";
        try {
            stringJson = URLEncoder.encode(gson.toJson(memberlist), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("memberid", memberid);
        map.put("memberlist", stringJson);
        CommonFacade.getInstance().exec(Constants.BOOK_CLEAR, map, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                comit.setClickable(false);
                comit.setText("您已完成结算");
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
