package com.yiqiji.money.modules.myModule.my.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.request.ApiService;
import com.yiqiji.money.modules.common.request.RetrofitInstance;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/22.
 */
public class QuestionReturnActivity extends BaseActivity implements OnClickListener {
    private ApiService apiService;
    private EditText et_question_context;
    private EditText et_contact_type;
    InputMethodManager inputMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_return);
        MyApplicaction.getInstence().addActivity(this);
        initView();
        apiService = RetrofitInstance.get().create(ApiService.class);
    }

    private void initView() {
        inputMgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        et_question_context = (EditText) findViewById(R.id.et_question_context);
        et_contact_type = (EditText) findViewById(R.id.et_contact_type);
        initTitle("问题反馈", -1, "发送", this);
    }


    /**
     * 发送反馈
     */
    private void getFeedBackSave(String content, String contact) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("content", content);
        hashMap.put("contact", contact);
        CommonFacade.getInstance().exec(Constants.FEEDBACK_SAVE, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);

            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
                showToast("反馈成功");
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

//        HashMap<String, String> hashMap = XzbUtils.mapParmas();
//
//        String[] params = new String[]{"tokenid", "content", "plat", "deviceid", "appver", "osver", "machine"};
//
//        String[] valus = new String[]{hashMap.get("tokenid"), content, hashMap.get("plat"), hashMap.get("deviceid"),
//                hashMap.get("appver"), hashMap.get("osver"), hashMap.get("machine")};
//
//        String sign = StringUtils.setSign(valus, params);
//        hashMap.put("sign", sign);
//        hashMap.put("content", content);
//
//        apiService.feedBackSave(hashMap).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Response<ResponseBody> arg0, Retrofit arg1) {
//                // TODO Auto-generated method stub
//                try {
//                    JSONObject object = new JSONObject(arg0.body().string());
//                    if (object.getInt("code") == 0) {
//                        inputMgr.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 关闭软件盘
//                        showToast("反馈成功");
//                        finish();
//                    } else {
//                        showMyDialog(null, object.getString("msg"), null, "好的", null, null);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable arg0) {
//
//            }
//        });
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_title_view_right_btn) {
            String context = et_question_context.getText().toString();
            String contact = et_contact_type.getText().toString();
            if (TextUtils.isEmpty(context)) {
                showToast("请输入反馈内容");
                return;
            }
            getFeedBackSave(context, contact);
        }
        if (v.getId() == R.id.layout_title_view_return) {
            outkeKyboard();
            finish();
        }
    }
}
