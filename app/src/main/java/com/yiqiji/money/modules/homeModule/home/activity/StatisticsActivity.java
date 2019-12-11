package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.callback.BaseCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.entity.StatInfo;
import com.yiqiji.money.modules.common.entity.StatInfoItem;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.DrawMulitpyColorRingView;
import com.yiqiji.money.modules.common.view.DrawSimpleLineChartView;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.common.widget.myviewpager.NoScrollViewPager;
import com.yiqiji.money.modules.common.widget.myviewpager.TabPageIndicator;
import com.yiqiji.money.modules.homeModule.home.adapter.MyFragmentPagerAdapter;
import com.yiqiji.money.modules.homeModule.home.fragment.StatisticsFragment;
import com.yiqiji.money.modules.homeModule.home.homeinterface.StaticsInteface;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.Response;
import retrofit.Retrofit;

public class StatisticsActivity extends BaseActivity implements TabPageIndicator.OnTabReselectedListener, StaticsInteface {
    // 统计界面

    private DrawMulitpyColorRingView dcv;
    private DrawSimpleLineChartView lineChartView;
    private LinearLayout the_second_container;
    private LinearLayout layout_root;// 界面加载完显示ui
    private TextView tv_week_days;
    private TextView tv_type;
    private TabPageIndicator indicator;
    private NoScrollViewPager pager;
    private FragmentStatePagerAdapter adapter;
    private List<String> names;
    private List<String> yearMoths;
    private List<Fragment> fragments;

    private TextView tv_all;
    private int type = 1;
    private int theScondeCategory;
    private final int GET_WEEKS_DATA = 10086;
    private final int GET_THE_DAY_DATA = GET_WEEKS_DATA + 1;
    private final int GET_THE_WEEKS_DATA_DETAILS = GET_WEEKS_DATA + 2;
    private final int GET_ONE_TYPE_DETAILS_WITH_WEEK = GET_WEEKS_DATA + 3;
    private final int DELAY_VISIBLE = 101;
    private Date date_time;
    private int current_moth = 70;
    private Calendar calendar = Calendar.getInstance();

    private int moths = 120;

    private String bookName = "";
    private String bookid = "";
    private String sorttype = "";
    private String mAccountbookcatename = "";
    private String bookUserName = "";
    private boolean isSubscribe;

    private ApiService apiService;
    private int screeWith;
    private String[] shareText = new String[]{"新浪微博", "微信", "朋友圈", "QQ", "复制链接"};
    private int share_icon[] = new int[]{R.drawable.weibo_icon, R.drawable.chart_icon, R.drawable.frend, R.drawable.qq, R.drawable.copy_icon};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ};
    private String share_url = "";
    private String share_text = "";
    private long accountbookltime;

    public static void openActivity(Context context, Date date_time, String mAccountbooktitle,
                                    String mAccountbookid, String bookUserName, String sorttype, String mAccountbookcatename, long accountbookltime, boolean isSubscribe) {
        Intent intent = new Intent(context, StatisticsActivity.class);
        intent.putExtra("data", date_time);
        intent.putExtra("bookName", mAccountbooktitle);
        intent.putExtra("bookid", mAccountbookid);
        intent.putExtra("bookUserName", bookUserName);
        intent.putExtra("sorttype", sorttype);
        intent.putExtra("accountbookltime", accountbookltime);
        intent.putExtra("mAccountbookcatename", mAccountbookcatename);
        intent.putExtra("isSubscribe", isSubscribe);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        apiService = RetrofitInstance.get().create(ApiService.class);
        date_time = (Date) getIntent().getSerializableExtra("data");
        bookName = getIntent().getStringExtra("bookName");
        bookid = getIntent().getStringExtra("bookid");
        sorttype = getIntent().getStringExtra("sorttype");
        accountbookltime = getIntent().getLongExtra("accountbookltime", 0);
        mAccountbookcatename = getIntent().getStringExtra("mAccountbookcatename");
        bookUserName = getIntent().getStringExtra("bookUserName");
        isSubscribe = getIntent().getBooleanExtra("isSubscribe", false);
        initView();
        DownUrlUtil.initBooksDataAndMember(bookid, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                super.onSuccess(booksDbInfo);
                if (booksDbInfo != null) {
                    sorttype = booksDbInfo.getSorttype();
                    mAccountbookcatename = booksDbInfo.getAccountbookcatename();
                    bookUserName = getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbInfo.getMember());

                }

            }
        });
        initTitle("统计" + "-" + bookName, -1, R.drawable.share_icon, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.layout_title_view_right_img_btn) {
                    if (!XzbUtils.isFastClick()) {
                        return;
                    }
                    Bitmap bitmap = screenshot();
                    setImageUp(bitmap);

                }
                if (v.getId() == R.id.layout_title_view_return) {
                    finish();
                }
            }
        });

        setRightImgBtnPading(UIHelper.Dp2Px(StatisticsActivity.this, 20), UIHelper.Dp2Px(StatisticsActivity.this, 0), UIHelper.Dp2Px(StatisticsActivity.this, 10), UIHelper.Dp2Px(StatisticsActivity.this, 0));
//        DownUrlUtil.searchTime(bookid, handler, RequsterTag.SEARCHTIME);
        initTabFragment();
        initPager();
        unrestrictedTitleLenth("统计" + "-" + bookName);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // layout_root.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(DELAY_VISIBLE);

        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // ((StatisticsFragment) fragments.get(current_moth % moths))
        // .initData(date_time);
    }

    private void initView() {

        fragments = new ArrayList<Fragment>();
        names = new ArrayList<String>();
        yearMoths = new ArrayList<String>();
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        pager = (NoScrollViewPager) findViewById(R.id.pager);
        tv_week_days = (TextView) findViewById(R.id.weekdays);
        tv_type = (TextView) findViewById(R.id.costtype);
        tv_all = (TextView) findViewById(R.id.tv_all_ac_main);
        layout_root = (LinearLayout) findViewById(R.id.layout_root);
        dcv = (DrawMulitpyColorRingView) findViewById(R.id.dcv_ac_main);
        lineChartView = (DrawSimpleLineChartView) findViewById(R.id.lineChartView);
    }

    private void initPager() {
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), names, fragments);
        pager.setAdapter(adapter);
        pager.setNoScroll(false);
        indicator.setViewPager(pager);
        indicator.setOnTabReselectedListener(this);
        indicator.setCurrentItem(current_moth);
        if (sorttype.equals("0")) {// 不显示
            indicator.setVisibility(View.GONE);
            pager.setNoScroll(true);
        }
    }

    Handler handler = new Handler() {
        // Light Warning:This Handler class should be static or leaks might
        // occur.
        // 理论上来说，handler应为static但此处为no-static应该不会有内存泄露的隐患，因为目前一次查询操作的耗时在1秒之内。所以除非在查询结束之前即退出activity，否则不会泄露。
        // 或者保守一点，在退出activity前终止阻塞的子线程并移除MessageQueue中的message。或handler为static
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 100:
                    // hideKeyboardandView();
                    break;
                case 101:
                    layout_root.setVisibility(View.VISIBLE);
                    break;
                case RequsterTag.SEARCHTIME:
                    initTabFragment();

                    break;
                default:
                    break;
            }

        }
    };

    private void initTabFragment() {
//        long cTime = (long) msg.obj;
        long cTime = accountbookltime * 1000;
        calendar.setTime(date_time);
        int moth = calendar.get(Calendar.MONTH) + 1;//
        if (names != null && names.size() > 0) {
            names.clear();
        }
        if (yearMoths != null && yearMoths.size() > 0) {
            yearMoths.clear();
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
        StatisticsFragment statisticsFragment = null;
        for (int i = 0; i < moths; i++) {

            long calendar_time = DateUtil.stringToTime(calendar.getTimeInMillis());// 开始时间
            names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            yearMoths.add(DateUtil.getYearMoth(new Date(calendar.getTimeInMillis())));
            statisticsFragment = new StatisticsFragment(this, date_time, bookName, bookid, sorttype, mAccountbookcatename, accountbookltime, isSubscribe);
            statisticsFragment.setStaticsInteface(this);
            fragments.add(statisticsFragment);

            if (calendar_time < cTime) {// 显示记账的前一个月
                calendar.add(Calendar.MONTH, -1);

                names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                yearMoths.add(DateUtil.getYearMoth(new Date(calendar.getTimeInMillis())));
                statisticsFragment = new StatisticsFragment(this, date_time, bookName, bookid, sorttype, mAccountbookcatename, accountbookltime, isSubscribe);
                statisticsFragment.setStaticsInteface(this);
                fragments
                        .add(statisticsFragment);
                break;
            }
            calendar.add(Calendar.MONTH, -1);
        }
        Collections.reverse(names);
        Collections.reverse(yearMoths);
        Collections.reverse(fragments);
        calendar.setTime(date_time);

        names.add("预设的");
        yearMoths.add("预设的");
        current_moth = names.size() - 2;
        statisticsFragment = new StatisticsFragment(this, date_time, bookName, bookid, sorttype, mAccountbookcatename, accountbookltime, isSubscribe);
        statisticsFragment.setStaticsInteface(this);
        fragments.add(statisticsFragment);
    }


    @Override
    public void onTabReselected(int position) {
        calendar.add(Calendar.MONTH, position - current_moth);
        date_time = calendar.getTime();
        ((StatisticsFragment) fragments.get(position % fragments.size())).initData(date_time);
        current_moth = position;

    }

    // 用于友盟分享
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private Bitmap screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        return bmp;

    }


    boolean isShareing = false;

    private void setImageUp(Bitmap bitmap) {
        if (isShareing) {
            return;
        }
        isShareing = true;
        File file = XzbUtils.saveImage(bitmap);
        bitmap = XzbUtils.decodeBitmap(this, bitmap, file.getAbsolutePath(), true);

        // Bitmap bitmap2 = XzbUtils.adjuseBmp(bitmap, file.getAbsolutePath());
        if (file.exists()) {
            file.delete();
        }
        File file2 = XzbUtils.saveImage(bitmap);

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file2);
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid")};
        String[] params = new String[]{"tokenid", "deviceid"};
        RequestBody useridBody = RequestBody.create(MediaType.parse("text/plain"), hashMap.get("tokenid"));
        RequestBody deviceidBody = RequestBody.create(MediaType.parse("text/plain"), hashMap.get("deviceid"));
        String content = "share";
        RequestBody opBody = RequestBody.create(MediaType.parse("text/plain"), content);
        HashMap<String, RequestBody> map = new HashMap<String, RequestBody>();
        map.put("tokenid", useridBody);
        map.put("deviceid", deviceidBody);
        map.put("op", opBody);
        String sign = StringUtils.setSign(valus, params);
        RequestBody signBody = RequestBody.create(MediaType.parse("text/plain"), sign);
        map.put("sign", signBody);

        map.put("imgfile\"; filename=\"" + file2.getName(), fileBody);

        apiService.setImageUp(map).enqueue(new BaseCallBack<StatInfo>(StatisticsActivity.this, true) {

            @Override
            public void onResponse(Response<StatInfo> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                super.onResponse(arg0, arg1);
                try {
                    StatInfo statInfo = arg0.body();
                    if (statInfo.getCode() == 0) {
                        StatInfoItem infoItem = statInfo.getData();
                        String url = infoItem.getShareurl();
                        share_url = url;

//                        String text = DateUtil.formatTheDateToMM_dd(date_time, 5) + "年"
//                                + DateUtil.formatTheDateToMM_dd(date_time, 3) + "月的-统计-" + bookName;
                        String text = LoginConfig.static_context;
                        share_text = text;
                        shareMeth(url, text);
                    } else {
                        showToast("加载出错了．．．");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                isShareing = false;
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                super.onFailure(arg0);
                showToast("加载出错了．．．");
                isShareing = false;
            }
        });
    }

    private MyPopuwindows myPopuwindows;

    private void shareMeth(final String url, final String text) {

        //todo 分享 （账本统计页面）
        String title = "“" + bookUserName + "”分享了" + "<<" + bookName + ">>" + "的统计情况，点此马上查看";
        ShareUtil.shareMeth(this, pager, shareText, share_icon, share_medias, url, title, text, screeWith, true, null, 0);
    }

    @Override
    public void getSorttype(String sorttype) {
        if (sorttype.equals("0")) {// 不显示
            indicator.setVisibility(View.GONE);
            pager.setNoScroll(true);
        }
    }
}
