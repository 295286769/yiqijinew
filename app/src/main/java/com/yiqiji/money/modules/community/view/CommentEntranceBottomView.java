package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.activity.BookCommentListActivity;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.view.BadgeView;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/8/2.
 */

public class CommentEntranceBottomView extends LinearLayout {

    public static final int TYPE_DECORATION=0;      //装修公司评论
    public static final int TYPE_BOOK=1;            //账本评论

    Context context;
    int commentType;
    String key;

    @BindView(R.id.image_message)
    ImageView imageMessage;
    @BindView(R.id.notes)
    BadgeView notes;
    @BindView(R.id.rela_message)
    RelativeLayout relaMessage;
    @BindView(R.id.rela_toComment)
    RelativeLayout relaToComment;
    @BindView(R.id.rela_bottom)
    RelativeLayout relaBottom;


    public CommentEntranceBottomView(Context context) {
        this(context, null);
    }

    public CommentEntranceBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_comment_entrance_bottom_layout, this);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(true);
            }
        });

        relaMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(false);
            }
        });
    }

    public void postEvent(boolean isShowInput){
        switch (commentType){
            case TYPE_DECORATION:
                StartActivityUtil.startDecorationComment(context,key,isShowInput);
                break;
            case TYPE_BOOK:
                BookCommentListActivity.open(context,key,isShowInput);
                break;
        }
    }

    public void initViewData(int commentType,String key,int commentCount){
        this.commentType=commentType;
        initUnReadCount(key,commentCount);
    }

    private void initUnReadCount(String key,int commentCout) {
        this.key=key;
        int unReadCommentCount = BookDataManager.getUnReadCommentCount(key, commentCout);
        if (unReadCommentCount <= 0) {
            notes.setVisibility(View.GONE);
        } else {
            notes.setVisibility(View.VISIBLE);
            notes.setText(unReadCommentCount + "");
        }
    }
}
