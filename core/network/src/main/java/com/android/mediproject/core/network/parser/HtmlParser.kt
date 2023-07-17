package com.android.mediproject.core.network.parser

import android.util.Log
import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import javax.inject.Inject

class HtmlParser @Inject constructor() {

    private val minSimilarity = 0.13

    suspend fun parse(query: String, src: String) = WeakReference(Jsoup.parse(src)).get()?.let { document ->
        // 테이블의 행 목록
        val rows = document.getElementsByClass("GpQGbf").select("tr")
        // 검색어와 유사한 웹 페이지 제목을 찾는다.
        val mostSimilarItem = rows.select("td").find { td ->
            td.getElementsByClass("fYyStc").find {
                // 유사도 검사
                query.similarity(it.text()) >= minSimilarity
            } != null
        }
        // 찾은 웹 페이지의 이미지 URL을 반환한다.
        mostSimilarItem?.getElementsByClass("yWs4tf")?.first()?.attr("src") ?: ""
    } ?: ""

    private fun String.levenshtein(b: String): Int {
        val distances = WeakReference(Array(length + 1) { IntArray(b.length + 1) }).get()!!

        for (i in 0..length) {
            distances[i][0] = i
        }

        for (j in 0..b.length) {
            distances[0][j] = j
        }

        for (i in 1..length) {
            for (j in 1..b.length) {
                val cost = if (this[i - 1] == b[j - 1]) 0 else 1

                // 편집 거리 계산
                // 첫 번째 문자열에서 문자를 삭제 or 두 번째 문자열에 문자를 삽입 or
                // 첫 번째 문자열의 문자를 두 번째 문자열의 해당 문자로 교체
                // distances[i][j]는 이 세 가지 값 중 가장 작은 값
                distances[i][j] = minOf(distances[i - 1][j] + 1, distances[i][j - 1] + 1, distances[i - 1][j - 1] + cost)
            }
        }

        return distances[length][b.length]
    }

    private fun String.similarity(comp: String): Double {
        val distance = levenshtein(comp)
        val maxLen = maxOf(length, comp.length)
        val similarity = (maxLen - distance).toDouble() / maxLen
        if (similarity >= minSimilarity)
            Log.d("wap", "$this - $comp, 유사도 : $similarity")

        return similarity
    }

}
