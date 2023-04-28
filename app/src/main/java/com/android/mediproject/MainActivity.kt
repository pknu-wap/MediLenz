package com.android.mediproject

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding

class MainActivity() : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun afterBinding() {
        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNav.setupWithNavController(navController)
        }
    }
}