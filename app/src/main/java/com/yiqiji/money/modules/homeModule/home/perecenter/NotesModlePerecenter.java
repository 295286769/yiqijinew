package com.yiqiji.money.modules.homeModule.home.perecenter;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.common.db.BooksDbInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/6/28.
 * address huang.weishui@71dai.com
 */
public class NotesModlePerecenter {
    public final static int[] notes_list_content = new int[]{R.string.share_book, R.string.has_settlement_detail, R.string.has_delet_detail, R.string.book_setting, R.string.notes_content};
    public final static int[] notes_member_detail_list_content = new int[]{R.string.change_name, R.string.goto_seetlement, R.string.quit_group, R.string.delet_member};
    public final static int[] member_detail_list_content = new int[]{R.string.change_member, R.string.delet_member_name, R.string.quit_group, R.string.cancel_text, R.string.i_known};
    public final static int[] notes_list_image = new int[]{R.drawable.share_book, R.drawable.settlement_detail_picture,
            R.drawable.delet_picture_detail, R.drawable.book_setting, R.drawable.message_picture};

    public static List<HashMap<Integer, Integer>> getHashMapsStringidimageid(BooksDbInfo booksDbInfo, int newmsgposition) {
        List<HashMap<Integer, Integer>> integerHashMap = new ArrayList<>();
        HashMap<Integer, Integer> hashMapsStringid;
        HashMap<Integer, Integer> hashMapsimageid;
        if (booksDbInfo != null) {
            String accountbookcount = booksDbInfo.getAccountbookcount();
            String isclear = booksDbInfo.getIsclear();
            if (!BooksDetailPerecenter.isAccountbookCount(accountbookcount)) {
                hashMapsStringid = getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.notes_content);
                hashMapsimageid = getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.message_picture);
            } else {
                if (isclear.equals("0")) {
                    hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.has_delet_detail, R.string.notes_content);
                    hashMapsimageid = NotesModlePerecenter.getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.delet_picture_detail, R.drawable.message_picture);

                } else {
                    hashMapsStringid = NotesModlePerecenter.getHashMapsStringid(R.string.share_book, R.string.book_setting, R.string.goto_seetlement, R.string.has_delet_detail, R.string.has_delet_detail, R.string.notes_content);
                    hashMapsimageid = NotesModlePerecenter.getHashMapsDrawbleid(R.drawable.share_book, R.drawable.book_setting, R.drawable.settlement_detail_picture, R.drawable.delet_picture_detail, R.drawable.delet_picture_detail, R.drawable.message_picture);
                }
            }
            integerHashMap.add(hashMapsStringid);
            integerHashMap.add(hashMapsimageid);
        }
        return integerHashMap;
    }

    public static HashMap<Integer, Integer> getHashMapsStringid(int... stringList) {
        HashMap<Integer, Integer> integerHashMap = new HashMap<>();
        for (int i = 0; i < stringList.length; i++) {
            integerHashMap.put(i, stringList[i]);
        }
        return integerHashMap;
    }

    public static HashMap<Integer, Integer> getHashMapsDrawbleid(int... imageList) {
        HashMap<Integer, Integer> integerHashMap = new HashMap<>();
        for (int i = 0; i < imageList.length; i++) {
            integerHashMap.put(i, imageList[i]);
        }
        return integerHashMap;
    }
}
