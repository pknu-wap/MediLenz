package com.android.mediproject.core.model.searchmedicines

import com.android.mediproject.core.model.navargs.BaseNavArgs

/**
 * 검색어를 전달할 인자
 *
 * @property query 검색어
 */
data class SearchQueryArgs(
    val query: String = "",
) : BaseNavArgs(SearchQueryArgs::class.java.name)
