package ru.user.todolist.service.impl

import android.content.Context
import ru.user.todolist.repository.ItemRepository
import ru.user.todolist.domain.Item
import ru.user.todolist.exception.TextValidationException
import ru.user.todolist.service.ItemService
import ru.user.todolist.util.repository

class ItemServiceImpl(private val repository: ItemRepository) :
    ItemService {

    override fun getAll(): List<Item> {
        return repository.getAll(11)
    }

    override fun insert(item: Item) {
        validate(item)
        repository.insert(item)
    }

    override fun update(item: Item) {
        validate(item)
        repository.update(item)
    }

    private fun validate(item: Item) {
        if (item.text.isBlank()) throw TextValidationException(
            "Name cannot be empty"
        )
        if (item.text.length > 100) throw TextValidationException(
            "Name cannot be longer than 100 symbols"
        )
    }

    override fun remove(id: Int) {
        repository.remove(id)
    }

    companion object {
        private var instance: ItemServiceImpl? = null

        @Synchronized
        fun getInstance(ctx: Context): ItemServiceImpl {
            if (instance == null) {
                instance =
                    ItemServiceImpl(ctx.repository)
            }
            return instance!!
        }
    }
}



