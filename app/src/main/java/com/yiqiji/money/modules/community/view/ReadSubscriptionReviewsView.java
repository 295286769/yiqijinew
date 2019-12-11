package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/28.
 * address huang.weishui@71dai.com
 */
public class ReadSubscriptionReviewsView extends LinearLayout {
    @BindView(R.id.read_subscription_reviews_layout)
    LinearLayout readSubscriptionReviewsLayout;
    @BindView(R.id.tv_read_number)
    TextView tvReadNumber;//阅读数
    @BindView(R.id.tv_subscription_number)
    TextView tvSubscriptionNumber;//订阅数
    @BindView(R.id.tv_reviews_number)
    TextView tvReviewsNumber;//评论数
    private Context mContext;

    public ReadSubscriptionReviewsView(Context context) {
        this(context, null);
        mContext = context;
    }

    public ReadSubscriptionReviewsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public ReadSubscriptionReviewsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_read_subscription_reviews_yout, this);
        ButterKnife.bind(this);
    }

    public void setReadSubscriptionReviews(BookCellModel bookCellModel) {
        if (readSubscriptionReviewsLayout != null) {
            tvReadNumber.setText(bookCellModel.getViewcount()+"");
            tvSubscriptionNumber.setText(bookCellModel.getFollownum()+"");
            tvReviewsNumber.setText(bookCellModel.getCommentcount()+"");
        }
    }
}
