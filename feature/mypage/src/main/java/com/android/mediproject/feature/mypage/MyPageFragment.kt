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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.model.user.UserDto
import com.android.mediproject.core.ui.R
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageBinding
import repeatOnStarted

class MyPageFragment :
    BaseFragment<FragmentMyPageBinding, MyPageViewModel>(FragmentMyPageBinding::inflate) {
    override val fragmentViewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myCommentListAdapter = MyPageMyCommentAdapter()
        binding.apply {

            myCommentsListRV.apply{
                adapter = myCommentListAdapter
                layoutManager = LinearLayoutManager(requireActivity())
                addItemDecoration(MyPageMyCommentDecoraion(requireContext()))
            }

            userDto = UserDto("WAP짱짱")

            //글자 Span 적용
            val span = SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.guestDescription)).apply {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.main)),
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

            viewModel = fragmentViewModel.apply {
                    viewLifecycleOwner.apply{
                        repeatOnStarted { eventFlow.collect { handleEvent(it) } }

                        repeatOnStarted { myCommentsList.collect{myCommentList ->

                            //만약 사이즈가 1개 이상일 경우 RecyclerView로 데이터를 뛰운다.
                            if(myCommentList.size != 0) myCommentListAdapter.submitList(myCommentList)

                            //없을 경우 텍스트를 보여줌
                            else{
                                myCommentsListRV.visibility = View.GONE
                                noMyCommentTV.visibility = View.VISIBLE

                                val span = SpannableStringBuilder(getString(com.android.mediproject.feature.mypage.R.string.noMyComment)).apply{
                                    setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(),R.color.main)),0,4,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                    setSpan(UnderlineSpan(),0,4,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                    setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(),R.color.main)),6,8,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                    setSpan(UnderlineSpan(),6,8,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                }
                                noMyCommentTV.text = span
                                myCommentsListHeaderView.setMoreVisiblity(false)
                                myCommentsListHeaderView.setExpandVisiblity(false)
                            }
                        } }
                    }
            }

            myCommentsListHeaderView.setOnMoreClickListener{
                findNavController().navigate("medilens://main/comments_nav/myCommentsListFragment".toUri())
            }
        }
    }

    private fun handleEvent(event: MyPageViewModel.MyPageEvent) = when (event) {
        is MyPageViewModel.MyPageEvent.Login -> findNavController().navigate("medilens://main/intro_nav/login".toUri())
        is MyPageViewModel.MyPageEvent.SignUp -> findNavController().navigate("medilens://main/intro_nav/signUp".toUri())
    }
}