package com.yiqiji.money.modules.common.adapter.base;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.entity.CapitalBean;

/**
 * Created by whl on 16/9/24.
 */
public class CapitalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<CapitalBean> mList;
	private Context mContext;

	public CapitalAdapter(Context context, List<CapitalBean> mList) {
		this.mContext = context;
		this.mList = mList;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_capital, parent, false);
		return new CapitalHolder((ViewGroup) view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		CapitalBean capitalBean = mList.get(position);
		((CapitalHolder) holder).tv_item_capital_type.setText(capitalBean.getType());
		((CapitalHolder) holder).tv_item_capital_time.setText(capitalBean.getTime());
		((CapitalHolder) holder).tv_item_capital_money.setText(capitalBean.getMoney());
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public class CapitalHolder extends RecyclerView.ViewHolder {

		private TextView tv_item_capital_type;
		private TextView tv_item_capital_time;
		private TextView tv_item_capital_money;

		public CapitalHolder(ViewGroup itemView) {
			super(itemView);

			tv_item_capital_type = (TextView) itemView.findViewById(R.id.tv_item_capital_type);
			tv_item_capital_time = (TextView) itemView.findViewById(R.id.tv_item_capital_time);
			tv_item_capital_money = (TextView) itemView.findViewById(R.id.tv_item_capital_money);

		}
	}

}
