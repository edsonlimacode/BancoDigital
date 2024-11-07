package com.edsonlimadev.bancodigital.utils

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.edsonlimadev.bancodigital.R
import com.edsonlimadev.bancodigital.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(toolbar: Toolbar, homeAsUpEnabled: Boolean = true) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)

    toolbar.setNavigationOnClickListener {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

fun Fragment.showBottomSheetDialog(
    title: String? = null,
    message: String,
    btnTitle: String? = null,
    onClick: () -> Unit = {}
) {

    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val bottomSheetDialogBinding: BottomSheetDialogBinding =
        BottomSheetDialogBinding.inflate(layoutInflater, null, false)

    bottomSheetDialogBinding.textTitleBottomSheet.text = title ?: "Atenção"
    bottomSheetDialogBinding.btnBottomSheet.text = btnTitle ?: "Entendi"
    bottomSheetDialogBinding.textMessageBottomSheet.text = message

    bottomSheetDialogBinding.btnBottomSheet.setOnClickListener {
        bottomSheetDialog.dismiss()
        onClick()
    }

    bottomSheetDialog.setContentView(bottomSheetDialogBinding.root)
    bottomSheetDialog.show()
}

fun Fragment.setStatusBarAndNavBarBottomColor() {

    requireActivity().window.statusBarColor =
        ContextCompat.getColor(requireContext(), R.color.white)
    requireActivity().window.navigationBarColor =
        ContextCompat.getColor(requireContext(), R.color.white)
}

fun Fragment.hideKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
        val imn = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(view.windowToken, 0)
    }
}