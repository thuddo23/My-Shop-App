package com.example.myshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Order
import com.example.myshop.utils.GlideLoader

class OrderAdapter(private val context: Context, private val orderList: List<Order>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = orderList[position]
        GlideLoader(context).loaderProductPicture(order.image, (holder as ViewHolder).imgOrder)
        (holder as ViewHolder).txtTitle.text = order.title
        (holder as ViewHolder).txtTotal.text = "$" + order.total_amount
        (holder as ViewHolder).imgDelete.visibility = View.GONE
        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it(order)
            }
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var imgOrder = view.findViewById<ImageView>(R.id.img_product_item)
        var txtTitle = view.findViewById<TextView>(R.id.txt_name_product_item)
        var txtTotal = view.findViewById<TextView>(R.id.txt_prices_product_item)
        var imgDelete = view.findViewById<ImageView>(R.id.img_remove_product)
    }

    private var onItemClick: ((Order) -> Unit)? = null

    fun setOnItemClickListener(onClick: (Order) -> Unit) {
        onItemClick = onClick
    }
}