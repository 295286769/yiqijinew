package com.yiqiji.money.modules.common.request;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.entity.QiniuInfo;
import com.yiqiji.money.modules.homeModule.home.entity.QiniuInfoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by ${huangweishui} on 2017/3/17.
 * address huang.weishui@71dai.com
 */
public class QiniuServer {
    private static UploadManager uploadManager;
    private static long time = new Date().getTime();

    public static UploadManager getUploadManager() {
        if (uploadManager == null) {
            configQiniu();
        }
        return uploadManager;
    }

    public static void configQiniu() {
        Configuration config = new Configuration.Builder().chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

    }


    public static void requstQiniu(Context context, final String fileString, final String fileName, final int position, final ViewCallBack viewCallBack, final DailycostEntity dailycostEntity) {

        String qiniuToken = LoginConfig.getInstance().getQiniuToken();
        String qiniuKey = LoginConfig.getInstance().getQiniuKey();
        String qiniuHost = LoginConfig.getInstance().getQiniuHost();
        if (TextUtils.isEmpty(qiniuToken)) {
            getQiniuToken(context, fileString, fileName, position, viewCallBack, dailycostEntity);
        } else {
            long newTime = new Date().getTime();
            long oneDayTime = 24 * 60 * 60 * 1000;
            if (newTime - time > oneDayTime) {
                time = newTime;
                getQiniuToken(context, fileString, fileName, position, viewCallBack, dailycostEntity);
            } else {
                getQiniuResult(context, fileString, fileName, qiniuToken, position, viewCallBack, dailycostEntity);
            }
        }
    }

    private static void getQiniuToken(final Context context, final String fileString, final String fileName, final int position, final ViewCallBack viewCallBack, final DailycostEntity dailycostEntity) {
        CommonFacade.getInstance().exec(Constants.QINIU, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                QiniuInfo qiniuInfo = GsonUtil.GsonToBean(o.toString(), QiniuInfo.class);
                QiniuInfoItem qiniuInfoItem = qiniuInfo.getData();
                String tokenQiniu = qiniuInfoItem.getToken();
                String host = qiniuInfoItem.getHost();
                String key = qiniuInfoItem.getBucket();
                LoginConfig.getInstance().setQiniuToken(tokenQiniu);
                LoginConfig.getInstance().setQiniuHost(host);
                LoginConfig.getInstance().setQiniuKey(key);

                getQiniuResult(context, fileString, fileName, tokenQiniu, position, viewCallBack, dailycostEntity);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }


    private static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    private static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isUpLoading = false;//是否需要取消上传

    private static void getQiniuResult(final Context context, final String fileString, final String fileName, String tokenQiniu, final int position, final ViewCallBack viewCallBack, final DailycostEntity dailycostEntity1) {

        Bitmap bitmap = BitmapFactory.decodeFile(fileString);
        byte[] bytes = Bitmap2Bytes(bitmap);
        uploadManager.put(bytes, fileName, tokenQiniu, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (responseInfo.isOK()) {
                            Object[] positions = new Object[]{position, fileName};
                            try {
                                String url = LoginConfig.getInstance().getQiniuHost() + fileName;
                                DailycostEntity dailycostEntity = dailycostEntity1;
                                dailycostEntity.setBillimg(url);
                                String where = DailycostEntity.BILLID + "=? ";
                                String whereStrings[] = {dailycostEntity.getBillid()};
                                DownUrlUtil.updateDataBaseDailycostInfo(dailycostEntity, where, whereStrings, null, 0);
                                viewCallBack.onSuccess(dailycostEntity);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            Message message = Message.obtain();
//                            message.what = what;
//                            message.arg1 = position;
//                            message.obj = fileName;
//                            handler.sendMessage(message);
                        } else {
                            try {
                                if (jsonObject == null) {
                                    SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                    viewCallBack.onFailed(simpleMsg);
//                                    RequsterTag.isSynchronizationing = false;
//                                    EventBus.getDefault().post("fail");
                                    return;
                                }
                                if (jsonObject.has("error")) {

                                    String error = jsonObject.getString("error");
                                    if (error.equals("expired token")) {
                                        getQiniuToken(context, fileString, fileName, position, viewCallBack, dailycostEntity1);
                                    } else {
                                        SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                        viewCallBack.onFailed(simpleMsg);
//                                        RequsterTag.isSynchronizationing = false;
//                                        EventBus.getDefault().post("fail");
                                    }
                                }
                            } catch (JSONException e) {
                                LogUtil.log_error(XzbUtils.getTraceInfo(), e);
                                SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                viewCallBack.onFailed(simpleMsg);
//                                RequsterTag.isSynchronizationing = false;
                                e.printStackTrace();
                            }
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }


                    }

                }

                , new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
//                                Log.i("qiniu", key + ": " + percent);
                            }
                        }, null)

        );
    }


    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 80, baos);
            bm.recycle();//自由选择是否进行回收
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static void requstQiniu(Context context, final byte[] bytes, final String fileName, final ViewCallBack viewCallBack) {

        String qiniuToken = LoginConfig.getInstance().getQiniuToken();
        String qiniuKey = LoginConfig.getInstance().getQiniuKey();
        String qiniuHost = LoginConfig.getInstance().getQiniuHost();
        if (TextUtils.isEmpty(qiniuToken)) {
            getQiniuToken(context, bytes, fileName, viewCallBack);
        } else {
            long newTime = new Date().getTime();
            long oneDayTime = 24 * 60 * 60 * 1000;
            if (newTime - time > oneDayTime) {
                time = newTime;
                getQiniuToken(context, bytes, fileName, viewCallBack);
            } else {
                getQiniuResult(context, bytes, fileName, qiniuToken, viewCallBack);
            }
        }
    }

    private static void getQiniuToken(final Context context, final byte[] bytes, final String fileName, final ViewCallBack viewCallBack) {
        CommonFacade.getInstance().exec(Constants.QINIU, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                QiniuInfo qiniuInfo = GsonUtil.GsonToBean(o.toString(), QiniuInfo.class);
                QiniuInfoItem qiniuInfoItem = qiniuInfo.getData();
                String tokenQiniu = qiniuInfoItem.getToken();
                String host = qiniuInfoItem.getHost();
                String key = qiniuInfoItem.getBucket();
                LoginConfig.getInstance().setQiniuToken(tokenQiniu);
                LoginConfig.getInstance().setQiniuHost(host);
                LoginConfig.getInstance().setQiniuKey(key);

                getQiniuResult(context, bytes, fileName, tokenQiniu, viewCallBack);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }

    private static void getQiniuResult(final Context context, final byte[] bytes, final String fileName, String tokenQiniu, final ViewCallBack viewCallBack) {

//        Bitmap bitmap = BitmapFactory.decodeFile(fileString);
//        byte[] bytes = Bitmap2Bytes(bitmap);
        uploadManager.put(bytes, fileName, tokenQiniu, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (responseInfo.isOK()) {
                            Object[] positions = new Object[]{fileName};
                            try {
                                String url = LoginConfig.getInstance().getQiniuHost() + fileName;
                                viewCallBack.onSuccess(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                if (jsonObject == null) {
                                    SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                    viewCallBack.onFailed(simpleMsg);
                                    return;
                                }
                                if (jsonObject.has("error")) {

                                    String error = jsonObject.getString("error");
                                    if (error.equals("expired token")) {
                                        getQiniuToken(context, bytes, fileName, viewCallBack);
                                    } else {
                                        SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                        viewCallBack.onFailed(simpleMsg);
                                    }
                                }
                            } catch (JSONException e) {
                                LogUtil.log_error(XzbUtils.getTraceInfo(), e);
                                SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                viewCallBack.onFailed(simpleMsg);
                                e.printStackTrace();
                            }
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }


                    }

                }

                , new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
//                                Log.i("qiniu", key + ": " + percent);
                            }
                        }, null)

        );
    }

    public static void requstQiniu(Context context, final int drawbleid, final String fileName, final ViewCallBack viewCallBack) {

        String qiniuToken = LoginConfig.getInstance().getQiniuToken();
        String qiniuKey = LoginConfig.getInstance().getQiniuKey();
        String qiniuHost = LoginConfig.getInstance().getQiniuHost();
        if (TextUtils.isEmpty(qiniuToken)) {
            getQiniuToken(context, drawbleid, fileName, viewCallBack);
        } else {
            long newTime = new Date().getTime();
            long oneDayTime = 24 * 60 * 60 * 1000;
            if (newTime - time > oneDayTime) {
                time = newTime;
                getQiniuToken(context, drawbleid, fileName, viewCallBack);
            } else {
                getQiniuResult(context, drawbleid, fileName, qiniuToken, viewCallBack);
            }
        }
    }

    private static void getQiniuToken(final Context context, final int drableid, final String fileName, final ViewCallBack viewCallBack) {
        CommonFacade.getInstance().exec(Constants.QINIU, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                QiniuInfo qiniuInfo = GsonUtil.GsonToBean(o.toString(), QiniuInfo.class);
                QiniuInfoItem qiniuInfoItem = qiniuInfo.getData();
                String tokenQiniu = qiniuInfoItem.getToken();
                String host = qiniuInfoItem.getHost();
                String key = qiniuInfoItem.getBucket();
                LoginConfig.getInstance().setQiniuToken(tokenQiniu);
                LoginConfig.getInstance().setQiniuHost(host);
                LoginConfig.getInstance().setQiniuKey(key);

                getQiniuResult(context, drableid, fileName, tokenQiniu, viewCallBack);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }

    private static void getQiniuResult(final Context context, final int drableid, final String fileName, String tokenQiniu, final ViewCallBack viewCallBack) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drableid);
        bitmap = BitmapUtil.compressImage(bitmap);
        byte[] bytes = Bitmap2Bytes(bitmap);
        uploadManager.put(bytes, fileName, tokenQiniu, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (responseInfo.isOK()) {
                            Object[] positions = new Object[]{fileName};
                            try {
                                String url = LoginConfig.getInstance().getQiniuHost() + fileName;
                                viewCallBack.onSuccess(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                if (jsonObject == null) {
                                    SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                    viewCallBack.onFailed(simpleMsg);
                                    return;
                                }
                                if (jsonObject.has("error")) {

                                    String error = jsonObject.getString("error");
                                    if (error.equals("expired token")) {
                                        getQiniuToken(context, drableid, fileName, viewCallBack);
                                    } else {
                                        SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                        viewCallBack.onFailed(simpleMsg);
                                    }
                                }
                            } catch (JSONException e) {
                                LogUtil.log_error(XzbUtils.getTraceInfo(), e);
                                SimpleMsg simpleMsg = new SimpleMsg(-1, "图片上传失败");
                                viewCallBack.onFailed(simpleMsg);
                                e.printStackTrace();
                            }
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }


                    }

                }

                , new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
//                                Log.i("qiniu", key + ": " + percent);
                            }
                        }, null)

        );
    }
}
