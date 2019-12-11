package com.yiqiji.money.modules.homeModule.home.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.control.RippleBackground;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.PermissionUtil;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.write.activity.DetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yiqiji.money.R.id.ripple_bg;

/**
 * Created by dansakai on 2017/7/14.
 * 语音输入记账页
 */

public class SpeechRecognizerActivity extends BaseActivity {

    @BindView(R.id.tv_pressSay)
    TextView tvPressSay;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.tv_lisState)
    TextView tvLisState;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(ripple_bg)
    RippleBackground rippleBg;
    // 语音听写对象
    private SpeechRecognizer mSpeechRecognizer;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private boolean IS_CANCLE_SEND = false;//是否取消记账
    private int screenheight;//

    private BooksDbInfo booksDbInfo;
    private String user_name;
    private String memberid;
    private Date date_time;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognizer);
        ButterKnife.bind(this);
        screenheight = UIHelper.getDisplayHeight(this);
        intent = getIntent();
        booksDbInfo = intent.getParcelableExtra("booksDbInfo");
        user_name = intent.getStringExtra("user_name");
        memberid = intent.getStringExtra("memberid");
        date_time = (Date) intent.getExtras().getSerializable("date_time");
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(SpeechRecognizerActivity.this, mInitListener);

        tvPressSay.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int Y = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String[] permissions = {Manifest.permission.RECORD_AUDIO};
                        PermissionUtil.requestPermisions(SpeechRecognizerActivity.this, 1, permissions, new PermissionUtil.RequestPermissionListener() {
                            @Override
                            public void onRequestPermissionSuccess() {
                                startListening();
                            }

                            @Override
                            public void onRequestPermissionFail(int[] grantResults) {
                                showToast("请开启录音权限");
                                return;
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        tvPressSay.setPressed(true);
                        //如果取消发送
                        rippleBg.stopRippleAnimation();
                        tvPressSay.setText("按住说话");
                        tvExplain.setVisibility(View.GONE);
                        if (IS_CANCLE_SEND) {
                            //TODO 取消发送
                            mSpeechRecognizer.cancel();
                            tvLisState.setText("按住说话 松开记账");
                        }
                        //如果发送
                        else {
                            //TODO 发送
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //位置判断在方框范围以内
                        if (Y >= screenheight - UIHelper.dip2px(mContext, 64)) {
                            tvPressSay.setText("松开 结束");
                            IS_CANCLE_SEND = false;
                            tvExplain.setVisibility(View.VISIBLE);
                            tvExplain.setText("手指上滑，取消记账");
                            tvExplain.setBackgroundColor(Color.parseColor("#605F5F"));
                        } else {
                            tvPressSay.setText("松开 取消");
                            IS_CANCLE_SEND = true;
                            tvExplain.setText("松开手指，取消记账");
                            tvExplain.setBackgroundColor(Color.parseColor("#8C343C"));
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 开起语音听写
     */
    private void startListening() {
        ivVoice.setImageResource(R.drawable.icon_voice);
        tvLisState.setText("正在听...");
        tvPressSay.setPressed(true);
        tvPressSay.setText("松开 结束");
        tvExplain.setVisibility(View.VISIBLE);
        tvExplain.setText("手指上滑，取消记账");
        tvExplain.setBackgroundColor(Color.parseColor("#605F5F"));
        rippleBg.startRippleAnimation();
        mSpeechRecognizer.startListening(mRecognizerListener);
    }

    /**
     * 打开当前activity
     * @param mContext
     * @param booksDbInfo
     * @param user_name
     * @param memberid
     * @param date_time
     */
    public static void open(Context mContext, BooksDbInfo booksDbInfo, String user_name, String memberid, Date date_time) {
        Intent intent = new Intent(mContext, SpeechRecognizerActivity.class);
        intent.putExtra("booksDbInfo", booksDbInfo);
        intent.putExtra("user_name", user_name);
        intent.putExtra("memberid", memberid);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date_time", (Serializable) date_time);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败，错误码：" + code);
            }
        }
    };


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        @Override
        public void onError(SpeechError error) {
            onFailureView();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //此处后期可以增加正在语音识别动画
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            if (isLast) {
                // TODO 最后的结果
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }
                requestAnalyze(resultBuffer.toString());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    /**
     * 请求语义解析
     */
    private void requestAnalyze(String sayStr) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cid", booksDbInfo.getAccountbookcate());
        map.put("text", sayStr);
        CommonFacade.getInstance().exec(Constants.ANALYZE_SEGMANTE, map, new ViewCallBack() {

            @Override
            public void onSuccess(Object o) throws Exception {
                JSONObject jo_main = (JSONObject) o;
                JSONObject jo_data = jo_main.optJSONObject("data");
                if (jo_data != null) {
                    long time = Long.parseLong(jo_data.optString("tradetime"));
                    Date data = new Date(time);
                    DetailsActivity.startActivity(mContext, booksDbInfo, user_name, memberid, data, jo_data.optString("billmark"), jo_data.optString("categoryid"), jo_data.optString("categorytitle"), jo_data.optString("billamount"), true);
                    finish();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                onFailureView();
            }
        });
    }

    private void onFailureView() {
        ivVoice.setImageResource(R.drawable.icon_question);
        tvLisState.setText("没听清...");
        tvPressSay.setPressed(false);
        tvPressSay.setText("按住说话");
        tvExplain.setVisibility(View.GONE);
        rippleBg.stopRippleAnimation();
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, null);

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "0");

    }

    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);
    }

    public String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    @OnClick({R.id.iv_cancle, R.id.iv_signOne})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancle:
                finish();
                break;
            case R.id.iv_signOne://记一笔
                if (booksDbInfo == null) {
                    return;
                }
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_NOTE_ONE);
                DetailsActivity.startActivity(mContext, booksDbInfo, user_name, memberid, date_time);
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mSpeechRecognizer) {
            // 退出时释放连接
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
        }
    }
}
