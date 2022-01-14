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

class ProductAdapter(private val context: Context, private val products: ArrayList<Product>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        if (holder is ViewHolder) {
            GlideLoader(context).loaderProductPicture(product.image, holder.itemView.findViewById(R.id.img_product_item))
            val txtName: TextView = holder.itemView.findViewById(R.id.txt_name_product_item)
            txtName.text = product.title
            val txtDescription: TextView = holder.itemView.findViewById(R.id.txt_description_product_item)
            txtDescription.text = product.description
            val txtPrices: TextView = holder.itemView.findViewById(R.id.txt_prices_product_item)
            txtPrices.text = product.price + " $"
            val imgRemoveProduct: ImageView = holder.itemView.findViewById(R.id.img_remove_product)
            imgRemoveProduct.setOnClickListener {
                trashOnClick?.let {
                    it(product)
                }
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context,ProductDetailsActivity::class.java)
                intent.putExtra(Constant.EXTRA_PRODUCT_ID,product.product_id)
                intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        //holds the views that will add each item to
    }

    private var trashOnClick: ((product: Product) -> Unit)? = null

    fun setTrashOnClickListener(listener: (Product) -> Unit) {
        trashOnClick = listener
    }
}