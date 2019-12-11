package com.yiqiji.money.modules.common.myserver;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.creater.manager.BookCreatorLocalDataManager;
import com.yiqiji.money.modules.book.creater.model.BookCoverTemplateListModel;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.entity.OuterBooksDbInfo;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil.DownloadStateListener;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BooksCatelistInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.greenrobot.event.EventBus;

public class MyServer extends Service {
    private List<DailycostEntity> dailycostEntitys;
    //    private List<PostBill> postBills;
    private List<PostBill> postBillList = new ArrayList<>();
    private Vector vector = new Vector();
    private int position = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        viewCallBack = new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                if (o != null) {
                    if (o instanceof DailycostEntity) {
                        DailycostEntity dailycostEntity = (DailycostEntity) o;
                        List<DailycostEntity> dailycostEntityList = new ArrayList<>();
                        dailycostEntityList.add(dailycostEntity);
                        List<PostBill> postBillss = DownUrlUtil.getPostBillData(dailycostEntityList);
                        BaserClassMode.setSynOneDailycostEntity(postBillss, dailycostEntityList, viewCallBack);
                    } else {
                        int position_o = (int) o;
                        position += position_o;
                        if (position == dailycostEntitys.size()) {
                            RequsterTag.isSynchronizationing = false;
                            position = 0;
                            EventBus.getDefault().post("updatebook");
                            EventBus.getDefault().post("success");
                        }
                    }
                }

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                RequsterTag.isSynchronizationing = false;
                EventBus.getDefault().post("fail");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<BooksDbInfo> booksDbInfos = (List<BooksDbInfo>) msg.obj;
                    if (booksDbInfos == null) {
                        return;
                    }
                    if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                        for (BooksDbInfo booksDbInfo : booksDbInfos) {
                            addBooks(booksDbInfo);
                        }
                    }

                    break;
                case 1:
                    if (InternetUtils.checkNet(MyApplicaction.getContext())) {
                        initBooks();
                    }
                    break;

                default:
                    break;
            }

        }

    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        List<MyBooksListInfo> booksListInfos = intent.getParcelableArrayListExtra("list");
        List<BooksDbInfo> booksDbInfos = intent.getParcelableArrayListExtra("books");
        List<BooksDbInfo> initbooks = intent.getParcelableArrayListExtra("initbooks");
        List<MyBooksListInfo> initbooklist = intent.getParcelableArrayListExtra("initbooklist");
        String update_mybook = intent.getStringExtra("update_mybook");
        if (!TextUtils.isEmpty(update_mybook)) {
            initBooks();
        }
        if (/*initbooks != null &&*/ initbooklist != null) {
            String mAccountid = "";
            if (initbooks != null && initbooks.size() > 0) {
                mAccountid = initbooks.get(0).getAccountbookid();
            }
            initBooks();
//            DownUrlUtil.insertBookList(mAccountid, initbooks, initbooklist, handler, 0);
        }

        if (booksDbInfos != null) {
            DownUrlUtil.myBooks(booksDbInfos);
            String bookid = LoginConfig.getInstance().getBookId();
            initInternetDate(bookid);
        }
        if (booksListInfos != null) {
            String jsonpath = intent.getStringExtra("jsonpath");
            if (!TextUtils.isEmpty(jsonpath)) {
                String downloadPath = XzbUtils.getPathString() + "/" + "booklist";
                File file = new File(downloadPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                LoginConfig.getInstance().setJsonbook(LoginConfig.MYJSONGBOOKINFO, jsonpath);
                downloadPath = file.getAbsolutePath();
                // DownUrlUtil.saveJson("jsonpath", jsonpath, downloadPath);
                DownUrlUtil.BooksList(booksListInfos);
                for (MyBooksListInfo myBooksListInfo : booksListInfos) {
                    String cateid = myBooksListInfo.getCategoryid();
                    String url = myBooksListInfo.getCategoryicon();
                    if (TextUtils.isEmpty(url)) {
                        continue;
                    }
                    url = myBooksListInfo.getCategoryicon()
                            .substring(0, myBooksListInfo.getCategoryicon().indexOf("@") + 1) + "3x.png";
                    String cateid_bg = myBooksListInfo.getCategoryid() + "_bg";
                    String url_bg = myBooksListInfo.getCategoryicon().replace("fm", "bg")
                            .substring(0, myBooksListInfo.getCategoryicon().indexOf("@") + 1)
                            + "3x.jpg";

                    hashMap.put(cateid, url);
                    hashMap2.put(cateid_bg, url_bg);

                }
                if (hashMap.size() > 0) {
                    downImage(hashMap, downloadPath);
                    downImage(hashMap2, downloadPath);
                }
            }


        }
        dailycostEntitys = intent.getParcelableArrayListExtra("dailycostEntitys");
//        postBills = intent.getParcelableArrayListExtra("postBills");
        if (viewCallBack == null) {
            viewCallBack = new ViewCallBack() {
            };
        }


        List<DailycostEntity> dailycostEntityList = new ArrayList<>();
        if (dailycostEntitys != null) {
            for (int i = 0; i < dailycostEntitys.size(); i++) {
                DailycostEntity dailycostEntity = dailycostEntitys.get(i);
                String uriPath = dailycostEntity.getBillimg();
                if (!TextUtils.isEmpty(uriPath) && !uriPath.contains("http")) {
                    File file = new File(uriPath);
                    DownUrlUtil.sysQiniu(this, getRealFilePath(Uri.parse(uriPath)), i, viewCallBack, dailycostEntity);
                } else {
                    dailycostEntityList.add(dailycostEntity);
                }
            }

            if (dailycostEntityList.size() > 0) {
                List<PostBill> postBillss = DownUrlUtil.getPostBillData(dailycostEntityList);

                //上传账单
                BaserClassMode.setSynOneDailycostEntity(postBillss, dailycostEntityList, viewCallBack);
            }
        }
        String id = intent.getStringExtra("id");
        String stringurl = intent.getStringExtra("stringurl");
        if (!TextUtils.isEmpty(id)) {

        }

        // return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    private ViewCallBack viewCallBack;

    public String getRealFilePath(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void downImage(HashMap<String, String> hashMap, String downloadPath) {
        DownUrlUtil downUrlUtil = new DownUrlUtil(downloadPath, hashMap, new DownloadStateListener() {

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailed() {
                // TODO Auto-generated method stub

            }
        });
        downUrlUtil.startDownload();
    }

    private void initInternetDate(String id) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {
            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo bookDetailInfo = o;
                List<BooksDbMemberInfo> booksDbMemberInfos = bookDetailInfo.getMember();
                if (booksDbMemberInfos == null) {
                    return;
                }
                DownUrlUtil.BooksDetailMember(bookDetailInfo.getAccountbookid(), booksDbMemberInfos);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg != null) {
                    ToastUtils.DiyToast(MyServer.this, simleMsg.getErrMsg());
                }
            }
        });
    }

    /**
     * 添加账本
     */
    private void addBooks(final BooksDbInfo booksDbIn) {
        String bookName = booksDbIn.getAccountbooktitle();
        String bookcateid = booksDbIn.getAccountbookcate();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", bookcateid);
        hashMap.put("name", bookName);
        CommonFacade.getInstance().exec(Constants.ADD_BOOK, hashMap, new ViewCallBack() {

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                OuterBooksDbInfo outerBooksDbInfo = GsonUtil.GsonToBean(o.toString(), OuterBooksDbInfo.class);
                BooksDbInfo booksDbInfo = outerBooksDbInfo.getData();
                booksDbInfo.setIssynchronization("true");
                booksDbInfo.setIsdelet("false");
                booksDbInfo.setIsnew("0");
                LoginConfig.getInstance().setBookId(booksDbInfo.getAccountbookid());

                DownUrlUtil.sysInfo(booksDbInfo, booksDbIn.getDeviceid());
                BaserClassMode.getBooksCate(booksDbInfo.getAccountbookid());
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg != null) {
                    ToastUtils.DiyToast(MyServer.this, simleMsg.getErrMsg());
                }

            }

        });
    }

    private void initBooks() {

        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                //保存账本模板到本地文件
                JSONObject jsonObject = new JSONObject(o.toString());
                JSONObject templateBooksJson = jsonObject.getJSONObject("catelist");
                BookCoverTemplateListModel bookCoverTemplateListModel = GsonUtil.GsonToBean(templateBooksJson.toString(), BookCoverTemplateListModel.class);
                BookCreatorLocalDataManager.saveTemplateBookCoverList(bookCoverTemplateListModel);

                //以前的逻辑，暂时不动他
                BooksListInfo booksListInfo = GsonUtil.GsonToBean(o.toString(), BooksListInfo.class);
                List<MyBooksListInfo> myBooksListInfos = new ArrayList<MyBooksListInfo>();
                BooksCatelistInfo booksCatelistInfo = booksListInfo.getCatelist();
                if (booksCatelistInfo != null) {
                    List<MyBooksListInfo> single = booksListInfo.getCatelist().getSingle();
                    if (single != null) {
                        for (MyBooksListInfo myBooksListInfo : single) {
                            myBooksListInfos.add(myBooksListInfo);
                        }
                    }
                    List<MyBooksListInfo> multiple = booksListInfo.getCatelist().getMultiple();
                    if (multiple != null) {
                        for (MyBooksListInfo myBooksListInfo : multiple) {
                            myBooksListInfos.add(myBooksListInfo);
                        }
                    }

                }

                List<BooksDbInfo> booksDbInfos = booksListInfo.getAccountbook();
//                if (!LoginConfig.getInstance().getIsFirstStartApp()
//                        && (booksDbInfos == null || booksDbInfos.size() == 0)) {
//                    BaserClassMode.addBooks(MyServer.this, handler);
//                }

                String jsonpath = GsonUtil.GsonString(booksListInfo);
                LoginConfig.getInstance().setJsonbookList(jsonpath);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                HashMap<String, String> hashMap2 = new HashMap<String, String>();
                if (booksDbInfos != null) {
                    for (int i = 0; i < booksDbInfos.size(); i++) {
                        BooksDbInfo booksDbInfo = booksDbInfos.get(i);
                        booksDbInfo.setIssynchronization("true");
                        booksDbInfos.set(i, booksDbInfo);
                    }
                    if (booksDbInfos.size() > 0) {
                        DownUrlUtil.myBooks(booksDbInfos);
                        String bookid = booksDbInfos.get(0).getAccountbookid();
                        LoginConfig.getInstance().setBookId(bookid);
                        initInternetDate(bookid);
                    }

                }
                if (myBooksListInfos != null) {

                    String downloadPath = XzbUtils.getPathString() + "/" + "booklist";
                    File file = new File(downloadPath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    downloadPath = file.getAbsolutePath();
                    DownUrlUtil.saveJson("jsonpath", jsonpath, downloadPath);
                    DownUrlUtil.BooksList(myBooksListInfos);
                    for (MyBooksListInfo myBooksListInfo : myBooksListInfos) {
                        String cateid = myBooksListInfo.getCategoryid();
                        String url = myBooksListInfo.getCategoryicon();
                        if (TextUtils.isEmpty(url)) {
                            continue;
                        }
                        url = myBooksListInfo.getCategoryicon().substring(0,
                                myBooksListInfo.getCategoryicon().indexOf("@") + 1)
                                + "3x.png";
                        String cateid_bg = myBooksListInfo.getCategoryid() + "_bg";
                        String url_bg = myBooksListInfo.getCategoryicon().replace("fm", "bg")
                                .substring(0, myBooksListInfo.getCategoryicon().indexOf("@") + 1)
                                + "3x.jpg";
                        ;

                        hashMap.put(cateid, url);
                        hashMap2.put(cateid_bg, url_bg);

                    }
                    if (hashMap.size() == 0) {
                        return;
                    }

                    downImage(hashMap, downloadPath);
                    downImage(hashMap2, downloadPath);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg != null) {
                    ToastUtils.DiyToast(MyServer.this, simleMsg.getErrMsg());
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
