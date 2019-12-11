package com.yiqiji.money.modules.homeModule.home.entity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.detailinfo.activity.BookCommentListActivity;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.activity.StartActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.entity.BookExpenditure;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.OuterBooksDbInfo;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.entity.PostBill.MemberlistBean;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.GroupMembersActivity;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalDetailActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.BooksListActivity;
import com.yiqiji.money.modules.myModule.my.activity.QuestionReturnActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BaserClassMode {
    private static String TAG = "BaserClassMode";
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;
    private static ExecutorService LIMITED_TASK_EXECUTOR = null;
    private static final ExecutorService FULL_TASK_EXECUTOR = null;
    public static final ExecutorService DEFAULT_TASK_EXECUTOR;
    private static Object lock = new Object();

    static {
        SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();
        LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(3);
        // FULL_TASK_EXECUTOR = (ExecutorService)
        // Executors.newCachedThreadPool();
        DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
    }

    public static long start_time;// 开始时间
    public static long end_time;// 结束时间


    /**
     * 本人是否参与这条账单
     *
     * @param itemEntity
     * @return
     */
    public static boolean isParticipate(DailycostEntity itemEntity) {
        if (itemEntity.getIsclear().equals("0")) {
            return true;
        }
        String deviceid = LoginConfig.getInstance().getDeviceid();
        String tokenid = LoginConfig.getInstance().getTokenId();
        String userid = LoginConfig.getInstance().getUserid();
        boolean participate = false;
        List<BillMemberInfo> billMemberInfos = itemEntity.getMemberlist();
        if (billMemberInfos != null) {
            if (billMemberInfos.size() < 1) {
                participate = true;
            }
            if (TextUtils.isEmpty(tokenid)) {// 未登录状态判断用户是否参与

                for (BillMemberInfo billMemberInfo : billMemberInfos) {
                    if (billMemberInfo.getDeviceid() == null || deviceid == null) {
                        continue;
                    }
                    if (billMemberInfo.getDeviceid().equals(deviceid)) {
                        participate = true;
                        break;
                    }
                }
            } else {
                for (BillMemberInfo billMemberInfo : billMemberInfos) {
                    if (billMemberInfo.getUserid().equals(userid)) {
                        participate = true;
                        break;
                    }
                }
            }

        } else {
            participate = true;
        }
        return participate;
    }

    /**
     * 是否是账单操作人
     *
     * @param itemEntity
     * @return
     */
    public static boolean isOperation(DailycostEntity itemEntity, String myUid) {
        String deviceid = LoginConfig.getInstance().getDeviceid();
        String tokenid = LoginConfig.getInstance().getTokenId();
        String userid = LoginConfig.getInstance().getUserid();
        boolean isOperation = false;
        List<BillMemberInfo> billMemberInfos = itemEntity.getMemberlist();
        if (itemEntity.getIssynchronization().equals("false")) {
            isOperation = true;
            return isOperation;
        }

        if (TextUtils.isEmpty(tokenid)) {// 未登录状态判断用户是否参与
            if (itemEntity.getDeviceid() != null && itemEntity.getDeviceid().equals(deviceid)) {
                isOperation = true;
                return isOperation;
            }
        } else {

            if (myUid != null && userid.equals(itemEntity.getUserid())) {
                isOperation = true;
                return isOperation;
            }
        }

        return isOperation;

    }

    /**
     * 获取账单结算详情
     */
    private static void getClearInfo(final Context context, List<DailycostEntity> dailycostEntities,
                                     final String mAccountbookid) {
        ApiService apiService = RetrofitInstance.get().create(ApiService.class);
        HashMap<String, String> hashMap = new HashMap<>();
        if (dailycostEntities == null) {
            return;
        }
        for (int i = 0; i < dailycostEntities.size(); i++) {
            DailycostEntity dailycostEntity = dailycostEntities.get(i);
            final String billid = dailycostEntity.getBillid();

            hashMap.put("id", mAccountbookid);
            hashMap.put("billid", billid);
            CommonFacade.getInstance().exec(Constants.GETCLEARINFO, hashMap, new ViewCallBack() {
                @Override
                public void onStart() {
                    super.onStart();
                    if (context != null && context instanceof Activity) {
                        ((BaseActivity) context).showLoadingDialog(true);
                    }
                }

                @Override
                public void onSuccess(Object o) throws Exception {
                    super.onSuccess(o);
                    SettmentDetailInfo settmentDetailInfo = GsonUtil.GsonToBean(o.toString(), SettmentDetailInfo.class);
                    DailycostEntity dailycostEntity = settmentDetailInfo.getData();
                    List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                    if (billMemberInfos != null) {
                        for (int j = 0; j < billMemberInfos.size(); j++) {
                            BillMemberInfo billMemberInfo = billMemberInfos.get(j);
                            billMemberInfo.setType(dailycostEntity.getBilltype());
                            billMemberInfo.setBillid(billid);
                            billMemberInfos.set(j, billMemberInfo);
                        }
                    }

                    dailycostEntity.setMemberlist(billMemberInfos);
                    DownUrlUtil.updateDailycostEntitys(dailycostEntity, mAccountbookid, billid);
                }

                @Override
                public void onFailed(SimpleMsg simleMsg) {
                    super.onFailed(simleMsg);
                    if (context != null && context instanceof Activity) {
                        ((BaseActivity) context).showToast(simleMsg);
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (context != null && context instanceof Activity) {
                        ((BaseActivity) context).dismissDialog();
                    }
                }
            });

        }

    }

//    /**
//     * 同步账单
//     *
//     * @param mContext
//     * @param booksDbInfo
//     * @param handler
//     * @param billInfos
//     * @param dailycostEntities
//     */
//    public static void setSyncbook(final Context mContext, final BooksDbInfo booksDbInfo, final Handler handler,
//                                   final List<PostBill> billInfos, final List<DailycostEntity> dailycostEntities) {
//
//        ApiService apiService = RetrofitInstance.get().create(ApiService.class);
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
//                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
//        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//
//        String json = GsonUtil.GsonString(billInfos);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//        apiService.sync_book(hashMap, body).enqueue(new Callback<BillSyncInfo>() {
//            @Override
//            public void onResponse(Response<BillSyncInfo> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                try {
//                    BillSyncInfo billSyncInfo = arg0.body();
//                    if (billSyncInfo.getCode() == 0) {
//
//                        Message message = Message.obtain();
//                        message.what = RequsterTag.SYNCHRONIZATION_SUS;
//                        handler.sendMessage(message);
//                        for (int i = 0; i < billSyncInfo.getData().size(); i++) {
//                            BillSyncInfo.DataInfo mBillData = billSyncInfo.getData().get(i);
//                            String a = mBillData.getPkid();
//                            String b = String.valueOf(mBillData.getBillid());
//                            for (DailycostEntity dailycostEntity : dailycostEntities) {
//                                String pkId = dailycostEntity.getPkid();
//                                dailycostEntity.setAccountbooktype(booksDbInfo.getAccountbooktype());
//                                dailycostEntity.setIsclear(booksDbInfo.getIsclear());
//                                dailycostEntity.setIssynchronization("true");
//                                if (a.equals(pkId)) {
//                                    dailycostEntity.setBillid(b);
//
//                                    dailycostEntities.set(i, dailycostEntity);
//                                }
//                            }
//
//                        }
//                        DownUrlUtil.updateMultipleRecords(dailycostEntities);
//                        Message msg = new Message();
//                        msg.what = RequsterTag.GETBILLS;
//
//                        handler.sendMessage(msg);
//
//                    } else {
//                        Message message = Message.obtain();
//                        message.what = RequsterTag.SYNCHRONIZATION_FAL;
//                        handler.sendMessage(message);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            public void onFailure(Throwable arg0) {
//                ToastUtils.DiyToast(mContext, "网络连接出错");
//                Message message = Message.obtain();
//                message.what = RequsterTag.SYNCHRONIZATION_FAL;
//                handler.sendMessage(message);
//            }
//        });
//    }

    /**
     * 同步一条账单  天上传图片后提交账单
     */
    public static void setSynOneDailycostEntity(
            final List<PostBill> billInfos, final List<DailycostEntity> dailycostEntities, final ViewCallBack viewCallBack) {
        ApiService apiService = RetrofitInstance.get().create(ApiService.class);
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
        String sign = StringUtils.setSign(valus, params);
        hashMap.put("sign", sign);

        String json = GsonUtil.GsonString(billInfos);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.sync_book(hashMap, body).enqueue(new Callback<BillSyncInfo>() {
            @Override
            public void onResponse(Response<BillSyncInfo> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                try {
                    BillSyncInfo billSyncInfo = arg0.body();
                    if (billSyncInfo != null && billSyncInfo.getCode() == 0) {

                        viewCallBack.onSuccess(dailycostEntities.size());
                        if (billSyncInfo.getData() != null) {
                            List<String> dailycostEntityList = new ArrayList<String>();
                            for (DailycostEntity dailycostEntity : dailycostEntities) {
                                String billid = dailycostEntity.getBillid();
                                dailycostEntityList.add(billid);
                            }
                            for (int i = 0; i < billSyncInfo.getData().size(); i++) {
                                BillSyncInfo.DataInfo mBillData = billSyncInfo.getData().get(i);
                                String a = mBillData.getPkid();
                                String b = String.valueOf(mBillData.getBillid());
                                for (int j = 0; j < dailycostEntities.size(); j++) {
                                    DailycostEntity dailycostEntity = dailycostEntities.get(j);
                                    String pkId = dailycostEntity.getPkid();
                                    dailycostEntity.setAccountbooktype(dailycostEntity.getAccountbooktype());
                                    dailycostEntity.setIsclear(dailycostEntity.getIsclear());
                                    dailycostEntity.setIssynchronization("true");
                                    if (a.equals(pkId)) {
                                        dailycostEntity.setBillid(b);
                                        dailycostEntities.set(j, dailycostEntity);
                                    }
                                }

                            }
                            DownUrlUtil.updateMultipleRecords(dailycostEntities, dailycostEntityList);
                        } else {
                            viewCallBack.onFailed(null);
                        }


                    } else {

                        viewCallBack.onFailed(null);
//                        Message message = Message.obtain();
//                        message.what = RequsterTag.SYNCHRONIZATION_FAL;
//                        handler.sendMessage(message);
//                        RequsterTag.isSynchronizationing = false;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    viewCallBack.onFailed(null);

                }
            }

            public void onFailure(Throwable arg0) {
//                ToastUtils.DiyToast(mContext, "网络连接出错");
                EventBus.getDefault().post("fail");
//                Message message = Message.obtain();
//                message.what = RequsterTag.SYNCHRONIZATION_FAL;
//                handler.sendMessage(message);
                RequsterTag.isSynchronizationing = false;
            }
        });
    }

    /**
     * 提交日志
     */
    public static void setSynOneDailycostEntity(
            List<PostBill> billInfo, final ViewCallBack viewCallBack) {
        ApiService apiService = RetrofitInstance.get().create(ApiService.class);
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), hashMap.get("plat"),
                hashMap.get("appver"), hashMap.get("machine"), hashMap.get("osver"),};
        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "machine", "osver"};
        String sign = StringUtils.setSign(valus, params);
        hashMap.put("sign", sign);

        String json = GsonUtil.GsonString(billInfo);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        apiService.sync_book(hashMap, body).enqueue(new Callback<BillSyncInfo>() {
            @Override
            public void onResponse(Response<BillSyncInfo> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                try {
                    BillSyncInfo billSyncInfo = arg0.body();
                    if (billSyncInfo != null && billSyncInfo.getCode() == 0) {

                        viewCallBack.onSuccess(billSyncInfo);

                    } else {

                        viewCallBack.onFailed(null);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    viewCallBack.onFailed(null);

                }
            }

            public void onFailure(Throwable arg0) {
                viewCallBack.onFailed(null);
                ToastUtils.DiyToast(MyApplicaction.getContext(), "网络连接出错");
            }
        });
    }

    /**
     * 生成一个提交表单对象
     *
     * @param action 当前是add updata delete
     * @return
     */
    public static List<PostBill> getPostBillData(String action, List<DailycostEntity> entities) {
        List<PostBill> postBills = new ArrayList<PostBill>();
        if (entities == null) {
            return postBills;
        }
        final String mPkid = "";// 作为主键的Pkid

        MemberlistBean mMemberlistBean = null;
        for (DailycostEntity dailycostEntity : entities) {
            String remark = dailycostEntity.getBillmark();
            PostBill mPostBill = new PostBill();

            mPostBill.setTradetime(Integer.parseInt(dailycostEntity.getTradetime()));// 需要修改
            mPostBill.setPkid(dailycostEntity.getPkid());//
            mPostBill.setAccountbookid(dailycostEntity.getAccountbookid());
            mPostBill.setCateid(dailycostEntity.getBillcateid());// 一级分类ID
            mPostBill.setAction(action);// 是添加删除修改
            mPostBill.setBillctime(Integer.parseInt(dailycostEntity.getBillctime()));// 时间选择器时间
            mPostBill.setBillclear(Integer.parseInt(dailycostEntity.getBillclear()));// 0创建时是未结算
            mPostBill.setBillamount(dailycostEntity.getBillamount());
            mPostBill.setBilltype(Integer.parseInt(dailycostEntity.getBilltype()));// 账单类型，收入，支出，转账
            // 需要修改
            mPostBill.setRemark(dailycostEntity.getBillmark());
            mPostBill.setAccountnumber(dailycostEntity.getAccountnumber());
            mPostBill.setBillid(dailycostEntity.getBillid());
            mPostBill.setBillimg(dailycostEntity.getBillimg());
            mPostBill.setAddress(dailycostEntity.getAddress());

            mPostBill.setAccountbooktype(Integer.parseInt(dailycostEntity.getAccountbooktype()));
            postBills.add(mPostBill);
            List<MemberlistBean> memberlist = new ArrayList<>();
            if (dailycostEntity.getMemberlist() != null) {
                for (BillMemberInfo billMemberInfo : dailycostEntity.getMemberlist()) {

                    String memberid = billMemberInfo.getMemberid();
                    mMemberlistBean = new MemberlistBean();
                    mMemberlistBean.setAmount(billMemberInfo.getAmount());
                    mMemberlistBean.setCtime(dailycostEntity.getUpdatetime());
                    mMemberlistBean.setMemberid(memberid);
                    mMemberlistBean.setStatus(Integer.parseInt(billMemberInfo.getStatus()));// 需要修改
                    mMemberlistBean.setType(Integer.parseInt(billMemberInfo.getType()));// 0.收入;1.支出;2.转账
                    memberlist.add(mMemberlistBean);
                }
            }

            mPostBill.setMemberlist(memberlist);
        }

        return postBills;
    }


    /**
     * 添加账本
     */
    public static void addBooks(final Context context, final Handler handler) {
        final String pkid = LoginConfig.getInstance().getDeviceid();// 此id用于生成用户唯一标识
        final BooksDbInfo booksDbIn = new BooksDbInfo();
        long time = (new Date().getTime()) / 1000;

        booksDbIn.setDeviceid(pkid);
        booksDbIn.setAccountbookbudget(0.00 + "");
        booksDbIn.setAccountbookcate("377");
        booksDbIn.setAccountbookcount("1");
        booksDbIn.setAccountbookcatename("日常账本");
        booksDbIn.setAccountbookctime(time + "");
        booksDbIn.setAccountbookcateicon("");
        booksDbIn.setAccountbookid(pkid);
        booksDbIn.setAccountbookstatus("1");
        booksDbIn.setAccountbooktitle("日常账本");
        booksDbIn.setAccountbooktype("1");
        booksDbIn.setAccountbookutime(time + "");
        booksDbIn.setBookdesc("");
        booksDbIn.setIsclear("0");
        booksDbIn.setBookid(pkid);
        booksDbIn.setSorttype(1 + "");
        booksDbIn.setMyspent("0.00");
        booksDbIn.setBudgetdiff("0.00");
        booksDbIn.setPayamount("0.00");
        booksDbIn.setUserid(pkid);
        booksDbIn.setMyuid(pkid);
        booksDbIn.setIssynchronization("false");
        booksDbIn.setIsdelet("false");

        String bookName = booksDbIn.getAccountbooktitle();
        String bookcateid = booksDbIn.getAccountbookcate();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", bookcateid);
        hashMap.put("name", bookName);
        CommonFacade.getInstance().exec(Constants.ADD_BOOK, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                OuterBooksDbInfo outerBooksDbInfo = GsonUtil.GsonToBean(o.toString(), OuterBooksDbInfo.class);
                LoginConfig.getInstance().setDeviceid(pkid);

                BooksDbInfo booksDbInfo = outerBooksDbInfo.getData();
                String mAccountbookcate = booksDbInfo.getAccountbookcate();
                booksDbInfo.setIssynchronization("true");
                booksDbInfo.setIsdelet("false");
                LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
                EventBus.getDefault().post(booksDbInfo);
                DownUrlUtil.deletBookInfos();
                DownUrlUtil.sysInfo(booksDbInfo, booksDbIn.getDeviceid());
                getBooksCate(booksDbInfo.getAccountbookid());
                if (handler != null) {
                    Message message = Message.obtain();
                    message.what = RequsterTag.BOOKSSYS_SUS;
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (context != null && context instanceof Activity) {
                    ((BaseActivity) context).showToast(simleMsg);
                }
            }

        });

    }

    public static void billDetail(Context context, int position, List<DailycostEntity> list, String mAccountbookid) {
        try {
//            Intent intent = new Intent(context, PaymentDetailsActivity.class);
//            Bundle bundle = new Bundle();
            if (list == null || list.size() == 0) {
                return;
            }
            String type = list.get(position).getBilltype();
            String billid = list.get(position).getBillid();
            if (type.equals(RequsterTag.DIARYACCOUNTBOOKCATE)) {
                WriteJournalDetailActivity.openActivity(context, mAccountbookid, billid, true);
            } else {
                PaymentDetailsActivity.openActivity(context, mAccountbookid, billid, true, false);
            }

//            intent.setExtrasClassLoader(BillListInfoItem.class.getClassLoader());
//            bundle.putParcelable("list", list.get(position));
//            bundle.setClassLoader(context.getClass().getClassLoader());
//            intent.putExtra("mAccountbookid", mAccountbookid);
//            intent.putExtra("billid", list.get(position).getBillid());
//
//            intent.putExtras(bundle);
//            context.startActivity(intent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void jumpConvention(final Context mContext, String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = null;

        final String[] sourceStrArray = url.split("/");
        if (sourceStrArray.length < 4) {
            intent = new Intent(mContext, StartActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("bill")) {

            if (sourceStrArray.length >= 7) {
                BillDetailPerecenter.getBillDetail(mContext, sourceStrArray[6], new ViewCallBack<String>() {
                    @Override
                    public void onSuccess(String o) throws Exception {
                        super.onSuccess(o);
                        Intent intent1 = null;
                        if (o.equals("5")) {
                            intent1 = new Intent(mContext, WriteJournalDetailActivity.class);
                        } else {
                            intent1 = new Intent(mContext, PaymentDetailsActivity.class);
                        }
                        intent1.putExtra("mAccountbookid", sourceStrArray[4]);
                        intent1.putExtra("billid", sourceStrArray[6]);
                        mContext.startActivity(intent1);
                    }
                });

            }

        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("member")) {
            // 成员列表
            GroupMembersActivity.startActivity(mContext, sourceStrArray[4]);
//            intent = new Intent(mContext, GroupMembersActivity.class);
//            intent.putExtra("mAccountbookid", sourceStrArray[4]);
//            mContext.startActivity(intent);

        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 5 && sourceStrArray[5].equals("clear")) {
            // 结算详情
            if (sourceStrArray.length >= 7) {
                intent = new Intent(mContext, PaymentDetailsActivity.class);
                intent.putExtra("mAccountbookid", sourceStrArray[4]);
                intent.putExtra("billid", sourceStrArray[6]);
                mContext.startActivity(intent);
            }

        } else if (sourceStrArray[3].equals("accountbook") && sourceStrArray.length > 4 && sourceStrArray[4].equals("create")) {
            // 创建账本
            intent = new Intent(mContext, BooksListActivity.class);
            mContext.startActivity(intent);

        } else if (sourceStrArray[3].equals("share") && sourceStrArray.length > 4 && sourceStrArray[4].equals("invite")) {


            int imageid = R.drawable.icon;
            SHARE_MEDIA[] share_medias = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
            ShareUtil.setUrlList(mContext, LoginConfig.share_url, LoginConfig.share_recommend_friend_title, LoginConfig.share_recommend_friend_text, share_medias, imageid);

        } else if (sourceStrArray[3].equals("comment")) {
            // 应用商店点赞反馈
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                mContext.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
            }
        } else if (sourceStrArray[3].equals("feedback")) {
            // 吐槽
            intent = new Intent(mContext, QuestionReturnActivity.class);
            mContext.startActivity(intent);
        } else if (sourceStrArray[3].equals("accountbook") /*&& url.contains("comment")*/) {
            String accountbookid = sourceStrArray[4];
            BookCommentListActivity.open(mContext, accountbookid, false);

        }
    }


    /**
     * todo 这里需要统一优化起来
     *
     * @param mAccountbookid
     */
    public static void getBooksCate(final String mAccountbookid) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", mAccountbookid);// 账本ID
        CommonFacade.getInstance().exec(Constants.BOOKS_CATE, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                try {
                    JSONObject jsonOb = (JSONObject) o;
                    String pathJson = XzbUtils.getPathString();
                    // 账本Id，用账本ID保存分类列表的JSON字符串
                    LoginConfig.getInstance().setJsonbook(mAccountbookid,
                            jsonOb.toString());
                    DataLocalPersistencer.saveBookCategoryJSONObject(mAccountbookid, jsonOb);
                    if (jsonOb.has("0")) {
                        JSONObject b = jsonOb.getJSONObject("0");// 支出
                        BookExpenditure bookExpenditure = GsonUtil.GsonToBean(b.toString(), BookExpenditure.class);
                        DownUrlUtil.downLoad(bookExpenditure);
                    }
                    if (jsonOb.has("1")) {
                        JSONObject incom = null;// 收入

                        incom = jsonOb.getJSONObject("1");

                        BookExpenditure bookincom = GsonUtil.GsonToBean(incom.toString(), BookExpenditure.class);
                        DownUrlUtil.downLoad(bookincom);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.log_error(null, e);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
//                if (BaseActivity.mContext != null) {
//                    ((BaseActivity) BaseActivity.mContext).showToast(simleMsg);
//                }
            }
        });
    }

    /**
     * 初始化账本在根据账本查询成员
     */
    public static void initBooksDataAndMember(final String bookid, final ViewCallBack<BooksDbInfo> viewCallBack) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBooksColumns.ACCOUNTBOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    BooksDbInfo booksDbInfo = DbInterface.getBooksDbInfo(where, selectionArgs, null, null, null, null);
                    // if (booksDbInfo == null) {
                    // return;
                    // }
                    if (booksDbInfo != null) {
                        where = DailycostContract.DtBookMemberColumns.BOOKID + "=?";
                        String[] selectionArgsmember = new String[]{booksDbInfo.getAccountbookid()};
                        List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                                selectionArgsmember, null, null, null, null);
                        booksDbInfo.setMember(booksDbMemberInfos);
                    }
                    try {
                        viewCallBack.onSuccess(booksDbInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
