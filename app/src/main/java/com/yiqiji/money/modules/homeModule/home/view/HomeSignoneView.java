package com.yiqiji.money.modules.homeModule.home.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiqiji.frame.core.Constants;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.utils.IntentUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.home.activity.SpeechRecognizerActivity;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalActivity;
import com.yiqiji.money.modules.homeModule.write.activity.DetailsActivity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${huangweishui} on 2017/7/27.
 * address huang.weishui@71dai.com
 */
public class HomeSignoneView extends RelativeLayout {
    @BindView(R.id.sign_button)
    TextView signButton;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    //    @BindView(R.id.rl_signOne)
//    RelativeLayout rlSignOne;
    private Context mContext;
    private BooksDbInfo booksDbInfo;
    private Date date_time;
    private String memberid = "";
    private String user_name = "";

    public HomeSignoneView(Context context) {
        this(context, null);
    }

    public HomeSignoneView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeSignoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initText();
        setOnClick();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_home_signone_yout, this, true);
        ButterKnife.bind(this, view);
    }

    private void setOnClick() {
        signButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo == null) {
                    return;
                }
                XzbUtils.hidePointInUmg(mContext, Constants.HIDE_NOTE_ONE);
                onStartActivity(booksDbInfo.getAccountbooktype(), booksDbInfo.getIsclear());
            }
        });
        ivVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksDbInfo == null) {
                    return;
                }
                SpeechRecognizerActivity.open(mContext, booksDbInfo, user_name, memberid, date_time);
            }
        });
        signButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    IntentUtils.setReflectionjump(mContext, WriteJournalActivity.class.getName(), "longdate", date_time.getTime() / 1000 + "", "accountbookid", booksDbInfo.getAccountbookid());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    public void initText() {
        String highlightStr = mContext.getResources().getString(R.string.long_click_add_log);
        String wholeStr = mContext.getResources().getString(R.string.sign_one_text) +
                mContext.getResources().getString(R.string.space) + highlightStr;
        int size = UIHelper.sp2px(mContext, 13);
        int textColor = mContext.getResources().getColor(R.color.long_click_text_color);
        SpannableStringBuilder spannableStringBuilder = StringUtils.getStypeTextString(wholeStr, highlightStr, size, textColor);
        signButton.setText(spannableStringBuilder);
    }

    public void setBookInfo(BooksDbInfo booksDbInfo, List<BooksDbMemberInfo> booksDbMemberInfos, Date date_time) {
        this.booksDbInfo = booksDbInfo;
        this.date_time = date_time;
        memberid = ((HomeActivity) mContext).getMySelfNyMemeberId(booksDbInfo.getMyuid(), booksDbMemberInfos);
        user_name = ((HomeActivity) mContext).getMySelfMemeberName(booksDbInfo.getMyuid(), booksDbMemberInfos);

    }

    /**
     * 根据账本类型分别跳转 在记一笔中区别就是有没有成员 1、单人记账 2、多人记账
     *
     * @param
     */
    private void onStartActivity(String bookNameType, String isclear) {
        DetailsActivity.startActivity(mContext, booksDbInfo, user_name, memberid, date_time);

    }
}
