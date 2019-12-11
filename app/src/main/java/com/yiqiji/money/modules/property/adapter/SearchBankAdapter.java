package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.property.activity.PropertyNewBankActivity;
import com.yiqiji.money.modules.property.activity.PropertySelectBanActivity;
import com.yiqiji.money.modules.property.activity.WealthNewAccountActivity;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.money.modules.property.entity.PropertyBanEntity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/12.
 */

public class SearchBankAdapter extends BaseAdapter {
    private Context mContext;
    private AddPropertyItemEntity transEntity;
    private List<PropertyBanEntity> mList;

    public SearchBankAdapter(Context context, List<PropertyBanEntity> list,AddPropertyItemEntity entity) {
        this.mContext = context;
        this.mList = list;
        this.transEntity = entity;
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
        private TextView tv_letter;
        private ImageView ban_icon;
        private TextView tv_bankName;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_bank_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
            viewHolder.ban_icon = (ImageView) convertView.findViewById(R.id.ban_icon);
            viewHolder.tv_bankName = (TextView) convertView.findViewById(R.id.tv_bankName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PropertyBanEntity entity = mList.get(position);
        viewHolder.tv_letter.setVisibility(View.GONE);
        viewHolder.tv_bankName.setText(entity.getBankname());
        XzbUtils.displayImage(viewHolder.ban_icon, entity.getBankicon(), 0);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("6".equals(transEntity.getItemtypeId())) {
                    Intent intent = new Intent(mContext, PropertyNewBankActivity.class);
                    transEntity.setBankid(entity.getBankid());
                    transEntity.setBankicon(entity.getBankicon());
                    transEntity.setBankname(entity.getBankname());

                    intent.putExtra("bankEntity", transEntity);
                    ((PropertySelectBanActivity) mContext).startActivityForResult(intent, 1002);
                } else {
                    Intent intent = new Intent(mContext, WealthNewAccountActivity.class);
                    transEntity.setBankid(entity.getBankid());
                    transEntity.setBankicon(entity.getBankicon());
                    transEntity.setBankname(entity.getBankname());

                    intent.putExtra("addproEntity", transEntity);
                    ((PropertySelectBanActivity) mContext).startActivityForResult(intent, 1002);
                }
            }
        });
        return convertView;
    }
}
