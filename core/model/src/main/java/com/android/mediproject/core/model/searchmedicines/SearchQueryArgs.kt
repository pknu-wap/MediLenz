package com.android.mediproject.core.model.searchmedicines

import com.android.mediproject.core.model.navargs.BaseNavArgs

/**
 * 검색어를 전달할 인자
 *
 * @property words 검색어
 */
data class SearchQueryArgs(
    val words: String = "",
) : BaseNavArgs(SearchQueryArgs::class.java.name)
