package org.example.todoapp

/**
 * Model for a TodoListItem. Based on kotlin_todo's [TodoListItem.kt file](https://github.com/programiz/kotlin-todo/blob/master/src/TodoListItem.kt)
 * @author Nick Apperley
 */
data class TodoListItem(val id: String, var title: String, var completed: Boolean)