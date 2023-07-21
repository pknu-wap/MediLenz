package com.android.mediproject.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.android.mediproject.feature.camera.tflite.CameraHelper
import com.android.mediproject.feature.search.result.ai.AiSearchResultViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.task.gms.vision.detector.Detection
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MedicinesDetectorFragment :
    BaseFragment<FragmentMedicinesDetectorBinding, MedicinesDetectorViewModel>(FragmentMedicinesDetectorBinding::inflate),
    CameraHelper.OnDetectionCallback {

    override val fragmentViewModel: MedicinesDetectorViewModel by navGraphViewModels(R.id.camera_nav) {
        defaultViewModelProviderFactory
    }

    private val aiSearchResultViewModel by activityViewModels<AiSearchResultViewModel>()

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

        fragmentViewModel.apply {
            cameraController.fragmentLifeCycleOwner = this@MedicinesDetectorFragment.viewLifecycleOwner
            cameraController.detectionCallback = this@MedicinesDetectorFragment
        }

        binding.apply {
            viewModel = fragmentViewModel
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            // AI모델 로드 상태
            fragmentViewModel.aiModelState.collect { state ->
                when (state) {
                    is AiModelState.Loaded -> {
                        LoadingDialog.dismiss()
                    }

                    is AiModelState.Loading -> {
                        LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.loadingAiModels))
                    }

                    is AiModelState.LoadFailed -> {
                        toast(getString(R.string.aiModelLoadFailed))
                        LoadingDialog.dismiss()
                        findNavController().popBackStack()
                    }
                }

            }
        }

        viewLifecycleOwner.repeatOnStarted {
            fragmentViewModel.inferenceState.collect { state ->
                when (state) {
                    is InferenceState.Detected -> {
                        findNavController().navigate(MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmDialogFragment())
                    }

                    is InferenceState.Initial -> {

                    }

                    is InferenceState.DetectFailed -> {
                        toast(getString(R.string.noMedicinesDetected))
                    }

                    is InferenceState.Classified -> {
                        aiSearchResultViewModel.setClassificationResult(state.classificationResult)
                        navigateWithUriNavOptions(
                            "medilens://main/search/search_medicines/airesult",
                            NavOptions.Builder().setPopUpTo(com.android.mediproject.feature.search.R.id.aiSearchResultFragment, true).build(),
                        )
                        LoadingDialog.dismiss()
                    }
                }
            }

        }
    }

    private fun initializeCamera() {
        fragmentViewModel.startCamera(binding.previewView)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionLauncher.unregister()
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

    override fun onDetectedResult(objects: List<Detection>, width: Int, height: Int) {
        if (isVisible) binding.overlayView.apply {
            setResults(objects, width, height)
            invalidate()
        }
    }
}
