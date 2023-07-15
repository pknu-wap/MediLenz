package com.android.mediproject.feature.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.databinding.FragmentMedicinesDetectorBinding
import com.android.mediproject.feature.camera.tflite.CameraHelper
import com.android.mediproject.feature.search.result.ai.AiSearchResultViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    @Inject lateinit var systemBarStyler: SystemBarStyler

    // AI관련 모든 처리 담당
    private val mainScope = MainScope()

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // 권한 부여 받았으므로 카메라 초기화
            initializeCamera()
        } else {
            // 권한 부여 거부된 경우 뒤로가기
            findNavController().popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systemBarStyler.changeMode(
            listOf(SystemBarStyler.ChangeView(binding.logo, SystemBarStyler.SpacingType.MARGIN)),
            listOf(SystemBarStyler.ChangeView(binding.detectionDescription, SystemBarStyler.SpacingType.MARGIN)),
        )

        fragmentViewModel.apply {
            // AI처리 객체를 관리하기 위해 생명주기 리스너를 설정
            cameraController.fragmentLifeCycleOwner = this@MedicinesDetectorFragment.viewLifecycleOwner
            // AI처리 객체의 콜백을 현재 프래그먼트로 설정
            cameraController.detectionCallback = this@MedicinesDetectorFragment
        }

        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            viewModel = fragmentViewModel

            viewLifecycleOwner.repeatOnStarted {
                launch {
                    // AI모델 로드 상태
                    fragmentViewModel.aiModelState.collectLatest { state ->
                        when (state) {
                            is AiModelState.Loaded -> {
                                LoadingDialog.dismiss()
                            }

                            is AiModelState.Loading -> {
                                LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.loadingAiModels))
                            }

                            is AiModelState.LoadFailed -> {
                                LoadingDialog.dismiss()
                                findNavController().popBackStack()
                            }

                            is AiModelState.NotLoaded -> {

                            }
                        }

                    }
                }

                launch {
                    fragmentViewModel.inferenceState.collectLatest { state ->
                        when (state) {
                            is InferenceState.Detected -> {
                                findNavController().navigate(MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmDialogFragment())
                            }

                            is InferenceState.Detecting -> {
                                overlayView.apply {
                                    if (results.isNotEmpty()) {
                                        fragmentViewModel.cameraController.pause()
                                        fragmentViewModel.makeDetectionResult(
                                            results,
                                            Size(overlayView.width, overlayView.height),
                                            Size(overlayView.resizedWidth, overlayView.resizeHeight),
                                            previewView.bitmap,
                                        )
                                    } else {
                                        toast(getString(R.string.noMedicinesDetected))
                                    }
                                }
                            }

                            is InferenceState.Initial -> {

                            }

                            is InferenceState.DetectFailed -> {
                                fragmentViewModel.cameraController.resume()
                                toast(getString(R.string.detectionFailed))
                            }

                            is InferenceState.Classified -> {
                                aiSearchResultViewModel.setClassificationResult(state.classificationResult)
                                findNavController().navigate(
                                    "medilens://main/search/search_medicines/airesult".toUri(),
                                    NavOptions.Builder()
                                        .setPopUpTo(com.android.mediproject.feature.search.R.id.aiSearchResultFragment, true).build(),
                                )
                                LoadingDialog.dismiss()
                            }
                        }

                    }
                }
            }

        }

    }

    private fun initializeCamera() {
        fragmentViewModel.loadModel(binding.previewView)
    }

    override fun onStart() {
        super.onStart()
        when {
            // 권한 확인
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // 권한 부여 받은 상태이므로 카메라 초기화
                initializeCamera()
            }
            // 권한 요청 다이얼로그 표시
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.cameraPermission))
                    .setMessage(getString(R.string.cameraPermissionMessage)).setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }.setNegativeButton(getString(R.string.close)) { _, _ -> }.setCancelable(false).show()
            }
            // 권한 요청
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    override fun onDetectedResult(objects: List<Detection>, width: Int, height: Int) {
        mainScope.launch {
            binding.overlayView.apply {
                setResults(objects, width, height)
                invalidate()
            }
        }
    }
}
