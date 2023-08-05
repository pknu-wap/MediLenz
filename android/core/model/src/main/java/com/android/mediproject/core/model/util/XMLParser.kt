package com.android.mediproject.core.model.util


import android.text.Html
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory


private val factory by lazy { DocumentBuilderFactory.newInstance() }

/**
 * XML 파싱
 *
 * @param xml XML 데이터
 * @return XML 파싱 결과
 *
 */
fun String.parseXmlString(): XMLParsedResult {
    val dBuilder: DocumentBuilder = factory.newDocumentBuilder()
    val xmlInput = InputSource(StringReader(this))
    val doc = dBuilder.parse(xmlInput)
    doc.documentElement.normalize()

    return doc.getElementsByTagName("DOC").let { docs ->
        val docElement = docs.item(0) as Element
        val title = docElement.getAttribute("title")

        getElementsByTagName(docElement, "SECTION").first().let { section ->
            getElementsByTagName(section, "ARTICLE").map { article ->
                val articleTitle = article.getAttribute("title")
                getElementsByTagName(article, "PARAGRAPH").map { paragraph ->
                    Html.fromHtml(paragraph.textContent.trim(), Html.FROM_HTML_MODE_COMPACT).toString()
                }.toList().let { paragraphContents ->
                    XMLParsedResult.Article(
                        title = articleTitle,
                        contentList = paragraphContents,
                    )
                }
            }
        }.toList().let { articleList ->
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
    }.toList()
}


data class XMLParsedResult(
    val title: String,
    val articleList: List<Article>,
) {
    data class Article(
        val title: String,
        val contentList: List<String>,
    )

}

/**
 *                 <ARTICLE title="2. 다음 환자에는 투여하지 말 것">
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[1) 이 약의 주성분 또는 다른 성분에 과민반응이 있는 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[2) 조절되지 않는 고혈압 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[3) 발작 장애 또는 발작 병력이 있는 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[4) 중추신경계 종양이 있는 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[5) 알코올 또는 벤조디아제핀계, 바르비탈류, 항간질약 등 약물복용을 갑자기 중단한 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[6) 양극성 장애 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[7) 부프로피온 또는 날트렉손을 함유하고 있는 다른 약을 투여 받고 있는 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[8) 대식증 또는 신경성 식욕부진을 현재 또는 과거에 진단 받은 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[9) 현재 아편성 또는 아편효능약(예, 메사돈) 의존성이 있는 환자 또는 급성 아편 금단증상을 지닌 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[10) MAO 억제제를 투여중인 환자 (MAO 억제제 투여중지 후 최소 14일이 경과한 후 이 약을 투여할 수 있다.)]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[11) 급성 간염·간부전환자, 중증의 간장애 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[12) 말기 신장질환 환자]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[13) 임부, 임신하고 있을 가능성이 있는 여성 또는 수유부]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[14) 이 약은 유당을 함유하고 있으므로, 갈락토오스 불내성(galactose intolerance), Lapp 유당분해효소 결핍증(Lapp lactase deficiency) 또는 포도당-갈락토오스 흡수장애(glucose-glactose malabosorption) 등의 유전적인 문제가 있는 환자에게는 투여하면 안된다.]]>

</PARAGRAPH>
<PARAGRAPH tagName="p" textIndent="" marginLeft="">
<![CDATA[15) 75세 이상의 고령자]]>

</PARAGRAPH>
</ARTICLE>
 */