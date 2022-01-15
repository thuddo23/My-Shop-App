package com.example.myshop.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.adapter.AddressAdapter
import com.example.myshop.databinding.ActivityAddressListBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Address
import com.example.myshop.recyclerviewswipeimport.SwipeToDeleteCallBack
import com.example.myshop.recyclerviewswipeimport.SwipeToEditCallback
import com.example.myshop.utils.Constant

class AddressListActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private var mSelectAddress: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_list)
        fullScreen()
        setUpActionBar()
        FireStoreClass().getAddressList(this)
        if (intent.hasExtra(Constant.EXTRA_SELECT_ADDRESS)) {
            mSelectAddress = intent.getBooleanExtra(Constant.EXTRA_SELECT_ADDRESS, false)
        }
        if (mSelectAddress) {
            binding.titleToolbarEditAddress.text =
                resources.getString(R.string.title_select_address)
        }
        binding.txtAddAddress.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@AddressListActivity,
                    AddEditAddressActivity::class.java
                ), Constant.ADD_ADDRESS_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            FireStoreClass().getAddressList(this)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        FireStoreClass().getAddressList(this)
//    }

    fun successGetAddress(addressList: ArrayList<Address>) {
        if (addressList.size > 0) {
            binding.txtNoAddressFound.visibility = View.GONE
            binding.recycleViewAddressLayout.visibility = View.VISIBLE
            binding.recycleViewAddressLayout.layoutManager = LinearLayoutManager(this)
            var addressAdapter = AddressAdapter(this, addressList)
            binding.recycleViewAddressLayout.adapter = addressAdapter
            if (mSelectAddress) {
                val adapter = binding.recycleViewAddressLayout.adapter as AddressAdapter
                adapter.setOnItemClickListener { address ->
                    val intent = Intent(this, CheckOutActivity::class.java)
                    intent.putExtra(Constant.EXTRA_SELECTED_ADDRESS, address)
                    startActivity(intent)
                }
            }
            if (!mSelectAddress) {
                val editSwipeHandler = object : SwipeToEditCallback(this) {
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

                val deleteSwipeHandler = object : SwipeToDeleteCallBack(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        FireStoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.recycleViewAddressLayout)
            }


        } else {
            binding.txtNoAddressFound.visibility = View.VISIBLE
            binding.recycleViewAddressLayout.visibility = View.GONE
        }
    }

    fun successDeleteAddress() {
        FireStoreClass().getAddressList(this)
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