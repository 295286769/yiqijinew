package com.yiqiji.frame.core.facade;

/***
 * view层回调类
 *
 * @param <T>
 * @author aeeiko
 */
public abstract class ViewCallBack<T> {
    T t;

    /***
     * 流程开始
     */
    public void onStart() {
    }

    /***
     * 成功返回
     *
     * @param t
     */
    public void onSuccess(T t) throws Exception {
    }

    /***
     * 失败返回
     *
     * @param simleMsg
     */
    public void onFailed(SimpleMsg simleMsg) {

    }

    /***
     * 在onSuccess和onFailed之前执行，用于销毁在onstart里面启动的组件
     */
    public void onFinish() {

    }
}
