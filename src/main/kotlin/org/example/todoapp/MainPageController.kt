package org.example.todoapp

import org.w3c.dom.*
import org.webscene.client.dom.DomEditType
import org.webscene.client.dom.DomEditor
import org.webscene.client.dom.DomQuery
import org.webscene.client.html.HtmlSection
import org.webscene.client.html.InputType
import org.webscene.client.html.HtmlCreator as html
import kotlin.browser.document

/**
 * Controller for the main page. Based on kotlin_todo's [View.kt file](https://github.com/programiz/kotlin-todo/blob/master/src/View.kt).
 * @author Nick Apperley
 */
internal class MainPageController {
    private val newTodo: HTMLInputElement
    private val todoList: HTMLUListElement

    init {
        val footer = createPageFooter()
        val section = createParentSection()

        DomEditor.editSection(HtmlSection.BODY)(footer.toDomElement(), DomEditType.PREPEND)
        DomEditor.editSection(HtmlSection.BODY)(section.toDomElement(), DomEditType.PREPEND)
        newTodo = DomQuery.elementById("new-todo") as HTMLInputElement
        todoList = DomQuery.elementById("todo-list") as HTMLUListElement
    }

    /**
     * Display given Todo_items in the UI.
     */
    fun displayItems(items: List<TodoListItem>) {
        todoList.innerHTML = ""
        items.forEach { addItem(it) }
    }

    /**
     * Sets the correct filter classes.
     */
    fun setFilter(route: String) {
        val selectedFilter = document.querySelector(".filters .selected") as HTMLElement
        selectedFilter.className = ""

        val currentPage = document.querySelector(".filters [href=\"#/$route\"]") as HTMLElement
        currentPage.className = "selected"
    }

    /**
     * Clears new Todo_input.
     */
    fun clearNewTodoInput() {
        newTodo.value = ""
    }

    /**
     * Adds item to the UI.
     */
    fun addItem(item: TodoListItem) {
        // Create List item.
        val todoElement = html.parentElement("li") {
            attributes["data-id"] = item.id
            if (item.completed) classes += "completed"
            children += html.input(InputType.CHECKBOX) {
                classes += "toggle"
                if (item.completed) attributes["checked"] = "true"
            }
            parentHtmlElement("label") { +item.title }
            children += html.input(InputType.BUTTON) { classes += "destroy" }
        }
        todoList.append(todoElement.toDomElement())
    }

    /**
     * Removes item from the UI.
     */
    fun removeItem(id: String) {
        val todoElement = document.querySelector("[data-id=\"$id\"]") as HTMLLIElement

        todoList.removeChild(todoElement)
    }

    /**
     * Toggle item's completed status in the UI.
     */
    fun toggleItem(id: String, completed: Boolean) {
        val todoElement = document.querySelector("[data-id=\"$id\"]") as HTMLLIElement

        todoElement.className = if (completed) "completed" else ""
    }
}