package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.activity.PropertyDetailActivity;
import com.yiqiji.money.modules.property.entity.BillEntity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/24.
 * 资产关联账单适配器
 */

public class BillListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BillEntity> mList;
    private List<Integer> sourceIds;
    private LayoutInflater layoutInflater;
    private PropertyDetailActivity activity;

    public BillListAdapter(Context mContext, List<BillEntity> list, List<Integer> sourceId) {
        this.mContext = mContext;
        this.mList = list;
        this.sourceIds = sourceId;
        layoutInflater = LayoutInflater.from(mContext);
        if (mContext instanceof PropertyDetailActivity) {
            activity = ((PropertyDetailActivity) mContext);
        }
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

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class FirstViewHolder {
        private ImageView iv_icon;
        private TextView tv_month;
        private TextView tv_year;

        private TextView tv_linc;
        private TextView tv_lincValue;

        private TextView tv_pay;
        private TextView tv_payValue;
    }

    static class SecondViewHolder {
        private RelativeLayout rl_item;
        private ImageView iv_cateicon;
        private TextView tv_title;
        private TextView tv_cost;
        private View v_divider;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FirstViewHolder firstViewHolder = null;
        SecondViewHolder secondViewHolder = null;
        final int type = getItemViewType(position);
        if (type == BillEntity.TYPE_FIRST) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(sourceIds.get(0), null);
                firstViewHolder = new FirstViewHolder();
                firstViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                firstViewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
                firstViewHolder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
                firstViewHolder.tv_linc = (TextView) convertView.findViewById(R.id.tv_linc);
                firstViewHolder.tv_lincValue = (TextView) convertView.findViewById(R.id.tv_lincValue);
                firstViewHolder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
                firstViewHolder.tv_payValue = (TextView) convertView.findViewById(R.id.tv_payValue);
                convertView.setTag(firstViewHolder);
            } else {
                firstViewHolder = (FirstViewHolder) convertView.getTag();
            }
        } else if (type == BillEntity.TYPE_SECOND) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(sourceIds.get(1), null);
                secondViewHolder = new SecondViewHolder();
                secondViewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                secondViewHolder.iv_cateicon = (ImageView) convertView.findViewById(R.id.iv_cateicon);
                secondViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                secondViewHolder.tv_cost = (TextView) convertView.findViewById(R.id.tv_cost);
                secondViewHolder.v_divider = convertView.findViewById(R.id.v_divider);
                convertView.setTag(secondViewHolder);
            } else {
                secondViewHolder = (SecondViewHolder) convertView.getTag();
            }
        }

        BillEntity entity = mList.get(position);
        if (type == BillEntity.TYPE_FIRST) {
            if (entity.isColse()) {
                firstViewHolder.iv_icon.setImageResource(R.drawable.arrow_unselect);
            } else {
                firstViewHolder.iv_icon.setImageResource(R.drawable.arrow_select);
            }

            if (!StringUtils.isEmpty(entity.getDate())) {
                String[] datas = DateUtil.formatDate(Long.parseLong(entity.getDate())).split("-");
                if (datas != null && datas.length >= 3) {
                    firstViewHolder.tv_year.setText(datas[0] + "年");
                    firstViewHolder.tv_month.setText(datas[1] + "月");
                    firstViewHolder.tv_lincValue.setText("流入" + StringUtils.moneySplitComma(entity.getTotalinc()));
                    firstViewHolder.tv_payValue.setText("流出" + StringUtils.moneySplitComma(entity.getTotalpay()));

                    int halfWidth = UIHelper.getDisplayWidth(activity) / 3;
                    LinearLayout.LayoutParams paramsLinc = (LinearLayout.LayoutParams) firstViewHolder.tv_linc.getLayoutParams();
                    if (Double.parseDouble(entity.getTotalinc()) > 0) {
                        int lincWidth = 0;
                        if (Double.parseDouble(entity.getTotalinc()) > Double.parseDouble(entity.getTotalpay())) {
                            lincWidth = halfWidth;
                        } else {
                            lincWidth = (int) (Double.parseDouble(entity.getTotalinc()) / Double.parseDouble(entity.getTotalpay())) * halfWidth;
                        }
                        paramsLinc.width = lincWidth;
                        paramsLinc.height = UIHelper.dip2px(mContext, 14);
                    } else {
                        paramsLinc.width = UIHelper.dip2px(mContext, 10);
                        paramsLinc.height = UIHelper.dip2px(mContext, 14);
                    }
                    firstViewHolder.tv_linc.setLayoutParams(paramsLinc);
                    LinearLayout.LayoutParams paramspay = (LinearLayout.LayoutParams) firstViewHolder.tv_pay.getLayoutParams();
                    if (Double.parseDouble(entity.getTotalpay()) > 0) {
                        int payWidth = 0;
                        if (Double.parseDouble(entity.getTotalpay()) > Double.parseDouble(entity.getTotalinc())) {
                            payWidth = halfWidth;
                        } else {
                            payWidth = (int) (Double.parseDouble(entity.getTotalpay()) / Double.parseDouble(entity.getTotalinc())) * halfWidth;
                        }
                        paramspay.width = payWidth;
                        paramspay.height = UIHelper.dip2px(mContext, 14);
                    } else {
                        paramspay.width = UIHelper.dip2px(mContext, 10);
                        paramspay.height = UIHelper.dip2px(mContext, 14);
                    }
                    firstViewHolder.tv_pay.setLayoutParams(paramspay);

                }
            }
        } else if (type == BillEntity.TYPE_SECOND) {
            if (entity.isColse()) {
                secondViewHolder.rl_item.setVisibility(View.GONE);
                secondViewHolder.v_divider.setVisibility(View.GONE);
            } else {
                secondViewHolder.rl_item.setVisibility(View.VISIBLE);
                XzbUtils.displayRoundImage(secondViewHolder.iv_cateicon, entity.getBillcateicon(), R.drawable.property_deafault_icon, UIHelper.Dp2Px(mContext, 3));
                secondViewHolder.tv_title.setText(entity.getBillcatename());
                secondViewHolder.tv_cost.setText(StringUtils.moneySplitComma(entity.getBillamount()));
                if (position == mList.size() - 1) {
                    secondViewHolder.v_divider.setVisibility(View.GONE);
                } else {
                    secondViewHolder.v_divider.setVisibility(View.VISIBLE);
                }
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == BillEntity.TYPE_FIRST) {
                    mList.get(position).setColse(!mList.get(position).isColse());
                    for (int i = position + 1; i < mList.size(); i++) {
                        if (mList.get(i).getType() == BillEntity.TYPE_FIRST) {
                            break;
                        } else {
                            mList.get(i).setColse(!mList.get(i).isColse());
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }
}
