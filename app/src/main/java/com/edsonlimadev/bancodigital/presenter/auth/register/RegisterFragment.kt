package com.edsonlimadev.bancodigital.presenter.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.databinding.FragmentRegisterBinding
import com.edsonlimadev.bancodigital.presenter.profile.ProfileViewModel
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbRegister)
        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = binding.editNameRegister.text.toString().trim()
        val email = binding.editEmailRegister.text.toString().trim()
        val phone = binding.editPhoneRegister.unMaskedText
        val password = binding.editPasswordRegister.text.toString().trim()

        if (name.isNotEmpty()) {
            if (email.isNotEmpty()) {
                if (phone?.isNotEmpty() == true) {
                    if (phone.length == 11) {
                        if (password.isNotEmpty()) {

                            registerViewModel.register(email, password)
                                .observe(viewLifecycleOwner) { stateView ->
                                    when (stateView) {

                                        is StateView.Loading -> {
                                            binding.pbRegister.isVisible = true
                                        }

                                        is StateView.Sucess -> {
                                            binding.pbRegister.isVisible = false

                                            saveProfileToDatabse(
                                                User(
                                                    FirebaseHelper.getUserId(),
                                                    name,
                                                    email,
                                                    phone
                                                )
                                            )
                                        }

                                        is StateView.Error -> {
                                            binding.pbRegister.isVisible = false
                                            showBottomSheetDialog(
                                                message = getString(
                                                    FirebaseHelper.validError(
                                                        stateView.message.toString()
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }

                        } else {
                            showBottomSheetDialog(message = "Senha é obrigatória")
                        }
                    } else {
                        showBottomSheetDialog(message = "Telefone inválido, verifique seu telefone")
                    }
                } else {
                    showBottomSheetDialog(message = "Telefone é obrigatório")
                }
            } else {
                showBottomSheetDialog(message = "Email é obrigatório")
            }
        } else {
            showBottomSheetDialog(message = "Nome é obrigatório")
        }
    }

    private fun saveProfileToDatabse(user: User) {
        profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {}

                is StateView.Sucess -> {
                    binding.pbRegister.isVisible = false
                    findNavController().navigate(R.id.action_global_homeFragment)
                }

                is StateView.Error -> {
                    binding.pbRegister.isVisible = false
                    showBottomSheetDialog(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                }
            }
        }

    }
}