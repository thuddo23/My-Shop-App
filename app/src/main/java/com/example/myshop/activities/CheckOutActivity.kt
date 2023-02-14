package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityCheckOutBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.fragments.MainActivity
import com.example.myshop.models.Address
import com.example.myshop.models.CartItem
import com.example.myshop.models.Order
import com.example.myshop.models.Product
import com.example.myshop.utils.Constant

class CheckOutActivity : BaseActivity() {
    private lateinit var binding: ActivityCheckOutBinding
    private var mAddressDetails: Address? = null
    private var mProductList = ArrayList<Product>()
    private var mCartList = ArrayList<CartItem>()
    private var mSubtotal = 0.0
    private var mTotal = 0.0
    private lateinit var mOrderDetails: Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out)
        fullScreen()
        setUpActionBar()
        displayAddressDetails()
        getAllProducts()
        binding.btnPlaceOrderCheckOut.setOnClickListener {
            placeAnOrder()
            startActivity(Intent(this@CheckOutActivity, BinancePaymentActivity::class.java))
        }

    }

    private fun getAllProducts() {
        FireStoreClass().getAllProducts(this)
    }

    fun successGetCartList(cartItemList: ArrayList<CartItem>) {
        for (product in mProductList) {
            for (cart in cartItemList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stock_quantity
                    }

                }
            }
        }
        mCartList = cartItemList
        binding.recyclerViewProductCheckout.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProductCheckout.setHasFixedSize(true)
        var cartListAdapter = CartAdapter(this, mCartList, false)
        binding.recyclerViewProductCheckout.adapter = cartListAdapter

        for (item in mCartList) {
            val cartQuantity = item.cart_quantity.toDouble()
            val price = item.price.toDouble()
            mSubtotal += price * cartQuantity
        }
        binding.subtotalValueCheckout.text = "$${mSubtotal}"
        binding.shippingChargeValueCheckout.text = "$1.5"
        if (mSubtotal > 0) {
            binding.btnPlaceOrderCheckOut.visibility = View.VISIBLE
        }
        mTotal = mSubtotal + 1.5
        binding.txtTotalValueCheckout.text = "$${mTotal}"
    }

    fun successUpdateAllDetails() {
        showErrorSnackBar("Your order was placed successfully.", false)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    fun successPlaceOrder() {
        FireStoreClass().updateAllDetails(this, mCartList, mOrderDetails)
    }

    private fun placeAnOrder() {
//        if (mAddressDetails != null) {
//            mOrderDetails = Order(
//                FireStoreClass().getCurrentUserID(),
//                mCartList,
//                mAddressDetails!!,
//                "My older ${System.currentTimeMillis()}",
//                mCartList[0].image,
//                mSubtotal.toString(),
//                "1.5",
//                mTotal.toString(),
//                System.currentTimeMillis()
//            )
//            FireStoreClass().placeOrder(this, mOrderDetails)
//        }
    }

    fun successGetAllProducts(productList: ArrayList<Product>) {
        mProductList = productList
        FireStoreClass().getCartList(this)
    }

    private fun displayAddressDetails() {
        if (intent.hasExtra(Constant.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails = intent.getParcelableExtra(Constant.EXTRA_SELECTED_ADDRESS)
        }
        if (mAddressDetails != null) {
            binding.addressTypeCheckout.text = mAddressDetails!!.type
            binding.addressFullNameCheckout.text = mAddressDetails!!.name
            binding.addressDetailsCheckout.text = mAddressDetails!!.address
            binding.addressPhoneCheckout.text = mAddressDetails!!.mobileNumber

            if (mAddressDetails!!.type == Constant.OTHER) {
                binding.addressOtherDetailsCheckout.visibility = View.VISIBLE
                binding.addressOtherDetailsCheckout.text = mAddressDetails!!.othersDetails
            }
            if (mAddressDetails!!.additionalNote.isEmpty()) {
                binding.addressAdditionalCheckout.visibility = View.GONE
            } else {
                binding.addressAdditionalCheckout.visibility = View.VISIBLE
                binding.addressAdditionalCheckout.text = mAddressDetails!!.additionalNote

            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarCheckout)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
        actionBar.setDisplayShowTitleEnabled(false)
        binding.toolbarCheckout.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}