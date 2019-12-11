package com.yiqiji.money.modules.book.bookcategory.model;

import java.io.Serializable;

/**
 * Created by leichi on 2017/5/17.
 */

public interface IBaseBookCategory extends Serializable{
    String getAccountbookid();

    String getCategoryid();

    String getCategorytitle();

    String getCategoryName();

    String getCategoryicon();

    String getIconUrl();

    String getCategorytype();

    String getParentid();

    String getStatus();

    String getPikId();

    String getBilltype();

    void setStatus(String status);

    int getSerialNum();

    String getCustomid();

    void setBilltype(String billtype);

    String getParentName();

    void setCategoryid(String categoryid);

    void setCategorytitle(String categorytitle);

    void setCustomid(String customid) ;

    void setCategoryicon(String categoryicon);


}
