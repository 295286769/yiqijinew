package com.yiqiji.frame.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.frame.ui.Constant;
import com.yiqiji.frame.ui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaolong on 2017/7/18.
 */

public class ListItemDialog extends BaseDialogFragment {


    private String[] itemNameList;
    private LinearLayout contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemNameList = getArguments().getStringArray(Constant.EXTRA_DIALOG_ITEM_NAME_LIST);
        if (itemNameList == null || itemNameList.length == 0) {
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = (LinearLayout) inflater.inflate(R.layout.dialog_list_item_layout, null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        for (int i = 0; i < itemNameList.length; i++) {
            String itemName = itemNameList[i];
            addItemView(itemName);
            if (i < itemNameList.length - 1) {
                addLineView();
            }
        }
    }


    private void addItemView(String itemName) {
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog_list_view, null);
        TextView textView = ButterKnife.findById(itemView, R.id.textView);
        textView.setText(itemName);
        contentView.addView(itemView);

        itemView.setOnClickListener(itemViewOnClickListener);
    }


    private void addLineView() {
        LayoutInflater.from(getActivity()).inflate(R.layout.line_view, contentView);
    }


    View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View itemView) {
            TextView textView = ButterKnife.findById(itemView, R.id.textView);
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(textView.getText().toString());
            }
            dismiss();
        }
    };

    OnItemClickListener onItemClickListener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnItemClickListener {
        void OnItemClick(String itemName);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static ListItemDialog newInstance(String[] itemNameList) {
        ListItemDialog listItemDialog = new ListItemDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray(Constant.EXTRA_DIALOG_ITEM_NAME_LIST, itemNameList);
        listItemDialog.setArguments(bundle);
        return listItemDialog;
    }
}
