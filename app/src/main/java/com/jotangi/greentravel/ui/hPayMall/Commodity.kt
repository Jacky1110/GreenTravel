package com.jotangi.greentravel.ui.hPayMall

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
data class Commodity(
    var id: String? = null,
    var des: String? = null,

): Parcelable