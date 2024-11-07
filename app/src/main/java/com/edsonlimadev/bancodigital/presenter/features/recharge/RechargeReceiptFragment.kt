package com.edsonlimadev.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.databinding.FragmentRechargeReceiptBinding
import com.edsonlimadev.bancodigital.presenter.features.transaction.TransactionViewModel
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.Mask
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {

    private lateinit var binding: FragmentRechargeReceiptBinding

    private val args: RechargeReceiptFragmentArgs by navArgs()

    private val rechargeViewModel: RechargeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbRecharge, args.setHomeAsUpEnabled)

        initListeners()

        setData()

    }

    private fun initListeners() {
        binding.btnRechargeReceiptOk.setOnClickListener {

            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun setData() {

        rechargeViewModel.getRecharge(args.rechargeId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    with(binding) {

                        stateView.data?.let {
                            textRechargeReceiptCode.text = it.id
                            textRechargeReceiptDate.text =
                                GetMask.getFormatedDate(
                                    it.date,
                                    GetMask.DAY_MONTH_YEAR_HOUR_MINUTE
                                )

                            textRechargeReceiptValue.text =
                                getString(
                                    R.string.currency_simble,
                                    GetMask.getFormatedValue(it.value))

                            textPhoneNumber.text = Mask.mask("(##) #####-####",it.number)
                        }
                    }
                }

                is StateView.Error -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}