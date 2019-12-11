package com.yiqiji.money.modules.common.view.pickerview;

final class OnItemSelectedRunnable implements Runnable {
	final WheelView loopView;

	OnItemSelectedRunnable(WheelView loopview) {
		loopView = loopview;
	}

	@Override
	public final void run() {
		loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
	}
}
