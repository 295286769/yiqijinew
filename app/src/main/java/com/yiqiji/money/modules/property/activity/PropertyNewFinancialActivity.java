package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.control.ActionSheetDialog;
import com.yiqiji.money.modules.common.control.CustomDatePickerDialog;
import com.yiqiji.money.modules.common.control.LinearLayoutForDatePicker;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.NetPropertEntity;

import java.util.HashMap;

/**
 * Created by dansakai on 2017/3/9.
 * 新建 网络理财、银行理财、自定义
 */

public class PropertyNewFinancialActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;//标题

    private EditText et_project;//名称
    private EditText et_capital;//本金
    private EditText et_yearbenifit;//年化利率
    private EditText et_limit;//期限
    private RadioGroup radio_group_wealth;
    private TextView tv_startData;//起息日期
    private LinearLayout ll_backWay;//回款方式
    private TextView tv_backWay;
    private int backWayInt = 4;

    private Button btn_save;//保存
    private LinearLayout ll_edit;
    private Button btn_delet;
    private Button btn_etSave;

    private ApiService apiService;//网络请求
    private AddPropertyItemEntity entity = null;
    private int itemType;
    private boolean isDay = true;


    private CustomDatePickerDialog mDatePicker;

    private NetPropertEntity netEntity = null;

    private boolean isEdit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_financial);
        apiService = RetrofitInstance.get().create(ApiService.class);
        initView();
        changeState();
    }

    private void changeState() {
        if (itemType == 15) {//网络理财
            tv_title.setText("新建网络理财");
            ll_backWay.setVisibility(View.VISIBLE);
        } else if (itemType == 16) {//银行理财
            tv_title.setText("新建银行理财");
            ll_backWay.setVisibility(View.GONE);
        } else if (itemType == 18) {
            tv_title.setText("新建自定义");
            ll_backWay.setVisibility(View.VISIBLE);
        }
        if (isEdit) {
            tv_title.setText("编辑账户");
            ll_edit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(netEntity.getItemname())) {
                et_project.setText(netEntity.getItemname());
                et_project.setSelection(netEntity.getItemname().length());
            }
            if (!StringUtils.isEmpty(netEntity.getAssetamount())) {
                et_capital.setText(netEntity.getAssetamount());
            }
            if (!StringUtils.isEmpty(netEntity.getYieldrate()) && "0.00".equals(netEntity.getYieldrate())) {
                et_yearbenifit.setText(netEntity.getYieldrate());
            }
            if (!StringUtils.isEmpty(netEntity.getDeadline())) {
                et_limit.setText(netEntity.getDeadline());
            }
            if (!StringUtils.isEmpty(netEntity.getInterestdate())) {
                tv_startData.setText(DateUtil.formatDate(Long.parseLong(netEntity.getInterestdate())));
            }
            if (itemType != 16) {
                backWayInt = Integer.parseInt(netEntity.getDividendmethod());
                tv_backWay.setText(parceString(backWayInt));

            }
        } else {
            ll_edit.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {//编辑
            netEntity = (NetPropertEntity) getIntent().getSerializableExtra("netEntity");
            if (netEntity != null) {
                itemType = Integer.parseInt(netEntity.getItemtype());
            }
        } else {
            entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("addproEntity");
            if (entity != null) {
                itemType = Integer.parseInt(entity.getItemtypeId());
            }
        }


        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_project = (EditText) findViewById(R.id.et_project);
        UIHelper.showSoftInputMethod(et_project);
        et_capital = (EditText) findViewById(R.id.et_capital);

        et_capital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_capital.setText(s);
                        et_capital.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_capital.setText(s);
                    et_capital.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_capital.setText(s.subSequence(0, 1));
                        et_capital.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_yearbenifit = (EditText) findViewById(R.id.et_yearbenifit);
        et_yearbenifit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_yearbenifit.setText(s);
                        et_yearbenifit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_yearbenifit.setText(s);
                    et_yearbenifit.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_yearbenifit.setText(s.subSequence(0, 1));
                        et_yearbenifit.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_limit = (EditText) findViewById(R.id.et_limit);

        radio_group_wealth = (RadioGroup) findViewById(R.id.radio_group_wealth);
        ((RadioButton) radio_group_wealth.getChildAt(0)).setChecked(true);
        radio_group_wealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        isDay = true;
                        break;
                    case R.id.rb_month:
                        isDay = false;
                        break;
                }
            }
        });

        tv_startData = (TextView) findViewById(R.id.tv_startData);
        tv_startData.setOnClickListener(this);
        tv_startData.setText(DateUtil.getTodayDate());
        ll_backWay = (LinearLayout) findViewById(R.id.ll_backWay);
        tv_backWay = (TextView) findViewById(R.id.tv_backWay);
        tv_backWay.setOnClickListener(this);
        tv_backWay.setText("每月还息，到期还本息");

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        btn_delet = (Button) findViewById(R.id.btn_delet);
        btn_delet.setOnClickListener(this);
        btn_etSave = (Button) findViewById(R.id.btn_etSave);
        btn_etSave.setOnClickListener(this);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showSoftInputMethod(et_project);
            }
        }, 100);

    }

    /**
     * 提交数据
     */
    private void submit() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assettype", entity.getCategoryid());
        hashMap.put("itemtype", entity.getItemtypeId());
        hashMap.put("itemname", et_project.getText().toString());
        String assetamount;
        if (!StringUtils.isEmpty(et_capital.getText().toString())) {
            assetamount = et_capital.getText().toString();
        } else {
            assetamount = "0";
        }
        hashMap.put("assetamount", assetamount);
        double yearPercent;
        if (!StringUtils.isEmpty(et_yearbenifit.getText().toString())) {
            yearPercent = Double.parseDouble(et_yearbenifit.getText().toString()) / 100;
        } else {
            yearPercent = 0.0;
        }
        hashMap.put("yieldrate", String.valueOf(yearPercent));
        int limatime;
        if (isDay) {
            limatime = Integer.parseInt(et_limit.getText().toString());
        } else {
            limatime = Integer.parseInt(et_limit.getText().toString()) * 30;
        }
        hashMap.put("deadline", String.valueOf(limatime));//天
        hashMap.put("interestdate", DateUtil.getTime(tv_startData.getText().toString()));//时间戳
        hashMap.put("method", String.valueOf(backWayInt));//回款方式


        CommonFacade.getInstance().exec(Constants.ADD_FINANC_ASSERT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                setResult(RESULT_OK);
                showToast("资产添加成功");
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_startData://日期选择
                // 创建并且显示对话框
                if (mDatePicker == null) {

                    mDatePicker = new CustomDatePickerDialog(mContext,
                            new LinearLayoutForDatePicker.DatePickListener() {

                                @Override
                                public void DatePick(int leftValue, int middleValue, int rightValue,String item) {
                                    tv_startData.setText(leftValue + "-" + middleValue + "-" + rightValue);
                                    mDatePicker.dismiss();
                                }

                            });
                    mDatePicker
                            .setTitle("请选择起息时间")
                            .setYearMonthDayRange(1990, 2020, 1, 12, 1, 31);

                    String[] datas = DateUtil.getTodayDate().split("-");

                    if (datas != null && datas.length == 3) {
                        mDatePicker.setCurrentSelectTime(Integer.parseInt(datas[0]), Integer.parseInt(datas[1]), Integer.parseInt(datas[2]));
                    }
                }

                mDatePicker.showDialog();
                break;
            case R.id.tv_backWay://回款方式
                new ActionSheetDialog(PropertyNewFinancialActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("到期还本付息", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_backWay.setText("到期还本付息");
                                        backWayInt = 1;
                                    }
                                })
                        .addSheetItem("月还等额本金", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_backWay.setText("月还等额本金");
                                        backWayInt = 2;
                                    }
                                })
                        .addSheetItem("月还等额本息", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_backWay.setText("月还等额本息");
                                        backWayInt = 3;
                                    }
                                })
                        .addSheetItem("每月还息，到期还本息", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_backWay.setText("每月还息，到期还本息");
                                        backWayInt = 4;
                                    }
                                }).show();
                break;
            case R.id.btn_save://保存
                if (StringUtils.isEmpty(et_project.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入项目名称", 500);
                    UIHelper.showSoftInputMethod(et_project);
                    return;
                } else if (StringUtils.isEmpty(et_limit.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入期限", 500);
                    UIHelper.showSoftInputMethod(et_limit);
                    return;
                } else {
                    submit();
                }
                break;
            case R.id.btn_delet://删除
                showSimpleAlertDialog("", "确定删除该资产么？", "确定", "取消", false, true,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                delet();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });
                break;
            case R.id.btn_etSave://更新
                if (StringUtils.isEmpty(et_project.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入项目名称", 500);
                    UIHelper.showSoftInputMethod(et_project);
                    return;
                } else if (StringUtils.isEmpty(et_limit.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入期限", 500);
                    UIHelper.showSoftInputMethod(et_limit);
                    return;
                } else {
                    upData();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 更新资产
     */
    private void upData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("assetid", netEntity.getAssetid());
        map.put("itemname", et_project.getText().toString());
        String assetamount;
        if (!StringUtils.isEmpty(et_capital.getText().toString())) {
            assetamount = et_capital.getText().toString();
        } else {
            assetamount = "0";
        }
        map.put("assetamount", assetamount);
        double yearPercent;
        if (!StringUtils.isEmpty(et_yearbenifit.getText().toString())) {
            yearPercent = Double.parseDouble(et_yearbenifit.getText().toString()) / 100;
        } else {
            yearPercent = 0.0;
        }
        map.put("yieldrate", String.valueOf(yearPercent));
        int limatime;
        if (isDay) {
            limatime = Integer.parseInt(et_limit.getText().toString());
        } else {
            limatime = Integer.parseInt(et_limit.getText().toString()) * 30;
        }
        map.put("deadline", String.valueOf(limatime));
        map.put("interestdate", DateUtil.getTime(tv_startData.getText().toString()));
        map.put("method", String.valueOf(backWayInt));
        CommonFacade.getInstance().exec(Constants.ADD_RENEW_FINAN, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                Intent intent = new Intent();
                intent.putExtra("isDel", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    /**
     * 删除资产
     */
    private void delet() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", netEntity.getAssetid());
        CommonFacade.getInstance().exec(Constants.ADD_DELE_ASSERT, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                Intent intent = new Intent();
                intent.putExtra("isDel", true);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatePicker != null) {
            mDatePicker.dismiss();
        }
    }

    private String parceString(int i) {
        String backString = "";
        switch (i) {
            case 1:
                backString = "到期还本付息";
                break;
            case 2:
                backString = "月还等额本金";
                break;
            case 3:
                backString = "月还等额本息";
                break;
            case 4:
                backString = "每月还息，到期还本息";
                break;
        }
        return backString;
    }
}
