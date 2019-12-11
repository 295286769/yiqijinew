package com.yiqiji.frame.core.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.FacadeUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.GZIPInputStream;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    // private final static SimpleDateFormat dateFormater = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new
    // SimpleDateFormat("yyyy-MM-dd");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * @return
     * @Description： 返回字符串格式的当前时间
     */
    public static String getCurrentTime() {
        return dateFormater.get().format(new Date());
    }

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
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 对象转浮点
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    // 解压缩
    public static String uncompress(InputStream str) throws IOException {
        if (str == null) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPInputStream gunzip = new GZIPInputStream(str);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
        return out.toString("utf-8");
    }

    // M5D加密
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串反转
     *
     * @param s
     * @return
     */
    public static String reverse1(String s) {
        int length = s.length();
        if (length <= 1)
            return s;
        String left = s.substring(0, length / 2);
        String right = s.substring(length / 2, length);
        return reverse1(right) + reverse1(left);
    }

    /**
     * 参数签名
     *
     * @param valus
     * @param params
     * @return
     */
    public static String setSign(String[] valus, String[] params) {

        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            hashMap.put(params[i], valus[i]);
        }

        Arrays.sort(params);
        String pa = "";
        if (params.length > 1) {
            for (int i = 0; i < params.length; i++) {

                if (i == params.length - 1) {
                    pa += params[i] + "=" + hashMap.get(params[i]);
                } else {
                    pa += params[i] + "=" + hashMap.get(params[i]) + "&";
                }

            }
        } else {
            pa += params[0] + "=" + hashMap.get(params[0]);
        }
        String secret = Constants.secret + pa;
        String sign = StringUtils.MD5(secret);
        sign = StringUtils.reverse1(sign);
        return sign;
    }

    /**
     * 参数签名
     *
     * @return
     */
    public static String setSign(String... parama) {

        HashMap<String, String> hashMap = FacadeUtil.getBaseParmasMap();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine"};
        Vector vector = new Vector();
        String key = "";
        String value = "";
        for (int i = 0; i < parama.length; i++) {
            if (i % 2 == 0) {
                key = parama[i];
            } else {
                value = parama[i];
                hashMap.put(key, value);
            }
        }
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            vector.add(entry.getKey());
        }
        Collections.sort(vector);
        String pa = "";
        if (hashMap.size() > 1) {
            for (int i = 0; i < hashMap.size(); i++) {

                if (i == params.length - 1) {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i));
                } else {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i)) + "&";
                }

            }
        } else {
            pa += vector.get(0) + "=" + hashMap.get(vector.get(0));
        }
        String secret = Constants.secret + pa;
        String sign = StringUtils.MD5(secret);
        sign = StringUtils.reverse1(sign);
        return sign;
    }

    /**
     * 参数签名
     *
     * @param key
     * @param value
     * @return
     */
    public static HashMap<String, String> getParamas(String[] key, String[] value) {

        HashMap<String, String> hashMap = FacadeUtil.getBaseParmasMap();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine"};

        Vector vector = new Vector();
        for (int i = 0; i < key.length; i++) {
            hashMap.put(key[i], value[i]);
        }

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            vector.add(entry.getKey());
        }

        Collections.sort(vector);
//        Arrays.sort(params);
        String pa = "";
        if (vector.size() > 1) {
            for (int i = 0; i < vector.size(); i++) {

                if (i == vector.size() - 1) {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i));
                } else {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i)) + "&";
                }

            }
        } else {
            pa += params[0] + "=" + hashMap.get(params[0]);
        }
        String secret = Constants.secret + pa;
        String sign = StringUtils.MD5(secret);
        sign = StringUtils.reverse1(sign);
        hashMap.put("sign", sign);
        return hashMap;
    }

    /**
     * 参数签名
     *
     * @return
     */
    public static HashMap<String, String> getParamas(String... param) {

        HashMap<String, String> hashMap = FacadeUtil.getBaseParmasMap();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine"};

        Vector vector = new Vector();
        String key = "";
        String value = "";
        for (int i = 0; i < param.length; i++) {
            if (i % 2 == 0) {
                key = param[i];
            } else {
                value = param[i];
                hashMap.put(key, value);
            }
        }

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            vector.add(entry.getKey());
        }

        Collections.sort(vector);
//        Arrays.sort(params);
        String pa = "";
        if (vector.size() > 1) {
            for (int i = 0; i < vector.size(); i++) {

                if (i == vector.size() - 1) {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i));
                } else {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i)) + "&";
                }

            }
        } else {
            pa += params[0] + "=" + hashMap.get(params[0]);
        }
        String secret = Constants.secret + pa;
        String sign = StringUtils.MD5(secret);
        sign = StringUtils.reverse1(sign);
        hashMap.put("sign", sign);
        return hashMap;
    }

    /**
     * 参数没有签名
     *
     * @return
     */
    public static HashMap<String, String> getParamasNoSign(String... param) {

        HashMap<String, String> hashMap = new HashMap<>();
        String key = "";
        String value = "";
        for (int i = 0; i < param.length; i++) {
            if (i % 2 == 0) {
                key = param[i];
            } else {
                value = param[i];
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    public static String getUrlparam(String... parama) {
        HashMap<String, String> hashMap = FacadeUtil.getBaseParmasMap();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
        String[] params = new String[]{"tokenid", "plat", "deviceid", "appver", "osver", "machine"};

        Vector vector = new Vector();
        String key = "";
        String value = "";
        for (int i = 0; i < parama.length; i++) {
            if (i % 2 == 0) {
                key = parama[i];
            } else {
                value = parama[i];
                hashMap.put(key, value);
            }
        }

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            vector.add(entry.getKey());
        }
        String pa = "";
        if (hashMap.size() > 1) {
            for (int i = 0; i < hashMap.size(); i++) {

                if (i == params.length - 1) {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i));
                } else {
                    pa += vector.get(i) + "=" + hashMap.get(vector.get(i)) + "&";
                }

            }
        } else {
            pa += vector.get(0) + "=" + hashMap.get(vector.get(0));
        }
        return pa;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 判断数据集是否为空，为空则返回true
     *
     * @param list input
     * @return boolean
     */
    public static boolean isEmptyList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串是否是double型
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断一个字符串是否是int型
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    public static String getStarsMobile(String pNumber) {
        StringBuilder sb = null;
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb == null ? "" : sb.toString();
    }

    /**
     * 从某一个开始隐藏text
     *
     * @param text
     * @param length
     * @return
     */
    public static String onHideText(String text, int length) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        if (text.length() > length) {
            return text.substring(0, length) + "...";
        } else {
            return text;
        }
    }

    public static String getCurMonth(String string) {
        String curMonth;
        switch (string) {
            case "01":
                curMonth = "一月";
                break;
            case "02":
                curMonth = "二月";
                break;
            case "03":
                curMonth = "三月";
                break;
            case "04":
                curMonth = "四月";
                break;
            case "05":
                curMonth = "五月";
                break;
            case "06":
                curMonth = "六月";
                break;
            case "07":
                curMonth = "七月";
                break;
            case "08":
                curMonth = "八月";
                break;
            case "09":
                curMonth = "九月";
                break;
            case "10":
                curMonth = "十月";
                break;
            case "11":
                curMonth = "十一月";
                break;
            case "12":
                curMonth = "十二月";
                break;
            default:
                curMonth = "八月";
        }

        return curMonth;

    }

    public static String moneySplitComma(String money) {
        if (TextUtils.isEmpty(money) || "null".equals(money)) {
            return "0.00";
        }

        //TODO 在这里修改价格显示。
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,##0.00");
        return myformat.format(Double.valueOf(money));
    }

    /**
     * 反格式化
     *
     * @param money
     * @return
     */
    public static String moneyAntiformatComma(String money) {
        if (TextUtils.isEmpty(money)) {
            return "0.00";
        }

        double doubleMoney = Double.valueOf(money);
        int intMoney = (int) doubleMoney;

        if (intMoney == doubleMoney) {
            return String.valueOf(intMoney);
        }

        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("0.00");
        return myformat.format(Double.valueOf(money));
    }

    public static SpannableStringBuilder getStypeTextString(String wholeStr, String highlightStr, int size, int textColor) {
        String stypeTextString = "";
        int startIndex = wholeStr.indexOf(highlightStr);
        int ednIndex = startIndex + highlightStr.length();
        if (!TextUtils.isEmpty(wholeStr) && !TextUtils.isEmpty(highlightStr)) {
            if (wholeStr.contains(highlightStr)) {
                /*
                 *  返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
                 */
                startIndex = wholeStr.indexOf(highlightStr);
                ednIndex = startIndex + highlightStr.length();
            } else {
                return null;
            }
        } else {
            return null;
        }
        SpannableStringBuilder spBuilder = new SpannableStringBuilder(wholeStr);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size);  // 设置字体大小
        CharacterStyle charaStyle = new ForegroundColorSpan(textColor);

        spBuilder.setSpan(ass, startIndex, ednIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(charaStyle, startIndex, ednIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spBuilder;
    }

    public static SpannableStringBuilder getStypeTextString(String wholeStr, String highlightStr, int size, int textColor, ClickableSpan clickableSpan) {
        String stypeTextString = "";
        int startIndex = wholeStr.indexOf(highlightStr);
        int ednIndex = startIndex + highlightStr.length();
        if (!TextUtils.isEmpty(wholeStr) && !TextUtils.isEmpty(highlightStr)) {
            if (wholeStr.contains(highlightStr)) {
                /*
                 *  返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
                 */
                startIndex = wholeStr.indexOf(highlightStr);
                ednIndex = startIndex + highlightStr.length();
            } else {
                return null;
            }
        } else {
            return null;
        }
        SpannableStringBuilder spBuilder = new SpannableStringBuilder(wholeStr);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size);  // 设置字体大小
        CharacterStyle charaStyle = new ForegroundColorSpan(textColor);
        spBuilder.setSpan(clickableSpan, startIndex, ednIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(ass, startIndex, ednIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spBuilder.setSpan(charaStyle, startIndex, ednIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spBuilder;
    }

    public static String getInputText(View view) {
        String input_text = "";
        if (view != null) {
            if (view instanceof EditText) {
                input_text = ((EditText) view).getText().toString();
            } else if (view instanceof TextView) {
                input_text = ((TextView) view).getText().toString();
            } else if (view instanceof Button) {
                input_text = ((Button) view).getText().toString();
            }
        }
        if (TextUtils.isEmpty(input_text)) {
            input_text = "";
        }
        return input_text;
    }

    public static String addBlueColor(String text) {
        return "<font color='#498be7'>" + text + "</font>";
    }

    public static String getactivityPath(Class context) {
        String path = context.getName();

        return path;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
