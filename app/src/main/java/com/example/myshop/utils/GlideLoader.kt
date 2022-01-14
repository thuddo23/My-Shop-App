package com.example.myshop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import java.net.URL

class GlideLoader(val context: Context) {
    fun loaderPicture(imgUri: Any, imgView: ImageView) {
        try {
            Glide.with(context)
                .load(Uri.parse(imgUri.toString()))// URI of img file
                .centerCrop()//scale type
                .placeholder(R.drawable.user)//A default place holder if img is failed to load.
                .into(imgView)//the view in which the image will be loaded.
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


        fun loaderProductPicture(imgUri: Any, imgView: ImageView) {
            try {
                Glide.with(context)
                        .load(Uri.parse(imgUri.toString()))// URI of img file
                        .centerCrop()//scale type
                        .into(imgView)//the view in which the image will be loaded.
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun loaderPicture(imgUrl: String, imgView: ImageView) {
        try {
            Glide.with(context)
                .load(imgUrl)// URI of img file
                .centerCrop()//scale type
                .placeholder(R.drawable.user)//A default place holder if img is failed to load.
                .into(imgView)//the view in which the image will be loaded.
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}