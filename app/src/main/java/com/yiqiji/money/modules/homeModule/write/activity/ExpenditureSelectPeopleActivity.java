package com.yiqiji.money.modules.homeModule.write.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.utils.DownUrlUtil;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.homeModule.home.activity.ManuallyAddActivity;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ExpenditureSelectPeopleActivity extends BaseActivity {
    private LinearLayout ll_expend_add_people;
    private CheckBox cb_selectAll;

    private LinearLayout ll_add_new_people;
    private String bookId;
    private String[] Member_Id;
    private String[] Memberid_Money;
    private boolean[] Memberid_Money_isChange;
    private List<CheckBox> mAllBoxList = new ArrayList<>();
    private boolean tag = false;
    private String MyMemberid;

    private RelativeLayout ll_selectAll;
    private int checkPeopleCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_select_people);
        EventBus.getDefault().register(this);

        bookId = getIntent().getStringExtra("bookId");
        Member_Id = getIntent().getStringArrayExtra("memberid");
        Memberid_Money = getIntent().getStringArrayExtra("Memberid_Money");
        Memberid_Money_isChange = getIntent().getBooleanArrayExtra("Memberid_Money_isChange");

        MyMemberid = getIntent().getStringExtra("MyMemberid");
        String peopleType = getIntent().getStringExtra("peopleType");
        initTitle("选择" + peopleType, "确定" + "(" + Member_Id.length + ")", new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.layout_title_view_right_btn:
                        if (checkPeopleCount <= 0) {
                            return;
                        }

                        List<BooksDbMemberInfo> resuleListData = getCheckBokIsCheckedObject();

                        for (int i = 0; i < Member_Id.length; i++) {
                            for (int j = 0; j < resuleListData.size(); j++) {
                                if (resuleListData.get(j).getMemberid().equals(Member_Id[i])) {
                                    resuleListData.get(j).setBalance(Double.valueOf(Memberid_Money[i]));
                                    resuleListData.get(j).setChange(Memberid_Money_isChange[i]);
                                }
                            }
                        }

                        Intent intent = new Intent(ExpenditureSelectPeopleActivity.this, ExpenditureActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("BooksDbMemberInfo", (ArrayList<? extends Parcelable>) resuleListData);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case R.id.layout_title_view_return:
                        onBack();
                        break;
                    default:
                        break;
                }

            }
        });

        //添加成员监听
        ll_expend_add_people = (LinearLayout) findViewById(R.id.ll_expend_add_people);
        ll_add_new_people = (LinearLayout) findViewById(R.id.ll_add_new_people);
        ll_add_new_people.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(ExpenditureSelectPeopleActivity.this, ManuallyAddActivity.class);
                in.putExtra("id", bookId);
                in.putExtra("inviteid", MyMemberid);
                mAllBoxList.clear();
                mAllBoxList.addAll(checkBoxList);
                onselectData.clear();
                onselectData.addAll(getCheckBokIsCheckedObject());
                startActivity(in);
            }
        });

        ll_selectAll = (RelativeLayout) findViewById(R.id.ll_selectAll);
        ll_selectAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_selectAll.isChecked()) {
                    cb_selectAll.setChecked(false);
                } else {
                    cb_selectAll.setChecked(true);
                }
                for (int i = 0; i < checkBoxList.size(); i++) {
                    if (cb_selectAll.isChecked()) {
                        checkBoxList.get(i).setChecked(true);
                        checkPeopleCount = checkBoxList.size();
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.main_back);
                    } else {
                        checkBoxList.get(i).setChecked(false);
                        checkPeopleCount = 0;
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.secondary_text);
                    }
                }

            }
        });
        cb_selectAll = (CheckBox) findViewById(R.id.cb_selectAll);
        getNativePeopleByBook();
    }

    /**
     * 初始化成员
     */
    private void getNativePeopleByBook() {
        DownUrlUtil.getPeopleByBook(bookId, handler, 1);
    }

    List<CheckBox> remainingList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    mDatalist.clear();
                    checkBoxList.clear();
                    List<BooksDbMemberInfo> lis = (List<BooksDbMemberInfo>) msg.obj;
                    mDatalist.addAll(lis);

                    if (!tag) {
                        initData(mDatalist, true);
                    } else {
                        initData(mDatalist, false);
                    }
                    tag = true;

                    if (mAllBoxList.size() < 1) {
                        return;
                    }
                    remainingList.clear();
                    remainingList.addAll(checkBoxList);

                    // 添加成员后将新添加的成员查出来，设置选中
                    for (int i = 0; i < remainingList.size(); i++) {
                        for (int j = 0; j < mAllBoxList.size(); j++) {
                            if (remainingList.get(i).getTag() == mAllBoxList.get(j).getTag()) {
                                // remainingList.remove(
                                // mAllBoxList.get(j).getTag());
                                // 不能应Tag代替下标来删除，只能删除当前i
                                remainingList.remove(i);
                            }
                        }
                    }

                    for (int i = 0; i < remainingList.size(); i++) {
                        remainingList.get(i).setChecked(true);
                    }

                    setAllCheck();


                    break;
                default:
                    break;
            }
        }
    };
    private List<CheckBox> checkBoxList = new ArrayList<>();

    //查询出账本下的人员
    List<BooksDbMemberInfo> mDatalist = new ArrayList<>();

    List<BooksDbMemberInfo> onselectData = new ArrayList<>();

    private void initData(List<BooksDbMemberInfo> mDatalist, boolean flage) {
        ll_expend_add_people.removeAllViews();

        BooksDbMemberInfo mBooksDbMemberInfo;

        int count = mDatalist.size();
        for (int i = 0; i < count; i++) {
            mBooksDbMemberInfo = mDatalist.get(i);
            int index = mBooksDbMemberInfo.getIndex();
            View view = getLayoutInflater().inflate(R.layout.item_select_people, null);
            RelativeLayout rl_select_ischeck = (RelativeLayout) view.findViewById(R.id.rl_select_ischeck);
            CheckBox cb_select_ischeck = (CheckBox) view.findViewById(R.id.cb_select_ischeck);
            TextView nameText = (TextView) view.findViewById(R.id.tv_select_people_name);
            CircleImageView mCircleImageView = (CircleImageView) view.findViewById(R.id.iv_select_people_icon);
            nameText.setText(mBooksDbMemberInfo.getUsername());
            int ind = Integer.parseInt(mBooksDbMemberInfo.getMemberid()) % 10;
            String image_url = mBooksDbMemberInfo.getUsericon();
            if (TextUtils.isEmpty(image_url)) {
                image_url = "drawable://" + RequsterTag.head_image[ind];
            }

            XzbUtils.displayImageHead(mCircleImageView, image_url, RequsterTag.head_image[ind]);
            // 设置选中

            //第一次进入时候显示是根据上个界面带过来的数据显示选中，并且Member_Id在后面onBack中判断中需要
            if (flage) {
                for (int j = 0; j < Member_Id.length; j++) {
                    if (mBooksDbMemberInfo.getMemberid().equals(Member_Id[j])) {
                        cb_select_ischeck.setChecked(true);
                    }
                }
            } else {
                //第二次进入时候显示是根据跳转的时候保存的数据进行显示选中
                for (int j = 0; j < onselectData.size(); j++) {
                    if (mBooksDbMemberInfo.getMemberid().equals(onselectData.get(j).getMemberid())) {
                        cb_select_ischeck.setChecked(true);
                    }
                }
            }
            rl_select_ischeck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.cb_select_ischeck);

                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                    int count = 0;
                    for (int i = 0; i < checkBoxList.size(); i++) {
                        if (checkBoxList.get(i).isChecked()) {
                            count++;
                        }
                    }
                    checkPeopleCount = count;
                    if (count > 0) {
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.main_back);
                    } else {
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.secondary_text);
                    }

                    if (count == checkBoxList.size()) {
                        cb_selectAll.setChecked(true);
                    } else {
                        cb_selectAll.setChecked(false);
                    }
                }
            });
            cb_select_ischeck.setTag(i);
            checkBoxList.add(cb_select_ischeck);
            ll_expend_add_people.addView(view);
        }
        setAllCheck();

    }

    private void setAllCheck() {
        int checkCount = 0;
        for (int i = 0; i < mDatalist.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                checkCount++;
            }
        }
        //全选是否选中
        if (mDatalist.size() == checkCount) {
            cb_selectAll.setChecked(true);
        } else {
            cb_selectAll.setChecked(false);
        }
        checkPeopleCount = checkCount;
        if (checkCount > 0) {
            changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.main_back);
        } else {
            changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.secondary_text);
        }
    }


    private List<BooksDbMemberInfo> getCheckBokIsCheckedObject() {
        List<BooksDbMemberInfo> resuleListData = new ArrayList<>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                int index = (int) checkBoxList.get(i).getTag();
                resuleListData.add(mDatalist.get(index));
            }
        }
        return resuleListData;
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        onBack();
    }

    private void showDialog() {
        showSimpleAlertDialog("提示", "你还未保存信息，是否确定返回", "是", "否", false, true, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                finish();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

    }

    private void onBack() {
        // 这个方法判断当前成员是否修改过
        int s = 0;
        List<BooksDbMemberInfo> resuleListData = getCheckBokIsCheckedObject();
        // 长度不一样着显示弹窗
        if (resuleListData.size() != Member_Id.length) {
            showDialog();
        } else {
            // 长度一样，将有的相加，判断长度
            for (int i = 0; i < Member_Id.length; i++) {
                for (int j = 0; j < resuleListData.size(); j++) {
                    String id = resuleListData.get(j).getMemberid();
                    if (Member_Id[i].equals(id)) {
                        s++;
                    }
                }
            }
            // 长度不相等
            if (Member_Id.length != s) {
                showDialog();
            } else {// 相等
                finish();
            }
        }
    }

    public void onEventMainThread(AddMemberResponse addMemberResponse) {// 添加成员刷新界面
        Log.i("LOG", "------------");
        TaskQueue.mainQueue().executeDelayed(new Runnable() {
            @Override
            public void run() {
                getNativePeopleByBook();

            }
        }, 500);

    }


}
