package com.yiqiji.money.modules.myModule.manager;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by ${huangweishui} on 2017/6/29.
 * address huang.weishui@71dai.com
 */
public class ThirdLoginListenerPecenter implements UMAuthListener {


    private ThirdLoginBaseMessaePerecenter thirdLoginMessaePerecenter;

    public void ThirdLoginBaseMessaePerecenter(ThirdLoginBaseMessaePerecenter thirdLoginMessaePerecenter) {
        this.thirdLoginMessaePerecenter = thirdLoginMessaePerecenter;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        String openid = "";

        String profile_image_url = "";
        String screen_name = "";
        String share_media_name = "";
        if (share_media.equals(SHARE_MEDIA.QQ)) {
            openid = map.get("unionid");

            profile_image_url = map.get("profile_image_url");
            screen_name = map.get("screen_name");
            share_media_name = "qq";
//            oauthCheck(openid, "qq");

        } else if (share_media.equals(SHARE_MEDIA.SINA)) {
            openid = map.get("id");
            profile_image_url = map.get("profile_image_url");
            screen_name = map.get("screen_name");
            share_media_name = "wb";
//             oauthCheck(openid, "wb");

        } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
            openid = map.get("unionid");
            profile_image_url = map.get("profile_image_url");
            screen_name = map.get("screen_name");
            share_media_name = "wx";
//             oauthCheck(openid, "wx");

        }

        thirdLoginMessaePerecenter.thirdLoginInfo(openid, profile_image_url, screen_name, share_media_name);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {

    }


}
