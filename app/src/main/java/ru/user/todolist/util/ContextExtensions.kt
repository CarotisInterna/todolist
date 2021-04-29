package ru.user.todolist.util

import android.content.Context
import ru.user.todolist.dao.DBHelper
import ru.user.todolist.repository.ItemRepository
import ru.user.todolist.repository.impl.ItemRepositoryImpl
import ru.user.todolist.service.impl.ItemServiceImpl

internal val Context.database: DBHelper
    get() = DBHelper.getInstance(this)

internal val Context.repository: ItemRepository
    get() = ItemRepositoryImpl.getInstance(this)

internal val Context.itemServiceImpl: ItemServiceImpl
    get() = ItemServiceImpl.getInstance(this)

