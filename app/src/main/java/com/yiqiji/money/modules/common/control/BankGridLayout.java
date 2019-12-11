package com.yiqiji.money.modules.common.control;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.property.entity.PropertyBanEntity;
import com.yiqiji.frame.core.utils.StringUtils;

import java.util.List;

/**
 * Created by dansakai on 2017/3/10.
 */

public class BankGridLayout extends LinearLayout {

    private static final int COLUMN_COUNT = 4;//每一行的个数
    private LinearLayout categoryContainer;//显示类目容器
    private LinearLayout layout;//最外的LinearLayout
    private OnClickListener listener;//点击监听器
    private TextView titleView;//显示文本标题

    /**
     * 构造函数
     */
    public BankGridLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    /**
     * 构造函数
     */
    public BankGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bank_categray_layout, this, true);
        //从布局中获取控件
        layout = (LinearLayout) findViewById(R.id.layout);//外部LinearLayout
        titleView = (TextView) findViewById(R.id.city_title);//标题显示文案
        categoryContainer = (LinearLayout) findViewById(R.id.recommend_category_container);//类目容器
    }

    /**
     * 创建一个TextView,作为一行中的一小项
     *
     * @param entity AreaEntity,也支持CityEntity传入
     */
    private TextView createRecommendItem(Object entity) {
        TextView textView = new TextView(this.getContext());
        textView.setClickable(true);//设置可以点击
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0F;
        layoutParams.leftMargin = UIHelper.dip2px(this.getContext(), 4.0F);
        layoutParams.rightMargin = UIHelper.dip2px(this.getContext(), 4.0F);
        layoutParams.topMargin = UIHelper.dip2px(this.getContext(), 4.0F);
        layoutParams.bottomMargin = UIHelper.dip2px(this.getContext(), 4.0F);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        textView.setPadding(0, UIHelper.dip2px(this.getContext(), 5.0F), 0, UIHelper.dip2px(this.getContext(), 5.0F));

        textView.setBackgroundResource(R.drawable.activity_search_bg);

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0F);
        textView.setText(((PropertyBanEntity) entity).getBankname());//设置文案
        textView.setTextColor(this.getResources().getColor(R.color.black));
        textView.setTag(entity);//设置Tag，NovaTextView的tag为该City对象
        textView.setOnClickListener(this.listener);//设置监听器

        return textView;
    }

    /**
     * 返回一行的数据，返回LinearLayout
     *
     * @param list 一行的数据集合 地区集合，也可能是城市集合
     */
    private LinearLayout createRecommendRow(List<Object> list) {
        LinearLayout llLine = new LinearLayout(this.getContext());
        llLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        llLine.setOrientation(LinearLayout.HORIZONTAL);
        llLine.setBackgroundResource(android.R.color.transparent);

        // 创建一行数据，没有数据的显示空的view
        for (int i = 0; i < list.size() || i < COLUMN_COUNT; ++i) {
            if (i < list.size()) {
                llLine.addView(createRecommendItem(list.get(i)));
            } else {
                LayoutParams lp = new LayoutParams(0, -2);
                lp.weight = 1.0F;
                View view = new View(this.getContext());
                view.setLayoutParams(lp);
                lp.leftMargin = UIHelper.dip2px(getContext(), 4.0F);
                lp.rightMargin = UIHelper.dip2px(getContext(), 4.0F);
                lp.topMargin = UIHelper.dip2px(getContext(), 4.0F);
                lp.bottomMargin = UIHelper.dip2px(getContext(), 4.0F);
                llLine.addView(view);
            }
        }

        return llLine;
    }

    /**
     * 设置该控件的数据
     *
     * @param title    title，例如：热门国内城市
     * @param list     数据列表
     * @param listener 页面点击事件监听器OnClickListener
     * @param maxNum   显示的最多城市个数，-1：显示，6：最多显示6项数据
     */
    public void setItems(String title, List<Object> list, OnClickListener listener, int maxNum) {
        //数据集合有数据并且显示个数要不为0
        if (list != null && list.size() != 0 && maxNum != 0) {
            layout.setVisibility(View.VISIBLE);//显示该布局
            if (StringUtils.isEmpty(title)) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(title);//设置标题
            }

            categoryContainer.removeAllViews();//去除城市容器所有子控件
            this.listener = listener;//设置监听器
            //如果显示项数不确定，则重置显示项数
            if (maxNum == -1) {
                maxNum = list.size();
            }

            /**
             * 循环，终止条件：循环册数小于最大个数和设置的最大显示个数
             * 循环一次，个数+3（每一行显示个数）
             */
            for (int i = 0; i < list.size() && i < maxNum; i += COLUMN_COUNT) {
                int pos;//保存下一个位置，如果当前位置+3的位置有元素，则var8=i+3，否则当前位置+3无元素，则var8的位置保存为最后一个元素的后面位置
                if (i + COLUMN_COUNT > list.size()) {
                    pos = list.size();
                } else {
                    pos = i + COLUMN_COUNT;
                }

                List var9 = list.subList(i, pos); //0-3,则取0,1,2的元素
                categoryContainer.addView(this.createRecommendRow(var9));
            }
        } else {
            layout.setVisibility(View.GONE);
        }

    }
}
