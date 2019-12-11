package com.yiqiji.money.modules.property.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.property.activity.PropertySearchStockActivity;
import com.yiqiji.money.modules.property.activity.WealthAddActivity;

import static com.yiqiji.money.modules.common.utils.MethodsCompat.overridePendingTransition;

/**
 * Created by dansakai on 2017/3/6.
 * 资产模块
 */

public class PropertyFragment extends Fragment {
    private View view;
    private RadioGroup rg_property;
    private WealthAssetsFragment assetsFragment;
    private WealthOptionalFragment optionalFragment;
    public ImageView iv_search;//自选显示搜索
    public TextView tv_add_property;//资产显示添加

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_property, null);
            iv_search = (ImageView) view.findViewById(R.id.iv_search);
            iv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(getActivity(), PropertySearchStockActivity.class);
                    startActivityForResult(inten, 1003);
                }
            });
            tv_add_property = (TextView) view.findViewById(R.id.tv_add_property);
            tv_add_property.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//跳转到添加资产
                    XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_ADD_PROPERTY);
                    Intent intent = new Intent(getActivity(), WealthAddActivity.class);
                    startActivityForResult(intent, 1002);
                    overridePendingTransition(getActivity(),R.anim.activity_in_anim,R.anim.activity_deafaut_anim);
                }
            });
        }
        initView();
        if (savedInstanceState == null) {
            addChildFragment();
        }

        return view;
    }

    private void addChildFragment() {
        assetsFragment = new WealthAssetsFragment();
        optionalFragment = new WealthOptionalFragment();

        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, assetsFragment)
                .add(R.id.fragment_container, optionalFragment).commit();

        getChildFragmentManager().beginTransaction().hide(optionalFragment).show(assetsFragment).commit();
    }


    private void initView() {
        rg_property = (RadioGroup) view.findViewById(R.id.rg_property);
        ((RadioButton) rg_property.getChildAt(0)).setChecked(true);
        iv_search.setVisibility(View.GONE);
        tv_add_property.setVisibility(View.VISIBLE);

        rg_property.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (checkedId) {
                    case R.id.rb_wealth_assets://资产Tab
                        XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_PROPERTY_TAB);
                        iv_search.setVisibility(View.GONE);
                        tv_add_property.setVisibility(View.VISIBLE);
                        ft.hide(optionalFragment).show(assetsFragment);
                        break;
                    case R.id.rb_wealth_optional://自选Tab
                        XzbUtils.hidePointInUmg(getActivity(), Constants.HIDE_OPTIONAL_TAB);
                        ft.hide(assetsFragment).show(optionalFragment);
                        iv_search.setVisibility(View.VISIBLE);
                        tv_add_property.setVisibility(View.GONE);
                        break;
                }
                ft.commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        optionalFragment.onActivityResult(requestCode, resultCode, data);
        assetsFragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 重新加载数据
     */
    public void reLoadData() {
        if (optionalFragment != null) {
            optionalFragment.reLoadOptional();
        }
        if (assetsFragment != null) {
            assetsFragment.loadData();
        }
    }
}
