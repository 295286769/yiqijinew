package com.yiqiji.money.modules.common.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.myserver.MyServer;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.FileUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.PermissionUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;
import com.yiqiji.money.modules.myModule.common.RemindReceiver;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.os.Build.MODEL;
import static android.os.Build.VERSION.RELEASE;

public class StartActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        String rid = JPushInterface.getRegistrationID(MyApplicaction.getContext());
//        if (SDK_INT >= VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        if (SDK_INT >= VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        } else {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_start);
        setPemission();
//        startRemind();
    }

    private void startRemind() {

        Calendar mCalendar;
        String time = SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindTime", "19:30");

        String[] times = time.split(":");

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis((Long) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindDay", System.currentTimeMillis()));
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        mCalendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        Intent intent = new Intent(mContext, RemindReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        int day;
        if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天").equals("每周")) {
            day = 7;
        } else {
            day = 1;
        }
//        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),  pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24 * day), pi);

    }

    private void setPemission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        PermissionUtil.requestPermisions(this, PERMISSION_REQUEST_CODE, permissions, new PermissionUtil.RequestPermissionListener() {

            @Override
            public void onRequestPermissionSuccess() {
                setStart();
            }

            @Override
            public void onRequestPermissionFail(int[] grantResults) {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void setStart() {
        // 设置系统唯一标示
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        LoginConfig.getInstance().setOsver("" + RELEASE);
        LoginConfig.getInstance().setMachine("" + MODEL);
        if (LoginConfig.getInstance().getIsFirstStartApp()) {
            TaskQueue taskQueue = new TaskQueue();
            taskQueue.execute(new Runnable() {
                @Override
                public void run() {
                    File file = new File(XzbUtils.getPathString());
                    FileUtil.RecursionDeleteFile(file);
                    String uuid = StringUtils.getUUID();
                    LoginConfig.getInstance().setDeviceid(uuid);
                    notNetInitBooks();
                }
            });


        } else {
            if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                Intent intent = new Intent(StartActivity.this, MyServer.class);
                intent.putExtra("update_mybook", "update_mybook");
                stopService(intent);
                startService(intent);
            }
        }
        JPushInterface.setAlias(MyApplicaction.getContext(), LoginConfig.getInstance().getDeviceid(), new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                if (code == 0) {

                }
            }
        });
        new TaskQueue().executeDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent();
                    Thread.sleep(1000);
                    if (LoginConfig.getInstance().getIsFirstStartApp()) {
                        intent = new Intent(StartActivity.this, WellcomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                    } else if (LoginConfig.getInstance().getIsOutLogin() && !isLogin()) {

//                        IntentUtils.startActivityOnLogin(StartActivity.this, IntentUtils.LoginIntentType.START);
//                        finish();
                        LoginBaseActivity.openActivity(StartActivity.this);
                        finish();
                    } else {
                        String orderBy = DailycostContract.DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
                        String limit = "1";
                        DownUrlUtil.serchBookList(null, null, null, null, orderBy, limit, handler, RequsterTag.BOOKSDBINFO);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            switch (msg.what) {
                case RequsterTag.BOOKSDBINFO:
                    List<BooksDbInfo> booksDbInfos = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfos == null || booksDbInfos.size() == 0) {
                        intent = new Intent(mContext, ChooseBookActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(StartActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                    }
                    break;
            }
        }
    };

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private List<MyBooksListInfo> booksListInfos;

    private void notNetInitBooks() {

        String pkid = LoginConfig.getInstance().getDeviceid();
        List<BooksDbInfo> booksDbInfos = new ArrayList<BooksDbInfo>();
//        for (int i = 0; i < 1; i++) {
//            BooksDbInfo booksDbInfo = new BooksDbInfo();
//            long time = (new Date().getTime()) / 1000;
//            switch (i) {
//                case 0:// 日常账本
//
//                    booksDbInfo.setDeviceid(pkid);
//                    booksDbInfo.setAccountbookbudget(0.00 + "");
//                    booksDbInfo.setAccountbookcate("377");
//                    booksDbInfo.setAccountbookcount("1");
//                    booksDbInfo.setAccountbookcatename("日常账本");
//                    booksDbInfo.setAccountbookctime(time + "");
//                    booksDbInfo.setAccountbookcateicon("");
//                    booksDbInfo.setAccountbookid(pkid);
//                    booksDbInfo.setAccountbookstatus("1");
//                    booksDbInfo.setAccountbooktitle("日常账本");
//                    booksDbInfo.setAccountbooktype("1");
//                    booksDbInfo.setAccountbookutime(time + "");
//                    booksDbInfo.setBookdesc("");
//                    booksDbInfo.setIsclear("0");
//                    booksDbInfo.setBookid(pkid);
//                    booksDbInfo.setSorttype(1 + "");
//                    booksDbInfo.setMyspent("0.00");
//                    booksDbInfo.setBudgetdiff("0.00");
//                    booksDbInfo.setPayamount("0.00");
//                    booksDbInfo.setUserid(pkid);
//                    booksDbInfo.setMyuid(pkid);
//                    booksDbInfo.setIssynchronization("false");
//                    booksDbInfo.setIsdelet("false");
//                    break;
//                case 1:// 家庭账本
//                    pkid = StringUtils.getUUID();
//                    booksDbInfo.setDeviceid(pkid);
//                    booksDbInfo.setAccountbookbudget(0.00 + "");
//                    booksDbInfo.setAccountbookcate("11");
//                    booksDbInfo.setAccountbookcount("1");
//                    booksDbInfo.setAccountbookcatename("家庭账本");
//                    booksDbInfo.setAccountbookctime(time + "");
//                    booksDbInfo.setAccountbookcateicon("");
//                    booksDbInfo.setAccountbookid(pkid);
//                    booksDbInfo.setAccountbookstatus("1");
//                    booksDbInfo.setAccountbooktitle("家庭账本");
//                    booksDbInfo.setAccountbooktype("1");
//                    booksDbInfo.setAccountbookutime(time + "");
//                    booksDbInfo.setBookdesc("");
//                    booksDbInfo.setIsclear("0");
//                    booksDbInfo.setBookid(pkid);
//                    booksDbInfo.setSorttype(1 + "");
//                    booksDbInfo.setMyspent("0.00");
//                    booksDbInfo.setBudgetdiff("0.00");
//                    booksDbInfo.setPayamount("0.00");
//                    booksDbInfo.setUserid(pkid);
//                    booksDbInfo.setMyuid(pkid);
//                    booksDbInfo.setIssynchronization("false");
//                    booksDbInfo.setIsdelet("false");
//                    break;
//                case 2:// 旅游账本
//                    pkid = StringUtils.getUUID();
//                    booksDbInfo.setDeviceid(pkid);
//                    booksDbInfo.setAccountbookbudget(0.00 + "");
//                    booksDbInfo.setAccountbookcate("12");
//                    booksDbInfo.setAccountbookcount("1");
//                    booksDbInfo.setAccountbookcatename("旅游账本");
//                    booksDbInfo.setAccountbookctime(time + "");
//                    booksDbInfo.setAccountbookcateicon("");
//                    booksDbInfo.setAccountbookid(pkid);
//                    booksDbInfo.setAccountbookstatus("1");
//                    booksDbInfo.setAccountbooktitle("旅游账本");
//                    booksDbInfo.setAccountbooktype("1");
//                    booksDbInfo.setAccountbookutime(time + "");
//                    booksDbInfo.setBookdesc("");
//                    booksDbInfo.setIsclear("1");
//                    booksDbInfo.setBookid(pkid);
//                    booksDbInfo.setSorttype(1 + "");
//                    booksDbInfo.setMyspent("0.00");
//                    booksDbInfo.setBudgetdiff("0.00");
//                    booksDbInfo.setPayamount("0.00");
//                    booksDbInfo.setUserid(pkid);
//                    booksDbInfo.setMyuid(pkid);
//                    booksDbInfo.setIssynchronization("false");
//                    booksDbInfo.setIsdelet("false");
//                    break;
//                case 3:// 饭团账本
//                    pkid = StringUtils.getUUID();
//                    booksDbInfo.setDeviceid(pkid);
//                    booksDbInfo.setAccountbookbudget(0.00 + "");
//                    booksDbInfo.setAccountbookcate("7");
//                    booksDbInfo.setAccountbookcount("1");
//                    booksDbInfo.setAccountbookcatename("饭团账本");
//                    booksDbInfo.setAccountbookctime(time + "");
//                    booksDbInfo.setAccountbookcateicon("");
//                    booksDbInfo.setAccountbookid(pkid);
//                    booksDbInfo.setAccountbookstatus("1");
//                    booksDbInfo.setAccountbooktitle("饭团账本");
//                    booksDbInfo.setAccountbooktype("1");
//                    booksDbInfo.setAccountbookutime(time + "");
//                    booksDbInfo.setBookdesc("");
//                    booksDbInfo.setIsclear("1");
//                    booksDbInfo.setBookid(pkid);
//                    booksDbInfo.setSorttype(1 + "");
//                    booksDbInfo.setMyspent("0.00");
//                    booksDbInfo.setBudgetdiff("0.00");
//                    booksDbInfo.setPayamount("0.00");
//                    booksDbInfo.setUserid(pkid);
//                    booksDbInfo.setMyuid(pkid);
//                    booksDbInfo.setIssynchronization("false");
//                    booksDbInfo.setIsdelet("false");
//                    break;
//                case 4:// 合租账本
//                    pkid = StringUtils.getUUID();
//                    booksDbInfo.setDeviceid(pkid);
//                    booksDbInfo.setAccountbookbudget(0.00 + "");
//                    booksDbInfo.setAccountbookcate("8");
//                    booksDbInfo.setAccountbookcount("1");
//                    booksDbInfo.setAccountbookcatename("合租账本");
//                    booksDbInfo.setAccountbookctime(time + "");
//                    booksDbInfo.setAccountbookcateicon("");
//                    booksDbInfo.setAccountbookid(pkid);
//                    booksDbInfo.setAccountbookstatus("1");
//                    booksDbInfo.setAccountbooktitle("合租账本");
//                    booksDbInfo.setAccountbooktype("1");
//                    booksDbInfo.setAccountbookutime(time + "");
//                    booksDbInfo.setBookdesc("");
//                    booksDbInfo.setIsclear("1");
//                    booksDbInfo.setBookid(pkid);
//                    booksDbInfo.setSorttype(1 + "");
//                    booksDbInfo.setMyspent("0.00");
//                    booksDbInfo.setBudgetdiff("0.00");
//                    booksDbInfo.setPayamount("0.00");
//                    booksDbInfo.setUserid(pkid);
//                    booksDbInfo.setMyuid(pkid);
//                    booksDbInfo.setIssynchronization("false");
//                    booksDbInfo.setIsdelet("false");
//                    break;
//
//                default:
//                    break;
//            }
//            booksDbInfos.add(booksDbInfo);
//        }

        booksListInfos = new ArrayList<MyBooksListInfo>();

//        for (int i = 1; i < 13; i++) {
//            MyBooksListInfo booksListInfo = new MyBooksListInfo();
//            switch (i) {
//
//                case 1:
//                    booksListInfo.setCategorydesc("记账是一种认真的生活方式");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("日常账本");
//                    booksListInfo.setCategorytype("0");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 2:
//                    booksListInfo.setCategorydesc("记录宝宝每一步成长");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("宝宝账本");
//                    booksListInfo.setCategorytype("0");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 3:
//                    booksListInfo.setCategorydesc("明白用车,清楚保养");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("汽车账本");
//                    booksListInfo.setCategorytype("0");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 4:
//                    booksListInfo.setCategorydesc("花出去的份子钱，早晚要还的");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("人情账本");
//                    booksListInfo.setCategorytype("0");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//
//                    break;
//                case 5:
//                    booksListInfo.setCategorydesc("从账本里找生意经");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("生意账本");
//                    booksListInfo.setCategorytype("0");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 6:
//                    booksListInfo.setCategorydesc("装修必备，贴心为装修场景打造");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("装修账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 7:
//                    booksListInfo.setCategorydesc("搭伙吃饭，清账便捷");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("饭团账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("1");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 8:
//                    booksListInfo.setCategorydesc("水电房租，一清二楚");
//                    booksListInfo.setCategoryid(i + "");
//                    booksListInfo.setCategorytitle("合租账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("1");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//
//                case 9:
//                    booksListInfo.setCategorydesc("进入神圣殿堂前，记一记更幸福");
//                    booksListInfo.setCategoryid(10 + "");
//                    booksListInfo.setCategorytitle("结婚账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 10:
//                    booksListInfo.setCategorydesc("一起管理温馨小家庭，成就满满又有爱");
//                    booksListInfo.setCategoryid(11 + "");
//                    booksListInfo.setCategorytitle("家庭账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 11:
//                    booksListInfo.setCategorydesc("结伴旅行，花费明了");
//                    booksListInfo.setCategoryid(12 + "");
//                    booksListInfo.setCategorytitle("旅游账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("1");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                case 12:
//                    booksListInfo.setCategorydesc("记账是一种认真的生活方式");
//                    booksListInfo.setCategoryid(377 + "");
//                    booksListInfo.setCategorytitle("日常账本");
//                    booksListInfo.setCategorytype("1");
//                    booksListInfo.setCategoryicon("");
//                    booksListInfo.setIsclear("0");
//                    booksListInfo.setParentid("0");
//                    booksListInfo.setStatus("1");
//                    break;
//                default:
//                    break;
//            }
//            booksListInfos.add(booksListInfo);
//        }
//        LoginConfig.getInstance().setBookId(booksDbInfos.get(0).getBookid());
        Intent intent = new Intent(this, MyServer.class);
        Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("initbooks", (ArrayList<? extends Parcelable>) booksDbInfos);
        bundle.putParcelableArrayList("initbooklist", (ArrayList<? extends Parcelable>) booksListInfos);
        intent.putExtras(bundle);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
