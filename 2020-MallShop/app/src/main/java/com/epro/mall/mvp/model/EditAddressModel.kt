package com.epro.mall.mvp.model

import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.AddNewAddressBean
import com.epro.mall.mvp.model.bean.UpdateAddressBean
import io.reactivex.Observable

class EditAddressModel : BaseModel() {
    fun editAddress(id:String,receive:String,province:String, city:String, area:String,
                    location:String,address:String, mobile:String,isDefult:Int,longitude:String,latitude:String): Observable<UpdateAddressBean> {
        return getApiSevice().editAddress(UpdateAddressBean.Send(id,receive,province,city,area,location,address,mobile,isDefult,longitude,latitude))
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<UpdateAddressBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    fun addNewAddress(receive:String,province:String, city:String, area:String,location:String,address:String, mobile:String,isDefult:Int,longitude:String,latitude:String):Observable<AddNewAddressBean>{
        return getApiSevice().addNewAddress(AddNewAddressBean.Send(receive, province, city,  area, location, address, mobile, isDefult, longitude, latitude))
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS){
                        return@flatMap Observable.just(it)
                    }else{
                        return@flatMap  Observable.error<AddNewAddressBean>(ApiException(it.message,it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }


}