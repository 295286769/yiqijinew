package com.yiqiji.money.modules.homeModule.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;

public class GroupSettlementEndActivity extends BaseActivity implements
        OnClickListener {
//    private TextView settlement_go, temporary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settlement_end);
        initView();
        bottomTwoButtonText("暂不结算", "继续结算", this);
    }

    private void initView() {
//        temporary = (TextView) findViewById(R.id.temporary);
//        settlement_go = (TextView) findViewById(R.id.settlement_go);
//        temporary.setOnClickListener(this);
//        settlement_go.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.share_button:
                finish();

                break;
            case R.id.settlement_button:
                Intent intent = new Intent(this, SettlementActivity.class);
                intent.putExtra("settlement", "settlement");
                setResult(120, intent);
                finish();

                break;

            default:
                break;
        }
    }

}
