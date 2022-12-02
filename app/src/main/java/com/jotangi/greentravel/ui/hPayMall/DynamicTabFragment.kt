package com.jotangi.greentravel.ui.hPayMall

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jotangi.greentravel.*
import com.jotangi.greentravel.Api.ApiEnqueue
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.databinding.FragmentDynamicTabBinding
import com.jotangi.jotangi2022.ApiConUtils
import com.jotangi.jotangi2022.ApiConUtils.OnConnect
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.ArrayAdapter as ArrayAdapter

/**
 * @Description: 新增需求:健康商城，上方動態tab，下方商品列表
 */

class DynamicTabFragment : ProjConstraintFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var typeSpinner: Spinner
    private val TAG: String = javaClass.simpleName + "(TAG)"

    private val ticketId: String = "api06"
    private val ticketName: String = "套票"

    private var productType: String = ""
    private var productName: String = ""
    val data: Bundle? = arguments
    val outState = Bundle()

    private var spinnerAdapter: ArrayAdapter<String>? = null


    //private var _binding: FragmentDymaticTabBinding? = null
    private lateinit var proListAdapter: ProListAdapter
    private lateinit var discountAdapter: ProListDiscountAdapter
    private lateinit var binding: FragmentDynamicTabBinding

    val ProTypeMap = HashMap<Int, String>()
    val ProTypeMap1 = HashMap<Int, String>()
    val ProTypeMap2 = HashMap<Int, String>()
    val ProTypeMap3 = HashMap<Int, String>()
    val ProListMap = HashMap<Int, String>()
    val listsPL = arrayListOf<Product_list>()
    val lists = arrayListOf<Product_type>()
    val list = arrayListOf<String>()

    companion object {
        fun newInstance() = DynamicTabFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDynamicTabBinding.inflate(inflater, container, false)

        val root: View? = binding.root
        //Pshow()
        when {
            MemberBean.member_id.isNullOrEmpty() || MemberBean.member_pwd.isNullOrEmpty() -> {
                fragmentListener.onAction(FUNC_ACCOUNT_MAIN_TO_LOGIN, null)
            }
        }

        // 點數商城 上方tablayout UI 更新
        if (lists.isNotEmpty()) {
            lists.clear()
        }

        if (!outState.getString("position").isNullOrEmpty()) {
//            binding.productTypeSpinner.setSelection()
            outState.getString("position")?.let { GetProList(it) }
        }

        GetProType()
        handleSpinner()
        return root
    }

    private fun handleSpinner() {
        binding.productTypeSpinner.apply {
            spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                list
            )
            activity?.runOnUiThread {
                spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.adapter = spinnerAdapter
            }

            this.onItemSelectedListener = this@DynamicTabFragment
        }
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.title_mall

        initSearchViewSetting()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initSearchViewSetting() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateList(s.toString())
            }
        })
    }

    private fun updateList(inputStr: String) {
        if (inputStr.isNotEmpty()) {
            val searchList = arrayListOf<Product_list>()
            listsPL.map { item ->
                if (item.product_name?.contains(inputStr) == true) {
                    searchList.add(item)
                }
            }

            proListAdapter.updateDataSource(searchList)
        } else {
            proListAdapter.updateDataSource(listsPL)
        }
    }

    private fun initSpinner() {
        val list = mutableListOf<String>()

        val data: Bundle? = arguments
        if (data != null) {
            productName = data.getString("Name", "")
            productType = data.getString("type", "")
            Log.d(TAG, "productType: $productType")
            if (productName == "i租車" && productType == "Rent") {
                lists.forEachIndexed { index, item ->
                    val typeName = item.producttype_name

                    if (!typeName.isNullOrEmpty()) {
                        list.add(typeName)
                    }

                    item.product_type?.let {
                        ProTypeMap[index] = it
                    }
                }
            } else if (productName == "改裝精品配件" && productType == "SC001") {
                lists.forEachIndexed { index, item ->
                    val typeName = item.producttype_name

                    if (!typeName.isNullOrEmpty()) {
                        list.add(typeName)
                    }

                    item.product_type?.let {
                        ProTypeMap[index] = it
                    }
                }
            }
        } else {
            lists.forEachIndexed { index, item ->
                val typeName = item.producttype_name

                if (!typeName.isNullOrEmpty()) {
                    list.add(typeName)
                }

                item.product_type?.let {
                    ProTypeMap[index] = it
                }
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
        ProTypeMap[position]?.let { it1 ->
            if (ticketId == it1) {
                GetTicket()
            } else if (productType == "Rent") {
                binding.productTypeSpinner.setSelection(1)
                GetProList(it1)
            } else if (productType == "SC001") {
                binding.productTypeSpinner.setSelection(2)
                GetProList(it1)
            }else{
                GetProList(it1)
            }
            saveBundle(it1)

        }
//        ProTypeMap1[position]?.let { it ->
//            if (productType == "Rent") {
//                binding.productTypeSpinner.setSelection(1)
//                GetProList(it)
//            } else {
//                GetProList(it)
//            }
////            binding.productTypeSpinner.setSelection(1)
//        }
//        ProTypeMap2[position]?.let { its ->
//            binding.productTypeSpinner.setSelection(2)
//            GetProList(its)
//        }
//        ProTypeMap3[position]?.let { it2 ->
//            val pos = outState?.getString("position", "")
//            val i: Int = pos?.toInt()!!
//            binding.productTypeSpinner.setSelection(i)
//            if (ticketId == it2) {
//                GetTicket()
//            } else {
//                GetProList(pos)
//            }
//        }
    }

    private fun saveBundle(it1: String) {
        outState?.putString("position", it1)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
        MemberBean.channel_id = 3
    }

    private fun tabSetting(tabList: ArrayList<Product_type>) {

        for (i in 0 until tabList.size) {

            tabList[i].product_type?.let {
                ProTypeMap.put(i, it)
            }

            binding?.dyT.apply {

                activity?.runOnUiThread {
                    // 更新 Tablayout
                    addTab(binding.dyT.newTab().setText(tabList[i].producttype_name))
                }
            }
        }

        // tablayout 選擇項目 index
        binding.dyT.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position.let {
                    ProTypeMap.get(it)?.let { it1 ->
                        if (ticketId.equals(it1)) {
                            GetTicket()
                        } else {
                            GetProList(it1)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        Log.d(TAG, "ProTypeMap: $ProTypeMap")

        // tablayout 第一項
        GetProList("A1")
    }

    // 頁面切換並帶值
    fun tackContext() {

        requireActivity().runOnUiThread {
            try {
                proListAdapter.WatchItemClick = {
                    val fragment = CDetailFragment()
                    val args = Bundle()
                    args.putString("Cname", it.product_name.toString())
                    args.putString("Cdollar", it.product_price.toString())
                    args.putString("Cno", it.product_no.toString())
//                        args.putString("Cdis", it.channel_price.toString())
                    fragment.setArguments(args)
                    val transaction: FragmentTransaction =
                        requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                    transaction.addToBackStack(DynamicTabFragment.javaClass.simpleName)
                    transaction.commit()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 頁面切換並帶值
    fun tabContext() {
        requireActivity().runOnUiThread {
            try {
                proListAdapter.WatchItemClick = {
                    val fragment = PagerCommodityFragment()
                    val args = Bundle()
                    args.putString("package_no", it.product_no.toString())
                    args.putString("product_name", it.product_name.toString())
                    args.putString("product_picture", it.product_picture.toString())
                    args.putString("product_price", it.product_price.toString())
                    args.putString("product_description", it.product_description.toString())
                    args.putString("product_stock", it.product_stock.toString())
                    fragment.setArguments(args)
                    val transaction: FragmentTransaction =
                        requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main, fragment)
                    transaction.addToBackStack(DynamicTabFragment.javaClass.simpleName)
                    transaction.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun progressTest() {
        binding.ring.apply {
            setProgressFormatter(object : RingProgrseeBar.ProgressFormatter {
                override fun format(progress: Int, max: Int): CharSequence? {
                    return progress.toString() + "%"
                }
            })
            setOnClickListener {
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        setProgressTextColor(Color.RED);
                        setProgressStartColor(Color.GREEN)

                        setDrawBackgroundOutsideProgress(true)
                    }

                }
            }
        }
    }

    private fun simulateProgress() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.ring.setProgress(progress)

        }
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = 4000
        animator.start()
    }

    // 4.商城商品類別
    fun GetProType() {

        Pshow()

        val accout = MemberBean.member_id
        val pwd = MemberBean.member_pwd

        ApiConUtils.product_type(
            ApiUrl.API_URL, ApiUrl.product_type, accout, pwd,
            object : OnConnect {
                override fun onSuccess(jsonString: String?) {

                    Pnoshow()

                    val jsonArray = JSONArray(jsonString)

                    for (i in 0 until jsonArray.length()) {

                        val jsonObject = jsonArray[i] as JSONObject

                        lists.add(
                            Product_type(
                                product_type = jsonObject.getString("product_type"),
                                producttype_name = jsonObject.getString("producttype_name"),
                            )
                        )
                    }

                    lists.add(
                        Product_type(
                            product_type = ticketId,
                            producttype_name = ticketName
                        )
                    )

                    Log.d(TAG, "lists: $lists")

//                    tabSetting(lists)
//                    handleProduct(lists)
                    initSpinner()
                }

                override fun onFailure(message: String?) {
                    Pnoshow()
                }

            }
        )
    }

    // 6.商城套票列表
    private fun GetTicket() {
        Pshow()

        var apiEnqueue = ApiEnqueue()

        apiEnqueue.package_list(object : ApiEnqueue.resultListener {
            override fun onSuccess(message: String?) {
                activity!!.runOnUiThread {
                    Pnoshow()

                    listsPL.clear()

                    try {

                        val jsonArray = JSONArray(message)
                        var jsonObject: JSONObject

                        for (i in 0 until jsonArray.length()) {

                            jsonObject = jsonArray[i] as JSONObject

                            listsPL.add(
                                Product_list(
                                    product_name = jsonObject.getString("product_name"),
                                    product_price = jsonObject.getString("product_price"),
                                    product_picture = jsonObject.getString("product_picture"),
                                    product_no = jsonObject.getString("package_no"),
                                    product_description = jsonObject.getString("product_description"),
                                    product_stock = jsonObject.getString("product_stock")
                                )
                            )
                        }

                        requireActivity().runOnUiThread {
                            binding.rT.apply {
                                proListAdapter = ProListAdapter()
                                layoutManager = GridLayoutManager(context, 2)
                                adapter = proListAdapter
                            }

                            proListAdapter.updateDataSource(listsPL)
                        }

                        tabContext()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(message: String?) {
                Pnoshow()
            }
        })

    }

    // 5.商城商品列表
    private fun GetProList(type: String) {

        Pshow()

        ApiConUtils.product_list(
            ApiUrl.API_URL,
            ApiUrl.product_list,
            MemberBean.member_id,
            MemberBean.member_pwd,
            object : OnConnect {
                override fun onSuccess(jsonString: String?) {
                    activity!!.runOnUiThread {
                        Pnoshow()

                        listsPL.clear()

                        try {

                            val jsonArray = JSONArray(jsonString)

                            for (i in 0 until jsonArray.length()) {

                                val jsonObject = jsonArray[i] as JSONObject
                                val proType: String = jsonObject.getString("product_type")

                                when {
                                    type.equals(proType) -> {
                                        listsPL.add(
                                            Product_list(
                                                product_type = jsonObject.getString("product_type"),
                                                product_name = jsonObject.getString("product_name"),
                                                product_price = jsonObject.getString("product_price"),
                                                product_no = jsonObject.getString("product_no"),
                                                product_picture = jsonObject.getString("product_picture"),
                                            )
                                        )
                                    }
                                }
                            }

                            requireActivity().runOnUiThread {
                                binding.rT.apply {
                                    proListAdapter = ProListAdapter()
                                    layoutManager = GridLayoutManager(context, 2)
                                    adapter = proListAdapter
                                }

                                proListAdapter.updateDataSource(listsPL)
                            }

                            tackContext()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(message: String?) {
                    Pnoshow()
                }
            }
        )
    }

    private fun Pshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding.pb2.visibility = View.VISIBLE
            }
        }

    }

    private fun Pnoshow() {
        if (requireActivity() != null) {
            requireActivity().runOnUiThread {
                requireActivity().getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.rT.visibility = View.VISIBLE
                binding.pb2.visibility = View.GONE
            }
        }
    }
}
