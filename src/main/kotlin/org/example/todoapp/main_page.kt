package org.example.todoapp

import org.webscene.client.html.InputType
import org.webscene.client.html.HtmlCreator as html

// Provides the view for the main page.
// Author - Nick Apperley

private val sectionFooterData = arrayOf("#/" to "All", "#/active" to "Active", "#/completed" to "Completed")

internal fun createPageFooter() = html.parentElement("footer") {
    classes += "info"
    parentHtmlElement("p") {
        parentHtmlElement("span") { +"Source on " }
        parentHtmlElement("a") {
            attributes["href"] = "https://github.com/webscene/todo-app-sample"
            +"Github"
        }
    }
}

private fun createSectionFooter() = html.parentElement("footer") {
    parentHtmlElement("ul") {
        classes += "filters"
        sectionFooterData.forEachIndexed { pos, d ->
            parentHtmlElement("li") {
                parentHtmlElement("a") {
                    if (pos == 0) classes += "selected"
                    attributes["href"] = d.first
                    +d.second
                }
            }
        }
    }
}

private fun createSectionHeader() = html.parentElement("header") {
    classes += "header"
    parentHtmlElement("h1") { +"Todos" }
    children += html.input(InputType.TEXT, autoFocus = true) {
        id = "new-todo"
        classes += "new-todo"
        attributes["placeholder"] = "What needs to be done?"
    }
}

private fun createChildSection() = html.parentElement("section") {
    classes += "main"
    children += html.input(InputType.CHECKBOX) {
        id = "toggle-all"
        classes += "toggle-all"
    }
    parentHtmlElement("label") {
        attributes["for"] = "toggle-all"
        +"Mark all as complete"
    }
    parentHtmlElement("ul") {
        id = "todo-list"
        classes += "todo-list"
    }
}

internal fun createParentSection() = html.parentElement("section") {
    classes += "todoapp"
    children += createSectionHeader()
    children += createChildSection()
    children += createSectionFooter()
}