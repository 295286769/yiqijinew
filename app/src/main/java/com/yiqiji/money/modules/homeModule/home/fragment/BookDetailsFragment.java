package com.yiqiji.money.modules.homeModule.home.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.entity.eventbean.IntentMessageBean;
import com.yiqiji.money.modules.common.entity.eventbean.UserInfoChangeEvent;
import com.yiqiji.money.modules.common.fragment.LazyFragment;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.PullToRefreshLayout;
import com.yiqiji.money.modules.common.widget.MyScrollView;
import com.yiqiji.money.modules.common.widget.myviewpager.NoScrollViewPager;
import com.yiqiji.money.modules.common.widget.myviewpager.TabPageIndicator;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.MyFragmentPagerAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksCatelistInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.homeinterface.DeletMembersInterface;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.home.view.HomeExpeseIncomView;
import com.yiqiji.money.modules.homeModule.home.view.HomeRenovationPromptView;
import com.yiqiji.money.modules.homeModule.home.view.HomeSignoneView;
import com.yiqiji.money.modules.homeModule.home.view.MembersHorizontalScrollView;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

@SuppressLint("ValidFragment")
public class BookDetailsFragment extends LazyFragment implements OnClickListener, TabPageIndicator.OnTabReselectedListener, SignOneBookFragment.PagerHeight,
        MyScrollView.OnScrollListener, PullToRefreshLayout.OnRefreshListener, PullToRefreshLayout.OnLoadMoreListenner,
        DeletMembersInterface {
    private Context mContext;
    private boolean isPress;
    private View view;
    private LinearLayout
            top_layout, timeLine, timeLine_top;
    private MyScrollView myScroll;
    private TabPageIndicator tabPageIndicator;
    private NoScrollViewPager noScrollViewPager;
    private TextView sign_button;
    private PullToRefreshLayout pullToRefreshLayout;
    private ImageView bagrou_image;
    private HomeRenovationPromptView home_renovation_prompt_view;
    private MembersHorizontalScrollView membersHorizontalScrollView;
    private HomeExpeseIncomView homeExpeseIncomView;
    private HomeSignoneView homeSignoneView;
    private int moths = 120;
    private Calendar calendar = Calendar.getInstance();
    private int current_moths = 1;
    private Date date_time;
    private String mAccountbooktitle = "";
    private String mAccountbookid = "";
    private String user_name = "";
    private FragmentStatePagerAdapter pagerAdapter;
    private List<String> names;
    private List<Fragment> fragments;
    // private String budget;// 设置预算
    private float screeWith;
    private int view_item_with;// 成员在屏幕显示宽度
    private int top_layout_height;
    private String deviceid = "";

    private int maxPage = 1;
    private boolean isRefresh = true;
    private int pageIndex = 1;
    private float screeHeight = 0;
    private static int pageHeight = 0;
    private int firstHeight = 0;// 记录第一次加载时NoScrollViewPager的高度
    private String isClear = "0";// 0:不需要结算 1：需要结算
    private List<DailycostEntity> dailycostEntities;
    private ImageView left_icon;
    private boolean isRefreshing = false;//是否正在刷新

    private int mCustomVariable;
    private int pagerSize = 10000;
    private long fistTimeLocation = 0;
    private boolean needChangeMember = false;//是否需要刷新成员

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putInt("variable", mCustomVariable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCustomVariable = savedInstanceState.getInt("variable", 0);
        }
        mContext = getActivity();
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        this.mAccountbookid = LoginConfig.getInstance().getBookId();
        screeWith = XzbUtils.getPhoneScreen((Activity) mContext).widthPixels;
        view_item_with = (int) (((screeWith - screeWith / 4) - UIHelper.Dp2Px(mContext, 5) * 5) / 4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        pageIndex = 1;
        isRefresh = true;
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sign_one, null);
            screeHeight = XzbUtils.getPhoneScreen(getActivity()).heightPixels;
            initView();
            setOnnewIntent(mAccountbookid);
        }

        isPress = true;
        deviceid = LoginConfig.getInstance().getDeviceid();
        lazyLoad();

        return view;
    }


    private void intHouse() {
        BooksDetailPerecenter.checkMessage(new ViewCallBack<CheckMessageInfo.MessgeinfoItem>() {
            @Override
            public void onSuccess(CheckMessageInfo.MessgeinfoItem messgeinfoItem) throws Exception {
                super.onSuccess(messgeinfoItem);
                home_renovation_prompt_view.setNumberBooks(messgeinfoItem, booksDbInfo);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);
            }
        });

    }

    private boolean isBookIn = false;//是否是从账本跳转

    public void setIsBookIn(boolean isBookIn) {
        this.isBookIn = isBookIn;
    }

    public void setOnnewIntent(String mAccountbookid) {
        setScrollToTop();
        isRefresh = true;
        isRefreshing = false;
        RequsterTag.isNeedSynchronizationing = false;
        this.mAccountbookid = mAccountbookid;
        left_icon.setVisibility(View.GONE);
        DownUrlUtil.searchTime(mAccountbookid, handler, RequsterTag.SEARCHTIME);
        DownUrlUtil.initBooksDataAndMember(mAccountbookid, handler, 0);
        if (InternetUtils.checkNet(MyApplicaction.getContext())) {
            BaserClassMode.getBooksCate(mAccountbookid);
        }

    }

    public void getBookDetails() {
        getBookDetailinfo(mAccountbookid);
    }

    private void initView() {
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
        }
        if (dailycostEntities == null) {
            dailycostEntities = new ArrayList<>();
        }
        names = new ArrayList<String>();
        date_time = new Date();
        calendar.setTime(date_time);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tabPageIndicator);
        noScrollViewPager = (NoScrollViewPager) view.findViewById(R.id.pager);
        myScroll = (MyScrollView) view.findViewById(R.id.myScroll);
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view); // 查看全部layout
        bagrou_image = (ImageView) view.findViewById(R.id.bagrou_image); //
        home_renovation_prompt_view = (HomeRenovationPromptView) view.findViewById(R.id.home_renovation_prompt_view); //装修账本弹框提示
        membersHorizontalScrollView = (MembersHorizontalScrollView) view.findViewById(R.id.membersHorizontalScrollView); //账本成员
        homeExpeseIncomView = (HomeExpeseIncomView) view.findViewById(R.id.homeExpeseIncomView); //预算收入支出消费栏
        homeSignoneView = (HomeSignoneView) view.findViewById(R.id.homeSignoneView); //记一笔
        top_layout = (LinearLayout) view.findViewById(R.id.top_layout);// 头部
        timeLine_top = (LinearLayout) view.findViewById(R.id.timeLine_top);// 时间显示器悬浮头部
        timeLine = (LinearLayout) view.findViewById(R.id.timeLine);// 时间显示器
        left_icon = (ImageView) view.findViewById(R.id.left_icon);//飞机
        initTime();
        membersHorizontalScrollView.setDeletMembersInterface(this);
        myScroll.setOnScrollListener(this);
        left_icon.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
        pullToRefreshLayout.setOnLoadMoreListenner(this);
        pullToRefreshLayout.setIsApha(true);
        pullToRefreshLayout.setImageScale(bagrou_image);
    }

    @Override
    public void onTabReselected(int position) {//账单列表顶部月份切换促发的回调
        isBookIn = false;
        needChangeMember = true;
        if (current_moths == fragments.size() - 1 && position != fragments.size() - 1) {
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, position - (current_moths - 1));
        } else {
            calendar.add(Calendar.MONTH, position - current_moths);
        }
        date_time = calendar.getTime();
        current_moths = position;
        if (booksDbInfo == null) {
            return;
        }
        if (position == fragments.size() - 1) {//预设的
            XzbUtils.hidePointInUmg(mContext, Constants.HIDE_TIME_LINE);
        }
        int countIndex = tabPageIndicator.getcount() - 1;
        int curentIndex = tabPageIndicator.getCurrentIndex();
        if (countIndex - 2 > curentIndex) {
            left_icon.setVisibility(View.VISIBLE);
        } else {
            left_icon.setVisibility(View.GONE);
        }
        pageIndex = 1;
        isRefresh = true;
        setSignOnBookInfo();
        ((SignOneBookFragment) fragments.get(current_moths)).setPagerHeight(this);
        if (InternetUtils.checkNet(mContext)) {
            getBookDetailinfo(mAccountbookid);
        } else {
            searchBills(date_time, RequsterTag.BILLS);
        }
    }

    @Override
    public void setPagerHeight(int height) {
        int[] fs = new int[2];
        noScrollViewPager.getLocationOnScreen(fs);
        if (!isAdded()) {
            return;
        }

        if (pageHeight == 0) {
            pageHeight = (int) (screeHeight - fs[1] - UIHelper.Dp2Px(mContext, 50));
            firstHeight = (int) (screeHeight - fs[1] - UIHelper.Dp2Px(mContext, 50));
        }


        float fslcation = XzbUtils.getStatusBarHeight(mContext) + UIHelper.Dp2Px(mContext, 70)
                + getResources().getDimension(R.dimen.titlebar_height);
        if (booksDbInfo != null && booksDbInfo.getAccountbooktype().equals("0")) {
//            fslcation = fslcation - UIHelper.Dp2Px(mContext, 70);
        }
        if (fs[1] < fslcation) {
            pageHeight = (int) (screeHeight - fslcation);

        } else {
            pageHeight = firstHeight;
        }
        if (height < pageHeight && height > 0) {
            if (tabPageIndicator != null) {
                if (tabPageIndicator.getParent() != timeLine) {
                    timeLine_top.removeView(tabPageIndicator);
                    timeLine.addView(tabPageIndicator);
                }
            }

        }
        if (noScrollViewPager.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noScrollViewPager.getLayoutParams();
            if (height == 0) {
                layoutParams.height = pageHeight;
            } else if (height < pageHeight) {
                layoutParams.height = firstHeight;
            } else {
                layoutParams.height = height;
            }
            noScrollViewPager.setLayoutParams(layoutParams);

        } else {
            LayoutParams layoutParams = (LayoutParams) noScrollViewPager.getLayoutParams();
            if (height == 0) {
                layoutParams.height = pageHeight;
            } else if (height < pageHeight) {
                layoutParams.height = firstHeight;
            } else {
                layoutParams.height = height;
            }
            noScrollViewPager.setLayoutParams(layoutParams);
        }


    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (home_renovation_prompt_view != null) {
            home_renovation_prompt_view.clearViewAnimation();
        }
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    String memberid = "";

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (booksDbInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.left_icon:// 飞机
                int countIndex = tabPageIndicator.getcount() - 1;
                tabPageIndicator.setCurrentItem(countIndex - 1);
                break;

            default:
                break;
        }

    }

    private List<TotalBalance> balances = new ArrayList<TotalBalance>();
    private List<BooksDbMemberInfo> booksDbMemberInfos = new ArrayList<BooksDbMemberInfo>();
    private BooksDbInfo booksDbInfo;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1111:
                    int height = (int) msg.obj;
                    if (top_layout != null) {
                        top_layout_height = height + UIHelper.Dp2Px(getActivity(), 25);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) bagrou_image
                                .getLayoutParams();
                        layoutParams.height = (int) (top_layout_height + getResources().getDimension(
                                R.dimen.titlebar_height));

                        bagrou_image.setLayoutParams(layoutParams);
                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.initImageHeight(layoutParams.height, (int) screeWith);

                        }
                    }
                    break;
                case 0://接收账本

                    BooksDbInfo booksDbInf = (BooksDbInfo) msg.obj;
                    if (booksDbInf != null) {
                        booksDbInfo = booksDbInf;
                        mAccountbookid = booksDbInfo.getAccountbookid();
                        Message message1 = Message.obtain();
                        message1.what = RequsterTag.SEARCHNEWBOOKMENBER;
                        message1.obj = booksDbInfo;
                        handler.sendMessage(message1);
                        getBookDetailinfo(mAccountbookid);
                        intHouse();
                    } else {
                        String iamge_url = "drawable://" + R.drawable.sing_icon;
                        if (bagrou_image != null) {
                            XzbUtils.displayImage(bagrou_image, iamge_url, 0);
                        }
                        getBookDetailinfo(mAccountbookid);
                        setTopheight();

                    }


                    break;
                case RequsterTag.SEARCHNEWBOOKMENBER:// 查询账本后跟新ui
                    BooksDbInfo booksDbIn = (BooksDbInfo) msg.obj;
                    if (booksDbIn != null) {
                        booksDbInfo = booksDbIn;
                        if (!TextUtils.isEmpty(booksDbInfo.getFirsttime())) {
                            long time = Long.parseLong(booksDbInfo.getFirsttime()) * 1000;
                            if (fistTimeLocation != 0) {
                                if (fistTimeLocation < time) {
                                    time = fistTimeLocation;
                                }
                            }

                            initTabFragment(time);
                        }
//                        tabPageIndicator.setAnimateToTab(current_moths);
                        mAccountbooktitle = booksDbInfo.getAccountbooktitle();
                        mAccountbookid = booksDbInfo.getAccountbookid();
                        isClear = booksDbInfo.getIsclear();
                        ((HomeActivity) mContext).setBooksInfo(booksDbInfo);
                        changeBoookData();
                        String imageurl = "file://" + DownUrlUtil.bookimage_new + booksDbInfo.getAccountbookid() + "_bg";
                        String book_image_url = booksDbInfo.getAccountbookcateicon();
                        String book_image = booksDbIn.getAccountbookbgimg();
                        if (bagrou_image != null) {
                            if (!TextUtils.isEmpty(book_image)) {
                                Object tag = bagrou_image.getTag(R.id.bagrou_image);
                                String url_tag = "";
                                if (tag == null) {
                                    url_tag = "";
                                } else {
                                    url_tag = bagrou_image.getTag(R.id.bagrou_image).toString();
                                    if (TextUtils.isEmpty(url_tag)) {
                                        url_tag = "";
                                    }
                                }
                                if (!book_image.equals(url_tag)) {
                                    bagrou_image.setTag(R.id.bagrou_image, book_image);
                                    ImageLoaderManager.loadImage(mContext, book_image, bagrou_image);
                                }


                            } else {
                                File file = new File(DownUrlUtil.bookimage_new + booksDbInfo.getAccountbookid() + "_bg");
                                if (file.exists()) {
                                    XzbUtils.displayImage(bagrou_image, imageurl, 0);
                                } else {
                                    if (booksDbInfo.getAccountbooktype().equals("0")) {
                                        imageurl = "drawable://" + R.drawable.sing_icon;
                                    } else {
                                        imageurl = "drawable://" + R.drawable.mumber_icon;
                                    }

                                    XzbUtils.displayImage(bagrou_image, imageurl, 0);
                                }
                                if (InternetUtils.checkNet(MyApplicaction.getContext()) && !TextUtils.isEmpty(book_image_url)) {
                                    if (book_image_url.contains("http")) {
                                        book_image_url.replace("fm", "bg");
                                    }
                                    if (!file.exists() || file.length() == 0) {
                                        String url_bg = book_image_url
                                                .substring(0, book_image_url.indexOf("@") + 1)
                                                + "3x.jpg";
                                        HashMap<String, String> stringHashMap = new HashMap<>();
                                        stringHashMap.put(booksDbInfo.getAccountbookid() + "_bg", url_bg);
                                        downImage(stringHashMap, DownUrlUtil.bookimage_new);
                                    }

                                    XzbUtils.displayImage(bagrou_image, book_image_url, 0);
                                }
                            }
                        }


                        if (booksDbInfo.getSorttype() != null) {
                            if (tabPageIndicator != null) {
                                if (booksDbInfo.getSorttype().equals("1")) {// 显示tab月份

                                    noScrollViewPager.setNoScroll(false);
                                    tabPageIndicator.setVisibility(View.VISIBLE);
                                    tabPageIndicator.setAnimateToTab(current_moths);
                                } else {
                                    noScrollViewPager.setNoScroll(true);
                                    tabPageIndicator.setVisibility(View.GONE);
                                }

                            }
                        }
                        List<BooksDbMemberInfo> lis = booksDbInfo.getMember();
                        if (lis != null) {
                            booksDbMemberInfos = lis;
                        }
                        if (booksDbMemberInfos != null) {
                            if (!needChangeMember) {
                                membersHorizontalScrollView.setBooksDbMemberInfos(booksDbIn, booksDbMemberInfos, date_time);
//                                initMumber();
                            }
                            needChangeMember = false;
                        }
                        memberid = ((HomeActivity) mContext).getMySelfNyMemeberId(booksDbInfo.getMyuid(), booksDbMemberInfos);
                        user_name = ((HomeActivity) mContext).getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbMemberInfos);
                        homeSignoneView.setBookInfo(booksDbIn, booksDbMemberInfos, date_time);
                        setTopheight();
                        setSignOnBookInfo();
                        searchBills(date_time, RequsterTag.BILLSLOCATIONSERVER);
                    }

                    break;
                case RequsterTag.SEARCHTIME:// 查询返回记账最早时间
                    if (msg != null) {
                        fistTimeLocation = (long) msg.obj;
//                        initTabFragment(time);
                    }


                    break;
                case RequsterTag.BILLSLOCATIONSERVER:// 时间轴切换的时候先查询本地有没有账单没有从服务器上拉取
                    List<DailycostEntity> dailycostEntityList = (List<DailycostEntity>) msg.obj;
//                    RequsterTag.isSynchronizationing = false;
                    finishLoadAndFresh();
                    if (dailycostEntityList == null || dailycostEntityList.size() == 0) {
                        getBills(false, false);
                    } else {
                        Message message = Message.obtain();
                        message.obj = dailycostEntityList;
                        message.what = RequsterTag.BILLS;
                        handler.sendMessage(message);
                    }

                    break;
                case RequsterTag.BILLS:
                    List<DailycostEntity> dailycost = (List<DailycostEntity>) msg.obj;
//                    RequsterTag.isSynchronizationing = false;
                    finishLoadAndFresh();
                    if (dailycost.size() < 20) {
                        pullToRefreshLayout.setHasMore(false);
                    }
                    if (isRefresh) {
                        dailycostEntities.clear();
                    }
                    if (dailycost.size() > 0) {
                        for (int i = 0; i < dailycost.size(); i++) {
                            dailycostEntities.add(dailycost.get(i));
                        }

                    }
                    if (fragments.size() > current_moths) {
                        ((SignOneBookFragment) fragments.get(current_moths)).setbillListInfoItems(dailycostEntities);
                    }

                    break;
                case RequsterTag.GETBILLS:
                    if (booksDbInfo == null) {
                        return;
                    }
                    getBills(false, true);
                    break;

                case RequsterTag.SEARCHBOOKDETAIL:// 重新拉去账本详情
                    if (booksDbInfo == null) {
                        return;
                    }
                    boolean isNeedBookDetail = (boolean) msg.obj;
                    if (isNeedBookDetail) {
                        getBookDetailinfo(booksDbInfo.getAccountbookid());
                    } else {
                        setSignOnBookInfo();
                        searchBills(date_time, RequsterTag.BILLS);
                    }


                    break;
                case RequsterTag.SYNCHRONIZATION_FAL:// 同步失败
                    handler_sys.removeCallbacks(runnable);
                    isRefreshing = false;
                    if (RequsterTag.isNeedSynchronizationing) {
                        ((HomeActivity) mContext).setSysfail();
                    }

                    break;
                case RequsterTag.SYNCHRONIZATION_SUS:// 同步成功
                    handler_sys.removeCallbacks(runnable);
                    isRefreshing = false;
                    if (RequsterTag.isNeedSynchronizationing) {
                        ((HomeActivity) mContext).setSysSuccess();
                    }

                    break;
                case RequsterTag.BOOKSSYS_SUS:// 同步账本成功后

                    getBookDetailinfo(LoginConfig.getInstance().getBookId());
                    break;
                case RequsterTag.SEARCHDAICOYS:
//                    if (booksDbInfo != null) {//跟新账单
////                        setSignOnBookInfo();
//                        DownUrlUtil.searchTime(mAccountbookid, handler, RequsterTag.SEARCHTIME);
//                    }

                    break;
                case RequsterTag.BOOKSDBINFO:
                    List<BooksDbInfo> booksDbInfos = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfos.size() > 0) {
                        BooksDbInfo dbInfo1 = booksDbInfos.get(0);
                        if (dbInfo1 != null) {
                            booksDbInfo = dbInfo1;
                            mAccountbookid = booksDbInfo.getAccountbookid();
                            LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
                            getBookDetailinfo(mAccountbookid);
                        }
                    } else {
                        Intent intent = new Intent(mContext, ChooseBookActivity.class);
                        startActivity(intent);
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

    private void downImage(HashMap<String, String> hashMap, String downloadPath) {
        DownUrlUtil downUrlUtil = new DownUrlUtil(downloadPath, hashMap, new DownUrlUtil.DownloadStateListener() {

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailed() {
                // TODO Auto-generated method stub

            }
        });
        downUrlUtil.startDownload();
    }

    private void setTopheight() {
        if (top_layout != null) {
            top_layout.post(new Runnable() {

                @Override
                public void run() {
                    int height = top_layout.getHeight();
                    Message msg = Message.obtain();
                    msg.what = 1111;
                    msg.obj = height;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private void initTabFragment(long time) {
        long cTime = time;
        Date date = new Date();
        calendar.setTime(date);
        int moth = calendar.get(Calendar.MONTH) + 1;//
        if (names != null && names.size() > 0) {
            names.clear();
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
        for (int i = 0; i < moths; i++) {

            long calendar_time = DateUtil.stringToTime(calendar.getTimeInMillis());// 开始时间
            names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            fragments.add(new SignOneBookFragment());

            if (calendar_time < cTime) {// 显示记账的前一个月
                calendar.add(Calendar.MONTH, -1);

                names.add(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");

                fragments.add(new SignOneBookFragment());
                break;
            }
            calendar.add(Calendar.MONTH, -1);
        }
        Collections.reverse(names);
        Collections.reverse(fragments);

        names.add("预设的");
//        current_moths = names.size() - 2;


        fragments.add(new SignOneBookFragment());

        String tabName = DateUtil.formatTheDateToMM_dd(date_time, 5) + "年" + Integer.parseInt(DateUtil.formatTheDateToMM_dd(date_time, 3)) + "月";
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        long start_time = DateUtil.stringToTime(calendar.getTimeInMillis()) - 1000;// 开始时间
        long current_time = date_time.getTime();
        if (date_time.getTime() > start_time) {
            tabName = "预设的";
        }
        int currentIndex = 0;
        if (isBookIn) {
            date_time = new Date();
            left_icon.setVisibility(View.GONE);
            current_moths = names.size() - 2;
            currentIndex = names.size() - 2;
        } else {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                if (name.equals(tabName)) {
                    current_moths = i;
                    currentIndex = i;
                    break;
                }
            }
        }

        calendar.setTime(date_time);

        pagerAdapter.notifyDataSetChanged();
        current_moths = currentIndex;
        tabPageIndicator.notifyDataSetChanged();
        tabPageIndicator.setCurrentItem(currentIndex);
        ((SignOneBookFragment) fragments.get(currentIndex)).setPagerHeight(this);


        noScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTime() {
        pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), names, fragments);
        noScrollViewPager.setAdapter(pagerAdapter);
        noScrollViewPager.setNoScroll(false);
        noScrollViewPager.setOffscreenPageLimit(3);
        tabPageIndicator.setViewPager(noScrollViewPager);
        tabPageIndicator.setOnTabReselectedListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示

        } else {// 重新显示到最前端中
        }
    }

    @Override
    protected void lazyLoad() {
        if (view == null || !isPress || !isVisible) {
            return;
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        home_renovation_prompt_view.clearViewAnimation();
    }

    // 1、一个类中可以存在多个接收消息的函数，只要同名函数的参数类型不同即可，EventBus在post消息的后，接收消息端会根据参数类型调用对应接收消息的函数。
    //
    // 2、接收消息函数的访问权限必须是public，且只能有一个参数
    public void onEventMainThread(IntentMessageBean messageBean) {
        String budget = messageBean.getBudget();

        if (booksDbInfo == null) {
            return;
        }
        double budget_float = Double.parseDouble(budget) - Double.parseDouble(booksDbInfo.getPayamount());
        String budget_floatString = XzbUtils.formatDouble("%.2f", budget_float);
//        yusuann.setText(StringUtils.moneySplitComma(budget_floatString));
        booksDbInfo.setAccountbookbudget(budget);
        booksDbInfo.setBudgetdiff(budget_floatString);
//        homePresenter.initSingTitle(booksDbInfo, date_time);
        homeExpeseIncomView.setBookInfo(booksDbInfo, booksDbMemberInfos, date_time);
//        initSingTitle();
    }

    public void onEventMainThread(UserInfoChangeEvent messageBean) {
        membersHorizontalScrollView.setBooksDbMemberInfos(booksDbInfo, booksDbMemberInfos, date_time);
//        initMumber();
    }

    public void finishLoadAndFresh() {
        pullToRefreshLayout.finishLoadAndFresh();
    }

    private void changeBoookData() {
        mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        if (booksDbInfo.getAccountbooktype() == null) {
            return;
        }
        int magin = 0;
        int magintop = 0;
//        expese_text.setBackgroundResource(0);
//        expese_text.setTextColor(getResources().getColor(R.color.home_text_color));
        homeExpeseIncomView.setbugetImage(booksDbInfo.getIsclear());
        if (membersHorizontalScrollView == null) {
            return;
        }
        String mAccountbookcount = booksDbInfo.getAccountbookcount();
        if (TextUtils.isEmpty(mAccountbookcount) || mAccountbookcount.equals("0")) {
            // 单人记账
            membersHorizontalScrollView.setViewVisible(View.GONE);
            magin = UIHelper.dip2px(mContext, getResources().getDimension(R.dimen.margin_left_5));
            magintop = UIHelper.dip2px(mContext, getResources().getDimension(R.dimen.margin_left_3));
        } else {// 多人账本
            membersHorizontalScrollView.setViewVisible(View.VISIBLE);
            magin = 0;
            magintop = 0;
        }

        homeExpeseIncomView.setBookInfo(booksDbInfo, booksDbMemberInfos, date_time);
        homeExpeseIncomView.setLinealayoutTopButtom(magin, magintop);
    }

    private void setSignOnBookInfo() {
        if (fragments == null || fragments.size() == 0 || (SignOneBookFragment) fragments.get(current_moths) == null) {
            return;
        }
        setbillFragmen();

    }

    @Override
    public void onScroll(int scrollY) {
        int height = top_layout_height;
        if (booksDbInfo != null && booksDbInfo.getAccountbooktype().equals("0")) {
            height = top_layout_height - UIHelper.Dp2Px(mContext, 25);
        } else {
            scrollY = scrollY + UIHelper.Dp2Px(mContext, 25);
        }

        if (scrollY >= height) {
            if (tabPageIndicator.getParent() != timeLine_top) {
                timeLine.removeView(tabPageIndicator);
                timeLine_top.addView(tabPageIndicator);
            }
        } else {
            if (tabPageIndicator.getParent() != timeLine) {
                timeLine_top.removeView(tabPageIndicator);
                timeLine.addView(tabPageIndicator);
            }
        }
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        // if (pageIndex <= maxPage) {


//        if (!RequsterTag.isSynchronizationing) {
//            RequsterTag.isNeedSynchronizationing = false;
//            pageIndex++;
//            isRefresh = false;
//            RequsterTag.isSynchronizationing = true;
//            searchBills(date_time, RequsterTag.BILLS);
//            TaskQueue taskQueue = TaskQueue.mainQueue();
//            taskQueue.executeDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    RequsterTag.isSynchronizationing = false;
//                }
//            }, 2000);
//        } else {
//            finishLoadAndFresh();
//        }
        finishLoadAndFresh();
    }

    Handler getHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finishLoadAndFresh();
        }
    };


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作

        if (booksDbInfo == null) {
            return;
        }
        RequsterTag.isNeedSynchronizationing = true;
        if (!RequsterTag.isSynchronizationing) {
            isRefresh = true;
            pageIndex = 1;
            pullToRefreshLayout.setHasMore(true);
            isRefreshing = true;
            setSysBooks();

        }
        finishLoadAndFresh();
    }

    public void setSysBooks() {
        String jsonString = LoginConfig.getInstance().getJsonbook(mAccountbookid);
        BaserClassMode.getBooksCate(mAccountbookid);
        onComBack();
    }


    private void setbillFragmen() {
        ((SignOneBookFragment) fragments.get(current_moths)).setBillList(booksDbInfo.getAccountbookid(), memberid,
                booksDbInfo.getMyuid(), booksDbInfo.getAccountbookcount(), booksDbInfo.getAccountbookcatename(),
                booksDbInfo.getAccountbooktitle(), date_time, booksDbInfo.getSorttype(), booksDbInfo.getIsclear());
    }

    /**
     * 重新拉去账本列表
     */
    public void initBooks() {
        date_time = new Date();
        calendar = Calendar.getInstance();
        isBookIn = true;

        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack<BooksListInfo>() {
            @Override
            public void onSuccess(BooksListInfo o) throws Exception {
                super.onSuccess(o);
                BooksListInfo booksListInfo = o;
                List<MyBooksListInfo> myBooksListInfos = new ArrayList<MyBooksListInfo>();
                BooksCatelistInfo booksCatelistInfo = booksListInfo.getCatelist();
                if (booksCatelistInfo == null) {
                    return;
                }
                List<MyBooksListInfo> single = booksCatelistInfo.getSingle();
                if (single != null) {
                    for (MyBooksListInfo myBooksListInfo : single) {
                        myBooksListInfos.add(myBooksListInfo);
                    }
                }
                List<MyBooksListInfo> multiple = booksCatelistInfo.getSingle();
                if (multiple != null) {
                    for (MyBooksListInfo myBooksListInfo : multiple) {
                        myBooksListInfos.add(myBooksListInfo);
                    }
                }
                List<BooksDbInfo> booksDbInfos = booksListInfo.getAccountbook();

                String bookid = "";
                long temp = 0;
                int position = 0;
                if (booksDbInfos == null || booksDbInfos.size() == 0) {
                    Intent intent = new Intent(mContext, ChooseBookActivity.class);
                    startActivity(intent);
                    return;
                }
                if (booksDbInfos != null) {
                    for (int i = 0; i < booksDbInfos.size(); i++) {
                        long ltime = Long.parseLong(booksDbInfos.get(i).getAccountbookltime());
                        if (ltime > temp) {
                            temp = ltime;
                            position = i;
                        }

                        BooksDbInfo booksDbInfo = booksDbInfos.get(i);
                        booksDbInfo.setIssynchronization("true");
                        booksDbInfos.set(i, booksDbInfo);

                    }
                    booksDbInfo = booksDbInfos.get(position);
                    bookid = booksDbInfo.getAccountbookid();

                    mAccountbookid = bookid;
                    LoginConfig.getInstance().setBookId(mAccountbookid);
                    DownUrlUtil.myBooks(booksDbInfos);
                    getBills(true, true);
                    BaserClassMode.getBooksCate(mAccountbookid);//获取账单分类icon
//                    getBookDetailinfo(bookid);

                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);

            }


        });
    }


    public void getBookDetailinfo(final String mAccountid) {
        if (booksDbInfo == null) {
            return;
        }
        long start_time = DateUtil.getStartTime(date_time);//开始时间
        long end_time = DateUtil.getEndTime(date_time);// 结束时间
        String string_start_time = start_time + "";
        String string_end_time = end_time + "";
        if (booksDbInfo.getSorttype().equals("0")) {
            string_start_time = "";
            string_end_time = "";
        }
        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountid,
                "stime", string_start_time, "etime", string_end_time);

        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {


            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo booksDbI = o;

                if (booksDbI == null) {
                    return;
                }
                booksDbInfo = booksDbI;
                mAccountbookid = booksDbInfo.getAccountbookid();
                String sorttype = booksDbInfo.getSorttype();
                String budgetkey = DateUtil.getBudgetkey(mAccountbookid, DateUtil.getYearMoth(date_time), sorttype);
                String locationBudget = LoginConfig.getInstance().getBudget(budgetkey);
                if (!locationBudget.equals("0")) {
                    LoginConfig.getInstance().setBudget(budgetkey,
                            booksDbInfo.getAccountbookbudget());
                }

                LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
                isClear = booksDbInfo.getIsclear();
                booksDbInfo.setIssynchronization("true");
                booksDbInfo.setIsdelet("false");
                booksDbInfo.setIsnew("0");
                List<BooksDbMemberInfo> booksDbMemberInfos1 = new ArrayList<BooksDbMemberInfo>();
                List<BooksDbMemberInfo> MemberInfos = booksDbInfo.getMember();
                if (MemberInfos != null) {
                    for (BooksDbMemberInfo booksDbMemberInfo : MemberInfos) {
                        booksDbMemberInfo.setBookid(booksDbInfo.getAccountbookid());
                        booksDbMemberInfos1.add(booksDbMemberInfo);
                    }
                }
                booksDbInfo.setMember(booksDbMemberInfos1);
                DownUrlUtil.updateBook(booksDbInfo, mAccountid);
                Message message = new Message();
                message.what = RequsterTag.SEARCHNEWBOOKMENBER;
                message.obj = booksDbInfo;
                handler.sendMessage(message);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    ((HomeActivity) mContext).showToast(simleMsg);
                } else {
                    if (simleMsg.getErrCode() == 50099) {
                        ((HomeActivity) mContext).showSimpleAlertDialog("提示", simleMsg.getErrMsg(), "确定", false, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((HomeActivity) mContext).dismissDialog();
                                //账本id不存在重新请求账本
                                initBooks();
                            }
                        });
                    } else {
                        //账本id不存在重新请求账本
                        initBooks();
                    }


                }


            }

            @Override
            public void onFinish() {
                super.onFinish();
                isRefreshing = false;
            }
        });

    }

    /**
     * 拉取账单
     *
     * @param mContext
     * @param booksDbInfo
     * @param date_time
     * @param pageIndex
     */
    public void initBoosIntenet(final Context mContext, final BooksDbInfo booksDbInfo, Date date_time,
                                int pageIndex, final Handler handler, final int what, final boolean isLogin, final boolean isNeedBookDetail) {
        if (booksDbInfo == null) {
            return;
        }
        final String mAccountbookid = booksDbInfo.getAccountbookid();
        final boolean isExpectMoth = DateUtil.isExpectMoth(date_time);
        long start_time = DateUtil.getStartTime(date_time);//开始时间
        long end_time = DateUtil.getEndTime(date_time);// 结束时间
        String string_start_time = start_time + "";
        String string_end_time = end_time + "";
        String string_pageIndex = pageIndex + "";

        if (booksDbInfo.getSorttype().equals("1")) {
            if (isLogin) {//是否是登录进来的
                string_start_time = "";
                string_end_time = "";
                string_pageIndex = "";
            }
        } else {
            string_start_time = "";
            string_end_time = "";
            string_pageIndex = "";
        }
        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountbookid, "memberid", "",
                "stime", string_start_time, "etime", string_end_time, "page", string_pageIndex);
        final long finalStart_time = start_time;
        final long finalEnd_time = end_time;
        CommonFacade.getInstance().exec(Constants.BOOKS_BILL, hashMap, new ViewCallBack<BillListInfo>() {
            @Override
            public void onSuccess(BillListInfo o) throws Exception {
                super.onSuccess(o);
                BillListInfo billListInfo = o;
                List<DailycostEntity> list = new ArrayList<DailycostEntity>();
                BillInfo billInfo = billListInfo.getData();
                if (billInfo == null) {
                    return;
                }
                List<DailycostEntity> getList = billInfo.getList();
                if (getList == null) {
                    return;
                }
                for (int i = 0; i < getList.size(); i++) {
                    DailycostEntity dailycostEntity = getList.get(i);

                    dailycostEntity.setIssynchronization("true");
                    dailycostEntity.setAccountbooktype(booksDbInfo.getAccountbooktype());
                    dailycostEntity.setIsclear(booksDbInfo.getIsclear());
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

                    list.add(dailycostEntity);

                }
                DownUrlUtil.synchronization(mAccountbookid, list, booksDbInfo.getSorttype(), finalStart_time, finalEnd_time, handler,
                        what, isLogin, isNeedBookDetail);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);
            }
        });


    }

    public void getBills(boolean isLogin, boolean isNeedBookDetail) {
        RequsterTag.sysTime = new Date().getTime();
        initBoosIntenet(getActivity(), booksDbInfo, date_time, 1, handler,
                RequsterTag.SEARCHBOOKDETAIL, isLogin, isNeedBookDetail);

    }

    Handler handler_sys = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

//            handler_sys.post(runnable);
        }
    };
    private int index = 0;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (booksDbInfo != null) {
                DownUrlUtil.seachUnSys(getActivity(), booksDbInfo, handler);
            } else {
                handler_sys.removeCallbacks(runnable);
                isRefreshing = false;
                RequsterTag.isSynchronizationing = false;
                ((HomeActivity) mContext).setSysfail();
            }
        }
    };

    public void onComBack() {
        if (!RequsterTag.isSynchronizationing) {
            if (RequsterTag.isNeedSynchronizationing) {
                ((HomeActivity) mContext).setSysing();
            }

            RequsterTag.isSynchronizationing = true;
            handler_sys.post(runnable);
        }
    }

    public void setScrollToTop() {

        if (myScroll != null) {
            if (tabPageIndicator.getParent() != timeLine) {
                timeLine_top.removeView(tabPageIndicator);
                timeLine.addView(tabPageIndicator);
            }
            myScroll.smoothScrollTo(0, 0);
        }
    }

//    @Override
//    public boolean onLongClick(View v) {
//        switch (v.getId()) {
//            case R.id.sign_button:
//                try {
//                    if (booksDbInfo != null) {
//                        IntentUtils.setReflectionjump(mContext, WriteJournalActivity.class.getName(), "longdate", date_time.getTime() / 1000 + "", "accountbookid", booksDbInfo.getAccountbookid());
//                    }
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//
//
//        return false;
//    }

    public void searchBills(final Date record_date, final int what) {

        this.date_time = record_date;
        if (date_time == null) {
            return;
        }
        if (view != null) {
            TaskQueue taskQueue = TaskQueue.mainQueue();
            taskQueue.executeDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isExpectMoth = DateUtil.isExpectMoth(date_time);

                    DownUrlUtil.initDataReturnDailycostEntitys(date_time, mAccountbookid, booksDbInfo.getSorttype(), "", isExpectMoth, pageIndex, pagerSize,
                            handler, what);
                }
            }, 200);

        }

    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    @Override
    public void quitMemberDeleBook(String mAccountbookid) {
        String where = DailycostContract.DtBooksColumns.ACCOUNTBOOKID + "=? ";
        String[] selectionArgs = {mAccountbookid};
        DownUrlUtil.deleBook(where, selectionArgs, handler, RequsterTag.DELETBOOK);
    }
}
