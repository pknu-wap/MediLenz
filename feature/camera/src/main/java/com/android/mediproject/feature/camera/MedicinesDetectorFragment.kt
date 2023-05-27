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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class MedicinesDetectorFragment :
    BaseFragment<FragmentMedicinesDetectorBinding, MedicinesDetectorViewModel>(FragmentMedicinesDetectorBinding::inflate) {

    override val fragmentViewModel: MedicinesDetectorViewModel by activityViewModels()


    private val requestPermissionLauncher = registerForActivityResult(
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
        binding.viewModel = fragmentViewModel

        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                initializeCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.cameraPermission))
                    .setMessage(getString(R.string.cameraPermissionMessage)).setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }.setNegativeButton(getString(R.string.close)) { _, _ -> }.setCancelable(false).show()
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }


        viewLifecycleOwner.repeatOnStarted {
            /**
             * Collecting the detected objects from the camera preview
             *
             * If the detected objects are not empty, then navigate to the [ConfirmDialogFragment]
             *
             * If the detected objects are empty, then show a toast and open the camera again
             *
             */
            launch {
                fragmentViewModel.detectedObjects.collectLatest { objs ->
                    if (objs.isNotEmpty()) {
                        findNavController().navigate(
                            MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmDialogFragment()
                        )
                    } else {
                        toast(getString(R.string.noMedicinesDetected))
                        fragmentViewModel.openCamera()
                    }
                }
            }

            launch {
                fragmentViewModel.loadedModel.collectLatest { isLoaded ->
                    if (isLoaded) {
                        fragmentViewModel.openCamera()
                        activity?.apply {
                            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        }
                    }

                    LoadingDialog.dismiss()
                }
            }
        }

    }


    private fun initializeCamera() {
        binding.apply {

            LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.loadingAiModels))

            val surfaceHolder = object : SurfaceHolder.Callback2 {
                override fun surfaceCreated(holder: SurfaceHolder) {
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    fragmentViewModel.surfaceCreated(holder)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
                }
            }

            surfaceView.holder.setFormat(PixelFormat.RGBA_8888)
            surfaceView.holder.addCallback(surfaceHolder)

            // Load the model from the assets folder
            fragmentViewModel.loadModel(requireContext().assets)
        }
    }


    override fun onPause() {
        super.onPause()
        activity?.apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        fragmentViewModel.closeCamera()
    }

    override fun onDestroy() {
        fragmentViewModel.clear()
        super.onDestroy()
    }

}