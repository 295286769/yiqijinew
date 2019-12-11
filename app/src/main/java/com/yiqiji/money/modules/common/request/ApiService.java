package com.yiqiji.money.modules.common.request;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.homeModule.home.entity.AddMemberResponse;
import com.yiqiji.money.modules.homeModule.home.entity.BillDetailInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BillSyncInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BooksSettlementListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.BudgetResponse;
import com.yiqiji.money.modules.common.entity.Card;
import com.yiqiji.money.modules.homeModule.home.entity.CheckMessageInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CheckVesionInfo;
import com.yiqiji.money.modules.homeModule.home.entity.CommentList;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.homeModule.home.entity.HasSettementBillInfo;
import com.yiqiji.money.modules.common.entity.Location;
import com.yiqiji.money.modules.common.entity.OuterBooksDbInfo;
import com.yiqiji.money.modules.homeModule.home.entity.QiniuInfo;
import com.yiqiji.money.modules.homeModule.home.entity.SettmentDetailInfo;
import com.yiqiji.money.modules.homeModule.home.entity.ShorturlInfo;
import com.yiqiji.money.modules.common.entity.StatInfo;
import com.yiqiji.money.modules.common.entity.Suggestion;
import com.yiqiji.money.modules.common.entity.UserInfo;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface ApiService {

    //
    // @"plat" : @"ios",
    // 平台
    // @"deviceid": deviceid,
    // 设备唯一标示
    // @"appver" : nowVersion,
    // app现在版本
    // @"osver" : osver,
    // 系统版本 5.2.0 4.4
    // @"machine" : machine 手机类型 （小米魅族）

    /**
     * 发送短信验证码
     */
    @POST(Constants.SEND_MSG_COD)
    Call<ResponseBody> sendMsgCode(@QueryMap Map<String, String> map);

    /**
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    @POST(Constants.USER_REG_VERIFYMCODE)
    Call<ResponseBody> SignIn(@QueryMap Map<String, String> map);

    /**
     * 设置密码
     */
    @POST(Constants.SET_PASSWORD)
    Call<ResponseBody> setLoginPwd(@QueryMap Map<String, String> map);

    /**
     * 账号，密码登录
     */
    @POST(Constants.LOGIN_BY_USER_NAME_PWD)
    Call<ResponseBody> loginByUserNameAndPwd(@QueryMap Map<String, String> map);

    /**
     * 短信验证码找回密码需要两步执行1:输入 mobile mtokenid mcode获得 tokenid user/userpass/reset
     * 手机验证码获得tokenid
     *
     * @param
     * @param
     * @param
     * @return
     */

    @POST(Constants.LOGIN_BY_MESSAGE_CODE)
    Call<ResponseBody> loginByMessageCode(@QueryMap Map<String, String> map);

    @POST(Constants.VERIFY_BY_MESSAGE_CODE)
    Call<ResponseBody> verifyByMessageCode(@QueryMap Map<String, String> map);

    /**
     * 2、根据返回tokenid和密码 调用
     *
     * @param
     * @param
     * @param
     * @return
     */
    @GET(Constants.RESET_PWD)
    Call<ResponseBody> resetPwdBecauseLost(@QueryMap Map<String, String> map);

    /**
     * 通过原密码重设密码
     */
    @POST(Constants.RESET_PWD_BY_OLD_PWD)
    Call<ResponseBody> resetPwdByOldPwd(@QueryMap Map<String, String> map);

    /**
     * 设置用户名
     */
    @POST(Constants.USERPASS_SET_USERNAME)
    Call<ResponseBody> setUserName(@QueryMap Map<String, String> map);

    /**
     * 获取用户信息
     */
    @POST(Constants.GET_USER_INFO)
    Call<UserInfo> getUserInfo(@QueryMap Map<String, String> map);

    /**
     * 设置用户头像
     */

    @Multipart
    @POST(Constants.SET_USER_ICON)
    Call<ResponseBody> setUserIcon(@PartMap Map<String, RequestBody> requestBodys);

    /**
     * 统计图片上传
     */

    @Multipart
    @POST(Constants.COMMON_IMAGE)
    Call<StatInfo> setImageUp(@PartMap Map<String, RequestBody> requestBodys);

    /**
     * 问题反馈feedbacksave
     */
    @POST(Constants.FEEDBACK_SAVE)
    Call<ResponseBody> feedBackSave(@QueryMap Map<String, String> map);

    /**
     * 消息列表
     */
    @POST(Constants.MESSAGE_INDEX)
    Call<ResponseBody> messageIndex(@QueryMap Map<String, String> hashMap);

    /**
     * 消息列表
     */
    @POST(Constants.MESSAGE_READ)
    Call<ResponseBody> messageRead(@QueryMap Map<String, String> hashMap);

    /**
     * 检测是否有消息
     */
    @POST(Constants.CHECK_MESSAGE)
    Call<CheckMessageInfo> checkMessage(@QueryMap Map<String, String> hashMap);

    /**
     * 账本列表
     */
    @POST(Constants.HOME_LIST)
    Call<ResponseBody> getHomeList(@QueryMap Map<String, String> hashMap);

    /**
     * 账本列表
     */
    @POST(Constants.HOME_LIST)
    Call<BooksListInfo> getBooksList(@QueryMap Map<String, String> hashMap);

    /**
     * 账本结算列表
     */
    @POST(Constants.BOOK_SETTLEMENT_LIST)
    Call<BooksSettlementListInfo> getBookSettelenmentList(@QueryMap Map<String, String> hashMap);

    /**
     * 账本结算详情
     */
    @POST(Constants.BOOK_CLEAR_DETAIL)
    Call<BooksSettlementListInfo> getBookSettelenmentClearDetail(@QueryMap Map<String, String> hashMap);

    /**
     * 账本结算接口
     */
    @POST(Constants.BOOK_CLEAR)
    Call<AddMemberResponse> getBookSettelenmentClear(@QueryMap Map<String, String> hashMap);

    /**
     * 账本详情
     */
    @POST(Constants.BOOKS_DETAIL)
    Call<BooksDbInfo> getBooksDetail(@QueryMap Map<String, String> hashMap);

    /**
     * 账本编辑
     */
    @POST(Constants.BOOKS_EDIT)
    Call<ResponseBody> onBookEdit(@QueryMap Map<String, String> hashMap);

    /**
     * 账本删除 退出
     */
    @POST(Constants.BOOKS_DEL)
    Call<ResponseBody> onBookDel(@QueryMap Map<String, String> hashMap);

    /**
     * 添加账本
     */
    @POST(Constants.ADD_BOOK)
    Call<ResponseBody> addBook(@QueryMap Map<String, String> hashMap);

    /**
     * 添加账本hashmap
     */
    @POST(Constants.ADD_BOOK)
    Call<OuterBooksDbInfo> addBookMap(@QueryMap Map<String, String> map);

    /**
     * 添加成员
     */
    @POST(Constants.ADD_MEMBER)
    Call<AddMemberResponse> addMember(@QueryMap Map<String, String> map);

    /**
     * 修改成员名片么
     */
    @POST(Constants.CHANG_MEMBER_NAME)
    Call<AddMemberResponse> chang_memeber_name(@QueryMap Map<String, String> map);

    /**
     * 删除成员
     */
    @POST(Constants.DELET_MEMBER_NAME)
    Call<AddMemberResponse> delet_memeber_name(@QueryMap Map<String, String> map);

    /**
     * 退出账本
     */
    @POST(Constants.QUIT_MEMBER_NAME)
    Call<AddMemberResponse> quit_memeber_name(@QueryMap Map<String, String> map);

    /**
     * 邀请
     */
    @POST(Constants.INVITATION)
    Call<AddMemberResponse> invitation(@QueryMap Map<String, String> map);

    /**
     * 获取已删除账单
     */
    @POST(Constants.BILL_IS_DELET)
    Call<HasSettementBillInfo> bill_is_delet(@QueryMap Map<String, String> map);

    /**
     * 获取已结算账单
     */
    @POST(Constants.CLEARED_BILL)
    Call<HasSettementBillInfo> cleared_bill(@QueryMap Map<String, String> map);

    /**
     * 评论列表
     */
    @POST(Constants.GETCOMMENT)
    Call<CommentList> bill_list(@QueryMap Map<String, String> map);

    /**
     * 评论列表
     */
    @POST(Constants.COMMENT)
    Call<AddMemberResponse> comment(@QueryMap Map<String, String> map);

    /**
     * 获取账本所属分类列表
     */
    @POST(Constants.BOOKS_CATE)
    Call<ResponseBody> getBookCate(@QueryMap Map<String, String> map);

    /**
     * 账单列表
     */
    @POST(Constants.BOOKS_BILL)
    Call<BillListInfo> getBookBill(@QueryMap Map<String, String> map);

    /**
     * 结算明细
     */
    @POST(Constants.GETCLEARINFO)
    Call<SettmentDetailInfo> getClearInfo(@QueryMap Map<String, String> map);

    /**
     * 账单详情
     */
    @POST(Constants.BILLDETAIL)
    Call<BillDetailInfo> getBillDetail(@QueryMap Map<String, String> map);

    /**
     * 同步账单数据
     */
    @POST(Constants.SYNC_BOOK)
    Call<BillSyncInfo> sync_book(@QueryMap HashMap<String, String> map, @Body RequestBody body);

    /**
     * 设置预算
     */

    @POST(Constants.BUDGET)
    Call<BudgetResponse> budget(@QueryMap HashMap<String, String> hashMap);

    /**
     * 版本更新
     *
     * @return
     */
    @POST(Constants.CHECK_VESION)
    Call<CheckVesionInfo> getCheckVesion(@QueryMap HashMap<String, String> hashMap);

    /**
     * 生成短连接
     *
     * @return
     */
    @POST(Constants.GENSHORTURL)
    Call<ShorturlInfo> getShorturl(@QueryMap HashMap<String, String> hashMap);


    /**
     * 银行卡列表
     *
     * @return
     */
    @POST(Constants.BANK)
    Call<Card> getBankList(@QueryMap HashMap<String, String> hashMap);


    /**
     * 资产首页
     */
    @POST(Constants.ASSERT)
    Call<ResponseBody> getPropertyData(@QueryMap HashMap<String, String> hashMap);

    /**
     * 自选详细页
     */
    @GET("/list")
    Call<ResponseBody> getOptionDetail(@Query("list") String url);

    /**
     * 添加资产
     */
    @POST(Constants.ADD_ASSERT)
    Call<ResponseBody> getAddProperty(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加信用卡
     */
    @POST(Constants.ADD_CREDIT_ASSERT)
    Call<ResponseBody> getAddCreditCar(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加基金
     */
    @POST(Constants.ADD_FUND_ASSERT)
    Call<ResponseBody> getAddFund(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加添加股票
     */
    @POST(Constants.ADD_STOCK_ASSERT)
    Call<ResponseBody> getAddStock(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加网络理财
     */
    @POST(Constants.ADD_FINANC_ASSERT)
    Call<ResponseBody> getAddFinan(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加借款资产
     */
    @POST(Constants.ADD_LOAN_ASSERT)
    Call<ResponseBody> getAddBorrow(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加完结资产
     */
    @POST(Constants.ADD_FINISH_ASSERT)
    Call<ResponseBody> getAddFinish(@QueryMap HashMap<String, String> hashMap);

    /**
     * 添加删除资产
     */
    @POST(Constants.ADD_DELE_ASSERT)
    Call<ResponseBody> getAddDele(@QueryMap HashMap<String, String> hashMap);

    /**
     * 获取资产分类接口
     */
    @POST(Constants.ADD_CATE_ASSERT)
    Call<ResponseBody> getAddCate(@QueryMap HashMap<String, String> hashMap);

    /**
     * 更新()
     */
    @POST(Constants.ADD_RENEW_ASSERT)
    Call<ResponseBody> getRenew(@QueryMap HashMap<String, String> hashMap);

    /**
     * 更新(信用卡)
     */
    @POST(Constants.ADD_RENEW_CREDIT)
    Call<ResponseBody> getRenewCredit(@QueryMap HashMap<String, String> hashMap);

    /**
     * 更新(基金)
     */
    @POST(Constants.ADD_RENEW_FUND)
    Call<ResponseBody> getRenewFund(@QueryMap HashMap<String, String> hashMap);

    /**
     * 更新(股票)
     */
    @POST(Constants.ADD_RENEW_STOCK)
    Call<ResponseBody> getRenewStock(@QueryMap HashMap<String, String> hashMap);


    /**
     * 更新(网络理财资产)
     */
    @POST(Constants.ADD_RENEW_FINAN)
    Call<ResponseBody> getRenewFinan(@QueryMap HashMap<String, String> hashMap);

    /**
     * 更新(借款)
     */
    @POST(Constants.ADD_RENEW_LOAN)
    Call<ResponseBody> getRenewLoan(@QueryMap HashMap<String, String> hashMap);

    /**
     * 获取银行列表
     */
    @POST(Constants.GET_BANK_LIST)
    Call<ResponseBody> getBanks(@QueryMap HashMap<String, String> hashMap);

    /**
     * 获取资产详情页
     */
    @POST(Constants.ASSERT_DETAIL)
    Call<ResponseBody> getAssertDetail(@QueryMap HashMap<String, String> hashMap);

    /**
     * 股票基金搜索接口
     */
    @POST(Constants.SEARCH_STOCKFUND)
    Call<ResponseBody> getSearchFundAndStock(@QueryMap HashMap<String, String> hashMap);

    /**
     * 获取转账资金账户接口
     */
    @POST(Constants.TRANSLATE_ASSERT_ACOUNT)
    Call<ResponseBody> getTranslateAcount(@QueryMap HashMap<String, String> hashMap);

    /**
     * 资金转账接口
     */
    @POST(Constants.TRANSLATE_MONEY)
    Call<ResponseBody> getTranslateMoney(@QueryMap HashMap<String, String> hashMap);

    /**
     * 验证手机号是否注册
     */
    @POST(Constants.CHECKMOBILE)
    Call<ResponseBody> onCheckmobile(@QueryMap HashMap<String, String> hashMap);

    /**
     * 验证手机号是否注册
     */
    @POST(Constants.AUTHCHECK)
    Call<ResponseBody> onCheck(@QueryMap HashMap<String, String> hashMap);

    /**
     * 第三方登录授权绑定接口
     */
    @POST(Constants.OAUTHBOUND)
    Call<ResponseBody> onOauthBound(@QueryMap HashMap<String, String> hashMap);

    /**
     * 用户更换手机号
     */
    @POST(Constants.CHGMOBILE)
    Call<ResponseBody> onChaMobile(@QueryMap HashMap<String, String> hashMap);

    /**
     * 验证手机验证码
     */
    @POST(Constants.VERIFYCODE)
    Call<ResponseBody> onVerifycode(@QueryMap HashMap<String, String> hashMap);


    /**
     * 第三方账号解绑（个人中心使用）
     */

    @POST(Constants.UNBOUND)
    Call<ResponseBody> onUnbound(@QueryMap HashMap<String, String> hashMap);


    /**
     * 个人中心直接绑定
     */
    @POST(Constants.USERPASS_BOUND)
    Call<ResponseBody> onUserpassBound(@QueryMap HashMap<String, String> hashMap);

    /**
     * GET 资产自选数据接口
     */
    @GET(Constants.ASSETSELECTION)
    Call<ResponseBody> onUser(@Part("list") String list);

    /**
     * 七牛上传图片
     */
    @POST(Constants.QINIU)
    Call<QiniuInfo> getQiniuToken(@QueryMap HashMap<String, String> hashMap);

    /**
     * 获取地理位置列表
     */
    @POST(Constants.LOCATIONURL)
    Call<Location> getLocation(@QueryMap HashMap<String, String> hashMap);


    /**
     * 搜索位置接口
     */
    @POST(Constants.SUGGESTION)
    Call<Suggestion> getSuggestionn(@QueryMap HashMap<String, String> hashMap);
}
