package com.edsonlimadev.bancodigital.presenter.features.extract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.edsonlimadev.bancodigital.MainGraphDirections
import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.databinding.FragmentExtractBinding
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.presenter.home.TransactionsAdapter
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExtractFragment : Fragment() {

    private lateinit var binding: FragmentExtractBinding

    private val transactionViewModel: TransactionViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentExtractBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbExtract)

        swipeRefresh()
        initRecyclerView()
        getTransactions()
    }

    private fun getTransactions() {
        transactionViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.pbExtract.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.pbExtract.isVisible = false

                    binding.textEmptyExtracts.isVisible = stateView.data?.isEmpty() == true

                    transactionAdapter.submitList(stateView.data)
                }

                is StateView.Error -> {
                    binding.pbExtract.isVisible = false
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }

    private fun swipeRefresh() {
        binding.swipeExtract.setOnRefreshListener {
            binding.swipeExtract.isRefreshing = false
            getTransactions()
        }
    }

    private fun initRecyclerView() {

        transactionAdapter = TransactionsAdapter(requireContext()) { transaction ->

            when (transaction.operation) {
                Operation.DEPOSIT -> {
                    val action = MainGraphDirections.actionGlobalDepositReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                Operation.RECHARGE -> {
                    val action = MainGraphDirections.actionGlobalRechargeReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }

                Operation.TRANSFER -> {
                    val action = MainGraphDirections.actionGlobalTransferReceiptFragment(transaction.id, true)
                    findNavController().navigate(action)
                }
                else -> {}
            }
        }

        binding.rvExtract.adapter = transactionAdapter

        binding.rvExtract.layoutManager = LinearLayoutManager(requireContext())
    }

}