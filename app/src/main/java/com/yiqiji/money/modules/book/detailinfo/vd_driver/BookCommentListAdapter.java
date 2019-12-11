package com.yiqiji.money.modules.book.detailinfo.vd_driver;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.BookCommentModel;
import com.yiqiji.money.modules.book.detailinfo.view.CommentMoreOperationHandler;
import com.yiqiji.money.modules.book.detailinfo.view.ReplayListView;
import com.yiqiji.money.modules.book.detailinfo.view.ReplyCommentViewHandler;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.utils.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/6/15.
 */

public class BookCommentListAdapter extends BaseQuickAdapter<BookCommentModel, BookCommentListAdapter.ViewHolder> {

    Context mContext;
    ReplyCommentViewHandler replyCommentViewHandler;

    public BookCommentListAdapter(Context context) {
        super(R.layout.item_book_comment_view);
        mContext = context;
        replyCommentViewHandler = new ReplyCommentViewHandler(mContext);
    }

    @Override
    protected void convert(ViewHolder helper, BookCommentModel item) {
        helper.bindViewData(item);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_commenter)
        TextView tvCommenter;
        @BindView(R.id.tv_commentDate)
        TextView tvCommentDate;
        @BindView(R.id.img_replay)
        ImageView imgReplay;
        @BindView(R.id.tv_commentText)
        TextView tvCommentText;
        @BindView(R.id.replay_thum)
        ReplayListView replayThum;
        @BindView(R.id.img_commentUserIcon)
        ImageView imgCommentUserIcon;
        @BindView(R.id.linear_content)
        LinearLayout linearContent;

        BookCommentModel replyBookCommentModel;
        CommentMoreOperationHandler commentMoreOperationHandler;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            replyCommentViewHandler.setPopWindowParentView(view);
            commentMoreOperationHandler = new CommentMoreOperationHandler(view.getContext(),replyCommentViewHandler);
            initEvent();
        }

        private void initEvent() {
            imgReplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyCommentViewHandler.showView(replyBookCommentModel);
                }
            });

            linearContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentMoreOperationHandler.showMoreOperationDialog();
                }
            });
        }

        public void bindViewData(BookCommentModel bookCommentModel) {
            this.replyBookCommentModel = bookCommentModel;
            tvCommenter.setText(bookCommentModel.username);
            tvCommentDate.setText(DateUtil.formatCommentTime(bookCommentModel.ctime));
            ImageLoaderManager.loadCircleImage(mContext, bookCommentModel.usericon, imgCommentUserIcon);
            tvCommentText.setText(bookCommentModel.content);
            if (bookCommentModel.child != null) {
                replayThum.setVisibility(View.VISIBLE);
                replayThum.bindViewData(bookCommentModel.child);
            } else {
                replayThum.setVisibility(View.GONE);
            }

            if (bookCommentModel.userid.equals(LoginConfig.getInstance().getUserid())) {
                imgReplay.setVisibility(View.VISIBLE);
                linearContent.setClickable(true);
            } else if (bookCommentModel.child != null && bookCommentModel.child.size() > 0 && bookCommentModel.username.equals(LoginConfig.getInstance().getUserName())) {
                imgReplay.setVisibility(View.VISIBLE);
                replyBookCommentModel = bookCommentModel.child.get(bookCommentModel.child.size() - 1);
                replyBookCommentModel.isChild = true;
                linearContent.setClickable(true);
            } else {
                imgReplay.setVisibility(View.GONE);
                linearContent.setClickable(false);
            }

            commentMoreOperationHandler.initListItemDialog(bookCommentModel);

        }
    }
}
