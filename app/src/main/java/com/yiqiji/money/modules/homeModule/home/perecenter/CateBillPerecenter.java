package com.yiqiji.money.modules.homeModule.home.perecenter;

import android.content.Context;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.entity.CateBillInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/6.
 * address huang.weishui@71dai.com
 */
public class CateBillPerecenter {
    public static void getCateBill(Context context, String accountbookid, String sortType, String cateid, long trdeTime, final ViewCallBack<List<DailycostEntity>> viewCallBack) {
        Date date = new Date(trdeTime * 1000);
        long startTime = DateUtil.getStartTime(date);
        long endTime = DateUtil.getEndTime(date);
        String startTime_String = startTime + "";
        String endTime_String = endTime + "";
        boolean isExpectMoth = DateUtil.isExpectMoth(date);

        if (isExpectMoth) {
            endTime_String = (endTime - 24 * 60 * 60) + "";
        }
        if (sortType.equals("0")) {
            startTime_String = "";
            endTime_String = "";
        }
        HashMap<String, String> stringHashMap = StringUtils.getParamas("id", accountbookid, "cid", cateid, "stime", startTime_String, "etime", endTime_String, "page", "1");
        CommonFacade.getInstance().exec(Constants.GETCATEBILL, stringHashMap, new ViewCallBack<CateBillInfo>() {
            @Override
            public void onSuccess(CateBillInfo o) throws Exception {
                super.onSuccess(o);
                if (o != null) {
                    List<DailycostEntity> dailycostEntities = o.getData();
                    if (dailycostEntities == null) {
                        dailycostEntities = new ArrayList<DailycostEntity>();
                    }
                    viewCallBack.onSuccess(dailycostEntities);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }

            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                viewCallBack.onFinish();
            }
        });
    }
}
