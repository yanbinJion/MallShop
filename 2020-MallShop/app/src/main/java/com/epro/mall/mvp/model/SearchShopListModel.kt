package com.epro.mall.mvp.model

import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.SearchShopListBean
import io.reactivex.Observable

class SearchShopListModel : BaseModel() {
    fun searchShopList( keyword:String, longitude:String,  latitude:String, page:Int): Observable<SearchShopListBean> {
        val p=SearchShopListBean.PageList(page)
        val send=SearchShopListBean.Send(keyword,longitude,latitude,p)
        return getApiSevice().searchShopList(send)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<SearchShopListBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

}