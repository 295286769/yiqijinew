package com.yiqiji.money.modules.homeModule.home.entity;

import android.text.TextUtils;

import com.yiqiji.money.R;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.common.utils.XzbUtils;

import java.util.Date;

/**
 * Created by ${huangweishui} on 2017/5/17.
 * address huang.weishui@71dai.com
 */
public class HomePresenter {
    public ShowbedugtIncomExpense showbedugtIncomExpense;

    public HomePresenter(ShowbedugtIncomExpense showbedugtIncomExpense) {
        this.showbedugtIncomExpense = showbedugtIncomExpense;
    }

    /**
     * 单人不需要结算
     */
    public void initSingTitle(BooksDbInfo booksDbInfo, Date date_time) {
        if (booksDbInfo != null) {
            String mBudgetdiff = booksDbInfo.getBudgetdiff();
            String mReceivable = booksDbInfo.getReceivable();
            String mPayamount = booksDbInfo.getPayamount();
            String mMyspent = booksDbInfo.getSpentdiff();
            String mMyspentdiff = booksDbInfo.getMyspentdiff();
            if (TextUtils.isEmpty(mBudgetdiff)) {
                mBudgetdiff = "0.00";
            }
            if (TextUtils.isEmpty(mReceivable)) {
                mReceivable = "0.00";
            }
            if (TextUtils.isEmpty(mPayamount)) {
                mReceivable = "0.00";
            }
            if (TextUtils.isEmpty(mMyspent)) {
                mMyspent = "0.00";
            }
            if (TextUtils.isEmpty(mMyspentdiff)) {
                mMyspentdiff = "0.00";
            }

            String budget_content = "设置预算";
            String incom_content = "本月收入";
            String expenditure_content = "本月支出";
            String yusuann_money = "";
            String incom_money = "";
            String expenditure_money = "";
            boolean showBudgetImage = true;
            int yusuann_content_size = R.dimen.text_13;
            int expenditure_content_color = R.color.white;
            int expenditure_content_bagroud = 0;
            int setgutIndex = DateUtil.getSwichMoth(date_time);
            String moth = DateUtil.getMoth(date_time);
            String budgetkey = DateUtil.getBudgetkey(booksDbInfo.getAccountbookid(), DateUtil.getYearMoth(date_time), booksDbInfo.getSorttype());
            String locationBudgetdiff = LoginConfig.getInstance().getBudget(budgetkey);
            if (booksDbInfo.getIsclear().equals("1")) {
                showBudgetImage = false;
                budget_content = "我的消费";
                incom_content = "全员消费";
                expenditure_content = "我需付";
                yusuann_content_size = R.dimen.text_16;

                mBudgetdiff = booksDbInfo.getMyspent();

                if (TextUtils.isEmpty(mBudgetdiff)) {
                    mBudgetdiff = "0.00";
                }

//                yusuann_money = StringUtils.moneySplitComma(mBudgetdiff);
                yusuann_money = XzbUtils.getBalanceOther(Double.parseDouble(mBudgetdiff));
                if (mMyspent.contains("-")) {
                    mMyspent = mMyspent.replace("-", "");
                }
//                incom_money = StringUtils.moneySplitComma(mMyspent);
                incom_money = XzbUtils.getBalanceOther(Double.parseDouble(mMyspent));
                float need_money = mMyspentdiff.equals("0.00") ? 0.00f : Float
                        .parseFloat(mMyspentdiff);

                if (need_money > 0) {

                    expenditure_content = "我应收";
                    expenditure_content_bagroud = R.drawable.need_pay_bagroud_green;
                } else if (need_money < 0) {
                    need_money = Math.abs(need_money);
                    expenditure_content = "我需付";
                    expenditure_content_bagroud = R.drawable.need_pay_bagroun_red;
                } else {
                    expenditure_content = "已结清";
                    expenditure_content_bagroud = R.drawable.need_pay_bagroud_green;
                }
//                expenditure_money = StringUtils.moneySplitComma(mMyspentdiff);
                expenditure_money = XzbUtils.getBalanceOther(Double.parseDouble(mMyspentdiff));
            } else {
                if (booksDbInfo.getSorttype().equals("0")) {// 没有时间
                    budget_content = "设置预算";
                    incom_content = "累计收入";
                    expenditure_content = "累计支出";
                } else {
                    switch (setgutIndex) {
                        case 0://前面的时间
                            budget_content = moth + "月预算";
                            incom_content = moth + "月收入";
                            expenditure_content = moth + "月支出";
                            break;
                        case 1://当前时间
                            budget_content = "本月预算";
                            incom_content = "本月收入";
                            expenditure_content = "本月支出";
                            break;
                        case 2://预期时间
                            budget_content = "无预算";
                            incom_content = "预设收入";
                            expenditure_content = "预设支出";
                            break;
                    }
                }

                if (TextUtils.isEmpty(mBudgetdiff) || locationBudgetdiff.equals("0")) {
                    yusuann_money = "设置预算";
                    showBudgetImage = true;
                    yusuann_content_size = R.dimen.text_13;
                } else {
                    if (Float.parseFloat(mBudgetdiff) < 0) {
                        switch (setgutIndex) {
                            case 0://前面的时间
                                budget_content = moth + "月预算超支";

                                break;
                            case 1://当前时间
                                if (booksDbInfo.getAccountbookcate().equals("10")) {
                                    budget_content = "预算超支";
                                } else {
                                    budget_content = "本月预算超支";
                                }
                                break;
                            case 2://预期时间
                                budget_content = "无预算";

                                break;
                        }
                        if (booksDbInfo.getAccountbookcate().equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE) || booksDbInfo.getAccountbookcate().equals(RequsterTag.CARACCOUNTBOOKCATE)) {
                            budget_content = "预算超支:";
                        }

                    } else {
                        switch (setgutIndex) {
                            case 0://前面的时间
                                budget_content = moth + "月预算剩余";
                                break;
                            case 1://当前时间
                                if (booksDbInfo.getAccountbookcate().equals("10")) {
                                    budget_content = "预算剩余";
                                } else {
                                    budget_content = "本月预算剩余";
                                }
                                break;
                            case 2://预期时间
                                budget_content = "无预算";
                                break;
                        }
                        if (booksDbInfo.getAccountbookcate().equals(RequsterTag.RENOVATIONACCOUNTBOOKCATE) || booksDbInfo.getAccountbookcate().equals(RequsterTag.CARACCOUNTBOOKCATE)) {
                            budget_content = "预算剩余:";
                        }
                    }
//                    yusuann_money = StringUtils.moneySplitComma(mBudgetdiff);
                    yusuann_money = XzbUtils.getBalanceOther(Double.parseDouble(mBudgetdiff));
                    yusuann_content_size = R.dimen.text_16;
                    showBudgetImage = false;
                }
//                incom_money = StringUtils.moneySplitComma(mReceivable);
                incom_money = XzbUtils.getBalanceOther(Double.parseDouble(mReceivable));
//                expenditure_money = StringUtils.moneySplitComma(mPayamount);
                expenditure_money = XzbUtils.getBalanceOther(Double.parseDouble(mPayamount));
                String mAccountbookcate = booksDbInfo.getAccountbookcate();
                if ((booksDbInfo.getAccountbookcatename() != null && booksDbInfo.getAccountbookcatename().equals("装修账本")) || mAccountbookcate.equals(RequsterTag.ACCOUNTBOOKCATE) || mAccountbookcate.equals(RequsterTag.CARACCOUNTBOOKCATE)) {
                    incom_content = "我的消费";
                    expenditure_content = "全员消费";
                    incom_money = XzbUtils.getBalanceOther(Math.abs(Double.parseDouble(mMyspent)));
                }
                if (booksDbInfo.getAccountbookcatename() != null && booksDbInfo.getAccountbookcatename().equals("结婚账本")) {
                    incom_content = "结婚收入";
                    expenditure_content = "结婚支出";
                }
            }
            showbedugtIncomExpense.showbedugtIncomExpense(budget_content, incom_content, expenditure_content, yusuann_money, incom_money, expenditure_money, yusuann_content_size, expenditure_content_bagroud, showBudgetImage);
        }

    }

    public interface ShowbedugtIncomExpense {

        public void showbedugtIncomExpense(String budget_content, String incom_content, String expenditure_content, String yusuann_money,
                                           String incom_money, String expenditure_money, int yusuann_content_size, int expenditure_content_bagroud, boolean showBudgetImage);
    }
}
