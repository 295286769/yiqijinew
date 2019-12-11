package com.yiqiji.money.modules.common.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by dansakai on 2017/3/15.
 */

public class DaySeleterDialog extends Dialog {

    private DataListener listener;
    private Context mContext;
    private LayoutInflater inflater;
    private WindowManager.LayoutParams lp;

    private TextView tvTitle;
    private Button btn_confirm;
    private WheelView wheelView;

    public DaySeleterDialog(Context context, final DataListener listener,String[] strs) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.listener = listener;

        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dataselect_layout_dialog, null);
        layout.setMinimumWidth(UIHelper.getDisplayWidth((Activity) context));
        tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
        tvTitle.setText("请选择日期");
        btn_confirm = (Button) layout.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.dataPicker(wheelView.getSelectedItemText());
                dismiss();
            }
        });
        wheelView = (WheelView) layout.findViewById(R.id.id_day);
        wheelView.setVisibleItems(7);
        wheelView.setViewAdapter(new ArrayWheelAdapter<>(mContext,strs));

        setContentView(layout);

        lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    public interface DataListener {
        void dataPicker(String day);
    }

    private void setDataListener(DataListener listener) {
        this.listener = listener;
    }

    @Override
    public void dismiss() {
        if (!((Activity) mContext).isFinishing() && isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void show() {
        if (!((Activity) mContext).isFinishing() && !isShowing()) {
            super.show();
        }
    }

}
