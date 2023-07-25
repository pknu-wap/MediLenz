package com.android.mediproject.core.common.util

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.android.mediproject.core.common.R
import com.android.mediproject.core.common.uiutil.SystemBarColorAnalyzer

@Navigator.Name("savingfragment")
class SavingFragmentNavigator(
    @IdRes private val fragmentContainerId: Int,
    private val fragmentManager: FragmentManager,
    private val systemBarColorAnalyzer: SystemBarColorAnalyzer,
) : Navigator<SavingFragmentNavigator.Destination>() {

    override fun createDestination(): Destination = Destination(this)

    private companion object {
        @get:Synchronized val fragmentTags = mutableSetOf<String>()
    }

    init {
        fragmentTags.clear()
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ): NavDestination? {
        val destinationTag = destination.id.toString()
        val showingFragment = fragmentManager.primaryNavigationFragment
        var fragmentInstance = fragmentManager.findFragmentByTag(destinationTag)
        var initializing = false

        fragmentManager.commit {
            if (showingFragment != null) hide(showingFragment)

            if (fragmentInstance != null) {
                show(fragmentInstance!!)
            } else {
                initializing = true
                fragmentInstance = fragmentManager.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), destination.className)
                add(
                    fragmentContainerId,
                    fragmentInstance!!,
                    destinationTag,
                )
            }

            setPrimaryNavigationFragment(fragmentInstance!!)
            setReorderingAllowed(true)
        }

        return if (initializing) {
            destination
        } else {
            systemBarColorAnalyzer.convert()
            null
        }
    }


    @NavDestination.ClassType(Fragment::class)
    class Destination(navigator: SavingFragmentNavigator) : NavDestination(navigator) {
        private var _className: String? = null
        val className get() = _className!!

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)
            _className = context.resources.obtainAttributes(attrs, R.styleable.SavingFragmentNavigator).run {
                val name = getString(R.styleable.SavingFragmentNavigator_android_name)
                recycle()
                name
            }

            fragmentTags.add(id.toString())
        }
    }
}
