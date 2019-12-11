package com.yiqiji.money.modules.book.bookcategory.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yiqiji.money.R;

import java.util.List;

/**
 * Created by leichi on 2017/5/22.
 */

public class BaseMoreHandleView extends IndicatorPopWindow {
    Context mContext;
    List<MoreItem> moreItemList;

    public static class MoreItem{
        public int instruct;
        public String titleName;
        public MoreItem(int instruct,String titleName){
               this.instruct=instruct;
               this.titleName=titleName;
        }
    }

    public BaseMoreHandleView(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setMoreItemList(List<MoreItem> moreItemList){
         this.moreItemList=moreItemList;
         addChildView();
    }


    private void addChildView() {
        removeAllChildView();
        for (int i = 0; i < moreItemList.size(); i++) {
            View itemView = View.inflate(mContext, R.layout.item_pop_more_handler_textview_layout, null);
            TextView tv = (TextView) itemView.findViewById(R.id.tv_pop);
            View lineView=itemView.findViewById(R.id.view_line);
            tv.setOnClickListener(itemOnclickListener);
            tv.setTag(moreItemList.get(i));
            tv.setText(moreItemList.get(i).titleName);
            addChildView(itemView);
            if(i==moreItemList.size()-1){
                lineView.setVisibility(View.GONE);
            }
        }
    }

    public View.OnClickListener itemOnclickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onItemClickCallBack!=null){
                onItemClickCallBack.onClick((MoreItem)v.getTag());
            }
            dismiss();
        }
    };

    OnItemClickCallBack onItemClickCallBack;
    public interface OnItemClickCallBack{
        void onClick(MoreItem moreItem);
    }

    public void setOnItemClickCallBack(OnItemClickCallBack onItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack;
    }
}
