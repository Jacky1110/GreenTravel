package com.jotangi.greentravel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Store_type(
    var name:String? = null,
    var id:String? = null,
    var createtime:String? = null,
    var updateTime:String? = null,

    ): Parcelable