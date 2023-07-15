package com.android.mediproject.feature.mypage

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.CHANGE_NICKNAME
import com.android.mediproject.core.common.CHANGE_PASSWORD
import com.android.mediproject.core.common.LOGOUT
import com.android.mediproject.core.common.WITHDRAWAL
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetFragment
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel>(FragmentMyPageBinding::inflate) {

    @Inject
    lateinit var systemBarStyler: SystemBarStyler
    override val fragmentViewModel: MyPageViewModel by viewModels()
    private val myCommentListAdapter: MyPageMyCommentAdapter by lazy { MyPageMyCommentAdapter() }
    private var myPageMoreBottomSheet: MyPageMoreBottomSheetFragment? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(
            SystemBarStyler.StatusBarColor.BLACK,
            SystemBarStyler.NavigationBarColor.BLACK,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarStyle()
        setRecyclerView()
        setFragmentResultListner()
        setBinding()
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    mypageBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }

    private fun setRecyclerView() = binding.myCommentsListRV.apply {
        adapter = myCommentListAdapter
        layoutManager = LinearLayoutManager(requireActivity())
        addItemDecoration(MyPageMyCommentDecoraion(requireContext()))
    }

    private fun setFragmentResultListner() {
        setBottomsheetFragmentResultListner()
        setDialogFragmentResultListner()
    }

    private fun setBottomsheetFragmentResultListner() {
        parentFragmentManager.setFragmentResultListener(
            MyPageMoreBottomSheetFragment.TAG,
            viewLifecycleOwner,
        ) { _, bundle ->
            val flag = bundle.getInt(MyPageMoreBottomSheetFragment.TAG)
            handleBottomSheetFlag(flag)
            myPageMoreBottomSheet!!.dismiss()
            myPageMoreBottomSheet = null
        }
    }

    private fun setDialogFragmentResultListner() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            MyPageMoreDialogFragment.TAG,
            viewLifecycleOwner,
        ) { _, bundle ->
            val flag = bundle.getInt(MyPageMoreDialogFragment.TAG)
            handleDialogCallback(flag)
        }
    }

    private fun setBinding() =
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { token.collect { handleToken(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { user.collect { userDto = it } }
                    repeatOnStarted { loginMode.collect { handleLoginMode(it) } }
                    repeatOnStarted {
                        myCommentsList.collect { commentList ->
                            if (checkCommentListSize(commentList)) showCommentList(commentList)
                            else noShowCommentList()
                        }
                    }
                }
                loadTokens()
            }

            myCommentsListHeaderView.setOnMoreClickListener {
                navigateWithUri("medilens://main/comments_nav/myCommentsListFragment")
            }
        }

    private fun checkCommentListSize(commentList: List<MyCommentDto>): Boolean {
        return (commentList.size != 0)
    }

    private fun handleToken(tokenState: TokenState<CurrentTokenDto>) {
        log(tokenState.toString())
        when (tokenState) {
            is TokenState.Empty -> setLoginMode(MyPageViewModel.LoginMode.GUEST_MODE)
            is TokenState.AccessExpiration -> {}
            is TokenState.Valid -> setLoginMode(MyPageViewModel.LoginMode.LOGIN_MODE)
            else -> {}
        }
    }

    private fun setLoginMode(loginMode: MyPageViewModel.LoginMode) {
        fragmentViewModel.setLoginMode(loginMode)
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.Login -> navigateWithUri("medilens://main/intro_nav/login")
        is MyPageViewModel.MyPageEvent.SignUp -> navigateWithUri("medilens://main/intro_nav/signUp")
        is MyPageViewModel.MyPageEvent.MyPageMore -> showMyPageBottomSheet()
    }

    private fun showMyPageBottomSheet() {
        if (isMyPageMoreBottomSheetNull()) {
            showMyPageMoreBottomSheet()
        }
    }

    private fun isMyPageMoreBottomSheetNull(): Boolean {
        return myPageMoreBottomSheet == null
    }

    private fun showMyPageMoreBottomSheet() {
        myPageMoreBottomSheet = MyPageMoreBottomSheetFragment {
            myPageMoreBottomSheet = null
        }

        myPageMoreBottomSheet!!.show(
            parentFragmentManager,
            MyPageMoreBottomSheetFragment.TAG,
        )
    }

    private fun handleLoginMode(loginMode: MyPageViewModel.LoginMode) {
        when (loginMode) {
            MyPageViewModel.LoginMode.GUEST_MODE -> guestModeScreen()
            MyPageViewModel.LoginMode.LOGIN_MODE -> loginModeScreen()
        }
    }

    private fun guestModeScreen() = binding.apply {
        guestTV.text = setGuestModeScreenSpan()
        setGuestModeScreenVisible()
    }

    private fun setGuestModeScreenSpan(): SpannableStringBuilder {
        val span =
            SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.guestDescription)).apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.main,
                        ),
                    ),
                    15, 18,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
                setSpan(UnderlineSpan(), 15, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        return span
    }

    private fun setGuestModeScreenVisible() = binding.apply {
        guestModeCL.visibility = View.VISIBLE
        loginModeCL.visibility = View.GONE
    }

    private fun loginModeScreen() = fragmentViewModel.apply {
        loadUser()
        loadComments()
    }

    private fun handleBottomSheetFlag(bottomSheetFlag: Int) {
        when (bottomSheetFlag) {
            MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_NICKNAME.value -> showMyPageMoreDialog(MyPageMoreDialogFragment.DialogFlag.ChangeNickName)
            MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_PASSWORD.value -> showMyPageMoreDialog(MyPageMoreDialogFragment.DialogFlag.ChangePassword)
            MyPageMoreBottomSheetFragment.BottomSheetFlag.WITHDRAWAL.value -> showMyPageMoreDialog(MyPageMoreDialogFragment.DialogFlag.Withdrawal)
            MyPageMoreBottomSheetFragment.BottomSheetFlag.LOGOUT.value -> showMyPageMoreDialog(MyPageMoreDialogFragment.DialogFlag.Logout)
        }
    }

    private fun showMyPageMoreDialog(dialogFlag: MyPageMoreDialogFragment.DialogFlag) {
        MyPageMoreDialogFragment(dialogFlag).show(
            requireActivity().supportFragmentManager,
            MyPageMoreDialogFragment.TAG,
        )
    }

    private fun handleDialogCallback(dialogFlag: Int) {
        when (dialogFlag) {
            MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_NICKNAME -> changeNicknameCallback()
            MyPageMoreBottomSheetFragment.BottomSheetFlag.CHANGE_PASSWORD -> changePasswordCallback()
            MyPageMoreBottomSheetFragment.BottomSheetFlag.WITHDRAWAL -> withdrawalCallback()
            MyPageMoreBottomSheetFragment.BottomSheetFlag.LOGOUT -> logoutCallback()
        }
    }

    private fun changeNicknameCallback() {
        log("MyPageDialog Callback : changeNickname() ")
        fragmentViewModel.loadUser()
    }

    private fun changePasswordCallback() {
        log("MyPageDialog Callback : changePassword() ")
    }

    private fun withdrawalCallback() {
        log("MyPageDialog Callback : withdrawal() ")
        fragmentViewModel.apply {
            signOut()
            setLoginMode(MyPageViewModel.LoginMode.GUEST_MODE)
        }
    }

    private fun logoutCallback() {
        log("MyPageDialog Callback : logout() ")
        fragmentViewModel.apply {
            signOut()
            setLoginMode(MyPageViewModel.LoginMode.GUEST_MODE)
        }
    }

    private fun noShowCommentList() = binding.apply {
        noMyCommentTV.text = setNoShowCommentListSpan()
        setNoShowCommentListVisible()
    }

    private fun setNoShowCommentListSpan(): SpannableStringBuilder {
        val span =
            SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.noMyComment)).apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.main,
                        ),
                    ),
                    7, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE,
                )
                setSpan(
                    UnderlineSpan(),
                    7,
                    9,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE,
                )
            }
        return span
    }

    private fun setNoShowCommentListVisible() = binding.apply {
        myCommentsListRV.visibility = View.GONE
        noMyCommentTV.visibility = View.VISIBLE
        myCommentsListHeaderView.setMoreVisiblity(false)
        myCommentsListHeaderView.setExpandVisiblity(false)
    }

    private fun showCommentList(myCommentList: List<MyCommentDto>) = binding.apply {
        setShowCommentListVisible()
        myCommentListAdapter.submitList(myCommentList)
    }

    private fun setShowCommentListVisible() = binding.apply {
        guestModeCL.visibility = View.GONE
        loginModeCL.visibility = View.VISIBLE
    }
}
