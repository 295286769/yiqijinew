package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.property.activity.StockDetailActivity;
import com.yiqiji.money.modules.property.entity.FundEntity;
import com.yiqiji.money.modules.property.fragment.WealthOptionalFragment;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/13.
 */

public class OptionalAdapter extends BaseAdapter {
    private Context mContext;
    private List<FundEntity> list;
    private WealthOptionalFragment wafragment;

    public OptionalAdapter(Context context, List<FundEntity> lis,Fragment fragment) {
        this.mContext = context;
        this.list = lis;
        if (fragment instanceof WealthOptionalFragment) {
            this.wafragment = (WealthOptionalFragment) fragment;
        }
    }

    @Override
    public int getCount() {
        if (StringUtils.isEmptyList(list)) {
            return 0;
        } else {
            return list.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private TextView tv_name;
        private TextView tv_code;
        private TextView tv_curPrice;
        private TextView tv_upDegr;
        private View v_devider;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_optional_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            viewHolder.tv_curPrice = (TextView) convertView.findViewById(R.id.tv_curPrice);
            viewHolder.tv_upDegr = (TextView) convertView.findViewById(R.id.tv_upDegr);
            viewHolder.v_devider = convertView.findViewById(R.id.v_devider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final FundEntity entity = list.get(position);
        viewHolder.tv_name.setText(entity.getName());
        viewHolder.tv_code.setText(entity.getCode());
        viewHolder.tv_curPrice.setText(entity.getCurPrice());
        viewHolder.tv_upDegr.setText(entity.getUpDegr());

        if (!StringUtils.isEmpty(entity.getUpDegr())) {
            if (!entity.getUpDegr().contains("-")) {
                viewHolder.tv_curPrice.setTextColor(Color.parseColor("#E26D64"));
                viewHolder.tv_upDegr.setBackgroundResource(R.drawable.activity_up_degree_bg);
            } else {
                viewHolder.tv_curPrice.setTextColor(Color.parseColor("#56B68C"));
                viewHolder.tv_upDegr.setBackgroundResource(R.drawable.activity_down_degree_bg);
            }
        }

        if (position == list.size() - 1) {
            viewHolder.v_devider.setVisibility(View.GONE);
        } else {
            viewHolder.v_devider.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StockDetailActivity.class);
                intent.putExtra("fundEntity", entity);
                intent.putExtra("isOption", true);
                if (wafragment != null) {
                    wafragment.startActivityForResult(intent,1003);
                }
            }
        });

        return convertView;
    }
}
