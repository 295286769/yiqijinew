package com.chad.library.adapter.base;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.chad.library.R;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseMultiItemQuickAdapter<T extends MultiItemEntity, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    /**
     * layouts indexed with their types
     */
    private SparseArray<ViewWrap> layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public BaseMultiItemQuickAdapter( List<T> data) {
        super( data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof MultiItemEntity) {
            return ((MultiItemEntity)item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ViewWrap viewWrap=getViewWrap(viewType);
        if(viewWrap.kClass==null){
            return createBaseViewHolder(parent, viewWrap.layoutResId);
        }
        return createBaseViewHolder(parent, viewWrap.layoutResId,viewWrap.kClass);
    }

    private ViewWrap getViewWrap(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, new ViewWrap(layoutResId));
    }


    protected void addItemType(int type, @LayoutRes int layoutResId,Class kClass ) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        if(!BaseViewHolder.class.isAssignableFrom(kClass)){
            throw new ClassCastException();
        }

        layouts.put(type, new ViewWrap(layoutResId, kClass));
    }

    public class ViewWrap {
        public @LayoutRes int layoutResId;
        public Class kClass;

        public ViewWrap(@LayoutRes int layoutResId){
            this.layoutResId= layoutResId;
        }
        public ViewWrap(@LayoutRes int layoutResId,Class kClass){
            this.layoutResId=layoutResId;
            this.kClass=kClass;
        }
    }
}


