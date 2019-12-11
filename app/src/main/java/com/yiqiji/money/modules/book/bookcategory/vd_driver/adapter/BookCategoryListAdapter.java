package com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.manager.IDragAndSwipeAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.activity.BookCategoryEditActivity;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataParseAssembler;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem.ViewType;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.BookCategoryMoreHandler;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/5/17.
 */

public class BookCategoryListAdapter extends BaseMultiItemQuickAdapter<BookCategoryListMultipleItem, BaseViewHolder> implements IDragAndSwipeAdapter {

    BookCategoryMoreHandler bookCategoryMoreHandler;
    Context mContext;


    public BookCategoryListAdapter(Context context, List<BookCategoryListMultipleItem> data) {
        super(data);
        addItemType(ViewType.GROUP.ordinal(), R.layout.item_book_category_list_layout, GroupViewHolder.class);
        addItemType(ViewType.CHILD.ordinal(), R.layout.view_item, ChildViewHolder.class);
        addItemType(ViewType.ADD_BUTTON.ordinal(), R.layout.view_item, AddChildCategoryViewHolder.class);
        addItemType(ViewType.CUTLINE.ordinal(), R.layout.item_book_category_cut_line_layout, GroupViewHolder.class);
        this.mContext = context;
        bookCategoryMoreHandler = new BookCategoryMoreHandler(this, context);
        registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BookCategoryListMultipleItem item) {
        int viewType = helper.getItemViewType();
        switch (BookCategoryListMultipleItem.getViewType(viewType)) {
            case GROUP:
                GroupViewHolder groupViewHolder = (GroupViewHolder) helper;
                groupViewHolder.bindData(item);
                break;
            case CHILD:
                ChildViewHolder childViewHolder = (ChildViewHolder) helper;
                childViewHolder.bindData(item);
                break;
            case ADD_BUTTON:
                AddChildCategoryViewHolder addChildCategoryViewHolder = (AddChildCategoryViewHolder) helper;
                addChildCategoryViewHolder.bindData((IBaseBookCategory) item.getData());
                break;
            case CUTLINE:

                break;
        }
    }


    //一级分类的ViewHolder
    public class GroupViewHolder extends BaseViewHolder {
        @BindView(R.id.img_indicator)
        ImageView imgIndicator;
        @BindView(R.id.img_category_icon)
        ImageView imgCategoryIcon;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.img_more_operation)
        ImageView imgMoreOperation;
        @BindView(R.id.viewline)
        View viewline;


        public GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(final BookCategoryListMultipleItem item) {
            final BookCategory bookGroupCategoryModel = (BookCategory) item.getData();
            tvCategoryName.setText(bookGroupCategoryModel.categorytitle);
            if (bookGroupCategoryModel.isLastItem) {
                viewline.setVisibility(View.GONE);
            } else {
                viewline.setVisibility(View.VISIBLE);
            }
            if (item.isExpanded()) {
                imgIndicator.setImageResource(R.drawable.ic_indicator_down_14_26);
            } else {
                imgIndicator.setImageResource(R.drawable.ic_indicator_14_26);
            }


//              XzbUtils.displayImage(imgCategoryIcon, bookGroupCategoryModel.getIconUrl(), R.drawable.gray_ordinary);
            ImageLoaderManager.loadImage(mContext, bookGroupCategoryModel.getIconUrl(), R.drawable.gray_ordinary, imgCategoryIcon);
            //添加点击收展功能
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (item.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                }
            });

            imgMoreOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookCategoryMoreHandler.showMoreHandlerView(item, v, getAdapterPosition());
                }
            });

            if (bookGroupCategoryModel.isEnable()) {
                imgCategoryIcon.setAlpha(1.0f);
                tvCategoryName.setAlpha(1.0f);
            } else {
                imgCategoryIcon.setAlpha(0.5f);
                tvCategoryName.setAlpha(0.5f);
            }

        }
    }

    //二级分类的ViewHolder
    public class ChildViewHolder extends BaseViewHolder {

        @BindView(R.id.icon_img)
        ImageView iconImg;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.item_container)
        FrameLayout itemContainer;

        public String parentid;

        public ChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(final BookCategoryListMultipleItem item) {
            final BookCategory bookChildCategoryModel = (BookCategory) item.getData();
            nameTv.setText(bookChildCategoryModel.getNameReplaceBar());
            parentid = bookChildCategoryModel.parentid;


//            XzbUtils.displayImage(iconImg,  bookChildCategoryModel.getIconUrl(), R.drawable.gray_ordinary);

            ImageLoaderManager.loadImage(iconImg.getContext(), bookChildCategoryModel.getIconUrl(), R.drawable.gray_ordinary, iconImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookCategoryMoreHandler.showMoreHandlerView(item, v, getAdapterPosition());
                }
            });

            if (bookChildCategoryModel.isEnable()) {
                iconImg.setAlpha(1.0f);
                nameTv.setAlpha(1.0f);
            } else {
                iconImg.setAlpha(0.5f);
                nameTv.setAlpha(0.5f);
            }
        }
    }

    //添加按钮的viewHolder
    public class AddChildCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.icon_img)
        ImageView iconImg;
        @BindView(R.id.name_tv)
        TextView nameTv;

        public String parentid;
        public View contentView;

        public AddChildCategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.contentView = view;
        }

        public void bindData(final IBaseBookCategory bookGroupCategoryModel) {
            nameTv.setText("添加子类");
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到添加界面
                    BookCategoryEditActivity.openAddChild(mContext, bookGroupCategoryModel);
                }
            });
        }
    }


    @Override
    public int getDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == ViewType.CHILD.ordinal()) {
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

        if (source.getItemViewType() == ViewType.CHILD.ordinal() || target.getItemViewType() == ViewType.CHILD.ordinal()) {
            ChildViewHolder sourceViewHolder = (ChildViewHolder) source;
            ChildViewHolder targetViewHolder = (ChildViewHolder) target;
            if (sourceViewHolder.parentid.equals(targetViewHolder.parentid)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemDragMovedCallBack(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);
        swapItem(from, to);
    }

    public void swapItem(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
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

    RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);

        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            BookCategoryListMultipleItem item = getItem(fromPosition);
            if (item.getItemType() == BookCategoryListMultipleItem.ViewType.CHILD.ordinal()) {//拖动第二级分类
                BookCategoryListMultipleItem parent = item.getParent();
                int groupPosition = getParentPosition(parent);
                fromPosition = fromPosition - groupPosition - 1;
                toPosition = toPosition - groupPosition - 1;
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(parent.getSubItems(), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(parent.getSubItems(), i, i - 1);
                    }
                }
            }
        }
    };


    public int getEnableGroupCount() {
        int count = 0;
        for (int i = 0; i < getData().size(); i++) {
            BookCategoryListMultipleItem bookCategoryListMultipleItem = getData().get(i);
            boolean isGroup = bookCategoryListMultipleItem.getItemType() == ViewType.GROUP.ordinal();
            if (isGroup) {
                boolean isEnable = ((IBaseBookCategory) bookCategoryListMultipleItem.getData()).getStatus().equals("1");
                if (isEnable) {
                    count++;
                }
            }
        }
        return count;
    }

    public void insertChild(IBaseBookCategory childBookCategory) {
        int insertIndex = 0;
        for (int i = 0; i < getData().size(); i++) {
            BookCategoryListMultipleItem item = getItem(i);
            if (item.getItemType() == ViewType.GROUP.ordinal()) {
                IBaseBookCategory group = (IBaseBookCategory) item.getData();
                if (group.getCategoryid().equals(childBookCategory.getParentid())) {
                    BookCategoryListMultipleItem childItem = new BookCategoryListMultipleItem();
                    childItem.setItemType(BookCategoryListMultipleItem.ViewType.CHILD);
                    childItem.setData(childBookCategory);
                    childItem.setParent(item);

                    item.addSubItem(item.getSubItems().size() - 1, childItem);

                    insertIndex = i + item.getSubItems().size() - 1;
                    getData().add(insertIndex, childItem);
                    break;
                }
            }
        }
        notifyItemInserted(insertIndex);
    }

    public void modifyChild(IBaseBookCategory childBookCategory) {
        for (int i = 0; i < getData().size(); i++) {
            BookCategoryListMultipleItem item = getItem(i);
            if (item.getItemType() == ViewType.CHILD.ordinal()) {
                IBaseBookCategory childItem = (IBaseBookCategory) item.getData();
                if (childItem.getCategoryid().equals(childBookCategory.getCategoryid())) {
                    childItem.setCategorytitle(childBookCategory.getCategorytitle());
                    childItem.setCategoryicon(childBookCategory.getCategoryicon());
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void insertGroup(IBaseBookCategory groupBookCategory) {
        int insertIndex = 0;
        for (int i = 0; i < getData().size(); i++) {
            BookCategoryListMultipleItem item = getItem(i);
            if (item.getItemType() == ViewType.CUTLINE.ordinal()) {
                insertIndex = i;
                break;
            }
        }
        getData().add(insertIndex, DataParseAssembler.getInstance().createGroupMultipleItem(groupBookCategory));
        notifyItemInserted(insertIndex);
    }

    public void modifyGroup(IBaseBookCategory groupBookCategory) {
        for (int i = 0; i < getData().size(); i++) {
            BookCategoryListMultipleItem item = getItem(i);
            if (item.getItemType() == ViewType.GROUP.ordinal()) {
                IBaseBookCategory groupItem = (IBaseBookCategory) item.getData();
                if (groupItem.getCategoryid().equals(groupBookCategory.getCategoryid())) {
                    groupItem.setCategorytitle(groupBookCategory.getCategorytitle());
                    groupItem.setCategoryicon(groupBookCategory.getCategoryicon());
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
}
