package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.frame.ui.wigit.BaseTitleLayout;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.DividerItemDecoration;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.homeModule.mybook.adapter.HouseStypeAdapter;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeMulty;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.HouseStypePerecenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 户型和装修方式
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypeActivity extends BaseActivity {
    @BindView(R.id.title_layout)
    BaseTitleLayout titleLayout;//
    @BindView(R.id.list_select)
    BaseRecylerview listSelect;
    private HouseStypeAdapter houseStypeAdapter;
    private String title_content;
    private String content;//选中的item
    private int type = 0;//0户型1装修方式
    private List<HouseStypeMulty> houseStypeMulties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housestype_layout);
        ButterKnife.bind(this);
        getIntentInfo();
        initText();
        initAdapter();
        initDate();
    }

    private void getIntentInfo() {
        type = getIntent().getIntExtra("type", 0);
        content = getIntent().getStringExtra("content");
        if (type == 0) {//户型
            title_content = "户型";
        } else {
            title_content = "装修方式";
        }
    }

    private void initText() {
        titleLayout.setTitle(title_content);
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listSelect.setLayoutManager(linearLayoutManager);
        listSelect.addItemDecoration(new DividerItemDecoration(this, 0));
        houseStypeAdapter = new HouseStypeAdapter(this);
        houseStypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                houseStypeMulties = HouseStypePerecenter.getCheckAfterList(houseStypeMulties, position);
                if (houseStypeMulties != null) {
                    HouseStypeMulty houseStypeMulty = houseStypeMulties.get(position);
                    HouseStypeInfo houseStypeInfo = (HouseStypeInfo) houseStypeMulty.getData();
                    if (houseStypeInfo != null) {
                        boolean isCheck = houseStypeInfo.isSelect();
                        if (!isCheck) {
                            houseStypeInfo.setSelect(true);
                            houseStypeMulty.setData(houseStypeInfo);
                            houseStypeMulties.set(position, houseStypeMulty);
                            houseStypeAdapter.notifyItemChanged(position);
                        }
                    }

                }
                HouseStypePerecenter.getIsCheck(HouseStypeActivity.this, houseStypeMulties.get(position));
            }
        });
        listSelect.setAdapter(houseStypeAdapter);

    }

    private void initDate() {
        houseStypeMulties = HouseStypePerecenter.getHouseStypeMulty(type, content);
        houseStypeAdapter.setDataList(houseStypeMulties);
    }

}
