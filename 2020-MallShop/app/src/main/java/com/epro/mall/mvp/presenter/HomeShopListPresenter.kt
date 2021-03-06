package com.epro.mall.mvp.presenter

import com.epro.mall.mvp.contract.HomeShopListContract
import com.epro.mall.mvp.model.HomeShopListModel
import com.epro.mall.mvp.model.bean.HomeShop
import com.epro.mall.utils.MallConst
import com.mike.baselib.base.ListPresenter
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.net.exception.ExceptionHandle

class HomeShopListPresenter : ListPresenter<HomeShop, HomeShopListContract.View>(), HomeShopListContract.Presenter {
    fun getBannerList(bannerType: Int,isCache:Boolean=true, type: String=MallConst.GET_HOME_BANNER_LIST) {
        checkViewAttached()
        mRootView?.showLoading(type)
        val disposable = model.getHomeBannerList(bannerType,isCache)
                .subscribe({ bean ->
                    mRootView?.onGetBannerListSuccess(bean.result)
                    mRootView?.dismissLoading(ErrorStatus.SUCCESS_MSG, ErrorStatus.SUCCESS, type)
                }, { throwable ->
                    //处理异常
                    ExceptionHandle.handleException(throwable)
                    mRootView?.showError(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                    mRootView?.dismissLoading(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                })
        addSubscription(disposable)
    }

    private val model by lazy { HomeShopListModel() }
    fun getHomeShopList(latitude: String, longitude: String,isCache:Boolean=true, type: String=MallConst.GET_HOME_SHOP_LIST) {
        checkViewAttached()
        mRootView?.showLoading(type)
        val disposable = model.getHomeShopList(latitude, longitude,isCache)
                .subscribe({ bean ->
                    mRootView?.getListDataSuccess(bean.result, type)
                    mRootView?.dismissLoading(ErrorStatus.SUCCESS_MSG, ErrorStatus.SUCCESS, type)
                }, { throwable ->
                    //处理异常
                    ExceptionHandle.handleException(throwable)
                    mRootView?.showError(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                    mRootView?.dismissLoading(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                })
        addSubscription(disposable)
    }


}
