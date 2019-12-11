package com.yiqiji.money.modules.book.detailinfo.vd_driver;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillItemModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookInfoListMultipItem;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/12.
 */

public class ItemBookBillViewHolder extends BaseViewHolder {

    @BindView(R.id.bill_categoryImage)
    ImageView billCategoryImage;
    @BindView(R.id.tv_bill_category)
    TextView tvBillCategory;
    @BindView(R.id.blance)
    TextView blance;
    @BindView(R.id.tv_bill_remark)
    TextView tvBillRemark;
    @BindView(R.id.relayout_item)
    RelativeLayout relayoutItem;
    @BindView(R.id.location_image)
    ImageView locationImage;
    @BindView(R.id.location_text)
    TextView locationText;
    @BindView(R.id.tv_partInCount)
    TextView tvPartInCount;
    @BindView(R.id.bill_image)
    SelectableRoundedImageView billImage;
    @BindView(R.id.rela_billImage)
    RelativeLayout relaBillImage;

    Context mContext;
    BookBillItemModel bookBillItemModel;

    public ItemBookBillViewHolder(View view) {
        super(view);
        ButterKnife.bind(this,view);
        mContext=view.getContext();
        initEvent();
    }


    public void bindViewData(BookInfoListMultipItem item) {

        bookBillItemModel = (BookBillItemModel) item.getData();

        if(bookBillItemModel.billtype.equals("4")){
            //成员交款
            initSettlementView(bookBillItemModel);
        }else {
            //支出收入
            initExpendAndIncomeView(bookBillItemModel);
        }

        //备注
        if (bookBillItemModel.billmark == null || bookBillItemModel.billmark.length() == 0) {
            tvBillRemark.setVisibility(View.GONE);
        } else {
            tvBillRemark.setText(bookBillItemModel.billmark);
            tvBillRemark.setVisibility(View.VISIBLE);
        }
        //位置
        boolean hasLocation=bookBillItemModel.address!=null&&bookBillItemModel.address.length()>0;
        locationImage.setVisibility(hasLocation?View.VISIBLE:View.GONE);
        locationText.setVisibility(hasLocation?View.VISIBLE:View.GONE);
        if(hasLocation){
            locationText.setText(bookBillItemModel.address);
        }
        //图片
        if(bookBillItemModel.billimg==null||bookBillItemModel.billimg.length()==0){
            relaBillImage.setVisibility(View.GONE);
        }else{
            relaBillImage.setVisibility(View.VISIBLE);
            XzbUtils.displayImage(billImage,bookBillItemModel.billimg,0);
        }
    }

    //成员交款
    private void initSettlementView(BookBillItemModel bookBillItemModel){
        billCategoryImage.setImageResource(R.drawable.group_settlement);
        tvBillCategory.setText("成员交款");
        blance.setText(bookBillItemModel.billamount);
        tvPartInCount.setText(bookBillItemModel.billcount + "人交款");
        blance.setTextColor(mContext.getResources().getColor(R.color.new_text_color_1));
    }

    //支出和收入
    private void initExpendAndIncomeView(BookBillItemModel bookBillItemModel){
        if(StringUtils.isEmpty(bookBillItemModel.billsubcateicon)){
            XzbUtils.displayImage(billCategoryImage, bookBillItemModel.getBillcateIconUrl(), R.drawable.gray_ordinary);
        }else {
            XzbUtils.displayImage(billCategoryImage, bookBillItemModel.getBillsubcateIconUrl(), R.drawable.gray_ordinary);
        }
        if(StringUtils.isEmpty(bookBillItemModel.billsubcatename)){
            tvBillCategory.setText(bookBillItemModel.billcatename);
        }else {
            tvBillCategory.setText(bookBillItemModel.billsubcatename);
        }

        if (bookBillItemModel.billtype.equals("0")) {
            //收入
            blance.setText(bookBillItemModel.billamount);
            blance.setTextColor(mContext.getResources().getColor(R.color.green_conner_button));
        } else if(bookBillItemModel.billtype.equals("1")){
            //支出
            blance.setText("-" + bookBillItemModel.billamount);
            blance.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        if(bookBillItemModel.bookIsAA){
            //AA账本显示消费人数
            if(bookBillItemModel.billcount==null||bookBillItemModel.billcount.length()==0){
                tvPartInCount.setVisibility(View.GONE);
            }else {
                tvPartInCount.setVisibility(View.VISIBLE);
                tvPartInCount.setText(bookBillItemModel.billcount+"人消费");
            }
        }else {
            //非AA账本显示操作账单操作人
            tvPartInCount.setVisibility(View.VISIBLE);
            tvPartInCount.setText(bookBillItemModel.username);
        }
    }


    private void initEvent(){
        relaBillImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//账单详情查看大图
                Intent intent = new Intent(v.getContext(), PhotosShowActivity.class);
                intent.putExtra("path", bookBillItemModel.billimg);
                intent.putExtra("isNeedDelet", true);
                v.getContext().startActivity(intent);
            }
        });
    }
}
