package com.android.mediproject.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.android.mediproject.feature.camera.tflite.CameraHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.gms.vision.detector.Detection
import javax.inject.Inject

@AndroidEntryPoint
class MedicinesDetectorFragment :
    BaseFragment<FragmentMedicinesDetectorBinding, MedicinesDetectorViewModel>(FragmentMedicinesDetectorBinding::inflate),
    CameraHelper.ObjDetectionCallback {

    override val fragmentViewModel: MedicinesDetectorViewModel by activityViewModels()

    @Inject lateinit var systemBarStyler: SystemBarController

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) initializeCamera()
        else findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        systemBarStyler.changeMode(
            listOf(SystemBarStyler.ChangeView(binding.logo, SystemBarStyler.SpacingType.MARGIN)),
            listOf(SystemBarStyler.ChangeView(binding.detectionDescription, SystemBarStyler.SpacingType.MARGIN)),
        )

        binding.apply {
            viewModel = fragmentViewModel
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.cameraConnectionState.collect { state ->
                state.onConnected {
                    LoadingDialog.dismiss()
                }.onDisconnected {
                    LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.connectingCamera))
                }.onUnableToConnect {
                    toast(getString(R.string.connectingCameraFailed))
                    LoadingDialog.dismiss()
                    findNavController().popBackStack()
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.inferenceState.collect { state ->
                state.onDetected { _, consumed ->
                    if (!consumed) {
                        (state as InferenceState.Detected).consumed = true
                        findNavController().navigate(MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmFragment())
                    }
                }.onDetectFailed {
                    toast(getString(R.string.noMedicinesDetected))
                }
            }
        }
    }

    private fun initializeCamera() {
        fragmentViewModel.connectCamera(binding.previewView, this@MedicinesDetectorFragment.viewLifecycleOwner, this@MedicinesDetectorFragment)
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel.disconnectCamera()
    }

    override fun onDestroy() {
        requestPermissionLauncher.unregister()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                initializeCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.cameraPermission))
                    .setMessage(getString(R.string.cameraPermissionMessage)).setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }.setNegativeButton(getString(R.string.close)) { _, _ -> }.setCancelable(false).show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onDetect(objects: List<Detection>, width: Int, height: Int) {
        if (isVisible) binding.overlayView.apply {
            setResults(objects, width, height)
            invalidate()
        }
    }
}
