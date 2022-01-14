package com.example.myshop.activities


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myshop.R
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.databinding.ActivityAddProductBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.Product
import com.example.myshop.models.User
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader
import java.lang.Exception

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddProductBinding
    private var mUserName = ""
    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product)
        fullScreen()
        setUpActionBar()
        binding.setupImgAddProduct.setOnClickListener { onClick(it) }
        binding.btnSubmitAddProduct.setOnClickListener { onClick(it) }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.setup_img_add_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constant.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constant.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_submit_add_product -> {
                    upLoadProductImage()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        //The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        val glideLoader = GlideLoader(this)
                        glideLoader.loaderPicture(
                            mSelectedImageFileUri!!,
                            binding.imgProductAddProduct
                        )
                        binding.setupImgAddProduct.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_baseline_edit_24
                            )
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            e.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.READ_STORAGE_PERMISSION_CODE) {
            if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showErrorSnackBar("Your permission is granted", false)
            } else {
                showErrorSnackBar("you've just denied the permission for camera.", true)
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarAddProduct)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
        actionBar!!.setDisplayShowTitleEnabled(false)
        actionBar!!.title = getString(R.string.add_product)
        actionBar!!.setBackgroundDrawable(
            ActivityCompat.getDrawable(
                this,
                R.drawable.background_gradient
            )
        )
        binding.toolbarAddProduct.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun validProductDetails(): Boolean {
        return when {
            mSelectedImageFileUri == null -> {
                showErrorSnackBar(getString(R.string.error_product_img), true)
                false
            }
            binding.productTitleAddProduct.text.toString().trim().length < 8 -> {
                showErrorSnackBar(getString(R.string.error_product_title), true)
                return false
            }
            binding.productPriceAddProduct.text.toString().trim().isEmpty() -> {
                showErrorSnackBar(getString(R.string.error_product_price), true)
                false
            }
            binding.productDescriptionAddProduct.text.toString().trim().isEmpty() -> {
                showErrorSnackBar(getString(R.string.error_product_description), true)
                false
            }
            binding.productQuantityAddProduct.text.toString().toLong() <= 0 -> {
                showErrorSnackBar(getString(R.string.error_product_quantity), true)
                false
            }
            else -> true
        }
    }

    fun upLoadProductSuccess() {
        finish()
    }

    private fun upLoadProductDetails() {
        val product = Product(
            FireStoreClass().getCurrentUserID(),
            mUserName,
            binding.productTitleAddProduct.text.toString().trim(),
            binding.productPriceAddProduct.text.toString().trim(),
            binding.productDescriptionAddProduct.text.toString().trim(),
            binding.productQuantityAddProduct.text.toString().trim(),
            mProductImageUrl
        )
        FireStoreClass().upLoadProductDetails(this,product)
    }

    private fun upLoadProductImage() {
        FireStoreClass().uploadImageToCloudStorage(
            this,
            mSelectedImageFileUri,
            Constant.PRODUCT_IMAGE
        )
    }

    fun imageUploadSuccess(url: String) {
        mProductImageUrl = url
        showErrorSnackBar(url, false)
        FireStoreClass().getUserDetails(this)
    }

    fun getUserName(user: User) {
        mUserName = "${user.firstName} ${user.lastName}"
        upLoadProductDetails()
    }


}