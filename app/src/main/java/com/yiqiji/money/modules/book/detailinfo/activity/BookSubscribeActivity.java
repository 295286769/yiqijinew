package com.yiqiji.money.modules.book.detailinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.detailinfo.fragment.BookInfoDataListFragment;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillModel;
import com.yiqiji.money.modules.book.view.AttentionView;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.share.ShareManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/6/13.
 * 我订阅的账本详情
 */

public class BookSubscribeActivity extends BaseActivity {

    @BindView(R.id.rl_view_title)
    View rlViewTitle;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.image_share)
    ImageView imageShare;
    @BindView(R.id.attention_view)
    AttentionView attentionView;

    private String accountBookId;
    private String mAccountbookcateid;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BookBillModel mBookBillModel;
    private ShareManager shareManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_subscribe_layout);
        ButterKnife.bind(this);
        accountBookId = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_ID);
        mAccountbookcateid = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_CATE_ID);
        showBookInfoListFragment();
        initViewEvent();
    }

    private void showBookInfoListFragment() {
        BookInfoDataListFragment fragment = BookInfoDataListFragment.newInstance(accountBookId, mAccountbookcateid, onDataBookInfoRefreshCallBack);
        addCommitFragment(fragment);
    }

    BookInfoDataListFragment.OnDataBookInfoRefreshCallBack onDataBookInfoRefreshCallBack = new BookInfoDataListFragment.OnDataBookInfoRefreshCallBack() {
        @Override
        public void onSuccess(BookBillModel bookBillModel) {
            mBookBillModel = bookBillModel;
            attentionView.setViewData(bookBillModel.bookdetail);
        }
    };

    private void initViewEvent() {
        shareManager=new ShareManager(this);
        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBookBillModel != null) {
                    shareManager.shareBook(mBookBillModel.bookdetail,false);
                }
            }
        });
    }

    private void addCommitFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 外部打开Activity方法
     *
     * @param context
     * @param accountBookId
     */
    public static void open(Context context, String accountBookId,String mAccountbookcateid) {
        Intent intent = new Intent();
        intent.setClass(context, BookSubscribeActivity.class);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_CATE_ID, mAccountbookcateid);
        context.startActivity(intent);
    }

}
