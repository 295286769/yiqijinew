package com.yiqiji.money.modules.common.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.view.Pullable;

public class MyScrollView extends ScrollView implements Pullable {
    private float dletY = 0;
    private float topHeight = 0;
    private Context mContext;

    private OnScrollListener onScrollListener;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public MyScrollView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            // 此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }

        }

        ;

    };


    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasWindowFocus);
        LinearLayout view = (LinearLayout) getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) view.getChildAt(0);
        int layouHeight = 0;
        if (view.getChildAt(1) instanceof LinearLayout) {
            LinearLayout linearLayoutwo = (LinearLayout) view.getChildAt(1);
            layouHeight = linearLayoutwo.getHeight();
        } else if (view.getChildAt(1) instanceof RelativeLayout) {
            RelativeLayout linearLayoutwo = (RelativeLayout) view.getChildAt(1);
            layouHeight = linearLayoutwo.getHeight();
        }

        topHeight = linearLayout.getHeight() + layouHeight
                + UIHelper.Dp2Px(MyApplicaction.getContext(), 15);
        // setMeasuredDimension(getWidth(), getHeight() + (int) topHeight);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

    private int downX = 0;
    private int downY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deldex = (int) (ev.getX() - downX);
                int deldey = (int) (ev.getY() - downY);
                if (Math.abs(deldex) > Math.abs(deldey)) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    // switch (ev.getAction()) {
    // case MotionEvent.ACTION_DOWN:
    // downX = (int) ev.getX();
    // downY = (int) ev.getY();
    // break;
    // case MotionEvent.ACTION_MOVE:
    // int deldex = (int) (ev.getX() - downX);
    // int deldey = (int) (ev.getY() - downY);
    // if (Math.abs(deldex) > Math.abs(deldey)) {
    // return false;
    // }
    //
    // break;
    //
    // default:
    // break;
    // }
    // return super.dispatchTouchEvent(ev);
    // }

}
