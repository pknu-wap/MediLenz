package com.android.mediproject.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicinesDetectorFragment :
    BaseFragment<FragmentMedicinesDetectorBinding, MedicinesDetectorViewModel>(FragmentMedicinesDetectorBinding::inflate) {

    override val fragmentViewModel: MedicinesDetectorViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initializeCamera()
            } else {
                findNavController().popBackStack()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                initializeCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.cameraPermission))
                    .setMessage(getString(R.string.cameraPermissionMessage))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                    .setNegativeButton(getString(R.string.close)) { _, _ -> }
                    .setCancelable(false)
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }


    private fun initializeCamera() {
        binding.apply {
            val surfaceHolder = object : SurfaceHolder.Callback2 {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    TODO("Not yet implemented")
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    fragmentViewModel.surfaceCreated(holder)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    TODO("Not yet implemented")
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
                    TODO("Not yet implemented")
                }
            }

            surfaceView.holder.setFormat(PixelFormat.RGBA_8888)
            surfaceView.holder.addCallback(surfaceHolder)

            detectionBtn.setOnClickListener {
                fragmentViewModel.closeCamera()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.checkCountsOfMedicines))
                    .setMessage(getString(R.string.checkCountsOfMedicinesMessage))
                    .setPositiveButton(getString(R.string.search)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.close)) { _, _ -> }
                    .setOnDismissListener {
                        fragmentViewModel.openCamera()
                    }
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().apply {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        fragmentViewModel.openCamera()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        fragmentViewModel.closeCamera()
    }
}