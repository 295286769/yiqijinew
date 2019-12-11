package com.yiqiji.money.modules.common.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public class Location {


    /**
     * code : 0
     * data : {"location":{"lng":121.511919,"lat":31.348212883936},"formatted_address":"上海市杨浦区国权北路","business":"新江湾城","addressComponent":{"country":"中国","country_code":0,"province":"上海市","city":"上海市","district":"杨浦区","adcode":"310110","street":"国权北路","street_number":"","direction":"","distance":""},"pois":[{"addr":"国权北路1600号","cp":"","direction":"内","distance":"0","name":"新江湾城","poiType":"房地产","point":{"x":121.51108241531,"y":31.34629388538},"tag":"房地产","tel":"","uid":"6a7fbeac0212fe01d2f49efd","zip":""},{"addr":"上海市杨浦区国权北路1600号","cp":" ","direction":"北","distance":"146","name":"湾谷科技园","poiType":"公司企业","point":{"x":121.51171122917,"y":31.347095723113},"tag":"公司企业;园区","tel":"","uid":"a34d7e1b3881120b0e0a705d","zip":""},{"addr":"上海市杨浦区淞行路","cp":" ","direction":"东","distance":"177","name":"A6","poiType":"房地产","point":{"x":121.51037275396,"y":31.347882134184},"tag":"房地产;写字楼","tel":"","uid":"4ee139222935f56929fc482b","zip":""},{"addr":"上海市杨浦区国帆路","cp":" ","direction":"西","distance":"301","name":"复旦大学(江湾新校区)-4号门","poiType":"出入口","point":{"x":121.51457682375,"y":31.347781905692},"tag":"出入口;门","tel":"","uid":"31c7df1c7d168c78902ca3df","zip":""},{"addr":" ","cp":" ","direction":"东南","distance":"628","name":"淞发路(地铁站)","poiType":" ","point":{"x":121.50713885412,"y":31.350796423196},"tag":" ","tel":"","uid":"e3b6b71440a0a7beadee7afa","zip":""},{"addr":"上海市杨浦区淞沪路2005号(近国帆路)","cp":" ","direction":"北","distance":"799","name":"复旦大学(江湾新校区)","poiType":"教育培训","point":{"x":121.5132652977,"y":31.342161227601},"tag":"教育培训;高等院校","tel":"","uid":"4e6c2fbe3ee051c66e20b99b","zip":""},{"addr":"华浜二村20～78号","cp":" ","direction":"东南","distance":"771","name":"华浜二村","poiType":"房地产","point":{"x":121.50647410805,"y":31.351898894998},"tag":"房地产;住宅区","tel":"","uid":"34f68ab073a1960b8f55ec7f","zip":""},{"addr":"上海市逸仙路3001号","cp":" ","direction":"东","distance":"835","name":"何家湾站","poiType":"交通设施","point":{"x":121.50496495479,"y":31.345792733292},"tag":"交通设施;火车站","tel":"","uid":"c90af14acb2c9890932ca332","zip":""},{"addr":"逸仙路3001号","cp":" ","direction":"东","distance":"842","name":"上海铁路局南翔直属站何家湾车站","poiType":"政府机构","point":{"x":121.50489309035,"y":31.345800443344},"tag":"政府机构","tel":"","uid":"626c8bcc294d6f122afc484d","zip":""},{"addr":"宝山区逸仙路3000号(近长逸路)","cp":" ","direction":"东","distance":"971","name":"上海国际工业设计中心","poiType":"公司企业","point":{"x":121.50359953041,"y":31.345954644261},"tag":"公司企业;园区","tel":"","uid":"906a7cdb9ebbd5a9132935cd","zip":""}],"poiRegions":[{"direction_desc":"内","name":"新江湾城","tag":"房地产"}],"sematic_description":"新江湾城内,湾谷科技园北146米","cityCode":289}
     */

    private int code;
    /**
     * location : {"lng":121.511919,"lat":31.348212883936}
     * formatted_address : 上海市杨浦区国权北路
     * business : 新江湾城
     * addressComponent : {"country":"中国","country_code":0,"province":"上海市","city":"上海市","district":"杨浦区","adcode":"310110","street":"国权北路","street_number":"","direction":"","distance":""}
     * pois : [{"addr":"国权北路1600号","cp":"","direction":"内","distance":"0","name":"新江湾城","poiType":"房地产","point":{"x":121.51108241531,"y":31.34629388538},"tag":"房地产","tel":"","uid":"6a7fbeac0212fe01d2f49efd","zip":""},{"addr":"上海市杨浦区国权北路1600号","cp":" ","direction":"北","distance":"146","name":"湾谷科技园","poiType":"公司企业","point":{"x":121.51171122917,"y":31.347095723113},"tag":"公司企业;园区","tel":"","uid":"a34d7e1b3881120b0e0a705d","zip":""},{"addr":"上海市杨浦区淞行路","cp":" ","direction":"东","distance":"177","name":"A6","poiType":"房地产","point":{"x":121.51037275396,"y":31.347882134184},"tag":"房地产;写字楼","tel":"","uid":"4ee139222935f56929fc482b","zip":""},{"addr":"上海市杨浦区国帆路","cp":" ","direction":"西","distance":"301","name":"复旦大学(江湾新校区)-4号门","poiType":"出入口","point":{"x":121.51457682375,"y":31.347781905692},"tag":"出入口;门","tel":"","uid":"31c7df1c7d168c78902ca3df","zip":""},{"addr":" ","cp":" ","direction":"东南","distance":"628","name":"淞发路(地铁站)","poiType":" ","point":{"x":121.50713885412,"y":31.350796423196},"tag":" ","tel":"","uid":"e3b6b71440a0a7beadee7afa","zip":""},{"addr":"上海市杨浦区淞沪路2005号(近国帆路)","cp":" ","direction":"北","distance":"799","name":"复旦大学(江湾新校区)","poiType":"教育培训","point":{"x":121.5132652977,"y":31.342161227601},"tag":"教育培训;高等院校","tel":"","uid":"4e6c2fbe3ee051c66e20b99b","zip":""},{"addr":"华浜二村20～78号","cp":" ","direction":"东南","distance":"771","name":"华浜二村","poiType":"房地产","point":{"x":121.50647410805,"y":31.351898894998},"tag":"房地产;住宅区","tel":"","uid":"34f68ab073a1960b8f55ec7f","zip":""},{"addr":"上海市逸仙路3001号","cp":" ","direction":"东","distance":"835","name":"何家湾站","poiType":"交通设施","point":{"x":121.50496495479,"y":31.345792733292},"tag":"交通设施;火车站","tel":"","uid":"c90af14acb2c9890932ca332","zip":""},{"addr":"逸仙路3001号","cp":" ","direction":"东","distance":"842","name":"上海铁路局南翔直属站何家湾车站","poiType":"政府机构","point":{"x":121.50489309035,"y":31.345800443344},"tag":"政府机构","tel":"","uid":"626c8bcc294d6f122afc484d","zip":""},{"addr":"宝山区逸仙路3000号(近长逸路)","cp":" ","direction":"东","distance":"971","name":"上海国际工业设计中心","poiType":"公司企业","point":{"x":121.50359953041,"y":31.345954644261},"tag":"公司企业;园区","tel":"","uid":"906a7cdb9ebbd5a9132935cd","zip":""}]
     * poiRegions : [{"direction_desc":"内","name":"新江湾城","tag":"房地产"}]
     * sematic_description : 新江湾城内,湾谷科技园北146米
     * cityCode : 289
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lng : 121.511919
         * lat : 31.348212883936
         */

        private LocationBean location;
        private String formatted_address;
        private String business;
        /**
         * country : 中国
         * country_code : 0
         * province : 上海市
         * city : 上海市
         * district : 杨浦区
         * adcode : 310110
         * street : 国权北路
         * street_number :
         * direction :
         * distance :
         */

        private AddressComponentBean addressComponent;
        private String sematic_description;
        private int cityCode;
        /**
         * addr : 国权北路1600号
         * cp :
         * direction : 内
         * distance : 0
         * name : 新江湾城
         * poiType : 房地产
         * point : {"x":121.51108241531,"y":31.34629388538}
         * tag : 房地产
         * tel :
         * uid : 6a7fbeac0212fe01d2f49efd
         * zip :
         */

        private List<PoisBean> pois;
        /**
         * direction_desc : 内
         * name : 新江湾城
         * tag : 房地产
         */

        private List<PoiRegionsBean> poiRegions;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public AddressComponentBean getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentBean addressComponent) {
            this.addressComponent = addressComponent;
        }

        public String getSematic_description() {
            return sematic_description;
        }

        public void setSematic_description(String sematic_description) {
            this.sematic_description = sematic_description;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public List<PoisBean> getPois() {
            return pois;
        }

        public void setPois(List<PoisBean> pois) {
            this.pois = pois;
        }

        public List<PoiRegionsBean> getPoiRegions() {
            return poiRegions;
        }

        public void setPoiRegions(List<PoiRegionsBean> poiRegions) {
            this.poiRegions = poiRegions;
        }

        public static class LocationBean {
            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class AddressComponentBean {
            private String country;
            private int country_code;
            private String province;
            private String city;
            private String district;
            private String adcode;
            private String street;
            private String street_number;
            private String direction;
            private String distance;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public int getCountry_code() {
                return country_code;
            }

            public void setCountry_code(int country_code) {
                this.country_code = country_code;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
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

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }

        public static class PoisBean {
            private String addr;
            private String cp;
            private String direction;
            private String distance;
            private String name;
            private String poiType;
            /**
             * x : 121.51108241531
             * y : 31.34629388538
             */

            private PointBean point;
            private String tag;
            private String tel;
            private String uid;
            private String zip;

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getCp() {
                return cp;
            }

            public void setCp(String cp) {
                this.cp = cp;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPoiType() {
                return poiType;
            }

            public void setPoiType(String poiType) {
                this.poiType = poiType;
            }

            public PointBean getPoint() {
                return point;
            }

            public void setPoint(PointBean point) {
                this.point = point;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public static class PointBean {
                private double x;
                private double y;

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }
            }
        }

        public static class PoiRegionsBean {
            private String direction_desc;
            private String name;
            private String tag;

            public String getDirection_desc() {
                return direction_desc;
            }

            public void setDirection_desc(String direction_desc) {
                this.direction_desc = direction_desc;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
