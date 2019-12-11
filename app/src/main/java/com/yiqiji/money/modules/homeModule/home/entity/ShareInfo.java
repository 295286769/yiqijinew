package com.yiqiji.money.modules.homeModule.home.entity;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by ${huangweishui} on 2017/5/24.
 * address huang.weishui@71dai.com
 */
public class ShareInfo {
    private int billid;
    private String shareContent;
    private SHARE_MEDIA share_media;

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
    }

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }

    public void setShare_media(SHARE_MEDIA share_media) {
        this.share_media = share_media;
    }
}
