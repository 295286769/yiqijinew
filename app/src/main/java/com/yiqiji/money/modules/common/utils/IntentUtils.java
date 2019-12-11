package com.yiqiji.money.modules.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.ActionSheetDialog;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.AddBookActivity;
import com.yiqiji.money.modules.homeModule.mybook.activity.ClipPictureActivity;
import com.yiqiji.money.modules.myModule.login.activity.LoginActivity;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;
import com.yiqiji.money.modules.myModule.login.activity.PictureListActivity;

import java.util.Vector;

/**
 * Created by Administrator on 2017/3/29.
 */
public class IntentUtils {
    //设置SETTING  在loginActivity中返回键做了处理
    public enum LoginIntentType {
        MINE, START, SETTING, WELLCOM, OTHER, PINGLUN;
    }

    public static String getClassName(Activity activity) {
        return activity.getPackageName() + "." + activity.getLocalClassName();
    }

    public static void setReflectionjump(Context context, String calssName, Object... para) throws ClassNotFoundException {

        Class clazz = Class.forName(calssName);
        Intent intent = new Intent();
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for (int i = 0; i < para.length; i++) {

            if (i % 2 == 0) {
                vector.add(para[i]);
            } else {
                vector1.add(para[i]);
            }
        }
        for (int j = 0; j < vector.size(); j++) {
            intent.putExtra(vector.get(j).toString(), vector1.get(j).toString());
        }
        String name = AddBookActivity.class.getName();
        if (calssName.equals(name)) {
            intent.setClass(context, ClipPictureActivity.class);
        } else {
            intent.setClass(context, clazz);
        }
        context.startActivity(intent);
    }


    public static void startActivityOnLogin(Activity context, LoginIntentType type) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("ClassName", getClassName(context));
        intent.putExtra("IntentType", type);
        context.startActivity(intent);
    }

    /**
     * 参考http://blog.csdn.net/lvxiangan/article/details/42120951
     *
     * @param context   清除掉 calssName上面的Activity，并且使用旧的calssName
     * @param className
     * @throws ClassNotFoundException
     */
    public static void startActivityClearTop(Context context, String className, LoginIntentType type) {
        //所有登录：登录成功后 MineFragment.SEND_MESSAGE设置为true，先获取个人信息后，再发送一个消息给首页接收，避免后台数据没合并
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log_error(null, e);
            ((Activity) context).finish();
            return;
        }
        Intent intent = new Intent();
//        if(className.equals("HomeActivity")&&XzbUtils.isExsitActivity(context,HomeActivity.class)){
//
//        }else{
//            EventBus.getDefault().post(new LoginLaterInfo());
//        }

        switch (type) {
            case MINE:
                clazz = HomeActivity.class;
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case START:
                clazz = HomeActivity.class;
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case SETTING:
                clazz = HomeActivity.class;
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case WELLCOM:
                LoginConfig.getInstance().isFirstStartApp(false);
                clazz = HomeActivity.class;
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case OTHER:
                //发送一个让MineFragment接收
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case PINGLUN:
                //账单评论
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                if (context instanceof LoginActivity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                }
                break;
        }
        intent.putExtra("action", true);
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }


    public static void startActivity(Context context, Class clasz) {
        Intent intent = new Intent(context, clasz);
        context.startActivity(intent);
    }

    /**
     * IntentUtils.startActivity(PictureDetailActivity.this, intentClass, new String[]{"path", pathStrng});
     *
     * @param context
     * @param clasz
     * @param para
     */
    public static void startActivity(Context context, Class clasz, Object... para) {
        Intent intent = new Intent();
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for (int i = 0; i < para.length; i++) {
            if (i % 2 == 0) {
                vector.add(para[i]);
            } else {
                vector1.add(para[i]);
            }
        }
        for (int j = 0; j < vector.size(); j++) {
            if (vector1.get(j) instanceof String) {
                intent.putExtra(vector.get(j).toString(), vector1.get(j).toString());
            } else if (vector1.get(j) instanceof Integer) {
                intent.putExtra(vector.get(j).toString(), Integer.parseInt(vector1.get(j).toString()));
            } else if (vector1.get(j) instanceof Boolean) {
                intent.putExtra(vector.get(j).toString(), (Boolean) vector1.get(j));
            } else if (vector1.get(j) instanceof Class) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(vector.get(j).toString(), (Parcelable) vector1.get(j));
                intent.putExtras(bundle);
            }
        }
        intent.setClass(context, clasz);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, Class clasz, int tag, String... para) {
        Intent intent = new Intent();
        Vector vector = new Vector();
        Vector vector1 = new Vector();
        for (int i = 0; i < para.length; i++) {

            if (i % 2 == 0) {
                vector.add(para[i]);
            } else {
                vector1.add(para[i]);
            }
        }
        for (int j = 0; j < vector.size(); j++) {
            intent.putExtra(vector.get(j).toString(), vector1.get(j).toString());
        }
        intent.setClass(context, clasz);
        context.startActivityForResult(intent, tag);

    }

    public static void startActivityForResult(Activity context, Class clasz, int tag) {
        Intent intent = new Intent(context, clasz);
        context.startActivityForResult(intent, tag);
    }


    public static void setResult(Activity context, int tag) {
        context.setResult(tag);
        context.finish();
    }

    public static void setResult(Activity context, int tag, Intent intent) {
        context.setResult(tag, intent);
        context.finish();
    }

    public static void drappedClear(Activity activity) {
        LoginConfig.getInstance().setTokenId("");
        String uuid = StringUtils.getUUID();//先从新获取deviceid后重新提交账本
        // 此id用于生成用户唯一标识
        LoginConfig.getInstance().setDeviceid(uuid);
        //必须在之前清除
        LoginConfig.getInstance().setTokenId("");
        // BaserClassMode.addBooks(SettingActivity.this, mHandler);
        LoginConfig.getInstance().setIssetnick(0);
        LoginConfig.getInstance().setIssetavatar(0);
        LoginConfig.getInstance().setUserinfojson("");
        LoginConfig.getInstance().setIsOutLogin(true);
        LoginBaseActivity.openActivity(activity);
//        IntentUtils.startActivityOnLogin(activity, IntentUtils.LoginIntentType.SETTING);
    }

    public static void choiceFromCamera(Context context, Uri uri) {
        if (context != null) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // Toast.makeText(this, "没有内存卡", Toast.LENGTH_SHORT).show();
                ToastUtils.DiyToast(context, "没有内存卡");
                return;
            }
            //獲取系統版本
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            XzbUtils.getTpPath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            //为拍摄的图片指定一个存储的路径
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, StringUtils.getRealFilePath(context, uri));
                Uri uri_string = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_string);
            }

            try {
                ((Activity) context).startActivityForResult(intent, RequsterTag.RQ_TAKE_A_PHOTO);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.log_error(null, e);
            }
        }

    }

    public static void showPhotoView(final Context context, final Uri uri) {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MyApplicaction.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return;
        }
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setCancelTextColor(context.getResources().getColor(R.color.main_back))
                .addSheetItem(context.getString(R.string.home_take_photo), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                IntentUtils.choiceFromCamera(context, uri);
                            }
                        })
                .addSheetItem(context.getString(R.string.home_from_ablum), ActionSheetDialog.SheetItemColor.MAIN_BACK,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(context, PictureListActivity.class);
                                intent.putExtra("isShowCut", false);
                                intent.putExtra("intentClass", context.getPackageName() + "." + ((Activity) context).getLocalClassName());
                                context.startActivity(intent);
                            }
                        }).show();
    }
}