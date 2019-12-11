package com.yiqiji.money.modules.homeModule.write.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewHolder;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;

import java.util.List;

public class DetailsAdapter extends CommonRecyclerViewAdapter<DailycostEntity> {

    public DetailsAdapter(Context context, List<DailycostEntity> data) {
        super(context, data);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, DailycostEntity entity, int position) {
        // TODO Auto-generated method stub

        String mDataid, title;
        if (!TextUtils.isEmpty(entity.getBillsubcatename())) {
            mDataid = entity.getBillsubcateid() + "child";
            title = entity.getBillsubcatename();
        } else {
            mDataid = entity.getBillcateid();
            title = entity.getBillcatename();

        }
        String url_native = DownUrlUtil.jsonPath + mDataid + "_s";
        h.setImage(R.id.iv_item_details, url_native, 0);
        h.setTextSign(R.id.account_item_view, null, title, entity.getBillmark(),
                DateUtil.transferLongToDate(6, Long.parseLong(entity.getTradetime())), "", "", entity.getBillamount(),
                entity.getUsername(), entity.getBilltype(), false);

    }

    @Override
    public int getLayoutViewId(int viewType) {
        // TODO Auto-generated method stub
        return R.layout.item_details;
    }
}
