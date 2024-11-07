package com.edsonlimadev.bancodigital.presenter.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.edsonlimadev.bancodigital.MainGraphDirections
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.databinding.FragmentHomeBinding
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.presenter.profile.ProfileViewModel
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import com.example.bancodigital.util.GetMask.Companion.getFormatedValue
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.math.BigDecimal

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val transactionViewModel: TransactionViewModel by viewModels()

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val navOptions: NavOptions =
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()

            findNavController().navigate(R.id.action_global_splashFragment, null, navOptions)

        }

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.purple_900)
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.purple_900)

        initRecyclerView()
        getTransactions()
        initListeners()
        setImageProfile()
    }

    private fun initListeners() {

        with(binding) {
            btnItemDeposit.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_depositFragment)
            }

            btnTextSeeAllTransaction.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
            }

            btnCardTransaction.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
            }
            btnCardProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }

            btnCardRecharge.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_rechargeFragment)
            }

            btnCardTransfer.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_transferUserFragment)
            }
        }
    }

    private fun getTransactions() {
        transactionViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.pbBalance.isVisible = true
                    binding.pbHome.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.pbBalance.isVisible = false
                    binding.pbHome.isVisible = false

                    getBalance(stateView)

                    binding.textEmptyTransactions.isVisible = stateView.data?.isEmpty() == true

                    transactionAdapter.submitList(stateView.data?.take(6))
                }

                is StateView.Error -> {
                    binding.pbBalance.isVisible = false
                    binding.pbHome.isVisible = false
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }

    private fun getBalance(stateView: StateView<List<Transaction>>) {
        var cashIn = 0f
        var cashOut = 0f

        stateView.data?.forEach {

            if (it.type == Type.CASH_IN) {
                cashIn += it.value
            } else {
                cashOut += it.value
            }
        }

        binding.textBalance.text =
            getString(R.string.currency_simble, getFormatedValue(cashIn - cashOut))
    }

    private fun initRecyclerView() {

        transactionAdapter = TransactionsAdapter(requireContext()) { transaction ->

            when (transaction.operation) {
                Operation.DEPOSIT -> {
                    val action = MainGraphDirections.actionGlobalDepositReceiptFragment(
                        transaction.id,
                        false
                    )
                    findNavController().navigate(action)
                }

                Operation.RECHARGE -> {
                    val action = MainGraphDirections.actionGlobalRechargeReceiptFragment(
                        transaction.id,
                        false
                    )
                    findNavController().navigate(action)
                }

                Operation.TRANSFER -> {
                    val action =
                        MainGraphDirections.actionGlobalTransferReceiptFragment(transaction.id)
                    findNavController().navigate(action)
                }

                else -> {}
            }
        }

        binding.rvTransaction.adapter = transactionAdapter

        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setImageProfile() {

        profileViewModel.getProfile(FirebaseHelper.getUserId().toString())
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Sucess -> {
                        stateView.data?.let {

                            if (it.image.isNotEmpty()) {

                                Picasso.get().load(it.image)
                                    .into(binding.imgProfileHome, object : Callback {
                                        override fun onSuccess() {
                                            binding.pbImageHome.isVisible = false
                                            binding.imgProfileHome.isVisible = true
                                        }

                                        override fun onError(e: Exception?) {}

                                    })

                            } else {
                                binding.pbImageHome.isVisible = false
                                binding.imgProfileHome.isVisible = true
                            }
                        }
                    }

                    is StateView.Error -> {
                        Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

}