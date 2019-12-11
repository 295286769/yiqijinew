package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.GsonUtil;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class SignAddMemberActivity extends BaseActivity implements OnClickListener {
    private TextView add;
    private TextView edit_hint;
    private int idClear = 0;
    // 账本ID
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_addmember);
        id = getIntent().getStringExtra("id");
        initView();
    }

    InputMethodManager inputMgr;

    private void initView() {
        edit_hint = (TextView) findViewById(R.id.edit_name);
        add = (TextView) findViewById(R.id.add);
        initClick();
        initTitle("手动添加",new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                    case R.id.layout_title_view_return:
                        outkeKyboard();
                        finish();
                        break;

                    default:
                        break;
                }

            }
        });
        inputMgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
    }

    private void initClick() {
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add:
                if (inputMgr.isActive()) {
                    // 隐藏软键盘
                    inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

                String name = edit_hint.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.DiyToast(MyApplicaction.getContext(), "你还没有添加名字");
                    return;
                }
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    ToastUtils.DiyToast(MyApplicaction.getContext(), "请检查网络连接");
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("visitcard", name);
                CommonFacade.getInstance().exec(Constants.ADD_MEMBER, hashMap, new ViewCallBack() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showLoadingDialog();
                    }

                    @Override
                    public void onSuccess(Object o)throws Exception {
                        super.onSuccess(o);
                        AddMemberResponse addMemberResponse = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                        EventBus.getDefault().post(addMemberResponse);
                        showToast("添加成功");
                        finish();
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

                break;

            default:
                break;
        }
    }

    private boolean outkeyboard = true;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        outkeyboard = false;
    }

    private void outkeKyboard() {
        if (outkeyboard) {
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
        }
    }
}
