package com.yiqiji.money.modules.book.bookcategory.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataController;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryOperationType;
import com.yiqiji.money.modules.book.bookcategory.model.IBaseBookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.IconModel;
import com.yiqiji.money.modules.book.bookcategory.model.OperaBookCategoryResult;
import com.yiqiji.money.modules.book.bookcategory.model.SyncCategoryResult;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.book.bookcategory.vd_driver.adapter.BookCategoryViewPagerAdapter;
import com.yiqiji.money.modules.common.activity.BaseActivity;
import com.yiqiji.money.modules.common.utils.MaxLengthWatcher;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.widget.myviewpager.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * 分类的编辑和添加
 * Created by leichi on 2017/5/17.
 */

public class BookCategoryEditActivity extends BaseActivity {

    public static final int ICON_COUNT = 57;         //图标总数
    public static final int PAGER_COUNT = 20;        //一页显示的图标数

    public static final int GROUP = 1;               //添加/编辑 一级目录
    public static final int CHILD = 2;               //添加/编辑 二级目录

    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_superclass_name)
    TextView tvSuperclassName;
    @BindView(R.id.ll_superclass)
    LinearLayout llSuperclass;
    @BindView(R.id.tv_child)
    TextView tvChild;
    @BindView(R.id.edit_categoryName)
    EditText editCategoryName;
    @BindView(R.id.image_choiced)
    ImageView imageChoiced;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewPage_Indicator)
    CirclePageIndicator viewPageIndicator;


    int mInstruct;                              //操作指令（1:编辑，5：添加）
    int operaHierarchy;                         //操作的层级（1：group,2:child）
    IBaseBookCategory baseBookCategory;         //需要提交更新的基础数据对象。
    IconModel choiceIcon = null;
    RetryDialogHandler retryDialogHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category_edit_layout);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }


    private void initData() {
        Intent intent = getIntent();
        mInstruct = intent.getIntExtra(DataConstant.BUNDLE_KEY_BOOK_OPERATION_TYPE, 0);
        operaHierarchy = intent.getIntExtra(DataConstant.BUNDLE_KEY_BOOK_OPERATION_HIERARCHY, 0);
        baseBookCategory = (IBaseBookCategory) intent.getSerializableExtra(DataConstant.BUNDLE_KEY_BOOK_CAREGORY);
        if (mInstruct == 0 || operaHierarchy == 0 || baseBookCategory == null) {
            finish();
        }
        initViewData();
        new MaxLengthWatcher(12,editCategoryName);
        retryDialogHandler=new RetryDialogHandler(this);
    }


    private void initViewData() {
        initTitle();
        initAdater(Integer.parseInt(baseBookCategory.getBilltype()));
        sycAllcategory(baseBookCategory.getAccountbookid());
    }

    private void initEvent() {
        EventBus.getDefault().register(this);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });
        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sycAllcategory(baseBookCategory.getAccountbookid());
            }
        });
    }


    private void saveEvent() {

        if (!inputIslegal()) {
            return;
        }
        String categorytitle = editCategoryName.getText().toString();
        if (operaHierarchy == CHILD) {
            categorytitle = baseBookCategory.getParentName() + "－" + categorytitle;
        }
        baseBookCategory.setCategorytitle(categorytitle);
        baseBookCategory.setCategoryicon(choiceIcon.getUrl());
        if (mInstruct == BookCategoryOperationType.TO_EDIT) {
            //编辑
            toEditCategory(baseBookCategory);
        } else {
            //添加
            toAddCategory(baseBookCategory);
        }
    }

    private void initTitle() {
        if (operaHierarchy == CHILD) {
            llSuperclass.setVisibility(View.VISIBLE);
            tvSuperclassName.setText(baseBookCategory.getParentName());
            tvTitle.setText(mInstruct == BookCategoryOperationType.TO_EDIT ? "编辑子类" : "添加子类");
        } else {
            tvChild.setText("大类名称：");
            llSuperclass.setVisibility(View.GONE);
            tvTitle.setText(mInstruct == BookCategoryOperationType.TO_EDIT ? "编辑大类" : "添加大类");
        }

        if (mInstruct == BookCategoryOperationType.TO_EDIT) {
            choiceIcon = new IconModel(this,baseBookCategory.getCategoryicon());
            choiceIcon.billtype=Integer.parseInt(baseBookCategory.getBilltype());
            ImageLoaderManager.loadImage(mContext, baseBookCategory.getIconUrl(), imageChoiced);

            editCategoryName.setText(baseBookCategory.getCategoryName());
            editCategoryName.setSelection(editCategoryName.length());
        }
    }

    /**
     * 根据账本类型加载图标数据适配器
     *
     * @param billtype
     */
    private void initAdater(int billtype) {
        ArrayList<IconModel> iconModelList = getImageResIdList(billtype);
        List<View> viewList = getViewPagerItemList(iconModelList.size());
        List<List<IconModel>> viewDataList = getViewPagerItemDataList(iconModelList);
        BookCategoryViewPagerAdapter adapter = new BookCategoryViewPagerAdapter(this, viewDataList, viewList);
        viewpager.setAdapter(adapter);
        viewPageIndicator.setViewPager(viewpager);
        //设置翻页的指示图标
        viewPageIndicator.setFillColor(R.color.calender_bagroud_color);
    }

    /**
     * 根据账本类型加载图标集合
     *
     * @param billtype
     * @return
     */
    private ArrayList<IconModel> getImageResIdList(int billtype) {
        int startIndex;
        ArrayList<IconModel> iconList = new ArrayList<>();
        if (billtype == DataConstant.EXPEND_BILL_TYPE) {
            //支出账本类型的图标从100开始
            startIndex = 100;
        } else {
            //收入账本类型的图标从0开始
            startIndex = 0;
        }

        for (int n = 0; n <= ICON_COUNT; n++) {
            int index = startIndex + n;
            int imageResId = getImageResId(index);
            if (imageResId == 0) {
                continue;
            }
            IconModel iconModel = new IconModel();
            iconModel.index = index;
            iconModel.resId = imageResId;
            iconModel.billtype = billtype;
            if (choiceIcon != null && choiceIcon.index == index) {
                iconModel.isChoice = true;
            }
            iconList.add(iconModel);
        }
        return iconList;
    }

    private int getImageResId(int index) {
        return getResources().getIdentifier("icon_" + index, "drawable", getPackageName());
    }


    /**
     * 根据图标的数量，初始化viewpager的itemView
     *
     * @param iconCount
     * @return
     */
    private List<View> getViewPagerItemList(int iconCount) {
        List<View> viewList = new ArrayList<>();
        //根据每页显示PAGER_COUNT个去计算页数
        int pageCount = iconCount / PAGER_COUNT + (iconCount % PAGER_COUNT > 0 ? 1 : 0);
        for (int i = 0; i < pageCount; i++) {
            View view = View.inflate(this, R.layout.item_viewpager_item_view, null);
            viewList.add(view);
        }
        return viewList;
    }

    /**
     * 根据图标集合，初始化每个gridView的适配器数据
     *
     * @param iconModelList
     * @return
     */
    private List<List<IconModel>> getViewPagerItemDataList(ArrayList<IconModel> iconModelList) {
        List<List<IconModel>> dataList = new ArrayList<>();
        List<IconModel> tempIconModelList = new ArrayList<>();
        for (int i = 0; i < iconModelList.size(); i++) {
            if (i % PAGER_COUNT == 0) {
                tempIconModelList = new ArrayList<>();
                dataList.add(tempIconModelList);
            }
            IconModel iconModel = iconModelList.get(i);
            tempIconModelList.add(iconModel);
        }
        return dataList;
    }


    private boolean inputIslegal() {
        if (choiceIcon == null) {
            ToastUtils.DiyToast(this,"请选择图标");
            return false;
        }
        if (editCategoryName.getText().length() == 0) {
            ToastUtils.DiyToast(this,"请输入名称");
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 添加父分类
     *
     * @param context
     */
    public static void openAddGroup(Context context, IBaseBookCategory groupCategoryModel) {
        groupCategoryModel.setCategoryicon("");
        groupCategoryModel.setCategorytitle("");
        groupCategoryModel.setCategoryid("");
        groupCategoryModel.setStatus("1");
        open(context, GROUP, BookCategoryOperationType.TO_ADD, groupCategoryModel);
    }

    /**
     * 编辑父分类
     *
     *  @param context
     */
    public static void openEditGroup(Context context, IBaseBookCategory groupCategoryModel) {
        open(context, GROUP, BookCategoryOperationType.TO_EDIT, groupCategoryModel);
    }


    /**
     * 添加一个子分类
     *
     * @param context
     * @param bookChildCategoryModel
     */
    public static void openAddChild(Context context, IBaseBookCategory bookChildCategoryModel) {
        bookChildCategoryModel.setStatus("1");
        open(context, CHILD, BookCategoryOperationType.TO_ADD, bookChildCategoryModel);
    }

    /**
     * 编辑一个子分类
     *
     * @param context
     * @param groupCategoryModel
     */
    public static void openEditChild(Context context, IBaseBookCategory groupCategoryModel) {
        open(context, CHILD, BookCategoryOperationType.TO_EDIT, groupCategoryModel);
    }

    /**
     * 打开页面
     *
     * @param context
     * @param baseBookCategory
     */
    private static void open(Context context, int operaHierarchy, int operationType, IBaseBookCategory baseBookCategory) {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable(DataConstant.BUNDLE_KEY_BOOK_CAREGORY, baseBookCategory);
        bundle.putInt(DataConstant.BUNDLE_KEY_BOOK_OPERATION_TYPE, operationType);
        bundle.putInt(DataConstant.BUNDLE_KEY_BOOK_OPERATION_HIERARCHY, operaHierarchy);

        intent.putExtras(bundle);
        intent.setClass(context, BookCategoryEditActivity.class);

        ((Activity) context).startActivityForResult(intent, DataConstant.REQUEST_CODE_CATEGORY_EDIT);
    }


    private void toEditCategory(IBaseBookCategory baseBookCategory) {
        showLoadingDialog();
        DataController.editCategory(baseBookCategory, new ViewCallBack<SyncCategoryResult>() {
            @Override
            public void onSuccess(SyncCategoryResult syncCategoryResult) throws Exception {
                dismissDialog();
                finishResult(syncCategoryResult);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                ToastUtils.DiyToast(mContext,simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void toAddCategory(final IBaseBookCategory baseBookCategory){
        showLoadingDialog();
        DataController.addCategory(baseBookCategory, new ViewCallBack<SyncCategoryResult>() {
            @Override
            public void onSuccess(SyncCategoryResult syncCategoryResult) throws Exception {
                dismissDialog();
                baseBookCategory.setCategoryid(syncCategoryResult.categoryid);
                if(operaHierarchy==CHILD){
                    DataLocalPersistencer.addSortBookForChildCategory(baseBookCategory);
                }else {
                    DataLocalPersistencer.addSortBookForGroupCategory(baseBookCategory);
                }
                finishResult(syncCategoryResult);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                ToastUtils.DiyToast(mContext,simleMsg.getErrMsg());
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    public void onEventMainThread(IconModel itemModel) {
        if (itemModel.isChoice) {
            choiceIcon = itemModel;
            imageChoiced.setImageResource(itemModel.resId);
        } else {
            choiceIcon = null;
            imageChoiced.setImageResource(R.drawable.transparent_drawable);
        }
    }


    private void finishResult(SyncCategoryResult syncCategoryResult) {

        OperaBookCategoryResult operaBookCategoryResult=new OperaBookCategoryResult();
        baseBookCategory.setCategoryid(syncCategoryResult.categoryid);
        operaBookCategoryResult.operaHierarchy=operaHierarchy;
        operaBookCategoryResult.mInstruct=mInstruct;
        operaBookCategoryResult.baseBookCategory=baseBookCategory;
        EventBus.getDefault().post(operaBookCategoryResult);

        BookCategoryOperationType type=new BookCategoryOperationType();
        type.setOperationType(mInstruct);
        EventBus.getDefault().post(type);

        setResult(Activity.RESULT_OK);
        finish();
    }


    public void sycAllcategory(String accountBookId){
        showLoadingDialog();
        DataController.sycAllCategoryList(accountBookId, new ViewCallBack<SyncCategoryResult>() {
            @Override
            public void onSuccess(SyncCategoryResult syncCategoryResult) throws Exception {
                super.onSuccess(syncCategoryResult);
                dismissDialog();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                retryDialogHandler.showRetryDialog(simleMsg.getErrMsg());
            }
        });
    }

}
