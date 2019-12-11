package com.yiqiji.money.modules.property.entity;

import com.yiqiji.frame.core.config.LoginConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/9.
 * 股票基金组合实体
 */

public class SearchEntity {

    private List<FundAndStockEntity> fundEntity;//基金
    private List<FundAndStockEntity> stockEntity;//股票

    private List<FundEntity> seaLis;//搜索结果实体

    public List<FundEntity> getSeaLis() {
        return seaLis;
    }

    public void setSeaLis(List<FundEntity> seaLis) {
        this.seaLis = seaLis;
    }

    public List<FundAndStockEntity> getFundEntity() {
        return fundEntity;
    }

    public void setFundEntity(List<FundAndStockEntity> fundEntity) {
        this.fundEntity = fundEntity;
    }

    public List<FundAndStockEntity> getStockEntity() {
        return stockEntity;
    }

    public void setStockEntity(List<FundAndStockEntity> stockEntity) {
        this.stockEntity = stockEntity;
    }


    public static SearchEntity parceList(String str) throws JSONException {
        List<FundAndStockEntity> temfundEntity;//基金
        List<FundAndStockEntity> temstockEntity;//股票
        SearchEntity entity = new SearchEntity();
        FundAndStockEntity temEntity = null;
        JSONArray array = new JSONArray(str);
        JSONObject obj = null;
        if (array != null && array.length() > 0) {
            temfundEntity = new ArrayList<>();
            temstockEntity = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temEntity = new FundAndStockEntity();
                obj = array.optJSONObject(i);
                temEntity.setF_asset(obj.optString("f_asset"));
                temEntity.setF_code(obj.optString("f_code"));
                temEntity.setIsatten(false);
                temEntity.setF_symbolName(obj.optString("f_symbolName"));
                if ("0".equals(obj.optString("f_asset"))) {
                    temstockEntity.add(temEntity);
                } else {
                    temfundEntity.add(temEntity);
                }
            }
            entity.setFundEntity(temfundEntity);
            entity.setStockEntity(temstockEntity);
        }

        return entity;
    }

    /**
     * 解析自选页搜索
     *
     * @param str
     * @return
     * @throws JSONException
     */
    public static SearchEntity parceSeaList(String str) throws JSONException {
        List<FundEntity> temfundEntity;//基金
        SearchEntity entity = new SearchEntity();
        FundEntity temEntity = null;
        JSONArray array = new JSONArray(str);
        JSONObject obj = null;
        if (array != null && array.length() > 0) {
            temfundEntity = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temEntity = new FundEntity();
                obj = array.optJSONObject(i);
                temEntity.setCode(obj.optString("f_code"));
                temEntity.setName(obj.optString("f_symbolName"));
                temEntity.setCurPrice("");
                temEntity.setUpDegr("");
                temEntity.setUserId(LoginConfig.getInstance().getUserid());
                temEntity.setAttention(false);
                temfundEntity.add(temEntity);
            }
            entity.setSeaLis(temfundEntity);
        }
        return entity;
    }
}
