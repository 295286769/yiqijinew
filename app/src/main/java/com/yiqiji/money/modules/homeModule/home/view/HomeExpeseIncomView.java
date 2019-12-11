package com.yiqiji.money.modules.homeModule.home.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.BillActivity;
import com.yiqiji.money.modules.homeModule.home.activity.GroupMembersActivity;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.activity.NumberDtailActivity;
import com.yiqiji.money.modules.homeModule.home.activity.SetBudgetActivity;
import com.yiqiji.money.modules.homeModule.home.entity.HomePresenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/27.
 * address huang.weishui@71dai.com
 */
public class HomeExpeseIncomView extends LinearLayout implements HomePresenter.ShowbedugtIncomExpense {
    @BindView(R.id.bedug_text)
    TextView bedugText;//预算文案
    @BindView(R.id.yusuann)
    TextView yusuann;//预算金额
    @BindView(R.id.moth_residual_budget)
    LinearLayout mothResidualBudget;//设置预算
    @BindView(R.id.incom_text)
    TextView incomText;//收入文案
    @BindView(R.id.incom)
    TextView incom;//收入金额
    @BindView(R.id.moth_incom)
    LinearLayout mothIncom;
    @BindView(R.id.expese_text)
    TextView expeseText;//支出文案
    @BindView(R.id.expense)
    TextView expense;//支出金额
    @BindView(R.id.moth_expense)
    LinearLayout mothExpense;
    @BindView(R.id.linealayout_top)
    LinearLayout linealayoutTop;
    @BindView(R.id.layout_renovation)
    View layout_renovation;//装修和汽车
    @BindView(R.id.bedug_renovation_layout)
    LinearLayout bedug_renovation_layout;//装修和汽车设置预算
    @BindView(R.id.expese_layout)
    LinearLayout expese_layout;//装修和汽车累计支出
    @BindView(R.id.renovation_bedug_balance)
    TextView renovation_bedug_balance;//装修和汽车预算金额
    @BindView(R.id.bedug_renovation)
    TextView bedug_renovation;//装修和汽车预算文案
    @BindView(R.id.accumulated_expenditure_balance)
    TextView accumulated_expenditure_balance;//装修和汽车累计消费
    private HomePresenter homePresenter;
    private Context mContext;
    private BooksDbInfo booksDbInfo;
    private String memberid;//邀请人id
    private String user_name;
    private Date date_time;

    public HomeExpeseIncomView(Context context) {
        this(context, null);
    }

    public HomeExpeseIncomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeExpeseIncomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        homePresenter = new HomePresenter(this);
        initView();
        setOnClick();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_expese_icon_layout, this, true);
        ButterKnife.bind(this, view);
    }

    private void setOnClick() {
        mothResidualBudget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo.getIsclear().equals("0")) {//设置预算
                    boolean isExpectMoth = DateUtil.isExpectMoth(date_time);
                    if (!isExpectMoth) {
                        SetBudgetActivity.openActivity(mContext, booksDbInfo.getAccountbookid(), DateUtil.getYearMoth(date_time), booksDbInfo.getSorttype());
                    }

                } else {//我的消费
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_MINE_COST);
                    NumberDtailActivity.startActivity(mContext, booksDbInfo.getAccountbookid(),
                            memberid, booksDbInfo.getUserid(), booksDbInfo.getMyuid()
                            , booksDbInfo.getSorttype(), booksDbInfo.getIsclear(), user_name, "", date_time);
//
                }
            }
        });
        mothIncom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo.getIsclear().equals("0")) {//本月收入
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_CURMONTH_GET);
                    String accountbookcate = booksDbInfo.getAccountbookcate();
                    if (booksDbInfo.getAccountbookcatename().equals("装修账本") || accountbookcate.equals(RequsterTag.ACCOUNTBOOKCATE)) {
                        NumberDtailActivity.startActivity(mContext, booksDbInfo.getAccountbookid(),
                                memberid, booksDbInfo.getUserid(), booksDbInfo.getMyuid()
                                , booksDbInfo.getSorttype(), booksDbInfo.getIsclear(), user_name, "", date_time);
                    } else {

                        BillActivity.startActivity(mContext, 0, booksDbInfo.getAccountbooktitle(), booksDbInfo.getAccountbookid(), booksDbInfo.getSorttype(), booksDbInfo.getAccountbookcatename(),
                                booksDbInfo.getAccountbooktype(), booksDbInfo.getAccountbookcount());
                    }

                } else {//全员消费
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_TOTAL_COST);
                    NumberDtailActivity.startActivity(mContext, booksDbInfo.getAccountbookid(),
                            memberid, booksDbInfo.getUserid(), booksDbInfo.getMyuid()
                            , booksDbInfo.getSorttype(), booksDbInfo.getIsclear(), user_name, "member", date_time);
                }
            }
        });
        mothExpense.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo.getIsclear().equals("1")) {//我应收应付（跳转到群主成员）
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_GET_PAY);
                    GroupMembersActivity.startActivity(mContext, booksDbInfo.getAccountbookid());
                } else {//本月支出
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_CURMONTH_PAY);
                    String accountbookcate = booksDbInfo.getAccountbookcate();
                    if (booksDbInfo.getAccountbookcatename().equals("装修账本") || accountbookcate.equals(RequsterTag.ACCOUNTBOOKCATE)) {
                        NumberDtailActivity.startActivity(mContext, booksDbInfo.getAccountbookid(),
                                memberid, booksDbInfo.getUserid(), booksDbInfo.getMyuid(), user_name, "member");

                    } else {
                        BillActivity.startActivity(mContext, 1, booksDbInfo.getAccountbooktitle(), booksDbInfo.getAccountbookid(), booksDbInfo.getSorttype(), booksDbInfo.getAccountbookcatename(),
                                booksDbInfo.getAccountbooktype(), booksDbInfo.getAccountbookcount());
                    }

                }
            }
        });
        bedug_renovation_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo.getIsclear().equals("0")) {//设置预算
                    boolean isExpectMoth = DateUtil.isExpectMoth(date_time);
                    if (!isExpectMoth) {
                        SetBudgetActivity.openActivity(mContext, booksDbInfo.getAccountbookid(), DateUtil.getYearMoth(date_time), booksDbInfo.getSorttype());
                    }

                }
            }
        });
        expese_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo.getIsclear().equals("0")) {//装修和汽车累计支出
                    BillActivity.startActivity(mContext, 1, booksDbInfo.getAccountbooktitle(), booksDbInfo.getAccountbookid(), booksDbInfo.getSorttype(), booksDbInfo.getAccountbookcatename(),
                            booksDbInfo.getAccountbooktype(), booksDbInfo.getAccountbookcount());
                }
            }
        });
    }


    public void setBookInfo(BooksDbInfo bookInfo, List<BooksDbMemberInfo> booksDbMemberInfos, Date date_time) {
        this.booksDbInfo = bookInfo;
        this.date_time = date_time;
        memberid = ((HomeActivity) mContext).getMySelfNyMemeberId(booksDbInfo.getMyuid(), booksDbMemberInfos);
        user_name = ((HomeActivity) mContext).getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbMemberInfos);
        titleChange();
        homePresenter.initSingTitle(booksDbInfo, date_time);
    }

    private void titleChange() {
        if (booksDbInfo.getAccountbookcate().equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE) || booksDbInfo.getAccountbookcate().equals(RequsterTag.CARACCOUNTBOOKCATE)) {
            layout_renovation.setVisibility(View.VISIBLE);
            linealayoutTop.setVisibility(View.GONE);

            if (booksDbInfo.getAccountbookcate().equals(RequsterTag.CARACCOUNTBOOKCATE)) {
                bedug_renovation.setText(mContext.getResources().getString(R.string.bedug_setting_text));
            } else {
                bedug_renovation.setText(mContext.getResources().getString(R.string.bedug_renovation));
            }
        } else {
            layout_renovation.setVisibility(View.GONE);
            linealayoutTop.setVisibility(View.VISIBLE);
        }
    }

    public void setViewVisible(int visible) {
        if (linealayoutTop != null) {
            linealayoutTop.setVisibility(visible);
        }
    }

    /**
     * 单人账本是设置magin
     */
    public void setLinealayoutTopButtom(int magin, int maginTop) {
        if (linealayoutTop != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linealayoutTop.getLayoutParams();
            layoutParams.bottomMargin = magin;
            linealayoutTop.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) linealayoutTop.getLayoutParams();
            layoutParams1.topMargin = maginTop;
            linealayoutTop.setLayoutParams(layoutParams1);

        }
    }

    @Override
    public void showbedugtIncomExpense(String budget_content, String incom_content, String expenditure_content, String yusuan_money, String incom_money, String expenditure_money, int yusuann_content_size, int expenditure_content_bagroud, boolean showBudgetImage) {
        bedugText.setText(budget_content);
        incomText.setText(incom_content);
        expeseText.setText(expenditure_content);
        yusuann.setText(yusuan_money);
        incom.setText(incom_money);
        expense.setText(expenditure_money);
        yusuann.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(yusuann_content_size));
        expeseText.setBackgroundResource(expenditure_content_bagroud);

        renovation_bedug_balance.setText(yusuan_money);
        bedug_renovation.setText(budget_content);
        if (yusuan_money.equals("设置预算")) {
            bedug_renovation.setText("");
        }
        accumulated_expenditure_balance.setText(expenditure_money);
        if (!showBudgetImage) {
            setbugetImage("1");
        } else {
            setbugetImage("0");
        }
    }

    public void setbugetImage(String isShow) {
        if (yusuann == null) {
            return;
        }
        if (isShow != null && isShow.equals("0")) {
            Drawable drawable = getResources().getDrawable(R.drawable.setting_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            yusuann.setCompoundDrawablePadding(UIHelper.Dp2Px(mContext, 5));
            yusuann.setCompoundDrawables(drawable, null, null, null);
            renovation_bedug_balance.setCompoundDrawablePadding(UIHelper.Dp2Px(mContext, 5));
            renovation_bedug_balance.setCompoundDrawables(drawable, null, null, null);
        } else {
            yusuann.setCompoundDrawables(null, null, null, null);
            renovation_bedug_balance.setCompoundDrawables(null, null, null, null);
        }

    }
}
