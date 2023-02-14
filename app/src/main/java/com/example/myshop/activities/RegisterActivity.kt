package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityRegisterBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : BaseActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fullScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.txtLoginRegister.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
        binding.toolbarRegister.setOnClickListener { onBackPressed() }
        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
        }
    }

    private fun registerUser() {
        showProgressDialog("Please wait ..")
        val email = binding.editEmailRegister.text.toString().trim()
        val password = binding.editPasswordRegister.text.toString().trim()
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showErrorSnackBar(resources.getString(R.string.notify_valid_details), false)
                val firebaseUser = task.result.user
                val user = User(
                    firebaseUser!!.uid,
                    binding.editFirstNameRegister.text.toString().trim(),
                    binding.editLastNameRegister.text.toString().trim(),
                    email
                )

                val task = FireStoreClass().registerUser(this@RegisterActivity, user)
                task.addOnSuccessListener {
                    auth.signOut()
                    finish()
                }
            } else {
                showErrorSnackBar(
                    (resources.getString(R.string.register_failed) + task.exception?.message) , true)
            }
        }
        hideProgressBar()
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            binding.editFirstNameRegister.text.toString().trim().length < 2 -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_first_name), true)
                false
            }
            binding.editLastNameRegister.text.toString().trim().length < 2 -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_last_name), true)
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editEmailRegister.text.toString().trim())
                .matches() -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_email), true)
                false
            }
            binding.editPasswordRegister.text.toString().trim().length < 8 -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_password_8_chars), true)
                false
            }
            binding.editConfirmPassword.text.toString().trim() != binding.editPasswordRegister.text.toString().trim() -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_confirm_password_mismatch), true)
                false
            }
            !binding.checkBoxRegister.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.error_agree_terms_and_condition), true)
                false
            }
            else -> {
                registerUser()
                true
            }
        }
    }

//    private fun setupActionBar() {
//
//        setSupportActionBar( binding.toolbarRegister)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setDisplayShowHomeEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
//        }
//        binding.toolbarRegister.setNavigationOnClickListener { onBackPressed() }
//    }
}

