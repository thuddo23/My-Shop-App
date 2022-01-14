package com.example.myshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.example.myshop.activities.*
import com.example.myshop.fragments.DashboardFragment
import com.example.myshop.fragments.ProductsFragment
import com.example.myshop.models.Address
import com.example.myshop.models.CartItem
import com.example.myshop.models.Product
import com.example.myshop.models.User
import com.example.myshop.utils.Constant
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.security.cert.PKIXRevocationChecker

class FireStoreClass {

    private val mFireStore = Firebase.firestore

    fun registerUser(activity: RegisterActivity, user: User): Task<Void> {
        // the "users" is collection name. If the collection is already created then it will not create the same one again.
        return mFireStore.collection(Constant.USERS)
            //document ID for users fields. Here the document it is the User ID.
            .document(user.id)
            //Here the user are Field and the setOption is the set to merge. It is for if we want to merge later on instead of replacing the fields.
            .set(user, SetOptions.merge())
            .addOnSuccessListener { task ->
                Log.e("RESULT", "success")
            }
            .addOnFailureListener { task ->
                Log.e("RESULT", task.message.toString())
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = Firebase.auth.currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getUserDetails(activity: Activity) {
        //here we pass the collection name from which we want to get data.
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { it ->
                Log.i(activity.javaClass.simpleName, it.toString())
                val user = it.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constant.MYSHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                //key - value : logged_in_username - Thuan Do
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    .putString(
                        Constant.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                    )
                editor.apply()

                //START
                when (activity) {
                    is LoginActivity -> activity.userLoggedInSuccess(user)
                    is SettingActivity -> activity.getUserDetailsSuccess(user)
                    is AddProductActivity -> activity.getUserName(user)
                }
            }
    }

    fun updateUserProfileData(activity: Activity, hashMap: HashMap<String, Any>): Task<Void> {
        return mFireStore.collection(Constant.USERS)
            .document(getCurrentUserID())
            .update(hashMap)
            .addOnSuccessListener {
                if (activity is UserProfileActivity) {
                    activity.showErrorSnackBar("Update the user details successful.", false)
                }
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Error while updating the user details", it)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imgType: String) {
        val imgExtension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(imageFileURI!!))
        val sRef = Firebase.storage.reference.child(
            imgType + System.currentTimeMillis() + "." + imgExtension
        )
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapShot ->
                taskSnapShot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->

                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(url.toString())
                            }
                            is AddProductActivity -> {
                                activity.imageUploadSuccess(url.toString())

                            }
                        }
                    }
            }
    }

    fun upLoadProductDetails(activity: AddProductActivity, productInfo: Product) {
        mFireStore.collection(Constant.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.finish()
            }
    }

    fun getProductsList(fragment: Fragment) {
        mFireStore.collection(Constant.PRODUCTS)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.successProductListFromFireStore(productsList)
                    }
                }
            }
    }

    fun getDashboardItemsList(fragment: Fragment) {
        mFireStore.collection(Constant.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                val dashboardItems: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    dashboardItems.add(product)
                }
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.successDashboardItemsList(dashboardItems)
                    }
                }
            }
    }

    fun deleteProduct(productsFragment: ProductsFragment, productId: String) {
        mFireStore.collection(Constant.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                productsFragment.successDeleteFromFireStore()
            }
    }

    fun getProductDetails(productDetailsActivity: ProductDetailsActivity, productId: String) {
        mFireStore.collection(Constant.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener {
                val product: Product? = it.toObject(Product::class.java)
                if (product != null) {
                    productDetailsActivity.successGetProductDetails(product)
                }
            }
    }

    fun addCartItems(productDetailsActivity: ProductDetailsActivity, cartItem: CartItem) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document()
            .set(cartItem, SetOptions.merge())
            .addOnSuccessListener {
                productDetailsActivity.successAddToCart()
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productID: String) {
        mFireStore.collection(Constant.CART_ITEMS)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .whereEqualTo(Constant.PRODUCT_ID, productID)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.documents.size > 0) {
                    activity.productExistsInCart()
                }
            }
    }

    fun getCartList(cartListActivity: CartListActivity) {
        mFireStore.collection(Constant.CART_ITEMS)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                var cartList = ArrayList<CartItem>()
                for (i in document.documents) {
                    var cartItem: CartItem? = i.toObject(CartItem::class.java)
                    cartItem!!.id = i.id
                    cartList.add(cartItem)
                }

                cartListActivity.successGetCartList(cartList)
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> context.successRemoveCartItem()
                }
            }
    }

    fun updateCartItem(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> context.successUpdateCartItem()
                }
            }
    }

    fun getAllProducts(cartListActivity: CartListActivity) {
        mFireStore.collection(Constant.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                var productList = ArrayList<Product>()

                for (i in document.documents) {
                    var product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productList.add(product)
                }
                cartListActivity.successGetAllProducts(productList)
            }
    }

    fun addAddress(context: AddEditAddressActivity, address: Address) {
        mFireStore.collection(Constant.ADDRESS)
            .document()
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                context.successAddAddress()
            }
    }


    fun getAddressList(context: AddressListActivity) {
        mFireStore.collection(Constant.ADDRESS)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                if (document.documents.size > 0) {
                    val addressList = ArrayList<Address>()
                    for (i in document.documents) {
                        val address = i.toObject(Address::class.java)
                        address!!.id = i.id
                        addressList.add(address)
                    }

                    when (context) {
                        is AddressListActivity -> context.successGetAddress(addressList)
                    }
                }
            }
    }

    fun updateAddress(context:AddEditAddressActivity, address_id : String, addressInfo :Address){
        mFireStore.collection(Constant.ADDRESS)
            .document(address_id)
            .set(addressInfo,SetOptions.merge())
            .addOnSuccessListener {
                context.successAddAddress()
            }
    }

}