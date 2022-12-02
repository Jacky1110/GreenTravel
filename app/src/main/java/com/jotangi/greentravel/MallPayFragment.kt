package com.jotangi.greentravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentTransaction
import com.jotangi.greentravel.Api.ApiUrl
import com.jotangi.greentravel.databinding.FragmentMallPaymoneyBinding
import com.jotangi.greentravel.ui.main.MainActivity


/**
 *
 * @Title: MallPayFragment.kt
 * @Package com.jotangi.healthy.HpayMall
 * @Description: MallPayFragment
 * @author Kelly
 * @date 2021-12
 * @version hpay_32版
 */
class MallPayFragment : ProjConstraintFragment() {

    private var _binding: FragmentMallPaymoneyBinding? = null

    companion object {
        const val TAG = "MallPayFragment"
        fun newInstance() = MallPayFragment()
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMallPaymoneyBinding.inflate(inflater, container, false)
        val data: Bundle? = arguments
        initWebview()
        if (data != null) {
            val ResOrder = data.getString("ResOrder").toString()
            Log.d("ResOrder: ", " " + ResOrder)


            binding.payMoney.loadUrl(ApiUrl.payUrl + ResOrder)
            Log.d("TAG ", " " + ApiUrl.payUrl + ResOrder)

        }
        val root: View = binding.root
        binding.apply { }

        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.account_listitem_order
    }

    override fun onResume() {
        super.onResume()
        Log.d("FFFFF", "crashonResume")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initWebview() {
        val webView = binding.payMoney
        webView.getSettings().setJavaScriptEnabled(true)
        webView.getSettings().setAllowFileAccessFromFileURLs(true)
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true)
        webView.getSettings().setAllowFileAccessFromFileURLs(true)
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true)
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webView.getSettings().setDomStorageEnabled(true)
        webView.getSettings().setBuiltInZoomControls(false)
        webView.getSettings().setAllowFileAccess(true)
        webView.getSettings().setSupportZoom(false)
        webView.getSettings().setSupportMultipleWindows(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.getSettings().setSaveFormData(true)
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        webView.getSettings().setPluginState(WebSettings.PluginState.ON)
        webView.setWebChromeClient(object : WebChromeClient() {
        })
        webView.addJavascriptInterface(WebAppInterface(requireContext()), "appInterface")


    }

}

open class WebAppInterface(private val mContext: Context) {
    @JavascriptInterface
    fun postMessage(msg: String) {
//        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        var intent = Intent(mContext, MainActivity::class.java);
        mContext?.startActivity(intent)
    }

}