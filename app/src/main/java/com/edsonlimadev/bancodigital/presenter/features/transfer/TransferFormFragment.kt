package com.edsonlimadev.bancodigital.presenter.features.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.databinding.FragmentDepositBinding
import com.edsonlimadev.bancodigital.databinding.FragmentTransferFormBinding
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.utils.MoneyTextWatcher
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.hideKeyboard
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferFormFragment : Fragment() {

    private lateinit var binding: FragmentTransferFormBinding

    private val args: TransferFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferFormBinding.inflate(inflater, container, false)

        initToolbar(binding.tbDeposit)
        initListeners()

        return binding.root
    }

    private fun initListeners() {

        with(binding.editTransferValue) {

            addTextChangedListener(MoneyTextWatcher(this))

            addTextChangedListener {
                if (MoneyTextWatcher.getValueUnMasked(this) > 1000000.01f) {
                    setText("R$ 0,00")
                }
            }

            doAfterTextChanged {
                text?.length?.let { setSelection(it) }
            }

        }

        binding.btnTransfer.setOnClickListener {
            transferConfirm()
        }
    }


    private fun transferConfirm() {

        val value = MoneyTextWatcher.getValueUnMasked(binding.editTransferValue)

        if (value > 0f) {

            val action = TransferFormFragmentDirections
                .actionTransferFormFragmentToTransferConfirmFragment(args.user, value)

            findNavController().navigate(action)
        } else {
            showBottomSheetDialog(message = "Digite um valor maior que 0,00")
        }
    }

}