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
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.activity.PropertyNewFinancialActivity;
import com.yiqiji.money.modules.property.activity.PropertyNewFundActivity;
import com.yiqiji.money.modules.property.activity.PropertyNewGetpayActivity;
import com.yiqiji.money.modules.property.activity.PropertySelectBanActivity;
import com.yiqiji.money.modules.property.activity.WealthAddActivity;
import com.yiqiji.money.modules.property.activity.WealthNewAccountActivity;
import com.yiqiji.money.modules.property.entity.AddPropertyItemEntity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;


/**
 * Created by dansakai on 2017/3/8.
 * 添加资产适配器
 */

public class AddPropertyAdapter extends BaseAdapter {
    private Context mContext;
    private List<AddPropertyItemEntity> mList;
    private int sourceId;

    public AddPropertyAdapter(Context context, List<AddPropertyItemEntity> lis, int sourceId) {
        this.mContext = context;
        this.mList = lis;
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
        private ImageView iv_icon;
        private TextView tv_name;
        private View v_devider;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(sourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.v_devider = convertView.findViewById(R.id.v_devider);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AddPropertyItemEntity entity = mList.get(position);
        XzbUtils.displayRoundImage(viewHolder.iv_icon, entity.getCategoryicon(),R.drawable.property_deafault_icon, UIHelper.Dp2Px(mContext,3));
        viewHolder.tv_name.setText(entity.getCategoryname());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                int categoryId = Integer.parseInt(entity.getCategoryid());
                int itemId = Integer.parseInt(entity.getItemtypeId());
                if (categoryId == 1) {//资金账户
                    if (itemId == 6 || itemId == 9) {//借记卡 信用卡
                        intent = new Intent(mContext, PropertySelectBanActivity.class);
                        intent.putExtra("addproEntity", entity);
                        if (mContext instanceof WealthAddActivity) {
                            ((WealthAddActivity) mContext).startActivityForResult(intent, 1002);
                        }
                    } else {
                        intent = new Intent(mContext, WealthNewAccountActivity.class);
                        intent.putExtra("addproEntity", entity);
                        if (mContext instanceof WealthAddActivity) {
                            ((WealthAddActivity) mContext).startActivityForResult(intent, 1002);
                        }
                    }

                } else if (categoryId == 2) {//投资理财
                    if (itemId == 13 || itemId == 14) {//
                        intent = new Intent(mContext, PropertyNewFundActivity.class);
                        intent.putExtra("addproEntity", entity);
                        if (mContext instanceof WealthAddActivity) {
                            ((WealthAddActivity) mContext).startActivityForResult(intent, 1002);
                        }
                    } else {//
                        intent = new Intent(mContext, PropertyNewFinancialActivity.class);
                        intent.putExtra("addproEntity", entity);
                        intent.putExtra("isEdit", false);
                        if (mContext instanceof WealthAddActivity) {
                            ((WealthAddActivity) mContext).startActivityForResult(intent, 1002);
                        }
                    }

                } else if (categoryId == 3) {//借入借出
                    intent = new Intent(mContext, PropertyNewGetpayActivity.class);
                    intent.putExtra("addproEntity", entity);
                    if (mContext instanceof WealthAddActivity) {
                        ((WealthAddActivity) mContext).startActivityForResult(intent, 1002);
                    }
                }
            }
        });

        return convertView;
    }
}
