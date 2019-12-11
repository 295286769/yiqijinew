package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.utils.ZXingUtils;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.entity.ShorturlInfo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class ManuallyAddActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout link_add, manually_add, phone_contant;
    private String id;
    private String inviteid;// 邀请人
    private String memberid;// 被邀请人
    private ImageView image;
    private String url;
    private int with;
    private int screeWith;
    private String[] shareText = new String[]{"微信", "新浪微博", "QQ"};
    private int share_icon[] = new int[]{R.drawable.chart_icon, R.drawable.weibo_icon, R.drawable.qq};
    private String type[] = new String[]{"wx", "wb", "qq"};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ};
    private String typeString = "";
    private String mMemberName = "";
    private String bookName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_add);
        screeWith = XzbUtils.getPhoneScreen(this).widthPixels;
        id = getIntent().getStringExtra("id");
        inviteid = getIntent().getStringExtra("inviteid");
        memberid = getIntent().getStringExtra("memberid");
        initView();
        DownUrlUtil.initBooksDataAndMember(id, handler, 0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    BooksDbInfo booksDbInfo = (BooksDbInfo) msg.obj;
                    if (booksDbInfo != null) {
                        bookName = booksDbInfo.getAccountbooktitle();
                        mMemberName = getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbInfo.getMember());
                        String isclear = booksDbInfo.getIsclear();
                        if (isclear.equals("0")) {
                            manually_add.setVisibility(View.GONE);
                        }
                    }

                    break;

            }
        }
    };


    private void initView() {
        link_add = (RelativeLayout) findViewById(R.id.link_add);
        manually_add = (RelativeLayout) findViewById(R.id.manually_add);
        phone_contant = (RelativeLayout) findViewById(R.id.phone_contant);
        image = (ImageView) findViewById(R.id.image);
        initTitle();
        initListener();
        setUrl();
        // initBitmap();

    }

    private void setUrl() {
        url = initUrl(true);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("url", url);
        CommonFacade.getInstance().exec(Constants.GENSHORTURL, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                ShorturlInfo info = GsonUtil.GsonToBean(o.toString(), ShorturlInfo.class);
                ShorturlInfo.UrlInfo urlInfo = info.getData();
                url = urlInfo.getShorturl();
                initBitmap();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
            }
        });
    }

    private void initBitmap() {
        with = XzbUtils.getPhoneScreen(this).widthPixels / 2;
        final String filePath = ZXingUtils.getFileRoot(ManuallyAddActivity.this) + File.separator + "qr_"
                + System.currentTimeMillis() + ".jpg";
        TaskQueue taskQueue = TaskQueue.mainQueue();
        taskQueue.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zxing_logo);
                final boolean success = ZXingUtils.createQRImage(url.trim(), with, with, bitmap, filePath);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (success) {
                            image.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }

                    }
                });

            }
        });

    }

    private void initListener() {
        link_add.setOnClickListener(this);
        manually_add.setOnClickListener(this);
        phone_contant.setOnClickListener(this);

    }

    private void initTitle() {
        initTitle("添加成员");
    }

    private MyPopuwindows myPopuwindows;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.link_add:// 链接添加


                //todo 分享 （邀请好友加入账本）
                String title = "“" + mMemberName + "”邀请你加入" + "<<" + bookName + ">>一起记账，快来加入吧！";
                String text = LoginConfig.invest_context;
                boolean isNeedTypeString = false;
                ShareUtil.shareMethInviteid(this, link_add, shareText, share_icon, share_medias, type, title, text, screeWith, isNeedTypeString, id, inviteid, memberid, null);

                break;
            case R.id.manually_add:// 手动添加

                intent = new Intent(this, SignAddMemberActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

                break;
            case R.id.phone_contant:// 通讯录添加
//                intent = new Intent(this, SignAddMemberActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);

                break;

            default:
                break;
        }
    }


    private String initUrl(boolean b) {
        long time = new Date().getTime() / 1000;
        HashMap<String, String> hashMap = XzbUtils.mapParmas();
        String sign = null;
        String url = null;
        if (b) {
            String[] valus = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), id, inviteid, memberid, time + ""};
            String[] params = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime"};

            sign = StringUtils.setSign(valus, params);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + id + "&inviteid="
                    + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&sign=" + sign;
            url = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
        } else {
            String[] valusStrings = new String[]{hashMap.get("tokenid"), hashMap.get("plat"), hashMap.get("deviceid"),
                    hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine"), id, inviteid, memberid, time + "", typeString};
            String[] paramsStrings = new String[]{"tokenid", "deviceid", "plat", "appver", "osver", "machine", "id", "inviteid",
                    "memberid", "ctime", "type"};

            sign = StringUtils.setSign(valusStrings, paramsStrings);
            url = Constants.BASE_URL + Constants.INVITATION + "?tokenid=" + hashMap.get("tokenid") + "&deviceid="
                    + hashMap.get("deviceid") + "&plat=" + hashMap.get("plat") + "&appver=" + hashMap.get("appver")
                    + "&osver=" + hashMap.get("osver") + "&machine=" + hashMap.get("machine") + "&id=" + id + "&inviteid="
                    + inviteid + "&memberid=" + memberid + "&ctime=" + time + "&type=" + typeString + "&sign=" + sign;
        }

        return url;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
