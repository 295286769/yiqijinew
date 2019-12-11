package com.yiqiji.money.modules.common.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.entity.Suggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public class SelectLocationAdapter extends BaseAdapter {

    private Context context;
    private List<Suggestion.DataBean> data = new ArrayList<>();

    public SelectLocationAdapter(Context context, List<Suggestion.DataBean> data) {
        this.context = context;
        this.data = data;
    }


    public void setData(List<Suggestion.DataBean> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }


    public String  getData(int position) {
        return data.get(position).getName();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 常见的优化ViewHolder
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_location, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_item_location_name = (TextView) convertView.findViewById(R.id.tv_item_location_name);
            viewHolder.tv_item_location_addr = (TextView) convertView.findViewById(R.id.tv_item_location_addr);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_location_name.setText(data.get(position).getName());
        viewHolder.tv_item_location_addr.setText(data.get(position).getCity());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_item_location_name;
        TextView tv_item_location_addr;

    }

}
