package com.yiqiji.money.modules.community.travel.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/8/3.
 * address huang.weishui@71dai.com
 */
public class RaidersSectionsBean {
    /**
     * id : 39167
     * title : 云南概览
     * description_user_ids : []
     * ctrip_attraction_ids : null
     * description : 位于中国西南边陲的云南是一块博大且神秘的土地，千年的历史，多民族多文化交融，形成了独特的彩云之南：从滇池海鸥翱翔的春城昆明，到“风花雪月”的大理名胜；从高原水城丽江、神奇的“香格里拉”，到孔雀曼舞的西双版纳；从“天下第一奇观”的石林、千姿百态的元谋土林，到世所罕见的“三江”并流，江狭水凶的虎跳峡等，都是让你爱上云南的原因。
     * travel_date : null
     * : null
     * attractions : []
     * hotels : []
     * pages : []
     * photos : []
     * items : []
     */

    private int id;
    private String title;
    private Object ctrip_attraction_ids;
    private String description;
    private String travel_date;
    @SerializedName("")
    private List<?> description_user_ids;
    private List<?> attractions;
    private List<?> hotels;
    private List<?> pages;
    private List<RaiderspPhotosInfo> photos;
    private List<?> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getCtrip_attraction_ids() {
        return ctrip_attraction_ids;
    }

    public void setCtrip_attraction_ids(Object ctrip_attraction_ids) {
        this.ctrip_attraction_ids = ctrip_attraction_ids;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public List<?> getDescription_user_ids() {
        return description_user_ids;
    }

    public void setDescription_user_ids(List<?> description_user_ids) {
        this.description_user_ids = description_user_ids;
    }

    public List<?> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<?> attractions) {
        this.attractions = attractions;
    }

    public List<?> getHotels() {
        return hotels;
    }

    public void setHotels(List<?> hotels) {
        this.hotels = hotels;
    }

    public List<?> getPages() {
        return pages;
    }

    public void setPages(List<?> pages) {
        this.pages = pages;
    }

    public List<RaiderspPhotosInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<RaiderspPhotosInfo> photos) {
        this.photos = photos;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }
}
