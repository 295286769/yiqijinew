package com.yiqiji.money.modules.homeModule.home.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.home.view.JournalImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/20.
 * address huang.weishui@71dai.com
 */
public class JounrnalImageAdapter extends BaseQuickAdapter<String, JounrnalImageAdapter.JounrnalImageHolder> {
    private Context mContext;

    public JounrnalImageAdapter(Context context) {
        super(R.layout.activity_journal_detail_head_image);
        mContext = context;
    }

    @Override
    protected void convert(JounrnalImageHolder helper, String item) {
        helper.bindDate(item);
    }

    public class JounrnalImageHolder extends BaseViewHolder {

        @BindView(R.id.journal_image)
        JournalImageView journal_image;

        public JounrnalImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDate(String imageurl) {
            ImageLoaderManager.loadImage(mContext, imageurl, journal_image);
        }
    }

}
