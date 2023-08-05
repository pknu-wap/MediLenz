package com.android.mediproject.feature.mypage.mypagemore

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
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
import com.android.mediproject.core.common.LOGOUT
import com.android.mediproject.core.common.WITHDRAWAL
import com.android.mediproject.core.ui.base.view.Subtitle.Companion.PASSWORD
import com.android.mediproject.feature.mypage.R
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        object Logout : DialogFlag()
    }

    private val fragmentViewModel: MyPageMoreDialogViewModel by viewModels()
    private var _binding: FragmentMyPageMoreDialogBinding? = null
    val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity()).apply {
            _binding = FragmentMyPageMoreDialogBinding.inflate(layoutInflater, null, false)
            setView(onCreateView(layoutInflater, binding.rootLayout, savedInstanceState))
        }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun getView(): View = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { dialogFlag.collect { handleDialogFlag(it) } }
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
                    }

                    is DialogFlag.ChangePassword -> {
                        val newPassword = binding.dialogSubtitle1.getEditable()
                        fragmentViewModel.changePassword(newPassword)
                    }

                    is DialogFlag.Withdrawal -> {
                        val withdrawalInput = binding.dialogSubtitle1.getValue()
                        fragmentViewModel.withdrawal(withdrawalInput)
                    }

                    is DialogFlag.Logout -> {
                        fragmentViewModel.logout()
                    }
                }
            }

            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.Toast -> Toast.makeText(
                requireContext(),
                event.message,
                Toast.LENGTH_SHORT
            )
                .show()

            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.CancelDialog -> dismiss()
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.WithdrawalComplete -> setFragmentResult(
                TAG,
                bundleOf(TAG to WITHDRAWAL)
            )

            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.ChangeNicknameComplete -> setFragmentResult(
                TAG,
                bundleOf(TAG to CHANGE_NICKNAME)
            )

            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.LogoutComplete -> setFragmentResult(
                TAG,
                bundleOf(TAG to LOGOUT))
        }
    }

    private fun handleDialogFlag(dialogFlag: DialogFlag) {
        when (dialogFlag) {
            //닉네임 변경 다이얼로그
            is DialogFlag.ChangeNickName -> {
                binding.apply {
                    completeButtonDisEnabled()
                    dialogTitleTV.text = getString(R.string.changeNickName)
                    dialogSubtitle1.apply {
                        setTitle(getString(com.android.mediproject.core.ui.R.string.nickName))
                        setHint(getString(com.android.mediproject.core.ui.R.string.nickNameHint))
                        inputData.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?, start: Int, count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?, start: Int, before: Int, count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (s.toString().length != 0) completeButtonEnabled()
                                else completeButtonDisEnabled()
                            }
                        })
                    }
                }
            }

            //비밀번호 변경 다이얼로그
            is DialogFlag.ChangePassword -> {
                binding.apply {
                    completeButtonDisEnabled()
                    dialogTitleTV.text = getString(R.string.changePassword)
                    dialogSubtitle1.apply {
                        setTitle(getString(com.android.mediproject.core.ui.R.string.password))
                        setHint(getString(com.android.mediproject.core.ui.R.string.passwordHint))
                        setDataType(PASSWORD)
                    }
                    dialogSubtitle2.apply {
                        setDataType(PASSWORD)
                        visibility = View.VISIBLE
                        inputData.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?, start: Int, count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?, start: Int, before: Int, count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (s.toString().length != 0 && (s.toString() == dialogSubtitle1.getValue())) completeButtonEnabled()
                                else completeButtonDisEnabled()
                            }
                        })
                    }
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
                                ),
                                4,
                                8,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )
                            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        }
                    completeButtonDisEnabled()
                    dialogTitleTV.text = getString(R.string.withdrawal)
                    dialogSubtitle1.apply {
                        title.text = span
                        setHint(getString(R.string.withdrawalHint))
                        setTitleStyleNormal()
                        inputData.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?, start: Int, count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?, start: Int, before: Int, count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (s.toString() == "회원탈퇴") completeButtonEnabled()
                                else completeButtonDisEnabled()
                            }
                        })
                    }
                }
            }

            is DialogFlag.Logout -> {
                binding.apply {
                    val span =
                        SpannableStringBuilder(getString(R.string.logoutDescription)).apply {
                            setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        com.android.mediproject.core.ui.R.color.red
                                    )
                                ),
                                4,
                                8,
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE
                            )
                            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        }
                    dialogTitleTV.text = getString(R.string.logout)
                    dialogSubtitle1.visibility = View.GONE
                    logoutDescriptionTV.visibility = View.VISIBLE
                    logoutDescriptionTV.text = span
                }
            }
        }
    }

    private fun completeButtonDisEnabled() = binding.myPageDialogCompleteTV.apply {
        isEnabled = false
        alpha = 0.5.toFloat()
    }

    private fun completeButtonEnabled() = binding.myPageDialogCompleteTV.apply {
        isEnabled = true
        alpha = 1.toFloat()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}