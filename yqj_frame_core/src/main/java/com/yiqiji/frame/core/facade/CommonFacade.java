package com.yiqiji.frame.core.facade;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.yiqiji.frame.core.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * 通用的facade服务类
 *
 * @author aeeiko
 */
public class CommonFacade {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private static CommonFacade commonFacade;

    public static CommonFacade getInstance() {
        if (commonFacade == null) {
            commonFacade = new CommonFacade();
        }
        return commonFacade;
    }


    /***
     * 执行的方法
     *
     * @param action       事件url
     * @param viewCallBack view层需要实现的回调
     */
    public void exec(String action, ViewCallBack viewCallBack) {
        HashMap<String, String> map = new HashMap<>();
        MTask task = new MTask(action, map, viewCallBack);
        task.executeOnExecutor(executor);
        if (viewCallBack != null) {
            viewCallBack.onStart();
        }
    }

    /***
     * 执行的方法
     *
     * @param action       事件url
     * @param map          参数map
     * @param viewCallBack view层需要实现的回调
     */
    public void exec(String action, HashMap<String, String> map, ViewCallBack viewCallBack) {
        MTask task = new MTask(action, map, viewCallBack);
        task.executeOnExecutor(executor);
        if (viewCallBack != null) {
            viewCallBack.onStart();
        }
    }


    /***
     * 执行的方法
     *
     * @param action       事件url
     * @param jsonObject   参数jsonObject
     * @param viewCallBack view层需要实现的回调
     */
    public void exec(String action, JSONObject jsonObject, ViewCallBack viewCallBack) {
        MTask task = new MTask(action, jsonObject, viewCallBack);
        task.executeOnExecutor(executor);
        if (viewCallBack != null) {
            viewCallBack.onStart();
        }
    }


    /***
     * 执行的方法
     *
     * @param action       事件url
     * @param jsonArray    参数jsonArray
     * @param viewCallBack view层需要实现的回调
     */
    public void exec(String action, JSONArray jsonArray, ViewCallBack viewCallBack) {
        MTask task = new MTask(action, jsonArray, viewCallBack);
        task.executeOnExecutor(executor);
        if (viewCallBack != null) {
            viewCallBack.onStart();
        }
    }


    /***
     * 同步接口调用（直接返回服务数据，如果有异常返回空字符串）
     *
     * @param action
     * @param map
     * @return
     */
    public String execSync(String action, HashMap<String, String> map) {
        try {
            // 有结果直接返回到ui线程去处理
            String responseBody = ApiAccessor.getHttpResponse(action, map);
            return responseBody;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // 有异常返回的ui层处理
            return "";
        }
    }

    /***
     * 异步线程
     *
     * @author aeeiko
     */
    public class MTask extends AsyncTask<Object, Object, Object> {
        String action;
        HashMap<String, String> map;
        JSONObject jsonObject;
        JSONArray jsonArray;
        ViewCallBack viewCallBack;
        Gson gson = new Gson();
        int paramType; //提交的参数类型 1：map; 2: jsonObject. 3: jsonArray

        public MTask(String action, HashMap<String, String> map, ViewCallBack viewCallBack) {
            paramType = 1;
            this.action = action;
            this.map = map;
            this.viewCallBack = viewCallBack == null ? new ViewCallBack() {
            } : viewCallBack;
        }

        public MTask(String action, JSONObject jsonObject, ViewCallBack viewCallBack) {
            paramType = 2;
            this.action = action;
            this.jsonObject = jsonObject;
            this.viewCallBack = viewCallBack == null ? new ViewCallBack() {
            } : viewCallBack;
        }

        public MTask(String action, JSONArray jsonArray, ViewCallBack viewCallBack) {
            paramType = 3;
            this.action = action;
            this.jsonArray = jsonArray;
            this.viewCallBack = viewCallBack == null ? new ViewCallBack() {
            } : viewCallBack;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                String responseBody = "";
                if (paramType == 1) {
                    responseBody = ApiAccessor.getHttpResponse(action, map);
                } else if (paramType == 2 && jsonObject != null) {
                    responseBody = ApiAccessor.getHttpResponse(action, jsonObject);
                } else if (paramType == 3 && jsonArray != null) {
                    responseBody = ApiAccessor.getHttpResponse(action, jsonArray);
                }
                // 有结果直接返回到ui线程去处理
                return responseBody;
            } catch (Exception e) {
                // 有异常返回的ui层处理
                String error = e.getMessage();
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            viewCallBack.onFinish();
            try {
                // 处理上面的结果，这里处理各种异常情况
                if (result instanceof String) {

                    LogUtil.log_service_result(result.toString());

                    JSONObject jo_main = new JSONObject(result.toString());
                    if (jo_main.optInt("code", -100) == 0) {
                        //得到泛型类
                        Class entityClass = null;
                        Type t = viewCallBack.getClass().getGenericSuperclass();
                        if (t instanceof ParameterizedType) {
                            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
                            entityClass = (Class) p[0];
                        }
                        if (entityClass == null || entityClass.getSimpleName().equals("JSONObject")) {
                            viewCallBack.onSuccess(jo_main);
                        } else {
                            viewCallBack.onSuccess(gson.fromJson(result.toString(), entityClass));
                        }

                    } else {
                        int code = jo_main.optInt("code");
//                        if(code==20012||code==20010){  //用户被挤下线
//                            Intent intent = new Intent(MyApplicaction.getContext(), LoginActivity.class);
//                            intent.putExtra("ClassName", MainActivity.class.getName());
//                            intent.putExtra("IntentType", IntentUtils.LoginIntentType.OTHER);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            MyApplicaction.getContext().startActivity(intent);
//                        }
                        viewCallBack.onFailed(new SimpleMsg(jo_main.optInt("code"), jo_main.optString("msg")));
                    }
                } else if (result instanceof UnknownHostException || result instanceof ConnectException) {
                    viewCallBack.onFailed(new SimpleMsg(-100, "网络未连接"));
                } else if (result instanceof SocketTimeoutException) {
                    viewCallBack.onFailed(new SimpleMsg(-110, "网络超时"));
                }
            } catch (Exception ex) {
                // 在callback的success回调里面出错也会在这里捕捉，所以尽量ui层的代码写在onsuccess里面
                viewCallBack.onFailed(new SimpleMsg(-110, ex == null ? "数据异常" : ex.getMessage()));
                LogUtil.log_error(null, ex);
            }
        }
    }
}
