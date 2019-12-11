package com.yiqiji.money.modules.community.utils;

import android.content.Context;
import android.content.Intent;

import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.detailinfo.activity.BookCommentListActivity;
import com.yiqiji.money.modules.community.discover.activity.DiscoverOtherActivity;
import com.yiqiji.money.modules.community.travel.activity.HotDestinationsActivity;
import com.yiqiji.money.modules.community.travel.activity.RaidersActivity;
import com.yiqiji.money.modules.community.travel.activity.TravelMainActivity;

/**
 * Created by leichi on 2017/8/2.
 */

public class StartActivityUtil {


    /**
     * 社区其他账本列表页面
     *
     * @param context
     */
    public static void startDiscoverOther(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DiscoverOtherActivity.class);
        context.startActivity(intent);
    }

    /**
     * 装修公司评论
     *
     * @param context
     * @param decorationId
     */
    public static void startDecorationComment(Context context, String decorationId, boolean showInput) {
        Intent intent = new Intent();
        intent.setClass(context, BookCommentListActivity.class);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_ID, decorationId);
        intent.putExtra(DataConstant.BUNDLE_KEY_SHOW_INPUT_BOARD, showInput);
        context.startActivity(intent);
    }


    /**
     * 外部打开旅游首页
     *
     * @param context
     */
    public static void startTravelMain(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TravelMainActivity.class);
        context.startActivity(intent);
    }

    public static void startHotDestinationsActivity(Context context, String city) {
        Intent intent = new Intent(context, HotDestinationsActivity.class);
        intent.putExtra("city", city);
        context.startActivity(intent);
    }

    public static void startHotDestinationsActivity(Context context, String city, String title_name, int position) {
        Intent intent = new Intent(context, RaidersActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("position", position);
        intent.putExtra("title", title_name);
        context.startActivity(intent);
    }
}
