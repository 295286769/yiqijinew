package com.yiqiji.money.modules.common.adapter.base;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.entity.SalaryBean;

/**
 * Created by whl on 16/9/24.
 */
public class SalaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<SalaryBean> mDataList;
	private Context mContext;

	public SalaryAdapter(Context mContext, List<SalaryBean> mDataList) {
		this.mDataList = mDataList;
		this.mContext = mContext;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_salary, parent, false);
		return new SalaryHolder((ViewGroup) view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		SalaryBean salaryBean = mDataList.get(position);
		((SalaryHolder) holder).tv_item_salary_time.setText(salaryBean.getTiem());
		((SalaryHolder) holder).tv_item_salary_money.setText(salaryBean.getMoney());
		((SalaryHolder) holder).tv_item_salary_prompt.setText(salaryBean.getPrompt());
		if (salaryBean.isShow()) {
			((SalaryHolder) holder).iv_item_salary_grant_wages.setVisibility(View.VISIBLE);
		} else {
			((SalaryHolder) holder).iv_item_salary_grant_wages.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	public class SalaryHolder extends RecyclerView.ViewHolder {
		private TextView tv_item_salary_time;
		private TextView tv_item_salary_money;
		private TextView tv_item_salary_prompt;
		private ImageView iv_item_salary_grant_wages;

		public SalaryHolder(ViewGroup itemView) {
			super(itemView);
			tv_item_salary_time = (TextView) itemView.findViewById(R.id.tv_item_salary_time);
			tv_item_salary_money = (TextView) itemView.findViewById(R.id.tv_item_salary_money);
			tv_item_salary_prompt = (TextView) itemView.findViewById(R.id.tv_item_salary_prompt);
			iv_item_salary_grant_wages = (ImageView) itemView.findViewById(R.id.iv_item_salary_grant_wages);
		}
	}

}
