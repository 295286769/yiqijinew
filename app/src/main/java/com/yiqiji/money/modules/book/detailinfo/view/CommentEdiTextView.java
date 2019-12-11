package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/15.
 */

public class CommentEdiTextView extends LinearLayout {

    @BindView(R.id.img_send)
    ImageView imgSend;
    @BindView(R.id.edit_comment)
    EditText editComment;

    public CommentEdiTextView(Context context) {
        super(context);
        initView();
    }

    public CommentEdiTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.view_commnet_edit_layout, this);
        ButterKnife.bind(this);
        initViewEvent();
    }


    private void initViewEvent() {
        imgSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputTextEmpty()) {
                    //呼出键盘
                    showInputBoard();
                } else {
                    //发送信息
                    if(onSendCommentListener !=null)
                        onSendCommentListener.onSend(editComment.getText().toString());
                }
            }
        });

        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                updateImgSendState();
            }
        });
    }

    public void onSendSuccessCallBack(){
        hideInputBoard();
        editComment.getText().clear();
        updateImgSendState();
    }

    private boolean InputTextEmpty() {
        return editComment.getText().toString().trim().length() == 0;
    }

    private void updateImgSendState() {
        imgSend.setImageResource(InputTextEmpty() ? R.drawable.icon_send_grey : R.drawable.icon_send_blue);
    }

    public void showInputBoard() {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editComment, 0);
    }

    public void hideInputBoard() {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editComment.getWindowToken(),0);
    }


    OnSendCommentListener onSendCommentListener;
    public interface OnSendCommentListener {
        void onSend(String text);
    }


    public void setOnSendCommentListener(OnSendCommentListener onSendCommentListener) {
        this.onSendCommentListener = onSendCommentListener;
    }

}
