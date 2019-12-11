package com.yiqiji.money.modules.book.detailinfo.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.money.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/18.
 * address huang.weishui@71dai.com
 */
public class DiaryItemView extends RelativeLayout {
    private Context mContext;
    @BindView(R.id.journal_text)
    TextView journal_text;//装修日志阶段
    @BindView(R.id.tv_billDiaryText)
    TextView tv_billDiaryText;//装修日志感想
    @BindView(R.id.diaryImageView)
    DiaryImageView diaryImageView;//装修日志图片

    public DiaryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_renovation_diary, this, true);
        ButterKnife.bind(this, view);

    }

    public DiaryItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiaryItemView(Context context) {
        this(context, null);
    }

    public void setBillDiaryText(final String billDiaryText) {
        if (tv_billDiaryText != null) {
//            tv_billDiaryText.post(new Runnable() {
//                @Override
//                public void run() {
//                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) tv_billDiaryText.getLayoutParams();
//                    Layout layout = tv_billDiaryText.getLayout();
//                    if (layout != null) {
//                        int lineCount = layout.getLineCount();//最大行数
//                        if (lineCount > 6) {
//                            String billDiaryTextall = billDiaryText + "点击查看全文";
//                            int textColor = getResources().getColor(R.color.main_back);
//                            SpannableStringBuilder spannableStringBuilder = StringUtils.getStypeTextString(billDiaryTextall,  "点击查看全文",
//                                    UIHelper.Dp2Px(mContext, 15), textColor, new ClickableSpan() {
//                                        @Override
//                                        public void onClick(View widget) {
//                                        }
//                                    });
//                        }
//                    }
//                }
//            });
            tv_billDiaryText.setText(billDiaryText);
        }
    }

    public void setJournalText(String journalText) {
        if (journal_text != null) {
            journal_text.setText(journalText);
        }
    }

    public void setDiaryImage(String url) {
        if (diaryImageView != null) {
            if (TextUtils.isEmpty(url)) {
                diaryImageView.setVisibility(View.GONE);
            } else {
                diaryImageView.setVisibility(View.VISIBLE);
                diaryImageView.setDiaryImage(url);
            }

        }

    }

    public void setDiaryNumber(String number) {
        if (diaryImageView != null) {
            diaryImageView.setDiaryNumber(number);
        }
    }
}
