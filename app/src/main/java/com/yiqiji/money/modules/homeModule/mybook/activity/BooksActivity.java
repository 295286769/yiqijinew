package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.decoration.GridSpacingItemDecoration;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.creater.activity.ChoiceCreateNewBookActivity;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.entity.Books;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.AddBookView;
import com.yiqiji.money.modules.common.view.MyTitleLayout;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.common.widget.MyViewGroup;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;
import com.yiqiji.money.modules.homeModule.mybook.adapter.BooksAdapter;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.IntentPerecenter;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends BaseActivity implements BooksAdapter.OnItemClickListener {
    private MyTitleLayout books_title;
    private Books books;
    private View include_on_login;
    private MyViewGroup books_group;
    private BaseRecylerview books_list;
    private BooksAdapter booksAdapter;
    private List<View> views;
    private float screeWith;
    private float bookView_with;
    private List<BooksDbInfo> listData = new ArrayList<BooksDbInfo>();
    private String bookName;
    private String bookid;
    private ApiService apiService;
    private int bookcount;
    private boolean isLook = true;
    private TextView book_text;

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, BooksActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        bookName = getIntent().getStringExtra("bookName");
        bookid = getIntent().getStringExtra("bookid");

        apiService = RetrofitInstance.get().create(ApiService.class);
        books_title = (MyTitleLayout) findViewById(R.id.my_title);
        books_title.setTitle("我的账本");
        books_title.showRightBtn("编辑");
        books_title.setBackgroudColor(getResources().getColor(R.color.title_back_color), getResources()
                .getColor(R.color.title_text_color));
        books_title.setListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_title_view_return:
                        finish();
                        break;
                    case R.id.layout_title_view_right_btn:
                        if (isLook) {
                            // 设为编辑
                            isLook = false;
                            initBook(!isLook);
                            books_title.showRightBtn("完成");
                        } else {
                            isLook = true;
                            initBook(!isLook);
                            books_title.showRightBtn("编辑");
                        }
                        break;

                }
            }
        });


        initView();
        initAdapter();
        getNativeMyBook();
        getServiceMyBook();
    }


    private void initView() {
        include_on_login = findViewById(R.id.include_on_login);
        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        bookView_with = (screeWith - UIHelper.Dp2PxFloat(this, 60)) / 3;
        books_group = (MyViewGroup) findViewById(R.id.books_group);
        books_list = (BaseRecylerview) findViewById(R.id.books_list);
        book_text = (TextView) findViewById(R.id.book_text);
        views = new ArrayList<View>();
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        gridLayoutManager.offsetChildrenHorizontal(UIHelper.dip2px(this, 20));
//
//        gridLayoutManager.offsetChildrenVertical(UIHelper.dip2px(this, 20));
        books_list.setLayoutManager(gridLayoutManager);
        booksAdapter = new BooksAdapter(this);
        books_list.addItemDecoration(new GridSpacingItemDecoration(3, UIHelper.dip2px(this, 20), true));
        books_list.setAdapter(booksAdapter);
        booksAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String tokenid = LoginConfig.getInstance().getTokenId();
        boolean isNetwork = InternetUtils.isNetworkAvailable(this);
        if (isNetwork && TextUtils.isEmpty(tokenid)) {
            include_on_login.setVisibility(View.VISIBLE);
            include_on_login.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//                    IntentUtils.startActivityOnLogin(BooksActivity.this, IntentUtils.LoginIntentType.OTHER);
                    LoginBaseActivity.openActivity(BooksActivity.this);
                }
            });
        } else {
            include_on_login.setVisibility(View.GONE);
        }

        books_title.showRightBtn(isLook ? "编辑" : "完成");
        getNativeMyBook();

        if (InternetUtils.isNetworkAvailable(this)) {
            getServiceMyBook();
        }
    }


    private void getServiceMyBook() {

        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                BooksListInfo books = GsonUtil.GsonToBean(object.toString(), BooksListInfo.class);
                setBooks(books, bookid);

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        apiService.getHomeList(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        // ic_error.setVisibility(View.GONE);
//                        BooksListInfo books = GsonUtil.GsonToBean(object.toString(), BooksListInfo.class);
//                        setBooks(books, bookid);
//                    } else {
//                        Toast.makeText(BooksActivity.this, object.getString("msg"), Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//
//            }
//        });
    }

    public void setBooks(BooksListInfo books, String bookid) {
        if (books.getAccountbook() != null) {
            bookcount = books.getAccountbook().size();
            book_text.setText("已添加" + books.getAccountbook().size() + "个账本");
            setBooksDbInfoList(books.getAccountbook(), bookid);
            initBook(!isLook);

        }
    }

    /**
     * 初始化我的账本
     */
    private void getNativeMyBook() {
        String orderBy = DailycostContract.DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
        DownUrlUtil.getListBooksDbInfo(handler, 0, null, null, null, null, orderBy, null);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    List<BooksDbInfo> booksDb = (List<BooksDbInfo>) msg.obj;
                    if (booksDb.size() > 0) {
                        bookcount = booksDb.size();
                        book_text.setText("已添加" + booksDb.size() + "个账本");
                        setBooksDbInfoList(booksDb, bookid);
                        initBook(!isLook);
                    }
                    break;

                default:
                    break;
            }
        }
    };


    List<BooksDbInfo> AccountbookBeanlist = new ArrayList<BooksDbInfo>();


    public void setBooksDbInfoList(List<BooksDbInfo> booksDbInfos, String mBookid) {
        listData.clear();
        AccountbookBeanlist.clear();
        AccountbookBeanlist = booksDbInfos;
        for (int i = 0; i < AccountbookBeanlist.size(); i++) {
            if (AccountbookBeanlist.get(i).getAccountbookid().equals(mBookid)) {
                listData.add(AccountbookBeanlist.get(i));
                AccountbookBeanlist.remove(i);
                break;
            }
        }

        for (int i = 0; i < AccountbookBeanlist.size(); i++) {
            if (AccountbookBeanlist.get(i).getIsnew().equals("1")) {
                listData.add(AccountbookBeanlist.get(i));
                AccountbookBeanlist.remove(i);
            }
        }
        AccountbookBeanlist.add(null);
        listData.addAll(AccountbookBeanlist);
    }


    private void initBook(final boolean isEdit) {
        if (booksAdapter != null) {
            booksAdapter.setIsEdit(isEdit);
            booksAdapter.setDataList(listData);

        }

//        if (books_group == null) {
//            return;
//        }
//
//        if (books_group.getChildCount() > 0) {
//            books_group.removeAllViews();
//        }
//        for (int i = 0; i < listData.size(); i++) {
//            BookView button;
//            if (i == 0) {
//                button = new BookView(this, listData.get(i), isEdit, true, true);
//            } else {
//                button = new BookView(this, listData.get(i), isEdit, true, false);
//            }
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.width = (int) bookView_with;
//            layoutParams.height = (int) (bookView_with * 1.23);
//            layoutParams.leftMargin = UIHelper.Dp2Px(this, 15f);
//            layoutParams.bottomMargin = UIHelper.Dp2Px(this, 10);
//            button.setLayoutParams(layoutParams);
//            button.setTag(i);
//
//            button.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    BooksDbInfo booksDbInfo = listData.get((int) v.getTag());
//                    // 是编辑就跳转到编辑界面
//                    if (isEdit) {
//                        IntentPerecenter.intentJrop(BooksActivity.this, AddBookActivity.class, booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbooktitle(),
//                                booksDbInfo.getAccountbookcateicon(), "false", "edit", booksDbInfo.getAccountbookid());
////                        Intent in = new Intent(BooksActivity.this, EditBookActivity.class);
////                        Bundle mBundle = new Bundle();
////                        mBundle.putParcelable("booksDbInfo", booksDbInfo);
////                        in.putExtra("bookcount", bookcount);
////                        in.putExtras(mBundle);
////                        startActivityForResult(in, RequsterTag.EDITBOOKJUMP);
//
//                    } else {
//                        // 不是则这个操作会返回homeActivity
//                        Intent in = new Intent(BooksActivity.this, HomeActivity.class);
//                        Bundle mBundle = new Bundle();
//                        mBundle.putParcelable("BooksDbInfo", booksDbInfo);
//                        in.putExtras(mBundle);
//                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(in);
//                    }
//
//                }
//            });
//
//            books_group.addView(button);
//        }
//
//        if (!isEdit) {
        AddBookView addBook = new AddBookView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.width = (int) bookView_with;
//            layoutParams.height = (int) (bookView_with * 1.23);
//            layoutParams.leftMargin = UIHelper.Dp2Px(this, 15f);
//            layoutParams.bottomMargin = UIHelper.Dp2Px(this, 10f);
//            addBook.setLayoutParams(layoutParams);
//            books_group.addView(addBook);
//
//            addBook.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent in = new Intent(BooksActivity.this, BooksListActivity.class);
//                    startActivity(in);
//                }
//            });
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequsterTag.EDITBOOKJUMP:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        books_group.removeAllViews();
        books_group = null;
        System.gc();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BooksDbInfo booksDbInfo = listData.get(position);
        if (booksDbInfo == null) {
            if (isLook) {
                Intent in = new Intent(BooksActivity.this, ChoiceCreateNewBookActivity.class);
                //Intent in = new Intent(BooksActivity.this, BooksListActivity.class);
                startActivity(in);
            } else {
                //编辑不处理
            }
            return;
        }
        LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
        // 是编辑就跳转到编辑界面
        if (!isLook) {
            IntentPerecenter.intentJrop(BooksActivity.this, AddBookActivity.class, booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbooktitle(),
                    booksDbInfo.getAccountbookbgimg(), "false", "edit", booksDbInfo.getAccountbookid());
//                        Intent in = new Intent(BooksActivity.this, EditBookActivity.class);
//                        Bundle mBundle = new Bundle();
//                        mBundle.putParcelable("booksDbInfo", booksDbInfo);
//                        in.putExtra("bookcount", bookcount);
//                        in.putExtras(mBundle);
//                        startActivityForResult(in, RequsterTag.EDITBOOKJUMP);

        } else {
            // 不是则这个操作会返回homeActivity
            HomeActivity.openActivity(BooksActivity.this, booksDbInfo);
//            Intent in = new Intent(BooksActivity.this, HomeActivity.class);
//            Bundle mBundle = new Bundle();
//            mBundle.putParcelable("BooksDbInfo", booksDbInfo);
//            in.putExtras(mBundle);
//            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(in);
        }

    }
}
