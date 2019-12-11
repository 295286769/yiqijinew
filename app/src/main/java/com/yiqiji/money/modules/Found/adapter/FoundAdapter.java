package com.yiqiji.money.modules.Found.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.activity.ArticleDetailActivity;
import com.yiqiji.money.modules.Found.entity.ArticleEntity;
import com.yiqiji.money.modules.common.utils.FaceConversionUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/20.
 */

public class FoundAdapter extends BaseAdapter {

    private Context mContext;
    private List<ArticleEntity> mList;

    public FoundAdapter(Context context, List<ArticleEntity> list) {
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
        private TextView tv_title;
        private TextView tv_content;
        private ImageView iv_imgs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.found_article_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.iv_imgs = (ImageView) convertView.findViewById(R.id.iv_imgs);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ArticleEntity entity = mList.get(position);
        viewHolder.tv_title.setText(entity.getArticletitle());
        viewHolder.tv_content.setText(FaceConversionUtil.getInstace()
                .HtmlFromStr(mContext, entity.getArticletext()));
        if (StringUtils.isEmpty(entity.getArticleimg())) {
            viewHolder.iv_imgs.setVisibility(View.GONE);
        } else {
            XzbUtils.displayRoundImage(viewHolder.iv_imgs,entity.getArticleimg(),0, UIHelper.Dp2Px(mContext,5));
            viewHolder.iv_imgs.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                intent.putExtra("articleEntity", entity);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
