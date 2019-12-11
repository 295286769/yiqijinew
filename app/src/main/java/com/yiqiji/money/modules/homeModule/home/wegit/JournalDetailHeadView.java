package com.yiqiji.money.modules.homeModule.home.wegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiqiji.money.R;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.book.detailinfo.activity.BookZoonImageActivity;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.DialogUtils;
import com.yiqiji.money.modules.common.utils.MaxLengthWatcher;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.common.utils.ToastUtils;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.view.CircleImageView;
import com.yiqiji.money.modules.common.widget.BaseRecylerview;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.activity.WriteJournalDetailActivity;
import com.yiqiji.money.modules.homeModule.home.adapter.JounrnalImageAdapter;
import com.yiqiji.money.modules.homeModule.home.entity.JournalCommentEvent;
import com.yiqiji.money.modules.homeModule.home.perecenter.CommentPerecenter;
import com.yiqiji.money.modules.homeModule.home.view.JournalRoundedImageView;
import com.yiqiji.money.modules.homeModule.write.activity.PhotosShowActivity;
import com.yiqiji.money.modules.myModule.login.activity.LoginBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/7/20.
 * address huang.weishui@71dai.com
 */
public class JournalDetailHeadView extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    @BindView(R.id.journal_date)
    TextView journal_date;//日期
    @BindView(R.id.relayout_head)
    RelativeLayout relayout_head;//
    @BindView(R.id.head_image)
    CircleImageView head_image;//头像
    @BindView(R.id.content_text)
    TextView content_text;//装修文本描述
    @BindView(R.id.username)
    TextView username;//用户名
    @BindView(R.id.stage)
    TextView stage;//装修阶段
    @BindView(R.id.location_text)
    TextView location_text;//定位文字
    @BindView(R.id.location_image)
    ImageView location_image;//定位图标
    @BindView(R.id.roundedImageView)
    JournalRoundedImageView roundedImageView;//
    @BindView(R.id.image_list)
    BaseRecylerview image_list;//图片列表
    //    @BindView(R.id.layout_clickreview)
//    RelativeLayout layout_clickreview;//评论容器
//    @BindView(R.id.pinglun_text)
//    TextView pinglun_text;//评论条数
    @BindView(R.id.write)
    TextView write;//写评论
    @BindView(R.id.comment_click_layout)
    LinearLayout comment_click_layout;//没有评论的容器
    @BindView(R.id.comment_click)
    TextView comment_click;//没有评论是评论按钮
    @BindView(R.id.operator)
    TextView operator;//操作人
    @BindView(R.id.time)
    TextView operator_time;//操作人
    private JounrnalImageAdapter jounrnalImageAdapter;
    private List<String> iamgeList = null;//图片列表
    private String mAccountbookid;
    private String mMemberid;
    private String mBillid;
    private String mImageurl;

    public JournalDetailHeadView(Context context) {
        this(context, null);
    }

    public JournalDetailHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public JournalDetailHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        onclick();
    }

    private void onclick() {
        write.setOnClickListener(this);
        comment_click.setOnClickListener(this);
        roundedImageView.setOnClickListener(this);
        jounrnalImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookZoonImageActivity.openActivity(mContext, iamgeList, position);

            }
        });
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_journal_detail_head, this, true);
        ButterKnife.bind(this, view);
        jounrnalImageAdapter = new JounrnalImageAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        image_list.setFocusable(false);
        image_list.setLayoutManager(linearLayoutManager);
        image_list.setAdapter(jounrnalImageAdapter);

        int textColor = getResources().getColor(R.color.main_back);
        SpannableStringBuilder spannableStringBuilder = StringUtils.getStypeTextString("还没有评论,点击评论", "点击评论",
                UIHelper.Dp2Px(mContext, 15), textColor, new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        onScroll();
                    }
                });
        comment_click.setText(spannableStringBuilder);
    }

    private int position = 0;

    public void setDailycostEntity(boolean isSubscribe, String mAccountbookid, String mMemberid, String accountbookcate, DailycostEntity dailycostEntity) {
        this.mAccountbookid = mAccountbookid;
        this.mMemberid = mMemberid;
        mBillid = dailycostEntity.getBillid();
        String date = DateUtil.getDateToString(Long.parseLong(dailycostEntity.getTradetime()), "MM月dd日");
        String head_image_url = dailycostEntity.getUsericon();
        String journal_text = dailycostEntity.getBillmark();
        String billcatename = dailycostEntity.getBillcatename();
        String address = dailycostEntity.getAddress();
        String billimg = dailycostEntity.getBillimg();
        String billnum = dailycostEntity.getBillnum();
        mImageurl = billimg;
        roundedImageView.setVisibility(View.GONE);
        if (isSubscribe && accountbookcate.equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE)) {
            journal_date.setVisibility(View.VISIBLE);
            image_list.setVisibility(View.VISIBLE);
            iamgeList = dailycostEntity.getImglist();
            journal_date.setText(date);
            if (iamgeList == null) {
                iamgeList = new ArrayList<>();
                image_list.setVisibility(View.GONE);
            }
            jounrnalImageAdapter.setDataList(iamgeList);
//            image_list.scrollTo(0, 0);

        } else {
            if (!TextUtils.isEmpty(billimg)) {
                roundedImageView.setVisibility(View.VISIBLE);
            }
            ImageLoaderManager.loadImage(mContext, billimg, roundedImageView);
        }
//        if (!TextUtils.isEmpty(billcatename)) {
//            stage.setVisibility(View.VISIBLE);
//            stage.setText(billcatename);
//        }
//        if (!TextUtils.isEmpty(address)) {
//            location_image.setVisibility(View.VISIBLE);
//            location_text.setVisibility(View.VISIBLE);
//            location_text.setText(dailycostEntity.getAddress());
//        }
        operator.setText("操作人:" + dailycostEntity.getUsername());
        operator_time.setText("操作时间:" + DateUtil.timeStampToStr(Long.parseLong(dailycostEntity.getTradetime())));
        ImageLoaderManager.loadImage(mContext, head_image_url, head_image);
        content_text.setText(Html.fromHtml(journal_text));

    }

    public void commCountSize(int commentSize) {
        comment_click_layout.setVisibility(View.GONE);
        write.setVisibility(View.GONE);
//        layout_clickreview.setVisibility(View.VISIBLE);
        if (commentSize == 0) {
            comment_click_layout.setVisibility(View.VISIBLE);
//            layout_clickreview.setVisibility(View.GONE);
        } else {
            write.setVisibility(View.VISIBLE);
        }
//        pinglun_text.setText("评论" + "(" + commentSize + ")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write://有评论时
                onScroll();
                break;
            case R.id.comment_click://没有评论时
                onScroll();
                break;
            case R.id.roundedImageView://点击查看大图
                Intent intent = new Intent(mContext, PhotosShowActivity.class);
                intent.putExtra("path", mImageurl);
                intent.putExtra("isNeedDelet", true);
                mContext.startActivity(intent);
                break;
        }
    }

    private PopupWindow basePopuWindow = null;

    public void onScroll() {

        View view2 = LayoutInflater.from(mContext).inflate(R.layout.beauty_finance_vedios, null);
        basePopuWindow = new MyPopuwindows(mContext, view2);
        basePopuWindow.showAtLocation(write, Gravity.BOTTOM, 0, 0);

        final EditText content_praise = (EditText) view2.findViewById(R.id.content_praise);
        new MaxLengthWatcher(120, content_praise);


        TextView publish_button = (TextView) view2.findViewById(R.id.publish_button);
        content_praise.setFocusable(true);
        content_praise.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) publish_button.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        publish_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = StringUtils.StringFilter(content_praise.getText().toString());

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.DiyToast(mContext, "您还没有添加评论内容");
                    return;
                }
                comment(content);

            }
        });
    }

    private void comment(final String content) {

        CommentPerecenter.cmitComment(mAccountbookid, mBillid, mMemberid, content, new ViewCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                basePopuWindow.dismiss();
                ((WriteJournalDetailActivity) mContext).showLoadingDialog();
            }

            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                EventBus.getDefault().post(new JournalCommentEvent());
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                if (simleMsg == null) {
                    return;
                }
                if (simleMsg.getErrCode() == -110 || simleMsg.getErrCode() == -100) {
                    ((WriteJournalDetailActivity) mContext).showToast(simleMsg);
                } else {
                    DialogUtils.showConfirm((Activity) mContext, "提示", "您还未登录,是否登录？ ", "确定", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            LoginBaseActivity.openActivity(mContext);
                            DialogUtils.dismissConfirmDialog();

                        }
                    }, "取消", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DialogUtils.dismissConfirmDialog();

                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ((WriteJournalDetailActivity) mContext).showLoadingDialog();
            }
        });


    }

}
