package com.android.mediproject.feature.camera.confirmdialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.mediproject.core.common.dialog.LoadingDialog
import com.android.mediproject.feature.camera.InferenceState
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.R
import com.android.mediproject.feature.camera.databinding.FragmentConfirmDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class ConfirmDialogFragment : DialogFragment() {
    private var _binding: FragmentConfirmDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedicinesDetectorViewModel by navGraphViewModels(R.id.camera_nav) {
        defaultViewModelProviderFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity()).apply {
            setTitle(getString(R.string.checkCountsOfMedicines))
            _binding = FragmentConfirmDialogBinding.inflate(layoutInflater, null, false)
            setView(onCreateView(layoutInflater, binding.root, savedInstanceState))
            setPositiveButton(getString(R.string.search)) { _, _ ->
                LoadingDialog.showLoadingDialog(requireContext(), getString(R.string.classifyingMedicines))
                viewModel.classifyMedicine((viewModel.inferenceState.replayCache.last() as InferenceState.Detected).detection)
            }
            setNegativeButton(getString(R.string.close)) { _, _ ->
                findNavController().popBackStack()
            }.setCancelable(true)
        }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun getView(): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.apply {
            setOnDismissListener {
                viewModel.cameraController.resume()
            }
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.inferenceState.collectLatest { detectionState ->
                when (detectionState) {
                    is InferenceState.Detected -> {
                        val detection = detectionState.detection
                        val text = "${detection.detection.size}${getString(R.string.checkCountsOfMedicinesMessage)}".toSpannable()
                        binding.confirmDialogTextView.text = text

                        binding.detectedObjectsRecyclerView.adapter = ImageListAdapter().apply {
                            detectionState.detection.detection.map {
                                it.onClick = { this@ConfirmDialogFragment.onDetectedObjectClicked() }
                                it
                            }.let {
                                submitList(it)
                            }
                        }
                    }

                    else -> {

                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun onDetectedObjectClicked() {
        findNavController().navigate(ConfirmDialogFragmentDirections.actionConfirmDialogFragmentToDetectedImageFragment())
    }
}
