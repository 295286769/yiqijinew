/***
 * Copyright (c) 2008-2009 CommonsWare, LLC
 * Portions (c) 2009 Google, Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yiqiji.money.modules.common.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that simply returns row views from a list.
 *
 * If you supply a size, you must implement newView(), to create a required
 * view. The adapter will then cache these views.
 *
 * If you supply a list of views in the constructor, that list will be used
 * directly. If any elements in the list are null, then newView() will be called
 * just for those slots.
 *
 * Subclasses may also wish to override areAllItemsEnabled() (default: false)
 * and isEnabled() (default: false), if some of their rows should be selectable.
 *
 * It is assumed each view is unique, and therefore will not get recycled.
 *
 * Note that this adapter is not designed for long lists. It is more for screens
 * that should behave like a list. This is particularly useful if you combine
 * this with other adapters (e.g., SectionedAdapter) that might have an
 * arbitrary number of rows, so it all appears seamless.
 */
public class SackOfViewsAdapter extends BaseAdapter {

    private List<View> views = null;// 绑定的集合

    /**
     * 构造函数
     */
    public SackOfViewsAdapter(int count) {
        super();

        views = new ArrayList<View>(count);

        for (int i = 0; i < count; i++) {
            views.add(null);
        }
    }

    /**
     * 构造函数，传入View集合
     */
    public SackOfViewsAdapter(List<View> views) {
        super();

        this.views = views;
    }

    @Override
    public Object getItem(int position) {
        return (views.get(position));
    }

    @Override
    public int getCount() {
        return (views.size());
    }

    @Override
    public int getViewTypeCount() {
        return (getCount());
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean isEnabled(int position) {
        return (false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = views.get(position);// 获取当前位置的View

        if (result == null) {
            result = newView(position, parent);
            views.set(position, result);
        }

        return (result);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    /**
     * Create a new View to go into the list at the specified position.
     *
     * @param position
     *            Position of the item whose data we want
     * @param parent
     *            ViewGroup containing the returned View
     */
    protected View newView(int position, ViewGroup parent) {
        throw new RuntimeException("You must override newView()!");
    }
}