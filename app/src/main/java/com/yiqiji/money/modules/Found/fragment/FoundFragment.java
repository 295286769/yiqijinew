package com.yiqiji.money.modules.Found.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.lee.pullrefresh.utils.UtilPullToRefresh;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.activity.BannerDetailsActivity;
import com.yiqiji.money.modules.Found.adapter.FoundAdapter;
import com.yiqiji.money.modules.Found.adapter.TabHScorllerAdapter;
import com.yiqiji.money.modules.Found.entity.ArticleEntity;
import com.yiqiji.money.modules.Found.entity.BannerAdEntity;
import com.yiqiji.money.modules.Found.entity.TabListModel;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.Found.view.TabHSView;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseOnItemClickListener;
import com.yiqiji.money.modules.community.travel.travelinterface.BaseScorllerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by dansakai on 2017/3/20.
 * 发现首页
 */

public class FoundFragment extends Fragment {
    private LayoutInflater layoutInflater;
    private View view = null;
    private PullToRefreshListView pullToRefreshListView;
    private LinearLayout ll_noData;
    private ListView listView;
    private FoundAdapter foundAdapter;
    private TabHSView tabHSView;
    private View headView;
    private View tabGroupView;
    private BGABanner banner;
    private List<BannerAdEntity> listAds = new ArrayList<>();//头部广告
    private List<ArticleEntity> listArticles = new ArrayList<>();//

    private ImageView iv_back;
    private TextView tv_title;
    private LoadingDialog loadingDialog;//加载数据dialog

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = LayoutInflater.from(getActivity());
        if (view == null) {
            view = layoutInflater.inflate(R.layout.fragment_found, null);
            initView();
            loadData();
        }
        return view;
    }

    /**
     * 请求网络数据
     */
    private void loadData() {
        CommonFacade.getInstance().exec(Constants.FOUND_INDEX, new ViewCallBack() {

            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                loadingDialog.dismiss();
                UtilPullToRefresh.refreshComplete(pullToRefreshListView);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;

                JSONObject jo_data = null;
                List<ArticleEntity> tempLis = null;
                List<BannerAdEntity> temAdLis = null;
                TabListModel tabListModel;
                jo_data = new JSONObject(jo_main.optString("data"));
                tempLis = ArticleEntity.parceLis(jo_data.optString("article"));
                temAdLis = BannerAdEntity.parceLis(jo_data.optString("ads"));
                tabListModel = GsonUtil.GsonToBean(jo_data.optString("tab"), TabListModel.class);

                listAds.clear();
                listArticles.clear();

                if (!StringUtils.isEmptyList(temAdLis)) {
                    listAds.addAll(temAdLis);
                }
                if (!StringUtils.isEmptyList(tempLis)) {
                    listArticles.addAll(tempLis);
                }

                if (listAds.size() <= 1) {
                    banner.setAutoPlayAble(false);
                } else {
                    banner.setAutoPlayAble(true);
                }

                if (StringUtils.isEmptyList(temAdLis) && StringUtils.isEmptyList(tempLis)) {
                    ll_noData.setVisibility(View.VISIBLE);
                } else {
                    ll_noData.setVisibility(View.GONE);
                }

                banner.setData(listAds, null);
                foundAdapter.notifyDataSetChanged();


                if (tabListModel != null && tabListModel.list != null && tabListModel.list.size() > 0) {
                    tabGroupView.setVisibility(View.VISIBLE);
                    initTabViewData(tabListModel);
                } else {
                    tabGroupView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(getActivity(), simleMsg.getErrMsg(), 500);
                if (StringUtils.isEmptyList(listAds) && StringUtils.isEmptyList(listArticles)) {
                    ll_noData.setVisibility(View.VISIBLE);
                } else {
                    ll_noData.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {

        loadingDialog = new LoadingDialog(getActivity(), R.layout.dialog_layout, R.style.DialogTheme);
        loadingDialog.setCancelable(true);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("发现");
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.GONE);

        ll_noData = (LinearLayout) view.findViewById(R.id.ll_noData);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setScrollLoadEnabled(false);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        listView.setDivider(null);

        headView = layoutInflater.inflate(R.layout.include_photo_auto_scroller, null);
        banner = (BGABanner) headView.findViewById(R.id.banner);
        tabGroupView = headView.findViewById(R.id.tab_group_view);

        int width = UIHelper.getDisplayWidth(getActivity());
        banner.getLayoutParams().height = (int) (width / 5 * 2);
        banner.setAdapter(new BGABanner.Adapter() {

            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                // 加载图片
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                XzbUtils.displayImage(((ImageView) view), ((BannerAdEntity) model).getImg(), 0);
            }
        });
        banner.setOnItemClickListener(new BGABanner.OnItemClickListener() {

            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
                BannerAdEntity entity = (BannerAdEntity) model;
                if (BaseWebView.overrideUrlLoading(getActivity(), entity.getUrl())) {
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), BannerDetailsActivity.class);
                    intent.putExtra("AdEntity", entity);
                    startActivity(intent);
                }
            }
        });

        foundAdapter = new FoundAdapter(getActivity(), listArticles);
        listView.addHeaderView(headView);
        listView.setAdapter(foundAdapter);
    }


    private void initTabViewData(TabListModel tabListModel) {
        TextView textView = (TextView) headView.findViewById(R.id.tv_tab_title);
        textView.setText(tabListModel.title);

        tabHSView = (TabHSView) headView.findViewById(R.id.h_scroll_view);
        BaseScorllerAdapter adapter = new TabHScorllerAdapter(getActivity(), tabListModel);
        tabHSView.setAdapter(adapter);
        tabHSView.setOnItemClick(new BaseOnItemClickListener<TabListModel.TabModel>() {
            @Override
            public void onItemClick(TabListModel.TabModel tabModel) {
                if (BaseWebView.overrideUrlLoading(getActivity(), tabModel.url)) {
                    return;
                }
                Intent in = new Intent(getActivity(), WebActivity.class);
                in.putExtra("url", tabModel.url);
                in.putExtra("title", tabModel.title);
                startActivity(in);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
