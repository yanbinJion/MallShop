package com.epro.mall.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.epro.mall.R
import com.epro.mall.mvp.model.bean.ScanCodeOrderListBean
import com.epro.mall.mvp.model.bean.ScanCodeOrderListDetailBean
import com.epro.mall.mvp.model.bean.ScanCodeOrderListOneBean
import com.mike.baselib.utils.AppBusManager
import com.mike.baselib.utils.ext_formatAmount
import com.mike.baselib.utils.ext_formatAmountWithUnit
import com.mike.baselib.utils.ext_setTextWithHorizontalLine
import com.mike.baselib.view.recyclerview.ViewHolder
import com.mike.baselib.view.recyclerview.adapter.CommonAdapter

class ScanCodeOrderDetailAdapter (mContext: Context, itemList: ArrayList<ScanCodeOrderListDetailBean.ScanCodeProductsBean>, layoutId: Int= R.layout.scan_code_order_detail_item) :
        CommonAdapter<ScanCodeOrderListDetailBean.ScanCodeProductsBean>(mContext, itemList, layoutId){

     init {
         logTools.t("YB").d("init  mdata: "+mData)
     }
    override fun bindData(holder: ViewHolder, data: ScanCodeOrderListDetailBean.ScanCodeProductsBean, position: Int) {
        logTools.t("YB").d("bindData : $data"+" mdata: "+mData)
        holder.setText(R.id.name,data.goodsName)
        holder.setText(R.id.price,data.salePrice.ext_formatAmountWithUnit())
        holder.setText(R.id.shopCount,"x"+data.productCount)

      val tvOriginalPrice= holder.getView<TextView>(R.id.tvOriginalPrice)
        if( data.discount==1F || TextUtils.isEmpty(data.discountPrice)){
            tvOriginalPrice.visibility= View.GONE
        }else{
            tvOriginalPrice.visibility=View.VISIBLE
            holder.setText(R.id.price,data.discountPrice.ext_formatAmount())
            tvOriginalPrice.ext_setTextWithHorizontalLine(AppBusManager.getAmountUnit()+" "+data.salePrice.ext_formatAmount())
        }

    }


}