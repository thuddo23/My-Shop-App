package com.example.myshop.activities

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.adapter.CartAdapter
import com.example.myshop.databinding.ActivityMyOrderDetailsBinding
import com.example.myshop.models.Order
import com.example.myshop.utils.Constant
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityMyOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_details)
        fullScreen()
        setUpActionBar()

        var myOrderDetails = Order()
        if (intent.hasExtra(Constant.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra(Constant.EXTRA_MY_ORDER_DETAILS)!!
        }
        setupUI(myOrderDetails)

    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarOrderDetails)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
        actionBar.setDisplayShowTitleEnabled(false)
        binding.toolbarOrderDetails.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupUI(order: Order) {
        binding.txtOrderIdValueOrderDetails.text = order.id
        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = order.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        binding.txtOrderDateValueOrderDetails.text = orderDateTime
        val diffInMilliseconds: Long = System.currentTimeMillis() - order.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliseconds)
        when{
            diffInHours < 1 -> {
                binding.txtOrderStatusValueOrderDetails.text = resources.getString(R.string.order_status_pending)
                binding.txtOrderStatusValueOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.snackbar_error_color
                    )
                )
            }
            diffInHours < 2 -> {
                binding.txtOrderStatusValueOrderDetails.text = resources.getString(R.string.order_status_in_process)
                binding.txtOrderStatusValueOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.accent_yellow_color
                    )
                )
            }
            else -> {
                binding.txtOrderStatusValueOrderDetails.text = resources.getString(R.string.order_status_in_process)
                binding.txtOrderStatusValueOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.accent_green_color
                    )
                )
            }
        }
        binding.recyclerViewProductItemsOrderDetails.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProductItemsOrderDetails.adapter = CartAdapter(this, order.items, false)
        binding.txtNameOrderDetails.text = order.address.name
        binding.txtTypeOrderDetails.text = order.address.type
        binding.txtAddressDetailsOrderDetails.text =
            "${order.address.address}, ${order.address.zipCode}"
        binding.txtAddressAdditionalOrderDetails.text = order.address.additionalNote
        if (order.address.othersDetails.isNotEmpty()) {
            binding.txtOtherDetailsOrderDetails.visibility = View.VISIBLE
            binding.txtOtherDetailsOrderDetails.text = order.address.othersDetails
        } else {
            binding.txtOtherDetailsOrderDetails.visibility = View.GONE
        }
        binding.txtPhoneNumberOrderDetails.text = order.address.mobileNumber

        binding.txtShippingChargeValueOrderDetails.text = order.shipping_chart
        binding.txtTotalAmountValueOrderDetails.text = order.total_amount
        binding.txtSubtotalValueOrderDetails.text = order.sub_total_amount
    }
}