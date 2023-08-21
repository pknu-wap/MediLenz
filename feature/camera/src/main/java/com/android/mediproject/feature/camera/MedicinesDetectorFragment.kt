package com.android.mediproject.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.ai.camera.CameraHelper
import com.android.mediproject.core.ai.model.InferenceState
import com.android.mediproject.core.ai.model.onFailure
import com.android.mediproject.core.ai.model.onSuccess
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedicinesDetectorFragment :
    BaseFragment<FragmentMedicinesDetectorBinding, MedicinesDetectorViewModel>(FragmentMedicinesDetectorBinding::inflate),
    CameraHelper.OnDetectionListener {

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
            fragmentViewModel.captureState.collect { state ->
                state.onSuccess { _, consumed ->
                    if (!consumed) {
                        (state as InferenceState.Success).consumed = true
                        findNavController().navigate(MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmFragment())
                    }
                }.onFailure {
                    toast(getString(R.string.noMedicinesDetected))
                }
            }
        }
    }

    private fun initializeCamera() {
        fragmentViewModel.connectCamera(binding.previewView, viewLifecycleOwner, this)
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

    override fun onDetect(objects: List<CameraHelper.OnDetectionListener.Object>, capturedImageWidth: Int, capturedImageHeight: Int) {
        if (isVisible) binding.overlayView.apply {
            setResults(objects, capturedImageWidth, capturedImageHeight)
            invalidate()
        }
    }

}
