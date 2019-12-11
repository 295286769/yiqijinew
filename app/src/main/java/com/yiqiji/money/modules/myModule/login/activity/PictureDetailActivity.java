package com.yiqiji.money.modules.myModule.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.bitmap_util.LGImgCompressor;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.entity.ExtraKey;
import com.yiqiji.money.modules.common.entity.LocalImageHelper;
import com.yiqiji.money.modules.common.entity.LocalImageHelper.LocalFile;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.AlbumViewPager;

import java.util.List;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

public class PictureDetailActivity extends BaseActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    GridView gridView;
    TextView title;// 标题
    View titleBar;// 标题栏
    View pagerContainer;// 图片显示部分
    TextView finish, headerFinish;
    AlbumViewPager viewpager;// 大图显示pager
    String folder;
    TextView mCountView;
    List<LocalFile> currentFolder = null;

    ImageView mBackView;
    View headerBar;
    CheckBox checkBox;
    LocalImageHelper helper = LocalImageHelper.getInstance();

    List<LocalFile> checkedItems;

    //是否需要裁剪，设置头像需要，记账跳转不需要
    private boolean isShowCut = true;
    private String intentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_album_detail);
        helper.setmContext(this);
        if (!LocalImageHelper.getInstance().isInited()) {
            finish();
            return;
        }
        isShowCut = getIntent().getBooleanExtra("isShowCut", true);
        intentClass = getIntent().getStringExtra("intentClass");
        initView();
    }

    private void initView() {
        initTitle("照片详情");

        title = (TextView) findViewById(R.id.album_title);
        finish = (TextView) findViewById(R.id.album_finish);
        headerFinish = (TextView) findViewById(R.id.header_finish);
        gridView = (GridView) findViewById(R.id.gridview);
        titleBar = findViewById(R.id.album_title_bar);
        viewpager = (AlbumViewPager) findViewById(R.id.albumviewpager);
        pagerContainer = findViewById(R.id.pagerview);
        mCountView = (TextView) findViewById(R.id.header_bar_photo_count);
        viewpager.setOnPageChangeListener(pageChangeListener);
        mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
        headerBar = findViewById(R.id.album_item_header_bar);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
        mBackView.setOnClickListener(this);
        finish.setOnClickListener(this);
        headerFinish.setOnClickListener(this);
        findViewById(R.id.album_back).setOnClickListener(this);

        folder = getIntent().getExtras().getString(ExtraKey.LOCAL_FOLDER_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 防止停留在本界面时切换到桌面，导致应用被回收，图片数组被清空，在此处做一个初始化处理
//                helper.initImage();
                // 获取该文件夹下地所有文件
                final List<LocalFile> folders = helper
                        .getFolder(folder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (folders != null) {
                            currentFolder = folders;
                            MyAdapter adapter = new MyAdapter(
                                    PictureDetailActivity.this, folders);
                            title.setText(folder);
                            gridView.setAdapter(adapter);
                            // 设置当前选中数量
                            if (checkedItems.size()
                                    + LocalImageHelper.getInstance()
                                    .getCurrentSize() > 0) {
                                finish.setText("完成("
                                        + (checkedItems.size() + LocalImageHelper
                                        .getInstance().getCurrentSize())
                                        + "/9)");
                                finish.setEnabled(true);
                                headerFinish.setText("完成("
                                        + (checkedItems.size() + LocalImageHelper
                                        .getInstance().getCurrentSize())
                                        + "/9)");
                                headerFinish.setEnabled(true);
                            } else {
                                finish.setText("完成");
                                // finish.setEnabled(false);
                                headerFinish.setText("完成");
                                // headerFinish.setEnabled(false);
                            }
                        }
                    }
                });
            }
        }).start();
        checkedItems = helper.getCheckedItems();
        LocalImageHelper.getInstance().setResultOk(false);
    }

    private void showViewPager(int index) {
        pagerContainer.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
        findViewById(R.id.album_title_bar).setVisibility(View.GONE);
        mCountView.setText((index + 1) + "/" + currentFolder.size());
        // 第一次载入第一张图时，需要手动修改
        if (index == 0) {
            checkBox.setTag(currentFolder.get(index));
            checkBox.setChecked(checkedItems.contains(currentFolder.get(index)));
        }
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1,
                (float) 0.9, 1, pagerContainer.getWidth() / 2,
                pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(300);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
    }

    private void hideViewPager() {
        pagerContainer.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        findViewById(R.id.album_title_bar).setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1,
                (float) 0.9, pagerContainer.getWidth() / 2,
                pagerContainer.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pagerContainer.startAnimation(set);
        ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (viewpager.getAdapter() != null) {
                String text = (position + 1) + "/"
                        + viewpager.getAdapter().getCount();
                mCountView.setText(text);
                checkBox.setTag(currentFolder.get(position));
                checkBox.setChecked(checkedItems.contains(currentFolder
                        .get(position)));
            } else {
                mCountView.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_bar_photo_back:
                hideViewPager();
                break;
            case R.id.album_finish:
            case R.id.header_finish:
                finish();
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
                break;
            case R.id.album_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pagerContainer.getVisibility() == View.VISIBLE) {
            hideViewPager();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            if (checkedItems.contains(compoundButton.getTag())) {
                checkedItems.remove(compoundButton.getTag());
            }
        } else {
            if (!checkedItems.contains(compoundButton.getTag())) {
                if (checkedItems.size()
                        + LocalImageHelper.getInstance().getCurrentSize() >= 9) {
                    Toast.makeText(this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalFile) compoundButton
                        .getTag());
            }
        }
        if (checkedItems.size()
                + LocalImageHelper.getInstance().getCurrentSize() > 0) {
            finish.setText("完成("
                    + (checkedItems.size() + LocalImageHelper.getInstance()
                    .getCurrentSize()) + "/9)");
            finish.setEnabled(true);
            headerFinish.setText("完成("
                    + (checkedItems.size() + LocalImageHelper.getInstance()
                    .getCurrentSize()) + "/9)");
            headerFinish.setEnabled(true);
        } else {
            finish.setText("完成");
            finish.setEnabled(false);
            headerFinish.setText("完成");
            headerFinish.setEnabled(false);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private Context m_context;
        private LayoutInflater miInflater;
        DisplayImageOptions options;
        List<LocalFile> paths;

        public MyAdapter(Context context, List<LocalFile> paths) {
            m_context = context;
            this.paths = paths;
            options = new DisplayImageOptions.Builder().cacheInMemory(false)
                    .cacheOnDisk(false)
                    .showImageForEmptyUri(R.drawable.write_photograph)
                    .showImageOnFail(R.drawable.write_photograph)
                    .showImageOnLoading(R.drawable.write_photograph)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new SimpleBitmapDisplayer()).build();
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public LocalFile getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.simple_list_item, null);
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.imageView);
                viewHolder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.checkbox);
                viewHolder.checkBox
                        .setOnCheckedChangeListener(PictureDetailActivity.this);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.imageView;
            LocalFile localFile = paths.get(i);
            // FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(),
            // imageView, options);

//            ImageLoader.getInstance().displayImage(localFile.getOriginalUri(),
//                    new ImageViewAware(viewHolder.imageView), options,
//                    loadingListener, null);
            ImageLoaderManager.loadImage(PictureDetailActivity.this, localFile.getOriginalUri(), R.drawable.write_photograph, R.drawable.write_photograph, viewHolder.imageView);
            viewHolder.checkBox.setTag(localFile);
            viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!XzbUtils.isFastClick()) {
                        return;
                    }

                    LocalFile localFile = currentFolder.get(i);
                    String path = localFile.getOriginalUri();
                    uri = Uri.parse(path);
                    String pathString = "";

                    if (!isShowCut) {

                        toCompressor(uri);
//                        try {
//                            if (uri != null) {
//
//                                Bitmap bitmap = BitmapUtil.getBitmapFormUri(PictureDetailActivity.this, uri, UIHelper.Dp2Px(PictureDetailActivity.this, 800), UIHelper.Dp2Px(PictureDetailActivity.this, 200));
//                                pathString = StringUtils.getRealFilePath(PictureDetailActivity.this, XzbUtils.getPath());
//                                BitmapUtil.saveBitmapToFile(bitmap, pathString);
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            IntentUtils.setReflectionjump(PictureDetailActivity.this, intentClass, "path", pathString);
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }

                    } else {
                        MyApplicaction.getInstence().isThubm = true;
                        initDUri();
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }


    private Uri uri;
    private Uri path;


    private void toCompressor(Uri uri) {
        LGImgCompressor.getInstance(this).withListener(new LGImgCompressor.CompressListener() {
            @Override
            public void onCompressStart() {

            }

            @Override
            public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
                String path = imageOutPath.getOutPath();
                if (StringUtils.isEmpty(path)) {
                    return;
                }
                try {
                    IntentUtils.setReflectionjump(PictureDetailActivity.this, intentClass, "path", path);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).starCompress(uri.toString(), 480, 800, 200);
    }

    /**
     * 调用裁剪
     */
    private void initDUri() {
        if (MyApplicaction.getInstence().isThubm) {
            path = XzbUtils.getPath();
            XzbUtils.doCropPhoto(this, uri, path, RequsterTag.RQ_CROPE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case RequsterTag.RQ_CROPE:

                if (path != null) {
                    String pathStrng = StringUtils.getRealFilePath(this, path);
                    try {
                        IntentUtils.setReflectionjump(PictureDetailActivity.this, intentClass, "path", pathStrng);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    SimpleImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      final Bitmap bm) {
            if (TextUtils.isEmpty(imageUri)) {
                return;
            }
            // 由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
            try {
                ((ImageView) view).getDrawable().setColorFilter(
                        Color.argb(0xff, 0xee, 0xee, 0xee),
                        PorterDuff.Mode.MULTIPLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }
}
