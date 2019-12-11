package com.yiqiji.money.modules.community.discover.manager;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverOtherListModel;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverListManager {
    /**
     * 发现首页数据
     *
     * @param viewCallBack
     */
    public static void getDiscoverListData(final ViewCallBack<DiscoverListModel> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        CommonFacade.getInstance().exec(Constants.FIND_INDEX, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object object) throws Exception {
                JSONObject jsonObject = (JSONObject) object;
                if (jsonObject.getString("code").equals("0")) {
                    DiscoverListModel discoverListModel= GsonUtil.GsonToBean(jsonObject.getString("data"),DiscoverListModel.class);
                    viewCallBack.onSuccess(discoverListModel);
                }else {
                    SimpleMsg simpleMsg=new SimpleMsg(-1,"请求失败");
                    viewCallBack.onFailed(simpleMsg);
                }
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


    /**
     * 发现其他数据
     *
     * @param viewCallBack
     */
    public static void getDiscoverOtherListData(final ViewCallBack<DiscoverOtherListModel> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        CommonFacade.getInstance().exec(Constants.FIND_INDEX_OTHER, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object object) throws Exception {
                JSONObject jsonObject = (JSONObject) object;
                if (jsonObject.getString("code").equals("0")) {
                    DiscoverOtherListModel discoverOtherListModel= GsonUtil.GsonToBean(jsonObject.getString("data"),DiscoverOtherListModel.class);
                    viewCallBack.onSuccess(discoverOtherListModel);
                }else {
                    SimpleMsg simpleMsg=new SimpleMsg(-1,"请求失败");
                    viewCallBack.onFailed(simpleMsg);
                }
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
