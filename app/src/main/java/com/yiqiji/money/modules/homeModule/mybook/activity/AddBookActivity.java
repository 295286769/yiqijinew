package com.yiqiji.money.modules.homeModule.mybook.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBooksColumns;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CommonToggleButton;
import com.yiqiji.money.modules.common.view.LeftTextRightImageView;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BooksCatelistInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.imageprecenter.ImagePerecenter;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.MyBookPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.util.MyBookActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

public class AddBookActivity extends BaseActivity implements OnClickListener {
    private EditText et_add_book_name;
    private Button bt_add_book_confirm;
    private ImageView book_image;//账本背景图片
    private RelativeLayout return_defult_image;
    private RelativeLayout change_book_image_layout;
    private View two_button;
    private View root_layout;
    private TextView quit_or_delet_button;
    private LinearLayout address_share_layout;//装修和旅游账本地址和分享layout
    private LeftTextRightImageView address;//装修正本地址
    private RelativeLayout rl_share_layout;//分享
    private CommonToggleButton commonToggleButton;//分享按钮开关
    private ApiService apiService;


    // 这个是未编辑的bookName
    private int isSing = 1;
    private int isClea = 1;

    private String id = "";
    private String accountbookid = "";
    private String bookName = "";
    private String categoryicon;
    InputMethodManager inputMgr;

    private TextView et_editbook_people;
    private TextView et_editbook_clear_type;

    private boolean isFirstAdd = false;
    private String isEdit = "false";//true为添加false为设置edit为编辑
    private String book_url = "";
    private Uri uri;
    private byte[] bytes;
    private int defultdrawbleid = R.drawable.book_detault_bagroud;
    private BooksDbInfo booksDbInfo = null;
    private String quitOrDeletString;
    private int isopen = 0;//是否分享至社区0：不分享1：分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        apiService = RetrofitInstance.get().create(ApiService.class);
        String className = getIntent().getComponent().getClassName();
        Log.i("tag", "--++++className++" + className);
        id = getIntent().getStringExtra("id");
        accountbookid = getIntent().getStringExtra("accountbookid");
        bookName = getIntent().getStringExtra("bookName");
        categoryicon = getIntent().getStringExtra("categoryicon");
        isEdit = getIntent().getStringExtra("isEdit");
        isFirstAdd = getIntent().getBooleanExtra("isFirstAdd", false);
        String firstAdd = getIntent().getStringExtra("isFirstAdd");
        if (firstAdd.equals("true")) {
            isFirstAdd = true;
        }
        String title_name = "添加" + bookName;
        if (!isEdit.equals("false")) {
            title_name = "编辑账本";

        }
        initTitle(title_name, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.layout_title_view_return:
//                        outkeKyboard();
                        finish();
                        break;

                    default:
                        break;
                }

            }
        });
        init();
        setOnPress();
        initTitleAndText();
    }

    //从相册里面取后返回
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        bytes = getIntent().getByteArrayExtra("bitmap");
        if (bytes != null) {
            ImagePerecenter.setImage(this, bytes, book_image);
            return_defult_image.setVisibility(View.VISIBLE);
            book_url = null;
        }
    }

    private void initTitleAndText() {
        bottomTwoButtonText("删除账本", "保存账本", this);
        address.setLeftText(getResources().getString(R.string.house_info));
        if (isEdit.equals("false")) {
            ImageLoaderManager.loadImage(this, categoryicon, book_image);
            getNativeMyBookByName();
        } else {
            if (id.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
                address_share_layout.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
            } else if (id.equals(RequsterTag.TOURISMACCOUNTBOOKCATE) || id.equals(RequsterTag.ACCOUNTBOOKCATE)) {
                address_share_layout.setVisibility(View.VISIBLE);
            }

            two_button.setVisibility(View.VISIBLE);
            bt_add_book_confirm.setVisibility(View.GONE);
            return_defult_image.setVisibility(View.VISIBLE);
            et_add_book_name.setSelection(et_add_book_name.getText().toString().length());// 将光标移至文字末尾
            initBookDetail();

        }
    }

    /**
     * 拉去账本详情
     */
    private void initBookDetail() {
        BooksDetailPerecenter.getBookDetail(this, accountbookid, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                super.onSuccess(booksDbInfo);
                if (booksDbInfo != null) {
                    AddBookActivity.this.booksDbInfo = booksDbInfo;
                    book_url = booksDbInfo.getAccountbookbgimg();
                    TaskQueue.commonQueue().execute(new Runnable() {
                        @Override
                        public void run() {
                            bytes = BitmapUtil.getBytes(book_url);
                        }
                    });
                    String book_userid = AddBookActivity.this.booksDbInfo.getUserid();
                    String book_deveceid = AddBookActivity.this.booksDbInfo.getDeviceid();
                    boolean isGroup = isBookFount(book_userid, book_deveceid);
                    if (!isGroup) {
                        quit_or_delet_button.setBackgroundResource(R.drawable.edit_book_return);
                        quit_or_delet_button.setText("退出账本");
                        quit_or_delet_button.setTextColor(getResources().getColor(R.color.white));
                    }
                    if (TextUtils.isEmpty(book_url)) {
                        ImagePerecenter.setImage(AddBookActivity.this, categoryicon, book_image);
                    } else {
                        ImagePerecenter.setImage(AddBookActivity.this, book_url, book_image);
                    }
                    et_add_book_name.setText(AddBookActivity.this.booksDbInfo.getAccountbooktitle());
                    et_add_book_name.setSelection(et_add_book_name.getText().toString().length());// 将光标移至文字末尾
                    boolean opent = AddBookActivity.this.booksDbInfo.getIsopen().equals("0") ? false : true;
                    commonToggleButton.setChecked(opent);

                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    private void init() {
        root_layout = (View) findViewById(R.id.root_layout);
        quit_or_delet_button = (TextView) findViewById(R.id.share_button);
        et_editbook_people = (TextView) findViewById(R.id.et_editbook_people);
        et_editbook_clear_type = (TextView) findViewById(R.id.et_editbook_clear_type);

        bt_add_book_confirm = (Button) findViewById(R.id.bt_add_book_confirm);
        et_add_book_name = (EditText) findViewById(R.id.et_add_book_name);
        book_image = (ImageView) findViewById(R.id.book_image);
        return_defult_image = (RelativeLayout) findViewById(R.id.return_defult_image);
        change_book_image_layout = (RelativeLayout) findViewById(R.id.change_book_image_layout);
        address_share_layout = (LinearLayout) findViewById(R.id.address_share_layout);
        address = (LeftTextRightImageView) findViewById(R.id.address);
        rl_share_layout = (RelativeLayout) findViewById(R.id.rl_share_layout);
        commonToggleButton = (CommonToggleButton) findViewById(R.id.commonToggleButton);
        two_button = (View) findViewById(R.id.two_button);
        return_defult_image.setOnClickListener(this);
        change_book_image_layout.setOnClickListener(this);
        inputMgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);


    }

    private void setOnPress() {
        bt_add_book_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                addBooktoService();

            }
        });
        address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBookActivityUtil.startAddressActivity(AddBookActivity.this, accountbookid);

            }
        });
        commonToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isopen = 1;
                } else {
                    isopen = 0;
                }
            }
        });
    }


    private void addBooktoService() {
        final String editBookName = et_add_book_name.getText().toString();
        if (TextUtils.isEmpty(editBookName)) {
            showToast("请填写账本名称");
            return;
        }
        if (!isEdit.equals("false")) {
            if (booksDbInfo != null) {
                String book_name = booksDbInfo.getAccountbooktitle();
                String book_url = booksDbInfo.getAccountbookbgimg();
                String open = booksDbInfo.getIsopen();
                if (book_name.equals(editBookName) && book_url.equals(this.book_url) && open.equals(isopen)) {
                    if (isEdit.equals("true")) {
                        Intent in = new Intent(this, HomeActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("BooksDbInfo", booksDbInfo);
                        in.putExtras(mBundle);
                        startActivity(in);
                    } else {
                        finish();
                    }

                    return;
                }
            } else {
                showToast("账本不存在");
                return;
            }

        }

        if (InternetUtils.isNetworkAvailable(AddBookActivity.this)) {
            showLoadingDialog();

            if (bytes != null) {
                DownUrlUtil.sysQiniu(this, bytes, new ViewCallBack<String>() {
                    @Override
                    public void onSuccess(String o) throws Exception {
                        super.onSuccess(o);
                        updateBook(editBookName, o);
                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        super.onFailed(simleMsg);
                        dismissDialog();
                    }
                });

            } else {
                updateBook(editBookName, "");
            }
        } else {
            // 没网的时候不操作
            // addBooktoNative(editBookName);
            showToast("请检查网络状态!");
        }

    }

    public void updateBook(String editBookName, String image_url) {
        MyBookPerecenter.updateBookDetail(isEdit, id, accountbookid, isopen, editBookName, image_url, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
                if (isEdit.equals("edit")) {
                    Intent in = new Intent(AddBookActivity.this, BooksActivity.class);
                    startActivity(in);
                    finish();
                    return;
                } else if (isEdit.equals("true")) {
                    EventBus.getDefault().post(new AddMemberResponse());
                    finish();
                    return;
                }
                if (booksDbInfo == null) {
                    booksDbInfo = o;
                }
                booksDbInfo.setAccountbookcatename(bookName);
                booksDbInfo.setIssynchronization("true");
                booksDbInfo.setAccountbookcateicon(categoryicon);
                booksDbInfo.setIsdelet("false");
                addBookNative(booksDbInfo);
                Intent in = new Intent(AddBookActivity.this, HomeActivity.class);
                if (isFirstAdd) {
                    LoginConfig.getInstance().isFirstStartApp(false);
                    LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                } else {
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("BooksDbInfo", booksDbInfo);
                    in.putExtras(mBundle);
                }
                startActivity(in);
                finish();
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
    }

//    private void onNativeBooksEdit(BooksDbInfo mBooksDbInfo, String mAccountbookid, String name) {
//
//        mBooksDbInfo.setAccountbooktitle(name);
//        long accountbookutime = System.currentTimeMillis() / 1000;
//        mBooksDbInfo.setAccountbookutime(String.valueOf(accountbookutime));
//        mBooksDbInfo.setAccountbookltime(accountbookutime + "");
//
//        String where = BooksDbInfo.ACCOUNTBOOKID + "=?";
//        String[] selectionArgs = new String[]{mAccountbookid};
//        DownUrlUtil.updateBooks(new ViewCallBack() {
//            @Override
//            public void onSuccess(Object o) throws Exception {
//                super.onSuccess(o);
//                TaskQueue.mainQueue().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent in = new Intent(AddBookActivity.this, BooksActivity.class);
//                        startActivity(in);
//                        finish();
//                    }
//                });
//
//            }
//        }, mBooksDbInfo, where, selectionArgs);
//    }

    private boolean outkeyboard = true;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        outkeyboard = false;
    }

    private void outkeKyboard() {
        if (outkeyboard) {
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
        }
    }

    private void addBookNative(BooksDbInfo booksDbInfo) {
        DownUrlUtil.addBookNative(handler, 1, booksDbInfo);
    }

    private void getNativeMyBookByName() {

        String where = DtBooksColumns.ACCOUNTBOOKCATE + "=?and " + DtBooksColumns.DEVICEID + "=?";
        String[] selectionArgs = new String[]{id, LoginConfig.getInstance().getDeviceid()};
        DownUrlUtil.getListBooksDbInfo(handler, 0, where, selectionArgs, null, null, null, null);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    @SuppressWarnings("unchecked")
                    List<BooksDbInfo> booksDb = (List<BooksDbInfo>) msg.obj;
                    int size = booksDb.size();

                    if (size > 0) {
                        et_add_book_name.setText(bookName + size++);
                    } else {
                        et_add_book_name.setText(bookName);
                    }

                    et_add_book_name.setSelection(et_add_book_name.getText().toString().length());// 将光标移至文字末尾

                    break;

                default:
                    break;
            }
        }

        ;
    };

    //创建一个以时间结尾的png图片路径
    private void initPath() {
        uri = XzbUtils.getTpPath();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_button://退出账本
                quitOrDeletString = "你确定要" + quit_or_delet_button.getText().toString() + "吗?";
                showSimpleAlertDialog("提示", quitOrDeletString, "确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        onServiceBookDel();

                    }
                }, "取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                    }
                });

                break;
            case R.id.settlement_button://保存账本
                addBooktoService();
                break;
            case R.id.change_book_image_layout:
                XzbUtils.hideOrShowSoft(this, et_add_book_name, false);
                initPath();
                IntentUtils.showPhotoView(this, uri);
                break;
            case R.id.return_defult_image:
                bytes = null;
                book_url = null;
                ImagePerecenter.setImage(AddBookActivity.this, categoryicon, book_image);
                break;
        }

    }

    private void onServiceBookDel() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", accountbookid);
        CommonFacade.getInstance().exec(Constants.BOOKS_DEL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                onNativeBookDel(accountbookid);
                String quit_or_deletString = quit_or_delet_button.getText().toString();
                showToast(quit_or_deletString + "成功");
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
    }

    private void onNativeBookDel(String mAccountbookid) {

        String where = BooksDbInfo.ACCOUNTBOOKID + "=?";
        String[] selectionArgs = new String[]{mAccountbookid};
        DownUrlUtil.deletBooks(new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                getNewDateToHome();
            }
        }, where, selectionArgs);

    }

    private void getNewDateToHome() {
        String orderBy = DtBooksColumns.ACCOUNTBOOKLTIME + " DESC";
        String limit = "1";
        DownUrlUtil.serchBookList(null, null, null, null, orderBy, limit, new ViewCallBack<List<BooksDbInfo>>() {
            @Override
            public void onSuccess(List<BooksDbInfo> booksDbInfos) throws Exception {
                super.onSuccess(booksDbInfos);
                List<BooksDbInfo> booksDbInfo = booksDbInfos;
                if (booksDbInfo.size() > 0) {
                    BooksDbInfo dbInfo = booksDbInfo.get(0);
                    // 发送到首页接收
                    EventBus.getDefault().post(dbInfo);
                    finish();
                } else {
                    initBooks();
                }
            }
        });
    }

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
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);

            }


        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case RequsterTag.RQ_TAKE_A_PHOTO:// 拍照
                if (uri != null) {
                    Bitmap bitmap;
                    String pathString = StringUtils.getRealFilePath(this, uri);

                    try {
//                        bitmap = BitmapUtil.getBitmapFormUri(this, uri, UIHelper.Dp2Px(this, 170), UIHelper.Dp2Px(this, 140));
//                        String pathString = StringUtils.getRealFilePath(this, XzbUtils.getPath());
//                        BitmapUtil.saveBitmapToFile(bitmap, pathString);
                        IntentUtils.setReflectionjump(AddBookActivity.this, StringUtils.getactivityPath(ClipPictureActivity.class), "path", pathString);
//                        uri = Uri.parse(pathString);
//                        if (!pathString.contains("file://")) {
//                            pathString = "file://" + pathString;
//                        }
//                        book_url = pathString;
//                        ImagePerecenter.setImage(AddBookActivity.this, pathString, book_image);
//                        XzbUtils.displayImage(book_image, pathString, 0);
//                        ImageLoaderManager.loadImage(WriteJournalActivity.this, R.drawable.write_photograph, iv_details_photograph);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                break;

            default:
                break;
        }
    }

}