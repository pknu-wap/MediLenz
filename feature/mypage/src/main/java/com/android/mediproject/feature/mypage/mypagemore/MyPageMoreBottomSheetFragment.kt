package com.android.mediproject.feature.mypage.mypagemore

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.CHANGE_NICKNAME
import com.android.mediproject.core.common.CHANGE_PASSWORD
import com.android.mediproject.core.common.LOGOUT
import com.android.mediproject.core.common.WITHDRAWAL
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MyPageMoreBottomSheetFragment(private val backCallback: () -> Unit) :
    BottomSheetDialogFragment() {

    companion object {
        const val TAG = "MyPageMoreBottomSheetFragment"
    }

    private var _binding: FragmentMyPageMoreBottomSheetBinding? = null
    val binding get() = _binding!!
    private val fragmentViewModel: MyPageMoreBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageMoreBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { bottomsheetFlag.collect { handleFlag(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { onShowListner(bottomSheetDialog) }
        return bottomSheetDialog
    }

    private fun onShowListner(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog
            .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

        if (isBottomSheetNotNull(bottomSheet)) {
            blockBottomSheetDragging(bottomSheet!!)
        }
    }

    private fun isBottomSheetNotNull(bottomSheet: FrameLayout?): Boolean {
        return bottomSheet != null
    }

    private fun blockBottomSheetDragging(bottomSheet: FrameLayout) {
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        behavior.isDraggable = false
    }

    private fun handleEvent(event: MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent) =
        when (event) {
            is MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent.Confirm -> {
                when (event.flag) {
                    CHANGE_NICKNAME -> setFragmentResult(TAG, bundleOf(TAG to CHANGE_NICKNAME))
                    CHANGE_PASSWORD -> setFragmentResult(TAG, bundleOf(TAG to CHANGE_PASSWORD))
                    WITHDRAWAL -> setFragmentResult(TAG, bundleOf(TAG to WITHDRAWAL))
                    LOGOUT -> setFragmentResult(TAG, bundleOf(TAG to LOGOUT))
                    else -> Unit
                }
            }
        }

    private fun handleFlag(flag: Int) {
        when (flag) {
            CHANGE_NICKNAME -> highlight(binding.changeNickNameTV)
            CHANGE_PASSWORD -> highlight(binding.changePasswordTV)
            WITHDRAWAL -> highlight(binding.withdrawalTV)
            LOGOUT -> highlight(binding.logoutTV)
            else -> Unit
        }
    }

    private fun highlight(view: TextView) {
        val flagTextViewList = listOf(binding.changePasswordTV, binding.withdrawalTV, binding.changeNickNameTV, binding.logoutTV)

        flagTextViewList.forEach { flagTextView ->
            if (view == flagTextView) {
                highlightTextView(view)
            } else {
                normalTextView(flagTextView)
            }
        }
    }

    private fun highlightTextView(view: TextView) {
        view.apply {
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.android.mediproject.core.ui.R.color.main,
                ),
            )
            setTypeface(null, Typeface.BOLD)
        }
    }

    private fun normalTextView(view: TextView) {
        view.apply {
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.android.mediproject.core.ui.R.color.gray3,
                ),
            )
            setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        backCallback()
    }
}
