package com.edsonlimadev.bancodigital.presenter.features.deposit

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
import com.edsonlimadev.bancodigital.databinding.FragmentDepositReceiptBinding
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.example.bancodigital.util.GetMask
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositReceiptFragment : Fragment() {


    private lateinit var binding: FragmentDepositReceiptBinding

    private val args: DepositReceiptFragmentArgs by navArgs()

    private val depositViewMode: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbReceipt, args.setHomeAsUpEnabled)

        setData()
        initListeners()
    }

    private fun initListeners() {
        binding.btnReceiptOk.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun setData() {

        depositViewMode.getDepositById(args.depositId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {

                    with(binding) {

                        stateView.data?.let {
                            textReceiptCode.text = it.id
                            textReceiptDate.text =
                                GetMask.getFormatedDate(
                                    it.date,
                                    GetMask.DAY_MONTH_YEAR_HOUR_MINUTE
                                )
                            textReceiptValue.text =
                                getString(
                                    R.string.currency_simble,
                                    GetMask.getFormatedValue(it.amount)
                                )
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