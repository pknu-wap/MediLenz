package com.android.mediproject.feature.intro.verification

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.mediproject.feature.intro.databinding.DialogEmailVerificationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailVerficationDialogFragment : DialogFragment() {

    private var _binding: DialogEmailVerificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VerificationViewModel>()

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
        setDialog()
    }

    private fun setDialog() {
        binding.run {}
    }

}
