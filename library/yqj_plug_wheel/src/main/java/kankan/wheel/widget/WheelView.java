/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 * 
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package kankan.wheel.widget;

import java.util.LinkedList;
import java.util.List;

import com.mrwujay.cascade.R;

import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

/**
 * Numeric wheel view.
 *
 * @author Yuri Kanivets
 */
public class WheelView extends View {

    /**
     * Top and bottom shadows colors
     */
    /*
     * / Modified by wulianghuan 2014-11-25 private int[] SHADOWS_COLORS = new
     * int[] { 0xFF111111, 0x00AAAAAA, 0x00AAAAAA }; //
     */
//    private int[] SHADOWS_COLORS = new int[] { 0xefE9E9E9, 0xcfE9E9E9,
//	    0x3fE9E9E9 };//灰色的背景

    private int[] SHADOWS_COLORS = new int[]{0xefFFFFFF, 0xcfFFFFFF,
            0x3fFFFFFF};//白色的背景

    /**
     * Top and bottom items offset (to hide that)
     */
    private static final int ITEM_OFFSET_PERCENT = 0;

    /**
     * Left and right padding value
     */
    private static final int PADDING = 10;

    /**
     * Default count of visible items
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Wheel Values,当前滚轮显示的第几项例如，显示第一个省份，则为0，显示第二个省份则为1，以此类推
    private int currentItem = 0;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    // Item height
    private int itemHeight = 0;

    // Center Line
    private Drawable centerDrawable;

    // Wheel drawables
    private int wheelBackground = R.drawable.wheel_bg;//控件背景，可以自己定义
    //    private int wheelBackground = android.R.color.white;//控件背景，可以自己定义
    private int wheelForeground = R.drawable.wheel_val;

    // Shadows drawables
    private GradientDrawable topShadow;
    private GradientDrawable bottomShadow;

    // Draw Shadows ,true显示透明度效果
    private boolean drawShadows = true;

    // Scrolling
    private WheelScroller scroller;
    private boolean isScrollingPerformed;
    private int scrollingOffset;//滚动偏移量

    // Cyclic
    boolean isCyclic = false;

    // Items layout
    private LinearLayout itemsLayout;

    // The number of first item in layout，再Layout布局中显示的第一项的位置
    private int firstItem;

    // View adapter
    private WheelViewAdapter viewAdapter;

    // Recycle
    private WheelRecycle recycle = new WheelRecycle(this);

    // Listeners
    private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
    private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * Initializes class data
     *
     * @param context the context
     */
    private void initData(Context context) {
        scroller = new WheelScroller(getContext(), scrollingListener);
    }

    // Scrolling listener
    WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);

            int height = getHeight();
            if (scrollingOffset > height) {
                scrollingOffset = height;
                scroller.stopScrolling();
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height;
                scroller.stopScrolling();
            }
        }

        @Override
        public void onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }

            scrollingOffset = 0;
            invalidate();
        }

        @Override
        public void onJustify() {
            // 滚多了，则滚回来
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller.scroll(scrollingOffset, 0);
            }
        }
    };

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.setInterpolator(interpolator);
    }

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * Sets the desired count of visible items. Actual amount of visible items
     * depends on wheel layout parameters. To apply changes and rebuild view
     * call measure().
     *
     * @param count the desired count for visible items
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
    }

    /**
     * Gets view adapter
     *
     * @return the view adapter
     */
    public WheelViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    /**
     * 获取当前项显示的文案
     */
    public String getSelectedItemText() {
        if (viewAdapter != null) {
            if (viewAdapter instanceof NumericWheelAdapter) {
                NumericWheelAdapter adapter = (NumericWheelAdapter) viewAdapter;
                return adapter.getItemText(getCurrentItem()).toString();
            } else {
                ArrayWheelAdapter adapter = (ArrayWheelAdapter) viewAdapter;
                return adapter.getItemText(getCurrentItem()).toString();
            }
        }

        return null;
    }

    /**
     * 获取当前项显示的文案
     */
    public String getItemText(int index) {
        if (viewAdapter != null && index >= 0 && index < viewAdapter.getItemsCount()) {
            if (viewAdapter instanceof NumericWheelAdapter) {
                NumericWheelAdapter adapter = (NumericWheelAdapter) viewAdapter;
                return adapter.getItemText(index).toString();
            }
        }

        return null;
    }

    // Adapter listener
    private DataSetObserver dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidateWheel(false);
        }

        @Override
        public void onInvalidated() {
            invalidateWheel(true);
        }
    };

    /**
     * Sets view adapter. Usually new adapters contain different views, so it
     * needs to rebuild view by calling measure().
     *
     * @param viewAdapter the view adapter
     */
    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterDataSetObserver(dataObserver);
        }
        this.viewAdapter = viewAdapter;
        if (this.viewAdapter != null) {
            this.viewAdapter.registerDataSetObserver(dataObserver);
        }

        invalidateWheel(true);
    }

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * Removes wheel changing listener
     *
     * @param listener the listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * Adds wheel clicking listener
     *
     * @param listener the listener
     */
    public void addClickingListener(OnWheelClickedListener listener) {
        clickingListeners.add(listener);
    }

    /**
     * Removes wheel clicking listener
     *
     * @param listener the listener
     */
    public void removeClickingListener(OnWheelClickedListener listener) {
        clickingListeners.remove(listener);
    }

    /**
     * Notifies listeners about clicking
     */
    protected void notifyClickListenersAboutClick(int item) {
        for (OnWheelClickedListener listener : clickingListeners) {
            listener.onItemClicked(this, item);
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    public void setCurrentItem(int index, boolean animated) {
        if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
            return; // throw?
        }

        int itemCount = viewAdapter.getItemsCount();
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount;
                }
                index %= itemCount;
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                int itemsToScroll = index - currentItem;
                if (isCyclic) {
                    int scroll = itemCount + Math.min(index, currentItem)
                            - Math.max(index, currentItem);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
            } else {
                scrollingOffset = 0;

                int old = currentItem;//将当前项设置oldvalue
                currentItem = index;//将新的当前项设置为newValue

                notifyChangingListeners(old, currentItem);

                invalidate();
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown
     * the last one
     *
     * @return true if wheel is cyclic
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidateWheel(false);
    }

    /**
     * Determine whether shadows are drawn
     *
     * @return true is shadows are drawn
     */
    public boolean drawShadows() {
        return drawShadows;
    }

    /**
     * Set whether shadows should be drawn
     *
     * @param drawShadows flag as true or false
     */
    public void setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
    }

    /**
     * Set the shadow gradient color
     *
     * @param start
     * @param middle
     * @param end
     */
    public void setShadowColor(int start, int middle, int end) {
        SHADOWS_COLORS = new int[]{start, middle, end};
    }

    /**
     * Sets the drawable for the wheel background
     *
     * @param resource
     */
    public void setWheelBackground(int resource) {
        wheelBackground = resource;
        setBackgroundResource(wheelBackground);
    }

    /**
     * Sets the drawable for the wheel foreground
     *
     * @param resource
     */
    public void setWheelForeground(int resource) {
        wheelForeground = resource;
        centerDrawable = getContext().getResources().getDrawable(
                wheelForeground);
    }

    /**
     * Invalidates wheel
     *
     * @param clearCaches if true then cached views will be clear
     */
    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            recycle.clearAll();
            if (itemsLayout != null) {
                itemsLayout.removeAllViews();
            }
            scrollingOffset = 0;
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        }

        invalidate();
    }

    /**
     * Initializes resources
     */
    private void initResourcesIfNecessary() {
        if (centerDrawable == null) {
            centerDrawable = getContext().getResources().getDrawable(
                    wheelForeground);
        }

        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM,
                    SHADOWS_COLORS);
        }

        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP,
                    SHADOWS_COLORS);
        }

        setBackgroundResource(wheelBackground);
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout the source layout
     * @return the desired layout height
     */
    private int getDesiredHeight(LinearLayout layout) {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }

        int desired = itemHeight * visibleItems - itemHeight
                * ITEM_OFFSET_PERCENT / 50;

        return Math.max(desired, getSuggestedMinimumHeight());
    }

    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        }

        if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
            itemHeight = itemsLayout.getChildAt(0).getHeight();
            return itemHeight;
        }

        return getHeight() / visibleItems;
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();//初始化资源，设置控件背景

        // TODO: make it static
        itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        itemsLayout
                .measure(MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
                        0, MeasureSpec.UNSPECIFIED));
        int width = itemsLayout.getMeasuredWidth();

        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width += 2 * PADDING;

            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }

        itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING,
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED));

        return width;
    }

    /**
     * 译解widthHeightSpec和heightMeasureSpec值，并计算出合适的高度和宽度值
     * <p/>
     * MeasureSpec.EXACTLY-        1073741824    specSize 代表的是精确的尺寸   FILL_PARENT/MATCH_PARENT或者精确值
     * MeasureSpec.AT_MOST-        -2147483648   specSize 代表的是最大可获得的空间   wrap_content
     * MeasureSpec.UNSPECIFIED-    0             对于控件尺寸来说，没有任何参考意义
     * View的onMeasure方法默认行为是当模式为UNSPECIFIED时，设置尺寸为mMinWidth(通常为0)或者背景drawable的最小尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        buildViewForMeasuring();//向LinearLayout中添加显示item，初始化时，又4个元素，0,1,2,3

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    /**
     * Sets layouts width and height，设置LinearLayout的layout
     *
     * @param width  the layout width
     * @param height the layout height
     */
    private void layout(int width, int height) {
        int itemsWidth = width - 2 * PADDING;

        itemsLayout.layout(0, 0, itemsWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
            updateView();//补齐view

            drawItems(canvas);//画项
            drawCenterRect(canvas);//画线
        }

        //画阴影
        if (drawShadows)
            drawShadows(canvas);
    }

    /**
     * Draws shadows on top and bottom of control
     *
     * @param canvas the canvas for drawing
     */
    private void drawShadows(Canvas canvas) {
    /*
	 * / Modified by wulianghuan 2014-11-25 int height = (int)(1.5 *
	 * getItemHeight()); //
	 */
        int height = (int) (3 * getItemHeight());
        // 画前三项层
        topShadow.setBounds(0, 0, getWidth(), height);
        topShadow.draw(canvas);

        bottomShadow
                .setBounds(0, getHeight() - height, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * Draws items
     *
     * @param canvas the canvas for drawing
     */
    private void drawItems(Canvas canvas) {
        canvas.save();

        int top = (currentItem - firstItem) * getItemHeight()
                + (getItemHeight() - getHeight()) / 2;
        canvas.translate(PADDING, -top + scrollingOffset);//有偏移量，则平移，顺便把padding也平移了
//	Toast.makeText(getContext(), top +","+scrollingOffset, 100).show();
//	Toast.makeText(getContext(), currentItem+","+firstItem+","+String.valueOf(-top + scrollingOffset), 100).show();
//	((MainActivity)getContext()).AddText(top +","+scrollingOffset+","+currentItem+","+firstItem);
        itemsLayout.draw(canvas);

        canvas.restore();
    }

    /**
     * Draws rect for current value
     *
     * @param canvas the canvas for drawing
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (getItemHeight() / 2 * 1.2);
	/*
	 * / Remarked by wulianghuan 2014-11-27 使用自己的画线，而不是描边 Rect rect = new
	 * Rect(left, top, right, bottom) centerDrawable.setBounds(bounds)
	 * centerDrawable.setBounds(0, center - offset, getWidth(), center +
	 * offset); centerDrawable.draw(canvas); //
	 */
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#C7C7C7"));//设置线的颜色
        // 设置线宽
        paint.setStrokeWidth((float) 3);
        // 绘制上边直线
        canvas.drawLine(0, center - offset, getWidth(), center - offset, paint);
        // 绘制下边直线
        canvas.drawLine(0, center + offset, getWidth(), center + offset, paint);
        // */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //让父容器不要拦截move的touch事件，知道事件结束
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isScrollingPerformed) {
                    int distance = (int) event.getY() - getHeight() / 2;
                    if (distance > 0) {
                        distance += getItemHeight() / 2;
                    } else {
                        distance -= getItemHeight() / 2;
                    }
                    int items = distance / getItemHeight();
                    if (items != 0 && isValidItemIndex(currentItem + items)) {
                        notifyClickListenersAboutClick(currentItem + items);
                    }
                }
                break;
        }

        return scroller.onTouchEvent(event);
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;

        int itemHeight = getItemHeight();
        int count = scrollingOffset / itemHeight;//距离当前位置的偏移项，负数，则当前项向下滑动，上面会多出的项数

        int pos = currentItem - count;//滚动之后应该停留的位置
        int itemCount = viewAdapter.getItemsCount();//31

        int fixPos = scrollingOffset % itemHeight;//滚动之后相对pos的位移
        if (Math.abs(fixPos) <= itemHeight / 2) {//如果相对pos的偏移量小于单项的一般高度，则忽略fixPos
            fixPos = 0;
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else {
            //
            if (pos < 0) {//向下滚，滚动到上空白
                count = currentItem;
                pos = 0;
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1;
                pos = itemCount - 1;
            } else if (pos > 0 && fixPos > 0) {//向下滚，滚动距离为 item高度*n+(item高度的一半，item的高度)
                pos--;
                count++;
            } else if (pos < itemCount - 1 && fixPos < 0) {//如果调整后的位置在正常范围，并且 滚动之后相对pos的位移向上偏移一半的item高度，当前滚动的位置pos即将滚到下一项---向上滚，滚动距离为 item高度*n+(item高度的一半，item的高度)
                pos++;
                count--;
            }
        }

        int offset = scrollingOffset;
        if (pos != currentItem) {//滚动之后的当前项如果不等于之前的当前项，则说明滚动，否则重绘
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }

        // update offset
        scrollingOffset = offset - count * itemHeight;//设置相对最新位置的偏移量
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    /**
     * Scroll the wheel
     *
     * @param itemsToScroll items to scroll
     * @param time        scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        int distance = itemsToScroll * getItemHeight() - scrollingOffset;
        scroller.scroll(distance, time);
    }

    /**
     * Calculates range for wheel items
     * 设置当前控件的范围，初始化时currentItem=0,scrollingOffset=0，则范围区间为：[-3,-2,-1,0,1,2,3]
     * 滚动状态，scrollingOffset非0，则需要特殊处理
     *
     * @return the items range
     */
    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }

        int first = currentItem;
        int count = 1;

        while (count * getItemHeight() < getHeight()) {
            first--;
            count += 2; // top + bottom items
        }

        if (scrollingOffset != 0) {
            if (scrollingOffset > 0) {
                first--;
            }
            count++;

            // process empty items above the first or below the second
            int emptyItems = scrollingOffset / getItemHeight();
            first -= emptyItems;
            count += Math.asin(emptyItems);
        }
        return new ItemsRange(first, count);
    }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private boolean rebuildItems() {
        boolean updated = false;
        ItemsRange range = getItemsRange();
        if (itemsLayout != null) {
            int first = recycle.recycleItems(itemsLayout, firstItem, range);
            updated = firstItem != first;//判断第一项是否改变
            firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }

        //如果没有改变第一项，第一项和range的第一项是否相同或者 布局的个数时候和range相同，不相同则为改变
        if (!updated) {
            updated = firstItem != range.getFirst()
                    || itemsLayout.getChildCount() != range.getCount();
        }

        if (firstItem > range.getFirst() && firstItem <= range.getLast()) {//第一项再range中，则补齐第一项的之前的项，如果为空，则不添加，firstItem不变------range第一项到当前第一项布局，为空则不添加
            for (int i = firstItem - 1; i >= range.getFirst(); i--) {
                if (!addViewItem(i, true)) {
                    break;
                }
                firstItem = i;
            }
        } else {
            firstItem = range.getFirst();
        }

        int first = firstItem;
        //补齐，如果显示的项数少于7个，则补齐7个
        for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
            if (!addViewItem(firstItem + i, false)
                    && itemsLayout.getChildCount() == 0) {
                first++;
            }
        }
        firstItem = first;

        return updated;
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items
     * sizes.添加布局
     */
    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);//如果重构，则重新计算layout宽度
            layout(getWidth(), getHeight());
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private void createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = new LinearLayout(getContext());
            itemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    /**
     * Builds view for measuring
     */
    private void buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            //如果再次进入，则清空，缓存里面的布局
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }

        // add views
        int addItems = visibleItems / 2;
        for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                firstItem = i;
            }
        }
    }

    /**
     * Adds view for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            if (first) {
                itemsLayout.addView(view, 0);
            } else {
                itemsLayout.addView(view);
            }

            return true;
        }

        return false;
    }

    /**
     * Checks whether intem index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private boolean isValidItemIndex(int index) {
        return viewAdapter != null
                && viewAdapter.getItemsCount() > 0
                && (isCyclic || index >= 0
                && index < viewAdapter.getItemsCount());
    }

    /**
     * Returns view for specified item
     *
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private View getItemView(int index) {
        if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = viewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return viewAdapter
                    .getEmptyItem(recycle.getEmptyItem(), itemsLayout);
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        index %= count;
        return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        scroller.stopScrolling();
    }
}
