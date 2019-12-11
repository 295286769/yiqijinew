package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.yiqiji.money.R;

/**
 * 支持网格布局，以及分割线
 */
public class LinearLayoutForTable extends LinearLayout {

    private int column = 0;
    private BaseAdapter adapter;
    private Context mContext;
    private LayoutParams lp_item, lp_item_first;
    private int def_margin = 10;
    private boolean showBorder;
    private OnItemClickLisntener onItemClickLisntener;

    public LinearLayoutForTable(Context context) {
        this(context,null);
    }

    public LinearLayoutForTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
        mContext = context;
        lp_item = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1);
        lp_item_first = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1);
        // 设置背景色为白色
        this.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    /**
     * 设置背景颜色resId
     * @param id
     */
    private void setBackground(int id) {
        this.setBackgroundColor(mContext.getResources().getColor(id));
    }

    /**
     * 获取水平或者垂直样式的线
     * @param type h-水平，v-垂直
     * @return
     */
    private View getLine(String type) {
        if ("h".equals(type)) {
            View v = new View(mContext);
            v.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, 1));
            v.setBackgroundColor(getResources().getColor(
                    R.color.split_line));
            return v;
            // return mLayoutInflater.inflate(R.layout.widget_line_h, null);
        } else if ("v".equals(type)) {
            View v = new View(mContext);
            v.setLayoutParams(new LayoutParams(1,
                    LayoutParams.MATCH_PARENT));
            v.setBackgroundColor(getResources().getColor(
                    R.color.split_line));
            return v;
            // return mLayoutInflater.inflate(R.layout.widget_line_v, null);
        } else {
            return null;
        }
    }

    /**
     * 开始绘制
     */
    private void drawLinearLayout() {
        /***************** 计算数量 *******************/
        removeAllViews();
        int realcount = adapter.getCount();
        int count = 0;
        if (realcount < column) {
            count = column;
        } else if (realcount % column != 0) {
            count = realcount + column - (realcount % column);
        } else {
            count = realcount;
        }
        /***************** 布局 *******************/
        LinearLayout ll_tr = new LinearLayout(mContext);
        ll_tr.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < count; i++) {
            final int index = i;
            View view = null;
            if (index >= realcount) {
                // 防止索引超出。超出的索引部分取最后的item；
                view = adapter.getView((realcount - 1), null, null);
                view.setVisibility(View.INVISIBLE);
            } else {
                view = adapter.getView(index, null, null);
            }
            if (index % column == 0) {
                // 第一列
                view.setLayoutParams(lp_item_first);
            } else {
                view.setLayoutParams(lp_item);
                if (showBorder) {
                    // 显示纵线
                    ll_tr.addView(getLine("v"));
                }
            }
            if (onItemClickLisntener != null) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        onItemClickLisntener.onItemClick(index);
                    }
                });
            }
            ll_tr.addView(view);
            if ((index + 1) % column == 0) {
                addView(ll_tr);
                if (showBorder && (index + 1) != count) {
                    // 在行的底部加border 最后一行不用
                    addView(getLine("h"));
                }
                ll_tr = new LinearLayout(mContext);
                ll_tr.setOrientation(LinearLayout.HORIZONTAL);
            }
        }
    }

    public void setAdapter(BaseAdapter baseAdapter, int column, int cmargin,
                           boolean border) {
        if (baseAdapter == null || baseAdapter.getCount() == 0) {
            removeAllViews();
            return;
        }
        this.showBorder = border;
        this.adapter = baseAdapter;
        this.column = column;
        lp_item.setMargins(cmargin, 0, 0, cmargin);
        lp_item_first.setMargins(0, 0, 0, cmargin);
        drawLinearLayout();
    }

    public void setAdapter(BaseAdapter baseAdapter, int column, int cmargin) {
        setAdapter(baseAdapter, column, cmargin, false);
    }

    public void setAdapter(BaseAdapter baseAdapter, int column) {
        setAdapter(baseAdapter, column, def_margin);
    }

    public void setAdapter(BaseAdapter baseAdapter) {
        setAdapter(baseAdapter, 4);
    }

    public OnItemClickLisntener getOnItemClickLisntener() {
        return onItemClickLisntener;
    }

    public void setOnItemClickLisntener(
            OnItemClickLisntener onItemClickLisntener) {
        this.onItemClickLisntener = onItemClickLisntener;
    }

    public interface OnItemClickLisntener {
        void onItemClick(int position);
    }
}
