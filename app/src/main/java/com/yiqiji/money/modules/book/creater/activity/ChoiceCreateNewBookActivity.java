package com.yiqiji.money.modules.book.creater.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.creater.adapter.BookTemplateListAdapter;
import com.yiqiji.money.modules.book.creater.manager.BookCreatorLocalDataManager;
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem;
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem.ViewType;
import com.yiqiji.money.modules.book.creater.model.BookCoverModel;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.AddBookActivity;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.IntentPerecenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/26.
 */

public class ChoiceCreateNewBookActivity extends BaseActivity {

    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recylerview_book_template)
    RecyclerView recylerviewBookTemplate;

    private BookTemplateListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_template_list_layout);
        ButterKnife.bind(this);
        initViewData();
        initEvent();
    }

    private void initViewData() {
        adapter = new BookTemplateListAdapter(BookCreatorLocalDataManager.getBookCoverListMultipleItemList());
        recylerviewBookTemplate.setAdapter(adapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                int titleType = ViewType.BOOK_NO_AA_TYPE_TITLE.ordinal();
                int aaTitleType = ViewType.BOOK_AA_TYPE_TITLE.ordinal();
                return (viewType == titleType || viewType == aaTitleType) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        recylerviewBookTemplate.setLayoutManager(gridLayoutManager);
    }

    private void initEvent() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookTemplateListAdapter bookTemplateListAdapter = (BookTemplateListAdapter) adapter;
                BookCoverListMultipleItem bookCoverListMultipleItem = bookTemplateListAdapter.getItem(position);
                if (bookCoverListMultipleItem.getViewType() == ViewType.BOOK_COVER_MODEL) {
                    BookCoverModel bookCoverModel = (BookCoverModel) bookCoverListMultipleItem.getData();
                    IntentPerecenter.intentJrop(ChoiceCreateNewBookActivity.this, AddBookActivity.class, bookCoverModel.categoryid, bookCoverModel.categorytitle,
                            bookCoverModel.accountbookbgimg, "false", "false");
                }
            }
        });

        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
