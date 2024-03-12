package com.example.enuyguncase.navigation

import android.util.SparseArray
import androidx.core.net.toUri
import androidx.core.util.forEach
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MultiNavHost(
    private val navGraphIds: List<Int>,
    private val containerId: Int,
    private val primaryFragmentIndex: Int,
    private val fragmentManager: FragmentManager,
    private val multiNavHostBackStackPolicy: MultiNavHostBackStackPolicy,
    private val multipleBackStackCount: Int = navGraphIds.size,
    private val bottomNavigationView: BottomNavigationView,
): BackPressedListener{
    // Map of tags
    private val graphIdToTagMap: SparseArray<String> = SparseArray<String>()

    private var primaryFragmentId: Int = 0

    private var selectedNavGraphId: Int = 0

    private var lastRetainedBackStackEntry: String? = null

    private var navController: NavController? = null

    private var navHistory = BottomNavHistory()

    private var navGraphStartDestinations = mutableMapOf<Int, Int>()

    fun create(selectedNavId: Int) {
        isMultiNavHostInitialized = true
        selectedNavGraphId = selectedNavId
        navGraphStartDestinations.clear()
        navGraphIds.forEachIndexed { index, navGraphId ->
            val fragmentTag: String = getFragmentTag(index)

            // Find or create the Navigation host fragment
            val navHostFragment = obtainNavHostFragment(
                fragmentTag = fragmentTag,
                navGraphId = navGraphId
            )
            // Obtain its id
            val graphId = navHostFragment.navController.graph.id

            if (index == 0) {
                navHistory.push(graphId)
            }
            navGraphStartDestinations[graphId] =
                navHostFragment.navController.graph.startDestinationId

            if (index == primaryFragmentIndex) primaryFragmentId = graphId

            // Save to the map
            graphIdToTagMap[graphId] = fragmentTag

            if (primaryFragmentId == graphId) {
                attachNavHostFragment(navHostFragment)
                navController = navHostFragment.navController
            } else {
                detachNavHostFragment(navHostFragment)
            }
        }
    }

    fun selectSiblings(navGraphId: Int): Boolean {
        // Don't do anything if the state is state has already been saved.
        return if (fragmentManager.isStateSaved) {
            false
        } else {
            navHistory.push(navGraphId)
            val newlySelectedItemTag = graphIdToTagMap[navGraphId]
            val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                    as NavHostFragment
            swapBackStackEntry(
                fragmentTag = newlySelectedItemTag,
                navHostFragment = selectedFragment
            )
            selectedNavGraphId = navGraphId
            navController = selectedFragment.navController
            true
        }
    }

    fun reselectSiblings(navGraphId: Int) {
        val newlySelectedItemTag = graphIdToTagMap[navGraphId]
        val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                as NavHostFragment
        val navController = selectedFragment.navController
        // Pop the back stack to the start destination of the current navController graph
        navController.popBackStack(navController.graph.startDestinationId, false)
    }

    fun observeBackStack(onStackChange: (Int) -> Unit) {
        fragmentManager.addOnBackStackChangedListener {
            navController?.let { navController ->
                // Reset the graph if the currentDestination is not valid (happens when the back
                // stack is popped after using the back button).
                if (navController.currentDestination == null) {
                    navController.navigate(navController.graph.id)
                } else {
                    when (multiNavHostBackStackPolicy) {
                        MultiNavHostBackStackPolicy.NO_BACK_STACK -> Unit
                        MultiNavHostBackStackPolicy.PRIMARY_BACK_STACK -> {
                            val newBackStackId = fragmentManager.currentBackStackId()

                            if (newBackStackId == null) onStackChange(primaryFragmentId)

                        }
                        MultiNavHostBackStackPolicy.MULTIPLE_BACK_STACK -> {
                            val isStackReset = validateStackAndReset()

                            if (isStackReset) {
                                onStackChange(primaryFragmentId)
                            } else {
                                val newBackStackId = fragmentManager.currentBackStackId()
                                if (newBackStackId != null) onStackChange(newBackStackId)
                            }

                        }
                    }
                }
            }
        }
    }

    private fun obtainNavHostFragment(
        fragmentTag: String,
        navGraphId: Int
    ): NavHostFragment {
        // If the Nav Host fragment exists, return it
        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
        existingFragment?.let { return it }

        // Otherwise, create it and return it.
        val navHostFragment = NavHostFragment.create(navGraphId)
        fragmentManager.beginTransaction()
            .add(containerId, navHostFragment, fragmentTag)
            .commitNow()

        return navHostFragment
    }

    override fun onBackPressed(): Boolean {
        return if (navHistory.isNotEmpty) {
            navController?.let {
                if (it.currentDestination == null || it.currentDestination?.id == navGraphStartDestinations[selectedNavGraphId]) {
                    if (primaryFragmentId == selectedNavGraphId) {
                        return false
                    }

                    if (it.currentDestination?.id != bottomNavigationView.menu.getItem(2).itemId) {
                        navHistory.pop(selectedNavGraphId)
                        selectItem(primaryFragmentId)
                    } else {
                        navHistory.pop(selectedNavGraphId)
                        selectItem(navHistory.current())
                    }

                    return true
                }
                return false // super.onBackPressed() will be called, which will pop the fragment itself
            } ?: false
        } else false
    }

    private fun selectItem(itemId: Int) {
        bottomNavigationView.selectedItemId = itemId
        bottomNavigationView.menu.findItem(itemId)
            ?.let {
                bottomNavigationView.menu.performIdentifierAction(itemId, 0)
            }
    }

    private fun swapBackStackEntry(
        fragmentTag: String,
        navHostFragment: NavHostFragment
    ) {
        if (navHostFragment.isDetached) attachNavHostFragment(navHostFragment)

        val backStackTag: String? = createAndValidateBackStackTag(fragmentTag)
        fragmentManager.beginTransaction()
            .show(navHostFragment)
            .setMaxLifecycle(navHostFragment, Lifecycle.State.RESUMED)
            .setPrimaryNavigationFragment(navHostFragment)
            .apply {
                // Detach all other Fragments
                graphIdToTagMap.forEach { _, fragmentTag1 ->
                    if (fragmentTag != fragmentTag1) {
                        val fragment = fragmentManager.findFragmentByTag(fragmentTag1)!!
                        hide(fragment)
                        setMaxLifecycle(fragment, Lifecycle.State.STARTED)
                    }
                }
            }
            .commit()
    }

    private fun detachNavHostFragment(navHostFragment: NavHostFragment) {
        fragmentManager.beginTransaction()
            .detach(navHostFragment)
            .commitNow()
    }

    private fun attachNavHostFragment(navHostFragment: NavHostFragment) {
        fragmentManager.beginTransaction()
            .attach(navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()
    }

    private fun createAndValidateBackStackTag(fragmentTag: String): String? {
        return when (multiNavHostBackStackPolicy) {
            MultiNavHostBackStackPolicy.NO_BACK_STACK -> null
            MultiNavHostBackStackPolicy.PRIMARY_BACK_STACK -> {
                val primaryBackStackTag = generateBackStackName(
                    backStackIndex = 0,
                    fragmentTag = graphIdToTagMap[primaryFragmentId]
                )
                fragmentManager.popBackStack(
                    primaryBackStackTag,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                primaryBackStackTag
            }
            MultiNavHostBackStackPolicy.MULTIPLE_BACK_STACK -> {
                val backStackCount = fragmentManager.backStackEntryCount
                if (backStackCount > multipleBackStackCount) {
                    val lastRetainedBackStackCount = backStackCount - multipleBackStackCount
                    lastRetainedBackStackEntry = generateBackStackName(
                        backStackIndex = lastRetainedBackStackCount,
                        fragmentTag = fragmentTag
                    )
                }
                generateBackStackName(
                    backStackIndex = backStackCount,
                    fragmentTag = fragmentTag
                )
            }
        }
    }

    private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
        val backStackCount = backStackEntryCount
        for (index in 0 until backStackCount) {
            if (getBackStackEntryAt(index).name == backStackName) return true
        }
        return false
    }

    private fun FragmentManager.currentBackStackId(): Int? {
        return currentBackStackEntry()?.toFragmentTag()?.let { name ->
            graphIdToTagMap.getKeyAt(name)
        }
    }

    private fun FragmentManager.currentBackStackEntry(): FragmentManager.BackStackEntry? {
        return if (backStackEntryCount > 0) {
            getBackStackEntryAt(backStackEntryCount - 1)
        } else null
    }

    private fun validateStackAndReset(): Boolean {
        val currentBackStackEntry = fragmentManager.currentBackStackEntry()?.name ?: return false
        return if (isLastRetainedBackEntry(currentBackStackEntry)) {
            fragmentManager.popBackStack(
                fragmentManager.getBackStackEntryAt(0).name,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            true
        } else false
    }

    private fun FragmentManager.BackStackEntry?.toFragmentTag(): String? {
        return this?.name?.split("-")?.getOrNull(1)
    }

    private fun isLastRetainedBackEntry(currentBackStackEntryTag: String): Boolean {
        return lastRetainedBackStackEntry != null && lastRetainedBackStackEntry == currentBackStackEntryTag
    }

    private fun <T> SparseArray<T>.getKeyAt(value: T): Int? {
        forEach { key, v -> if (v == value) return key }
        return null
    }

    @Suppress("HardCodedStringLiteral")
    private fun getFragmentTag(index: Int) = "multiNavHost#$index"

    private fun generateBackStackName(backStackIndex: Int, fragmentTag: String): String {
        return "$backStackIndex-$fragmentTag"
    }

    enum class MultiNavHostBackStackPolicy {
        NO_BACK_STACK,
        PRIMARY_BACK_STACK,
        MULTIPLE_BACK_STACK
    }

    companion object {
        var isMultiNavHostInitialized: Boolean = false
        lateinit var backPressedlistener: BackPressedListener
        lateinit var selectItemListener: SelectItemListener
        lateinit var bottomNavigationListener: BottomNavigationListener
    }
}