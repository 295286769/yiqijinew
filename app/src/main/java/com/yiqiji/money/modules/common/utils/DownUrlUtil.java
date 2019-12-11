package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBillMemberColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBookMemberColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBooksColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtInfoColumns;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.entity.BookExpenditure;
import com.yiqiji.money.modules.common.entity.BookExpenditure.ChildBean;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.myserver.MyServer;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.QiniuServer;
import com.yiqiji.money.modules.homeModule.home.entity.StaticsticsBillsInfo;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;
import com.yiqiji.money.modules.homeModule.home.perecenter.StatisticsPerecenter;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class DownUrlUtil {
    public static List<String> expexe_id = new ArrayList<String>();
    public static String imageUri = XzbUtils.getPathString() + "/"; // SD卡图片
    public static String jsonPath = "file://" + XzbUtils.getPathString() + "/";
    public static String jsonPathString = XzbUtils.getPathString() + "/" + "bookid_";
    public static String booklistpath = XzbUtils.getPathString() + "/" + "booklist" + "/" + "bookid_";
    public static String bookimage = XzbUtils.getPathString() + "/" + "booklist" + "/";
    public static String bookimage_new = XzbUtils.getPathString() + "/" + "booklist" + "/" + "type" + "/";
    public static String booklistjsonname = "jsonpath";
    public static String suffix2 = "@2x.png"; // 后缀
    public static String suffix3 = "@3x.png"; // 后缀
    private static String TAG = "DownloadService";
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;
    private static ExecutorService LIMITED_TASK_EXECUTOR = null;
    private static final ExecutorService FULL_TASK_EXECUTOR = null;
    public static final ExecutorService DEFAULT_TASK_EXECUTOR;
    private static Object lock = new Object();

    static {
        SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();
        LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(3);
        // FULL_TASK_EXECUTOR = (ExecutorService)
        // Executors.newCachedThreadPool();
        DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
    }

    ;
    // 下载状态监听，提供回调
    DownloadStateListener listener;
    // 下载目录
    private String downloadPath;

    // 下载链接集合
    private List<String> listURL;
    private List<HashMap<String, String>> list;
    private HashMap<String, String> hashMap;
    // 下载个数
    private int size = 0;
    private static int total = 0;

    // 下载完成回调接口
    public interface DownloadStateListener {
        public void onFinish();

        public void onFailed();
    }

    public DownUrlUtil(String downloadPath, HashMap<String, String> hashMap, DownloadStateListener listener) {
        this.downloadPath = downloadPath;
        // this.listURL = listURL;
        this.listener = listener;
        this.hashMap = hashMap;
        expexe_id.clear();

    }

    /**
     * 暂未提供设置
     */
    public void setDefaultExecutor() {

    }

    /**
     * 开始下载
     */
    public void startDownload() {
        // 首先检测path是否存在
        File downloadDirectory = new File(downloadPath);
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            final String id = (String) entry.getKey();
            final String url = (String) entry.getValue();
            if (TextUtils.isEmpty(url)) {
                continue;

            }
            // 捕获线程池拒绝执行异常
            try {
                // 线程放入线程池
                DEFAULT_TASK_EXECUTOR.execute(new Runnable() {

                    @Override
                    public void run() {
                        downloadBitmap(url, id);
                    }
                });
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
                Log.e(TAG, "thread pool rejected error");
                listener.onFailed();
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailed();
            }

        }

        // for (final Map<<String, String>>.Entry hashMap : list.) {
        //
        // }

    }

    /**
     * 下载图片
     *
     * @param urlString
     * @return
     */
    private File downloadBitmap(String urlString, String id) {
        String fileName = id;
        // 图片命名方式
        File cacheFile = null;
        if (TextUtils.isEmpty(urlString)) {
            return null;
        }
        try {
            cacheFile = new File(downloadPath, fileName);
            if (cacheFile.exists()) {
                if (cacheFile.length() == 0) {
                    cacheFile.delete();
                    cacheFile.createNewFile();
                } else {
                    return cacheFile;
                }

            } else {
                cacheFile.createNewFile();
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        expexe_id.add(fileName);
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            final InputStream in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile), IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            // 每下载成功一个，统计一下图片个数
            // statDownloadNum();
            return cacheFile;

        } catch (final IOException e) {
            // 有一个下载失败，则表示批量下载没有成功
            Log.e(TAG, "download " + urlString + " error");
            listener.onFailed();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }

        return null;
    }

    /**
     * Creates a constant cache file path given a target cache directory and an
     * image key.
     *
     * @param cacheDir
     * @param key
     * @return
     */
    public static String createFilePath(File cacheDir, String key) {
        // Use URLEncoder to ensure we have a valid filename, a tad hacky
        // but it will do for
        // this example
        return cacheDir.getAbsolutePath();

    }

    /**
     * 统计下载个数
     */
    private void statDownloadNum() {
        synchronized (lock) {
            size++;
            if (size == hashMap.size() * 2) {
                total = 0;
                Log.d(TAG, "download finished total " + size);
                // 释放资源
                // DEFAULT_TASK_EXECUTOR.shutdown();
                // 如果下载成功的个数与列表中 url个数一致，说明下载成功
                listener.onFinish(); // 下载成功回调
            }
        }
    }

    public static void downLoad(BookExpenditure bookExpenditure) {
        MyApplicaction.ids.clear();
        if (bookExpenditure == null) {
            return;
        }
        List<ChildBean> childBeans = bookExpenditure.getChild();
        if (childBeans == null) {
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        for (ChildBean childBean : childBeans) {
            String id = childBean.getCategoryid();
            String url = childBean.getCategoryicon();
            if (TextUtils.isEmpty(url)) {
                continue;
            }
            url = url.replace("2x", "3x");
            hashMap.put(id, url);
            String[] u = url.split("/");
            String u_s[] = u[u.length - 1].split("@");
            String url_s = url.substring(0, url.lastIndexOf("/")) + "/" + id + "_s" + "@" + u_s[1];

            hashMap2.put(id + "_s", url_s);
            if (childBean.getChild() != null) {
                for (ChildBean childBean2 : childBean.getChild()) {
                    url = childBean2.getCategoryicon();
                    if (TextUtils.isEmpty(url)) {
                        continue;
                    }
                    url = url.replace("2x", "3x");
                    url_s = url.substring(0, url.indexOf("@")) + "_s" + url.substring(url.indexOf("@"), url.length());
                    id = childBean2.getCategoryid();
                    hashMap.put(id, url);
                    hashMap2.put(id + "child_s", url_s);
                }
            }

        }
        if (hashMap2.size() == 0) {
            return;
        }

        String downloadPath = XzbUtils.getPathString();

//        DownUrlUtil downUrlUtil = new DownUrlUtil(downloadPath, hashMap, new DownloadStateListener() {
//
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onFailed() {
//
//            }
//        });
//        downUrlUtil.startDownload();
        DownUrlUtil downUrlUtil2 = new DownUrlUtil(downloadPath, hashMap2, new DownloadStateListener() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailed() {

            }
        });
        downUrlUtil2.startDownload();
    }

    public static void saveJson(final String id, final String jsonString, final String pathJson) {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            DEFAULT_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    FileOutputStream outStream = null;
                    try {
                        if (TextUtils.isEmpty(pathJson)) {
                            return;
                        }
                        File file = new File(pathJson, "bookid_" + id);
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        outStream = new FileOutputStream(file);
                        outStream.write(jsonString.getBytes());

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outStream != null) {
                                outStream.close();
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private static String ret = "";

    // 读取sdcard文件
    public static void sdcardRead(final String fileName, final Handler handler) {
        if (TextUtils.isEmpty(fileName)) {
            return;
        }

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            DEFAULT_TASK_EXECUTOR.execute(new Runnable() {

                @SuppressWarnings("unused")
                @Override
                public void run() {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(fileName);
                        if (fis == null) {
                            return;
                        }
                        int len = fis.available();
                        byte[] buffer = new byte[len];

                        fis.read(buffer);
                        ret = EncodingUtils.getString(buffer, "UTF-8");
                        Message msg = Message.obtain();
                        msg.what = 110;
                        msg.obj = ret;
                        handler.sendMessage(msg);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fis != null) {
                                fis.close();
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void sysInfo(final BooksDbInfo booksDbInfo, final String mDeviceid) {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBooksColumns.DEVICEID + "=?";
                    String whereStrings[] = new String[]{mDeviceid};
                    DbInterface.updateBooks(booksDbInfo, where, whereStrings);

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有账本
     */
    public static void deletBookInfos() {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    DbInterface.deletBooks(null, null);

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void searchTime(final String mAccountId, final Handler handler, final int what) {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED + "=?";
                    String whereStrings[] = new String[]{mAccountId, "false"};
                    String orderBy = DtInfoColumns.TRADETIME + " ASC";
                    String limit = "1";
                    List<DailycostEntity> dailycostEntitys = DbInterface.getQueryDailycostEntitys(where, whereStrings,
                            null, null, orderBy, limit);
                    DailycostEntity dailycostEntity = null;
                    if (dailycostEntitys != null && dailycostEntitys.size() != 0) {
                        dailycostEntity = dailycostEntitys.get(0);
                    }
                    long cTimeLong;
                    if (dailycostEntity == null) {
                        cTimeLong = new Date().getTime();
                    } else {
                        String cTime = dailycostEntity.getTradetime();
                        cTimeLong = Long.parseLong(cTime) * 1000;
                    }
                    Message message = new Message();
                    message.what = what;

                    message.obj = cTimeLong;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 跟新账本和账本成员
     *
     * @param booksDbInfo
     */
    public static void updateBook(final BooksDbInfo booksDbInfo, final String mAccountid) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (booksDbInfo != null) {
                        String where = null;
                        String[] selectionArgs = null;
                        where = DtBooksColumns.ACCOUNTBOOKID + "=?";
                        selectionArgs = new String[]{mAccountid};
                        long id_book = DbInterface.updateBooks(booksDbInfo, where, selectionArgs);
                        if (id_book == 0) {
                            DbInterface.insertBooks(booksDbInfo);
                        }
                        if (id_book > -1) {
                            List<BooksDbMemberInfo> booksDbMemberInfos = booksDbInfo.getMember();
                            if (booksDbMemberInfos != null) {
                                where = DtBookMemberColumns.BOOKID + "=?";
                                selectionArgs = new String[]{mAccountid};
                                DbInterface.deletAndAddBooksMember(booksDbMemberInfos, where, selectionArgs);
                            }
                        }
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param mAccountBookid
     * @param booksDbMemberInfos
     */
    public static void BooksDetailMember(final String mAccountBookid, final List<BooksDbMemberInfo> booksDbMemberInfos) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBookMemberColumns.BOOKID + "=?";
                    String selectionArgs[] = new String[]{mAccountBookid};
                    DbInterface.deletAndAddBooksMember(booksDbMemberInfos, where, selectionArgs);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化账本分类列表
     *
     * @param myBooksListInfos
     */
    public static void BooksList(final List<MyBooksListInfo> myBooksListInfos) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (myBooksListInfos == null) {
                        return;
                    }
                    DbInterface.insertBookList(myBooksListInfos);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化账本
     */
    public static void initBooksData(final String bookid, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBooksColumns.ACCOUNTBOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    BooksDbInfo booksDbInfo = DbInterface.getBooksDbInfo(where, selectionArgs, null, null, null, null);
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfo;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化账本在根据账本查询成员
     */
    public static void initBooksDataAndMember(final String bookid, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBooksColumns.ACCOUNTBOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    BooksDbInfo booksDbInfo = DbInterface.getBooksDbInfo(where, selectionArgs, null, null, null, null);
                    // if (booksDbInfo == null) {
                    // return;
                    // }
                    if (booksDbInfo != null) {
                        where = DtBookMemberColumns.BOOKID + "=?";
                        String[] selectionArgsmember = new String[]{booksDbInfo.getAccountbookid()};
                        List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                                selectionArgsmember, null, null, null, null);
                        booksDbInfo.setMember(booksDbMemberInfos);
                    }
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfo;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化账本在根据账本查询成员
     */
    public static void initBooksDataAndMember(final String bookid, final ViewCallBack<BooksDbInfo> viewCallBack) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBooksColumns.ACCOUNTBOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    BooksDbInfo booksDbInfo = DbInterface.getBooksDbInfo(where, selectionArgs, null, null, null, null);
                    // if (booksDbInfo == null) {
                    // return;
                    // }
                    if (booksDbInfo != null) {
                        where = DtBookMemberColumns.BOOKID + "=?";
                        String[] selectionArgsmember = new String[]{booksDbInfo.getAccountbookid()};
                        List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                                selectionArgsmember, null, null, null, null);
                        booksDbInfo.setMember(booksDbMemberInfos);
                        try {
                            viewCallBack.onSuccess(booksDbInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        viewCallBack.onFailed(null);
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据账本获取人员
     *
     * @param bookid
     * @param handler
     * @param what
     */
    public static void getPeopleByBook(final String bookid, final Handler handler, final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBookMemberColumns.BOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where, selectionArgs,
                            null, null, null, null);
                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = booksDbMemberInfos;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存我的账本
     */
    public static void myBooks(final List<BooksDbInfo> booksDbInfos) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.deletAndInsert(booksDbInfos);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化成员
     */
    public static void initMemberData(final String mAccountBookid, final Handler handler, final int what) {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBookMemberColumns.BOOKID + "=?";
                    String[] selectionArgs = new String[]{mAccountBookid};
                    List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                            selectionArgs, null, null, null, null);
                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = booksDbMemberInfos;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 求当前月份收入和支出的总和天作为为分组
     *
     * @return
     */
    public static void initDayTotal(final Context mContext, final Date record_date, final String mAccountbookid,
                                    final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<TotalBalance> list = DbInterface.getTotalBalanceTypeDay(mContext.getContentResolver(),
                            Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 5)),
                            Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)), mAccountbookid);
                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化消费或支出总额
     *
     * @param record_date
     */
    public static void initData(final Context mContext, final Date record_date, final String mAccountbookid,
                                final Handler handler, final int what) {

        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<TotalBalance> list = DbInterface.getTotalBalanceGroupByType(mContext.getContentResolver(),
                            Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 5)),
                            Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)), mAccountbookid, "false");
                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新账单
     *
     * @param dailycostEntities
     */
    public static void updateMultipleRecords(final List<DailycostEntity> dailycostEntities, final List<String> dailycostEntityList) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    long id = DbInterface.updateMultipleRecordsDailycostInfo(dailycostEntities);
                    for (int j = 0; j < dailycostEntities.size(); j++) {
                        DailycostEntity dailycostEntity = dailycostEntities.get(j);
                        String billid = dailycostEntity.getBillid();
                        String billidListitem = dailycostEntityList.get(j);
                        List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                        if (billMemberInfos != null) {
                            String where = DtBillMemberColumns.BILLID + "=? ";
                            String whereStrings[] = {billidListitem};

                            for (int i = 0; i < billMemberInfos.size(); i++) {
                                BillMemberInfo billMemberInfo = billMemberInfos.get(i);
                                billMemberInfo.setBillid(billid);
                                billMemberInfo.setIssynchronization("true");
                                billMemberInfos.set(i, billMemberInfo);
                            }

                            DbInterface.deletAndAdd(where, whereStrings, billMemberInfos);

                        }
                    }
                    for (DailycostEntity dailycostEntity : dailycostEntities) {

                    }
//                    if (id > -1) {
//                        EventBus.getDefault().post("getBills");
//
//                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化账单
     * <p/>
     * record_date时间
     * mAccounBooid账本id
     * sorttype是否显示时间搓
     * billType是否需要分类
     * isExpectMoth是否是预期账单
     * pager账单页数
     * pagersize账单每页大小
     * handler
     * what
     */
    public static void initDataReturnDailycostEntitys(final Date record_date, final String mAccounBooid,
                                                      final String sorttype, final String billType, final boolean isExpectMoth, final int pager,
                                                      final int pagersize, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (sorttype == null) {
                        return;
                    }
                    String year = DateUtil.formatTheDateToMM_dd(record_date, 5);
                    String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)) + "";
                    String selection = null;
                    String[] selectionArg = null;
                    String groupBy = null;
                    String having = null;
                    String orderBy = DtInfoColumns.TRADETIME + " DESC";
                    String limit = (pager - 1) * pagersize + "," + pagersize;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                    calendar.add(Calendar.MONTH, 120);// 结束时间
                    long end_time = calendar.getTimeInMillis() / 1000;
                    List<DailycostEntity> list;
                    if (TextUtils.isEmpty(billType)) {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED + "=? and " + DtInfoColumns.BILLSTATUS + "=? ";
                            String[] selectionArgs = {mAccounBooid, "false", "1"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期

                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED
                                        + "=? and " + DtInfoColumns.TRADETIME + ">? and " + DtInfoColumns.TRADETIME
                                        + "<? and " + DtInfoColumns.BILLSTATUS + "=? ";
                                String[] selectionArgs = {mAccounBooid, "false", start_time + "", end_time + "", "1"};
                                selectionArg = selectionArgs;
                            } else {
                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.YEAR + "=? and "
                                        + DtInfoColumns.MOTH + "=? and " + DtInfoColumns.ISDELETED + "=? and " + DtInfoColumns.BILLSTATUS + "=? ";
                                String[] selectionArgs = {mAccounBooid, year, moth, "false", "1"};
                                selectionArg = selectionArgs;
                            }

                        }
                    } else {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLTYPE + "=? and "
                                    + DtInfoColumns.ISDELETED + "=? and " + DtInfoColumns.BILLSTATUS + "=? ";
                            String[] selectionArgs = {mAccounBooid, billType, "false", "1"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期
                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED
                                        + "=? and " + DtInfoColumns.BILLTYPE + "=? and " + DtInfoColumns.TRADETIME
                                        + ">? and " + DtInfoColumns.TRADETIME + "<? and " + DtInfoColumns.BILLSTATUS + "=? ";
                                String[] selectionArgs = {mAccounBooid, "false", billType, start_time + "",
                                        end_time + "", "1"};
                                selectionArg = selectionArgs;
                            } else {

                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.YEAR + "=? and "
                                        + DtInfoColumns.MOTH + "=? and " + DtInfoColumns.BILLTYPE + "=? and "
                                        + DtInfoColumns.ISDELETED + "=? and " + DtInfoColumns.BILLSTATUS + "=? ";
                                String[] selectionArgs = {mAccounBooid, year, moth, billType, "false", "1"};
                                selectionArg = selectionArgs;
                            }

                        }
                    }
                    list = DbInterface.queryDailycostEntitys(selection, selectionArg, groupBy, having, orderBy, limit);
                    for (int i = 0; i < list.size(); i++) {
                        DailycostEntity dailycostEntity = list.get(i);
                        selection = DtBillMemberColumns.BILLID + "=?";
                        String[] selectionArgs = {dailycostEntity.getBillid()};
                        selectionArg = selectionArgs;
                        List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(selection, selectionArg, null, null, null, limit);
                        dailycostEntity.setMemberlist(billMemberInfos);
                        list.set(i, dailycostEntity);
                    }
                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 统计账单分类列表
     * <p/>
     * record_date时间
     * mAccounBooid账本id
     * sorttype是否显示时间搓
     * billType是否需要分类
     * isExpectMoth是否是预期账单
     * pager账单页数
     * pagersize账单每页大小
     * handler
     * what
     */
    public static void getTotalBalances(final Date record_date, final String mAccounBooid, final String sorttype,
                                        final String billType, final boolean isExpectMoth, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (sorttype == null) {
                        return;
                    }
                    String year = DateUtil.formatTheDateToMM_dd(record_date, 5);
                    String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)) + "";
                    String selection = null;
                    String[] selectionArg = null;
                    String groupBy = DtInfoColumns.BILLCATEID;
                    String having = null;
                    String orderBy = "SUM(" + DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                    String limit = null;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                    calendar.add(Calendar.MONTH, 12);// 结束时间
                    long end_time = calendar.getTimeInMillis() / 1000;
                    List<TotalBalance> list;
                    if (TextUtils.isEmpty(billType)) {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED + "=? ";
                            String[] selectionArgs = {mAccounBooid, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期

                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED
                                        + "=? and " + DtInfoColumns.TRADETIME + ">? and " + DtInfoColumns.TRADETIME
                                        + "<? ";
                                String[] selectionArgs = {mAccounBooid, "false", start_time + "", end_time + ""};
                                selectionArg = selectionArgs;
                            } else {
                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.YEAR + "=? and "
                                        + DtInfoColumns.MOTH + "=? and " + DtInfoColumns.ISDELETED + "=? ";
                                String[] selectionArgs = {mAccounBooid, year, moth, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    } else {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLTYPE + "=? and "
                                    + DtInfoColumns.ISDELETED + "=? ";
                            String[] selectionArgs = {mAccounBooid, billType, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期
                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.ISDELETED
                                        + "=? and " + DtInfoColumns.BILLTYPE + "=? and " + DtInfoColumns.TRADETIME
                                        + ">? and " + DtInfoColumns.TRADETIME + "<? ";
                                String[] selectionArgs = {mAccounBooid, "false", billType, start_time + "",
                                        end_time + ""};
                                selectionArg = selectionArgs;
                            } else {

                                selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.YEAR + "=? and "
                                        + DtInfoColumns.MOTH + "=? and " + DtInfoColumns.BILLTYPE + "=? and "
                                        + DtInfoColumns.ISDELETED + "=? ";
                                String[] selectionArgs = {mAccounBooid, year, moth, billType, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    }
                    list = DbInterface.getTotalBalances(selection, selectionArg, groupBy, having, orderBy, limit);
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {

                            List<TotalBalance> totalBalanceschilds = null;


                            TotalBalance totalBalance = list.get(i);
                            LogUtil.log_msg("++++++++++++++++" + totalBalance.getBillsubcateid());
                            String billcateid = totalBalance.getBillcateid();
                            String billsubcateid = totalBalance.getBillsubcateid();
                            StaticsticsBillsInfo staticsticsBillsInfo1 = StatisticsPerecenter.getStaticsticsBillsInfos(record_date, mAccounBooid, sorttype, billType, billcateid, billsubcateid);
                            if (staticsticsBillsInfo1 == null) {
                                return;
                            }
//                            String where = staticsticsBillsInfo.getWhere();
//                            String[] whereStrings = staticsticsBillsInfo.getWhereStrings();
//                            String groupBy1 = staticsticsBillsInfo.getGroupBy();
//                            String having1 = staticsticsBillsInfo.getHaving();
//                            String orderBy1 = staticsticsBillsInfo.getOrderBy();
//                            String limit1 = staticsticsBillsInfo.getLimit();
                            totalBalanceschilds = DbInterface.getTotalBalances(staticsticsBillsInfo1.getWhere(), staticsticsBillsInfo1.getWhereStrings(), staticsticsBillsInfo1.getGroupBy(), staticsticsBillsInfo1.getHaving(), staticsticsBillsInfo1.getOrderBy(), staticsticsBillsInfo1.getLimit());
                            for (int j = 0; j < totalBalanceschilds.size(); j++) {
                                TotalBalance totalBalanceschild = totalBalanceschilds.get(j);
                                String biicateid_child = totalBalanceschild.getBillcateid();
                                String billsubcateid_child = totalBalanceschild.getBillsubcateid();
                                StaticsticsBillsInfo staticsticsBillsInfo = StatisticsPerecenter.getBillsCount(record_date, mAccounBooid, sorttype, billType, biicateid_child, billsubcateid_child);
//                                List<DailycostEntity> dailycostEntities = DbInterface.queryDailycostEntitys(staticsticsBillsInfo.getWhere(), staticsticsBillsInfo.getWhereStrings(), staticsticsBillsInfo.getGroupBy(), staticsticsBillsInfo.getHaving(), staticsticsBillsInfo.getOrderBy(), staticsticsBillsInfo.getLimit());
                                int countSize1 = DbInterface.getSameClassification(staticsticsBillsInfo.getWhere(), staticsticsBillsInfo.getWhereStrings(), staticsticsBillsInfo.getGroupBy(), staticsticsBillsInfo.getHaving(), staticsticsBillsInfo.getOrderBy(), staticsticsBillsInfo.getLimit());
                                LogUtil.log_msg("++dsdsaa++++" + countSize1);
                                int countSize = countSize1;
                                totalBalanceschild.setCounts(countSize);
                                totalBalanceschilds.set(j, totalBalanceschild);
                            }
                            totalBalance.setTotalBalances(totalBalanceschilds);
                            list.set(i, totalBalance);

                        }
                    }

                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化账单
     * <p/>
     */
    public static void updateDailycostEntitys(final DailycostEntity dailycostEntity, final String mAccounBooid,
                                              final String billid) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                    if (billMemberInfos != null) {
                        String where = DtBillMemberColumns.BILLID + "=?";
                        String selectionArgsString[] = new String[]{billid};
                        DbInterface.deletAndAdd(where, selectionArgsString, billMemberInfos);

                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计饼图预期
     *
     * @param record_date
     */
    public static void getDailycostMothInfosTypeFirstThreeLast(final Context context, final Date record_date,
                                                               final String mAccounBooid, final String mbilltype, final String sorttype, final Handler handler) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<TotalBalance> list;
                    boolean isExpectMoth = DateUtil.isExpectMoth(record_date);
                    int what = 3;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    if (isExpectMoth) {// 预期月份
                        what = RequsterTag.EXPECTMOTHBING;

                        long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                        calendar.add(Calendar.YEAR, 120);// 结束时间
                        long end_time = calendar.getTimeInMillis() / 1000;
                        String selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLTYPE + "=? and " + DtInfoColumns.TRADETIME + ">=? and " + DtInfoColumns.TRADETIME + "<? and " + DtInfoColumns.ISDELETED + "=? ";
                        String[] selectionArgs = new String[]{mAccounBooid, mbilltype, start_time + "", end_time + "", "false"};
                        String groupBy = DtInfoColumns.BILLCATEID;
                        String orderBy = "SUM(" + DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                        String limit = "3";
                        list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, limit);

                    } else {
                        if (sorttype.equals("1")) {//
                            String selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLTYPE + "=? and " + DtInfoColumns.YEAR + "=? and " + DtInfoColumns.MOTH + "=? and " + DtInfoColumns.ISDELETED + "=? ";
                            String[] selectionArgs = new String[]{mAccounBooid, mbilltype, calendar.get(Calendar.YEAR) + "", (calendar.get(Calendar.MONTH) + 1) + "", "false"};
                            String groupBy = DtInfoColumns.BILLCATEID;
                            String orderBy = "SUM(" + DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                            String limit = "3";
                            list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, limit);


                        } else {
                            String selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLTYPE + "=? and " + DtInfoColumns.ISDELETED + "=? ";
                            String[] selectionArgs = new String[]{mAccounBooid, mbilltype, "false"};
                            String groupBy = DtInfoColumns.BILLCATEID;
                            String orderBy = "SUM(" + DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                            String limit = "3";
                            list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, limit);
                        }

                    }

                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询某一账单和账单成员
     */
    public static void searchDailycostEntitysAndMember(final String mAccounBooid, final String mBillid,
                                                       final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DailycostEntity dailycostEntity;
                    String selection = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLID + "=?";

                    String[] selectionArgs = {mAccounBooid, mBillid};
                    String orderBy = null;
                    String limit = null;
                    dailycostEntity = DbInterface.getDataBaeDailycostEntity(selection, selectionArgs, null, null,
                            orderBy, null);
                    if (dailycostEntity != null) {
                        selection = DtBillMemberColumns.BILLID + "=?";
                        String[] selectionArgsStrings = {mBillid};
                        List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(selection,
                                selectionArgsStrings, null, null, null, null);
                        dailycostEntity.setMemberlist(billMemberInfos);
                    }
                    Message msg = Message.obtain();
                    msg.obj = dailycostEntity;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除账单
     * <p/>
     * _id
     * listData
     * position
     */
    public static void deleteDailycostInfo(final String billid, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @SuppressWarnings("unused")
                @Override
                public void run() {
                    long id = DbInterface.deleteDailycostEntityDataBase(billid);
                    if (id > -1) {
                        Message message = new Message();
                        message.what = what;
                        handler.sendMessage(message);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询账单成员
     *
     * @param billid
     * @param i
     */
    public static void seachBillMember(final String billid, final int i, final int what, final Handler handler) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DtBillMemberColumns.BILLID + "=?";
                    String[] selectionArgs = new String[]{billid};
                    List<BillMemberInfo> infos = DbInterface.getListBillDbMemberInfo(where, selectionArgs, null, null,
                            null, null);
                    Message message = Message.obtain();
                    message.what = 2;
                    message.arg1 = i;
                    message.obj = infos;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询账单成员并赋值给账单
     * <p/>
     * billid
     * i
     */
    public static void seachBillMemberAssignmentDai(final List<DailycostEntity> dailycostEntities, final int what,
                                                    final Handler handler) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    for (int j = 0; j < dailycostEntities.size(); j++) {
                        DailycostEntity dailycostEntity = dailycostEntities.get(j);
                        String billid = dailycostEntity.getBillid();
                        String where = DtBillMemberColumns.BILLID + "=?";
                        String[] selectionArgs = new String[]{billid};
                        List<BillMemberInfo> infos = DbInterface.getListBillDbMemberInfo(where, selectionArgs, null,
                                null, null, null);
                        dailycostEntity.setMemberlist(infos);
                        dailycostEntities.set(j, dailycostEntity);
                    }

                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = dailycostEntities;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 同步账单和账单成员
     *
     * @param dailycostEntities
     */
    public static void synchronization(final String mAccounbookid, final List<DailycostEntity> dailycostEntities,
                                       final String mSorttype, final long start_time, final long end_time, final Handler handler, final int what, final boolean islogin, final boolean isNeedBookDetail) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    String wheres = null;
                    String whereStrings[] = null;
                    if (mSorttype.equals("0")) {// 没有时间tab
                        wheres = DtInfoColumns.ACCOUNTBOOKID + "=?";
                        String whereStr[] = new String[]{mAccounbookid};
                        whereStrings = whereStr;
                    } else {
                        if (islogin) {
                            wheres = DtInfoColumns.ACCOUNTBOOKID + "=?";
                            String whereStr[] = new String[]{mAccounbookid};
                            whereStrings = whereStr;
                        } else {
                            wheres = DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.TRADETIME + ">? and "
                                    + DtInfoColumns.TRADETIME + "<?";
                            String whereString[] = new String[]{mAccounbookid, start_time + "", end_time + ""};
                            whereStrings = whereString;
                        }

                    }
                    long id = DbInterface.deleAllAndAdd(wheres, whereStrings, dailycostEntities);
                    if (id > -1) {
                        for (DailycostEntity dailycostEntity : dailycostEntities) {
                            String where = DtBillMemberColumns.BILLID + "=?";
                            String whereBill[] = new String[]{dailycostEntity.getBillid()};
                            DbInterface.deletAndAdd(where, whereBill, dailycostEntity.getMemberlist());
                        }

                    }
                    Message message = null;
                    switch (what) {
                        case RequsterTag.SEARCHDAICOYS:
                            message = new Message();
                            message.what = RequsterTag.SEARCHDAICOYS;
                            handler.sendMessage(message);
                            break;
                        case RequsterTag.SEARCHBOOKDETAIL:
                            message = new Message();
                            message.what = RequsterTag.SEARCHBOOKDETAIL;
                            message.obj = isNeedBookDetail;
                            handler.sendMessage(message);
//                            if (islogin) {//重新查询最早时间
//                                message = new Message();
//                                message.what = RequsterTag.SEARCHDAICOYS;
//                                handler.sendMessage(message);
//                            }
                            break;

                        default:
                            break;
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入账本列表
     * <p/>
     * dailycostEntities
     */
    public static void insertBookList(final String mAccountbookid, final List<BooksDbInfo> booksDbInfos,
                                      final List<MyBooksListInfo> booksListInfos, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (booksDbInfos != null) {
                        long id_book = DbInterface.insertBooksMyList(booksDbInfos);
                        if (id_book > 0) {
                            LoginConfig.getInstance().setBookId(
                                    booksDbInfos.get(0).getDeviceid());
                            Message message = Message.obtain();
                            message.what = what;
                            message.obj = booksDbInfos;
                            handler.sendMessage(message);

                        }
                    }
                    if (booksListInfos != null) {
                        LoginConfig.getInstance().setBookId(mAccountbookid);
                        long id = DbInterface.insertBookList(booksListInfos);
                        if (id > 0) {
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按条件查询账本列表
     *
     * @param
     */
    public static void serchBookList(final String where, final String[] selectionArgs, final String groupBy,
                                     final String having, final String orderBy, final String limit, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<BooksDbInfo> booksDbInfos = DbInterface.getListBooksDbInfo(where, selectionArgs, groupBy,
                            having, orderBy, limit);
                    if (booksDbInfos != null) {
                        for (BooksDbInfo booksDbInfo : booksDbInfos) {
                            String wheremember = DtBookMemberColumns.BOOKID + "=? ";
                            String whereStringsmember[] = new String[]{booksDbInfo.getAccountbookid()};
                            List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(wheremember, whereStringsmember, null, null, null, null);
                            booksDbInfo.setMember(booksDbMemberInfos);
                        }
                    }

                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfos;

                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按条件查询账本列表
     *
     * @param
     */
    public static void serchBookList(final String where, final String[] selectionArgs, final String groupBy,
                                     final String having, final String orderBy, final String limit, final ViewCallBack<List<BooksDbInfo>> viewCallBack) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<BooksDbInfo> booksDbInfos = DbInterface.getListBooksDbInfo(where, selectionArgs, groupBy,
                            having, orderBy, limit);
                    if (booksDbInfos != null) {
                        for (BooksDbInfo booksDbInfo : booksDbInfos) {
                            String wheremember = DtBookMemberColumns.BOOKID + "=? ";
                            String whereStringsmember[] = new String[]{booksDbInfo.getAccountbookid()};
                            List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(wheremember, whereStringsmember, null, null, null, null);
                            booksDbInfo.setMember(booksDbMemberInfos);
                        }
                        try {
                            viewCallBack.onSuccess(booksDbInfos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按条件查询账本
     *
     * @param
     */
    public static void serchBook(final String where, final String[] selectionArgs, final String groupBy,
                                 final String having, final String orderBy, final String limit, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    BooksDbInfo booksDbInfos = DbInterface.getBooksDbInfo(where, selectionArgs, groupBy, having,
                            orderBy, limit);
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfos;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按条件删除账本
     *
     * @param
     */
    public static void deleBook(final String where, final String[] selectionArgs, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {

                    long id = DbInterface.deletBooks(where, selectionArgs);
                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDailycostInfo(final DailycostEntity dailycostEntity, final Handler handler, final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.addDailycostInfo(dailycostEntity);
                    List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                    if (billMemberInfos != null) {
                        String where = DtBillMemberColumns.BILLID + "=?";
                        String whereBill[] = new String[]{dailycostEntity.getBillid()};
                        DbInterface.deletAndAdd(where, whereBill, billMemberInfos);
                    }
                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDataBaseDailycostInfo(final DailycostEntity dailycost, final String whereClause,
                                                   final String[] whereArgs, final Handler handler, final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.updateDataBaseDailycostInfo(dailycost, whereClause, whereArgs);
                    List<BillMemberInfo> billMemberInfos = dailycost.getMemberlist();
                    if (billMemberInfos != null && id > -1) {
                        String where = DtBillMemberColumns.BILLID + "=? and " + DtBillMemberColumns.MEMBERID + "=? ";
                        DbInterface.updateBillMember(billMemberInfos, where, dailycost.getBillid());
                    }

                    Message message = Message.obtain();
                    if (handler != null) {
                        message.what = what;
                        handler.sendMessage(message);
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getNativePeopleByBook(final String whereClause, final String[] whereArgs, final Handler handler,
                                             final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(whereClause,
                            whereArgs, null, null, null, null);

                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = booksDbMemberInfos;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getNativePeopleByBill(final String whereClause, final String[] whereArgs, final Handler handler,
                                             final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    List<BillMemberInfo> mBillMemberInfo = BillMemberInfo.getListBillDbMemberInfo(whereClause,
                            whereArgs, null, null, null, null);

                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = mBillMemberInfo;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDailycostInfo(final DailycostEntity dailycostEntity, final Handler handler, final int what,
                                        final List<BillMemberInfo> billMemberInfoList) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.addDailycostInfo(dailycostEntity);
                    BillMemberInfo.insertList(billMemberInfoList);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDataBaseDailycostInfo(final DailycostEntity dailycost, final String whereClause,
                                                   final String[] whereArgs, final Handler handler, final int what,
                                                   final List<BillMemberInfo> billMemberInfoList) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.updateDataBaseDailycostInfo(dailycost, whereClause, whereArgs);

                    long ids = BillMemberInfo.delet(whereClause, whereArgs);
                    BillMemberInfo.insertList(billMemberInfoList);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询本地的所有账本模板
     *
     * @param handler
     * @param what
     */
    public static void getMyBooksListInfo(final Handler handler, final int what) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    List<MyBooksListInfo> myBooksListInfo = MyBooksListInfo.getMyBooksListInfo();

                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = myBooksListInfo;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getListBooksDbInfo(final Handler handler, final int what, final String where,
                                          final String[] selectionArgs, final String groupBy, final String having, final String orderBy,
                                          final String limit) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    List<BooksDbInfo> booksDbInfos = DbInterface.getListBooksDbInfo(where, selectionArgs, groupBy,
                            having, orderBy, limit);

                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfos;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateBooks(final Handler handler, final int what, final BooksDbInfo booksDbInfo,
                                   final String where, final String... selectionArgs) {

        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.updateBooks(booksDbInfo, where, selectionArgs);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateBooks(final ViewCallBack viewCallBack, final BooksDbInfo booksDbInfo,
                                   final String where, final String... selectionArgs) {

        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.updateBooks(booksDbInfo, where, selectionArgs);
                    try {
                        viewCallBack.onSuccess("true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Message message = Message.obtain();
//                    message.what = what;
//                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void deletBooks(final Handler handler, final int what, final String where,
                                  final String... selectionArgs) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.deletBooks(where, selectionArgs);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletBooks(final ViewCallBack viewCallBack, final String where, final String... selectionArgs) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.deletBooks(where, selectionArgs);
                    try {
                        viewCallBack.onSuccess(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBookNative(final Handler handler, final int what, final BooksDbInfo booksDbInfo) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.insertBooks(booksDbInfo);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seachUnSys(final Context context, final BooksDbInfo booksDbInfo, final Handler handler) {
        try {
            total += 1;
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    List<DailycostEntity> dailycostEntities = new ArrayList<>();
                    List<PostBill> postBills = new ArrayList<>();
                    String selection;
                    String[] selectionArs = null;
                    if (booksDbInfo != null) {
                        selection = DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DtInfoColumns.ACCOUNTBOOKID + "=? and "
                                + DtInfoColumns.ISDELETED + "=?";
                        String[] selectionArgs = new String[]{"false", booksDbInfo.getAccountbookid(), "false"};
                        selectionArs = selectionArgs;
                    } else {
                        selection = DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DtInfoColumns.ISDELETED + "=?";
                        String[] selectionArgs = new String[]{"false", "false"};
                        selectionArs = selectionArgs;
                    }

                    List<DailycostEntity> dailycoss = DbInterface.getQueryDailycostEntitys(selection, selectionArs, null, null, null, null);

                    if (dailycoss != null && dailycoss.size() > 0) {

                        for (int i = 0; i < dailycoss.size(); i++) {
                            DailycostEntity dailycostEntity = dailycoss.get(i);
                            String whereString = DtBillMemberColumns.BILLID + "=?";
                            String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                            List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString, selectionAr,
                                    null, null, null, null);
                            dailycostEntity.setMemberlist(billMemberInfos);
                            dailycostEntities.add(dailycostEntity);
                            dailycoss.get(i).setMemberlist(billMemberInfos);
                        }

                    }
                    if (booksDbInfo != null) {
                        String selectiondelet = DtInfoColumns.ISDELETED + "=? and " + DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLSTATUS + "=? and " + DtInfoColumns.ISSYNCHRONIZATION + "=? ";
                        String[] selectionArgsdelet = new String[]{"true", booksDbInfo.getAccountbookid(), "1", "true"};
                        List<DailycostEntity> dailEntities = DbInterface.getQueryDailycostEntitys(selectiondelet,
                                selectionArgsdelet, null, null, null, null);
                        if (dailEntities != null && dailEntities.size() > 0) {

                            for (int i = 0; i < dailEntities.size(); i++) {
                                DailycostEntity dailycostEntity = dailEntities.get(i);
                                String whereString = DtBillMemberColumns.BILLID + "=?";
                                String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                                List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString,
                                        selectionAr, null, null, null, null);
                                dailycostEntity.setMemberlist(billMemberInfos);
                                dailycostEntities.add(dailycostEntity);
                                dailEntities.get(i).setMemberlist(billMemberInfos);
                            }
                        }
                    }
                    if (booksDbInfo != null) {
                        String selectiondelet = DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DtInfoColumns.ACCOUNTBOOKID + "=? and " + DtInfoColumns.BILLSTATUS + "=? ";
                        String[] selectionArgsdelet = new String[]{"edit", booksDbInfo.getAccountbookid(), "1"};
                        List<DailycostEntity> dailEntities = DbInterface.getQueryDailycostEntitys(selectiondelet,
                                selectionArgsdelet, null, null, null, null);
                        if (dailEntities != null && dailEntities.size() > 0) {

                            for (int i = 0; i < dailEntities.size(); i++) {
                                DailycostEntity dailycostEntity = dailEntities.get(i);
                                String whereString = DtBillMemberColumns.BILLID + "=?";
                                String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                                List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString,
                                        selectionAr, null, null, null, null);
                                dailycostEntity.setMemberlist(billMemberInfos);
                                dailycostEntities.add(dailycostEntity);
                                dailEntities.get(i).setMemberlist(billMemberInfos);
                            }
//                            List<PostBill> postBillss = getPostBillData("edit", dailEntities);
//                            for (PostBill postBill : postBillss) {
//                                postBills.add(postBill);
//
//                            }
                        }
                    }


                    if (dailycostEntities.size() == 0) {
                        long defaltTime = RequsterTag.sysTime;
                        long currentTime = new Date().getTime();
                        if (currentTime - defaltTime > 10 * 1000) {
                            RequsterTag.sysTime = new Date().getTime();
                            Message msg = Message.obtain();
                            msg.what = RequsterTag.GETBILLS;
                            handler.sendMessage(msg);
                        }

                        Message msgMessage2 = Message.obtain();
                        msgMessage2.what = RequsterTag.SYNCHRONIZATION_SUS;
                        msgMessage2.obj = "0";
                        handler.sendMessage(msgMessage2);
                        RequsterTag.isSynchronizationing = false;
                    } else {
                        Intent intent = new Intent(context, MyServer.class);
                        Bundle bundle = new Bundle();
//                        bundle.putParcelableArrayList("postBills", (ArrayList<? extends Parcelable>) postBills);
                        bundle.putParcelableArrayList("dailycostEntitys", (ArrayList<? extends Parcelable>) dailycostEntities);
                        intent.putExtras(bundle);
                        context.stopService(intent);
                        context.startService(intent);
                    }
                }


            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            RequsterTag.isSynchronizationing = false;
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
            RequsterTag.isSynchronizationing = false;
        }
    }

    public static void sysQiniu(final Context context, final String uriPath, final int i, final ViewCallBack viewCallBack, final DailycostEntity dailycostEntity) {
        try {
            total += 1;
            // 线程放入线程池
            LIMITED_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    File file = new File(uriPath);
                    QiniuServer.requstQiniu(context, StringUtils.getRealFilePath(context, Uri.parse(uriPath)), "book/" + file.getName(), i, viewCallBack, dailycostEntity);

                }

            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sysQiniu(final Context context, final byte[] bytes, final ViewCallBack viewCallBack) {
        try {
            total += 1;
            // 线程放入线程池
            LIMITED_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    QiniuServer.requstQiniu(context, bytes, "book/" + StringUtils.getUUID(), viewCallBack);
                }

            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sysQiniu(final Context context, final int drawbleid, final ViewCallBack viewCallBack) {
        try {
            total += 1;
            // 线程放入线程池
            LIMITED_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    QiniuServer.requstQiniu(context, drawbleid, "book/" + StringUtils.getUUID(), viewCallBack);

                }

            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, "thread pool rejected error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成一个提交表单对象
     *
     * @param
     * @return
     */
    public static List<PostBill> getPostBillData(List<DailycostEntity> entities) {
        List<PostBill> postBills = new ArrayList<PostBill>();
        if (entities == null) {
            return postBills;
        }
        final String mPkid = "";// 作为主键的Pkid

        PostBill.MemberlistBean mMemberlistBean = null;

        for (DailycostEntity dailycostEntity : entities) {
            String remark = dailycostEntity.getBillmark();
            PostBill mPostBill = new PostBill();

            mPostBill.setTradetime(Integer.parseInt(dailycostEntity.getTradetime()));// 需要修改
            mPostBill.setPkid(dailycostEntity.getPkid());//
            mPostBill.setAccountbookid(dailycostEntity.getAccountbookid());
            mPostBill.setCateid(dailycostEntity.getBillcateid());// 一级分类ID
            mPostBill.setSubcateid(dailycostEntity.getBillsubcateid());
            if (dailycostEntity.getIssynchronization().equals("false")) {
                mPostBill.setAction("add");//是添加
            } else if (dailycostEntity.getIssynchronization().equals("edit") && dailycostEntity.getIsdeleted().equals("false")) {
                mPostBill.setAction("edit");//修改
            } else {
                mPostBill.setAction("del");// 删除
            }
            mPostBill.setBillbrand(dailycostEntity.getBillbrand());
            mPostBill.setBillctime(Integer.parseInt(dailycostEntity.getBillctime()));// 时间选择器时间
            mPostBill.setBillclear(Integer.parseInt(dailycostEntity.getBillclear()));// 0创建时是未结算
            mPostBill.setBillamount(dailycostEntity.getBillamount());
            mPostBill.setBilltype(Integer.parseInt(dailycostEntity.getBilltype()));// 账单类型，收入，支出，转账
            // 需要修改
            mPostBill.setRemark(dailycostEntity.getBillmark());
            mPostBill.setAccountnumber(dailycostEntity.getAccountnumber());
            mPostBill.setBillid(dailycostEntity.getBillid());
            mPostBill.setBillimg(dailycostEntity.getBillimg());
            mPostBill.setAddress(dailycostEntity.getAddress());

            mPostBill.setAccountbooktype(Integer.parseInt(dailycostEntity.getAccountbooktype()));
            postBills.add(mPostBill);
            List<PostBill.MemberlistBean> memberlist = new ArrayList<>();
            if (dailycostEntity.getMemberlist() != null) {
                for (BillMemberInfo billMemberInfo : dailycostEntity.getMemberlist()) {

                    String memberid = billMemberInfo.getMemberid();
                    mMemberlistBean = new PostBill.MemberlistBean();
                    mMemberlistBean.setAmount(billMemberInfo.getAmount());
                    mMemberlistBean.setCtime(dailycostEntity.getUpdatetime());
                    mMemberlistBean.setMemberid(memberid);
                    mMemberlistBean.setStatus(Integer.parseInt(billMemberInfo.getStatus()));// 需要修改
                    mMemberlistBean.setType(Integer.parseInt(billMemberInfo.getType()));// 0.收入;1.支出;2.转账
                    memberlist.add(mMemberlistBean);
                }
            }

            mPostBill.setMemberlist(memberlist);
        }

        return postBills;
    }

    /**
     * 生成一个提交表单对象
     *
     * @param
     * @return
     */
    public static PostBill getPostBillData(DailycostEntity entitie) {

        PostBill mPostBill = new PostBill();
        if (entitie == null) {
            return mPostBill;
        }
        final String mPkid = "";// 作为主键的Pkid

        PostBill.MemberlistBean mMemberlistBean = null;

        String remark = entitie.getBillmark();


        mPostBill.setTradetime(Integer.parseInt(entitie.getTradetime()));// 需要修改
        mPostBill.setPkid(entitie.getPkid());//
        mPostBill.setAccountbookid(entitie.getAccountbookid());
        mPostBill.setCateid(entitie.getBillcateid());// 一级分类ID
        mPostBill.setSubcateid("0");// 一级分类ID
        if (entitie.getIssynchronization().equals("false")) {
            mPostBill.setAction("add");//是添加
        } else if (entitie.getIssynchronization().equals("edit") && entitie.getIsdeleted().equals("false")) {
            mPostBill.setAction("edit");//修改
        } else {
            mPostBill.setAction("del");// 删除
        }

        mPostBill.setBillctime(Integer.parseInt(entitie.getBillctime()));// 时间选择器时间
        mPostBill.setBillclear(Integer.parseInt(entitie.getBillclear()));// 0创建时是未结算
        mPostBill.setBillamount(entitie.getBillamount());
        mPostBill.setBilltype(Integer.parseInt(entitie.getBilltype()));// 账单类型，收入，支出，转账
        // 需要修改
        mPostBill.setRemark(entitie.getBillmark());
        mPostBill.setAccountnumber(entitie.getAccountnumber());
        if (!TextUtils.isEmpty(entitie.getBillid())) {
            mPostBill.setBillid(entitie.getBillid());
        } else {
            mPostBill.setBillid("");
        }

        mPostBill.setBillimg(entitie.getBillimg());
        mPostBill.setAddress(entitie.getAddress());

        mPostBill.setAccountbooktype(Integer.parseInt(entitie.getAccountbooktype()));
        List<PostBill.MemberlistBean> memberlist = new ArrayList<>();
        if (entitie.getMemberlist() != null) {
            for (BillMemberInfo billMemberInfo : entitie.getMemberlist()) {

                String memberid = billMemberInfo.getMemberid();
                mMemberlistBean = new PostBill.MemberlistBean();
                mMemberlistBean.setAmount(billMemberInfo.getAmount());
                mMemberlistBean.setCtime(entitie.getUpdatetime());
                mMemberlistBean.setMemberid(memberid);
                mMemberlistBean.setStatus(Integer.parseInt(billMemberInfo.getStatus()));// 需要修改
                mMemberlistBean.setType(Integer.parseInt(billMemberInfo.getType()));// 0.收入;1.支出;2.转账
                memberlist.add(mMemberlistBean);
            }
        }

        mPostBill.setMemberlist(memberlist);

        return mPostBill;
    }
}
