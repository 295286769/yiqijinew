package com.yiqiji.money.modules.community.decoration.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.community.decoration.adapter.CommpanyDetailAdapter;
import com.yiqiji.money.modules.community.decoration.model.CommpanyDetailEntity;
import com.yiqiji.money.modules.community.decoration.view.CommpanyHeadView;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/2.
 * 装修公司详情页
 */

public class DecorateCommpanyDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.iv_noData)
    ImageView ivNoData;

    private String commpanyId;

    private CommpanyDetailAdapter adapter;
    private List<BookCellModel> mList = new ArrayList<>();
    private CommpanyHeadView headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commpany_detail);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void initView() {
        tvTitle.setText("公司详情");
        commpanyId = getIntent().getStringExtra("commpanyId");
        headView = new CommpanyHeadView(this);
        headView.setVisibility(View.GONE);

        adapter = new CommpanyDetailAdapter();
        adapter.addHeaderView(headView);
        recylerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recylerView.setAdapter(adapter);
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", commpanyId);
        CommonFacade.getInstance().exec(Constants.COMMPANY_DETAIL, map, new ViewCallBack<CommpanyDetailEntity>() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(CommpanyDetailEntity commpanyDetailEntity) throws Exception {
                headView.setVisibility(View.VISIBLE);
                CommpanyDetailEntity.DataBean dataBean = commpanyDetailEntity.getData();
                if (dataBean != null) {
                    headView.setData(dataBean.getCompany());
                    CommpanyDetailEntity.DataBean.RelatedBean relatedBean = dataBean.getRelated();
                    List<BookCellModel> tempLis = relatedBean.getList();
                    mList.clear();
                    if (!StringUtils.isEmptyList(tempLis)) {
                        headView.setBookTitle(relatedBean.getTitle() + " (" + tempLis.size() + ")");
                        mList.addAll(tempLis);
                    }

                    if (StringUtils.isEmptyList(mList)) {
                        ivNoData.setVisibility(View.VISIBLE);
                    } else {
                        ivNoData.setVisibility(View.GONE);
                    }
                    adapter.setDataList(mList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
