package com.yiqiji.money.modules.common.entity;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/3/20.
 * address huang.weishui@71dai.com
 */
public class Suggestion {


    /**
     * code : 0
     * msg : ok
     * data : [{"name":"上海银行","uid":"","city":"","district":"","business":"","cityid":"0"},{"name":"上海滩","uid":"","city":"","district":"","business":"","cityid":"0"},{"name":"上海市","location":{"lat":31.236305,"lng":121.480237},"uid":"8eb697c2bdf8bae8b6f42fe7","city":"上海市","district":"","business":"","cityid":"289"},{"name":"上海虹桥站","location":{"lat":31.200547,"lng":121.326997},"uid":"6763c15f0a0bf9e96140a9c8","city":"上海市","district":"闵行区","business":"","cityid":"289"},{"name":"上海南站","location":{"lat":31.159439,"lng":121.435865},"uid":"905220f3f41e706bdb131806","city":"上海市","district":"徐汇区","business":"","cityid":"289"},{"name":"上海火车站-地铁站","location":{"lat":31.255155,"lng":121.46396},"uid":"722cee03d327cb7527d2bbd9","city":"上海市","district":"闸北区","business":"","cityid":"289"},{"name":"上海长途汽车客运总站","location":{"lat":31.258332,"lng":121.460301},"uid":"199afc6cddbec7e0e856fa43","city":"上海市","district":"闸北区","business":"","cityid":"289"},{"name":"上海浦东国际机场","location":{"lat":31.157386,"lng":121.81502},"uid":"41b461a937e4a0528c1a890e","city":"上海市","district":"浦东新区","business":"","cityid":"289"},{"name":"上海站","location":{"lat":31.255923,"lng":121.462056},"uid":"5fd644188840626b102935ba","city":"上海市","district":"闸北区","business":"","cityid":"289"},{"name":"上海迪士尼度假区","location":{"lat":31.146432,"lng":121.671036},"uid":"81ad5716b2c9c7fbe8e58272","city":"上海市","district":"浦东新区","business":"","cityid":"289"}]
     */

    private int code;
    private String msg;
    /**
     * name : 上海银行
     * uid :
     * city :
     * district :
     * business :
     * cityid : 0
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String uid;
        private String city;
        private String district;
        private String business;
        private String cityid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }
    }
}
