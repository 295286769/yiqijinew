package com.yiqiji.money.modules.homeModule.mybook.entity;

/**
 * Created by ${huangweishui} on 2017/8/1.
 * address huang.weishui@71dai.com
 */
public class HouseStypeInfo {
    private int type;//100:房屋户型101：装修方式102：风格
    private boolean isSelect;//是否选中
    private String title;//
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
