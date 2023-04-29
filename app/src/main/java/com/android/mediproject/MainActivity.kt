package com.android.mediproject

import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(ActivityMainBinding::inflate) {

    override val activityViewModel: MainViewModel by viewModels()

    override fun afterBinding() {
        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNav.apply{
                itemIconTintList = null
                setupWithNavController(navController)
            }

            viewModel = activityViewModel.apply{
                repeatOnStarted { eventFlow.collect{ handleEvent(it)} }
            }
        }
    }

    fun handleEvent(event : MainViewModel.MainEvent) = when(event){
        else -> {}
    }

}