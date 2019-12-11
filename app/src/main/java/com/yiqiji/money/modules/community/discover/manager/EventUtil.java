package com.yiqiji.money.modules.community.discover.manager;

import android.content.Context;
import android.content.Intent;

import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.Found.view.BaseWebView;
import com.yiqiji.money.modules.common.activity.WebActivity;
import com.yiqiji.money.modules.community.model.BookCellModel;

/**
 * Created by leichi on 2017/8/1.
 */

public class EventUtil {

    public static void OnBookItemClick(Context context,BookCellModel bookCellModel){
        if(StringUtils.isEmpty(bookCellModel.getUrl())){
            return;
        }
        if (BaseWebView.overrideUrlLoading(context, bookCellModel.getUrl())) {
            return;
        }
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra("url", bookCellModel.getUrl());
        in.putExtra("title", bookCellModel.getTitle());
        context.startActivity(in);
    }
}
