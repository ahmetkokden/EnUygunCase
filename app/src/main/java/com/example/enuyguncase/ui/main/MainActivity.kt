package com.example.enuyguncase.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.ActivityMainBinding
import com.example.enuyguncase.navigation.setUpDeeplinkNavigationBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    private var currentNavController: NavController? = null

    private lateinit var graphIds: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setUpDeeplinkNavigationBehavior(
            navHostFragmentId = binding.navHostFragmentContainer.id,
            graphId = R.navigation.nav_main
        )
    }

}