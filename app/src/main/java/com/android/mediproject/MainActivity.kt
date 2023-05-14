package com.android.mediproject

import android.view.View
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
                background = null
                menu.getItem(2).isEnabled = false
            }
            R.array.hideBottomNavDestinationIds
            setDestinationListener()

            viewModel = activityViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }
        }
    }


    private val hideBottomNavDestinationIds: Set<Int> by lazy {
        resources.obtainTypedArray(R.array.hideBottomNavDestinationIds).let { typedArray ->
            val destinationIds = mutableSetOf<Int>()
            for (i in 0 until typedArray.length()) {
                destinationIds.add(typedArray.getResourceId(i, 0))
            }
            typedArray.recycle()
            destinationIds
        }
    }


    /**
     * <2번째 방법>
     *
     * 1번째 방법은 nav_graph.xml 에서 destination 에서 argument 를 추가해주고
     *
     * 2번째 방법은 navController.addOnDestinationChangedListener 에서
     *
     * destination.id 를 통해 destination 을 구분하고
     *
     * argument 를 통해 bottomNav 를 숨길지 말지 결정한다.
     */
    private fun setDestinationListener() =
        navController.addOnDestinationChangedListener { _, destination, arg ->
            log(arg.toString())
            if (destination.id in hideBottomNavDestinationIds) {
                bottomVisible(INVISIBLE)
            } else {
                bottomVisible(VISIBLE)
            }
        }

    private fun bottomVisible(isVisible: Int) {
        log(isVisible.toString())
        binding.cameraFAB.visibility = when (isVisible) {
            VISIBLE -> View.VISIBLE
            else -> View.GONE
        }
        binding.bottomAppBar.visibility = when (isVisible) {
            VISIBLE -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun handleEvent(event: MainViewModel.MainEvent) = when (event) {
        is MainViewModel.MainEvent.AICamera -> navController.navigate("medilens://main/camera_nav".toUri())
    }

}