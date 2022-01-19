package com.example.myshop.fragments

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myshop.R
import com.example.myshop.activities.BaseActivity
import com.example.myshop.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // TODO Step 1: Update the background color of the action bar as per our design requirement.
        // START
        // Update the background color of the action bar as per our design requirement.
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.background_gradient
            )
        )

        // END

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        val navController: NavController = navHostFragment.navController

        //SET UP ACTION BAR
        //passing each menu id as a set of Ids because
        //each menu be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment,
                R.id.productsFragment,
                R.id.ordersFragment,
                R.id.soldProductFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        doubleBackToExist()
    }

}