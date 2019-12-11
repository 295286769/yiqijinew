package com.yiqiji.money.modules.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.Found.entity.FaceEntity;
import com.yiqiji.money.modules.common.utils.GifOpenHelper;

import java.util.List;

/**
 * TODO(表情适配器)
 *
 * @author shaoxs
 * @version V1.0
 * @Date 2014-2-26 下午3:00:39
 */
public class FaceAdapter extends BaseAdapter {

    private List<FaceEntity> data;

    private LayoutInflater inflater;

    private int size = 0;
    GifOpenHelper helper;
    Context context;

    public FaceAdapter(Context context, List<FaceEntity> list) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.size = list.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FaceEntity emoji = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_face, null);
            viewHolder.iv_face = (ImageView) convertView
                    .findViewById(R.id.item_iv_face);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (emoji.getId() == R.drawable.face_selector_del_icon) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageResource(emoji.getId());
        } else if (TextUtils.isEmpty(emoji.getCharacter())) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageDrawable(null);
        } else {
            helper = new GifOpenHelper();
            viewHolder.iv_face.setTag(emoji);
            helper.read(context.getResources().openRawResource(emoji.getId()));
            Bitmap bitmap = helper.getImage();
            viewHolder.iv_face.setImageBitmap(bitmap);
        }

        return convertView;
    }

    class ViewHolder {
        public ImageView iv_face;
    }

}
