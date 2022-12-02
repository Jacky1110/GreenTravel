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
data class Cart(
    var Img: Boolean = false,
    var Name: String? = null,
    var Nt: String? = null,
    var Count: String? = null,
    var Dollar: String? = null,
    var product_picture: String? = null,
    var product_no: String? = null,
    var channel_price: String? = null,
    var total_amount: String? = null,
    var discount_amount: String? = null,
    var order_pay: String? = null,
    var pkglist_id: String? = null,
    var assigntype: String? = null,
    var product_stock: String? = null,
) : Parcelable

@Parcelize
data class Cartdis(
    var Img: Boolean = false,
    var Name: String? = null,
    var Nt: String? = null,
    var Count: String? = null,
    var Dollar: String? = null,
    var product_picture: String? = null,
    var product_no: String? = null,
    var channel_price: String? = null,
) : Parcelable

@Parcelize
data class Ecorder_list(
    var IsPay: Boolean = false,
    var oid: String? = null,
    var order_no: String? = null,
    var order_date: String? = null,
    var store_id: String? = null,
    var account: String? = null,
    var order_amount: String? = null,
    var coupon_no: String? = null,
    var discount_amount: String? = null,
    var pay_type: String? = null,
    var order_pay: String? = null,
    var pay_status: String? = null,
    var bonus_point: String? = null,
    var deliverytype: String? = null,
    var recipientname: String? = null,
    var recipientaddr: String? = null,
    var recipientphone: String? = null,
    var recipientmail: String? = null,
    var deliverystatus: String? = null,
    var invoicetype: String? = null,
    var invoicephone: String? = null,
    var companytitle: String? = null,
    var uniformno: String? = null,
    var invoicestatus: String? = null,
    var order_status: String? = null,
    var assigntype: String? = null,


    ) : Parcelable

@Parcelize
data class Ecorder_info(
    var oid: String? = null,
    var order_no: String? = null,
    var order_date: String? = null,
    var store_id: String? = null,
    var account: String? = null,
    var order_amount: String? = null,
    var coupon_no: String? = null,
    var discount_amount: String? = null,
    var pay_type: String? = null,
    var order_pay: String? = null,
    var pay_status: String? = null,
    var bonus_point: String? = null,
    var deliverytype: String? = null,
    var recipientname: String? = null,
    var recipientaddr: String? = null,
    var recipientphone: String? = null,
    var recipientmail: String? = null,
    var deliverystatus: String? = null,
    var invoicetype: String? = null,
    var invoicephone: String? = null,
    var companytitle: String? = null,
    var uniformno: String? = null,
    var invoicestatus: String? = null,
    var order_status: String? = null,


    ) : Parcelable

@Parcelize
data class EcorderIn_Child(


    var product_no: String? = null,
    var product_spec: String? = null,
    var product_price: String? = null,
    var order_qty: String? = null,
    var total_amount: String? = null,
    var deliverystatus: String? = null,
    var order_status: String? = null,
    var product_name: String? = null,
    var product_picture: String? = null,

    ) : Parcelable