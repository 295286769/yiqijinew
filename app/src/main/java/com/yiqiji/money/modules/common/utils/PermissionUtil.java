package com.yiqiji.money.modules.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Zhang Jiaxing
 * Company: YiTiao Ltd
 * E-mail: zhangjiaxing@yit.com
 * Date: 2017-02-25 14:32
 */

/**
 * PermissionUtil is used to request permission. If the api above 23, then request permission
 * in running time.
 */
public class PermissionUtil {
    public interface RequestPermissionListener {
        void onRequestPermissionSuccess();
        void onRequestPermissionFail(int[] grantResults);
    }

    /**
     * PermissionRequestList is a map that used to store permission list. The key make up by activity's
     * simpleName and requestCode that concatenate by a '#'
     */
    static class PermissionRequestList {
        public static class Request {
            public Activity activity;
            public int requestCode;
            public String[] permissions;
            public RequestPermissionListener listener;
        }

        private Map<String, Request> mPermissionReqs;

        public void add(Request req) {
            if (req != null) {
                if (mPermissionReqs == null) {
                    mPermissionReqs = new HashMap<>(5);
                }
                mPermissionReqs.put(getRequestKey(req.activity, req.requestCode), req);
            }
        }

        private Request pop(Activity activity, int requestCode) {
            Request req = null;

            if (mPermissionReqs != null) {
                req = mPermissionReqs.remove(getRequestKey(activity, requestCode));
            }

            return req;
        }

        /**
         * Get a request's key by activity and requestCode.
         * @param activity The request's activity.
         * @param requestCode The request's requestCode.
         * @return The key of request.
         */
        private String getRequestKey(Activity activity, int requestCode) {
            return String.format("%s#%d", activity.getClass().getSimpleName(), requestCode);
        }
    }

    private static PermissionRequestList mReqs = new PermissionRequestList();

    /**
     * Check whether the api is not less than 23.
     * @return If the api is not less than 23, then return true, else return false.
     */
    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * request permissions. If the api is not less than 23, then request permissions in running time,
     * else we regard the request as success.
     * @param activity
     * @param requestCode
     * @param permissions
     * @param listener
     */
    public static void requestPermisions(Activity activity,
                                         int requestCode,
                                         String[] permissions,
                                         RequestPermissionListener listener) {
        if (activity != null && permissions != null && permissions.length != 0 && listener != null) {
            if (!isOverMarshmallow()) {
                listener.onRequestPermissionSuccess();
            } else {
                List<String> denied = findDeniedPermissions(activity, permissions);
                if (denied.size() > 0) {
                    doRequestPermisions(activity, requestCode, denied.toArray(new String[denied.size()]), listener);
                } else {
                    listener.onRequestPermissionSuccess();
                }
            }
        }
    }

    /**
     * find permissions that don't grant in a given permissions.
     * @param activity
     * @param permissions The permissions need to be checked.
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static List<String> findDeniedPermissions(Activity activity, String[] permissions) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            try {
                if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            } catch (Exception e) {
                // do nothing.
            }
        }
        return denyPermissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void doRequestPermisions(Activity activity,
                                            int requestCode,
                                            String[] permissions,
                                            RequestPermissionListener listener) {
        if (activity != null && permissions != null && permissions.length != 0 && listener != null) {
            PermissionRequestList.Request req = new PermissionRequestList.Request();
            req.activity = activity;
            req.requestCode = requestCode;
            req.permissions = permissions;
            req.listener = listener;
            mReqs.add(req);

            try {
                activity.requestPermissions(permissions, requestCode);
            } catch (Exception e) {
                // do nothing.
            }
        }
    }

    /**
     * An callback must be invoked by the activity's onRequestPermissionsResult.
     * @param activity  The activity correspond to {@code requestPermisions}'s activity.
     * @param requestCode  The requestCode correspond to {@code requestPermisions}'s requestCode.
     * @param permissions  The permissions that request to grant permission.
     * @param grantResults The result of granting permissions.
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode,
                                                  String[] permissions, int[] grantResults) {
        if (activity != null) {
            PermissionRequestList.Request req = mReqs.pop(activity, requestCode);

            if (req != null
                    && req.listener != null
                    && grantResults.length > 0) {
                boolean hasPermission = true;

                for (int permission : grantResults) {
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        hasPermission = false;
                        break;
                    }
                }

                if (hasPermission) {
                    req.listener.onRequestPermissionSuccess();
                } else {
                    req.listener.onRequestPermissionFail(grantResults);
                }
            }
        }
    }
}
