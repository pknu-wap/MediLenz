package com.android.mediproject.core.model.util


import android.text.Html
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.lang.ref.WeakReference
import javax.xml.parsers.DocumentBuilderFactory


private val factory = DocumentBuilderFactory.newInstance()


/**
 * XML 파싱
 *
 * @return XML 파싱 결과
 *
 */
fun String.parseXmlString(): XMLParsedResult {
    if (isEmpty()) return XMLParsedResult()

    val doc = WeakReference(factory.newDocumentBuilder().parse(InputSource(StringReader(this)))).get()!!
    doc.documentElement.normalize()

    return doc.getElementsByTagName("DOC").let { docs ->
        val docElement = docs.item(0) as Element
        val title = docElement.getAttribute("title")

        getElementsByTagName(docElement, "SECTION").first().let { section ->
            getElementsByTagName(section, "ARTICLE").map { article ->
                val articleTitle = article.getAttribute("title")
                getElementsByTagName(article, "PARAGRAPH").map { paragraph ->
                    Html.fromHtml(paragraph.textContent.trimIndent(), Html.FROM_HTML_MODE_COMPACT).toString()
                }.let { paragraphContents ->
                    XMLParsedResult.Article(
                        title = articleTitle,
                        contentList = paragraphContents,
                    )
                }
            }
        }.let { articleList ->
            XMLParsedResult(
                title = title,
                articleList = articleList,
            )
        }
    }
}


private fun getElementsByTagName(element: Element, tagName: String): List<Element> = element.getElementsByTagName(tagName).let { elements ->
    (0 until elements.length).filter { elements.item(it).nodeType == Node.ELEMENT_NODE }.map { elementIdx ->
        elements.item(elementIdx) as Element
    }
}


data class XMLParsedResult(
    val title: String = "",
    val articleList: List<Article> = emptyList(),
) {


    fun isEmpty(): Boolean = articleList.isEmpty()

    data class Article(
        val title: String,
        val contentList: List<String>,
    )

}
