package com.yiqiji.money.modules.homeModule.facade;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by dansakai on 2017/4/17.
 */

public class HomeFacade {
    public static void addMember(int id, String name , final ViewCallBack<Boolean> viewCallBack){
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("id", id);
        hashMap.put("visitcard", name);
        CommonFacade.getInstance().exec(Constants.ADD_MEMBER, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(Object o)throws Exception {
                super.onSuccess(o);
                AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                EventBus.getDefault().post(addMemberResponse);
                viewCallBack.onSuccess(true);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                dismissDialog();
            }
        });


    }
}
