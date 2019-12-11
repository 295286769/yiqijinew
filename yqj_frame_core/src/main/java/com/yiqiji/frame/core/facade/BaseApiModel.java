package com.yiqiji.frame.core.facade;

import org.json.JSONObject;

/**
 * Created by aeeiko on 2017/3/17.
 */

public abstract class BaseApiModel {
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public abstract BaseApiModel deserialize(JSONObject jsonObject) throws Exception;
}
