package org.example.todoapp

import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

/**
 * App class for TodoList. This class creates and hooks the functionalities for a_todo. Based on kotlin_todo's
 * [App.kt file](https://github.com/programiz/kotlin-todo/blob/master/src/App.kt).
 * @author Nick Apperley
 */
internal class App(dbName: String) {
    private val controller = MainPageController()
    private val storage = Storage(dbName)

    init {
        val todoList = document.querySelector(".todo-list") as HTMLElement

        setupNewTodoEvents()
        setupDestroyButtonEvents(todoList)
        setupToggleButtonEvents(todoList)

        // Refresh items on hash change.
        window.addEventListener("hashchange", { refreshItems() })
        // Load filters on first load.
        refreshItems()
    }

    private fun setupToggleButtonEvents(todoList: HTMLElement) {
        delegateEvent(todoList, ".toggle", "click", { element: HTMLElement, _: Event ->
            element as HTMLInputElement

            val item = element.parentNode as HTMLElement
            val id = item.dataset["id"]
            toggleTodo(id!!, element.checked)
        })
    }

    private fun setupDestroyButtonEvents(todoList: HTMLElement) {
        delegateEvent(todoList, ".destroy", "click", { element: HTMLElement, _: Event ->
            val item = element.parentNode as HTMLElement
            removeTodo(item.dataset["id"]!!)
        })
    }

    private fun setupNewTodoEvents() {
        // Hooks for the HTMLElements.
        // Add NewTodo button.
        val newTodo = document.getElementById("new-todo") as HTMLInputElement
        val enterKey = 13

        on(target = newTodo, type = "keyup") {
            it.preventDefault()

            it as KeyboardEvent
            if (it.keyCode == enterKey) {
                val item = it.target as HTMLInputElement

                if (item.value != "") addTodo(item.value)
            }
        }
    }

    /**
     * Creates an event listener for the given target element.
     */
    private fun on(target: Element, type: String, useCapture: Boolean = false, callback: (Event) -> Unit) {
        target.addEventListener(type = type, callback = callback, options = useCapture)
    }

    /**
     * Creates a delegateEvent event handler. Connects the event handler when the given selector element is present.
     */
    private fun delegateEvent(
        target: HTMLElement,
        selector: String,
        type: String,
        handler: (HTMLElement, Event) -> Unit
    ) {
        val dispatchEvent = { event: Event ->
            val targetElement = event.target as HTMLElement
            val potentialElements = document.querySelectorAll(selector)
            val hasMatch = potentialElements.asList().any { it == targetElement }

            if (hasMatch) handler(targetElement, event)
        }
        val useCapture = type == "blur" || type == "focus"

        on(target = target, type = type, callback = dispatchEvent, useCapture = useCapture)
    }

    /**
     * Refreshes the items list.
     */
    private fun refreshItems() {
        changeFilter(window.location.hash)
    }

    /**
     * Sets the filter to the current hash location and recreates the items list.
     */
    private fun changeFilter(location: String) {
        val route = location.substringAfter('/').capitalize()

        val items = when (route) {
            "Active" -> storage.find { !it.completed }
            "Completed" -> storage.find { it.completed }
            else -> storage.find { true }
        }

        controller.setFilter(route.toLowerCase())
        controller.displayItems(items)
    }

    /**
     * Adds a TodoList item.
     */
    private fun addTodo(title: String) {
        val todo = TodoListItem(
            id = Date().getTime().toString(),
            title = title,
            completed = false
        )

        storage.save(todo)

        // Update UI.
        controller.clearNewTodoInput()
        refreshItems()
    }

    /**
     * Removes a TodoItem.
     */
    private fun removeTodo(id: String) {
        storage.remove(id)

        controller.removeItem(id)
        refreshItems()
    }

    /**
     * Toggles a TodoItem
     * Completed or Uncompleted.
     */
    private fun toggleTodo(id: String, completed: Boolean) {

        val items = storage.find { it.id == id }

        if (items.isNotEmpty()) {
            val item = items[0]

            item.completed = completed
            storage.save(item)
        }

        controller.toggleItem(id, completed)
        refreshItems()
    }
}
