package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineMoreDto
import com.android.mediproject.core.model.remote.token.CurrentTokenDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.favoritemedicine.databinding.FragmentFavoriteMedicineMoreBinding
import com.android.mediproject.feature.favoritemedicine.favoritemedicinemore.recyclerview.FavoriteMedcineMoreDecoration
import dagger.hilt.android.AndroidEntryPoint
import repeatOnStarted
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteMedicineMoreFragment :
    BaseFragment<FragmentFavoriteMedicineMoreBinding, FavoriteMedicineMoreViewModel>(
        FragmentFavoriteMedicineMoreBinding::inflate,
    ) {
    override val fragmentViewModel: FavoriteMedicineMoreViewModel by viewModels()
    private val favoriteMedicineMoreAdapter: FavoriteMeidicneMoreAdapter by lazy { FavoriteMeidicneMoreAdapter() }

    @Inject
    lateinit var systemBarStyler: SystemBarStyler

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
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { favoriteMedicineList.collect { setFavoriteMedicineMoreList(it) } }
                    repeatOnStarted { token.collect { handleToken(it) } }
                    loadTokens()
                }
            }

            favoriteMedicineListRV.apply {
                adapter = favoriteMedicineMoreAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(FavoriteMedcineMoreDecoration(requireContext()))
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
                fragmentViewModel.loadFavoriteMedicines()
            }
        }
    }

    private fun setFavoriteMedicineMoreList(medicineList: List<FavoriteMedicineMoreDto>) {
        favoriteMedicineMoreAdapter.submitList(medicineList)
    }

    private fun setBarStyle() = binding.apply {
        systemBarStyler.changeMode(
            topViews = listOf(
                SystemBarStyler.ChangeView(
                    favoriteMedicineListBar,
                    SystemBarStyler.SpacingType.PADDING,
                ),
            ),
        )
    }

}
