package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;

/**
 * 热门,A,B,C,D.... 侧边的选项bar
 *
 * @author wangf
 */
public class BladeView extends View {

    private Context context;
    private Paint paint;

    private ListView list;
    private int mCurIdx;
    private OnSelectedListener mOnSelectedListener;
    private String[] mSections;
    private int m_nItemHeight = 25;// 每一项的高度
    private SectionIndexer sectionIndexter = null;

    private View toastView;
    private TextView tv_toast;
    private Toast costumeToast;

    /**
     * 构造函数
     */
    public BladeView(Context context) {
        super(context);
        this.context = context;
        initToastView();
    }

    /**
     * 构造函数
     */
    public BladeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initToastView();
    }

    /**
     * 构造函数
     */
    public BladeView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        this.context = context;
        initToastView();
    }

    /**
     * 初始化toast显示的view和设置背景
     */
    private void initToastView() {
        paint = new Paint();
        toastView = LayoutInflater.from(context).inflate(R.layout.custome_toast_layout, (ViewGroup) null);
        tv_toast = (TextView) toastView.findViewById(R.id.tv);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));// 设置背景颜色
    }

    /**
     * 获取当前选中项
     */
    public int getCurIndex() {
        return this.mCurIdx;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSections != null) {
            // 画笔设置颜色
            paint.setColor(Color.parseColor("#7689A2"));
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            paint.setTextSize(UIHelper.sp2px(context, 12));
            // 设置画笔画出的东西居中
            paint.setTextAlign(Align.CENTER);
            float xPos = (float) getMeasuredWidth() / 2.0F;

            // 循环每一项，画图
            for (int i = 0; i < mSections.length; ++i) {
                String text = String.valueOf(this.mSections[i]);// 单项值
                // 如果长度大于2，则截取第一个字符
                if (text.length() > 2) {
                    text = text.substring(0, 1);
                }

                canvas.drawText(text, xPos, (float) (m_nItemHeight + i * m_nItemHeight), paint);
            }

            super.onDraw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mSections != null && this.mSections.length > 0) {
            // 设置每一个的项的高度
            this.m_nItemHeight = (-10 + (bottom - top)) / this.mSections.length;
        }
    }

    /**
     * 触摸AlaphaBar事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int touchPos = (int) event.getY() / m_nItemHeight;// 触摸的是第几项
        // 如果超过边界设置
        if (touchPos >= mSections.length) {
            touchPos = -1 + mSections.length;
        } else if (touchPos < 0) {
            touchPos = 0;
        }

        mCurIdx = touchPos;
        if (event.getAction() != MotionEvent.ACTION_DOWN
                && event.getAction() != MotionEvent.ACTION_MOVE) {
            if (event.getAction() == MotionEvent.ACTION_UP) {// 如果触摸事件是提起
                if (mOnSelectedListener != null) {
                    mOnSelectedListener.onUnselected();
                }
                setBackgroundColor(getResources().getColor(android.R.color.transparent));// up后背景还原
                if(costumeToast != null) {
                    costumeToast.cancel();// 取消toast
                }
                return true;
            }
        } else {
            if (sectionIndexter == null) {
                sectionIndexter = (SectionIndexer) list.getAdapter();
            }
            // 区域的位置,MergeAdapter的第几项
            int setctionPos = sectionIndexter.getPositionForSection(touchPos);
            if (setctionPos != -1) {
                list.setSelection(setctionPos);// ListView选中第 var4项，设置背景
                // 设置toast显示的view
                if (mSections[touchPos].length() == 1) {
                    tv_toast.setText(mSections[touchPos]);
                    if (costumeToast == null) {
                        costumeToast = new Toast(context.getApplicationContext());
                    }
                    costumeToast.setGravity(Gravity.CENTER, 0, 0);
                    costumeToast.setView(toastView);
                    costumeToast.setDuration(Toast.LENGTH_LONG);
                    costumeToast.show();
                }

                if (this.mOnSelectedListener != null) {
                    this.mOnSelectedListener.onSelected(setctionPos);
                }
                // 设置按压的背景颜色
//                setBackgroundResource(R.drawable.bg_littleround_gray);
                return true;
            }
        }

        return true;
    }

    /**
     * 设置ListView
     */
    public void setListView(ListView listview) {
        this.list = listview;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mOnSelectedListener = listener;
    }

    /**
     * 赋值实现了 SectionIndexter的MerageAdapter
     */
    public void setSectionIndexter(SectionIndexer var1) {
        this.sectionIndexter = var1;
        this.mSections = (String[]) ((String[]) var1.getSections());
        this.requestLayout();
    }

    public void setSections(String[] var1) {
        this.mSections = var1;
        if (var1 == null || var1.length == 0) {
            this.setVisibility(View.GONE);
        }

    }

    /**
     * 接口，选中监听器
     */
    public interface OnSelectedListener {

        void onSelected(int var1);

        void onUnselected();
    }
}