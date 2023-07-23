package com.android.mediproject.feature.camera.imagedialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.feature.camera.InferenceState
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.R
import com.android.mediproject.feature.camera.databinding.FragmentDetectedImageDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted
import javax.inject.Inject


@AndroidEntryPoint
class DetectedImageFragment : DialogFragment() {
    private var _binding: FragmentDetectedImageDialogBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var systemBarStyler: SystemBarController

    private val medicinesDetectorViewModel: MedicinesDetectorViewModel by navGraphViewModels(R.id.camera_nav) {
        defaultViewModelProviderFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            isCancelable = true
            window?.let { window ->
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
                window.navigationBarColor = Color.TRANSPARENT
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = medicinesDetectorViewModel
            backBtn.setOnClickListener {
                dismiss()
            }

            systemBarStyler.changeMode(
                topViews = listOf(SystemBarStyler.ChangeView(backBtn, SystemBarStyler.SpacingType.MARGIN)),
                bottomViews = listOf(
                    SystemBarStyler.ChangeView(zoomOut, SystemBarStyler.SpacingType.MARGIN),
                ),
            )

            imageView.minimumScale = 1.0f
            imageView.maximumScale = 2.5f
            zoomIn.setOnClickListener {
                val scale = imageView.scale + 0.4f
                if (scale <= imageView.maximumScale) imageView.setScale(scale, true)
                else imageView.setScale(imageView.maximumScale, true)
            }
            zoomOut.setOnClickListener {
                val scale = imageView.scale - 0.4f
                if (scale >= imageView.minimumScale) imageView.setScale(scale, true)
                else imageView.setScale(imageView.minimumScale, true)
            }

            viewLifecycleOwner.repeatOnStarted {
                medicinesDetectorViewModel.inferenceState.collectLatest { objs ->
                    when (objs) {
                        is InferenceState.Detected -> {
                            objs.detection.run {
                                image = backgroundImage
                                executePendingBindings()
                                overlayView.setResults(
                                    detection.map {
                                        it.detection
                                    },
                                    backgroundImage.width, backgroundImage.height,
                                )
                                overlayView.invalidate()
                            }
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetectedImageDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.DialogFullscreen

    override fun onDestroyView() {
        binding.image?.recycle()
        super.onDestroyView()
        _binding = null
    }
}
