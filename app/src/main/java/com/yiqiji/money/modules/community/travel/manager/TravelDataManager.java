package com.yiqiji.money.modules.community.travel.manager;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.community.travel.model.HotDestinationsInfo;
import com.yiqiji.money.modules.community.travel.model.RaidersDataItemBean;
import com.yiqiji.money.modules.community.travel.model.RaidersInfo;
import com.yiqiji.money.modules.community.travel.model.TravelMainListModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by leichi on 2017/8/2.
 */

public class TravelDataManager {
    public static final int guidle = 1;//指南
    public static final int route = 2;//了女友路线
    public static final int point = 3;//交通指南
    public static final int food = 4;//舌尖美食
    public static final int tips = 5;//景点推荐

    /**
     * 发现其他数据
     *
     * @param viewCallBack
     */
    public static void getTravleMainListData(final ViewCallBack<TravelMainListModel> viewCallBack) {
        HashMap<String, String> hashMap = new HashMap<>();
        CommonFacade.getInstance().exec(Constants.TRAVEL_INDEX, hashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object object) throws Exception {
                JSONObject jsonObject = (JSONObject) object;
                if (jsonObject.getString("code").equals("0")) {
                    TravelMainListModel travelMainListModel = GsonUtil.GsonToBean(jsonObject.getString("data"), TravelMainListModel.class);
                    viewCallBack.onSuccess(travelMainListModel);
                } else {
                    SimpleMsg simpleMsg = new SimpleMsg(-1, "请求失败");
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

    public static void getDestinationData(String city, final ViewCallBack<HotDestinationsInfo.DataBean> viewCallBack) {
        HashMap<String, String> stringHashMap = StringUtils.getParamas("city", city);
        CommonFacade.getInstance().exec(Constants.TRAVEL_PLACE, stringHashMap, new ViewCallBack<HotDestinationsInfo>() {
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

            @Override
            public void onSuccess(HotDestinationsInfo o) throws Exception {
                super.onSuccess(o);
                if (o.getCode() == 0) {
                    HotDestinationsInfo.DataBean dataBean = o.getData();
                    viewCallBack.onSuccess(dataBean);
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }
        });
    }

    public static void getRaidersInfo(String city, final int position, final ViewCallBack viewCallBack) {
        HashMap<String, String> stringHashMap = StringUtils.getParamas("city", city);
        CommonFacade.getInstance().exec(Constants.TRAVEL_STRATEGY, stringHashMap, new ViewCallBack<RaidersInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(RaidersInfo o) throws Exception {
                super.onSuccess(o);
                if (o.getCode() == 0) {
                    List<RaidersDataItemBean> raidersDataItemBeen = o.getData().getGuide();
                    switch (position + 1) {
                        case guidle:
                            raidersDataItemBeen = o.getData().getGuide();
                            break;
                        case route:
                            raidersDataItemBeen = o.getData().getRoute();
                            break;
                        case point:
                            raidersDataItemBeen = o.getData().getPoint();
                            break;
                        case food:
                            raidersDataItemBeen = o.getData().getFood();
                            break;
                        case tips:
                            raidersDataItemBeen = o.getData().getTips();
                            break;
                    }
                    viewCallBack.onSuccess(raidersDataItemBeen);
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

