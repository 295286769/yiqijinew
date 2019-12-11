package com.yiqiji.money.modules.common.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class AnimationUtil {
    // 左右晃动动画
    public static void shake(View v) {
        Animation animation = new TranslateAnimation(0, 10, 0, 0);
        animation.setInterpolator(new CycleInterpolator(8));
        animation.setDuration(1000);
        v.startAnimation(animation);
    }

    /**
     * @param activity
     * @param view
     */
    public static void setAddBillShow(final Activity activity, final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.clearAnimation();
                        float y = view.getMeasuredHeight();
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.height = XzbUtils.getStatusBarHeight(activity);
                        layoutParams.gravity = Gravity.CENTER;
                        view.setLayoutParams(layoutParams);
                        if (y == 0) {
                            y = XzbUtils.getStatusBarHeight(activity);
                        }

                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-y, 0);
                        valueAnimator.setDuration(500);
                        valueAnimator.setTarget(view);
                        valueAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                view.setVisibility(View.VISIBLE);
                                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float translationY = (float) animation.getAnimatedValue();
                                view.setTranslationY(translationY);
                            }
                        });
                        ValueAnimator valueAnimatorend = ValueAnimator.ofFloat(0, -y);
                        valueAnimatorend.setStartDelay(1000);
                        valueAnimatorend.setDuration(500);
                        valueAnimatorend.setTarget(view);
                        valueAnimatorend.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                view.setVisibility(View.GONE);
                                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        valueAnimatorend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float translationY = (float) animation.getAnimatedValue();
                                view.setTranslationY(translationY);
                            }
                        });
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playSequentially(valueAnimator, valueAnimatorend);
                        animatorSet.start();
                    }
                });
            }
        });

    }

    public static void onSelectedTimeAnimStart(final Context context, final Date date_time, final TextView todayTime, final TextView selecteTime) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(todayTime, "alpha", 1, 0);
        ObjectAnimator alpha_animator = ObjectAnimator.ofFloat(selecteTime, "alpha", 1, 0);
        ObjectAnimator translationX_animator = ObjectAnimator.ofFloat(selecteTime, "translationX", 0, UIHelper.Dp2Px(context, -200));
        AnimatorSet animatorSet = new AnimatorSet();
        //动画一起执行
        animatorSet.playTogether(animator, alpha_animator, translationX_animator);
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onTimeSelected(false, date_time, todayTime, selecteTime);
                onSelectedTimeAnimStartEnd(context, todayTime, selecteTime);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * @param isSelected
     * @param date         当前时间
     * @param todayTime
     * @param electionTime 点击的是时间控件
     */
    private static void onTimeSelected(boolean isSelected, Date date, TextView todayTime, TextView electionTime) {
        String[] SelectedStrings = DateUtil.getTimeSelectedStrings(isSelected, date);
        todayTime.setText(SelectedStrings[0]);
        electionTime.setText(SelectedStrings[1]);
    }

    private static void onSelectedTimeAnimStartEnd(Context context, TextView todayTime, TextView selecteTime) {
        //显示——透明——显示
        ObjectAnimator animator = ObjectAnimator.ofFloat(todayTime, "alpha", 0, 1);
        animator.setDuration(100);
        animator.start();
        //显示—不显示
        ObjectAnimator alpha_animator = ObjectAnimator.ofFloat(selecteTime, "alpha", 0, 1);
        //像左移动
        ObjectAnimator translationX_animator = ObjectAnimator.ofFloat(selecteTime, "translationX", UIHelper.Dp2Px(context, -200), 0);
        AnimatorSet animatorSet = new AnimatorSet();
        //先后执行
        animatorSet.playSequentially(translationX_animator, alpha_animator);
        animatorSet.setDuration(200);
        animatorSet.start();

    }
}
