package com.example.myshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivitySettingBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        fullScreen()
        setUpActionBar()
        FireStoreClass().getUserDetails(this)

        binding.btnLogoutSetting.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.txtEditProfileSetting.setOnClickListener {
            val intent = Intent(this@SettingActivity, UserProfileActivity::class.java)
            intent.putExtra(Constant.EXTRA_USERS_DETAILS, mUserDetails)
            startActivity(intent)
        }
        binding.txtAddressTxt.setOnClickListener {
            startActivity(Intent(this@SettingActivity, AddressListActivity::class.java))
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarSetting)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
        }
        binding.toolbarSetting.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun getUserDetailsSuccess(user: User) {
        mUserDetails = user
        GlideLoader(this).loaderPicture(user.image, binding.imgUserProfileSetting)
        binding.txtNameSetting.text = "${user.firstName} ${user.lastName}"
        binding.txtGmailSetting.text = user.email
        binding.txtNumberSetting.text = user.mobile.toString()
        binding.txtGenderSetting.text = user.gender
    }


    override fun onResume() {
        super.onResume()
        FireStoreClass().getUserDetails(this)
    }
}