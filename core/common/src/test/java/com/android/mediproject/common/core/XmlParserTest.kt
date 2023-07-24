package com.android.mediproject.common.core

import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.core.model.util.parseXmlString
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class XmlParserTest {


    @Test
    fun `공공데이터 포털 XML값 파싱 테스트`() {
        val eeDoc = eeDocData.parseXmlString()
        val ubDoc = ubDocData.parseXmlString()
        val ndDoc = nbDocData.parseXmlString()

        eeDoc.show()
        ubDoc.show()
        ndDoc.show()
    }

    private fun XMLParsedResult.show() = run {
        println("title: $title")
        articleList.forEach { article ->
            println("article title: ${article.title}")
            article.contentList.forEach { content ->
                println("$content")
            }
        }

        println()
    }
}
