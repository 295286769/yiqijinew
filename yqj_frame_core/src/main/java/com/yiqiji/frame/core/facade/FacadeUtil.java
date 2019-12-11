package com.yiqiji.frame.core.facade;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.system.AssistUtil;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by leichi on 2017/5/16.
 */

public class FacadeUtil {

    public static HashMap<String, String> getBaseParmasMap() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("tokenid", LoginConfig.getInstance().getTokenId());
        hashMap.put("deviceid", LoginConfig.getInstance().getDeviceid());
        hashMap.put("plat", "android");
        hashMap.put("appver", LoginConfig.getInstance().getAppver());
        hashMap.put("machine", LoginConfig.getInstance().getMachine());
        hashMap.put("osver", LoginConfig.getInstance().getOsver());
        hashMap.put("channel", AssistUtil.getChannelName());
        return hashMap;
    }

    public static String getBaseParmas() {
        String baseParmasText = convertHashMapToString(getBaseParmasMap());
        return baseParmasText;
    }

    public static String convertHashMapToString(HashMap<String, String> map) {
        String parmasText = "";
        if (map != null) {
            String[] keyArray = new String[map.keySet().size()];
            map.keySet().toArray(keyArray);
            Arrays.sort(keyArray);
            for (int i = 0; i < keyArray.length; i++) {
                String key = keyArray[i];
                if (parmasText.equals("")) {
                    parmasText += key + "=" + map.get(key);
                } else {
                    parmasText += "&" + key + "=" + map.get(key);
                }
            }
        }
        return parmasText;
    }

    /**
     * 签个名真TMD绕，有必要吗？
     *
     * @param hashMap
     * @return
     */
    public static String getSignParamasString(HashMap<String, String> hashMap) {

        String signUrlParams = "";
        String sign;

        HashMap<String, String> allHashMap = new HashMap<>();
        if (hashMap != null) {
            allHashMap.putAll(hashMap);
        }
        allHashMap.putAll(getBaseParmasMap());

        signUrlParams = convertHashMapToString(allHashMap);
        sign = StringUtils.MD5(Constants.secret + signUrlParams);
        sign = StringUtils.reverse1(sign);
        signUrlParams += "&sign=" + sign;

        return signUrlParams;
    }

    /**
     * 签个名真TMD绕，有必要吗？
     *
     * @return
     */
    public static String getSignParamasString() {
        return getSignParamasString(null);
    }
}
