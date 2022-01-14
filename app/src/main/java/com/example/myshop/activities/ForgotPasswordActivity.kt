package com.example.myshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myshop.R
import com.example.myshop.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        fullScreen()
        binding.btnBackForgot.setOnClickListener { onBackPressed() }
        binding.btnSubmitForgot.setOnClickListener {
            val email = binding.editNewPasswordForgot.text.toString().trim()
            if (email == "") {
                Toast.makeText(this, "Please enter your username or e-mail address!", Toast.LENGTH_LONG).show()
            } else {
                var auth = Firebase.auth
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar("Email sent successfully to reset your password. Check your e-mail!", false)
                        @Suppress("DEPRECATION")
                        Handler().postDelayed({
                            finish()//should be closed - onDestroy
                        }, 1500)
                    } else {
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}