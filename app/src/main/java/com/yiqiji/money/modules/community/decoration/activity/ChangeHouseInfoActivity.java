package com.yiqiji.money.modules.community.decoration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.homeModule.mybook.util.MyBookActivityUtil;
import com.zaaach.citypicker.CityPickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dansakai on 2017/8/4.
 * 修改小区信息
 */

public class ChangeHouseInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_inputName)
    EditText etInputName;
    @BindView(R.id.tv_selCity)
    TextView tvSelCity;

    private String selCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_house_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("填写小区信息");
    }

    @OnClick({R.id.iv_back, R.id.rl_selectCity, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_selectCity:
                MyBookActivityUtil.startCityPickerActivity(this);
                break;
            case R.id.tv_sure:
                String houseName = etInputName.getText().toString();
                if (StringUtils.isEmpty(houseName)) {
                    showToast("请输入小区名称");
                    return;
                }

                if (StringUtils.isEmpty(selCity)) {
                    showToast("请选择所在地区");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("city", selCity);
                intent.putExtra("houseName", houseName);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CityPickerActivity.KEY_RESULTCODE_CITY) {
            selCity = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            tvSelCity.setText(selCity);
        }
    }
}
