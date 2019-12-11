package com.yiqiji.money.modules.Found.entity;

import java.io.Serializable;

/**
 * TODO(表情实体)
 *
 * @author shaoxs
 * @version V1.0
 * @Date 2014-2-26 上午11:22:15
 */
public class FaceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;// 表情资源图片对应的ID
    private String character;// 表情资源对应的文字描述
    private String faceName;// 表情资源的文件名
    private String faceUrl;// 拼接的表情链接

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

}
