package com.yiqiji.money.modules.myModule.my.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.LinearLayoutForItem;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.myModule.my.adapter.BookListAdapter;
import com.yiqiji.money.modules.myModule.my.entity.BooksEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/6/28.
 * 选择账本（导出账本）
 */

public class SelectBookActivity extends BaseActivity {

    @BindView(R.id.ll_item)
    LinearLayoutForItem llItem;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<BooksEntity.AccountbookBean> mBookList = new ArrayList<>();
    private BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_book);
        ButterKnife.bind(this);
        tvTitle.setText("选择账本");
        mBookList = getIntent().getParcelableArrayListExtra("ListBook");
        if (!StringUtils.isEmptyList(mBookList)) {
            adapter = new BookListAdapter(this, mBookList);
        }
        llItem.setOnItemClickLisntener(new LinearLayoutForItem.OnItemClickLisntener() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i < mBookList.size(); i++) {
                    if (i == position) {
                        mBookList.get(i).setIsCheck(1);
                    } else {
                        mBookList.get(i).setIsCheck(0);
                    }
                }
                llItem.setAdapter(adapter);
            }
        });
        llItem.setAdapter(adapter);
    }

    /**
     * 开启当前activity
     */
    public static void open(Context context, int request_code, List<BooksEntity.AccountbookBean> listBean) {
        Intent intent = new Intent(context, SelectBookActivity.class);
        intent.putParcelableArrayListExtra("ListBook", (ArrayList<? extends Parcelable>) listBean);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, request_code);
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        Intent intent = new Intent();
        for (int i = 0; i < mBookList.size(); i++) {
            if (mBookList.get(i).getIsCheck() == 1) {
                intent.putExtra("SelBook", mBookList.get(i));
            }
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
