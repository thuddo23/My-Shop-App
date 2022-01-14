package com.example.myshop.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityUserProfileBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.fragments.MainActivity
import com.example.myshop.models.User
import com.example.myshop.utils.Constant
import com.example.myshop.utils.GlideLoader
import java.lang.Exception

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        fullScreen()
        userDetails = User()
        if (intent.hasExtra(Constant.EXTRA_USERS_DETAILS)) {
            userDetails = intent.getParcelableExtra(Constant.EXTRA_USERS_DETAILS)!!
        }
        if (userDetails.profileCompleted != 0) {
            setUpActionBar()
        }
        //pass user details to editTexts and ImageView, etc ...
        binding.editFirstNameUserProfile.setText(userDetails.firstName)
        binding.editLastNameUserProfile.setText(userDetails.lastName)
        binding.editEmailUserProfile.setText(userDetails.email)
        if (userDetails.mobile != 0L) {
            binding.editMobileNumberUserProfile.setText(userDetails.mobile.toString())
        }
        if (userDetails.gender == Constant.FEMALE) {
            binding.btnGenderFemaleProfile.isChecked = true
        }
        GlideLoader(this@UserProfileActivity).loaderPicture(
            userDetails.image,
            binding.imgUserProfile
        )
        binding.imgUserProfile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constant.showImageChooser(this@UserProfileActivity)


            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constant.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        //SAVE BUTTON
        binding.btnSaveProfile.setOnClickListener {
            if (validateUserProfileDetails()) {
                if (mSelectedImageFileUri != null) {
                    FireStoreClass().uploadImageToCloudStorage(
                        this@UserProfileActivity,
                        mSelectedImageFileUri,
                        Constant.USER_PROFILE_IMAGE
                    )
                } else {
                    updateUserProfileDetails()
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        val firstName = binding.editFirstNameUserProfile.text.toString().trim()
        val lastName = binding.editLastNameUserProfile.text.toString().trim()
        val mobileNumber = binding.editMobileNumberUserProfile.text.toString()
        val gender = if (binding.btnGenderMaleProfile.isChecked) {
            Constant.MALE
        } else {
            Constant.FEMALE
        }

        userHashMap
        if (mobileNumber.isNotEmpty() && mobileNumber.toLong() != userDetails.mobile) {
            userHashMap[Constant.MOBILE_PHONE] = mobileNumber.toLong()
        }
        if (gender.isNotEmpty() && gender != userDetails.gender) {
            userHashMap[Constant.GENDER] = gender
        }
        if (firstName != userDetails.firstName) {
            userHashMap[Constant.FIRST_NAME] = firstName
        }
        if (lastName != userDetails.lastName) {
            userHashMap[Constant.LAST_NAME] = lastName
        }
        if (mUserProfileImageUrl.isNotEmpty() && mUserProfileImageUrl != userDetails.image) {
            userHashMap[Constant.IMAGE] = mUserProfileImageUrl
        }
        userHashMap[Constant.PROFILE_COMPLETED] = 1
//        upload img to fireStorage
        FireStoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)
            .addOnCompleteListener {
                Handler().postDelayed(
                    {
                        startActivity(
                            Intent(
                                this@UserProfileActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    },
                    10
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        //The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        val glideLoader = GlideLoader(this@UserProfileActivity)
                        glideLoader.loaderPicture(mSelectedImageFileUri!!, binding.imgUserProfile)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@UserProfileActivity,
                            e.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            binding.editFirstNameUserProfile.text.toString().trim().length < 2 -> {
                showErrorSnackBar(resources.getString(R.string.error_valid_first_name), true)
                false
            }
            binding.editLastNameUserProfile.text.toString().trim().length < 2 -> {
                showErrorSnackBar(resources.getString(R.string.error_valid_last_name), true)
                false
            }
            binding.editLastNameUserProfile.text.toString().trim().isEmpty() -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_phone_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageUrl = imageURL
        updateUserProfileDetails()
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarProfileUser)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_ios_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarProfileUser.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}