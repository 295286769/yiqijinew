package com.yiqiji.money.modules.myModule.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.myModule.my.activity.SelectDataActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/7/5.
 */

public class SelectDataAdapter extends BaseAdapter {
    private List<SelDataEntity> mDataList;
    private Context mContext;

    public SelectDataAdapter(Context context, List<SelDataEntity> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        if (!StringUtils.isEmptyList(mDataList)) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookListAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_book_list, null);
            viewHolder = new BookListAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BookListAdapter.ViewHolder) convertView.getTag();
        }
        SelDataEntity entity = mDataList.get(position);
        if (entity.getIsCheck() == 1) {
            viewHolder.ivChk.setVisibility(View.VISIBLE);
            if (entity.getLimit().equals("4")) {
                ((SelectDataActivity)mContext).setVisibleSelData(View.VISIBLE);
            } else {
                ((SelectDataActivity)mContext).setVisibleSelData(View.GONE);
            }
        } else {
            viewHolder.ivChk.setVisibility(View.GONE);
        }

        viewHolder.tvName.setText(entity.getTitle());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivChk)
        ImageView ivChk;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
