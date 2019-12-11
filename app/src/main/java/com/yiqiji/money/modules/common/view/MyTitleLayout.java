package com.yiqiji.money.modules.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.plication.MyApplicaction;

public class MyTitleLayout extends RelativeLayout {
    private Context context;
    private ImageView returnBtn;// 返回按钮
    private TextView titleText;// 标题
    private TextView rightBtn;// 右边的button
    private ImageView titleImg;// 标题图片
    private ImageView rightImgBtn;// 右边的图片按钮
    private ImageView layout_title_view_sencond_right_img_btn;// 右边的第二個圖片图片按钮
    private TextView returnBtnText;
    private RelativeLayout layout;
    private NotesView notes;
    private TextView book_note_text, message_note_text;
    private LinearLayout layou_list;
    private RelativeLayout sys;
    private ImageView rote_image;

    public MyTitleLayout(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public MyTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MyTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    /**
     * 初始化界面图 初始化界面 layout
     */
    private void initView() {
        layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_title_view, this);
        layout.setBackgroundColor(getResources().getColor(R.color.title_back_color));
        returnBtn = (ImageView) layout.findViewById(R.id.layout_title_view_return);
        titleText = (TextView) layout.findViewById(R.id.layout_title_view_title);
        rightBtn = (TextView) layout.findViewById(R.id.layout_title_view_right_btn);
        titleImg = (ImageView) layout.findViewById(R.id.layout_title_view_title_img);
        rightImgBtn = (ImageView) layout.findViewById(R.id.layout_title_view_right_img_btn);
        layout_title_view_sencond_right_img_btn = (ImageView) layout
                .findViewById(R.id.layout_title_view_sencond_right_img_btn);
        returnBtnText = (TextView) layout.findViewById(R.id.layout_title_view_return_text);
        notes = (NotesView) layout.findViewById(R.id.notes);
        book_note_text = (TextView) layout.findViewById(R.id.book_note_text);
        message_note_text = (TextView) layout.findViewById(R.id.message_note_text);
        layou_list = (LinearLayout) layout.findViewById(R.id.layou_list);
        sys = (RelativeLayout) layout.findViewById(R.id.sys);
        rote_image = (ImageView) layout.findViewById(R.id.rote_image);
    }

    private void setGone() {
        returnBtn.setVisibility(View.GONE);
        titleText.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        titleImg.setVisibility(View.GONE);
        rightImgBtn.setVisibility(View.GONE);
        layout_title_view_sencond_right_img_btn.setVisibility(View.GONE);
        returnBtnText.setVisibility(View.GONE);
        notes.setVisibility(View.GONE);
        book_note_text.setVisibility(View.GONE);
        message_note_text.setVisibility(View.GONE);
        layou_list.setVisibility(View.GONE);
        rote_image.clearAnimation();
        sys.setVisibility(View.GONE);

    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setListener(OnClickListener listener) {
        returnBtn.setOnClickListener(listener);
        rightBtn.setOnClickListener(listener);
        rightImgBtn.setOnClickListener(listener);
        layout_title_view_sencond_right_img_btn.setOnClickListener(listener);
        returnBtnText.setOnClickListener(listener);
        titleText.setOnClickListener(listener);
    }

    /**
     * 设置背景颜色有标题
     *
     * @param colorId
     */
    public void setBackgroudColor(int colorId, int titleColorId) {
        titleText.setVisibility(View.VISIBLE);
//        rightBtn.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(colorId);
        titleText.setTextColor(titleColorId);
//        rightBtn.setTextColor(titleColorId);
    }

    /**
     * 设置背景有标题
     *
     * @param colorId
     */
    public void setBackgroudImage(int colorId, int titleColorId) {
        titleText.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(colorId);
        titleText.setTextColor(titleColorId);
        rightBtn.setTextColor(titleColorId);
    }

    /**
     * 设置背景
     *
     * @param colorId
     */
    public void setBackgroudColor(int colorId) {
        layout.setBackgroundResource(colorId);
    }

    /**
     * 消息提示是否显示
     *
     * @param
     */
    public void setNotesHodeOrShow(boolean isShow) {
        if (isShow) {
            notes.setVisibility(View.VISIBLE);
        } else {
            notes.setVisibility(View.GONE);
        }

    }

    /**
     * 显示账本消息
     *
     * @param
     */
    public void setBookNoteShow(int number) {

        if (number > 0) {
//            book_note_text.setText(number + "");
            book_note_text.setVisibility(View.VISIBLE);
        } else {
            book_note_text.setVisibility(View.GONE);
        }

    }

    /**
     * 显示消息
     * 0 为不显示消息数量
     *
     * @param
     */
    public void setMessageNoteShow(int number) {

        if (number > 0) {
            message_note_text.setText(number + "");
            message_note_text.setVisibility(View.VISIBLE);
        } else {
            message_note_text.setVisibility(View.GONE);
        }

    }

    public void initBookTitleHasLeftAndRight(String title, int drawableid, int drawbleLEftID, int drawbleRightID,
                                             int drawbleSecondRightID) {
        setGone();
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
        if (drawableid != 0) {
            Drawable drawable = getResources().getDrawable(drawableid);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            titleText.setCompoundDrawables(null, null, drawable, null);
            titleText.setCompoundDrawablePadding(UIHelper.Dp2Px(MyApplicaction.getContext(), 10));
        } else {
            titleText.setCompoundDrawables(null, null, null, null);
        }
        showRightSecondImgBtn(drawbleSecondRightID);
        showRightImgBtn(drawbleLEftID);
        setLeftBtnImg(drawbleRightID);

    }

    public void setTitleImage(int drawableid) {
        if (drawableid > 0) {
            Drawable drawable = getResources().getDrawable(drawableid);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            titleText.setCompoundDrawables(null, null, drawable, null);
            titleText.setCompoundDrawablePadding(UIHelper.Dp2Px(MyApplicaction.getContext(), 10));
        } else {
            titleText.setCompoundDrawables(null, null, null, null);
        }
    }

    /**
     * 同步状态1.同步中,2同步成功3.同步失败
     *
     * @param drawableid
     * @param title
     */
    public void synchronizationTitle(int drawableid, String title, int colorid) {
        if (drawableid == 1) {
//            Drawable drawable = getResources().getDrawable(drawableid);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            sys.setVisibility(View.VISIBLE);
            rote_image.setImageResource(R.drawable.synchronization);
            // 添加匀速转动动画
            LinearInterpolator lir = new LinearInterpolator();

            RotateAnimation rotaAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.sys_rote);
            rotaAnimation.setInterpolator(lir);
            rote_image.startAnimation(rotaAnimation);
//            titleText.setCompoundDrawables(drawable, null, null, null);
//            titleText.setCompoundDrawablePadding(UIHelper.Dp2Px(MyApplicaction.getContext(), 10));
        } else if (drawableid == 2) {
            rote_image.clearAnimation();
            rote_image.setImageResource(R.drawable.synchronization_sus);
        } else if (drawableid == 3) {
            rote_image.clearAnimation();
            rote_image.setImageResource(R.drawable.synchronization_fal);
        } else {
            rote_image.clearAnimation();
            sys.setVisibility(View.GONE);
//            titleText.setCompoundDrawables(null, null, null, null);
        }
        if (!TextUtils.isEmpty(title)) {
            title = StringUtils.onHideText(title, 8);
        }
        titleText.setText(title);
        titleText.setTextColor(getResources().getColor(colorid));

    }

    public void setSysText(String text) {
        titleText.setText(text);
    }

    public void changeInitBookTitle(String title) {

        titleText.setText(title);
    }

    public void onelyTitleShow(String title) {
        setGone();
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
        titleText.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 设置背景颜色
     */
    public void setBackgroudColor(String colorString, String titleColorString) {
        titleText.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(Color.parseColor(colorString));
        titleText.setTextColor(Color.parseColor(titleColorString));
    }

    /**
     * 是否显示返回按钮
     *
     * @param isShow
     */
    public void showReturnBtn(boolean isShow) {
        if (isShow) {
            returnBtn.setVisibility(View.VISIBLE);
            return;
        }
        returnBtn.setVisibility(View.GONE);
    }

    /**
     * 设置是否显示返回提示
     */
    public void showReturnBtnText(String returnText) {
        returnBtnText.setVisibility(View.VISIBLE);
        returnBtnText.setText(returnText);
    }

    public void showReturnBtnText(String returnText, int color) {
        returnBtnText.setVisibility(VISIBLE);
        returnBtnText.setText(returnText);
        returnBtnText.setTextColor(color);
    }

    /**
     * 设置标题名称
     *
     * @param titleName
     */
    public void setTitle(String titleName) {
        titleText.setVisibility(View.VISIBLE);


        if (!TextUtils.isEmpty(titleName)) {
            titleName = StringUtils.onHideText(titleName, 8);
//            titleText.setSingleLine();
//            titleText.setEllipsize(TextUtils.TruncateAt.END);
        }
        titleText.setText(titleName);
    }

    /**
     * 不限制标题名称长度
     */
    public void unrestrictedTitleLenth(Context context, String title, int left, int right) {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) titleText.getLayoutParams();
        layoutParams.rightMargin = 0;
        titleText.setLayoutParams(layoutParams);
        titleText.setPadding(left, 0, right, 0);
        returnBtn.setPadding(left, 0, right, 0);
        titleText.setText(title);
    }

    public void cleaSysAnimation() {
        sys.setVisibility(View.GONE);
        rote_image.clearAnimation();
    }

    /**
     * 设置标题名称
     *
     * @param resourceId
     */
    public void setTitle(int resourceId) {
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(resourceId);
    }

    /**
     * 设置标题名称颜色
     *
     * @param colorId
     */
    public void setTitleColor(int colorId) {
        titleText.setVisibility(View.VISIBLE);
        titleText.setTextColor(getResources().getColor(colorId));
    }

    /**
     * 显示左边的按钮
     *
     * @param resourceId
     */
    public void setLeftBtnImg(int resourceId) {
        returnBtn.setVisibility(View.VISIBLE);
        returnBtn.setImageResource(resourceId);
        // BitmapDrawable drawable = (BitmapDrawable)
        // getResources().getDrawable(
        // resourceId);
        // drawable.setBounds(0, 0, drawable.getMinimumWidth(),
        // drawable.getMinimumHeight());
        // returnBtn.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 显示右边文字的按钮
     *
     * @param reghtText
     */
    public void showRightBtn(String reghtText) {
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText(reghtText);
    }

    public void showRightBtn(String reghtText, int color) {
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setTextColor(getResources().getColor(color));
        rightBtn.setText(reghtText);
    }

    /**
     * 显示右边的图片按钮
     *
     * @param bitmap
     */
    public void showRightImgBtn(Bitmap bitmap) {
        rightImgBtn.setVisibility(View.VISIBLE);
        rightImgBtn.setImageBitmap(bitmap);
    }

    /**
     * 显示右边的图片按钮
     *
     * @param resourceId
     */
    public void showRightImgBtn(int resourceId) {
        rightImgBtn.setVisibility(View.VISIBLE);
        rightImgBtn.setImageResource(resourceId);
    }

    /**
     * 显示右边的第二个图片按钮
     *
     * @param resourceId
     */
    public void showRightSecondImgBtn(int resourceId) {
        layout_title_view_sencond_right_img_btn.setVisibility(View.VISIBLE);
        layout_title_view_sencond_right_img_btn.setImageResource(resourceId);
    }

    /**
     * 设置标题为图片
     *
     * @param bitmap
     */
    public void setImageBtn(Bitmap bitmap) {
        titleText.setVisibility(View.GONE);
        titleImg.setVisibility(View.VISIBLE);
        titleImg.setImageBitmap(bitmap);
    }

    /**
     * 设置标题为图片
     */
    public void setImageBtn(int drawable) {
        titleText.setVisibility(View.GONE);
        titleImg.setVisibility(View.VISIBLE);
        titleImg.setImageResource(drawable);
    }

    /**
     * 设置右边图片padding
     *
     * @param context
     */
    public void setRightImgBtnPading(Context context, int left, int top, int right, int bottom) {
        rightImgBtn.setPadding(left, top, right, bottom);
    }

    public void leftImageFinish(final Activity context) {
        returnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }
}
