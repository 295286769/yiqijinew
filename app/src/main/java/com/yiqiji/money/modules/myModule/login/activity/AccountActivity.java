package com.yiqiji.money.modules.myModule.login.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.ActionSheetDialog;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.LocalImageHelper;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.entity.eventbean.UserInfoChangeEvent;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 个人信息
 */
public class AccountActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout rl_account_login_password;
    private ApiService apiService;
    private TextView nike_name;
    private CircleImageView image_head;
    private Uri uri = null;
    private Uri selectedImageUri;
    private Bitmap bmp;
    private Bitmap bitmap_url = null;
    private RelativeLayout rl_setting_user_name;

    private RelativeLayout rl_account_login_phone_number;
    private RelativeLayout rl_account_login_weichar;
    private RelativeLayout rl_account_login_qq;
    private RelativeLayout rl_account_login_weibo;

    private TextView tv_account_phone_number;
    private TextView tv_account_weichar;
    private TextView tv_account_qq;
    private TextView tv_account_weibo;

    private TextView bt_account_weichar;
    private TextView bt_account_qq;
    private TextView bt_account_weibo;

    private UserInfo userInfo;

    private RelativeLayout rl_image_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        MyApplicaction.getInstence().addActivity(this);
        initTitle("个人信息");
        apiService = RetrofitInstance.get().create(ApiService.class);
        userInfo = (UserInfo) getIntent().getParcelableExtra("userInfo");
        initView();
        initData();
    }


    private void initView() {

        tv_account_phone_number = (TextView) findViewById(R.id.tv_account_phone_number);
        tv_account_weichar = (TextView) findViewById(R.id.tv_account_weichar);
        tv_account_qq = (TextView) findViewById(R.id.tv_account_qq);
        tv_account_weibo = (TextView) findViewById(R.id.tv_account_weibo);

        bt_account_weichar = (TextView) findViewById(R.id.bt_account_weichar);
        bt_account_qq = (TextView) findViewById(R.id.bt_account_qq);
        bt_account_weibo = (TextView) findViewById(R.id.bt_account_weibo);


        rl_account_login_phone_number = (RelativeLayout) findViewById(R.id.rl_account_login_phone_number);
        rl_account_login_weichar = (RelativeLayout) findViewById(R.id.rl_account_login_weichar);
        rl_account_login_qq = (RelativeLayout) findViewById(R.id.rl_account_login_qq);
        rl_account_login_weibo = (RelativeLayout) findViewById(R.id.rl_account_login_weibo);

        rl_account_login_phone_number.setOnClickListener(this);
        rl_account_login_weichar.setOnClickListener(this);
        rl_account_login_qq.setOnClickListener(this);
        rl_account_login_weibo.setOnClickListener(this);
        nike_name = (TextView) findViewById(R.id.nike_name);

        rl_account_login_password = (RelativeLayout) findViewById(R.id.rl_account_login_password);
        rl_account_login_password.setOnClickListener(this);
        image_head = (CircleImageView) findViewById(R.id.image_head);
        rl_image_head = (RelativeLayout) findViewById(R.id.rl_image_head);
        rl_image_head.setOnClickListener(this);
        rl_setting_user_name = (RelativeLayout) findViewById(R.id.rl_setting_user_name);
        rl_setting_user_name.setOnClickListener(this);


    }

    private void initData() {
        uri = XzbUtils.getPath();

        bitmap_url = LoginConfig.getInstance().getImage_head();
        if (bitmap_url != null) {
            image_head.setImageBitmap(bitmap_url);
        } else {
            XzbUtils.displayImageHead(image_head, LoginConfig.getInstance().getUsericon(), 0);
        }

        nike_name.setText(LoginConfig.getInstance().getUserName());

        tv_account_phone_number.setText(StringUtils.getStarsMobile(userInfo.getMobile()));


        if (TextUtils.isEmpty(userInfo.getWxid())) {
            bt_account_weichar.setVisibility(View.VISIBLE);
            tv_account_weichar.setVisibility(View.GONE);
        } else {
            bt_account_weichar.setVisibility(View.GONE);
            tv_account_weichar.setVisibility(View.VISIBLE);
            tv_account_weichar.setText(userInfo.getWxname());
        }
        if (TextUtils.isEmpty(userInfo.getQqid())) {
            bt_account_qq.setVisibility(View.VISIBLE);
            tv_account_qq.setVisibility(View.GONE);
        } else {
            bt_account_qq.setVisibility(View.GONE);
            tv_account_qq.setVisibility(View.VISIBLE);
            tv_account_qq.setText(userInfo.getQqname());
        }

        if (TextUtils.isEmpty(userInfo.getWbid())) {
            bt_account_weibo.setVisibility(View.VISIBLE);
            tv_account_weibo.setVisibility(View.GONE);
        } else {
            bt_account_weibo.setVisibility(View.GONE);
            tv_account_weibo.setVisibility(View.VISIBLE);
            tv_account_weibo.setText(userInfo.getWbname());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (!MyApplicaction.getInstence().isThubm) {
            String tokenId = LoginConfig.getInstance().getTokenId();
            getUserinfo(tokenId);
        }
        cutData();
    }

    /**
     * 裁剪接收
     */
    private void cutData() {

        if (MyApplicaction.getInstence().isThubm) {
            String path = getIntent().getStringExtra("path");
            uri = Uri.parse(path);

            bmp = BitmapUtil.decodeSampledBitmapFromResource(path, UIHelper.Dp2Px(AccountActivity.this, 40), UIHelper.Dp2Px(AccountActivity.this, 40));

            if (bmp != null) {
                setUsericon(bmp);
                image_head.setImageBitmap(bmp);

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


    private Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();

        float scrreWith = XzbUtils.getPhoneScreen(this).widthPixels;
        if (bitmap.getWidth() < scrreWith / 2) {
            float with = scrreWith / bitmap.getWidth();
            matrix.postScale(with, with); // 长和宽放大缩小的比例
        } else {
            matrix.postScale(1.0f, 1.0f); // 长和宽放大缩小的比例
        }

        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                new ActionSheetDialog(AccountActivity.this)
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
                                        Intent intent = new Intent(AccountActivity.this, PictureListActivity.class);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        LoginConfig lc = LoginConfig.getInstance();
        Intent intent = null;
        Bundle bundle = new Bundle();
        bundle.putParcelable("userInfo", userInfo);
        switch (v.getId()) {
            case R.id.rl_setting_user_name: // 设置昵称
                intent = new Intent(AccountActivity.this, SettingUserNameActivity.class);
                String username = nike_name.getText().toString();
                intent.putExtra("username", username);
                startActivityForResult(intent, RequsterTag.CHANGENAME);

                break;
            case R.id.rl_account_login_password:
                intent = new Intent(AccountActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_image_head:
                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MyApplicaction.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//需要弹出dialog让用户手动赋予权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
                new ActionSheetDialog(AccountActivity.this)
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
                                        Intent intent = new Intent(AccountActivity.this, PictureListActivity.class);
                                        intent.putExtra("intentClass", getPackageName() + "." + getLocalClassName());
                                        startActivity(intent);
                                    }
                                }).show();
                break;
            case R.id.rl_account_login_phone_number:
                intent = new Intent(AccountActivity.this, ChangeActivity.class);
                intent.putExtra("ChangeType", "phone");
                startActivity(intent);
                break;
            case R.id.rl_account_login_weichar:
                if (TextUtils.isEmpty(userInfo.getWxid())) {
                    //跳转到第三方登录获取用户信息
                    UMShareAPI.get(this).getPlatformInfo(AccountActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                } else {
                    intent = new Intent(AccountActivity.this, ChangeActivity.class);
                    intent.putExtra("ChangeType", "wx");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
            case R.id.rl_account_login_qq:
                if (TextUtils.isEmpty(userInfo.getQqid())) {
                    //跳转到第三方登录获取用户信息

                    UMShareAPI.get(this).getPlatformInfo(AccountActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                } else {
                    intent = new Intent(AccountActivity.this, ChangeActivity.class);
                    intent.putExtra("ChangeType", "qq");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


                break;
            case R.id.rl_account_login_weibo:
                if (TextUtils.isEmpty(userInfo.getWbid())) {
                    //跳转到第三方登录获取用户信息
                    UMShareAPI.get(this).getPlatformInfo(AccountActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                } else {
                    intent = new Intent(AccountActivity.this, ChangeActivity.class);
                    intent.putExtra("ChangeType", "wb");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


                break;
            default:
                break;
        }
    }

    //创建一个以时间结尾的png图片路径
    private void initPath() {
        uri = XzbUtils.getTpPath();
    }

    private void choiceFromCamera() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "没有内存卡", Toast.LENGTH_SHORT).show();
            ToastUtils.DiyToast(this, "没有内存卡");
            return;
        }
        initPath();
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //为拍摄的图片指定一个存储的路径
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        if (currentapiVersion < 24) {
            //为拍摄的图片指定一个存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, StringUtils.getRealFilePath(this, uri));
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        try {
            startActivityForResult(intent, RequsterTag.RQ_TAKE_A_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log_error(null, e);
        }
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
            case RequsterTag.RQ_PICK_A_PICTURE:// 相册
                if (RESULT_OK == resultCode) {
                    if (data == null) {
                        return;
                    }
                    selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        XzbUtils.doCropPhoto(this, selectedImageUri, uri, RequsterTag.RQ_CROPE);

                    } else {
                        ToastUtils.DiyToast(this, "没有获得图片，请检查SD卡是否正常！");
                    }

                }
                break;
            case RequsterTag.RQ_CROPE:// 裁剪

                // 有网调接口
                bmp = BitmapUtil.decodeUriAsBitmap(this, uri, true);
                setUsericon(bmp);
                if (bmp != null) {
                    image_head.setImageBitmap(bmp);

                    try {
                        getImageToView();
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        LogUtil.log_error(null, e);
                    }

                }
                break;
            case RequsterTag.CHANGENAME://修改昵称
                nike_name.setText(LoginConfig.getInstance().getUserName());
                break;

            default:
                break;
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

    private void setUsericon(Bitmap bitmap) {
        File file = XzbUtils.saveImage(bitmap);

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
                    JSONObject object = new JSONObject(arg0.body().string());
                    if (object.getInt("code") == 0) {
                        LoginConfig.getInstance().setIssetavatar(1);
                        LoginConfig.getInstance().setUsericon(object.getString("usericon"));
                        EventBus.getDefault().post(new UserInfoChangeEvent());
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


    //请求个人信息回调 记得添加下面的

    //  UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            String openid = null;
            String profile_image_url = null;
            String screen_name = null;
            String type = null;
            if (platform.equals(SHARE_MEDIA.QQ)) {
                openid = data.get("unionid");
                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                type = "qq";

            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                openid = data.get("id");
                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                type = "wb";

            } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                openid = data.get("unionid");
                profile_image_url = data.get("profile_image_url");
                screen_name = data.get("screen_name");
                type = "wx";
            }

            onUserpassBound(type, openid, screen_name, profile_image_url);


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };


    /**
     * 直接在
     */
    private void onUserpassBound(String type, String openid, String nickname, String avatar) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("type", type);
        hashMap.put("openid", openid);
        hashMap.put("nickname", nickname);
        hashMap.put("avatar", avatar);
        CommonFacade.getInstance().exec(Constants.USERPASS_BOUND, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                //{"uid":153,"tokenid":"zu_58c0bdc50d291_v1.0","code":0}
                String tokenId = LoginConfig.getInstance().getTokenId();
                showToast("绑定成功");
                //查询用户信息
                getUserinfo(tokenId);

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
//        String[] params = new String[]{"type", "openid", "nickname", "avatar", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{type, openid, nickname, avatar, hashMap.get("plat"), hashMap.get("deviceid"), hashMap.get("appver"),
//                hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//
//        hashMap.put("type", type);
//        hashMap.put("openid", openid);
//        hashMap.put("nickname", nickname);
//        hashMap.put("avatar", avatar);
//        apiService.onUserpassBound(hashMap).enqueue(new BaseCallBack<ResponseBody>(AccountActivity.this, false) {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                super.onResponse(arg0, arg1);
//
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    //{"uid":153,"tokenid":"zu_58c0bdc50d291_v1.0","code":0}
//                    if (object.getInt("code") == 0) {
//                        //查询用户信息
//                        String tokenId = LoginConfig.getInstance().getTokenId();
//                        showToast("绑定成功");
//                        getUserinfo(tokenId);
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


    /**
     * * 获取个人信息
     *
     * @param tokenid
     */
    private void getUserinfo(String tokenid) {

        CommonFacade.getInstance().exec(Constants.GET_USER_INFO, new ViewCallBack<UserInfo>() {

            @Override
            public void onSuccess(UserInfo o) throws Exception {
                super.onSuccess(o);
                userInfo = o;
                LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
                LoginConfig.getInstance().setUserName(userInfo.getUsername());
                LoginConfig.getInstance().setMobile(userInfo.getMobile());
                LoginConfig.getInstance().setUserid(userInfo.getUid());
                initData();

                LoginConfig.getInstance().setIssetnick(1);
                EventBus.getDefault().post(new UserInfoChangeEvent());

            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

        });

//
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
//        apiService.getUserInfo(hashMap).enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Response<UserInfo> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                try {
//                    // {"uid":"13","username":"vhhh","code":0,"usericon":"http:\/\/static.test.uziniu.com\/avatar\/default@2x.png","mobile":"18201883983"}
//                    userInfo = arg0.body();
//                    if (userInfo.getCode() == 0) {
//                        LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
//                        LoginConfig.getInstance().setUserName(userInfo.getUsername());
//                        LoginConfig.getInstance().setMobile(userInfo.getMobile());
//                        LoginConfig.getInstance().setUserid(userInfo.getUid());
//                        initData();
//
//                    } else if (userInfo.getCode() == 20012) {
//                        LoginConfig.getInstance().setTokenId("");
//                    } else {
//                        (AccountActivity.this).showToast(userInfo.getMsg());
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
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }


    @Override
    public void onNetChange(int netMobile) {

    }
}
