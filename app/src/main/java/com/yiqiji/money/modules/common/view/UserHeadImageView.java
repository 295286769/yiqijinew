package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;

/**
 * Created by leichi on 2017/5/4.
 */

public class UserHeadImageView extends CircleImageView {


    public UserHeadImageView(Context context) {
        super(context);
    }

    public UserHeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserHeadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void displayImage(BooksDbMemberInfo memberInfo) {
        XzbUtils.displayImage(this,memberInfo.getUsericon(),getNativeImageResuoceId(memberInfo.getUserid(),  memberInfo.getDeviceid(),memberInfo.getMemberid()));
    }

    public void displayImage(BillMemberInfo memberInfo) {
        XzbUtils.displayImage(this,memberInfo.getUsericon(),getNativeImageResuoceId(memberInfo.getUserid(),  memberInfo.getDeviceid(),memberInfo.getMemberid()));
    }

    /**
     * 根据成员的userId和deviceId映射本地的头像图片路径
     * @param userId
     * @param deviceId
     * @return
     */
    private int getNativeImageResuoceId(String userId, String deviceId,String memberid) {
        int iconIndex = 0;
        if (StringUtils.isEmpty(userId) || userId.equals("0")) {
            if (!StringUtils.isEmpty(deviceId)) {
                String firstChar = deviceId.substring(0, 1);
                if (StringUtils.isInteger(firstChar)) {
                    iconIndex = Integer.parseInt(firstChar);
                } else {
                    int ascii = firstChar.charAt(0);
                    if (ascii >= 97) {
                        ascii = ascii - 97;
                    } else if (ascii >= 65) {
                        ascii = ascii - 65;
                    }
                    iconIndex = ascii % 10;
                }
            } else if(!StringUtils.isEmpty(memberid)){
                iconIndex =  Integer.parseInt(memberid)%10;
            }else {
                iconIndex=0;
            }
        } else {
            iconIndex = (Integer.parseInt(userId)) % 10;
        }
        return RequsterTag.head_image[iconIndex];
    }
}
