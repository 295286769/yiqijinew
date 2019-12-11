package com.yiqiji.money.modules.homeModule.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.fragment.LazyFragment;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.DrawSimpleLineChartView;
import com.yiqiji.money.modules.common.view.IncomeExpenditureCircleView;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.common.widget.FullyLinearLayoutManager;
import com.yiqiji.money.modules.homeModule.home.activity.CateBillActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.BillExpandableAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.CatelistBeanChild;
import com.yiqiji.money.modules.homeModule.home.entity.StaticsBillInfo;
import com.yiqiji.money.modules.homeModule.home.entity.StatisticsInfo;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.homeinterface.StaticsInteface;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.home.perecenter.StatisticsPerecenter;
import com.yiqiji.money.modules.homeModule.home.wegit.NestedExpandaleListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressLint("ValidFragment")
public class StatisticsFragment extends LazyFragment implements OnClickListener,
        ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupExpandListener {
    private final int GET_WEEKS_DATA = 10086;
    private final int GET_THE_DAY_DATA = GET_WEEKS_DATA + 1;
    private final int GET_THE_WEEKS_DATA_DETAILS = GET_WEEKS_DATA + 2;
    private final int GET_ONE_TYPE_DETAILS_WITH_WEEK = GET_WEEKS_DATA + 3;
    private int theScondeCategory;
    private TextView tv_all, tv_week_days;

    private static boolean isPress = false;
    private Context mContext;
    private View view;
    private LinearLayout nodata;// 没有数据
    private TextView current_notdata;
    private ImageView no_statics_data_image;
    private TextView moth_expese_incom, switch_expese_incom, costtype;
    private DrawSimpleLineChartView lineChartView;
    private IncomeExpenditureCircleView incomeExpenditureCircleView;
    private MyRecyclerView recylerView;
    private NestedExpandaleListView bill_listview;
    private BillExpandableAdapter billExpandableAdapter;

    private LinearLayout notime_text;
    private LinearLayout layout;
    private RelativeLayout rela_expend;
    private CommonRecyclerViewAdapter<TotalBalance> adapter;
    private String type = "1";// 消费
    private String type_content = "支出";
    private Calendar calendar = Calendar.getInstance();
    private String[] moths_money = new String[5];
    private long[] time_money = new long[5];
    private String[] year_moth = new String[5];
    private String[] points;
    private Date data_time;
    private String bookName = "日常账本";
    private String bookid = "";
    private String cateName = "";
    private String sorttype = "0";// 0:月1：天
    private long accountbookltime = 0;
    private String[] incom_expex_text = new String[]{"切换支出情况", "切换收入情况"};
    private String which_month;
    private int[] lineColor = new int[]{R.color.expenditure, R.color.income};
    private int[] total_color = new int[]{R.color.total_one, R.color.total_two, R.color.total_three,
            R.color.total_four};

    private BooksDbInfo booksDbInfo;

    JSONObject data_info_expese = null;
    JSONObject data_info_incom = null;
    private StaticsInteface staticsInteface;
    private List<StatisticsInfo> statisticsInfos = null;
    private HashMap<String, StatisticsInfo> stringListHashMap = null;
    private String currentyearMoth = "";
    private List<String> yearMoths;
    private HashMap<String, TotalBalance> yearMothsmap = null;
    private boolean isSubscribe;

    public void setStaticsInteface(StaticsInteface staticsInteface) {
        this.staticsInteface = staticsInteface;
    }

    public StatisticsFragment(Context context, Date data_time, String bookName, String bookid, String sorttype,
                              String cateName, long accountbookltime, boolean isSubscribe) {
        this.mContext = context;
        this.data_time = data_time;
        this.bookName = bookName;
        this.bookid = bookid;
        this.sorttype = sorttype;
        this.cateName = cateName;
        this.accountbookltime = accountbookltime;
        this.isSubscribe = isSubscribe;
    }

    private void initYearMoths() {
        currentyearMoth = DateUtil.getYearMoth(data_time);
        yearMoths = new ArrayList<>();
        Calendar calendar_1 = Calendar.getInstance();
        long cTime = accountbookltime * 1000;
        calendar_1.setTime(data_time);
        for (int i = 0; i < 120; i++) {

            long calendar_time = DateUtil.stringToTime(calendar_1.getTimeInMillis());// 开始时间
            yearMoths.add(DateUtil.getYearMoth(new Date(calendar_1.getTimeInMillis())));

            if (calendar_time < cTime) {// 显示记账的前一个月
                calendar_1.add(Calendar.MONTH, -1);
                yearMoths.add(DateUtil.getYearMoth(new Date(calendar_1.getTimeInMillis())));
                break;
            }
            calendar_1.add(Calendar.MONTH, -1);
        }
        Collections.reverse(yearMoths);

        yearMoths.add("预设的");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_statistics_fragment, null);
            initYearMoths();
            initView();

        }
        isPress = true;
        lazyLoad();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView() {

        moth_expese_incom = (TextView) view.findViewById(R.id.moth_expese_incom);
        switch_expese_incom = (TextView) view.findViewById(R.id.switch_expese_incom);
        lineChartView = (DrawSimpleLineChartView) view.findViewById(R.id.lineChartView);
        incomeExpenditureCircleView = (IncomeExpenditureCircleView) view.findViewById(R.id.incomeExpenditureCircleView);
        tv_all = (TextView) view.findViewById(R.id.tv_all_ac_main);
        tv_week_days = (TextView) view.findViewById(R.id.weekdays);
        recylerView = (MyRecyclerView) view.findViewById(R.id.recylerView);
        bill_listview = (NestedExpandaleListView) view.findViewById(R.id.bill_listview);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        costtype = (TextView) view.findViewById(R.id.costtype);
        nodata = (LinearLayout) view.findViewById(R.id.nodata);
        rela_expend = (RelativeLayout) view.findViewById(R.id.rela_expend);
        current_notdata = (TextView) view.findViewById(R.id.current_notdata);
        no_statics_data_image = (ImageView) view.findViewById(R.id.no_statics_data_image);
        notime_text = (LinearLayout) view.findViewById(R.id.notime_text);
        BooksDetailPerecenter.getBookDetail(mContext, bookid, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                super.onSuccess(booksDbInfo);
                if (booksDbInfo != null) {
                    StatisticsFragment.this.booksDbInfo = booksDbInfo;
                    sorttype = booksDbInfo.getSorttype();
                    cateName = booksDbInfo.getAccountbookcatename();
                }
            }
        });
        initAdapter();
        switch_expese_incom.setOnClickListener(this);

    }


    @SuppressLint("NewApi")
    private void initAdapter() {
        billExpandableAdapter = new BillExpandableAdapter(mContext, lists);
        bill_listview.setAdapter(billExpandableAdapter);
        bill_listview.setOnGroupClickListener(this);
        bill_listview.setOnChildClickListener(this);
        bill_listview.setOnGroupExpandListener(this);

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setCanScroll(false);
        recylerView.setHasFixedSize(true);
        // recylerView.setNestedScrollingEnabled(false);
        adapter = new CommonRecyclerViewAdapter<TotalBalance>(mContext, lists) {

            @Override
            public int getLayoutViewId(int viewType) {
                // TODO Auto-generated method stub
                return R.layout.activity_balance_list;
            }

            @Override
            public void convert(CommonRecyclerViewHolder h, TotalBalance totalBalance, int position) {
                TextView contentView = h.getView(R.id.content);
                double mBalance = totalBalance.getTotal_balance();
//                String mBalanceString = XzbUtils.getBalance(mBalance);
                String mBalanceString = XzbUtils.formatDouble("%.2f", totalBalance.getTotal_balance());
                if (totalBalance.getBilltype().equals("1")) {// 类型（0表示收入，1表示支出,2转账，3结算，4交款）
                    h.setText(R.id.balance, "-" + StringUtils.moneySplitComma(mBalanceString));
                    h.setTextColor(R.id.balance, getResources().getColor(R.color.expenditure));
                } else {
                    h.setText(R.id.balance, "+" + StringUtils.moneySplitComma(mBalanceString));
                    h.setTextColor(R.id.balance, getResources().getColor(R.color.income));
                }
                String iamge_url = totalBalance.getBillcateicon();
                String cateid = totalBalance.getBillcateid();
                String cateName = totalBalance.getContent();
                iamge_url = XzbUtils.initImageUrlNeedFile(iamge_url, cateid);
                h.setImage(R.id.image, iamge_url, 0);
                h.setText(R.id.content, cateName);

                double pesent = XzbUtils.setTwoDecimalFormat("#.000", mBalance / total_expese) * 100;
                String pesentString = XzbUtils.formatDouble("%.1f", pesent);
                h.setText(R.id.balance_pesent, pesentString + "%");

            }
        };
        recylerView.setAdapter(adapter);
    }

    public void initData(Date record_date) {
        this.data_time = record_date;
        currentyearMoth = DateUtil.getYearMoth(data_time);
        calendar.setTime(record_date);
        initMothsCalender();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private long start_time = 0;
    private long end_time = 0;

    private void initMothsCalender() {
        calendar.add(Calendar.MONTH, -3);
        for (int i = 0; i < 5; i++) {
            if (i > 0) {
                calendar.add(Calendar.MONTH, 1);
            }
            if (i == 0) {
                start_time = calendar.getTimeInMillis();
                start_time = DateUtil.stringToTime(start_time);// 开始时间为00:00:00

            } else if (i == 4) {
                end_time = calendar.getTimeInMillis();
                Date date = new Date(end_time);
                Calendar calendar_end_time = Calendar.getInstance();
                calendar_end_time.setTime(date);
                calendar_end_time.add(Calendar.MONTH, 12);
                end_time = DateUtil.stringToTime(calendar_end_time.getTimeInMillis()) - 24 * 60 * 60 * 1000;// 结束时间
                // 23:59
            }
            moths_money[i] = (calendar.get(Calendar.MONTH) + 1) + "月";
            year_moth[i] = DateUtil.getDateToString(calendar.getTimeInMillis() / 1000, "yyyyMM");
            time_money[i] = calendar.getTimeInMillis();
        }
        calendar.setTime(data_time);
        changeIncomExpexe();
        // calendar.setTime(data_time);
    }

    private void changeIncomExpexe() {
        if (switch_expese_incom != null) {
            expeseIncomText();
            getFiveMoths();
        }


    }

    private void expeseIncomText() {
        boolean isExpect = DateUtil.isExpectMoth(data_time);
        if (!TextUtils.isEmpty(sorttype) && sorttype.equals("0")) {
            if (lineChartView != null) {
                lineChartView.setVisibility(View.GONE);
            }
        }
        if (type.equals("1")) {// 支出
            tv_all.setTextColor(getResources().getColor(R.color.expenditure));
            which_month = (calendar.get(Calendar.MONTH) + 1) + "月" + "支出情况";
            if (isExpect) {
                which_month = "预设的" + "支出情况";
            }
            switch_expese_incom.setText(incom_expex_text[1]);
            if (lineChartView != null) {
                lineChartView.setLineColor(getResources().getColor(lineColor[0]));
            }
            costtype.setText("支出");
            if (sorttype.equals("0")) {
                which_month = "支出情况";
            }
        } else {
            tv_all.setTextColor(getResources().getColor(R.color.income));
            which_month = (calendar.get(Calendar.MONTH) + 1) + "月" + "收入情况";
            if (isExpect) {
                which_month = "预设的" + "收入情况";
            }
            switch_expese_incom.setText(incom_expex_text[0]);
            if (lineChartView != null) {
                lineChartView.setLineColor(getResources().getColor(lineColor[1]));
            }
            costtype.setText("收入");
            if (sorttype.equals("0")) {
                which_month = "收入情况";
            }
        }


        moth_expese_incom.setText(which_month);
    }

    private void initMothsValues() {
        boolean isExpectMoth = DateUtil.isExpectMoth(data_time);
        int what = 4;
        if (isExpectMoth) {// 预期月份
            what = RequsterTag.EXPECTMOTH;
        }
        DownUrlUtil.getTotalBalances(data_time, bookid, sorttype, type, isExpectMoth, handler, what);

    }


    private void initTypeTotalBalance() {
        DownUrlUtil.getDailycostMothInfosTypeFirstThreeLast(mContext, data_time, bookid, type, sorttype, handler);

    }

    private void getFiveMoths() {

        new Thread() {
            public void run() {
                List<TotalBalance> list = DbInterface.getDailycostMothInfosTypeTotal(mContext.getContentResolver(),
                        start_time / 1000, end_time / 1000, bookid, type);
                Message msg = Message.obtain();
                msg.obj = list;
                msg.what = 1;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private List<TotalBalance> lists = new ArrayList<>();
    private List<TotalBalance> entities = new ArrayList<TotalBalance>();
    private List<TotalBalance> copeTypeName = new ArrayList<TotalBalance>();
    private double total_expese = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    List<TotalBalance> lists_five = (List<TotalBalance>) msg.obj;
                    initFiveMothInfo(lists_five);
                    initTypeTotalBalance();
                    initMothsValues();

                    break;
                case 3:

                    List<TotalBalance> copeType = (List<TotalBalance>) msg.obj;
                    initPieChart(copeType);

                    break;
                case 4:
                    final List<TotalBalance> list = (List<TotalBalance>) msg.obj;
                    total_expese = 0.00;
                    for (TotalBalance totalBalance : list) {
                        total_expese += totalBalance.getTotal_balance();
                    }
                    setTotal(total_expese);
                    updatList(list);
                    DownUrlUtil.initBooksDataAndMember(bookid, new ViewCallBack<BooksDbInfo>() {
                        @Override
                        public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                            super.onSuccess(booksDbInfo);
                            StatisticsFragment.this.booksDbInfo = booksDbInfo;
                        }

                        @Override
                        public void onFailed(SimpleMsg simleMsg) {
                            super.onFailed(simleMsg);
                            initStatisticsData(bookid);
                        }
                    });
                    break;
                case RequsterTag.EXPECTMOTH:
                    List<TotalBalance> list3 = (List<TotalBalance>) msg.obj;
                    Message message2 = new Message();
                    message2.what = 4;
                    message2.obj = list3;
                    handler.sendMessage(message2);
                    break;
                case RequsterTag.EXPECTMOTHBING:
                    List<TotalBalance> copeTypeBalances = (List<TotalBalance>) msg.obj;
                    Message message2Message = new Message();
                    message2Message.what = 3;
                    message2Message.obj = copeTypeBalances;
                    handler.sendMessage(message2Message);
                    break;

                default:
                    break;
            }

        }

    };

    private void setTotal(double total_expese) {
        billExpandableAdapter.setGroupTotalbalance(total_expese);
        String total_expese_String = XzbUtils.formatDouble("%.2f", total_expese);
        if (type.equals("1")) {

            tv_all.setTextColor(getResources().getColor(R.color.expenditure));
            tv_all.setText("-" + StringUtils.moneySplitComma(total_expese_String));
        } else {
            tv_all.setTextColor(getResources().getColor(R.color.income));
            tv_all.setText("+" + StringUtils.moneySplitComma(total_expese_String));
        }
        if (total_expese == 0) {
            tv_all.setTextColor(getResources().getColor(R.color.secondary_text));
            tv_all.setText("" + total_expese_String);
        }
    }

    JSONObject data_info = null;

    private void initStatisticsData(String id) {
        StatisticsPerecenter.iniStatisticsInfo(mContext, id, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                JSONObject jsonObject = new JSONObject(o.toString());
                String has_expese = jsonObject.optString("1", "");
                String has_incon = jsonObject.optString("0", "");

                if (!TextUtils.isEmpty(has_expese)) {
                    data_info = jsonObject.getJSONObject("1");
                    data_info_expese = data_info;
                }
                if (!TextUtils.isEmpty(has_incon)) {
                    data_info = jsonObject.getJSONObject("0");
                    data_info_incom = data_info;
                }

                setStatiscBIllListChange(data_info_expese);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }
        });
    }

    private void setStatiscBIllListChange(JSONObject jsonObject) throws Exception {
        stringListHashMap = new HashMap<String, StatisticsInfo>();
        yearMothsmap = new HashMap<String, TotalBalance>();
        statisticsInfos = new ArrayList<StatisticsInfo>();
        if (jsonObject != null) {
            String billtyName = jsonObject.getString("billtypename");
            type_content = billtyName;
            sorttype = jsonObject.getString("sorttype");
            staticsInteface.getSorttype(sorttype);
            String totalamount = "";
            JSONObject datelist = null;
            if (!TextUtils.isEmpty(jsonObject.optString("datelist", ""))) {
                datelist = jsonObject.getJSONObject("datelist");
            } else if (!TextUtils.isEmpty(jsonObject.optString("list", ""))) {
                datelist = jsonObject.getJSONObject("list");
            }


            List<TotalBalance> totalBalanceLists = new ArrayList<TotalBalance>();
            if (billtyName.equals("支出")) {
                type = "1";

            } else {
                type = "0";
            }
            if (!TextUtils.isEmpty(jsonObject.optString("totalamount", ""))) {
                totalamount = jsonObject.getString("totalamount");
            }
            if (sorttype.equals("1")) {
                if (datelist != null) {
                    for (int i = 0; i < yearMoths.size(); i++) {
                        String moth_string = yearMoths.get(i);

                        if (!TextUtils.isEmpty(datelist.optString(moth_string, ""))) {
                            JSONObject yearmoth = datelist.getJSONObject(moth_string);

                            if (yearmoth != null) {
                                String moth = "";
                                if (moth_string.equals("预设的")) {
                                    moth = year_moth[year_moth.length - 1].substring(4);
                                    moth_string = year_moth[year_moth.length - 1];
                                } else {
                                    moth = moth_string.substring(4);
                                }
                                StatisticsInfo statisticsInfo = GsonUtil.GsonToBean(yearmoth.toString(), StatisticsInfo.class);
                                stringListHashMap.put(moth_string, statisticsInfo);

                                totalamount = statisticsInfo.getTotalamount() + "";

                                TotalBalance totalBalance = new TotalBalance();
                                totalBalance.setTotal_balance(Double.parseDouble(totalamount));
                                totalBalance.setMoths(Integer.parseInt(moth));
                                yearMothsmap.put(moth_string, totalBalance);

                            }
                        }

                    }
                    changeMothDate(totalBalanceLists);


                }
            }

            if (!TextUtils.isEmpty(jsonObject.optString("catelist", ""))) {

                JSONArray catelist = jsonObject.getJSONArray("catelist");
                if (catelist != null) {

                    List<StaticsBillInfo> staticsBillInfos = GsonUtil.fromJsonArray(catelist.toString(), StaticsBillInfo.class);
                    setTotalList(totalamount, totalBalanceLists, staticsBillInfos);
//                    initPieChart(copeType);
                }
            }


        } else {

            setTotalList("0.00", new ArrayList<TotalBalance>(), new ArrayList<StaticsBillInfo>());
        }

    }

    /**
     * 更新统计视图
     *
     * @param totalamount
     * @param totalBalanceLists
     */
    private void updateDateList(String totalamount, List<TotalBalance> totalBalanceLists) {
        setTotal(Double.parseDouble(totalamount));
        expeseIncomText();
        updatList(totalBalanceLists);
    }

    /**
     * 有时间轴切换数据变化
     *
     * @param totalBalanceLists
     */

    private void changeMothDate(List<TotalBalance> totalBalanceLists) {
        List<TotalBalance> totalBalances = new ArrayList<TotalBalance>();
        for (int i = 0; i < 5; i++) {
            String yearMoth = year_moth[i];
            if (yearMothsmap != null) {
                TotalBalance totalBalance = yearMothsmap.get(yearMoth);
                if (totalBalance != null) {
                    totalBalances.add(totalBalance);
                }
            }
        }
        initFiveMothInfo(totalBalances);
        String totalamount = "0.00";

        if (stringListHashMap != null) {
            StatisticsInfo statisticsInfo = stringListHashMap.get(currentyearMoth);
            totalamount = statisticsInfo.getTotalamount() + "";
            List<StaticsBillInfo> statisticsInfos = statisticsInfo.getList();
            setTotalList(totalamount, totalBalanceLists, statisticsInfos);

        }
    }

    /**
     * 获取最后饼图和账单列表
     *
     * @param totalBalanceLists
     * @param staticsBillInfos
     */
    private void setTotalList(String totalamount, List<TotalBalance> totalBalanceLists, List<StaticsBillInfo> staticsBillInfos) {
        List<TotalBalance> copeType = new ArrayList<TotalBalance>();
        if (staticsBillInfos == null) {
            staticsBillInfos = new ArrayList<>();
        }
        if (staticsBillInfos != null) {
            for (int i = 0; i < staticsBillInfos.size(); i++) {

                StaticsBillInfo catelistBean = staticsBillInfos.get(i);
                List<CatelistBeanChild> catelistBeanChildren = catelistBean.getList();

                TotalBalance totalBalance = new TotalBalance();
                totalBalance.setBilltype(type);
                totalBalance.setPercentage(catelistBean.getPercentage() + "");
                totalBalance.setBillcateicon(catelistBean.getCategoryicon());
                totalBalance.setContent(catelistBean.getCategorytitle());
                totalBalance.setBillcateid(catelistBean.getCategoryid());
                totalBalance.setTotal_balance(Double.parseDouble(catelistBean.getTotalamount()));
                if (i < 3) {
                    copeType.add(totalBalance);
                }
                List<TotalBalance> totalBalancechilds = new ArrayList<TotalBalance>();
                if (catelistBeanChildren != null) {
                    for (int j = 0; j < catelistBeanChildren.size(); j++) {
                        CatelistBeanChild catelistBeanChild = catelistBeanChildren.get(j);
                        TotalBalance totalBalancechild = new TotalBalance();
                        totalBalancechild.setBillsubcateicon(catelistBeanChild.getCategoryicon());
                        totalBalancechild.setBilltype(type);
                        totalBalancechild.setCounts(catelistBeanChild.getBillcount());
                        totalBalancechild.setBillsubcatename(catelistBeanChild.getCategorytitle());
                        totalBalancechild.setBillsubcateid(catelistBeanChild.getCategoryid());
                        String totalamountchild = catelistBeanChild.getTotalamount();
                        if (TextUtils.isEmpty(totalamountchild)) {
                            totalamountchild = "0.00";
                        }
                        totalBalancechild.setTotal_balance(Double.parseDouble(totalamountchild));
                        totalBalancechilds.add(totalBalancechild);
                    }
                    totalBalance.setTotalBalances(totalBalancechilds);

                }
                totalBalanceLists.add(totalBalance);

            }
            initPieChart(copeType);
            updateDateList(totalamount, totalBalanceLists);

        }
    }

    /**
     * 饼图数据
     *
     * @param copeType
     */
    private void initPieChart(List<TotalBalance> copeType) {
        if (copeTypeName != null && copeTypeName.size() > 0) {
            copeTypeName.clear();
        }
        double other_account_add = 0;
        for (int k = 0; k < copeType.size(); k++) {
            copeTypeName.add(copeType.get(k));
            other_account_add += copeTypeName.get(k).getTotal_balance();
        }

        double other_account = 0;
        if (entities.size() > 0) {
            other_account = entities.get(0).getTotal_balance() - other_account_add;
        }

        if (incomeExpenditureCircleView != null) {
            double[] fs = null;
            String[] contents = null;
            int[] statis_color = null;
            if (copeTypeName.size() == 3 && other_account > 0) {
                fs = new double[copeTypeName.size() + 1];
                contents = new String[copeTypeName.size() + 1];
                statis_color = new int[copeTypeName.size() + 1];
                fs[3] = other_account;
                contents[3] = "其他类别";
                statis_color[3] = getResources().getColor(R.color.black);

            } else {
                fs = new double[copeTypeName.size()];
                contents = new String[copeTypeName.size()];
                statis_color = new int[copeTypeName.size()];
            }

            for (int k = 0; k < copeTypeName.size(); k++) {
                fs[k] = copeTypeName.get(k).getTotal_balance();
                String name = copeTypeName.get(k).getContent();
//                if (!TextUtils.isEmpty(copeTypeName.get(k).getBillsubcatename())) {
//                    name = copeTypeName.get(k).getBillsubcatename();
//                }
                contents[k] = name;

                statis_color[k] = getResources().getColor(total_color[k]);

            }

            incomeExpenditureCircleView.setPesent(fs, statis_color, contents, type);
            incomeExpenditureCircleView.setExpenditure_content(type_content);

        }
    }

    /**
     * 五个月数据
     *
     * @param lists_five
     */
    private void initFiveMothInfo(List<TotalBalance> lists_five) {
        if (entities != null && entities.size() > 0) {
            entities.clear();
        }
        for (int j2 = 0; j2 < lists_five.size(); j2++) {
            entities.add(lists_five.get(j2));
        }
        points = new String[5];
        for (int j = 0; j < 5; j++) {
            points[j] = "0.00";

        }
        for (int j = 0; j < entities.size(); j++) {
            double total_money = entities.get(j).getTotal_balance();
//            int type = Integer.parseInt(entities.get(j).getBilltype());
            String moth = entities.get(j).getMoths() + "月";
            for (int k = 0; k < moths_money.length; k++) {
                if (moth.equals(moths_money[k])) {

                    points[k] = total_money + "";
                    String total_moneyString = XzbUtils.getBalance(total_money);

                }
            }
        }

        if (lineChartView != null) {
            lineChartView.resetTheSpicalPointStorkeWidth(3, data_time);
            lineChartView.reDrawFoldLinePoionts(points, time_money, moths_money, type);
        }
    }

    private void updatList(List<TotalBalance> list) {
        if (lists != null && lists.size() > 0) {
            lists.clear();
        }
        for (int l = 0; l < list.size(); l++) {
            lists.add(list.get(l));
        }
        Date date = new Date();
        String current_day = DateUtil.formatTheDateToMM_dd(date, 3);
        String current_year = DateUtil.formatTheDateToMM_dd(date, 5);
        String day = DateUtil.formatTheDateToMM_dd(data_time, 3);
        String year = DateUtil.formatTheDateToMM_dd(data_time, 5);
        if (lists.size() == 0) {

            if (current_day.equals(day) && current_year.equals(year)) {
                current_notdata.setVisibility(View.VISIBLE);
                no_statics_data_image.setVisibility(View.GONE);
            } else {
                current_notdata.setVisibility(View.GONE);
                no_statics_data_image.setVisibility(View.VISIBLE);
            }
            if (sorttype.equals("0")) {
                current_notdata.setText("暂无数据");
            }
            nodata.setVisibility(View.VISIBLE);
            rela_expend.setVisibility(View.GONE);
        } else {
            nodata.setVisibility(View.GONE);
            rela_expend.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (billExpandableAdapter != null) {
            billExpandableAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.switch_expese_incom:
                String text = switch_expese_incom.getText().toString();
                if (type.endsWith("1")) {
                    type = "0";
                    type_content = "收入";
//                    expeseIncomText();

                    try {
                        if (booksDbInfo == null) {
                            setStatiscBIllListChange(data_info_incom);
                        } else {
                            changeIncomExpexe();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    type = "1";
                    type_content = "支出";
//                    expeseIncomText();

                    try {
                        if (booksDbInfo == null) {
                            setStatiscBIllListChange(data_info_expese);
                        } else {
                            changeIncomExpexe();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

            default:
                break;
        }
    }

    private boolean isSeachMoth = true;

    @Override
    protected void lazyLoad() {
        if (!isPress || view == null || !isVisible) {
            return;
        }
        if (cateName != null && (cateName.equals("结婚账本") || cateName.equals("装修账本"))) {
            type = "1";
            isSeachMoth = false;
        }
        initData(data_time);
    }

    public void onEventMainThread(String updateBill) {
        if (updateBill != null && updateBill.equals("updatebill")) {
            initData(data_time);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        view = null;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        TotalBalance totalBalance = lists.get(groupPosition).getTotalBalances().get(childPosition);
        String billcateid = totalBalance.getBillcateid();
        String billcateidname = totalBalance.getContent();
        String billsubcatename = totalBalance.getBillsubcatename();
        String billsubcateid = totalBalance.getBillsubcateid();
        if (!TextUtils.isEmpty(billsubcatename)) {
            billcateid = billsubcateid;
            billcateidname = billsubcatename;
        }
        CateBillActivity.openActivity(mContext, bookid, sorttype, billcateid, billcateidname, data_time.getTime() / 1000, isSubscribe);
        return false;
    }

    private boolean isSelect = false;

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        return false;
    }

    private int position = -1;

    @Override
    public void onGroupExpand(int groupPosition) {
        if (position > -1 && position != groupPosition) {
            bill_listview.collapseGroup(position);
        }
        position = groupPosition;
    }
}
