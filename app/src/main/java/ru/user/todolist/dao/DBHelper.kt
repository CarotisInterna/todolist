package ru.user.todolist.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {
    companion object {
        const val items = "Item"
        const val id = "id"
        const val text = "text"
        const val lastModifyDate = "lastModifyDate"

        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelper {
            if (instance == null) {
                instance =
                    DBHelper(ctx)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            items, true,
            id to INTEGER + PRIMARY_KEY,
            text to TEXT + UNIQUE,
            lastModifyDate to INTEGER,
        )
        db.insert(
            items,
            text to "Lorem ipsum dolor",
            lastModifyDate to System.currentTimeMillis()
        )
        db.insert(
            items,
            text to "Dolor ipsum lorem",
            lastModifyDate to System.currentTimeMillis()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteDatabase(db)
    }

    private fun deleteDatabase(db: SQLiteDatabase) {
        db.dropTable(items, true)
    }

}

