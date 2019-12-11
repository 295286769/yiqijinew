package com.yiqiji.money.modules.common.entity;

/**
 * Created by whl on 16/9/24.
 */
public class SalaryBean {
    private String tiem;
    private boolean isShow;
    private String money;
    private String prompt;

    public SalaryBean(String tiem, boolean isShow, String money, String prompt) {
        this.tiem = tiem;
        this.isShow = isShow;
        this.money = money;
        this.prompt = prompt;
    }

    public String getTiem() {
        return tiem;
    }

    public void setTiem(String tiem) {
        this.tiem = tiem;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
