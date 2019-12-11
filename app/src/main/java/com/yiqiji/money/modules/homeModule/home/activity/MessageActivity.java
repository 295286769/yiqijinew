package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.socialize.UMShareAPI;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.view.PullToRefreshLayout;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.homeModule.home.entity.MessageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MessageActivity extends BaseActivity implements CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener, PullToRefreshLayout.OnRefreshListener, PullToRefreshLayout.OnLoadMoreListenner {
    private View not_data;

    private MyRecyclerView rl_message;
    private List<MessageBean.DataBean> dataList = new ArrayList<MessageBean.DataBean>();
    private int page = 1;

    private String billId;
    private CommonRecyclerViewAdapter<MessageBean.DataBean> commonRecyclerViewAdapter;
    private PullToRefreshLayout refreshable_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        not_data = (View) findViewById(R.id.not_data);
        refreshable_view = (PullToRefreshLayout) findViewById(R.id.refreshable_view);
        refreshable_view.setIsApha(true);
        refreshable_view.setOnRefreshListener(this);
        refreshable_view.setOnLoadMoreListenner(this);
        initTitle("消息中心", -1, "全部已读", new OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.layout_title_view_right_btn:
                        toMarkAllRead();
                        break;
                    case R.id.layout_title_view_return:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        rl_message = (MyRecyclerView) findViewById(R.id.rl_message);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this);
        rl_message.setLayoutManager(manager);

        commonRecyclerViewAdapter = new CommonRecyclerViewAdapter<MessageBean.DataBean>(this, dataList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, MessageBean.DataBean entity, int position) {

                h.setText(R.id.tv_message_title, entity.getContent());
                String time = DateUtil.transferLongToDate(0, Long.parseLong(entity.getCtime()));
                h.setText(R.id.tv_message_time, time);

                if (!entity.getIsread().equals("0")) {
                    h.setTextGoneImage(R.id.iv_message_red_dot);
                } else {
                    h.setTextVisibleImage(R.id.iv_message_red_dot);
                }
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_message;
            }
        };

        commonRecyclerViewAdapter.setOnRecyclerViewItemClickListener(this);
        rl_message.setAdapter(commonRecyclerViewAdapter);

        getMessage(1, 1000, true);
    }

    BooksDbInfo booksDb;
    BillMemberInfo mBillMemberInfo;

    private void getMessage(int page, int size, final boolean flag) {

        HashMap<String, String> hashMap = new HashMap<>();


        hashMap.put("page", page + "");
        hashMap.put("size", size + "");

        CommonFacade.getInstance().exec(Constants.MESSAGE_INDEX, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                if (flag) {
                    showLoadingDialog(true);
                }
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject jsonObject = (JSONObject) o;
                MessageBean bean = GsonUtil.GsonToBean(jsonObject.toString(), MessageBean.class);


                try {
                    if (jsonObject.getString("data").equals("null")) {
                        not_data.setVisibility(View.VISIBLE);
                    } else {
                        if (flag) {
                            dataList.clear();
                        }
                        for (int i = 0; i < bean.getData().size(); i++) {
                            dataList.add(bean.getData().get(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.log_error(XzbUtils.getTraceInfo(), e);

                }
                if (dataList == null || dataList.size() == 0) {
                    not_data.setVisibility(View.VISIBLE);
                } else {
                    not_data.setVisibility(View.GONE);

                }

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
                refreshable_view.finishLoadAndFresh();
                if (flag) {
                    dismissDialog();
                }
            }
        });

        /**
         * 通知关闭
         * 如果有新消息，弹框提示开启通知
         */
        if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "hasNewMsg", false)) {
            showSimpleAlertDialog("请打开推送通知以便提升您的记账体验", "我们将会及时的通知您最新账单以及新成员的加入,以便您更好的了解账本的最新动态",
                    "知道了", false,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                            showSimpleAlertDialog(null, "请在手机的“设置→一起记→通知”选项中，允许向您推送记账消息", "现在设置", "我知道了", false, false, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                    dismissDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismissDialog();
                                }
                            });
                        }
                    });
            SPUtils.setParam(LoginConfig.getInstance().getUserid() + "hasNewMsg", false);
        }

    }


    /**
     * 标记全部已读
     */
    private void toMarkAllRead(){
        if(dataList.size()==0){
            return;
        }
        String[] ids = new String[dataList.size()];
        for (int i=0;i<ids.length;i++){
            ids[i]=dataList.get(i).getMessageid();
        }
        onMessageRead(ids,true);
    }


    private void onMessageRead(String[] ids, final boolean flag) {

        HashMap<String, String> hashMap = new HashMap<>();
        StringBuffer sBuffer = new StringBuffer();

        for (int i = 0; i < ids.length; i++) {
            int index = i;
            if (++index == ids.length) {
                sBuffer.append(ids[i]);
            } else {
                sBuffer.append(ids[i] + ",");
            }
        }
        hashMap.put("ids", sBuffer.toString());
        CommonFacade.getInstance().exec(Constants.MESSAGE_READ, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                if (flag) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                if (flag) {
                    getMessage(1, 1000, true);
                }
                EventBus.getDefault().post(new CheckMessageInfo());//读取消息后发通知到首页
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (flag) {
                    dismissDialog();
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        MessageBean.DataBean message = dataList.get(position);
        // 是未读
        if (message.getIsread().equals("0")) {
            String[] ids = {message.getMessageid()};
            onMessageRead(ids, true);
        }

        String url = message.getUrl();
        BaserClassMode.jumpConvention(this, url);
        // ziniuapp://money/accountbook/1902/bill/3566
        // [ziniuapp:, , money, accountbook, 1902, bill, 3566]
        // [ziniuapp:, , money, accountbook, 1902, clear, 3562]
        // [ziniuapp:, , money, accountbook, 1902, clear, 3562]
        // [ziniuapp:, , money, accountbook, 2378, clear, 4699]
//        final String[] sourceStrArray = url.split("/");
//        if (sourceStrArray.length < 4) {
//            return;
//        }
//
//        Intent intent = null;
//        if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("bill")) {
//
//            if (sourceStrArray.length >= 7) {
//                BillDetailPerecenter.getBillDetail(this, sourceStrArray[6], new ViewCallBack<String>() {
//                    @Override
//                    public void onSuccess(String o) throws Exception {
//                        super.onSuccess(o);
//                        Intent intent1 = null;
//                        if (o.equals("5")) {
//                            intent1 = new Intent(MessageActivity.this, WriteJournalDetailActivity.class);
//                        } else {
//                            intent1 = new Intent(MessageActivity.this, PaymentDetailsActivity.class);
//                        }
//                        intent1.putExtra("mAccountbookid", sourceStrArray[4]);
//                        intent1.putExtra("billid", sourceStrArray[6]);
//                        startActivity(intent1);
//                    }
//                });
//
//
//            }
//
//        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("member")) {
//            // 成员列表
//            intent = new Intent(MessageActivity.this, GroupMembersActivity.class);
//            intent.putExtra("mAccountbookid", sourceStrArray[4]);
//            startActivity(intent);
//        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("clear")) {
//            // 结算详情
//            if (sourceStrArray.length >= 7) {
//                intent = new Intent(MessageActivity.this, PaymentDetailsActivity.class);
//                intent.putExtra("mAccountbookid", sourceStrArray[4]);
//                intent.putExtra("billid", sourceStrArray[6]);
//                startActivity(intent);
//            }
//
//        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 4 && sourceStrArray[4].equals("create")) {
//            // 创建账本
//
//        } else if (sourceStrArray[3].equals("share") && sourceStrArray.length > 4 && sourceStrArray[4].equals("invite")) {
//            // 分享好友
//            UMImage image2 = new UMImage(MessageActivity.this, R.drawable.icon);// 资源文件
//            UMWeb web = new UMWeb(LoginConfig.share_url);
//            web.setTitle(LoginConfig.share_recommend_friend_title);//标题
//            web.setThumb(image2);  //缩略图
//            web.setDescription(LoginConfig.share_recommend_friend_text);
//            new ShareAction(MessageActivity.this)
//                    .withMedia(web)
//                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
//                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE).setCallback(UnListenerHelper.getUMShareListener(MessageActivity.this)).open();
//
//        } else if (sourceStrArray[3].equals("comment")) {
//            // 应用商店点赞反馈
//            Uri uri = Uri.parse("market://details?id=" + MessageActivity.this.getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(MessageActivity.this, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
//            }
//        } else if (sourceStrArray[3].equals("feedback")) {
//            // 吐槽
//            intent = new Intent(MessageActivity.this, QuestionReturnActivity.class);
//            startActivity(intent);
//
//
//        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshable_view.finishLoadAndFresh();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (InternetUtils.checkNet(MyApplicaction.getContext())) {
            page++;
            getMessage(page, 1000, false);
        } else {
            refreshable_view.finishLoadAndFresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
