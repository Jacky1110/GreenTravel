package com.jotangi.greentravel.ui.store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.ProjConstraintFragment
import com.jotangi.greentravel.R
import com.jotangi.greentravel.Store_List
import com.jotangi.greentravel.Store_type
import com.jotangi.greentravel.databinding.FragmentStoreTypeBinding
import com.jotangi.greentravel.ui.hPayMall.MemberBean
import com.jotangi.jotangi2022.ApiConUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class StoreTabFragment : ProjConstraintFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentStoreTypeBinding
    private lateinit var storeListAdapter: StoreListAdapter

    private var spinnerAdapter: ArrayAdapter<String>? = null

    lateinit var tab1: String
    val ProTypeMap = HashMap<Int, String>()
    val ProListMap = HashMap<Int, String>()
    val listsPL = arrayListOf<Store_List>()
    val istrue: Boolean = false
    val lists = arrayListOf<Store_type>()
    val list = arrayListOf<String>()

    lateinit var sid: String

    companion object {
        const val TAG = "StoreTabFragment"
        fun newInstance() = StoreTabFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreTypeBinding.inflate(inflater, container, false)

        val root: View? = binding.root

        when {
            MemberBean.member_id.isNullOrEmpty() || MemberBean.member_pwd.isNullOrEmpty() -> {
                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null)

            }
        }

        /*確保登入後才能查詢上方動態列表*/
        if (istrue != true) {
            if (lists.isNotEmpty()) {
                lists.clear()
            }

            GetProType()
            handleProduct()
            handleSpinner()
        }

//        Timer().schedule(500) {
//            requireActivity().runOnUiThread {
//                binding.rY.apply {
//                    storeListAdapter = StoreListAdapter(listsPL)
////                    layoutManager = LinearLayoutManager(context)
//                    layoutManager = GridLayoutManager(context, 2)
//                    adapter = storeListAdapter
//                }
//
//                binding?.dyT1.apply {
//                    getTabAt(0)?.select()
//                    //GetProList ("c1")->query Api:product_list
//                    listsPL.clear()
//
//                    val set: Set<Int> = ProTypeMap.keys
//                    val it: Iterator<Int> = set.iterator()
//                    while (it.hasNext()) {
//                        val key: Int = it.next()
//                        val value: String? = ProTypeMap[key]
//                        value?.let { it1 ->
//                            GetStoreList(it1)
//                        }
//                    }
//
//                    istrue == false
//                }
//            }
//        }
        return root
    }

    private fun handleSpinner() {
        binding.storeTypeSpinner.apply {
            spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list
            )
            activity?.runOnUiThread {
                spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.adapter = spinnerAdapter
            }

            this.onItemSelectedListener = this@StoreTabFragment
        }


    }

    private fun handleProduct() {
        val data: Bundle? = arguments
        if (data != null) {
            sid = data.getString("sid", "")
        }
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_store

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
        MemberBean.channel_id = 4
    }

    private fun initSpinner() {
        val list = mutableListOf<String>()
        lists.forEachIndexed { index, item ->
            val typeName = item.name

            if (!typeName.isNullOrEmpty()) {
                list.add(typeName)
            }

            item.id?.let {
                ProTypeMap[index] = item.id.toString()
            }
        }

        activity?.runOnUiThread {
            spinnerAdapter?.clear()
            spinnerAdapter?.addAll(list)
            spinnerAdapter?.notifyDataSetChanged()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listsPL.clear()
        ProTypeMap[position]?.let { key ->
            GetStoreList(key)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun tabSetting(tabList: ArrayList<Store_type>) {
        for (i in 0 until tabList.size) {
            val tText = tabList[i].id + ":" + tabList[i].name
            tabList[i].id?.let {
                ProTypeMap.put(i, it)
            }
            val tab: String = tText.substringAfterLast(":")
            tab1 = tText.substringBeforeLast(":")

            binding.dyT1.apply {
                addTab(binding.dyT1.newTab().setText(tab))
                Pnoshow()
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        listsPL.clear()

                        var position = tab?.position
                        val set: Set<Int> = ProTypeMap.keys
                        val it: Iterator<Int> = set.iterator()
                        while (it.hasNext()) {
                            val key: Int = it.next()
                            if (position == key) {
                                val value: String? = ProTypeMap.get(position)
                                value?.let { it1 ->
                                    GetStoreList(it1)
                                }
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                })

            }

        }


        //  }
    }

    /*商品個別細節查詢*/
    fun tabContext() {
        requireActivity().runOnUiThread {
            try {
                storeListAdapter.ItemClick = {
                    val fragment = HotelIntroduceFragment()
                    val args = Bundle()

                    args.putString("store_name", it.store_name.toString())
                    args.putString("store_address", it.store_address.toString())
                    args.putString("store_id", it.store_id.toString())
                    args.putString("store_opentime", it.store_opentime.toString())
                    args.putString("store_phone", it.store_phone.toString())
                    args.putString("store_descript", it.store_describe.toString())
                    args.putString("store_picture", it.store_picture.toString())
                    args.putString("store_fixmotor", it.store_fixmotor.toString())
                    fragment.setArguments(args)
                    val transaction: FragmentTransaction =
                        requireActivity().getSupportFragmentManager().beginTransaction()
                    transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                    transaction.addToBackStack(StoreTabFragment.javaClass.simpleName)
                    transaction.commit()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                Pnoshow()
            }
        }
    }

    /*上方商品列表項目查詢*/
    fun GetProType() {
        val accout = MemberBean.member_id
        val pwd = MemberBean.member_pwd
        ApiConUtils.store_type(
            ApiUrl.API_URL, ApiUrl.store_type,
            accout,
            pwd,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
                    activity!!.runOnUiThread {
                        val jsonArray = JSONArray(jsonString)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray[i] as JSONObject
                            lists.add(
                                Store_type(
                                    name = jsonObject.getString("name"),
                                    id = jsonObject.getString("id"),
                                )
                            )
                        }

//                        tabSetting(lists)
                        initSpinner()
                    }
                }

                override fun onFailure(message: String) {
                }
            })
    }

    /*商品分類列表個別查詢*/
    private fun GetStoreList(type: String) {
        Pshow()
        ApiConUtils.store_list(
            ApiUrl.API_URL, ApiUrl.store_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            type,
            object : ApiConUtils.OnConnect {
                @Throws(JSONException::class)
                override fun onSuccess(jsonString: String) {
//                    listsPL.clear()
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            listsPL.clear()
                            try {
                                val data: Bundle? = arguments
                                val jsonArray = JSONArray(jsonString)
                                for (i in 0 until jsonArray.length()) {
                                    val jsonObject = jsonArray[i] as JSONObject
                                    val proType: String = jsonObject.getString("store_type")
                                    when {
                                        type.equals(proType) -> {
                                            listsPL.add(
                                                Store_List(
                                                    store_id = jsonObject.getString("store_id"),
                                                    store_type = jsonObject.getString("store_type"),
                                                    store_name = jsonObject.getString("store_name"),
                                                    store_address = jsonObject.getString("store_address"),
                                                    store_picture = jsonObject.getString("store_picture"),
                                                    store_opentime = jsonObject.getString("store_opentime"),
                                                    store_phone = jsonObject.getString("store_phone"),
                                                    store_describe = jsonObject.getString("store_descript"),
                                                    store_fixmotor = jsonObject.getString("fixmotor"),
                                                )
                                            )
                                        }
                                    }
                                    requireActivity().runOnUiThread {
                                        binding.rY.apply {
                                            storeListAdapter = StoreListAdapter(listsPL)
                                            layoutManager = GridLayoutManager(context, 2)
                                            adapter = storeListAdapter
                                        }
                                        storeListAdapter.notifyDataSetChanged()
                                    }
                                    tabContext()
                                }
                            } catch (e: Exception) {
                                Pnoshow()
                            }
                        }
                    }


                }

                override fun onFailure(message: String) {
                    Pnoshow()
                }
            })


    }

    private fun Pshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding.pb.visibility = View.VISIBLE
            }
        }

    }

    private fun Pnoshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.rY.visibility = View.VISIBLE
                binding.pb.visibility = View.GONE
            }
        }
    }
}



