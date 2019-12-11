package com.yiqiji.money.modules.community.decoration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.community.decoration.adapter.DecorateNearAdapter;
import com.yiqiji.money.modules.community.decoration.model.DecorateNearBookEntity;
import com.yiqiji.money.modules.community.decoration.model.NearBookMultiItem;
import com.yiqiji.money.modules.community.discover.model.TitleModel;
import com.yiqiji.money.modules.community.model.BookCellModel;
import com.yiqiji.money.modules.community.travel.model.DecorateListMultiItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/3.
 * 查看附近共享的装修账本
 */

public class DecorateNearBookActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    private DecorateNearAdapter adaper;
    private List<NearBookMultiItem> mList = new ArrayList<>();

    public static final int REQUEST_CODE = 11002;
    private String requstCity = "上海";
    private String curHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decorate_nearsharebook);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void initView() {
        tvTitle.setText("附近共享装修账本");
        adaper = new DecorateNearAdapter(this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(adaper);
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("city", requstCity);
        CommonFacade.getInstance().exec(Constants.FIND_NEARBY, map, new ViewCallBack<DecorateNearBookEntity>() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(DecorateNearBookEntity decorateNearBookEntity) throws Exception {
                DecorateNearBookEntity.DataBean dataBean = decorateNearBookEntity.getData();
                mList.clear();
                NearBookMultiItem item = null;
                TitleModel titleModle = null;
                BookCellModel cellModle = null;
                if (dataBean != null) {
                    DecorateNearBookEntity.DataBean.DefaultBean defautBean = dataBean.getDefaultX();
                    if (defautBean != null) {
                        item = new NearBookMultiItem();
                        titleModle = new TitleModel();
                        titleModle.type = 0;
                        if ("上海".equals(requstCity)) {
                            titleModle.titleName = defautBean.getTitle();
                        } else {
                            titleModle.titleName = curHouse;
                        }

                        item.setData(titleModle);
                        item.setItemType(NearBookMultiItem.TITLE);
                        mList.add(item);
                        List<BookCellModel> defaultLis = defautBean.getList();
                        item = new NearBookMultiItem();
                        item.setData(defaultLis);
                        item.setItemType(NearBookMultiItem.DEFAUT_BOOKS);
                        mList.add(item);
                    }

                    DecorateNearBookEntity.DataBean.OtherbookBean otherBookbean = dataBean.getOtherbook();
                    if (otherBookbean != null) {
                        item = new NearBookMultiItem();
                        titleModle = new TitleModel();
                        titleModle.type = 1;
                        titleModle.titleName = otherBookbean.getTitle();
                        item.setData(titleModle);
                        item.setItemType(NearBookMultiItem.TITLE);
                        mList.add(item);
                        List<BookCellModel> otherLis = otherBookbean.getList();
                        List<String> imgList = null;

                        if (!StringUtils.isEmptyList(otherLis)) {
                            for (int i = 0; i < otherLis.size(); i++) {
                                item = new NearBookMultiItem();
                                imgList = otherLis.get(i).getImglist();
                                if (StringUtils.isEmptyList(imgList)) {
                                    item.setItemType(NearBookMultiItem.TEXT_NO_IMG);
                                } else if (imgList.size() == 1) {
                                    item.setItemType(NearBookMultiItem.SING_SMALL_IMG);
                                } else {
                                    item.setItemType(NearBookMultiItem.THREE_IMG_HASTITLE);
                                }
                                item.setData(otherLis.get(i));
                                mList.add(item);
                            }
                        }
                    }
                }

                adaper.setDataList(mList);
                adaper.notifyDataSetChanged();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            requstCity = data.getStringExtra("city");
            curHouse = data.getStringExtra("houseName");
            loadData();
        }
    }
}
