package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.property.entity.FundEntity;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/13.
 */

public class SearchFundAdapter extends BaseAdapter {
    private Context mContext;
    private List<FundEntity> list;

    public SearchFundAdapter(Context context, List<FundEntity> lis) {
        this.mContext = context;
        this.list = lis;
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
        private TextView tv_attention;
        private View v_divider;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_stock_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_attention = (TextView) convertView.findViewById(R.id.tv_attention);
            viewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.v_divider = convertView.findViewById(R.id.v_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FundEntity entity = list.get(position);
        viewHolder.tv_name.setText(entity.getName());
        viewHolder.tv_code.setText(entity.getCode());
        if (entity.isAttention()) {
            viewHolder.tv_attention.setText("已关注");
            viewHolder.tv_attention.setTextColor(Color.parseColor("#CCCCCC"));
            viewHolder.tv_attention.setBackgroundResource(R.drawable.unattention_btn_bg);
        } else {
            viewHolder.tv_attention.setText("+关注");
            viewHolder.tv_attention.setTextColor(mContext.getResources().getColor(R.color.main_back));
            viewHolder.tv_attention.setBackgroundResource(R.drawable.attention_btn_bg);
        }

        viewHolder.tv_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.isAttention()) {
                    entity.setAttention(false);
                } else {
                    entity.setAttention(true);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
