package com.yiqiji.money.modules.myModule.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.PinnedHeaderListView;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.StatisticsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalDetailActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.CustomAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.BillInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillListInfo;
import com.yiqiji.money.modules.myModule.my.entity.AccountBookEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dansakai on 2017/5/22.
 */

public class SubscribAccounterDetail extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_share;
    private ImageView iv_counter;

    private RelativeLayout rl_content;
    private TextView mTvName;
    private TextView mTvTotleMoney;
    private TextView mTvDays;
    private TextView mTvNm;
    private TextView mTvSequence;
    private PinnedHeaderListView mListView;
    private CustomAdapter customAdapter;
    private List<DailycostEntity> list = new ArrayList<>();

    private AccountBookEntity accountBookEntity;

    private String[] shareText = new String[]{"微信", "新浪微博", "QQ"};
    private int share_icon[] = new int[]{R.drawable.chart_icon, R.drawable.weibo_icon, R.drawable.qq};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ};

    private boolean isOrder = true;//排序 true 时间由近至远 false时间由远至近

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscrib_accounter_detail);
        initView();
        loadData();
    }

    /**
     * 加载网络数据
     */
    private void loadData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountBookEntity.getAccountbookid());
        CommonFacade.getInstance().exec(Constants.BOOKS_BILL, hashMap, new ViewCallBack<BillListInfo>() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(BillListInfo billListInfo) throws Exception {
                List<DailycostEntity> templist = new ArrayList<DailycostEntity>();
                BillInfo billInfo = billListInfo.getData();
                if (billInfo == null) {
                    return;
                }
                mTvTotleMoney.setText(billInfo.getMyspent());
                mTvDays.setText("记账天数 " + billInfo.getBilldays() + "天");
                mTvNm.setText("记账笔数 " + billInfo.getBillcount() + "笔");

                List<DailycostEntity> getList = billInfo.getList();
                if (getList == null) {
                    return;
                }
                for (int i = 0; i < getList.size(); i++) {
                    DailycostEntity dailycostEntity = getList.get(i);
                    dailycostEntity.setIssynchronization("true");
                    dailycostEntity.setAccountbooktype(accountBookEntity.getAccountbooktype());
                    dailycostEntity.setIsclear(accountBookEntity.getIsclear());
                    if (dailycostEntity.getBillstatus().equals("0")) {
                        dailycostEntity.setIsdeleted("true");
                    } else {
                        dailycostEntity.setIsdeleted("false");
                    }
                    long time = Long.parseLong(dailycostEntity.getTradetime());
                    Date date_time = new Date(time * 1000);
                    dailycostEntity.setYear(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 5)));
                    dailycostEntity.setMoth(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 3)));
                    dailycostEntity.setDay(Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 2)));
                    List<BillMemberInfo> billMemberInfos = new ArrayList<BillMemberInfo>();
                    List<BillMemberInfo> members = dailycostEntity.getMemberlist();

                    if (members != null && members.size() > 0) {
                        for (int j = 0; j < members.size(); j++) {
                            BillMemberInfo billMemberInfo = members.get(j);
                            billMemberInfo.setBillid(dailycostEntity.getBillid());
                            billMemberInfos.add(billMemberInfo);
                        }
                        dailycostEntity.setMemberlist(billMemberInfos);
                    }
                    templist.add(dailycostEntity);
                }

                list.clear();
                if (!StringUtils.isEmptyList(templist)) {
                    for (int i = 0; i < templist.size(); i++) {
                        if (Integer.parseInt(templist.get(i).getBillstatus()) != 0) {
                            list.add(templist.get(i));
                        }
                    }
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }

    private void initView() {
        accountBookEntity = getIntent().getParcelableExtra("AccountBookEntity");

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(accountBookEntity.getAccountbooktitle());
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        iv_share.setVisibility(View.VISIBLE);
        iv_counter = (ImageView) findViewById(R.id.iv_counter);
        iv_counter.setOnClickListener(this);
        iv_counter.setVisibility(View.GONE);

        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvName.setText("by: " + accountBookEntity.getUsername());
        mTvTotleMoney = (TextView) findViewById(R.id.tv_totle_money);
        mTvDays = (TextView) findViewById(R.id.tv_days);
        mTvNm = (TextView) findViewById(R.id.tv_Nm);
        mTvSequence = (TextView) findViewById(R.id.tv_sequence);
        mTvSequence.setOnClickListener(this);
        mListView = (PinnedHeaderListView) findViewById(R.id.listView);

        View HeaderView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_header, mListView, false);
        mListView.setPinnedHeader(HeaderView);
        customAdapter = new CustomAdapter(mContext, list, this, true);
        mListView.setAdapter(customAdapter);
        mListView.setFocusable(false);
        mListView.setOnScrollListener(customAdapter);
    }

    @Override
    public void onClick(View v) {
        int positio = 0;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.relayout_item://跳转到账单详情
                positio = Integer.parseInt(v.getTag().toString());
                billDetail(positio);
                break;
//            case R.id.roundedImageView://查看大图
//                positio = Integer.parseInt(v.getTag().toString());
//                String urlPath = list.get(positio).getBillimg();
//                Intent intent = new Intent(mContext, PhotosShowActivity.class);
//                intent.putExtra("path", urlPath);
//                intent.putExtra("isNeedDelet", true);
//                startActivity(intent);
//                break;
            case R.id.tv_sequence://排序
                List<DailycostEntity> temlist = new ArrayList<>();
                for (int i = list.size() - 1; i >= 0; i--) {
                    temlist.add(list.get(i));
                }
                list.clear();
                list.addAll(temlist);
                customAdapter.notifyDataSetChanged();
                isOrder = !isOrder;
                if (isOrder) {
                    mTvSequence.setText("时间由近至远");
                } else {
                    mTvSequence.setText("时间由远至近");
                }

                break;
            case R.id.iv_share://分享
                String title = "“" + accountBookEntity.getUsername() + "”分享了他订阅的《" + accountBookEntity.getAccountbooktitle() + "》，好奇他都记了些什么？点此马上好查看。";
                String text = LoginConfig.share_acounter_context;
                ShareUtil.shareMeth(this, rl_content, shareText, share_icon, share_medias, getShareUrl(), title, text, XzbUtils.getPhoneScreen(this).widthPixels, false, null, 0);
                break;
            case R.id.iv_counter://统计

                StatisticsActivity.openActivity(mContext, new Date(), accountBookEntity.getAccountbooktitle(), accountBookEntity.getAccountbookid(), accountBookEntity.getUsername(),
                        accountBookEntity.getSorttype(), accountBookEntity.getAccountbooktitle(), Long.parseLong(accountBookEntity.getFirsttime()), true);
//                Intent intent = new Intent(mContext, StatisticsActivity.class);
//                intent.putExtra("data", new Date());
//                intent.putExtra("bookName", accountBookEntity.getAccountbooktitle());
//                intent.putExtra("bookid", accountBookEntity.getAccountbookid());
//                intent.putExtra("bookUserName", accountBookEntity.getUsername());
//                intent.putExtra("sorttype", accountBookEntity.getSorttype());
//                intent.putExtra("mAccountbookcatename", accountBookEntity.getAccountbooktitle());
//                startActivity(intent);
                break;
            default:
        }

    }

    /**
     * 获取账本分享点击url
     *
     * @return
     */
    private String getShareUrl() {
        long time = new Date().getTime() / 1000;
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String sign = null;
        String url = null;

        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), accountBookEntity.getAccountbookid(), accountBookEntity.getUserid()};
        String[] params = new String[]{"tokenid", "deviceid", "id", "memberid"};

        sign = StringUtils.setSign(valus, params);
        url = Constants.BASE_URL + Constants.ACCOUNTER_SHARE + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                + hashMap.get("deviceid") + "&sign=" + sign + "&id=" + accountBookEntity.getAccountbookid() + "&memberid=" + accountBookEntity.getUserid();
        url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        return url;
    }

    /**
     * 根据多点击的位置跳转到账单详情
     *
     * @param position
     */
    public void billDetail(int position) {
        Intent intent = null;
        Bundle bundle = new Bundle();
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.get(position).getBilltype().equals("5")) {
            intent = new Intent(mContext, WriteJournalDetailActivity.class);
        } else {
            intent = new Intent(mContext, PaymentDetailsActivity.class);
        }
        bundle.putParcelable("list", list.get(position));
        intent.putExtra("mAccountbookid", accountBookEntity.getAccountbookid());
        intent.putExtra("billid", list.get(position).getBillid());
        intent.putExtra("isSubscribe", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // 用于友盟分享
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
