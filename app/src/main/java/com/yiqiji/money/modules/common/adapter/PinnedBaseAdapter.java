package com.yiqiji.money.modules.common.adapter;

import android.view.View;
import android.widget.BaseAdapter;

import com.yiqiji.money.modules.common.control.PinnedHeaderListView;
import com.yiqiji.money.modules.common.control.YQJSectionIndexer;


/**
 * Created by duanyu on 2016/11/1.
 */

public abstract class PinnedBaseAdapter extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter {

    protected YQJSectionIndexer indexer;

    @Override
    public int getPinnedHeaderState(int position) {
        int realPosition = position;
        // 如果在第一个显示头部的位置之前，隐藏头部布局
        if (position <= indexer.getFirstVisiblePosition() - 1) {
            return PINNED_HEADER_GONE;
        }
        int section = indexer.getSectionForPosition(realPosition);
        int nextSectionPosition = indexer.getPositionForSection(section + 1);
        // 如果当前位置的下一个位置是下一个section的位置，则状态为Push状态，否则是显示状态
        if (nextSectionPosition != -1
                && realPosition == nextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }
        return PINNED_HEADER_VISIBLE;
    }

    @Override
    public abstract void configurePinnedHeader(View header, int position, int alpha);


}
