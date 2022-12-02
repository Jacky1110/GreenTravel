package com.jotangi.greentravel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 *
 * @Title:
 * @Package com.jotangi.healthy.HpayMall
 * @Description:
 * @author Kelly
 * @date 2022-02-07
 * @version hpay_32版
 */
@Parcelize
data class Product_type(
    var product_type:String? = null,
    var product_name:String? = null,
    var producttype_name:String? = null,
    var producttype_picture:String? = null,

    ): Parcelable