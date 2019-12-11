package com.yiqiji.money.modules.book.bookcategory.data_manager;

/**
 * Created by leichi on 2017/5/18.
 */

public class DataConstant {

    public static final int EXPEND_BILL_TYPE = 1;                                                                        //支出账本
    public static final int INCOME_BILL_TYPE = 0;                                                                        //收入账本
    public static final int PAYMENT_BILL_TYPE = 4;                                                                       //成员交款

    public static final String BUNDLE_KEY_BOOK_ID = "bundle_key_account_book_id";                                        //账本id
    public static final String BUNDLE_KEY_MEMBER_ID = "bundle_key_account_member_id";                                     //账本id
    public static final String BUNDLE_KEY_BOOK_NAME = "bundle_key_book_name";                                            //账本名称
    public static final String BUNDLE_KEY_BOOK_TYPE = "bundle_key_book_type";                                            //账本类型
    public static final String BUNDLE_KEY_BOOK_CATE_ID = "bundle_key_book_cate_id";                                            //账本类型分类id
    public static final String BUNDLE_KEY_BOOK_CAREGORY = "bundle_key_book_caregory";                                    //账本分类
    public static final String BUNDLE_KEY_BOOK_OPERATION_TYPE = "bundle_key_book_operation_type";                        //账本分类的操作类型
    public static final String BUNDLE_KEY_BOOK_OPERATION_HIERARCHY = "bundle_key_book_operation_hierarchy";              //账本分类的操作类型
    public static final String BUNDLE_KEY_SHOW_INPUT_BOARD = "bundle_key_show_input_board";                              //是否需要显示键盘


    public static final int REQUEST_CODE_CATEGORY_MANAGER = 0x00007;                                                     //打开管理账本的requestCode
    public static final int REQUEST_CODE_CATEGORY_EDIT = 0x00008;                                                        //打开编辑账本的requestCode
    public static final int REQUEST_CODE_CATEGORY_ORDER = 0x00009;                                                       //打开分类排序的requestCode
}
