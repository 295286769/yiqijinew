package com.yiqiji.money.modules.myModule.manager;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.entity.LoginLaterInfo;
import com.yiqiji.money.modules.myModule.login.entity.ThirdLoginInfo;

import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/6/28.
 */

public class LoginManager {
    private static LoginManager loginManager = new LoginManager();

    private LoginManager() {

    }

    public LoginManager getInstence() {
        return loginManager;
    }


    public static void toLogin(final String mobile, String userpass, String mcode, String mtokenid, final ViewCallBack callBack) {
        userpass = StringUtils.MD5(userpass);
        userpass = StringUtils.reverse1(userpass);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("userpass", userpass);
        hashMap.put("mcode", mcode);
        hashMap.put("mtokenid", mtokenid);
        CommonFacade.getInstance().exec(Constants.LOGIN_BY_MESSAGE_CODE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
                LoginConfig.getInstance().setUserid(object.getString("uid"));
                getUserInfo(callBack);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callBack.onFinish();
            }
        });
    }

    public static void toLoginPassword(final String username, String userpass, final ViewCallBack callBack) {
        userpass = StringUtils.MD5(userpass);
        userpass = StringUtils.reverse1(userpass);
        HashMap<String, String> hashMap = StringUtils.getParamas("username", username, "userpass", userpass);
        CommonFacade.getInstance().exec(Constants.LOGIN_BY_USER_NAME_PWD, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
                getUserInfo(callBack);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callBack.onFinish();
            }
        });
    }

    public static void getUserInfo(final ViewCallBack callBack) {
        CommonFacade.getInstance().exec(Constants.GET_USER_INFO, new ViewCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo o) throws Exception {
                super.onSuccess(o);
                UserInfo userInfo = o;
                LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
                LoginConfig.getInstance().setUserName(userInfo.getUsername());
                LoginConfig.getInstance().setMobile(userInfo.getMobile());
                LoginConfig.getInstance().setUserid(userInfo.getUid());
                LoginConfig.getInstance().setUserinfojson(GsonUtil.GsonString(userInfo));
                EventBus.getDefault().post(new LoginLaterInfo());
                callBack.onSuccess(o);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

        });
    }

    public static void QQLogin(Context context, UMAuthListener umAuthListener) {

        UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.QQ, umAuthListener);
    }

    public static void WXLogin(Context context, UMAuthListener umAuthListener) {
        UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    public static void SNWBLogin(Context context, UMAuthListener umAuthListener) {
        UMShareAPI.get(context).getPlatformInfo((Activity) context, SHARE_MEDIA.SINA, umAuthListener);
    }

    public static void thirdLogin(Context context, SHARE_MEDIA share_media, UMAuthListener umAuthListener) {
        UMShareAPI.get(context).getPlatformInfo((Activity) context, share_media, umAuthListener);
    }

    public static void oauthCheck(final String openid, final String type, final ViewCallBack<ThirdLoginInfo> callBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("openid", openid);
        hashMap.put("type", type);
        CommonFacade.getInstance().exec(Constants.AUTHCHECK, hashMap, new ViewCallBack<ThirdLoginInfo>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ThirdLoginInfo o) throws Exception {
                super.onSuccess(o);
                final ThirdLoginInfo thirdLoginInfo = o;
                if (thirdLoginInfo.getUid().equals("0")) {
                    callBack.onSuccess(thirdLoginInfo);
                } else {
                    LoginConfig.getInstance().setTokenId(thirdLoginInfo.getTokenid());
                    getUserInfo(new ViewCallBack() {
                        @Override
                        public void onSuccess(Object o) throws Exception {
                            super.onSuccess(o);
                            callBack.onSuccess(thirdLoginInfo);
                        }

                        @Override
                        public void onFailed(SimpleMsg simleMsg) {
                            super.onFailed(simleMsg);
                            callBack.onFailed(simleMsg);
                        }
                    });
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                callBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    public static void oauthBoundPhone(Context context, HashMap<String, String> hashMap, final ViewCallBack viewCallBack) {

        CommonFacade.getInstance().exec(Constants.OAUTHBOUND, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                JSONObject object = (JSONObject) o;
                LoginConfig.getInstance().setTokenId(object.getString("tokenid"));
                LoginConfig.getInstance().setUserid(object.getString("uid"));
                getUserInfo(new ViewCallBack() {
                    @Override
                    public void onSuccess(Object o) throws Exception {
                        super.onSuccess(o);
                        viewCallBack.onSuccess(o);
                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        super.onFailed(simleMsg);
                        viewCallBack.onFailed(simleMsg);
                    }
                });
            }


            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                viewCallBack.onFinish();
            }
        });
    }

}
