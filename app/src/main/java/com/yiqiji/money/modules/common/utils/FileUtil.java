package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static String readFile(Context app, String key) {
        return readFile(app, key, 0);
    }

    // 读取文件
    public static String readFile(Context mContext, String key, int minutes) {
        try {
            File file = new File(mContext.getFilesDir(), key + ".dat");
            if (!file.exists()) {
                return "";
            }
            if (minutes > 0) {
                long s = file.lastModified();
                long n = System.currentTimeMillis();
                int c = getDistMinutes(s, n);
                LogUtil.log_msg("缓存更新倒计时：" + (minutes - c) + " minutes");
                if (c >= minutes) {
                    file.delete();
                    return "";
                }
            }
            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            String res = new String(buffer, "UTF-8");
            fis.close();
            return res;
        } catch (Exception ex) {
            LogUtil.log_error("读取文件", ex);
            return "";
        }

    }

    // 写入文件
    public static void writeFile(Context mContext, String key, String write_str) {
        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(key + ".dat", Context.MODE_PRIVATE);
            if (fos != null) {
                fos.write(write_str.getBytes());
            }
        } catch (Exception ex) {
            LogUtil.log_error("写入文件", ex);
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void deleteFile(Context context, String key) {
        if (context != null && !StringUtils.isEmpty(key)) {
            File file = new File(context.getFilesDir(), key + ".dat");
            file.delete();
        }
    }

    public static int getDistMinutes(long startDate, long endDate) {
        long diff = endDate - startDate;
        long minutes = diff / (1000 * 60);
        return (int) minutes;
    }

    /**
     * 保存对象（files目录下）
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public static boolean saveObject(Context mContext, Serializable ser,
                                     String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = mContext.openFileOutput(file, mContext.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象（files目录下）
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readObject(Context mContext, String file) {
        if (!isExistDataCache(mContext, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = mContext.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = mContext.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在（files目录）
     *
     * @param cachefile
     * @return
     */
    public static boolean isExistDataCache(Context mContext, String cachefile) {
        boolean exist = false;
        File data = mContext.getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 读取表情配置文件
     *
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");// 表情文件
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }


    /***
     * InputStream 转 String
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * *
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }


    public static SharedPreferences getSharedPreferences(Context mContext) {
        return mContext.getSharedPreferences("YqjSharedPreferences",
                Context.MODE_PRIVATE);
    }

    public static void setSpValue(Context mContext, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSpValue(Context mContext, String key) {
        SharedPreferences preferences = getSharedPreferences(mContext);
        return preferences.getString(key, "");
    }


    /**
     * *
     * 文件存储根目录
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * uri路劲文件是否为空 true不为空 false ：为空
     *
     * @param uri
     * @return
     */
    public static boolean getfileLenth(Uri uri) {
        boolean isFileLenth = false;
        if (uri != null) {
            File file = new File(uri.toString());
            if (file.length() > 0) {
                isFileLenth = true;
            }
        }
        return isFileLenth;
    }
}
