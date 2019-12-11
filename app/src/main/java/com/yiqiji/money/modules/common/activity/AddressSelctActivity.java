package com.yiqiji.money.modules.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.frame.ui.wigit.BaseTitleLayout;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.view.SelectLocationView;
import com.yiqiji.money.modules.homeModule.mybook.entity.AddressInfo;
import com.yiqiji.money.modules.homeModule.mybook.entity.HouseDataInfo;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.HouseStypePerecenter;
import com.yiqiji.money.modules.homeModule.mybook.perecenter.MyBookPerecenter;
import com.yiqiji.money.modules.homeModule.mybook.util.MyBookActivityUtil;
import com.zaaach.citypicker.CityPickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ${huangweishui} on 2017/7/26.
 * address huang.weishui@71dai.com
 */
public class AddressSelctActivity extends BaseActivity {
    @BindView(R.id.title_layout)
    BaseTitleLayout titleLayout;
    @BindView(R.id.selectLocationView)
    SelectLocationView selectLocationView;
    @BindView(R.id.leftText)
    TextView leftText;
    @BindView(R.id.edRightText)
    EditText edRightText;
    @BindView(R.id.villageName)
    RelativeLayout villageName;
    @BindView(R.id.tv_area_leftText)
    TextView tvAreaLeftText;
    @BindView(R.id.tv_area_rightText)
    EditText tvAreaRightText;
    @BindView(R.id.tv_area_mi)
    TextView tvAreaMi;
    @BindView(R.id.housingArea)
    RelativeLayout housingArea;
    @BindView(R.id.houseSelectLocationView)
    SelectLocationView houseSelectLocationView;
    @BindView(R.id.decorationStyleSelectLocationView)
    SelectLocationView decorationStyleSelectLocationView;
    @BindView(R.id.styleSelectLocationView)
    SelectLocationView styleSelectLocationView;
    @BindView(R.id.tvCompanyLeftText)
    TextView tvCompanyLeftText;
    @BindView(R.id.tvCompanyRightText)
    EditText tvCompanyRightText;
    @BindView(R.id.rtCompanyLayout)
    RelativeLayout rtCompanyLayout;
    private Unbinder unbind;
    private String mAccountbookid;
    private String mhouseid = "";
    private HouseDataInfo houseDataInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selct);
        unbind = ButterKnife.bind(this);
        initIntentData();
        onPress();
        initHouseInfo();
    }


    private void initIntentData() {
        mAccountbookid = getIntent().getStringExtra("accountbookid");
        titleLayout.setTitle("填写信息");
        titleLayout.setRightText("保存");

    }

    private void onPress() {
        selectLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//区域
                MyBookActivityUtil.startCityPickerActivity(AddressSelctActivity.this);
            }
        });
        houseSelectLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//户型
                String content = houseSelectLocationView.getSelectText();
                MyBookActivityUtil.startHouseStypeActivity(AddressSelctActivity.this, 0, content);
            }
        });
        decorationStyleSelectLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//装修方式
                String content = decorationStyleSelectLocationView.getSelectText();
                MyBookActivityUtil.startHouseStypeActivity(AddressSelctActivity.this, 1, content);
            }
        });
        styleSelectLocationView.setOnClickListener(new View.OnClickListener() {//装修风格
            @Override
            public void onClick(View v) {
                String content = styleSelectLocationView.getSelectText();
                MyBookActivityUtil.startRenovationStypeActivity(AddressSelctActivity.this, content);
            }
        });
        titleLayout.setOnClickRightText(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location_content = selectLocationView.getSelectText();
                String housestype_content = houseSelectLocationView.getSelectText();
                String decorationstyle_content = decorationStyleSelectLocationView.getSelectText();
                String style_content = styleSelectLocationView.getSelectText();
                String vallige_name = edRightText.getText().toString();
                String house_area = tvAreaRightText.getText().toString();
                String company_name = tvCompanyRightText.getText().toString();
                if (TextUtils.isEmpty(vallige_name)) {
                    showToast("请填写小区名称");
                    return;
                }
                if (location_content.equals(getResources().getString(R.string.select_text))) {
                    location_content = "";
                }
                AddressInfo addressInfo = new AddressInfo();
                addressInfo.setId(mAccountbookid);//账本id
                addressInfo.setHouseid(mhouseid);//房子id
                addressInfo.setHousecity(location_content);//所在城市
                addressInfo.setHousename(vallige_name);//小区名字
                addressInfo.setHousesquare(house_area);//房子面积
                addressInfo.setHousestyle(housestype_content);//户型
                addressInfo.setHousetype(style_content);//装修风格
                addressInfo.setHouseway(decorationstyle_content);//装修方式
                addressInfo.setCompany(company_name);//装修公司
                submitAddress(HouseStypePerecenter.getAddressInfo(mAccountbookid, mhouseid, location_content, housestype_content, decorationstyle_content, style_content, vallige_name, house_area, company_name));
            }


        });
    }

    private void submitAddress(AddressInfo addressInfo) {
        HouseStypePerecenter.subMitData(addressInfo, houseDataInfo, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                showToast("保存成功");
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
    }

    private void initHouseInfo() {
        MyBookPerecenter.initHouseInfo(mAccountbookid, new ViewCallBack<HouseDataInfo>() {
            @Override
            public void onSuccess(HouseDataInfo houseDataInfo) throws Exception {
                super.onSuccess(houseDataInfo);
                AddressSelctActivity.this.houseDataInfo = houseDataInfo;
                if (houseDataInfo != null) {
                    if (!TextUtils.isEmpty(houseDataInfo.getHousecity())) {
                        selectLocationView.setSelectText(houseDataInfo.getHousecity());
                    }
                    edRightText.setText(houseDataInfo.getHousename());
                    tvAreaRightText.setText(houseDataInfo.getHousesquare());
                    houseSelectLocationView.setSelectText(houseDataInfo.getHousestyle());
                    decorationStyleSelectLocationView.setSelectText(houseDataInfo.getHouseway());
                    styleSelectLocationView.setSelectText(houseDataInfo.getHousetype());
                    tvCompanyRightText.setText(houseDataInfo.getCompany());
                    mhouseid = houseDataInfo.getHouseid();
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                showToast(simleMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String content = "";
        if (data != null) {
            switch (resultCode) {
                case CityPickerActivity.KEY_RESULTCODE_CITY:
                    content = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                    selectLocationView.setSelectText(content);
                    break;
                case MyBookActivityUtil.HOUSESTYPEACTIVITY_RESULTCODE:
                    content = data.getStringExtra("content");
                    houseSelectLocationView.setSelectText(content);
                    break;
                case MyBookActivityUtil.RENOVATIONSTYPEACTIVITY_RESULTCODE:
                    content = data.getStringExtra("content");
                    decorationStyleSelectLocationView.setSelectText(content);
                    break;
                case MyBookActivityUtil.DECORATE_RESULTCODE:
                    content = data.getStringExtra("content");
                    styleSelectLocationView.setSelectText(content);
                    break;
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbind != null) {
            unbind.unbind();
        }
    }
}
