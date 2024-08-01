package com.obss.firstapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        changeVisibilityBottomBar(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray_top)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.gray_bottom)
        setupBottomNavigation()

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

}