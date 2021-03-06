package com.epro.mall.mvp.model

import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.City
import com.epro.mall.mvp.model.bean.GetCityListBean
import com.epro.mall.mvp.model.bean.GetResultListBean
import com.epro.mall.mvp.model.cn.CNPinyin
import com.epro.mall.mvp.model.cn.CNPinyinFactory
import com.google.gson.reflect.TypeToken
import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.utils.AppBusManager
import com.mike.baselib.utils.AppContext
import com.mike.baselib.utils.AppUtils
import io.reactivex.Observable
import kotlin.collections.ArrayList

class SelectCityModel : BaseModel() {

    fun getCities(): Observable<GetResultListBean<CNPinyin<City>>> {
        return Observable.just(true).flatMap {
            val citys = ArrayList<CNPinyin<City>>()
            val cityJson = AppUtils.assetsFileToString(AppContext.getInstance().context, "_city1.json")
            //logTools.d(cityJson)
            val type = object : TypeToken<ArrayList<City>>() {}.type
            val cityList = AppBusManager.fromJson<ArrayList<City>>(cityJson, type)
            if (cityList != null && cityList.isNotEmpty()) {
                val cnps = CNPinyinFactory.createCNPinyinList(cityList)
                citys.addAll(cnps)
                logTools.toJson(citys[0])
                logTools.d(citys[0].pinyinStr())
            }
            if (citys.isEmpty()) {
                return@flatMap Observable.error<GetResultListBean<CNPinyin<City>>>(ApiException("解析错误", ErrorStatus.UNKNOWN_ERROR))
            } else {
                citys.sort()
                return@flatMap Observable.just(GetResultListBean(ErrorStatus.SUCCESS, ErrorStatus.SUCCESS_MSG, citys))
            }
        }.compose(SchedulerUtils.ioToMain())
    }

    fun getCityList(): Observable<GetCityListBean> {
        return getApiSevice().getCityList(0)
                .flatMap {
                    if(it.code== ErrorStatus.SUCCESS){
                        return@flatMap  Observable.just(it)
                    }else{
                        return@flatMap Observable.error<GetCityListBean>(ApiException(it.message,it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

}