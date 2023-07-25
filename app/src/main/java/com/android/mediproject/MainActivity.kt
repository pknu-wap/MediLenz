package com.android.mediproject

import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUiSaveStateControl
import androidx.navigation.ui.setupWithNavController
import com.android.mediproject.core.common.uiutil.LayoutController
import com.android.mediproject.core.common.uiutil.SystemBarColorAnalyzer
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.SavingFragmentNavigator
import com.android.mediproject.core.network.InternetNetworkListener
import com.android.mediproject.core.ui.WindowViewModel
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.lastOrNull
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate), LayoutController {

    private val windowViewModel: WindowViewModel by viewModels()

    @Inject lateinit var layoutController: LayoutController
    @Inject lateinit var systemBarController: SystemBarController
    @Inject lateinit var internetNetworkListener: InternetNetworkListener
    @Inject lateinit var systemBarColorAnalyzer: SystemBarColorAnalyzer

    override val activityViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController


    @OptIn(NavigationUiSaveStateControl::class)
    override fun afterBinding() {
        systemBarController.init(this, window, this)
        systemBarColorAnalyzer.init(this, systemBarController, lifecycle)

        internetNetworkListener.activityLifeCycle = lifecycle
        internetNetworkListener.networkStateCallback = InternetNetworkListener.NetworkStateCallback { isConnected ->
            if (!isConnected) {
                NetworkStateDialogFragment().show(supportFragmentManager, NetworkStateDialogFragment::class.java.name)
            }
        }

        binding.apply {
            initNav()
            viewModel = activityViewModel.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
            }

            root.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        if (bottomNav.marginBottom == 0) {
                            systemBarController.changeMode(
                                emptyList(),
                                listOf(SystemBarStyler.ChangeView(bottomNav, SystemBarStyler.SpacingType.MARGIN)),
                            )
                        }

                        if (bottomAppBar.height > 0 && bottomNav.marginBottom == systemBarController.navigationBarHeightPx) {
                            root.viewTreeObserver.removeOnPreDrawListener(this)
                            windowViewModel.bottomNavHeightInPx = bottomAppBar.height
                            this@MainActivity.changeFragmentContainerHeight(false)
                        }
                        return true
                    }
                },
            )

            //DevDialogFragment().show(supportFragmentManager, DevDialogFragment::class.java.name)
        }
    }

    override fun setSplash() {
        installSplashScreen()
    }

    private fun initNav() {
        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(fragmentContainerView.id) as NavHostFragment
            navController = navHostFragment.navController

            bottomNav.apply {
                itemIconTintList = null
                background = null
                menu.getItem(2).isEnabled = false

                navController.navigatorProvider.addNavigator(
                    SavingFragmentNavigator(
                        fragmentContainerView.id,
                        navHostFragment.childFragmentManager,
                        systemBarColorAnalyzer,
                    ),
                )

                navController.setGraph(R.navigation.main_nav)
                bottomNav.setupWithNavController(navController)
            }
            setDestinationListener()

            repeatOnStarted {
                activityViewModel.selectedBottomNavFragmentId.lastOrNull()?.let { lastSelectedId ->
                    //bottomNav.selectedItemId = lastSelectedId
                }
            }
        }
    }

    private fun setDestinationListener() {
        val hideBottomNavDestinationIds = activityViewModel.getHideBottomNavDestinationIds(resources)
        navController.addOnDestinationChangedListener { _, destination, arg ->
            log(arg.toString())
            bottomVisible(destination.id !in hideBottomNavDestinationIds)
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    systemBarColorAnalyzer.convert()
                }
            },
            true,
        )
    }

    private fun bottomVisible(visible: Boolean) {
        log(visible.toString())
        binding.cameraFAB.isVisible = visible
        binding.bottomAppBar.isVisible = visible
        this.changeFragmentContainerHeight(!visible)
    }

    fun handleEvent(event: MainViewModel.MainEvent) = when (event) {
        is MainViewModel.MainEvent.AICamera -> navController.navigate("medilens://main/camera_nav".toUri())
    }

    /**
     * fragmentContainerView의 높이를 조정해주는 함수
     *
     * CoordinatorLayout으로 인해 fragmentContainerView의 높이가 앱 전체의 높이로 되어있는데
     * isFull true를 전달받으면 fragmentContainerView의 bottom좌표가 bottomNav의 top좌표가 되고,
     * isFull false를 전달받으면 fragmentContainerView의 bottom좌표가 앱 전체의 bottom좌표가 된다.
     *
     * @param isFull true: 전체화면, false: 전체화면X
     */
    override fun changeFragmentContainerHeight(isFull: Boolean) {
        if (windowViewModel.bottomNavHeight.value > 0) {
            binding.fragmentContainerView.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = if (!isFull) (windowViewModel.bottomNavHeight.value) else 0
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(activityViewModel.lastSelectedBottomNavFragmentIdKey, binding.bottomNav.selectedItemId)
    }

}
