package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by leichi on 2017/6/15.
 */

public class ReplayListView extends LinearLayout {

    Context mContext;

    public ReplayListView(Context context) {
        super(context);
        mContext = context;
    }

    public ReplayListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void bindViewData(List<BookCommentModel> child) {
        removeAllViews();
        for (int i=0;i<child.size();i++){
            BookCommentModel bookCommentModel =child.get(i);
            addView(getReplayItemView(bookCommentModel));
        }
    }

    private View getCommentItemView() {
        TextView textView = (TextView) View.inflate(mContext, R.layout.item_replay_thum_textview, null);

        return textView;
    }

    private View getReplayItemView(BookCommentModel child) {
        TextView textView = (TextView) View.inflate(mContext, R.layout.item_replay_thum_textview, null);
        String commnetUser= StringUtils.addBlueColor(child.username);
        String toRelayUser=StringUtils.addBlueColor("@"+child.tousername);
        //String commnet=commnetUser+"回复"+toRelayUser+"："+child.content;
        String commnet=commnetUser+"："+child.content;
        textView.setText(Html.fromHtml(commnet));
        return textView;
    }



    private View getCountCommetItemView(){
        TextView textView = (TextView) View.inflate(mContext, R.layout.item_replay_thum_textview, null);
        return textView;
    }
}
