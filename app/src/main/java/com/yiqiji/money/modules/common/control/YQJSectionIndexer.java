package com.yiqiji.money.modules.common.control;

import android.widget.SectionIndexer;

import java.util.Arrays;

/**
 * Created by dansakai on 2017/3/10.
 */

public class YQJSectionIndexer implements SectionIndexer{
    private final String[] mSections;// 例如 ["国产"，"进口"]   ，国产有8个，进口有13个
    private final int[] mPositions;// [国产第一个对应的位置0,进口第一个对应的位置8]
    private final int mCount;// 国产和进口总个数 8+13 = 21

    /**
     * 通过sections和对应的个数，算出mPostions和mCount
     *  @param sections      ["国产","进口"]
     * @param counts        [8,13]
     * @param extraTopCount 头部非section部分个数
     */
    public YQJSectionIndexer(String[] sections, int[] counts, int extraTopCount) {
        if (sections == null || counts == null) {
            throw new NullPointerException();
        }
        if (sections.length != counts.length) {
            throw new IllegalArgumentException(
                    "The sections and counts arrays must have the same length");
        }
        this.mSections = sections;
        mPositions = new int[counts.length];

        int position = 0;
        if (extraTopCount > 0) {
            position += extraTopCount;
        }
        for (int i = 0; i < counts.length; i++) {
            if (mSections[i] == null) {
                mSections[i] = "";
            } else {
                mSections[i] = mSections[i].trim();
            }

            mPositions[i] = position;
            position += counts[i];

        }
        mCount = position;
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    /**
     * 根据mSection中的位置获取，如果超过mSection的位置，返回-1，否则返回对应的个数，例如，获取section
     */
    @Override
    public int getPositionForSection(int section) {
        if (section < 0 || section >= mSections.length) {
            return -1;
        }
        return mPositions[section];
    }

    /**
     * 根据列表中的位置，设置获取section对应的位置
     */
    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mCount) {
            return -1;
        }
        // 注意这个方法的返回值，它就是index<0时，返回-index-2的原因
        // 解释Arrays.binarySearch，如果搜索结果在数组中，刚返回它在数组中的索引，如果不在，则返回第一个比它大的索引的负数-1
        // 如果没弄明白，请自己想查看api
        // 二分搜索法：mPositions必须顺序，如果它包含在数组中，则返回搜索键的索引；否则返回 (-(插入点：第一 个大于此键的 元素索引) - 1)
        int index = Arrays.binarySearch(mPositions, position);
        return index >= 0 ? index : -index - 2; // 当index小于0时，返回-index-2，

    }

    /**
     * 获取第一个显示mHeadView的位置
     *
     * @return
     */
    public int getFirstVisiblePosition() {
        if (mPositions != null && mPositions.length > 0) {
            return mPositions[0];
        } else {
            return -1;
        }
    }
}
