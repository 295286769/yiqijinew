package com.yiqiji.money.modules.homeModule.home.perecenter;

import android.content.Context;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;

/**
 * Created by ${huangweishui} on 2017/5/25.
 * address huang.weishui@71dai.com
 */
public class JournalItemPerecenter {
    public static void setShowLoolAll(final Context mContext, final TextView textView, final TextView look_all) {
        if (textView != null) {
            textView.post(new Runnable() {
                @Override
                public void run() {
                    Layout layout = textView.getLayout();

                    if (layout != null) {
                        String full_text = "全文";
                        String wholeStr = "..." + full_text;
                        int size = UIHelper.Dp2Px(mContext, 15);
                        int textColor = mContext.getResources().getColor(R.color.main_back);
                        SpannableStringBuilder spannableStringBuilder = StringUtils.getStypeTextString(wholeStr, full_text, size, textColor);
                        int loneCounts = layout.getLineCount();
                        if (loneCounts > 2) {
                            look_all.setVisibility(View.VISIBLE);


                            look_all.setText(spannableStringBuilder);
//                            textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
//                            textView.setFocusable(false);
                        } else {
                            int ellipsisCount = layout.getEllipsisCount(loneCounts - 1);
                            if (ellipsisCount > 0) {
                                look_all.setVisibility(View.VISIBLE);
                                look_all.setText(spannableStringBuilder);
                            } else {
                                look_all.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        look_all.setVisibility(View.GONE);
                    }
                }
            });
        }

    }
}
