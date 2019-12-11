package com.yiqiji.money.modules.property.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.PropertyItemEntity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/13.
 */

public class PropertyCompletAdapter extends BaseAdapter {
    private Context mContext;
    private List<PropertyItemEntity> mList;

    public PropertyCompletAdapter(Context context, List<PropertyItemEntity> list) {
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
        private ImageView iv_icon;//图标
        private TextView tv_name;//名称
        private TextView tv_mark;//标记

        private TextView tv_fundTag;//透支理财 需要显示的标记
        private TextView tv_costTitle;//类型
        private TextView tv_cost;//金额

        private TextView tv_assetamount;//
        private TextView tv_time;
        private View v_divider;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_property_item_second, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_fundTag = (TextView) convertView.findViewById(R.id.tv_fundTag);
            viewHolder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            viewHolder.tv_costTitle = (TextView) convertView.findViewById(R.id.tv_costTitle);
            viewHolder.tv_cost = (TextView) convertView.findViewById(R.id.tv_cost);
            viewHolder.tv_assetamount = (TextView) convertView.findViewById(R.id.tv_assetamount);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.v_divider = convertView.findViewById(R.id.v_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PropertyItemEntity entity = mList.get(position);

        if (position == 0) {
            viewHolder.v_divider.setVisibility(View.GONE);
        } else {
            viewHolder.v_divider.setVisibility(View.VISIBLE);
        }
        XzbUtils.displayRoundImage(viewHolder.iv_icon, entity.getItemcateicon(), R.drawable.property_deafault_icon, UIHelper.Dp2Px(mContext,3));
        viewHolder.tv_name.setText(entity.getItemname());
        viewHolder.tv_assetamount.setText(entity.getAssetamount());
        int itemId = Integer.parseInt(entity.getItemtype());
        if (itemId == 15 || itemId == 16 || itemId == 18) {//投资理财
            viewHolder.tv_fundTag.setVisibility(View.VISIBLE);
            viewHolder.tv_fundTag.setText(entity.getItemcatename());
            viewHolder.tv_costTitle.setText("昨日收益");
            viewHolder.tv_costTitle.setVisibility(View.VISIBLE);
            viewHolder.tv_cost.setVisibility(View.VISIBLE);
            viewHolder.tv_time.setVisibility(View.VISIBLE);
            viewHolder.tv_mark.setText(entity.getMarktext());
            viewHolder.tv_cost.setText(entity.getTodaydiff());
            viewHolder.tv_time.setText(entity.getProfitamount());
        } else if (itemId == 19 || itemId == 20 || itemId == 21) {//应收付款
            viewHolder.tv_fundTag.setVisibility(View.GONE);
            viewHolder.tv_costTitle.setVisibility(View.VISIBLE);
            viewHolder.tv_cost.setVisibility(View.GONE);
            viewHolder.tv_time.setVisibility(View.GONE);
            viewHolder.tv_mark.setText(entity.getMark());
            if (itemId == 20) {
                viewHolder.tv_costTitle.setText("向 " + entity.getMarktext() + " 借款");
            } else if (itemId == 19) {
                viewHolder.tv_costTitle.setText("借给 " + entity.getMarktext());
            } else {
                int loantype=-1;
                if(entity.getAttach()!=null&&!StringUtils.isEmpty(entity.getAttach().loantype)){
                    loantype = Integer.parseInt(entity.getAttach().loantype);
                }
                if (loantype == 0) {//借出（应收）
                    viewHolder.tv_costTitle.setText("应收 " + entity.getMarktext());
                } else if (loantype == 1) {//借入，（应付）
                    viewHolder.tv_costTitle.setText("应付 " + entity.getMarktext());
                } else {
                    viewHolder.tv_costTitle.setText("");
                }
            }
        }
        return convertView;
    }
}
