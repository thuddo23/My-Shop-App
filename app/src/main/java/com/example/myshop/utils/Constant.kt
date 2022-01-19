package com.example.myshop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

object Constant {

    //COLLECTIONS
    const val USERS = "users"
    const val PRODUCTS = "Products"
    const val ORDERS = "orders"
    const val SOLD_PRODUCTS: String = "sold_products"
    const val MYSHOP_PREFERENCES: String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USERS_DETAILS: String = "extra_users_details"
    const val READ_STORAGE_PERMISSION_CODE: Int = 1
    const val PICK_IMAGE_REQUEST_CODE: Int = 2

    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val MALE = "male"
    const val FEMALE = "female"
    const val MOBILE_PHONE = "mobile"
    const val GENDER = "gender"
    const val IMAGE = "image"
    const val PROFILE_COMPLETED = "profileCompleted"

    const val PRODUCT_IMAGE = "Product_Image"

    const val USER_PROFILE_IMAGE = "user_profile_image"

    const val USER_ID = "user_id"

    const val PRODUCT_ID = "product_id"

    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID = "extra_product_OWNER_id"
    const val EXTRA_ADDRESS_DETAILS = "extra_address_details"
    const val EXTRA_SELECT_ADDRESS = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS = "extra_selected_address"
    const val EXTRA_MY_ORDER_DETAILS = "extra_my_order_details"
    const val EXTRA_SOLD_PRODUCT_DETAILS = "extra_sold_product_details"
    const val ADD_ADDRESS_REQUEST_CODE = 121

    const val DEFAULT_CART_QUANTITY = "1"
    const val CART_QUANTITY = "cart_quantity"
    const val STOCK_QUANTITY = "stock_quantity"

    const val CART_ITEMS = "cart_items"

    const val HOME = "Home"
    const val OFFICE = "Office"
    const val OTHER = "Other"

    const val ADDRESS = "address"

    fun showImageChooser(activity: Activity) {
        //An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //launching the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


}