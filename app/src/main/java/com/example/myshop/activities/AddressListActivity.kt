package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.adapter.AddressAdapter
import com.example.myshop.databinding.ActivityAddressListBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Address
import com.example.myshop.recyclerviewswipeimport.SwipeToDeleteCallback

class AddressListActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_list)
        fullScreen()
        setUpActionBar()
        binding.txtAddAddress.setOnClickListener {
            startActivity(Intent(this@AddressListActivity, AddEditAddressActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getAddressList(this)
    }

    fun successGetAddress(addressList: ArrayList<Address>) {
        if (addressList.size > 0) {
            binding.txtNoAddressFound.visibility = View.GONE
            binding.recycleViewAddressLayout.visibility = View.VISIBLE
            binding.recycleViewAddressLayout.layoutManager = LinearLayoutManager(this)
            var addressAdapter = AddressAdapter(this, addressList)
            binding.recycleViewAddressLayout.adapter = addressAdapter
            val editSwipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding.recycleViewAddressLayout.adapter as AddressAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition
                    )
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding.recycleViewAddressLayout)


        } else {
            binding.txtNoAddressFound.visibility = View.VISIBLE
            binding.recycleViewAddressLayout.visibility = View.GONE
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddressList)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddressList.setNavigationOnClickListener {
            onBackPressed()
        }

    }


}