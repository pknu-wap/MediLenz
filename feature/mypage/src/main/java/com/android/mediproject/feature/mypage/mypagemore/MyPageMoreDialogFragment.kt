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
import com.android.mediproject.core.ui.base.view.Subtitle.Companion.PASSWORD
import com.android.mediproject.feature.mypage.R
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.android.mediproject.core.common.viewmodel.repeatOnStarted

@AndroidEntryPoint
class MyPageMoreDialogFragment(private val flag: DialogType) : DialogFragment() {

    companion object {
        const val TAG = "MyPageMoreDialogFragment"
    }

    enum class DialogType(val value: Int) {
        CHANGE_NICKNAME(305),
        CHANGE_PASSWORD(306),
        WITHDRAWAL(307),
        LOGOUT(308)
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
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun getView(): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                repeatOnStarted { dialogType.collect { handleDialogType(it) } }
                repeatOnStarted { myPageMoreDialogState.collect { handleDialogState(it) } }
            }
            setDialogType(flag)
        }
    }

    private fun handleEvent(event: MyPageMoreDialogViewModel.MyPageMoreDialogEvent) {
        when (event) {
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.CompleteDialog -> handleCompleteDialog()
            is MyPageMoreDialogViewModel.MyPageMoreDialogEvent.CancelDialog -> dismiss()
        }
    }

    private fun handleCompleteDialog() {
        when (getDialogType()) {
            DialogType.CHANGE_NICKNAME -> changeNickname()
            DialogType.CHANGE_PASSWORD -> changePassword()
            DialogType.WITHDRAWAL -> withdrawal()
            DialogType.LOGOUT -> logout()
        }
    }

    private fun handleDialogState(dialogState: MyPageMoreDialogViewModel.MyPageDialogState) {
        when (dialogState) {
            is MyPageMoreDialogViewModel.MyPageDialogState.Success -> handleSuccessFlag(dialogState.myPageDialogFlag)
            is MyPageMoreDialogViewModel.MyPageDialogState.Error -> handleErrorFlag(dialogState.myPageDialogFlag)
            is MyPageMoreDialogViewModel.MyPageDialogState.initial -> Unit
        }
    }

    private fun handleSuccessFlag(dialogFlag: MyPageMoreDialogViewModel.MyPageDialogFlag) {
        when (dialogFlag) {
            MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGENICKNAME -> completeChangeNickName()
            MyPageMoreDialogViewModel.MyPageDialogFlag.WITHDRAWAL -> completeWithdrawal()
            MyPageMoreDialogViewModel.MyPageDialogFlag.LOGOUT -> completeLogout()
            MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGEPASSWORD -> completeChangePassword()
        }
    }

    private fun handleErrorFlag(dialogFlag: MyPageMoreDialogViewModel.MyPageDialogFlag) {
        when (dialogFlag) {
            MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGENICKNAME -> errorChangeNickName()
            MyPageMoreDialogViewModel.MyPageDialogFlag.WITHDRAWAL -> errorWithdrawal()
            MyPageMoreDialogViewModel.MyPageDialogFlag.LOGOUT -> errorLogout()
            MyPageMoreDialogViewModel.MyPageDialogFlag.CHANGEPASSWORD -> errorChangePassword()
        }
    }

    private fun completeWithdrawal() {
        setFragmentResult(
            TAG,
            bundleOf(TAG to DialogType.WITHDRAWAL.value),
        )
        dismiss()
    }

    private fun completeChangeNickName() {
        setFragmentResult(
            TAG,
            bundleOf(TAG to DialogType.CHANGE_NICKNAME),
        )
        dismiss()
    }

    private fun completeLogout() {
        setFragmentResult(
            TAG,
            bundleOf(TAG to DialogType.LOGOUT.value),
        )
        toast("로그아웃이 완료되었습니다.")
        dismiss()
    }

    private fun completeChangePassword() {
        setFragmentResult(
            TAG,
            bundleOf(TAG to DialogType.CHANGE_PASSWORD),
        )
        toast("비밀번호 변경이 완료되었습니다.")
        dismiss()
    }

    private fun errorChangeNickName(){
        toast("닉네임 변경에 실패하셨습니다.")
    }

    private fun errorWithdrawal(){
        toast("회원 탈퇴에 실패하셨습니다.")
    }

    private fun errorLogout(){
        toast("로그아웃에 실패하셨습니다.")
    }

    private fun errorChangePassword(){
        toast("비밀번호 변경에 실패하셨습니다.")
    }

    private fun getDialogType(): DialogType {
        return fragmentViewModel.dialogType.value
    }

    private fun changeNickname() {
        fragmentViewModel.changeNickname(binding.dialogSubtitle1.getValue())
    }

    private fun changePassword() {
        fragmentViewModel.changePassword(binding.dialogSubtitle1.getEditable())
    }

    private fun withdrawal() {
        fragmentViewModel.withdrawal()
    }

    private fun logout() {
        fragmentViewModel.logout()
    }

    private fun toast(str: String) {
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }

    private fun handleDialogType(dialogType: DialogType) {
        when (dialogType) {
            DialogType.CHANGE_NICKNAME -> setChangeNickNameDialog()
            DialogType.CHANGE_PASSWORD -> setChangePasswordDialog()
            DialogType.WITHDRAWAL -> setWithdrawalDialog()
            DialogType.LOGOUT -> setLogoutDialog()
        }
    }

    private fun setChangeNickNameDialog() = binding.apply {
        completeButtonDisEnabled()
        dialogTitleTV.text = getString(R.string.changeNickName)
        setChangeNickNameSubtitle()
    }

    private fun setChangePasswordDialog() = binding.apply {
        completeButtonDisEnabled()
        dialogTitleTV.text = getString(R.string.changePassword)
        setChangePasswordSubtitle()
    }

    private fun setWithdrawalDialog() = binding.apply {
        completeButtonDisEnabled()
        dialogTitleTV.text = getString(R.string.withdrawal)
        setWithdrawalSubtitle()
    }

    private fun setLogoutDialog() = binding.apply {
        dialogTitleTV.text = getString(R.string.logout)
        logoutDescriptionTV.text = getLogoutSpan()
        setLogoutVisible()
    }

    private fun setChangeNickNameSubtitle() = binding.dialogSubtitle1.apply {
        setTitle(getString(com.android.mediproject.core.ui.R.string.nickName))
        setHint(getString(com.android.mediproject.core.ui.R.string.nickNameHint))
        inputData.addTextChangedListener(getChangeNicknameTextChangedListner())
    }

    private fun getChangeNicknameTextChangedListner(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int,
            ) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable?.isNotEmpty()!!) completeButtonEnabled()
                else completeButtonDisEnabled()
            }
        }
    }

    private fun setChangePasswordSubtitle() = binding.apply {
        dialogSubtitle1.apply {
            setTitle(getString(com.android.mediproject.core.ui.R.string.password))
            setHint(getString(com.android.mediproject.core.ui.R.string.passwordHint))
            setDataType(PASSWORD)
        }
        dialogSubtitle2.apply {
            setDataType(PASSWORD)
            visibility = View.VISIBLE
            inputData.addTextChangedListener(getChangePasswordTextChangedListner())
        }
    }

    private fun getChangePasswordTextChangedListner(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (checkChangePasswordEditable(s)) completeButtonEnabled()
                else completeButtonDisEnabled()
            }
        }
    }

    private fun checkChangePasswordEditable(editable: Editable?): Boolean {
        return (editable?.isNotEmpty()!!) && (editable.toString() == binding.dialogSubtitle1.getValue())
    }

    private fun setWithdrawalSubtitle() = binding.dialogSubtitle1.apply {
        title.text = getWithdrawalSpan()
        setHint(getString(R.string.withdrawalHint))
        setTitleStyleNormal()
        inputData.addTextChangedListener(getWithdrawalTextChangedListner())
    }

    private fun getWithdrawalTextChangedListner(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (checkEditableWithdrawal(s)) completeButtonEnabled()
                else completeButtonDisEnabled()
            }
        }
    }

    private fun getWithdrawalSpan(): SpannableStringBuilder {
        return SpannableStringBuilder(getString(R.string.withdrawalDescription)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        com.android.mediproject.core.ui.R.color.red,
                    ),
                ),
                4,
                8,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    private fun setLogoutVisible() = binding.apply {
        dialogSubtitle1.visibility = View.GONE
        logoutDescriptionTV.visibility = View.VISIBLE
    }

    private fun getLogoutSpan(): SpannableStringBuilder {
        return SpannableStringBuilder(getString(R.string.logoutDescription)).apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        com.android.mediproject.core.ui.R.color.red,
                    ),
                ),
                4,
                8,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE,
            )
            setSpan(UnderlineSpan(), 4, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    private fun checkEditableWithdrawal(editable: Editable?): Boolean {
        return editable.toString() == "회원탈퇴"
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
