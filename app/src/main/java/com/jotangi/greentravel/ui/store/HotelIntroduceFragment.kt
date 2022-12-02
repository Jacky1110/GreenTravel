package com.jotangi.greentravel.ui.store;

import android.os.Bundle;
import android.util.Log

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.isVisible
import com.jotangi.greentravel.DataBeen
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.ui.hPayMall.MemberBean
import com.jotangi.greentravel.ProjConstraintFragment
import com.jotangi.greentravel.databinding.FragmentHotelIntroduceBinding
import com.squareup.picasso.Picasso


class HotelIntroduceFragment : ProjConstraintFragment() {
    private lateinit var binding: FragmentHotelIntroduceBinding

    companion object {
        const val TAG = "HotelIntroduceFragment"
        fun newInstance() = HotelIntroduceFragment()
    }

    lateinit var proID: String
    lateinit var proPIC: String
    lateinit var fixmotor: String
    lateinit var storeName: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelIntroduceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val data: Bundle? = arguments

        if (data != null) {

            val storeAddress = data.getString("store_address")
            val storePhone = data.getString("store_phone")
            val storeOpenTime = data.getString("store_opentime")
            val storeDescript = data.getString("store_descript")

            storeName = data.getString("store_name").toString()
            proPIC = data.getString("store_picture").toString()
            proID = data.getString("store_id").toString()
            fixmotor = data.getString("store_fixmotor").toString()

            binding.hotelInfoTextView.text = "$storeName\n$storeAddress\n電話：$storePhone"
            binding.openTime.text = storeOpenTime
            binding.hotelIntroduction.text = storeDescript
            Picasso.get().load(ApiUrl.API_URL + "ticketec/" + proPIC).into(binding.hotelPicture)

            Log.d("TAG", "ApiUrl.API_URL + proPIC: ${ApiUrl.API_URL + proPIC}")
        }
        MemberBean.store_id = proID
        DataBeen.storeName = storeName

        if (fixmotor.equals("1")) {

            binding.btnReserve.isVisible = true

        } else if (fixmotor.equals("0")) {
            binding.btnReserve.isVisible = false
        }
        binding.apply {

            btnReserve.setOnClickListener {

                fragmentListener.onAction(FUNC_HOTEL_FILL, null)

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
