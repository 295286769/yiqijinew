package com.yiqiji.money.modules.common.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，
 *
 * @author huangweishui
 */
public class PullToRefreshLayout extends RelativeLayout {
    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // // 有更多数据
    // public static final int HASMORE = 6;
    // // 没有更多数据
    // public static final int NOMORE = 7;
    // 当前状态
    private int state = INIT;

    // 刷新回调接口
    private OnRefreshListener mListener;
    // 加载回调接口
    private OnLoadMoreListenner loadMoreListenner;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    // 释放加载的距离
    private float loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private View refreshingView;
    // 刷新结果图标
    private View refreshStateImageView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;

    // 下拉菊花
    private View loading_refresh;

    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
//    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    private Context mContext;
    private AnimationDrawable animationDrawable;
    private boolean isFisrt = true;
    private boolean hasMore = true;// true可以加载更多false没有更多数据

    private boolean isApha = false;// true透明 false 不透明
    private ImageView imageView;
    private int position = 0;
    private float image_height = 0;
    private float image_with = 0;

    public void setImageScale(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setIsApha(boolean isApha) {
        this.isApha = isApha;
        if (refreshView == null) {
            return;
        }
        if (isApha) {
            refreshView.setBackgroundColor(getResources().getColor(R.color.alpha));
            refreshingView.setVisibility(View.INVISIBLE);
        } else {
            refreshView.setBackgroundColor(getResources().getColor(R.color.refre_bagroun));
        }

    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        if (!hasMore) {
            hide();
        }
    }

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }

            }

            if (pullDownY > 0) {
                pullDownY -= MOVE_SPEED;

            } else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                pullView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
//                pullUpView.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            setImageHeight();
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY + Math.abs(pullUpY) == 0)
                timer.cancel();
        }

    };

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public void setOnLoadMoreListenner(OnLoadMoreListenner loadMoreListenner) {
        this.loadMoreListenner = loadMoreListenner;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        if (loading_refresh == null || refreshingView == null) {
            return;
        }
        loading_refresh.clearAnimation();
        loading_refresh.setVisibility(View.GONE);

        refreshingView.clearAnimation();
        // refreshingView.setVisibility(View.GONE);
        animationDrawable.stop();
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                if (!isApha) {
                    refreshStateImageView.setVisibility(View.VISIBLE);
                }

                refreshStateTextView.setText(R.string.refresh_succeed);
                // requestLayout();
                // refreshStateImageView.setBackgroundResource(R.drawable.over);
                break;
            case FAIL:
            default:
                // 刷新失败
                if (!isApha) {
                    refreshStateImageView.setVisibility(View.VISIBLE);
                }
                refreshStateTextView.setText(R.string.refresh_fail);
                // refreshStateImageView
                // .setBackgroundResource(R.drawable.refresh_failed);
                break;
        }
        if (pullDownY > 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 1000);
        } else {
            changeState(DONE);
            hide();
        }
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        if (loadingView == null) {
            return;
        }
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);

        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_succeed);
                loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
                // requestLayout();
                break;
            case FAIL:
            default:
                // 加载失败
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_fail);
                loadStateImageView.setBackgroundResource(R.drawable.load_failed);
                break;
        }
        if (pullUpY < 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 1000);
        } else {
            // if(hasMore){
            // changeState();
            // }else{
            changeState(DONE);
            // }
            hide();
        }
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                pullView.clearAnimation();
                pullView.setVisibility(View.GONE);
                // 上拉布局初始状态
                loadmoreView.setVisibility(View.INVISIBLE);
                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.pullup_to_load);
//                pullUpView.clearAnimation();
//                pullUpView.setVisibility(View.VISIBLE);
                if (imageView != null) {
                    loading_refresh.setVisibility(View.INVISIBLE);
                } else {
                    loading_refresh.setVisibility(View.VISIBLE);
                    loading_refresh.startAnimation(refreshingAnimation);
                }

                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshStateTextView.setText(R.string.release_to_refresh);
                pullView.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                pullView.clearAnimation();
                if (!isApha) {
                    refreshingView.setVisibility(View.VISIBLE);
                }
                if (imageView != null) {
                    loading_refresh.setVisibility(View.INVISIBLE);
                } else {
                    loading_refresh.setVisibility(View.VISIBLE);
                }

                animationDrawable.start();
                pullView.setVisibility(View.GONE);
                // refreshingView.startAnimation(refreshingAnimation);
                refreshStateTextView.setText(R.string.refreshing);

                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadmoreView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.release_to_load);
//                pullUpView.startAnimation(rotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
//                pullUpView.clearAnimation();
                loadingView.setVisibility(View.VISIBLE);
//                pullUpView.setVisibility(View.INVISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                // if (pullScaleDown > 0 && imageView != null) {
                // overImageScale();
                // }

                break;
            // case HASMORE:
            // break;
            // case NOMORE:
            // loadStateTextView.setText(R.string.has_no_more);
            // break;

        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /**
     * dip转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private float imageView_with = 0;
    private float imageView_height = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            if (pullDownY < refreshingView_height + Dp2Px(mContext, 20)) {
                params.height = (int) (pullDownY * (refreshingView_height / (refreshingView_height + Dp2Px(mContext, 20))));

                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                refreshingView.setLayoutParams(params);
                // setScale();
            }

        }

    };
    private int downX = 0;
    private int dowY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                dowY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deldex = (int) (ev.getX() - downX);
                int deldey = (int) (ev.getY() - dowY);
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

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (isFisrt) {
                    isFisrt = false;
                    refresh_height = refreshView.getMeasuredHeight();
                    refreshingView_height = refreshingView.getMeasuredHeight();
                }

                downY = ev.getY();
                downX = (int) ev.getX();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:

                int deldex = (int) (ev.getX() - downX);
                int deldey = (int) (ev.getY() - downY);
                if (Math.abs(deldex) > Math.abs(deldey)) {// 左右切换事件
                    return super.dispatchTouchEvent(ev);
                }
                if (mEvents == 0) {
                    if ((pullDownY > 0 || (((Pullable) pullableView).canPullDown() && canPullDown && state != LOADING))) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;

                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                        handler.sendEmptyMessage(0);

                    } else if ((pullUpY < 0 || (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING))) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                setImageHeight();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0)
                    requestLayout();
                if (pullDownY > 0) {
                    if (pullDownY <= refreshDist && (state == RELEASE_TO_REFRESH || state == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }
                    if (pullDownY >= refreshDist && state == INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }
                } else if (pullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadmoreDist && (state == RELEASE_TO_LOAD || state == DONE)) {
                        changeState(INIT);
                    }
                    // 上拉操作
                    if (-pullUpY >= loadmoreDist && state == INIT) {
                        if (hasMore) {
                            changeState(RELEASE_TO_LOAD);
                        } else {
                            changeState(DONE);
                        }

                    }

                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null)
                        mListener.onRefresh(this);
                } else if (state == RELEASE_TO_LOAD) {
                    if (hasMore) {

                        changeState(LOADING);
                        // 加载操作
                        if (loadMoreListenner != null)
                            loadMoreListenner.onLoadMore(this);
                    } else {
                        changeState(DONE);
                    }

                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 根据上下滑动动态改变image大小
     */
    private void setImageHeight() {
        if (getScrollY() >= 0) {

            int height_imageView = (int) ((int) (pullDownY + pullUpY) + image_height);
            int with_imageView = (int) (((pullDownY + pullUpY) + image_height) / image_height * image_with);
            if (pullDownY == 0) {
                with_imageView = (int) image_with;
            }
            if (with_imageView < image_height) {
                with_imageView = (int) image_height;
            }
            if (imageView != null) {
                LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
                layoutParams.height = height_imageView;
                layoutParams.width = with_imageView;
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ScaleType.CENTER_CROP);

            }
        }
    }

    /**
     * @author chenjing 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullToRefreshLayout.this);

            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        pullUpY = -loadmoreDist;
        requestLayout();
        changeState(LOADING);
        // 加载操作
        if (loadMoreListenner != null) {
            loadMoreListenner.onLoadMore(this);
        }
    }

    private void initView() {
        // 初始化下拉布局
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.state_iv);
        loading_refresh = refreshView.findViewById(R.id.loading_refresh);
        // 初始化上拉布局
//        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);

        animationDrawable = (AnimationDrawable) ((ImageView) refreshingView).getDrawable();
        if (isApha) {
            refreshView.setBackgroundColor(getResources().getColor(R.color.alpha));
            refreshingView.setVisibility(View.INVISIBLE);
        } else {
            refreshView.setBackgroundColor(getResources().getColor(R.color.refre_bagroun));
        }
    }

    private float refresh_height = 0;
    private float refreshingView_height = 0;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasWindowFocus);

    }

    public void initImageHeight(int height, int with) {
        image_height = height;
        image_with = with;
        // if (position == 0) {
        // image_height = imageView.getHeight();
        // image_with = imageView.getWidth();
        //
        // }
        // Log.i("tag", "onMeasure+++++" + image_height);
        // if (image_height > 0) {
        // position++;
        // }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);

            isLayout = true;
            initView();
            refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
        }
        refreshView.setBackgroundColor(getResources().getColor(R.color.alpha));
        // loadmoreView.setBackgroundColor(getResources().getColor(R.color.alpha));
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        refreshView.layout(0, (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));

        int height = pullableView.getMeasuredHeight();
        if (pullableView instanceof MyRecyclerView) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) ((MyRecyclerView) pullableView)
                    .getLayoutManager();

            if (layoutManager != null) {
                View view = layoutManager.findViewByPosition(0);

                if (view != null) {
                    height = view.getMeasuredHeight() * ((MyRecyclerView) pullableView).getAdapter().getItemCount();
                    if (height > pullableView.getMeasuredHeight()) {
                        height = pullableView.getMeasuredHeight();
                    }
                }
            }
        }

        pullableView.layout(0, (int) (pullDownY + pullUpY), pullableView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + height);
        loadmoreView.layout(0, (int) (pullDownY + pullUpY) + height, loadmoreView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + height + loadmoreView.getMeasuredHeight());

    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        // /**
        // * 加载操作
        // */
        // void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

    public interface OnLoadMoreListenner {
        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

    /**
     * 完成刷新，加载操作
     */
    public void finishLoadAndFresh() {
        // 千万别忘了告诉控件加载完毕了哦！
        refreshFinish(SUCCEED);
        loadmoreFinish(SUCCEED);

    }

}
