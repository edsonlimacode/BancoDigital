package com.edsonlimadev.bancodigital.presenter.features.recharge

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
import com.edsonlimadev.bancodigital.data.model.Recharge
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.databinding.FragmentRechargeBinding
import com.edsonlimadev.bancodigital.presenter.features.deposit.DepositFragmentDirections
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.utils.MoneyTextWatcher
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFragment : Fragment() {

    private lateinit var binding: FragmentRechargeBinding

    private val rechargeViewModel: RechargeViewModel by viewModels()

    private val createTransactionViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRechargeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbRecharce)
        initListeners()
    }

    private fun initListeners() {

        with(binding.editRechargeValue) {

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

        binding.btnRecharConfirm.setOnClickListener {
            save()
        }
    }

    private fun save() {

        with(binding) {

            val value = MoneyTextWatcher.getValueUnMasked(editRechargeValue)
            val number = editRechargeNumber.unMaskedText.toString().trim()

            if (value in 10f..50f) {

                if (number.isNotEmpty()) {

                    binding.pbBtnRecharge.isVisible = true
                    binding.btnRecharConfirm.text = ""
                    binding.btnRecharConfirm.isEnabled = false


                    val recharge = Recharge(
                        value = value,
                        number = number
                    )

                    rechargeViewModel.save(recharge).observe(viewLifecycleOwner) { stateView ->
                        when (stateView) {
                            is StateView.Loading -> {}

                            is StateView.Sucess -> {
                                stateView.data?.let {
                                    createTransaction(it)
                                }
                            }

                            is StateView.Error -> {
                                binding.pbBtnRecharge.isVisible = false
                                binding.btnRecharConfirm.text = "confirmar"
                                binding.btnRecharConfirm.isEnabled = true
                                showBottomSheetDialog(message = stateView.message.toString())
                            }
                        }
                    }

                } else {
                    showBottomSheetDialog(message = "Numero é obrigatório")
                }

            } else {
                showBottomSheetDialog(message = "Digite um valor entre 10 e 50")
            }

        }

    }

    private fun createTransaction(recharge: Recharge) {

        val transaction = Transaction(
            id = recharge.id,
            operation = Operation.RECHARGE,
            type = Type.CASH_OUT,
            value = recharge.value
        )

        createTransactionViewModel.create(transaction).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.rechargeFragment, true).build()

                    val action =
                        MainGraphDirections.actionGlobalRechargeReceiptFragment(recharge.id, false)
                    findNavController().navigate(action, navOptions)
                }

                is StateView.Error -> {
                    binding.pbBtnRecharge.isVisible = false
                    binding.btnRecharConfirm.text = "confirmar"
                    binding.btnRecharConfirm.isEnabled = true
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }
}