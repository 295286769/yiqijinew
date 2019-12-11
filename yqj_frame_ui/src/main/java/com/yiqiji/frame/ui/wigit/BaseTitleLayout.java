package com.yiqiji.frame.ui.wigit;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.ui.R;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/19.
 * address huang.weishui@71dai.com
 */
public class BaseTitleLayout extends RelativeLayout {
    private Context mContext;
    private ImageView base_title_return_ui;//返回
    private ImageView second_image_ui;//
    private ImageView right_image_ui;//右边图片
    private TextView title_ui;//标题
    private TextView title_right_text_ui;//右边标题
    private int title_color;
    private float title_size;
    private int right_text_color;
    private boolean right_text_visble;
    private float right_text_size;
    private boolean base_title_return_ui_visible;//是否显示
    private boolean second_image_ui_visible;//是否显示
    private boolean right_image_ui_visible;//是否显示
    private int base_title_return_ui_drawbleid;//返回资源id
    private int second_image_ui_drawbleid;//右边第二图片资源id
    private int right_image_ui_drawbleid;//右边图片资源id

    public BaseTitleLayout(Context context) {
        this(context, null);
    }

    public BaseTitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
        initParamas(attrs);
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.base_title_layout_ui, this, true);
        base_title_return_ui = (ImageView) view.findViewById(R.id.base_title_return_ui);
        second_image_ui = (ImageView) view.findViewById(R.id.second_image_ui);
        right_image_ui = (ImageView) view.findViewById(R.id.right_image_ui);
        title_ui = (TextView) view.findViewById(R.id.title_ui);
        title_right_text_ui = (TextView) view.findViewById(R.id.title_right_text_ui);

    }

    private void initParamas(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.base_title_layout_styeable_ui);
        if (typedArray != null) {
            title_color = typedArray.getColor(R.styleable.base_title_layout_styeable_ui_base_title_color_ui, mContext.getResources().getColor(R.color.context_color_ui));
            right_text_color = typedArray.getColor(R.styleable.base_title_layout_styeable_ui_base_title_right_text_color, mContext.getResources().getColor(R.color.blue_button_login_ui));
            title_size = typedArray.getDimension(R.styleable.base_title_layout_styeable_ui_base_title_size_ui, mContext.getResources().getDimension(R.dimen.contexts_titel_text_ui));
            right_text_size = typedArray.getDimension(R.styleable.base_title_layout_styeable_ui_base_title_right_text_size, mContext.getResources().getDimension(R.dimen.context_text_ui));
            base_title_return_ui_visible = typedArray.getBoolean(R.styleable.base_title_layout_styeable_ui_base_title_left_return_ui_visible, true);
            second_image_ui_visible = typedArray.getBoolean(R.styleable.base_title_layout_styeable_ui_base_title_right_second_image_ui_visible, false);
            right_image_ui_visible = typedArray.getBoolean(R.styleable.base_title_layout_styeable_ui_base_title_right_image_ui_visible, false);
            base_title_return_ui_drawbleid = typedArray.getResourceId(R.styleable.base_title_layout_styeable_ui_base_title_left_return_ui, R.drawable.left_white_arrow_ui);
            second_image_ui_drawbleid = typedArray.getResourceId(R.styleable.base_title_layout_styeable_ui_base_title_right_second_image_ui, -1);
            right_image_ui_drawbleid = typedArray.getResourceId(R.styleable.base_title_layout_styeable_ui_base_title_right_image_ui, -1);
            if (base_title_return_ui_visible) {//返回键是否需要显示
                base_title_return_ui.setVisibility(View.VISIBLE);
            } else {
                base_title_return_ui.setVisibility(View.GONE);
            }
            if (second_image_ui_visible) {//右边第二张图片是否需要显示
                second_image_ui.setVisibility(View.VISIBLE);
            } else {
                second_image_ui.setVisibility(View.GONE);
            }
            if (right_image_ui_visible) {//右边图片是否需要显示
                right_image_ui.setVisibility(View.VISIBLE);
            } else {
                right_image_ui.setVisibility(View.GONE);
            }
            if (base_title_return_ui_drawbleid > 0) {
                ImageLoaderManager.loadImage(mContext, base_title_return_ui_drawbleid, base_title_return_ui);
                setOnClickReturn(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) mContext).finish();
                    }
                });
            }
            if (second_image_ui_drawbleid > 0) {
                ImageLoaderManager.loadImage(mContext, second_image_ui_drawbleid, second_image_ui);
            }
            if (right_image_ui_drawbleid > 0) {
                ImageLoaderManager.loadImage(mContext, right_image_ui_drawbleid, right_image_ui);
            }
            title_ui.setTextColor(title_color);
            title_ui.setTextSize(TypedValue.COMPLEX_UNIT_PX, title_size);
            title_right_text_ui.setTextSize(TypedValue.COMPLEX_UNIT_PX, right_text_size);
            title_right_text_ui.setTextColor(right_text_color);
            typedArray.recycle();
        }
    }

    public void setOnClickRightText(OnClickListener onPress) {
        if (title_right_text_ui != null) {
            title_right_text_ui.setOnClickListener(onPress);
        }
    }

    public void setOnClickReturn(OnClickListener onPress) {
        if (base_title_return_ui != null) {
            base_title_return_ui.setOnClickListener(onPress);
        }
    }

    public void setOnClickRightImage(OnClickListener onPress) {
        if (right_image_ui != null) {
            right_image_ui.setOnClickListener(onPress);
        }
    }

    public void setOnClickRightSecondImage(OnClickListener onPress) {
        if (second_image_ui != null) {
            second_image_ui.setOnClickListener(onPress);
        }
    }

    public void setBaseTitleReturnUi(int drawbleid) {
        if (base_title_return_ui != null) {
            ImageLoaderManager.loadImage(mContext, drawbleid, base_title_return_ui);
        }
    }

    public void setRightImage(int drawbleid) {
        if (right_image_ui != null) {
            ImageLoaderManager.loadImage(mContext, drawbleid, right_image_ui);
        }
    }

    public void setSecondRightImage(int drawbleid) {
        if (second_image_ui != null) {
            ImageLoaderManager.loadImage(mContext, drawbleid, second_image_ui);
        }
    }

    public void setTitle(String title) {
        if (title_ui != null) {
            title_ui.setText(title);
        }
    }

    public void setRightText(String rightText) {
        if (title_right_text_ui != null) {
            title_right_text_ui.setText(rightText);
        }
    }
}
