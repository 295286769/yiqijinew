package com.yiqiji.money.modules.homeModule.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.home.entity.MessageBean;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private Context context;
	private List<MessageBean.DataBean> mData = new ArrayList<>();
	private boolean mShowFooter = false;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_FOOTER = 1;
	private ILoadCallback mCallback;

	private onItemOnClicList mItemOnClicList;

	public MessageAdapter(Context context, List<MessageBean.DataBean> list) {
		this.context = context;
		this.mData = list;
	}

	public void setDate(List<MessageBean.DataBean> list, boolean flag) {
		if (flag) {
			mData.clear();
			this.mData = list;
		} else {
			mData.addAll(list);
		}
		notifyDataSetChanged();
	}

	public MessageBean.DataBean getItemData(int position) {
		return mData.get(position);
	}

	public List<MessageBean.DataBean> getAll() {
		return mData;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (!mShowFooter) {
			return TYPE_ITEM;
		}

		if (position + 1 == mData.size()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
		}
	}

	public void setShowFooter(boolean mShowFooter) {
		this.mShowFooter = mShowFooter;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		if (holder instanceof ItemViewHolder) {
			if (mData.size() == 0) {
				return;
			}
			MessageBean.DataBean entity = mData.get(position);
			((ItemViewHolder) holder).tv_message_title.setText(entity.getContent());
			((ItemViewHolder) holder).tv_message_time.setText(entity.getCtime() + "");

			if (entity.getIsread().equals("0")) {
				((ItemViewHolder) holder).iv_message_red_dot.setVisibility(View.VISIBLE);
			} else {
				((ItemViewHolder) holder).iv_message_red_dot.setVisibility(View.GONE);
			}

		} else if (holder instanceof FooterViewHolder) {
			mCallback.onLoad();
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int viewType) {
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_message, null);
			ItemViewHolder viewHolder = new ItemViewHolder(view);
			return viewHolder;
		} else {
			View view = LayoutInflater.from(context).inflate(R.layout.footer, null);
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			return new FooterViewHolder(view);
		}
	}

	private class FooterViewHolder extends RecyclerView.ViewHolder {
		public FooterViewHolder(View view) {
			super(view);
		}
	}

	private class ItemViewHolder extends RecyclerView.ViewHolder {
		private TextView tv_message_title;
		private TextView tv_message_time;
		private ImageView iv_message_red_dot;

		public ItemViewHolder(View view) {
			super(view);
			tv_message_title = (TextView) view.findViewById(R.id.tv_message_title);
			tv_message_time = (TextView) view.findViewById(R.id.tv_message_time);
			iv_message_red_dot = (ImageView) view.findViewById(R.id.iv_message_red_dot);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mItemOnClicList.onItemOnClicList(getPosition());
				}
			});
		}
	}

	public interface ILoadCallback {
		void onLoad();
	}

	// 对外暴露设置接口方法
	public void setLoadCallback(ILoadCallback callback) {
		this.mCallback = callback;
	}

	// 对外暴露设置接口方法
	public void setItemOnClicList(onItemOnClicList mItemOnClicList) {
		this.mItemOnClicList = mItemOnClicList;
	}

	public interface onItemOnClicList {
		void onItemOnClicList(int position);
	}

}
