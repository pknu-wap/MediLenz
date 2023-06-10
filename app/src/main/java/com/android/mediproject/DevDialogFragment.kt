package com.android.mediproject

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import com.android.mediproject.databinding.ViewDevStateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevDialogFragment : BottomSheetDialogFragment() {

    private var _binding: ViewDevStateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ViewDevStateBinding.inflate(layoutInflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val text = Html.fromHtml(getString(R.string.messageDevelopment), Html.FROM_HTML_MODE_COMPACT).toSpannable()
            messageTextView.text = text
        }

        binding.okButton.setOnClickListener {
            dismiss()
        }
    }
}