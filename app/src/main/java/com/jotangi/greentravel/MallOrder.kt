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
data class MallOrder(
    var oId: String? = null,
    var order_date: String? = null,
    var order_amount: String? = null,
    var order_status: String? = null,
    var IsPay: Boolean = false,
    var pay_status: String? = null
    ) : Parcelable
