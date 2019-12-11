package com.yiqiji.money.modules.homeModule.home.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.control.LinearLayoutForTable;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.utils.AnimationUtil;
import com.yiqiji.frame.core.utils.BitmapUtil;
import com.yiqiji.money.modules.common.utils.DataBaseUtil;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.FileUtil;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.LogUtil;
import com.yiqiji.money.modules.common.utils.PermissionUtil;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.adapter.DecorateCateAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.BaserClassMode;
import com.yiqiji.money.modules.homeModule.home.entity.BillSyncInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CateEntity;
import com.yiqiji.money.modules.homeModule.home.entity.ShareInfo;
import com.yiqiji.money.modules.homeModule.home.modelperecenter.ModelPerecenter;
import com.yiqiji.money.modules.homeModule.write.activity.LocationActivity;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/5/18.
 * address huang.weishui@71dai.com
 */
public class WriteJournalActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    private EditText input_content;
    private CheckBox check_freinds, check_space;
    //    private SelectableRoundedImageView iv_details_photograph;
    private ImageView ivCaram;
    private RelativeLayout rl_details_time;
    private TextView tv_details_time, tv_details_time_selected;
    private String longDate;
    private Date date_time;
    private SHARE_MEDIA share_media;
    //从相册获取图片返回的地址
    private Uri uri = null;
    //定位地址
    private String address = "不显示位置";
    private String accountbookid = "";
    private DailycostEntity mDailycostEntity;
    private boolean isAdd = true;
    private String accountbooktype = "";
    private String isClear = "";
    private String billcateid = "";
    private BooksDbInfo booksDbInfo;

    private ImageView ivLocation;//定位
    private LinearLayout location_layout;//
    private TextView tvAddr;
    private LinearLayoutForTable ll_cate;//装修日记，装修阶段分类
    private DecorateCateAdapter adapter;
    private int cateId = 0;//装修账本分类ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);
        longDate = getIntent().getStringExtra("longdate");
        mDailycostEntity = getIntent().getParcelableExtra("DailycostEntity");
        date_time = new Date(Long.parseLong(longDate) * 1000);
        accountbookid = getIntent().getStringExtra("accountbookid");
        initView();
        initTitle(this, "取消", "发布", "日志感想");
        if (mDailycostEntity == null) {
            cateId = 0;
            date_time = new Date();
            isAdd = true;
        } else {
            isAdd = false;
            String billmark = mDailycostEntity.getBillmark();
            String billimg = mDailycostEntity.getBillimg();
            String address = mDailycostEntity.getAddress();
            String tradetime = mDailycostEntity.getTradetime();
            cateId = mDailycostEntity.getBillcateid() != null ? Integer.parseInt(mDailycostEntity.getBillcateid()) : 0;
            date_time = new Date(Long.parseLong(tradetime) * 1000);
            input_content.setText(billmark);
            input_content.setSelection(billmark.length());
            if (!TextUtils.isEmpty(billimg)) {
                ImageLoaderManager.loadRoundCornerImage(this, billimg, ivCaram, UIHelper.dip2px(this, 5));
            }
            if (!TextUtils.isEmpty(address)) {
                tvAddr.setVisibility(View.VISIBLE);
                tvAddr.setText(address);
                ivLocation.setImageResource(R.drawable.location_icon);
            } else {
                tvAddr.setVisibility(View.GONE);
                ivLocation.setImageResource(R.drawable.unlocation_icon);
            }
            //设置时间
            if (!TextUtils.isEmpty(mDailycostEntity.getTradetime())) {
                if (DateUtil.isToday(date_time)) {
                    tv_details_time.setText("今天");
                    tv_details_time_selected.setText("昨天?");
                } else {
                    tv_details_time.setText(DateUtil.formatTheDateToMM_dd(date_time, 1));
                    tv_details_time_selected.setText("今天?");
                }
            }
        }
        DownUrlUtil.initBooksDataAndMember(accountbookid, handler, 1);
//        getBookDetailinfo(accountbookid);
    }

    public void getBookDetailinfo(final String mAccountid) {
        long start_time = DateUtil.getStartTime(date_time);//开始时间
        long end_time = DateUtil.getEndTime(date_time);// 结束时间
        String string_start_time = start_time + "";
        String string_end_time = end_time + "";
        HashMap<String, String> hashMap = DateUtil.getmapParama("id", mAccountid,
                "stime", string_start_time, "etime", string_end_time);

        CommonFacade.getInstance().exec(Constants.BOOKS_DETAIL, hashMap, new ViewCallBack<BooksDbInfo>() {


            @Override
            public void onSuccess(BooksDbInfo o) throws Exception {
                super.onSuccess(o);
                BooksDbInfo booksDbI = o;

                if (booksDbI == null) {
                    return;
                }
                booksDbInfo = booksDbI;
                accountbooktype = booksDbInfo.getAccountbooktype();
                isClear = booksDbInfo.getIsclear();
                billcateid = booksDbInfo.getAccountbookcate();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((WriteJournalActivity) mContext).showToast(simleMsg);

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }


    //从相册里面取后返回
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        String path = getIntent().getStringExtra("path");
        if (!StringUtils.isEmpty(path)) {
            uri = Uri.parse(path);
        }
        if (!TextUtils.isEmpty(path) && !path.contains("file://")) {
            path = "file://" + path;
        }
        XzbUtils.displayRoundImage(ivCaram, path, 0, UIHelper.Dp2Px(mContext, 5));
    }

    private void initView() {
        input_content = (EditText) findViewById(R.id.input_content);
        rl_details_time = (RelativeLayout) findViewById(R.id.rl_details_time);
        ivCaram = (ImageView) findViewById(R.id.ivCaram);
        tv_details_time = (TextView) findViewById(R.id.tv_details_time);
        tv_details_time_selected = (TextView) findViewById(R.id.tv_details_time_selected);
        ll_cate = (LinearLayoutForTable) findViewById(R.id.ll_cate);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        ivLocation = (ImageView) findViewById(R.id.ivLocation);
        location_layout = (LinearLayout) findViewById(R.id.location_layout);

        check_freinds = (CheckBox) findViewById(R.id.check_freinds);
        check_space = (CheckBox) findViewById(R.id.check_space);
        input_content.setOnFocusChangeListener(this);
        check_freinds.setOnCheckedChangeListener(this);
        check_space.setOnCheckedChangeListener(this);
        rl_details_time.setOnClickListener(this);
        ivCaram.setOnClickListener(this);
        tv_details_time_selected.setOnClickListener(this);
//        ivLocation.setOnClickListener(this);
        location_layout.setOnClickListener(this);
    }

    //创建一个以时间结尾的png图片路径
    private void initPath() {
        uri = XzbUtils.getTpPath();
    }

    List<DailycostEntity> dailycostEntitysub = null;
    List<PostBill> postBill = null;

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_details_time:
                XzbUtils.hideOrShowSoft(this, input_content, false);
                intent = new Intent(this, CalendarActivity.class);
                Bundle bundle = new Bundle();
                //时间选则器时间,date当前时间
                bundle.putSerializable("date", date_time);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequsterTag.CALENDERCODE);
                break;
            case R.id.ivCaram:
                if (!FileUtil.getfileLenth(uri)) {
                    XzbUtils.hideOrShowSoft(this, input_content, false);
                    initPath();
                    IntentUtils.showPhotoView(WriteJournalActivity.this, uri);
                } else {
                    this.getPackageName();
                    this.getClass().getName();
                    intent = new Intent(WriteJournalActivity.this, PhotosShowActivity.class);
                    intent.putExtra("path", uri.toString());
                    startActivityForResult(intent, RequsterTag.IsphotosShow);
                }

                break;
            case R.id.tv_details_time_selected:
                //当前时间为今天
                if (DateUtil.isToday(date_time)) {
                    this.date_time = DateUtil.getUpDay(new Date());
                } else {
                    this.date_time = new Date();
                }
                AnimationUtil.onSelectedTimeAnimStart(WriteJournalActivity.this, date_time, tv_details_time, tv_details_time_selected);
                break;
            case R.id.location_layout:
                XzbUtils.hideOrShowSoft(this, input_content, false);
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                PermissionUtil.requestPermisions(WriteJournalActivity.this, RequsterTag.ACCESS_FINE_LOCATION_S, permissions, new PermissionUtil.RequestPermissionListener() {

                    @Override
                    public void onRequestPermissionSuccess() {
                        Intent intent = new Intent(mContext, LocationActivity.class);
                        intent.putExtra("address", address);
                        startActivityForResult(intent, RequsterTag.Islocation);
                    }

                    @Override
                    public void onRequestPermissionFail(int[] grantResults) {
                        //拒绝不做操作
                    }
                });
                break;
            case R.id.layout_title_view_return_text://取消
                isCanBackShowToast();
                break;
            case R.id.layout_title_view_right_btn://发布
                String input_text = StringUtils.getInputText(input_content);
                String location = StringUtils.getInputText(tvAddr);
                if (TextUtils.isEmpty(input_text)) {
                    showToast("您还没有填写日志内容");
                    return;
                }

//                if (!TextUtils.isEmpty(input_text) || FileUtil.getfileLenth(uri)) {
                showLoadingDialog();
                if (isAdd) {
                    if (billcateid.equals("6")) {
                        dailycostEntitysub = ModelPerecenter.addDailycostInfo(input_text, accountbookid, location, billcateid, accountbooktype, uri, date_time, cateId);
                    } else {
                        dailycostEntitysub = ModelPerecenter.addDailycostInfo(input_text, accountbookid, location, billcateid, accountbooktype, uri, date_time);
                    }
                    mDailycostEntity = dailycostEntitysub.get(0);

                } else {
                    List<DailycostEntity> dailycostEntities = new ArrayList<>();
                    mDailycostEntity.setIssynchronization("edit");
                    if (FileUtil.getfileLenth(uri)) {
                        String urlPath = uri.toString();
                        if (!urlPath.contains("file://")) {
                            urlPath = "file://" + urlPath;
                        }
                        mDailycostEntity.setBillimg(urlPath);
                    }
                    mDailycostEntity.setBillmark(input_text);
                    mDailycostEntity.setBillcateid(cateId + "");
                    mDailycostEntity.setTradetime(date_time.getTime() / 1000 + "");
                    if (location.equals("定位位置")) {
                        input_text = "";
                    }
                    mDailycostEntity.setAddress(location);
                    dailycostEntities.add(mDailycostEntity);
                    dailycostEntitysub = dailycostEntities;
                }

                postBill = DownUrlUtil.getPostBillData(dailycostEntitysub);
                if (dailycostEntitysub != null && !TextUtils.isEmpty(mDailycostEntity.getBillimg()) && !mDailycostEntity.getBillimg().contains("http")) {
                    DownUrlUtil.sysQiniu(this, StringUtils.getRealFilePath(WriteJournalActivity.this, Uri.parse(mDailycostEntity.getBillimg())), 0, new ViewCallBack() {
                        @Override
                        public void onSuccess(Object o) throws Exception {
                            super.onSuccess(o);
                            DailycostEntity dailycostEnti = (DailycostEntity) o;
                            List<DailycostEntity> entities = new ArrayList<>();
                            entities.add(dailycostEnti);
                            postBill = DownUrlUtil.getPostBillData(entities);
                            submitJournal(postBill);
                        }

                        @Override
                        public void onFailed(SimpleMsg simleMsg) {
                            super.onFailed(simleMsg);
                            dismissDialog();
                        }
                    }, mDailycostEntity);
                } else {
                    submitJournal(postBill);
                }

//                }

                break;
        }
    }

    private void submitJournal(List<PostBill> postBill) {
        BaserClassMode.setSynOneDailycostEntity(postBill, new ViewCallBack<BillSyncInfo>() {
            @Override
            public void onSuccess(BillSyncInfo o) throws Exception {
                super.onSuccess(o);
                dismissDialog();
                BillSyncInfo.DataInfo dataInfo = o.getData().get(0);
                mDailycostEntity.setIssynchronization("true");
                mDailycostEntity.setBillstatus("0");
                mDailycostEntity.setBillid(dataInfo.getBillid() + "");
                String usericon = LoginConfig.getInstance().getUsericon();

                if (share_media != null) {
                    String content = input_content.getText().toString();
                    ShareInfo shareInfo = new ShareInfo();
                    shareInfo.setBillid(dataInfo.getBillid());
                    shareInfo.setShare_media(share_media);
                    shareInfo.setShareContent(content);
                    EventBus.getDefault().post(shareInfo);
                }
                if (isAdd) {
                    mDailycostEntity.setUsericon(usericon);
                    DataBaseUtil.addDailycostInfo(mDailycostEntity, handler, 0);
                } else {
                    String where = DailycostContract.DtInfoColumns.BILLID + "=? ";
                    String whereStrings[] = new String[]{dataInfo.getBillid() + ""};
                    DataBaseUtil.updateDataBaseDailycostInfo(mDailycostEntity, where, whereStrings, handler, 0);
                }


            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    IntentUtils.startActivity(WriteJournalActivity.this, HomeActivity.class, "mTradetime", DateUtil.toTimeStamp(date_time) + "", "isShow", "true");
                    break;
                case 1:
                    BooksDbInfo booksDbInf = (BooksDbInfo) msg.obj;
                    if (booksDbInf != null) {
                        booksDbInfo = booksDbInf;
                        accountbooktype = booksDbInfo.getAccountbooktype();
                        isClear = booksDbInfo.getIsclear();
                        billcateid = booksDbInfo.getAccountbookcate();
                        if ("6".equals(billcateid)) {//装修日记
                            changeTitleText("装修日记");
                            input_content.setHint("记录分享装修的酸甜苦辣");
                            loadCateData(billcateid);
                            ll_cate.setVisibility(View.VISIBLE);
                        } else {//日志感想
                            changeTitleText("日志感想");
                            input_content.setHint("此刻的一些想法");
                            ll_cate.setVisibility(View.GONE);
                        }
                    }
                    break;
            }

        }
    };

    /**
     * 获取装修日志对应的分类列表
     */
    private void loadCateData(String billcateid) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("id", billcateid);
        CommonFacade.getInstance().exec(Constants.JOURNAL_CATE, map, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                String str = jo_main.optString("data");
                if (isAdd) {
                    cateId = SPUtils.getParam(LoginConfig.getInstance().getUserid() + "deccate", 0);
                }

                Gson gson = new Gson();
                String[] acate = gson.fromJson(str, String[].class);
                final List<CateEntity> list = new ArrayList<CateEntity>();
                if (acate != null && acate.length > 0) {
                    CateEntity entity = null;
                    for (int i = 0; i < acate.length; i++) {
                        entity = new CateEntity();
                        if (cateId == i) {
                            entity.setIsChk(1);
                        } else {
                            entity.setIsChk(0);
                        }
                        entity.setCate(acate[i]);

                        list.add(entity);
                    }
                }

                ll_cate.setOnItemClickLisntener(new LinearLayoutForTable.OnItemClickLisntener() {
                    @Override
                    public void onItemClick(int position) {
                        cateId = position;
                        SPUtils.setParam(LoginConfig.getInstance().getUserid() + "deccate", position);
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                list.get(i).setIsChk(1);
                            } else {
                                list.get(i).setIsChk(0);
                            }
                        }
                        ll_cate.setAdapter(adapter, 4, 0);
                    }
                });
                adapter = new DecorateCateAdapter(mContext, list);
                ll_cate.setAdapter(adapter, 4, 0);
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            input_content.setCursorVisible(true);
        } else {
            input_content.setCursorVisible(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.check_freinds:
                    share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                    check_space.setChecked(false);
                    break;
                case R.id.check_space:
                    check_freinds.setChecked(false);
                    share_media = SHARE_MEDIA.QZONE;
                    break;
            }
        } else {
            share_media = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case RequsterTag.RQ_TAKE_A_PHOTO:// 拍照
                if (uri != null) {
                    Bitmap bitmap;
                    String pathString;
                    try {
                        bitmap = BitmapUtil.getBitmapFormUri(this, uri, UIHelper.Dp2Px(this, 200), UIHelper.Dp2Px(this, 200));
                        pathString = StringUtils.getRealFilePath(this, XzbUtils.getPath());

                        BitmapUtil.saveBitmapToFile(bitmap, pathString);
                        uri = Uri.parse(pathString);
                        if (!pathString.contains("file://")) {
                            pathString = "file://" + pathString;
                        }

                        XzbUtils.displayRoundImage(ivCaram, pathString, 0, UIHelper.dip2px(WriteJournalActivity.this, 5));
//                        ImageLoaderManager.loadImage(WriteJournalActivity.this, R.drawable.write_photograph, iv_details_photograph);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.log_error(null, e);
                    }
                }

                break;

            case RequsterTag.IsphotosShow:
                //点击删除图片后返回
                String path = data.getStringExtra("path");
                if (TextUtils.isEmpty(path)) {
                    uri = Uri.parse(path);
                    ImageLoaderManager.loadRoundCornerImage(WriteJournalActivity.this, R.drawable.write_photograph, ivCaram, UIHelper.dip2px(WriteJournalActivity.this, 5));
                }
                break;


            case RequsterTag.Islocation:
                String name = data.getStringExtra("name");
                address = name;
                if (name.equals("不显示位置")) {
                    tvAddr.setVisibility(View.VISIBLE);
                    tvAddr.setText("定位位置");
                    ivLocation.setImageResource(R.drawable.location_icon);
                } else {
                    if (!StringUtils.isEmpty(name)) {
                        tvAddr.setVisibility(View.VISIBLE);
                        tvAddr.setText(name);
                        ivLocation.setImageResource(R.drawable.location_icon);
                    } else {
                        tvAddr.setVisibility(View.GONE);
                        ivLocation.setImageResource(R.drawable.unlocation_icon);
                    }

                }
                break;
//            case 1002:
//                loadData(false);
//                break;
            case RequsterTag.CALENDERCODE://时间选择接收
                if (data != null) {
                    Date date = (Date) data.getExtras().getSerializable("date");
                    if (date != null) {
                        this.date_time = date;
                        onTimeSelected(true, date_time, tv_details_time, tv_details_time_selected);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param isSelected
     * @param date         当前时间
     * @param todayTime
     * @param electionTime 点击的是时间控件
     */
    private void onTimeSelected(boolean isSelected, Date date, TextView todayTime, TextView electionTime) {
        String[] SelectedStrings = DateUtil.getTimeSelectedStrings(isSelected, date);
        todayTime.setText(SelectedStrings[0]);
        electionTime.setText(SelectedStrings[1]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            isCanBackShowToast();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void isCanBackShowToast() {
        String input_text = StringUtils.getInputText(input_content);
        if (!TextUtils.isEmpty(input_text) || FileUtil.getfileLenth(uri)) {
            showSimpleAlertDialog("确定退出此次编辑?", "", false, "确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog();
                    finish();
                }
            }, "取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                }
            });
        } else {
            finish();
        }
    }


}
