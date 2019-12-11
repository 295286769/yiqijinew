package com.yiqiji.money.modules.common.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by leichi on 2017/5/5.
 */

public class MaxLengthWatcher implements TextWatcher {
    private int maxLen = 0;
    private EditText editText = null;
    private String showToastText;

    public MaxLengthWatcher(int maxLen, EditText editText) {
        this.maxLen = maxLen;
        this.editText = editText;
        editText.addTextChangedListener(this);
    }

    public MaxLengthWatcher(int maxLen, EditText editText, String showToastText) {
        this.maxLen = maxLen;
        this.editText = editText;
        this.showToastText = showToastText;
        editText.addTextChangedListener(this);
    }

    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        Editable editable = editText.getText();
        int len = calculateLength(arg0);

        if (len > maxLen) {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0, str.length()-1);
            editText.setText(newStr);
            editable = editText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
            if (!TextUtils.isEmpty(showToastText)) {
                Toast.makeText(editText.getContext(), showToastText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected int calculateLength(final CharSequence c) {
        int len = 0;
        final int l = c.length();
        for (int i = 0; i < l; i++) {
            final char tmp = c.charAt(i);
            if (tmp >= 0x20 && tmp <= 0x7E) {
                // 字元值 32~126 是 ASCII 半形字元的範圍
                len++;
            } else {
                // 非半形字元
                len += 3;
            }
        }
        return len;
    }
}
