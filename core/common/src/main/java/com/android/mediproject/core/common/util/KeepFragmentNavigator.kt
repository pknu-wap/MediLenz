package com.android.mediproject.core.common.util

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.content.res.use
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.NavigatorState
import com.android.mediproject.core.common.R
import java.lang.ref.WeakReference

@Navigator.Name("keep_fragment")
class KeepFragmentNavigator(
    private val systemBarColorAnalyzer: SystemBarColorAnalyzer,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int,
) : Navigator<KeepFragmentNavigator.Destination>() {

    private val savedIds = mutableSetOf<String>()

    /**
     * List of entries that were popped by direct calls to popBackStack (i.e. from NavController)
     */
    internal val entriesToPop: Set<String>
        get() = (state.transitionsInProgress.value - state.backStack.value.toSet()).map { it.id }.toSet()

    /**
     * Get the back stack from the [state].
     */
    private val backStack get() = state.backStack
    private fun createFragmentTransaction(
        entry: NavBackStackEntry,
    ): FragmentTransaction {
        val destination = entry.destination as Destination
        var className = destination.className
        if (className[0] == '.') className = context.packageName + className

        val fragmentTransaction = fragmentManager.beginTransaction()
        val currentFragment = fragmentManager.primaryNavigationFragment
        var destFragment = fragmentManager.findFragmentByTag(entry.id)

        if (destFragment?.isVisible == true) fragmentTransaction.hide(destFragment)

        if (destFragment == null) {
            destFragment = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
            destFragment.arguments = entry.arguments
            fragmentTransaction.add(containerId, destFragment, entry.id)
        } else {
            fragmentTransaction.show(destFragment)
        }

        if (currentFragment != null) {
            val isKeep = state.backStack.value.find { it.id == currentFragment.tag }?.destination?.navigatorName == "keep_fragment"
            if (isKeep) {
                fragmentTransaction.hide(currentFragment)
                systemBarColorAnalyzer.convert()
            } else {
                fragmentTransaction.remove(currentFragment)
            }
        }

        fragmentTransaction.setPrimaryNavigationFragment(destFragment)
        fragmentTransaction.setReorderingAllowed(true)
        return fragmentTransaction
    }


    private val fragmentObserver = LifecycleEventObserver { source, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            val fragment = source as Fragment
            val entry = state.transitionsInProgress.value.lastOrNull { entry ->
                entry.id == fragment.tag
            }
            if (entry != null) {
                if (!state.backStack.value.contains(entry)) {
                    state.markTransitionComplete(entry)
                }
            }
        }
    }

    private val fragmentViewObserver = { entry: NavBackStackEntry ->
        LifecycleEventObserver { _, event ->
            // Once the lifecycle reaches RESUMED, if the entry is in the back stack we can mark
            // the transition complete
            if (event == Lifecycle.Event.ON_RESUME && state.backStack.value.contains(entry)) {
                state.markTransitionComplete(entry)
            }
            // Once the lifecycle reaches DESTROYED, if the entry is not in the back stack, we can
            // mark the transition complete
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (!state.backStack.value.contains(entry)) {
                    state.markTransitionComplete(entry)
                }
            }
        }
    }

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)

        fragmentManager.addFragmentOnAttachListener { _, fragment ->
            val entry = state.backStack.value.lastOrNull { it.id == fragment.tag }
            if (entry != null) {
                attachObservers(entry, fragment)
                // We need to ensure that if the fragment has its state saved and then that state
                // later cleared without the restoring the fragment that we also clear the state
                // of the associated entry.
                attachClearViewModel(fragment, entry, state)
            }
        }

        fragmentManager.addOnBackStackChangedListener(
            object : FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {}

                override fun onBackStackChangeStarted(fragment: Fragment, pop: Boolean) {
                    // We only care about the pop case here since in the navigate case by the time
                    // we get here the fragment will have already been moved to STARTED.
                    // In the case of a pop, we move the entries to STARTED
                    if (pop) {
                        val entry = state.backStack.value.lastOrNull { it.id == fragment.tag }
                        entry?.let { state.prepareForTransition(it) }
                    }
                }

                override fun onBackStackChangeCommitted(fragment: Fragment, pop: Boolean) {
                    val entry = (state.backStack.value + state.transitionsInProgress.value).lastOrNull {
                        it.id == fragment.tag
                    }
                    if (!pop) {
                        requireNotNull(entry) {
                            "The fragment $fragment is unknown to the FragmentNavigator. Please use the navigate() function to add fragments to the FragmentNavigator managed FragmentManager."
                        }
                    }
                    if (entry != null) {
                        // In case we get a fragment that was never attached to the fragment manager,
                        // we need to make sure we still return the entries to their proper final state.
                        attachClearViewModel(fragment, entry, state)
                        if (pop) {
                            // This is the case of system back where we will need to make the call to
                            // popBackStack. Otherwise, popBackStack was called directly and this should
                            // end up being a no-op.
                            if (entriesToPop.isEmpty() && fragment.isRemoving) {
                                state.popWithTransition(entry, false)
                            }
                        }
                    }
                }
            },
        )
    }

    private fun attachObservers(entry: NavBackStackEntry, fragment: Fragment) {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { owner ->
            // attach observer unless it was already popped at this point
            if (owner != null && !entriesToPop.contains(fragment.tag)) {
                val viewLifecycle = fragment.viewLifecycleOwner.lifecycle
                // We only need to add observers while the viewLifecycle has not reached a final
                // state
                if (viewLifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    viewLifecycle.addObserver(fragmentViewObserver(entry))
                }
            }
        }
        fragment.lifecycle.addObserver(fragmentObserver)
    }

    internal fun attachClearViewModel(
        fragment: Fragment,
        entry: NavBackStackEntry,
        state: NavigatorState,
    ) {
        val viewModel = ViewModelProvider(
            fragment.viewModelStore,
            viewModelFactory { initializer { ClearEntryStateViewModel() } },
            CreationExtras.Empty,
        )[ClearEntryStateViewModel::class.java]
        viewModel.completeTransition = WeakReference {
            entry.let {
                state.transitionsInProgress.value.forEach { entry ->
                    state.markTransitionComplete(entry)
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * This method must call
     * [FragmentTransaction.setPrimaryNavigationFragment]
     * if the pop succeeded so that the newly visible Fragment can be retrieved with
     * [FragmentManager.getPrimaryNavigationFragment].
     *
     * Note that the default implementation pops the Fragment
     * asynchronously, so the newly visible Fragment from the back stack
     * is not instantly available after this call completes.
     */
    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state",
            )
            return
        }
        val beforePopList = state.backStack.value
        // Get the set of entries that are going to be popped
        val poppedList = beforePopList.subList(
            beforePopList.indexOf(popUpTo),
            beforePopList.size,
        )
        if (savedState) {
            val initialEntry = beforePopList.first()
            // Now go through the list in reversed order (i.e., started from the most added)
            // and save the back stack state of each.
            for (entry in poppedList.reversed()) {
                if (entry == initialEntry) {
                    Log.i(
                        TAG,
                        "FragmentManager cannot save the state of the initial destination $entry",
                    )
                } else {
                    fragmentManager.saveBackStack(entry.id)
                    savedIds += entry.id
                }
            }
        } else {
            fragmentManager.popBackStack(
                popUpTo.id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE,
            )
        }
        state.popWithTransition(popUpTo, savedState)
    }

    override fun createDestination(): Destination = Destination(this)


    /**
     * {@inheritDoc}
     *
     * This method should always call
     * [FragmentTransaction.setPrimaryNavigationFragment]
     * so that the Fragment associated with the new destination can be retrieved with
     * [FragmentManager.getPrimaryNavigationFragment].
     *
     * Note that the default implementation commits the new Fragment
     * asynchronously, so the new Fragment is not instantly available
     * after this call completes.
     *
     * This call will be ignored if the FragmentManager state has already been saved.
     */
    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?,
    ) {
        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already saved its state",
            )
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?,
    ) {
        val initialNavigation = state.backStack.value.isEmpty()
        val restoreState = (navOptions != null && !initialNavigation && navOptions.shouldRestoreState() && savedIds.remove(entry.id))
        if (restoreState) {
            // Restore back stack does all the work to restore the entry
            fragmentManager.restoreBackStack(entry.id)
            state.pushWithTransition(entry)
            return
        }
        val ft = createFragmentTransaction(entry)

        if (!initialNavigation) {
            ft.addToBackStack(entry.id)
        }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.commit()
        // The commit succeeded, update our view of the world
        state.pushWithTransition(entry)
    }

    /**
     * {@inheritDoc}
     *
     * This method should always call
     * [FragmentTransaction.setPrimaryNavigationFragment]
     * so that the Fragment associated with the new destination can be retrieved with
     * [FragmentManager.getPrimaryNavigationFragment].
     *
     * Note that the default implementation commits the new Fragment
     * asynchronously, so the new Fragment is not instantly available
     * after this call completes.
     *
     * This call will be ignored if the FragmentManager state has already been saved.
     */
    override fun onLaunchSingleTop(backStackEntry: NavBackStackEntry) {
        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG,
                "Ignoring onLaunchSingleTop() call: FragmentManager has already saved its state",
            )
            return
        }
        val ft = createFragmentTransaction(backStackEntry)
        if (state.backStack.value.size > 1) {
            // If the Fragment to be replaced is on the FragmentManager's
            // back stack, a simple replace() isn't enough so we
            // remove it from the back stack and put our replacement
            // on the back stack in its place
            fragmentManager.popBackStack(
                backStackEntry.id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE,
            )
            ft.addToBackStack(backStackEntry.id)
        }
        ft.commit()
        // The commit succeeded, update our view of the world
        state.onLaunchSingleTop(backStackEntry)
    }

    override fun onSaveState(): Bundle? {
        if (savedIds.isEmpty()) {
            return null
        }
        return bundleOf(KEY_SAVED_IDS to ArrayList(savedIds))
    }

    override fun onRestoreState(savedState: Bundle) {
        val savedIds = savedState.getStringArrayList(KEY_SAVED_IDS)
        if (savedIds != null) {
            this.savedIds.clear()
            this.savedIds += savedIds
        }
    }

    @NavDestination.ClassType(Fragment::class)
    open class Destination(fragmentNavigator: Navigator<out Destination>) : NavDestination(fragmentNavigator) {

        /**
         * Construct a new fragment destination. This destination is not valid until you set the
         * Fragment via [setClassName].
         *
         * @param navigatorProvider The [NavController] which this destination
         * will be associated with.
         */
        constructor(navigatorProvider: NavigatorProvider) : this(navigatorProvider.getNavigator(KeepFragmentNavigator::class.java))

        @CallSuper
        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)
            context.resources.obtainAttributes(attrs, R.styleable.KeepFragmentNavigator).use { array ->
                val className = array.getString(R.styleable.KeepFragmentNavigator_android_name)
                if (className != null) setClassName(className)
            }
        }

        /**
         * Set the Fragment class name associated with this destination
         * @param className The class name of the Fragment to show when you navigate to this
         * destination
         * @return this [Destination]
         */
        fun setClassName(className: String): Destination {
            _className = className
            return this
        }

        private var _className: String? = null

        /**
         * The Fragment's class name associated with this destination
         *
         * @throws IllegalStateException when no Fragment class was set.
         */
        val className: String
            get() {
                checkNotNull(_className) { "Fragment class was not set" }
                return _className as String
            }

        override fun toString(): String {
            val sb = StringBuilder()
            sb.append(super.toString())
            sb.append(" class=")
            if (_className == null) {
                sb.append("null")
            } else {
                sb.append(_className)
            }
            return sb.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Destination) return false
            return super.equals(other) && _className == other._className
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + _className.hashCode()
            return result
        }
    }

    /**
     * Extras that can be passed to FragmentNavigator to enable Fragment specific behavior
     */
    class Extras internal constructor(sharedElements: Map<View, String>) : Navigator.Extras {
        private val _sharedElements = LinkedHashMap<View, String>()

        /**
         * The map of shared elements associated with these Extras. The returned map
         * is an [unmodifiable][Map] copy of the underlying map and should be treated as immutable.
         */
        val sharedElements: Map<View, String>
            get() = _sharedElements.toMap()

        /**
         * Builder for constructing new [Extras] instances. The resulting instances are
         * immutable.
         */
        class Builder {
            private val _sharedElements = LinkedHashMap<View, String>()

            /**
             * Adds multiple shared elements for mapping Views in the current Fragment to
             * transitionNames in the Fragment being navigated to.
             *
             * @param sharedElements Shared element pairs to add
             * @return this [Builder]
             */
            fun addSharedElements(sharedElements: Map<View, String>): Builder {
                for ((view, name) in sharedElements) {
                    addSharedElement(view, name)
                }
                return this
            }

            /**
             * Maps the given View in the current Fragment to the given transition name in the
             * Fragment being navigated to.
             *
             * @param sharedElement A View in the current Fragment to match with a View in the
             * Fragment being navigated to.
             * @param name The transitionName of the View in the Fragment being navigated to that
             * should be matched to the shared element.
             * @return this [Builder]
             * @see FragmentTransaction.addSharedElement
             */
            fun addSharedElement(sharedElement: View, name: String): Builder {
                _sharedElements[sharedElement] = name
                return this
            }

            /**
             * Constructs the final [Extras] instance.
             *
             * @return An immutable [Extras] instance.
             */
            fun build(): Extras {
                return Extras(_sharedElements)
            }
        }

        init {
            _sharedElements.putAll(sharedElements)
        }
    }

    private companion object {
        private const val TAG = "KeepFragmentNavigator"
        private const val KEY_SAVED_IDS = "androidx-nav-fragment:navigator:savedIds"
    }

    private class ClearEntryStateViewModel : ViewModel() {
        lateinit var completeTransition: WeakReference<() -> Unit>
        override fun onCleared() {
            super.onCleared()
            completeTransition.get()?.invoke()
        }
    }
}
