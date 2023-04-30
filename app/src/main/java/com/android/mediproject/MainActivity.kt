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

    companion object {
        const val VISIBLE = 0
        const val INVISIBLE = 1
    }

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
            setUpBottomNav()

            viewModel = activityViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }


    private fun setUpBottomNav() =
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> true.apply {
                    log("홈")
                    navController.navigate(MainNavDirections.actionToHomeNav())
                }

                R.id.community_nav -> true.apply {
                    log("커뮤니티")
                    navController.navigate(MainNavDirections.actionToCommunityNav())
                }

                R.id.mypage_nav -> true.apply {
                    log("마이페이지")
                    navController.navigate(MainNavDirections.actionToMypageNav())
                }

                R.id.setting_nav -> true.apply {
                    log("설정")
                    navController.navigate(MainNavDirections.actionToSettingNav())
                }

                else -> false
            }
        }

    private fun setDestinationListener() =
        navController.addOnDestinationChangedListener { _, _, arg ->
            log(arg.toString())
            if (arg != null) {
                if (arg.isEmpty) {
                    bottomVisible(VISIBLE)
                } else if (arg.getBoolean(getString(com.android.mediproject.core.ui.R.string.hideBottom))) {
                    bottomVisible(INVISIBLE)
                } else {
                    bottomVisible(VISIBLE)
                }
            } else {
                bottomVisible(VISIBLE)
            }
        }

    private fun bottomVisible(isVisible: Int) {
        log(isVisible.toString())
        binding.bottomNav.visibility = when (isVisible) {
            VISIBLE -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun handleEvent(event: MainViewModel.MainEvent) = when (event) {
        else -> {}
    }

}