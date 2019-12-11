package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.activity.PropertyDetailActivity;
import com.yiqiji.money.modules.property.activity.PropertyNetDetailActivity;
import com.yiqiji.money.modules.property.activity.StockDetailActivity;
import com.yiqiji.money.modules.property.entity.PropertyEntity;
import com.yiqiji.money.modules.property.entity.PropertyItemEntity;
import com.yiqiji.money.modules.property.fragment.WealthAssetsFragment;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/7.
 */

public class PropertyAdapter extends BaseAdapter {
    private Context mContext;
    private List<PropertyItemEntity> mList;
    private List<Integer> sourceIds;
    private LayoutInflater layoutInflater;
    private WealthAssetsFragment fragment;

    public PropertyAdapter(Context context, List<PropertyItemEntity> list, List<Integer> ids, WealthAssetsFragment fragment) {
        this.mContext = context;
        this.mList = list;
        this.sourceIds = ids;
        this.fragment = fragment;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int type = getItemViewType(position);
        FirstViewHolder firstViewHolder = null;
        SecondViewHolder secondViewHolder = null;
        if (convertView == null) {
            if (type == PropertyEntity.TYPE_FIRST) {
                firstViewHolder = new FirstViewHolder();
                convertView = layoutInflater.inflate(sourceIds.get(0), null);
                firstViewHolder.v_divider = convertView.findViewById(R.id.v_divider);
                firstViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                firstViewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                convertView.setTag(firstViewHolder);
            } else if (type == PropertyEntity.TYPE_SECOND) {
                secondViewHolder = new SecondViewHolder();
                convertView = layoutInflater.inflate(sourceIds.get(1), null);
                secondViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                secondViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                secondViewHolder.tv_fundTag = (TextView) convertView.findViewById(R.id.tv_fundTag);
                secondViewHolder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
                secondViewHolder.ll_costToday = (LinearLayout) convertView.findViewById(R.id.ll_costToday);
                secondViewHolder.tv_costTitle = (TextView) convertView.findViewById(R.id.tv_costTitle);
                secondViewHolder.tv_cost = (TextView) convertView.findViewById(R.id.tv_cost);
                secondViewHolder.ll_money = (LinearLayout) convertView.findViewById(R.id.ll_money);
                secondViewHolder.tv_assetamount = (TextView) convertView.findViewById(R.id.tv_assetamount);
                secondViewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(secondViewHolder);
            }
        } else {
            if (type == PropertyEntity.TYPE_FIRST) {
                firstViewHolder = (FirstViewHolder) convertView.getTag();
            } else if (type == PropertyEntity.TYPE_SECOND) {
                secondViewHolder = (SecondViewHolder) convertView.getTag();
            }
        }

        final PropertyItemEntity entity = mList.get(position);
        if (type == PropertyEntity.TYPE_FIRST) {
            if (position == 0) {
                firstViewHolder.v_divider.setVisibility(View.GONE);
            } else {
                firstViewHolder.v_divider.setVisibility(View.VISIBLE);
            }
            firstViewHolder.tv_title.setText(entity.getCategoryname());
            firstViewHolder.tv_money.setText(entity.getTotalamount());
            if (fragment.ishideMoney) {
                firstViewHolder.tv_money.setVisibility(View.GONE);
            } else {
                firstViewHolder.tv_money.setVisibility(View.VISIBLE);
            }

        } else if (type == PropertyEntity.TYPE_SECOND) {
            if (fragment.ishideMoney) {
                secondViewHolder.ll_costToday.setVisibility(View.GONE);
                secondViewHolder.ll_money.setVisibility(View.GONE);
            } else {
                secondViewHolder.ll_costToday.setVisibility(View.VISIBLE);
                secondViewHolder.ll_money.setVisibility(View.VISIBLE);
            }
            XzbUtils.displayRoundImage(secondViewHolder.iv_icon, entity.getItemcateicon(), R.drawable.property_deafault_icon, UIHelper.Dp2Px(mContext,3));
            secondViewHolder.tv_name.setText(entity.getItemname());
            secondViewHolder.tv_assetamount.setText(entity.getAssetamount());
            int cateGoryId = Integer.parseInt(entity.getCategoryid());
            int itemId = Integer.parseInt(entity.getItemtype());
            if (cateGoryId == 1) {//资金账户
                secondViewHolder.tv_cost.setVisibility(View.VISIBLE);
                if (Double.parseDouble(entity.getTodaydiff()) == 0) {
                    secondViewHolder.tv_costTitle.setVisibility(View.GONE);
                    secondViewHolder.tv_cost.setVisibility(View.GONE);
                } else if (Double.parseDouble(entity.getTodaydiff()) > 0) {
                    secondViewHolder.tv_costTitle.setVisibility(View.VISIBLE);
                    secondViewHolder.tv_cost.setVisibility(View.VISIBLE);
                    secondViewHolder.tv_costTitle.setText("今日收入");
                    secondViewHolder.tv_cost.setText(StringUtils.moneySplitComma(entity.getTodaydiff()));
                    secondViewHolder.tv_time.setTextColor(Color.parseColor("#E26D64"));
                } else {
                    secondViewHolder.tv_costTitle.setVisibility(View.VISIBLE);
                    secondViewHolder.tv_cost.setVisibility(View.VISIBLE);
                    secondViewHolder.tv_costTitle.setText("今日支出");
                    secondViewHolder.tv_cost.setText(StringUtils.moneySplitComma(entity.getTodaydiff().substring(1,entity.getTodaydiff().length())));
                    secondViewHolder.tv_cost.setTextColor(Color.parseColor("#56B68C"));
                }
                if (itemId == 6) {//信用卡
                    secondViewHolder.tv_fundTag.setVisibility(View.GONE);
                    secondViewHolder.tv_time.setVisibility(View.VISIBLE);
                    secondViewHolder.tv_mark.setText(entity.getItemcatename()+" "+entity.getMarktext());

                    secondViewHolder.tv_time.setText(entity.getAssetctime());
                } else if (itemId == 9) {
                    secondViewHolder.tv_fundTag.setVisibility(View.GONE);
                    secondViewHolder.tv_time.setVisibility(View.GONE);
                    secondViewHolder.tv_mark.setText(entity.getItemcatename()+" "+entity.getMarktext());
                } else {
                    secondViewHolder.tv_fundTag.setVisibility(View.GONE);
                    secondViewHolder.tv_time.setVisibility(View.GONE);
                    secondViewHolder.tv_mark.setText(entity.getMarktext());
                }
                secondViewHolder.tv_time.setTextColor(Color.parseColor("#999999"));
            } else if (cateGoryId == 2) {//投资理财
                secondViewHolder.tv_fundTag.setVisibility(View.VISIBLE);
                secondViewHolder.tv_fundTag.setText(entity.getItemcatename());
                if (itemId == 14) {//股票
                    secondViewHolder.tv_costTitle.setText("今日收益");
                } else {
                    secondViewHolder.tv_costTitle.setText("昨日收益");
                }
                secondViewHolder.tv_costTitle.setVisibility(View.VISIBLE);
                secondViewHolder.tv_cost.setVisibility(View.VISIBLE);
                secondViewHolder.tv_time.setVisibility(View.VISIBLE);
                secondViewHolder.tv_mark.setText(entity.getMarktext());
                if (itemId == 13 || itemId == 14) {
                    if (!StringUtils.isEmpty(entity.getProfitamount()) && entity.getProfitamount().contains("-")) {
                        secondViewHolder.tv_time.setTextColor(Color.parseColor("#56B68C"));
                    } else {
                        secondViewHolder.tv_time.setTextColor(Color.parseColor("#E26D64"));
                    }
                } else {
                    secondViewHolder.tv_time.setTextColor(Color.parseColor("#999999"));
                }
                if (!StringUtils.isEmpty(entity.getTodaydiff()) && entity.getTodaydiff().contains("-")) {
                    secondViewHolder.tv_cost.setTextColor(Color.parseColor("#56B68C"));
                } else {
                    secondViewHolder.tv_cost.setTextColor(Color.parseColor("#E26D64"));
                }
                secondViewHolder.tv_cost.setText(StringUtils.moneySplitComma(entity.getTodaydiff()));
                secondViewHolder.tv_time.setText(entity.getProfitamount());
            } else if (cateGoryId == 3) {//应收付款
                secondViewHolder.tv_fundTag.setVisibility(View.GONE);
                secondViewHolder.tv_costTitle.setVisibility(View.VISIBLE);
                secondViewHolder.tv_cost.setVisibility(View.GONE);
                secondViewHolder.tv_time.setVisibility(View.GONE);
                secondViewHolder.tv_mark.setText(entity.getMark());
                if (itemId == 20) {
                    secondViewHolder.tv_costTitle.setText("向 " + entity.getMarktext() + " 借款");
                } else if (itemId == 19) {
                    secondViewHolder.tv_costTitle.setText("借给 " + entity.getMarktext());
                } else {
                    if (entity.getAttach()!=null&&!StringUtils.isEmpty(entity.getAttach().loantype)) {
                        int loantype = Integer.parseInt(entity.getAttach().loantype);
                        if (loantype == 0) {//借出（应收）
                            secondViewHolder.tv_costTitle.setText("应收 " + entity.getMarktext());
                        } else if (loantype == 1) {//借入，（应付）
                            secondViewHolder.tv_costTitle.setText("应付 " + entity.getMarktext());
                        } else {
                            secondViewHolder.tv_costTitle.setText("");
                        }
                    }
                }

            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到资产详情
                if (type == PropertyEntity.TYPE_SECOND) {
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_PROPERTY_DETAIL);
                    if (!"2".equals(entity.getCategoryid())) {
                        Intent intent = new Intent(mContext, PropertyDetailActivity.class);
                        intent.putExtra("assetid", entity.getAssetid());
                        intent.putExtra("itemtype", entity.getItemtype());
                        fragment.startActivityForResult(intent, 1002);//跳转到资产详情页
                    } else {
                        if ("14".equals(entity.getItemtype())) {
                            Intent intent = new Intent(mContext, StockDetailActivity.class);
                            intent.putExtra("entity", entity);
                            intent.putExtra("isOption", false);
                            fragment.startActivityForResult(intent, 1002);//跳转到资产详情页
                        } else if ("15".equals(entity.getItemtype()) || "16".equals(entity.getItemtype()) || "18".equals(entity.getItemtype())){//网络理财(银行理财)
                            Intent intent = new Intent(mContext, PropertyNetDetailActivity.class);
                            intent.putExtra("assetid", entity.getAssetid());
                            intent.putExtra("itemtype", entity.getItemtype());
                            fragment.startActivityForResult(intent, 1002);//跳转到资产详情页
                        }
                    }
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (type == PropertyEntity.TYPE_SECOND) {
                    fragment.delectCounter(Integer.parseInt(entity.getAssetid()));
                }
                return true;
            }
        });

        return convertView;
    }

    static class FirstViewHolder {
        private View v_divider;//分割线
        private TextView tv_title;//资产名称
        private TextView tv_money;//资产额
    }

    static class SecondViewHolder {
        private ImageView iv_icon;//图标
        private TextView tv_name;//名称
        private TextView tv_fundTag;//透支理财 需要显示的标记
        private TextView tv_mark;//标记

        private LinearLayout ll_costToday;//今日资金收益
        private TextView tv_costTitle;//类型
        private TextView tv_cost;//金额

        private LinearLayout ll_money;//子类资产额
        private TextView tv_assetamount;//
        private TextView tv_time;

    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        if (StringUtils.isEmptyList(mList)) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
