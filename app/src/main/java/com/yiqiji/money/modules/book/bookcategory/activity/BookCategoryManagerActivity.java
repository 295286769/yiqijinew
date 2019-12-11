package com.yiqiji.money.modules.book.bookcategory.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataController;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataLocalPersistencer;
import com.yiqiji.money.modules.book.bookcategory.fragment.ExpendCategoryListFragment;
import com.yiqiji.money.modules.book.bookcategory.fragment.IncomeCategoryListFragment;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryModel;
import com.yiqiji.money.modules.book.bookcategory.model.BookCategoryOperationType;
import com.yiqiji.money.modules.book.bookcategory.model.SyncCategoryResult;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.common.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by leichi on 2017/5/17.
 */

public class BookCategoryManagerActivity extends BaseActivity {

    @BindView(R.id.iv_details_return)
    ImageView ivDetailsReturn;
    @BindView(R.id.rb_details_expenditure)
    RadioButton rbDetailsExpenditure;
    @BindView(R.id.accounts)
    RadioButton accounts;
    @BindView(R.id.radio_group_details)
    RadioGroup radioGroupDetails;
    @BindView(R.id.ll_radiogroup)
    RelativeLayout llRadiogroup;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.liear_add_group_category)
    LinearLayout liearAddGroupCategory;
    @BindView(R.id.tv_sort)
    TextView tvSort;

    int currentBillType;                                                              //当先显示的账本类型 1：支出；0：收入
    String accountBookId;                                                             //账本id
    String accountBookName;                                                           //账本名称
    BookCategoryModel mBookCategoryModel;                                             //账本的分类
    ExpendCategoryListFragment expendCategoryListFragment;                            //支出的分类列表
    IncomeCategoryListFragment incomeCategoryListFragment;                            //收入的分类列表
    FragmentManager fragmentManager = getSupportFragmentManager();
    RetryDialogHandler retryDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category_manager_layout);
        ButterKnife.bind(this);
        initData();
        initEvent();
    }

    private void initData() {
        accountBookId = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_ID);
        accountBookName = getIntent().getStringExtra(DataConstant.BUNDLE_KEY_BOOK_NAME);
        currentBillType=getIntent().getIntExtra(DataConstant.BUNDLE_KEY_BOOK_TYPE,0);
        retryDialogHandler=new RetryDialogHandler(this);
        getAccountBookCategoryData();
    }


    private void getAccountBookCategoryData(){
        showLoadingDialog();
        DataController.getAccountBookCategoryByAccountBookId(accountBookId, new ViewCallBack<BookCategoryModel>() {
            @Override
            public void onSuccess(BookCategoryModel bookCategoryModel) throws Exception {
                super.onSuccess(bookCategoryModel);
                mBookCategoryModel=bookCategoryModel;
                initViewData(mBookCategoryModel);
                sycAllcategory();
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                dismissDialog();
                retryDialogHandler.showRetryDialog(simleMsg.getErrMsg());
            }
        });
    }

    private void initEvent() {
        radioGroupDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_details_expenditure:
                        showExpendCategoryFragment();      //支出
                        currentBillType=DataConstant.EXPEND_BILL_TYPE;
                        break;
                    case R.id.accounts:
                        showIncomeCategoryFragment();      //交款
                        currentBillType=DataConstant.INCOME_BILL_TYPE;
                        break;
                }
            }
        });

        liearAddGroupCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加大类
                BookCategoryEditActivity.openAddGroup(mContext,getCurrentBookCategoryModel());
            }
        });

        ivDetailsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //排序
                BookCategoryOrderActivity.open(BookCategoryManagerActivity.this,accountBookId,currentBillType);
            }
        });

        EventBus.getDefault().register(this);

        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountBookCategoryData();
            }
        });

        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private BookCategory getCurrentBookCategoryModel(){
        if(currentBillType==DataConstant.EXPEND_BILL_TYPE){
            BookCategory bookCategory=mBookCategoryModel.expendCategory.child.get(0);
            bookCategory.billtype= mBookCategoryModel.expendCategory.getBilltype();
            return bookCategory;
        }else {
            BookCategory bookCategory=mBookCategoryModel.incomeCategory.child.get(0);
            bookCategory.billtype= mBookCategoryModel.incomeCategory.getBilltype();
            return bookCategory;
        }
    }

    private void initViewData(BookCategoryModel bookCategoryModel){
        if (bookCategoryModel.expendCategory != null && bookCategoryModel.incomeCategory != null&&bookCategoryModel.incomeCategory.child!=null) {
            radioGroupDetails.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            rbDetailsExpenditure.setText(bookCategoryModel.expendCategory.categorytitle);
            accounts.setText(bookCategoryModel.incomeCategory.categorytitle);
        } else {
            radioGroupDetails.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
        }

        if(currentBillType==DataConstant.EXPEND_BILL_TYPE){
            if (bookCategoryModel.expendCategory != null) {
                showExpendCategoryFragment();
                radioGroupDetails.check(R.id.rb_details_expenditure);
            }
        }else{
            if (bookCategoryModel.incomeCategory != null) {
                showIncomeCategoryFragment();
                radioGroupDetails.check(R.id.accounts);
            }
        }
        tvTitle.setText(accountBookName);
    }


    /**
     * 显示支出分类列表
     */
    private void showExpendCategoryFragment() {
        if (expendCategoryListFragment == null) {
            expendCategoryListFragment = ExpendCategoryListFragment.newInstance(accountBookId);
            addCommitFragment(expendCategoryListFragment);
        }
        showCommitFragment(expendCategoryListFragment);
        hideCommitFragment(incomeCategoryListFragment);
    }

    /**
     * 显示收入分类列表
     */
    private void showIncomeCategoryFragment() {
        if (incomeCategoryListFragment == null) {
            incomeCategoryListFragment = IncomeCategoryListFragment.newInstance(accountBookId);
            addCommitFragment(incomeCategoryListFragment);
        }
        showCommitFragment(incomeCategoryListFragment);
        hideCommitFragment(expendCategoryListFragment);
    }

    private void addCommitFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_content, fragment);
        fragmentTransaction.commit();
    }

    private void showCommitFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    private void hideCommitFragment(Fragment fragment) {
        if(fragment==null){
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 外部打开Activity方法
     *
     * @param context
     * @param accountBookId
     */
    public static void open(Context context, String accountBookId, String accountBookName,int mType) {
        Intent intent = new Intent();
        intent.setClass(context, BookCategoryManagerActivity.class);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_NAME, accountBookName);
        intent.putExtra(DataConstant.BUNDLE_KEY_BOOK_TYPE, mType);
        ((Activity)context).startActivityForResult(intent,DataConstant.REQUEST_CODE_CATEGORY_MANAGER);
    }

    @Override
    public void finish() {
        saveSortData();
        DataLocalPersistencer.updateBookCategoryJSONObject(accountBookId);

        Intent in=new Intent();
        in.putExtra(DataConstant.BUNDLE_KEY_BOOK_OPERATION_TYPE,mOperationType.getOperationType());
        setResult(Activity.RESULT_OK,in);

        super.finish();
    }

    private void saveSortData(){
        if(expendCategoryListFragment!=null){
            expendCategoryListFragment.saveSortData();
        }
        if(incomeCategoryListFragment!=null){
            incomeCategoryListFragment.saveSortData();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    BookCategoryOperationType mOperationType=new BookCategoryOperationType();
    public void onEventMainThread(BookCategoryOperationType operationType) {
        mOperationType=operationType;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==DataConstant.REQUEST_CODE_CATEGORY_EDIT){


        }
        if(requestCode==DataConstant.REQUEST_CODE_CATEGORY_ORDER){
            if(currentBillType==DataConstant.EXPEND_BILL_TYPE){
                expendCategoryListFragment.refreshData();
            }else {
                incomeCategoryListFragment.refreshData();
            }
        }
    }

    public void sycAllcategory(){
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
