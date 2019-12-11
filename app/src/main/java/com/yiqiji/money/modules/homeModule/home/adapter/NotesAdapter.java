package com.yiqiji.money.modules.homeModule.home.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.UIHelper;
import com.yiqiji.money.modules.common.utils.XzbUtils;
import com.yiqiji.money.modules.common.widget.MyPopuwindows;
import com.yiqiji.money.modules.homeModule.home.homeinterface.NotesItemClick;
import com.yiqiji.money.modules.homeModule.home.perecenter.BooksDetailPerecenter;
import com.yiqiji.money.modules.homeModule.home.perecenter.NotesModlePerecenter;

import java.util.HashMap;
import java.util.List;

import huangshang.com.yiqiji_imageloadermaniger.ImageLoaderManager;

/**
 * Created by ${huangweishui} on 2017/5/15.
 * address huang.weishui@71dai.com
 */
public class NotesAdapter implements View.OnClickListener {
    private Context mContext;
    private View view_list;
    private View alignment_view_list;
    private int view_item_id;
    private HashMap<Integer, Integer> hashMapsStringid;
    private HashMap<Integer, Integer> hashMapsimageid;
    private BooksDbInfo booksDbInfo;
    private NotesItemClick notesItemClick;
    private MyPopuwindows myPopuwindows;
    private int screeWith = 0;

    public void setNotesItemClick(NotesItemClick notesItemClick) {
        this.notesItemClick = notesItemClick;
    }

    public NotesAdapter(Context context, View view_list, int view_item_id, View alignment_view_list) {
        this.mContext = context;
        this.view_list = view_list;
        this.view_item_id = view_item_id;
        this.alignment_view_list = alignment_view_list;
        screeWith = XzbUtils.getPhoneScreenintWith((Activity) mContext);
        hashMapsStringid = new HashMap<Integer, Integer>();
        hashMapsimageid = new HashMap<Integer, Integer>();
    }

    public NotesAdapter(Context context, View view_list, int view_item_id) {
        this.mContext = context;
        this.view_list = view_list;
        this.view_item_id = view_item_id;
        screeWith = XzbUtils.getPhoneScreenintWith((Activity) mContext);
    }

    public void setNotesHasImageList(View alignment_view_list, LinearLayout linearLayoutGroup, int messgeinfoItem, List<HashMap<Integer, Integer>> hashMapList) {
        HashMap<Integer, Integer> hashMapsStringid = null;
        HashMap<Integer, Integer> hashMapsimageid = null;
        if (hashMapList != null) {
            hashMapsStringid = hashMapList.get(0);
            hashMapsimageid = hashMapList.get(1);
            this.alignment_view_list = alignment_view_list;
            for (int i = 0; i < hashMapsStringid.size(); i++) {
                View view = LayoutInflater.from(mContext).inflate(view_item_id, null);
                ImageView notes_image = (ImageView) view.findViewById(R.id.notes_image);
                TextView textview_content = (TextView) view.findViewById(R.id.textview_content);
                TextView message_note_text = (TextView) view.findViewById(R.id.message_note_text);
                int text_contentid = hashMapsStringid.get(i);
                String text_content = mContext.getResources().getString(text_contentid);
                textview_content.setText(text_content);
                int text_imageid = hashMapsimageid.get(i);
                ImageLoaderManager.loadImage(mContext, text_imageid, 0, notes_image);
                linearLayoutGroup.addView(view);
                view.setTag(text_contentid);
                view.setOnClickListener(this);
                if (text_contentid == R.string.notes_content && messgeinfoItem > 0) {
                    message_note_text.setVisibility(View.VISIBLE);
                    message_note_text.setText(messgeinfoItem + "");
                }
            }
            showPopuwindows();
        }


    }

    public void setNotesOnlyStringList(View alignment_view_list, LinearLayout linearLayoutGroup, HashMap<Integer, Integer> hashMapsStringid) {
        for (int i = 0; i < hashMapsStringid.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(view_item_id, null);
            TextView textview_content = (TextView) view.findViewById(R.id.textview_content);
            int text_contentid = hashMapsStringid.get(i);
            String text_content = mContext.getResources().getString(text_contentid);
            textview_content.setText(text_content);
            linearLayoutGroup.addView(view);
            view.setTag(text_contentid);
            view.setOnClickListener(this);
        }
        showPopuwindows();
    }

    public void setNotesList(BooksDbInfo booksDbInfo, int messgeinfoItem) {
        this.booksDbInfo = booksDbInfo;
        if (booksDbInfo != null) {
            String accountbookcount = booksDbInfo.getAccountbookcount();
            String isclear = booksDbInfo.getIsclear();
            if (!BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.notes_content);
                hashMapsimageid = NotesModlePerecenter.getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.message_picture);
            } else {
                if (isclear.equals("0")) {
                    hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.has_delet_detail, R.string.notes_content);
                    hashMapsimageid = NotesModlePerecenter.getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.delet_picture_detail, R.drawable.message_picture);

                } else {
                    hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.has_settlement_detail, R.string.has_delet_detail, R.string.notes_content);
                    hashMapsimageid = NotesModlePerecenter.getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.settlement_detail_picture, R.drawable.delet_picture_detail, R.drawable.message_picture);
//                    for (int i = 0; i < RequsterTag.notes_list_content.length; i++) {
//                        hashMapsStringid.put(i, RequsterTag.notes_list_content[i]);
//                        hashMapsimageid.put(i, RequsterTag.notes_list_image[i]);
//                    }

                }
            }
            showNotesList(messgeinfoItem);
        }
    }

    public void setNotesList(boolean isMyself, boolean isBookFount, String isClear) {
        if (isMyself) {
            if (isClear.equals("1")) {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_name, R.string.goto_seetlement, R.string.quit_group);
//                hashMapsStringid.put(0, RequsterTag.notes_member_detail_list_content[0]);
//                hashMapsStringid.put(1, RequsterTag.notes_member_detail_list_content[1]);
//                hashMapsStringid.put(2, RequsterTag.notes_member_detail_list_content[2]);
            } else {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_name, R.string.quit_group);
//                hashMapsStringid.put(0, RequsterTag.notes_member_detail_list_content[0]);
//                hashMapsStringid.put(1, RequsterTag.notes_member_detail_list_content[2]);
            }

        } else {
            if (isBookFount) {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_name, R.string.delet_member);
//                hashMapsStringid.put(0, RequsterTag.notes_member_detail_list_content[0]);
//                hashMapsStringid.put(1, RequsterTag.notes_member_detail_list_content[RequsterTag.notes_member_detail_list_content.length - 1]);
            } else {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_name);
//                hashMapsStringid.put(0, RequsterTag.notes_member_detail_list_content[0]);
            }

        }

        showNotesList();
    }

    public void setNotesList(boolean isMyself, boolean isGroup, boolean isBookFount) {
        if (!isGroup) {
            hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.i_known);
//            hashMapsStringid.put(0, RequsterTag.member_detail_list_content[RequsterTag.notes_member_detail_list_content.length - 1]);
        } else if (isMyself) {
            hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_member, R.string.quit_group, R.string.cancel_text);
//            hashMapsStringid.put(0, RequsterTag.member_detail_list_content[0]);
//            hashMapsStringid.put(1, RequsterTag.member_detail_list_content[2]);
//            hashMapsStringid.put(2, RequsterTag.member_detail_list_content[3]);
        } else {
            if (isBookFount) {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_member, R.string.delet_member_name, R.string.cancel_text);
//                hashMapsStringid.put(0, RequsterTag.member_detail_list_content[0]);
//                hashMapsStringid.put(1, RequsterTag.member_detail_list_content[1]);
//                hashMapsStringid.put(2, RequsterTag.member_detail_list_content[3]);
            } else {
                hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.change_member, R.string.cancel_text);
//                hashMapsStringid.put(0, RequsterTag.member_detail_list_content[0]);
//                hashMapsStringid.put(1, RequsterTag.member_detail_list_content[3]);
            }

        }

        showNotesList(isGroup);
    }

    private void showNotesList(boolean isGroup) {
        if (view_list != null) {
            LinearLayout linearLayout = (LinearLayout) view_list.findViewById(R.id.layout_member);
            for (int i = 0; i < hashMapsStringid.size(); i++) {
                View view_item = LayoutInflater.from(mContext).inflate(view_item_id, null);
                TextView change_member_text = (TextView) view_item.findViewById(R.id.change_member_text);
                int text_contentid = hashMapsStringid.get(i);
                String text_content = mContext.getResources().getString(text_contentid);
                change_member_text.setText(text_content);
                linearLayout.addView(view_item);
                view_item.setTag(text_contentid);
                view_item.setOnClickListener(this);
            }
        }
        myPopuwindows = new MyPopuwindows(mContext, view_list);
        myPopuwindows.showAtLocation(alignment_view_list, Gravity.CENTER, 0, 0);
    }


    private void showNotesList(int messgeinfoItem) {
        if (view_list != null) {
            LinearLayout linearLayout = (LinearLayout) view_list.findViewById(R.id.notes_layout);
            for (int i = 0; i < hashMapsStringid.size(); i++) {
                View view_item = LayoutInflater.from(mContext).inflate(view_item_id, null);
                ImageView notes_image = (ImageView) view_item.findViewById(R.id.notes_image);
                TextView textview_content = (TextView) view_item.findViewById(R.id.textview_content);
                TextView message_note_text = (TextView) view_item.findViewById(R.id.message_note_text);
                int text_contentid = hashMapsStringid.get(i);
                String text_content = mContext.getResources().getString(text_contentid);
                textview_content.setText(text_content);
                if (hashMapsimageid.size() > 0) {
                    int text_imageid = hashMapsimageid.get(i);
                    String text_image = "drawable://" + text_imageid;
                    XzbUtils.displayImage(notes_image, text_image, 0);
                }
                linearLayout.addView(view_item);
                view_item.setTag(text_contentid);
                view_item.setOnClickListener(this);
                if (text_contentid == RequsterTag.notes_list_content[RequsterTag.notes_list_content.length - 1] && messgeinfoItem > 0) {
                    message_note_text.setVisibility(View.VISIBLE);
                    message_note_text.setText(messgeinfoItem + "");
                }
            }
        }
        showPopuwindows();
    }

    private void showNotesList() {
        if (view_list != null) {
            LinearLayout linearLayout = (LinearLayout) view_list.findViewById(R.id.notes_layout);
            for (int i = 0; i < hashMapsStringid.size(); i++) {
                View view_item = LayoutInflater.from(mContext).inflate(view_item_id, null);
                ImageView notes_image = (ImageView) view_item.findViewById(R.id.notes_image);
                TextView textview_content = (TextView) view_item.findViewById(R.id.textview_content);
                notes_image.setVisibility(View.GONE);
                int text_contentid = hashMapsStringid.get(i);
                String text_content = mContext.getResources().getString(text_contentid);
                textview_content.setText(text_content);
                linearLayout.addView(view_item);
                view_item.setTag(text_contentid);
                view_item.setOnClickListener(this);
            }
        }
        showPopuwindows();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopuwindows() {
        final int[] location = new int[2];
        alignment_view_list.getLocationOnScreen(location);
        view_list.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        myPopuwindows = new MyPopuwindows(mContext, view_list);
        myPopuwindows.setWithAndHeightList();
        myPopuwindows.backgroundAlpha(1.0f);
        if (view_list.isAttachedToWindow()) {
            view_list.post(new Runnable() {
                @Override
                public void run() {
                    final int view_listwith = view_list.getMeasuredWidth();
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myPopuwindows.showAtLocation(alignment_view_list, Gravity.NO_GRAVITY,
                                    (int) (screeWith - view_listwith - alignment_view_list.getMeasuredWidth() / 2
                                            + UIHelper.dip2px(mContext, 5)), location[1] + alignment_view_list.getHeight() - UIHelper.dip2px(mContext, 5));
                        }
                    });
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int view_listwith = view_list.getMeasuredWidth();
                    myPopuwindows.showAtLocation(alignment_view_list, Gravity.NO_GRAVITY,
                            (int) (screeWith - view_listwith - alignment_view_list.getMeasuredWidth() / 2
                                    + UIHelper.dip2px(mContext, 5)), location[1] + alignment_view_list.getHeight() - UIHelper.dip2px(mContext, 5));
                }
            }, 100);
//            view_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    view_list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    final int view_listwith = view_list.getMeasuredWidth();
//                    myPopuwindows.showAtLocation(alignment_view_list, Gravity.NO_GRAVITY,
//                            (int) (screeWith - view_listwith - alignment_view_list.getMeasuredWidth() / 2
//                                    + UIHelper.dip2px(mContext, 5)), location[1] + alignment_view_list.getHeight() - UIHelper.dip2px(mContext, 5));
//                }
//            });
        }


    }

    @Override
    public void onClick(View v) {
        myPopuwindows.dismiss();
        notesItemClick.onClickNotesItem(v);
    }
}
