package com.edsonlimadev.bancodigital.presenter.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.databinding.FragmentSplashBinding
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(this::verifyAuth, 3000)
    }

    private fun verifyAuth() {
        val action = if (FirebaseHelper.isAuthenticated()) {
            R.id.action_global_homeFragment
        } else {
            R.id.action_global_autentication
        }

        val navOptions: NavOptions =
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()

        findNavController().navigate(
            action,
            null,
            navOptions
        )
    }
}