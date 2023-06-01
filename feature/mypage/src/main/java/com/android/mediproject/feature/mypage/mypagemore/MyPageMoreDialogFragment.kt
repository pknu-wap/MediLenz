package com.android.mediproject.feature.mypage.mypagemore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MyPageMoreDialogFragment(private val flag : DialogFlag) : DialogFragment() {

    sealed class DialogFlag{
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
        _binding = FragmentMyPageMoreDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply{
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                    repeatOnStarted { dialogFlag.collect { handleFlag(it) } }
                }
                setDialogFlag(flag)
            }
        }
    }

    private fun handleEvent(event: MyPageMoreDialogViewModel.MyPageMoreDialogEvent) = when (event) {
        else -> {}
    }

    private fun handleFlag(dialogFlag : DialogFlag) = when(dialogFlag){
        is DialogFlag.ChangeNickName -> {}
        is DialogFlag.ChangePassword -> {}
        is DialogFlag.Withdrawal -> {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}