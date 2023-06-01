package com.android.mediproject.feature.camera.imagedialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.android.mediproject.core.common.bindingadapter.GlideApp
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.feature.camera.MedicinesDetectorViewModel
import com.android.mediproject.feature.camera.databinding.FragmentDetectedImageDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted


@AndroidEntryPoint
class DetectedImageFragment : DialogFragment() {
    private var _binding: FragmentDetectedImageDialogBinding? = null
    private val binding get() = _binding!!

    private val medicinesDetectorViewModel by activityViewModels<MedicinesDetectorViewModel>()

    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = super.onCreateDialog(savedInstanceState).also { dialog ->
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = medicinesDetectorViewModel
        viewLifecycleOwner.repeatOnStarted {
            medicinesDetectorViewModel.capturedImage.collectLatest {
                when (it) {
                    is UiState.Success -> {
                        GlideApp.with(requireContext()).load(it.data).into(binding.imageView)
                    }

                    else -> {

                    }
                }
            }
        }

        binding.apply {
            scaleGestureDetector = ScaleGestureDetector(requireContext(), object : SimpleOnScaleGestureListener() {
                private var mScaleFactor = 1.0f

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    mScaleFactor *= scaleGestureDetector.scaleFactor
                    mScaleFactor = maxOf(0.1f, minOf(mScaleFactor, 10.0f))
                    imageView.scaleX = mScaleFactor
                    imageView.scaleY = mScaleFactor
                    return true
                }
            })

            imageView.setOnTouchListener { v, event ->
                scaleGestureDetector.onTouchEvent(event)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetectedImageDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getView(): View {
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun isCancelable(): Boolean = false
}