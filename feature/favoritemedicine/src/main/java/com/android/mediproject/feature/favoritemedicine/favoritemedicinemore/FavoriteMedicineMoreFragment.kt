package com.android.mediproject.feature.favoritemedicine.favoritemedicinemore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.model.favoritemedicine.FavoriteMedicineMoreInfo
import com.android.mediproject.core.model.token.CurrentTokens
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.favoritemedicine.databinding.FragmentFavoriteMedicineMoreBinding
import com.android.mediproject.feature.favoritemedicine.favoritemedicinemore.recyclerview.FavoriteMedcineMoreDecoration
import dagger.hilt.android.AndroidEntryPoint
import com.android.mediproject.core.common.viewmodel.repeatOnStarted
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
    }

    private fun setBinding() = binding.apply {
        viewModel = fragmentViewModel.apply {
            viewLifecycleOwner.apply {
                repeatOnStarted { favoriteMedicineList.collect { setFavoriteMedicineMoreList(it) } }
                repeatOnStarted { token.collect { handleToken(it) } }
                loadTokens()
            }
        }
        setBarStyle()
        setRecyclerView()
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

    private fun setRecyclerView() = binding.favoriteMedicineListRV.apply {
        adapter = favoriteMedicineMoreAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(FavoriteMedcineMoreDecoration(requireContext()))
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setFavoriteMedicineMoreList(medicineList: List<FavoriteMedicineMoreInfo>) {
        favoriteMedicineMoreAdapter.submitList(medicineList)
    }

    private fun handleToken(token: TokenState<CurrentTokens>) {
        when (token) {
            is TokenState.Empty -> {}
            is TokenState.Error -> {}
            is TokenState.Tokens.RefreshExpiration -> {}
            is TokenState.Tokens.AccessExpiration -> {}
            is TokenState.Tokens.Valid -> loadFavoriteMedicines()
        }
    }

    private fun loadFavoriteMedicines() {
        fragmentViewModel.loadFavoriteMedicines()
    }
}
