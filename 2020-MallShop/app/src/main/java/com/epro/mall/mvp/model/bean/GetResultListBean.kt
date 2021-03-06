package com.epro.mall.mvp.model.bean


data class GetResultListBean<T>(override val code: Int, override val message: String, override val result: ArrayList<T>) : BaseBean<ArrayList<T>> {
 data class ShopGoodsCategorySend(val shopId:String)
 data class GetHomeShopListSend(val latitude:String,val longitude:String)
}