package com.yiqiji.frame.core.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yiqiji.frame.core.system.BaseApplication;
import com.yiqiji.frame.core.utils.BitmapUtil;

//http://blog.csdn.net/xiaanming/article/details/9339515
public final class LoginConfig {

    private static SharedPreferences perference;
    private static LoginConfig instance;
    private String image_head = "bitmap";
    public static String share_context = "Hi，我正在用“一起记”记账，了解钱去哪儿了，推荐你试试哟！（一起记可以多人协同记账及支持成员之间AA结算，有日常、家庭、合租、旅游、装修、结婚等，点这里免费下载…）";//分享文案
    public static String group_context = "成员的消费详情，点此马上查看。";//群组成员分享文案
    public static String static_context = "更直观的展现你的收入支出，以及消费类别，账本参与人之间的收付金额，一目了然";//统计分享文案
    public static String share_url = "http://api.yiqijiba.com/common/index/download";

    //推荐好友使用的文案
    public static String share_recommend_friend_title = "Hi，我正在用“一起记”记账，了解钱去哪儿了，推荐你试试哟！";
    public static String share_recommend_friend_text = "一起记可以多人协同记账及支持成员之间AA结算，有日常、家庭、合租、旅游、装修、结婚等，点这里免费下载…";
    //邀请好友加入账本使用文案
    public static String invest_context = "一起记为多人协同记账及支持成员之间AA结算，点击加入账本一起记账吧！（有效期7天）";
    public static String share_acounter_context = "订阅你喜欢的账本，可以参考作者的消费开支，来优化自己的消费，包括装修预算和旅行预算哦。";
    //日志分享文案
    public static String share_journal_title = "";
    public static String share_journal_content = "";

    private LoginConfig(Context context) {
        perference = context.getSharedPreferences("login_prefrence", Context.MODE_PRIVATE);
    }

    public final static String TOKENID = "tokenid";//登录凭证
    public final static String TOKENIDQINIU = "tokenid_qiniu";//七牛token
    public final static String TOKENIDKEY = "tokenid_key";//七牛key
    public final static String TOKENIDHOST = "tokenid_host";//七牛host
    public final static String USERNAME = "userName";// 登录成功后，用户名
    public final static String MOBILE = "Mobile";//手机号
    public final static String isForstStartApp = "startapp";//是否是第一次启动app
    public final static String isFisrtBook = "isfisrtbook";//是否是第一次使用操作账本
    public final static String LATITUDEANDLONGITUDE = "Latitudeandlongitude";//定位经纬度
    public final static String LOCATIONCITY = "LocationCity";//定位城市
    public final static String USERICON = "usericon";//服务器返回的头像地址
    public final static String DEVICE_ID = "deviceid";// 设备唯一标示
    public final static String USERIID = "userid";//用户唯一ID
    public final static String OSVER = "osver";//系统版本
    public final static String APPVER = "appver";//app版本号
    public final static String MYJSONGBOOKINFO = "myjsongbookinfo";//存储账本类型jsong字符创字段
    public final static String IS_LOGIN = "is_login";//是否登录过
    public final static String JSONGBOOKLIST = "jsongbooklist";//账本分类列表
    public final static String BOOKID = "bookid";//账本名称
    public final static String PLAT = "android";// 设备标识，android |ios
    public final static String MACHINE = "machine";// 手机型号
    public final static String BUDGET = "budget";// 设置预算
    public final static String MEMBERID = "memberid";// 手动添加成员id设置
    public final static String BOOKNAME = "bookname";// 账本名称
    public final static String ISSETNICK = "issetnick";// 是否设置过用户名0:没有 1：有
    public final static String ISSETAVATAR = "issetavatar";// 是否设置过头像0:没有 1：有
    public final static String USERINFOJSON = "userinfojson";// 用户信息json字符串
    public final static String HOMECREATE = "homecreate";// homeactivity是否已经创建


    public static synchronized LoginConfig getInstance() {
        if (instance == null) {
            instance = new LoginConfig(BaseApplication.getInstence().getContext());
        }
        return instance;
    }

    public void setImage_head(Bitmap bitmap) {

        perference.edit().putString(image_head, BitmapUtil.bitmapToBase64(bitmap)).commit();
    }

    public Bitmap getImage_head() {
        String path = perference.getString(image_head, null);
        Bitmap bitmap = null;
        if (path != null) {
            bitmap = BitmapUtil.base64ToBitmap(perference.getString(image_head, ""));
        }
        return bitmap;
    }

    public void isFirstStartApp(boolean isStart) {
        perference.edit().putBoolean(isForstStartApp, isStart).commit();
    }

    public boolean getIsFirstStartApp() {
        return perference.getBoolean(isForstStartApp, true);
    }

    public void isFisrtbook(boolean isfisrtbook) {
        perference.edit().putBoolean(isFisrtBook, isfisrtbook).commit();
    }

    public boolean getIsFisrtbook() {
        return perference.getBoolean(isFisrtBook, true);
    }


    public void setTokenId(String tokenid) {
        perference.edit().putString(TOKENID, tokenid).commit();
    }

    public String getTokenId() {
        return perference.getString(TOKENID, "");
    }

    public void setUserName(String userName) {
        perference.edit().putString(USERNAME, userName).commit();
    }

    public String getUserName() {
        return perference.getString(USERNAME, "");
    }


    public void setUsericon(String usericon) {
        perference.edit().putString(USERICON, usericon).commit();
    }

    public String getUsericon() {
        return perference.getString(USERICON, "");
    }


    public void setUserid(String userid) {
        perference.edit().putString(USERIID, userid).commit();
    }

    public String getUserid() {
        return perference.getString(USERIID, "");
    }

    public void setMobile(String mobile) {
        perference.edit().putString(MOBILE, mobile).commit();
    }

    public String getMobile() {
        return perference.getString(MOBILE, "");
    }

    public void setDeviceid(String deviceid) {
        perference.edit().putString(DEVICE_ID, deviceid).commit();
    }

    public String getDeviceid() {
        return perference.getString(DEVICE_ID, "");
    }


    public void setPlat(String plat) {
        perference.edit().putString(PLAT, plat).commit();
    }

    public String getPlat() {
        return perference.getString(PLAT, "");
    }


    public void setAppver(String appver) {
        perference.edit().putString(APPVER, appver).commit();
    }

    public String getAppver() {
        return perference.getString(APPVER, "");
    }


    public void setOsver(String osver) {
        perference.edit().putString(OSVER, osver).commit();
    }

    public String getOsver() {
        return perference.getString(OSVER, "");
    }


    public void setMachine(String machine) {
        perference.edit().putString(MACHINE, machine).commit();
    }

    public String getMachine() {
        return perference.getString(MACHINE, "");
    }


    public void setBudget(String id, String budget) {
        perference.edit().putString(BUDGET + "_" + id, budget).commit();
    }

    public String getBudget(String id) {
        return perference.getString(BUDGET + "_" + id, "0");
    }

    public void setMemberid(long memberid) {
        perference.edit().putLong(MEMBERID, memberid).commit();
    }

    public long getMemberid() {
        return perference.getLong(MEMBERID, 1);
    }


    public void setBookname(String bookname) {
        perference.edit().putString(BOOKNAME, bookname).commit();
    }

    public String getBookname() {
        return perference.getString(BOOKNAME, "日常账本");
    }

    public void setBookId(String bookid) {
        perference.edit().putString(BOOKID, bookid).commit();
    }

    public String getBookId() {
        return perference.getString(BOOKID, "");
    }


    public void setJsonbook(String jsonbookid, String jsonbookString) {
        perference.edit().putString(jsonbookid, jsonbookString).commit();
    }

    public String getJsonbook(String jsonbookid) {
        return perference.getString(jsonbookid, "");
    }

    public void setJsonbookList(String jsonbooklistString) {
        perference.edit().putString(JSONGBOOKLIST, jsonbooklistString).commit();
    }

    public String getJsonbookList() {
        return perference.getString(JSONGBOOKLIST, "");
    }

    public void setQiniuToken(String QiniuTokenvalue) {
        perference.edit().putString(TOKENIDQINIU, QiniuTokenvalue).commit();
    }

    public String getQiniuToken() {
        return perference.getString(TOKENIDQINIU, "");
    }

    public void setQiniuKey(String qiniuKey) {
        perference.edit().putString(TOKENIDKEY, qiniuKey).commit();
    }

    public String getQiniuKey() {
        return perference.getString(TOKENIDKEY, "");
    }

    public void setQiniuHost(String qiniuHost) {
        perference.edit().putString(TOKENIDHOST, qiniuHost).commit();
    }

    public String getQiniuHost() {
        return perference.getString(TOKENIDHOST, "");
    }

    public void setLatitudeAndLongitude(String latitudeAndLongitude) {
        perference.edit().putString(LATITUDEANDLONGITUDE, latitudeAndLongitude).commit();
    }

    public String getLatitudeAndLongitude() {
        return perference.getString(LATITUDEANDLONGITUDE, "");
    }

    public void setLocationCity(String locationCity) {
        perference.edit().putString(LOCATIONCITY, locationCity).commit();
    }

    public String getLocationCity() {
        return perference.getString(LOCATIONCITY, "");
    }


    public void setIsOutLogin(boolean value) {
        perference.edit().putBoolean(IS_LOGIN, value).commit();
    }

    public boolean getIsOutLogin() {
        return perference.getBoolean(IS_LOGIN, false);
    }

    public boolean isLogin() {
        String tokenId = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenId)) {
            return false;
        }
        return true;
    }

    public void setIssetnick(int issetnick) {
        perference.edit().putInt(ISSETNICK, issetnick).commit();
    }

    public int getIssetnick() {
        return perference.getInt(ISSETNICK, 0);
    }

    public void setIssetavatar(int issetavatar) {
        perference.edit().putInt(ISSETAVATAR, issetavatar).commit();
    }

    public int getIssetavatar() {
        return perference.getInt(ISSETAVATAR, 0);
    }

    public void setUserinfojson(String userinfojson) {
        perference.edit().putString(USERINFOJSON, userinfojson).commit();
    }

    public String getUserinfojson() {
        return perference.getString(USERINFOJSON, "");
    }

    public void setHomecreate(boolean homecreate) {
        perference.edit().putBoolean(HOMECREATE, homecreate).commit();
    }

    public boolean getetHomecreate() {
        return perference.getBoolean(HOMECREATE, false);
    }

}
