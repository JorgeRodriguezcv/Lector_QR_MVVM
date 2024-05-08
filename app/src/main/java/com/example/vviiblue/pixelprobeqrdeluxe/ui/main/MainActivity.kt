package com.example.vviiblue.pixelprobeqrdeluxe.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.vviiblue.pixelprobeqrdeluxe.R
import com.example.vviiblue.pixelprobeqrdeluxe.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

    }

    private fun initUI() {

        initNavigation()
        initListeners()

    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun initListeners() {
        binding.fabScan.setOnClickListener {
            showViewScan()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.scanFragment -> {
                    binding.cvBottomNavView.visibility = View.GONE
                    binding.fabScan.visibility = View.GONE
                }

                else -> {
                    binding.cvBottomNavView.visibility = View.VISIBLE
                    binding.fabScan.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun showViewScan() {
        if (navController != null)
            navController.navigate(R.id.scanFragment)
    }


}