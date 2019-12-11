package com.ecloud.pulltozoomview;

public interface OnListViewScrollListenner {
    void onScroll(int i, int firstVisibleItem, int visibleItemCount,
                  int totalItemCount);
}
