package ru.user.todolist.util

import android.annotation.SuppressLint
import android.widget.ListView
import ru.user.todolist.view.listadapter.ItemListAdapter

class ContextHolder {
    lateinit var itemsLv: ListView
    lateinit var itemsLA: ItemListAdapter

    fun updateLv() {
        itemsLv.adapter = itemsLA
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ContextHolder? = null

        fun getInstance(): ContextHolder {
            if (instance == null) {
                instance =
                    ContextHolder()
            }
            return instance as ContextHolder
        }
    }

}