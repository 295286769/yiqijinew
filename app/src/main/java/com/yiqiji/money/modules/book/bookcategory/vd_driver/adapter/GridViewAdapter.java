package com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.model.IconModel;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/5/26.
 */

public class GridViewAdapter extends BaseAdapter{

    List<IconModel> itemModelList;
    Context context;
    public GridViewAdapter(Context context,List<IconModel> itemModelList){
        this.itemModelList=itemModelList;
        this.context=context;
        EventBus.getDefault().register(this);
    }

    @Override
    public int getCount() {
        return itemModelList.size();
    }

    @Override
    public IconModel getItem(int position) {
        return itemModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    IconModel choiceIcon;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.item_gridview_layout,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.icon_img);
        ImageView imageBg=(ImageView)view.findViewById(R.id.icon_img_bg);
        final IconModel itemModel=getItem(position);
        imageView.setImageResource(itemModel.resId);

        if(itemModel.billtype== DataConstant.EXPEND_BILL_TYPE){
            imageBg.setBackgroundResource(R.drawable.write_default_expenditure);
        }else {
            imageBg.setBackgroundResource(R.drawable.write_default_income);
        }

        imageBg.setVisibility(itemModel.isChoice?View.VISIBLE:View.GONE);

        if(itemModel.isChoice){
            choiceIcon=itemModel;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!itemModel.isChoice){
                    if(choiceIcon!=null){
                        choiceIcon.isChoice=false;
                    }
                    itemModel.isChoice=true;
                    choiceIcon=itemModel;
                    EventBus.getDefault().post(itemModel);
                }else {

                }
                notifyDataSetChanged();
            }
        });
        return view;
    }


    public void onEventMainThread(IconModel itemModel) {
        if(itemModel!=null&&choiceIcon!=null){
            if(itemModel.index!=choiceIcon.index){
                choiceIcon.isChoice=false;
                choiceIcon=null;
                notifyDataSetChanged();
            }
        }
    }
}

