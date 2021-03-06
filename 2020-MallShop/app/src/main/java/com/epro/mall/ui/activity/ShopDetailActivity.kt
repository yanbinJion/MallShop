package com.epro.mall.ui.activity;

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.epro.mall.R
import com.epro.mall.mvp.contract.ShopDetailContract
import com.epro.mall.mvp.model.bean.GetShopHomeViewBean
import com.epro.mall.mvp.model.bean.ShopInfo
import com.epro.mall.mvp.presenter.ShopDetailPresenter
import com.epro.mall.ui.MainActivity
import com.epro.mall.ui.fragment.ShopNewGoodsListFragment
import com.epro.mall.ui.fragment.ShopCategoryFragment
import com.epro.mall.ui.fragment.ShopHomeFragment
import com.epro.mall.utils.MallConst
import com.flyco.tablayout.listener.OnTabSelectListener
import com.mike.baselib.activity.BaseTitleBarCustomActivity
import com.mike.baselib.utils.StatusBarUtil
import com.mike.baselib.utils.ext_loadCircleImageWithDomain
import com.mike.baselib.utils.ext_loadImage
import com.mike.baselib.utils.ext_loadImageWithDomain
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.activity_shop_detail.ivBack
import com.mike.baselib.utils.toast
import com.mike.baselib.utils.toast
import org.jetbrains.anko.support.v4.viewPager
import java.util.*

/**
 * 店铺详情页面
 */
class ShopDetailActivity : BaseTitleBarCustomActivity<ShopDetailContract.View, ShopDetailPresenter>(), ShopDetailContract.View, View.OnClickListener {

    override fun onGetShopHomeViewSuccess(result: GetShopHomeViewBean.Result) {
        ivShopLogo.ext_loadCircleImageWithDomain(result.shopLogo)
        tvShopName.text = result.shopName
        shopId = result.shopId
        shopName = result.shopName
        tvShopName.postDelayed(Runnable {
            val fragment = mFragments[0] as ShopHomeFragment
            fragment.updateData(result.goodsHotsList, result.homeRecommendList)
        },100)
    }

    override fun onFollowShopSuccess() {
        toast(getString(R.string.followed))
    }

    override fun onClick(p0: View?) {
        when (p0) {
            etSearch -> {
                val shopId = intent.getStringExtra(SHOP_ID)
                SearchActivity.launchWithShopId_Keyword_Location(this, shopId!!,null,null)
                //toast("功能开发中,敬请期待")
            }
            ivSearch -> {
                val shopId = intent.getStringExtra(SHOP_ID)
                SearchActivity.launchWithShopId_Keyword_Location(this, shopId!!,null,null)
                //toast("功能开发中,敬请期待")
            }
            rlShop -> {
                val shopId = intent.getStringExtra(SHOP_ID)
                ShopInfoActivity.launchWithShopId(this, shopId!!)
            }
            ivBack -> {
                finish()
            }
            ivHome -> {
                MainActivity.launchMain(this, 0)
            }
            ivCart -> {
                //MainActivity.launchMain(this, 2)
                CartActivity.launch(this)
            }
            llFollow -> {//关注
//                val shopId = intent.getStringExtra(SHOP_ID)
//                mPresenter.followShop(shopId,MallConst.FOLLOW_SHOP)
            }
            btnScanBuy -> { //扫码购物
                if (shopId.isEmpty()) {
                    toast("shopId is null")
                    return
                }
                ScanBarPurchaseActivity.launchWithShopId_ShopName(this, shopId, shopName)
            }
        }
    }

    override fun initStatusBar() {
        StatusBarUtil.immersive(this, resources.getColor(R.color.white), 0f)
    }

    companion object {
        const val SHOP_ID = "shopId"
        fun launch(context: Context) {
            launchWithShopId(context, "")
        }

        fun launchWithShopId(context: Context, shopId: String) {
            context.startActivity(Intent(context, ShopDetailActivity::class.java).putExtra(SHOP_ID, shopId))
        }
    }

    override fun getPresenter(): ShopDetailPresenter {
        return ShopDetailPresenter()
    }

    override fun onGetShopInfoSuccess(shopInfo: ShopInfo) {

    }

    override fun layoutContentId(): Int {
        return R.layout.activity_shop_detail
    }

    override fun initData() {
        // mPresenter.getShopInfo("",MallConst.GET_SHOP_INFO)
    }

    private val mFragments = ArrayList<Fragment>()
    private val mTitles = arrayOf(R.string.home, R.string.new_product, R.string.product_classification)
    // private val mTitles = arrayOf("首页","分类")
    private var mAdapter: MyPagerAdapter? = null
    var shopId: String = ""
    var shopName: String = ""
    override fun initView() {
        super.initView()
        val shopId = intent.getStringExtra(SHOP_ID)
        getTitleBar().visibility = View.GONE
        mFragments.add(ShopHomeFragment.newInstance(shopId!!).setViewPageFragment(true))
        mFragments.add(ShopNewGoodsListFragment.newInstance(shopId).setViewPageFragment(true))
        mFragments.add(ShopCategoryFragment.newInstance(shopId).setViewPageFragment(true))
        mAdapter = MyPagerAdapter(supportFragmentManager)
        vpGoods.adapter = mAdapter
        tlGoods.setViewPager(vpGoods)
        mPresenter.getShopHomeView(shopId, MallConst.GET_SHOP_HOME_VIEW)
        vpGoods.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 0) {
                    mPresenter.getShopHomeView(shopId, MallConst.GET_SHOP_HOME_VIEW)
                }
            }
        })
    }

    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getString(mTitles[position])
        }

        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }
    }


    override fun initListener() {
        etSearch.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        rlShop.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivHome.setOnClickListener(this)
        ivCart.setOnClickListener(this)
        llFollow.setOnClickListener(this)
        btnScanBuy.setOnClickListener(this)
    }

}


