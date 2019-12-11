package com.yiqiji.frame.core.utils;

import android.util.Log;


import com.yiqiji.frame.core.Constants;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class LogUtil {


    private static final String TAG_MSG = "yqj_msg";

    private static final String TAG_STAT = "yqj_stat";

    private static final String TAG_IMAGE = "yqj_myimage";

    private static final String TAG_ERROR = "yqj_error";

    private static final String TAG_SERVICE = "yqj_service";

    private static final String TAG_SERVICE_RESULT = "yqj_result";

    private static final String TAG_WEB = "yqj_web";

    public static boolean SHOWLOG = Constants.DEBUG;

    public static void log_msg(String msg) {
        log(TAG_MSG, msg, null);
    }

    public static void log_web(String msg) {
        log(TAG_WEB, msg, null);
    }

    public static void log_service(String msg) {
        log(TAG_SERVICE, msg, null);
    }

    public static void log_service_result(String msg) {

        log(TAG_SERVICE_RESULT, msg, null);
    }

    public static void log_image(String msg) {
        log(TAG_IMAGE, msg, null);
    }

    public static void log_stat(String msg) {
        log(TAG_STAT, msg, null);
    }


    public static void log_error(String msg) {
        log_error(msg, null);
    }

    public static void log_error(String msg, Throwable ex) {
        log(TAG_ERROR, msg, ex);
    }

    private static void log(String tag, String msg, Throwable ex) {
        if (SHOWLOG) {
            if (msg != null) {
                Log.i(tag, msg);
            }
            if (ex != null) {
                String exMsg = getExceptionMessage(ex);
                Log.e(tag, exMsg);
            }
        }
    }

    public static String getExceptionMessage(Throwable ex) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return info;
    }


    private static String getExtraMsg() {
        StackTraceElement[] p0 = Thread.currentThread().getStackTrace();
        if (p0 != null) {
            int p1 = p0.length;

            for (int p2 = 0; p2 < p1; ++p2) {
                StackTraceElement p3 = p0[p2];
                if (!p3.isNativeMethod()
                        && !p3.getClassName().equals(Thread.class.getName())
                        && !p3.getClassName().equals(LogUtil.class.getName())) {
                    StringBuilder p4 = new StringBuilder();
                    p4.append("t:");
                    p4.append(Thread.currentThread().getName());
                    p4.append(" f:");
                    p4.append(p3.getFileName());
                    p4.append(" l:");
                    p4.append(p3.getLineNumber());
                    p4.append(" m:");
                    p4.append(p3.getMethodName());
                    return p4.toString();
                }
            }
        }

        return null;
    }

    private static String getFinalMsg(Object p0) {
        String p1;
        if (p0 != null) {
            p1 = p0.toString();
        } else {
            p1 = "null";
        }

        String p2 = p1;
        String p3 = getExtraMsg();
        if (p3 != null) {
            StringBuilder p4 = new StringBuilder();
            p4.append(p3);
            p4.append(" - ");
            p4.append(p1);
            p2 = p4.toString();
        }

        return p2;
    }


}
