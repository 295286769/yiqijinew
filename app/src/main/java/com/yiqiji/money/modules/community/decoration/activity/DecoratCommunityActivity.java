package com.yiqiji.money.modules.community.decoration.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.community.decoration.adapter.DecorateListAdapter;
import com.yiqiji.money.modules.community.travel.model.DecorateHomeEntity;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;
import com.yiqiji.money.modules.community.decoration.model.DecorationcompanyBean;
import com.yiqiji.money.modules.community.decoration.view.DecorateHeadView;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.model.BookCellModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/7/28.
 * 装修社区首页
 * DecoratCommunityAdapter
 */

public class DecoratCommunityActivity extends BaseActivity {

    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private DecorateListAdapter adapter;
    private DecorateHeadView headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_decoration);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        CommonFacade.getInstance().exec(Constants.FIND_HOUSE, new ViewCallBack<DecorateHomeEntity>() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(DecorateHomeEntity decorateHomeEntity) throws Exception {
                headView.setVisibility(View.VISIBLE);
                List<DecorateListMultiItem> decorateListMultiItemList = new ArrayList<>();
                DecorateListMultiItem multiItem = null;
                DecorateHomeEntity.DataBean dataBean = decorateHomeEntity.getData();
                headView.setData(String.valueOf(dataBean.getBooktotal()), dataBean.getDefaultcity(), String.valueOf(dataBean.getCompanytotal()));
                //热门共享账本
                DecorateHomeEntity.DataBean.HotbookBean hotbookBean = dataBean.getHotbook();
                if (hotbookBean != null) {
                    multiItem = new DecorateListMultiItem();
                    TitleModel titleModel = new TitleModel();
                    titleModel.type = 0;
                    titleModel.titleName = hotbookBean.getTitle();
                    multiItem.setData(titleModel);
                    multiItem.setItemType(DecorateListMultiItem.TITLE);
                    decorateListMultiItemList.add(multiItem);
                    List<BookCellModel> hotBookLis = hotbookBean.getList();
                    if (!StringUtils.isEmptyList(hotBookLis)) {
                        multiItem = new DecorateListMultiItem();
                        multiItem.setItemType(DecorateListMultiItem.SING_BIG_IMG);
                        multiItem.setData(hotBookLis.get(0));
                        decorateListMultiItemList.add(multiItem);
                        List<BookCellModel> tempHotBookLis = hotBookLis.subList(1, hotBookLis.size());
                        multiItem = new DecorateListMultiItem();
                        multiItem.setItemType(DecorateListMultiItem.THREE_IMG);
                        multiItem.setData(tempHotBookLis);
                        decorateListMultiItemList.add(multiItem);
                    }
                }
                //装修公司
                List<DecorationcompanyBean> decorationcompanyBeanList = dataBean.getDecorationcompany();
                if (!StringUtils.isEmptyList(decorationcompanyBeanList)) {
                    multiItem = new DecorateListMultiItem();
                    TitleModel titleModel = new TitleModel();
                    titleModel.type = 1;
                    titleModel.titleName = "装修公司";
                    multiItem.setData(titleModel);
                    multiItem.setItemType(DecorateListMultiItem.TITLE);
                    decorateListMultiItemList.add(multiItem);
                    for (int i = 0; i < decorationcompanyBeanList.size(); i++) {
                        multiItem = new DecorateListMultiItem();
                        multiItem.setItemType(DecorateListMultiItem.DECORAT_SINGLE_IMG);
                        multiItem.setData(decorationcompanyBeanList.get(i));
                        decorateListMultiItemList.add(multiItem);
                    }

                }
                //精选推荐
                DecorateHomeEntity.DataBean.OtherbookBean otherbookBean = dataBean.getOtherbook();
                if (otherbookBean != null) {
                    multiItem = new DecorateListMultiItem();
                    TitleModel titleModel = new TitleModel();
                    titleModel.type = 2;
                    titleModel.titleName = otherbookBean.getTitle();
                    multiItem.setData(titleModel);
                    multiItem.setItemType(DecorateListMultiItem.TITLE);
                    decorateListMultiItemList.add(multiItem);
                    List<BookCellModel> otherbooklis = otherbookBean.getList();
                    List<String> imgList = null;
                    if (!StringUtils.isEmptyList(otherbooklis)) {
                        for (int i = 0; i < otherbooklis.size(); i++) {
                            multiItem = new DecorateListMultiItem();
                            imgList = otherbooklis.get(i).getImglist();
                            if (StringUtils.isEmptyList(imgList)) {
                                multiItem.setItemType(DecorateListMultiItem.TEXT_NO_IMG);
                            } else if (imgList.size() == 1) {
                                multiItem.setItemType(DecorateListMultiItem.SING_SMALL_IMG);
                            } else {
                                multiItem.setItemType(DecorateListMultiItem.THREE_IMG_HASTITLE);
                            }
                            multiItem.setData(otherbooklis.get(i));
                            decorateListMultiItemList.add(multiItem);
                        }
                    }
                }

                adapter.setDataList(decorateListMultiItemList);
                adapter.notifyDataSetChanged();
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

    private void initView() {
        tvTitle.setText("装修社区");
        adapter = new DecorateListAdapter(this);
        headView = new DecorateHeadView(this);
        headView.setVisibility(View.GONE);
        adapter.addHeaderView(headView);
        recylerView.setLayoutManager(new LinearLayoutManager(mContext));
        recylerView.setAdapter(adapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
