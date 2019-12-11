package com.yiqiji.money.modules.property.entity;

import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dansakai on 2017/3/7.
 * 资产分类组合实体
 */

public class PropertyEntity {
    public final static int TYPE_FIRST = 0;// 第一级 资产大类
    public final static int TYPE_SECOND = 1;// 第二级 资产小类

    private List<PropertyItemEntity> propertyItemEntities;//资产组合实体

    private String compleItem;//已完结项目
    private List<PropertyItemEntity> comEntitise;

    public List<PropertyItemEntity> getComEntitise() {
        return comEntitise;
    }

    public void setComEntitise(List<PropertyItemEntity> comEntitise) {
        this.comEntitise = comEntitise;
    }

    public String getCompleItem() {
        return compleItem;
    }

    public void setCompleItem(String compleItem) {
        this.compleItem = compleItem;
    }

    public List<PropertyItemEntity> getPropertyItemEntities() {
        return propertyItemEntities;
    }

    public void setPropertyItemEntities(List<PropertyItemEntity> propertyItemEntities) {
        this.propertyItemEntities = propertyItemEntities;
    }

    public static PropertyEntity parse(String str) throws Exception {
        PropertyEntity propertyEntity = null;
        if (!StringUtils.isEmpty(str)) {
            propertyEntity = new PropertyEntity();


            List<PropertyItemEntity> entities = new ArrayList<>();
            List<PropertyItemEntity> compEntities = new ArrayList<>();
            PropertyItemEntity entity = null;
            JSONArray array = new JSONArray(str);
            JSONObject obj = null;
            JSONArray arr = null;

            if (array != null && array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    entity = new PropertyItemEntity();
                    obj = array.optJSONObject(i);
                    entity.setType(TYPE_FIRST);
                    String categoryid = obj.optString("categoryid");
                    entity.setCategoryid(categoryid);
                    entity.setTotalamount(StringUtils.moneySplitComma(obj.optString("totalamount")));
                    entity.setCategory(obj.optString("categoryname"));
                    arr = new JSONArray(obj.optString("list"));
                    if (arr != null && arr.length() > 0) {
                        entity.setChildNm(arr.length());
                    }
                    entity.setCategoryname(obj.optString("categoryname") + "(" + arr.length() + ")");
                    if (!"99".equals(categoryid)) {
                        if (arr != null && arr.length() > 0) {
                            entities.add(entity);
                        }
                    }

                    if (arr != null && arr.length() > 0) {
                        for (int j = 0; j < arr.length(); j++) {
                            obj = arr.optJSONObject(j);
                            entity = new PropertyItemEntity();
                            entity.setType(TYPE_SECOND);
                            entity.setCategoryid(categoryid);
                            entity.setItemtype(obj.optString("itemtype"));

                            entity.setMarktext(obj.optString("marktext"));
                            entity.setAssetamount(StringUtils.moneySplitComma(obj.optString("assetamount")));
                            entity.setTodaydiff(obj.optString("todaydiff"));
                            entity.setAssetctime(obj.optString("assetctime"));
                            entity.setItemcatename(obj.optString("itemcatename"));
                            entity.setProfitamount(obj.optString("profitamount"));
                            entity.setAssetid(obj.optString("assetid"));
                            int itemType = Integer.parseInt(obj.optString("itemtype"));
                            if (itemType == 13 || itemType == 14 || itemType == 15 || itemType == 16 || itemType == 18) {//投资理财
                                if (!"99".equals(categoryid)) {
                                    entity.setTodaydiff(obj.optString("profitamount"));
                                }
                                //获取格式化对象
                                NumberFormat nt = NumberFormat.getPercentInstance();
                                //设置百分数精确度2即保留两位小数
                                nt.setMinimumFractionDigits(2);
                                if ("14".equals(obj.optString("itemtype"))) {//股票
                                    entity.setProfitamount(nt.format(Double.parseDouble(obj.optString("profitratio"))));
                                    JSONObject jo_attach = new JSONObject(obj.optString("attach"));
                                    entity.setStockcode(jo_attach.optString("stockcode"));
                                    entity.setStocknum(jo_attach.optString("stocknum"));
                                    entity.setCurrentprice(jo_attach.optString("currentprice"));

                                } else if ("13".equals(obj.optString("itemtype"))) {//基金
                                    entity.setProfitamount(nt.format(Double.parseDouble(obj.optString("profitratio"))));
                                } else {
                                    JSONObject jo_attach = new JSONObject(obj.optString("attach"));
                                    int deadline = Integer.parseInt(jo_attach.optString("deadline"));
                                    String[] interestdate = DateUtil.formatDate(Long.parseLong(jo_attach.optString("interestdate"))).split("-");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Integer.parseInt(interestdate[0]), Integer.parseInt(interestdate[1])-1, Integer.parseInt(interestdate[2]));
                                    calendar.add(Calendar.DAY_OF_MONTH, +deadline);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date begin = sdf.parse(DateUtil.getTodayDate());
                                    Date end = sdf.parse(sdf.format(calendar.getTime()));
                                    long day = (begin.getTime() - end.getTime()) / (24 * 60 * 60 * 1000);
                                    if (day == 0) {
                                        entity.setProfitamount("今日到期");
                                    } else if (day > 0) {
                                        entity.setProfitamount("到期超过" + day + "天");
                                    } else {
                                        entity.setProfitamount(sdf.format(calendar.getTime()) + "到期");
                                    }
                                }
                            } else if (itemType == 21 || itemType == 19 || itemType == 20) {//自定义借款
                                if (!StringUtils.isEmpty(obj.optString("attach"))) {
                                    JSONObject jo_att = new JSONObject(obj.optString("attach"));
                                    PropertyItemEntity.Attach  attach=new PropertyItemEntity.Attach();
                                    attach.loantype=jo_att.optString("loantype");
                                    attach.remark=jo_att.optString("remark");
                                    entity.setAttach(attach);
                                } else {

                                }
                            }

                            if ("9".equals(obj.optString("itemtype")) || "6".equals(obj.optString("itemtype"))) {
                                JSONObject jo_bank = new JSONObject(obj.optString("bank"));
                                entity.setItemcateicon(jo_bank.optString("bankicon"));
                                entity.setItemname(jo_bank.optString("bankname"));
                                if ("6".equals(obj.optString("itemtype"))) {
                                    JSONObject jo_attach = new JSONObject(obj.optString("attach"));
                                    int billday = Integer.parseInt(jo_attach.optString("billday"));//账单日
                                    int repayday = Integer.parseInt(jo_attach.optString("repayday"));//还款日
                                    if (repayday >= billday) {//已出账
                                        String[] curTime = DateUtil.getTodayDate().split("-");
                                        entity.setAssetctime(curTime[1] + "-" + repayday + "还款");
                                    } else {
                                        int diff = billday - repayday;
                                        entity.setAssetctime("距离账单日" + diff + "天");
                                    }

                                }
                            } else {
                                entity.setItemcateicon(obj.optString("itemcateicon"));
                                entity.setItemname(obj.optString("itemname"));
                            }

                            if ("99".equals(categoryid)) {//
                                compEntities.add(entity);
                            } else {
                                entities.add(entity);
                            }
                        }
                    }

                }
            }

            if (!StringUtils.isEmptyList(entities)) {
                propertyEntity.setPropertyItemEntities(entities);
            }

            if (!StringUtils.isEmptyList(compEntities)) {
                propertyEntity.setComEntitise(compEntities);
                propertyEntity.setCompleItem("已完结(" + compEntities.size() + ")");
            }
        }

        return propertyEntity;
    }
}
