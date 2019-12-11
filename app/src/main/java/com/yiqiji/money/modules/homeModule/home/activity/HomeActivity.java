package com.yiqiji.money.modules.homeModule.home.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.activity.BookSharePreviewActivity;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.LocationServer;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.FaceConversionUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyTitleLayout;
import com.yiqiji.money.modules.community.discover.fragment.DiscoverFragment;
import com.yiqiji.money.modules.homeModule.home.adapter.NotesAdapter;
import com.yiqiji.money.modules.homeModule.home.adapter.TabAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CheckVesionInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CheckVwsionItem;
import com.yiqiji.money.modules.homeModule.home.entity.LoginLaterInfo;
import com.yiqiji.money.modules.homeModule.home.entity.MessageUrlInfo;
import com.yiqiji.money.modules.homeModule.home.entity.ShareInfo;
import com.yiqiji.money.modules.homeModule.home.fragment.BookDetailsFragment;
import com.yiqiji.money.modules.homeModule.home.homeinterface.NotesItemClick;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.AddBookActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.BooksActivity;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.IntentPerecenter;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;
import com.yiqiji.money.modules.myModule.my.activity.QuestionReturnActivity;
import com.yiqiji.money.modules.myModule.my.fragment.MineFragment;
import com.yiqiji.money.modules.property.fragment.PropertyFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class HomeActivity extends BaseActivity implements OnClickListener, TabAdapter.OnTabChangeListener, NotesItemClick {
    private RadioGroup radiogroup_main;
    private View unlogin_prompt_include;// 没有登录提示
    private TextView login_now;
    private TextView longding;

    private MyTitleLayout title_singe_book;
    private List<Fragment> fragments;

    private String mAccountbooktitle = "";
    private Date date_time;
    private boolean isShow = false;// 是否有未读消息 false：没有 true： 有
    private String[] tabNames = new String[]{"日常账本", "财富", "发现", "我的"};
    private TabAdapter adapter;
    private NotesAdapter notesAdapter;
    private int fragmentIndex = 0;
    private String isClear = "0";// 0:不需要结算 1：需要结算
    private String memberid;
    private String mAccountbookid;
    private BooksDbInfo booksDbInfo;
    private String bookUserName = "";
    CheckMessageInfo.MessgeinfoItem messgeinfoItem = null;
    LocationServer locationServer;
    private ImageView layout_title_view_return;
    private boolean isOpen = true;//是否是重新打开app

    private String[] shareText = new String[]{"微信", "新浪微博", "QQ"};
    private int share_icon[] = new int[]{R.drawable.chart_icon, R.drawable.weibo_icon, R.drawable.qq};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ};

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void openActivity(Context context, BooksDbInfo booksDbInfo) {
        // 不是则这个操作会返回homeActivity
        Intent in = new Intent(context, HomeActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("BooksDbInfo", booksDbInfo);
        in.putExtras(mBundle);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singe_book);
        LoginConfig.getInstance().isFirstStartApp(false);
        EventBus.getDefault().register(this);
        LoginConfig.getInstance().setHomecreate(true);
        mAccountbooktitle = LoginConfig.getInstance().getBookname();
        mAccountbookid = LoginConfig.getInstance().getBookId();
        if (TextUtils.isEmpty(mAccountbooktitle)) {
            mAccountbooktitle = "日常账本";
        }
        TaskQueue taskQueue = TaskQueue.mainQueue();
        taskQueue.execute(new Runnable() {
            @Override
            public void run() {
                // 表情资源
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        });

        initView();
        initTitle();
        initFragment();
        setRegistBreoadcast();
        onCreateAndNewIntent(getIntent());
        isLoginIn(getIntent());

        initCheckVesion();//检测是否强制升级
        checkIsShowCommentDialog();
    }

    /**
     * 检测是否需要弹出去评分对话框
     */
    private void checkIsShowCommentDialog() {
        if (!"-1".equals((String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "showCommentScore", "-1"))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long day = 0;
            try {
                Date begin = sdf.parse(DateUtil.getTodayDate());
                Date end = sdf.parse(DateUtil.getTodayDate());
                day = (begin.getTime() - end.getTime()) / (24 * 60 * 60 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (day >= 3) {
                showSimpleAlertDialog("邀请你给一起记评分", "你的建议我们十分重视，我们期待和你一起做一款卓越的产品", "忍不住去赞", "",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_GOOD_COMMENT);
                                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(mContext, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
                                }
                                dismissDialog();
                                SPUtils.setParam(LoginConfig.getInstance().getUserid() + "showCommentScore", "-1");
                            }
                        },
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_PROBLEM_FADBACK);
                                Intent intent = new Intent(mContext, QuestionReturnActivity.class);
                                startActivity(intent);
                                dismissDialog();
                            }
                        }, false, true);
            }

        }
    }


    public void onCreateAndNewIntent(Intent intent) {
//        setUnloginPromptVisible();
        isJumpConvention(intent);
        checkMessage();

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setUnloginPromptVisible();
        if (!XzbUtils.isForn) {// app切换到前台同步数据
            XzbUtils.isForn = true;
            RequsterTag.isNeedSynchronizationing = false;
            ((BookDetailsFragment) fragments.get(0)).onComBack();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        isOpen = true;
        boolean isBookIn = false;//是否是从账本跳转
        boolean isLoginJump = intent.getBooleanExtra("action", false);//是否是登录跳转
        BooksDbInfo booksDbInf = intent.getParcelableExtra("BooksDbInfo");
        final String isShow = intent.getStringExtra("isShow");//是否是添加账单后跳转：true显示
        if (!isLoginJump) {
            if (booksDbInf != null) {
                isBookIn = initHomeIntent(booksDbInf);

            } else {
                String mTradetimeString = intent.getStringExtra("mTradetime");
                if (!TextUtils.isEmpty(mTradetimeString)) {
                    Long mTradetime = Long.parseLong(mTradetimeString);
                    ((BookDetailsFragment) fragments.get(0)).setDate_time(new Date(mTradetime));
                }
                if (!TextUtils.isEmpty(isShow)) {
                    if (isShow.equals("true")) {
                        billAddShow();
                    } else {
                        //不处理
                    }
                }
                mAccountbookid = LoginConfig.getInstance().getBookId();
                if (XzbUtils.isForn) {
                    RequsterTag.isNeedSynchronizationing = false;
                    ((BookDetailsFragment) fragments.get(0)).onComBack();
                }
            }
            ((BookDetailsFragment) fragments.get(0)).setIsBookIn(isBookIn);
            ((BookDetailsFragment) fragments.get(0)).setOnnewIntent(mAccountbookid);
            onCreateAndNewIntent(intent);
        }
        isLoginIn(intent);
    }

    private boolean initHomeIntent(BooksDbInfo booksDbInf) {
        boolean isBookIn;
        isBookIn = true;
        this.booksDbInfo = booksDbInf;
        mAccountbookid = booksDbInfo.getAccountbookid();
        mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        isClear = booksDbInfo.getIsclear();
        LoginConfig.getInstance().setBookId(mAccountbookid);
        return isBookIn;
    }

    public void isLoginIn(Intent intent) {
        boolean isLoginJump = intent.getBooleanExtra("action", false);//是否是登录跳转
        if (isLoginJump) {
            unlogin_prompt_include.setVisibility(View.GONE);
            getUserinfo(LoginConfig.getInstance().getTokenId(), false);
        }
    }

    /**
     * 推送跳转
     *
     * @param intent
     */
    private void isJumpConvention(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (TextUtils.isEmpty(extra)) {
                    return;
                }
                MessageUrlInfo bean = GsonUtil.GsonToBean(extra, MessageUrlInfo.class);
                String url = bean.getUrl();
                BaserClassMode.jumpConvention(this, url);
            }
        }

    }

    private void setUnloginPromptVisible() {
        String tokenid = LoginConfig.getInstance().getTokenId();
        if (unlogin_prompt_include == null) {
            return;
        }
        if (InternetUtils.checkNet(MyApplicaction.getContext())) {
            if (!TextUtils.isEmpty(tokenid)) {
                unlogin_prompt_include.setVisibility(View.GONE);
            } else {
                if (adapter != null && adapter.getCurrentFragmentIndex() == 0) {
                    unlogin_prompt_include.setVisibility(View.VISIBLE);
                } else {
                    unlogin_prompt_include.setVisibility(View.GONE);
                }
            }
        } else {
            unlogin_prompt_include.setVisibility(View.GONE);
        }

    }

    private void initView() {
        title_singe_book = (MyTitleLayout) findViewById(R.id.my_title);
        radiogroup_main = (RadioGroup) findViewById(R.id.radiogroup_main);
        unlogin_prompt_include = (View) findViewById(R.id.unlogin_prompt_include);
        login_now = (TextView) findViewById(R.id.login_now);
        longding = (TextView) findViewById(R.id.longding);
        layout_title_view_return = (ImageView) findViewById(R.id.layout_title_view_return);
//        radiogroup_main.setOnCheckedChangeListener(this);
        login_now.setOnClickListener(this);

    }

    private void initTitle() {
        initTitle(mAccountbooktitle, R.color.home_text_color, 0, R.color.alpha, R.drawable.book_change, R.drawable.message_note, R.drawable.statistics_picture, isShow, this, this);
    }

    private MineFragment mineFragment;

    private void initFragment() {
        date_time = new Date();
        fragments = new ArrayList<Fragment>();
        fragments.add(new BookDetailsFragment());

        fragments.add(new PropertyFragment());
        fragments.add(new DiscoverFragment());
        mineFragment = new MineFragment();
        fragments.add(mineFragment);


        adapter = new TabAdapter(this, radiogroup_main, R.id.fl_content, fragments, fragmentIndex);
        adapter.setTachangeListener(this);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 999:
                    longding.setVisibility(View.GONE);

                    break;
                case RequsterTag.SYNCHRONIZATION_CHANGE:// 同步后跟新title
//                    setSysAfterTile();
                    break;
//                case RequsterTag.CHECKMESSAGE:// 检测消息
//                    messgeinfoItem = (CheckMessageInfo.MessgeinfoItem) msg.obj;
//                    if (adapter.getCurrentFragment() instanceof BookDetailsFragment) {
//                        setBookNoteShow(Math.min(99, messgeinfoItem.getNewbook()));
//                        setMessageNoteShow(Math.min(99, messgeinfoItem.getNewmsg()));
//                        if (messgeinfoItem.getNewmsg() > 0 && !XzbUtils.areNotificationsEnabled(mContext)) {
//                            SPUtils.setParam(LoginConfig.getInstance().getUserid() + "hasNewMsg", true);
//                        }
//                    }
//                    break;

                default:
                    break;
            }
        }

    };


    public void onEventMainThread(String updateBill) {
        if (updateBill.equals("updatebill")) {
            ((BookDetailsFragment) fragments.get(0)).onComBack();
        } else if (updateBill.equals("getBills")) {
            ((BookDetailsFragment) fragments.get(0)).getBills(false, true);
        } else if (updateBill.equals("fail")) {
            if (RequsterTag.isNeedSynchronizationing) {
                setSysfail();
            }

        } else if (updateBill.equals("success")) {
            if (RequsterTag.isNeedSynchronizationing) {
                setSysSuccess();
            }

        } else if (updateBill.equals("updatebook")) {
            ((BookDetailsFragment) fragments.get(0)).getBookDetails();
        }
    }

    public void onEventMainThread(CheckMessageInfo checkMessageInfo) {

        checkMessage();
    }

    public void onEventMainThread(LoginLaterInfo laterInfo) {
        unlogin_prompt_include.setVisibility(View.GONE);
        getUserinfo(LoginConfig.getInstance().getTokenId(), false);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        if (booksDbInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.layout_title_view_return:// 切换账本
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_ACCOUNT_CHANGE);
                intent = new Intent(HomeActivity.this, BooksActivity.class);
                intent.putExtra("bookName", mAccountbooktitle);
                intent.putExtra("bookid", booksDbInfo.getAccountbookid());
                // intent.putExtra("bookid", booksDbInfo.getAccountbookid());
                startActivityForResult(intent, RequsterTag.BOOKS_CHANGE);
                overridePendingTransition(0, 0);

                break;
            case R.id.layout_title_view_right_img_btn:
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_MORE_BTN);
                initPopuwindows();
                break;
            case R.id.layout_title_view_sencond_right_img_btn:// 统计
                if (booksDbInfo == null) {
                    return;
                }
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_STATICS_ENTER);
                StatisticsActivity.openActivity(this, date_time, mAccountbooktitle, booksDbInfo.getAccountbookid(), bookUserName, booksDbInfo.getSorttype(), booksDbInfo.getAccountbookcatename(), Long.parseLong(booksDbInfo.getFirsttime()), false);
//                intent = new Intent(HomeActivity.this, StatisticsActivity.class);
//                intent.putExtra("data", date_time);
//                intent.putExtra("bookName", mAccountbooktitle);
//                intent.putExtra("bookid", booksDbInfo.getAccountbookid());
//                bookUserName = getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbInfo.getMember());
//                intent.putExtra("bookUserName", bookUserName);
//                intent.putExtra("sorttype", booksDbInfo.getSorttype() + "");
//                intent.putExtra("mAccountbookcatename", booksDbInfo.getAccountbookcatename());
//                startActivity(intent);
                break;

            case R.id.layout_title_view_title:

                break;
            case R.id.login_now:
                LoginBaseActivity.openActivity(this);
//                IntentUtils.startActivityOnLogin(this, IntentUtils.LoginIntentType.MINE);
                break;

            default:
                break;
        }
    }

    /**
     * 初始化多人记账右上角列表
     */
    private void initPopuwindows() {
        ImageView layou_list = (ImageView) findViewById(R.id.layout_title_view_right_img_btn);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_books_note_list, null);
        LinearLayout linearLayoutGroup = (LinearLayout) view.findViewById(R.id.notes_layout);
        notesAdapter = new NotesAdapter(this, view, R.layout.activity_books_note_item, layou_list);
//        notesAdapter = new NotesAdapter(this, view, R.layout.activity_books_note_item);
        notesAdapter.setNotesItemClick(this);
        int newmsgposition = -1;
        if (messgeinfoItem != null && messgeinfoItem.getNewmsg() > 0) {
            newmsgposition = messgeinfoItem.getNewmsg();
        }
//        List<HashMap<Integer, Integer>> hashMapList = NotesModlePerecenter.getHashMapsStringidimageid(booksDbInfo, newmsgposition);
//        notesAdapter.setNotesHasImageList(layou_list, linearLayoutGroup, newmsgposition, hashMapList);
//        notesAdapter.setHashMapsStringid(booksDbInfo,newmsgposition);
//        notesAdapter.setNotesHasImageList(layou_list,linearLayoutGroup,);
        notesAdapter.setNotesList(booksDbInfo, newmsgposition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RequsterTag.BOOKS_CHANGE: // 用于点击上面的账本类型后，选则账本类型后携带返回
                break;
            case RequsterTag.REQUESCODE_PICTURELIST: // 用于在
                mineFragment.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                /** 用于分享attention to this below ,must add this **/
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        handler_sys.removeCallbacks(runnable);
        LoginConfig.getInstance().setHomecreate(false);
        setUnRegistBreoadcast();
        handler.removeCallbacksAndMessages(null);
        XzbUtils.imageLoader.clearMemoryCache();
        XzbUtils.imageLoader.clearDiskCache();
        EventBus.getDefault().unregister(this);
//        setdismissPopuWindow();
        //  locationServer.stop();
        if (MyApplicaction.mOpenHelper.getWritableDatabase().isOpen()) {
            MyApplicaction.mOpenHelper.getWritableDatabase().close();
        }
    }

    public void onEventMainThread(AddMemberResponse addMemberResponse) {// 添加成员刷新界面
        ((BookDetailsFragment) fragments.get(0)).getBookDetailinfo(booksDbInfo.getAccountbookid());

    }

    public void onEventMainThread(ShareInfo shareInfo) {// 添加成员刷新界面
        int billid = shareInfo.getBillid();
        final String content = shareInfo.getShareContent();
        final SHARE_MEDIA share_media = shareInfo.getShare_media();
        final String url = ShareUtil.initUrl(Constants.JOURNAL_SHARE, "billid", billid + "");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShareUtil.toShareForPlatform(HomeActivity.this, share_media, R.drawable.icon, url, content, "");
            }
        }, 2000);


    }

    public void onEventMainThread(BooksDbInfo dbInfo) {// 退出登录后重新请求
        booksDbInfo = dbInfo;
        String mAccountid = dbInfo.getAccountbookid();
        mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        changeBookName(booksDbInfo.getAccountbookcount());
        LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
        ((BookDetailsFragment) fragments.get(0)).getBookDetailinfo(mAccountid);
//        initInternetDate(mAccountid);
        ((PropertyFragment) fragments.get(1)).reLoadData();//退出登录资产模块重新拉取数据

    }


    public void changeBookName(String mAccountbookcount) {
        LoginConfig.getInstance().setBookname(mAccountbooktitle);
        int pagerIndex = adapter.getCurrentFragmentIndex();
        if (pagerIndex == 0) {
            if (!TextUtils.isEmpty(mAccountbookcount) && Integer.parseInt(mAccountbookcount) > 0) {// 多人账本
                changeRightImage(R.drawable.message_note);
            } else {
//                changeRightImage(R.drawable.message_icon);
            }
            changeTitle(mAccountbooktitle);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            handler.sendEmptyMessage(999);

        }
        if (hasFocus && (BookDetailsFragment) fragments.get(0) != null
                && ((BookDetailsFragment) fragments.get(0)).isAdded()) {
//            ((BookDetailsFragment) fragments.get(0)).onMetureToplayoutHeight();
        }
    }

    private List<DailycostEntity> dailycostEntities;


    @Override
    public void onNetChange(int netMobile) {
        // 网络状态变化时的操作
        if (netMobile == InternetUtils.NETWORK_NONE) {// 没有网络连接
            unlogin_prompt_include.setVisibility(View.GONE);

        } else {
//            ((BookDetailsFragment) fragments.get(0)).onComBack();
            setUnloginPromptVisible();
        }

    }


    private void checkMessage() {
        BooksDetailPerecenter.checkMessage(new ViewCallBack<CheckMessageInfo.MessgeinfoItem>() {
            @Override
            public void onSuccess(CheckMessageInfo.MessgeinfoItem messgeinfoItem) throws Exception {
                super.onSuccess(messgeinfoItem);
                HomeActivity.this.messgeinfoItem = messgeinfoItem;
                if (adapter.getCurrentFragment() instanceof BookDetailsFragment) {
                    setBookNoteShow(Math.min(99, messgeinfoItem.getNewbook()));
                    setMessageNoteShow(Math.min(99, messgeinfoItem.getNewmsg()));
                    if (messgeinfoItem.getNewmsg() > 0 && !XzbUtils.areNotificationsEnabled(mContext)) {
                        SPUtils.setParam(LoginConfig.getInstance().getUserid() + "hasNewMsg", true);
                    }
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }
        });

    }

    /**
     * 检测更新（如果强制升级则弹出对话框，不强升不做处理）
     */
    private void initCheckVesion() {
        final ProgressDialog m_progressDlg = new ProgressDialog(this);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
        CommonFacade.getInstance().exec(Constants.CHECK_VESION, new ViewCallBack<CheckVesionInfo>() {
            @Override
            public void onSuccess(CheckVesionInfo o) throws Exception {
                super.onSuccess(o);
                CheckVesionInfo checkVesionInfo = o;
                CheckVwsionItem checkVwsionItem = checkVesionInfo.getData();
                final String downurl = checkVwsionItem.getDownurl();
                final boolean isHtml;
                if (checkVwsionItem.getVersion().compareTo(XzbUtils.getVersion(MyApplicaction.getContext())) > 0) {
                    if (checkVwsionItem.getIsforce().equals("1")) {// 强制更新
                        if (checkVwsionItem.getUptext().contains("br")) {
                            isHtml = true;
                        } else {
                            isHtml = false;
                        }
                        showSimpleAlertDialog("软件更新提示", checkVwsionItem.getUptext(), "立即升级", checkVwsionItem.getVersion(),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        m_progressDlg.setTitle("正在下载");
                                        m_progressDlg.setMessage("请稍候...");
                                        m_progressDlg.setCancelable(false);
                                        m_progressDlg.setCanceledOnTouchOutside(false);
                                        XzbUtils.downFile(HomeActivity.this, downurl, m_progressDlg);
                                        dismissDialog();
                                    }
                                }, null, isHtml, false);
                    } else {//不强制弹可取消的对话框
                        if (checkVwsionItem.getUptext().contains("br")) {
                            isHtml = true;
                        } else {
                            isHtml = false;
                        }
                        showSimpleAlertDialog("软件更新提示", checkVwsionItem.getUptext(), "立即升级", checkVwsionItem.getVersion(),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        m_progressDlg.setTitle("正在下载");
                                        m_progressDlg.setMessage("请稍候...");
                                        m_progressDlg.setCancelable(false);
                                        m_progressDlg.setCanceledOnTouchOutside(false);
                                        XzbUtils.downFile(HomeActivity.this, downurl, m_progressDlg);
                                        dismissDialog();
                                    }
                                }, null, isHtml, true);
                    }
                }
            }
        });


    }

    @Override
    public void onTabChange(RadioGroup group, int checkedId, Fragment fragment, int index) {
        switch (index) {
            case 0:// 记账

                String tokenid = LoginConfig.getInstance().getTokenId();
                setTiltgone(View.VISIBLE);
                setBagroudColor(0);
                initTitle(mAccountbooktitle, R.color.home_text_color, 0, R.color.alpha, R.drawable.book_change, R.drawable.message_note, R.drawable.statistics_picture, isShow, this, this);
                if (RequsterTag.isSynchronizationing) {
                    setSysing();
                } else {
                    checkMessage();
                }
                ((BookDetailsFragment) fragments.get(0)).setScrollToTop();

                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_ACCOUNT);
                break;
            case 1:// 资产
                getHandler.removeMessages(0);
                taskQueue.cancel(runnable);
//                unlogin_prompt_include.setVisibility(View.GONE);
                setTiltgone(View.GONE);
                changeTitlteColor(R.color.title_text_color);
                changeTitle(tabNames[index]);
                setBagroudColor(R.color.title_back_color);
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_PROPERTY);
                break;
            case 2:// 发现
                getHandler.removeMessages(0);
                taskQueue.cancel(runnable);
                setTiltgone(View.GONE);
                changeTitle(tabNames[index]);
                changeTitlteColor(R.color.title_text_color);
                setBagroudColor(R.color.title_back_color);
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_FUND);
                break;
            case 3:// 我的
                getHandler.removeMessages(0);
                taskQueue.cancel(runnable);
                setTiltgone(View.VISIBLE);
                onelyTitleShow(tabNames[index]);
                changeTitlteColor(R.color.title_text_color);
                setBagroudColor(R.color.title_back_color);
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_MINE);
                break;
            default:
                break;
        }
        setUnloginPromptVisible();
    }

    private TaskQueue taskQueue = TaskQueue.mainQueue();
    private boolean isSys = true;
    private boolean isSysing = false;

    public void setSysSuccess() {
        isSys = true;
        if (adapter.getCurrentFragmentIndex() != 0) {
            return;
        }

        taskQueue.executeDelayed(runnable, 1000);


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isSys) {
                        title_singe_book.synchronizationTitle(2, "同步成功", R.color.home_text_color);
                    } else {
                        title_singe_book.synchronizationTitle(3, "同步失败", R.color.home_text_color);
                    }


                    getHandler.sendEmptyMessageDelayed(0, 800);
                }
            });
        }
    };
    Handler getHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSysAfterTile();
        }
    };

    private void setSysAfterTile() {

        handler.removeCallbacksAndMessages(null);
        taskQueue.cancel(runnable);
        if (booksDbInfo == null || booksDbInfo.getAccountbooktitle() == null) {
            return;
        }
        isSysing = false;
        changeTitle(booksDbInfo.getAccountbooktitle(), 0, R.color.home_text_color);
    }

    public void setSysfail() {
//        getHandler.removeCallbacks(runnable);
//        taskQueue.cancel(runnable);
        isSys = false;
        if (adapter.getCurrentFragmentIndex() != 0) {
            return;
        }
        taskQueue.executeDelayed(runnable, 1000);


    }

    String dian = "";

    public void setSysing() {//同步中
        if (adapter.getCurrentFragmentIndex() != 0) {
            return;
        }
        isSysing = true;
        title_singe_book.synchronizationTitle(1, "同步中...", R.color.home_text_color);


        checkMessage();
    }


    public void setBooksInfo(BooksDbInfo booksInfo) {
        if (booksInfo == null) {
            return;
        }
        this.booksDbInfo = booksInfo;
        mAccountbooktitle = booksDbInfo.getAccountbooktitle();

        isClear = booksDbInfo.getIsclear();
        if (isOpen) {
            changeBookName(booksDbInfo.getAccountbookcount());
            isOpen = false;
        } else {
            if (!isSysing) {
                changeBookName(booksDbInfo.getAccountbookcount());
            }
        }

        memberid = getMySelfNyMemeberId(booksDbInfo.getMyuid(), booksDbInfo.getMember());
//        changeMemberIncomExpexe(booksDbInfo.getMember());
        if (messgeinfoItem != null && adapter != null && adapter.getCurrentFragmentIndex() == 0)

        {
            setBookNoteShow(Math.min(99, messgeinfoItem.getNewbook()));
            setMessageNoteShow(Math.min(99, messgeinfoItem.getNewmsg()));
        }

    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
//                finish();
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * * 获取个人信息
     *
     * @param tokenid
     */
    private void getUserinfo(String tokenid, boolean flage) {
        if (!TextUtils.isEmpty(LoginConfig.getInstance().getUserinfojson())) {
            //获取用户信息后合并账本
            ((PropertyFragment) fragments.get(1)).reLoadData();//登录成功资产模块重新拉取数据
            ((BookDetailsFragment) fragments.get(0)).initBooks();
            return;
        }

        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String params[] = {"tokenid", "plat", "deviceid", "appver", "osver", "machine"};

        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};

        String sign = StringUtils.setSign(valus, params);
        hashMap.put("sign", sign);


        CommonFacade.getInstance().exec(Constants.GET_USER_INFO, new ViewCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo o) throws Exception {
                super.onSuccess(o);
                UserInfo userInfo = o;
                LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
                LoginConfig.getInstance().setUserName(userInfo.getUsername());
                LoginConfig.getInstance().setMobile(userInfo.getMobile());
                LoginConfig.getInstance().setUserid(userInfo.getUid());
                LoginConfig.getInstance().setIssetavatar(userInfo.getIssetavatar());
                LoginConfig.getInstance().setIssetnick(userInfo.getIssetnick());
                LoginConfig.getInstance().setUserinfojson(GsonUtil.GsonString(userInfo));
                //获取用户信息后合并账本
                ((PropertyFragment) fragments.get(1)).reLoadData();//登录成功资产模块重新拉取数据
                ((BookDetailsFragment) fragments.get(0)).initBooks();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg.getErrCode() == 20012) {
                    LoginConfig.getInstance().setTokenId("");
                }
            }
        });
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

        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("deviceid"), booksDbInfo.getAccountbookid(), memberid};
        String[] params = new String[]{"tokenid", "deviceid", "id", "memberid"};

        sign = StringUtils.setSign(valus, params);
        url = Constants.BASE_URL + Constants.ACCOUNTER_SHARE + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                + hashMap.get("deviceid") + "&sign=" + sign + "&id=" + booksDbInfo.getAccountbookid() + "&memberid=" + memberid;
        url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        return url;
    }

    @Override
    public void onClickNotesItem(View view) {
        Intent intent = null;
        int tagid = Integer.parseInt(view.getTag().toString());
        switch (tagid) {
            case R.string.share_book://账本分享
//                String title = "“" + getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbInfo.getMember()) + "”分享了他创建的《" + booksDbInfo.getAccountbooktitle() + "》，好奇他都记了些什么？点此马上好查看。";
//                String text = LoginConfig.share_acounter_context;
//                ShareUtil.shareMeth(this, title_singe_book, shareText, share_icon, share_medias, getShareUrl(), title, text, XzbUtils.getPhoneScreen(this).widthPixels, false, null, 0);
                BookSharePreviewActivity.open(mContext, booksDbInfo.getAccountbookid(), memberid, booksDbInfo.getAccountbookcate());
                break;
            case R.string.has_settlement_detail://已结算
                intent = new Intent(HomeActivity.this, SettledDetailActivity.class);
                intent.putExtra("titleName", "已结算明细");
                intent.putExtra("id", booksDbInfo.getAccountbookid());
                startActivity(intent);
                break;
            case R.string.has_delet_detail://已删除
                intent = new Intent(HomeActivity.this, SettledDetailActivity.class);
                intent.putExtra("id", booksDbInfo.getAccountbookid());
                intent.putExtra("titleName", "已删除明细");
                startActivity(intent);
                break;
            case R.string.notes_content://消息
                intent = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.string.book_setting://账本设置
                if (booksDbInfo == null) {
                    return;
                }
//                String book_userid = booksDbInfo.getUserid();
//                String book_deveceid = booksDbInfo.getDeviceid();
//                boolean isGroup = isBookFount(book_userid, book_deveceid);
//                if (!isGroup) {
//                    showSimpleAlertDialog("您不是账本创建者,暂没有权限操作");
//                    return;
//                }
                IntentPerecenter.intentJrop(HomeActivity.this, AddBookActivity.class, booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbooktitle(),
                        booksDbInfo.getAccountbookbgimg(), "false", "true", booksDbInfo.getAccountbookid());
//                intent = new Intent(HomeActivity.this, AddBookActivity.class);
//
//                intent.putExtra("isEdit", true);
//                startActivity(intent);
                break;
        }
    }

}

