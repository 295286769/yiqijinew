package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.property.entity.FundAndStockEntity;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/9.
 */

public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<FundAndStockEntity> mList;
    private int sourceId;

    public SearchAdapter(Context context, List<FundAndStockEntity> mlis, int sourceId) {
        this.mContext = context;
        this.mList = mlis;
        this.sourceId = sourceId;
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

    static class ViewHolder {
        private TextView tv_code, tv_name;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(sourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FundAndStockEntity entity = mList.get(position);
        viewHolder.tv_code.setText(entity.getF_code());
        viewHolder.tv_name.setText(entity.getF_symbolName());

        return convertView;
    }
}
