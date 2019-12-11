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
import com.yiqiji.money.modules.myModule.my.entity.BooksEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dansakai on 2017/7/4.
 */

public class BookListAdapter extends BaseAdapter {
    private List<BooksEntity.AccountbookBean> mBookList;
    private Context mContext;

    public BookListAdapter(Context context, List<BooksEntity.AccountbookBean> mBookList) {
        this.mContext = context;
        this.mBookList = mBookList;
    }

    @Override
    public int getCount() {
        if (!StringUtils.isEmptyList(mBookList)) {
            return mBookList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_book_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BooksEntity.AccountbookBean bean = mBookList.get(position);
        if (bean.getIsCheck() == 1) {
            viewHolder.ivChk.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivChk.setVisibility(View.GONE);
        }
        viewHolder.tvName.setText(bean.getAccountbooktitle());

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
