package ru.user.todolist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import ru.user.todolist.domain.Item

object EditActivityInflater {

    fun inflate(
        context: Context,
        item: Item? = null,
        action: Action
    ) {
        val intent = Intent(context, EditActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("item", item)
        bundle.putSerializable("action", action)
        intent.putExtra("bundle", bundle)
        startActivity(context, intent, null)
    }

}