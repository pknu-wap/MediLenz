package com.android.mediproject

import android.annotation.SuppressLint
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import com.android.mediproject.feature.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    @SuppressLint("CommitTransaction")
    override fun afterBinding() {
        binding.apply {
            /*
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNav.setupWithNavController(navController)

             */

            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commit()
        }
    }

}