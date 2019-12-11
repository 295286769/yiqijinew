package com.yiqiji.money.modules.homeModule.write;

import android.content.res.Resources;
import android.widget.ImageView;

import com.yiqiji.money.modules.book.bookcategory.model.IconModel;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

/**
 * Created by leichi on 2017/5/28.
 */

public class WriteUtil {

    /**
     * @param imageView
     * @param categoryId 类别ID
     */
    public static void setImage(ImageView imageView, String categoryId, String url) {
        Resources resources=imageView.getContext().getResources();
        String defPackage=imageView.getContext().getPackageName();
        String url_native = DownUrlUtil.jsonPath + categoryId + "_s";
        String url_service = "icon_" + categoryId + "_s_3x";
        //本地图片和服务器一样， 第一个参数为ID名，第二个为资源属性是ID或者是Drawable，第三个为包名。 如果找到了，返回资源Id，如果找不到，返回0 。
        int id=0;
        if(!StringUtils.isEmpty(url)){
            id = resources.getIdentifier("icon_"+ IconModel.UrlMapIndex(url)+"", "drawable", defPackage);
        }
        if(id<=0){
            id = resources.getIdentifier(url_service, "drawable", defPackage);
        }
        if (id > 0) {
            imageView.setImageResource(id);
        } else {
            XzbUtils.displayImage(imageView, url_native, 0);
        }
    }

    /**
     * @param imageView
     * @param categoryId 类别ID
     */
    public static void setImage(ImageView imageView, String categoryId) {
        setImage(imageView,categoryId,null);
    }
}
