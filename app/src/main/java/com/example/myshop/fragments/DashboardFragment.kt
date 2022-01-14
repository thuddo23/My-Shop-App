package com.example.myshop.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshop.R
import com.example.myshop.activities.CartListActivity
import com.example.myshop.activities.SettingActivity
import com.example.myshop.adapter.DashboardAdapter
import com.example.myshop.databinding.FragmentDashboardBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Product

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsFromFireStore()
    }

    fun getDashboardItemsFromFireStore() {
        FireStoreClass().getDashboardItemsList(this)
    }

    fun successDashboardItemsList(productsList: ArrayList<Product>) {
        if (productsList.size > 0) {
            binding.recycleViewDashboard.visibility = View.VISIBLE
            binding.txtNoDashboard.visibility = View.GONE
            binding.recycleViewDashboard.layoutManager = GridLayoutManager(activity,2)
            binding.recycleViewDashboard.setHasFixedSize(true)
            binding.recycleViewDashboard.adapter = DashboardAdapter(requireContext(), productsList)
        } else {
            binding.recycleViewDashboard.visibility = View.GONE
            binding.txtNoDashboard.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_menu -> {
                startActivity(Intent(activity, SettingActivity::class.java))
            }R.id.cart_menu -> {
                startActivity(Intent(activity, CartListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}