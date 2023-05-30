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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.GUEST_MODE
import com.android.mediproject.core.common.LOGIN_MODE
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.user.UserDto
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import repeatOnStarted

@AndroidEntryPoint
class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel>(FragmentMyPageBinding::inflate) {

    override val fragmentViewModel: MyPageViewModel by viewModels()
    private val myCommentListAdapter: MyPageMyCommentAdapter by lazy { MyPageMyCommentAdapter() }
    private var loginMode = GUEST_MODE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { token.collect { handleToken(it) } }
                }
                loadTokens()
            }

            myCommentsListHeaderView.setOnMoreClickListener {
                findNavController().navigate("medilens://main/comments_nav/myCommentsListFragment".toUri())
            }
        }
    }

    private fun setRecyclerView() = binding.apply {
        myCommentsListRV.apply {
            adapter = myCommentListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(MyPageMyCommentDecoraion(requireContext()))
        }
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.Login -> findNavController().navigate("medilens://main/intro_nav/login".toUri())
        is MyPageViewModel.MyPageEvent.SignUp -> findNavController().navigate("medilens://main/intro_nav/signUp".toUri())
    }

    private fun handleToken(tokenState: TokenState<CurrentTokenDto>) {
        when (tokenState) {
            is TokenState.Empty -> {}
            is TokenState.Error -> {}
            is TokenState.Expiration -> loginMode = GUEST_MODE
            is TokenState.Valid -> loginMode = LOGIN_MODE
        }

        repeatOnStarted {
            fragmentViewModel.apply {
                viewLifecycleOwner.repeatOnStarted {
                    myCommentsList.collect { myCommentList -> setMyCommentsList(myCommentList) }
                }
            }
        }
    }

    private fun setMyCommentsList(myCommentList: List<MyCommentDto>) {
        //로그인 상태일 경우
        if (loginMode == LOGIN_MODE) {
            binding.apply {

                guestModeCL.visibility = View.GONE
                loginModeCL.visibility = View.VISIBLE

                userDto = UserDto("WAP짱짱")

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
                            setSpan(UnderlineSpan(), 7, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        }
                    noMyCommentTV.text = span
                    myCommentsListHeaderView.setMoreVisiblity(false)
                    myCommentsListHeaderView.setExpandVisiblity(false)
                }
            }
            //로그인 상태가 아닐 경우
        } else {
            binding.apply {
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
                            ),
                            15,
                            18,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            UnderlineSpan(), 15, 18,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                guestTV.text = span
            }
        }
    }
}