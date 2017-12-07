package org.example.todoapp

import org.webscene.client.dom.DomEditType
import org.webscene.client.dom.DomEditor
import org.webscene.client.html.HtmlSection
import org.webscene.client.html.HtmlCreator as html

fun main(args: Array<String>) {
    val title = html.element("h1") { +"DOM Test" }

    DomEditor.editSection(HtmlSection.BODY)(title.toDomElement(), DomEditType.PREPEND)
}
