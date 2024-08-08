package com.obss.firstapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import com.obss.firstapp.R
import com.obss.firstapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemBars()
        setSplashScreen(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setLandscapeMode()
        setBarsColors()
        setupBottomNavigation()
    }

    private fun setSplashScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.bnvMain.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                showSystemBars()
                findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)
                binding.bnvMain.visibility = View.VISIBLE
                changeVisibilityBottomBar(true)
            }, 2000)
        }
    }

    private fun setLandscapeMode() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val windowInsetsController = WindowCompat.getInsetsController(this.window, binding.root)
            windowInsetsController.let {
                it.hide(WindowInsetsCompat.Type.statusBars())
                it.hide(WindowInsetsCompat.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            binding.bnvMain.itemIconSize = resources.getDimension(R.dimen.bottom_menu_icon_size_landscape).toInt()
        } else {
            binding.bnvMain.itemIconSize = resources.getDimension(R.dimen.bottom_menu_icon_size).toInt()
        }
    }

    private fun setBarsColors() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray_top)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.gray_bottom)
    }

    private fun setupBottomNavigation() {
        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)
                    true
                }
                R.id.bottom_favorite -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.favoriteFragment)
                    true
                }
                R.id.bottom_search -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.searchFragment)
                    true
                }
                else -> false
            }
        }
    }

    fun changeVisibilityBottomBar(isActive: Boolean) {
        binding.bnvMain.visibility = if (isActive) View.VISIBLE else View.GONE
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(this.window, binding.root)
        windowInsetsController.let {
            it.hide(WindowInsetsCompat.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(this.window, binding.root)
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }
}
