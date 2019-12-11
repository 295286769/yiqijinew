package com.yiqiji.money.modules.homeModule.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.InternetUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.UserHeadImageView;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.activity.GroupMembersActivity;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.activity.ManuallyAddActivity;
import com.yiqiji.money.modules.homeModule.home.activity.NumberDtailActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.NotesAdapter;
import com.yiqiji.money.modules.homeModule.home.homeinterface.DeletMembersInterface;
import com.yiqiji.money.modules.homeModule.home.homeinterface.NotesItemClick;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/27.
 * address huang.weishui@71dai.com
 */
public class MembersHorizontalScrollView extends RelativeLayout {
    @BindView(R.id.head_layout)
    LinearLayout headLayout;//账本成员
    @BindView(R.id.head_add_layout)
    LinearLayout headAddLayout;//添加账本成员
    @BindView(R.id.horizontalScroll)
    HorizontalScrollView horizontalScroll;
    @BindView(R.id.go_member)
    TextView goMember;//去群组成员
    @BindView(R.id.mumber_layout)
    LinearLayout mumberLayout;//
    private Context mContext;
    private DeletMembersInterface deletMembersInterface;
    private View mumber_layout;
    private BooksDbInfo booksDbInfo;
    private List<BooksDbMemberInfo> booksDbMemberInfos;
    private String memberid;
    private Date date_time;
    private MyPopuwindows myPopuwindows = null;
    BooksDbMemberInfo booksDbMemberInfo;
    private NotesAdapter notesAdapter;
    private int view_item_with;// 成员在屏幕显示宽度
    private int screeWith = 0;//屏幕宽度

    public void setDeletMembersInterface(DeletMembersInterface deletMembersInterface) {
        this.deletMembersInterface = deletMembersInterface;
    }

    public MembersHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MembersHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MembersHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        screeWith = XzbUtils.getPhoneScreen((Activity) mContext).widthPixels;
        view_item_with = (int) (((screeWith - screeWith / 4) - UIHelper.Dp2Px(mContext, 5) * 5) / 4);
        initView();
        setOnClick();
    }

    private void setOnClick() {
        if (booksDbInfo != null) {
            headAddLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_ADD_MEMBER);
                    Intent intent = new Intent(mContext, ManuallyAddActivity.class);
                    intent.putExtra("id", booksDbInfo.getAccountbookid());
                    intent.putExtra("inviteid", memberid);
                    mContext.startActivity(intent);
                }
            });
            goMember.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    XzbUtils.hidePointInUmg(mContext, Constants.HIDE_GROUP_MEMBER);
                    GroupMembersActivity.startActivity(mContext, booksDbInfo.getAccountbookid());
                }
            });
        }

    }

    private void initView() {
        mumber_layout = LayoutInflater.from(mContext).inflate(R.layout.activity_members_horizontalscroll, this, true);
        ButterKnife.bind(this, mumber_layout);

    }

    public void setViewVisible(int visible) {
        if (mumberLayout != null) {
            mumberLayout.setVisibility(VISIBLE);
        }
    }

    public void setBooksDbMemberInfos(BooksDbInfo booksDbInfo, List<BooksDbMemberInfo> booksDbMemberInfos, Date date_time) {
        this.booksDbInfo = booksDbInfo;
        this.booksDbMemberInfos = booksDbMemberInfos;
        this.date_time = date_time;
        initMumber();
    }

    private void initMumber() {

        if (headLayout == null || booksDbMemberInfos == null) {
            return;
        }
        goMember.setText("共" + booksDbMemberInfos.size() + "人");
        memberid = ((HomeActivity) mContext).getMySelfNyMemeberId(booksDbInfo.getMyuid(), booksDbMemberInfos);
        headLayout.removeAllViews();
        for (int j = 0; j < booksDbMemberInfos.size(); j++) {
            BooksDbMemberInfo booksDbMemberInfo = booksDbMemberInfos.get(j);
            String deviceid = LoginConfig.getInstance().getDeviceid();
            String tokenid = LoginConfig.getInstance().getTokenId();
            String userid = LoginConfig.getInstance().getUserid();
            if (TextUtils.isEmpty(tokenid)) {
                if (booksDbMemberInfo.getDeviceid().equals(deviceid)) {
                    BooksDbMemberInfo booksDbMemberIn = booksDbMemberInfos.get(0);
                    booksDbMemberInfos.set(0, booksDbMemberInfo);
                    booksDbMemberInfos.set(j, booksDbMemberIn);
                }

            } else {
                if (booksDbMemberInfo.getUserid().equals(userid)) {
                    BooksDbMemberInfo booksDbMemberIn = booksDbMemberInfos.get(0);
                    booksDbMemberInfos.set(0, booksDbMemberInfo);
                    booksDbMemberInfos.set(j, booksDbMemberIn);
                }
            }
        }

        //TODO 这里做头像的显示
        boolean isNeedShowMine = ((HomeActivity) mContext).getIsNeedShowMine();//成员头像是否需要显示我的

        for (int i = 0; i < booksDbMemberInfos.size(); i++) {
            View view_item = LayoutInflater.from(mContext).inflate(R.layout.activity_add_mumber_item, null);
            UserHeadImageView head_view = (UserHeadImageView) view_item.findViewById(R.id.head_view);// 成员头像
            TextView head_name = (TextView) view_item.findViewById(R.id.head_name);
            TextView mine_text = (TextView) view_item.findViewById(R.id.mine_text);
            String headNmae = booksDbMemberInfos.get(i).getUsername();

            if (i == 0 && isNeedShowMine) {
                mine_text.setVisibility(View.VISIBLE);
            }

            //加载头像
            head_view.displayImage(booksDbMemberInfos.get(i));

            head_name.setSingleLine(true);
            head_name.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            head_name.setText(headNmae);

            view_item.setTag(i);
            view_item.setId(R.id.member_id);

            onMemberClick(view_item);
            onMemberLongClick(view_item);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = view_item_with;
            layoutParams.rightMargin = UIHelper.Dp2Px(mContext, 5);
            layoutParams.gravity = Gravity.CENTER;
            view_item.setLayoutParams(layoutParams);
            headLayout.addView(view_item);

        }

    }

    private void onMemberLongClick(View view_item) {
        view_item.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                booksDbMemberInfo = booksDbMemberInfos.get(position);
                if (booksDbMemberInfo != null) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.activity_edit_member, null);
                    TextView change_memeber_titl = (TextView) view.findViewById(R.id.change_memeber_title);
                    final String memberidother = booksDbMemberInfo.getMemberid();
                    String memberName = booksDbMemberInfo.getUsername();
                    notesAdapter = new NotesAdapter(mContext, view, R.layout.activity_edit_member_item, mumberLayout);

                    if (booksDbInfo != null && booksDbMemberInfos != null) {
                        String userid = booksDbInfo.getUserid();
                        String deviceid = booksDbInfo.getDeviceid();
                        boolean isMySelf = memberid.equals(memberidother) == true ? true : false;
                        boolean isGroup = ((HomeActivity) mContext).isGroup(userid, deviceid, booksDbMemberInfo);//能否对账本成员进行操作
                        boolean isBookFount = ((HomeActivity) mContext).isBookFount(userid, deviceid);//是否是账本创建者
                        if (!isGroup) {
                            change_memeber_titl.setText("您暂无权限对账本创建者执行该操作");
                        } else {
                            change_memeber_titl.setText("请选择对" + "\"" + memberName + "\"" + "的操作");
                        }
                        notesAdapter.setNotesList(isMySelf, isGroup, isBookFount);
                    }
                    notesAdapter.setNotesItemClick(new NotesItemClick() {
                        @Override
                        public void onClickNotesItem(View view) {
                            String deletOrQuit = "";
                            int tagid = Integer.parseInt(view.getTag().toString());
                            final String memberidother = booksDbMemberInfo.getMemberid();
                            switch (tagid) {
                                case R.string.change_member://修改名片

                                    changeMemberName(memberidother);
                                    break;
                                case R.string.delet_member_name://删除成员

                                    deletOrQuit = "你确定要删除吗？";
                                    quitOrDelet(memberidother, deletOrQuit, false);

                                    break;
                                case R.string.quit_group://退出出群组
                                    deletOrQuit = "你确定要退出吗？";
                                    quitOrDelet(memberidother, deletOrQuit, true);
                                    break;
                                case R.string.cancel_text://取消
                                    break;
                                case R.string.i_known://知道了
                                    break;
                            }
                        }
                    });
                }
                return false;
            }
        });

    }

    private void onMemberClick(View view_item) {
        view_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                String user_name = booksDbMemberInfos.get(position).getUsername();
                String memberid_other = booksDbMemberInfos.get(position).getMemberid();
                String deviceid_other = booksDbMemberInfos.get(position).getDeviceid();
                String userid_other = booksDbMemberInfos.get(position).getUserid();
                NumberDtailActivity.startActivity(mContext, booksDbInfo.getAccountbookid(),
                        memberid_other, userid_other, booksDbInfo.getMyuid()
                        , booksDbInfo.getSorttype(), booksDbInfo.getIsclear(), user_name, "", date_time);
            }
        });

    }

    /**
     * 修改成员名片
     */
    private void changeMemberName(final String memberidother) {
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.change_member_name, null);
        final EditText editText = (EditText) view2.findViewById(R.id.editName);
        Button change_cancels = (Button) view2.findViewById(R.id.change_cancels);
        Button comit_change_name = (Button) view2.findViewById(R.id.comit_change_name);
        change_cancels.setText("取消");
        comit_change_name.setText("确定");

        change_cancels.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftInputMethod(editText);
                DialogUtils.dismissConfirmDialog();
            }
        });
        comit_change_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memberName = editText.getText().toString();
                if (TextUtils.isEmpty(memberName)) {
                    ToastUtils.DiyToast(mContext, "请填写成员名片");
                    return;
                }
                if (!InternetUtils.checkNet(MyApplicaction.getContext())) {
                    ((HomeActivity) mContext).showToast("无网络");
                    return;
                }

                UIHelper.hideSoftInputMethod(editText);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("id", booksDbInfo.getAccountbookid());
                hashMap.put("memberid", memberidother);
                hashMap.put("visitcard", memberName);
                CommonFacade.getInstance().exec(Constants.CHANG_MEMBER_NAME, hashMap, new ViewCallBack() {
                    @Override
                    public void onSuccess(Object o) throws Exception {
                        super.onSuccess(o);

//                        AddMemberResponse response = GsonUtil.GsonToBean(o.toString(), AddMemberResponse.class);
                        if (memberid.equals(memberidother)) {//自己
                            LoginConfig.getInstance().setIssetavatar(1);
                        }
                        booksDbMemberInfo.setUsername(memberName);
                        String where = DailycostContract.DtBookMemberColumns.BOOKID + "=? and " + DailycostContract.DtBookMemberColumns.MEMBERID + "=? ";
                        String whereStrings[] = new String[]{booksDbInfo.getAccountbookid(), memberidother};
                        DbInterface.updateBooksMember(booksDbMemberInfo, where, whereStrings);
                        String whereMember = DailycostContract.DtBookMemberColumns.BOOKID + "=? ";
                        String whereMemberStrings[] = new String[]{booksDbInfo.getAccountbookid()};
                        List<BooksDbMemberInfo> booksDbMemberInfosss = DbInterface.getListBooksDbMemberInfo(whereMember, whereMemberStrings, null, null, null, null);
                        booksDbMemberInfos = booksDbMemberInfosss;
                        booksDbInfo.setMember(booksDbMemberInfos);
                        goMember.setText("共" + booksDbMemberInfos.size() + "人");
                        initMumber();

                    }

                    @Override
                    public void onFailed(SimpleMsg simleMsg) {
                        super.onFailed(simleMsg);
                        ((HomeActivity) mContext).showToast(simleMsg);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        ((HomeActivity) mContext).showLoadingDialog();
                        UIHelper.hideSoftInputMethod(editText);
                        DialogUtils.dismissConfirmDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        DialogUtils.dismissConfirmDialog();
                        ((HomeActivity) mContext).dismissDialog();
                    }
                });
            }
        });
        DialogUtils.showDialog((HomeActivity) mContext, view2);
        XzbUtils.show(editText, mContext);
    }

    private void quitOrDelet(final String memberidother, final String deletOrQuit, final boolean isDeletQuit) {
        ((HomeActivity) mContext).showSimpleAlertDialog("提示", deletOrQuit, false, "确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).dismissDialog();
                HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("id", booksDbInfo.getAccountbookid());
                hashMap.put("memberid", memberidother);

                if (isDeletQuit) {//退出群组
                    quite(hashMap);
                } else {
                    delet(hashMap, memberidother);
                }
            }
        }, "取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).dismissDialog();
            }
        });

    }

    /**
     * 退出群
     *
     * @param hashMap
     */
    private void quite(HashMap<String, String> hashMap) {
        CommonFacade.getInstance().exec(Constants.QUIT_MEMBER_NAME, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                DialogUtils.dismissConfirmDialog();
                ((HomeActivity) mContext).showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                deletMembersInterface.quitMemberDeleBook(booksDbInfo.getAccountbookid());
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    ((HomeActivity) mContext).showToast(simleMsg);
                } else {
                    ((HomeActivity) mContext).showSimpleAlertDialog("提示", simleMsg.getErrMsg(), "确定", false, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity) mContext).dismissDialog();
                        }
                    });

                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((HomeActivity) mContext).dismissDialog();

            }
        });

    }

    /**
     * 删除成员
     *
     * @param hashMap
     * @param memberidother
     */
    private void delet(HashMap<String, String> hashMap, final String memberidother) {
        CommonFacade.getInstance().exec(Constants.DELET_MEMBER_NAME, hashMap, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
//                DialogUtils.dismissConfirmDialog();
                ((HomeActivity) mContext).showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);

                String where = DailycostContract.DtBookMemberColumns.BOOKID + "=? and " + DailycostContract.DtBookMemberColumns.MEMBERID + "=? ";
                String whereStrings[] = new String[]{booksDbInfo.getAccountbookid(), memberidother};
                DbInterface.deletBooksMember(where, whereStrings);
                String whereMember = DailycostContract.DtBookMemberColumns.BOOKID + "=? ";
                String whereMemberStrings[] = new String[]{booksDbInfo.getAccountbookid()};
//                List<BooksDbMemberInfo> booksDbMemberInfosss = DbInterface.getListBooksDbMemberInfo(whereMember, whereMemberStrings, null, null, null, null);
                for (int i = 0; i < booksDbMemberInfos.size(); i++) {
                    String memeberid = booksDbMemberInfos.get(i).getMemberid();
                    if (memeberid.equals(memberidother)) {
                        booksDbMemberInfos.remove(i);
                        break;
                    }
                }
//                booksDbMemberInfos = booksDbMemberInfosss;
                booksDbInfo.setMember(booksDbMemberInfos);
                goMember.setText("共" + booksDbMemberInfos.size() + "人");
                initMumber();


            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -100 || simleMsg.getErrCode() == -110) {
                    ((HomeActivity) mContext).showToast(simleMsg);
                } else {
                    ((HomeActivity) mContext).showSimpleAlertDialog("提示", simleMsg.getErrMsg(), "确定", false, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity) mContext).dismissDialog();
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((HomeActivity) mContext).dismissDialog();
            }
        });
    }


}
