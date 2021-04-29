package ru.user.todolist.repository

import ru.user.todolist.domain.Item
import ru.user.todolist.exception.EntryExistsException
import ru.user.todolist.exception.EntryNotFoundException
import java.util.concurrent.atomic.AtomicInteger

class MockItemRepository : ItemRepository {

    private val items = HashMap<Int, Item>()

    val counter = AtomicInteger(1)

    override fun getAll(limit: Int): List<Item> {
        return ArrayList<Item>(items.values).subList(0, limit)
    }

    override fun insert(item: Item) {
        if (item.id != null && items[item.id!!] != null) throw EntryExistsException(
            "Item {$item} already exists"
        )
        if (item.id == null) item.id = counter.incrementAndGet()
        items[item.id!!] = item
    }

    override fun update(item: Item) {
        if (item.id == null) throw IllegalArgumentException("No id specified in item $item")
        items[item.id!!] ?: throw EntryNotFoundException("No item with id ${item.id}")
        items[item.id!!] = item
    }

    override fun remove(id: Int) {
        val found = items[id] ?: throw EntryNotFoundException(
            "No item with id $id"
        )
        items.remove(id)
    }

    override fun getById(id: Int): Item {
        return if (items.containsKey(id)) items[id]!! else throw EntryNotFoundException("No item with id $id")
    }

}