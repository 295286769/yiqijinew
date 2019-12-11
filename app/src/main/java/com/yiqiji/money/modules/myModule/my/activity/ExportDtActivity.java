package com.yiqiji.money.modules.myModule.my.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.myModule.my.adapter.SelDataEntity;
import com.yiqiji.money.modules.myModule.my.entity.BooksEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/6/28.
 * 导出账本数据页面
 */

public class ExportDtActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tvBook)
    TextView tvBook;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.etEmail)
    EditText etEmail;

    private String bookId;//账本id
    private String limit = "0";//期限类型默认所有时间
    private String sdate;//开始时间
    private String edate;//结束时间

    private String emailAddr = "";//本地存储的邮箱地址

    private List<BooksEntity.AccountbookBean> list = new ArrayList<>();

    public static final int REQUEST_SELCEBOOK = 10100;
    public static final int REQUEST_SELCETIME = 10110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    /**
     * 获取账本列表数据
     */
    private void loadData() {
        CommonFacade.getInstance().exec(Constants.HOME_LIST, new ViewCallBack<BooksEntity>() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(BooksEntity booksEntity) throws Exception {
                List<BooksEntity.AccountbookBean> lisBean = booksEntity.getAccountbook();
                if (!StringUtils.isEmptyList(lisBean)) {
                    list.clear();
                    list.addAll(lisBean);
                    list.get(0).setIsCheck(1);
                    BooksEntity.AccountbookBean curBean = lisBean.get(0);
                    tvBook.setText(curBean.getAccountbooktitle());
                    bookId = curBean.getAccountbookid();
                }
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });

    }

    private void initView() {
        tvTitle.setText("导出数据");
        tvTime.setText("所有时间");
        emailAddr = SPUtils.getParam(LoginConfig.getInstance().getUserid() + "exportEmailaddr", "");
        if (!StringUtils.isEmpty(emailAddr)) {
            etEmail.setText(emailAddr);
            etEmail.setSelection(emailAddr.length());
        }
    }

    /**
     * 开启当前activity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, ExportDtActivity.class);
        context.startActivity(intent);
    }

    /**
     * 确认发送
     */
    private void sendData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", bookId);//账本id
        map.put("email", etEmail.getText().toString());//用于接受账本导出邮箱
        map.put("limit", limit);//期限类型：0.所有账单；1.本月账单；2.上个月账单；3.今年的账单；4.自定义时间账单
        if ("4".equals(limit)) {
            map.put("sdate", sdate);//开始时间2017-05-01
            map.put("edate", edate);//结束时间
        }
        CommonFacade.getInstance().exec(Constants.BOOKS_EXPORT, map, new ViewCallBack() {
            @Override
            public void onStart() {
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                showToast("数据发送成功，请稍后查收邮件");
                finish();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                showToast(simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        });

        /**
         * 记住当前输入的邮箱
         */
        SPUtils.setParam(LoginConfig.getInstance().getUserid() + "exportEmailaddr", etEmail.getText().toString());
    }

    @OnClick({R.id.iv_back, R.id.tvSend, R.id.rl_selBook, R.id.rl_selTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tvSend:
                if (!StringUtils.isEmail(etEmail.getText().toString())) {
                    showToast("请输入正确的邮箱地址");
                    return;
                }
                sendData();
                break;
            case R.id.rl_selBook://选择账本
                SelectBookActivity.open(this, REQUEST_SELCEBOOK, list);
                break;
            case R.id.rl_selTime://选择时间区间
                SelectDataActivity.open(this, REQUEST_SELCETIME, limit);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELCETIME) {//选择时间区间返回
            SelDataEntity entity = data.getParcelableExtra("SelData");
            limit = entity.getLimit();
            tvTime.setText(entity.getTitle());
            if ("4".equals(limit)) {
                sdate = data.getStringExtra("sTime");
                edate = data.getStringExtra("eTime");
            }
        }
        if (requestCode == REQUEST_SELCEBOOK) {//选择账本返回
            BooksEntity.AccountbookBean bean = data.getParcelableExtra("SelBook");
            tvBook.setText(bean.getAccountbooktitle());
            bookId = bean.getAccountbookid();
            for (int i = 0; i < list.size(); i++) {
                if (bean.getAccountbookid().equals(list.get(i).getAccountbookid())) {
                    list.get(i).setIsCheck(1);
                } else {
                    list.get(i).setIsCheck(0);
                }
            }
        }
    }
}
