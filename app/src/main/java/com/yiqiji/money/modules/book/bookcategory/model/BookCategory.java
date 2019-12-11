package com.yiqiji.money.modules.book.bookcategory.model;

import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by leichi on 2017/5/27.
 */

public class BookCategory implements IBaseBookCategory {
    public String accountbookid;                    //账本Id
    public String categoryid;                       //分类id
    public String categorytitle;                    //分类名称
    public String categoryicon;                     //分类图标
    public String categorytype;                     //分类类型 0 单人账本，1多人账本
    public String parentid;                         //父分类的Id
    public String parentName;                       //父分类的名称
    public String status;                           //分类状态 0：禁用；1：启用
    public String billtype;                         //记账类型 0: 收入；1：支出
    public String pikId = StringUtils.getUUID();     //本地的临时id
    public String customid;                         //自定义字段id(模板分类是没有这个id的，只有同步过后才有，才能做删除禁用启用等操作)
    public List<BookCategory> child;                //子分类
    //-----------本地使用的字段----------------//
    public boolean isLastItem;                      //是否是最后一个item
    public int serialNum;                           //排序序号

    public String getAccountbookid() {
        return accountbookid;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public String getCategorytitle() {
        return categorytitle;
    }

    public String getCategoryName() {
        if (categorytitle.contains("－")) {
            return getNameReplaceBar();
        }
        return categorytitle;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public String getParentid() {
        return parentid;
    }

    public String getStatus() {
        return status;
    }

    public String getPikId() {
        return pikId;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getSerialNum() {
        return serialNum;
    }

    @Override
    public String getCustomid() {
        return customid;
    }

    @Override
    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public void setCategorytitle(String categorytitle) {
        this.categorytitle = categorytitle;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    //http://static.test.yiqijiba.com/accountbook/category/new/10/312@3x.png
    public String getIconUrl() {
        return getIconUrl(categoryicon);
    }

    public static String getHDIconUrl(String iconurl) {
        String iconHDUrl = getIconUrl(iconurl);
        if (iconHDUrl.length() == 0) {
            return iconHDUrl;
        }
        int tagIndex = iconHDUrl.lastIndexOf("@");
        String start = iconHDUrl.substring(0, tagIndex + 1);
        String end = iconHDUrl.substring(tagIndex + 2, iconHDUrl.length());
        iconHDUrl = start + "3" + end;
        return iconHDUrl;
    }

    public String getNameReplaceBar() {
        String[] str = categorytitle.split("－");
        return str.length > 1 ? str[1] : categorytitle ;
    }

    public boolean isEnable(){
        return status.equals("1");
    }

    public static String getIconUrl(String iconurl){
        String iconUrl = "";
        if (iconurl == null || iconurl.length() == 0) {
            return "";
        }
        int tagIndex = iconurl.lastIndexOf("@");

        if (tagIndex < 0) {
            return "";
        }
        iconUrl = iconurl.substring(0, tagIndex) + "_s" + iconurl.substring(tagIndex, iconurl.length());
        return iconUrl;
    }
}
