package ru.user.todolist.repository.impl

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import org.jetbrains.anko.db.*
import ru.user.todolist.dao.DBHelper
import ru.user.todolist.dao.DBHelper.Companion.items
import ru.user.todolist.dao.DBHelper.Companion.text
import ru.user.todolist.repository.ItemRepository
import ru.user.todolist.domain.Item
import ru.user.todolist.exception.EntryExistsException
import ru.user.todolist.exception.EntryNotFoundException
import ru.user.todolist.util.database
import ru.user.todolist.dao.DBHelper.Companion.lastModifyDate
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.time.Instant
import java.util.*

class ItemRepositoryImpl(private val db: DBHelper) :
    ItemRepository {

    override fun getAll(limit: Int): List<Item> =
        try {
            db.use {
                select(items)
                    .orderBy(lastModifyDate, direction = SqlOrderDirection.DESC)
                    .limit(limit)
                    .parseList(parser)
            }
        } catch (e: Exception) {
            Log.e("error", e.message)
            throw e
        }

    override fun getById(id: Int): Item =
        try {
            db.use {
                select(items)
                    .whereArgs("id = {id}", "id" to id)
                    .parseSingle(parser)
            }
        } catch (e: SQLiteException) {
            Log.e("error", e.message)
            throw e
        }


    override fun insert(item: Item) {
        try {
            val inserted = db.use {
                val values = ContentValues()
                values.put(text, item.text)
                values.put(lastModifyDate, item.lastModifyDate.toInstant().toEpochMilli())
                insert(items, null, values)
            }
            if (inserted <= 0) throw SQLiteException("Cannot insert  " + item)
        } catch (e: SQLiteException) {
            Log.e("error", e.message)
            throw EntryExistsException(
                "Item {$item} already exists",
                e
            )
        }
    }

    override fun update(item: Item) {
        try {
            if (item.id == null) throw IllegalArgumentException("No id specified in item $item")
            val byId = getById(item.id!!)
            if (byId == null) throw EntryNotFoundException(
                "No item with id ${item.id}"
            )
            val updated = db.use {
                update(
                    items,
                    text to item.text,
                    lastModifyDate to Instant.now().toEpochMilli()
                ).whereArgs("id = {id}", "id" to item.id!!).exec()
            }
            if (updated == -1) throw SQLiteException("Cannot update " + item)
        } catch (e: SQLiteException) {
            Log.e("error", e.message)
            throw EntryExistsException(
                "Item {$item} already exists",
                e
            )
        }
    }

    override fun remove(id: Int) {
        val byId = getById(id)
        if (byId == null) throw EntryNotFoundException("Item with id $id does not exist")
        try {
            db.use {
                delete(
                    items,
                    "id = {id}", "id" to id
                )
            }
        } catch (e: SQLiteException) {
            Log.e("error", "Cannot delete item with id $id", e)
            throw e
        }
    }

    private val parser = ItemRowParser()

    companion object {
        private var instance: ItemRepository? = null

        @Synchronized
        fun getInstance(ctx: Context): ItemRepository {
            if (instance == null) {
                instance =
                    ItemRepositoryImpl(ctx.database)
            }
            return instance!!
        }
    }


    class ItemRowParser : RowParser<Item> {

        override fun parseRow(columns: Array<Any?>): Item {
            val id = columns[0]
            val name = columns[1]
            val lastModifyDate = columns[2]
            return Item(
                (id as Long).toInt(),
                name as String,
                Date.from(Instant.ofEpochMilli(lastModifyDate as Long))
            )
        }
    }
}


