package ru.user.todolist.service

import ru.user.todolist.domain.Item

interface ItemService {

    fun getAll(): List<Item>
    fun insert(item: Item)
    fun update(item: Item)
    fun remove(id: Int)
}