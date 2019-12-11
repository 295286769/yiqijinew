package com.yiqiji.money.modules.community.discover.manager;

import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.community.discover.model.DiscoverListModel;
import com.yiqiji.money.modules.community.discover.model.DiscoverOtherListModel;

/**
 * Created by leichi on 2017/7/31.
 */

public class DiscoverListController {

    /**
     *
     * @param viewCallBack
     */
    public  void getDiscoverListData(final ViewCallBack<DiscoverListModel> viewCallBack) {
        DiscoverListManager.getDiscoverListData(new ViewCallBack<DiscoverListModel>() {
            @Override
            public void onSuccess(DiscoverListModel discoverListModel) throws Exception {
                super.onSuccess(discoverListModel);
                viewCallBack.onSuccess(discoverListModel);
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }
        });
    }

    /**
     *
     * @param viewCallBack
     */
    public  void getDiscoverOtherListData(final ViewCallBack<DiscoverOtherListModel> viewCallBack) {
        DiscoverListManager.getDiscoverOtherListData(viewCallBack);
    }

}
