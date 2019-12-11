package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.adapter.CommonRecyclerViewAdapter;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.common.view.MyRecyclerView;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementItemInfo;

import java.util.ArrayList;
import java.util.List;

public class ChangeSettlementMemberActivity extends BaseActivity implements OnClickListener {
    private CheckBox check_all;
    private MyRecyclerView list_view;
    private CommonRecyclerViewAdapter<BooksSettlementItemInfo> adapter;
    private int is_check_all = 0;
    private List<BooksSettlementItemInfo> booksDbMemberInfos;
    private String id = "";
    private List<String> booleans;
    private List<CheckBox> checkBoxList;

    private LinearLayout ll_expend_add_people;

    private int checkPeopleCount;
    private CheckBox cb_selectAll;

    private RelativeLayout ll_selectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_settlement_member);
        id = getIntent().getStringExtra("id");
        booksDbMemberInfos = getIntent().getParcelableArrayListExtra("list");
        booleans = getIntent().getStringArrayListExtra("booleans");
        checkBoxList = new ArrayList<CheckBox>();
        initView();
        //    initAdapter();
        initData(booksDbMemberInfos);
        initTitle();
    }

    private void initView() {
        ll_expend_add_people = (LinearLayout) findViewById(R.id.ll_expend_add_people);
        cb_selectAll = (CheckBox) findViewById(R.id.cb_selectAll);
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
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")",R.color.title_text_color);
                        booleans.set(i, "true");
                    } else {
                        checkBoxList.get(i).setChecked(false);
                        checkPeopleCount = 0;
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.secondary_text);
                        booleans.set(i, "true");
                    }
                }

            }
        });

    }

    private void initTitle() {
        int number = 0;
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isChecked()) {
                number++;
            }
        }
        //全选是否选中
        if (booksDbMemberInfos.size() == number) {
            cb_selectAll.setChecked(true);
        } else {
            cb_selectAll.setChecked(false);
        }
        checkPeopleCount = number;
        if (number > 0) {
            initTitle("更换结算对象", -1, "确定" + "(" + number + ")", this);
        } else {
            initTitle("更换结算对象", -1, "确定" + "(" + number + ")", this);
            changeRightBtn("确定" + "(" + checkPeopleCount + ")", R.color.secondary_text);
        }


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.layout_title_view_return:
                finish();
                break;
            case R.id.layout_title_view_right_btn:
                if (checkPeopleCount <= 0) {
                    return;
                }

                Intent intent = new Intent(this, SingeSettlementActivity.class);
                intent.putStringArrayListExtra("booleans", (ArrayList<String>) booleans);
                setResult(11, intent);
                finish();
                break;
        }
    }


    private void initData(List<BooksSettlementItemInfo> booksDbMemberInfos) {
        ll_expend_add_people.removeAllViews();
        BooksSettlementItemInfo mBooksDbMemberInfo;
        int count = booksDbMemberInfos.size();
        for (int i = 0; i < count; i++) {
            mBooksDbMemberInfo = booksDbMemberInfos.get(i);

            View view = getLayoutInflater().inflate(R.layout.item_select_people, null);
            RelativeLayout rl_select_ischeck = (RelativeLayout) view.findViewById(R.id.rl_select_ischeck);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_select_ischeck);
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
            rl_select_ischeck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.cb_select_ischeck);

                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        booleans.set((Integer) v.getTag(), "false");
                    } else {
                        checkBox.setChecked(true);
                        booleans.set((Integer) v.getTag(), "true");
                    }
                    int count = 0;
                    for (int i = 0; i < checkBoxList.size(); i++) {
                        if (checkBoxList.get(i).isChecked()) {
                            count++;
                        }
                    }
                    checkPeopleCount = count;
                    if (count > 0) {
                        changeRightBtn("确定" + "(" + checkPeopleCount + ")",R.color.title_text_color);
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
            if (booleans.get(i).equals("false")) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }

            rl_select_ischeck.setTag(i);
            checkBoxList.add(checkBox);
            ll_expend_add_people.addView(view);
        }
    }


}