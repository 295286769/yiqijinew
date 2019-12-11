package com.yiqiji.money.modules.homeModule.write.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.activity.BookCategoryManagerActivity;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataParseAssembler;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataRequstManager;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.ActionSheetDialog;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBookMemberColumns;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.entity.BookExpenditure;
import com.yiqiji.money.modules.common.entity.BookExpenditure.ChildBean;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.CountMoney;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.FullScreenDialogUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.PermissionUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.utils.popupWindowUtils;
import com.yiqiji.money.modules.common.utils.popupWindowUtils.onSonIconClickListener;
import com.yiqiji.money.modules.common.view.UserHeadImageView;
import com.yiqiji.money.modules.common.view.ViewPageItem;
import com.yiqiji.money.modules.homeModule.home.activity.CalendarActivity;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.write.WriteUtil;
import com.yiqiji.money.modules.homeModule.write.adapter.RecyclePageAdapter;
import com.yiqiji.money.modules.homeModule.write.entity.PropertyTransListInfo;
import com.yiqiji.money.modules.myModule.common.RemindReceiver;
import com.yiqiji.money.modules.myModule.login.activity.PictureListActivity;
import com.yiqiji.money.modules.property.activity.WealthAddActivity;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 多人记账宿主Activity
 * 记一笔界面
 *
 * @author Administrator
 */
public class ExpenditureActivity extends BaseActivity implements OnClickListener {
    private TextView tv_details_money;
    private View ll_details_money;
    private ViewPager vp_details_expenditure;
    private RecyclePageAdapter mExpenditurePageAdapter;

    private LinearLayout ll_details_remarks; // 备注说明
    private EditText tv_details_remarks; // 备注说明

    private RelativeLayout rl_details_time;// 选择时间
    private TextView tv_details_time;// 选择时间
    private TextView tv_details_time_selected;

    private LinearLayout ll_details_account;// 选则账户
    private TextView tv_details_account;// 选则账户
    private ImageView iv_details_account;

    private LinearLayout ll_details_location;//定位
    private TextView tv_details_location;//定位

    private ImageView iv_details_photograph;//拍照
    private ImageView iv_details_picture;//照片

    private TextView name_accounting;  //记账名称
    private ImageView icon_accounting; //记账Icon
    private View i_accounting_icon;  //记账View

    private Date currentDate;
    // 是修改，则传整个对象
    private DailycostEntity mDailycostEntity;

    private Date date;
    //返回给Home时间
    private Date returnDate;

    // 消费类型（0表示收入，1表示支出）
    private int mType = 1;
    // Item所选中的View,使用这个名字作为 ：开销类型
    private String mCosttype;

    private boolean isAdd = true;
    // 是添加一条，则传账本名称
    private String bookName;
    // 账本的类型名称，根据名称获取工厂模式里面的对象
    private String bookNameType;
    // 当前账本操作人
    private String myuid;
    private String bookid;
    private String isNumberBook = "1";// 是否是多人账本，0.单人
    private ApiService apiService;
    // 再记一笔
    private Button bt_expend_agin;
    // 确定
    private Button bt_expend_confirm;

    private LinearLayout ll_expend_drawee;
    private LinearLayout ll_expend_participant;
    private ScrollView scroll_expend;
    private String jsonString;
    private String billcateid;
    private String billsubcateid;
    private boolean isSynchronization = false;
    private RadioGroup radio_group_details;
    //  private View expenditure_view;
    private TextView tv_details_money_icon;
    private String mUserName;
    private ImageView iv_details_return;


    //从相册获取图片返回的地址
    private Uri uri = null;
    private List<PropertyTransEntity> cunterList = new ArrayList<>();//账户列表
    private String accountnumber = "0";


    private ChildBean expenditure_bean;  //支出1级对象
    private ChildBean expenditure_son_bean = null;// 支出2级集合选中对象
    private List<ChildBean> expenditure_bean_list; // 支出2级对象所有集合
    private ChildBean before_expenditure_bean;// 支出用于保存之前的集合，点击一个将前面一个设为未选中

    InputMethodManager imm;
    //一页ViewPage显示的item个数
    private final int ICON_COUNT = 8;

    Category expenditureCategory;
    public boolean isNeeddruphome = false;//是否需要跳转到首页
    public boolean isStatistics;//是否是订阅修改账单

    private class Category {
        private String category;
        private String icon;
        private String topCategoryId;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTopCategoryId() {
            return topCategoryId;
        }

        public void setTopCategoryId(String topCategoryId) {
            this.topCategoryId = topCategoryId;
        }
    }

    //是否是第一修改
    private boolean isFistQuit = true;
    private float item_viewGroup;

    private boolean isSpeech = false;//是否是语音记账
    private String billmark;
    private String categoryid;
    private String categorytitle;
    private String billamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);
        getOnIntent();
        initView();
        initKeyboard();
        expenditureCategory = new Category();
        retryDialogHandler = new RetryDialogHandler(this);
        if (isAdd) {
            getNativePeopleByBook(bookid);
        } else {
            initData0nEdit();
            getNativePeopleByBill(mDailycostEntity.getBillid());
            if (mType == 1) {
                ((RadioButton) radio_group_details.getChildAt(0)).setChecked(true);
                vp_details_expenditure.setVisibility(View.VISIBLE);
                ll_details_point_group.setVisibility(View.VISIBLE);
            } else {
                ((RadioButton) radio_group_details.getChildAt(1)).setChecked(true);
                vp_details_expenditure.setVisibility(View.GONE);
                ll_details_point_group.setVisibility(View.GONE);
            }
        }
        setTextColor(tv_details_money_icon);
        setTextColor(tv_details_money);
        getNativeData();
        if (isAdd) {
            if (isSpeech) {//是语音记账
                expenditure_bean.setCategoryid(categoryid);
                expenditure_bean.setCategorytitle(categorytitle);
                before_expenditure_bean = expenditure_bean;

                expenditureCategory.setCategory(categorytitle);
                expenditureCategory.setIcon(categoryid);
                expenditureCategory.setTopCategoryId(categoryid);
                setImage(icon_accounting, categoryid);
                name_accounting.setText(categorytitle);
                icon_accounting.setBackgroundResource(0);
                tv_details_remarks.setText(billmark);
                tv_details_money.setText(billamount);
            } else {
                delayedShowAccountIcon();
            }
        } else {
            //不处理
        }
        loadData(true);
        initEvent();
    }

    String iconUrl;

    private void getOnIntent() {
        isSpeech = getIntent().getBooleanExtra("isSpeech", false);
        if (isSpeech) {
            billmark = getIntent().getStringExtra("billmark");
            categoryid = getIntent().getStringExtra("categoryid");
            categorytitle = getIntent().getStringExtra("categorytitle");
            billamount = getIntent().getStringExtra("billamount");
            billcateid = categoryid;
        } else {
            //不处理
        }

        bookName = getIntent().getStringExtra("bookName");
        isAdd = getIntent().getBooleanExtra("intentType", true);
        isNumberBook = getIntent().getStringExtra("isNumberBook");
        bookNameType = getIntent().getStringExtra("bookNameType");
        bookid = getIntent().getStringExtra("bookid");
        myuid = getIntent().getStringExtra("myuid");
        iconUrl = getIntent().getStringExtra("iconUrl");
        isStatistics = getIntent().getBooleanExtra("isStatistics", false);
        returnDate = (Date) getIntent().getExtras().getSerializable("date_time");
        apiService = RetrofitInstance.get().create(ApiService.class);

        if (isAdd) {
            date = new Date();
        } else {
            //如果是修改则获取传的值
            mDailycostEntity = (DailycostEntity) getIntent().getParcelableExtra("DailycostEntity");
            billcateid = mDailycostEntity.getBillcateid();
            billsubcateid = mDailycostEntity.getBillsubcateid();
            mType = Integer.valueOf(mDailycostEntity.getBilltype());
            date = new Date(Long.valueOf(mDailycostEntity.getTradetime()) * 1000);
        }
    }


    //从相册里面取后返回
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        String path = getIntent().getStringExtra("path");
        uri = Uri.parse(path);
        iv_details_photograph.setVisibility(View.GONE);
        iv_details_picture.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(path) && !path.contains("file://")) {
            path = "file://" + path;
            uri = Uri.parse(path);
        }
        XzbUtils.displayImage(iv_details_picture, uri.toString(), 0);
    }


    BookExpenditure expenditure_data;
    BookExpenditure accounts_data;

    private void getNativeData() {

        String jsonString = LoginConfig.getInstance().getJsonbook(bookid);
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }
        JSONObject object;
        try {
            object = DataLocalPersistencer.getBookCategoryJSONObject(bookid);
            //只有支出
            if (object.has("expendCategory")) {
                JSONObject b = object.getJSONObject("expendCategory");// 支出
                expenditure_data = DataParseAssembler.getInstance().getExpenditureForEnable(b);
            }
            if (object.has("incomeCategory")) {
                JSONObject incom = object.getJSONObject("incomeCategory");// 收入
                accounts_data = DataParseAssembler.getInstance().getExpenditureForEnable(incom);
            }
            if (expenditure_data != null && expenditure_data.getChild() != null) {
                setViewPage(expenditure_data, vp_details_expenditure, ll_details_point_group);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.log_error(null, e);
        }
    }

    private void initData0nEdit() {
        isFistQuit = true;
        //设置图片和类别
        setIconAndName(mDailycostEntity.getBillcatename(), billcateid, mDailycostEntity.getBillsubcatename(), billsubcateid);
        //设置金额
        tv_details_money.setText(mDailycostEntity.getBillamount());
        //设置备注
        if (!TextUtils.isEmpty(mDailycostEntity.getBillmark())) {
            tv_details_remarks.setText(mDailycostEntity.getBillmark());
        }
        //编辑显示图片
        if (!TextUtils.isEmpty(mDailycostEntity.getBillimg())) {
            iv_details_photograph.setVisibility(View.GONE);
            iv_details_picture.setVisibility(View.VISIBLE);
            uri = Uri.parse(mDailycostEntity.getBillimg());
            XzbUtils.displayImage(iv_details_picture, mDailycostEntity.getBillimg(), 0);
        }
        //设置时间
        if (!TextUtils.isEmpty(mDailycostEntity.getTradetime())) {
            if (DateUtil.isToday(date)) {
                tv_details_time.setText("今天");
                tv_details_time_selected.setText("昨天?");
            } else {
                tv_details_time.setText(DateUtil.formatTheDateToMM_dd(date, 1));
                tv_details_time_selected.setText("今天?");
            }
        }
        //设置账户信息
        accountnumber = mDailycostEntity.getAccountnumber();
        //设置定位信息
        if (!TextUtils.isEmpty(mDailycostEntity.getAddress())) {
            tv_details_location.setText(mDailycostEntity.getAddress());
            address = mDailycostEntity.getAddress();
        }


    }


    /**
     * 设置账户信息
     */
    private void OnEditsetAccountnumber(PropertyTransEntity propertyTransEntity) {
        tv_details_account.setText(propertyTransEntity.getItemname());
        XzbUtils.displayImage(iv_details_account, propertyTransEntity.getItemicon(), 0);
    }


    private void delayedShowAccountIcon() {
        TaskQueue.mainQueue().executeDelayed(new Runnable() {
            @Override
            public void run() {
                showAccountIcon(i_accounting_icon);
            }
        }, 500);
    }

    private void initView() {
        float screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        item_viewGroup = (screeWith - UIHelper.Dp2PxFloat(this, 16)) / 4;

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mItemBean = new ItemBean();
        if (date == null) {
            date = new Date();
        }
        tv_details_location = (TextView) findViewById(R.id.tv_details_location);
        iv_details_photograph = (ImageView) findViewById(R.id.iv_details_photograph);
        iv_details_photograph.setOnClickListener(this);
        iv_details_picture = (ImageView) findViewById(R.id.iv_details_picture);
        iv_details_picture.setOnClickListener(this);

        ll_details_location = (LinearLayout) findViewById(R.id.ll_details_location);
        ll_details_location.setOnClickListener(this);
        // 初始化时间、备注、转账
        iv_details_return = (ImageView) findViewById(R.id.iv_details_return);
        iv_details_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
                startActivityToHome("false");

            }
        });

        tv_details_money_icon = (TextView) findViewById(R.id.tv_details_money_icon);

        //    expenditure_view = findViewById(R.id.expenditure_view);

        ll_details_remarks = (LinearLayout) findViewById(R.id.ll_details_remarks);
        rl_details_time = (RelativeLayout) findViewById(R.id.rl_details_time);
        ll_details_account = (LinearLayout) findViewById(R.id.ll_details_account);
        tv_details_time_selected = (TextView) findViewById(R.id.tv_details_time_selected);

        tv_details_time_selected.setOnClickListener(this);
        ll_details_remarks.setOnClickListener(this);
        rl_details_time.setOnClickListener(this);
        ll_details_account.setOnClickListener(this);


        tv_details_remarks = (EditText) findViewById(R.id.tv_details_remarks);
        // 设置光标
        tv_details_remarks.setCursorVisible(false);
        tv_details_remarks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_details_remarks.setCursorVisible(true);// 再次点击显示光标
                tv_details_money.setText(onSumMoney(tv_details_money.getText().toString(), tv_details_money));
                onCalculateMoney(mItemBean);

                imm.showSoftInput(tv_details_remarks, InputMethodManager.SHOW_FORCED);
            }
        });
        tv_details_remarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hidesystemkeyboard();
                    return true;
                }
                return false;
            }
        });

        tv_details_time = (TextView) findViewById(R.id.tv_details_time);
        tv_details_account = (TextView) findViewById(R.id.tv_details_account);
        iv_details_account = (ImageView) findViewById(R.id.iv_details_account);


        tv_details_money = (TextView) findViewById(R.id.tv_details_money);
        tv_details_money.setTag(9999);
        tv_details_money = (TextView) findViewById(R.id.tv_details_money);
        tv_details_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() < 10) {
                    tv_details_money.setTextSize(22);
                } else if (s.length() > 10 && s.length() < 15) {
                    tv_details_money.setTextSize(20);
                } else if (s.length() > 15 && s.length() < 20) {
                    tv_details_money.setTextSize(18);
                } else if (s.length() > 20) {
                    tv_details_money.setTextSize(16);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 10) {
                    tv_details_money.setTextSize(22);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mItemBean.setItemTextView(tv_details_money);

        ll_details_money = findViewById(R.id.i_accounting_money);
        ll_details_money.setOnClickListener(this);

        vp_details_expenditure = (ViewPager) findViewById(R.id.vp_details_expenditure);
        ll_details_point_group = (LinearLayout) findViewById(R.id.ll_details_point_group);

        bt_expend_agin = (Button) findViewById(R.id.bt_expend_agin);
        bt_expend_confirm = (Button) findViewById(R.id.bt_expend_confirm);
        bt_expend_agin.setOnClickListener(this);
        bt_expend_confirm.setOnClickListener(this);

        ll_expend_drawee = (LinearLayout) findViewById(R.id.ll_expend_drawee);
        ll_expend_participant = (LinearLayout) findViewById(R.id.ll_expend_participant);

        scroll_expend = (ScrollView) findViewById(R.id.scroll_expend);

        radio_group_details = (RadioGroup) findViewById(R.id.radio_group_details);
        radio_group_details.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_details_expenditure:// 支出
                        for (int i = 0; i < swipeLayoutList1.size(); i++) {
                            TextView textView = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
                            TextView textView2 = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money_type);
                            textView.setTextColor(getResources().getColor(R.color.expenditure));
                            textView2.setText("支付");
                        }
                        for (int i = 0; i < swipeLayoutList2.size(); i++) {
                            TextView textView = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
                            TextView textView2 = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money_type);
                            textView.setTextColor(getResources().getColor(R.color.expenditure));
                            textView2.setText("花费");
                        }
                        peopleType1.setText("付款人" + "(" + swipeLayoutList1.size() + ")");
                        peopleType2.setText("参与人" + "(" + swipeLayoutList2.size() + ")");
                        iv_expend_people1.setImageResource(R.drawable.participant);
                        iv_expend_people2.setImageResource(R.drawable.drawee);


                        tv_details_money_icon.setTextColor(getResources().getColor(R.color.expenditure));
                        tv_details_money.setTextColor(getResources().getColor(R.color.expenditure));

                        vp_details_expenditure.setVisibility(View.VISIBLE);
                        if (ll_details_point_group_is_show) {
                            ll_details_point_group.setVisibility(View.VISIBLE);
                        } else {
                            ll_details_point_group.setVisibility(View.INVISIBLE);
                        }
                        //当切换到支出的时候，可以点击出现选中Icon
                        icon_accounting.setClickable(true);
                        if (TextUtils.isEmpty(expenditureCategory.getCategory())) {
                            setIconAndName();
                            showAccountIcon(i_accounting_icon);
                        } else {
                            hideAccountIcon(i_accounting_icon);
                            setIconAndName(expenditureCategory);
                        }
                        mType = 1;
                        break;
                    case R.id.accounts:// 交款

                        if (swipeLayoutList1.size() > 0) {
                            for (int i = 0; i < swipeLayoutList1.size(); i++) {
                                TextView textView = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
                                TextView textView2 = (TextView) swipeLayoutList1.get(i).findViewById(
                                        R.id.tv_expend_money_type);
                                textView.setTextColor(getResources().getColor(R.color.context_color));
                                textView2.setText("收取");
                            }
                            for (int i = 0; i < swipeLayoutList2.size(); i++) {
                                TextView textView = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
                                TextView textView2 = (TextView) swipeLayoutList2.get(i).findViewById(
                                        R.id.tv_expend_money_type);
                                textView.setTextColor(getResources().getColor(R.color.context_color));
                                textView2.setText("交款");
                            }
                            peopleType1.setText("收款人" + "(" + swipeLayoutList1.size() + ")");
                            peopleType2.setText("付款人" + "(" + swipeLayoutList2.size() + ")");
                            iv_expend_people1.setImageResource(R.drawable.peceivables);
                            iv_expend_people2.setImageResource(R.drawable.participant);
                        }

                        tv_details_money_icon.setTextColor(getResources().getColor(R.color.context_color));
                        tv_details_money.setTextColor(getResources().getColor(R.color.context_color));

                        //当切换到交款的时候，不能点击出现选中Icon
                        icon_accounting.setClickable(false);
                        hideAccountIcon(i_accounting_icon);
                        icon_accounting.setImageResource(R.drawable.payment);
                        icon_accounting.setBackgroundResource(0);
                        name_accounting.setText("成员交款");
                        mType = 4;
                        break;
                }
            }
        });


        name_accounting = (TextView) findViewById(R.id.name_accounting);
        icon_accounting = (ImageView) findViewById(R.id.icon_accounting);
        icon_accounting.setOnClickListener(this);
        i_accounting_icon = findViewById(R.id.i_accounting_icon);

    }

    // 当前点击Item所拥有的money;
    private double selectTextMoney = 0;

    private String MyMemberid;


    private static final int Mars = 0;
    private static final int Moon = 1;

    List<BooksDbMemberInfo> mBooksDbMemberInfosList1 = new ArrayList<>();
    List<BooksDbMemberInfo> mBooksDbMemberInfosList2 = new ArrayList<>();

    private List<RelativeLayout> swipeLayoutList1 = new ArrayList<>();
    private List<RelativeLayout> swipeLayoutList2 = new ArrayList<>();
    String[] Memberid_1 = null;
    String[] Memberid_1_Money = null;
    boolean[] Memberid_1_Money_isChange = null;

    String[] Memberid_2 = null;
    String[] Memberid_2_Money = null;
    boolean[] Memberid_2_Money_isChange = null;

    private int selectTag1 = 0;
    private int selectTag2 = -1; // 设置负号是为了在点击不同Item的时候判断是否是当前Item,用于输入的时候删除

    TextView peopleType1;
    TextView peopleType2;
    ImageView iv_expend_people1;
    ImageView iv_expend_people2;

    /* =====================付款人11111==================== */

    private void initParticipant(final List<BooksDbMemberInfo> mParticipantPeopleList) {
        Memberid_1 = new String[mParticipantPeopleList.size()];
        Memberid_1_Money = new String[mParticipantPeopleList.size()];
        Memberid_1_Money_isChange = new boolean[mParticipantPeopleList.size()];
        ll_expend_drawee.removeAllViews();
        // 不清除则
        noEditTextView_Participant_List1.clear();
        swipeLayoutList1.clear();

        View childview_participant = LayoutInflater.from(this).inflate(R.layout.item_expenditure_participant, null);
        peopleType1 = (TextView) childview_participant.findViewById(R.id.tv_expend_people_type);
        iv_expend_people1 = (ImageView) childview_participant.findViewById(R.id.iv_expend_people);

        if (mType == 1) {
            peopleType1.setText("付款人" + "(" + mParticipantPeopleList.size() + ")");
            iv_expend_people1.setImageResource(R.drawable.participant);
        } else {
            peopleType1.setText("收款人" + "(" + mParticipantPeopleList.size() + ")");
            iv_expend_people1.setImageResource(R.drawable.peceivables);
        }

        childview_participant.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hidesystemkeyboard();
                for (int i = 0; i < swipeLayoutList1.size(); i++) {
                    TextView textView = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
                    Memberid_1_Money[i] = textView.getText().toString();
                    Memberid_1_Money_isChange[i] = noEditTextView_Participant_List1.get(i).isChange();
                }

                Intent in = new Intent(ExpenditureActivity.this, ExpenditureSelectPeopleActivity.class);
                if (mType == 1) {
                    in.putExtra("peopleType", "付款人");
                } else {
                    in.putExtra("peopleType", "收款人");
                }
                in.putExtra("bookId", bookid);

                in.putExtra("memberid", Memberid_1);
                in.putExtra("Memberid_Money", Memberid_1_Money);
                in.putExtra("Memberid_Money_isChange", Memberid_1_Money_isChange);
                in.putExtra("MyMemberid", MyMemberid);
                startActivityForResult(in, Mars);
            }
        });

        ll_expend_drawee.addView(childview_participant);

        BooksDbMemberInfo mBooksDbMemberInfos = null;
        View childview_expenditure1;
        RelativeLayout swipeLayout1;
        UserHeadImageView mCircleImageView;
        TextView tv_expend_name, tv_expend_money, tv_expend_money_type;
        ItemBean mitemBean;

        for (int i = 0; i < mParticipantPeopleList.size(); i++) {
            mitemBean = new ItemBean();
            mBooksDbMemberInfos = mParticipantPeopleList.get(i);
            Memberid_1[i] = mBooksDbMemberInfos.getMemberid();
            // 初始化布局
            childview_expenditure1 = LayoutInflater.from(this).inflate(R.layout.item_expenditure, null);

            mCircleImageView = (UserHeadImageView) childview_expenditure1.findViewById(R.id.iv_expend_payment);
            tv_expend_name = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_name);
            tv_expend_money_type = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_money_type);
            tv_expend_money = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_money);
            if (mType == 1) {
                tv_expend_money_type.setText("支付");
            } else {
                tv_expend_money_type.setText("收取");
            }

            setTextColor(tv_expend_money);
            // 初始化值
            tv_expend_name.setText(mBooksDbMemberInfos.getUsername());

            if (mBooksDbMemberInfos.getBalance() == 0) {
                tv_expend_money.setText("0.00");
            } else {
                tv_expend_money.setText(String.valueOf(XzbUtils.inNumberFormat(mBooksDbMemberInfos.getBalance())));
            }

            mCircleImageView.displayImage(mBooksDbMemberInfos);

            swipeLayout1 = (RelativeLayout) childview_expenditure1.findViewById(R.id.expend_swipeLayout);
            tv_expend_money.setTag(i);
            swipeLayout1.setTag(i);

            // 设置删除监听
            swipeLayout1.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    final int id = Integer.valueOf(v.getTag().toString());
                    if (mParticipantPeopleList.size() == 1) {
                        Toast.makeText(ExpenditureActivity.this, "至少有一个成员", Toast.LENGTH_SHORT).show();
                    } else {
                        TextView nameText = (TextView) v.findViewById(R.id.tv_expend_name);
                        String name = nameText.getText().toString();

                        showSimpleAlertDialog("提示", "您确定要删除参与人：" + name, "确定", "取消", false, true, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBooksDbMemberInfosList1.remove(id);
                                initParticipant(mBooksDbMemberInfosList1);
                                double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
                                onAverageParticipant(mCountMoney);
                                dismissDialog();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });


                    }
                    return true;
                }
            });

            swipeLayout1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    // 这个顺序不能变，显示showKeyboard要在mItemBean赋值上面
                    showKeyboard();
                    onScrillView(scroll_expend, view);
                    setSwipeLayoutBackgroundColor();
                    view.setBackgroundColor(getResources().getColor(R.color.item_select));
                    selectTag1 = (int) view.getTag();

                    TextView editMoneyTextView = (TextView) view.findViewById(R.id.tv_expend_money);
                    editMoneyTextView.setTag(selectTag1);
                    mItemBean.setItemTextView(editMoneyTextView);
                    mItemBean.setMemberid(mParticipantPeopleList.get(selectTag1).getMemberid());
                    selectTextMoney = Double.valueOf(editMoneyTextView.getText().toString());
                    isPeopleType = 0;

                }
            });
            swipeLayoutList1.add(swipeLayout1);

            // 如果都编辑过
            // Memberid_1_Money_isChange[]
            mitemBean.setItemTextView(tv_expend_money);
            mitemBean.setMemberid(mBooksDbMemberInfos.getMemberid());
            mitemBean.setChange(mBooksDbMemberInfos.isChange());

            noEditTextView_Participant_List1.add(mitemBean);
            ll_expend_drawee.addView(childview_expenditure1);
        }
    }

	/* =====================参与人2222222222==================== */

    private void initDrawee(final List<BooksDbMemberInfo> mDraweePeopleList) {
        Memberid_2 = new String[mDraweePeopleList.size()];
        Memberid_2_Money = new String[mDraweePeopleList.size()];
        Memberid_2_Money_isChange = new boolean[mDraweePeopleList.size()];
        ll_expend_participant.removeAllViews();
        noEditTextView_Drawee_List1.clear();
        swipeLayoutList2.clear();

        View childview_participant = LayoutInflater.from(this).inflate(R.layout.item_expenditure_participant, null);
        iv_expend_people2 = (ImageView) childview_participant.findViewById(R.id.iv_expend_people);

        peopleType2 = (TextView) childview_participant.findViewById(R.id.tv_expend_people_type);
        if (mType == 1) {
            peopleType2.setText("参与人" + "(" + mDraweePeopleList.size() + ")");
            iv_expend_people2.setImageResource(R.drawable.drawee);
        } else {
            peopleType2.setText("付款人" + "(" + mDraweePeopleList.size() + ")");
            iv_expend_people2.setImageResource(R.drawable.participant);
        }

        childview_participant.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hidesystemkeyboard();
                for (int i = 0; i < swipeLayoutList2.size(); i++) {
                    TextView textView = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
                    Memberid_2_Money[i] = textView.getText().toString();
                    Memberid_2_Money_isChange[i] = noEditTextView_Drawee_List1.get(i).isChange();
                }

                Intent in = new Intent(ExpenditureActivity.this, ExpenditureSelectPeopleActivity.class);
                if (mType == 1) {
                    in.putExtra("peopleType", "参与人");
                } else {
                    in.putExtra("peopleType", "付款人");
                }
                in.putExtra("peopleType", "参与人");
                in.putExtra("bookId", bookid);
                in.putExtra("memberid", Memberid_2);
                in.putExtra("Memberid_Money", Memberid_2_Money);
                in.putExtra("Memberid_Money_isChange", Memberid_2_Money_isChange);
                in.putExtra("MyMemberid", MyMemberid);
                startActivityForResult(in, Moon);
            }
        });
        ll_expend_participant.addView(childview_participant);
        View childview_expenditure1;

        TextView tv_expend_name;
        TextView tv_expend_money, tv_expend_money_type;
        RelativeLayout swipeLayout2;
        BooksDbMemberInfo mBooksDbMemberInfos;
        ItemBean mitemBean;
        for (int i = 0; i < mDraweePeopleList.size(); i++) {
            mitemBean = new ItemBean();
            mBooksDbMemberInfos = mDraweePeopleList.get(i);
            Memberid_2[i] = mBooksDbMemberInfos.getMemberid();
            // 初始化参与人View
            childview_expenditure1 = LayoutInflater.from(this).inflate(R.layout.item_expenditure, null);
            tv_expend_money_type = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_money_type);
            tv_expend_money = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_money);
            tv_expend_name = (TextView) childview_expenditure1.findViewById(R.id.tv_expend_name);
            if (mType == 1) {
                tv_expend_money_type.setText("花费");
            } else {
                tv_expend_money_type.setText("交款");
            }

            setTextColor(tv_expend_money);

            if (mBooksDbMemberInfos.getBalance() == 0) {
                tv_expend_money.setText("0.00");
            } else {
                tv_expend_money.setText(String.valueOf(XzbUtils.inNumberFormat(mBooksDbMemberInfos.getBalance())));
            }

            UserHeadImageView mCircleImageView = (UserHeadImageView) childview_expenditure1
                    .findViewById(R.id.iv_expend_payment);

            // 设置名称
            tv_expend_name.setText(mBooksDbMemberInfos.getUsername());
            // 设置头像
            mCircleImageView.displayImage(mBooksDbMemberInfos);

            swipeLayout2 = (RelativeLayout) childview_expenditure1.findViewById(R.id.expend_swipeLayout);
            tv_expend_money.setTag(-i);
            swipeLayout2.setTag(-i);

            swipeLayout2.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    final int id = Math.abs(Integer.valueOf(v.getTag().toString()));
                    if (mDraweePeopleList.size() == 1) {
                        Toast.makeText(ExpenditureActivity.this, "至少有一个成员", Toast.LENGTH_SHORT).show();
                    } else {
                        TextView nameText = (TextView) v.findViewById(R.id.tv_expend_name);
                        String name = nameText.getText().toString();

                        showSimpleAlertDialog("提示", "您确定要删除参与人：" + name, "确定", "取消", false, true, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBooksDbMemberInfosList2.remove(id);
                                initDrawee(mBooksDbMemberInfosList2);
                                double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
                                onAverageDrawee(mCountMoney);
                                dismissDialog();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });

                    }
                    return true;

                }
            });

            // 设置点击监听
            swipeLayout2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    // 这个顺序不能变，显示showKeyboard要在mItemBean赋值上面
                    showKeyboard();
                    onScrillView(scroll_expend, view);
                    setSwipeLayoutBackgroundColor();

                    view.setBackgroundColor(getResources().getColor(R.color.item_select));
                    selectTag2 = (int) view.getTag();
                    TextView editMoneyTextView = (TextView) view.findViewById(R.id.tv_expend_money);

                    editMoneyTextView.setTag(selectTag2);
                    mItemBean.setItemTextView(editMoneyTextView);
                    mItemBean.setMemberid(mDraweePeopleList.get(Math.abs(selectTag2)).getMemberid());
                    selectTextMoney = Double.valueOf(editMoneyTextView.getText().toString());
                    isPeopleType = 1;
                }
            });

            swipeLayoutList2.add(swipeLayout2);
            mitemBean.setItemTextView(tv_expend_money);
            mitemBean.setMemberid(mBooksDbMemberInfos.getMemberid());
            mitemBean.setChange(mBooksDbMemberInfos.isChange());
            noEditTextView_Drawee_List1.add(mitemBean);

            ll_expend_participant.addView(childview_expenditure1);
        }

    }

    private void setTextColor(TextView mTextView) {

        if (mType == 1) {
            mTextView.setTextColor(getResources().getColor(R.color.expenditure));
        } else {
            mTextView.setTextColor(getResources().getColor(R.color.context_color));
        }

    }

    /**
     * @param mData
     */
    private void settingImage(String billcateid, String billsubcateid, BookExpenditure mData, List<ViewGroup> mViewGroupList) {

        expenditure_bean = mData.getChild().get(0);
        before_expenditure_bean = mData.getChild().get(0);

        int childCount = 0;
        for (int i = 0; i < mViewGroupList.size(); i++) {
            childCount += (mViewGroupList.get(i)).getChildCount();
        }

        ViewGroup item_expenditure_viewpage = ((ViewGroup) mViewGroupList.get(0));
        for (int j = 0; j < childCount; j++) {

            if (j % ICON_COUNT == 0 && j != 0) {
                item_expenditure_viewpage = mViewGroupList.get(j / ICON_COUNT);
            }

            //设置显示跟多目录
            View item_ViewGroup = item_expenditure_viewpage.getChildAt(j % ICON_COUNT);
            ImageView with_more = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(2);

            //如果是自定义按钮
            if (j == childCount - 1) {
                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);
                with_more.setVisibility(View.GONE);
                imageview.setImageResource(R.drawable.ic_custom_expenditure_category_83_83);
                continue;
            }

            String mDataid = mData.getChild().get(j).getCategoryid();
            //获得子集合
            List<ChildBean> child_list = mData.getChild().get(j).getChild();
            if (StringUtils.isEmptyList(child_list)) {
                with_more.setVisibility(View.GONE);
            } else {
                with_more.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(billcateid) && mDataid.equals(billcateid)) {

                //设置viewPage下面第几屏小点选中
                int index_vp_details = j / ICON_COUNT;
                vp_details_expenditure.setCurrentItem(index_vp_details);
                item_ViewGroup = item_expenditure_viewpage.getChildAt(j % ICON_COUNT);
                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);

                //如果没有二级目录
                if (TextUtils.isEmpty(billsubcateid) || billsubcateid.equals("0")) {
                    // 首先取drawable里面的图片，然后没有再取sd卡
                    setImage(imageview, mDataid, mData.getChild().get(j).getCategoryicon());
                    imageview.setBackgroundResource(R.drawable.write_default_expenditure);
                    before_expenditure_imageView = imageview;
                    before_expenditure_bean = mData.getChild().get(j);
                    expenditure_bean = mData.getChild().get(j);


                } else {
                    //有二级目录
                    int child_count = mData.getChild().get(j).getChild().size();
                    for (int i = 0; i < child_count; i++) {
                        // 获取二级子目录的Id
                        String childId = mData.getChild().get(j).getChild().get(i).getCategoryid();
                        if (childId.equals(billsubcateid)) {
                            // 获得icon
                            setImage(imageview, childId, mData.getChild().get(j).getChild().get(i).getCategoryicon());
                            imageview.setBackgroundResource(R.drawable.write_default_expenditure);
                            before_expenditure_imageView = imageview;
                            before_expenditure_bean = mData.getChild().get(j).getChild().get(i);
                            expenditure_bean = mData.getChild().get(j);
                        }
                    }
                }

            } else {
                // 当是第一个ViewGroup的时候，index当时是0开始，从1开始取起，上面已经取过0
                item_ViewGroup = item_expenditure_viewpage.getChildAt(j % ICON_COUNT);
                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);
                // 先根据名字得到ID,再根据ID获取Map中的值
                // int id = product.getIdByName(name);
                setImage(imageview, mDataid, mData.getChild().get(j).getCategoryicon());
                imageview.setBackgroundResource(0);
            }
        }

    }


    /**
     * @param imageView
     * @param categoryId 类别ID
     */
    private void setImage(ImageView imageView, String categoryId) {
        WriteUtil.setImage(imageView, categoryId);
    }


    /**
     * @param imageView
     * @param CategoryId 类别ID
     */
    private void setImage(ImageView imageView, String CategoryId, String url) {
        WriteUtil.setImage(imageView, CategoryId, url);
    }


    private View keyboard_view;
    /**
     * 初始化软件盘
     */
    private ImageView iv_keyboard_plus;// 加
    private ImageView iv_keyboard_delete;
    private TextView tv_keyboard_complete;
    private TextView tv_keyboard_equal;

    private void initKeyboard() {
        keyboard_view = findViewById(R.id.keyboard_view);
        keyboard_view.findViewById(R.id.tv_keyboard_zero2).setOnClickListener(this);

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

        keyboard_view.findViewById(R.id.ll_gone_keyboard_many_people).setOnClickListener(this);

        iv_keyboard_plus = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_plus);
        iv_keyboard_delete = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_delete);
        tv_keyboard_complete = (TextView) keyboard_view.findViewById(R.id.tv_keyboard_complete);
        tv_keyboard_equal = (TextView) keyboard_view.findViewById(R.id.tv_keyboard_equal);

        iv_keyboard_plus.setOnClickListener(this);
        iv_keyboard_delete.setOnClickListener(this);
        tv_keyboard_complete.setOnClickListener(this);
    }

    private static String KEY_SPOT = ".";
    private static String KEY_REDUCE = "-";
    private static String KEY_PLUS = "+";
    private static String KEY_MONEY_INCOME = "I";
    private static String KEY_MONEY_EXPENDITURE = "E";
    private static String KEY_DELETE = "D";
    private static String KEY_COMPLETE = "C";
    private static String KEY_EQUAL = "equal";


    private static final int IsphotosShow = 2;
    private static final int Islocation = 3;
    //定位地址
    private String address = "不显示位置";
    private static final int ACCESS_FINE_LOCATION_S = 2;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_details_photograph: //拍照
                hidesystemkeyboard();
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MyApplicaction.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//需要弹出dialog让用户手动赋予权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
                new ActionSheetDialog(ExpenditureActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .setCancelTextColor(getResources().getColor(R.color.main_back))
                        .addSheetItem(getString(R.string.home_take_photo), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        choiceFromCamera();

                                    }
                                })
                        .addSheetItem(getString(R.string.home_from_ablum), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(ExpenditureActivity.this, PictureListActivity.class);
                                        intent.putExtra("isShowCut", false);
                                        intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                        startActivity(intent);
                                    }
                                }).show();
                break;

            case R.id.ll_details_location://定位
                hidesystemkeyboard();
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                PermissionUtil.requestPermisions(this, ACCESS_FINE_LOCATION_S, permissions, new PermissionUtil.RequestPermissionListener() {

                    @Override
                    public void onRequestPermissionSuccess() {
                        Intent intent = new Intent(ExpenditureActivity.this, LocationActivity.class);
                        intent.putExtra("address", address);
                        startActivityForResult(intent, Islocation);
                    }

                    @Override
                    public void onRequestPermissionFail(int[] grantResults) {
                        //拒绝不做操作
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                showSimpleAlertDialog(null, "请在手机的“设置→一起记→位置”选项中，允许访问您的地理位置", "现在设置", "我知道了", false, false, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        startActivity(intent);
                                        dismissDialog();
                                    }
                                }, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                    }
                                });
                            }
                        }
                    }
                });

                break;

            case R.id.iv_details_picture:  //点击拍的照片

                this.getPackageName();
                this.getClass().getName();

                intent = new Intent(ExpenditureActivity.this, PhotosShowActivity.class);
                intent.putExtra("path", uri.toString());
                startActivityForResult(intent, IsphotosShow);

                break;

            case R.id.ll_gone_keyboard_many_people:
                // 关闭软件盘的时候去掉Item选中的颜色
                onCalculateMoney(mItemBean);

                break;
            case R.id.bt_expend_agin:
                // 点击确定和点击闪存入口是一样的判断
                onSubmit(true);
                break;
            case R.id.bt_expend_confirm:// 点击确定
                // 点击确定和点击闪存入口是一样的判断
                onSubmit(false);
                break;
            case R.id.i_accounting_money:
                setSwipeLayoutBackgroundColor();
                // 这个顺序不能变，显示showKeyboard要在mItemBean赋值上面
                showKeyboard();
                mItemBean.setItemTextView(tv_details_money);
                isPeopleType = 1000;
                break;
            case R.id.rl_details_time: // 选则时间
                hidesystemkeyboard();
                intent = new Intent(this, CalendarActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", date);
                intent.putExtra("type", mType);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequsterTag.CALENDERCODE);
//                pvTime.show();
                break;
            case R.id.tv_details_time_selected:
                hidesystemkeyboard();
                //当前时间为今天
                if (DateUtil.isToday(date)) {
                    this.date = DateUtil.getUpDay(new Date());
                } else {
                    this.date = new Date();
                }
                onSelectedTimeAnimStart(tv_details_time, tv_details_time_selected);
                break;
            case R.id.ll_details_account:// 选则账户
                hidesystemkeyboard();
                shoCapital(accountnumber, cunterList);
                break;
            case R.id.icon_accounting: //Icon显示
                showAccountIcon(i_accounting_icon);
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

            case R.id.tv_keyboard_equal:
                onTextChange(KEY_EQUAL);

                break;
            default:
                break;
        }
    }

    private void onTextChange(String changData) {
        onTextChange(changData, mItemBean);
        // 判断是否是在消费金额上输入
        if (mItemBean.getItemTextView().getId() == tv_details_money.getId()) {
            // 总金额修改过
            mConuntMoney_Change = false;
        }
    }

    private double toDouble(double number) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private boolean onMoneyIsOK(TextView tv_details_money, List<RelativeLayout> swipeLayoutList1, List<RelativeLayout> swipeLayoutList2) {

        double payMoney = Double.valueOf(tv_details_money.getText().toString());
        double paymentMoneyCount = 0;
        double participateMoneyCount = 0;

        // 付款人总金额
        for (int i = 0; i < swipeLayoutList1.size(); i++) {
            TextView payment = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
            paymentMoneyCount += Double.valueOf(payment.getText().toString());
        }
        // 参与人总金额
        for (int i = 0; i < swipeLayoutList2.size(); i++) {
            TextView payment = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
            participateMoneyCount += Double.valueOf(payment.getText().toString());
        }

        payMoney = toDouble(payMoney);
        paymentMoneyCount = toDouble(paymentMoneyCount);
        participateMoneyCount = toDouble(participateMoneyCount);

        if (payMoney != paymentMoneyCount || payMoney != participateMoneyCount
                || paymentMoneyCount != participateMoneyCount) {
            // 弹出提示
            showSimpleAlertDialog("提示", "记账总金额" + payMoney + ",付款人总金额" + paymentMoneyCount + ",参与人总金额" + participateMoneyCount
                    + ",三者金额不相符,请调整后在保存", "确定", true, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                }
            });
            return false;
        }
        return true;
    }

    // 点击切换不同的Item和关闭软件盘后再点击，则清空上面的数字

    private int before_isPeopleType = -1;
    private int before_textTag = 0;

    @SuppressWarnings("unused")
    private void onTextChange(String changData, ItemBean mItemBean) {
        TextView editMoneyTextView = mItemBean.getItemTextView();
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
            tv_keyboard_equal.setVisibility(View.GONE);
            tv_keyboard_complete.setVisibility(View.VISIBLE);
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
            tv_keyboard_equal.setVisibility(View.VISIBLE);
            tv_keyboard_complete.setVisibility(View.GONE);

        } else if (changData.equals(KEY_DELETE)) {
            if (mMoney.length() == 1) {
                editMoneyTextView.setText(0 + "");

            } else if (XzbUtils.equalsIsZero(editMoneyTextView)) {
                XzbUtils.cleanTextToZero(editMoneyTextView);
            } else {
                editMoneyTextView.setText(mMoney.substring(0, mMoney.length() - 1));
            }

        } else if (changData.equals(KEY_COMPLETE)) {// 确定
            // 选中颜色改变

            onCalculateMoney(mItemBean);

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
            // 不在上一个Item则将数据清空 ，在关闭软件盘的时候before_isPeopleType赋值为-1，
            if (before_isPeopleType != isPeopleType || before_textTag != (int) editMoneyTextView.getTag()) {
                // 进入则表示选中的与上一次不一样
                cleanData = XzbUtils.cleanText(editMoneyTextView);
            }
            if (cleanData.length() >= 4
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && !cutMoney.equals(KEY_REDUCE) && !cutMoney.equals(KEY_PLUS)) {
                XzbUtils.shakeTextView(editMoneyTextView);
                return;
            }

            // 如果输入里面没有加减，并且大于百万级别，则不操作
            if (XzbUtils.isOverOneMillion(cleanData + changData)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }
            editMoneyTextView.setText(cleanData + changData);
            before_isPeopleType = isPeopleType;
            before_textTag = (int) editMoneyTextView.getTag();
        }
    }

    // 如果输入金额没有计算先计算金额
    private String onPlusMoney(String mMoney) {
        // 点击确定和点击闪存入口是一样的判断
        String money = mMoney;
        if (mMoney.contains(KEY_REDUCE) || mMoney.contains(KEY_PLUS)) {
            money = onSumMoney(mMoney, mItemBean.getItemTextView());
            mItemBean.getItemTextView().setText(money);
            if (money.equals("0.00")) {
                XzbUtils.shakeTextView(mItemBean.getItemTextView());
                return money;
            }
        } else {
            // 避免删除一个数后就关闭
            DecimalFormat df = new DecimalFormat("0.00");
            money = df.format(Double.valueOf(mMoney));
            mItemBean.getItemTextView().setText(money);
            if (money.equals("0.00")) {
                XzbUtils.shakeTextView(mItemBean.getItemTextView());
                return money;
            }
        }
        return money;
    }

    private void onCalculateMoney(ItemBean mItemBean) {
        setSwipeLayoutBackgroundColor();
        hideKeyboard();
        TextView editMoneyTextView = mItemBean.getItemTextView();
        String mMoney = editMoneyTextView.getText().toString();
        mMoney = onPlusMoney(mMoney);

        if (editMoneyTextView.getId() == R.id.tv_details_money) {
            double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
            onAverageParticipant(mCountMoney);
            onAverageDrawee(mCountMoney);
            return;
        }
        if (isPeopleType == 0) {
            itemMoney1_Chang = false;
            goHalvesParticipant(Double.valueOf(mMoney));
        } else if (isPeopleType == 1) {// 参与人
            itemMoney2_Chang = false;
            goHalvesDrawee(Double.valueOf(mMoney));
        }
    }


    // 区分当前要修改的是付款人或参与人 1:参与人，0付款人
    private int isPeopleType = -1;

    /**
     * 点击确定的时候计算参与人与付款人钱
     *
     * @param mMoney
     * Item 编辑的钱
     */

    // 未编辑的参与人
    // private List<TextView> noEditTextView_Participant_List = new
    // ArrayList<>();// 没有编辑的是全部，编辑一次，查找一次T出来
    // 编辑后的参与人
    // private List<TextView> editAfterParticipant = new ArrayList<>();

    // 未编辑的付款人
    // private List<TextView> noEditTextView_Drawee_List = new ArrayList<>();

    // 编辑后的付款人
    // private List<TextView> editAfterDrawee = new ArrayList<>();

    private boolean mConuntMoney_Change = true;
    private boolean itemMoney1_Chang = true;
    private boolean itemMoney2_Chang = true;

    // 未编辑的参与人
    private List<ItemBean> noEditTextView_Participant_List1 = new ArrayList<>();// 没有编辑的是全部，编辑一次，查找一次T出来
    // 未编辑的付款人
    // private List<ItemBean> editAfterParticipant1 = new ArrayList<>();

    private List<ItemBean> noEditTextView_Drawee_List1 = new ArrayList<>();
    // 编辑后的付款人
    // private List<ItemBean> editAfterDrawee1 = new ArrayList<>();
    ItemBean mItemBean;

    private class ItemBean {
        private TextView itemTextView;
        private String memberid;
        private boolean Change = false;

        public boolean isChange() {
            return Change;
        }

        public void setChange(boolean change) {
            Change = change;
        }

        public TextView getItemTextView() {
            return itemTextView;
        }

        public void setItemTextView(TextView itemTextView) {
            this.itemTextView = itemTextView;
        }

        public String getMemberid() {
            return memberid;
        }

        public void setMemberid(String memberid) {
            this.memberid = memberid;
        }

    }

    /**
     * 付款人开始计算钱--------------------------
     * <p/>
     * Item编辑的钱
     */

    private void goHalvesParticipant(double mItemMoney) {

        // 区分编辑未编辑
        for (int i = 0; i < noEditTextView_Participant_List1.size(); i++) {
            // 判断当前添加的集合中是否有当前控件
            if (noEditTextView_Participant_List1.get(i).getMemberid().equals(mItemBean.getMemberid())) {
                noEditTextView_Participant_List1.get(i).setChange(true);
                // noEditTextView_Participant_List1.remove(i);
                // editAfterParticipant1.add(mItemBean);
            }
        }
        // 获取总钱数

        // 获取总钱数

        // 设置总消费数
        // 总消费数没有被修改过
        if (mConuntMoney_Change) {
            double editAfterParticipantMoney = 0;
            for (int i = 0; i < swipeLayoutList1.size(); i++) {
                TextView payment = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
                editAfterParticipantMoney += Double.valueOf(payment.getText().toString());
            }

            tv_details_money.setText(XzbUtils.inNumberFormat(editAfterParticipantMoney) + "");
            // 未编辑的参与人平分钱
            onAverageDrawee(editAfterParticipantMoney);
        } else {
            double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
            // 总钱数被修改过，当钱输入的值大于总金：不变
            if (mItemMoney > mCountMoney) {

            } else {
                // 小于 评分自己
                onAverageParticipant(mCountMoney);
            }

        }

    }

    /**
     * 参与人开始计算钱 1 没有输入消费金额，直接输 参与人， 当前参与人金额改变，当前付款人金额改变（），消费金额改变 参与人金额不能大于消费金额
     *
     * @param money
     */

    /**
     * 计算出总额 参与人开始------------------------------------
     */
    private void goHalvesDrawee(double mItemMoney) {

        // if (selectTextMoney != money) {
        // 将编辑过和未编辑过的分别放到集合
        for (int i = 0; i < noEditTextView_Drawee_List1.size(); i++) {
            // 判断当前添加的集合中是否有当前控件
            if (noEditTextView_Drawee_List1.get(i).getMemberid().equals(mItemBean.getMemberid())) {
                noEditTextView_Drawee_List1.get(i).setChange(true);
                // editAfterDrawee1.add(mItemBean);
            }
        }

        double editAfterDraweeMoney = 0;
        // 如果总额输入过
        if (!mConuntMoney_Change) {
            // // 总钱数 参与人列表总钱数

            double mCountMoney = Double.valueOf(tv_details_money.getText().toString());

            tv_details_money.setText(XzbUtils.inNumberFormat(mCountMoney) + "");
            onAverageParticipant(mCountMoney);
            onAverageDrawee(mCountMoney);
        } else if (!itemMoney1_Chang) {

            for (int i = 0; i < swipeLayoutList1.size(); i++) {
                TextView payment = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
                editAfterDraweeMoney += Double.valueOf(payment.getText().toString());
            }
            tv_details_money.setText(XzbUtils.inNumberFormat(editAfterDraweeMoney) + "");
            if (mItemMoney > editAfterDraweeMoney) {

            } else {
                // 小于 评分自己
                onAverageDrawee(editAfterDraweeMoney);
            }
        } else {
            for (int i = 0; i < swipeLayoutList2.size(); i++) {
                TextView payment = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
                editAfterDraweeMoney += Double.valueOf(payment.getText().toString());
            }
            tv_details_money.setText(XzbUtils.inNumberFormat(editAfterDraweeMoney) + "");
            onAverageParticipant(editAfterDraweeMoney);
            if (mItemMoney > editAfterDraweeMoney) {

            } else {
                // 小于 评分自己
                onAverageDrawee(editAfterDraweeMoney);
            }

        }

    }

    /**
     * 未编辑的付款人平分钱； 减去编辑过的，剩下的评分
     */
    private void onAverageParticipant(double money) {
        // 修改的人总钱数 未修改的赋值

        int noEditTextView_Count = 0;// 未编辑过的人数
        double editAfterParticipantMoney = 0;
        for (int i = 0; i < noEditTextView_Participant_List1.size(); i++) {
            // 编辑过的
            if (noEditTextView_Participant_List1.get(i).isChange()) {
                TextView payment = noEditTextView_Participant_List1.get(i).getItemTextView();
                editAfterParticipantMoney += Double.valueOf(payment.getText().toString());
            } else { // 未编辑过的
                noEditTextView_Count++;
            }
        }

        // 要赋值给未修改的，付款人的品均值
        double moneyCount = money - editAfterParticipantMoney;
        if (moneyCount < 0) {
            return;
        }

        double averageMoney;
        // 参与人可能只有一个，可能被编辑了，所以要判断
        // 获取未编辑的人数

        // 如果都没有编辑
        if (noEditTextView_Count > 0) {
            averageMoney = toDouble(moneyCount / noEditTextView_Count);
        } else {
            averageMoney = toDouble(moneyCount);
        }

        double averageMoneyCount;
        if (noEditTextView_Count > 0) {
            averageMoneyCount = toDouble(averageMoney * noEditTextView_Count);
        } else {
            averageMoneyCount = toDouble(averageMoney);
        }

        double m2 = toDouble(moneyCount - averageMoneyCount);
        boolean isfalse = true;
        for (int s = 0; s < noEditTextView_Participant_List1.size(); s++) {
            // 没有编辑过的
            if (!noEditTextView_Participant_List1.get(s).isChange()) {
                TextView payment = (TextView) noEditTextView_Participant_List1.get(s).getItemTextView();
                payment.setText(averageMoney + "");

                if (m2 != 0 && isfalse) {
                    isfalse = false;
                    payment.setText(toDouble(averageMoney + m2) + "");
                } else {
                    payment.setText(XzbUtils.inNumberFormat(averageMoney) + "");
                }
            }
        }

    }

    // 未编辑的参与人平分钱；传过来的钱就是付款人所有总金额，需要平分的是总金额减去textView上编辑过的钱

    /**
     * @param money 总钱数
     */
    private void onAverageDrawee(double money) {
        int noEditTextView_Count = 0;

        // 得到编辑过的参与人总钱数
        double editAfterDraweeMoney = 0;
        for (int i = 0; i < noEditTextView_Drawee_List1.size(); i++) {
            // 编辑过的
            if (noEditTextView_Drawee_List1.get(i).isChange()) {
                TextView payment = (TextView) noEditTextView_Drawee_List1.get(i).getItemTextView();
                editAfterDraweeMoney += Double.valueOf(payment.getText().toString());
            } else {
                noEditTextView_Count++;
            }

        }

        double averageCountMoney = money - editAfterDraweeMoney;
        if (averageCountMoney < 0) {
            return;
        }
        // 得到品均分摊的钱，赋值给没修改过的TextView

        double averageMoney;
        if (noEditTextView_Count > 0) {
            averageMoney = toDouble(averageCountMoney / noEditTextView_Count);
        } else {
            averageMoney = toDouble(averageCountMoney);
        }

        double averageMoneyCount;

        if (noEditTextView_Count > 0) {
            averageMoneyCount = toDouble(averageMoney * noEditTextView_Count);
        } else {
            averageMoneyCount = toDouble(averageMoney);
        }

        double m1 = toDouble(averageCountMoney - averageMoneyCount);

        boolean isfalse = true;
        for (int s = 0; s < noEditTextView_Drawee_List1.size(); s++) {
            // // 没有编辑过的
            if (!noEditTextView_Drawee_List1.get(s).isChange()) {
                TextView payment = (TextView) noEditTextView_Drawee_List1.get(s).getItemTextView();

                if (m1 != 0 && isfalse) {
                    isfalse = false;
                    payment.setText(toDouble(averageMoney + m1) + "");
                } else {
                    payment.setText(XzbUtils.inNumberFormat(averageMoney) + "");
                }
            }
        }
    }

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

    private void hidesystemkeyboard() {
        if (tv_details_remarks.hasFocus()) {
            tv_details_remarks.setCursorVisible(false);
            imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
        }
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        // 用于当关闭软件盘的后，再弹起对谁输入都是直接删除
        before_isPeopleType = -1;
        before_textTag = 0;
        int visibility = keyboard_view.getVisibility();
        if (visibility == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
            anim.setDuration(300);
            keyboard_view.setVisibility(View.GONE);
            keyboard_view.startAnimation(anim);
            if (canScroll(scroll_expend)) {
                setScrollPadding(scroll_expend, 0);
            } else {
                View layout = scroll_expend.getChildAt(0);
                layout.scrollBy(0, -scrollCount);
            }
            scrollCount = 0;
        }

    }

    public void showKeyboard() {
        tv_details_remarks.setCursorVisible(false);
        imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
        int visibility = keyboard_view.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            anim.setDuration(300);
            keyboard_view.setVisibility(View.VISIBLE);
            keyboard_view.startAnimation(anim);

        } else {
            // 如果软件盘没有关闭的情况下，切换item则需要计算
            String mMoney = mItemBean.getItemTextView().getText().toString();
            mMoney = onPlusMoney(mMoney);
            mItemBean.getItemTextView().setText(mMoney);

            if (isPeopleType == 0) {
                goHalvesParticipant(Double.valueOf(mMoney));
            } else if (isPeopleType == 1) {// 参与人
                goHalvesDrawee(Double.valueOf(mMoney));
            } else if (isPeopleType == 1000) {
                // 当点击上面的总金额输入条后，会赋值1000
                onAverageParticipant(Double.valueOf(mMoney));
                onAverageDrawee(Double.valueOf(mMoney));
            }

        }

    }

    private void showAccountIcon(View view) {
        int visibility = view.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.accounting_icon_enlarge);
            anim.setDuration(300);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(anim);
        }
    }

    public void hideAccountIcon(View view) {
        int visibility = view.getVisibility();
        if (visibility == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.accounting_icon_narrow);
            anim.setDuration(300);
            view.setVisibility(View.GONE);
            view.startAnimation(anim);
        }
    }

    private void setIconAndName(ChildBean bean) {
        String mDataid = bean.getCategoryid();
        setImage(icon_accounting, mDataid, bean.getCategoryicon());
        name_accounting.setText(bean.getCategorytitle());
        icon_accounting.setBackgroundResource(0);
    }

    private void setIconAndName(Category bean) {
        String mDataid = bean.getIcon();
        if (!TextUtils.isEmpty(bean.getTopCategoryId()) && !mDataid.equals(bean.getTopCategoryId())) {
            mDataid = mDataid + "child";
        }
        setImage(icon_accounting, mDataid, bean.getIcon());
        name_accounting.setText(bean.getCategory());
        icon_accounting.setBackgroundResource(0);
    }

    private void setIconAndName() {
        icon_accounting.setBackgroundResource(R.drawable.write_default_icon);
        icon_accounting.setImageResource(-1);
        name_accounting.setText("类别");
    }

    /**
     * @param billcatename    一级名称
     * @param billcateid      一级图片ID
     * @param billsubcatename 二级名称
     * @param billsubcateid   二级图片ID
     */
    private void setIconAndName(String billcatename, String billcateid, String billsubcatename, String billsubcateid) {
        icon_accounting.setBackgroundResource(0);
        //支出
        if (mType == 1) {
            if (!TextUtils.isEmpty(billsubcatename)) {
                setImage(icon_accounting, billsubcateid + "child");
                name_accounting.setText(billsubcatename);

                expenditureCategory.setCategory(billsubcatename);
                expenditureCategory.setIcon(billsubcateid /*+ "child"*/);
                expenditureCategory.setTopCategoryId(billcateid);
            } else {
                setImage(icon_accounting, billcateid);
                name_accounting.setText(billcatename);
                expenditureCategory.setCategory(billcatename);
                expenditureCategory.setIcon(billcateid);
            }
        } else {
            //交款
            icon_accounting.setImageResource(R.drawable.payment);
            name_accounting.setText("成员交款");
        }
    }


    private void onSelectedTimeAnimStart(final TextView todayTime, final TextView selecteTime) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(todayTime, "alpha", 1, 0);
        ObjectAnimator alpha_animator = ObjectAnimator.ofFloat(selecteTime, "alpha", 1, 0);
        ObjectAnimator translationX_animator = ObjectAnimator.ofFloat(selecteTime, "translationX", 0, UIHelper.Dp2Px(this, -200));
        AnimatorSet animatorSet = new AnimatorSet();
        //动画一起执行
        animatorSet.playTogether(animator, alpha_animator, translationX_animator);
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onTimeSelected(false, date, tv_details_time, tv_details_time_selected);
                onSelectedTimeAnimStartEnd(todayTime, selecteTime);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void onSelectedTimeAnimStartEnd(TextView todayTime, TextView selecteTime) {
        //显示——透明——显示
        ObjectAnimator animator = ObjectAnimator.ofFloat(todayTime, "alpha", 0, 1);
        animator.setDuration(100);
        animator.start();
        //显示—不显示
        ObjectAnimator alpha_animator = ObjectAnimator.ofFloat(selecteTime, "alpha", 0, 1);
        //像左移动
        ObjectAnimator translationX_animator = ObjectAnimator.ofFloat(selecteTime, "translationX", UIHelper.Dp2Px(this, -200), 0);
        AnimatorSet animatorSet = new AnimatorSet();
        //先后执行
        animatorSet.playSequentially(translationX_animator, alpha_animator);
        animatorSet.setDuration(200);
        animatorSet.start();

    }

    /**
     * @param isSelected
     * @param date         当前时间
     * @param todayTime
     * @param electionTime 点击的是时间控件
     */
    private void onTimeSelected(boolean isSelected, Date date, TextView todayTime, TextView electionTime) {
        //判断是否是今天
        if (isSelected) {
            if (DateUtil.isToday(date)) {
                todayTime.setText("今天");
                electionTime.setText("昨天?");
            } else {
                todayTime.setText(DateUtil.formatTheDateToMM_dd(date, 1));
                electionTime.setText("今天?");
            }
        } else {
            if (DateUtil.isToday(date)) {
                todayTime.setText("今天");
                electionTime.setText("昨天?");
            } else {
                todayTime.setText("昨天");
                electionTime.setText("今天?");
            }
        }
    }


    private LinearLayout ll_details_point_group;
    private boolean ll_details_point_group_is_show;

    private void setViewPage(BookExpenditure mData, ViewPager mViewPage, final LinearLayout ll_details_point_group) {
        final List<ViewGroup> mViewGroupList = new ArrayList<ViewGroup>();
        mViewGroupList.clear();
        // 这里拿到服务器数据，将服务器数据添加到View中
        View childitemBookView;
        int count = mData.getChild().size();
        count = count + 1;//加一是为了加自定义按钮
        ViewPageItem item_expenditure_viewpage_one = new ViewPageItem(this);

        mViewGroupList.add(item_expenditure_viewpage_one);
        item_expenditure_viewpage_one.removeAllViews();

        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < count; i++) {
            if (i % ICON_COUNT == 0 && i != 0) {
                item_expenditure_viewpage_one = new ViewPageItem(this);
                mViewGroupList.add(item_expenditure_viewpage_one);
            }

            if (i == count - 1) {
                //这里添添加设置自定义字段的按钮
                childitemBookView = LayoutInflater.from(this).inflate(R.layout.item_book, item_expenditure_viewpage_one,
                        false);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.width = (int) item_viewGroup;
                childitemBookView.setLayoutParams(layoutParams);
                TextView tv = (TextView) childitemBookView.findViewById(R.id.tv_itembook);
                tv.setText("自定义");
                childitemBookView.setTag(i);
                item_expenditure_viewpage_one.addView(childitemBookView);
                continue;
            }

            ChildBean item = mData.getChild().get(i);
            childitemBookView = LayoutInflater.from(this).inflate(R.layout.item_book, item_expenditure_viewpage_one,
                    false);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (int) item_viewGroup;
            childitemBookView.setLayoutParams(layoutParams);
            TextView tv = (TextView) childitemBookView.findViewById(R.id.tv_itembook);
            tv.setText(item.getCategorytitle());
            childitemBookView.setTag(i);
            item_expenditure_viewpage_one.addView(childitemBookView);

        }

        mExpenditurePageAdapter = new RecyclePageAdapter(this, mViewGroupList);
        mViewPage.setAdapter(mExpenditurePageAdapter);

        ll_details_point_group.removeAllViews();
        for (int i = 0; i < mViewGroupList.size(); i++) {
            // 每循环一次添加一个点到现形布局中
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_background);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIHelper.Dp2Px(this, 5), UIHelper.Dp2Px(
                    this, 5));
            params.leftMargin = UIHelper.Dp2Px(this, 5);
            view.setEnabled(false);
            view.setLayoutParams(params);
            ll_details_point_group.addView(view); // 向线性布局中添加“点”
        }
        ll_details_point_group.getChildAt(0).setEnabled(true);
        // viewPage添加监听
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int preEnablePositon = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                // 取余后的索引
                int newPositon = position % mViewGroupList.size();
                // 根据索引设置那个点被选中
                ll_details_point_group.getChildAt(newPositon).setEnabled(true);
                // 把上一个点设置为被选中
                ll_details_point_group.getChildAt(preEnablePositon).setEnabled(false);
                preEnablePositon = newPositon;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (mViewGroupList.size() > 1) {
            ll_details_point_group.setVisibility(View.VISIBLE);
            ll_details_point_group_is_show = true;
        } else {
            ll_details_point_group.setVisibility(View.INVISIBLE);
            ll_details_point_group_is_show = false;
        }

        // 如果是添加则下标第一个为选中
        if (isAdd) {
            if (isSpeech) {
                settingImage(billcateid, "", mData, mViewGroupList);
            } else {
                settingImage("", "", mData, mViewGroupList);
            }
        } else {
            //如果是支出
            if (mDailycostEntity.getBilltype().equals("1")) {
                settingImage(billcateid, billsubcateid, mData, mViewGroupList);
            } else {
                settingImage("", "", mData, mViewGroupList);
            }

        }
        initViewPageListener(mData, mViewGroupList);
    }


    private void initViewPageListener(final BookExpenditure mData, List<ViewGroup> mViewGroupList) {
        final int count = mData.getChild().size() + 1;//加一是为了多出一个【自定义】按钮


        ViewGroup item_expenditure_viewpage = mViewGroupList.get(0);
        for (int i = 0; i < count; i++) {
            final int index = i;
            if (i % ICON_COUNT == 0 && i != 0) {
                item_expenditure_viewpage = mViewGroupList.get(i / ICON_COUNT);
            }

            if (i == count - 1) {//如果是【自定义】按钮
                item_expenditure_viewpage.getChildAt(i % ICON_COUNT).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BookCategoryManagerActivity.open(mContext, bookid, bookName, 1);
                    }
                });
                continue;
            }

            item_expenditure_viewpage.getChildAt(i % ICON_COUNT).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 当前点击的对象
                    expenditure_bean = mData.getChild().get((int) v.getTag());
                    String mDataid = mData.getChild().get(index).getCategoryid();
                    ImageView imageview = ((ImageView) ((ViewGroup) v).getChildAt(0));

                    if (expenditure_bean.getCategoryid() != before_expenditure_bean.getCategoryid()) {
                        if (before_expenditure_imageView != null) {
                            before_expenditure_imageView.setBackgroundResource(0);
                        } else {
                            //不处理
                        }
                        setImage(imageview, mDataid, expenditure_bean.getCategoryicon());
                        imageview.setBackgroundResource(R.drawable.write_default_expenditure);
                    } else {
                        imageview.setBackgroundResource(R.drawable.write_default_expenditure);
                    }

                    before_expenditure_bean = expenditure_bean;
                    before_expenditure_imageView = imageview;

                    String expenditure_category = ((TextView) ((ViewGroup) v).getChildAt(1)).getText().toString();
                    List<ChildBean> expenditure_bean_list = expenditure_bean.getChild();

                    expenditureCategory.setCategory(expenditure_category);
                    expenditureCategory.setIcon(mDataid);
                    expenditureCategory.setTopCategoryId(mDataid);
                    if (!StringUtils.isEmptyList(expenditure_bean_list)) {
                        List<ChildBean> mList = new ArrayList<>();
                        ChildBean bean = new ChildBean();
                        bean.setCategoryicon(expenditure_bean.getCategoryicon());
                        bean.setCategorytitle(expenditure_bean.getCategorytitle());
                        bean.setCategoryid(expenditure_bean.getCategoryid());
                        mList.add(expenditure_bean);
                        mList.addAll(expenditure_bean_list);
                        // 传二级子目录
                        showSonPopu(expenditure_bean.getCategorytitle(), expenditure_category, mList, v);
                    } else {
                        //如果点击的item没有2级那就隐藏View
                        hideAccountIcon(i_accounting_icon);
                        setIconAndName(expenditure_bean);
                        showKeyboard();
                    }
                }
            });
        }
    }

    /**
     * 初始化成员
     */
    private void getNativePeopleByBook(final String bookid) {
        String where = DtBookMemberColumns.BOOKID + "=?";
        String[] selectionArgs = new String[]{bookid};

        DownUrlUtil.getNativePeopleByBook(where, selectionArgs, handler, 2);

    }

    /**
     *
     */
    private void getNativePeopleByBill(final String billId) {
        // BillMemberInfo.BILLID;
        String where = BillMemberInfo.BILLID + "=?";
        String[] selectionArgs = new String[]{billId};

        DownUrlUtil.getNativePeopleByBill(where, selectionArgs, handler, 1);

    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    // 付款人
                    mBooksDbMemberInfosList1.clear();
                    // 参与人
                    mBooksDbMemberInfosList2.clear();
                    List<BillMemberInfo> mBillMemberInfoList = (List<BillMemberInfo>) msg.obj;

                    BooksDbMemberInfo mBooksDbMemberInfo;
                    for (BillMemberInfo data : mBillMemberInfoList) {
                        mBooksDbMemberInfo = new BooksDbMemberInfo();

                        mBooksDbMemberInfo.setBalance(Double.valueOf(data.getAmount()));
                        mBooksDbMemberInfo.setBookid(bookid);
                        mBooksDbMemberInfo.setBookName(bookName);
                        mBooksDbMemberInfo.setDeviceid(data.getDeviceid());
                        mBooksDbMemberInfo.setId(data.getId());
                        mBooksDbMemberInfo.setIndex(data.getIndexid());
                        mBooksDbMemberInfo.setMemberid(data.getMemberid());
                        mBooksDbMemberInfo.setStatus(Integer.valueOf(data.getStatus()));
                        mBooksDbMemberInfo.setType(data.getType());
                        mBooksDbMemberInfo.setUserid(data.getUserid());
                        mBooksDbMemberInfo.setUsername(data.getUsername());
                        mBooksDbMemberInfo.setUsericon(data.getUsericon());

                        if (mDailycostEntity.getBilltype().equals("4")) {
                            if (data.getType().equals("0")) {
                                mBooksDbMemberInfosList2.add(mBooksDbMemberInfo);
                            } else if (data.getType().equals("1")) {
                                mBooksDbMemberInfosList1.add(mBooksDbMemberInfo);
                            }
                        } else {
                            if (data.getType().equals("0")) {
                                mBooksDbMemberInfosList1.add(mBooksDbMemberInfo);
                            } else if (data.getType().equals("1")) {
                                mBooksDbMemberInfosList2.add(mBooksDbMemberInfo);
                            }
                        }

                    }

                    initDrawee(mBooksDbMemberInfosList2);
                    initParticipant(mBooksDbMemberInfosList1);
                    break;

                case 2:
                    // 付款人
                    mBooksDbMemberInfosList1.clear();
                    // 参与人
                    mBooksDbMemberInfosList2.clear();
                    List<BooksDbMemberInfo> lis = (List<BooksDbMemberInfo>) msg.obj;
                    mBooksDbMemberInfosList2.addAll(lis);
                    initDrawee(mBooksDbMemberInfosList2);
                    for (int i = 0; i < lis.size(); i++) {

                        // 登录的情况下使用Userid获取付款人
                        if (!TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                            if (lis.get(i).getUserid().equals(myuid)) {
                                MyMemberid = lis.get(i).getMemberid();
                                mUserName = lis.get(i).getUsername();
                                mBooksDbMemberInfosList1.add(lis.get(i));
                            }
                        } else {
                            // 未登录的情况下使用获取付款人
                            if (lis.get(i).getDeviceid()
                                    .equals(LoginConfig.getInstance().getDeviceid())) {
                                MyMemberid = lis.get(i).getMemberid();
                                mUserName = lis.get(i).getUsername();
                                mBooksDbMemberInfosList1.add(lis.get(i));
                            }
                        }

                    }

                    initParticipant(mBooksDbMemberInfosList1);

                    if (isSpeech) {
                        double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
                        onAverageParticipant(mCountMoney);
                        onAverageDrawee(mCountMoney);
                    }

                    break;

                case 110:
                    jsonString = (String) msg.obj;
                    if (TextUtils.isEmpty(jsonString)) {
                        return;
                    }
                    getNativeData();
                    break;

                case 10:
                    //记一笔存储时间，3天后弹评分窗口
                    SPUtils.setParam(LoginConfig.getInstance().getUserid() + "showCommentScore", DateUtil.getTodayDate());
                    stopRemind();
                    isNeeddruphome = true;
                    // 不是再记一笔则跳转出去
                    startActivityToHome("true");
                    break;
                case 11:
                    stopRemind();
                    isNeeddruphome = true;
                    billAddShow();//显示账单添加成功
                    saveBillAgainChangImage();
                    break;
                case RequsterTag.QINIUPATH:
                    // 调用云存储接口后获取七牛图片地址
                    String key = msg.toString();
//                    QiniuJsonInfo qiniuJsonInfo = GsonUtil.GsonToBean(jsonString, QiniuJsonInfo.class);
                    String url = LoginConfig.getInstance().getQiniuHost() + key;
                    uri = Uri.parse(url);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 关闭提醒
     */
    private void stopRemind() {
        if ((boolean) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "isRemind", true)) {
            if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天").equals("智能")) {
                Intent intent = new Intent(mContext, RemindReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(mContext, 0,
                        intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                //取消警报
                am.cancel(pi);
            }
        }
    }

    private void saveBillAgainChangImage() {
        if (mType == 1) {
            expenditureCategory.setCategory("");
            before_expenditure_imageView.setBackgroundResource(0);
            setIconAndName();
            showAccountIcon(i_accounting_icon);
        } else {
            //不处理
        }
        //金额
        tv_details_money.setText("0.00");
        double mCountMoney = Double.valueOf(tv_details_money.getText().toString());
        onAverageParticipant(mCountMoney);
        onAverageDrawee(mCountMoney);

        //清空拍照
        uri = null;
        iv_details_photograph.setVisibility(View.VISIBLE);
        iv_details_picture.setVisibility(View.GONE);
        //备注
        tv_details_remarks.setText("");
        // 添加完数据后去除备注上的光标
        tv_details_remarks.setCursorVisible(false);
        //重新初始化为今天
//        date = new Date();
//        onTimeSelected(true, date, tv_details_time, tv_details_time_selected);
//        //账户
//        tv_details_account.setText("");
//        iv_details_account.setImageResource(R.drawable.write_account);
//        accountnumber = "0";
//        //定位
//        tv_details_location.setText("");
    }

    private void saveBillToNative(final boolean agin) {
        String pkid = StringUtils.getUUID();
        if (isAdd) {
            addDailycostInfo(pkid, pkid, agin);
        } else {
            if (isFistQuit) {
                updateDailycostInfo(mDailycostEntity, mDailycostEntity.getPkid(), mDailycostEntity.getBillid(), agin);
                isFistQuit = false;
            } else {
                addDailycostInfo(pkid, pkid, agin);
            }
        }

    }


    /**
     * 本地添加账单
     *
     * @param mPkid   账单为唯一ID
     * @param mBillid 账单对应的服务器ID
     */
    private void addDailycostInfo(String mPkid, final String mBillid, final boolean agin) {
        long curTimestamp = System.currentTimeMillis() / 1000;
        String details_money = tv_details_money.getText().toString();
        final DailycostEntity dailycostEntity = new DailycostEntity();

        String textName = tv_details_remarks.getText().toString();
        if (textName.equals("备注说明")) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }
        // 消费类型
        dailycostEntity.setBilltype(mType + "");// 收入或支出
        // 消费金额

        dailycostEntity.setIssynchronization(isSynchronization + "");
        dailycostEntity.setBillstatus("1");
        dailycostEntity.setWhichbook(bookName);
        dailycostEntity.setAccountbooktype("1");// 是否多人账本
        dailycostEntity.setAccountbookid(bookid);
        dailycostEntity.setUsername(mUserName);
        dailycostEntity.setBillcount(mBooksDbMemberInfosList2.size() + ""); // 记账人数
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber(accountnumber);

        long mTradetime = getLongData();
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)
        String money = onSumMoney(details_money, tv_details_money);
        dailycostEntity.setBillamount(money);
        dailycostEntity.setPkid(mPkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid(mBillid);
        dailycostEntity.setBillclear(0 + "");// 0还没有结算
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间
        dailycostEntity.setBillctime(curTimestamp + "");
        dailycostEntity.setDeviceid(LoginConfig.getInstance().getDeviceid());
        dailycostEntity.setIsclear(1 + "");// 是否需要结算1 需要结算
        if (mType == 1) {
            dailycostEntity.setBillcateid(expenditure_bean.getCategoryid());// 账单一级分类id
            dailycostEntity.setBillcatename(expenditure_bean.getCategorytitle()); // 账单一级分类名称

            String mCategoryid = expenditure_bean.getCategoryid();
            String url_native_mCategoryid = XzbUtils.getPathString() + "/" + mCategoryid + "_s";
            dailycostEntity.setBillcateicon(url_native_mCategoryid); // 账单一级分类Icon

            if (expenditure_son_bean != null) {
                dailycostEntity.setBillsubcateid(expenditure_son_bean.getCategoryid());// 账单二级分类id
                dailycostEntity.setBillsubcatename(expenditure_son_bean.getCategorytitle());// 账单二级分类名称

                String billsubcateicon = expenditure_son_bean.getCategoryid();
                String url_native_billsubcateicon = XzbUtils.getPathString() + "/" + billsubcateicon + "child_s";
                dailycostEntity.setBillsubcateicon(url_native_billsubcateicon);// 账单二级分类Icon
            }
        } else {
            dailycostEntity.setBillcatename("成员交款"); // 账单一级分类名称
        }

        if (uri != null) {
            String urlPath = uri.toString();
            if (TextUtils.isEmpty(urlPath)) {
                dailycostEntity.setBillimg("");
            } else {
                if (!urlPath.contains("file://")) {
                    urlPath = "file://" + urlPath;
                }
                dailycostEntity.setBillimg(urlPath);
            }

        } else {
            dailycostEntity.setBillimg("");
        }
        String location = tv_details_location.getText().toString();
        if (TextUtils.isEmpty(location)) {
            dailycostEntity.setAddress("");
        } else {
            dailycostEntity.setAddress(location);
        }

        dailycostEntity.setUserid(LoginConfig.getInstance().getUserid());
        dailycostEntity.setMemberlist(addBillMemberInfo(mBooksDbMemberInfosList1, mBooksDbMemberInfosList2, mBillid));
        int what;
        if (!agin) {
            what = 10;
        } else {
            what = 11;
        }

        DownUrlUtil.addDailycostInfo(dailycostEntity, handler, what,
                addBillMemberInfo(mBooksDbMemberInfosList1, mBooksDbMemberInfosList2, mBillid));
    }

    /**
     * 添加账单人员
     */
    private List<BillMemberInfo> addBillMemberInfo(List<BooksDbMemberInfo> mBooksDbMemberInfosList1,
                                                   List<BooksDbMemberInfo> mBooksDbMemberInfosList2, String mBillid) {
        List<BillMemberInfo> billMemberInfoList = new ArrayList<>();

        BillMemberInfo mBillMemberInfo;
        BooksDbMemberInfo mBooksDbMemberInfo;
        TextView moneyText;
        String amount;
        for (int i = 0; i < mBooksDbMemberInfosList1.size(); i++) {
            mBillMemberInfo = new BillMemberInfo();

            mBooksDbMemberInfo = mBooksDbMemberInfosList1.get(i);
            moneyText = (TextView) swipeLayoutList1.get(i).findViewById(R.id.tv_expend_money);
            amount = moneyText.getText().toString();

            mBillMemberInfo.setMemberid(mBooksDbMemberInfo.getMemberid());
            mBillMemberInfo.setType(0 + "");
            if (mType == 4) {
                mBillMemberInfo.setType(1 + "");
            }

            mBillMemberInfo.setAmount(amount);
            mBillMemberInfo.setStatus(mBooksDbMemberInfo.getStatus() + "");
            mBillMemberInfo.setUsername(mBooksDbMemberInfo.getUsername());
            mBillMemberInfo.setUsericon(mBooksDbMemberInfo.getUsericon());
            mBillMemberInfo.setIndexid(mBooksDbMemberInfo.getIndex());
            mBillMemberInfo.setBillid(mBillid);
            mBillMemberInfo.setIssynchronization(isSynchronization + "");
            mBillMemberInfo.setDeviceid(mBooksDbMemberInfo.getDeviceid());
            mBillMemberInfo.setUserid(mBooksDbMemberInfo.getUserid());

            billMemberInfoList.add(mBillMemberInfo);

        }

        for (int i = 0; i < mBooksDbMemberInfosList2.size(); i++) {
            mBillMemberInfo = new BillMemberInfo();

            mBooksDbMemberInfo = mBooksDbMemberInfosList2.get(i);
            moneyText = (TextView) swipeLayoutList2.get(i).findViewById(R.id.tv_expend_money);
            amount = moneyText.getText().toString();

            mBillMemberInfo.setMemberid(mBooksDbMemberInfo.getMemberid());
            mBillMemberInfo.setType(1 + "");
            if (mType == 4) {
                mBillMemberInfo.setType(0 + "");
            }
            mBillMemberInfo.setAmount(amount);
            mBillMemberInfo.setStatus(mBooksDbMemberInfo.getStatus() + "");
            mBillMemberInfo.setUsername(mBooksDbMemberInfo.getUsername());
            mBillMemberInfo.setUsericon(mBooksDbMemberInfo.getUsericon());
            mBillMemberInfo.setIndexid(mBooksDbMemberInfo.getIndex());
            mBillMemberInfo.setBillid(String.valueOf(mBillid));
            mBillMemberInfo.setIssynchronization(isSynchronization + "");
            mBillMemberInfo.setDeviceid(mBooksDbMemberInfo.getDeviceid());
            mBillMemberInfo.setUserid(mBooksDbMemberInfo.getUserid());

            billMemberInfoList.add(mBillMemberInfo);

        }
        return billMemberInfoList;

    }

    /**
     * 修改本地账单
     *
     * @param dailycostEntity
     * @param mPkid
     * @param mBillid
     * @param agin
     */
    private void updateDailycostInfo(final DailycostEntity dailycostEntity, String mPkid, final String mBillid,
                                     final boolean agin) {

        long curTimestamp = System.currentTimeMillis() / 1000;

        String money = tv_details_money.getText().toString();
        if (money.equals("0.00")) {
            double balance = Double.parseDouble(dailycostEntity.getBillamount());
            DecimalFormat df = new DecimalFormat("0.00");
            String a = df.format(balance);
            tv_details_money.setText(a);
            return;
        }

        String textName = tv_details_remarks.getText().toString();
        // 如果当前备注说明textName为：“备注说明”则setContext设置为“”,否则有值的情况下才赋值
        // 首页适配器做修改
        if (textName.equals("备注说明")) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }

        // 消费类型
        dailycostEntity.setBilltype(mType + "");
        money = onSumMoney(money, tv_details_money);
        dailycostEntity.setBillamount(money);
        dailycostEntity.setAccountbooktype("1");
        dailycostEntity.setBillcount(mBooksDbMemberInfosList2.size() + ""); // 记账人数
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber(accountnumber);


        dailycostEntity.setPkid(mPkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid(mBillid + "");//
        long mTradetime = getLongData();
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)
        dailycostEntity.setIsclear(1 + "");// 是否需要结算1 需要结算
        dailycostEntity.setIssynchronization("edit");

        if (mType == 1) {
            dailycostEntity.setBillcateid(expenditure_bean.getCategoryid());// 账单一级分类id
            dailycostEntity.setBillcatename(expenditure_bean.getCategorytitle()); // 账单一级分类名称
            String mCategoryid = expenditure_bean.getCategoryid();
            String url_native_mCategoryid = XzbUtils.getPathString() + "/" + mCategoryid + "_s";
            dailycostEntity.setBillcateicon(url_native_mCategoryid); // 账单一级分类Icon
            if (expenditure_son_bean != null) {
                dailycostEntity.setBillsubcateid(expenditure_son_bean.getCategoryid());// 账单二级分类id
                String billsubcateicon = expenditure_son_bean.getCategoryid();
                String url_native = XzbUtils.getPathString() + "/" + billsubcateicon + "child_s";
                dailycostEntity.setBillsubcatename(expenditure_son_bean.getCategorytitle());
                dailycostEntity.setBillsubcateicon(url_native);
            }
        } else {
            //就是交款
        }


        if (uri != null) {
            String urlPath = uri.toString();
            if (TextUtils.isEmpty(urlPath)) {
                dailycostEntity.setBillimg("");
            } else {
                if (!urlPath.contains("file://") && !urlPath.contains("http")) {
                    urlPath = "file://" + urlPath;
                }
                dailycostEntity.setBillimg(urlPath);
            }
        } else {
            dailycostEntity.setBillimg("");
        }
        String location = tv_details_location.getText().toString();
        if (TextUtils.isEmpty(location)) {
            dailycostEntity.setAddress("");
        } else {
            dailycostEntity.setAddress(location);
        }

        if (dailycostEntity.getIssynchronization().equals("true")) {
            dailycostEntity.setIssynchronization("edit");
        } else {
            //不做修改
        }
        int what;
        if (!agin) {
            what = 10;
        } else {
            what = 11;

        }
        String where = DbInterface.BILLID + "=?";
        String[] selectionArgs = new String[]{dailycostEntity.getBillid()};
        dailycostEntity.setMemberlist(addBillMemberInfo(mBooksDbMemberInfosList1, mBooksDbMemberInfosList2, mBillid));
        DownUrlUtil.updateDataBaseDailycostInfo(dailycostEntity, where, selectionArgs, handler, what,
                addBillMemberInfo(mBooksDbMemberInfosList1, mBooksDbMemberInfosList2, mBillid));
        MobclickAgent.onEvent(this, "updateDailycostInfo");

    }

    private ImageView before_expenditure_imageView;

    // 显示2级子目录
    private void showSonPopu(String parentName, String titleName, final List<ChildBean> mChildSon_List, final View view) {

        popupWindowUtils po = new popupWindowUtils();
        po.showSonIconPopupWindow(true, bookid, parentName, titleName, this, mChildSon_List);
        po.setIconClickListener(new onSonIconClickListener() {

            @Override
            public void setonSonIcon(View child) {
                // TODO Auto-generated method stub
                int index = (int) child.getTag();

                TextView textView = (TextView) ((ViewGroup) view).getChildAt(1);
                ImageView imageview = (ImageView) ((ViewGroup) view).getChildAt(0);

                before_expenditure_imageView = imageview;
                expenditure_son_bean = mChildSon_List.get(index);
                textView.setText(expenditure_son_bean.getCategorytitle());
                String mDataid = expenditure_son_bean.getCategoryid();
                setIconAndName(expenditure_son_bean);
                setImage(imageview, mDataid, expenditure_son_bean.getCategoryicon());
                expenditureCategory.setCategory(expenditure_son_bean.getCategorytitle());
                expenditureCategory.setIcon(mDataid);
                imageview.setBackgroundResource(R.drawable.write_default_expenditure);
                //二级子目录点击的是展开的第一个那么expenditure_son_bean就为空
                if (index == 0) {
                    expenditure_son_bean = null;
                } else {
                    //不做处理
                }
                hideAccountIcon(i_accounting_icon);
                showKeyboard();
            }
        });
    }


    public boolean canScroll(ScrollView mSceollView) {
        View child = mSceollView.getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return mSceollView.getHeight() < childHeight;
        }
        return false;
    }

    private int scrollCount;

    // 对View中的类容滑动 MarginLayoutParams 这是View移动
    private void onScrillView(final ScrollView mSceollView, View view) {
        // 获取点击View的坐标
        int Pos[] = {-1, -1}; // 保存当前坐标的数组
        view.getLocationOnScreen(Pos);
        int OldListY = Pos[1];
        // 获取软件盘的坐标
        int Pos_Keyboard[] = {-1, -1}; // 保存当前坐标的数组
        keyboard_view.getLocationOnScreen(Pos_Keyboard);
        int KeyboardY = Pos_Keyboard[1];
        int view_touch = OldListY + view.getHeight();

        if (KeyboardY > view_touch) {
            return;
        }
        final int scrollY = mSceollView.getScrollY();
        scrollCount = view_touch - KeyboardY;

        if (canScroll(mSceollView)) {
            setScrollPadding(mSceollView, scrollCount);
            mSceollView.smoothScrollBy(0, scrollCount);
        } else {
            View layout = mSceollView.getChildAt(0);
            layout.scrollBy(0, scrollCount);
        }

    }

    private void setScrollPadding(ScrollView mSceollView, int scrollCount) {
        mSceollView.setPadding(0, 0, 0, scrollCount);

    }

    /**
     * 关闭软件盘的时候或者点击另外一个Item的时候改变颜色
     */
    private void setSwipeLayoutBackgroundColor() {
        if (swipeLayoutList1.size() > selectTag1) {
            swipeLayoutList1.get(selectTag1).setBackgroundColor(0);
        }
        if (swipeLayoutList2.size() > Math.abs(selectTag2)) {
            swipeLayoutList2.get(Math.abs(selectTag2)).setBackgroundColor(0);
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivityToHome("false");
    }

    //创建一个以时间结尾的png图片路径
    private void initPath() {
        uri = XzbUtils.getTpPath();
    }

    //权限管理，用于拍照，现在targetSdkVersion21,还不涉及到权限申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (requestCode == 1) {
                    if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        new ActionSheetDialog(ExpenditureActivity.this)
                                .builder()
                                .setCancelable(true)
                                .setCanceledOnTouchOutside(true)
                                .setCancelTextColor(getResources().getColor(R.color.main_back))
                                .addSheetItem(getString(R.string.home_take_photo), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                choiceFromCamera();

                                            }
                                        })
                                .addSheetItem(getString(R.string.home_from_ablum), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                        new ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                Intent intent = new Intent(ExpenditureActivity.this, PictureListActivity.class);
                                                intent.putExtra("isShowCut", false);
                                                intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                                startActivity(intent);
                                            }
                                        }).show();
                    } else {
                        //用户不同意，自行处理即可
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showSimpleAlertDialog(null, "请在手机的“设置→一起记→存储空间”选项中，允许访问您的存储空间", "现在设置", "我知道了", false, false, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        startActivity(intent);
                                        dismissDialog();
                                    }
                                }, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dismissDialog();
                                    }
                                });
                            }
                        }
                    }
                }
                break;
            case ACCESS_FINE_LOCATION_S:
                PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
                break;
        }

    }

    private void choiceFromCamera() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "没有内存卡", Toast.LENGTH_SHORT).show();
            ToastUtils.DiyToast(this, "没有内存卡");
            return;
        }
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        initPath();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        //为拍摄的图片指定一个存储的路径
        if (currentapiVersion < 24) {
            //为拍摄的图片指定一个存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, StringUtils.getRealFilePath(this, uri));
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        try {
            startActivityForResult(intent, RequsterTag.RQ_TAKE_A_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log_error(null, e);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            // 付款人
            case Mars:
                ArrayList<BooksDbMemberInfo> mBooksDbMemberInfo1 = data.getParcelableArrayListExtra("BooksDbMemberInfo");
                mBooksDbMemberInfosList1.clear();
                mBooksDbMemberInfosList1.addAll(mBooksDbMemberInfo1);
                initParticipant(mBooksDbMemberInfosList1);
                double mCountMoney1 = Double.valueOf(tv_details_money.getText().toString());
                onAverageParticipant(mCountMoney1);

                break;
            // 参与人
            case Moon:
                ArrayList<BooksDbMemberInfo> mBooksDbMemberInfo2 = data.getParcelableArrayListExtra("BooksDbMemberInfo");
                mBooksDbMemberInfosList2.clear();
                mBooksDbMemberInfosList2.addAll(mBooksDbMemberInfo2);
                initDrawee(mBooksDbMemberInfosList2);
                double mCountMoney2 = Double.valueOf(tv_details_money.getText().toString());
                onAverageDrawee(mCountMoney2);
                break;

            case RequsterTag.RQ_TAKE_A_PHOTO:// 拍照
                iv_details_photograph.setVisibility(View.GONE);
                iv_details_picture.setVisibility(View.VISIBLE);
//                File file = new File(uri.toString());
                File file = new File(uri.toString());
                if (uri != null) {
                    Bitmap bitmap = null;
                    String pathString;
                    try {
                        bitmap = BitmapUtil.getBitmapFormUri(this, uri, UIHelper.Dp2Px(this, 200), UIHelper.Dp2Px(this, 200));
                        pathString = StringUtils.getRealFilePath(this, XzbUtils.getPath());

                        BitmapUtil.saveBitmapToFile(bitmap, pathString);
                        if (!pathString.contains("file://")) {
                            pathString = "file://" + pathString;
                        }
                        uri = Uri.parse(pathString);

                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.log_error(null, e);
                    }


                }
                XzbUtils.displayImage(iv_details_picture, uri.toString(), 0);
                break;

            case IsphotosShow:
                //点击删除图片后返回
                String path = data.getStringExtra("path");
                if (TextUtils.isEmpty(path)) {
                    uri = Uri.parse(path);
                    iv_details_photograph.setVisibility(View.VISIBLE);
                    iv_details_picture.setVisibility(View.GONE);
                    XzbUtils.displayImage(iv_details_picture, "", 0);
                }
                break;

            case Islocation:
                String name = data.getStringExtra("name");
                address = name;
                if (name.equals("不显示位置")) {
                    tv_details_location.setText("定位位置");
                } else {
                    tv_details_location.setText(name);
                }
                break;
            case 1002:
                loadData(false);
                break;
            case RequsterTag.CALENDERCODE://时间选择接收
                if (data != null) {
                    String dateString = data.getStringExtra("dateString");
                    Date date = (Date) data.getExtras().getSerializable("date");
                    if (date != null) {
                        this.date = date;
                        onTimeSelected(true, date, tv_details_time, tv_details_time_selected);

                    }
                }
            case DataConstant.REQUEST_CODE_CATEGORY_MANAGER:
                int operationType = data.getIntExtra(DataConstant.BUNDLE_KEY_BOOK_OPERATION_TYPE, 0);
                if (operationType == 0) {
                    getNativeData();
                    if (isAdd && name_accounting.getText().equals("类别"))
                        delayedShowAccountIcon();
                } else {
                    clearChoiceCategoryData();
                    getBillCategoryList();
                }
                break;
            case DataConstant.REQUEST_CODE_CATEGORY_EDIT:
                clearChoiceCategoryData();
                getBillCategoryList();
                break;
            default:
                break;
        }
    }


    public String getRealFilePath(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param isAgain 软件盘点击传true,按钮点击传false
     */
    private void onSubmit(boolean isAgain) {
        String mMoneys = tv_details_money.getText().toString();
        mMoneys = onSumMoney(mMoneys, tv_details_money);

        String category = name_accounting.getText().toString();
        if (category.equals("类别")) {
            showToast("请选择类别");
            return;
        }

        if (Double.valueOf(mMoneys) == 0) {
            showDialog();
            return;
        }
        boolean isOk = onMoneyIsOK(tv_details_money, swipeLayoutList1, swipeLayoutList2);
        if (!isOk) {
            return;
        }
        saveBillToNative(isAgain);
    }


    private void showDialog() {
        showSimpleAlertDialog("提示", "请输入有效的支出金额", "确定", true, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }


    /**
     * 获取转账资金账户接口
     */
    private void loadData(final boolean flag) {

        CommonFacade.getInstance().exec(Constants.TRANSLATE_ASSERT_ACOUNT, new ViewCallBack<PropertyTransListInfo>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(PropertyTransListInfo o) throws Exception {
                super.onSuccess(o);
                cunterList = o.getData();
                if (cunterList == null) {
                    cunterList = new ArrayList<PropertyTransEntity>();
                }

                for (int i = 0; i < cunterList.size(); i++) {
                    PropertyTransEntity propertyTransEntity = cunterList.get(i);
                    String mAssetid = propertyTransEntity.getAssetid();
                    if (mAssetid.equals(accountnumber)) {
                        OnEditsetAccountnumber(cunterList.get(i));
                    }
                }
                if (!flag) {
                    PropertyTransEntity propertyTransEntity = cunterList.get(cunterList.size() - 1);
                    String mAssetid = propertyTransEntity.getAssetid();
                    accountnumber = mAssetid;
                    OnEditsetAccountnumber(propertyTransEntity);
                }

                PropertyTransEntity entity = new PropertyTransEntity();
                entity.setAssetid("-1");
                entity.setItemname("添加账户");
                entity.setMarktext("");
                cunterList.add(entity);
//                JSONObject jo_main = (JSONObject) o;
//                List<PropertyTransEntity> beforecunterList = null;
//                if (!flag) {
//                    beforecunterList = new ArrayList<>();
//                    beforecunterList.addAll(cunterList);
//                }
//                cunterList = PropertyTransEntity.parceList(jo_main.optString("data"));
//                if (!StringUtils.isEmptyList(cunterList) && !isAdd && flag) {
//                    for (int i = 0; i < cunterList.size(); i++) {
//                        if (cunterList.get(i).getAssetid().equals(accountnumber)) {
//                            OnEditsetAccountnumber(cunterList.get(i));
//                        }
//                    }
//                } else if (!StringUtils.isEmptyList(cunterList) && !flag) {
//                    List<PropertyTransEntity> dataList = new ArrayList<>();
//                    dataList.addAll(cunterList);
//                    if (beforecunterList.size() == 0) {
//                        PropertyTransEntity data = new PropertyTransEntity();
//                        data.setAssetid("-1");
//                        beforecunterList.add(data);
//                    } else {
//                        //不处理
//                    }
//                    List<PropertyTransEntity> diffrentData = getDiffrent(dataList, beforecunterList);
//                    if (diffrentData.size() == 1) {
//                        OnEditsetAccountnumber(diffrentData.get(0));
//                        accountnumber = diffrentData.get(0).getAssetid();
//                    } else {
//                        //不做处理
//                    }
//                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg);
            }
        });
    }


    private List<PropertyTransEntity> getDiffrent(List<PropertyTransEntity> list1, List<PropertyTransEntity> list2) {
        for (int i = 0; i < list2.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (list2.get(i).getAssetid().equals(list1.get(j).getAssetid())) {
                    list1.remove(j);
                }
            }
        }
        return list1;
    }


    // 添加账户
    private void shoCapital(String number, final List<PropertyTransEntity> cunterList) {
        final FullScreenDialogUtil fullScreenDialogUtil = new FullScreenDialogUtil();

        fullScreenDialogUtil.showDialog(this, number, cunterList, new FullScreenDialogUtil.Oklistener() {
            @Override
            public void onOklistener(PropertyTransEntity propertyTransEntity) {
                if (propertyTransEntity.getAssetid().equals("-1")) {
                    //添加账户
                    Intent intent = new Intent(ExpenditureActivity.this, WealthAddActivity.class);
                    startActivityForResult(intent, 1002);
                } else {
                    //选则账户
                    accountnumber = propertyTransEntity.getAssetid();
                    OnEditsetAccountnumber(propertyTransEntity);
                }
                fullScreenDialogUtil.dismissConfirmDialog();
            }
        });
    }

    private long getLongData() {
        long mTradetime = DateUtil.toTimeStamp(date) / 1000;
        mTradetime = mTradetime - DateUtil.longTime(date) + DateUtil.longTime(new Date());
        this.returnDate = date;
        return mTradetime;
    }

    private void startActivityToHome(String isShow) {
        if (isStatistics) {
            EventBus.getDefault().post("updatebill");
            finish();
            return;
        }
        if (isNeeddruphome) {
            IntentUtils.startActivity(ExpenditureActivity.this, HomeActivity.class, new String[]{"mTradetime", DateUtil.toTimeStamp(returnDate) + "", "isShow", isShow});
        } else {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void clearChoiceCategoryData() {
        expenditure_bean = null;
        expenditure_son_bean = null;
        before_expenditure_bean = null;
        billcateid = "";
        billsubcateid = "";
        expenditureCategory = new Category();
        setIconAndName();
    }

    public void getBillCategoryList() {
        showLoadingDialog();
        DataRequstManager.getAccountBookCategoryByAccountBookId(bookid, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                dismissDialog();
                JSONObject jsonOb = (JSONObject) o;
                LoginConfig.getInstance().setJsonbook(bookid, jsonOb.toString());
                getNativeData();
                if (isAdd)
                    delayedShowAccountIcon();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                retryDialogHandler.showRetryDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private RetryDialogHandler retryDialogHandler;


    private void initEvent() {
        retryDialogHandler.setExitlickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        retryDialogHandler.setRetryclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getBillCategoryList();
            }
        });
    }


}
