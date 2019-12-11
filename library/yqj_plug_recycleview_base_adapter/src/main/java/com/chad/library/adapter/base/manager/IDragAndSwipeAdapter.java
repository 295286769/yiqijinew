package com.chad.library.adapter.base.manager;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * Created by leichi on 2017/5/18.
 */

public interface IDragAndSwipeAdapter {

    //根据viewHolder获取可拖拽的Flags
    int getDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    //根据viewHolder获取可滑动的Flags
    int getSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    //根据source和target去判断是都可以移动换位置
    boolean getCanMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);
    //当拖拽产生item互相移动是的回调
    void onItemDragMovedCallBack(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);
    //当滑动item结束时的回调
    void onItemSwipedCallBack(RecyclerView.ViewHolder viewHolder);
    //item滑动过程中的回调
    void onItemSwipingCallBack(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive);
    //是否是头或者底部布局
    boolean isHeadOrFooterView(RecyclerView.ViewHolder viewHolder);
}
