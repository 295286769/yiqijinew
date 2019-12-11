package com.yiqiji.money.modules.homeModule.home.entity;

/**
 * Created by ${huangweishui} on 2017/3/17.
 * address huang.weishui@71dai.com
 */
public class QiniuInfoItem {
    private String bucket;// 空间名称
    private String token;
    private String host;// 访问域名

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
