package com.android.mediproject.feature.mypage.mypagemore

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreBottomSheetBinding
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_NICKNAME
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_PASSWORD
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.WITHDRAWAL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted

@AndroidEntryPoint
class MyPageMoreBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "MyPageMoreBottomSheetFragment"
    }

    private var _binding: FragmentMyPageMoreBottomSheetBinding? = null
    val binding get() = _binding!!
    private val fragmentViewModel: MyPageMoreBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageMoreBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { bottomsheetFlag.collect { handleFlag(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                }
            }
        }
    }

    private fun handleEvent(event: MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent) =
        when (event) {
            is MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent.Confirm -> {
                when (event.flag) {
                    CHANGE_NICKNAME -> {}
                    CHANGE_PASSWORD -> {}
                    WITHDRAWAL -> {}
                    else -> Unit
                }
            }
        }

    private fun handleFlag(flag: Int) {
        when (flag) {
            CHANGE_NICKNAME -> {
                binding.apply {
                    changeNickNameTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.main
                            )
                        )
                        setTypeface(null, Typeface.BOLD)
                    }
                    changePasswordTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                    withdrawalTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                }
            }

            CHANGE_PASSWORD -> {
                binding.apply {
                    changeNickNameTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                    changePasswordTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.main
                            )
                        )
                        setTypeface(null, Typeface.BOLD)
                    }
                    withdrawalTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                }
            }

            WITHDRAWAL -> {
                binding.apply {
                    changeNickNameTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                    changePasswordTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.gray3
                            )
                        )
                        setTypeface(null, Typeface.NORMAL)
                    }
                    withdrawalTV.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                com.android.mediproject.core.ui.R.color.main
                            )
                        )
                        setTypeface(null, Typeface.BOLD)
                    }
                }
            }

            else -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}