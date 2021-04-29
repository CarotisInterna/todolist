package ru.user.todolist.repository

import ru.user.todolist.domain.Item

interface ItemRepository {
    fun getAll(limit: Int): List<Item>
    fun insert(item: Item)
    fun update(item: Item)
    fun remove(id: Int)
    fun getById(id: Int): Item
}