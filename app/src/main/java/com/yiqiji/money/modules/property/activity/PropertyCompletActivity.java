package com.yiqiji.money.modules.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.adapter.PropertyCompletAdapter;
import com.yiqiji.money.modules.property.entity.PropertyEntity;
import com.yiqiji.money.modules.property.entity.PropertyItemEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dansakai on 2017/3/13.
 * 已完结项目
 */

public class PropertyCompletActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private ListView listView;
    private PropertyCompletAdapter adapter;
    private List<PropertyItemEntity> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_complet);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("已完结项目");
        listView = (ListView) findViewById(R.id.listView);
        list = (List<PropertyItemEntity>) getIntent().getSerializableExtra("compList");
        adapter = new PropertyCompletAdapter(this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PropertyItemEntity entity = list.get(position);
                int itemType = Integer.parseInt(entity.getItemtype());
                Intent intent = null;
                if (itemType == 19 || itemType == 20 || itemType == 21) {
                    intent = new Intent(PropertyCompletActivity.this, PropertyDetailActivity.class);
                } else {
                    intent = new Intent(PropertyCompletActivity.this, PropertyNetDetailActivity.class);
                }
                intent.putExtra("assetid", entity.getAssetid());
                intent.putExtra("itemtype", entity.getItemtype());
                intent.putExtra("isComple", true);
                startActivityForResult(intent, 1002);//跳转到资产详情页
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showSimpleAlertDialog("", "确定删除该资产吗？", "确定", "取消", false, true,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                                delect(Integer.parseInt(list.get(position).getAssetid()));
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });
                return true;
            }
        });
    }

    /**
     * 删除账户
     */
    private void delect(final int assetid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(assetid));
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
                Iterator<PropertyItemEntity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    PropertyItemEntity entity = iterator.next();
                    if (entity.getType() == PropertyEntity.TYPE_SECOND) {
                        if (assetid == Integer.parseInt(entity.getAssetid())) {
                            iterator.remove();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
}
