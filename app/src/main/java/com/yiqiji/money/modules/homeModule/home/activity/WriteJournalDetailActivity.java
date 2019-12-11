package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DataBaseUtil;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillSyncInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CommentListItem;
import com.yiqiji.money.modules.homeModule.home.entity.JournalCommentEvent;
import com.yiqiji.money.modules.homeModule.home.perecenter.CommentPerecenter;
import com.yiqiji.money.modules.homeModule.home.wegit.JournalDetailHeadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ${huangweishui} on 2017/5/22.
 * address huang.weishui@71dai.com
 */
public class WriteJournalDetailActivity extends BaseActivity implements View.OnClickListener {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private CommonRecyclerViewAdapter<CommentListItem> commonRecyclerViewAdapter;
    private View no_data;
    private View bottom_button;
    private List<CommentListItem> commentListItems;
    private List<DailycostEntity> dailycostEntities;
    private DailycostEntity dailycostEntity;
    private String billid;
    private String mAccountbookid;
    private String mMemberid = "";
    private String mMyuid = "";
    //    private String accountbooktype = "";
    private String isClear = "";
    private String billcateid = "";
    private float screaWith = 0;

    private boolean isSubscribe = false;
    private JournalDetailHeadView journalDetailHeadView;
    private String mAccountbookcate;

    public static void openActivity(Context context, String mAccountbookid, String billid, boolean isSubscribe) {
        Intent intent = new Intent(context, WriteJournalDetailActivity.class);
        intent.putExtra("mAccountbookid", mAccountbookid);
        intent.putExtra("billid", billid);
        intent.putExtra("isSubscribe", isSubscribe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal_detail);
        EventBus.getDefault().register(this);
        screaWith = XzbUtils.getPhoneScreen(this).widthPixels / 3.5f;
        apiService = RetrofitInstance.get().create(ApiService.class);
        billid = getIntent().getStringExtra("billid");
        mAccountbookid = getIntent().getStringExtra("mAccountbookid");
        isSubscribe = getIntent().getBooleanExtra("isSubscribe", false);

        initView();
        initTitle("日志详情", -1, null);
        bottomTwoButtonText("删除", "修改", this);
        getBookDetailinfo(mAccountbookid);
    }


    private void initView() {
        commentListItems = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        no_data = (View) findViewById(R.id.no_data);
        bottom_button = (View) findViewById(R.id.bottom_button);
        LinearLayoutManager fullyLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(fullyLinearLayoutManager);
        commonRecyclerViewAdapter = new CommonRecyclerViewAdapter<CommentListItem>(this, commentListItems) {
            @Override
            public void convert(CommonRecyclerViewHolder h, CommentListItem entity, int position) {
                h.setText(R.id.user_name, entity.getUsername());
                String time_content = DateUtil.getDateToString(Long.parseLong(entity.getCtime()), "yyyy年MM月dd日");
                h.setText(R.id.time_text, time_content);
                h.setImage(R.id.head_image, entity.getUsericon(), 0);
                h.setText(R.id.content_text, entity.getContent());
            }

            @Override
            public int getLayoutViewId(int viewType) {

                return R.layout.activity_write_journal_commont_item;
            }

        };
        journalDetailHeadView = new JournalDetailHeadView(this);
        commonRecyclerViewAdapter.addHeaderView(journalDetailHeadView, true);
        recyclerView.setAdapter(commonRecyclerViewAdapter);


    }

    public void getBookDetailinfo(final String mAccountid) {
        CommentPerecenter.getBookDetail(isSubscribe, mAccountbookid, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo booksDbI = o;
                mMyuid = booksDbI.getMyuid();
                mMemberid = getMySelfNyMemeberId(mMyuid, booksDbI.getMember());
                mAccountbookcate = booksDbI.getAccountbookcate();
                isClear = booksDbI.getIsclear();
                billcateid = booksDbI.getAccountbookcate();
                getBillDetail();
                initData();

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }
        });


    }

    private void getBillDetail() {
        CommentPerecenter.getBillInfo(billid, new ViewCallBack<DailycostEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(false);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                no_data.setVisibility(View.VISIBLE);
                showToast(simleMsg);
            }

            @Override
            public void onSuccess(DailycostEntity dailycost) throws Exception {
                super.onSuccess(dailycost);
                no_data.setVisibility(View.GONE);
                dailycostEntity = dailycost;
                isOperation = BaserClassMode.isOperation(dailycostEntity, mMyuid);
                journalDetailHeadView.setDailycostEntity(isSubscribe, mAccountbookid, mMemberid, mAccountbookcate, dailycostEntity);
                setMyidButtonVisible();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

    }

    private void setMyidButtonVisible() {
        boolean participate = BaserClassMode.isOperation(dailycostEntity, mMyuid);
        if (!participate) {
            bottomTwoButtonGoneOrVisible(View.GONE);
            bottom_button.setVisibility(View.GONE);
        } else {
            if (!isSubscribe) {
                bottomTwoButtonGoneOrVisible(View.VISIBLE);
                bottom_button.setVisibility(View.VISIBLE);
            }
        }
    }


    private void initData() {
        CommentPerecenter.getComments(mAccountbookid, billid, new ViewCallBack<List<CommentListItem>>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(List<CommentListItem> commens) throws Exception {
                super.onSuccess(commens);
                List<CommentListItem> commentLists = commens;
                if (commentListItems != null) {
                    commentListItems.clear();
                }
                for (int i = 0; i < commentLists.size(); i++) {
                    commentListItems.add(commentLists.get(i));
                }
                journalDetailHeadView.commCountSize(commentListItems.size());
                if (commonRecyclerViewAdapter != null) {
                    commonRecyclerViewAdapter.notifyDataSetChanged();
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

    boolean isOperation = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settlement_button:// 修改
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    showToast("请连接网络");
                    return;
                }
                if (dailycostEntity != null && isOperation) {
                    onStartActivity();
                }


                break;

            case R.id.share_button:// 删除
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    showToast("请连接网络");
                    return;
                }
                DialogUtils.showConfirm(this, "提示", "您确定要删除吗？", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletBill();
                        DialogUtils.dismissConfirmDialog();
                    }
                }, "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.dismissConfirmDialog();
                    }
                });

                break;
        }
    }

    private void deletBill() {
        if (dailycostEntity == null) {
            return;
        }
        dailycostEntity.setIssynchronization("del");
        List<PostBill> dataList = new ArrayList<>();

        if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
            showToast("没有网络");
            return;
        }


        HashMap<String, String> hashMap = StringUtils.getParamas();
//        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
//                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
//        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);


        final PostBill mBillData = DownUrlUtil.getPostBillData(dailycostEntity);
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
                        String billid = dailycostEntity.getBillid();
                        DataBaseUtil.deleteDailycostInfo(billid, handler, 0);
//                        DownUrlUtil.deleteDailycostInfo(beans.getBillid(), handler, 0);


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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WriteJournalDetailActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    /**
     *
     */
    private void onStartActivity() {
        // 多人账本
        Intent intent = new Intent(this, WriteJournalActivity.class);

        if (dailycostEntity == null) {
            return;
        }
        // 账本ID
        intent.putExtra("accountbookid", mAccountbookid);
        intent.putExtra("longdate", dailycostEntity.getTradetime());
        Bundle bundle = new Bundle();
        bundle.putParcelable("DailycostEntity", dailycostEntity);
        intent.setExtrasClassLoader(DailycostEntity.class.getClassLoader());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void onEventMainThread(JournalCommentEvent commentEvent) {//评论后刷新数据
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
