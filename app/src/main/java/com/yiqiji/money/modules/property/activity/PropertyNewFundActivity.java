package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.deviceUtil;
import com.yiqiji.money.modules.property.adapter.SearchAdapter;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.FundAndStockEntity;
import com.yiqiji.money.modules.property.entity.SearchEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dansakai on 2017/3/8.
 * 新建(编辑) 股票、基金账户
 */

public class PropertyNewFundActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;//返回
    private TextView tv_title;//标题

    private TextView tv_brand;//基金名称
    private EditText et_brand;

    private TextView tv_curValue;//当前市值
    private TextView tv_stock;//股（股票显示）
    private EditText et_curValue;

    private TextView tv_capital;//本金
    private EditText et_capital;

    private Button btn_save;//保存

    private LinearLayout ll_edit;//
    private Button btn_delet;
    private Button btn_etSave;

    private boolean isEdit = false;

    private ListView listView;
    private SearchAdapter adapter;
    private AddPropertyItemEntity entity = null;
    private List<FundAndStockEntity> fundEntity = new ArrayList<>();//基金
    private List<FundAndStockEntity> stockEntity = new ArrayList<>();//股票
    private List<FundAndStockEntity> list = new ArrayList<>();

    private int itemType;//13基金14股票
    private ApiService service;//网络请求

    private SearchEntity searchEntity;

    private String fundCode = "";
    private String FunName = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (!isEdit) {
                        search(msg.obj.toString());
                    }
                    break;
                default:
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_new_fund);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gp.baidu.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);

        entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("addproEntity");
        itemType = Integer.parseInt(entity.getItemtypeId());
        initView();
        changeState();
    }

    /**
     * 根据传过来的数据，改变布局（基金、股票）
     */
    private void changeState() {
        if (13 == itemType) {//基金
            ll_edit.setVisibility(View.GONE);
            tv_brand.setText("基金名称");
            et_brand.setHint("基金名称/代码/拼音");
            tv_curValue.setText("当前市值");
            tv_stock.setVisibility(View.INVISIBLE);
            et_curValue.setHint("0.00(选填)");
            et_curValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            tv_capital.setText("本金");
            et_capital.setHint("0.00(选填)");
            tv_title.setText("添加基金");

            et_curValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            et_curValue.setText(s);
                            et_curValue.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        et_curValue.setText(s);
                        et_curValue.setSelection(2);
                    }
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            et_curValue.setText(s.subSequence(0, 1));
                            et_curValue.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (14 == itemType) {//股票
            if (isEdit) {
                tv_title.setText("编辑");
                et_brand.setText(entity.getItemname());
                et_brand.setEnabled(false);
                if (!StringUtils.isEmpty(entity.getStocknum()) && !"0".equals(entity.getStocknum())) {
                    et_curValue.setText(entity.getStocknum());
                    if (!StringUtils.isEmpty(entity.getStocknum())) {
                        et_curValue.setSelection(entity.getStocknum().length());
                    }
                }

                if (!StringUtils.isEmpty(entity.getCurrentprice())) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    et_capital.setText(df.format(Double.parseDouble(entity.getCurrentprice()) / Double.parseDouble(entity.getStocknum())));
                }

                btn_save.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
            } else {
                tv_title.setText("添加股票");
                et_brand.setEnabled(true);
                btn_save.setVisibility(View.VISIBLE);
                ll_edit.setVisibility(View.GONE);
            }
            tv_brand.setText("股票名称");
            et_brand.setHint("股票名称/代码/拼音");
            tv_curValue.setText("持股数");
            tv_stock.setVisibility(View.VISIBLE);
            et_curValue.setHint("请输入当前持股数");
            tv_capital.setText("每股成本");
            et_capital.setHint("0.00(选填)");
            et_curValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        et_brand = (EditText) findViewById(R.id.et_brand);
        et_brand.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    UIHelper.hideSoftInputMethod(et_brand);
                }
                return false;
            }
        });
        tv_curValue = (TextView) findViewById(R.id.tv_curValue);
        tv_stock = (TextView) findViewById(R.id.tv_stock);
        et_curValue = (EditText) findViewById(R.id.et_curValue);
        tv_capital = (TextView) findViewById(R.id.tv_capital);
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

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        btn_delet = (Button) findViewById(R.id.btn_delet);
        btn_delet.setOnClickListener(this);
        btn_etSave = (Button) findViewById(R.id.btn_etSave);
        btn_etSave.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        View headView = LayoutInflater.from(this).inflate(R.layout.activity_include_listhead, null);
        listView.addHeaderView(headView);
        adapter = new SearchAdapter(this, list, R.layout.activity_search_item);
        listView.setAdapter(adapter);

        if (!isEdit) {
            et_brand.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (StringUtils.isEmpty(s.toString())) {
                        listView.setVisibility(View.INVISIBLE);
                    } else {
                        // 输入改变处理
                        handler.removeMessages(100);// 先清空消息
                        Message msg = Message.obtain();
                        msg.what = 100;
                        msg.obj = s.toString();// 输入的文案
                        handler.sendMessageDelayed(msg, 500);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_brand.setText(list.get(position - 1).getF_symbolName());
                listView.setVisibility(View.INVISIBLE);
                fundCode = list.get(position - 1).getF_code();
                FunName = list.get(position - 1).getF_symbolName();
            }
        });


        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEdit) {
                    UIHelper.showSoftInputMethod(et_curValue);
                } else {
                    UIHelper.showSoftInputMethod(et_brand);
                }
            }
        }, 100);
    }


    /**
     * 搜索
     */
    private void search(String str) {
        HashMap<String, String> map = new HashMap<>();
        map.put("channel", "android");
        map.put("os_ver", LoginConfig.getInstance().getOsver());
        map.put("vv", "3.5.0");
        map.put("device", deviceUtil.getPhoneBrand());
        String netType = "";
        switch (InternetUtils.getAPNType(this)) {
            case 1:
                netType = "wifi";
                break;
            case 2:
                netType = "2G";
                break;
            case 3:
                netType = "3G";
                break;
            case 4:
                netType = "4G";
                break;
            case 0://无网络
                break;
            default:
                break;
        }
        map.put("device_net_type", netType);
        map.put("special_version_name", "");
        map.put("stoken", "");
        map.put("from", "android");
        map.put("actionid", String.valueOf(System.currentTimeMillis()));
        map.put("logid", String.valueOf(System.currentTimeMillis()));
        map.put("cuid", deviceUtil.getDeviceId(this));
        map.put("format", "json");
        map.put("query_content", str);

        service.getSearchFundAndStock(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    JSONObject jo_main = new JSONObject(response.body().string());
                    if (jo_main.optInt("code") == 0) {//成功
                        if (!StringUtils.isEmpty(jo_main.optString("data"))) {
                            JSONObject jo_data = new JSONObject(jo_main.optString("data"));
                            if (!StringUtils.isEmpty(jo_data.optString("stock_data"))) {
                                searchEntity = SearchEntity.parceList(jo_data.optString("stock_data"));
                            }
                            if (!StringUtils.isEmptyList(fundEntity)) {
                                fundEntity.clear();
                            }
                            if (!StringUtils.isEmptyList(stockEntity)) {
                                stockEntity.clear();
                            }

                            list.clear();
                            if (searchEntity != null) {
                                fundEntity = searchEntity.getFundEntity();
                                stockEntity = searchEntity.getStockEntity();
                                if (itemType == 13 && !StringUtils.isEmptyList(fundEntity)) {//基金
                                    list.addAll(fundEntity);
                                } else if (itemType == 14 && !StringUtils.isEmptyList(stockEntity)) {
                                    list.addAll(stockEntity);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    LogUtil.log_error(null, e);
                } catch (IOException e) {
                    LogUtil.log_error(null, e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    /**
     * 删除账户
     */
    private void delect() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", entity.getAssetid());
        CommonFacade.getInstance().exec(Constants.ADD_DELE_ASSERT, hashMap, new ViewCallBack() {
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

    /**
     * 更新股票(基金)
     */
    private void upData() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("assetid", entity.getAssetid());//资产id
        if (itemType == 14) {//股票
            hashMap.put("stockcode", entity.getStockcode());//股票代码
            String stockNum = "0", costprice = "0";
            if (!StringUtils.isEmpty(et_curValue.getText().toString())) {
                stockNum = et_curValue.getText().toString();
            }
            if (!StringUtils.isEmpty(et_capital.getText().toString())) {
                costprice = et_capital.getText().toString();
            }
            hashMap.put("stocknum", stockNum);
            hashMap.put("costprice", costprice);

            CommonFacade.getInstance().exec(Constants.ADD_RENEW_STOCK, hashMap, new ViewCallBack() {
                @Override
                public void onStart() {
                    showLoadingDialog();
                }

                @Override
                public void onSuccess(Object o) throws Exception {
                    Intent intent = new Intent();
//                        intent.putExtra("isDel", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailed(SimpleMsg simleMsg) {
                    showToast(simleMsg);
                }

                @Override
                public void onFinish() {
                    dismissDialog();
                }
            });
        } else if (itemType == 13) {//基金
            hashMap.put("fundcode", fundCode);
            String assetamount;
            if (!StringUtils.isEmpty(et_capital.getText().toString())) {
                assetamount = et_capital.getText().toString();
            } else {
                assetamount = "0";
            }
            hashMap.put("assetamount", assetamount);
            hashMap.put("marketvalue", et_curValue.getText().toString());

            CommonFacade.getInstance().exec(Constants.ADD_FUND_ASSERT, hashMap, new ViewCallBack() {
                @Override
                public void onStart() {
                    showLoadingDialog();
                }

                @Override
                public void onSuccess(Object o) throws Exception {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailed(SimpleMsg simleMsg) {
                    showToast(simleMsg);
                }

                @Override
                public void onFinish() {
                    dismissDialog();
                }
            });
        }

    }

    /**
     * 新建股票（基金）
     */
    private void submit() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assettype", entity.getCategoryid());
        hashMap.put("itemtype", entity.getItemtypeId());
        hashMap.put("itemname", FunName);
        if (itemType == 13) {//基金
            hashMap.put("fundcode", fundCode);
            String assetamount;
            String curValue;
            if (!StringUtils.isEmpty(et_capital.getText().toString())) {
                assetamount = et_capital.getText().toString();
            } else {
                assetamount = "0";
            }

            if (!StringUtils.isEmpty(et_curValue.getText().toString())) {
                curValue = et_curValue.getText().toString();
            } else {
                curValue = "0";
            }
            hashMap.put("assetamount", assetamount);
            hashMap.put("marketvalue", curValue);

            CommonFacade.getInstance().exec(Constants.ADD_FUND_ASSERT, hashMap, new ViewCallBack() {
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
        } else if (itemType == 14) {//股票
            hashMap.put("stockcode", fundCode);
            String stockNum, costprice;
            if (!StringUtils.isEmpty(et_curValue.getText().toString())) {
                stockNum = et_curValue.getText().toString();
            } else {
                stockNum = "0";
            }
            if (!StringUtils.isEmpty(et_capital.getText().toString())) {
                costprice = et_capital.getText().toString();
            } else {
                costprice = "0";
            }
            hashMap.put("stocknum", stockNum);
            hashMap.put("costprice", costprice);

            CommonFacade.getInstance().exec(Constants.ADD_STOCK_ASSERT, hashMap, new ViewCallBack() {
                @Override
                public void onFailed(SimpleMsg simleMsg) {
                    UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
                }

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
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_delet://删除
                showSimpleAlertDialog("", "确定删除该资产么？", "确定", "取消", false, true,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                delect();
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
                if (StringUtils.isEmpty(et_curValue.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入当前持股数", 500);
                } else if (StringUtils.isEmpty(et_capital.getText().toString())) {
                    UIHelper.showShortToast(this, "请输入每股成本", 500);
                } else {
                    upData();
                }
                break;
            case R.id.btn_save://保存
                if (itemType == 13) {
                    if (StringUtils.isEmpty(et_brand.getText().toString())) {
                        UIHelper.showSoftInputMethod(et_brand);
                        UIHelper.showShortToast(this, "基金名称不能为空", 500);
                    } else {
                        submit();
                    }
                } else if (itemType == 14) {
                    if (StringUtils.isEmpty(et_brand.getText().toString())) {
                        UIHelper.showSoftInputMethod(et_brand);
                        UIHelper.showShortToast(this, "股票名称不能为空", 500);
                    } else if (!StringUtils.isEmpty(et_curValue.getText().toString())) {
                        long stockNm = Long.parseLong(et_curValue.getText().toString());
                        if (stockNm >= 100) {
                            submit();
                        } else {
                            UIHelper.showShortToast(this, "持有股票数不能低于100", 500);
                            UIHelper.showSoftInputMethod(et_curValue);
                            return;
                        }
                    } else {
                        submit();
                    }
                }

                break;
            default:
                break;
        }
    }
}
