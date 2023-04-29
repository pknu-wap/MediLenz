package com.android.mediproject

import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    override val activityViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun afterBinding() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.navController

            bottomNav.apply {
                itemIconTintList = null
                setupWithNavController(navController)
            }

            setDestinationListener()

            viewModel = activityViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }


    private fun setDestinationListener() =
        navController.addOnDestinationChangedListener { _, _, arg ->
            log(arg.toString())
            if (arg != null) {
                if (arg.isEmpty) {
                    bottomVisible(true)
                } else if (arg.getBoolean(getString(com.android.mediproject.core.ui.R.string.hide_bottom))) {
                    bottomVisible(false)
                }
            } else {
                bottomVisible(true)
            }
        }

    private fun bottomVisible(isVisible: Boolean) {
        binding.bottomNav.visibility = if (isVisible) View.VISIBLE
        else View.GONE
    }

    fun handleEvent(event: MainViewModel.MainEvent) = when (event) {
        else -> {}
    }

}