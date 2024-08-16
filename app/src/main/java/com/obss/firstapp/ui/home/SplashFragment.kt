package com.obss.firstapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.obss.firstapp.R

class SplashFragment : Fragment() {
    private lateinit var rotateAnimation: Animation
    private lateinit var appIconSplash: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anim)
        appIconSplash = view.findViewById(R.id.iv_movie_app_icon_splash)
        appIconSplash.startAnimation(rotateAnimation)
    }
}
