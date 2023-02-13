package com.example.myshop.utils.binance

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject

class BinanceConnection(private val context: Context) {
    private val requestQueue: RequestQueue
    private val key = arrayOf(
        "mhrahnj583jov5y6rrni8wf3ocmnu18xa5dg6guaoxaljjejolq2lv4hn5qbrrrk","l2kfohwgtukk2ez3lwyw5ys04wppoabtrnkdxuewcah9wmzoyiz2euyrxtzjp4j5"
        )

    init {
        requestQueue = Volley.newRequestQueue(context)
    }

    fun getQRlink(orderID: String, price: Int, productName: String, imV: ImageView) {
        val url = "https://bpay.binanceapi.com/binancepay/openapi/v2/order"
        val sig = Signature()
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var none = ""
        var signature = ""
        for (i in 0..31) {
            val chart =
                chars[Math.round(Math.random() * (chars.length - 1)).toInt()]
            none += chart
        }
        val timestamp = System.currentTimeMillis()
        val jsonbody = JSONObject()
        val json2 = JSONObject()
        val json3 = JSONObject()
        json3.put("goodsType", "01")
        json3.put("goodsCategory", "0000")
        json3.put("referenceGoodsId", "abc001")
        json3.put("goodsName", productName)
        json2.put("terminalType", "APP")
        jsonbody.put("env", json2)
        jsonbody.put("merchantTradeNo", orderID)
        jsonbody.put("orderAmount", "1.00")
        jsonbody.put("currency", "BUSD")
        jsonbody.put("goods", json3)
        val payload = timestamp.toString()+"\n"+none+"\n"+jsonbody.toString()+"\n";

        signature = sig.getSignature(payload, key[1]).toUpperCase()
        Log.d("body1", jsonbody.toString())
        val result = ""
        val finalNone = none
        val finalSignature = signature
        Log.d("sig:", finalSignature)
        println(finalNone)
        val jsrq: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonbody, { response ->
                val img: String
                try {
                    Log.d("Response", response.toString())
                    val res1: JSONObject = response.getJSONObject("data")
                    img = res1.getString("qrcodeLink").toString()
                    Log.d("imf", img)
                    Glide.with(context).load(img).into(imV!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { error ->
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
                    ha["BinancePay-Signature"] = finalSignature
                    return ha
                }
            }
        requestQueue.add(jsrq)
    }

    @Throws(AuthFailureError::class, JSONException::class)
    fun openApp(orderID: String?, gia: Int, productName: String?) {
        val url = "https://bpay.binanceapi.com/binancepay/openapi/v2/order"
        val sig = Signature()
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var none = ""
        var signature = ""
        for (i in 0..31) {
            val chart =
                chars[Math.round(Math.random() * (chars.length - 1)).toInt()]
            none += chart
        }
        val timestamp = System.currentTimeMillis()
        val jsonbody = JSONObject()
        val json2 = JSONObject()
        val json3 = JSONObject()
        json3.put("goodsType", "01")
        json3.put("goodsCategory", "0000")
        json3.put("referenceGoodsId", "abc001")
        json3.put("goodsName", productName)
        json2.put("terminalType", "APP")
        jsonbody.put("env", json2)
        jsonbody.put("merchantTradeNo", orderID)
        jsonbody.put("orderAmount", Integer.toString(gia))
        jsonbody.put("currency", "USDT")
        jsonbody.put("goods", json3)
        val payload = """
                $timestamp
                $none
                $jsonbody
                
                """.trimIndent()
        signature = sig.getSignature(payload, key[1]).toUpperCase()
        Log.d("body1", jsonbody.toString())
        val result = ""
        val finalNone = none
        val finalSignature = signature
        Log.d("sig:", finalSignature)
        println(finalNone)
        val jsrq2: JsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonbody, { response ->
                var img: String
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
                Log.d("erron", response.headers.toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    return LinkedHashMap<String, String>().also { ha ->
                        ha["content-type"] = "application/json"
                        ha["BinancePay-Timestamp"] = timestamp.toString()
                        ha["BinancePay-Nonce"] = finalNone
                        ha["BinancePay-Certificate-SN"] = key[0]
                        ha["BinancePay-Signature"] = finalSignature
                    }
                }
            }
        requestQueue.add(jsrq2)
    }
}