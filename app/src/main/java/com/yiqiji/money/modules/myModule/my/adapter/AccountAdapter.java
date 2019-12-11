package com.yiqiji.money.modules.myModule.my.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.activity.BookSubscribeActivity;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.myModule.my.activity.AttAccountActivity;
import com.yiqiji.money.modules.myModule.my.entity.AccountBookEntity;

import java.util.List;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by dansakai on 2017/5/19.
 * 我关注账本适配器
 */

public class AccountAdapter extends BaseAdapter {

    private Context mContext;
    private List<AccountBookEntity> mList;

    public AccountAdapter(Context context, List<AccountBookEntity> list) {
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

    class ViewHolder {
        private ImageView mIvBookicon;
        private ImageView iv_clear;
        private TextView account_name;
        private TextView tv_name;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_item_book, parent, false);
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            int screenWidth = XzbUtils.getPhoneScreen((Activity) mContext).widthPixels;
            layoutParams.width = (screenWidth - UIHelper.dip2px(mContext, 45)) / 3;
            layoutParams.height = (int) (((screenWidth - UIHelper.dip2px(mContext, 45)) / 3) * 1.23);
            convertView.setLayoutParams(layoutParams);
            viewHolder.mIvBookicon = (ImageView) convertView.findViewById(R.id.iv_bookicon);
            viewHolder.iv_clear = (ImageView) convertView.findViewById(R.id.iv_clear);
            viewHolder.account_name = (TextView) convertView.findViewById(R.id.account_name);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AccountBookEntity entity = mList.get(position);
        if (entity.isEdit()) {
            viewHolder.iv_clear.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_clear.setVisibility(View.GONE);
        }

        ImageLoaderManager.loadRoundCornerImage(viewHolder.mIvBookicon.getContext(), entity.getAccountbookicon(), viewHolder.mIvBookicon, 10);

        viewHolder.account_name.setText(entity.getAccountbooktitle());
        viewHolder.tv_name.setText("@ " + entity.getUsername());

        viewHolder.iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof AttAccountActivity) {
                    ((AttAccountActivity) mContext).cancleSuscrib(entity.getSubscribeid());
                }
            }
        });

        viewHolder.mIvBookicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookSubscribeActivity.open(mContext, entity.getAccountbookid(), entity.getAccountbookcate());
            }
        });

        return convertView;
    }
}
