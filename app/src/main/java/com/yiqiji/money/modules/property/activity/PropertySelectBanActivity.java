package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.BankGridLayout;
import com.yiqiji.money.modules.common.control.BladeView;
import com.yiqiji.money.modules.common.control.PinnedHeaderListView;
import com.yiqiji.money.modules.common.control.YQJSectionIndexer;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.adapter.PropertyBankAdaper;
import com.yiqiji.money.modules.property.adapter.SearchBankAdapter;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.BankEntity;
import com.yiqiji.money.modules.property.entity.PropertyBanEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dansakai on 2017/3/9.
 * 选择银行
 */

public class PropertySelectBanActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;

    private EditText et_search;
    private PinnedHeaderListView pinnedListview;
    private PropertyBankAdaper adapter;
    private BladeView bladeView;
    private ListView listView;//搜索bank
    private SearchBankAdapter searchBankAdapter;

    private List<PropertyBanEntity> searchList = new ArrayList<>();//搜索结果数据源

    private BankGridLayout bangridLayout;

    private AddPropertyItemEntity entity = null;//上个页面

    private BankEntity bankEntity = null;
    private List<PropertyBanEntity> mList = new ArrayList<>();//数据源
    private List<Object> hotList = new ArrayList<>();//热门城市列表

    private View.OnClickListener hotBankClick = null;

    private int itemTypeId;
    // 处理搜索请求
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                String text = msg.obj.toString();
                if (StringUtils.isEmpty(text)) {
                    // 无搜索文案，隐藏搜索列表
                    listView.setVisibility(View.GONE);
                } else {
                    searchList.clear();

                    // 处理搜索
                    for (int i = 0; i < mList.size(); i++) {
                        PropertyBanEntity bank = mList.get(i);
                        if (bank.getBankname().contains(text) || bank.getLetters().contains(text)) {
                            searchList.add(bank);
                        }
                    }
                    if (StringUtils.isEmptyList(searchList)) {
                        listView.setVisibility(View.GONE);
                    } else {
                        searchBankAdapter.notifyDataSetChanged();
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        initView();
        loadData();
    }

    private void initView() {
        entity = (AddPropertyItemEntity) getIntent().getSerializableExtra("addproEntity");
        if (entity != null) {
            itemTypeId = Integer.parseInt(entity.getItemtypeId());
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择银行");
        et_search = (EditText) findViewById(R.id.et_search);
        pinnedListview = (PinnedHeaderListView) findViewById(R.id.pinnedListview);
        pinnedListview.setDivider(null);
        bladeView = (BladeView) findViewById(R.id.bladeView);
        listView = (ListView) findViewById(R.id.listView);

        searchBankAdapter = new SearchBankAdapter(this, searchList, entity);
        listView.setAdapter(searchBankAdapter);

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_banpicker_top, null);
        bangridLayout = (BankGridLayout) headView.findViewById(R.id.bank_head_layout);
        pinnedListview.addHeaderView(headView);

        hotBankClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {//热门银行点击事件
                PropertyBanEntity banEntity = (PropertyBanEntity) v.getTag();
                if (itemTypeId == 6) {
                    Intent intent = new Intent(PropertySelectBanActivity.this, PropertyNewBankActivity.class);
                    entity.setBankid(banEntity.getBankid());
                    entity.setBankicon(banEntity.getBankicon());
                    entity.setBankname(banEntity.getBankname());

                    intent.putExtra("bankEntity", entity);
                    startActivityForResult(intent, 1002);
                } else {
                    Intent intent = new Intent(PropertySelectBanActivity.this, WealthNewAccountActivity.class);
                    entity.setBankid(banEntity.getBankid());
                    entity.setCategoryicon(banEntity.getBankicon());
                    entity.setCategoryname(banEntity.getBankname());

                    intent.putExtra("addproEntity", entity);
                    startActivityForResult(intent, 1002);
                }

            }
        };

        et_search.addTextChangedListener(new TextWatcher() {
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
            }
        });
    }

    /**
     * 获取银行列表
     */
    private void loadData() {

        HashMap<String, String> hashMap = new HashMap<>();
        String typeId = "";
        if (itemTypeId == 6) {//信用卡
            typeId = "2";
        } else if (itemTypeId == 9) {//借记卡
            typeId = "1";
        }
        hashMap.put("type", typeId);
        CommonFacade.getInstance().exec(Constants.GET_BANK_LIST, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog(true);
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                bankEntity = BankEntity.parce(jo_main.optString("data"));
                mList.clear();
                hotList.clear();
                if (bankEntity != null) {
                    mList.addAll(bankEntity.getPropertyBanEntities());
                    hotList.addAll(bankEntity.getHotList());
                }
                bangridLayout.setItems("常用", hotList, hotBankClick, -1);

                List<String> strList = new ArrayList<String>();
                List<Integer> nms = new ArrayList<Integer>();

                String letters = "";
                int num = 0;

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).ishasHead()) {
                        if (i != 0) {
                            nms.add(num);
                            num = 0;
                        }
                        num++;

                        letters = mList.get(i).getLetters();
                        strList.add(letters);
                    } else {
                        num++;
                    }

                    if (i == mList.size() - 1) {
                        nms.add(num);
                    }
                }

                String[] str = new String[strList.size()];

                for (int j1 = 0; j1 < strList.size(); j1++) {
                    str[j1] = strList.get(j1);
                }
                int[] ints = new int[nms.size()];
                for (int j = 0; j < nms.size(); j++) {
                    ints[j] = nms.get(j);
                }
                YQJSectionIndexer indexer = new YQJSectionIndexer(str, ints, 1);
                adapter = new PropertyBankAdaper(PropertySelectBanActivity.this, mList, indexer, entity);
                pinnedListview.setAdapter(adapter);
                pinnedListview.setDispatchPinnedLayer(false);
                pinnedListview.setOnScrollListener(adapter);

                View pinnedHead = LayoutInflater.from(PropertySelectBanActivity.this).inflate(R.layout.activity_bank_item, pinnedListview, false);
                pinnedHead.findViewById(R.id.rl_bankName).setVisibility(View.GONE);

                pinnedListview.setPinnedHeaderView(pinnedHead);

                // 设置左边字母数据
                bladeView.setListView(pinnedListview);
                bladeView.setSectionIndexter(indexer);
                bladeView.setVisibility(View.VISIBLE);

                pinnedListview.setVisibility(View.VISIBLE);
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
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1002) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
