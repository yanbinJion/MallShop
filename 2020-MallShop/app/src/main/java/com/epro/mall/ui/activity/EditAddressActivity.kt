package com.epro.mall.ui.activity;

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.epro.mall.R
import com.epro.mall.mvp.contract.EditAddressContract
import com.epro.mall.mvp.model.bean.AddNewAddressBean
import com.epro.mall.mvp.model.bean.Address
import com.epro.mall.mvp.model.bean.GeolocationBean
import com.epro.mall.mvp.model.bean.UpdateAddressBean
import com.epro.mall.mvp.presenter.EditAddressPresenter
import com.epro.mall.utils.MallConst
import com.mike.baselib.activity.BaseTitleBarCustomActivity
import com.mike.baselib.utils.ValidateUtils
import com.mike.baselib.utils.ext_getFromJsonWithClassKey
import com.mike.baselib.utils.ext_putJsonExtra
import kotlinx.android.synthetic.main.activity_edit_address.*
import com.mike.baselib.utils.toast


/**
 * 编辑或添加地址
 */
class EditAddressActivity : BaseTitleBarCustomActivity<EditAddressContract.View, EditAddressPresenter>(), EditAddressContract.View, View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0) {
            btnConfirm -> {
                val receiver = etReceiver.text.toString().trim()
                var phone = etPhone.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val isDefault = cbDefault.isChecked
                if (TextUtils.isEmpty(receiver)) {
                    toast("收货人不能为空")
                    return
                }
                if (TextUtils.isEmpty(phone)) {
                    toast("手机号不能为空")
                    return
                }
                if (!ValidateUtils.validatePhoneNo(phone)) {
                    toast(getString(R.string.phone_style_error))
                    return
                }
                phone = tvAreaCode.text.toString() + "_" + phone
                if (location == null) {
                    toast("定位地址不能为空")
                    return
                }
                if (TextUtils.isEmpty(address)) {
                    toast("详细地址不能为空")
                    return
                }

//                if (phone.startsWith("+86")) {
//                    phoneNum = "+86_" + phone.substring(3)
//                } else if (phone.startsWith("86")) {
//                    phoneNum = "+86_" + phone.substring(2)
//                } else if (phone.startsWith("+852")) {
//                    phoneNum = "+852_" + phone.substring(4)
//                } else if (phone.startsWith("852")) {
//                    phoneNum = "+852_" + phone.substring(3)
//                } else {
//                    if (phone.startsWith("6") || phone.startsWith("9")) {
//                        phoneNum = "+852_" + phone
//                    } else {
//                        phoneNum = "+86_" + phone
//                    }
//                }
                val statusDefault = if (isDefault) 1 else 0
                val province = location!!.province
                val city = location!!.city
                val area = location!!.area
                val snippet = location!!.title //定位地址
                val lo = location!!.longitude
                val la = location!!.latitude
                if (this.address == null) {
                    mPresenter.addNewAddress(MallConst.ADD_ADDRESS, receiver, province, city, area, snippet, address, phone,
                            statusDefault, lo, la)
                } else {
                    mPresenter.editAddress(MallConst.UPDATE_ADDRESS, this.address!!.id, receiver, province, city, area, snippet, address, phone,
                            statusDefault, lo, la)

                }
            }
            etArea -> {
                GeolocationSelectActivity.launchForReuslt(this, FOR_ADDRESS_RESULT)
            }

            tvAreaCode -> {
                AreaSelectActivity.launchForResult(this, FOR_AREA_CODE)
            }
        }

    }

    companion object {
        private const val FOR_AREA_CODE = 2
        private const val FOR_ADDRESS_RESULT = 3
        fun launchWithAddress(activity: Activity, address: Address?, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, EditAddressActivity::class.java).ext_putJsonExtra(address), requestCode)
        }
    }

    override fun getPresenter(): EditAddressPresenter {
        return EditAddressPresenter()
    }

    /**
     * 编辑地址
     */
    override fun onEditAddressSuccess(result: UpdateAddressBean) {
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * 增加地址
     */
    override fun onAddNewAddressSuccess(result: AddNewAddressBean) {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun layoutContentId(): Int {
        return R.layout.activity_edit_address
    }

    override fun initData() {

    }

    var address: Address? = null
    override fun initView() {
        super.initView()
        address = intent.ext_getFromJsonWithClassKey(Address::class.java)
        if (address == null) {//新增
            getTitleView().text = getString(R.string.add_address_list)
        } else {//修改
            getTitleView().text = getText(R.string.change_address_list)
            etReceiver.setText(address!!.receive)
            etPhone.setText(address!!.mobile.split("_")[1])
            tvAreaCode.text = address!!.mobile.split("_")[0]
            etArea.text = address!!.location
            etAddress.setText(address!!.address)
            cbDefault.isChecked = address!!.isDefult == 1
            location = GeolocationBean(address!!.location, address!!.location, address!!.latitude, address!!.longitude, 0, address!!.area, address!!.city, address!!.province)
            //btnConfirm.text="确认修改"
        }
    }

    override fun initListener() {
        btnConfirm.setOnClickListener(this)
        etArea.setOnClickListener(this)
        tvAreaCode.setOnClickListener(this)
    }

    var location: GeolocationBean? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FOR_AREA_CODE -> {
                    tvAreaCode.text = "+" + data?.getStringExtra("area_code")
                }
                FOR_ADDRESS_RESULT -> {
                    location = data!!.ext_getFromJsonWithClassKey(GeolocationBean::class.java)
                    etArea.setText(location!!.title)
                }
            }
        }
    }
}


