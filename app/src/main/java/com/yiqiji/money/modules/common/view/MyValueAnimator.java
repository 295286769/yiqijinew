package com.yiqiji.money.modules.common.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyValueAnimator {
	private final View view;
	public static int start = 0;
	private ImageView image_delete;
	private ImageView image_icon;

	public MyValueAnimator(View view, ImageView image_delete, ImageView image_icon) {
		this.view = view;
		this.image_delete = image_delete;
		this.image_icon = image_icon;
	}

	public void startAnimator() {

		View parent = (View) view.getParent();
		if (parent == null)
			throw new IllegalStateException("Cannot animate the layout of a view that has no parent");

		start = view.getMeasuredHeight();
		view.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		int end = 0;

		final ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				final ViewGroup.LayoutParams lp = view.getLayoutParams();
				lp.height = (Integer) animation.getAnimatedValue();
				view.setLayoutParams(lp);
			}
		});
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub

				onAnimation.onAnimation();

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		animator.setDuration(500);
		animator.start();

	}

	private OnAnimationListener onAnimation;

	public interface OnAnimationListener {
		void onAnimation();
	}

	public void setOnAnimationListener(OnAnimationListener onAnimation) {
		this.onAnimation = onAnimation;
	}

}
