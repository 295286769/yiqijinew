package com.yiqiji.money.modules.homeModule.home.entity;

/**
 * Created by ${huangweishui} on 2017/3/10.
 * address huang.weishui@71dai.com
 */
public class CheckMessageInfo {
    private int code;
    private String msg;
    private MessgeinfoItem data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessgeinfoItem getData() {
        return data;
    }

    public void setData(MessgeinfoItem data) {
        this.data = data;
    }

    public class MessgeinfoItem {
        private int newbook; // 新账本数
        private int newmsg;// 新消息数
        private int subscribe;//我关注的账本总数
        private String sharehouse; // 共享装修账本数
        private String sharetravel; //  共享旅行账本数

        public int getNewbook() {
            return newbook;
        }

        public void setNewbook(int newbook) {
            this.newbook = newbook;
        }

        public int getNewmsg() {
            return newmsg;
        }

        public void setNewmsg(int newmsg) {
            this.newmsg = newmsg;
        }

        public int getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(int subscribe) {
            this.subscribe = subscribe;
        }

        public String getSharehouse() {
            return sharehouse;
        }

        public void setSharehouse(String sharehouse) {
            this.sharehouse = sharehouse;
        }

        public String getSharetravel() {
            return sharetravel;
        }

        public void setSharetravel(String sharetravel) {
            this.sharetravel = sharetravel;
        }
    }
}
