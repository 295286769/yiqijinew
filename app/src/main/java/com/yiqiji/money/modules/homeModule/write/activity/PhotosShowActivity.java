package com.yiqiji.money.modules.homeModule.write.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.view.MyTitleLayout;
import com.yiqiji.money.modules.common.view.TouchImageView;


/**
 * Created by Administrator on 2017/3/20.
 *
 * 查看大图
 */
public class PhotosShowActivity extends BaseActivity {
    private TouchImageView view_touchimageview;
    private String path;
    private boolean isNeedDelet = false;//是否需要删除按钮

    private MyTitleLayout my_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_show);

        view_touchimageview = (TouchImageView) findViewById(R.id.view_touchimageview);
        view_touchimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        my_title = (MyTitleLayout) findViewById(R.id.my_title);
        isNeedDelet = getIntent().getBooleanExtra("isNeedDelet", false);
        if (!isNeedDelet) {
            my_title.showRightImgBtn(R.drawable.delet_picture_detail);
        }


        my_title.setListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.layout_title_view_right_img_btn) {
                    Intent intent = new Intent(PhotosShowActivity.this, DetailsActivity.class);
                    intent.putExtra("path", "");
                    setResult(RESULT_OK, intent);
                    finish();
                }
                if (v.getId() == R.id.layout_title_view_return) {
                    finish();
                }
            }
        });


        my_title.setBackgroudColor(getResources().getColor(R.color.black), getResources().getColor(R.color.black));
        path = getIntent().getStringExtra("path");
        if (!path.contains("http")) {
            if (!path.contains("file://")) {
                path = "file://" + path;
            }
        }else {
            path+="?imageMogr2/auto-orient";
        }

        XzbUtils.displayImage(view_touchimageview, path, 0);
    }
}
