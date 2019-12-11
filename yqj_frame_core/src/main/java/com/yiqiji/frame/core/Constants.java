package com.yiqiji.frame.core;

public class Constants {


    public static final boolean DEBUG = false;

    public Constants() {
    }

    public final static String secret = "1384f237af30b52e60031db95cbd9ec0dfb0b1c5";


    // 用于区分是编辑跳转或添加跳转,添加跳转传true,编辑跳转传false
    public final static String INTENTTYPE = "INTENTTYPE";

    //    public static String BASE_URL = "http://api.yiqijiba.com";
    public static String BASE_URL = DEBUG ? "http://api.test.yiqijiba.com" : "http://api.yiqijiba.com";
    //public static String BASE_URL = "http://api.yiqijiba.com";
    public static String ICON_BASE_URL = "http://static.yiqijiba.com";


    public static final String BLACK = "#bcbd22";
    public static final String DARK_BLUE = "#dbdb8d";
    public static final String DARK_GREEN = "#ff7f0e";
    public static final String DARK_CYAN = "#ffbb78";
    public static final String DEEP_SKY_BLUE = "#2ca02c";
    public static final String MEDIUM_SPRING_GREEN = "#98df8a";
    public static final String LIME = "#d62728";
    public static final String CYAN = "#ff9896";
    public static final String MID_NIGHT_BLUE = "#9467bd";
    public static final String MEDIUM_SEA_GREEN = "#c5b0d5";
    public static final String ROYAL_BLUE = "#8c564b";
    public static final String INDIGO = "#c49c94";
    public static final String DARK_OLIVE_GREEN = "#e377c2";
    public static final String DIM_GRAY = "#f7b6d2";
    public static final String MAROON = "#ffa45c";
    public static final String GREEN_YELLOW = "#f4c6a2";
    public static final String MEDIUM_ORCHID = "#17becf";
    public static final String GOLD = "#9edae5";
    public static final String LIGHT_PINK = "#1f77b4";
    public static final String FUCHSIA = "#aec7e8";

    public static final String ALICEBLUE = "F0F8FF";
    public static final String NO_RECORDS = "#CED6DD";

    public static String[] COLORS = new String[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_CYAN, DEEP_SKY_BLUE,
            MEDIUM_SPRING_GREEN, LIME, CYAN, MID_NIGHT_BLUE, MEDIUM_SEA_GREEN, ROYAL_BLUE, INDIGO, DARK_OLIVE_GREEN,
            DIM_GRAY, MAROON, GREEN_YELLOW, MEDIUM_ORCHID, GOLD, LIGHT_PINK, FUCHSIA, BLACK, DARK_BLUE, DARK_GREEN,
            DARK_CYAN, DEEP_SKY_BLUE, MEDIUM_SPRING_GREEN, LIME, CYAN, MID_NIGHT_BLUE};


    /**
     * 短信验证码
     */
    public static final String SEND_MSG_COD = "/common/mobile/getcode";

    public static final String USER_REG_VERIFYMCODE = "/user/reg/verifymcode";
    /**
     * 设置密码
     */
    public static final String SET_PASSWORD = "/user/userpass/set";

    /**
     * 手机，密码登录
     */
    public static final String LOGIN_BY_USER_NAME_PWD = "/user/login/passport";

    /**
     * 手机短信验证码登录
     */
    public static final String LOGIN_BY_MESSAGE_CODE = "/user/login/mobileport";

    public static final String VERIFY_BY_MESSAGE_CODE = "/user/userpass/verifymcode";

    /**
     * 忘记密码，重设登录密码
     */
    public static final String RESET_PWD = "/user/userpass/reset";

    /**
     * 通过原密码重设密码
     */
    public static final String RESET_PWD_BY_OLD_PWD = "/user/userpass/change";

    /**
     * 设置用户名
     */

    public static final String USERPASS_SET_USERNAME = "/user/userpass/setusername";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "/user/userpass/getuserinfo";
    /**
     * 上传用户头像
     */
    public static final String SET_USER_ICON = "/user/userpass/setusericon";

    /**
     * 问题反馈
     */
    public static final String FEEDBACK_SAVE = "/user/feedback/save";

    /**
     * 消息接口
     */
    public static final String MESSAGE_INDEX = "/user/message/index";
    /**
     * 检测是否有新消息和账本
     */
    public static final String CHECK_MESSAGE = "/common/index/checkdata";

    /**
     * 消息接口
     */
    public static final String MESSAGE_READ = "/user/message/read";

    /**
     * 账本列表
     */
    public static final String HOME_LIST = "/accounting/books/home";
    /**
     * 账本结算列表
     */
    public static final String BOOK_SETTLEMENT_LIST = "/accounting/books/clearlist";
    /**
     * 账本结算详情
     */
    public static final String BOOK_CLEAR_DETAIL = "/accounting/books/cleardetail";
    /**
     * 账本结算接口
     */
    public static final String BOOK_CLEAR = "/accounting/books/clear";
    /**
     * 账本详情
     */
    public static final String BOOKS_DETAIL = "/accounting/books/detail";

    /**
     * 编辑账本
     */
    public static final String BOOKS_EDIT = "/accounting/books/edit";
    /**
     * 获取日志对应分类列表
     */
    public static final String JOURNAL_CATE = "/accounting/index/getJournalCate";

    /**
     * 资产模块相关接口
     */
    public static final String ASSERT = "/asset/index/home";//资产首页
    public static final String ADD_ASSERT = "/asset/add/account";//添加资产
    public static final String ADD_CREDIT_ASSERT = "/asset/add/credit";//添加信用卡资产
    public static final String ADD_FUND_ASSERT = "/asset/add/fund";//添加基金资产
    public static final String ADD_STOCK_ASSERT = "/asset/add/stock";//添加股票资产
    public static final String ADD_FINANC_ASSERT = "/asset/add/financing";//添加网络理财资产
    public static final String ADD_LOAN_ASSERT = "/asset/add/loan";//添加借款资产
    public static final String ADD_FINISH_ASSERT = "/asset/index/finish";//添加完结资产
    public static final String ADD_DELE_ASSERT = "/asset/index/del";//添加删除资产
    public static final String ADD_CATE_ASSERT = "/asset/index/cate";//获取资产分类接口
    public static final String ADD_RENEW_ASSERT = "/asset/edit/account";//更新接口()
    public static final String ADD_RENEW_CREDIT = "/asset/edit/credit";//更新信用卡
    public static final String ADD_RENEW_FUND = "/asset/edit/fund";//更新基金
    public static final String ADD_RENEW_STOCK = "/asset/edit/stock";//更新股票
    public static final String ADD_RENEW_FINAN = "/asset/edit/financing";//更新网络理财自定义
    public static final String ADD_RENEW_LOAN = "/asset/edit/loan";//更新借款资产
    public static final String GET_BANK_LIST = "/asset/index/bank";//获取银行列表
    public static final String ASSERT_DETAIL = "/asset/detail/index";//资产详情页接口
    public static final String SEARCH_STOCKFUND = "/search/stockquery";//股票基金搜索接口
    public static final String TRANSLATE_ASSERT_ACOUNT = "/asset/index/getAssetAccount";//获取转账资金账户接口
    public static final String TRANSLATE_MONEY = "/asset/edit/virement";//资金账户转账接口


    /**
     * 删除 退出账本
     */
    public static final String BOOKS_DEL = "/accounting/books/del";

    /**
     * 添加账本
     */
    public static final String ADD_BOOK = "/accounting/books/add";
    /**
     * 添加成员
     */
    public static final String ADD_MEMBER = "/accounting/member/add";
    /**
     * 修改成员名片
     */
    public static final String CHANG_MEMBER_NAME = "/accounting/member/edit";
    /**
     * 删除成员名片
     */
    public static final String DELET_MEMBER_NAME = "/accounting/member/del";
    /**
     * 退出账本
     */
    public static final String QUIT_MEMBER_NAME = "/accounting/books/quit";
    /**
     * 邀请接口
     */
    public static final String INVITATION = "/user/index/invite";
    /**
     * 获取已删除账单
     */
    public static final String BILL_IS_DELET = "/accounting/bill/getdeletedbill";
    /**
     * 获取已结算账单
     */
    public static final String CLEARED_BILL = "/accounting/bill/getclearedbill";
    /**
     * 评论列表
     */
    public static final String GETCOMMENT = "/accounting/bill/getcomment";
    /**
     * 评论
     */
    public static final String COMMENT = "/accounting/bill/comment";
    /**
     * 群组成员分享
     */
    public static final String COST = "/accounting/member/cost";

    /**
     * 获取账本所属分类列表
     */
    public static final String BOOKS_CATE = "/accounting/books/cate";
    /**
     * 拉取账单列表
     */
    public static final String BOOKS_BILL = "/accounting/bill/getbill";

    /**
     * 同步账单数据
     */
    public static final String SYNC_BOOK = "/accounting/bill/sync";
    /**
     * 设置预算
     */
    public static final String BUDGET = "/accounting/books/budget";

    // /**
    // * 版本更新
    // */
    // public static final String CHECK_VESION = "/leichi/ziniu/init";
    /**
     * 版本更新
     */
    public static final String CHECK_VESION = "/index/index/start";
    /**
     * 统计图片上传
     */
    public static final String COMMON_IMAGE = "/common/image/upload";
    /**
     * 分享app下载官网
     */
    public static final String shareapp = "/common/index/download";
    /**
     * 获取账单结算明细
     */
    public static final String GETCLEARINFO = "/accounting/bill/getclearinfo";
    /**
     * 获取账单详情
     */
    public static final String BILLDETAIL = "/accounting/bill/getbilldetail";
    /**
     * 生成短连接
     */
    public static final String GENSHORTURL = "/common/index/genshorturl";
    /**
     * 银行卡列表
     */
    public static final String BANK = "/asset/index/bank";

    /**
     * 验证手机号是否注册
     */
    public static final String CHECKMOBILE = "/user/login/checkmobile";


    /**
     * 验证手机号是否注册
     */
    public static final String AUTHCHECK = "/user/oauth/check";


    /**
     * 第三方登录授权绑定接口
     */
    public static final String OAUTHBOUND = "/user/oauth/bound";


    /**
     * 用户更换手机号
     */
    public static final String CHGMOBILE = "/user/userpass/chgmobile";


    /**
     * 验证手机验证码
     */
    public static final String VERIFYCODE = "/common/mobile/verifycode";

    /**
     * 验证手机验证码
     */
    public static final String UNBOUND = "/user/userpass/unbound";


    /**
     * 个人中心直接绑定第三放
     */
    public static final String USERPASS_BOUND = "/user/userpass/bound";
    //资产自选数据接口
    public static final String ASSETSELECTION = "http://hq.sinajs.cn";
    //七牛获取token接口
    public static final String QINIU = "/common/cloud/init";

    /**
     * 发现模块
     */
    public static final String FOUND_INDEX = "/activity/find/index";//发现首页
    public static final String FOUND_COMMENT = "/activity/find/comment";//文章评论
    public static final String GET_FOUND_COMMENT = "/activity/find/getcomment";//获取文章评论

    //搜索位置接口
    public static final String SUGGESTION = "/common/place/suggestion";
    //地理位置列表
    public static final String LOCATIONURL = "/common/place/gecoder";
    //同步账本分类数据
    public static final String SYNC_CATEGORY = "/accounting/books/syncCustomCate";
    //账本分享
    public static final String ACCOUNTER_SHARE = "/accounting/share/h5index";
    //我关注的账本
    public static final String ACCOUNTER_SUBSCRIBE = "/accounting/books/mysubscribe";
    //取消我关注的账本
    public static final String CANCLE_ACCOUNTER_SUBSCRIBE = "/accounting/books/cancelsubscribe";
    //日志分享
    public static final String JOURNAL_SHARE = "/accounting/bill/h5detail";

    //评论账本
    public static final String COMMENT_BOOK = "/accounting/bill/comment";
    //删除评论
    public static final String DELETE_COMMENT_BOOK = "/accounting/bill/delComment";

    //账本导出接口
    public static final String BOOKS_EXPORT = "/accounting/books/export";
    //统计接口
    public static final String STATISTICS_BOOK = "/accounting/books/statistics";
    //根据账单分类查询账单
    public static final String GETCATEBILL = "/accounting/bill/getcatebill";
    //账本订阅接口
    public static final String SAVESUBSCRIBE = "/accounting/books/savesubscribe";
    //语音记账语义解析接口
    public static final String ANALYZE_SEGMANTE = "/accounting/index/analyzeSemantics";
    //装修社区首页接口
    public static final String FIND_HOUSE = "/activity/find/house";
    //装修公司列表页
    public static final String COMMPANY_LIST = "/activity/find/companylist";
    //装修公司详情
    public static final String COMMPANY_DETAIL = "/activity/find/companyRelated";
    //查看附近装修账本
    public static final String FIND_NEARBY = "/activity/find/nearby";

    //发现首页
    public static final String FIND_INDEX = "/activity/find/index";
    //发现其他
    public static final String FIND_INDEX_OTHER = "/activity/find/other";
    //房屋地址信息
    public static final String HOUSE_ADDRESS = "/accounting/index/sethouseinfo";

    //旅游首页
    public static final String TRAVEL_INDEX = "/activity/find/travel";
    //旅游目的地
    public static final String TRAVEL_PLACE = "/activity/find/travelPlace";
    //旅游目的地旅游攻略
    public static final String TRAVEL_STRATEGY = "/common/place/travelStrategy";
    //查询房屋信息
    public static final String GETHOUSEINFO = "/accounting/index/gethouseinfo";

    /**
     * 数据埋点 统计事件的ID
     */
    /*****************************
     * 一级点
     **********************************/
    public static final String HIDE_ACCOUNT = "10000";//记账
    public static final String HIDE_PROPERTY = "20000";//资产
    public static final String HIDE_FUND = "30000";//发现
    public static final String HIDE_MINE = "40000";//我的
    /*****************************
     * 二级点
     **********************************/
    public static final String HIDE_ACCOUNT_CHANGE = "11000";//账本切换
    public static final String HIDE_STATICS_ENTER = "12000";//统计入口
    public static final String HIDE_MORE_BTN = "13000";//更多按钮
    public static final String HIDE_ADD_MEMBER = "14000";//添加成员+号
    public static final String HIDE_GROUP_MEMBER = "15000";//群主成员入口
    public static final String HIDE_ACOUNTER_DETAIL = "16000";//账单详情
    public static final String HIDE_MINE_COST = "17000";//我的消费
    public static final String HIDE_TOTAL_COST = "18000";//全员消费
    public static final String HIDE_GET_PAY = "19000";//我应收、我需付
    public static final String HIDE_SET_BUDGET = "1A000";//设置预算
    public static final String HIDE_CURMONTH_GET = "1B000";//本月收入
    public static final String HIDE_CURMONTH_PAY = "1C000";//本月支出
    public static final String HIDE_NOTE_ONE = "1D000";//记一笔按钮
    public static final String HIDE_TIME_LINE = "1E000";//Timeline预设的
    public static final String HIDE_COST_DETAIL = "1F000";//消费详情页
    public static final String HIDE_PHOTO_SEE = "1G000";//图片预览
    public static final String HIDE_PROPERTY_TAB = "21000";//资产Tab
    public static final String HIDE_OPTIONAL_TAB = "22000";//自选Tab
    public static final String HIDE_ADD_PROPERTY = "23000";//添加资产按钮
    public static final String HIDE_HINE_MONEY = "24000";//隐藏金额按钮
    public static final String HIDE_SHOW_MONEY = "25000";//显示金额按钮
    public static final String HIDE_PROPERTY_DETAIL = "26000";//资产详情
    public static final String HIDE_FOUND_SHARE = "31000";//发现分享
    public static final String HIDE_USER_INFO = "41000";//用户信息
    public static final String HIDE_GOOD_COMMENT = "42000";//好评鼓励
    public static final String HIDE_INVITE_FRIEND = "43000";//邀请好友
    public static final String HIDE_NOTE_REMIND = "44000";//记账提醒
    public static final String HIDE_PROBLEM_FADBACK = "45000";//问题反馈
    public static final String HIDE_SETTING = "46000";//我的设置
    public static final String HIDE_WEIXIN_LOGIN = "51000";//微信登录
    public static final String HIDE_QQ_LOGIN = "52000";//QQ登录
    public static final String HIDE_WEIBO_LOGIN = "53000";//微博登录
    public static final String HIDE_PHONE_CODE_LOGIN = "54000";//手机验证码
    public static final String HIDE_ACCOUNT_LOGIN = "55000";//帐号密码登录


}
