package com.yiqiji.money.modules.homeModule.home.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.facade.TaskQueue;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.community.decoration.ActivityUtil;
import com.yiqiji.money.modules.community.utils.StartActivityUtil;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/27.
 * address huang.weishui@71dai.com
 */
public class HomeRenovationPromptView extends RelativeLayout {
    @BindView(R.id.prompt_text)
    TextView promptText;
    private Context mContext;
    private int sreenHeight;
    private int viewWith = 0;
    private int start_viewWith = 0;
    private int end_viewWith = 0;
    private View view;
    private String number_books_string = "";
    private String mAccountbookcateid;
    private Drawable drawable;
    private boolean isStart;


    public HomeRenovationPromptView(Context context) {
        this(context, null);
    }

    public HomeRenovationPromptView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeRenovationPromptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        sreenHeight = XzbUtils.getPhoneScreenintWith((Activity) mContext);
        initView();
        initDrawble();

    }

    private void initDrawble() {
        drawable = getResources().getDrawable(R.drawable.book_prompt_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_home_renovation_prompt, this, true);
        ButterKnife.bind(this, view);
        this.setVisibility(View.GONE);
        onClickView();
    }

    private void onClickView() {
        if (promptText != null) {
            promptText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String prompt_text = promptText.getText().toString();
                    if (TextUtils.isEmpty(prompt_text)) {
                        getvalueAnimatorStart().start();
                    } else {
                        getvalueAnimatorEnd().start();
                        if (mAccountbookcateid.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
                            ActivityUtil.startDecorateHome(mContext);
                        } else {
                            StartActivityUtil.startTravelMain(mContext);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWith = getMeasuredWidth();
        start_viewWith = viewWith - UIHelper.Dp2Px(mContext, 30);
        end_viewWith = sreenHeight - UIHelper.Dp2Px(mContext, 30);
    }

    public void setNumberBooks(CheckMessageInfo.MessgeinfoItem messgeinfoItem, BooksDbInfo booksDbInfo) {
        this.setVisibility(View.GONE);
        if (booksDbInfo != null) {
            mAccountbookcateid = booksDbInfo.getAccountbookcate();
            if (booksDbInfo.getAccountbookcate().equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
                this.setVisibility(View.VISIBLE);
                number_books_string = "发现" + messgeinfoItem.getSharehouse() + "个共享的装修账本";
                setTextChange();
            } else if (booksDbInfo.getAccountbookcate().equals(RequsterTag.ACCOUNTBOOKCATE) ||
                    booksDbInfo.getAccountbookcate().equals(RequsterTag.TOURISMACCOUNTBOOKCATE)) {
                this.setVisibility(View.VISIBLE);
                number_books_string = "发现" + messgeinfoItem.getSharetravel() + "个共享的旅行账本";
                setTextChange();
            }
        }
    }

    private void setTextChange() {
        if (!isStart) {
            promptText.setText(number_books_string);
            promptText.setCompoundDrawables(null, null, null, null);
        }
        TaskQueue.mainQueue().executeDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStart) {
                    getvalueAnimatorStart().start();
                } else {

                    getvalueAnimatorEnd().start();
                }

            }
        }, 5000);
    }

    public ValueAnimator getvalueAnimatorStart() {
        isStart = false;
        view.clearAnimation();
        ValueAnimator valueAnimator_start = ValueAnimator.ofFloat(-start_viewWith, 0);
        valueAnimator_start.setTarget(view);
        valueAnimator_start.setDuration(500);
        valueAnimator_start.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animation_trance = -(float) animation.getAnimatedValue();
                view.setTranslationX(animation_trance);
            }
        });
        valueAnimator_start.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                promptText.setText(number_books_string);
                promptText.setCompoundDrawables(null, null, null, null);
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
        return valueAnimator_start;
    }

    public ValueAnimator getvalueAnimatorEnd() {
        isStart = true;
        view.clearAnimation();
        ValueAnimator valueAnimator_end = ValueAnimator.ofFloat(0, start_viewWith);
        valueAnimator_end.setTarget(view);
        valueAnimator_end.setDuration(500);
        valueAnimator_end.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animation_trance = (float) animation.getAnimatedValue();
                view.setTranslationX(animation_trance);
            }
        });
        valueAnimator_end.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                promptText.setText("");
                promptText.setCompoundDrawables(drawable, null, null, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return valueAnimator_end;
    }

    private void animationView() {
        ValueAnimator valueAnimator = getvalueAnimatorEnd();
        valueAnimator.setStartDelay(5000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(500);
        animatorSet.play(getvalueAnimatorStart()).before(valueAnimator);
        animatorSet.start();
    }

    public void clearViewAnimation() {
        if (view != null) {
            view.clearAnimation();
            promptText.setText("");
            promptText.setCompoundDrawables(drawable, null, null, null);
        }
    }
}
