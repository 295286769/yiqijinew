package com.yiqiji.money.modules.common.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.AccountItemView;

/**
 * Created by huangweishui on 2016/4/28.
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * 用来保存条目视图里面所有的控件
     */
    private SparseArray<View> mViews = null;

    /**
     * 构造函数
     *
     * @param itemView
     */
    public CommonRecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();

    }

    /**
     * 根据控件id获取控件对象
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getView(int viewId) {

        // 从集合中根据这个id获取view视图对象
        View view = mViews.get(viewId);

        // 如果为空，说明是第一次获取，里面没有，那就在布局文件中找到这个控件，并且存进集合中
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        // 返回控件对象
        return (T) view;
    }

    /**
     * 为TextView设置文本,按钮也可以用这个方法,button是textView的子类
     *
     * @param textViewId
     * @param content
     */
    public void setText(int textViewId, String content) {
        ((TextView) getView(textViewId)).setText(content);
    }

    public void setViewText(int textViewId, String content) {
        if (getView(textViewId) instanceof TextView) {
            ((TextView) getView(textViewId)).setText(content);
        } else if (getView(textViewId) instanceof Button) {
            ((Button) getView(textViewId)).setText(content);
        }

    }

    public void setTextVisible(int textViewId) {
        ((TextView) getView(textViewId)).setVisibility(View.VISIBLE);
    }

    public void setViewVisibleOrGone(int textViewId, int isvisible) {
        ((View) getView(textViewId)).setVisibility(isvisible);
    }

    public void setTextGone(int textViewId) {
        ((TextView) getView(textViewId)).setVisibility(View.GONE);
    }

    public void setTextGoneImage(int textViewId) {
        ((ImageView) getView(textViewId)).setVisibility(View.GONE);
    }

    public void setTextVisibleImage(int textViewId) {
        ((ImageView) getView(textViewId)).setVisibility(View.VISIBLE);
    }

    public void setTextInvisible(int textViewId) {
        ((TextView) getView(textViewId)).setVisibility(View.INVISIBLE);
    }

    /**
     * 为TextView设置文本,设置颜色
     *
     * @param textViewId
     * @param
     */
    public void setTextColor(int textViewId, int color) {
        ((TextView) getView(textViewId)).setTextColor(color);
    }

    /**
     * 为TextView设置文本,设置字体大小
     *
     * @param textViewId
     * @param
     */
    public void setTextSize(int textViewId, float size) {
        ((TextView) getView(textViewId)).setTextSize(size);
    }

    /**
     * 为TextView设置文本,按钮也可以用这个方法,button是textView的子类
     *
     * @param textViewId
     * @param content
     */
    public void setTextSign(int textViewId, String deawbleId, String title, String content, String name, String time,
                            String isClearText, String money, String type, String billtype) {

        ((AccountItemView) getView(textViewId)).setContent(deawbleId, title, content, name, time, isClearText, money,
                type, billtype, true);

    }

    public void setTextSign(int textViewId, String deawbleId, String title, String content, String name, String time,
                            String isClearText, String money, String type, String billtype, boolean isDraLine) {

        ((AccountItemView) getView(textViewId)).setContent(deawbleId, title, content, name, time, isClearText, money,
                type, billtype, isDraLine);

    }

    /**
     * 为ImageView设置图片
     *
     * @param iv
     * @param imageId
     */
    public void setImage(ImageView iv, int imageId) {
        iv.setImageResource(imageId);
    }

    /**
     * 为ImageView设置图片
     *
     * @param imgId
     * @param imageId
     */
    public void setImage(int imgId, int imageId) {
        ((ImageView) getView(imgId)).setImageResource(imageId);
    }

    /**
     * 为ImageView设置图片
     *
     * @param imgId
     */
    public void setImage(int imgId, Bitmap bitmap) {
        ((ImageView) getView(imgId)).setImageBitmap(bitmap);
    }

    /**
     * 为ImageView设置图片
     *
     * @param imgId
     * @param url
     */
    public void setImage(int imgId, String url, int drable) {
        XzbUtils.displayImageHead((ImageView) getView(imgId), url, drable);
    }

    /**
     * chechkbox设置选中监听
     *
     * @param Id
     * @param changeListener
     */
    public void setCheckBoxOnCheck(int Id, OnCheckedChangeListener changeListener) {
        ((CheckBox) getView(Id)).setOnCheckedChangeListener(changeListener);
    }


    // public void setimage(Context context,int viewid, String url) {
    // ImageView imageView = (ImageView) array.get(viewid);
    // Picasso.with(context).load(url).into(imageView);
    // }
    //
    public void setOnClick(int viewid, OnClickListener clickListener, int position) {
        View view = getView(viewid);
        view.setTag(position);
        view.setOnClickListener(clickListener);
    }

}
