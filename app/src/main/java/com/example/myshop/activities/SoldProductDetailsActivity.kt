package com.example.myshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivitySoldProductDetailsBinding
import com.example.myshop.models.SoldProduct
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_sold_product_details.*
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivitySoldProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sold_product_details)
        fullScreen()
        setUpActionBar()
        var mSoldProductDetails = SoldProduct()
        if (intent.hasExtra(Constant.EXTRA_SOLD_PRODUCT_DETAILS)) {
            mSoldProductDetails =
                intent.getParcelableExtra(Constant.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        setupUI(mSoldProductDetails)
    }

    private fun setupUI(soldProductDetails: SoldProduct) {
        binding.txtOrderIdValueSoldProductsDetails.text = soldProductDetails.order_id
        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = soldProductDetails.order_date
        val orderDateTime = formatter.format(calendar.time)
        binding.txtOrderDateValueSoldProductsDetails.text = orderDateTime
        GlideLoader(this).loaderProductPicture(
            soldProductDetails.image,
            binding.imgProductItemSoldProductDetails
        )
        binding.txtProductNameSoldProductDetails.text = soldProductDetails.title
        binding.txtPriceSoldProductDetails.text = soldProductDetails.price
        binding.txtProductQuantitySoldProductDetails.text = soldProductDetails.sold_quantity
        binding.txtFullNameSoldProductsDetails.text = soldProductDetails.address.name
        binding.txtAddressDetailsSoldProductsDetails.text =
            "${soldProductDetails.address.address}, ${soldProductDetails.address.zipCode}"
        if (soldProductDetails.address.additionalNote.isNotEmpty()) {
            binding.txtAddressAdditionalSoldProductsDetails.visibility = View.VISIBLE
            binding.txtAddressAdditionalSoldProductsDetails.text =
                soldProductDetails.address.additionalNote
        } else {
            binding.txtAddressAdditionalSoldProductsDetails.visibility = View.GONE

        }
        if (soldProductDetails.address.othersDetails.isNotEmpty()) {
            binding.txtOtherDetailsSoldProductsDetails.visibility = View.VISIBLE
            binding.txtOtherDetailsSoldProductsDetails.text =
                soldProductDetails.address.othersDetails
        } else {
            binding.txtOtherDetailsSoldProductsDetails.visibility = View.GONE
        }
        binding.txtPhoneNumberSoldProductsDetails.text = soldProductDetails.address.mobileNumber

        binding.txtSubtotalValueSoldProductDetails.text = soldProductDetails.sub_total_amount
        binding.txtShippingChargeValueSoldProductDetails.text = soldProductDetails.shipping_charge
        binding.txtTotalAmountValueSoldProductDetails.text = soldProductDetails.total_amount
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarOrderDetails)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarOrderDetails.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}