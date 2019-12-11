package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.yiqiji.money.R;

/**
 * Created by dansakai on 2017/7/3.
 * 线性列表布局（用于少量item布局）
 */

public class LinearLayoutForItem extends LinearLayout {
    private Context mContext;
    private BaseAdapter adapter;

    private OnItemClickLisntener onItemClickLisntener;

    public LinearLayoutForItem(Context context) {
        this(context, null);
    }

    public LinearLayoutForItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
        mContext = context;
        this.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    /**
     * 获取水平线
     *
     * @return
     */
    private View getLine() {
        View v = new View(mContext);
        v.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        v.setBackgroundColor(getResources().getColor(
                R.color.split_line));
        return v;
    }

    /**
     * 开始绘制
     */
    private void drawLinearLayout() {
        removeAllViews();
        LinearLayout ll_tr = new LinearLayout(mContext);
        ll_tr.setOrientation(LinearLayout.VERTICAL);

        int count = adapter.getCount();
        for (int i = 0;i < count;i++) {
            final int index = i;
            View view = adapter.getView(index, null, null);
            ll_tr.addView(view);
            if (onItemClickLisntener != null) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        onItemClickLisntener.onItemClick(index);
                    }
                });
            }

        }

        addView(ll_tr);

    }

    public void setAdapter(BaseAdapter baseAdapter) {
        if (baseAdapter == null || baseAdapter.getCount() == 0) {
            removeAllViews();
            return;
        }
        this.adapter = baseAdapter;
        drawLinearLayout();
    }

    public void setOnItemClickLisntener(
            OnItemClickLisntener onItemClickLisntener) {
        this.onItemClickLisntener = onItemClickLisntener;
    }

    public interface OnItemClickLisntener {
        void onItemClick(int position);
    }
}
