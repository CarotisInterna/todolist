package ru.user.todolist.view

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import ru.user.todolist.R
import ru.user.todolist.domain.Item
import ru.user.todolist.util.ContextHolder
import ru.user.todolist.util.itemServiceImpl
import ru.user.todolist.view.listadapter.ItemListAdapter


class MainActivity : AppCompatActivity() {

    lateinit var lv: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lv = findViewById<ListView>(R.id.editable_list)
        val progressBar = findViewById<ProgressBar>(R.id.load_progress_bar)
        progressBar.visibility = View.VISIBLE
        Handler().postDelayed({
            initItemListAdapter(lv)
            progressBar.visibility = View.GONE
        }, 300)
    }

    private fun initItemListAdapter(lv: ListView) {
        val objects = itemServiceImpl.getAll() as ArrayList<Item>
        val itemListAdapter =
            ItemListAdapter(this, objects)
        lv.adapter = itemListAdapter
        ContextHolder.getInstance().itemsLv = lv
        ContextHolder.getInstance().itemsLA = itemListAdapter
        lv.emptyView = findViewById(R.id.empty_list)
    }

    override fun onStart() {
        super.onStart()
        initItemListAdapter(lv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_button, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.add -> {
            EditActivityInflater.inflate(
                this,
                action = Action.INSERT,
            )
            lv.invalidate()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}


