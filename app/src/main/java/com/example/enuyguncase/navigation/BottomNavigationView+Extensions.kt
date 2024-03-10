package com.example.enuyguncase.navigation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.enuyguncase.R
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

// global variable to store user provided navGraphids
internal var mNavGraphIds: List<Int> = emptyList()
    private set

// global variable to store user provided containerId
internal var mContainerId: Int = -1
    private set

fun Fragment.setupMultipleBackStackBottomNavigation(
    navGraphIds: List<Int>,
    containerId: Int,
    bottomNavigationView: BottomNavigationView,
    badge: BadgeDrawable
) {
    storeNavDefaults(navGraphIds, containerId)
    bottomNavigationView.setupMultipleBackStack(
        fragmentManager = childFragmentManager,
        badge
    )
}

private fun BottomNavigationView.setupMultipleBackStack(
    fragmentManager: FragmentManager,
    badge: BadgeDrawable
) {
    val multiNavHost = MultiNavHost(
        navGraphIds = mNavGraphIds,
        containerId = mContainerId,
        primaryFragmentIndex = 0,
        fragmentManager = fragmentManager,
        multiNavHostBackStackPolicy = MultiNavHost.MultiNavHostBackStackPolicy.PRIMARY_BACK_STACK,
        bottomNavigationView = this
    )

    multiNavHost.create(selectedItemId)

/*
    // When a navigation item is selected
    setOnNavigationItemSelectedListener { item ->

    }

 */

    setOnItemSelectedListener { item ->
        multiNavHost.selectSiblings(item.itemId)
        when (item.itemId) {
            R.id.navigation_home -> {
                badge.isVisible = false
                true
            }

            R.id.navigation_favorite -> {
                badge.isVisible = false

                true
            }

            R.id.navigation_basket -> {
                badge.isVisible = true

                true
            }

            else -> {
                badge.isVisible = false
                true
            }
        }
    }

    // Optional: on item reselected, pop back stack to the destination of the graph
    setOnNavigationItemReselectedListener { item -> multiNavHost.reselectSiblings(item.itemId) }

    multiNavHost.observeBackStack { itemId ->
        selectedItemId = itemId
    }
}

private fun storeNavDefaults(
    navGraphIds: List<Int>,
    containerId: Int
) {
    // Store all the information in above objects
    mNavGraphIds = navGraphIds
    mContainerId = containerId
}

/**
 * Activity extension to specify how does you want the library to handle backstack of the activity
 * NavController.
 *
 * @param navHostFragmentId [FragmentContainerView] id for activity
 * @param primaryFragmentId This is the fragment id from which [handleDeeplink] is called (BottomNavigationView
 * is also setup here)
 * @param graphId activity graph, this is graph is to be set on activity fragment container
 * @param fragmentBackStackBehavior map of all the fragment deeplink with the behavior you want.
 * If the deeplink is not specified the default behavior will be [DeeplinkNavigationPolicy.EXIT_AND_NAVIGATE]
 *
 * This should only be called from primary activity. Activity that hosts fragment with bottom navigation.
 */
fun AppCompatActivity.setUpDeeplinkNavigationBehavior(
    @IdRes navHostFragmentId: Int,
    @NavigationRes graphId: Int,
    safeNavigation: Boolean = true
) {
    val navController = getNavController(navHostFragmentId)

    // extract deeplink so that setGraph can not manually handle the deeplink
    val deeplink = intent.data
    intent.data = null
    navController.setGraph(graphId)

    // once setGraph is called set the deeplink again so that validations can be prformed
    // we set the data to null internally
    intent.data = deeplink
}

/**
 * Find the [NavController] for the activity
 * @param fragmentContainerId Id of the FragmentContainerView which will host all the fragments
 *
 * Use NavHostFragment to find controller when used in activity onCreate
 * Issue: https://issuetracker.google.com/issues/142847973
 */
private fun FragmentActivity.getNavController(@IdRes fragmentContainerId: Int): NavController {
    val navHostFragment =
        supportFragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
    return navHostFragment.navController
}