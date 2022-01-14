package com.example.myshop.activities

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private lateinit var progressDialog: Dialog
    private var doubleBackToExistPressedOnce = false

    fun showProgressDialog(text: String) {
        progressDialog = Dialog(this)
        /* Set the screen content from a layout resource
        the resource will be inflated, adding all top-level views to the screen */
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.findViewById<TextView>(R.id.txt_progress_dialog).text = text
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        //display the dialog
        progressDialog.show()
    }

    fun hideProgressBar() {
        progressDialog.dismiss()
    }

    fun fullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackbar_error_color
                )
            )
            snackBar.setActionTextColor(resources.getColor(R.color.black))
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.snackbar_success_color
                )
            )
        }
        snackBar.show()
    }

    fun doubleBackToExist() {
        if (doubleBackToExistPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExistPressedOnce = true
        Toast.makeText(this, "Press back button again to exists",Toast.LENGTH_LONG).show()
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            doubleBackToExistPressedOnce = false
        }, 3000)
    }
}