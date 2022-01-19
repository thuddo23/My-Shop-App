package com.example.myshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityProductDetailsBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.fragments.ProductsFragmentDirections
import com.example.myshop.models.CartItem
import com.example.myshop.models.Product
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId = ""
    private var productOwnerId = ""
    private var mProduct: Product? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        fullScreen()
        setUpActionBar()
        if (intent.hasExtra(Constant.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constant.EXTRA_PRODUCT_ID)!!
        }
        if (intent.hasExtra(Constant.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constant.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (productOwnerId != FireStoreClass().getCurrentUserID()) {
            binding.btnAddToCartProductDetails.visibility = View.VISIBLE
        } else {
            binding.btnAddToCartProductDetails.visibility = View.GONE
            binding.btnMoveToCartProductDetails.visibility = View.GONE
        }
        FireStoreClass().getProductDetails(this, mProductId)

        binding.btnAddToCartProductDetails.setOnClickListener {
            addToCart()
        }
        binding.btnMoveToCartProductDetails.setOnClickListener {
            startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
        }


    }

    private fun addToCart() {
        val cartItem = CartItem(
            FireStoreClass().getCurrentUserID(),
            productOwnerId,
            mProductId,
            mProduct!!.title,
            mProduct!!.price,
            mProduct!!.image,
            Constant.DEFAULT_CART_QUANTITY
        )
        FireStoreClass().addCartItems(this, cartItem)
    }

    fun successGetProductDetails(product: Product) {
        mProduct = product
        GlideLoader(this).loaderProductPicture(product.image, binding.imgProductDetails)
        binding.txtTitleProductDetails.text = product.title
        binding.txtPricesProductDetails.text = product.price
        binding.txtDescriptionProductDetails.text = product.description
        binding.txtStockQuantityDetails.text = product.stock_quantity

        FireStoreClass().checkIfItemExistInCart(this, mProductId)

        if (product.stock_quantity.toInt() == 0) {
            binding.btnAddToCartProductDetails.visibility = View.GONE
            binding.btnMoveToCartProductDetails.visibility = View.GONE

            binding.txtStockQuantityDetails.text = resources.getString(R.string.out_of_stock)
            binding.txtStockQuantityDetails.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.snackbar_error_color
                )
            )
        } else {
            FireStoreClass().checkIfItemExistInCart(this, mProductId)
        }
    }

    fun successAddToCart() {
        showErrorSnackBar("Added successfully to cart.", false)
        binding.btnAddToCartProductDetails.visibility = View.GONE
        binding.btnMoveToCartProductDetails.visibility = View.VISIBLE
    }

    fun productExistsInCart() {
        binding.btnAddToCartProductDetails.visibility = View.GONE
        binding.btnMoveToCartProductDetails.visibility = View.VISIBLE
    }


    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarProductDetails)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarProductDetails.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}