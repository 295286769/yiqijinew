package com.yiqiji.money.modules.property.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.lee.pullrefresh.utils.UtilPullToRefresh;
import com.squareup.okhttp.ResponseBody;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.adapter.MergeAdapter;
import com.yiqiji.money.modules.common.utils.DBManager;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.property.activity.PropertySearchStockActivity;
import com.yiqiji.money.modules.property.adapter.OptionalAdapter;
import com.yiqiji.money.modules.property.entity.FundEntity;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.frame.core.utils.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WealthOptionalFragment extends Fragment {
    private View view;

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private MergeAdapter adapter;
    private OptionalAdapter optionAdapter;

    private LinearLayout ll_fundData;
    private TextView tv_pricefir;
    private TextView tv_upfir;
    private TextView tv_upfirDegr;
    private TextView tv_namefir;
    private TextView tv_priceSec;
    private TextView tv_upSec;
    private TextView tv_upSecDegr;
    private TextView tv_nameSec;
    private TextView tv_priceTir;
    private TextView tv_upTir;
    private TextView tv_upTirDegr;
    private TextView tv_nameTir;

    private ImageView iv_add;
    private LinearLayout ll_add;

    private RelativeLayout rl_baner;
    private ApiService service;
    private List<FundEntity> entities = new ArrayList<>();

    private List<FundEntity> cacheList = new ArrayList<>();

    private DBManager db;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wealth_optional, null);
            initView();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://hq.sinajs.cn")
                    .build();

            service = retrofit.create(ApiService.class);
            loadData();
            loadFundegr();
        }
        return view;
    }

    /**
     * 通过本地存储的code加载价格和涨幅信息
     */
    private void loadFundegr() {
        db = new DBManager(getActivity());
        cacheList = db.query();
        if (StringUtils.isEmptyList(cacheList)) {
            entities.clear();
            optionAdapter.notifyDataSetChanged();
            ll_add.setVisibility(View.VISIBLE);
            rl_baner.setVisibility(View.GONE);
            return;
        }

        String reqStr = "";
        for (int i = 0; i < cacheList.size(); i++) {
            if (i == cacheList.size() - 1) {
                reqStr += cacheList.get(i).getCode();
            } else {
                reqStr += cacheList.get(i).getCode() + ",";
            }
        }

        db.revertSeq();
        service.getOptionDetail(reqStr).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    UtilPullToRefresh.refreshComplete(pullToRefreshListView);
                    String str = response.body().string();
                    if (!StringUtils.isEmpty(str)) {
                        String[] parts = str.split(";");
                        if (parts != null && parts.length > 0) {
                            List<FundEntity> mList = new ArrayList<>();
                            FundEntity entity = null;
                            for (int i = 0; i < parts.length; i++) {
                                if (!"\n".equals(parts[i])) {
                                    String[] splitFir = parts[i].split("=");
                                    if (splitFir != null && splitFir.length >= 2) {
                                        if (!StringUtils.isEmpty(splitFir[1])) {
                                            entity = new FundEntity();
                                            DecimalFormat df = new DecimalFormat("0.00");//这样为保持2位
                                            //获取格式化对象
                                            NumberFormat nt = NumberFormat.getPercentInstance();
                                            //设置百分数精确度2即保留两位小数
                                            nt.setMinimumFractionDigits(2);

                                            String[] splitSec = splitFir[0].split("_");
                                            if (splitSec != null && splitSec.length >= 3) {
                                                entity.setCode(splitSec[2]);
                                            } else {
                                                continue;
                                            }
                                            String[] splitThir = splitFir[1].split(",");
                                            if (splitThir != null && splitThir.length >= 4) {
                                                entity.setUserId(LoginConfig.getInstance().getUserid());
                                                entity.setName(splitThir[0].substring(1));
                                                if (StringUtils.isDouble(splitThir[1]) && StringUtils.isDouble(splitThir[2])) {
                                                    entity.setCurPrice(df.format(Double.parseDouble(splitThir[3])));
                                                    //涨幅值
                                                    double upvaluefir = Double.parseDouble(splitThir[3]) - Double.parseDouble(splitThir[2]);
                                                    //涨幅率
                                                    double updegrefir = upvaluefir / Double.parseDouble(splitThir[2]);//
                                                    entity.setUpDegr(nt.format(updegrefir));
                                                }
                                            } else {
                                                continue;
                                            }
                                            mList.add(entity);
                                        }
                                    }
                                }
                            }
                            entities.clear();
                            entities.addAll(mList);
                        }
                    }

                    if (!StringUtils.isEmptyList(entities)) {
                        ll_add.setVisibility(View.GONE);
                        rl_baner.setVisibility(View.VISIBLE);
                        optionAdapter.notifyDataSetChanged();
                        for (FundEntity ent : entities) {
                            ent.setAttention(true);
                        }
                        db.add(entities);
                    } else {
                        rl_baner.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    LogUtil.log_error(null,e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        service.getOptionDetail("sh000001,sz399001,sz399006").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    UtilPullToRefresh.refreshComplete(pullToRefreshListView);
                    String str = response.body().string();
                    if (!StringUtils.isEmpty(str)) {
                        ll_fundData.setVisibility(View.VISIBLE);
                        String[] parts = str.split(";");
                        if (parts != null && parts.length >= 3) {
                            String[] part1 = parts[0].split(",");
                            String[] part2 = parts[1].split(",");
                            String[] part3 = parts[2].split(",");

                            DecimalFormat df = new DecimalFormat("##0.00");//这样为保持2位
                            //获取格式化对象
                            NumberFormat nt = NumberFormat.getPercentInstance();
                            //设置百分数精确度2即保留两位小数
                            nt.setMinimumFractionDigits(2);
                            if (part1 != null && part1.length >= 4) {
                                //上证指数
                                tv_pricefir.setText(df.format(Double.parseDouble(part1[3])));
                                //涨幅值
                                double upvaluefir = Double.parseDouble(part1[3]) - Double.parseDouble(part1[2]);
                                if (upvaluefir >= 0) {
                                    tv_pricefir.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upfir.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upfirDegr.setTextColor(Color.parseColor("#E26D64"));
                                } else {
                                    tv_pricefir.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upfir.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upfirDegr.setTextColor(Color.parseColor("#56B68C"));
                                }
                                tv_upfir.setText(df.format(upvaluefir));
                                //涨幅率
                                double updegrefir = upvaluefir / Double.parseDouble(part1[2]);//
                                tv_upfirDegr.setText(nt.format(updegrefir));
                            }

                            if (part2 != null && part2.length >= 4) {
                                //深证成指
                                tv_priceSec.setText(df.format(Double.parseDouble(part2[3])));
                                //涨幅值
                                double upvalueSec = Double.parseDouble(part2[3]) - Double.parseDouble(part2[2]);

                                if (upvalueSec >= 0) {
                                    tv_priceSec.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upSec.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upSecDegr.setTextColor(Color.parseColor("#E26D64"));
                                } else {
                                    tv_priceSec.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upSec.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upSecDegr.setTextColor(Color.parseColor("#56B68C"));
                                }
                                tv_upSec.setText(df.format(upvalueSec));
                                //涨幅率
                                double updegreSec = upvalueSec / Double.parseDouble(part2[2]);//
                                tv_upSecDegr.setText(nt.format(updegreSec));
                            }

                            if (part2 != null && part2.length >= 4) {
                                //创业板指
                                tv_priceTir.setText(df.format(Double.parseDouble(part3[3])));
                                //涨幅值
                                double upvalueTir = Double.parseDouble(part3[3]) - Double.parseDouble(part3[2]);
                                if (upvalueTir >= 0) {
                                    tv_priceTir.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upTir.setTextColor(Color.parseColor("#E26D64"));
                                    tv_upTirDegr.setTextColor(Color.parseColor("#E26D64"));
                                } else {
                                    tv_priceTir.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upTir.setTextColor(Color.parseColor("#56B68C"));
                                    tv_upTirDegr.setTextColor(Color.parseColor("#56B68C"));
                                }
                                tv_upTir.setText(df.format(upvalueTir));
                                //涨幅率
                                double updegreTir = upvalueTir / Double.parseDouble(part3[2]);//
                                tv_upTirDegr.setText(nt.format(updegreTir));
                            }
                        }
                    } else {
                        ll_fundData.setVisibility(View.GONE);
                    }

                } catch (IOException e) {
                    LogUtil.log_error(null,e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void initView() {
        ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(getActivity(), PropertySearchStockActivity.class);
                startActivityForResult(inten, 1003);
            }
        });
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(false);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {//下啦刷新
                loadData();
                loadFundegr();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        listView.setDivider(null);
        adapter = new MergeAdapter();
        optionAdapter = new OptionalAdapter(getActivity(), entities, this);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_include_optional_head, null);
        ll_fundData = (LinearLayout) headView.findViewById(R.id.ll_fundData);
        tv_pricefir = (TextView) headView.findViewById(R.id.tv_pricefir);
        tv_upfir = (TextView) headView.findViewById(R.id.tv_upfir);
        tv_upfirDegr = (TextView) headView.findViewById(R.id.tv_upfirDegr);
        tv_namefir = (TextView) headView.findViewById(R.id.tv_namefir);
        tv_priceSec = (TextView) headView.findViewById(R.id.tv_priceSec);
        tv_upSec = (TextView) headView.findViewById(R.id.tv_upSec);
        tv_upSecDegr = (TextView) headView.findViewById(R.id.tv_upSecDegr);
        tv_nameSec = (TextView) headView.findViewById(R.id.tv_nameSec);
        tv_priceTir = (TextView) headView.findViewById(R.id.tv_priceTir);
        tv_upTir = (TextView) headView.findViewById(R.id.tv_upTir);
        tv_upTirDegr = (TextView) headView.findViewById(R.id.tv_upTirDegr);
        tv_nameTir = (TextView) headView.findViewById(R.id.tv_nameTir);
        rl_baner = (RelativeLayout) headView.findViewById(R.id.rl_baner);
        adapter.addView(headView, true);
        adapter.addAdapter(optionAdapter);
        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1003) {
            pullToRefreshListView.doPullRefreshing(true, 500);
        }
    }

    /**
     * 重新加载数据
     */
    public void reLoadOptional() {
        loadData();
        loadFundegr();
    }


}
