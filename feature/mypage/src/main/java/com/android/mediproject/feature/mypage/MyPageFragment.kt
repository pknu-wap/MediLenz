package com.android.mediproject.feature.mypage

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
import com.android.mediproject.core.common.GUEST_MODE
import com.android.mediproject.core.common.LOGIN_MODE
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetFragment
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_NICKNAME
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_PASSWORD
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.WITHDRAWAL
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel>(FragmentMyPageBinding::inflate) {

    override val fragmentViewModel: MyPageViewModel by viewModels()
    private val myCommentListAdapter: MyPageMyCommentAdapter by lazy { MyPageMyCommentAdapter() }
    private var loginMode = GUEST_MODE
    var myCommentList: List<MyCommentDto> = listOf()
    private var myPageMoreBottomSheet: MyPageMoreBottomSheetFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { token.collect { handleToken(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { user.collect { userDto = it } }
                    repeatOnStarted { myCommentsList.collect { myCommentList = it } }
                }
                loadTokens()
            }

            myCommentsListHeaderView.setOnMoreClickListener {
                findNavController().navigate("medilens://main/comments_nav/myCommentsListFragment".toUri())
            }
        }

        parentFragmentManager.setFragmentResultListener(
            MyPageMoreBottomSheetFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val flag = bundle.getInt(MyPageMoreBottomSheetFragment.TAG)
            handleFlag(flag)
            myPageMoreBottomSheet!!.dismiss()
            myPageMoreBottomSheet = null
        }
    }

    private fun handleFlag(flag: Int) {
        when (flag) {
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

            else -> Unit
        }
    }

    private fun setRecyclerView() = binding.myCommentsListRV.apply {
        adapter = myCommentListAdapter
        layoutManager = LinearLayoutManager(requireActivity())
        addItemDecoration(MyPageMyCommentDecoraion(requireContext()))
    }

    private fun handleToken(tokenState: TokenState<CurrentTokenDto>) {
        when (tokenState) {
            is TokenState.Empty -> {
                //for test
                loginMode = LOGIN_MODE
                //loginMode = GUEST_MODE
                setMyCommentsList()
            }

            is TokenState.AccessExpiration -> {}
            is TokenState.Valid -> {
                loginMode = LOGIN_MODE
                setMyCommentsList()
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

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.Login -> findNavController().navigate("medilens://main/intro_nav/login".toUri())
        is MyPageViewModel.MyPageEvent.SignUp -> findNavController().navigate("medilens://main/intro_nav/signUp".toUri())
        is MyPageViewModel.MyPageEvent.MyPageMore -> showMyPageBottomSheet()
    }

    private fun setMyCommentsList() {
        when (loginMode) {
            LOGIN_MODE -> loginMyCommentList()
            GUEST_MODE -> guestMyCommentList()
        }
    }

    //로그인 상태일 시 보여주는 화면
    private fun loginMyCommentList() = binding.apply {
        guestModeCL.visibility = View.GONE
        loginModeCL.visibility = View.VISIBLE

        fragmentViewModel.loadUser()
        fragmentViewModel.loadComments()

        //만약 사이즈가 1개 이상일 경우 RecyclerView로 데이터를 뛰운다.
        if (myCommentList.size != 0) myCommentListAdapter.submitList(myCommentList)

        //없을 경우 텍스트를 보여줌
        else {
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
    }

    //비로그인 상태일 시 보여주는 화면
    private fun guestMyCommentList() = binding.apply {
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