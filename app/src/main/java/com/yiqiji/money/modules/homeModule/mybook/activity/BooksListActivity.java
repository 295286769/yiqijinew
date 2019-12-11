package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.BookView;
import com.yiqiji.money.modules.common.widget.MyViewGroup;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.IntentPerecenter;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends BaseActivity {


    private MyViewGroup many_books_group;
    private MyViewGroup single_books_group;

    private float screeWith;
    private float bookView_with;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        initTitle("选择记账场景");
        many_books_group = (MyViewGroup) findViewById(R.id.many_books_group);
        single_books_group = (MyViewGroup) findViewById(R.id.single_books_group);

        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        bookView_with = (screeWith - UIHelper.Dp2PxFloat(this, 60)) / 3;
        getNativeBookList();
    }

    /**
     * 初始化单人多人账本
     */
    private void getNativeBookList() {
        DownUrlUtil.getMyBooksListInfo(handler, 1);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 获取的单人账本和多人账本集合；
                    List<MyBooksListInfo> MyBooksListInfo = (List<MyBooksListInfo>) msg.obj;

                    List<MyBooksListInfo> one = new ArrayList<>();
                    List<MyBooksListInfo> more = new ArrayList<>();

                    for (MyBooksListInfo data : MyBooksListInfo) {

                        if (data.getIsclear().equals("0")) {
                            one.add(data);
                        } else {
                            more.add(data);
                        }
                    }
                    setBook(onMyBooksListInfoToBooksDbInfo(one), many_books_group);
                    setBook(onMyBooksListInfoToBooksDbInfo(more), single_books_group);

                    break;
                default:
                    break;
            }
        }
    };

    private List<BooksDbInfo> onMyBooksListInfoToBooksDbInfo(List<MyBooksListInfo> datalist) {
        List<BooksDbInfo> list = new ArrayList<>();
        BooksDbInfo booksDbInfo;
        for (MyBooksListInfo data : datalist) {
            booksDbInfo = new BooksDbInfo();
            booksDbInfo.setAccountbookcateicon(data.getCategoryicon());
            booksDbInfo.setAccountbooktype(data.getCategorytype());
            booksDbInfo.setAccountbookcate(data.getCategoryid());
            booksDbInfo.setIsclear(data.getIsclear());
            booksDbInfo.setAccountbooktitle(data.getCategorytitle());
            booksDbInfo.setBookdesc(data.getCategorydesc());
            list.add(booksDbInfo);
        }
        return list;
    }


    private void setBook(final List<BooksDbInfo> listData, MyViewGroup books_group) {
        if (books_group == null) {
            return;
        }
        if (books_group.getChildCount() > 0) {
            books_group.removeAllViews();
        }
        for (int i = 0; i < listData.size(); i++) {
            BookView button;
            button = new BookView(this, listData.get(i), false, true, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (int) bookView_with;
            layoutParams.height = (int) (bookView_with * 1.23);
            layoutParams.leftMargin = UIHelper.Dp2Px(this, 15f);
            layoutParams.bottomMargin = UIHelper.Dp2Px(this, 10);
            button.setLayoutParams(layoutParams);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    BooksDbInfo booksDbInfo = listData.get((int) v.getTag());
//                    Intent in = new Intent(BooksListActivity.this, AddBookActivity.class);
                    IntentPerecenter.intentJrop(BooksListActivity.this, AddBookActivity.class, booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbooktitle(),
                            booksDbInfo.getAccountbookbgimg(), "false", "false");
//                    in.putExtra("categorydesc", booksDbInfo.getBookdesc());
//                    in.putExtra("id", booksDbInfo.getAccountbookcate());
//                    in.putExtra("bookName", booksDbInfo.getAccountbooktitle());
//                    in.putExtra("categorytype", booksDbInfo.getAccountbooktype());
//                    in.putExtra("isclear", booksDbInfo.getIsclear());
//                    in.putExtra("isEdit", "false");
//                    in.putExtra("categoryicon", booksDbInfo.getAccountbookcateicon());
//                    startActivity(in);
                }
            });
            books_group.addView(button);
        }
    }


}
