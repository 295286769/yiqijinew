package com.yiqiji.money.modules.book.detailinfo.vd_driver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillItemModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookInfoListMultipItem;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.SelectableRoundedImageView;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leichi on 2017/6/12.
 */

public class ItemBookDiaryViewHolder extends BaseViewHolder {

    Context mContext;
    @BindView(R.id.tv_billDiaryText)
    TextView tvBillDiaryText;
    @BindView(R.id.tv_billDiaryCreater)
    TextView tvBillDiaryCreater;
    @BindView(R.id.location_image)
    ImageView locationImage;
    @BindView(R.id.location_text)
    TextView locationText;
    @BindView(R.id.stage)
    TextView stage;
    @BindView(R.id.bill_image)
    SelectableRoundedImageView billImage;
    @BindView(R.id.rela_billImage)
    RelativeLayout relaBillImage;

    BookBillItemModel bookBillItemModel;

    public ItemBookDiaryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        initEvent();
    }


    public void bindViewData(BookInfoListMultipItem item) {

        bookBillItemModel = (BookBillItemModel) item.getData();
        tvBillDiaryText.setText(bookBillItemModel.billmark);
        tvBillDiaryCreater.setText(bookBillItemModel.username);

        boolean hasLocation = bookBillItemModel.address != null && bookBillItemModel.address.length() > 0;
        locationImage.setVisibility(hasLocation ? View.VISIBLE : View.GONE);
        locationText.setVisibility(hasLocation ? View.VISIBLE : View.GONE);
        if (hasLocation) {
            locationText.setText(bookBillItemModel.address);
        }

        if (bookBillItemModel.billimg == null || bookBillItemModel.billimg.length() == 0) {
            relaBillImage.setVisibility(View.GONE);
        } else {
            relaBillImage.setVisibility(View.VISIBLE);
            XzbUtils.displayImage(billImage, bookBillItemModel.billimg, 0);
        }
        String billcatename = bookBillItemModel.billcatename;
        if (!TextUtils.isEmpty(billcatename)) {
            stage.setVisibility(View.VISIBLE);
            stage.setText(billcatename);
        }
    }

    private void initEvent() {
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
