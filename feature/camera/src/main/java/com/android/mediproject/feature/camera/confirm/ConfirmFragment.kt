package com.android.mediproject.feature.camera.confirm

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.viewmodel.onSuccess
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.InferenceState
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.databinding.FragmentConfirmBinding
import com.android.mediproject.feature.camera.databinding.ViewClassificationLoadingBinding
import com.android.mediproject.feature.camera.onSuccess
import com.android.mediproject.feature.camera.util.SpanMapper
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.pknujsp.simpledialog.SimpleDialogBuilder
import io.github.pknujsp.simpledialog.constants.DialogType
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmFragment : BaseFragment<FragmentConfirmBinding, ConfirmViewModel>(FragmentConfirmBinding::inflate) {

    private val medicineDetectorViewModel: MedicinesDetectorViewModel by activityViewModels()

    override val fragmentViewModel: ConfirmViewModel by viewModels()

    @Inject lateinit var systemBarStyler: SystemBarController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = medicineDetectorViewModel
            systemBarStyler.changeMode(
                listOf(SystemBarStyler.ChangeView(backBtn, SystemBarStyler.SpacingType.MARGIN)),
                listOf(SystemBarStyler.ChangeView(bottomSheet.root, SystemBarStyler.SpacingType.PADDING)),
            )

            bottomSheet.descriptionTextView.text = SpanMapper.createCheckCountsOfMedicinesMessage(requireContext())
            bottomSheet.cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            bottomSheet.searchBtn.setOnClickListener {
                SimpleDialogBuilder.builder(requireActivity(), DialogType.Normal).setDim(true).setBehindBlur(true).setCornerRadius(0)
                    .setBackgroundColor(Color.TRANSPARENT).setBehindBlur(blur = true, forceApply = false).setLayoutSize(MATCH_PARENT, MATCH_PARENT)
                    .setContentView(ViewClassificationLoadingBinding.inflate(layoutInflater).root).buildAndShow()
            }

            imageView.minimumScale = 1.0f
            imageView.maximumScale = 2.5f
            val scaleAmount = 0.4f

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            bottomSheet.searchBtn.setOnClickListener {
                val detectionResultEntity = medicineDetectorViewModel.inferenceState.replayCache.last() as InferenceState.Success
                fragmentViewModel.classify(detectionResultEntity.entity)
            }
            zoomIn.setOnClickListener {
                val scale = imageView.scale + scaleAmount
                if (scale <= imageView.maximumScale) imageView.setScale(scale, true)
                else imageView.setScale(imageView.maximumScale, true)
            }
            zoomOut.setOnClickListener {
                val scale = imageView.scale - scaleAmount
                if (scale >= imageView.minimumScale) imageView.setScale(scale, true)
                else imageView.setScale(imageView.minimumScale, true)
            }

            viewLifecycleOwner.repeatOnStarted {
                medicineDetectorViewModel.inferenceState.replayCache.last().let { state ->
                    state.onSuccess { detectionObjects, _ ->
                        detectionObjects.run {
                            bottomSheet.detectionTextView.text = SpanMapper.createCheckCountsOfMedicinesTitle(
                                requireContext(),
                                detection.size,
                            )

                            fragmentViewModel.createBitmap(
                                detectionObjects,
                            )
                        }

                    }
                }
            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.bitmap.collect {
                    it.onSuccess { bitmap ->
                        Glide.with(requireContext()).load(bitmap).into(imageView)
                    }
                }
            }

            viewLifecycleOwner.repeatOnStarted {
                fragmentViewModel.classificationResult.collect { state ->
                    state.onSuccess { classificationResultEntities, consumed ->
                        toast(
                            classificationResultEntities.map {
                                it.classificationRecognitionEntity.medicineSeq
                            }.toString(),
                        )
                    }
                }
            }
        }
    }

}
