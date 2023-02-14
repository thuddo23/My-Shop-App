package com.example.myshop.utils.binance

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class BinanceConnection(private val context: Context) {
    companion object {
        const val BASE_URL = "https://bpay.binanceapi.com/binancepay/openapi/v2/order"
    }

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val key = arrayOf(
        "mhrahnj583jov5y6rrni8wf3ocmnu18xa5dg6guaoxaljjejolq2lv4hn5qbrrrk",
        "l2kfohwgtukk2ez3lwyw5ys04wppoabtrnkdxuewcah9wmzoyiz2euyrxtzjp4j5"
    )
    private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun getQRLink(
        orderID: String,
        price: Int,
        productName: String,
        onQrRequestCompleted: (String?) -> Unit
    ) {
        var none = ""
        for (i in 0..31) {
            val chart =
                chars[Math.round(Math.random() * (chars.length - 1)).toInt()]
            none += chart
        }
        val timestamp = System.currentTimeMillis()
        val body = providesBody(productName, orderID, price)

        val signature = providesSignature(key[1], none, timestamp, body)
        Log.d("body", body.toString())
        val finalNone = none
        Log.d("sig:", signature)
        println(finalNone)
        val objectRequestBinance: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, BASE_URL, body, { response ->
                val img: String
                try {
                    Log.d("Response", response.toString())
                    val res1: JSONObject = response.getJSONObject("data")
                    img = res1.getString("qrcodeLink").toString()
                    Log.d("img qr code", img)
                    onQrRequestCompleted(img)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onQrRequestCompleted(null)
                }
            }, { error ->
                onQrRequestCompleted(null)
                val response: NetworkResponse = error.networkResponse
                Log.d("erron", response.statusCode.toString())
                Log.d("erron", response.headers.toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val ha: MutableMap<String, String> = java.util.LinkedHashMap()
                    ha["content-type"] = "application/json"
                    ha["BinancePay-Timestamp"] = java.lang.Long.toString(timestamp)
                    ha["BinancePay-Nonce"] = finalNone
                    ha["BinancePay-Certificate-SN"] = key[0]
                    ha["BinancePay-Signature"] = signature
                    return ha
                }
            }
        requestQueue.add(objectRequestBinance)
    }

    @Throws(AuthFailureError::class, JSONException::class)
    fun openApp(orderID: String, price: Int, productName: String) {
        val url = "https://bpay.binanceapi.com/binancepay/openapi/v2/order"
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var none = ""
        for (i in 0..31) {
            val chart =
                chars[Math.round(Math.random() * (chars.length - 1)).toInt()]
            none += chart
        }
        val body = providesBody(productName, orderID, price)
        val timestamp = System.currentTimeMillis()
        val signature = providesSignature(key[1], none, timestamp, body)
        Log.d("body1", body.toString())
        val result = ""
        val finalNone = none
        Log.d("final signature:", signature)
        val objectRequestBinance: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, body, { response ->
                val deepLink: String
                try {
                    Log.d("Response", response.toString())
                    val res1: JSONObject = response.getJSONObject("data")
                    deepLink = res1.getString("deeplink").split("returnLink".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0]
                    val binance = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                    context.startActivity(binance)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { error ->
                val response: NetworkResponse = error.networkResponse
                Log.d("request error", response.headers.toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    return LinkedHashMap<String, String>().also {
                        it["content-type"] = "application/json"
                        it["BinancePay-Timestamp"] = timestamp.toString()
                        it["BinancePay-Nonce"] = finalNone
                        it["BinancePay-Certificate-SN"] = key[0]
                        it["BinancePay-Signature"] = signature
                    }
                }
            }
        requestQueue.add(objectRequestBinance)
    }

    private fun providesBody(productName: String, orderID: String, price: Int): JSONObject {
        val body = JSONObject()
        val terminalJson = JSONObject().apply { put("terminalType", "APP") }
        val goodJson = JSONObject().apply {
            put("goodsType", "01")
            put("goodsCategory", "0000")
            put("referenceGoodsId", "abc001")
            put("goodsName", productName)
        }
        return body.apply {
            put("env", terminalJson)
            put("merchantTradeNo", orderID)
            put("orderAmount", price.toString())
            put("currency", "USDT")
            put("goods", goodJson)
        }
    }

    private fun providesSignature(
        key: String,
        none: String,
        timestamp: Long,
        body: JSONObject
    ): String {

        val payload = """
                $timestamp
                $none
                $body
                
                """.trimIndent()
        return Signature().getSignature(payload, key).toUpperCase()
    }
}