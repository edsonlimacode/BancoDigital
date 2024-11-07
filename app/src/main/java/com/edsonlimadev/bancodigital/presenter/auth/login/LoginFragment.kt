package com.edsonlimadev.bancodigital.presenter.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.databinding.FragmentLoginBinding
import com.edsonlimadev.bancodigital.presenter.auth.register.RegisterViewModel
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnTextRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverFragment)
        }

        binding.btnTextNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login() {

        val email = binding.editEmailLogin.text.toString().trim()
        val password = binding.editPasswordLogin.text.toString().trim()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                loginViewModel.login(email, password).observe(viewLifecycleOwner) { stateView ->
                    when (stateView) {

                        is StateView.Sucess -> {

                            val navOptions: NavOptions =
                                NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()

                            findNavController().navigate(
                                R.id.action_global_homeFragment,
                                null,
                                navOptions
                            )
                        }

                        is StateView.Error -> {
                            showBottomSheetDialog(
                                message = getString(
                                    FirebaseHelper.validError(
                                        stateView.message ?: ""
                                    )
                                )
                            )
                        }

                        is StateView.Loading -> {}
                    }
                }
            } else {
                showBottomSheetDialog(message = "Senha é obrigatória")
            }

        } else {
            showBottomSheetDialog(message = "Email é obrigatório")
        }
    }
}