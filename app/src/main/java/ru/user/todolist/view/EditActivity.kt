package ru.user.todolist.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import ru.user.todolist.R
import ru.user.todolist.domain.Item
import ru.user.todolist.util.itemServiceImpl

class EditActivity : AppCompatActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val bundleExtra = intent.getBundleExtra("bundle")
        val action: Action = bundleExtra.get("action") as Action
        val oldItem = bundleExtra.get("item") as Item?
        oldText = oldItem?.text ?: ""
        editedItem = oldItem?.copy()
        bindItem(action)
        findViewById<Button>(R.id.cancel_button).setOnClickListener {
            finish()
        }
    }

    private fun bindItem(action: Action) {
        val nameText = findViewById<EditText>(R.id.item_text)
        val button = findViewById<Button>(R.id.save_button)

        if (editedItem == null) {
            editedItem =
                Item(text = "")
            button.isEnabled = false
        }

        nameText.text.insert(0, editedItem!!.text)

        nameText.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button.isEnabled = !s.isNullOrBlank()
                Log.d("EditActivity", "nameText, update")
                editedItem!!.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })


        button.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.save_progress_bar)
            progressBar.visibility = View.VISIBLE

            button.isEnabled = false

            Handler().postDelayed({
                try {
                    if (oldText == editedItem?.text) {
                        Toast.makeText(applicationContext, R.string.no_changes, Toast.LENGTH_SHORT)
                            .show()
                        progressBar.visibility = View.GONE
                        return@postDelayed
                    }
                    when (action) {
                        Action.INSERT -> {
                            itemServiceImpl.insert(editedItem!!)
                            finish()
                        }
                        Action.UPDATE -> {
                            itemServiceImpl.update(editedItem!!)
                            finish()
                        }
                    }
                    progressBar.visibility = View.GONE

                    editedItem = null
                    oldText = ""
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, R.string.cannot_save, Toast.LENGTH_LONG)
                        .show()
                    progressBar.visibility = View.GONE
                }
                button.isEnabled = true
            }, 300)
        }
    }


    companion object {
        private var oldText: String = ""
        private var editedItem: Item? = null
    }
}
