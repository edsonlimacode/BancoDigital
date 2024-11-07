package com.edsonlimadev.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.databinding.FragmentTransferReceiptBinding
import com.edsonlimadev.bancodigital.presenter.profile.ProfileViewModel
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import com.example.bancodigital.util.GetMask.Companion.DAY_MONTH_YEAR_HOUR_MINUTE
import com.example.bancodigital.util.GetMask.Companion.getFormatedDate
import com.example.bancodigital.util.GetMask.Companion.getFormatedValue
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferReceiptFragment : Fragment() {

    private lateinit var binding: FragmentTransferReceiptBinding

    private val args: TransferReceiptFragmentArgs by navArgs()

    private val transferViewModel: TransferViewModel by viewModels()

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbTransferConfirm, args.setHomeAsUpEnabled)

        getTransfer()
        initListeners()
    }

    private fun initListeners() {

        binding.btnTransferConfirmOk.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun getTransfer() {
        transferViewModel.getTransfer(args.idTransference)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Sucess -> {

                        stateView.data?.let { transfer ->

                            val userId =
                                if (transfer.userSentId == FirebaseHelper.getUserId().toString()) {
                                    binding.textTitleTransfer.text = "Destinatário"
                                    transfer.userReceiverId
                                } else {
                                    binding.textTitleTransfer.text = "Remetente"
                                    transfer.userSentId
                                }

                            getProfile(userId)

                            binding.textTransferCode.text = transfer.id

                            binding.textDateValueTransferReceipt.text =
                                getFormatedDate(transfer.date, DAY_MONTH_YEAR_HOUR_MINUTE)

                            binding.textValueTransferReceipt.text =
                                getString(
                                    R.string.currency_simble,
                                    getFormatedValue(transfer.value)
                                )

                        }
                    }

                    is StateView.Error -> {
                        showBottomSheetDialog(message = stateView.message.toString())
                    }
                }
            }
    }

    private fun getProfile(id: String) {
        profileViewModel.getProfile(id).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    stateView.data?.let { user ->

                        if (user.image.isNotEmpty()) {
                            Picasso.get().load(user.image).into(binding.imgTransferReceipt, object :
                                Callback {
                                override fun onSuccess() {
                                    binding.pbTransferReceipt.isVisible = false
                                    binding.imgTransferReceipt.isVisible = true
                                }

                                override fun onError(e: Exception?) {}
                            })
                        } else {
                            binding.pbTransferReceipt.isVisible = false
                            binding.imgTransferReceipt.isVisible = true
                        }
                        binding.textUserNameTransferReceipt.text = user.name
                    }
                }

                is StateView.Error -> {
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }

}