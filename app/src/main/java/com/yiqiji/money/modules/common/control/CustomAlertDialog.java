package com.yiqiji.money.modules.common.control;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;

import java.util.List;

/**
 * Created by dansakai on 2017/3/16.
 */

public class CustomAlertDialog extends Dialog {

    private Context mContext;// 上下文
    private TextView tvTitle, tvMessage, tvLeftButton, tvRightButton;// 标题，正文，左下按钮，右下按钮
    private LinearLayout llContent;// 内部添加自定义布局
    private View view_line;// 底部的线
    private LinearLayout llButtons;// 底部的按钮布局
    private View view_button;// 按钮之间的线

    //升级和吐槽弹框
    private RelativeLayout rl_leftIcon;//左边的图标布局
    private ImageView iv_leftIcon;//左边图标
    private TextView tv_versionNm;//升级版本号
    private RelativeLayout rl_cancle;//右边取消按钮布局
    private LinearLayout ll_upgrade;//升级按钮组
    private TextView tv_upVersion;//升级按钮
    private TextView tv_debunk;//吐槽按钮

    public CustomAlertDialog(Context context) {
        super(context, R.style.Dialog);// 创建一个使用指定样式的对话框
        setContentView(R.layout.a_widget_customalertdialog);
        this.mContext = context;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.GONE);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setVisibility(View.GONE);
        tvLeftButton = (TextView) findViewById(R.id.tvLeftButton);
        tvLeftButton.setBackgroundResource(R.drawable.actionsheet_cancel_selector);
        tvLeftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvRightButton = (TextView) findViewById(R.id.tvRightButton);
        llContent = (LinearLayout) findViewById(R.id.llContent);
        view_line = findViewById(R.id.view_line);
        llButtons = (LinearLayout) findViewById(R.id.llButtons);
        view_button = findViewById(R.id.view_button);

        rl_leftIcon = (RelativeLayout) findViewById(R.id.rl_leftIcon);
        rl_leftIcon.setVisibility(View.GONE);
        iv_leftIcon = (ImageView) findViewById(R.id.iv_leftIcon);
        tv_versionNm = (TextView) findViewById(R.id.tv_versionNm);

        rl_cancle = (RelativeLayout) findViewById(R.id.rl_cancle);
        rl_cancle.setVisibility(View.GONE);
        rl_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_upgrade = (LinearLayout) findViewById(R.id.ll_upgrade);
        ll_upgrade.setVisibility(View.GONE);
        tv_upVersion = (TextView) findViewById(R.id.tv_upVersion);
        tv_upVersion.setVisibility(View.GONE);
        tv_debunk = (TextView) findViewById(R.id.tv_debunk);
        tv_debunk.setVisibility(View.GONE);

    }

    private AdapterView.OnItemClickListener mItemClickListener;

    /**
     * 充值对话框
     */
    public CustomAlertDialog reset() {
        tvTitle.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
        llContent.removeAllViews();
        view_line.setVisibility(View.VISIBLE);
        llButtons.setVisibility(View.VISIBLE);
        tvLeftButton.setVisibility(View.VISIBLE);
        view_button.setVisibility(View.VISIBLE);
        tvRightButton.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置单选
     *
     * @param list     泛型集合
     * @param listener 每一项点击事件
     */
    public CustomAlertDialog setItems(List<?> list, AdapterView.OnItemClickListener listener) {
        Object[] items;
        if (!StringUtils.isEmptyList(list)) {
            items = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                items[i] = list.get(i);
            }
            setItems(items, listener);
        }
        return this;
    }

    /**
     * 设置单选
     *
     * @param items    显示文案支持 String 和Spanned类型
     * @param listener
     */
    public CustomAlertDialog setItems(Object[] items, AdapterView.OnItemClickListener listener) {
        tvMessage.setVisibility(View.GONE);

        this.mItemClickListener = listener;

        llButtons.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);

        ScrollView scroll = new ScrollView(mContext);
        scroll.setVerticalScrollBarEnabled(false);
        LinearLayout llItems = new LinearLayout(mContext);
        scroll.addView(llItems);
        scroll.setScrollbarFadingEnabled(false);
        llItems.setOrientation(LinearLayout.VERTICAL);
        llContent.removeAllViews();
        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                UIHelper.dip2px(mContext, 0.5f)));
        view.setBackgroundColor(Color.parseColor("#D8D8D8"));
        llContent.addView(view);
        llContent.addView(scroll, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // 设置ListView的按压效果
        for (int i = 0; i < items.length; i++) {
            final int itemPos = i;
            TextView item = (TextView) LayoutInflater.from(mContext).inflate(
                    R.layout.common_listview_item, null);
            if (itemPos == 0) {
                if (itemPos == (items.length - 1)) {
                    // 只有一项
                    item.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    // 第一项
                    item.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                }
            } else if (itemPos == (items.length - 1))
                // 最后一项
                if (itemPos < 8) {
                    item.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    item.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                }
            else {
                // 中间一项
                item.setBackgroundResource(R.drawable.actionsheet_middle_selector);
            }
            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(null, null, itemPos, 0);
                    }
                    dismiss();
                }
            });
            // 显示文案支持 String 和Spanned类型
            if (items[itemPos] instanceof String) {
                String text = (String) items[itemPos];
                item.setText(text);
            } else if (items[itemPos] instanceof Spanned) {
                Spanned sp = (Spanned) items[itemPos];
                item.setText(sp);
            } else {
                // 其他类型的实体，返回对应得tostring，对应实体需要重写tostring方法
                String text = items[itemPos].toString();
                item.setText(text);
            }

            item.setPadding(UIHelper.dip2px(mContext, 20), 0, UIHelper.dip2px(mContext, 20), 0);
            llItems.addView(item);
        }
        return this;
    }

    /**
     * 设置自定义View
     *
     * @param v
     */
    public CustomAlertDialog setView(View v) {
        llContent.removeAllViews();
        llContent.addView(v);
        llContent.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置自定义View
     */
    public CustomAlertDialog setView(View v, boolean hasButtons) {
        llContent.removeAllViews();
        llContent.addView(v);
        if (hasButtons) {
            llButtons.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
        } else {
            llButtons.setVisibility(View.GONE);
            view_line.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    /**
     * 设置title
     *
     * @param text
     */
    public CustomAlertDialog setTitle(String text) {
        if (StringUtils.isEmpty(text)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(text);
        }
        return this;
    }

    /**
     * 设置message
     *
     * @param text
     */
    public CustomAlertDialog setMessage(String text) {
        if (StringUtils.isEmpty(text)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(text);
        }
        // 隐藏列表布局
        llContent.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置message支持html
     *
     * @param text
     */
    public CustomAlertDialog setMessageHtml(String text) {
        if (StringUtils.isEmpty(text)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(Html.fromHtml(text));
        }
        // 隐藏列表布局
        llContent.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置左边按钮文案和点击事件
     *
     * @param text
     * @param listener
     */
    public CustomAlertDialog setLeftButton(String text, View.OnClickListener listener) {
        tvLeftButton.setVisibility(View.VISIBLE);
        view_button.setVisibility(View.VISIBLE);
        tvLeftButton.setText(text);
        if (listener != null) {
            tvLeftButton.setOnClickListener(listener);
        } else {
            tvLeftButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }

    /**
     * 设置左边按钮文案和点击事件
     *
     * @param listener
     */
    public CustomAlertDialog setLeftButtonListener(View.OnClickListener listener) {
        if (listener != null) {
            tvLeftButton.setOnClickListener(listener);
        } else {
            tvLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }


    /**
     * 设置左边按钮文案和点击事件
     *
     * @param text
     * @param listener
     */
    public CustomAlertDialog setRightButton(String text, View.OnClickListener listener) {
        tvRightButton.setText(text);
        tvRightButton.setBackgroundResource(R.drawable.actionsheet_confirm_selector);
        if (listener != null) {
            tvRightButton.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置左边按钮点击事件
     *
     * @param listener
     */
    public CustomAlertDialog setRightButtonListener(View.OnClickListener listener) {
        tvRightButton.setOnClickListener(listener);
        return this;
    }

    /**
     * 只显示一个按钮
     *
     * @param text
     * @param listener 如果null,则默认dismiss
     */
    public CustomAlertDialog showOneButton(String text, View.OnClickListener listener) {
        tvRightButton.setText(text);
        tvRightButton
                .setBackgroundResource(R.drawable.actionsheet_bottom_selector);
        if (listener != null) {
            tvRightButton.setOnClickListener(listener);
        } else {
            tvRightButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        tvLeftButton.setVisibility(View.GONE);
        view_button.setVisibility(View.GONE);
        return this;
    }

    /**
     * 显示升级按钮
     * @param text //
     * @param Uplistener //升级
     * @param debunklistener//吐槽（没有吐槽按钮传null）
     * @return
     */
    public CustomAlertDialog showUpButton(String text,String version, View.OnClickListener Uplistener, View.OnClickListener debunklistener,boolean isCancleable) {
        llButtons.setVisibility(View.GONE);
        rl_leftIcon.setVisibility(View.VISIBLE);
        ll_upgrade.setVisibility(View.VISIBLE);
        tv_upVersion.setVisibility(View.VISIBLE);
        tv_upVersion.setText(text);
        view_line.setVisibility(View.GONE);
        if (isCancleable) {
            rl_cancle.setVisibility(View.VISIBLE);
        } else {
            rl_cancle.setVisibility(View.GONE);
        }
        setIsCancel(isCancleable);
        if (debunklistener == null) {//是升级弹窗
            tv_debunk.setVisibility(View.GONE);
            iv_leftIcon.setImageResource(R.drawable.icon_upgrade);
            tv_versionNm.setVisibility(View.VISIBLE);
            tv_versionNm.setText(version);
        } else {
            tv_debunk.setVisibility(View.VISIBLE);
            iv_leftIcon.setImageResource(R.drawable.icon_write_back);
            tv_versionNm.setVisibility(View.GONE);
        }

        if (Uplistener != null) {
            tv_upVersion.setOnClickListener(Uplistener);
        } else {
            tv_upVersion.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        if (debunklistener != null) {
            tv_debunk.setOnClickListener(debunklistener);
        } else {
            tv_debunk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        return this;
    }


    /**
     * 仅设置Message
     *
     * @param text
     */
    public CustomAlertDialog setMessageOnly(String text) {
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(StringUtils.isEmpty(text) ? "" : text);

        view_line.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        llButtons.setVisibility(View.GONE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvMessage
                .getLayoutParams();
        lp.bottomMargin = UIHelper.dip2px(mContext, 15);
        tvMessage.setLayoutParams(lp);
        return this;
    }



    /**
     * 是否可以取消
     */
    public CustomAlertDialog setIsCancel(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
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
