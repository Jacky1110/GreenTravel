package com.jotangi.greentravel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Store_List(
    var store_id:String? = null,
    var store_type:String? = null,
    var store_name:String? = null,
    var store_address:String? = null,
    var store_picture:String? = null,
    var store_phone:String? = null,
    var store_email:String? = null,
    var store_describe:String? = null,
    var store_opentime:String? = null,
    var store_fixmotor:String? = null
    ): Parcelable

