package com.android.mediproject.feature.mypage.mypagemore

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
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

    enum class BottomSheetFlag(val value: Int) {
        CHANGE_NICKNAME(301),
        CHANGE_PASSWORD(302),
        WITHDRAWAL(303),
        LOGOUT(304)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { bottomsheetFlag.collect { handleFlag(it) } }
                }
            }
        }
    }

    private fun handleEvent(event: MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent) {
        when (event) {
            is MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent.CompleteBottomSheet -> {
                when (event.flag) {
                    BottomSheetFlag.CHANGE_NICKNAME -> completeBottomSheetChangeNickname()
                    BottomSheetFlag.CHANGE_PASSWORD -> completeBottomSheetChangePassword()
                    BottomSheetFlag.WITHDRAWAL -> completeBottomSheetWithdrawal()
                    BottomSheetFlag.LOGOUT -> completeBottomSheetLogout()
                }
            }
        }
    }

    private fun completeBottomSheetChangeNickname() {
        setFragmentResult(TAG, bundleOf(TAG to BottomSheetFlag.CHANGE_NICKNAME.value))
    }

    private fun completeBottomSheetChangePassword() {
        setFragmentResult(TAG, bundleOf(TAG to BottomSheetFlag.CHANGE_PASSWORD.value))
    }

    private fun completeBottomSheetWithdrawal() {
        setFragmentResult(TAG, bundleOf(TAG to BottomSheetFlag.WITHDRAWAL.value))
    }

    private fun completeBottomSheetLogout() {
        setFragmentResult(TAG, bundleOf(TAG to BottomSheetFlag.LOGOUT.value))
    }

    private fun handleFlag(flag: BottomSheetFlag) {
        when (flag) {
            BottomSheetFlag.CHANGE_NICKNAME -> highlight(binding.changeNickNameTV)
            BottomSheetFlag.CHANGE_PASSWORD -> highlight(binding.changePasswordTV)
            BottomSheetFlag.WITHDRAWAL -> highlight(binding.withdrawalTV)
            BottomSheetFlag.LOGOUT -> highlight(binding.logoutTV)
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
