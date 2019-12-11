package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/5/3.
 */
public class RelativeLayoutSelectPeople extends RelativeLayout {
    public RelativeLayoutSelectPeople(Context context) {
        super(context);
    }

    public RelativeLayoutSelectPeople(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutSelectPeople(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 对事件进行拦截，让checkbox不获得监听
     * @param arg0
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return true;
    }
}
