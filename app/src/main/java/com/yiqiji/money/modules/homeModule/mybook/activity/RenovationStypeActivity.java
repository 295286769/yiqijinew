package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.decoration.GridSpacingItemDecoration;
import com.yiqiji.frame.ui.wigit.BaseTitleLayout;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.AddressSelctActivity;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.homeModule.mybook.adapter.RenovationStypeAdapter;
import com.yiqiji.money.modules.homeModule.mybook.entity.RenovationStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.HouseStypePerecenter;
import com.yiqiji.money.modules.homeModule.mybook.util.MyBookActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 风格
 * Created by ${huangweishui} on 2017/8/2.
 * address huang.weishui@71dai.com
 */
public class RenovationStypeActivity extends BaseActivity {
    @BindView(R.id.title_renovation)
    BaseTitleLayout titleRenovation;
    @BindView(R.id.list_renovation)
    BaseRecylerview listRenovation;
    private RenovationStypeAdapter renovationStypeAdapter;
    private Unbinder unbinder;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renovation_stype_layout);
        unbinder = ButterKnife.bind(this);
        getIntentInfo();
        initThisTitle();
        initAdapter();
        initData();
    }

    private void getIntentInfo() {
        content = getIntent().getStringExtra("content");
    }

    private void initThisTitle() {
        titleRenovation.setTitle("选择装修风格");
    }


    private void initAdapter() {
        renovationStypeAdapter = new RenovationStypeAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        listRenovation.setLayoutManager(gridLayoutManager);
        listRenovation.addItemDecoration(new GridSpacingItemDecoration(4, UIHelper.dip2px(this, 20), true));
        listRenovation.setAdapter(renovationStypeAdapter);
        renovationStypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RenovationStypeInfo stypeInfo = (RenovationStypeInfo) adapter.getData().get(position);
                Intent intent = new Intent(RenovationStypeActivity.this, AddressSelctActivity.class);
                intent.putExtra("content", stypeInfo.getContent());
                setResult(MyBookActivityUtil.DECORATE_RESULTCODE, intent);
                finish();
            }
        });


    }

    private void initData() {
        renovationStypeAdapter.setDataList(HouseStypePerecenter.getRenovationStypeInfo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
