package com.example.myshop.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.activities.AddProductActivity
import com.example.myshop.activities.SettingActivity
import com.example.myshop.adapter.ProductAdapter
import com.example.myshop.databinding.FragmentProductsBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Product

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getProductsListFromFireStore()

    }

    fun getProductsListFromFireStore() {
        FireStoreClass().getProductsList(this)
    }

    fun successProductListFromFireStore(productsList: ArrayList<Product>) {
        if (productsList.size > 0) {
            binding.recycleViewProducts.visibility = View.VISIBLE
            binding.txtNoProducts.visibility = View.GONE
            binding.recycleViewProducts.layoutManager = LinearLayoutManager(activity)
            binding.recycleViewProducts.setHasFixedSize(true)
            var adapter = ProductAdapter(requireContext(), productsList)
            adapter.setTrashOnClickListener {   product ->
                showAlertDialogToDeleteProduct(product.product_id)
            }
            binding.recycleViewProducts.adapter = adapter


        } else {
            binding.recycleViewProducts.visibility = View.GONE
            binding.txtNoProducts.visibility = View.VISIBLE
        }
    }

    fun successDeleteFromFireStore() {
        FireStoreClass().getProductsList(this)
    }

    private fun showAlertDialogToDeleteProduct(productId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            FireStoreClass().deleteProduct(this, productId)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}