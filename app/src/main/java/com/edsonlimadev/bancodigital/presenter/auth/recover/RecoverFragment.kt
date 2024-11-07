package com.edsonlimadev.bancodigital.presenter.auth.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.edsonlimadev.bancodigital.databinding.FragmentRecoverBinding
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverFragment : Fragment() {

    private lateinit var binding: FragmentRecoverBinding

    private val recoverViewModel: RecoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecoverBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbRecover)
        initListeners()
    }

    private fun initListeners() {
        binding.btnRecover.setOnClickListener {
            recover()
        }
    }

    private fun recover() {
        val email = binding.editEmailRecover.text.toString().trim()

        if (email.isNotEmpty()) {
            recoverViewModel.recover(email).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {

                    is StateView.Loading -> {}

                    is StateView.Sucess -> {
                        showBottomSheetDialog(message = "Foi enviado para o e-mail ${stateView.data.toString()} um link de recuperação de senha")
                    }

                    is StateView.Error -> {
                        showBottomSheetDialog(message = getString(FirebaseHelper.validError(stateView.message.toString())))
                    }
                }
            }
        } else {
            showBottomSheetDialog(message = "Email é obrigatório")
        }

    }
}