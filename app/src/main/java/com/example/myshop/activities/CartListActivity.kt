package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityCartListBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.models.Product
import com.example.myshop.utils.Constant

class CartListActivity : BaseActivity() {
    private lateinit var binding: ActivityCartListBinding
    private lateinit var mProductList: List<Product>
    private lateinit var mCartItemList: List<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_list)
        fullScreen()
        setUpActionBar()
        binding.btnCheckOutCartList.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constant.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getAllProducts(this)
    }

    fun successGetAllProducts(products: List<Product>) {
        mProductList = products
        FireStoreClass().getCartList(this)
    }

    fun successUpdateCartItem() {
        FireStoreClass().getCartList(this)
    }

    fun successGetCartList(cartList: List<CartItem>) {

        for (product in mProductList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stock_quantity
                    }

                }
            }
        }
        mCartItemList = cartList

        if (mCartItemList.size > 0) {
            binding.recycleViewCartList.visibility = View.VISIBLE
            binding.btnCheckOutCartList.visibility = View.GONE
            binding.txtNoCartItemFoundCartList.visibility = View.GONE

            binding.recycleViewCartList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.recycleViewCartList.setHasFixedSize(true)
            var cartAdapter = CartAdapter(this, mCartItemList)
            binding.recycleViewCartList.adapter = cartAdapter
            var subTotal = 0.0
            for (i in mCartItemList) {
                val availableQuantity = i.stock_quantity.toInt()
                if (availableQuantity > 0) {
                    val price = i.price.toDouble()
                    val quantity = i.cart_quantity.toDouble()
                    subTotal += price * quantity
                }
            }
            binding.txtSubtotalValueCartList.text = "$${subTotal}"
            binding.txtShippingChargeValueCartList.text = "$1.5"
            if (subTotal > 0) {
                binding.btnCheckOutCartList.visibility = View.VISIBLE
                val total = subTotal + 1.5
                binding.txtTotalAmountValueCartList.text = "$${total}"
            } else {
                binding.btnCheckOutCartList.visibility = View.GONE
            }
        } else {
            binding.recycleViewCartList.visibility = View.GONE
            binding.txtNoCartItemFoundCartList.visibility = View.VISIBLE
            binding.btnCheckOutCartList.visibility = View.GONE
        }
    }

    fun successRemoveCartItem() {
        showErrorSnackBar(resources.getString(R.string.item_removed_successfully), false)
        FireStoreClass().getCartList(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarCartList)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
        actionBar.setDisplayShowTitleEnabled(false)
        binding.toolbarCartList.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}