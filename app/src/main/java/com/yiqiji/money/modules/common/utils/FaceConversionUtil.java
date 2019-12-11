package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.entity.FaceEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情转换工具
 *
 * @author shaoxs
 * @version V1.0
 * @Date 2014-3-1 上午11:23:07
 */
public class FaceConversionUtil {

    /**
     * 每一页表情的个数
     */
    private int pageSize = 27;

    private static FaceConversionUtil mFaceConversionUtil;

    /**
     * 保存于内存中的表情HashMap,例如 {79=emoji_79, 78=emoji_78 ... }
     */
    private HashMap<String, String> emojiMap = new HashMap<String, String>();

    /**
     * 保存于内存中的表情集合
     */
    private List<FaceEntity> emojis = new ArrayList<FaceEntity>();

    /**
     * 表情分页的结果集合
     */
    public List<List<FaceEntity>> emojiLists = new ArrayList<List<FaceEntity>>();
    public static String LENGTHSTR = "<img src=\"http://www.yangche51.com/Kindeditor/plugins/emoticons/images/"
            + "11.gif\" alt=\"picture\"/>";

    private static String PATTERN = "<img(.*?)src=(.*?) (.*?)>";

    private FaceConversionUtil() {

    }

    public static FaceConversionUtil getInstace() {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceConversionUtil();
        }
        return mFaceConversionUtil;
    }

    public Spanned HtmlFromStr(final Context context, String source) {
        ImageGetter imageGetter = new ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                GifOpenHelper helper = new GifOpenHelper();
                Drawable d = null;
                if (source.lastIndexOf(".gif") == -1) {
                    return d;
                }
                String key = source.substring(0, source.lastIndexOf(".gif"));
                if (key.lastIndexOf("/") == -1) {
                    return d;
                }
                key = key.substring(key.lastIndexOf("/") + 1);
                String value = emojiMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    return d;
                }
                int resId = context.getResources().getIdentifier(value,
                        "drawable", context.getPackageName());
                helper.read(context.getResources().openRawResource(resId));
                d = new BitmapDrawable(helper.getImage());
                int width = UIHelper.dip2px(context, 20);
                // d = context.getResources().getDrawable(resId);
                d.setBounds(0, 0, width, width);
                return d;
            }
        };
        return Html.fromHtml(source, imageGetter, null);
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
        }
        return spannableString;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString) {
        GifOpenHelper helper = new GifOpenHelper();
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        helper.read(context.getResources().openRawResource(imgId));
        Bitmap bitmap = helper.getImage();
        int width = UIHelper.dip2px(context, 20);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper = null;
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String str = matcher.group();
            String key = str.substring(0, str.lastIndexOf(".gif"));
            key = key.substring(key.lastIndexOf("/") + 1);
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                int width = UIHelper.dip2px(context, 30);
                bitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(bitmap);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + str.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    public void getFileText(Context context) {
        ParseData(FileUtil.getEmojiFile(context), context);
    }

    /**
     * 解析字符
     *
     * @param data
     */
    private void ParseData(List<String> data, Context context) {
        if (data == null) {
            return;
        }
        FaceEntity emojEentry;
        try {
            if (emojis != null && emojis.size() > 0) {
                emojis.clear();
            }
            if (emojiMap != null && emojiMap.size() > 0) {
                emojiMap.clear();
            }
            if (emojiLists != null && emojiLists.size() > 0) {
                emojiLists.clear();
            }
            for (String str : data) {
                String[] text = str.split(",");
                String fileName = text[0]
                        .substring(0, text[0].lastIndexOf("."));
                emojiMap.put(fileName.split("_")[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());

                if (resID != 0) {
                    emojEentry = new FaceEntity();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(fileName.split("_")[1]);
                    emojEentry.setFaceName(getFaceUrl(fileName));
                    emojis.add(emojEentry);
                }
            }
            int pageCount = (int) Math.ceil(emojis.size() / pageSize + 0.1);
            for (int i = 0; i < pageCount; i++) {
                emojiLists.add(getData(i));
            }
            // emojiLists.add(getData(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFaceUrl(String fileName) {
        String[] name = fileName.split("_");
        return "<img src=\"http://www.yangche51.com/Kindeditor/plugins/emoticons/images/"
                + name[1] + ".gif\" alt=\"picture\"/>";
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private List<FaceEntity> getData(int page) {
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        if (endIndex > emojis.size()) {
            endIndex = emojis.size();
        }

        // 不这么写，会在viewpager加载中报集合操作异常
        List<FaceEntity> list = new ArrayList<FaceEntity>();
        list.addAll(emojis.subList(startIndex, endIndex));
        if (list.size() < pageSize) {
            for (int i = list.size(); i < pageSize; i++) {
                FaceEntity object = new FaceEntity();
                list.add(object);
            }
        }
        if (list.size() == pageSize) {
            FaceEntity object = new FaceEntity();
            object.setId(R.drawable.face_selector_del_icon);
            list.add(object);
        }
        return list;
    }

    /**
     * 输入的字符是否是汉字
     *
     * @param a char
     * @return boolean
     */
    public static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    // public static boolean containsChinese(String s) {
    // if (null == s || "".equals(s.trim()))
    // return false;
    // for (int i = 0; i < s.length(); i++) {
    // if (isChinese(s.charAt(i)))
    // return true;
    // }
    // return false;
    // }

    /**
     * 判断字符长度汉字为两位 字母数字为一位 然后判断22长度的时候是第几位
     *
     * @param value
     * @return
     */
    public static int findLengthByString(Spanned value) {
        char[] b = value.toString().toCharArray();
        int length = 0;
        int position = -1;
        for (int i = 0; i < b.length; i++) {
            // Log.i("char", b[i] + "-是否为汉字->" + isChinese(b[i]));
            if (isChinese(b[i])) {
                length += 2;
            } else {
                length++;
            }
            if (length > 22) {
                position = i;
                break;
            }
        }
        return position;
    }
}