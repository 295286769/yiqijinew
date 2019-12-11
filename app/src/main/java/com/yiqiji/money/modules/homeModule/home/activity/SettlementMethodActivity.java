package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;

public class SettlementMethodActivity extends BaseActivity {
    private RelativeLayout group_settlement, singe_settlement;
    private String id;
    private String memberid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_method);
        id = getIntent().getStringExtra("id");
        memberid = getIntent().getStringExtra("memberid");
        initView();
        initTitle();
        initListener();
    }

    private void initView() {
        group_settlement = (RelativeLayout) findViewById(R.id.group_settlement);
        singe_settlement = (RelativeLayout) findViewById(R.id.singe_settlement);
    }

    private void initTitle() {
        initTitle( "选择结算方式");
    }

    private void initListener() {
        group_settlement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettlementMethodActivity.this, SettlementActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("memberid", memberid);
                startActivity(intent);

            }
        });
        singe_settlement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettlementMethodActivity.this, SingeSettlementActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("memberid", memberid);
                startActivity(intent);

            }
        });
    }

}
