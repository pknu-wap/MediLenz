package com.android.mediproject.feature.camera.yolo
/*
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
import com.android.mediproject.core.common.viewmodel.UiState
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

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // 권한 부여 받았으므로 카메라 초기화
            initializeCamera()
        } else {
            // 권한 부여 거부된 경우 뒤로가기
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = fragmentViewModel

        viewLifecycleOwner.repeatOnStarted {
            launch {
                // 검출된 객체 상태
                fragmentViewModel.detectedObjects.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            LoadingDialog.dismiss()
                            findNavController().navigate(MedicinesDetectorFragmentDirections.actionMedicinesDetectorFragmentToConfirmDialogFragment())
                        }

                        is UiState.Error -> {
                            LoadingDialog.dismiss()
                            toast(getString(R.string.noMedicinesDetected))
                            fragmentViewModel.openCamera()
                        }

                        is UiState.Loading -> {
                            LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.getDetectedObjects))
                        }

                        is UiState.Initial -> {

                        }
                    }
                }
            }

            launch {
                // AI모델 로드 상태
                fragmentViewModel.aiModelState.collectLatest { state ->
                    when (state) {
                        is AiModelState.Loaded -> {
                            LoadingDialog.dismiss()
                            activity?.apply {
                                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                            }
                            fragmentViewModel.openCamera()
                        }

                        is AiModelState.Loading -> {
                            LoadingDialog.showLoadingDialog(requireActivity(), getString(R.string.loadingAiModels))
                        }

                        is AiModelState.LoadFailed -> {
                            LoadingDialog.dismiss()
                        }

                        is AiModelState.NotLoaded -> {

                        }
                    }

                }
            }
        }

    }


    private fun initializeCamera() {
        binding.apply {
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

            // AI모델 로드
            fragmentViewModel.loadModel(requireContext().assets)
        }
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

 */