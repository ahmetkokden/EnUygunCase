package com.example.enuyguncase.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.FragmentMainBinding
import com.example.enuyguncase.navigation.setupMultipleBackStackBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private var currentNavController: NavController? = null

    private lateinit var graphIds: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(savedInstanceState)
    }



    fun initUI(savedInstanceState: Bundle?) {
        graphIds = arrayListOf(
            R.navigation.nav_home,
            R.navigation.nav_favorite,
            R.navigation.nav_basket
        )

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {
        // Setup the bottom navigation view with a list of navigation graphs
        setupMultipleBackStackBottomNavigation(
            navGraphIds = graphIds,
            containerId = binding.navHostContainer.id,
            bottomNavigationView = binding.navView
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentNavController = null
    }
}