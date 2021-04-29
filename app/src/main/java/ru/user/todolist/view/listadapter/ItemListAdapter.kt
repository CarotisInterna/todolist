package ru.user.todolist.view.listadapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.*
import ru.user.todolist.R
import ru.user.todolist.domain.Item
import ru.user.todolist.util.ContextHolder
import ru.user.todolist.util.itemServiceImpl
import ru.user.todolist.view.Action
import ru.user.todolist.view.EditActivityInflater

class ItemListAdapter(context: Context, private var objects: ArrayList<Item>) :
    AbstractListAdapter<Item>(context, R.layout.item_list_element, objects) {

    override fun createViewFromResource(v: View, position: Int): View {
        val nameText: TextView = v.findViewById<TextView>(R.id.item_text)
        val editButton: ImageButton = v.findViewById<ImageButton>(R.id.edit_button)
        val deleteButton: ImageButton = v.findViewById<ImageButton>(R.id.delete_button)

        val item = getItem(position)!!

        nameText.text = item.text

        editButton.setOnClickListener {
            EditActivityInflater.inflate(
                context,
                item,
                Action.UPDATE
            )
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            builder.setMessage(R.string.delete_confirmation)
            builder.setNegativeButton(
                R.string.no,
                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })

            builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, _ ->
                try {
                    context.itemServiceImpl.remove(item.id!!)
                    this.remove(getItem(position))
                    ContextHolder.getInstance().updateLv()
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            })
            builder.create().show()

        }

        return v
    }
}