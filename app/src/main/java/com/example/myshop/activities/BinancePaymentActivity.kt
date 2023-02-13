package com.example.myshop.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.example.myshop.databinding.ActivityBinancePaymentBinding
import com.example.myshop.utils.binance.BinanceConnection
import org.json.JSONException

class BinancePaymentActivity : AppCompatActivity() {
   private lateinit var binding: ActivityBinancePaymentBinding
   private lateinit var binanceConnection: BinanceConnection
   private lateinit var javaBinanceConnection: JavaBinanceConnection

    //    var qrCodeImg: ImageView? = null
//    var openButton: Button? = null
//    var br: ProgressBar? = null
//    var toolbarthanhtoan: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinancePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binanceConnection = BinanceConnection(this)
        javaBinanceConnection = JavaBinanceConnection(this)
        try {
            getPayment()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.apply {
            btnPayNow.setOnClickListener {
                btnPayNow.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                try {
                    getPayment2()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        setupToolbarAction()
    }

    private fun setupToolbarAction() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Throws(JSONException::class)
    fun getPayment() {
        val adv = Integer.toString((Math.random() * 1000).toInt())
        var ProductName = "test"
        var totalPrice = 100
        //TODO set price of product here
        try {
            binanceConnection.getQRlink(adv, totalPrice, ProductName, binding.imgQrCode)
        } catch (e: AuthFailureError) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    fun getPayment2() {
        val adv = Integer.toString((Math.random() * 1000).toInt())
        var productName = ""
        var totalPrice = 100
        //TODO leave price here
        try {
            binanceConnection.openApp(adv, totalPrice, productName)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}