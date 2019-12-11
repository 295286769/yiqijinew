package com.yiqiji.money.modules.common.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.common.widget.MyViewGroup;
import com.yiqiji.money.modules.homeModule.home.entity.ShorturlInfo;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ${huangweishui} on 2017/3/13.
 * address huang.weishui@71dai.com
 */
public class ShareUtil {

    private static final int margin = 65;

    /**
     * context
     * view_parent 参照父类view
     * shareText分享平台描述
     * share_icon分享平台icon
     * share_medias分享平台
     * url分享url
     * title分享标题
     * text分享描述
     * screeWith屏幕宽度
     * hasCopy是否需要复制功能
     * hasCopy是否是邀请
     * b是否需要typeString区分邀请平台
     * id邀请账本id
     * inviteid// 邀请人id
     * memberid// 被邀请人id
     */
    public static void shareMeth(final Context context, View view_parent, final String[] shareText,
                                 final int[] share_icon, final SHARE_MEDIA[] share_medias,
                                 final String url, final String title, final String text, int screeWith,
                                 final boolean hasCopy, final String imageUrl,final int image_Id) {

        View view = LayoutInflater.from(context).inflate(R.layout.share_layout_item, null);
        MyViewGroup share_viewgroup = (MyViewGroup) view.findViewById(R.id.share_viewgroup);
        Button cancell = (Button) view.findViewById(R.id.cancell);
        final MyPopuwindows myPopuwindows = new MyPopuwindows(context, view);
        for (int i = 0; i < shareText.length; i++) {
            View view_item = LayoutInflater.from(context).inflate(R.layout.hare_layou_item_item, null);
            CircleImageView share_image = (CircleImageView) view_item.findViewById(R.id.share_image);
            final TextView share_text = (TextView) view_item.findViewById(R.id.share_text);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screeWith - UIHelper.Dp2Px(context, margin)) / 3;
            layoutParams.gravity = Gravity.CENTER;
            view_item.setLayoutParams(layoutParams);
            share_image.setImageResource(share_icon[i]);
            share_text.setText(shareText[i]);
            share_viewgroup.addView(view_item);
            view_item.setTag(i);
            view_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = Integer.parseInt(v.getTag().toString());
                    if (TextUtils.isEmpty(url)) {
                        return;
                    }
                    if (hasCopy) {//统计有复制
                        if (position < shareText.length - 1) {
                            int imageid = R.drawable.share_image;
//                            setUrl(context, url, title, text, share_medias[position], imageid);
                            UMImage image = null;
                            if (StringUtils.isEmpty(imageUrl)) {
                                if (image_Id != 0) {
                                    image = new UMImage(context, image_Id);// 资源文件
                                } else {
                                    image = new UMImage(context, imageid);// 资源文件
                                }

                            } else {
                                image = new UMImage(context, imageUrl);
                            }

                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);//标题
                            web.setThumb(image);  //缩略图
                            web.setDescription(text);
                            new ShareAction((Activity) context).withMedia(web)
                                    .setPlatform(share_medias[position])
                                    .setCallback(UnListenerHelper.getUMShareListener(context)).share();


                        } else {
                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(url);
                        }
                    } else {
                        int imageid = R.drawable.icon;
                        setUrl(context, url, title, text, share_medias[position], imageid,imageUrl);
//                        UMImage image = new UMImage(context, R.drawable.icon);// 资源文件
//
//                        UMWeb web = new UMWeb(url);
//                        web.setTitle(title);//标题
//                        web.setThumb(image);  //缩略图
//                        web.setDescription(text);
//                        new ShareAction((Activity) context).withMedia(web)
//                                .setPlatform(share_medias[position])
//                                .setCallback(UnListenerHelper.getUMShareListener(context)).share();


                    }


                    myPopuwindows.dismiss();
                }
            });
            cancell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPopuwindows.dismiss();
                }
            });
        }

//        myPopuwindows.setWithAndHeight();
        myPopuwindows.showAtLocation(view_parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 邀请分享
     * context
     * view_parent 参照父类view
     * shareText分享平台描述
     * share_icon分享平台icon
     * share_medias分享平台
     * types
     * title分享标题
     * text分享描述
     * screeWith屏幕宽度
     * b是否需要typeString区分邀请平台
     * id邀请账本id
     * inviteid// 邀请人id
     * memberid// 被邀请人id
     */
    public static void shareMethInviteid(final Context context, View view_parent, final String[] shareText,
                                         final int[] share_icon, final SHARE_MEDIA[] share_medias, final String[] types,
                                         final String title, final String text, int screeWith,
                                         final boolean isNeedTypeString, final String id,
                                         final String inviteid, final String memberid,final String imageUrl) {

        View view = LayoutInflater.from(context).inflate(R.layout.share_layout_item, null);
        MyViewGroup share_viewgroup = (MyViewGroup) view.findViewById(R.id.share_viewgroup);
        Button cancell = (Button) view.findViewById(R.id.cancell);
        final MyPopuwindows myPopuwindows = new MyPopuwindows(context, view);
        for (int i = 0; i < shareText.length; i++) {
            View view_item = LayoutInflater.from(context).inflate(R.layout.hare_layou_item_item, null);
            CircleImageView share_image = (CircleImageView) view_item.findViewById(R.id.share_image);
            final TextView share_text = (TextView) view_item.findViewById(R.id.share_text);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screeWith - UIHelper.Dp2Px(context, margin)) / 3;
            layoutParams.gravity = Gravity.CENTER;
            view_item.setLayoutParams(layoutParams);
            share_image.setImageResource(share_icon[i]);
            share_text.setText(shareText[i]);
            share_viewgroup.addView(view_item);
            view_item.setTag(i);
            view_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String thisText = text;
                    int position = Integer.parseInt(v.getTag().toString());
                    if (share_medias[position].equals(SHARE_MEDIA.SINA)) {
                        thisText = title + text;
                    }
                    String typeString = types[position];
                    String shareUrl = initUrl(isNeedTypeString, id, inviteid, memberid, typeString);
                    if (TextUtils.isEmpty(shareUrl)) {
                        return;
                    }
                    int image_id = R.drawable.icon;
                    setUrl(context, shareUrl, title, thisText, share_medias[position], image_id,imageUrl);
                    myPopuwindows.dismiss();
                }
            });
            cancell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myPopuwindows.dismiss();
                }
            });
        }

//        myPopuwindows.setWithAndHeight();
        myPopuwindows.showAtLocation(view_parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * b是否需要typeString区分邀请平台
     * <p/>
     * id邀请账本id
     *
     * @param inviteid
     * @param memberid
     * @param typeString
     * @return
     */

    private static String initUrl(boolean isNeedTypeString, String id, String inviteid, String memberid, String typeString) {
        long time = new Date().getTime() / 1000;
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String sign = null;
        String url = null;

        if (isNeedTypeString) {
            String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), id, inviteid, memberid, time + ""};
            String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime"};

            sign = StringUtils.setSign(valus, params);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + id + "&inviteid="
                    + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&sign=" + sign;
            url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        } else {
            String[] valusStrings = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), id, inviteid, memberid, time + "", typeString};
            String[] paramsStrings = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime", "type"};

            sign = StringUtils.setSign(valusStrings, paramsStrings);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + id + "&inviteid="
                    + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&type=" + typeString + "&sign=" + sign;
            url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        }
        return url;
    }

    public static String initUrl(String url, String... param) {
        String urlShare = "";
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
        String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine"};
        String sign = StringUtils.setSign(param);
        String parama = StringUtils.getUrlparam(param);
        urlShare = Constants.BASE_URL + url + "?" + parama + "&sign=" + sign;
        return urlShare;
    }

    public static void setUrl(final Context context, final String url, final String title, final String text, final SHARE_MEDIA shareMedias, final int imageid,final String imageUrl) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", url);
        CommonFacade.getInstance().exec(Constants.GENSHORTURL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                ((BaseActivity) context).showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                ShorturlInfo info = GsonUtil.GsonToBean(o.toString(), ShorturlInfo.class);
                ShorturlInfo.UrlInfo urlInfo = info.getData();
                String share_url = urlInfo.getShorturl();
                UMImage image = null;
                if (StringUtils.isEmpty(imageUrl)) {
                    image = new UMImage(context, imageid);// 资源文件
                } else {
                    image = new UMImage(context, imageUrl);
                }
                UMWeb web = new UMWeb(share_url);
                web.setTitle(title);//标题

                web.setDescription(text);
                web.setThumb(image);  //缩略图
                new ShareAction((Activity) context).withMedia(web)
                        .setPlatform(shareMedias)
                        .setCallback(UnListenerHelper.getUMShareListener(context)).share();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((BaseActivity) context).showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) context).dismissDialog();
            }
        });

    }

    public static void setUrlList(final Context context, String url, final String title, final String text, final SHARE_MEDIA[] shareMedias, final int imageid) {
        url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", url);
        CommonFacade.getInstance().exec(Constants.GENSHORTURL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                ((BaseActivity) context).showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                ShorturlInfo info = GsonUtil.GsonToBean(o.toString(), ShorturlInfo.class);
                ShorturlInfo.UrlInfo urlInfo = info.getData();
                final String share_url = urlInfo.getShorturl();


                new ShareAction((Activity) context).setDisplayList(shareMedias
                ).setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        toShareForPlatform(context, share_media, imageid, share_url, title, text);
                    }
                }).open();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((BaseActivity) context).dismissDialog();
            }
        });
    }


    public static void openShare(final Context c, final SHARE_MEDIA[] shareMedias, final int imageid, final String share_url, final String title, final String text) {
        new ShareAction((Activity) c).setDisplayList(shareMedias
        ).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                toShareForPlatform(c, share_media, imageid, share_url, title, text);
            }
        }).open();
    }

    public static void toShareForPlatform(Context c, SHARE_MEDIA share_media, final int imageid, String share_url, String title, String text) {
        switch (share_media) {
            case SINA:
                text = title + text;
                break;
            default:
                break;
        }
        UMImage image = new UMImage(c, imageid);// 资源文件
        UMWeb web = new UMWeb(share_url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(text);
        new ShareAction((Activity) c).withMedia(web)
                .setCallback(UnListenerHelper.getUMShareListener(c)).setPlatform(share_media).share();
    }
}
