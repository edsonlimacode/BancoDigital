package com.edsonlimadev.bancodigital.presenter.profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.databinding.BottomSheetProfilePictureBinding
import com.edsonlimadev.bancodigital.databinding.FragmentProfileBinding
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import com.edsonlimadev.bancodigital.utils.hideKeyboard
import com.edsonlimadev.bancodigital.utils.initToolbar
import com.edsonlimadev.bancodigital.utils.setStatusBarAndNavBarBottomColor
import com.edsonlimadev.bancodigital.utils.showBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.lang.Exception
import java.util.UUID


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    private var imageProfile: String = ""
    private var imageUri: Uri? = null

    private val openGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {
            binding.imgProfile.setImageURI(uri)
            updateImageProfile(uri)
        } else {
            Toast.makeText(requireContext(), "Imagem não selecionada", Toast.LENGTH_SHORT).show()
        }

    }


    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {

            imageUri?.let {
                binding.imgProfile.setImageURI(it)
                updateImageProfile(it)
            }
        }else {
            Toast.makeText(requireContext(), "Não foi possível carregar a imagem", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        setStatusBarAndNavBarBottomColor()

        setProfile(FirebaseHelper.getUserId().toString())

        initListeners()

    }

    private fun initListeners() {

        binding.imgProfile.setOnClickListener {
            openBottomSheet()
        }

        binding.btnProfile.setOnClickListener {
            updateProfile()
        }
    }



    private fun updateProfile() {
        with(binding) {
            val name = editNameProfile.text.toString().trim()
            val phone = editPhoneProfile.unMaskedText
            val email = editEmailProfile.text.toString().trim()

            val user = User(
                id = FirebaseHelper.getUserId().toString(),
                name = name,
                phone = phone.toString(),
                email = email,
                image = imageProfile
            )

            profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {}
                    is StateView.Sucess -> {
                        Toast.makeText(requireContext(), "Perfil atualizado", Toast.LENGTH_SHORT)
                            .show()
                        hideKeyboard()
                    }

                    is StateView.Error -> {
                        Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun setProfile(id: String) {

        profileViewModel.getProfile(id).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {
                    with(binding) {

                        stateView.data?.let {

                            if (it.image.isNotEmpty()) {
                                Picasso.get().load(it.image).into(binding.imgProfile, object :
                                    Callback {
                                    override fun onSuccess() {
                                        binding.pbImageProfile.isVisible = false
                                        binding.imgProfile.isVisible = true
                                    }

                                    override fun onError(e: Exception?) {}

                                })

                                imageProfile = stateView.data.image

                            }else {
                                binding.pbImageProfile.isVisible = false
                                binding.imgProfile.isVisible = true
                            }

                            editNameProfile.setText(stateView.data.name)
                            editPhoneProfile.setText(stateView.data.phone)
                            editEmailProfile.setText(stateView.data.email)
                        }
                    }
                }

                is StateView.Error -> {
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermissionCamera() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "permissão negada", Toast.LENGTH_SHORT
                ).show()
            }
        }

        showDialogPermissionDenied(
            permissionlistener, Manifest.permission.CAMERA,
            "Permissão negada!, por favor acesse as configurações e ative a permissão"
        )
    }

    private fun openCamera() {
        val uri = createImageUri()
        cameraLauncher.launch(uri)
    }

    private fun checkPermissionGallery() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openGallery.launch("image/*")
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "permissão negada", Toast.LENGTH_SHORT
                ).show()
            }
        }

        showDialogPermissionDenied(
            permissionlistener, Manifest.permission.READ_MEDIA_IMAGES,
            "Permissão negada!, por favor acesse as configurações e ative a permissão"
        )
    }

    private fun updateImageProfile(uri: Uri) {
        profileViewModel.updateImageProfile(uri).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {}
                is StateView.Sucess -> {
                    Toast.makeText(requireContext(), "Imagem atualizada", Toast.LENGTH_SHORT).show()
                    stateView.data?.let {
                        imageProfile = it
                    }
                }

                is StateView.Error -> {
                    showBottomSheetDialog(message = stateView.message.toString())
                }
            }
        }
    }

    private fun showDialogPermissionDenied(
        permissionlistener: PermissionListener,
        permission: String,
        message: String
    ) {
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedTitle("Permissão negada")
            .setDeniedMessage(message)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(permission)
            .check();
    }

    private fun createImageUri(): Uri {
        val imageFileName = UUID.randomUUID().toString()
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        ).apply {
            deleteOnExit()
        }

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "com.edsonlimadev.bancodigital.fileprovider",
            image
        )

        imageUri = uri

        return uri
    }

    private fun openBottomSheet() {

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetProfilePictureBinding: BottomSheetProfilePictureBinding =
            BottomSheetProfilePictureBinding.inflate(layoutInflater, null, false)

        bottomSheetProfilePictureBinding.btnImageGallery.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkPermissionGallery()
        }

        bottomSheetProfilePictureBinding.btnImageCamera.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkPermissionCamera()
        }

        bottomSheetDialog.setContentView(bottomSheetProfilePictureBinding.root)
        bottomSheetDialog.show()
    }
}