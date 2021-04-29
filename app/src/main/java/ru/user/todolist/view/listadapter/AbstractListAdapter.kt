package ru.user.todolist.view.listadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

abstract class AbstractListAdapter<T>(
    context: Context,
    private val resource: Int,
    objects: ArrayList<T>
) :
    ArrayAdapter<T>(context, resource, objects) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView != null) {
            convertView
        } else {
            val v =
                LayoutInflater
                    .from(context)
                    .inflate(resource, parent, false)
            createViewFromResource(v, position)
        }
    }


    abstract fun createViewFromResource(v: View, position: Int): View
}