package com.android.mediproject

import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnEnd
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.WindowViewModel
import com.android.mediproject.core.ui.base.BaseActivity
import com.android.mediproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    private val windowViewModel: WindowViewModel by viewModels()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    companion object {
        const val VISIBLE = 0
        const val INVISIBLE = 1
    }

    override val activityViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun afterBinding() {
        systemBarStyler.init(this, window)
        systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.WHITE, SystemBarStyler.NavigationBarColor.BLACK)

        //SDK 31이상일 때 Splash가 소소하게 사라지는 이펙트 입니다. 추후 걸리적거리면 삭제해도 됌
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f).run {
                    interpolator = AnticipateInterpolator()
                    duration = 1000L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }

        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
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

            root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (bottomNav.marginBottom == 0) {
                        systemBarStyler.changeMode(emptyList(),
                            listOf(SystemBarStyler.ChangeView(bottomNav, SystemBarStyler.SpacingType.MARGIN)))
                        return true
                    }

                    if (bottomAppBar.height > 0 && bottomNav.marginBottom == systemBarStyler.navigationBarHeightPx) {
                        root.viewTreeObserver.removeOnPreDrawListener(this)
                        windowViewModel.bottomNavHeightInPx = bottomAppBar.height
                        setFragmentContainerFullHeight(false)
                    }
                    return true
                }
            })
        }
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
    private fun setFragmentContainerFullHeight(isFull: Boolean) {
        if (windowViewModel.bottomNavHeight.value > 0) {
            binding.fragmentContainerView.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                if (isFull) ViewGroup.LayoutParams.MATCH_PARENT else (binding.root.height - windowViewModel.bottomNavHeight.value))
        }
    }

    override fun setSplash() {
        installSplashScreen()
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
    private fun setDestinationListener() = navController.addOnDestinationChangedListener { _, destination, arg ->
        log(arg.toString())
        bottomVisible(destination.id !in hideBottomNavDestinationIds)
    }

    private fun bottomVisible(visible: Boolean) {
        log(visible.toString())
        binding.cameraFAB.isVisible = visible
        binding.bottomAppBar.isVisible = visible
        setFragmentContainerFullHeight(!visible)
    }

    fun handleEvent(event: MainViewModel.MainEvent) = when (event) {
        is MainViewModel.MainEvent.AICamera -> navController.navigate("medilens://main/camera_nav".toUri())
    }

}