package com.android.mediproject.core.network.parser

import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import javax.inject.Inject

class HtmlParser @Inject constructor() {

    suspend fun parse(src: String) =
        WeakReference(Jsoup.parse(src)).get()?.let { document ->
            // 구글 이미지 검색 결과를 보여주는 테이블
            val imageTables = document.getElementsByClass("GpQGbf")
            // 테이블의 행 목록
            val trs = imageTables.select("tr")
            // 행의 첫번째 아이템
            val img = trs.select("td").first()?.getElementsByClass("yWs4tf")
            // 아이템의 첫번째 이미지
            img?.attr("src") ?: ""
        } ?: ""

}
