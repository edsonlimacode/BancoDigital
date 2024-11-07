package com.edsonlimadev.bancodigital.presenter.features.deposit

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
import com.edsonlimadev.bancodigital.MainGraphDirections
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.databinding.FragmentDepositBinding
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.utils.MoneyTextWatcher
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.hideKeyboard
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFragment : Fragment() {

    private lateinit var binding: FragmentDepositBinding

    private val depositViewModel: DepositViewModel by viewModels()
    private val createTransactionViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbDeposit)
        initListeners()
    }

    private fun initListeners() {

        with(binding.editDepositValue) {

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

        binding.btnDeposit.setOnClickListener {
            createDeposit()
        }
    }


    private fun createDeposit() {
        val value = MoneyTextWatcher.getValueUnMasked(binding.editDepositValue)

        if (value > 0f) {

            binding.btnDeposit.setText("")
            binding.pbBtnDeposit.isVisible = true
            binding.btnDeposit.isEnabled = false

            val deposit = Deposit(
                amount = value
            )

            depositViewModel.create(deposit).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Sucess -> {
                        stateView.data?.let {
                            createTransaction(stateView.data)
                            hideKeyboard()
                        }
                    }

                    is StateView.Error -> {
                        binding.btnDeposit.text = "confirmar"
                        binding.pbBtnDeposit.isVisible = false
                        binding.btnDeposit.isEnabled = true
                        showBottomSheetDialog(message = stateView.message.toString())
                    }
                }
            }

        } else {
            showBottomSheetDialog(message = "Digite um valor maior que 0,00")
        }
    }

    private fun createTransaction(deposit: Deposit) {

        val transaction = Transaction(
            id = deposit.id,
            operation = Operation.DEPOSIT,
            type = Type.CASH_IN,
            value = deposit.amount
        )

        createTransactionViewModel.create(transaction).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.depositFragment, true).build()

                    val action =
                        MainGraphDirections.actionGlobalDepositReceiptFragment(deposit.id, false)
                    findNavController().navigate(action, navOptions)
                }

                is StateView.Error -> {
                    binding.btnDeposit.setText("confirmar")
                    binding.pbBtnDeposit.isVisible = false
                    binding.btnDeposit.isEnabled = true
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }
}