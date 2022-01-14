package com.example.myshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.activities.ProductDetailsActivity
import com.example.myshop.models.Product
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader

class DashboardAdapter(private val context: Context, private val products: List<Product>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.product_item_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        GlideLoader(context).loaderProductPicture(product.image, (holder as ViewHolder).imgProduct)
        (holder as ViewHolder).txtTitle.text = product.title
        (holder as ViewHolder).txtPrices.text = "$" + product.price
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constant.EXTRA_PRODUCT_ID, product.product_id)
            intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID, product.user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var imgProduct = view.findViewById<ImageView>(R.id.img_product_dashboard)
        var txtTitle = view.findViewById<TextView>(R.id.txt_product_title_dashboard)
        var txtPrices = view.findViewById<TextView>(R.id.txt_product_price_dashboard)
    }
}
