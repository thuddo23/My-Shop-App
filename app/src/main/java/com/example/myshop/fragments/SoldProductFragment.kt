package com.example.myshop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.adapter.SoldProductAdapter
import com.example.myshop.databinding.FragmentSoldProductBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.SoldProduct

class SoldProductFragment : Fragment() {
    private lateinit var binding: FragmentSoldProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sold_product, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getSoldProductsList(this)
    }

    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {
        if (soldProductsList.size > 0) {
            binding.recyclerViewSoldProducts.visibility = View.VISIBLE
            binding.txtNoProductsSoldYet.visibility = View.GONE

            binding.recyclerViewSoldProducts.layoutManager = LinearLayoutManager(requireContext())
            val soldProductAdapter = SoldProductAdapter(requireContext(), soldProductsList)
            binding.recyclerViewSoldProducts.adapter = soldProductAdapter
        } else {
            binding.recyclerViewSoldProducts.visibility = View.GONE
            binding.txtNoProductsSoldYet.visibility = View.VISIBLE
        }
    }

//    private fun getSoldProductsList() {
//        FireStoreClass().getSoldProductsList(this@SoldProductFragment)
//    }

}