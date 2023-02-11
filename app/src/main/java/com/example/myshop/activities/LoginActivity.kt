package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityLoginBinding
import com.example.myshop.firestore.FireStoreClass
import com.example.myshop.fragments.MainActivity
import com.example.myshop.models.User
import com.example.myshop.utils.Constant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        fullScreen()
        binding.txtRegisterLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.txtForgetLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }

    private fun validLogin(): Boolean {
        return when {
            binding.editAccountLogin.text.toString().trim().length < 2 -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_email), true)
                false
            }
            binding.editPassword.text.toString().trim() == "" -> {
                showErrorSnackBar(resources.getString(R.string.error_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginUser() {
        if (validLogin()) {
            val email = binding.editAccountLogin.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            val auth = Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    val firebaseUser = task.result.user
                    FireStoreClass().getUserDetails(this@LoginActivity)
                } else {
                    showErrorSnackBar(resources.getString(R.string.login_failed), true)
                }
            }
        }
    }

    fun userLoggedInSuccess(user: User) {
        showErrorSnackBar(resources.getString(R.string.login_successful), false)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (user.profileCompleted == 0) {
                val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                intent.putExtra(Constant.EXTRA_USERS_DETAILS, user)
                startActivity(intent)
            } else {
                //Hide the progress dialog.

                //Print the user details in the log as of now.
                //Redirect the user to Main Screen after log in.
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

                //In login screen the clickable components are Login Button, ForgotPassword text and Register Text.
            }
        }
    }

    fun userLoggedFail(message: String?) {
        showErrorSnackBar((resources.getString(R.string.login_failed) + message) ?: "", true)
    }
}