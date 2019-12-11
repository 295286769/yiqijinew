package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.book.detailinfo.model.AttentionResultModel;
import com.yiqiji.money.modules.book.detailinfo.model.BookDetailModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by leichi on 2017/6/13.
 */

public class BookBillHeadView extends LinearLayout {

    Context mContext;

    @BindView(R.id.image_bookCover)
    ImageView imageBookCover;
    @BindView(R.id.tv_fansNum)
    TextView tvFansNum;//订阅
    @BindView(R.id.user_name)
    TextView user_name;//创建者
    @BindView(R.id.browse_number)
    TextView browse_number;//浏览量
    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    @BindView(R.id.tv_creater)
    TextView tvCreater;
    @BindView(R.id.tv_bookTitle)
    TextView tvBookTitle;
    @BindView(R.id.ll_bookDescribe)
    LinearLayout llBookDescribe;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.ll_superclass)
    RelativeLayout llSuperclass;
    @BindView(R.id.check_sort)
    CheckBox check_sort;                                  //账单排序 isChecked=true 由近至远 sort=0;isChecked=false 由远至近 sort=1
    @BindView(R.id.decoration_log_details_group)
    RadioGroup decoration_log_details_group;              //装修账本，装修清单和日记

    int sortType = 0;

    public BookBillHeadView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BookBillHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View contentView = View.inflate(mContext, R.layout.book_info_list_headview_layout, null);
        addView(contentView);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        llSuperclass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSwitchSortTypeListener != null) {
                    sortType = sortType == 0 ? 1 : 0;
                    onSwitchSortTypeListener.onSwitchSortType(sortType);
                }
            }
        });

        check_sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvSort.setText(isChecked ? "由近至远" : "由远至近");

            }
        });

        check_sort.setChecked(true);
    }

    public void bindViewData(BookDetailModel detailModel) {
        if (detailModel.accountbookdesc != null && detailModel.accountbookdesc.size() > 0) {
            assembleBookDescribe(detailModel.accountbookdesc);
        }
        tvCreater.setText("@" + detailModel.username + "创建");
        user_name.setText(detailModel.username);
        tvFansNum.setText(detailModel.follownum);
        browse_number.setText(detailModel.viewcount);
        tvBookTitle.setText(detailModel.accountbooktitle);
        ImageLoaderManager.loadImage(mContext, detailModel.accountbookbgimg, R.drawable.sing_icon, imageBookCover);
    }

    private void assembleBookDescribe(ArrayList<String> textList) {
        llBookDescribe.removeAllViews();
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            llBookDescribe.addView(getItemView(text));
        }
    }

    private View getItemView(String text) {
        TextView textView = (TextView) View.inflate(mContext, R.layout.item_replay_thum_textview, null);
        float px = getResources().getDimension(R.dimen.text_size_book_describe);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_book_describe));
        textView.setTextColor(getResources().getColor(R.color.secondary_text));
        textView.setText(text);
        return textView;
    }

    public void setRenovationDiaryVisible(int visible) {
        if (decoration_log_details_group != null) {
            decoration_log_details_group.setVisibility(visible);
        }
    }

    OnSwitchSortTypeListener onSwitchSortTypeListener;

    public interface OnSwitchSortTypeListener {
        void onSwitchSortType(int sort);
    }

    public void setOnSwitchSortTypeListener(OnSwitchSortTypeListener onSwitchSortTypeListener) {
        this.onSwitchSortTypeListener = onSwitchSortTypeListener;
    }

    public void onSortSuccessCabbBack() {
        check_sort.setChecked(sortType == 0 ? true : false);
    }

    public int getSortType() {
        return sortType;
    }

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        if (decoration_log_details_group != null) {
            decoration_log_details_group.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(AttentionResultModel attentionResultModel) {
        tvFansNum.setText(attentionResultModel.follownum);
    }

}
