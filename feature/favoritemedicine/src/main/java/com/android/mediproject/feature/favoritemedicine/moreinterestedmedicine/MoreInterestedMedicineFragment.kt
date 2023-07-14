package com.android.mediproject.feature.favoritemedicine.moreinterestedmedicine

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.interestedmedicine.MoreInterestedMedicineDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.favoritemedicine.databinding.FragmentMoreInterestedMedicineBinding
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class MoreInterestedMedicineFragment :
    BaseFragment<FragmentMoreInterestedMedicineBinding, MoreInterestedMedicineViewModel>(
        FragmentMoreInterestedMedicineBinding::inflate
    ) {
    override val fragmentViewModel: MoreInterestedMedicineViewModel by viewModels()
    private val moreInterestedMedicineAdapter: MoreInterestedMeidicneAdapter by lazy { MoreInterestedMeidicneAdapter() }

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(
            SystemBarStyler.StatusBarColor.BLACK,
            SystemBarStyler.NavigationBarColor.BLACK
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBarStyle()
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { interstedMedicineList.collect { setInterstedMedicineList(it) } }
                    repeatOnStarted { token.collect { handleToken(it) } }
                    loadTokens()
                }
            }

            interestedMedicineListRV.apply {
                adapter = moreInterestedMedicineAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(MoreInterestedMedcineDecoration(requireContext()))
                addItemDecoration(DividerItemDecoration(requireContext(), 1))
            }
        }
    }

    private fun handleToken(token: TokenState<CurrentTokenDto>) {
        when (token) {
            is TokenState.Empty -> {}
            is TokenState.Error -> {}
            is TokenState.RefreshExpiration -> {}
            is TokenState.AccessExpiration -> {}
            is TokenState.Valid -> {
                fragmentViewModel.loadInterestedMedicines()
            }
        }
    }

    private fun setInterstedMedicineList(medicineList: List<MoreInterestedMedicineDto>) {
        moreInterestedMedicineAdapter.submitList(medicineList)
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    interestedMedicineListBar,
                    SystemBarStyler.SpacingType.PADDING
                )
            )
        )
    }

}