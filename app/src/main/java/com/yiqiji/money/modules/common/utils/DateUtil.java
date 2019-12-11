package com.yiqiji.money.modules.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DateUtil {
    /**
     * date string to Date
     *
     * @param date eg:2016-10-17
     * @return
     */
    public static Date stringToDate(String date) {
        String[] split = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(split[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(split[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(split[2]));
        calendar.set(Calendar.HOUR, Integer.valueOf(0));
        calendar.set(Calendar.MINUTE, Integer.valueOf(0));
        calendar.set(Calendar.SECOND, Integer.valueOf(0));
        calendar.set(Calendar.MILLISECOND, Integer.valueOf(0));
        return calendar.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public static String transferLongToDate(int type, Long millSec) {
        millSec = millSec * 1000;
        String str = "";
        switch (type) {
            case 0:
                str = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE).format(millSec);
                break;
            case 1:
                str = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE).format(millSec);
                break;
            case 2:
                str = new SimpleDateFormat("yyyy", Locale.CHINESE).format(millSec);
                break;
            case 3:
                str = new SimpleDateFormat("MM", Locale.CHINESE).format(millSec);
                break;
            case 4:
                str = new SimpleDateFormat("dd", Locale.CHINESE).format(millSec);
                break;
            case 5:
                str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(millSec);
                break;
            case 6:
                str = new SimpleDateFormat("MM-dd", Locale.CHINESE).format(millSec);
                break;
            default:
                break;
        }

        return str;
    }

    /**
     * @return eg:09-29
     */
    public static String formatTheDateToMM_dd(Date date, int type) {
        String str = "";
        switch (type) {
            case 0:
                str = new SimpleDateFormat("MM-dd", Locale.CHINESE).format(date);
                break;
            case 1:
                str = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(date);
                break;
            case 2:
                str = new SimpleDateFormat("dd", Locale.CHINESE).format(date);
                break;
            case 3:
                str = new SimpleDateFormat("MM", Locale.CHINESE).format(date);
                break;
            case 4:
                str = new SimpleDateFormat("EEEE", Locale.CHINESE).format(date);
                break;
            case 5:
                str = new SimpleDateFormat("yyyy", Locale.CHINESE).format(date);
                break;
            case 6:
                str = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE).format(date);
                break;
            case 7:
                str = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE).format(date);
                break;
            case 8:
                str = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINESE).format(date);
                break;
            case 9:
                str = new SimpleDateFormat("MM月dd日", Locale.CHINESE).format(date);
                break;

        }
        return str;
    }

    public static long longTime(Date date) {
        String stringTime = formatTheDateToMM_dd(date, 7);
        Date date1 = null;
        //Date或者String转化为时间戳
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            date1 = format.parse(stringTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date1.getTime() / 1000;
        return time;
    }

    public static long longTime(Date date, String fomatTime) {
        String stringTime = new SimpleDateFormat(fomatTime, Locale.CHINESE).format(date);
        Date date1 = null;
        //Date或者String转化为时间戳
        SimpleDateFormat format = new SimpleDateFormat(fomatTime);
        try {
            date1 = format.parse(stringTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date1.getTime() / 1000;
        return time;
    }

    /**
     * @param amount :back or forward day's count
     * @return
     */
    public static Date getTheDayBeforeToDayOrAfter(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return 1 to 7 1:Sunday, 7:Saturday
     */
    public static int getTheDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param time
     * @return
     */

    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /**
     * 时间戳转换成字符窜 hh:mm
     *
     * @param time
     * @return
     */

    public static String getDateToStringhhmm(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(d);
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param time
     * @return
     */

    public static String getDateToString(long time, String timeFormat) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        return sf.format(d);
    }

    public static long toTimeStamp(Date date) {
        return date.getTime();
    }

    /**
     * yyyy-MM-dd 转时间搓
     *
     * @param
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long stringToTime(long time) {
        Date dateString = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String timeString = simpleDateFormat.format(dateString);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static boolean isExpectMoth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.MONTH, 1);

        long end_time = (DateUtil.stringToTime(calendar.getTimeInMillis()));// 结束时间
        long current_time = (new Date()).getTime();
        boolean isExpectMoth = false;
        if (end_time > current_time) {
            isExpectMoth = true;
        }
        return isExpectMoth;

    }

    /**
     * 0:前面的时间1：当前时间2：预期时间
     *
     * @param date
     * @return
     */
    public static int getSwichMoth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long end_time = DateUtil.stringToTime(calendar.getTimeInMillis());//选中月份开始时间搓
        long time_long = date.getTime();// 选中时间搓
        long current_time = (new Date()).getTime();//当前时间搓
        long current_moth_time = DateUtil.stringToTime((new Date()).getTime());//当前月份开始时间搓
        int wichMoth = 1;
        if (end_time > current_time) {
            wichMoth = 2;
        } else if (time_long < current_moth_time) {
            wichMoth = 0;
        } else {
            wichMoth = 1;
        }
        return wichMoth;
    }

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */

    public static long getUnixStamp() {

        return System.currentTimeMillis() / 1000;

    }

    /**
     * 得到昨天的日期
     *
     * @return
     */

    public static String getYestoryDate() {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String yestoday = sdf.format(calendar.getTime());

        return yestoday;

    }

    /**
     * 得到上个月的日期
     *
     * @return
     */

    public static String getLastMonthDate() {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, +1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String yestoMonth = sdf.format(calendar.getTime());

        return yestoMonth;

    }

    /**
     * 得到今天的日期
     *
     * @return
     */

    public static String getTodayDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(new Date());

        return date;

    }

    /**
     * 比较时间大小
     *
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public static boolean compareTime(String time1, String time2) throws ParseException {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将字符串形式的时间转化为Date类型的时间
        Date a = sdf.parse(time1);
        Date b = sdf.parse(time2);
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if (a.before(b))
            return true;
        else
            return false;
        /*
         * 如果你不喜欢用上面这个太流氓的方法，也可以根据将Date转换成毫秒
        if(a.getTime()-b.getTime()<0)
            return true;
        else
            return false;
        */
    }

    /**
     * 时间戳转化为时间格式
     *
     * @param timeStamp
     * @return
     */

    public static String timeStampToStr(long timeStamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sdf.format(timeStamp * 1000);

        return date;

    }

    /**
     * 得到日期   yyyy-MM-dd
     *
     * @param timeStamp 时间戳
     * @return
     */

    public static String formatDate(long timeStamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(timeStamp * 1000);

        return date;

    }

    /**
     * 得到时间  HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return
     */

    public static String getTime(long timeStamp) {

        String time = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sdf.format(timeStamp * 1000);

        String[] split = date.split("\\s");

        if (split.length > 1) {

            time = split[1];

        }

        return time;

    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */

    public static String convertTimeToFormat(long timeStamp) {

        long curTime = System.currentTimeMillis() / (long) 1000;

        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {

            return "刚刚";

        } else if (time >= 60 && time < 3600) {

            return time / 60 + "分钟前";

        } else if (time >= 3600 && time < 3600 * 24) {

            return time / 3600 + "小时前";

        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {

            return time / 3600 / 24 + "天前";

        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {

            return time / 3600 / 24 / 30 + "个月前";

        } else if (time >= 3600 * 24 * 30 * 12) {

            return time / 3600 / 24 / 30 / 12 + "年前";

        } else {

            return "刚刚";

        }

    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */

    public static String timeStampToFormat(long timeStamp) {

        long curTime = System.currentTimeMillis() / (long) 1000;

        long time = curTime - timeStamp;

        return time / 60 + "";

    }

    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    public static String timeStampDate(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }


    /**
     * 判断时间是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取昨天时间
     *
     * @param date
     * @return
     */
    public static Date getUpDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取明天时间
     *
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 查询一个月开始时间
     *
     * @param date
     * @return
     */
    public static long getStartTime(Date date) {
        long start_time = stringToTime(date.getTime()) / 1000;
        return start_time;
    }

    /**
     * 查询一个月结束时间
     *
     * @param date
     * @return
     */
    public static long getEndTime(Date date) {
        boolean isExpectMoth = DateUtil.isExpectMoth(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (isExpectMoth) {
            calendar.add(Calendar.MONTH, 12);
        } else {
            calendar.add(Calendar.MONTH, 1);
        }

        long end_time = (stringToTime(calendar.getTimeInMillis()) - 60 * 1000) / 1000 - 1000;// 结束时间
        return end_time;
    }

    public static HashMap<String, String> getmapParama(String... parama) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (parama != null && parama.length > 0) {
            int lenth = parama.length;
            String key = "";
            String value = "";
            for (int i = 0; i < parama.length; i++) {
                if (i % 2 == 0) {
                    key = parama[i];
                } else {
                    value = parama[i];
                    stringHashMap.put(key, value);
                }

            }

        }
        return stringHashMap;
    }

    public static String getMoth(Date date) {
        String moth = "";
        if (date != null) {
            moth = formatTheDateToMM_dd(date, 3);
            if (TextUtils.isEmpty(moth)) {
                moth = Integer.parseInt(moth) + "";
            }
        }
        return moth;
    }

    public static String getYearMoth(Date date) {
        String yearMoth = "";
        if (date != null) {
            yearMoth = formatTheDateToMM_dd(date, 5) + formatTheDateToMM_dd(date, 3);
        }
        return yearMoth;
    }

    public static String getBudgetkey(String accountid, String month, String sorptye) {
        String budgetkey = "";
        if (!TextUtils.isEmpty(accountid) && !TextUtils.isEmpty(sorptye)) {
            if (sorptye.equals("0")) {
                budgetkey = accountid;
            } else {
                budgetkey = accountid + month;
            }

        }
        return budgetkey;
    }

    /**
     * @param isSelected
     * @param date       当前时间
     */
    public static String[] getTimeSelectedStrings(boolean isSelected, Date date) {
        String[] date_content = new String[2];
        date_content[0] = "";
        date_content[1] = "";
        //判断是否是今天
        if (isSelected) {
            if (DateUtil.isToday(date)) {
                date_content[0] = "今天";
                date_content[1] = "昨天?";
            } else {
                date_content[0] = DateUtil.formatTheDateToMM_dd(date, 1);
                date_content[1] = "今天?";
            }
        } else {
            if (DateUtil.isToday(date)) {
                date_content[0] = "今天";
                date_content[1] = "昨天?";
            } else {
                date_content[0] = "昨天";
                date_content[1] = "今天?";
            }
        }
        return date_content;
    }


    public static String formatTime(String seconds, String pattern) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        Date date = new Date(Long.valueOf(seconds + "000"));
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String timeText = sf.format(date);

        Date nowDate = new Date();
        if (nowDate.getYear() != date.getYear()) {
            timeText = new SimpleDateFormat("yyyy").format(date) + "-" + timeText;
        }
        return timeText;
    }

    public static String formatCommentTime(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        Date date = new Date(Long.valueOf(seconds + "000"));
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:MM");
        String timeText = sf.format(date);

        Date nowDate = new Date();
        if (nowDate.getYear() != date.getYear()) {
            timeText = new SimpleDateFormat("yyyy").format(date) + "-" + timeText;
        }
        return timeText;
    }
}
