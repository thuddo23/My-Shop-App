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
import com.example.myshop.activities.SoldProductDetailsActivity
import com.example.myshop.models.Product
import com.example.myshop.models.SoldProduct
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader

class SoldProductAdapter(
    private val context: Context,
    private val soldProducts: ArrayList<SoldProduct>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val soldProduct = soldProducts[position]
        if (holder is ViewHolder) {
            GlideLoader(context).loaderProductPicture(
                soldProduct.image,
                holder.itemView.findViewById(R.id.img_product_item)
            )
            val txtName: TextView = holder.itemView.findViewById(R.id.txt_name_product_item)
            txtName.text = soldProduct.title
            val txtDescription: TextView = holder.itemView.findViewById(R.id.txt_description_product_item)
            txtDescription.text = soldProduct.sold_quantity
            val txtPrices: TextView = holder.itemView.findViewById(R.id.txt_prices_product_item)
            txtPrices.text = soldProduct.price + "$"
            val imgRemoveProduct: ImageView = holder.itemView.findViewById(R.id.img_remove_product)
            imgRemoveProduct.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val intent = Intent(context,SoldProductDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_SOLD_PRODUCT_DETAILS,soldProduct)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return soldProducts.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //holds the views that will add each item to
    }
}