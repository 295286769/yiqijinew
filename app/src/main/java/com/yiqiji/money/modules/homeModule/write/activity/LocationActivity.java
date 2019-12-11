package com.yiqiji.money.modules.homeModule.write.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.base.SelectLocationAdapter;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.Location;
import com.yiqiji.money.modules.common.entity.Suggestion;
import com.yiqiji.money.modules.common.entity.eventbean.SuggestionItem;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.LocationServer;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.write.adapter.LocationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.yiqiji.frame.core.config.LoginConfig.getInstance;

/**
 * Created by Administrator on 2017/3/20.
 */
public class LocationActivity extends BaseActivity {

    private ApiService apiService;
    private LocationServer locationServer;
    private Location data = new Location();

    private ListView listView1;
    private ListView listView2;
    private LocationAdapter adapter;
//    private MyTitleLayout my_title;

    private RelativeLayout rl_location_title;
    private TextView tv_cancel;
    private EditText et_input;

    private String address;
    private String location;
    private String region;

    private List<Suggestion.DataBean> data2 = new ArrayList<>();
    private SelectLocationAdapter adapter2;
    private LoadingDialog progressDlg;

    private View view_location_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getLocation();
        address = getIntent().getStringExtra("address");
        initView();
        initTitle("所在位置");
        apiService = RetrofitInstance.get().create(ApiService.class);
        View view = getLayoutInflater().inflate(R.layout.item_selected, null);
        adapter = new LocationAdapter(LocationActivity.this, data);

        listView1.addHeaderView(view);
        listView1.setAdapter(adapter);


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name;
                if (position == 1) {
                    name = "不显示位置";
                } else {
                    name = data.getData().getPois().get(position - 2).getName();
                }
                Intent intent = new Intent(LocationActivity.this, DetailsActivity.class);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        view.findViewById(R.id.tv_item_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTiltgone(View.GONE);
                rl_location_title.setVisibility(View.VISIBLE);
                view_location_title.setVisibility(View.VISIBLE);
                setTiltgone(View.GONE);
                listView1.setVisibility(View.GONE);
                imm.showSoftInput(et_input, InputMethodManager.SHOW_FORCED);
                listView2.setVisibility(View.VISIBLE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                setTiltgone(View.VISIBLE);
                rl_location_title.setVisibility(View.GONE);
                view_location_title.setVisibility(View.GONE);
                setTiltgone(View.VISIBLE);
                listView1.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);
                et_input.setText("");
            }
        });

        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    String keytag = et_input.getText().toString().trim();

                    if (TextUtils.isEmpty(keytag)) {
                        Toast.makeText(LocationActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                    getSuggestionn(keytag, region, location, "true");
                    // 搜索功能主体

                    return true;
                }
                return false;
            }
        });


        adapter2 = new SelectLocationAdapter(LocationActivity.this, data2);
        listView2.setAdapter(adapter2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter2.getData(position);
                Intent intent = new Intent(LocationActivity.this, DetailsActivity.class);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getLocation() {
        EventBus.getDefault().register(this);
        locationServer = MyApplicaction.getInstence().getLocationServer();
        locationServer.start();

        showLoadingDialog(true);
    }

    InputMethodManager imm;

    private void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        listView1 = (ListView) findViewById(R.id.lv_location_listView1);
        listView2 = (ListView) findViewById(R.id.lv_location_listView2);
        rl_location_title = (RelativeLayout) findViewById(R.id.rl_location_title);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        et_input = (EditText) findViewById(R.id.et_input);
        view_location_title = findViewById(R.id.view_location_title);
    }


    public void getSuggestionn(String query, String region, String location, String citylimit) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("query", query);
        hashMap.put("region", region);
        hashMap.put("location", location);
        hashMap.put("citylimit", citylimit);

        CommonFacade.getInstance().exec(Constants.SUGGESTION, hashMap, new ViewCallBack<Suggestion>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Suggestion suggestion) throws Exception {
                super.onSuccess(suggestion);
                if (!StringUtils.isEmptyList(suggestion.getData())) {
                    adapter2.setData(suggestion.getData());
                } else {
                    //不处理
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//
//        String[] keys = new String[]{"query", "region", "location", "citylimit"};
//        String[] values = new String[]{query, region, location, "true"};
//
//        HashMap<String, String> stringHashMap = StringUtils.getParamas(keys, values);
//        apiService.getSuggestionn(stringHashMap).enqueue(new BaseCallBack<Suggestion>(this, true) {
//            @Override
//            public void onResponse(Response<Suggestion> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                Suggestion suggestion = arg0.body();
//                if (!StringUtils.isEmptyList(suggestion.getData())) {
//                    adapter2.setData(suggestion.getData());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//
//            }
//        });

    }


    public void getLocationList(String location, String callback, String pois) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("location", location);
        hashMap.put("callback", callback);
        hashMap.put("pois", pois);


        CommonFacade.getInstance().exec(Constants.LOCATIONURL, hashMap, new ViewCallBack<Location>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Location location) throws Exception {
                super.onSuccess(location);
                data = location;
                if (!StringUtils.isEmptyList(data.getData().getPois()) && location != null) {
                    adapter.setData(data, address);
                }
                if (progressDlg != null && progressDlg.isShowing()) {
                    progressDlg.dismiss();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//        String[] keys = new String[]{"location", "callback", "pois"};
//        String[] values = new String[]{location, "renderReverse", "1"};
//
//        HashMap<String, String> stringHashMap = StringUtils.getParamas(keys, values);
//        apiService.getLocation(stringHashMap).enqueue(new BaseCallBack<Location>(LocationActivity.this, false) {
//            @Override
//            public void onResponse(Response<Location> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                data = arg0.body();
//                if (data != null) {
//                    adapter.setData(data, address);
//                }
//                if (progressDlg != null && progressDlg.isShowing()) {
//                    progressDlg.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//                if (progressDlg != null && progressDlg.isShowing()) {
//                    progressDlg.dismiss();
//                }
//            }
//        });
    }

    public void onEventMainThread(SuggestionItem suggestionItem) {
        locationServer.stop();
        location = suggestionItem.getLocation().getLat() + "," + suggestionItem.getLocation().getLng();
        region = suggestionItem.getCity();
        Log.i("LOG", "------------location" + location);
        getInstance().setLocationCity(region);
        getInstance().setLatitudeAndLongitude(location);
        getLocationList(location, "renderReverse", "1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationServer.stop();
    }
}
