package com.yiqiji.money.modules.homeModule.write.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.TextView;

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
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.entity.BookExpenditure;
import com.yiqiji.money.modules.common.entity.BookExpenditure.ChildBean;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.CountMoney;
import com.yiqiji.money.modules.common.utils.DataBaseUtil;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.FullScreenDialogUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.PermissionUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.utils.popupWindowUtils;
import com.yiqiji.money.modules.common.utils.popupWindowUtils.onSonIconClickListener;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.common.view.ViewPageItem;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.activity.CalendarActivity;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.write.WriteUtil;
import com.yiqiji.money.modules.homeModule.write.adapter.DetailsAdapter;
import com.yiqiji.money.modules.homeModule.write.adapter.RecyclePageAdapter;
import com.yiqiji.money.modules.homeModule.write.entity.PropertyTransListInfo;
import com.yiqiji.money.modules.myModule.login.activity.PictureListActivity;
import com.yiqiji.money.modules.property.activity.WealthAddActivity;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 单人记账宿主Activity
 *
 * @author Administrator
 */
public class DetailsActivity extends BaseActivity implements OnClickListener {

    private View title;
    private RelativeLayout rl_details_title;
    private View rl_view_title;
    private RadioGroup radio_group_details;
    private TextView tv_details_money;
    private View ll_details_money;
    private ViewPager vp_details_expenditure;
    private ViewPager vp_details_income;

    private RecyclePageAdapter mExpenditurePageAdapter;
    private RecyclePageAdapter incomePageAdapter;

    private LinearLayout ll_brand;//品牌
    private EditText etBrand;

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
    private SelectableRoundedImageView iv_details_picture;//照片


    private TextView name_accounting;  //记账名称
    private ImageView icon_accounting; //记账Icon
    private View i_accounting_icon;  //记账View


    private List<DailycostEntity> mDataList;
    private DetailsAdapter adapter;

    private RecyclerView mRecyclerView;

    private DailycostEntity mDailycostEntity;
    //创建的时候时间是今天
    private Date date;
    //返回给Home时间
    private Date returnDate;


    private String bookName = "日常账本";
    private boolean isAdd = true;
    private String isNumberBook = "0";// 是否是多人账本0：单人账本
    private String cateId;//账本分类id
    private String bookid;
    private String memberid;
    private String username;
    private ApiService apiService;

    private String billcateid;
    private String billsubcateid;
    private int mType = 1;  //mType:1支出  mType：0 收入
    private boolean isSynchronization = false;
    private TextView tv_details_money_icon;
    // 再记一笔
    private Button bt_expend_agin;
    // 确定
    private Button bt_expend_confirm;
    private ImageView iv_details_return;


    //从相册获取图片返回的地址
    private Uri uri = null;
    private List<PropertyTransEntity> cunterList = null;//账户列表
    private String accountnumber = "0";
    private MyPopuwindows myPopuwindows;

    InputMethodManager imm;
    //一页ViewPage显示的item个数
    private final int ICON_COUNT = 8;


    //存储当前选择的分类（支出）用于对分类选择弹框显示做判断
    Category expenditureCategory;
    //存储当前选择的分类（收入）用于对分类选择弹出显示做判断
    Category incomeCategory;

    /**
     * ******分类选择的实际保存对象（支出）
     *****************/
    private ChildBean expenditure_bean;  //支出1级对象
    private ChildBean expenditure_son_bean = null;// 支出2级集合选中对象
    private ChildBean before_expenditure_bean;// 支出用于保存之前的集合，点击一个将前面一个设为未选中

    /**
     * ******分类选择的实际保存对象（收入）
     *****************/
    private ChildBean income_bean; //收入1级对象
    private ChildBean income_son_bean = null;//收入2级集合选中对象
    private ChildBean before_income_bean;// 收入用于保存之前的集合，点击一个将前面一个设为未选中


    public boolean isNeeddruphome = false;//是否需要跳转到首页

    private List<BooksDbMemberInfo> booksDbMemberInfos;//多人账本成员

    private boolean isStatistics;//是否是统计过来的账单修改

    private class Category {
        private String category;
        private String icon;
        private String topCategoryId;
        private String childCategoryId;

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
            if (topCategoryId == null) {
                return "";
            }
            return topCategoryId;
        }

        public void setTopCategoryId(String topCategoryId) {
            this.topCategoryId = topCategoryId;
        }
    }

    private int backgroundResource;//选中圆圈的颜色
    private int with_more_backgroundResource;//拥有二级子目录的更多图标


    //当是修改的时候，第一次再记一笔是修改，后面再记一笔是添加
    private boolean isFistQuit = true;
    private float item_viewGroup;

    private RetryDialogHandler retryDialogHandler;

    private boolean isSpeech = false;//是否是语音记账
    private String billmark;
    private String categoryid;
    private String categorytitle;
    private String billamount;

    /**
     * 根据账本类型分别跳转 在记一笔中区别就是有没有成员 1、单人记账 2、多人记账
     *
     * @param
     */
    public static void startActivity(Context mContext, BooksDbInfo booksDbInfo, String user_name, String memberid, Date date_time) {
        Intent intent = null;
        if (booksDbInfo == null) {
            return;
        }
        String isClear = booksDbInfo.getIsclear();
        String mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        String mAccountbookid = booksDbInfo.getAccountbookid();
        String mAccountbookcount = booksDbInfo.getAccountbookcount();
        String mAccountbookcate = booksDbInfo.getAccountbookcate();
        String mAccountbookcatename = booksDbInfo.getAccountbookcatename();
        String mMyuid = booksDbInfo.getMyuid();
        if (isClear.equals("0")) {
            // 单人账本
            intent = new Intent(mContext, DetailsActivity.class);
        } else {
            // 多人账本
            intent = new Intent(mContext, ExpenditureActivity.class);

        }
        if (intent == null || booksDbInfo == null) {
            return;
        }
        // 判断是添加还是修改
        intent.putExtra("intentType", true);
        // 账本编辑后的名字
        intent.putExtra("bookName", mAccountbooktitle);
        // 单人账本还是多人账本
        intent.putExtra("isNumberBook", mAccountbookcount);
        intent.putExtra("username", user_name);
        // 账本类型名字
        intent.putExtra("bookNameType", mAccountbookcatename);
        // 账本当前操作人
        intent.putExtra("myuid", mMyuid);
        // 单人记账的时候，使用这个memberid作为账单成员
        intent.putExtra("memberid", memberid);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date_time", (Serializable) date_time);
        intent.putExtras(bundle);
        // 账本ID
        intent.putExtra("bookid", mAccountbookid);
        //账本分类ID
        intent.putExtra("cateId", mAccountbookcate);

        mContext.startActivity(intent);
    }

    /**
     * 语音记账跳转入口
     *
     * @param mContext
     * @param booksDbInfo
     * @param user_name
     * @param memberid
     * @param date_time
     * @param billmark
     * @param categoryid
     * @param categorytitle
     * @param billamount
     */
    public static void startActivity(Context mContext, BooksDbInfo booksDbInfo, String user_name, String memberid, Date date_time, String billmark, String categoryid, String categorytitle, String billamount, boolean isSpeech) {
        Intent intent = null;
        if (booksDbInfo == null) {
            return;
        }
        String isClear = booksDbInfo.getIsclear();
        String mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        String mAccountbookid = booksDbInfo.getAccountbookid();
        String mAccountbookcount = booksDbInfo.getAccountbookcount();
        String mAccountbookcate = booksDbInfo.getAccountbookcate();
        String mAccountbookcatename = booksDbInfo.getAccountbookcatename();
        String mMyuid = booksDbInfo.getMyuid();
        if (isClear.equals("0")) {
            // 单人账本
            intent = new Intent(mContext, DetailsActivity.class);
        } else {
            // 多人账本
            intent = new Intent(mContext, ExpenditureActivity.class);

        }
        if (intent == null || booksDbInfo == null) {
            return;
        }
        // 判断是添加还是修改
        intent.putExtra("intentType", true);
        // 账本编辑后的名字
        intent.putExtra("bookName", mAccountbooktitle);
        // 单人账本还是多人账本
        intent.putExtra("isNumberBook", mAccountbookcount);
        intent.putExtra("username", user_name);
        // 账本类型名字
        intent.putExtra("bookNameType", mAccountbookcatename);
        // 账本当前操作人
        intent.putExtra("myuid", mMyuid);
        // 单人记账的时候，使用这个memberid作为账单成员
        intent.putExtra("memberid", memberid);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date_time", (Serializable) date_time);
        intent.putExtras(bundle);
        // 账本ID
        intent.putExtra("bookid", mAccountbookid);
        //账本分类ID
        intent.putExtra("cateId", mAccountbookcate);

        /**
         * 语音识别结果
         */
        intent.putExtra("billmark", billmark);
        intent.putExtra("categoryid", categoryid);
        intent.putExtra("categorytitle", categorytitle);
        intent.putExtra("billamount", billamount);
        intent.putExtra("isSpeech", isSpeech);

        mContext.startActivity(intent);
    }

    /**
     * 账单修改跳转
     *
     * @param
     */
    public static void startActivity(Context mContext, BooksDbInfo booksDbInfo, DailycostEntity dailycostEntity, String memberid, Date date_time, boolean isStatistics) {
        Intent intent = null;
        if (booksDbInfo == null || dailycostEntity == null) {
            return;
        }
        String isClear = booksDbInfo.getIsclear();
        String mAccountbooktitle = booksDbInfo.getAccountbooktitle();
        String mAccountbookid = booksDbInfo.getAccountbookid();
        String mAccountbookcount = booksDbInfo.getAccountbookcount();
        String mAccountbookcate = booksDbInfo.getAccountbookcate();
        String mAccountbookcatename = booksDbInfo.getAccountbookcatename();
        String mMyuid = booksDbInfo.getMyuid();
        if (isClear.equals("0")) { // 非AA单人账本

            intent = new Intent(mContext, DetailsActivity.class);
        } else {// AA多人账本

            intent = new Intent(mContext, ExpenditureActivity.class);

        }
        if (intent == null || dailycostEntity == null) {
            return;
        }
        // 判断是添加还是修改
        intent.putExtra("intentType", false);
        // 账本编辑后的名字
        intent.putExtra("bookName", mAccountbooktitle);
        // 单人账本还是多人账本
        intent.putExtra("isNumberBook", mAccountbookcount);
        // 账本类型名字
        intent.putExtra("bookNameType", mAccountbookcatename);
        // 账本当前操作人
        intent.putExtra("myuid", mMyuid);
        intent.putExtra("memberid", memberid);
        // 账本ID
        intent.putExtra("bookid", mAccountbookid);
        //账本分类ID
        intent.putExtra("cateId", mAccountbookcate);
        intent.putExtra("isStatistics", isStatistics);
        Bundle bundle = new Bundle();
        bundle.putParcelable("DailycostEntity", dailycostEntity);
        String iconUrl = "";
        if (StringUtils.isEmpty(dailycostEntity.getBillsubcateid())) {
            iconUrl = dailycostEntity.getBillcateicon();
        } else {
            iconUrl = dailycostEntity.getBillsubcateicon();
        }
        intent.putExtra("iconUrl", iconUrl);
        bundle.putSerializable("date_time", (Serializable) new Date(Long.parseLong(dailycostEntity.getTradetime()) * 1000));
        intent.setExtrasClassLoader(DailycostEntity.class.getClassLoader());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getOnIntent();
        initView();
        initKeyboard();
        expenditureCategory = new Category();
        incomeCategory = new Category();
        if (isAdd) {
            backgroundResource = R.drawable.write_default_expenditure;
            tv_details_money_icon.setTextColor(getResources().getColor(R.color.expenditure));
            tv_details_money.setTextColor(getResources().getColor(R.color.expenditure));
        } else {
            initData0nEdit();
            if (mType == 1) {// 支出
                backgroundResource = R.drawable.write_default_expenditure;

                tv_details_money_icon.setTextColor(getResources().getColor(R.color.expenditure));
                tv_details_money.setTextColor(getResources().getColor(R.color.expenditure));
                ((RadioButton) radio_group_details.getChildAt(0)).setChecked(true);
                vp_details_expenditure.setVisibility(View.VISIBLE);
                ll_details_point_group.setVisibility(View.VISIBLE);
                vp_details_income.setVisibility(View.GONE);
                ll_incomel_point_group.setVisibility(View.GONE);
            } else {// 收入
                backgroundResource = R.drawable.write_default_income;
                tv_details_money_icon.setTextColor(getResources().getColor(R.color.income));
                tv_details_money.setTextColor(getResources().getColor(R.color.income));
                ((RadioButton) radio_group_details.getChildAt(1)).setChecked(true);
                vp_details_expenditure.setVisibility(View.GONE);
                ll_details_point_group.setVisibility(View.GONE);
                vp_details_income.setVisibility(View.VISIBLE);
                ll_incomel_point_group.setVisibility(View.VISIBLE);
            }
        }

        initAdapter();
        loadData(true);
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
        initEvent();
    }

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

        cateId = getIntent().getStringExtra("cateId");
        bookid = getIntent().getStringExtra("bookid");
        isAdd = getIntent().getBooleanExtra("intentType", true);
        bookName = getIntent().getStringExtra("bookName");
        isAdd = getIntent().getBooleanExtra("intentType", true);
        isNumberBook = getIntent().getStringExtra("isNumberBook");
        memberid = getIntent().getStringExtra("memberid");
        username = getIntent().getStringExtra("username");
        isStatistics = getIntent().getBooleanExtra("isStatistics", false);
        //进来获取一个传的data，如果没有记一笔则mTradetime，date不会重新赋值，如果选则了时间，则在对应选则时间的地方和保存数据的地方更改时间
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
        if (BooksDetailPerecenter.isAccountbookCount(isNumberBook)) {
            DataBaseUtil.initMemberData(bookid, handler, RequsterTag.SEARCHBOOKMENBER);
        }
    }

    private void initView() {
        float screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        item_viewGroup = (screeWith - UIHelper.Dp2PxFloat(this, 16)) / 4;

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_details_location = (TextView) findViewById(R.id.tv_details_location);
        iv_details_photograph = (ImageView) findViewById(R.id.iv_details_photograph);
        iv_details_photograph.setOnClickListener(this);
        iv_details_picture = (SelectableRoundedImageView) findViewById(R.id.iv_details_picture);
        iv_details_picture.setOnClickListener(this);

        ll_details_location = (LinearLayout) findViewById(R.id.ll_details_location);
        ll_details_location.setOnClickListener(this);
        iv_details_return = (ImageView) findViewById(R.id.iv_details_return);
        iv_details_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imm != null && imm.isActive()) {
                    imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
                }
                startActivityToHome("false");
            }
        });
        tv_details_money_icon = (TextView) findViewById(R.id.tv_details_money_icon);
        title = findViewById(R.id.details_title);
        rl_details_title = (RelativeLayout) findViewById(R.id.rl_details_title);
        rl_view_title = findViewById(R.id.rl_view_title);


        radio_group_details = (RadioGroup) findViewById(R.id.radio_group_details);

        ll_details_remarks = (LinearLayout) findViewById(R.id.ll_details_remarks);
        ll_brand = (LinearLayout) findViewById(R.id.ll_brand);
        if ("6".equals(cateId)) {//装修账本显示
            ll_brand.setVisibility(View.VISIBLE);
        } else {
            ll_brand.setVisibility(View.GONE);
        }
        rl_details_time = (RelativeLayout) findViewById(R.id.rl_details_time);
        ll_details_account = (LinearLayout) findViewById(R.id.ll_details_account);

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
                tv_details_money.setText(onSumMoney(tv_details_money.getText().toString()));
                hideKeyboard();
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

        etBrand = (EditText) findViewById(R.id.etBrand);
        // 设置光标
        etBrand.setCursorVisible(false);
        etBrand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etBrand.setCursorVisible(true);// 再次点击显示光标
                hideKeyboard();
                imm.showSoftInput(etBrand, InputMethodManager.SHOW_FORCED);
            }
        });

        etBrand.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        tv_details_time_selected = (TextView) findViewById(R.id.tv_details_time_selected);
        tv_details_time_selected.setOnClickListener(this);

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
        ll_details_money = findViewById(R.id.i_accounting_money);
        ll_details_money.setOnClickListener(this);

        vp_details_expenditure = (ViewPager) findViewById(R.id.vp_details_expenditure);
        vp_details_income = (ViewPager) findViewById(R.id.vp_details_income);

        ll_details_point_group = (LinearLayout) findViewById(R.id.ll_details_point_group);
        ll_incomel_point_group = (LinearLayout) findViewById(R.id.ll_incomel_point_group);

        bt_expend_agin = (Button) findViewById(R.id.bt_expend_agin);
        bt_expend_confirm = (Button) findViewById(R.id.bt_expend_confirm);
        bt_expend_agin.setOnClickListener(this);
        bt_expend_confirm.setOnClickListener(this);

        name_accounting = (TextView) findViewById(R.id.name_accounting);
        icon_accounting = (ImageView) findViewById(R.id.icon_accounting);
        icon_accounting.setOnClickListener(this);
        i_accounting_icon = findViewById(R.id.i_accounting_icon);

        retryDialogHandler = new RetryDialogHandler(this);
    }


    private void initTitle(boolean tag, BookExpenditure expenditure_data) {
        if (tag) {
            title.setVisibility(View.VISIBLE);
            rl_details_title.setVisibility(View.GONE);
            rl_view_title.setVisibility(View.GONE);
            if (expenditure_data != null) {
                initTitle(expenditure_data.getCategorytitle(), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.layout_title_view_return) {
                            if (imm != null && imm.isActive()) {
                                imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
                            }
                            startActivityToHome("false");
                        }
                    }
                });
            }


        } else {
            title.setVisibility(View.GONE);
            rl_details_title.setVisibility(View.VISIBLE);
            rl_view_title.setVisibility(View.VISIBLE);
            radio_group_details.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_details_expenditure:// 支出
                            backgroundResource = R.drawable.write_default_expenditure;
                            tv_details_money_icon.setTextColor(getResources().getColor(R.color.expenditure));
                            tv_details_money.setTextColor(getResources().getColor(R.color.expenditure));

                            vp_details_expenditure.setVisibility(View.VISIBLE);
                            // ll_details_point_group.setVisibility(View.VISIBLE);
                            vp_details_income.setVisibility(View.GONE);
                            ll_incomel_point_group.setVisibility(View.GONE);

                            if (ll_details_point_group_is_show) {
                                ll_details_point_group.setVisibility(View.VISIBLE);
                            } else {
                                ll_details_point_group.setVisibility(View.INVISIBLE);
                            }

                            //判断保存记账类别的对象
                            if (TextUtils.isEmpty(expenditureCategory.getCategory())) {
                                setIconAndName();
                                showAccountIcon(i_accounting_icon);
                            } else {
                                hideAccountIcon(i_accounting_icon);
                                setIconAndName(expenditureCategory);
                            }

                            mType = 1;
                            break;
                        case R.id.rb_details_income:// 收入
                            backgroundResource = R.drawable.write_default_income;
                            tv_details_money_icon.setTextColor(getResources().getColor(R.color.income));
                            tv_details_money.setTextColor(getResources().getColor(R.color.income));

                            vp_details_expenditure.setVisibility(View.GONE);
                            ll_details_point_group.setVisibility(View.GONE);
                            vp_details_income.setVisibility(View.VISIBLE);

                            if (ll_incomel_point_group_is_show) {
                                ll_incomel_point_group.setVisibility(View.VISIBLE);
                            } else {
                                ll_incomel_point_group.setVisibility(View.INVISIBLE);
                            }

                            if (TextUtils.isEmpty(incomeCategory.getCategory())) {
                                setIconAndName();
                                showAccountIcon(i_accounting_icon);
                            } else {
                                hideAccountIcon(i_accounting_icon);
                                setIconAndName(incomeCategory);
                            }

                            mType = 0;
                            break;
                    }
                }
            });
        }
    }


    //从相册里面取后返回
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        String path = getIntent().getStringExtra("path");
        if (!StringUtils.isEmpty(path)) {
            uri = Uri.parse(path);
        }

        iv_details_photograph.setVisibility(View.GONE);
        iv_details_picture.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(path) && !path.contains("file://")) {
            path = "file://" + path;
        }
        XzbUtils.displayImage(iv_details_picture, path, 0);

    }

    /**
     * 获取账本分类数据
     */
    private void getNativeData() {
        BookExpenditure expenditure_data = null;
        BookExpenditure accounts_data = null;
        JSONObject object;
        try {
            object = DataLocalPersistencer.getBookCategoryJSONObject(bookid);
            boolean tag = true;
            if (object.has("expendCategory")) {
                JSONObject b = object.getJSONObject("expendCategory");// 支出
                expenditure_data = DataParseAssembler.getInstance().getExpenditureForEnable(b);
                tag = true;
            }
            if (object.has("incomeCategory")) {
                JSONObject incom = object.getJSONObject("incomeCategory");// 收入
                accounts_data = DataParseAssembler.getInstance().getExpenditureForEnable(incom);
                tag = false;
            }
            initTitle(tag, expenditure_data);
            if (expenditure_data != null && expenditure_data.getChild() != null) {
                with_more_backgroundResource = R.drawable.with_more_expenditure;
                setViewPage(expenditure_data, true, vp_details_expenditure, ll_details_point_group);
            }
            if (accounts_data != null && accounts_data.getChild() != null) {
                with_more_backgroundResource = R.drawable.with_more_income;
                setViewPage(accounts_data, false, vp_details_income, ll_incomel_point_group);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.log_error(null, e);
        }
    }

    /**
     * 当前记账是编辑时，初始化传过来的编辑数据
     */
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
        //设置品牌
        if (!TextUtils.isEmpty(mDailycostEntity.getBillbrand())) {
            etBrand.setText(mDailycostEntity.getBillbrand());
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


    /**
     * 500毫秒后显示记账类别
     */
    private void delayedShowAccountIcon() {
        TaskQueue.mainQueue().executeDelayed(new Runnable() {
            @Override
            public void run() {
                showAccountIcon(i_accounting_icon);
            }
        }, 500);
    }


    //创建一个以时间结尾的png图片路径
    private void initPath() {
        uri = XzbUtils.getTpPath();
    }

    private void initAdapter() {
        mDataList = new ArrayList<>();
        if (!isAdd) {
            mDataList.add(mDailycostEntity);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_details);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new DetailsAdapter(this, mDataList);
        mRecyclerView.setAdapter(adapter);
    }

    private View keyboard_view;
    /**
     * 初始化软件盘
     */
    private ImageView iv_keyboard_plus;// 加
    private ImageView iv_keyboard_delete;
    private TextView tv_keyboard_complete;
    private ImageView quit_save;
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
        keyboard_view.findViewById(R.id.quit_save).setOnClickListener(this);
        keyboard_view.findViewById(R.id.ll_gone_keyboard_many_people).setOnClickListener(this);

        iv_keyboard_plus = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_plus);
        iv_keyboard_delete = (ImageView) keyboard_view.findViewById(R.id.iv_keyboard_delete);
        tv_keyboard_complete = (TextView) keyboard_view.findViewById(R.id.tv_keyboard_complete);
        quit_save = (ImageView) keyboard_view.findViewById(R.id.quit_save);
        tv_keyboard_equal = (TextView) keyboard_view.findViewById(R.id.tv_keyboard_equal);

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
    private static final int IsphotosShow = 0;
    private static final int Islocation = 1;
    //定位地址
    private String address = "不显示位置";

    private static final int ACCESS_FINE_LOCATION_S = 2;

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.iv_details_photograph: //拍照
                hidesystemkeyboard();

                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MyApplicaction.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//需要弹出dialog让用户手动赋予权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
                new ActionSheetDialog(DetailsActivity.this)
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
                                        Intent intent = new Intent(DetailsActivity.this, PictureListActivity.class);
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
                        Intent intent = new Intent(DetailsActivity.this, LocationActivity.class);
                        intent.putExtra("address", address);
                        startActivityForResult(intent, Islocation);
                    }

                    @Override
                    public void onRequestPermissionFail(int[] grantResults) {
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

                intent = new Intent(DetailsActivity.this, PhotosShowActivity.class);
                intent.putExtra("path", uri.toString());
                startActivityForResult(intent, IsphotosShow);
                break;
            case R.id.bt_expend_agin:
                String mMoney = tv_details_money.getText().toString();
                onSubmit(mMoney, false, true);
                break;
            case R.id.bt_expend_confirm:// 点击确定

                String mMoneys = tv_details_money.getText().toString();
                onSubmit(mMoneys, false, false);

                break;
            case R.id.ll_gone_keyboard_many_people:
                tv_details_money.setText(onSumMoney(tv_details_money.getText().toString()));
                hideKeyboard();
                break;
            case R.id.i_accounting_money:
                showKeyboard();
                break;
            case R.id.rl_details_time: // 选则时间
                hidesystemkeyboard();
                intent = new Intent(this, CalendarActivity.class);
                Bundle bundle = new Bundle();
                //时间选则器时间,date当前时间
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

    //用于清除Text
    private boolean before_textTag = true;

    private void onTextChange(String changData) {

        String mMoney = tv_details_money.getText().toString();
        // 最后一个字符
        String cutMoney = mMoney.substring(mMoney.length() - 1);
        if (changData.equals(KEY_EQUAL)) {

            // 如果里面有加号或者减号则是计算值,否则关闭
            if (mMoney.contains(KEY_REDUCE) || mMoney.contains(KEY_PLUS)) {
                String money = onSumMoney(mMoney);
                tv_details_money.setText(money);
            } else {
                // 避免删除一个数后就关闭
                DecimalFormat df = new DecimalFormat("0.00");
                String a = df.format(Float.valueOf(mMoney));
                tv_details_money.setText(a);
            }
            tv_keyboard_equal.setVisibility(View.GONE);
            tv_keyboard_complete.setVisibility(View.VISIBLE);
            quit_save.setVisibility(View.VISIBLE);
            return;
        } else if (changData.equals(KEY_REDUCE)) {// 减
            // 如果最后一个是“.”则删除最后一个
            String deleteSpotMoney = XzbUtils.deleteSpot(tv_details_money, KEY_SPOT);
            String deleteSpotCutMoney = deleteSpotMoney.substring(deleteSpotMoney.length() - 1);
            // 最后一个是"-"号不做操作
            if (deleteSpotCutMoney.equals(KEY_REDUCE)) {
                return;
            }
            // 如果最后一个是+号,则加号改-号
            if (deleteSpotCutMoney.equals(KEY_PLUS)) {
                tv_details_money.setText(deleteSpotMoney.substring(0, deleteSpotMoney.length() - 1) + KEY_REDUCE);
            } else {
                tv_details_money.setText(deleteSpotMoney + KEY_REDUCE);
            }
            // tv_keyboard_complete.setImageResource(R.drawable.equal);

        } else if (changData.equals(KEY_PLUS)) {// 加
            // 如果最后一个是点则删除最后一个
            String deleteSpotMoney = XzbUtils.deleteSpot(tv_details_money, KEY_SPOT);
            String deleteSpotCutMoney = deleteSpotMoney.substring(deleteSpotMoney.length() - 1);

            // 最后一个是"+"号不做操作
            if (deleteSpotCutMoney.equals(KEY_PLUS)) {
                return;
            }
            // 如果最后一个是-号,则加号改+号
            if (deleteSpotCutMoney.equals(KEY_REDUCE)) {

                tv_details_money.setText(deleteSpotMoney.substring(0, deleteSpotMoney.length() - 1) + KEY_PLUS);
            } else {
                tv_details_money.setText(deleteSpotMoney + KEY_PLUS);
            }
            tv_keyboard_equal.setVisibility(View.VISIBLE);
            tv_keyboard_complete.setVisibility(View.GONE);
            quit_save.setVisibility(View.GONE);
            // tv_keyboard_complete.setImageResource(R.drawable.equal);

        } else if (changData.equals(KEY_DELETE)) {
            if (mMoney.length() == 1) {
                tv_details_money.setText(0 + "");

            } else if (XzbUtils.equalsIsZero(tv_details_money)) {
                XzbUtils.cleanTextToZero(tv_details_money);
            } else {
                tv_details_money.setText(mMoney.substring(0, mMoney.length() - 1));
            }

        } else if (changData.equals(KEY_COMPLETE)) {// 确定和等于
            onSubmit(mMoney, true, false);
        } else if (changData.equals(KEY_QUIT_SAVE)) {// 闪存一笔
            onSubmit(mMoney, true, true);

        } else if (changData.equals(KEY_SPOT)) {// 输入点
            String cleanData;
            // 用于刚开始是0.00的时候，然后走最下面的追加 “.”
            cleanData = XzbUtils.cleanTextToZero(tv_details_money);
            // 不让输入点的条件
            // 1、 最后是点 不让输入
            if (cutMoney.equals(KEY_SPOT)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }
            // 2、倒数第二位是点、后面判断加减 处理这样情况 633.22+603.2+
            if (cleanData.length() >= 3
                    && cleanData.substring(cleanData.length() - 2, cleanData.length() - 1).equals(KEY_SPOT)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }
            // 3、倒数第三位是点
            if (cleanData.length() >= 4
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && !cutMoney.equals(KEY_REDUCE) && !cutMoney.equals(KEY_PLUS)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }
            // 最后是加减号 输入点，则在前面加0
            if (cutMoney.equals(KEY_REDUCE) || cutMoney.equals(KEY_PLUS)) {
                cleanData = XzbUtils.addTextToZero(tv_details_money);
            }
            tv_details_money.setText(cleanData + changData);

        } else {
            String cleanData = XzbUtils.cleanTextView(tv_details_money);
            if (before_textTag) {
                cleanData = XzbUtils.cleanText(tv_details_money);
            }

            if (cleanData.length() >= 4
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && cleanData.substring(cleanData.length() - 3, cleanData.length() - 2).equals(KEY_SPOT)
                    && !cutMoney.equals(KEY_REDUCE) && !cutMoney.equals(KEY_PLUS)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }
            // 如果输入里面没有加减，并且大于百万级别，则不操作
            if (XzbUtils.isOverOneMillion(cleanData + changData)) {
                XzbUtils.shakeTextView(tv_details_money);
                return;
            }

            tv_details_money.setText(cleanData + changData);
            before_textTag = false;
        }
    }

    private void showDialog() {
        showSimpleAlertDialog("提示", "请输入有效的支出金额", "确定", true, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    // 点击确定计算
    private String onSumMoney(String money) {
        // 如果最后一个是加减则删除,避免输入一个符号就点确定
        // 最后一个字符
        String geData;
        String cutdata = money.substring(money.length() - 1);
        if (cutdata.equals(KEY_REDUCE) || cutdata.equals(KEY_PLUS)) {
            geData = tv_details_money.getText().toString() + "0.00";
        } else {
            geData = tv_details_money.getText().toString();
        }
        ArrayList result = CountMoney.getStringList(geData); // String转换为List
        result = CountMoney.getPostOrder(result); // 中缀变后缀
        double i = CountMoney.calculate(result);

        DecimalFormat df = new DecimalFormat("0.00");
        String datas = df.format(i) + "";
        // 如果值为负数则至为0;
        char a = datas.charAt(0);
        if (a == '-') {
            datas = "0.00";
        }
        return datas;
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        before_textTag = true;
        int visibility = keyboard_view.getVisibility();
        if (visibility == View.VISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
            anim.setDuration(300);
            keyboard_view.setVisibility(View.GONE);
            keyboard_view.startAnimation(anim);
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


    /**
     * 隐藏系统键盘
     */
    private void hidesystemkeyboard() {
        if (tv_details_remarks.hasFocus()) {
            tv_details_remarks.setCursorVisible(false);
            imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);
        }
        if (etBrand.hasFocus()) {
            etBrand.setCursorVisible(false);
            imm.hideSoftInputFromWindow(etBrand.getWindowToken(), 0);
        }
    }


    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        tv_details_remarks.setCursorVisible(false);
        imm.hideSoftInputFromWindow(tv_details_remarks.getWindowToken(), 0);

        int visibility = keyboard_view.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            anim.setDuration(300);
            keyboard_view.setVisibility(View.VISIBLE);
            keyboard_view.startAnimation(anim);

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

    private void setIconAndName(ChildBean bean, int index) {
        String mDataid = bean.getCategoryid();
        if (index > 0) {
            mDataid = bean.getCategoryid() + "child";
        }
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
        if (mType == 1) {
            if (!TextUtils.isEmpty(billsubcatename)) {
                setImage(icon_accounting, billsubcateid + "child");
                name_accounting.setText(billsubcatename);

                expenditureCategory.setCategory(billsubcatename);
                expenditureCategory.setIcon(billsubcateid);
                expenditureCategory.setTopCategoryId(billcateid);
            } else {
                setImage(icon_accounting, billcateid);
                name_accounting.setText(billcatename);
                expenditureCategory.setCategory(billcatename);
                expenditureCategory.setIcon(billcateid);
            }
        } else {
            if (!TextUtils.isEmpty(billsubcatename)) {
                setImage(icon_accounting, billsubcateid + "child");
                name_accounting.setText(billsubcatename);
                incomeCategory.setCategory(billsubcatename);
                incomeCategory.setIcon(billsubcateid);
                incomeCategory.setTopCategoryId(billcateid);
            } else {
                setImage(icon_accounting, billcateid);
                name_accounting.setText(billcatename);
                incomeCategory.setCategory(billcatename);
                incomeCategory.setIcon(billcateid);
            }
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


    /**
     * 带过来的数据点击：再记一笔，就是修改当前数据(调修改方法)，添加到列表，软件盘不关闭，再点击记一笔(调添加方法)
     * 点击对勾：有金钱不符合要求则计算金钱，符合就修改数据(调修改方法)，直接结束当前界面返回
     */
    private void addDailycostInfo(String mPkid, String mBillid, final boolean agin) {
        ChildBean childbean;
        ChildBean chilsSon_bean;
        if (mType == 1) {
            childbean = this.expenditure_bean;
            chilsSon_bean = this.expenditure_son_bean;
        } else {
            childbean = this.income_bean;
            chilsSon_bean = this.income_son_bean;
        }

        final DailycostEntity dailycostEntity = new DailycostEntity();

        String textName = tv_details_remarks.getText().toString();
        if (TextUtils.isEmpty(textName)) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }
        String billBrand = etBrand.getText().toString();
        if (StringUtils.isEmpty(billBrand)) {
            dailycostEntity.setBillbrand("");
        } else {
            dailycostEntity.setBillbrand(billBrand);
        }
        dailycostEntity.setBillstatus("1");
        // 消费类型
        dailycostEntity.setBilltype(mType + "");
        dailycostEntity.setIssynchronization(isSynchronization + "");// 是否已同步
        dailycostEntity.setWhichbook(bookName);
        dailycostEntity.setAccountbookid(bookid);
        dailycostEntity.setUsername(username);
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber(accountnumber);

        long mTradetime = getLongData();
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        // 消费金额
        String money = tv_details_money.getText().toString();
        money = onSumMoney(money);
        dailycostEntity.setBillamount(money);
        if (BooksDetailPerecenter.isAccountbookCount(isNumberBook)) {
            dailycostEntity.setAccountbooktype("1");// 多人账本
        } else {
            dailycostEntity.setAccountbooktype("0");// 单人账本
        }

        dailycostEntity.setPkid(mPkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid(mBillid);
        dailycostEntity.setBillclear(0 + "");
        dailycostEntity.setIsclear(0 + "");// 是否需要结算
        dailycostEntity.setYear(Integer.parseInt(DateUtil.transferLongToDate(2, mTradetime)));
        dailycostEntity.setMoth(Integer.parseInt(DateUtil.transferLongToDate(3, mTradetime)));
        dailycostEntity.setDay(Integer.parseInt(DateUtil.transferLongToDate(4, mTradetime)));
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)

        long curTimestamp = System.currentTimeMillis() / 1000;
        dailycostEntity.setBillctime(curTimestamp + "");// 记录所在时间的那一天 毫秒
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间
        dailycostEntity.setBillctime(curTimestamp + "");

        dailycostEntity.setBillcateid(childbean.getCategoryid());// 账单一级分类id
        dailycostEntity.setBillcatename(childbean.getCategorytitle()); // 账单一级分类名称
        String mCategoryid = childbean.getCategoryid();
        String url_native_mCategoryid = XzbUtils.getPathString() + "/" + mCategoryid + "_s";
        dailycostEntity.setBillcateicon(url_native_mCategoryid); // 账单一级分类Icon
        dailycostEntity.setDeviceid(LoginConfig.getInstance().getDeviceid());
        if (chilsSon_bean != null) {
            dailycostEntity.setBillsubcateid(chilsSon_bean.getCategoryid());// 账单二级分类id
            String billsubcateicon = chilsSon_bean.getCategoryid();
            String url_native = XzbUtils.getPathString() + "/" + billsubcateicon + "child_s";
            dailycostEntity.setBillsubcatename(chilsSon_bean.getCategorytitle());
            dailycostEntity.setBillsubcateicon(url_native);
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
        mDataList.add(0, dailycostEntity);
        adapter.notifyDataSetChanged();

        int what;
        if (!agin) {
            what = 10;
        } else {
            what = 11;
        }
        if (BooksDetailPerecenter.isAccountbookCount(isNumberBook)) {//AA多人账本
            List<BillMemberInfo> billMemberInfos = getBillMemberInfos(money, mBillid);
            dailycostEntity.setMemberlist(billMemberInfos);
        }

        DownUrlUtil.addDailycostInfo(dailycostEntity, handler, what);
    }

    public List<BillMemberInfo> getBillMemberInfos(String money, String mBillid) {
        List<BillMemberInfo> memberlist = new ArrayList<>();
        if (booksDbMemberInfos != null) {
            for (BooksDbMemberInfo booksDbMemberInfo : booksDbMemberInfos) {
                BillMemberInfo billMemberInfo = new BillMemberInfo();
                billMemberInfo.setAmount(booksDbMemberInfo.getAmount() + "");
                if (isBookFount(booksDbMemberInfo.getUserid(), booksDbMemberInfo.getDeviceid())) {
                    billMemberInfo.setAmount(money);
                }
                billMemberInfo.setMemberid(booksDbMemberInfo.getMemberid());
                billMemberInfo.setStatus(booksDbMemberInfo.getStatus() + "");
                billMemberInfo.setType(1 + "");
                billMemberInfo.setBillid(mBillid);
                billMemberInfo.setDeviceid(booksDbMemberInfo.getDeviceid());
                billMemberInfo.setUsericon(booksDbMemberInfo.getUsericon());
                billMemberInfo.setUserid(booksDbMemberInfo.getUserid());
                billMemberInfo.setUsername(booksDbMemberInfo.getUsername());
                billMemberInfo.setIndexid(booksDbMemberInfo.getIndex());
                billMemberInfo.setIssynchronization(isSynchronization + "");
                memberlist.add(billMemberInfo);
                if (isBookFount(booksDbMemberInfo.getUserid(), booksDbMemberInfo.getDeviceid())) {
                    BillMemberInfo billMemberInfo1 = new BillMemberInfo();
                    billMemberInfo1.setAmount(money);
                    billMemberInfo1.setMemberid(booksDbMemberInfo.getMemberid());
                    billMemberInfo1.setStatus(booksDbMemberInfo.getStatus() + "");
                    billMemberInfo1.setType(0 + "");
                    billMemberInfo1.setBillid(mBillid);
                    billMemberInfo1.setDeviceid(booksDbMemberInfo.getDeviceid());
                    billMemberInfo1.setUsericon(booksDbMemberInfo.getUsericon());
                    billMemberInfo1.setUserid(booksDbMemberInfo.getUserid());
                    billMemberInfo1.setUsername(booksDbMemberInfo.getUsername());
                    billMemberInfo1.setIndexid(booksDbMemberInfo.getIndex());
                    billMemberInfo1.setIssynchronization(isSynchronization + "");
                    memberlist.add(billMemberInfo1);
                }
            }
            for (int i = 0; i < memberlist.size(); i++) {
                BillMemberInfo billMemberInfo = memberlist.get(i);
                String amount = billMemberInfo.getAmount();
                if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
                    memberlist.remove(i);
                }
            }

        }
        return memberlist;
    }

    private void updateDailycostInfo(final DailycostEntity dailycostEntity, String mPkid, final String mBillid,
                                     final boolean agin) {
        ChildBean childbean;
        ChildBean chilsSon_bean;
        if (mType == 1) {
            childbean = this.expenditure_bean;
            chilsSon_bean = this.expenditure_son_bean;
        } else {
            childbean = this.income_bean;
            chilsSon_bean = this.income_son_bean;
        }

        long curTimestamp = System.currentTimeMillis() / 1000;
        String money = tv_details_money.getText().toString();
        String textName = tv_details_remarks.getText().toString();
        if (textName.equals("备注说明")) {
            dailycostEntity.setBillmark("");
        } else {
            dailycostEntity.setBillmark(textName);
        }

        String billBrand = etBrand.getText().toString();
        if (StringUtils.isEmpty(billBrand)) {
            dailycostEntity.setBillbrand("");
        } else {
            dailycostEntity.setBillbrand(billBrand);
        }

        // 消费类型
        dailycostEntity.setBilltype(mType + "");
        if (BooksDetailPerecenter.isAccountbookCount(isNumberBook)) {//AA多人
            dailycostEntity.setAccountbooktype("1");
        } else {
            dailycostEntity.setAccountbooktype("0");
        }

        dailycostEntity.setAccountbookid(bookid);
        // 资产账户ID,默认为0
        dailycostEntity.setAccountnumber(accountnumber);

        money = onSumMoney(money);
        dailycostEntity.setBillamount(money);
        dailycostEntity.setPkid(mPkid);// 账单主键唯一ID,精确到秒
        dailycostEntity.setBillid(mBillid + "");// 需要修
        dailycostEntity.setBillcateid(childbean.getCategoryid());// 账单一级分类id
        dailycostEntity.setUpdatetime(curTimestamp);// 更新时间
        long mTradetime = getLongData();
        dailycostEntity.setTradetime(mTradetime + "");// 创建时间(时间轴选则的时间)
        dailycostEntity.setBillcateid(childbean.getCategoryid());// 账单一级分类id
        dailycostEntity.setBillcatename(childbean.getCategorytitle()); // 账单一级分类名称
        String mCategoryid = childbean.getCategoryid();
        String url_native_mCategoryid = XzbUtils.getPathString() + "/" + mCategoryid + "_s";
        dailycostEntity.setBillcateicon(url_native_mCategoryid); // 账单一级分类Icon
        if (dailycostEntity.getIssynchronization().equals("true")) {
            dailycostEntity.setIssynchronization("edit");
        }
        if (chilsSon_bean != null) {
            dailycostEntity.setBillsubcateid(chilsSon_bean.getCategoryid());// 账单二级分类id
            String billsubcateicon = chilsSon_bean.getCategoryid();
            String url_native = XzbUtils.getPathString() + "/" + billsubcateicon + "child_s";
            dailycostEntity.setBillsubcatename(chilsSon_bean.getCategorytitle());
            dailycostEntity.setBillsubcateicon(url_native);
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
        List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
        if (billMemberInfos != null) {
            for (int i = 0; i < billMemberInfos.size(); i++) {
                BillMemberInfo billMemberInfo = billMemberInfos.get(i);
                if (isBookFount(billMemberInfo.getUserid(), billMemberInfo.getDeviceid())) {
                    billMemberInfo.setAmount(money);
                    billMemberInfos.set(i, billMemberInfo);
                }
            }
            for (int i = 0; i < billMemberInfos.size(); i++) {
                BillMemberInfo billMemberInfo = billMemberInfos.get(i);
                String amount = billMemberInfo.getAmount();
                if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
                    billMemberInfos.remove(i);
                }
            }
        }


        dailycostEntity.setMemberlist(billMemberInfos);
        mDataList.add(0, dailycostEntity);
        adapter.notifyDataSetChanged();
        int what;
        if (!agin) {
            what = 10;
        } else {
            what = 11;
        }

        String where = DailycostContract.DtInfoColumns.BILLID + "=? ";
        String whereStrings[] = new String[]{mBillid};
        DownUrlUtil.updateDataBaseDailycostInfo(dailycostEntity, where, whereStrings, handler, what);

    }

    private LinearLayout ll_details_point_group;
    private LinearLayout ll_incomel_point_group;
    private boolean ll_details_point_group_is_show = false;
    private boolean ll_incomel_point_group_is_show = false;

    /**
     * @param mData                  收入或支出的数据源
     * @param isExpenditure          true支出 false 收入
     * @param mViewPage              收入或支出的Viewpage
     * @param ll_details_point_group 显示收入支出小点的View
     */
    @SuppressWarnings("deprecation")
    private void setViewPage(BookExpenditure mData, boolean isExpenditure, ViewPager mViewPage,
                             final LinearLayout ll_details_point_group) {
        final List<ViewGroup> mViewGroupList = new ArrayList<>();
        mViewGroupList.clear();
        // 这里拿到服务器数据，将服务器数据添加到View中
        View childitemBookView;
        int count = mData.getChild().size();
        ViewPageItem item_expenditure_viewpage_one = new ViewPageItem(this);

        mViewGroupList.add(item_expenditure_viewpage_one);
        item_expenditure_viewpage_one.removeAllViews();
        LinearLayout.LayoutParams layoutParams;


        count = count + 1;//加一是为了加自定义按钮
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

            //在这里做选择过得类型文字的初始化
            if (isExpenditure && expenditureCategory.getTopCategoryId().equals(item.getCategoryid())) {
                tv.setText(expenditureCategory.getCategory());
            } else if (incomeCategory != null && incomeCategory.getTopCategoryId().equals(item.getCategoryid())) {
                tv.setText(incomeCategory.getCategory());
            } else {
                tv.setText(item.getCategorytitle());
            }
            childitemBookView.setTag(i);
            item_expenditure_viewpage_one.addView(childitemBookView);
        }


        if (isExpenditure) {
            mExpenditurePageAdapter = new RecyclePageAdapter(this, mViewGroupList);
            mViewPage.setAdapter(mExpenditurePageAdapter);
        } else {
            incomePageAdapter = new RecyclePageAdapter(this, mViewGroupList);
            mViewPage.setAdapter(incomePageAdapter);
        }

        //添加指示标点
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
            showView(ll_details_point_group, true, isExpenditure);
        } else {
            showView(ll_details_point_group, false, isExpenditure);
        }


        //如果是支出
        if (isExpenditure) {
            if (isAdd) {
                // 如果是添加设置数据源第一个为选中
                settingImage(isExpenditure, billcateid, billsubcateid, mData, mViewGroupList);
            } else {
                // 如果是修改，并且是记录是支出，对应的下标是选中  （0表示收入，1表示支出,2转账，3结算，4交款）
                if (mDailycostEntity.getBilltype().equals("1")) {
                    settingImage(isExpenditure, billcateid, billsubcateid, mData, mViewGroupList);
                } else {
                    settingImage(isExpenditure, billcateid, billsubcateid, mData, mViewGroupList);
                }
            }
            initViewPageListener_expenditure(mData, mViewGroupList);
        } else {
            if (isAdd) {
                // 支出集合中第一条数据
                settingImage(isExpenditure, billcateid, billsubcateid, mData,
                        mViewGroupList);
            } else {
                // 如果是修改，并且是记录是收入，对应的下标是选中 （0表示收入，1表示支出,2转账，3结算，4交款）
                if (mDailycostEntity.getBilltype().equals("0")) {
                    settingImage(isExpenditure, billcateid, billsubcateid, mData, mViewGroupList);
                } else {
                    settingImage(isExpenditure, billcateid, billsubcateid, mData,
                            mViewGroupList);
                }
            }
            initViewPageListener_income(mData, mViewGroupList);
        }

    }

    /**
     * 设置ViewPage 下面的小点的LinearLayout显示与隐藏
     *
     * @param view
     * @param isShow        true：大于0  false：小于0
     * @param isExpenditure
     */
    private void showView(LinearLayout view, boolean isShow, boolean isExpenditure) {
        if (isShow && isExpenditure && mType == 1) {
            view.setVisibility(View.VISIBLE);
        } else if (isShow && !isExpenditure && mType == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }

        if (isExpenditure) {
            ll_details_point_group_is_show = isShow;
        } else {
            ll_incomel_point_group_is_show = isShow;
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

    /**
     * 绑定支出的选择分类点击事件，在这里做 【自定义】按钮的事件的绑定
     *
     * @param mData
     * @param mViewGroupList
     */
    private void initViewPageListener_expenditure(final BookExpenditure mData, List<ViewGroup> mViewGroupList) {

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
                        hideAccountIcon(i_accounting_icon);
                        BookCategoryManagerActivity.open(mContext, bookid, bookName, mType);
                    }
                });
                continue;
            }

            item_expenditure_viewpage.getChildAt(i % ICON_COUNT).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 当前点击的对象
                    expenditure_bean = mData.getChild().get((int) v.getTag());
                    ChildBean childBean = mData.getChild().get(index);
                    String mDataid = childBean.getCategoryid();
                    billcateid = mDataid;
                    ImageView imageview = ((ImageView) ((ViewGroup) v).getChildAt(0));
                    // 这里得到的是上一个点击的chilsSon_bean，如果是add刚进来，则已经在前面初始化了ImageView
                    // 设置当前选中
                    if (expenditure_bean.getCategoryid() != before_expenditure_bean.getCategoryid()) {
                        if (before_expenditure_imageView != null) {
                            before_expenditure_imageView.setBackgroundResource(0);
                        } else {
                            //不处理
                        }
                        setImage(imageview, mDataid, childBean.getCategoryicon());
                        imageview.setBackgroundResource(backgroundResource);
                    } else {
                        imageview.setBackgroundResource(backgroundResource);
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
                        showSonPopu(true, expenditure_bean.getCategorytitle(), expenditure_category, mList, v);
                    } else {
                        //如果点击的item没有2级那就隐藏View
                        hideAccountIcon(i_accounting_icon);
                        setIconAndName(expenditure_bean, -1);
                        showKeyboard();
                    }
                }
            });
        }
    }

    /**
     * 绑定收入选择分类的点击事件，在这里做 【自定义】按钮的事件的绑定
     *
     * @param mData
     * @param mViewGroupList
     */
    private void initViewPageListener_income(final BookExpenditure mData, List<ViewGroup> mViewGroupList) {

        final int count = mData.getChild().size() + 1;//加一是为了多出一个【自定义】按钮

        ViewGroup item_expenditure_viewpage_1 = mViewGroupList.get(0);
        for (int i = 0; i < count; i++) {
            final int index = i;
            if (i % ICON_COUNT == 0 && i != 0) {
                item_expenditure_viewpage_1 = mViewGroupList.get(i / ICON_COUNT);
            }

            if (i == count - 1) {//如果是【自定义】按钮
                item_expenditure_viewpage_1.getChildAt(i % ICON_COUNT).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideAccountIcon(i_accounting_icon);
                        BookCategoryManagerActivity.open(mContext, bookid, bookName, mType);
                    }
                });
                continue;
            }

            item_expenditure_viewpage_1.getChildAt(i % ICON_COUNT).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 当前点击的对象
                    income_bean = mData.getChild().get((int) v.getTag());
                    String mDataid = mData.getChild().get(index).getCategoryid();
                    ImageView imageview = ((ImageView) ((ViewGroup) v).getChildAt(0));
                    billcateid = mDataid;
                    // 设置当前选中
                    if (income_bean.getCategoryid() != before_income_bean.getCategoryid()) {
                        if (before_income_imageView != null) {
                            before_income_imageView.setBackgroundResource(0);
                        }
                        setImage(imageview, mDataid, income_bean.getCategoryicon());
                        imageview.setBackgroundResource(backgroundResource);
                    } else {
                        imageview.setBackgroundResource(backgroundResource);
                    }

                    before_income_bean = income_bean;
                    before_income_imageView = imageview;
                    String income_category = ((TextView) ((ViewGroup) v).getChildAt(1)).getText().toString();
                    List<ChildBean> income_bean_list = income_bean.getChild();
                    incomeCategory.setCategory(income_category);
                    incomeCategory.setIcon(mDataid);
                    incomeCategory.setTopCategoryId(mDataid);

                    if (!StringUtils.isEmptyList(income_bean_list)) {
                        List<ChildBean> mList = new ArrayList<>();
                        ChildBean bean = new ChildBean();
                        bean.setCategoryicon(income_bean.getCategoryicon());
                        bean.setCategorytitle(income_bean.getCategorytitle());
                        bean.setCategoryid(income_bean.getCategoryid());
                        mList.add(income_bean);
                        mList.addAll(income_bean_list);
                        // 传二级子目录
                        showSonPopu(false, income_bean.getCategorytitle(), income_category, mList, v);
                    } else {
                        //如果点击的item没有2级那就隐藏View
                        hideAccountIcon(i_accounting_icon);
                        setIconAndName(income_bean, -1);
                        showKeyboard();
                    }
                }
            });
        }
    }


    private ImageView before_expenditure_imageView;
    private ImageView before_income_imageView;

    // 显示2级子目录
    private void showSonPopu(final boolean isExpenditure, String parentName, String titleName, final List<ChildBean> mChildSon_List, final View view) {

        popupWindowUtils po = new popupWindowUtils();
        po.showSonIconPopupWindow(isExpenditure, bookid, parentName, titleName, this, mChildSon_List);
        po.setIconClickListener(new onSonIconClickListener() {

            @Override
            public void setonSonIcon(View child) {
                int index = (int) child.getTag();

                TextView textView = (TextView) ((ViewGroup) view).getChildAt(1);
                ImageView imageview = (ImageView) ((ViewGroup) view).getChildAt(0);


                if (isExpenditure) {
                    before_expenditure_imageView = imageview;
                } else {
                    before_income_imageView = imageview;
                }
                String mDataid;
                if (isExpenditure) {
                    expenditure_son_bean = mChildSon_List.get(index);
                    textView.setText(expenditure_son_bean.getCategorytitle());
                    mDataid = expenditure_son_bean.getCategoryid();
                    setIconAndName(expenditure_son_bean, index);
                    expenditureCategory.setCategory(expenditure_son_bean.getCategorytitle());
                    expenditureCategory.setIcon(mDataid);
                } else {
                    income_son_bean = mChildSon_List.get(index);
                    textView.setText(income_son_bean.getCategorytitle());
                    mDataid = income_son_bean.getCategoryid();
                    setIconAndName(income_son_bean, index);
                    incomeCategory.setCategory(income_son_bean.getCategorytitle());
                    incomeCategory.setIcon(mDataid);
                }

                billsubcateid = mDataid;

                if (index > 0) {
                    billsubcateid = mDataid + "child";
                }
                setImage(imageview, billsubcateid, mChildSon_List.get(index).getCategoryicon());
                imageview.setBackgroundResource(backgroundResource);
                //二级子目录点击的是展开的第一个那么expenditure_son_bean就为空
                if (index == 0) {
                    if (isExpenditure) {
                        expenditure_son_bean = null;
                    } else {
                        income_son_bean = null;
                    }
                } else {
                    //不做处理
                }
                hideAccountIcon(i_accounting_icon);

                showKeyboard();
            }
        });
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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    isNeeddruphome = true;
                    // 不是再记一笔则跳转出去
                    startActivityToHome("true");
                    break;
                case 11:
                    isNeeddruphome = true;
                    billAddShow();//显示账单添加成功
                    showAccountIcon(i_accounting_icon);//
                    saveBillAgainChangImage(); //清除数据
                    break;
                case RequsterTag.SEARCHBOOKMENBER:
                    booksDbMemberInfos = (List<BooksDbMemberInfo>) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };

    private void saveBillAgainChangImage() {
        tv_details_money.setText("0.00");


        if (mType == 1) {
            expenditureCategory.setCategory("");
            before_expenditure_imageView.setBackgroundResource(0);
        } else {
            incomeCategory.setCategory("");
            before_income_imageView.setBackgroundResource(0);
        }
        //设置上面图片
        setIconAndName();

        //备注
        tv_details_remarks.setText("");
        tv_details_remarks.setCursorVisible(false);
        //品牌
        if ("6".equals(cateId)) {
            etBrand.setText("");
            etBrand.setCursorVisible(false);
        }

        //清空拍照
        uri = null;
        iv_details_photograph.setVisibility(View.VISIBLE);
        iv_details_picture.setVisibility(View.GONE);

//        //重新初始化为今天
//        date = new Date();
//        onTimeSelected(true, date, tv_details_time, tv_details_time_selected);
//        //账户
//        tv_details_account.setText("");
//        iv_details_account.setImageResource(R.drawable.write_account);
//        accountnumber="0";
//        //定位
//        tv_details_location.setText("");
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(DetailsActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    //权限管理，用于拍照，现在targetSdkVersion21,还不涉及到权限申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new ActionSheetDialog(DetailsActivity.this)
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
                                            Intent intent = new Intent(DetailsActivity.this, PictureListActivity.class);
                                            intent.putExtra("isShowCut", false);
                                            intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                            startActivity(intent);
                                        }
                                    }).show();
                } else {
                    //拍照用户不同意，自行处理即可
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
            // 从文件中创建uri
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case RequsterTag.RQ_TAKE_A_PHOTO:// 拍照
                iv_details_photograph.setVisibility(View.GONE);
                iv_details_picture.setVisibility(View.VISIBLE);

                if (uri != null) {
                    Bitmap bitmap;
                    String pathString;
                    try {
                        bitmap = BitmapUtil.getBitmapFormUri(this, uri, UIHelper.Dp2Px(this, 200), UIHelper.Dp2Px(this, 200));
                        pathString = StringUtils.getRealFilePath(this, XzbUtils.getPath());

                        BitmapUtil.saveBitmapToFile(bitmap, pathString);
                        if (!pathString.contains("file://")) {
                            pathString = "file://" + pathString;
                        }
                        uri = Uri.parse(pathString);
                        XzbUtils.displayImage(iv_details_picture, uri.toString(), 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.log_error(null, e);
                    }
                }

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
                    Date date = (Date) data.getExtras().getSerializable("date");
                    if (date != null) {
                        this.date = date;
                        onTimeSelected(true, date, tv_details_time, tv_details_time_selected);
                    }
                }
                break;
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
        if (null == uri) return "";
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
     * @param isKeyboard 软件盘点击传true,按钮点击传false
     * @param isAgain    再记一笔传true，保存传false
     */
    private void onSubmit(String onMoney, boolean isKeyboard, boolean isAgain) {
        String category = name_accounting.getText().toString();
        if (category.equals("类别")) {
            showToast("请选择类别");
            return;
        }
        // 点击确定和点击闪存入口是一样的判断

        if (onMoney.contains(KEY_REDUCE) || onMoney.contains(KEY_PLUS)) {
            String money = onSumMoney(onMoney);
            tv_details_money.setText(money);
            if (money.equals("0.00")) {
                if (isKeyboard) {
                    XzbUtils.shakeTextView(tv_details_money);
                } else {
                    showDialog();
                }
                return;
            }
        } else {
            // 避免删除一个数后就关闭
            DecimalFormat df = new DecimalFormat("0.00");
            String a = df.format(Double.parseDouble(onMoney));
            tv_details_money.setText(a);
            if (a.equals("0.00")) {
                if (isKeyboard) {
                    XzbUtils.shakeTextView(tv_details_money);
                } else {
                    showDialog();
                }
                return;
            }
        }
        saveBillToNative(isAgain);
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

//                JSONObject jo_main = (JSONObject) o;
//                List<PropertyTransEntity> beforecunterList = null;
//                if (!flag) {
//                    beforecunterList = new ArrayList<PropertyTransEntity>();
//                    beforecunterList.addAll(cunterList);
//                }
//                cunterList = PropertyTransEntity.parceList(jo_main.optString("data"));
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
                    Intent intent = new Intent(DetailsActivity.this, WealthAddActivity.class);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivityToHome("false");

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
            IntentUtils.startActivity(DetailsActivity.this, HomeActivity.class, new String[]{"mTradetime", DateUtil.toTimeStamp(returnDate) + "", "isShow", isShow});
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void getBillCategoryList() {
        showLoadingDialog();
        hideAccountIcon(i_accounting_icon);
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
                retryDialogHandler.showRetryDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /************************
     * 设置账单分类的图片列表*************************************************************
     *
     * @param billcateid     数据源第一个bean的ID *************************************************************
     * @param billsubcateid  bean下面的子beanId  *************************************************************
     * @param mData          数据源              *************************************************************
     * @param mViewGroupList 对应的集合          *************************************************************
     */
    // 将mViewGroupList
    private void settingImage(boolean isExpenditure, String billcateid, String billsubcateid, BookExpenditure mData,
                              List<ViewGroup> mViewGroupList) {
        if (isExpenditure) {
            expenditure_bean = mData.getChild().get(0);
            before_expenditure_bean = mData.getChild().get(0);
        } else {
            income_bean = mData.getChild().get(0);
            before_income_bean = mData.getChild().get(0);
        }
        int childCount = 0;
        for (int i = 0; i < mViewGroupList.size(); i++) {
            childCount += (mViewGroupList.get(i)).getChildCount();
        }

        ViewGroup item_expenditure_viewpage = (mViewGroupList.get(0));
        for (int j = 0; j < childCount; j++) {

            //获得子集合
            if (j % ICON_COUNT == 0 && j != 0) {
                item_expenditure_viewpage = mViewGroupList.get(j / ICON_COUNT);
            }

            //设置显示跟多目录
            View item_ViewGroup = item_expenditure_viewpage.getChildAt(j % ICON_COUNT);
            ImageView with_more = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(2);

            //如果是自定义按钮
            if (j == childCount - 1) {
                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);
                if (isExpenditure) {
                    imageview.setImageResource(R.drawable.ic_custom_expenditure_category_83_83);
                } else {
                    imageview.setImageResource(R.drawable.ic_custom_income_category_83_83);
                }
                with_more.setVisibility(View.GONE);
                continue;
            }

            List<ChildBean> child_list = mData.getChild().get(j).getChild();

            //设置图片是支出还是收入
            with_more.setImageResource(with_more_backgroundResource);
            if (StringUtils.isEmptyList(child_list)) {
                with_more.setVisibility(View.GONE);
            } else {
                with_more.setVisibility(View.VISIBLE);
            }

            String mDataid = mData.getChild().get(j).getCategoryid();

            //如果是修改,设置选中
            if (!TextUtils.isEmpty(billcateid) && mDataid.equals(billcateid)) {

                //设置viewPage下面第几屏小点选中
                int index_vp_details = j / ICON_COUNT;
                if (isExpenditure) {
                    vp_details_expenditure.setCurrentItem(index_vp_details);
                } else {
                    vp_details_income.setCurrentItem(index_vp_details);
                }


                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);

                //如果没有二级目录
                if (TextUtils.isEmpty(billsubcateid) || billsubcateid.equals("0")) {
                    // 首先取drawable里面的图片，然后没有再取sd卡
                    setImage(imageview, mDataid, mData.getChild().get(j).getCategoryicon());
                    imageview.setBackgroundResource(backgroundResource);
                    if (isExpenditure) {
                        before_expenditure_imageView = imageview;
                        before_expenditure_bean = mData.getChild().get(j);
                        expenditure_bean = mData.getChild().get(j);
                    } else {
                        before_income_imageView = imageview;
                        before_income_bean = mData.getChild().get(j);
                        income_bean = mData.getChild().get(j);
                    }


                } else {
                    //有二级目录

                    if (mData.getChild().get(j) == null || mData.getChild().get(j).getChild() == null) {
                        return;
                    }
                    int child_count = mData.getChild().get(j).getChild().size();
                    for (int i = 0; i < child_count; i++) {
                        // 获取二级子目录的Id
                        ChildBean childBean = mData.getChild().get(j).getChild().get(i);
                        String childId = childBean.getCategoryid();
                        if (childId.equals(billsubcateid)) {
                            // 获得icon
                            setImage(imageview, childId, childBean.getCategoryicon());
                            imageview.setBackgroundResource(backgroundResource);
                            if (isExpenditure) {
                                before_expenditure_imageView = imageview;
                                //TODO　这里做了修改
                                before_expenditure_bean = mData.getChild().get(j);
                                expenditure_bean = mData.getChild().get(j);
                            } else {
                                //TODO　这里做了修改
                                before_income_imageView = imageview;
                                before_income_bean = mData.getChild().get(j);
                                income_bean = mData.getChild().get(j);
                            }
                        }
                    }
                }
            } else {
                //如果是添加
                // 当是第一个ViewGroup的时候，index当时是0开始，从1开始取起，上面已经取过0
                ImageView imageview = (ImageView) ((ViewGroup) item_ViewGroup).getChildAt(0);
                // 先根据名字得到ID,再根据ID获取Map中的值
                // int id = product.getIdByName(name);
                setImage(imageview, mDataid, mData.getChild().get(j).getCategoryicon());
                imageview.setBackgroundResource(0);
            }
        }
    }

    private void clearChoiceCategoryData() {

        expenditure_bean = null;
        expenditure_son_bean = null;
        before_expenditure_bean = null;

        income_bean = null;
        income_son_bean = null;
        before_income_bean = null;

        billcateid = "";
        billsubcateid = "";

        expenditureCategory = new Category();
        incomeCategory = new Category();

        setIconAndName();

    }

}
