package com.mike.baselib.utils

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃   神兽保佑
//    ┃　　　┃   代码无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * Created by xuhao on 2017/11/27.
 * desc: 常量
 */
class Constants private constructor() {

    companion object {

        //腾讯 Bugly APP id
        val BUGLY_APPID = "2649a1f58b"


        // 状态.0待付款,1已付款，2已取消，3已过期

        const val DV_TEST = 0//测试环境
        const val DV_PRE_RELEASE = 1 //预发布环境
        const val DV_RELEASE = 2 // 发布环境

        const val BASE_URL_TEST = "http://139.9.7.209"
        const val BASE_URL_PRE_RELEASE = "http://139.9.7.209"
        const val BASE_URL_RELEASE = "https://shop.epro.com.hk"

        // 高德：com.autonavi.minimap
        // 百度：com.baidu.BaiduMap
        // 腾讯：com.tencent.map
        const val MAP_GAODE="com.autonavi.minimap"
        const val MAP_BAIDU="com.baidu.BaiduMap"
        const val MAP_TENCENT="com.tencent.map"

        const val BASE_FILE_DOWNLOAD_URL="https://file.zigsom.com"

        const val HEADER_ONLINE_CACHETIME="onlineCacheTime"
        const val HEADER_OFFLINE_CACHETIME="offlineCacheTime"
        const val ONLINE_CACHETIME=30
        const val OFFLINE_CACHETIME=24*60*60

        const val ENGLISH="en-us"
        const val TRADITIONAL_CHINESE="zh-hk"
        const val SIMPLIFIED_CHINESE="zh-cn"
        val LIMIT_OFFSET = 20

        const val STATUS_RECYCLE = -1 //被回收
        const val STATUS_NORMAL = 1    //正常

        const val LOGIN = "/auth"
        const val OPEN = "/open"
        const val MODIFY_PASSWORD = "/passwordModify"
        const val FIND_PASSWORD = "/open/pwdreset" //忘记密码
        const val REGISTER = "/open/register"//注册

        // EP:箱密码登录, MP:手机密码登录, MV:手机验证码登录 ，FB:facebook登录，TW:推特登录
        const val LOGIN_TYPE_EP="ep"
        const val LOGIN_TYPE_MP="mp"
        const val LOGIN_TYPE_MV="mv"
        const val LOGIN_TYPE_FB="fb"
        const val LOGIN_TYPE_TW="tw"




    }
}