package com.yiqiji.money.modules.community.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.community.discover.manager.EventUtil;
import com.yiqiji.money.modules.community.model.BookCellModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/28.
 * address huang.weishui@71dai.com
 */
public class DiscoverItemBookNoImageView extends LinearLayout {
    @BindView(R.id.tv_title_content)
    TextView tvTitleContent;//标题
    @BindView(R.id.tv_content)
    TextView tvContent;//内容
    @BindView(R.id.readSubscriptionReviewsView)
    ReadSubscriptionReviewsView readSubscriptionReviewsView;//阅读订阅评论数
    private Context mContext;

    public DiscoverItemBookNoImageView(Context context) {
        this(context, null);
    }

    public DiscoverItemBookNoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscoverItemBookNoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_discover_item_book_noimage_layout, this);
        ButterKnife.bind(this, view);
        initEvent();
    }

    BookCellModel bookCellModel;
    public void setDiscoverItemBookNoImage(BookCellModel bookCellModel) {
        this.bookCellModel=bookCellModel;
        if (tvTitleContent != null && bookCellModel != null) {
            tvTitleContent.setText(bookCellModel.getTitle());
            tvContent.setText(bookCellModel.getText());
            readSubscriptionReviewsView.setReadSubscriptionReviews(bookCellModel);
        }
    }
    private void initEvent() {
        setOnClickListener(new OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   EventUtil.OnBookItemClick(mContext,bookCellModel);
                               }
                           }
        );
    }
}
