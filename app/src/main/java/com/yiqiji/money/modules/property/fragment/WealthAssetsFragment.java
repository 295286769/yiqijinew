package com.yiqiji.money.modules.property.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomBase;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.adapter.MergeAdapter;
import com.yiqiji.money.modules.common.control.CustomAlertDialog;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.activity.PropertyCompletActivity;
import com.yiqiji.money.modules.property.adapter.PropertyAdapter;
import com.yiqiji.money.modules.property.entity.PropertyEntity;
import com.yiqiji.money.modules.property.entity.PropertyItemEntity;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 资产
 */
public class WealthAssetsFragment extends Fragment implements View.OnClickListener, PullToZoomBase.OnPullZoomListener {
    private View view;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private int scrollValue;
    private CustomAlertDialog alertDialog = null;

    private ImageView iv_eye;
    private TextView tv_totle_money;
    private TextView tv_profit;
    private View footView;//底部已完结
    private View v_divider;//底部完结分割线
    private TextView tv_title;
    private TextView tv_money;
    private ImageView right_arrow;
    private ImageView no_data;//没有添加资产展示

    private LoadingDialog loadingDialog;//加载数据dialog

    private PullToZoomListViewEx mListView;
    private PropertyAdapter propertyAdapter;
    private List<Integer> sourceIds = new ArrayList<>();
    private PropertyEntity propertyEntity;//资产组合类
    private List<PropertyItemEntity> propertyList = new ArrayList<>();//列表数据源

    public boolean ishideMoney = false;
    private String tatalMoney = "0.00";
    private String profitMoney = "0.00";

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        layoutInflater = LayoutInflater.from(mContext);
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wealth_assets, null);
            initView();
        }
        loadData();
        return view;
    }

    private void initView() {
        loadingDialog = new LoadingDialog(getActivity(), R.layout.dialog_layout, R.style.DialogTheme);
        loadingDialog.setCancelable(true);
        alertDialog = new CustomAlertDialog(mContext);
        ishideMoney = (boolean) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "hideMoney", false);
        mListView = (PullToZoomListViewEx) view.findViewById(R.id.mPullToZoomListViewEx);
        mListView.setOnPullZoomListener(this);
        View head_view = new View(mContext);
        head_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageResource(R.color.main_back);
        mListView.setHeaderView(head_view, img);
        footView = LayoutInflater.from(mContext).inflate(R.layout.fragment_property_item_first, null);
        footView.setVisibility(View.GONE);
        v_divider = footView.findViewById(R.id.v_divider);
        tv_title = (TextView) footView.findViewById(R.id.tv_title);
        tv_money = (TextView) footView.findViewById(R.id.tv_money);
        tv_money.setVisibility(View.GONE);
        right_arrow = (ImageView) footView.findViewById(R.id.right_arrow);
        right_arrow.setVisibility(View.VISIBLE);

        // 设置头部布局params
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1);
        mListView.setHeaderLayoutParams(localObject);
        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        MergeAdapter adaper = new MergeAdapter();
        View headView = layoutInflater.inflate(R.layout.include_property_head, null);
        iv_eye = (ImageView) headView.findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        if (ishideMoney) {
            iv_eye.setImageResource(R.drawable.icon_closeeye);
        } else {
            iv_eye.setImageResource(R.drawable.icon_openeye);
        }
        no_data = (ImageView) headView.findViewById(R.id.no_data);
        tv_totle_money = (TextView) headView.findViewById(R.id.tv_totle_money);
        tv_profit = (TextView) headView.findViewById(R.id.tv_profit);

        sourceIds.add(R.layout.fragment_property_item_first);
        sourceIds.add(R.layout.fragment_property_item_second);

        propertyAdapter = new PropertyAdapter(mContext, propertyList, sourceIds, this);
        adaper.addView(headView, true);
        adaper.addAdapter(propertyAdapter);
        adaper.addView(footView, true);
        mListView.setAdapter(adaper);

        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCompletList();
            }
        });
    }


    /**
     * 跳转到已完成项目列表
     */
    private void skipCompletList() {
        Intent intent = new Intent(mContext, PropertyCompletActivity.class);
        intent.putExtra("compList", (Serializable) propertyEntity.getComEntitise());
        startActivityForResult(intent, 1002);
    }

    /**
     * 删除
     *
     * @param assetid
     */
    public void delectCounter(final int assetid) {
        alertDialog.reset()
                .setMessage("确定删除该资产吗？")
                .setRightButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        alertDialog.dismiss();
                        delect(assetid);
                    }
                })
                .setLeftButton("取消", null)
                .show();
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
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                Iterator<PropertyItemEntity> iterator = propertyList.iterator();
                while (iterator.hasNext()) {
                    PropertyItemEntity entity = iterator.next();
                    if (entity.getType() == PropertyEntity.TYPE_SECOND) {
                        if (assetid == Integer.parseInt(entity.getAssetid())) {
                            iterator.remove();
                            for (int i = 0; i < propertyList.size(); i++) {
                                if (propertyList.get(i).getType() == PropertyEntity.TYPE_FIRST) {
                                    if (propertyList.get(i).getCategoryid() == entity.getCategoryid()) {
                                        int childNm = propertyList.get(i).getChildNm() - 1;
                                        propertyList.get(i).setChildNm(childNm);
                                        propertyList.get(i).setCategoryname(propertyList.get(i).getCategory() + "(" + childNm + ")");
                                    }
                                }
                            }
                        }
                    }
                }
                Iterator<PropertyItemEntity> iterat = propertyList.iterator();
                while (iterat.hasNext()) {
                    PropertyItemEntity entit = iterat.next();
                    if (entit.getType() == PropertyEntity.TYPE_FIRST) {
                        if (entit.getChildNm() == 0) {
                            iterat.remove();
                        }
                    }
                }
                if (!StringUtils.isEmptyList(propertyList) || !StringUtils.isEmptyList(propertyEntity.getComEntitise())) {
                    no_data.setVisibility(View.GONE);
                } else {
                    no_data.setVisibility(View.VISIBLE);
                }
                propertyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                UIHelper.showShortToast(mContext, simleMsg.getErrMsg(), 500);
            }
        });
    }

    /**
     * 获取资产首页数据
     */
    public void loadData() {

        CommonFacade.getInstance().exec(Constants.ASSERT, new ViewCallBack() {
            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onFinish() {
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                List<PropertyItemEntity> temLis = new ArrayList<>();
                JSONObject jo_sumary = new JSONObject(jo_main.optString("summary"));
                tatalMoney = jo_sumary.optString("totalamount");
                profitMoney = jo_sumary.optString("todayamount");
                if (ishideMoney) {
                    tv_totle_money.setText("******");
                    tv_profit.setText("******");
                } else {
                    tv_totle_money.setText(StringUtils.moneySplitComma(tatalMoney));
                    if ("0.00".equals(profitMoney)) {
                        tv_profit.setText("0.00");
                    } else {
                        tv_profit.setText(StringUtils.moneySplitComma(profitMoney));
                        tv_profit.setTextColor(Color.parseColor("#ffffff"));
//                        if (Double.parseDouble(profitMoney) < 0) {
//                            tv_profit.setTextColor(Color.parseColor("#56B68C"));
//                        } else {
//                            tv_profit.setTextColor(Color.parseColor("#E26D64"));
//                        }

                    }

                }

                propertyEntity = PropertyEntity.parse(jo_main.optString("assetlist"));
                if (propertyEntity != null) {
                    temLis = propertyEntity.getPropertyItemEntities();
                    if (StringUtils.isEmptyList(propertyEntity.getComEntitise())) {
                        footView.setVisibility(View.GONE);
                    } else {
                        footView.setVisibility(View.VISIBLE);
                        tv_title.setText(propertyEntity.getCompleItem());
                    }

                    if (StringUtils.isEmptyList(temLis) && StringUtils.isEmptyList(propertyEntity.getComEntitise())) {
                        no_data.setVisibility(View.VISIBLE);
                    } else {
                        no_data.setVisibility(View.GONE);
                        if (StringUtils.isEmptyList(temLis) && !StringUtils.isEmptyList(propertyEntity.getComEntitise())) {
                            v_divider.setVisibility(View.GONE);
                        } else {
                            v_divider.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    no_data.setVisibility(View.VISIBLE);
                }
                propertyList.clear();
                if (!StringUtils.isEmptyList(temLis)) {
                    propertyList.addAll(temLis);
                } else {
                    //暂无数据
                }

                propertyAdapter.notifyDataSetChanged();
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
            case R.id.iv_eye://显示隐藏金额
                String key = LoginConfig.getInstance().getUserid() + "hideMoney";
                SPUtils.setParam(key, !ishideMoney);
                ishideMoney = !ishideMoney;
                if (ishideMoney) {
                    XzbUtils.hidePointInUmg(mContext,Constants.HIDE_HINE_MONEY);
                    iv_eye.setImageResource(R.drawable.icon_closeeye);
                    tv_totle_money.setText("******");
                    tv_profit.setText("******");
                } else {
                    XzbUtils.hidePointInUmg(mContext,Constants.HIDE_SHOW_MONEY);
                    tv_totle_money.setText(StringUtils.moneySplitComma(tatalMoney));
                    if ("0.00".equals(profitMoney)) {
                        tv_profit.setText("0.00");
                    } else {
                        tv_profit.setText(StringUtils.moneySplitComma(profitMoney));
                        tv_profit.setTextColor(Color.parseColor("#ffffff"));
//                        if (Double.parseDouble(profitMoney) < 0) {
//                            tv_profit.setTextColor(Color.parseColor("#56B68C"));
//                        } else {
//                            tv_profit.setTextColor(Color.parseColor("#E26D64"));
//                        }

                    }
                    iv_eye.setImageResource(R.drawable.icon_openeye);
                }
                propertyAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
        if (requestCode == 1002) {
            loadData();
        }
    }

    @Override
    public void onPullZooming(int newScrollValue) {
        scrollValue = newScrollValue;
    }

    @Override
    public void onPullZoomEnd() {
        if (scrollValue > -280) {//下拉距离小于280像素刷新
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
