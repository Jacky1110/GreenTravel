package com.jotangi.greentravel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 *
 * @Title:
 * @Package com.jotangi.healthy.HpayMall
 * @Description:
 * 新需求:2022/2/23新增channel_price
 * @author Kelly
 * @date 2022-02-07
 * @version hpay_32版
 */
@Parcelize
data class Product_list(
    var product_no:String? = null,
    var product_type:String? = null,
    var producttype_name:String? = null,
    var product_name:String? = null,
    var product_price:String? = null,
    var product_picture:String? = null,
    var product_description:String? = null,
    var product_stock:String? = null,

    ): Parcelable