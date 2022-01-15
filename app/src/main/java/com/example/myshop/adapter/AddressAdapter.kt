package com.example.myshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.activities.AddEditAddressActivity
import com.example.myshop.models.Address
import com.example.myshop.utils.Constant

class AddressAdapter(private val context: Context, private var addressList: ArrayList<Address>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_address, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var address = addressList[position]
        if (holder is ViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.txt_name_address_layout).text = address.name
            holder.itemView.findViewById<TextView>(R.id.txt_phone_address_layout).text =
                address.mobileNumber
            holder.itemView.findViewById<TextView>(R.id.txt_address_details_address_layout).text =
                address.address
            holder.itemView.findViewById<TextView>(R.id.txt_type_address_layout).text = address.type

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(address)
                }
            }
        }
    }

    fun notifyEditItem(context: Context, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constant.EXTRA_ADDRESS_DETAILS, addressList[position])
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    private var onItemClickListener: ((Address) -> Unit)? = null

    fun setOnItemClickListener(onClick: ((Address) -> Unit)) {
        onItemClickListener = onClick
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}