package com.yiqiji.money.modules.myModule.login.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.umeng.socialize.UMShareAPI;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.ActionSheetDialog;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.LocalImageHelper;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.MaxLengthWatcher;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.perecenter.BookListPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.activity.ChooseBookActivity;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 2017/3/23.
 */
public class LoginOnSettingName extends BaseActivity implements View.OnClickListener {

    private CircleImageView iv_setting_usericon;
    private EditText ed_setting_user_name;
    private Button bt_login_start;
    private ApiService apiService;

    private Uri uri = null;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_setting_name);
        initView();
        initTitle("设置用户名");
        apiService = RetrofitInstance.get().create(ApiService.class);
        String tokenid = LoginConfig.getInstance().getTokenId();
        getUserinfo(tokenid);
    }

    private void initView() {
        iv_setting_usericon = (CircleImageView) findViewById(R.id.iv_setting_usericon);
        ed_setting_user_name = (EditText) findViewById(R.id.ed_setting_user_name);
        ed_setting_user_name.addTextChangedListener(new MaxLengthWatcher(10, ed_setting_user_name, "用户名不能超过10个字符"));
        bt_login_start = (Button) findViewById(R.id.bt_login_start);
        iv_setting_usericon.setOnClickListener(this);
        bt_login_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.iv_setting_usericon:
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MyApplicaction.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//需要弹出dialog让用户手动赋予权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }

                new ActionSheetDialog(LoginOnSettingName.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .setCancelTextColor(getResources().getColor(R.color.main_back))
                        .addSheetItem(getString(R.string.home_take_photo), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        choiceFromCamera();

                                    }
                                })
                        .addSheetItem(getString(R.string.home_from_ablum), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(LoginOnSettingName.this, PictureListActivity.class);
                                        intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                        startActivity(intent);
                                    }
                                }).show();
                break;

            case R.id.bt_login_start:

                String userNames = ed_setting_user_name.getText().toString().trim();
                if (!TextUtils.isEmpty(userName) && !userName.equals(userNames)) {

                    if (TextUtils.isEmpty(userNames)) {
                        showToast("请输入昵称");
                        return;
                    }
                    //调用保存设置用户名接口
                    setUsername(userNames);
                } else {
                    BookListPerecenter.initBooks(new ViewCallBack<BooksDbInfo>() {//是否有账本的跳转
                        @Override
                        public void onSuccess(BooksDbInfo booksDbInfo) throws Exception {
                            super.onSuccess(booksDbInfo);
                            if (booksDbInfo!=null) {//已经有账本
                                IntentUtils.startActivityClearTop(LoginOnSettingName.this, HomeActivity.class.getName(), IntentUtils.LoginIntentType.MINE);
                            } else {//没有账本
                                Intent intents = new Intent(LoginOnSettingName.this, ChooseBookActivity.class);
                                startActivity(intents);
                            }

                        }
                    });


                }


                break;

        }

    }

    private void choiceFromCamera() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "没有内存卡", Toast.LENGTH_SHORT).show();
            ToastUtils.DiyToast(this, "没有内存卡");
            return;
        }
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        uri = XzbUtils.getTpPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        //为拍摄的图片指定一个存储的路径
        if (currentapiVersion < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, StringUtils.getRealFilePath(this, uri));
            Uri uri_path = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_path);
        }

        try {
            startActivityForResult(intent, RequsterTag.RQ_TAKE_A_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log_error(null, e);
        }

    }


    private String userName;

    /**
     * * 获取个人信息
     *
     * @param tokenid
     */
    private void getUserinfo(String tokenid) {

        CommonFacade.getInstance().exec(Constants.GET_USER_INFO, new ViewCallBack<UserInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(UserInfo userInfo) throws Exception {
                super.onSuccess(userInfo);
                userName = userInfo.getUsername();
                ed_setting_user_name.setText(userInfo.getUsername());
                ed_setting_user_name.setSelection(userInfo.getUsername().length());// 将光标移至文字末尾
                XzbUtils.displayImageHead(iv_setting_usericon, userInfo.getUsericon(), 0);
                LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
                LoginConfig.getInstance().setUserName(userInfo.getUsername());
                LoginConfig.getInstance().setMobile(userInfo.getMobile());
                LoginConfig.getInstance().setUserid(userInfo.getUid());
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
//        String params[] = {"tokenid", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//
//
//        apiService.getUserInfo(hashMap).enqueue(new BaseCallBack<UserInfo>(this, false) {
//            @Override
//            public void onResponse(Response<UserInfo> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    UserInfo userInfo = arg0.body();
//                    if (userInfo.getCode() == 0) {
//                        userName = userInfo.getUsername();
//                        ed_setting_user_name.setText(userInfo.getUsername());
//                        ed_setting_user_name.setSelection(userInfo.getUsername().length());// 将光标移至文字末尾
//                        XzbUtils.displayImageHead(iv_setting_usericon, userInfo.getUsericon(), 0);
//                        LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
//                        LoginConfig.getInstance().setUserName(userInfo.getUsername());
//                        LoginConfig.getInstance().setMobile(userInfo.getMobile());
//                        LoginConfig.getInstance().setUserid(userInfo.getUid());
//
//                    } else if (userInfo.getCode() == 20012) {
//                        //已经掉线
//                        LoginConfig.getInstance().setTokenId("");
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                    LogUtil.log_error(null,e);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//                super.onFailure(arg0);
//            }
//        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case RequsterTag.RQ_TAKE_A_PHOTO:// 拍照
                XzbUtils.doCropPhoto(this, uri, uri, RequsterTag.RQ_CROPE);
                break;

            case RequsterTag.RQ_CROPE:// 裁剪

                // 有网调接口
                bmp = BitmapUtil.decodeUriAsBitmap(this, uri, true);
                setUsericon(bmp);
                if (bmp != null) {
                    iv_setting_usericon.setImageBitmap(bmp);

//                    try {
//                        getImageToView();
//                    } catch (URISyntaxException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }

                }
                break;

            default:
                break;
        }

    }

    private void setUsericon(Bitmap bitmap) {
        File file = XzbUtils.saveImage(bitmap);
        if (file == null) {
            return;
        }
        RequestBody useridBody = RequestBody.create(MediaType.parse("text/plain"), LoginConfig.getInstance()
                .getTokenId());
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("tokenid", useridBody);
        map.put("usericon\"; filename=\"" + file.getName(), fileBody);

        apiService.setUserIcon(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
                // TODO Auto-generated method stub
                try {
                    if (arg0.body() == null) {
                        return;
                    }
                    JSONObject object = new JSONObject(arg0.body().string());
                    if (object.getInt("code") == 0) {
                        LoginConfig.getInstance().setUsericon(object.getString("usericon"));
                    } else {
                        showToast("加载出错了．．．");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    LogUtil.log_error(null, e);
                }
            }

            @Override
            public void onFailure(Throwable arg0) {
                showToast("加载出错了．．．");
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        cutData();

    }

    /**
     * 裁剪接收
     */
    private void cutData() {
        if (MyApplicaction.getInstence().isThubm) {
            String path = getIntent().getStringExtra("path");
            uri = Uri.parse(path);
            // 有网调接口
            bmp = BitmapUtil.decodeSampledBitmapFromResource(path, UIHelper.Dp2Px(this, 40), UIHelper.Dp2Px(this, 40));
            if (bmp != null) {
                setUsericon(bmp);
                iv_setting_usericon.setImageBitmap(bmp);
                try {
                    getImageToView();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LogUtil.log_error(null, e);
                }
            }
            MyApplicaction.getInstence().isThubm = false;
        }

    }


    private void getImageToView() throws URISyntaxException {
        if (uri == null) {
            return;
        }

        File file = null;
        file = new File(uri.toString());

        LocalImageHelper helper = LocalImageHelper.getInstance();
        helper.deleteFile(file);

    }

    //  ChooseBookActivity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void setUsername(final String username) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);

        CommonFacade.getInstance().exec(Constants.USERPASS_SET_USERNAME, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                LoginConfig.getInstance().setUserName(username);
                Intent intent = new Intent(LoginOnSettingName.this, ChooseBookActivity.class);
                startActivity(intent);
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
//        String params[] = {"username", "tokenid", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{username, hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("username", username);
//        apiService.setUserName(hashMap).enqueue(new BaseCallBack<ResponseBody>(this, true) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                super.onResponse(arg0, arg1);
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        LoginConfig.getInstance().setUserName(username);
//                        Intent intent = new Intent(LoginOnSettingName.this, ChooseBookActivity.class);
//                        startActivity(intent);
//                    } else if (object.getInt("code") == 20010) {
//
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

    //权限管理，用于拍照，现在targetSdkVersion21,还不涉及到权限申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ActionSheetDialog(LoginOnSettingName.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .setCancelTextColor(getResources().getColor(R.color.main_back))
                        .addSheetItem(getString(R.string.home_take_photo), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        choiceFromCamera();

                                    }
                                })
                        .addSheetItem(getString(R.string.home_from_ablum), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(LoginOnSettingName.this, PictureListActivity.class);
                                        intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                        startActivity(intent);
                                    }
                                }).show();
            } else {
                //用户不同意，自行处理即可
//                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showSimpleAlertDialog(null, "请在手机的“设置→一起记→存储空间”选项中，允许访问您的存储空间", "现在设置", "我知道了", false, false, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismissDialog();
                            }
                        });
                    }
                }
            }
        }
    }

}
