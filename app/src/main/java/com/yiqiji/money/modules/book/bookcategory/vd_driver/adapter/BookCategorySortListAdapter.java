package com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.manager.IDragAndSwipeAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem.ViewType;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/1.
 */

public class BookCategorySortListAdapter extends BaseMultiItemQuickAdapter<BookCategoryListMultipleItem, BaseViewHolder> implements IDragAndSwipeAdapter {


    Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BookCategorySortListAdapter(Context context, List<BookCategoryListMultipleItem> data) {
        super(data);
        this.mContext = context;
        addItemType(ViewType.GROUP.ordinal(), R.layout.view_item, GroupViewHolder.class);
        addItemType(ViewType.CUTLINE.ordinal(), R.layout.item_order_category_cutline_layout, CutLineViewHolder.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BookCategoryListMultipleItem item) {
        int viewType = helper.getItemViewType();
        switch (BookCategoryListMultipleItem.getViewType(viewType)) {
            case GROUP:
                GroupViewHolder groupViewHolder = (GroupViewHolder) helper;
                groupViewHolder.bindData(item);
                break;
            case CUTLINE:
                CutLineViewHolder cutLineViewHolder = (CutLineViewHolder) helper;
                cutLineViewHolder.bindData(item);
                break;
        }
    }

    //一级分类的ViewHolder
    public class GroupViewHolder extends BaseViewHolder {
        @BindView(R.id.icon_img)
        ImageView iconImg;
        @BindView(R.id.name_tv)
        TextView nameTv;

        public String parentid;

        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(final BookCategoryListMultipleItem item) {
            final BookCategory bookChildCategoryModel = (BookCategory) item.getData();
            nameTv.setText(bookChildCategoryModel.getNameReplaceBar());
            parentid = bookChildCategoryModel.parentid;
            Log.v("imageUrl",nameTv.getText()+"  url="+bookChildCategoryModel.getIconUrl());
//            ImageLoaderManager.loadImage(iconImg.getContext(), bookChildCategoryModel.getIconUrl(), R.drawable.gray_ordinary, iconImg);
            XzbUtils.displayImage(iconImg, bookChildCategoryModel.getIconUrl(), R.drawable.gray_ordinary);
        }
    }

    /**
     * 分割线的viewHolder
     */
    public class CutLineViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_cutline)
        TextView tvCutline;

        public CutLineViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(final BookCategoryListMultipleItem item) {
            tvCutline.setText("第"+item.getData()+"页");
        }
    }


    @Override
    public int getDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == ViewType.GROUP.ordinal()) {
            return ItemDragAndSwipeCallback.defualt_drag_move_flags;
        }
        return 0;
    }

    @Override
    public int getSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return 0;
    }

    @Override
    public boolean getCanMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        return true;
    }

    @Override
    public void onItemDragMovedCallBack(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);
        swapItem(from,to);
    }

    /**
     * 这个逻辑搞得头晕了
     * @param from
     * @param to
     */
    public void swapItem(int from,int to){
        if (from < to) {
            for (int i = from; i < to; i++) {
                int swapIndex=i+1;

                //遇到分割线
                if(getItemViewType(swapIndex)==ViewType.CUTLINE.ordinal()){
                    //越过分割线
                    swapIndex=swapIndex+1;

                    Collections.swap(mData, i, swapIndex);
                    i=swapIndex-1;
                    continue;
                }
                Collections.swap(mData, i, swapIndex);
            }

        } else {
            for (int i = from; i > to; i--) {
                int swapIndex=i-1;

                if(getItemViewType(swapIndex)==ViewType.CUTLINE.ordinal()&&swapIndex-1!=0){
                    swapIndex=swapIndex-1;
                    Collections.swap(mData, i, swapIndex);
                    i=swapIndex+1;
                    continue;
                }
                Collections.swap(mData, i, swapIndex);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemSwipedCallBack(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onItemSwipingCallBack(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

    }

    @Override
    public boolean isHeadOrFooterView(RecyclerView.ViewHolder viewHolder) {
        return false;
    }
}
