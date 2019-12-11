package com.yiqiji.money.modules.homeModule.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.homeModule.home.perecenter.BillDetailPerecenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/6.
 * address huang.weishui@71dai.com
 */
public class CateBillAdapter extends BaseQuickAdapter<DailycostEntity, CateBillAdapter.CateBillViewHolder> {
    private Context mContext;

    public CateBillAdapter(Context context) {
        super(R.layout.actiivty_cate_bill_item);
        this.mContext = context;
    }

    @Override
    protected void convert(CateBillViewHolder helper, DailycostEntity item) {
        helper.bindDate(item);
    }

    public class CateBillViewHolder extends BaseViewHolder {
        @BindView(R.id.bill_image)
        ImageView bill_image;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.cate_title)
        TextView cate_title;
        @BindView(R.id.bill_time)
        TextView bill_time;
        @BindView(R.id.bill_balance)
        TextView bill_balance_view;

        public CateBillViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        public void bindDate(final DailycostEntity dailycostEntity) {

            BillDetailPerecenter.setCateNameImage(dailycostEntity, new BillDetailPerecenter.CateNameImageInterface() {
                @Override
                public void getCateNameImageInterface(String cateid, String bill_type, String url, String cate_name, int title_color, String bill_mark, String bill_balance, int cilor_balance, String bill_text, String bill_brand) {
                    ImageLoaderManager.loadImage(mContext, url, bill_image);
                    cate_title.setText(cate_name);
                    String date_string = DateUtil.getDateToString(Long.parseLong(dailycostEntity.getTradetime()), "MM月dd日");
                    bill_time.setText(date_string);
                    bill_balance_view.setText(bill_balance);
                    bill_balance_view.setTextColor(mContext.getResources().getColor(cilor_balance));

                }
            });
        }
    }
}
