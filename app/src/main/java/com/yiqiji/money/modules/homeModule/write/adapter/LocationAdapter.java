package com.yiqiji.money.modules.homeModule.write.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.entity.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public class LocationAdapter extends BaseAdapter {

    private Context context;
    private Location data;
    private List<Location.DataBean.PoisBean> pois = new ArrayList<Location.DataBean.PoisBean>();

    public LocationAdapter(Context context, Location data) {
        this.context = context;
        this.data = data;
    }
    private String address;

    public void setData(Location data, String address) {
        this.data = data;
        this.address = address;
        pois.clear();
        pois.addAll(data.getData().getPois());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data.getData() != null) {
            return data.getData().getPois().size() + 1;
        }
        return 0;
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
            viewHolder.iv_item_location_selected = (ImageView) convertView.findViewById(R.id.iv_item_location_selected);
            viewHolder.tv_item_location_name = (TextView) convertView.findViewById(R.id.tv_item_location_name);
            viewHolder.tv_item_location_addr = (TextView) convertView.findViewById(R.id.tv_item_location_addr);
            viewHolder.tv_item_location_not_show = (TextView) convertView.findViewById(R.id.tv_item_location_not_show);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.tv_item_location_not_show.setVisibility(View.VISIBLE);
            viewHolder.tv_item_location_name.setVisibility(View.INVISIBLE);
            viewHolder.tv_item_location_addr.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_item_location_not_show.setVisibility(View.GONE);
            viewHolder.tv_item_location_name.setVisibility(View.VISIBLE);
            viewHolder.tv_item_location_addr.setVisibility(View.VISIBLE);

            viewHolder.tv_item_location_name.setText(pois.get(position - 1).getName());
            viewHolder.tv_item_location_addr.setText(pois.get(position - 1).getAddr());
        }
        if (position == 0 && address.equals("不显示位置")) {
            viewHolder.iv_item_location_selected.setVisibility(View.VISIBLE);
        } else {
            if (position > 0) {
                if (address.equals(pois.get(position - 1).getName())) {
                    viewHolder.iv_item_location_selected.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.iv_item_location_selected.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_item_location_selected;
        TextView tv_item_location_name;
        TextView tv_item_location_addr;
        TextView tv_item_location_not_show;

    }

}
