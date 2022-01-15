package com.example.myshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.activities.CartListActivity
import com.example.myshop.activities.ProductDetailsActivity
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader

class CartAdapter(private val context: Context, private val cartItems: List<CartItem>,private val updateCartItem:Boolean = true) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_cart_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        GlideLoader(context).loaderProductPicture(
            cartItem.image,
            (holder as ViewHolder).imgCartItem
        )

        (holder as ViewHolder).txtTitle.text = cartItem.title
        (holder as ViewHolder).txtPrices.text = "$" + cartItem.price
        (holder as ViewHolder).txtQuantityCartItem.text = cartItem.cart_quantity
        if (cartItem.cart_quantity == "0") {
            (holder as ViewHolder).imgMinusCartItem.visibility = View.GONE
            (holder as ViewHolder).imgAddCartItem.visibility = View.GONE

            (holder as ViewHolder).txtQuantityCartItem.text =
                context.getString(R.string.out_of_stock)
            (holder as ViewHolder).txtQuantityCartItem.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.snackbar_error_color
                )
            )
        } else {
            (holder as ViewHolder).imgMinusCartItem.visibility = View.VISIBLE
            (holder as ViewHolder).imgAddCartItem.visibility = View.VISIBLE
            (holder as ViewHolder).txtQuantityCartItem.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.secondary_text
                )
            )
        }

        (holder as ViewHolder).imgDeleteCartItem.setOnClickListener {
            FireStoreClass().removeItemFromCart(context, cartItem.id)
        }

        (holder as ViewHolder).imgAddCartItem.setOnClickListener {
            if (cartItem.cart_quantity.toInt() < cartItem.stock_quantity.toInt()) {
                val cartQuantity = cartItem.cart_quantity.toInt()
                val newCartQuantity = cartQuantity + 1
                val itemHashMap = HashMap<String, Any>()
                itemHashMap[Constant.CART_QUANTITY] = newCartQuantity.toString()
                FireStoreClass().updateCartItem(context, cartItem.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    context.showErrorSnackBar(context.resources.getString(R.string.msg_for_available_stock,cartItem.stock_quantity), true)
                }
            }
        }
        (holder as ViewHolder).imgMinusCartItem.setOnClickListener {
            if (cartItem.cart_quantity == Constant.DEFAULT_CART_QUANTITY) {
                FireStoreClass().removeItemFromCart(context, cartItem.id)
            } else {
                val cartQuantity = cartItem.cart_quantity.toInt()
                val newCartQuantity = cartQuantity - 1
                var itemHashMap = HashMap<String, Any>()

                itemHashMap[Constant.CART_QUANTITY] = newCartQuantity.toString()
                FireStoreClass().updateCartItem(context, cartItem.id, itemHashMap)
            }
        }

        if(!updateCartItem){
            (holder as ViewHolder).imgDeleteCartItem.visibility = View.GONE
            (holder as ViewHolder).imgMinusCartItem.visibility = View.GONE
            (holder as ViewHolder).imgAddCartItem .visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var imgCartItem = view.findViewById<ImageView>(R.id.img_cart_item)
        var imgDeleteCartItem = view.findViewById<ImageView>(R.id.img_delete_cart_item)
        var imgAddCartItem = view.findViewById<ImageView>(R.id.img_add_cart_item)
        var imgMinusCartItem = view.findViewById<ImageView>(R.id.img_minus_cart_item)
        var txtTitle = view.findViewById<TextView>(R.id.txt_title_cart_item)
        var txtPrices = view.findViewById<TextView>(R.id.txt_prices_cart_item)
        var txtQuantityCartItem = view.findViewById<TextView>(R.id.txt_cart_item_quantity)
    }
}