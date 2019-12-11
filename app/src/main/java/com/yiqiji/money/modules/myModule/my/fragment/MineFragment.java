package com.yiqiji.money.modules.myModule.my.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.entity.UserInfo;
import com.yiqiji.money.modules.common.fragment.LazyFragment;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.SPUtils;
import com.yiqiji.money.modules.common.utils.ShareUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.myModule.login.activity.AccountActivity;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;
import com.yiqiji.money.modules.myModule.my.activity.AboutXzbActivity;
import com.yiqiji.money.modules.myModule.my.activity.AccountRemindActivity;
import com.yiqiji.money.modules.myModule.my.activity.AttAccountActivity;
import com.yiqiji.money.modules.myModule.my.activity.QuestionReturnActivity;
import com.yiqiji.money.modules.myModule.my.activity.SettingActivity;

import de.greenrobot.event.EventBus;

public class MineFragment extends LazyFragment implements View.OnClickListener {
    private Context mContext;
    private View view;

    private RelativeLayout rl_setting_question_return;
    private RelativeLayout rl_setting_account;
    private RelativeLayout rl_setting_about;
    private RelativeLayout rl_invitation;
    private RelativeLayout rl_remind;
    private RelativeLayout rl_setting_praise;
    private TextView tv_type;
    private RelativeLayout rl_attention;//我关注的账本
    private TextView tv_accuntNm;

    private RelativeLayout rl_setting;
    private CircleImageView image_head;
    private Bitmap bitmap;

    private TextView tv_my_name;
    private TextView tv_my_mobile;
    private TextView tv_mine_login_state;
    private RelativeLayout rl_mine_login;
    private UserInfo userInfo;

    private String[] shareText = new String[]{"新浪微博", "微信", "朋友圈", "QQ", "复制链接"};
    private int[] share_icon = new int[]{R.drawable.weibo_icon, R.drawable.chart_icon, R.drawable.frend, R.drawable.qq, R.drawable.copy_icon};
    private SHARE_MEDIA[] share_medias = new SHARE_MEDIA[]{SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ};

    @Override
    public void onCreate(Bundle savedInstanceState) {//保存数据防止fragment切换重叠
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mind, null);
        }
        init(view);

        return view;
    }


    /**
     * 获取订阅账本数
     */
    private void getSubscriBookAccount() {

        CommonFacade.getInstance().exec(Constants.CHECK_MESSAGE, new ViewCallBack<CheckMessageInfo>() {
            @Override
            public void onSuccess(CheckMessageInfo o) throws Exception {
                super.onSuccess(o);
                CheckMessageInfo checkMessageInfo = o;
                CheckMessageInfo.MessgeinfoItem messgeinfoItem = checkMessageInfo.getData();
                tv_accuntNm.setText(messgeinfoItem.getSubscribe() + "本");

            }
        });
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "isRemind", true)) {
            tv_type.setText((String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天"));
        } else {
            tv_type.setText("关闭");
        }
        getSubscriBookAccount();
        String tokenId = LoginConfig.getInstance().getTokenId();
        if (TextUtils.isEmpty(tokenId)) {
            tv_mine_login_state.setVisibility(View.VISIBLE);
            rl_mine_login.setVisibility(View.GONE);
            image_head.setImageResource(R.drawable.me_icon);
        } else {
            tv_mine_login_state.setVisibility(View.GONE);
            rl_mine_login.setVisibility(View.VISIBLE);
            bitmap = LoginConfig.getInstance().getImage_head();
            if (bitmap != null) {
                image_head.setImageBitmap(bitmap);
            } else {
                XzbUtils.displayImageHead(image_head, LoginConfig.getInstance().getUsericon(), 0);
            }
            tv_my_mobile.setText(StringUtils.getStarsMobile(LoginConfig.getInstance().getMobile()));
            tv_my_name.setText(LoginConfig.getInstance().getUserName());
        }
        if (!TextUtils.isEmpty(tokenId)) {
            getUserinfo(tokenId, false);
            rl_attention.setVisibility(View.VISIBLE);
        } else {
            rl_attention.setVisibility(View.GONE);
        }
    }

    private void init(View view) {


        MyApplicaction.getInstence().addActivity(getActivity());

        tv_my_name = (TextView) view.findViewById(R.id.tv_my_name);
        tv_my_mobile = (TextView) view.findViewById(R.id.tv_my_mobile);

        tv_mine_login_state = (TextView) view.findViewById(R.id.tv_mine_login_state);
        rl_mine_login = (RelativeLayout) view.findViewById(R.id.rl_mine_login);

        rl_setting_question_return = (RelativeLayout) view.findViewById(R.id.rl_setting_question_return);
        rl_setting_account = (RelativeLayout) view.findViewById(R.id.rl_setting_account);
        rl_setting_about = (RelativeLayout) view.findViewById(R.id.rl_setting_about);
        rl_invitation = (RelativeLayout) view.findViewById(R.id.rl_invitation);// 要请好友一起记
        rl_remind = (RelativeLayout) view.findViewById(R.id.rl_remind);// 记账提醒
        rl_setting_praise = (RelativeLayout) view.findViewById(R.id.rl_setting_praise);// 好评鼓励
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        if (SPUtils.getParam(LoginConfig.getInstance().getUserid() + "isRemind", true)) {
            tv_type.setText((String) SPUtils.getParam(LoginConfig.getInstance().getUserid() + "remindType", "每天"));
        } else {
            tv_type.setText("关闭");
        }

        rl_attention = (RelativeLayout) view.findViewById(R.id.rl_attention);
        tv_accuntNm = (TextView) view.findViewById(R.id.tv_accuntNm);

        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        image_head = (CircleImageView) view.findViewById(R.id.iv_my_usericon);


        rl_setting_question_return.setOnClickListener(this);
        rl_setting_account.setOnClickListener(this);
        rl_setting_about.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_invitation.setOnClickListener(this);// 要请好友一起记
        rl_remind.setOnClickListener(this);// 要请好友一起记
        rl_setting_praise.setOnClickListener(this);
        rl_attention.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_attention://我关注的账本
                //TODO　暂时屏蔽
                startActivity(new Intent(mContext, AttAccountActivity.class));
                break;
            case R.id.rl_setting_account:
                if (TextUtils.isEmpty(LoginConfig.getInstance().getTokenId())) {
                    LoginBaseActivity.openActivity(mContext);
//                    IntentUtils.startActivityOnLogin(getActivity(), IntentUtils.LoginIntentType.MINE);
                    return;
                }
                if (userInfo != null) {//跳转到个人信息
                    XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_USER_INFO);
                    intent = new Intent(getActivity(), AccountActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("userInfo", userInfo);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RequsterTag.REQUESCODE_PICTURELIST);
                } else {
                    String tokenId = LoginConfig.getInstance().getTokenId();
                    getUserinfo(tokenId, true);
                }
                break;
            case R.id.rl_setting_question_return://问题反馈
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_PROBLEM_FADBACK);
                intent = new Intent(getActivity(), QuestionReturnActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_remind://记账提醒
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_NOTE_REMIND);
                intent = new Intent(getActivity(), AccountRemindActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_about:
                intent = new Intent(getActivity(), AboutXzbActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting://设置
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_SETTING);
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_invitation: // 邀请好友一起记
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_INVITE_FRIEND);
                ShareUtil.shareMeth(getActivity(), rl_setting, shareText, share_icon, share_medias, LoginConfig.share_url, LoginConfig.share_recommend_friend_title, LoginConfig.share_recommend_friend_text, UIHelper.getDisplayWidth(getActivity()), true, null, R.drawable.icon);
                break;
            case R.id.rl_setting_praise: // 好评鼓励
                XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_GOOD_COMMENT);
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RequsterTag.REQUESCODE_PICTURELIST:
                bitmap = LoginConfig.getInstance().getImage_head();
                if (bitmap != null) {
                    image_head.setImageBitmap(bitmap);
                }
                break;

            default:
                break;
        }
    }

    /**
     * * 获取个人信息
     *
     * @param tokenid
     */
    private void getUserinfo(String tokenid, boolean flage) {

        CommonFacade.getInstance().exec(Constants.GET_USER_INFO, new ViewCallBack<UserInfo>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(UserInfo o) throws Exception {
                super.onSuccess(o);
                userInfo = o;
                tv_my_mobile.setText(StringUtils.getStarsMobile(userInfo.getMobile()));
                tv_my_name.setText(userInfo.getUsername());

                XzbUtils.displayImageHead(image_head, userInfo.getUsericon(), 0);
                LoginConfig.getInstance().setUsericon(userInfo.getUsericon());
                LoginConfig.getInstance().setUserName(userInfo.getUsername());
                LoginConfig.getInstance().setMobile(userInfo.getMobile());
                LoginConfig.getInstance().setUserid(userInfo.getUid());
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                ((HomeActivity) mContext).showToast(simleMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || view == null) {
            return;
        }

    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if (view != null) {

            ((ViewGroup) view.getParent()).removeView(view);
        }
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(String refreshMine) {

        if (refreshMine.equals(RequsterTag.RefreshMine)) {
            onResume();
        }
    }

}
