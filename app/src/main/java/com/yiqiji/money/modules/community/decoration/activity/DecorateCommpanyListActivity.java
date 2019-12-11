package com.yiqiji.money.modules.community.decoration.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.community.decoration.adapter.CommpanyListAdapter;
import com.yiqiji.money.modules.community.decoration.model.CommpanyListEntity;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/2.
 * 装修公司列表页
 */

public class DecorateCommpanyListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private CommpanyListAdapter adapter;
    private List<DecorationcompanyBean> mList = new ArrayList<>();

    private static final int PAGE_SIZE = 20;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commpany_list);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void initView() {
        tvTitle.setText("装修公司");
        adapter = new CommpanyListAdapter();
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, recylerView);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(adapter);
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(pageIndex));
        map.put("size", String.valueOf(PAGE_SIZE));
        CommonFacade.getInstance().exec(Constants.COMMPANY_LIST, map, new ViewCallBack<CommpanyListEntity>() {
            @Override
            public void onStart() {
                if (pageIndex == 1) {
                    showLoadingDialog();
                }
            }

            @Override
            public void onSuccess(CommpanyListEntity commpanyListEntity) throws Exception {
                CommpanyListEntity.DataBean dataBean = commpanyListEntity.getData();
                if (dataBean != null) {
                    List<DecorationcompanyBean> companyLis = dataBean.getCompany();
                    if (pageIndex == 1) {
                        mList.clear();
                    } else {
                        adapter.loadMoreComplete();
                    }
                    if (!StringUtils.isEmptyList(companyLis)) {
                        mList.addAll(companyLis);
                    } else {
//                        adapter.loadMoreComplete();
                    }

                    adapter.setDataList(mList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg.getErrMsg());
                adapter.loadMoreComplete();
            }

            @Override
            public void onFinish() {
                if (pageIndex == 1) {
                    dismissDialog();
                }
            }
        });
    }


    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        loadData();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
