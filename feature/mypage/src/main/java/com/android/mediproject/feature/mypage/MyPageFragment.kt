package com.android.mediproject.feature.mypage

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
            SystemBarStyler.NavigationBarColor.BLACK
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setBarStyle()
        setFragmentResultListner()

        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { token.collect { handleToken(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { user.collect { userDto = it } }
                    repeatOnStarted { loginMode.collect { handleLoginMode(it) } }
                    repeatOnStarted {
                        myCommentsList.collect { commentList ->
                            if (commentList.size != 0) showCommentList(commentList)
                            else showNoCommentList()
                        }
                    }
                }
                loadTokens()
            }

            myCommentsListHeaderView.setOnMoreClickListener {
                findNavController().navigate("medilens://main/comments_nav/myCommentsListFragment".toUri())
            }
        }
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    mypageBar,
                    SystemBarStyler.SpacingType.PADDING
                )
            )
        )
    }

    private fun setRecyclerView() = binding.myCommentsListRV.apply {
        adapter = myCommentListAdapter
        layoutManager = LinearLayoutManager(requireActivity())
        addItemDecoration(MyPageMyCommentDecoraion(requireContext()))
    }

    private fun setFragmentResultListner() {
        parentFragmentManager.setFragmentResultListener(
            MyPageMoreBottomSheetFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val flag = bundle.getInt(MyPageMoreBottomSheetFragment.TAG)
            handleBottomSheetFlag(flag)
            myPageMoreBottomSheet!!.dismiss()
            myPageMoreBottomSheet = null
        }

        //다이얼로그
        requireActivity().supportFragmentManager.setFragmentResultListener(
            MyPageMoreDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val flag = bundle.getInt(MyPageMoreDialogFragment.TAG)
            handleDialogFlag(flag)
        }
    }

    //가장 처음 토큰 값을 식별하는 함수입니다.
    private fun handleToken(tokenState: TokenState<CurrentTokenDto>) {
        log(tokenState.toString())

        when (tokenState) {
            is TokenState.Empty -> fragmentViewModel.setLoginMode(MyPageViewModel.LoginMode.GUEST_MODE)
            is TokenState.AccessExpiration -> {}
            is TokenState.Valid -> fragmentViewModel.setLoginMode(MyPageViewModel.LoginMode.LOGIN_MODE)
            else -> {}
        }
    }

    private fun handleLoginMode(loginMode: MyPageViewModel.LoginMode) {
        when (loginMode) {
            MyPageViewModel.LoginMode.GUEST_MODE -> guestModeScreen()
            MyPageViewModel.LoginMode.LOGIN_MODE -> {
                log("MyPageFragment : 로그인 모드")
                fragmentViewModel.apply {
                    loadUser()
                    loadComments()
                }
            }
        }
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.Login -> findNavController().navigate("medilens://main/intro_nav/login".toUri())
        is MyPageViewModel.MyPageEvent.SignUp -> findNavController().navigate("medilens://main/intro_nav/signUp".toUri())
        is MyPageViewModel.MyPageEvent.MyPageMore -> showMyPageBottomSheet()
    }

    //바텀시트로 돌아왔을 때 실행되는 함수입니다.
    private fun handleBottomSheetFlag(bottomSheetFlag: Int) {
        when (bottomSheetFlag) {
            CHANGE_NICKNAME -> MyPageMoreDialogFragment(MyPageMoreDialogFragment.DialogFlag.ChangeNickName).show(
                requireActivity().supportFragmentManager,
                MyPageMoreDialogFragment.TAG
            )

            CHANGE_PASSWORD -> MyPageMoreDialogFragment(MyPageMoreDialogFragment.DialogFlag.ChangePassword).show(
                requireActivity().supportFragmentManager,
                MyPageMoreDialogFragment.TAG
            )

            WITHDRAWAL -> MyPageMoreDialogFragment(MyPageMoreDialogFragment.DialogFlag.Withdrawal).show(
                requireActivity().supportFragmentManager,
                MyPageMoreDialogFragment.TAG
            )

            LOGOUT -> MyPageMoreDialogFragment(MyPageMoreDialogFragment.DialogFlag.Logout).show(
                requireActivity().supportFragmentManager,
                MyPageMoreDialogFragment.TAG
            )
        }
    }

    //다이얼로그로 돌아왔을 때 실행되는 함수입니다.
    private fun handleDialogFlag(dialogFlag: Int) {
        when (dialogFlag) {
            CHANGE_NICKNAME -> {
                log("MyPageDialog Callback : changeNickname() ")
                fragmentViewModel.loadUser()
            }

            CHANGE_PASSWORD -> {
                log("MyPageDialog Callback : changePassword() ")
            }

            WITHDRAWAL -> {
                log("MyPageDialog Callback : withdrawal() ")
                fragmentViewModel.apply {
                    signOut()
                    setLoginMode(MyPageViewModel.LoginMode.GUEST_MODE)
                }
            }

            LOGOUT -> {

            }

        }
    }

    private fun showMyPageBottomSheet() {
        if (myPageMoreBottomSheet == null) {
            myPageMoreBottomSheet = MyPageMoreBottomSheetFragment {
                myPageMoreBottomSheet = null
            }
            myPageMoreBottomSheet!!.show(
                parentFragmentManager,
                MyPageMoreBottomSheetFragment.TAG
            )
        }
    }

    //로그인 상태일 시 보여주는 화면 (댓글 없을 시)
    private fun showNoCommentList() = binding.apply {
        myCommentsListRV.visibility = View.GONE
        noMyCommentTV.visibility = View.VISIBLE

        val span =
            SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.noMyComment)).apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.main
                        )
                    ), 7, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                setSpan(
                    UnderlineSpan(),
                    7,
                    9,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        noMyCommentTV.text = span
        myCommentsListHeaderView.setMoreVisiblity(false)
        myCommentsListHeaderView.setExpandVisiblity(false)
    }

    //로그인 상태일 시 보여주는 화면 (댓글 있을 시)
    private fun showCommentList(myCommentList: List<MyCommentDto>) = binding.apply {
        guestModeCL.visibility = View.GONE
        loginModeCL.visibility = View.VISIBLE
        myCommentListAdapter.submitList(myCommentList)
    }

    //비로그인 상태일 시 보여주는 화면
    private fun guestModeScreen() = binding.apply {
        guestModeCL.visibility = View.VISIBLE
        loginModeCL.visibility = View.GONE

        //글자 Span 적용
        val span =
            SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.guestDescription)).apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.main
                        )
                    ), 15, 18,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(UnderlineSpan(), 15, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        guestTV.text = span
    }
}