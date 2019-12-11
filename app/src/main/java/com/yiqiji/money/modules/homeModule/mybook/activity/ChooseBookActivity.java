package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.creater.adapter.BookTemplateListAdapter;
import com.yiqiji.money.modules.book.creater.manager.BookCreatorLocalDataManager;
import com.yiqiji.money.modules.book.creater.model.BookCoverListMultipleItem;
import com.yiqiji.money.modules.book.creater.model.BookCoverModel;
import com.yiqiji.money.modules.book.creater.model.BookCoverTemplateListModel;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.BookView;
import com.yiqiji.money.modules.common.widget.MyViewGroup;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ChooseBookActivity extends BaseActivity {
    @BindView(R.id.recylerview_book_template)
    RecyclerView recylerviewBookTemplate;
    private BookTemplateListAdapter adapter;
    private MyViewGroup many_books_group;
    private MyViewGroup single_books_group;
    private ApiService apiService;
    private float screeWith;
    private float bookView_with;
    private LinearLayout sign_one;
    private String jsonpath;
    private String tokenid = "";

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, ChooseBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseb_book);
        ButterKnife.bind(this);
        tokenid = LoginConfig.getInstance().getTokenId();
        initTitle("选择记账场景");
        setReturnBtnVisiable(false);
        many_books_group = (MyViewGroup) findViewById(R.id.many_books_group);
        single_books_group = (MyViewGroup) findViewById(R.id.single_books_group);

        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        bookView_with = (screeWith - UIHelper.Dp2PxFloat(this, 60)) / 3;
        apiService = RetrofitInstance.get().create(ApiService.class);
        sign_one = (LinearLayout) findViewById(R.id.sign_one);
        sign_one.setVisibility(View.GONE);

//        if (isLogin()) {
//
//        } else {
//            sign_one.setVisibility(View.VISIBLE);
//            sign_one.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    IntentUtils.startActivityOnLogin(ChooseBookActivity.this, IntentUtils.LoginIntentType.WELLCOM);
//
//                }
//            });
//        }
        getNativeBookList();

    }

    private void initViewData() {
        adapter = new BookTemplateListAdapter(BookCreatorLocalDataManager.getBookCoverListMultipleItemList());
        recylerviewBookTemplate.setAdapter(adapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                int titleType = BookCoverListMultipleItem.ViewType.BOOK_NO_AA_TYPE_TITLE.ordinal();
                int aaTitleType = BookCoverListMultipleItem.ViewType.BOOK_AA_TYPE_TITLE.ordinal();
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
                if (bookCoverListMultipleItem.getViewType() == BookCoverListMultipleItem.ViewType.BOOK_COVER_MODEL) {
                    BookCoverModel bookCoverModel = (BookCoverModel) bookCoverListMultipleItem.getData();
                    addBooktoService(bookCoverModel.categorytitle, bookCoverModel.categoryid, bookCoverModel.categoryicon);
//                    IntentPerecenter.intentJrop(ChooseBookActivity.this, AddBookActivity.class, bookCoverModel.categoryid, bookCoverModel.categorytitle,
//                            bookCoverModel.categoryicon, "false", "false");
                }
            }
        });


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
                    getServiceMyBook();
//                    if (com.yiqiji.frame.core.utils.StringUtils.isEmptyList(MyBooksListInfo)) {
//                        getServiceMyBook();
//                    } else {
//                        //不处理
//                    }
                    break;
                case 2:
                    LoginConfig.getInstance().isFirstStartApp(false);
                    LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
                    Intent in = new Intent(ChooseBookActivity.this, HomeActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    finish();
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
                    if (!isadd) {
//                        boolean isFirstStart = LoginConfig.getInstance().getIsFirstStartApp();
//                        String accountbookid = booksDbInfo.getAccountbookid();
//                        LoginConfig.getInstance().setBookId(accountbookid);
//                        Intent intent = new Intent(ChooseBookActivity.this, HomeActivity.class);
//                        if (isFirstStart) {
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        } else {
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable("BooksDbInfo", booksDbInfo);
//                            intent.putExtras(bundle);
//                        }
//                        startActivity(intent);

//                        IntentPerecenter.intentJrop(ChooseBookActivity.this, AddBookActivity.class,
//                                booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbooktitle(),
//                                booksDbInfo.getAccountbookcateicon(), "true", "false");
//                        Intent in = new Intent(ChooseBookActivity.this, AddBookActivity.class);
//                        in.putExtra("categorydesc", booksDbInfo.getBookdesc());
//                        in.putExtra("id", booksDbInfo.getAccountbookcate());
//                        in.putExtra("bookName", booksDbInfo.getAccountbooktitle());
//                        in.putExtra("categorytype", booksDbInfo.getAccountbooktype());
//                        in.putExtra("isclear", booksDbInfo.getIsclear());
//                        in.putExtra("isEdit", false);
//                        in.putExtra("categoryicon", booksDbInfo.getAccountbookcateicon());
//                        in.putExtra("isFirstAdd", true);
//                        startActivity(in);
                        addBooktoService(booksDbInfo.getAccountbooktitle(), booksDbInfo.getAccountbookcate(), booksDbInfo.getAccountbookcateicon());
                    } else {
                        //不做处理
                    }


                }
            });
            books_group.addView(button);
        }
    }

    BooksDbInfo booksDbInfo;

    private boolean isadd = false;

    private void addBooktoService(final String editBookName, String id, final String bookicon) {
        isadd = true;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", editBookName);

        CommonFacade.getInstance().exec(Constants.ADD_BOOK, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                isadd = false;

                JSONObject object = (JSONObject) o;
                String json = object.getString("data");
                booksDbInfo = GsonUtil.GsonToBean(json, BooksDbInfo.class);
//                booksDbInfo.setAccountbookcatename(editBookName);
                booksDbInfo.setIssynchronization("true");
//                booksDbInfo.setAccountbookcateicon(bookicon);
                booksDbInfo.setIsdelet("false");
                addBookNative(booksDbInfo);


            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg, true);
                isadd = false;

            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });

//
//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//
//        String[] params = new String[]{"tokenid", "id", "name", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{hashMap.get("tokenid"), id, editBookName, hashMap.get("plat"),
//                hashMap.get("deviceid"), hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("id", id);
//        hashMap.put("name", editBookName);
//
//        apiService.addBook(hashMap).enqueue(new BaseCallBack<ResponseBody>(ChooseBookActivity.this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                isadd = false;
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//
//                    if (object.getInt("code") == 0) {
//                        String json = object.getString("data");
//                        booksDbInfo = GsonUtil.GsonToBean(json, BooksDbInfo.class);
//                        booksDbInfo.setAccountbookcatename(editBookName);
//                        booksDbInfo.setIssynchronization("true");
//                        booksDbInfo.setAccountbookicon(bookicon);
//                        booksDbInfo.setIsdelet("false");
//                        addBookNative(booksDbInfo);
//
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//                isadd = false;
//            }
//        });
    }


    private void addBookNative(BooksDbInfo booksDbInfo) {
        DownUrlUtil.addBookNative(handler, 2, booksDbInfo);
    }


    private void getServiceMyBook() {
        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                //保存账本模板到本地文件
                JSONObject jsonObject = new JSONObject(o.toString());
                JSONObject templateBooksJson = jsonObject.getJSONObject("catelist");
                BookCoverTemplateListModel bookCoverTemplateListModel = GsonUtil.GsonToBean(templateBooksJson.toString(), BookCoverTemplateListModel.class);
                BookCreatorLocalDataManager.saveTemplateBookCoverList(bookCoverTemplateListModel);
                BooksListInfo books = GsonUtil.GsonToBean(object.toString(), BooksListInfo.class);
                setBook(onMyBooksListInfoToBooksDbInfo(books.getCatelist().getSingle()), many_books_group);
                setBook(onMyBooksListInfoToBooksDbInfo(books.getCatelist().getMultiple()), single_books_group);
                initViewData();
                initEvent();
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
//                        BooksListInfo books = GsonUtil.GsonToBean(object.toString(), BooksListInfo.class);
//                        setBook(onMyBooksListInfoToBooksDbInfo(books.getCatelist().getMultiple()), many_books_group);
//                        setBook(onMyBooksListInfoToBooksDbInfo(books.getCatelist().getSingle()), single_books_group);
//                    } else {
//                        Toast.makeText(ChooseBookActivity.this, object.getString("msg"), Toast.LENGTH_LONG).show();
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

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!LoginConfig.getInstance().getIsFirstStartApp()/*!TextUtils.isEmpty(tokenid)*/) {
//            if (!TextUtils.isEmpty(tokenid)) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    showToast("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    moveTaskToBack(true);
                }
                return true;
            }
//            } else {
//                LoginBaseActivity.openActivity(ChooseBookActivity.this);
//            }

        }

        return super.onKeyDown(keyCode, event);
    }
}
