package com.android.mediproject.feature.camera.confirm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mediproject.core.common.util.SystemBarController
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.SpanMapper
import com.android.mediproject.feature.camera.databinding.FragmentConfirmBinding
import com.android.mediproject.feature.camera.onDetected
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
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

            bottomSheet.descriptionTextView.text = SpanMapper.createCheckCountsOfMedicinesMessage(requireContext())
            bottomSheet.cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            systemBarStyler.changeMode(
                listOf(SystemBarStyler.ChangeView(backBtn, SystemBarStyler.SpacingType.MARGIN)),
                listOf(SystemBarStyler.ChangeView(bottomSheet.root, SystemBarStyler.SpacingType.PADDING)),
            )

            imageView.minimumScale = 1.0f
            imageView.maximumScale = 2.5f

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
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
                medicineDetectorViewModel.inferenceState.replayCache.last().let { state ->
                    state.onDetected { detectionObjects, _ ->
                        detectionObjects.run {
                            bottomSheet.detectionTextView.text = SpanMapper.createCheckCountsOfMedicinesTitle(
                                requireContext(),
                                detection.size,
                            )

                            Glide.with(imageView.context).load(backgroundImage).centerInside().into(imageView)
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
