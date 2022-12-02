package com.jotangi.greentravel;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.greentravel.databinding.FragmentPointStoreBinding

class PointStoreFragment : ProjConstraintFragment() {

     private var _binding: FragmentPointStoreBinding? = null

     companion object{
         const val TAG = "PointStoreFragment"
         fun newInstance() = PointStoreFragment()
     }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPointStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {

        }
        return root
    }

    override fun onStart() {
        super.onStart()
        activityTitleRid = R.string.account_listitem_order
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}