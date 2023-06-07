package com.android.mediproject.feature.mypage.mypagemore

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.CHANGE_NICKNAME
import com.android.mediproject.core.common.WITHDRAWAL
import com.android.mediproject.core.common.uiutil.dialogResize
import com.android.mediproject.core.common.util.isPasswordValid
import com.android.mediproject.feature.mypage.R
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MyPageMoreDialogFragment(private val flag: DialogFlag) : DialogFragment() {

    companion object {
        const val TAG = "MyPageMoreDialogFragment"
    }

    sealed class DialogFlag {
        object ChangeNickName : DialogFlag()
        object ChangePassword : DialogFlag()
        object Withdrawal : DialogFlag()
    }

    private val fragmentViewModel: MyPageMoreDialogViewModel by viewModels()
    private var _binding: FragmentMyPageMoreDialogBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.isCancelable = false
        _binding = FragmentMyPageMoreDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        when (flag) {
            is DialogFlag.ChangeNickName -> requireContext().dialogResize(this, 0.95f, 0.38f)
            is DialogFlag.ChangePassword -> requireContext().dialogResize(this, 0.95f, 0.5f)
            is DialogFlag.Withdrawal -> requireContext().dialogResize(this, 0.95f, 0.4f)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { dialogFlag.collect { handleFlag(it) } }
                }
                setDialogFlag(flag)
            }
        }
    }

    private fun handleEvent(event: MyPageMoreDialogViewModel.MyPageMoreDialogEvent) {
        when (event) {
            //확인 버튼을 눌렀을 때 로직
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.CompleteDialog -> {
                when (fragmentViewModel.dialogFlag.value) {
                    is DialogFlag.ChangeNickName -> {
                        val newNickname = binding.dialogSubtitle1.getValue()
                        fragmentViewModel.changeNickname(newNickname)
                        setFragmentResult(TAG, bundleOf(TAG to CHANGE_NICKNAME))
                    }
                    is DialogFlag.ChangePassword -> {
                        val newPassword = binding.dialogSubtitle1.getEditable()
                        fragmentViewModel.changePassword(newPassword)
                    }
                    is DialogFlag.Withdrawal -> {
                        val withdrawalInput = binding.dialogSubtitle1.getValue()
                        fragmentViewModel.withdrawal(withdrawalInput)
                    }
                }
            }
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.Toast -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.CancelDialog -> dismiss()
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.WithdrawalComplete -> setFragmentResult(
                TAG,
                bundleOf(TAG to WITHDRAWAL)
            )
        }
    }

    private fun handleFlag(dialogFlag: MyPageMoreDialogFragment.DialogFlag) {
        when (dialogFlag) {
            //닉네임 변경 다이얼로그
            is DialogFlag.ChangeNickName -> {
                binding.apply {
                    dialogTitleTV.text = getString(R.string.changeNickName)
                    dialogSubtitle1.setTitle(getString(com.android.mediproject.core.ui.R.string.nickName))
                    dialogSubtitle1.setHint(getString(com.android.mediproject.core.ui.R.string.nickNameHint))
                    dialogSubtitle2.visibility = View.GONE
                }
            }

            //비밀번호 변경 다이얼로그
            is DialogFlag.ChangePassword -> {
                binding.apply {
                    dialogTitleTV.text = getString(R.string.changePassword)
                    dialogSubtitle1.setTitle(getString(com.android.mediproject.core.ui.R.string.password))
                    dialogSubtitle1.setHint(getString(com.android.mediproject.core.ui.R.string.passwordHint))
                    dialogSubtitle1.set
                    dialogSubtitle2.visibility = View.VISIBLE
                }
            }

            //회원 탈퇴 다이얼로그
            is DialogFlag.Withdrawal -> {
                binding.apply {
                    val span =
                        SpannableStringBuilder(getString(R.string.withdrawalDescription)).apply {
                            setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        com.android.mediproject.core.ui.R.color.red
                                    )
                                ), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )
                            setSpan(
                                UnderlineSpan(),
                                4,
                                8,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )
                        }

                    dialogTitleTV.text = getString(R.string.withdrawal)
                    dialogSubtitle1.title.text = span
                    dialogSubtitle1.setHint(getString(R.string.withdrawalHint))
                    dialogSubtitle1.setTitleStyleNormal()
                    dialogSubtitle2.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}