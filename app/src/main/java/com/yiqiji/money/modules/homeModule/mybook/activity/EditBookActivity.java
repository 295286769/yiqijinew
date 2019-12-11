package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBooksColumns;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.BooksCatelistInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 编辑账本
 *
 * @author Administrator
 */
public class EditBookActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_edit_book_name;
    private Button bt_edit_book_return;
    private Button bt_edit_book_keep;
    private BooksDbInfo mBooksDbInfo;
    private ApiService apiService;

    InputMethodManager inputMgr;

    private TextView et_editbook_people;
    private TextView et_editbook_clear_type;
    private int bookcount;
    private final static int BOOKS_EDIT = 0;
    private final static int BOOKS_DEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbook);
        mBooksDbInfo = (BooksDbInfo) getIntent().getParcelableExtra("booksDbInfo");
        bookcount = getIntent().getIntExtra("bookcount", 0);

        initTitle("编辑账本");
        apiService = RetrofitInstance.get().create(ApiService.class);
        init();
    }

    private void init() {
        et_editbook_people = (TextView) findViewById(R.id.et_editbook_people);
        et_editbook_clear_type = (TextView) findViewById(R.id.et_editbook_clear_type);

        et_edit_book_name = (EditText) findViewById(R.id.et_edit_book_name);
        bt_edit_book_return = (Button) findViewById(R.id.bt_edit_book_return);
        bt_edit_book_keep = (Button) findViewById(R.id.bt_edit_book_keep);
        bt_edit_book_return.setOnClickListener(this);
        bt_edit_book_keep.setOnClickListener(this);
        // 类型：0.单人，1多人
        if (BooksDetailPerecenter.isAccountbookCount(mBooksDbInfo.getAccountbookcount())) {
            bt_edit_book_return.setText("退出账本");
            et_editbook_people.setText("支持多人");
        } else {
            bt_edit_book_return.setText("删除账本");
            et_editbook_people.setText("不支持多人");
        }
        if (mBooksDbInfo.getIsclear().equals("1")) {
            et_editbook_clear_type.setText("需结算");
        } else {
            et_editbook_clear_type.setText("无需结算");
        }

        et_edit_book_name.setText(mBooksDbInfo.getAccountbooktitle());
        et_edit_book_name.setSelection(mBooksDbInfo.getAccountbooktitle().length());// 将光标移至文字末尾
        et_edit_book_name.setFocusable(true);
        et_edit_book_name.setFocusableInTouchMode(true);
        et_edit_book_name.requestFocus();
        // inputMgr = (InputMethodManager)
        // this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//
        // 调用此方法才能自动打开输入法软键盘
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.bt_edit_book_return:
//                if (bookcount < 2) {
//                    showToast("请至少保留一个账本记账");
//                    return;
//                }

                final String id = mBooksDbInfo.getAccountbookid();
                if (InternetUtils.isNetworkAvailable(EditBookActivity.this)) {

                    showSimpleAlertDialog("提示", "确定退出账本吗？", "是", "否", false, true, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onServiceBookDel(id);
                            dismissDialog();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                        }
                    });

                } else {
                    // 没网不操作
                    // onNativeBookDel(mBooksDbInfo,
                    // mBooksDbInfo.getAccountbookctime());
                    showToast("请检查网络状态!");
                }

                break;
            case R.id.bt_edit_book_keep:
                String ids = mBooksDbInfo.getAccountbookid();
                String name = mBooksDbInfo.getAccountbooktitle();
                String changebookname = et_edit_book_name.getText().toString();
                if (InternetUtils.isNetworkAvailable(EditBookActivity.this)) {
                    if (changebookname.equals(name)) {
                        showToast("请修改账本名称");
                        return;
                    }
                    onServiceBooksEdit(ids, changebookname);
                } else {
                    // 没网不操作
                    // onNativeBooksEdit(mBooksDbInfo,
                    // mBooksDbInfo.getAccountbookctime(), name);
                    showToast("请检查网络状态!");
                }

            default:
                break;
        }
    }

    private BooksDbInfo dbInfo;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RequsterTag.BOOKSDBINFO:
                    List<BooksDbInfo> booksDbInfo = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfo.size() > 0) {
                        dbInfo = booksDbInfo.get(0);
                        // 发送到首页接收
                        EventBus.getDefault().post(dbInfo);
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        initBooks();
                    }
                    break;

                case BOOKS_EDIT:
//                    Intent intent = new Intent();
//                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case BOOKS_DEL:
                    getNewDateToHome();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 重新拉去账本列表
     */
    public void initBooks() {

        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack<BooksListInfo>() {
            @Override
            public void onSuccess(BooksListInfo o) throws Exception {
                super.onSuccess(o);
                BooksListInfo booksListInfo = o;
                List<MyBooksListInfo> myBooksListInfos = new ArrayList<MyBooksListInfo>();
                BooksCatelistInfo booksCatelistInfo = booksListInfo.getCatelist();
                if (booksCatelistInfo == null) {
                    return;
                }
                List<MyBooksListInfo> single = booksCatelistInfo.getSingle();
                if (single != null) {
                    for (MyBooksListInfo myBooksListInfo : single) {
                        myBooksListInfos.add(myBooksListInfo);
                    }
                }
                List<MyBooksListInfo> multiple = booksCatelistInfo.getSingle();
                if (multiple != null) {
                    for (MyBooksListInfo myBooksListInfo : multiple) {
                        myBooksListInfos.add(myBooksListInfo);
                    }
                }
                List<BooksDbInfo> booksDbInfos = booksListInfo.getAccountbook();
                if (booksDbInfos == null || booksDbInfos.size() == 0) {
                    Intent intent = new Intent(mContext, ChooseBookActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

//                String bookid = "";
//                long temp = 0;
//                int position = 0;
//                if (booksDbInfos != null) {
//                    for (int i = 0; i < booksDbInfos.size(); i++) {
//                        long ltime = Long.parseLong(booksDbInfos.get(i).getAccountbookltime());
//                        if (ltime > temp) {
//                            temp = ltime;
//                            position = i;
//                        }
//
//                        BooksDbInfo booksDbInfo = booksDbInfos.get(i);
//                        booksDbInfo.setIssynchronization("true");
//                        booksDbInfos.set(i, booksDbInfo);
//
//                    }
//                    BooksDbInfo booksDbInfo = booksDbInfos.get(position);
//                    bookid = booksDbInfo.getAccountbookid();
//
//                    String mAccountbookid = bookid;
//                    LoginConfig.getInstance().setBookId(mAccountbookid);
//                    DownUrlUtil.myBooks(booksDbInfos);
//                    getBills(true, true);
//                    BaserClassMode.getBooksCate(mAccountbookid);//获取账单分类icon
////                    getBookDetailinfo(bookid);
//
//                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);

            }


        });
    }

    private void onNativeBookDel(String mAccountbookid) {

        String where = BooksDbInfo.ACCOUNTBOOKID + "=?";
        String[] selectionArgs = new String[]{mAccountbookid};
        DownUrlUtil.deletBooks(handler, BOOKS_DEL, where, selectionArgs);

    }

    private void onNativeBooksEdit(BooksDbInfo mBooksDbInfo, String mAccountbookid, String name) {

        mBooksDbInfo.setAccountbooktitle(name);
        long accountbookutime = System.currentTimeMillis() / 1000;
        mBooksDbInfo.setAccountbookutime(String.valueOf(accountbookutime));
        mBooksDbInfo.setAccountbookltime(accountbookutime + "");

        String where = BooksDbInfo.ACCOUNTBOOKID + "=?";
        String[] selectionArgs = new String[]{mAccountbookid};
        DownUrlUtil.updateBooks(handler, BOOKS_EDIT, mBooksDbInfo, where, selectionArgs);
    }

    private void getNewDateToHome() {
        String orderBy = DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
        String limit = "1";
        DownUrlUtil.serchBookList(null, null, null, null, orderBy, limit, handler, RequsterTag.BOOKSDBINFO);
    }

    private void onServiceBookDel(String id) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        CommonFacade.getInstance().exec(Constants.BOOKS_DEL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                onNativeBookDel(mBooksDbInfo.getAccountbookid());
                showToast(bt_edit_book_return.getText().toString() + "成功");
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


//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//
//        String[] params = new String[]{"tokenid", "id", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{hashMap.get("tokenid"), id, hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("id", id);
//
//        apiService.onBookDel(hashMap).enqueue(new BaseCallBack<ResponseBody>(EditBookActivity.this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        onNativeBookDel(mBooksDbInfo.getAccountbookid());
//                        showToast(bt_edit_book_return.getText().toString() + "成功");
//                    } else {
//                        showToast(object.getString("msg"));
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                // TODO Auto-generated method stub
//                super.onFailure(arg0);
//            }
//
//        });
    }

    private void onServiceBooksEdit(String id, final String name) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", name);

        CommonFacade.getInstance().exec(Constants.BOOKS_EDIT, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                onNativeBooksEdit(mBooksDbInfo, mBooksDbInfo.getAccountbookid(), name);
                showToast("修改账本名称成功");
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


//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//        String[] params = new String[]{"tokenid", "id", "name", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{hashMap.get("tokenid"), id, name, hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("id", id);
//        hashMap.put("name", name);
//        apiService.onBookEdit(hashMap).enqueue(new BaseCallBack<ResponseBody>(EditBookActivity.this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//
//                    if (object.getInt("code") == 0) {
//                        onNativeBooksEdit(mBooksDbInfo, mBooksDbInfo.getAccountbookid(), name);
//                        showToast("修改账本名称成功");
//                    } else {
//                        showToast(object.getString("msg"));
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
//            }
//        });
    }

    private boolean outkeyboard = true;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // outkeyboard = false;
    }

    private void outkeKyboard() {
        if (outkeyboard) {
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
        }
    }


}
