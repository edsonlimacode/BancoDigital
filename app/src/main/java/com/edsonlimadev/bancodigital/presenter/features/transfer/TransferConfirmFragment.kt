package com.edsonlimadev.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edsonlimadev.bancodigital.MainGraphDirections
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.model.Transfer
import com.edsonlimadev.bancodigital.databinding.FragmentTransferConfirmBinding
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import com.example.bancodigital.util.GetMask.Companion.getFormatedValue
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferConfirmFragment : Fragment() {

    private lateinit var binding: FragmentTransferConfirmBinding

    private val args: TransferConfirmFragmentArgs by navArgs()

    private val transferViewModel: TransferViewModel by viewModels()

    private var balanceTotal: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbTransferConfirm)
        setData()
        getBalance()
        initListeners()
    }

    private fun initListeners() {

        binding.btnTransferConfirmOk.setOnClickListener {
            if (balanceTotal >= args.value) {

                binding.pbBtnTransfer.isVisible = true
                binding.btnTransferConfirmOk.text = ""
                binding.btnTransferConfirmOk.isEnabled = false


                val transfer = Transfer(
                    userSentId = FirebaseHelper.getUserId().toString(),
                    userReceiverId = args.user.id.toString(),
                    value = args.value
                )

                saveTransfer(transfer)
            } else {
                showBottomSheetDialog(message = "Saldo insuficiente para realizar esta transação")
            }
        }
    }

    private fun saveTransfer(transfer: Transfer) {
        transferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->

            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    saveTransferTransaction(transfer)
                }

                is StateView.Error -> {
                    binding.pbBtnTransfer.isVisible = false
                    binding.btnTransferConfirmOk.text = "Confirmar"
                    binding.btnTransferConfirmOk.isEnabled = true
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }

        }
    }

    private fun saveTransferTransaction(transfer: Transfer) {
        transferViewModel.saveTransferTransaction(transfer)
            .observe(viewLifecycleOwner) { stateView ->

                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Sucess -> {

                        val navOptions: NavOptions =
                            NavOptions.Builder().setPopUpTo(R.id.transferUserFragment, true).build()

                        val action = MainGraphDirections.actionGlobalTransferReceiptFragment(transfer.id, false)
                        findNavController().navigate(action, navOptions)
                    }

                    is StateView.Error -> {
                        binding.pbBtnTransfer.isVisible = false
                        binding.btnTransferConfirmOk.text = "Confirmar"
                        binding.btnTransferConfirmOk.isEnabled = true
                        showBottomSheetDialog(message = stateView.message.toString())
                    }
                }

            }
    }

    private fun setData() {

        if (args.user.image.isNotEmpty()) {
            Picasso.get().load(args.user.image).into(binding.imgTransferConfirm, object :
                Callback {
                override fun onSuccess() {
                    binding.pbTransferConfirm.isVisible = false
                    binding.imgTransferConfirm.isVisible = true
                }

                override fun onError(e: Exception?) {}
            })
        } else {
            binding.pbTransferConfirm.isVisible = false
            binding.imgTransferConfirm.isVisible = true
        }

        binding.textUserNameConfirm.text = args.user.name

        binding.textTransferConfirmValue.text =
            getString(R.string.currency_simble, getFormatedValue(args.value))
    }

    private fun getBalance() {
        transferViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {
                    stateView.data?.let { value ->
                        balanceTotal = value
                    }
                }

                is StateView.Error -> {
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }
}