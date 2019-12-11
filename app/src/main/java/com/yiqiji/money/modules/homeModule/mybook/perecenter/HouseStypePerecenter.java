package com.yiqiji.money.modules.homeModule.mybook.perecenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.activity.AddressSelctActivity;
import com.yiqiji.money.modules.homeModule.mybook.entity.AddressInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.AddressResultInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseDataInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseStypeMulty;
import com.yiqiji.money.modules.homeModule.mybook.entity.RenovationStypeInfo;
import com.yiqiji.money.modules.homeModule.mybook.util.MyBookActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypePerecenter {
    //风格
    public static final String[] sylpes = new String[]{"现代", "美式", "简约", "简欧", "欧式", "宜家", "中式", "田园", "地中海", "北欧", "日式", "新古典", "东南亚", "混搭"};
    //户型
    public static final String[] housesize = new String[]{"一居", "二居", "三居", "四居", "小户型", "公寓", "复式", "别墅"};
    //装修方式
    public static final String[] renovationstype = new String[]{"清包", "半包", "全包"};
    //装修方式描述
    public static final String[] renovationstypecontent = new String[]{"预算最低,只包施工", "大众首选,包施工和辅料", "省时省力,包施工和所有材料"};

    /**
     * 获取户型和装修方式的list
     *
     * @param type
     * @return
     */
    public static List<HouseStypeMulty> getHouseStypeMulty(int type, String content) {
        List<HouseStypeMulty> houseStypeMulties = new ArrayList<>();
        if (type == 0) {
            for (int i = 0; i < housesize.length; i++) {
                HouseStypeMulty houseStypeMulty = new HouseStypeMulty();
                HouseStypeInfo houseStypeInfo = new HouseStypeInfo();
                houseStypeInfo.setTitle(housesize[i]);
                if (housesize[i].equals(content)) {
                    houseStypeInfo.setSelect(true);
                }
                houseStypeMulty.setItemType(100);
                houseStypeMulty.setData(houseStypeInfo);
                houseStypeMulties.add(houseStypeMulty);
            }
        } else {
            for (int i = 0; i < renovationstype.length; i++) {
                HouseStypeMulty houseStypeMulty = new HouseStypeMulty();
                HouseStypeInfo houseStypeInfo = new HouseStypeInfo();
                houseStypeInfo.setTitle(renovationstype[i]);
                if (renovationstype[i].equals(content)) {
                    houseStypeInfo.setSelect(true);
                }
                houseStypeInfo.setContent(renovationstypecontent[i]);
                houseStypeMulty.setItemType(101);
                houseStypeMulty.setData(houseStypeInfo);
                houseStypeMulties.add(houseStypeMulty);
            }
        }

        return houseStypeMulties;
    }

    /**
     * 获取装修风格list
     *
     * @return
     */
    public static List<RenovationStypeInfo> getRenovationStypeInfo() {
        List<RenovationStypeInfo> stypeInfos = new ArrayList<>();
        for (int i = 0; i < sylpes.length; i++) {
            RenovationStypeInfo stypeInfo = new RenovationStypeInfo();
            stypeInfo.setContent(sylpes[i]);
            stypeInfos.add(stypeInfo);
        }

        return stypeInfos;
    }

    public static List<HouseStypeMulty> getCheckAfterList(List<HouseStypeMulty> houseStypeMulties, int position) {
        if (houseStypeMulties != null) {
            HouseStypeMulty houseStypeMulty = houseStypeMulties.get(position);
            HouseStypeInfo houseStypeInfo = (HouseStypeInfo) houseStypeMulties.get(position).getData();
            boolean isSelect = houseStypeInfo.isSelect();
            houseStypeInfo.setSelect(!isSelect);
            houseStypeMulty.setData(houseStypeInfo);
            houseStypeMulties.set(position, houseStypeMulty);
            return houseStypeMulties;
        }

        return null;
    }

    public static void getIsCheck(Context context, HouseStypeMulty houseStypeMulty) {
        if (houseStypeMulty != null) {
            HouseStypeInfo houseStypeInfo = (HouseStypeInfo) houseStypeMulty.getData();
            boolean isSelect = houseStypeInfo.isSelect();
            if (isSelect && context != null) {
                Intent intent = new Intent(context, AddressSelctActivity.class);
                intent.putExtra("content", houseStypeInfo.getTitle());
                if (houseStypeMulty.getItemType() == 100) {
                    ((Activity) context).setResult(MyBookActivityUtil.HOUSESTYPEACTIVITY_RESULTCODE, intent);
                } else {
                    ((Activity) context).setResult(MyBookActivityUtil.RENOVATIONSTYPEACTIVITY_RESULTCODE, intent);
                }
                ((Activity) context).finish();
            }

        }

    }

    /**
     * 获取提交地址对象
     *
     * @param mAccountbookid//账本id
     * @param mhouseid/房子id
     * @param location_content//所在城市
     * @param housestype_content//户型
     * @param decorationstyle_content//装修方式
     * @param style_content//装修风格
     * @param vallige_name//小区名字
     * @param house_area//房子面积
     * @param company_name/装修公司
     * @return
     */
    public static AddressInfo getAddressInfo(String mAccountbookid, String mhouseid, String location_content, String housestype_content, String decorationstyle_content, String style_content
            , String vallige_name, String house_area, String company_name) {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(mAccountbookid);//账本id
        addressInfo.setHouseid(TextUtils.isEmpty(mhouseid) ? "" : mhouseid);//房子id
        addressInfo.setHousecity(TextUtils.isEmpty(location_content) ? "" : location_content);//所在城市
        addressInfo.setHousename(TextUtils.isEmpty(vallige_name) ? "" : vallige_name);//小区名字
        addressInfo.setHousesquare(TextUtils.isEmpty(house_area) ? "" : house_area);//房子面积
        addressInfo.setHousestyle(TextUtils.isEmpty(housestype_content) ? "" : housestype_content);//户型
        addressInfo.setHousetype(TextUtils.isEmpty(style_content) ? "" : style_content);//装修风格
        addressInfo.setHouseway(TextUtils.isEmpty(decorationstyle_content) ? "" : decorationstyle_content);//装修方式
        addressInfo.setCompany(TextUtils.isEmpty(company_name) ? "" : company_name);//装修公司
        return addressInfo;
    }

    /**
     * 提交房屋信息
     *
     * @param addressInfo
     * @param houseDataInfo//已存在的房屋信息
     * @param viewCallBack
     */
    public static void subMitData(AddressInfo addressInfo, HouseDataInfo houseDataInfo, final ViewCallBack viewCallBack) {
        if (houseDataInfo != null) {
            boolean isCansubmit = addressInfo.getHousecity().equals(houseDataInfo.getHousecity()) ? false : true;
            boolean isCansubmit1 = addressInfo.getHousename().equals(houseDataInfo.getHousename()) ? false : true;
            boolean isCansubmit2 = addressInfo.getHousesquare().equals(houseDataInfo.getHousesquare()) ? false : true;
            boolean isCansubmit3 = addressInfo.getHousestyle().equals(houseDataInfo.getHousestyle()) ? false : true;
            boolean isCansubmit4 = addressInfo.getHousetype().equals(houseDataInfo.getHousetype()) ? false : true;
            boolean isCansubmit5 = addressInfo.getHouseway().equals(houseDataInfo.getHouseway()) ? false : true;
            boolean isCansubmit6 = addressInfo.getCompany().equals(houseDataInfo.getCompany()) ? false : true;
            if (!isCansubmit && !isCansubmit1 && !isCansubmit2 && !isCansubmit3 && !isCansubmit4 && !isCansubmit5 && !isCansubmit6) {//没有做修改
                try {
                    viewCallBack.onSuccess(new AddressResultInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
        }

        HashMap<String, String> stringHashMap = StringUtils.getParamasNoSign("id", addressInfo.getId(), "houseid", addressInfo.getHouseid(), "housecity", addressInfo.getHousecity(), "housename",
                addressInfo.getHousename(), "housesquare", addressInfo.getHousesquare(),
                "housestyle", addressInfo.getHousestyle(), "housetype", addressInfo.getHousetype(), "houseway", addressInfo.getHouseway(), "company", addressInfo.getCompany());
        CommonFacade.getInstance().exec(Constants.HOUSE_ADDRESS, stringHashMap, new ViewCallBack<AddressResultInfo>() {
            @Override
            public void onStart() {
                super.onStart();
                viewCallBack.onStart();
            }

            @Override
            public void onSuccess(AddressResultInfo o) throws Exception {
                super.onSuccess(o);
                if (o.getCode() == 0) {
                    viewCallBack.onSuccess(o);
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
