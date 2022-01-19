package com.example.myshop.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.activities.MyOrderDetailsActivity
import com.example.myshop.adapter.OrderAdapter
import com.example.myshop.databinding.FragmentOrdersBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Order
import com.example.myshop.utils.Constant


class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getMyOrdersList(this)
    }

    fun successGetOrderList(orderList: ArrayList<Order>) {
        if (orderList.size > 0) {
            binding.recyclerViewOrders.visibility = View.VISIBLE
            binding.txtNoOrderPlacedOrders.visibility = View.GONE

            val orderAdapter = OrderAdapter(requireContext(), orderList)
            binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext())
            orderAdapter.setOnItemClickListener { order ->
                val intent = Intent(requireContext(),MyOrderDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_MY_ORDER_DETAILS,order)
                startActivity(intent)
            }
            binding.recyclerViewOrders.adapter = orderAdapter
        } else {
            binding.recyclerViewOrders.visibility = View.GONE
            binding.txtNoOrderPlacedOrders.visibility = View.VISIBLE
        }
    }
}