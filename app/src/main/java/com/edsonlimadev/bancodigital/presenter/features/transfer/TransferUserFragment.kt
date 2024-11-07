package com.edsonlimadev.bancodigital.presenter.features.transfer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.databinding.FragmentTransferUserBinding
import com.edsonlimadev.bancodigital.presenter.profile.ProfileViewModel
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferUserFragment : Fragment() {

    private lateinit var binding: FragmentTransferUserBinding

    private lateinit var transferUserAdapter: TransferUserAdapter

    private var userList: List<User> = listOf()

    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTransferUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.tbTransfer)
        initMenuSearch()
        initRecyclerView()
        configSearch()
        getProfileList()
    }

    private fun initRecyclerView() {

        transferUserAdapter = TransferUserAdapter {
            val action = TransferUserFragmentDirections
                .actionTransferUserFragmentToTransferFormFragment(it)
            findNavController().navigate(action)
        }

        binding.rvTransfer.adapter = transferUserAdapter

        binding.rvTransfer.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initMenuSearch() {

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.item_search, menu)

                val item = menu.findItem(R.id.action_search)
                binding.sbTransfer.setMenuItem(item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getProfileList() {

        profileViewModel.getAllProfile().observe(viewLifecycleOwner) { stateView ->

            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        userList = it
                        transferUserAdapter.submitList(it)
                    }


                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }

        }

    }

    private fun configSearch() {
        binding.sbTransfer.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.isNotEmpty()) {
                    val newList = userList.filter { it.name.contains(newText, true) }

                    binding.textSearchEmpty.isVisible = newList.isEmpty()
                    transferUserAdapter.submitList(newList)
                    true
                } else {
                    binding.textSearchEmpty.isVisible = userList.isEmpty()
                    transferUserAdapter.submitList(userList)
                    false
                }
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

        binding.sbTransfer.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {
                binding.textSearchEmpty.isVisible = userList.isEmpty()
                transferUserAdapter.submitList(userList)
            }

            override fun onSearchViewShownAnimation() {}

            override fun onSearchViewClosedAnimation() {}
        })

    }
}