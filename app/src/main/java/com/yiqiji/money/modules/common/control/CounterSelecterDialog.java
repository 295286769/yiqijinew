package com.yiqiji.money.modules.common.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.adapter.SelecterCounterAdapter;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.activity.WealthAddActivity;
import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import java.util.List;

/**
 * Created by dansakai on 2017/3/11.
 */

public class CounterSelecterDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater inflater;
    private WindowManager.LayoutParams lp;
    private ImageView btn_close;
    private TextView tv_tittle;
    private DismissListener dismissDialog;
    private ListView listView;
    private SelecterCounterAdapter adapter;
    public CounterSelecterDialog(final Context context, String title, final List<PropertyTransEntity> list) {
        super(context, R.style.ActionSheetDialogStyle);
        setCancelable(true);
        this.mContext = context;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(
                R.layout.activity_counter_selecter_dialog, null);

        layout.setMinimumWidth(UIHelper.getDisplayWidth((Activity) mContext));
        btn_close = (ImageView) layout.findViewById(R.id.btn_close);
        tv_tittle = (TextView) layout.findViewById(R.id.tv_title);
        listView = (ListView) layout.findViewById(R.id.listView);
        listView.getLayoutParams().height = (int) (UIHelper
                .getDisplayHeight((Activity) mContext) * 0.45);

        setContentView(layout);
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);

        btn_close.setOnClickListener(this);
        tv_tittle.setText(title);

        //新增新建账户item
        PropertyTransEntity entity = new PropertyTransEntity();
        entity.setItemname("新建账户");
        list.add(entity);
        adapter = new SelecterCounterAdapter(mContext, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list.size() - 1) {
                   Intent intent = new Intent(mContext, WealthAddActivity.class);
                    ((Activity) mContext).startActivityForResult(intent,1004);
                } else {
                    dismissDialog.DismissDialog(list.get(position));
                }
                dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                dismiss();
                break;
        }

    }

    public interface DismissListener {
        void DismissDialog(PropertyTransEntity entity);
    }

    public CounterSelecterDialog setDissmissCallBack(DismissListener listener) {
        this.dismissDialog = listener;
        return this;
    }

//    @Override
//    public void dismiss() {
//        if (!((Activity) mContext).isFinishing()) {
//            super.dismiss();
//            dismiss();
//        }
//    }
}
