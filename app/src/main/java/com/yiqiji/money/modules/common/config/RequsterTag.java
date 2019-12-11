/**
 * huangweishui
 */
package com.yiqiji.money.modules.common.config;

import android.os.Environment;

import com.yiqiji.money.R;

import java.io.File;
import java.util.Date;

/**
 * @author huangweishui
 */
public class RequsterTag {
    public static long sysTime = new Date().getTime();
    public static boolean isSynchronizationing = false;
    public static boolean isNeedSynchronizationing = false;//是
    public static final int VERSION = 7;//数据库版本
    public static final boolean DEBUG = true;
    public final static int[] head_image = new int[]{R.drawable.head_one, R.drawable.head_two, R.drawable.head_three,
            R.drawable.head_four, R.drawable.head_five, R.drawable.head_six, R.drawable.head_seven,
            R.drawable.head_eight, R.drawable.head_night, R.drawable.head_ten};
    public final static int[] notes_list_content = new int[]{R.string.share_book, R.string.has_settlement_detail, R.string.has_delet_detail, R.string.book_setting, R.string.notes_content};
    public final static int[] notes_member_detail_list_content = new int[]{R.string.change_name, R.string.goto_seetlement, R.string.quit_group, R.string.delet_member};
    public final static int[] member_detail_list_content = new int[]{R.string.change_member, R.string.delet_member_name, R.string.quit_group, R.string.cancel_text, R.string.i_known};
    public final static int[] notes_list_image = new int[]{R.drawable.share_book, R.drawable.settlement_detail_picture, R.drawable.delet_picture_detail, R.drawable.book_setting, R.drawable.message_picture};
    public static final File IMAGE_DISK_CACHE_DIR = new File(Environment.getExternalStorageDirectory(), "qiyirong"
            + File.separator + "ImageCache");

    public static final int IsphotosShow = 0;
    public static final int Islocation = 1;
    public static final int ACCESS_FINE_LOCATION_S = 2;
    public static final int RESULTCODE_PICTURELIST = 9;
    public static final int REQUESCODE_PICTURELIST = 10;
    public static final int RQ_CROPE = 11;
    public static final int RQ_PICK_A_PICTURE = 12;
    public static final int RQ_TAKE_A_PHOTO = 13;
    public static final int USER_NAME = 14;
    public static final int BOOKS_CHANGE = 15;
    public static final int EDITBOOKJUMP = 16;
    public static final int SYNCHRONIZATION_IN = 1111;// 同步中
    public static final int SYNCHRONIZATION_SUS = 1112;// 同步成功
    public static final int SYNCHRONIZATION_FAL = 1113;// 同步失败
    public static final int SEARCHTIME = 1114;// 同步失败
    public static final int EXPECTMOTH = 1115;// 预期月份
    public static final int SEARCHBOOKDETAIL = 1116;// 查询账本详情
    public static final int SEARCHBOOKMENBER = 1117;// 查询账成员
    public static final int BILLS = 1117;// 查询账单列表
    public static final int GETBILLS = 1118;// 拉去账单
    public static final int SYNCHRONIZATION_CHANGE = 1119;// 同步后更改title
    public static final int BOOKSDBINFO = 1120;// 返回账本对象
    public static final int BOOKSDBINFOS = 1121;// 返回账本对象列表
    public static final int DELETBOOK = 1122;// 删除账本
    public static final int EXPECTMOTHBING = 1123;// 删除账本
    public static final int SEACHUNSYS = 10000;// 查询未删除账单
    public static final int BOOKSSYS_SUS = 1124;// 账本同步成功
    public static final int SEARCHNEWBOOKMENBER = 1125;// 新账本请求成员
    public static final int SEARCHNDAIANMENBER = 1126;// 查询账单和成员
    public static final int SEARCHDAICOYS = 1127;// 刷新账单
    public static final int CHECKMESSAGE = 1128;// 检测消息
    public static final int FIRSTBOOK = 1129;// 第一次安装账本提示
    public static final int NEWBOOK = 1130;// 新账本提示
    public static final int QINIUPATH = 1131;// 调用云存储接口后获取七牛图片地址
    public static final int QINIUPATHFAIL = 1132;// 七牛上传失败
    public static final int CHANGENAME = 1201;// 七牛上传失败
    public static final int LOCALBILLS = 1202;// 跟新本地账单
    /*activityforesult*/
    public static final int CALENDERCODE = 5555;// 日历requscode


    public static final int DROPPED = 20010;// 登录掉线
    public static final String RefreshMine = "RefreshMine";//掉线后刷新MineFragmenrt
    public static final int BILLSLOCATIONSERVER = 20011;// 时间轴切换的时候先查询本地有没有账单没有从服务器上拉取
    public static final int STARTACTIVITYRESULT = 20012;//密码登陆result
    public static final int STARTACTIVITYREREQUST = 20013;//密码登陆requstcode
    /**/

    public static final String ACCOUNTBOOKCATE = "474";//旅行账本分类id
    public static final String TOURISMACCOUNTBOOKCATE = "12";//旅游账本分类id
    public static final String CARACCOUNTBOOKCATE = "3";//汽车账本分类id
    public static final String RENOVATIONACCOUNTBOOKCATE = "6";//装修账本分类id
    public static final String DIARYACCOUNTBOOKCATE = "5";//装修账本日志id


}
