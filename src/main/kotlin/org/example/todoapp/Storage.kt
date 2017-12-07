package org.example.todoapp

/**
 * Storage class for the app. Based on kotlin_todo's [Storage.kt file](https://github.com/programiz/kotlin-todo/blob/master/src/Storage.kt)
 * @author Nick Apperley
 */
class Storage(val dbName: String) {
    var todos = mutableListOf<TodoListItem>()

    /**
     * Finds the items matching the given condition.
     */
    fun find(predicate: (TodoListItem) -> Boolean): List<TodoListItem> {
        return todos.filter { predicate(it) }
    }

    /**
     * Saves the item to the list.
     */
    fun save(item: TodoListItem) {
        // id present
        val todo = todos.find { it.id == item.id }

        if (todo != null) {
            todo.title = item.title
            todo.completed = item.completed
        } else {
            todos.add(item)
        }
    }

    /**
     * Removes the item from the list.
     */
    fun remove(id: String) {
        // id present
        val todo = todos.find { it.id == id }

        if (todo != null) {
            todos.remove(todo)
        }
    }
}