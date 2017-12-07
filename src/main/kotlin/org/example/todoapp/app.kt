package org.example.todoapp

import org.webscene.client.dom.DomEditType
import org.webscene.client.dom.DomEditor
import org.webscene.client.html.HtmlSection
import org.webscene.client.html.HtmlCreator as html

fun main(args: Array<String>) {
    val footer = createPageFooter()
    val section = createParentSection()

    DomEditor.editSection(HtmlSection.BODY)(footer.toDomElement(), DomEditType.PREPEND)
    DomEditor.editSection(HtmlSection.BODY)(section.toDomElement(), DomEditType.PREPEND)
}
