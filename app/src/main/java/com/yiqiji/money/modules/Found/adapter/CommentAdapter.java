package com.yiqiji.money.modules.Found.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.entity.CommentEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.FaceConversionUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/21.
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentEntity> mList;

    public CommentAdapter(Context context, List<CommentEntity> lis) {
        this.mContext = context;
        this.mList = lis;
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
        private ImageView iv_photo;
        private TextView tv_usrName;
        private TextView tv_time;
        private TextView tv_commentContent;
        private View v_devider;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_comment_item, null);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.tv_usrName = (TextView) convertView.findViewById(R.id.tv_usrName);
            viewHolder.tv_commentContent = (TextView) convertView.findViewById(R.id.tv_commentContent);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.v_devider = convertView.findViewById(R.id.v_devider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentEntity entity = mList.get(position);
        XzbUtils.displayRoundImage(viewHolder.iv_photo, entity.getUsericon(), 0, 360);
        viewHolder.tv_usrName.setText(entity.getUsername());
        if (!StringUtils.isEmpty(entity.getCtime())) {
            viewHolder.tv_time.setVisibility(View.VISIBLE);
            String timeStr = DateUtil.formatDate(Long.parseLong(entity.getCtime()))+" "+ DateUtil.getTime(Long.parseLong(entity.getCtime())).substring(0,5);
            viewHolder.tv_time.setText(timeStr);
        } else {
            viewHolder.tv_time.setVisibility(View.GONE);
        }

        if (position == mList.size() - 1) {
            viewHolder.v_devider.setVisibility(View.GONE);
        } else {
            viewHolder.v_devider.setVisibility(View.VISIBLE);
        }

        viewHolder.tv_commentContent.setText(FaceConversionUtil.getInstace()
                .HtmlFromStr(mContext, entity.getContent()));

        return convertView;
    }
}
