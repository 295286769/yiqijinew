package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.utils.DBManager;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.deviceUtil;
import com.yiqiji.money.modules.property.adapter.SearchFundAdapter;
import com.yiqiji.money.modules.property.entity.FundEntity;
import com.yiqiji.money.modules.property.entity.SearchEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dansakai on 2017/3/13.
 */

public class PropertySearchStockActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_cancel;
    private EditText et_input;
    private ListView listView;
    private SearchFundAdapter adapter;
    private List<FundEntity> list = new ArrayList<>();

    private SearchEntity searchEntity;
    private ApiService service;

    private DBManager db = null;
    private List<String> lisCode = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    search(msg.obj.toString());
                    break;
                default:
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stock);
//        StatusBarCompat.setStatusBarColor(this, mContext.getResources().getColor(R.color.title_back_color));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gp.baidu.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
        initView();
        db = new DBManager(this);
        if (!StringUtils.isEmptyList(db.query())) {
            for (FundEntity enti : db.query()) {
                lisCode.add(enti.getCode());
            }
        }


    }

    private void initView() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        et_input = (EditText) findViewById(R.id.et_input);

        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    UIHelper.hideSoftInputMethod(et_input);
                }
                return false;
            }
        });

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入改变处理
                handler.removeMessages(100);// 先清空消息
                Message msg = Message.obtain();
                msg.what = 100;
                msg.obj = s.toString();// 输入的文案
                handler.sendMessageDelayed(msg, 500);
                listView.setVisibility(View.VISIBLE);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        adapter = new SearchFundAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PropertySearchStockActivity.this, StockDetailActivity.class);
                intent.putExtra("fundEntity", list.get(position));
                intent.putExtra("isOption", true);
                startActivity(intent);
            }
        });
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
                                searchEntity = SearchEntity.parceSeaList(jo_data.optString("stock_data"));
                            }
                            list.clear();
                            if (searchEntity != null) {
                                if (!StringUtils.isEmptyList(searchEntity.getSeaLis())) {
                                    list.addAll(searchEntity.getSeaLis());
                                }
                            }
                            if (!StringUtils.isEmptyList(list) && !StringUtils.isEmptyList(lisCode)) {
                                for (FundEntity en : list) {
                                    if (lisCode.contains(en.getCode())) {
                                        en.setAttention(true);
                                    }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                saveData();
                finish();
                break;
        }
    }

    /**
     * 保存已关注的数据
     */
    private void saveData() {
        if (!StringUtils.isEmptyList(list)) {

            List<FundEntity> tempLis = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isAttention()) {
                    tempLis.add(list.get(i));
                }
            }
            if (!StringUtils.isEmptyList(tempLis)) {
                DBManager manager = new DBManager(this);
                manager.add(tempLis);
            }
        }
    }
}
