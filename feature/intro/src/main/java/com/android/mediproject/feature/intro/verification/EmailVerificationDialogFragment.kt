package com.android.mediproject.feature.intro.verification

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
import com.android.mediproject.feature.intro.R
import com.android.mediproject.feature.intro.databinding.DialogEmailVerificationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class EmailVerficationDialogFragment : DialogFragment() {

    private var _binding: DialogEmailVerificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VerificationViewModel>()

    companion object {
        const val TAG = "EmailVerficationDialogFragment"
        const val CONFIRMED = "confirmed"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEmailVerificationBinding.inflate(layoutInflater, null, false)
        return MaterialAlertDialogBuilder(requireActivity()).apply {
            setView(binding.root)
        }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun getView(): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        init()
        viewLifecycleOwner.repeatOnStarted {
            viewModel.verificationState.filterNotNull().collect {
                when (it) {
                    is VerificationState.Verified -> {
                        Toast.makeText(requireContext(), getString(R.string.confirmedVerificationCode), Toast.LENGTH_SHORT).show()
                        parentFragmentManager.apply {
                            setFragmentResult(TAG, bundleOf(CONFIRMED to true))
                        }
                        dismiss()
                    }

                    is VerificationState.VerifyFailed -> {
                        Toast.makeText(requireContext(), getString(R.string.verificationCodeError), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun init() {
        binding.run {
            positiveButton.setOnClickListener {
                if (dialogSubtitle1.inputData.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.verificationCodeHint), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                this@EmailVerficationDialogFragment.viewModel.onClickWithText(dialogSubtitle1.inputData.text.toString())
            }
            negativeButton.setOnClickListener {
                dismiss()
            }
        }
    }

}
