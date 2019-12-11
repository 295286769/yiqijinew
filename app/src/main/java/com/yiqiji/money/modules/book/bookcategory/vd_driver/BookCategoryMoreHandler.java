package com.yiqiji.money.modules.book.bookcategory.vd_driver;

import android.content.Context;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.activity.BookCategoryEditActivity;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataController;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryListMultipleItem;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryOperationType;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.SyncCategoryResult;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategoryListAdapter;
import com.yiqiji.money.modules.book.bookcategory.view.BaseMoreHandleView;
import com.yiqiji.money.modules.common.utils.LoadingDialog;
import com.yiqiji.money.modules.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/5/22.
 */

public class BookCategoryMoreHandler {

    BaseMoreHandleView.MoreItem editMoreItem = new BaseMoreHandleView.MoreItem(BookCategoryOperationType.TO_EDIT, "编辑分类");
    BaseMoreHandleView.MoreItem disableMoreItem = new BaseMoreHandleView.MoreItem(BookCategoryOperationType.TO_DISABLE, "停用分类");
    BaseMoreHandleView.MoreItem deleteMoreItem = new BaseMoreHandleView.MoreItem(BookCategoryOperationType.TO_DELETE, "删除分类");
    BaseMoreHandleView.MoreItem enableMoreItem = new BaseMoreHandleView.MoreItem(BookCategoryOperationType.TO_ENABLE, "启用分类");

    private Context context;
    private int mInstruct;
    private int atPosition;
    private BookCategoryListMultipleItem multipleItem;
    private IBaseBookCategory mBaseBookCategory;
    private BaseMoreHandleView baseMoreHandleView;
    private BookCategoryListAdapter adapter;
    private LoadingDialog loadingDialog;
    private BookCategoryOperationType bookCategoryOperationType = new BookCategoryOperationType();
    private DeleteTipDialogHandler deleteTipDialogHandler;
    public BookCategoryMoreHandler(BookCategoryListAdapter adapter, Context context) {
        this.context = context;
        this.adapter = adapter;
        baseMoreHandleView = new BaseMoreHandleView(context);
        baseMoreHandleView.setOnItemClickCallBack(onItemClickCallBack);
        deleteTipDialogHandler = new DeleteTipDialogHandler(context);
    }


    private void initDialog(Context context) {
        loadingDialog = new LoadingDialog(context, R.layout.dialog_layout, R.style.DialogTheme);
        loadingDialog.setCancelable(false);
    }

    /***
     * 加载数据dialog
     */
    public void showLoadingDialog() {
        this.dismissDialog();
        if (loadingDialog == null) {
            initDialog(context);
        }
        loadingDialog.show();
    }


    /***
     * dismiss所有正在显示的对话框
     */
    public void dismissDialog() {
        if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
            this.loadingDialog.dismiss();
        }
    }

    public void showMoreHandlerView(BookCategoryListMultipleItem item, View tagView, int atPosition) {
        this.multipleItem = item;
        this.mBaseBookCategory = (IBaseBookCategory) item.getData();
        this.atPosition = atPosition;
        List<BaseMoreHandleView.MoreItem> moreItemList = getMoreItem(mBaseBookCategory);
        baseMoreHandleView.setMoreItemList(moreItemList);
        baseMoreHandleView.show(tagView);
    }


    private List<BaseMoreHandleView.MoreItem> getMoreItem(IBaseBookCategory bookGroupCategoryModel) {
        List<BaseMoreHandleView.MoreItem> moreItemList = new ArrayList<>();
        if (bookGroupCategoryModel.getStatus().equals("0")) {                       //已停用的
            moreItemList.add(editMoreItem);
            moreItemList.add(enableMoreItem);
            moreItemList.add(deleteMoreItem);
        } else {                                                                    //已启用
            moreItemList.add(editMoreItem);
            moreItemList.add(disableMoreItem);
            moreItemList.add(deleteMoreItem);
        }
        return moreItemList;
    }

    public BaseMoreHandleView.OnItemClickCallBack onItemClickCallBack = new BaseMoreHandleView.OnItemClickCallBack() {
        @Override
        public void onClick(BaseMoreHandleView.MoreItem moreItem) {
            mInstruct = moreItem.instruct;
            switch (moreItem.instruct) {
                case BookCategoryOperationType.TO_EDIT:
                    //跳转编辑页面
                    if (multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                        BookCategoryEditActivity.openEditGroup(context, (IBaseBookCategory) multipleItem.getData());
                    } else {
                        BookCategoryEditActivity.openEditChild(context, (IBaseBookCategory) multipleItem.getData());
                    }
                    break;
                case BookCategoryOperationType.TO_DISABLE:
                    //停用分类
                    if (multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                        int groupItemCount = adapter.getEnableGroupCount();
                        if (groupItemCount == 1) {
                            ToastUtils.DiyToast(context, "至少保留一个大类");
                            return;
                        }
                    }
                    showLoadingDialog();
                    DataController.disableCategory(mBaseBookCategory, viewCallBack);
                    break;
                case BookCategoryOperationType.TO_DELETE:
                    //删除分类
                    boolean isGroup = multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal();
                    if (isGroup) {
                        int groupItemCount = adapter.getEnableGroupCount();
                        if (groupItemCount == 1) {
                            ToastUtils.DiyToast(context, "至少保留一个大类");
                            return;
                        }
                    }
                    deleteTipDialogHandler.showRetryDialog(isGroup);
                    deleteTipDialogHandler.setConfirmDeleteclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoadingDialog();
                            DataController.deleteCategory(mBaseBookCategory, viewCallBack);
                        }
                    });
                    break;
                case BookCategoryOperationType.TO_ENABLE:
                    //启用分类
                    showLoadingDialog();
                    DataController.enableCategory(mBaseBookCategory, viewCallBack);
                    break;
            }

            bookCategoryOperationType.setOperationType(moreItem.instruct);
            EventBus.getDefault().post(bookCategoryOperationType);
        }
    };

    ViewCallBack<SyncCategoryResult> viewCallBack = new ViewCallBack<SyncCategoryResult>() {
        @Override
        public void onSuccess(SyncCategoryResult syncCategoryResult) throws Exception {
            super.onSuccess(syncCategoryResult);
            dismissDialog();
            switch (mInstruct) {
                case BookCategoryOperationType.TO_DISABLE:
                    mBaseBookCategory.setStatus("0");
                    //这里需要分出是一级还是二级，一级要移动item,二级不需要。
                    if (multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                        List<BookCategoryListMultipleItem> subItems = multipleItem.getSubItems();
                        adapter.getData().remove(multipleItem);
                        if (multipleItem.isExpanded()) {
                            for (BookCategoryListMultipleItem item : subItems) {
                                adapter.getData().remove(item);
                            }
                        }
                        adapter.getData().add(multipleItem);
                        if (multipleItem.isExpanded()) {
                            for (BookCategoryListMultipleItem item : subItems) {
                                adapter.getData().add(item);
                            }
                        }
                        adapter.notifyItemRangeChanged(atPosition, adapter.getItemCount() - atPosition);
                    } else {
                        adapter.notifyItemChanged(atPosition);
                    }

                    break;
                case BookCategoryOperationType.TO_ENABLE:
                    mBaseBookCategory.setStatus("1");
                    //这里需要分出是一级还是二级，一级要移动item,二级不需要。
                    if (multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                        List<BookCategoryListMultipleItem> subItems = multipleItem.getSubItems();
                        adapter.getData().remove(multipleItem);
                        if (multipleItem.isExpanded()) {
                            for (BookCategoryListMultipleItem item : subItems) {
                                adapter.getData().remove(item);
                            }
                        }

                        int lastEnableItemIndex = getLastEnbaleItemIndex();
                        adapter.getData().add(lastEnableItemIndex, multipleItem);
                        if (multipleItem.isExpanded()) {
                            for (BookCategoryListMultipleItem item : subItems) {
                                lastEnableItemIndex++;
                                adapter.getData().add(lastEnableItemIndex, item);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemChanged(atPosition);
                    }
                    break;
                case BookCategoryOperationType.TO_DELETE:
                    if (multipleItem.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal()) {
                        int count = 1;
                        List<BookCategoryListMultipleItem> subItems = multipleItem.getSubItems();
                        adapter.getData().remove(multipleItem);
                        if (multipleItem.isExpanded() && subItems != null) {
                            adapter.getData().removeAll(subItems);
                            if (subItems != null && subItems.size() > 0) {
                                count = count + subItems.size();
                            }
                        }
                        adapter.notifyItemRangeRemoved(atPosition, count);
                    } else {
                        BookCategoryListMultipleItem parent = multipleItem.getParent();
                        parent.getSubItems().remove(multipleItem);

                        adapter.getData().remove(atPosition);
                        adapter.notifyItemRangeRemoved(atPosition, 1);
                    }
                    break;
            }
            ToastUtils.DiyToast(context, "操作成功");
        }


        @Override
        public void onFailed(SimpleMsg simleMsg) {
            super.onFailed(simleMsg);
            dismissDialog();
            ToastUtils.DiyToast(context, "操作失败");
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    };


    private int getLastEnbaleItemIndex() {
        int lastEnableItemIndex = 0;
        List<BookCategoryListMultipleItem> items = adapter.getData();
        for (int i = 0; i < items.size(); i++) {
            BookCategoryListMultipleItem item = items.get(i);
            IBaseBookCategory baseBookCategory = (IBaseBookCategory) item.getData();
            if (item.getItemType() == BookCategoryListMultipleItem.ViewType.GROUP.ordinal() && baseBookCategory.getStatus().equals("0")) {
                break;
            }
            lastEnableItemIndex = i;
        }

        return lastEnableItemIndex;
    }
}
