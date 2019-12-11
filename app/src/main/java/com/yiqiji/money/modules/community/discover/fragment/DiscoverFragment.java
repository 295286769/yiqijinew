package com.yiqiji.money.modules.community.discover.fragment;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.community.discover.adapter.DiscoverListAdapter;
import com.yiqiji.money.modules.community.discover.manager.DiscoverListController;
import com.yiqiji.money.modules.community.discover.manager.DiscoverListDataAssembler;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.view.HeadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverFragment extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.rl_view_title)
    View rlViewTitle;
    @BindView(R.id.ryv_discover)
    RecyclerView ryvDiscover;


    HeadView headView;
    DiscoverListAdapter discoverListAdapter;
    LoadingDialog loadingDialog;
    DiscoverListController discoverListController = new DiscoverListController();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_discover_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(getActivity());
        initRecycleView();
        initEvent();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initRecycleView() {
        headView = new HeadView(getActivity());
        discoverListAdapter = new DiscoverListAdapter(getActivity());
        discoverListAdapter.addHeaderView(headView);
        ryvDiscover.setLayoutManager(new LinearLayoutManager(getContext()));
        ryvDiscover.setAdapter(discoverListAdapter);
    }

    private void initEvent() {


    }

    private void loadData() {
        loadingDialog.show();
        discoverListController.getDiscoverListData(new ViewCallBack<DiscoverListModel>() {
            @Override
            public void onSuccess(DiscoverListModel discoverListModel) throws Exception {
                super.onSuccess(discoverListModel);
                headView.setViewData(discoverListModel);
                discoverListAdapter.setDataList(DiscoverListDataAssembler.getAssemblerMultipItem(discoverListModel));
                loadingDialog.dismiss();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                loadingDialog.dismiss();
                ToastUtils.DiyToast(getActivity(), simleMsg.getErrMsg());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
