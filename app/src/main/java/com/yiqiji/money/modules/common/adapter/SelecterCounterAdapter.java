package com.yiqiji.money.modules.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/11.
 */

public class SelecterCounterAdapter extends BaseAdapter {
    private Context mContext;
    private List<PropertyTransEntity> mList;

    public SelecterCounterAdapter(Context context, List<PropertyTransEntity> list) {
        this.mContext = context;
        this.mList = list;
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
        private ImageView icon_bank;
        private TextView tv_name;
        private TextView tv_mark;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_selecter_counter_item, null);
            viewHolder = new ViewHolder();
            viewHolder.icon_bank = (ImageView) convertView.findViewById(R.id.icon_bank);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PropertyTransEntity entity = mList.get(position);
        viewHolder.tv_name.setText(entity.getItemname());
        if ("-1".equals(entity.getAssetid())) {
            viewHolder.tv_mark.setVisibility(View.GONE);
            viewHolder.icon_bank.setImageResource(R.drawable.crash_icon);
        } else {
            if (!StringUtils.isEmpty(entity.getMarktext())) {
                viewHolder.tv_mark.setVisibility(View.VISIBLE);
                viewHolder.tv_mark.setText(entity.getMarktext());
            } else {
                viewHolder.tv_mark.setVisibility(View.GONE);
            }

            XzbUtils.displayRoundImage(viewHolder.icon_bank, entity.getItemicon(),R.drawable.crash_icon, UIHelper.Dp2Px(mContext,3));
        }
        return convertView;
    }
}
