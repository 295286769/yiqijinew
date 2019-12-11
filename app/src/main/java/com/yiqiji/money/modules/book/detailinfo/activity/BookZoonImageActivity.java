package com.yiqiji.money.modules.book.detailinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.csonezp.imagezoomdemo.HackyViewPager;
import com.example.csonezp.imagezoomdemo.ImageViewPagerAdapter;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/21.
 * address huang.weishui@71dai.com
 */
public class BookZoonImageActivity extends BaseActivity {
    private ImageViewPagerAdapter adapter;
    private HackyViewPager pager;
    private ImageView return_zoon_image;
    private List<String> list;
    private int position;

    public static void openActivity(Context context, List<String> urls, int position) {
        Intent intent = new Intent(context, BookZoonImageActivity.class);
        intent.putStringArrayListExtra("urls", (ArrayList<String>) urls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_zoonimage);
        getIntents();
        initTile();
        initAdapter();
    }

    private void initTile() {
        initTitle("");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntents();
        initAdapter();
    }

    private void getIntents() {
        position = getIntent().getIntExtra("position", 0);
        list = getIntent().getStringArrayListExtra("urls");
    }

    private void initAdapter() {
        pager = (HackyViewPager) findViewById(R.id.hackyViewPager);
        return_zoon_image = (ImageView) findViewById(R.id.return_zoon_image);
        adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
        return_zoon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
