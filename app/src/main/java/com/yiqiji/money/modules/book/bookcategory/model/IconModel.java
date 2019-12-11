package com.yiqiji.money.modules.book.bookcategory.model;

import android.content.Context;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by leichi on 2017/5/26.
 */

public class IconModel implements Serializable {
    public int index;                         //图片的下标值，目前做id用
    public int resId;                         //图片的本地资源id
    public int billtype;                      //图片对应的账本类型
    public String url;                        //图片的url
    public boolean isChoice;                  //是否被选中

    public IconModel() {

    }

    public IconModel(Context context, String url) {
        index = IconModel.UrlMapIndex(url);
        resId = getImageResId(context, index);
        this.url = url;
    }

    public String getUrl() {
        if (resId == 0) {
            return url == null ? "" : url;
        }
        return Constants.ICON_BASE_URL + "/accountbook/category/0/" + index + "@2x.png";
    }

    public static int UrlMapIndex(String url) {
        if(StringUtils.isEmpty(url)||url.contains("new")){
            return -1;
        }
        try {
            StringBuffer stringBuffer = new StringBuffer(url);
            int end = stringBuffer.indexOf("@");
            int start = stringBuffer.lastIndexOf("/");
            String index = stringBuffer.substring(start + 1, end);
            return Integer.parseInt(index);
        } catch (Exception e) {

        }
        return -1;
    }

    private int getImageResId(Context context, int index) {
        return context.getResources().getIdentifier("icon_" + index, "drawable", context.getPackageName());
    }
}
