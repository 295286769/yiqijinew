package com.yiqiji.money.modules.book.detailinfo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.bookcategory.data_manager.DataConstant;
import com.yiqiji.money.modules.book.view.RetryDialogHandler;
import com.yiqiji.money.modules.book.detailinfo.activity.BookCommentListActivity;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataController;
import com.yiqiji.money.modules.book.detailinfo.manager.BookDataManager;
import com.yiqiji.money.modules.book.detailinfo.manager.BookInfoListDataAssembler;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillItemModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookBillModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookInfoListMultipItem;
import com.yiqiji.money.modules.book.detailinfo.vd_driver.BookInfoListAdapter;
import com.yiqiji.money.modules.book.detailinfo.view.BadgeView;
import com.yiqiji.money.modules.book.detailinfo.view.BookBillHeadView;
import com.yiqiji.money.modules.book.view.BaseFragment;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.activity.PaymentDetailsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.StatisticsActivity;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalDetailActivity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookInfoDataListFragment extends BaseFragment {

    @BindView(R.id.ryv_book_info)
    RecyclerView ryvBookInfo;
    @BindView(R.id.image_statistical)
    ImageView imageStatistical;
    @BindView(R.id.image_message)
    ImageView imageMessage;
    @BindView(R.id.rela_toComment)
    RelativeLayout relaToComment;
    @BindView(R.id.notes)
    BadgeView notes;

    private BookBillHeadView headView;
    Unbinder unbinder;

    private String accountBoolId;
    private String mMemberid;
    private String mAccountbookcateid;
    private int pagetype = 0;
    private int renovationordairy = 1;//1 账单2 日记
    private BookInfoListAdapter adapter;
    private BookBillModel mBookBillModel;
    private RetryDialogHandler retryDialogHandler;
    private List<BookBillItemModel> bookBillItemModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBoolId = getArguments().getString(DataConstant.BUNDLE_KEY_BOOK_ID);
        mMemberid = getArguments().getString(DataConstant.BUNDLE_KEY_MEMBER_ID);
        mAccountbookcateid = getArguments().getString(DataConstant.BUNDLE_KEY_BOOK_CATE_ID);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_book_info_list_layout, null);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        if (mAccountbookcateid.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
            pagetype = 3;
        }
        getBookBill(headView.getSortType(), pagetype);
    }

    private void getBookBill(int sortType, int pagetype) {
        showLoadingDialog();
        BookDataController.getInstance().getBookBillInfo(accountBoolId, mMemberid, sortType, pagetype, new ViewCallBack<BookBillModel>() {
            @Override
            public void onSuccess(BookBillModel bookBillModel) throws Exception {
                super.onSuccess(bookBillModel);
                dismissDialog();
                mBookBillModel = bookBillModel;

                headView.onSortSuccessCabbBack();
                setViewData(mBookBillModel);
                if (onDataBookInfoRefreshCallBack != null) {
                    onDataBookInfoRefreshCallBack.onSuccess(mBookBillModel);
                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                dismissDialog();
                retryDialogHandler.showRetryDialog();
            }
        });
    }

    private void initEvent() {
        retryDialogHandler.setRetryclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        retryDialogHandler.setExitlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        headView.setOnSwitchSortTypeListener(new BookBillHeadView.OnSwitchSortTypeListener() {
            @Override
            public void onSwitchSortType(int sortType) {
                getBookBill(sortType, pagetype);
            }
        });
        headView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.decoration://装修
                        renovationordairy = 1;
                        getBookBill(headView.getSortType(), pagetype);
                        break;
                    case R.id.diary://日记
                        renovationordairy = 2;
                        getBookBill(headView.getSortType(), pagetype);
                        break;
                }
            }
        });

        imageStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), StatisticsActivity.class);

                long firstTime = StringUtils.isEmpty(mBookBillModel.bookdetail.firsttime) ? System.currentTimeMillis() / 1000 : Long.parseLong(mBookBillModel.bookdetail.firsttime);
                StatisticsActivity.openActivity(getActivity(), new Date(), mBookBillModel.bookdetail.accountbooktitle, mBookBillModel.bookdetail.accountbookid, mBookBillModel.bookdetail.username,
                        "", mBookBillModel.bookdetail.accountbooktitle, firstTime, true);
//                intent.putExtra("data", new Date());
//                intent.putExtra("bookName", mBookBillModel.bookdetail.accountbooktitle);
//                intent.putExtra("bookid", mBookBillModel.bookdetail.accountbookid);
//                intent.putExtra("bookUserName", mBookBillModel.bookdetail.username);
//                intent.putExtra("bookUserName", mBookBillModel.bookdetail.accountbooktype);
//                intent.putExtra("mAccountbookcatename", mBookBillModel.bookdetail.accountbooktitle);
//                startActivity(intent);
            }
        });

        imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookCommentListActivity.open(getContext(), accountBoolId, false);

            }
        });

        relaToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookCommentListActivity.open(getContext(), accountBoolId, true);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                BookInfoListMultipItem bookInfoListMultipItem = adapter.getItem(position);
                if (bookInfoListMultipItem.getData() != null && bookInfoListMultipItem.getData() instanceof BookBillItemModel) {
                    BookBillItemModel bookBillItemModel = (BookBillItemModel) bookInfoListMultipItem.getData();

                    if (mAccountbookcateid.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {//装修账本
                        if (bookBillItemModel.billtype.equals(RequsterTag.DIARYACCOUNTBOOKCATE)) {
                            WriteJournalDetailActivity.openActivity(getActivity(), accountBoolId, bookBillItemModel.billid, true);
                            return;
                        }

                    }
                    if (bookBillItemModel.billtype.equals(RequsterTag.DIARYACCOUNTBOOKCATE)) {
                        WriteJournalDetailActivity.openActivity(getActivity(), accountBoolId, bookBillItemModel.billid, true);
                    } else {
                        PaymentDetailsActivity.openActivity(getActivity(), accountBoolId, bookBillItemModel.billid, true, true);
                    }


                }

            }
        });
    }


    private void initView() {

        retryDialogHandler = new RetryDialogHandler(getContext());
        headView = new BookBillHeadView(getActivity());

        adapter = new BookInfoListAdapter(getActivity());
        adapter.addHeaderView(headView);
        ryvBookInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        ryvBookInfo.setAdapter(adapter);

    }

    public void setOnDataBookInfoRefreshCallBack(OnDataBookInfoRefreshCallBack onDataBookInfoRefreshCallBack) {
        this.onDataBookInfoRefreshCallBack = onDataBookInfoRefreshCallBack;
    }

    OnDataBookInfoRefreshCallBack onDataBookInfoRefreshCallBack;

    public interface OnDataBookInfoRefreshCallBack {
        void onSuccess(BookBillModel bookBillModel);
    }

    private void setViewData(BookBillModel bookBillModel) {
        String accountbookcate = mBookBillModel.bookdetail.accountbookcate;
        if (accountbookcate.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
            headView.setRenovationDiaryVisible(View.VISIBLE);
        }
        headView.bindViewData(bookBillModel.bookdetail);
        adapter.setDataList(BookInfoListDataAssembler.getAssemblerMultipItem(bookBillModel, mAccountbookcateid, renovationordairy));
        initUnReadCount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUnReadCount();
    }

    private void initUnReadCount() {
        if (mBookBillModel == null) {
            return;
        }
        String commentCountText = mBookBillModel.bookdetail.commenttotal;
        int commentCount = commentCountText == null || commentCountText.length() == 0 ? 0 : Integer.parseInt(commentCountText);
        int unReadCommentCount = BookDataManager.getUnReadCommentCount(accountBoolId, commentCount);
        if (unReadCommentCount <= 0) {
            notes.setVisibility(View.GONE);
//            notes.show();
        } else {
//            notes.hide();
            notes.setVisibility(View.VISIBLE);
            notes.setText(unReadCommentCount + "");
        }
    }

    public static BookInfoDataListFragment newInstance(String accountBookId, String maccountbookcateid, OnDataBookInfoRefreshCallBack onDataBookInfoRefreshCallBack) {
        BookInfoDataListFragment fragment = new BookInfoDataListFragment();
        fragment.setOnDataBookInfoRefreshCallBack(onDataBookInfoRefreshCallBack);
        Bundle bundle = new Bundle();
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_CATE_ID, maccountbookcateid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BookInfoDataListFragment newInstance(String accountBookId, String mMemberid, String maccountbookcateid, OnDataBookInfoRefreshCallBack onDataBookInfoRefreshCallBack) {
        BookInfoDataListFragment fragment = new BookInfoDataListFragment();
        fragment.setOnDataBookInfoRefreshCallBack(onDataBookInfoRefreshCallBack);
        Bundle bundle = new Bundle();
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_ID, accountBookId);
        bundle.putString(DataConstant.BUNDLE_KEY_MEMBER_ID, mMemberid);
        bundle.putString(DataConstant.BUNDLE_KEY_BOOK_CATE_ID, maccountbookcateid);
        fragment.setArguments(bundle);
        return fragment;
    }
}

