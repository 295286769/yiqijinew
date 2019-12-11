package com.yiqiji.money.modules.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

public class RegexUtil {

    /**
     * 过滤非法字符
     * 
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]"; // 要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 过略特殊字符
     * 
     * @param str
     * @return
     */
    public static String delectWarningFilter(String str) {
        Pattern pt = Pattern.compile("[`~!@#$%^&*()+=-|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]");
        Matcher mt = pt.matcher(str);
        str = mt.replaceAll("");
        return str;
    }

    /**
     * 过滤空字符
     * 
     * @param str
     * @return
     */
    public static String getStr(Object str) {
        if (str == null) {
            return "";
        } else if ("null".equals(str.toString())) {
            return "";
        }
        return str.toString();
    }

    public static String replaceBlank(String str) {
        Pattern pt = Pattern.compile("^\\s*|\\s*$");
        Matcher mt = pt.matcher(str);
        str = mt.replaceAll("");
        return str;
    }

    /**
     * 讲传进来的手机号码换成133****0000格式
     * 
     * @param phone
     *            string
     * @return 133****0000
     */
    public static String replacePhone(String phone) {
        if (phone != null && phone.length() == 11) {
            String start = phone.substring(0, 3);
            String end = phone.substring(phone.length() - 4, phone.length());
            return start + "****" + end;
        }
        return "";
    }

    /**
     * 是否全是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否全是字母
     * @param str
     * @return
     */
    public static boolean isChar(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 是否全是邮箱
     * 
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile(
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean checkNetworkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isAvailable() && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、147、150、151、157(TD)、158、159、187、188
    联通：130、131、132、145、152、155、156、176、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //  String telRegex = "[1][34587]\\d{9}";//"[1]"代表第1位为数字1，"[3587]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1]\\d{10}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else return mobiles.matches(telRegex);
    }


    public static boolean isPasw(String pasw) {
        String regStr = "^[a-zA-Z0-9]{6,18}$";

//        1、密码必须由数字、字符、特殊字符三种中的两种组成；
//        2、密码长度不能少于6个字符；
//        满足以上两点，应该怎么实现？
//        (?!^\\d+$)不能全是数字
//                (?!^[a-zA-Z]+$)不能全是字母
//                (?!^[_#@]+$)不能全是符号（这里只列出了部分符号，可自己增加，有的符号可能需要转义）
//       .{6,18}  6-18位
        //   String regStr = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,18}";
        return pasw.matches(regStr);
    }
}
