package com.yiqiji.money.modules.homeModule.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.entity.CateEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/7/6.
 * 装修阶段分类---- 装修日记
 */

public class DecorateCateAdapter extends BaseAdapter {
    private Context mContext;
    private List<CateEntity> mList;

    public DecorateCateAdapter(Context context, List<CateEntity> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (!StringUtils.isEmptyList(mList)) {
            return mList.size();
        } else {
            return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cate_decotrebook, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CateEntity entity = mList.get(position);
        if (entity.getIsChk() == 1) {//选中状态
            viewHolder.tvCate.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tvCate.setBackgroundResource(R.drawable.circle_chk);
        } else {
            viewHolder.tvCate.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
            viewHolder.tvCate.setBackgroundResource(R.drawable.circle_unchk);
        }
        viewHolder.tvCate.setText(entity.getCate());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvCate)
        TextView tvCate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
